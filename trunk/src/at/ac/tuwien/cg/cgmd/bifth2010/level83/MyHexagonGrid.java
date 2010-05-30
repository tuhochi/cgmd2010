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
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import static at.ac.tuwien.cg.cgmd.bifth2010.level83.Constants.*;

/**
 * This class builds the level, moves the character, adds items to the grid and
 * animates items which have been added to the grid.
 * @author Manuel Keglevic, Thomas Schulz
 */
public class MyHexagonGrid extends Animatable implements Drawable{

	private int width, height;
	private int viewportWidth, viewportHeight;
	
	private int textureHandle;
	private int textureHandle_blue;
	private int textureHandle_r;
	private int textureHandle_dollar;
	private int textureHanlde_bw;
	private int textureHandle_deadLenny;
	private int textureHandle_background;
	/**
	 * Stores the dimmension of the laoded map.
	 */
	private int mapWidth, mapHeight;
	
	/**
	 * Stores the map.
	 */
	private byte[][] map;
	private float scroll=0;
	private MySprite character;
	private float delta=0;
	private int xMin=0;
	private int characterLevel=3;
	private float beamAnimationScroll;
	private int nextLvlAnimation =0;
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
	
	private ArrayList choices = new ArrayList(12);
	
	private ItemAnimated deadLenny;
	private ItemAnimated itemBomb;
	private AnimationManager animatedItemsManager;
	private ArrayList<ItemAnimated> currentItems;

	private Vibrator vib;
	
	private int cashCount = 0;
	private int deathCount = 0;
	
	/**
	 * This function loads a bitmap containing map data.
	 * 
	 * @param file		Filename of the map data.
	 * @param context	Context for accessing resources.
	 */
	private void readFile(int id, Context context) {
		
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
        		if((byte)bitmap.getPixel(x, mapHeight-1-y) == -1)
        			map[x][y] = GRID_NULL;
        		else
        			map[x][y] = GRID_WALL;
        
        Log.d("HexagonGrid", "Loaded "+id);
        bitmap.recycle();
	}
	
	/**
	 * Constructor for the HexagonGrid class. Sets up the hexagonal grid to 
	 * display the level.
	 * 
	 * @param viewportWidth		Width of the viewport.
	 * @param viewportHeight	Height of the viewport.
	 * @param texture			Bitmap containing the texture for the hexagonal
	 * 							fields
	 * @param gl 
	 * @param context			Context for accessing resources.
	 * @param map				Bitmap file containing the map data.
	 */
	public MyHexagonGrid(int resourceId, GL10 gl, Context context, int mapResource) {
		//Load texture for hexagonal elements
		textureHandle = MyTextureManager.singleton.addTextureFromResources(resourceId, gl);
		textureHandle_blue = MyTextureManager.singleton.addTextureFromResources(TEXTURE_HEXAGON_B, gl);
		textureHandle_r = MyTextureManager.singleton.addTextureFromResources(TEXTURE_HEXAGON_R, gl);
		textureHandle_dollar = MyTextureManager.singleton.addTextureFromResources(TEXTURE_DOLLAR, gl);
		textureHanlde_bw = MyTextureManager.singleton.addTextureFromResources(ITEM_WALL_BW, gl);
		textureHandle_background = MyTextureManager.singleton.addTextureFromResources(TEXTURE_BACKGROUND,gl);
		
		animatedItemsManager = new AnimationManager(10);
		
		itemBomb = new ItemAnimated(new int[]{ITEM_BOMB,ITEM_BOMB_BLINK},gl,GRID_ELEMENT_WIDTH, GRID_ELEMENT_HEIGHT,ItemQueue.BOMB);
		currentItems = new ArrayList<ItemAnimated>(10);
		
		textureHandle_deadLenny = MyTextureManager.singleton.addTextureFromResources(TEXTURE_LENNY_DEAD, gl);
		
		
		//Read map file
		readFile(mapResource, context);
		
		//Place Money
		placeDollar(15);	//normal 10
		
		//Generic field of view
		width = 5;
		height = mapHeight;
		
		Log.d("HexagonGrid","Size: "+width+"x"+height);
	}
	
	/**
	 * This function starts the animation to either resume or start the game. Not 
	 * all animation is stopped, only animation done by this class.
	 */
	public void resumeGame(){
		stop = false;
		SoundManager.singleton.playBackGround();
	}
	
