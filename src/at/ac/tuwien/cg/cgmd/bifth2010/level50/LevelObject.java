package at.ac.tuwien.cg.cgmd.bifth2010.level50;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;

/**
 * Object to display a textured rectangle on the screen.
 * This is used by all level objects including the player objects and enemies.
 * The class manages the current score.
 * 
 * @author      Alexander Fritz
 * @author      Michael Benda
 */
public class LevelObject {
	
	private int tileSizeX = 30;
	private int tileSizeY = 30;
	private int tex[]=new int[1];
	private IntBuffer texBuf=IntBuffer.wrap(tex);
	private static HashMap<Integer,Integer> textures = new HashMap<Integer, Integer>(0);
	private Context context;
	private LevelCollision level;
	private boolean collision = false, gravity = false;
	private int score = 0;
	private boolean alive = true;
	private float scaleX = 1.0f;
	private float scaleY = 1.0f;
	private boolean climbable = false, climbableTop = false;
	private boolean enemy = false;
	
	float x, y;
	int width, height;
	int id;
	float movement[] = new float[4];
	public int direction = 1;
	int textureID = -1;
	GL10 gl;
	static MediaPlayer mp[] = new MediaPlayer[5];
	
	
	// Our vertices.
	private float vertices[] = {
		       0.0f, 0.0f, 0.0f,  // 0, Top Left
		       0.0f, 1.0f, 0.0f,  // 1, Bottom Left
		       1.0f, 1.0f, 0.0f,  // 2, Bottom Right
		       1.0f, 0.0f, 0.0f   // 3, Top Right
		};
	
	private float texcoord[] = {
		       0.0f,  1.0f,
		       0.0f,  0.0f,
		       1.0f,  0.0f,
		       1.0f,  1.0f
		};

	// The order we like to connect them.
	private short[] indices = { 0, 1, 2, 0, 2, 3 };

	// Our vertex buffer.
	private FloatBuffer vertexBuffer, texcoordBuffer;

	// Our index buffer.
	private ShortBuffer indexBuffer;

	/**
	 * Class constructor.
	 * Initializes vertex buffer, texture buffer and index buffer and the texture.
	 * 
	 * @param gl		the GL interface
	 * @param context	the rendering context
	 * @param level		the LevelCollision object to be used for
	 * @param x			the x position of the rectangle in world coordinates
	 * @param y			the y position of the rectangle in world coordinates
	 * @param width		the width of the rectangle in world units
	 * @param height	the height of the rectangle in world units
	 * @param id		the resource id of the texture to be used
	 * @param texcoords	the texture coordinates in correct order.
	 * 					if texcoords = <code>null</code> the default texture coordinates are used.
	 */
	public LevelObject(GL10 gl, Context context, LevelCollision level, float x, float y, int width, int height, int id, float texcoords[]) {
		this.context = context;
		this.level = level;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.id = id;
		this.gl = gl;
		if (texcoords != null)
			this.texcoord = texcoords;
		
		movement[0]=0.0f;
		movement[1]=0.0f;
		movement[2]=0.0f;
		movement[3]=0.0f;
		
		// a float is 4 bytes, therefore we multiply the number if
		// vertices with 4.
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		// short is 2 bytes, therefore we multiply the number if
		// vertices with 2.
		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		ByteBuffer tbb = ByteBuffer.allocateDirect(texcoord.length * 4);
		tbb.order(ByteOrder.nativeOrder());
		texcoordBuffer = tbb.asFloatBuffer();
		texcoordBuffer.put(texcoord);
		texcoordBuffer.position(0);
		
		if (textures.containsKey(id)) {
			texBuf.put(textures.get(id));
		} else {
			gl.glGenTextures(1, texBuf);
			textures.put(id, texBuf.get(0));
			LoadTexture(id,gl,context);
		}
		revive();
	}
	
