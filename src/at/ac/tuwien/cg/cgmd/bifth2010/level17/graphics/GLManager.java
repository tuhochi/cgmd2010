package at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;

public class GLManager {


	private MatrixTrackingGL mGL;
	private Context mContext;
    private GLTextures mTextures;

	public void init(MatrixTrackingGL gl, Context context) {
		mGL = gl;
		mContext = context;
		mTextures = new GLTextures(mGL, mContext);
	}
	
	
	public MatrixTrackingGL getGLContext() {
		return mGL;
	}

	public void setGLContext(MatrixTrackingGL gl) {
		this.mGL = gl;
	}

	public GLTextures getTextures() {
		return mTextures;
	}











	private static GLManager instance = null;

	private GLManager() {}

	public static GLManager getInstance() {
		if (instance == null) {
			instance = new GLManager();
		}
		return instance;
	}

}
