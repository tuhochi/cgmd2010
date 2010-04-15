package at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;

/**
 * A singleton that encapsulates the OpenGL context
 * @author MaMa
 *
 */
public class GLManager {


	private MatrixTrackingGL mGL;
	private Context mContext;
    private GLTextures mTextures;

    /**
     * Initializes the class and sets the OpenGL context
     * @param gl The OpenGL context
     * @param context The Activity that runs the game
     */
	public void init(MatrixTrackingGL gl, Context context) {
		mGL = gl;
		mContext = context;
		mTextures = new GLTextures(mGL, mContext);
	}
	
	/**
	 * Getter for the OpenGL context
	 * @return Returns the OpenGL context
	 */
	public MatrixTrackingGL getGLContext() {
		return mGL;
	}

	/**
	 * Setter for the OpenGL context
	 * @param gl The new OpenGL context
	 */
	public void setGLContext(MatrixTrackingGL gl) {
		this.mGL = gl;
	}

	/**
	 * Getter for the TextureManager
	 * @return Returns the TextureManager
	 */
	public GLTextures getTextures() {
		return mTextures;
	}



	private static GLManager instance = null;

	private GLManager() {}

	/**
	 * Getter for the singleton instance
	 * @return Returns the singleton instance
	 */
	public static GLManager getInstance() {
		if (instance == null) {
			instance = new GLManager();
		}
		return instance;
	}

}
