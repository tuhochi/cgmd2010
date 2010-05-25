package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import java.util.HashMap;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Class to take care of the collision testing and the display and update
 * of all level objects.
 * Initializes all non player level objects and manages the textures.
 * 
 * @author      Alexander Fritz
 * @author      Michael Benda
 */
public class LevelCollision {
	private int tileSizeX = 30;
	private int tileSizeY = 30;
	private final float tileCountInv = 1.0f/5.0f;
	
	private int collisionArray[];
	private int width, height;
	private String coinState = "";
	private HashMap<Integer, LevelObject> coins = new HashMap<Integer, LevelObject>();
	private HashMap<Integer, LevelObject> levelParts = new HashMap<Integer, LevelObject>();
	private HashMap<Integer, LevelObject> enemies = new HashMap<Integer, LevelObject>();
	
	/**
	 * Class constructor specifying the GL interface the rendering context.
	 * 
	 * @param gl		the GL interface
	 * @param context	the rendering context
	 * @param id		resource id of the level collision texture to be used
	 */
	public LevelCollision(GL10 gl, Context context, int id) {
		LoadTexture(gl, id, context);
	}
	
	/**
	 * Loads a collision map and initializes all level objects
	 * <p>
	 * The map uses following code:
	 * <p>
	 * <table border="0">
	 * <tr><td><code>0xffffff</code></td><td>white</td><td>clear</td></tr>
	 * <tr><td><code>0x00ffff</code></td><td>cyan</td><td>coin</td></tr>
	 * <tr><td><code>0xaaaa00</code></td><td>yellow</td><td>middle floor</td></tr>
	 * <tr><td><code>0xdddd00</code></td><td>yellow</td><td>right floor</td></tr>
	 * <tr><td><code>0x555500</code></td><td>yellow</td><td>left floor</td></tr>
	 * <tr><td><code>0xffff00</code></td><td>yellow</td><td>crate</td></tr>
	 * <tr><td><code>0xaa00aa</code></td><td>magenta</td><td>middle tree</td></tr>
	 * <tr><td><code>0xdd00dd</code></td><td>magenta</td><td>right tree</td></tr>
	 * <tr><td><code>0x550055</code></td><td>magenta</td><td>left tree</td></tr>
	 * <tr><td><code>0xff00ff</code></td><td>magenta</td><td>tree trunk</td></tr>
	 * <tr><td><code>0x0000ff</code></td><td>blue</td><td>enemy</td></tr>
	 * </table>
	 * 
	 * @param gl		the GL interface
	 * @param id		resource id of the texture to be loaded as level collision map
	 * @param context	the rendering context
	 */
	private void LoadTexture(GL10 gl, int id, Context context)
	{			
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
		width = bmp.getWidth();
		height = bmp.getHeight();
		int pixels[] = new int[width*height];
		bmp.getPixels(pixels, 0, width, 0, 0, width, height);	
		collisionArray=new int[width*height];
		for(int i=0; i<height; i++)
        {
             for(int j=0; j<width; j++)
             {
                  //correction of R and B
                  int pix=pixels[i*width+j];
                  int pb=(pix>>16)&0xff;
                  int pr=(pix<<16)&0x00ff0000;
                  int px1=(pix&0xff00ff00) | pr | pb;
                  //correction of rows
                  collisionArray[i*width+j]=px1;
                  if ((px1&0x00ffffff) == 0x00ffff00) { //blue
                	  float texcoord[] = { //empty coin
                			  tileCountInv,  2*tileCountInv,
                			  tileCountInv,  tileCountInv,
                			  2*tileCountInv,  tileCountInv,
                			  2*tileCountInv,  2*tileCountInv
                   	  };
                   	  LevelObject coin = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
                   	  coins.put(i*width+j,coin);
//					} else if ((px1&0x00ffffff) == 0x00ffffff) { //white
//						float texcoord[] = { //blue background
//							   0.0f,  1.0f,
//						       0.0f,  0.75f,
//						       0.25f,  0.75f,
//						       0.25f,  1.0f
//						};
//						LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
//						levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x0000aaaa) { //dark yellow
						float texcoord[] = { //middle floor
								   tileCountInv,  3*tileCountInv,
								   tileCountInv,  2*tileCountInv,
								   2*tileCountInv,  2*tileCountInv,
							       2*tileCountInv,  3*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x0000ffff) { //yellow
						float texcoord[] = { //crates
								   0.0f,  2*tileCountInv,
							       0.0f,  tileCountInv,
							       tileCountInv,  tileCountInv,
							       tileCountInv,  2*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x0000dddd) {
						float texcoord[] = { //right floor
								2*tileCountInv,  3*tileCountInv,
								2*tileCountInv,  2*tileCountInv,
								3*tileCountInv,  2*tileCountInv,
								3*tileCountInv,  3*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00005555) {
						float texcoord[] = { //left floor
								   0.0f,  3*tileCountInv,
							       0.0f,  2*tileCountInv,
							       tileCountInv,  2*tileCountInv,
							       tileCountInv,  3*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00550055) {
						float texcoord[] = { //left tree
								2*tileCountInv,  2*tileCountInv,
								2*tileCountInv,  tileCountInv,
							    3*tileCountInv,  tileCountInv,
							    3*tileCountInv,  2*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00aa00aa) {
						float texcoord[] = { //middle tree
								3*tileCountInv,  2*tileCountInv,
								3*tileCountInv,  tileCountInv,
							    4*tileCountInv,  tileCountInv,
							    4*tileCountInv,  2*tileCountInv
							};
							collisionArray[i*width+j] = 0x00550055;
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00dd00dd) {
						float texcoord[] = { //right tree
								4*tileCountInv,  2*tileCountInv,
								4*tileCountInv,  tileCountInv,
							    5*tileCountInv,  tileCountInv,
							    5*tileCountInv,  2*tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00ff00ff) {
						float texcoord[] = { //tree trunk
								3*tileCountInv,  tileCountInv,
								3*tileCountInv,  0.0f,
							    4*tileCountInv,  0.0f,
							    4*tileCountInv,  tileCountInv
							};
							LevelObject levelPart = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_tiles, texcoord);
							levelParts.put(i*width+j,levelPart);
					} else if ((px1&0x00ffffff) == 0x00ff0000) {
							LevelObject enemy = new LevelObject(gl, context, this, j*tileSizeX, i*tileSizeY, tileSizeX, tileSizeY, R.drawable.l50_enemy, null);
							enemy.awake();
							enemies.put(i*width+j,enemy);
					}
             }
        }  
	}
	
	/**
	 * Checks if x and y is located in an enemy.
	 * 
	 * @param x	x value to be checked in world coordinates
	 * @param y y value to be checked in world coordinates
	 * @return <code>true</code> if inside of an enemy
	 * 			<code>false</code> otherwise
	 */
	public boolean TestEnemyCollision(float x, float y) {
		for (LevelObject e : enemies.values()) {
//			Log.d("hit","e posx: "+e.getPositionX()+" e posy: "+e.getPositionY()+
//					" x: "+x+" y: "+y+" tileSizeX: "+tileSizeX+" tileSizeY: "+tileSizeY);
			if (e.getPositionX()<=x &&
				x<=e.getPositionX()+tileSizeX &&
				e.getPositionY()<=y &&
				y<=e.getPositionY()+tileSizeY) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Checks if x and y is located in an enemy.
	 * 
	 * @param x		x value to be checked in level coordinates
	 * @param y		y value to be checked in level coordinates
	 * @param enemy	if caller is an enemy <code>true</code>
	 * 				<code>false</code> otherwise
	 * @return		color value of level collision map
	 */
	public int TestCollision(int x,int y, boolean enemy) {
		if (0<=x && x<width && 0<=y && y<height) {
			int returnVal = collisionArray[y*width+x];
			if(enemy) return returnVal;
						
			if (coins.containsKey(y*width+x)){
				int i = y*width+x;
				float texcoord[] = {
					       0.0f,  1.0f,
					       0.0f,  0.0f,
					       1.0f,  0.0f,
					       1.0f,  1.0f
					};
				coins.get(i).changeTexture(R.drawable.l00_coin,texcoord);
				collisionArray[i] = 0x00ffffff; //don't bother with coin anymore
				levelParts.put(i,coins.get(i));
				coins.remove(i);
				coinState += (int)Math.ceil(Math.log10(i))+""+i;
			}
			return returnVal;
		} else {
			return 0x00ffffff;
		}
	}
	
	/**
	 * Draws all level objects
	 * 
	 * @param gl		the GL interface
	 * @param frames	current frame rate (0 if not known)
	 */
	public void draw(GL10 gl, float frames) {
		for (LevelObject coin : coins.values()) {
				coin.draw(gl);
		}
		for (LevelObject part : levelParts.values()) {
				part.draw(gl);
		}
		for (LevelObject enemy : enemies.values()) {
				enemy.update(gl, frames);
				enemy.draw(gl);
		}
	}	
	
	/**
	 * Resets all coins and level objects to their original positions and states
	 */
	public void reset() {
		for (int i = 0; i<height*width; i++) {
			if (levelParts.containsKey(i) && collisionArray[i] == 0x00ffffff) {
	 			float texcoord[] = {
	      			  tileCountInv,  2*tileCountInv,
	      			  tileCountInv,  tileCountInv,
	      			  2*tileCountInv,  tileCountInv,
	      			  2*tileCountInv,  2*tileCountInv
	         	};
	 			levelParts.get(i).changeTexture(R.drawable.l50_tiles,texcoord);
	 			coins.put(i,levelParts.get(i));
	 			levelParts.remove(i);
	 			collisionArray[i] = 0x00ffff00;
	 			coinState = "";
			}
		}
	}
	
	/**
	 * Scales all level objects.
	 * 	
	 * @param xscale	amount in x direction
	 * @param yscale	amount in y direction
	 */
	public void scale(float xscale, float yscale) {
		for (LevelObject coin : coins.values()) {
			coin.scale(xscale,yscale);
		}
		for (LevelObject part : levelParts.values()) {
			part.scale(xscale,yscale);
		}
		for (LevelObject enemy : enemies.values()) {
			enemy.scale(xscale,yscale);
		}
		tileSizeX*=xscale;
		tileSizeY*=yscale;
	}
	
	/**
	 * Returns current height of the level. (in level units)
	 */
	public float getHeight() {return height;}
	/**
	 * Returns current width of the level. (in level units)
	 */
	public float getWidth() {return width;}
	/**
	 * Returns a <code>String</code> containing the coded positions of the coins.
	 * 
	 * @see #setCoinState(String)
	 */
	public String getCoinState() {return coinState;}
	/**
	 * Sets the positions of the coins using a <code>String</code> code.
	 * <p>
	 * The String contains the indices of the coins which are already replaced.
	 * To ensure the correctness of the String each index is preceded 
	 * by the number of digits of the index.
	 * <p>
	 * E.g. the coins with the indices
	 * 123, 42 and 3314 would be coded as "312324243314".
	 */
	public void setCoinState(String state) {
		coinState=state;
		for (int actpos=0; actpos<coinState.length();) {
			int numpos = Integer.parseInt(coinState.substring(actpos, actpos+1));
			actpos++;
			int i = Integer.parseInt(coinState.substring(actpos, actpos+numpos));
			actpos+=numpos;
			float texcoord[] = {
				       0.0f,  1.0f,
				       0.0f,  0.0f,
				       1.0f,  0.0f,
				       1.0f,  1.0f
				};
			coins.get(i).changeTexture(R.drawable.l00_coin,texcoord);
			collisionArray[i] = 0x00ffffff; //don't bother with coin anymore
			levelParts.put(i,coins.get(i));
			coins.remove(i);
		}
	}
}