	/**
	 * This function stops the animation to either pause or stop the game.Not 
	 * all animation is resumed, only animation done by this class.
	 */
	public void pauseGame(){
		stop = true;
	}
	/**
	 * Calculates the field of view depending on the viewport. 
	 * 
	 * @param viewportWidth	width of the viewport.
	 * @param viewportHeight height of the viewport
	 */
	public void setWidth(int viewportWidth, int viewportHeight) {
		
		this.viewportHeight = viewportHeight; 
		this.viewportWidth = viewportWidth;
		
		//Calculate grid size
		width = (int)Math.ceil((float)viewportWidth / (3f*R_HEX/2f))+2;
		
		//DEBUG: Resets the Level
		//scroll =0;
		//characterLevel = 3;
		
		character.x = CHARACTER_POSITION*3f*R_HEX/2f-7.5f;
		
		int deltaH = ((xMin+CHARACTER_POSITION)%2 == 0)?2:1;
		character.y = RI_HEX*deltaH+characterLevel*2f*RI_HEX;
		
		LevelActivity.deathsUpdateHandler.sendEmptyMessage(deathCount);
		LevelActivity.coinsUpdateHandler.sendEmptyMessage(cashCount);
		//Start animation
		
		deadLenny = new ItemAnimated(new int[]{textureHandle_deadLenny}, character.width, character.height, TEXTURE_LENNY_DEAD);
		
		itemBomb.width = GRID_ELEMENT_WIDTH;
		itemBomb.height = GRID_ELEMENT_HEIGHT;
		///DEBUG
		//xMin = mapWidth-2*width;
		//scroll = mapWidth-2*width;
	}
	
	@Override
	public void Dispose(GL10 gl) {
		// TODO Auto-generated method stub

	}

	/**
	 * Draws the grid and the enabled items. I.e. renders the level.
	 */
	@Override
	public void Draw(GL10 gl) {	
		//Draw background (static)
		MyTextureManager.singleton.textures[textureHandle_background].Bind(gl);
		((GL11Ext) gl).glDrawTexfOES(0f,0f,0f,viewportWidth,viewportHeight);
		
		//Bind texture for hexagonal elements
		MyTextureManager.singleton.textures[textureHandle].Bind(gl);
		
		
		//Render grid
		byte element = 0;
		boolean std = true;
		
		//Draw Grid
		for(int x=0;x<width;x++){

			for(int y=0;y<height;y++){
				element = map[x+xMin][y];
				
				if((xMin+x >= mapWidth-width-1+CHARACTER_POSITION) )
					MyTextureManager.singleton.textures[textureHanlde_bw].Bind(gl);
				
				switch(element){
				
				case GRID_CUSTOM_WALL:	
					
					MyTextureManager.singleton.textures[textureHandle_blue].Bind(gl);	
					std = false;
					break;
					
				case GRID_DOLLAR:
					MyTextureManager.singleton.textures[textureHandle_dollar].Bind(gl);
					std = false;
					break;
					
				default:
					if(x == CHARACTER_POSITION+3){
						MyTextureManager.singleton.textures[textureHandle_r].Bind(gl);
						std = false;
					}
//					if( x < CHARACTER_POSITION+3){
//						MyTextureManager.singleton.textures[textureHanlde_bw].Bind(gl);
//						std = false;
//					}
					break;
				}
				
				if(((map[x+xMin][y] & GRID_NULL) == 0) || (map[x+xMin][y] == GRID_DOLLAR))
					((GL11Ext) gl).glDrawTexfOES(3f*R_HEX/2*(x-delta)-0.5f*R_HEX,
													y*2*RI_HEX-DIF_HEX-(RI_HEX*((x+xMin)%2)),
																0,GRID_ELEMENT_WIDTH, GRID_ELEMENT_HEIGHT);
				
				if(!std)
					MyTextureManager.singleton.textures[textureHandle].Bind(gl);
		
			}	
		}
		
		//Draw animated Items
		for(int i=0; i< currentItems.size(); i++){
			ItemAnimated item = currentItems.get(i);
			int x = item.gridX-xMin;
			
			if( (x >= 0) && (x < width)){

				item.x = 3f*R_HEX/2*(x-delta)-0.5f*R_HEX;
				item.y = item.gridY*2*RI_HEX-DIF_HEX-(RI_HEX*((x+xMin)%2));
				//item.width = GRID_ELEMENT_WIDTH;
				//item.height = GRID_ELEMENT_HEIGHT;
				
				item.Draw(gl);
			}
		}
	}

	/**
	 * Sets the character for the animation.
	 * 
	 * @param character	The game character.
	 */
	public void setCharacterControl(MySprite character) {
		this.character = character;
	}
	