	/**
	 * Changes the position of the rectangle according to the movement values
	 * and collision tests
	 * 
	 * @param gl		the GL interface
	 * @param frames	frame rate
	 * @return			<code>true</code> if a ceiling is hit (player)
	 * 					or <code>true</code> if object has turned around due to collision (enemy)
	 * 					<code>false</code> otherwise
	 */
	public boolean update(GL10 gl, float frames) {
		float fps = Math.max(frames, 20.0f);
		
		if (enemy) {
			float xn = x+direction*2.5f*20.0f/fps*tileSizeX/16.0f;
			if (testCollision(xn,y)) {
				turn();
				return true;
			} else {
				if (!testCollision(x+direction*tileSizeX,y+1)) {
					turn();
					return true;
				} else {
					x = xn;
					return false;
				}
			}
		}
		
				
		float xn = x-(movement[2]-movement[3])*20.0f/fps*tileSizeX/16.0f; //16... yeah... of course
		float yn = y-(movement[0]-movement[1])*20.0f/fps*tileSizeY/16.0f;
        boolean retVal = false;
		if (collision)
			if (!testCollision(xn, yn)){
				x = xn; y = yn;
			} else {
				if (testCollision(x, yn)){
					if (y<yn) {
						retVal = true;
						y = (float)Math.floor(yn/tileSizeY)*tileSizeY;
					} else {
						if (movement[0]>5.0f) movement[0] = 5.0f;
						y = (float)Math.ceil(yn/tileSizeY)*tileSizeY;
					}
				} else y = yn;
				if (testCollision(xn, y)){
					if (x<xn)
						x = (float)Math.floor(xn/tileSizeX)*tileSizeX;
					else
						x = (float)Math.ceil(xn/tileSizeX)*tileSizeX;
				} else x = xn;
			}
		
		if (gravity && !climbable && movement[0]>0.0f) movement[0]-=20.0f/fps;
		
		if (movement[3]-movement[2] < 0) direction = -1; //change rabbit direction
		else if (movement[3]-movement[2] > 0) direction = 1;
		
		return retVal;
	}

	/**
	 * This function draws the rectangle on the screen.
	 * 
	 * @param gl	the GL interface
	 */
	public void draw(GL10 gl) {
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glEnable(GL10.GL_TEXTURE_2D);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, texBuf.get(0));      
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
        gl.glTexCoordPointer(2, GL10.GL_FLOAT,0, texcoordBuffer);
        
        gl.glPushMatrix();
		gl.glLoadIdentity();
		
