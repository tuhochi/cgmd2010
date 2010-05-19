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
import at.ac.tuwien.cg.cgmd.bifth2010.R;

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
	
	float x, y;
	int width, height;
	int id;
	float movement[] = new float[4];
	int direction = 1;
	int textureID = -1;
	GL10 gl;
	
	
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
			LoadTexture(0,id,gl,context);
		}
	}
	
	public boolean update(GL10 gl) {
		float xn = x-(movement[2]-movement[3])*tileSizeX/18;
		float yn = y-(movement[0]-movement[1])*tileSizeY/18;
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
		
		if (gravity && movement[0]>0.0f) movement[0]-=1.0f;
		
		if (movement[3]-movement[2] < 0) direction = -1; //change rabbit direction
		else if (movement[3]-movement[2] > 0) direction = 1;
		
		return retVal;
	}

	/**
	 * This function draws our object on screen.
	 * @param gl
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
	
	public void move(float x, float y) {
		if (!testCollision(this.x+x,this.y+y)) {
			this.x += x;
			this.y += y;
		}
	}
	
	public void move(int direction, float amount) {
		if (direction != 1)
		movement[direction] = amount;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getPositionX() {return x;}
	public float getPositionY() {return y;}
	
	boolean testCollision(float x, float y) {
		int ul = level.TestCollision((int) Math.floor((x)/tileSizeX), (int) Math.floor((y)/tileSizeY));
		int ur = level.TestCollision((int) Math.floor((x+width-1)/tileSizeX), (int) Math.floor((y)/tileSizeY));
		int lr = level.TestCollision((int) Math.floor((x+width-1)/tileSizeX), (int) Math.floor((y+height-1)/tileSizeY));
		int ll = level.TestCollision((int) Math.floor((x)/tileSizeX), (int) Math.floor((y+height-1)/tileSizeY));
		if ((ul&0x00ffffff) == 0x00ff0000)
			score+=5;
		if ((ur&0x00ffffff) == 0x00ff0000)
			score+=5;
		if ((lr&0x00ffffff) == 0x00ff0000)
			score+=5;
		if ((ll&0x00ffffff) == 0x00ff0000)
			score+=5;
		
		if (y>level.getHeight()*tileSizeY)
			alive = false;
		
		if ((ul&0x00ff0000) != 0 &&
		    (ur&0x00ff0000) != 0 &&
			(lr&0x00ff0000) != 0 &&
			(ll&0x00ff0000) != 0) {
			return false;
		} else return true;
	}
	
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
				LoadTexture(0,id,gl,context);
			}	
		}
	}
	
	public static void clearTextures(GL10 gl) {
		IntBuffer texInts = IntBuffer.allocate(textures.size());
		for (Integer tex : textures.values()) {
			texInts.put(tex);
		}
		textures.clear();
		texInts.position(0);
		gl.glDeleteTextures(textures.size(),texInts);
	}
	
	private void LoadTexture(int num, int id, GL10 gl, Context context)
	{
		gl.glBindTexture(GL10.GL_TEXTURE_2D, texBuf.get(num));				
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
	
	public void enableGravity(boolean b) {
		gravity = b;
		if (b) movement[1] = 5.0f;
		else movement[1] = 0.0f;
	}
	
	public void enableCollision(boolean b) {
		collision = b;
	}
	
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
	
	public int getScore() {return score;}
	public void setScore(int s) {score = (score+s>0)?s:0;}
	public boolean getLife(){return alive;}

	public void revive() {
		alive = true;
	}
}