	@Override
	public void Init(GL10 gl) {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Calculates a list of accessible waypoints. This waypoints are used to 
	 * randomly choose the next path.
	 */
	public boolean calculateChoices() {
	
		int x = xMin+CHARACTER_POSITION;
		int y = characterLevel;
		
		choices.clear();
		
		MyWaypoint current;
		
		for(int i=0; i<MyWaypoint.waypoints.length; i++){	//Schleife Ÿber Sprung Hšhe
			
			current = MyWaypoint.waypoints[i][0];
			
			if(current.y+y >= mapHeight)	//Hšhe au§erhalb der Map
				break;
			
			if( (map[current.x+x][current.y+y] & GRID_NULL) == 0 )	//Element blockiert
				break;
				
			for(int j=0; j<MyWaypoint.waypoints[i].length; j++){	//Schleife Ÿber Waypoints
				current = MyWaypoint.waypoints[i][j];
				
				if( (current.x+x < mapWidth)  )		//Au§erhalb von Map ? unwahrscheinlich
					if( (map[current.x+x][current.y+y] & GRID_NULL) == 0 ){		//Element nicht frei
						
						if((current.endpoint) && ((map[current.x+x][current.y+y+1] & GRID_NULL) != 0))	//Wenn endpoint dann hinzu
							choices.add(current);
						
						break;
					}
			}
		}
		
		//sry, no choice must beam
		if(choices.size() == 0){
			for(int i=0; i<height; i++){
				if((map[x+2][i] & GRID_NULL) == 0 && (map[x+2][i+1] & GRID_NULL) != 0)
					choices.add(new MyWaypoint(x+2, i,true));
			}
			return false;
		}else
			return true;
		
	}
	
	
	/**
	 * Calculated the current progress in percent and updated 
	 * the value in the gui.
	 */
	public void UpdateProgress(){
		
		double maxSize = mapWidth - width -1 -1;
		
		int progress = (int)Math.round((double)xMin/maxSize *100.0);
		
		
		Log.d("Progress",""+progress+" | "+xMin+" | "+maxSize);
		LevelActivity.progressUpdateHandler.sendEmptyMessage(progress);
	}
	
	/**
	 * Checks if the character is in the sourounding of the detonated timer bomb.
	 * @param x - x position on the map
	 * @param y - y position on the map
	 * @return
	 */
	public boolean checkBombRadius(int x, int y){
		int s = (x%2 == 0)?1:-1;
		int[][] check = {{x,y},{x-1,y+s},{x-1,y},{x+1,y},{x+1,y+s}};
		
		boolean ok = false;
		for(int i=0; i<check.length; i++){
			if(check[i][0] == CHARACTER_POSITION+xMin && check[i][1] == characterLevel+1){
				ok = true;
				break;
			}
		}
		
		return ok;
	}
	
	/**
	 * Checks if the current position of the character contains dollar items.
	 */
	public void checkDollar(){
		
		if(map[CHARACTER_POSITION+xMin][characterLevel+1] == GRID_DOLLAR){
			
			map[CHARACTER_POSITION+xMin][characterLevel+1] = GRID_NULL;

			SoundManager.singleton.play(SOUND_DOLLAR, false, 0.1f, 1f);
			
			cashCount++;
			
			LevelActivity.coinsUpdateHandler.sendEmptyMessage(cashCount);	
		}
	}
	/**
	 * Animates the Grid, the character and the items. I.e. everything.
	 */
	@Override
	public void animate(float deltaTime) {
		//
		if(stop)
			return;
	
		
			
		ItemAnimated itm;
		///
		for(int i=0; i<currentItems.size(); i++){
			itm = currentItems.get(i);
			if(itm.checkStatus()){
				
				//Check if bomb detonated at lenny's position
				if(itm.itemId == ItemQueue.BOMB)
					if(checkBombRadius(itm.gridX, itm.gridY)){
						
						//Play ByBy Sound
						
						SoundManager.singleton.play(SOUND_BYBY, false, 1f, 1f);
						deathCount++;
						LevelActivity.deathsUpdateHandler.sendEmptyMessage(deathCount);
						if (vib != null) vib.vibrate(1000);
						
						ItemAnimated b = deadLenny.addCloneToGrid(animatedItemsManager, xMin+CHARACTER_POSITION, characterLevel+1);
						currentItems.add(b);
						b.startDeadLennyAnimation(Constants.VIEWPORT_HEIGHT,(scroll-(float)Math.floor(scroll))*1.5f*R_HEX);
					}
				animatedItemsManager.removeAnimation(itm);
				currentItems.remove(i);
			}
		}
		//
		animatedItemsManager.animate(deltaTime);
		
		int lani = character.getAnimator().animationRunning();
		
		//move grid according to beam animation
		if( lani == LennyAnimator.BEAM_ANIMATION_IN ||
				lani == LennyAnimator.BEAM_ANIMATION_OUT )
			return;
		
		//Calculated new scroll value
		scroll += deltaTime*jumpTimeScale*SPEED;
		
		//Calculate animation displacement
		delta = scroll-(float)Math.floor(scroll);
		int newxMin = (int)Math.floor(scroll);
		
		//Detects Grid boarders
		if(newxMin != xMin){
			next++;
			//Update progress indicator
			UpdateProgress();
		}
		xMin = newxMin;
		
		int deltaH = ((xMin+CHARACTER_POSITION)%2 == 0)?2:1;
		
		character.x = CHARACTER_POSITION*3f*R_HEX/2f-character.width/2f;
		
		//Handle beam animation
		if( lani == LennyAnimator.BEAM_ANIMATION_INVISIBLE){
			jumpTimeScale = 2.5f;
			if(scroll >= beamAnimationScroll + 2f){
				jumpTimeScale = 1f;
				characterLevel = nextLvlAnimation;
				character.y = RI_HEX*deltaH+characterLevel*2f*RI_HEX;
				UpdateProgress();
				
				character.getAnimator().startAnimation(LennyAnimator.BEAM_ANIMATION_IN, 0.7f);
			}
			return;
		}
		
		//End of map reached - end of game
		//sends results back to LevelActivity
		if(scroll+width >= mapWidth-1) {
//			scroll = 0;
			Message msg = new Message();
			msg.arg1 = deathCount;
			msg.arg2 = cashCount;
			LevelActivity.finishLevel.sendMessage(msg);
			stop = true;
			return;
		}	
		
		//next field reached
		if(next > delay){

			//check if dollar collected
			checkDollar();
			
			jumpTimeScale = 1f;
			next = 0;
			delay = 0;
			
			//y groundlevel for the animation	
			animLevel = RI_HEX*deltaH+characterLevel*2f*RI_HEX;
			
			//hexagonal insanity need consideration
			sign = ((xMin+CHARACTER_POSITION)%2 == 0)?-1:1;
			
				
			/// NEW CHOICES
			boolean nobeam = calculateChoices();	//false if beam is necessary
			
//			for(int i=0; i<choices.size(); i++)
//				Log.d("Choices",((MyWaypoint)choices.get(i)).toString());
			
			///
			
			MyWaypoint wp = new MyWaypoint(1, 0, true);
			
			//calculate waypoints
			wp = (MyWaypoint)choices.get(random.nextInt(choices.size()));
			
			//End of jump
			if(characterMode == 'J')
				SoundManager.singleton.play(SOUND_JUMP_DOWN, false, 0.3f, 1f);
			
			//Start beam animation
			if(nobeam == false){
				nextLvlAnimation = wp.y;
				character.getAnimator().startAnimation(LennyAnimator.BEAM_ANIMATION_OUT, 0.7f);
				beamAnimationScroll = scroll;
			}else{
				//new characterMode
				if((wp.x == 1) && (wp.y == 0))
					characterMode = 'W';
				else{
					characterMode = 'J';
					delay = wp.x-1;
					characterLevel += wp.y;
					
					//play jump up sound
					SoundManager.singleton.play(SOUND_JUMP_UP, false, 0.3f, 1f);
						
					if(wp.x%2 == 0)
						jump(wp.y*2f,1f,wp.x);
					else
						jump(wp.y*2f+sign, 1f, wp.x);	
				}
			}
			Log.d("Character","Choice="+characterMode);
		}
		
		float y,t;
		
		switch(characterMode){
		
		case 'W':	//Walk mode
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
			
		
		case 'J':	//jump mode
			t = (delta+next)/jumpTimeScale;
			y = (jumpSpeed*t+jumpGrav*t*t)*RI_HEX;
			character.y = animLevel+y;
			break;
			
		default:	//should not happen
			Log.d("HexagonGrid","UPS... bad level design");
			break;
		}
	
	}
	
	/**
	 * Calculates the physical values for a jump.
	 * @param h - delta y in grid coordinates
	 * @param s - defines the angular point. If h is negativ the angular point is s, otherwise h+s.
	 * @param width
	 * @return
	 */
	public float jump(float h, float s, float width) {
		
		float v,g,xEnd;
		
		s = (h>0)?s+h:s;
		 
		g = 12f;
		v = (float)Math.sqrt(s*4.0*g);	//s wegen scheitel
		xEnd = (1f/(2f*g))*(v+(float)Math.sqrt(v*v-4*g*h));
		
		this.jumpSpeed = v;
		this.jumpTimeScale = 1f/xEnd*width;
		this.jumpGrav = -g;
		
		return xEnd;
	}
	
	/**
	 * This function enables the item at a specified position on the grid.
	 * 
	 * @param x		X coordinate of the viewport
	 * @param y		Y coordinate of the viewport
	 * @param item	Item-id as defined in ItemQueue
	 * @return		True, if the item has been dropped on a valid position. 
	 * 				False otherwise.
	 */
	public boolean useItem(float x, float y, int item) {
		
		//play item drop sound
		SoundManager.singleton.play(SOUND_ITEM,false,0.2f,1f);
		
		y = VIEWPORT_HEIGHT-y;
		//Approximation by using quads instead of hexagons
		int xGrid = (int)Math.floor( (x+(1.0/4.0+1.5*scroll)*R_HEX) / (3.0/2.0*R_HEX) );
		int yGrid = (int)Math.floor((y+RI_HEX*(xGrid%2)) / (2.0*RI_HEX) );
		
		if((xGrid<mapWidth) && (yGrid<mapHeight) && (yGrid >= 0) && (xGrid > xMin+CHARACTER_POSITION+3)){
			switch(item){
				case ItemQueue.DELETEWALL:	
					if(map[xGrid][yGrid] == GRID_WALL){
						map[xGrid][yGrid] = GRID_NULL;
						return true;
					}
					break;
					
				case ItemQueue.WALL:
					if(map[xGrid][yGrid] == GRID_NULL){
						map[xGrid][yGrid] = GRID_CUSTOM_WALL;
						return true;
					}
					break;
					
				case ItemQueue.BOMB:
					return setBomb(xGrid, yGrid);
			}
		}
		
		return false;
	}
	
	/**
	 * This functions tries to determine what the player meant by placing a bomb on
	 * an unsupported/supported field. 
	 * @param x - x position of the user drop in grid coordinates
	 * @param y -y position of the user drop in grid coordinates
	 * @return true if bomb was placed, false otherwise
	 */
	public boolean setBomb(int x, int y){
		boolean ok = false;
		int xnew=x, ynew=y;
		
		//Correct placement
		if((map[x][y] == GRID_NULL) && ((map[x][y-1] & GRID_NULL) == 0)){
			xnew = x;
			ynew = y;
			ok = true;
		}else{
			if(y+1 < mapHeight)	//Drop on wall - placement above
				if((map[x][y] == GRID_WALL || map[x][y] == GRID_CUSTOM_WALL) && map[x][y+1] == GRID_NULL){
					ynew = y+1;
					ok = true;
				}
			if(y-2 >= 0)		//Drop in the air - placement on next wall below
				if(map[x][y] == GRID_NULL && map[x][y-1] == GRID_NULL && 
						(map[x][y-2] == GRID_WALL || map[x][y-2] == GRID_CUSTOM_WALL)){
					ynew = y-1;
					ok = true;
				}
					
		}
		
		if(ok){		//Item was placed
			ItemAnimated b = itemBomb.addCloneToGrid(animatedItemsManager, xnew, ynew);
			currentItems.add(b);
			b.startBombAnimation(7f);
		}
		return ok;
	}
	/**
	 * Places Dollar items randomly, equally distributed, on the grid.
	 * @param num - amount of items 
	 */
	public void placeDollar(int num) {
		ArrayList<Integer> list = new ArrayList<Integer>(mapHeight);
		
		//equally distributed dollar signs 
		int delta = (int)Math.floor(mapWidth/(float)num);
		int current,h;
		
		for(int j=1; j<num; j++){
			current = random.nextInt(delta);
			
			for(int i=0; i<mapHeight-1;i++){
				if((map[current+j*delta][i] == GRID_WALL) &&  (map[current+j*delta][i+1] == GRID_NULL))
					list.add(i+1);
			}
			
			if(list.size() != 0){
				h = list.get(random.nextInt(list.size()));
				map[current+j*delta][h] = GRID_DOLLAR;
			}
			
			list.clear();
		}
		
	}
	
	/**
	 * Sets the vibrator for more fun ;)
	 * @param v Vibrator
	 */
	public void setVibrator(Vibrator v) {
		vib = v;
	}

}