		gl.glTranslatef(x + (1-direction)/2*width, y, 0);
		gl.glScalef((float)width*direction, (float)height, 1.0f);

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		gl.glPopMatrix();

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);

		gl.glDisable(GL10.GL_CULL_FACE);
	}
	
	/**
	 * Moves the the rectangle by the given amount if collision test passed.
	 */
	public void move(float x, float y) {
		if (!testCollision(this.x+x,this.y+y)) {
			this.x += x;
			this.y += y;
		}
	}
	
	/**
	 * Sets the movement values of the rectangle in the given direction to the given value.
	 * No collision tested.
	 * 
	 * @param direction	pass 0/1/2/3 for direction up/down/left/right
	 * @param amount	the value the movement should change to
	 */
	public void move(int direction, float amount) {
//		if (direction != 1)
		movement[direction] = amount;
	}
	/**
	 * Sets current position of the rectangle.
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	/**
	 * Returns current x position of the rectangle.
	 */
	public float getPositionX() {return x;}
	/**
	 * Returns current y position of the rectangle.
	 */
	public float getPositionY() {return y;}
	/**
	 * Sets current width of the rectangle.
	 */
	public void setWidth(int w) { width=w; }
	/**
	 * Sets current height of the rectangle.
	 */
	public void setHeight(int h) { height=h; }
	
	/**
	 * Checks if the rectangle is intersecting any level object of the collision map.
	 * Checks if the rectangle is intersecting with a wall.
	 * Checks if the rectangle is intersecting with a coin and changes score accordingly.
	 * Checks if the rectangle is intersecting with a tree and allows climbing. 
	 * 
	 * @param x	x value to be checked in world coordinates
	 * @param y y value to be checked in world coordinates
	 * @return <code>true</code> if there is a collision with a wall
	 * 			<code>false</code> otherwise
	 */
	boolean testCollision(float x, float y) {
		int ul = level.TestCollision((int) Math.floor((x)/tileSizeX), (int) Math.floor((y)/tileSizeY), enemy);
		int ur = level.TestCollision((int) Math.floor((x+width-1)/tileSizeX), (int) Math.floor((y)/tileSizeY), enemy);
		int lr = level.TestCollision((int) Math.floor((x+width-1)/tileSizeX), (int) Math.floor((y+height-1)/tileSizeY), enemy);
		int ll = level.TestCollision((int) Math.floor((x)/tileSizeX), (int) Math.floor((y+height-1)/tileSizeY), enemy);
		
		if (enemy)
			if((ul&0x00ff0000) != 0 && // whole free
			    (ur&0x00ff0000) != 0 &&
				(lr&0x00ff0000) != 0 &&
				(ll&0x00ff0000) != 0
				) {
				return false;
			} else return true;
		
		if ((ul&0x00ffffff) == 0x00ffff00) {
			score+=5;
			if (((LevelActivity)context).sound)
				for (MediaPlayer a : mp) {
					if (!a.isPlaying()) {
						a.start();
						break;
					}
				}
		}
		if ((ur&0x00ffffff) == 0x00ffff00) {
			score+=5;
			if (((LevelActivity)context).sound)
				for (MediaPlayer a : mp) {
					if (!a.isPlaying()) {
						a.start();
						break;
					}
				}
		} 
		if ((lr&0x00ffffff) == 0x00ffff00) {
			score+=5;
			if (((LevelActivity)context).sound)
				for (MediaPlayer a : mp) {
					if (!a.isPlaying()) {
						a.start();
						break;
					}
				}
		}
		if ((ll&0x00ffffff) == 0x00ffff00) {
			score+=5;
			if (((LevelActivity)context).sound)
				for (MediaPlayer a : mp) {
					if (!a.isPlaying()) {
						a.start();
						break;
					}
				}
		}
		
		if (y>level.getHeight()*tileSizeY)
			alive = false;
				
		if ((ul&0x00ff0000) == 0 || // whole free
		    (ur&0x00ff0000) == 0 ||
			(lr&0x00ff0000) == 0 ||
			(ll&0x00ff0000) == 0) {
			return true;
		}		

		if (level.TestEnemyCollision(x+(float)width/10.0f,y+(float)height/10.0f) ||
			level.TestEnemyCollision(x+width-(float)width/10.0f,y+(float)height/10.0f) ||
			level.TestEnemyCollision(x+width-(float)width/10.0f,y+height-(float)height/10.0f) ||
			level.TestEnemyCollision(x+(float)width/10.0f,y+height-(float)height/10.0f)) {
			
			alive = false;
			move(0,15.0f);
			return true;
		}
		
		if ((ul&0x00555555) != 0x00550055 && //whole not in tree
		    (ur&0x00555555) != 0x00550055 &&
		    (lr&0x00555555) != 0x00550055 &&
			(ll&0x00555555) != 0x00550055) {
			climbable = false;
			climbableTop = false;
			if (gravity) movement[1] = 5.0f;
			return false;
			
		} else if ((ul&0x00555555) != 0x00550055 && //bottom in tree
		    (ll&0x00555555) == 0x00550055 ||
			(ur&0x00555555) != 0x00550055 &&
		    (lr&0x00555555) == 0x00550055) {
			if (!climbable) {
				movement[0] = 0.0f;
				movement[1] = 0.0f;
			}
			climbable = true;
			climbableTop = true;
//			if (gravity) movement[1] = 0.0f;
			return false;
			
		} else if ((ul&0x00555555) == 0x00550055 && //whole in tree
		    (ll&0x00555555) == 0x00550055 ||
		    (ur&0x00555555) == 0x00550055 &&
			(lr&0x00555555) == 0x00550055){
			if (!climbable) {
				movement[0] = 0.0f;
				movement[1] = 0.0f;
			}
			climbable = true;
			climbableTop = false;
//			if (gravity) movement[1] = 0.0f;
			return false;
		}

		return false;
	}
	
	/**
	 * Changes the texture used for the rectangle
	 * @param id		resource id of the new rectangle
	 * @param texcoords	the texture coordinates in correct order.
	 * 					if texcoords = <code>null</code> the default texture coordinates are used.
	 */
	public void changeTexture(int id, float texcoords[]) {
		if (texcoords != null && texcoords != texcoord) {
			ByteBuffer tbb = ByteBuffer.allocateDirect(texcoords.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			texcoordBuffer = tbb.asFloatBuffer();
			texcoordBuffer.put(texcoords);
			texcoordBuffer.position(0);
		}
			
		if (this.id!=id) {
			this.id = id;
			texBuf.clear();
			if (textures.containsKey(id)) {
				texBuf.put(textures.get(id));
			} else {
				gl.glGenTextures(1, texBuf);
				textures.put(id, texBuf.get(0));
				LoadTexture(id,gl,context);
			}	
		}
	}
	
	/**
	 * Deletes all textures from the buffer.
	 * 
	 * @param gl	the GL interface
	 */
	public static void clearTextures(GL10 gl) {
		IntBuffer texInts = IntBuffer.allocate(textures.size());
		for (Integer tex : textures.values()) {
			texInts.put(tex);
		}
		textures.clear();
		texInts.position(0);
		gl.glDeleteTextures(textures.size(),texInts);
	}
	
	/**
	 * Loads texture into the graphical buffer and sets the texture parameters.
	 * 
	 * @param id		resource id of the texture
	 * @param gl		the Gl interface
	 * @param context	the rendering context
	 */
	private void LoadTexture(int id, GL10 gl, Context context)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texBuf.get(0));				
		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), id);
		
		if (width == 0) width = bmp.getWidth();
		if (height == 0) height = bmp.getHeight();
		int w = bmp.getWidth();
		int h = bmp.getHeight();

		int pixels[]=new int[w*h];
		bmp.getPixels(pixels, 0, w, 0, 0, w, h);
		int pix1[]=new int[w*h];
		for(int i=0; i<h; i++)
        {
             for(int j=0; j<w; j++)
             {
                  //correction of R and B
                  int pix=pixels[i*w+j];
                  int pb=(pix>>16)&0xff;
                  int pr=(pix<<16)&0x00ff0000;
                  int px1=(pix&0xff00ff00) | pr | pb;
                  //correction of rows
                  pix1[(h-i-1)*w+j]=px1;
             }
        }     
		
		IntBuffer tbuf=IntBuffer.wrap(pix1);
