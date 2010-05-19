package at.ac.tuwien.cg.cgmd.bifth2010.level70.traingame;

import java.nio.ByteBuffer;

import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GlTexture;

/**
 * TrainTexture. Manages the sprite textures for the train. It holds an OpenGL 
 * texture handle and the texture coordinate buffer for each train sprite.
 * 
 * @author herrjohann
 */

public class TrainTexture {
	
	// ----------------------------------------------------------------------------------
	// -- Static members ----
	
	private static int SPRITE_SIZE = 64; //< Size of a train sprite
	
	
	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	private GlTexture tex;                  //< OpenGl texture handle
	private ArrayList<FloatBuffer> texBufs; //< Texture coordinates
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor ----
	
	/**
	 * Create tile texture.
	 */
	public TrainTexture() {
		
		tex = new GlTexture(R.drawable.l70_train);
		
		texBufs = new ArrayList<FloatBuffer>();
		
		float tw = (float)SPRITE_SIZE / tex.getWidth();
		float th = (float)SPRITE_SIZE / tex.getHeight();
		
		// Create texcoord buffers and store them in an array
		for (int i = 0; i < 2; i++) {
			float[] texcoords = getTexcoords(i, tw, th);
			ByteBuffer tbb = ByteBuffer.allocateDirect(texcoords.length * 4);
			tbb.order(ByteOrder.nativeOrder());
			FloatBuffer texBuf = tbb.asFloatBuffer();
			texBuf.put(texcoords);
			texBuf.position(0);
			texBufs.add(texBuf);
		}
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Bind tile texture.
	 */
	public void bind() {
		tex.bind();
	}
	
	
	
	/**
	 * Unbind tile texture.
	 */
	public void unbind() {
		tex.unbind();
	}
	
	
	/**
	 * Return texture coordinate buffer for the specified tile.
	 * @param index Sprite index.
	 * @return Texture coordinate buffer.
	 */
	public FloatBuffer getTexBuffer(int index) {
		return texBufs.get(index);
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Private methods ----
	
	/**
	 * Return texture coordinates for the specified tile. The order in TileEnum
	 * must correspond to the order in the texture image file.
	 * @param type The tile type.
	 * @param tw Normalized sprite width.
	 * @param th Normalized sprite height.
	 */
	private static float[] getTexcoords(int index, float tw, float th) {
		float left  = tw * index;
		float right = tw * (index + 1);
		float texcoords[] = { left, 1,  right, 1,  right, 0,  left, 0 };
		return texcoords;
	}
}
