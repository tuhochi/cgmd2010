package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import static at.ac.tuwien.cg.cgmd.bifth2010.level83.Constants.*;


public class MyHexagonGrid implements Drawable,Animatable{

	private int width, height;
	
	private int textureHandle;
	private int textureHandle_blue;
	private int textureHandle_r;
	private int textureHandle_dollar;
	
	private int mapWidth, mapHeight;
	private byte[][] map;
	private float scroll=0;
	private MySprite character;
	private float delta=0;
	private int xMin=0;
	private int characterLevel=3;
	private int direction;
	private char characterMode;
	private int next=1;
	private float animLevel;
	private int delay=0;
	private float sign = 1;
	private boolean stop = true;
	private Random random = new Random();
	private float jumpSpeed=0;
	private float jumpTimeScale=1;
	private float jumpGrav=12f;
	private float jumpTimeDelta=0;
	private int jumpWidth=1;
	
	private ArrayList choices = new ArrayList(12);
	
	
	/**
	 * This function loads a bitmap containing map data.
	 * @param file - filename storing map data
	 * @param context - context for accessing resources
	 */
	private void readFile(int id, Context context){
		
		 //Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open(file));
        InputStream is = context.getResources().openRawResource(id);
        
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
            	 Log.e("HexagonGrid", "Unable to load "+id);
                 Log.e("Exception",e.getStackTrace().toString());
                // System.exit(-1);
            }
        }
        
        mapWidth = bitmap.getWidth();
        mapHeight = bitmap.getHeight();
        
        map = new byte[mapWidth][mapHeight];
        
        for(int x=0; x< mapWidth; x++)
        	for(int y=0; y < mapHeight; y++)
        		map[x][y] = (byte)bitmap.getPixel(x, mapHeight-1-y);
        
        Log.d("HexagonGrid", "Loaded "+id);
        bitmap.recycle();
	}
	
	/**
	 * Constructor for the HexagonGrid class. Sets up the hexagonal grid to display the level. 
	 * @param viewportWidth
	 * @param viewportHeight
	 * @param texture - bitmap containing the texture for the hexagonal fields
	 * @param gl - opengl 
	 * @param context - context for accessing resources
	 * @param map - bitmap file containing the map data
	 */
	public MyHexagonGrid(int resourceId,GL10 gl,Context context, int mapResource) {
		//Load texture for hexagonal elements
		textureHandle = MyTextureManager.singleton.addTexturefromResources(resourceId, gl);
		textureHandle_blue = MyTextureManager.singleton.addTexturefromResources(TEXTURE_HEXAGON_B, gl);
		textureHandle_r = MyTextureManager.singleton.addTexturefromResources(TEXTURE_HEXAGON_R, gl);
		textureHandle_dollar = MyTextureManager.singleton.addTexturefromResources(TEXTURE_DOLLAR, gl);
		
		//Read map file
		readFile(mapResource, context);
		
		//Generic field of view
		width = 5;
		height = mapHeight;
		
		Log.d("HexagonGrid","Size: "+width+"x"+height);
	}
	
	/**
	 * Calculates the field of view depending on the viewport. 
	 * @param viewportWidth
	 */
	public void setWidth(float viewportWidth){
		//Calculate grid size
		width = (int)Math.ceil(viewportWidth / (3f*R_HEX/2f))+2;
		
		//DEBUG: Resets the Level
		scroll =0;
		characterLevel = 3;
		
		character.x = CHARACTER_POSITION*3f*R_HEX/2f-7.5f;
		
		int deltaH = ((xMin+CHARACTER_POSITION)%2 == 0)?2:1;
		character.y = RI_HEX*deltaH+characterLevel*2f*RI_HEX;
		
		//Start animation
		stop = false;
	}
	@Override
	public void Dispose(GL10 gl) {
		// TODO Auto-generated method stub

	}

	/**
	 * Draws the grid and the enabled items.
	 */
	@Override
	public void Draw(GL10 gl) {	
		
		//Bind texture for hexagonal elements
		MyTextureManager.singleton.textures[textureHandle].Bind(gl);
		
		//Log.d("HexagonGrid","Draw "+scroll);
		//Render grid
		for(int x=0;x<width;x++){

			for(int y=0;y<height;y++){
				if(map[x+xMin][y] == GRID_CUSTOM_WALL)
					MyTextureManager.singleton.textures[textureHandle_blue].Bind(gl);
				else if(map[x+xMin][y] == GRID_DOLLAR)
					MyTextureManager.singleton.textures[textureHandle_dollar].Bind(gl);
				else if(x == CHARACTER_POSITION+3)
					MyTextureManager.singleton.textures[textureHandle_r].Bind(gl);
				
				
				if(((map[x+xMin][y] & GRID_NULL) == 0) || (map[x+xMin][y] == GRID_DOLLAR))
					((GL11Ext) gl).glDrawTexfOES(3f*R_HEX/2*(x-delta)-0.5f*R_HEX,
													y*2*RI_HEX-DIF_HEX-(RI_HEX*((x+xMin)%2)),
																0,GRID_ELEMENT_WIDTH, GRID_ELEMENT_HEIGHT);
				
				if((map[x+xMin][y] == GRID_CUSTOM_WALL) || (x == CHARACTER_POSITION+3) || (map[x+xMin][y] == GRID_DOLLAR))
					MyTextureManager.singleton.textures[textureHandle].Bind(gl);
		
			}	
		}
	}

	/**
	 * Sets the character for animation.
	 * @param character
	 */
	public void setCharacterControl(MySprite character){
		this.character = character;
	}
	@Override
	public void Init(GL10 gl) {
		// TODO Aut o-generated method stub

	}
	
	/**
	 * Calculates a list of accessible waypoints 
	 */
	public void calculateChoices(){
	
		int x = xMin+CHARACTER_POSITION;
		int y = characterLevel;
		
		choices.clear();
		
		MyWaypoint current;
		
		for(int i=0; i<MyWaypoint.waypoints.length; i++){	//Schleife über Sprung Höhe
			
			current = MyWaypoint.waypoints[i][0];
			
			if(current.y+y >= mapHeight)	//Höhe außerhalb der Map
				break;
			
			if( (map[current.x+x][current.y+y] & GRID_NULL) == 0 )	//Element blockiert
				break;
				
			for(int j=0; j<MyWaypoint.waypoints[i].length; j++){	//Schleife über Waypoints
				current = MyWaypoint.waypoints[i][j];
				
				if( (current.x+x < mapWidth)  )		//Außerhalb von Map ? unwahrscheinlich
					if( (map[current.x+x][current.y+y] & GRID_NULL) == 0 ){		//Element nicht frei
						
						if((current.endpoint) && ((map[current.x+x][current.y+y+1] & GRID_NULL) != 0))	//Wenn endpoint dann hinzu
							choices.add(current);
						
						break;
					}
			}
		}
		
	}
	
	
	/**
	 * Animates the Grid, the character and the items. Eg. everything.
	 */
	@Override
	public void animate(float deltaTime) {
		//
		if(stop)
			return;
		
		//Calculated new scroll value
		scroll += deltaTime*jumpTimeScale*SPEED;
		
		//Log.d("HexagonGrid", "Animate "+scroll);
		//Calculate animation displacement
		delta = scroll-(float)Math.floor(scroll);
		int newxMin = (int)Math.floor(scroll);
		
		//Detects Grid boarders
		if(newxMin != xMin)
			next++;
		
		xMin = newxMin;
		
		//Reset level to start
		if(scroll+width >= mapWidth-1)
			scroll = 0;
		
		int deltaH = ((xMin+CHARACTER_POSITION)%2 == 0)?2:1;
	
		character.x = CHARACTER_POSITION*3f*R_HEX/2f-7.5f;
		
			//next field
		if(next > delay){
				
			jumpTimeScale = 1f;
			next = 0;
			delay = 0;
			
			//Up, down walk
			//characterLevel += direction;
				
			animLevel = RI_HEX*deltaH+characterLevel*2f*RI_HEX;
			sign = ((xMin+CHARACTER_POSITION)%2 == 0)?-1:1;
			
			
			
			/// NEW CHOICES
			
			calculateChoices();
			for(int i=0; i<choices.size(); i++)
				Log.d("Choices",((MyWaypoint)choices.get(i)).toString());
			
			///
			
			MyWaypoint wp = new MyWaypoint(1, 0, true);
			
			if(choices.size() == 0)
				Log.d("HexagonGrid","UPS... no CHOICE found... check level");
			else
				wp = (MyWaypoint)choices.get(random.nextInt(choices.size()));
			
			
			//new characterMode
			if((wp.x == 1) && (wp.y == 0))
				characterMode = 'W';
			else{
				characterMode = 'J';
				delay = wp.x-1;
				characterLevel += wp.y;
				
				float up = (wp.y < 0)?-1f:1f;
				
				if(wp.x%2 == 0)
					jump(wp.y*2f,1f,wp.x);
				else
					jump(wp.y*2f+sign, 1f, wp.x);
				
			}
			
			Log.d("Character","Choice="+characterMode);
		}
		float a,v,y,t;
		
		//Richtung bestimmen
		
		
		switch(characterMode){
		
		case 'W':
			direction = 0;
			if( delta >= (2f/3f)){
				
				
//				//Down
//				if(characterLevel+1 < height)
//					if((map[xMin+CHARACTER_POSITION+1][characterLevel+1] == GRID_WALL)
//							&& (sign == -1)){
//						sign *= -1;		//Up
//						direction = 1;
//					}
//					
//				//Up
//				if(characterLevel-1 >= 0)
//					if((map[xMin+CHARACTER_POSITION+1][characterLevel-1] == GRID_WALL)&&
//							(map[xMin+CHARACTER_POSITION+1][characterLevel]!= GRID_WALL)
//							&& (sign == 1)){
//						sign *= -1;	//down
//						direction = -1;
//					}
				
				character.y = sign*(delta-2f/3f)*3*RI_HEX+RI_HEX*deltaH+characterLevel*2*RI_HEX;
			}
			else{
				character.y = RI_HEX*deltaH+characterLevel*2f*RI_HEX;
			}
			break;
			
		case 'U':		//Up
			if(sign == -1){	//Down
				v = 355f/21f;
				a = -250f/21f;	
				jump(5,6,1);
			}else{			//Up
				v = 18.875f;	
				a = -11.875f;
				jump(7,8,1);
			}
			
			//y = (v*delta+a*delta*delta)*RI_HEX;
			t = delta/jumpTimeScale;
			y = (jumpSpeed*t+jumpGrav*t*t)*RI_HEX;
			character.y = animLevel+y;					
			break;
			
		case '3':		//Jump 3 fields
			if(sign == -1){	//Down
				v = 127f/60f;
				a = -49f/60f;
				jump(-1,1f,3);
			}else{			//Up
				a = -5f/6f;
				v = 17f/6f;
				jump(1,2f,3);
			}
//			t = delta+next;
//			y = (v*t+a*t*t)*RI_HEX;
			t = (delta+next)/jumpTimeScale;
			y = (jumpSpeed*t+jumpGrav*t*t)*RI_HEX;
			character.y = animLevel+y;
			break;
			
		case '2':		//Jump 2 fields
			v = 2;
			a = -1;
			
			jump(0,1,2);
			
			t = (delta+next)/jumpTimeScale;
			y = (jumpSpeed*t+jumpGrav*t*t)*RI_HEX;
			character.y = animLevel+y;
			break;
			
		case 'D':
			if( delta >= 0.5){
				if(sign == -1){	//Down
					a = -55f;
					v = 27f/2f;
					jump(-7,0.5f,0.5f);
				}else{			//Up
					a = -125f/3f;
					v = 65f/6f;
					jump(-5,0.5f,0.5f);
				}
//				t=delta-0.5f;
//				y = (v*t+a*t*t)*RI_HEX;
				
				t = (delta-0.5f)/jumpTimeScale;
				y = (jumpSpeed*t+jumpGrav*t*t)*RI_HEX;
				character.y = animLevel+y;
			}
			break;
			
		case 'J':
			t = (delta+next)/jumpTimeScale;
			y = (jumpSpeed*t+jumpGrav*t*t)*RI_HEX;
			character.y = animLevel+y;
			break;
			
		default:
			Log.d("HexagonGrid","UPS... bad level design");
			break;
		}
	
	}
	
	public float jump(float h, float s, float width){
		
		float v,g,l=0,xEnd,x;
		
		s = (h>0)?s+h:s;
		 
		g = 12f;
		v = (float)Math.sqrt(s*4.0*g);	//s wegen scheitel
		xEnd = (1f/(2f*g))*(v+(float)Math.sqrt(v*v-4*g*h));
		
//		float yLast=0,temp = 0;		//Bogenlänge zeug, brauch ma nicht
		
//		for(int i=0;i<20;i++){
//			x = xEnd/20f * (i+1);
//			temp = (-g*x*x+v*x);
//			l += Math.abs(temp-yLast);
//			yLast = temp;
//		}
		
		this.jumpSpeed = v;
		this.jumpTimeScale = 1f/xEnd*width;
		this.jumpGrav = -g;
		
		return xEnd;
	}
	/**
	 * This function enables the item at a specified position on the grid.
	 * 
	 * @param x - x viewport coordinate
	 * @param y - y viewport coordinate
	 * @param item - item id as defined in ItemQueue
	 * @return True, if the item has been dropped on a valid position. False 
	 * otherwise.
	 */
	public boolean useItem(float x, float y, int item) {
		Log.d("HexagonGrid", x+":"+y + " - " + item);
		
		y = VIEWPORT_HEIGHT-y;
		//Approximation by using quads instead of hexagons
		int xGrid = (int)Math.floor( (x+(1.0/4.0+1.5*scroll)*R_HEX) / (3.0/2.0*R_HEX) );
		int yGrid = (int)Math.floor((y+RI_HEX*(xGrid%2)) / (2.0*RI_HEX) );
		
		if((xGrid<mapWidth) && (yGrid<mapHeight) && (yGrid != 0) && (xGrid > xMin+CHARACTER_POSITION+3)){
			if(map[xGrid][yGrid] == GRID_WALL)
				map[xGrid][yGrid] = GRID_NULL;
			else
				map[xGrid][yGrid] = GRID_CUSTOM_WALL;
				//map[xGrid][yGrid] = GRID_DOLLAR;
			
			return true;
		}
		
		Log.d("HexagonGrid","Item Pos=("+xGrid+","+yGrid+")");
		return false;
	}

}