//		ib.position(0);		
		gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, w, h, 0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, tbuf);		
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR );
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR );											
	}
	
	/**
	 * Triggers the downward pull of the object.
	 * 
	 * @param b	<code>true</code> to turn gravity on
	 * 			<code>false</code> to turn gravity off
	 */
	public void enableGravity(boolean b) {
		gravity = b;
		if (b) movement[1] = 5.0f;
		else movement[1] = 0.0f;
	}
	
	/**
	 * Triggers testing of collision while moving or updating the object.
	 * 
	 * @param b	<code>true</code> to turn collision tests on
	 * 			<code>false</code> to turn collision tests off
	 */
	public void enableCollision(boolean b) {
		collision = b;
	}
	
	/**
	 * Scales the object.
	 * 	
	 * @param xscale	amount in x direction
	 * @param yscale	amount in y direction
	 */
	public void scale(float xscale, float yscale) {
		x*=xscale;
		y*=yscale;
		width*=xscale;
		height*=yscale;
		tileSizeX*=xscale;
		tileSizeY*=yscale;
		scaleX*=xscale;
		scaleY*=yscale;
	}
	/**
	 * Returns the current score.
	 */
	public int getScore() {return score;}
	/**
	 * Sets the current score to the given parameter.
	 */
	public void setScore(int s) {score = (score+s>0)?s:0;}
	/**
	 * Returns <code>false</code> if object has fallen below the bottom
	 * edge of the level or has hit an enemy otherwise <code>true</code>.
	 */
	public boolean getLife(){return alive;}
	/**
	 * Sets the alive member <code>true</code>
	 */
	public void revive() {
		alive = true;
	}
	/**
	 * Declares the object an enemy.
	 * This implies automatic left/right movement as well as the death of the player when hit.
	 */
	public void awake() {
		enemy=true;
	}
	/**
	 * Initializes the static MediaPlayer for all level objects.
	 * @param context	rendering context
	 * @param id		the resource id of the soundfile to be used.
	 */
	public static void initMP(Context context, int id) {
		for (int i = 0; i<mp.length; i++) {
			mp[i] = MediaPlayer.create(context, id);
		}
	}
	
	/** 
     * Performs cleanup of the object.
     * Releases any {@link MediaPlayer}.
     */
	public static void clean() {
		for (MediaPlayer a : mp) {
			if (a!=null) {
				a.release();
			}
		}
	}
	public boolean getClimbable() {return climbable;}
	public boolean getClimbableTop() {return climbableTop;}
	/**
	 * Flips the object horizontally.
	 */
	public void turn() {direction*=-1;}
}
