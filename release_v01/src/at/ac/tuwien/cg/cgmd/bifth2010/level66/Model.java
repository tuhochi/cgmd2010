/**
 * Flight66 - a trip to hell
 * 
 * @author brm, dwi
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.os.Handler;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public abstract class Model {

	private Context mContext;

	protected float _posX;
	protected float _posY;
	protected float _posZ;
	
	protected float _rotZ;
	protected float _rotX;
	protected float _rotY;
	
	protected float _scale;
	
	protected float _boundX;
	protected float _boundY;
	protected float _boundZ;
	
	protected boolean _renderCoord;
	
	protected ShortBuffer _coordIndexBuffer;
	protected FloatBuffer _coordVertexBuffer;
	protected FloatBuffer _coordColorBuffer;
	
	//for test purpose, the coords of a coordsystem
	private static float _coordVertices[]= { 0.0f, 0.0f, 0.0f,
											  1.0f, 0.0f, 0.0f,
											  0.0f, 0.0f, 0.0f,
											  0.0f, 1.0f, 0.0f,
											  0.0f, 0.0f, 0.0f,
											  0.0f, 0.0f, 1.0f,
											  0.0f, 0.0f, 0.0f,
											  -1.0f, 0.0f, 0.0f,
											  0.0f, 0.0f, 0.0f,
											  0.0f, -1.0f, 0.0f,
											  0.0f, 0.0f, 0.0f,
											  0.0f, 0.0f, -1.0f
											};
	
	private static short _coordIndices[]  ={ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	
	private static float _coordColors[] = { 1.0f, 0.0f, 0.0f, 1.0f,
											1.0f, 0.0f, 0.0f, 1.0f,
											0.0f, 1.0f, 0.0f, 1.0f,
											0.0f, 1.0f, 0.0f, 1.0f,
											0.0f, 0.0f, 1.0f, 1.0f,
											0.0f, 0.0f, 1.0f, 1.0f,
											1.0f, 0.0f, 0.0f, 0.3f,
											1.0f, 0.0f, 0.0f, 0.3f,
											0.0f, 1.0f, 0.0f, 0.3f,
											0.0f, 1.0f, 0.0f, 0.3f,
											0.0f, 0.0f, 1.0f, 0.3f,
											0.0f, 0.0f, 1.0f, 0.3f
										  };

	public Model(Context context)
	{

		this.mContext = context;
		
		// set initial position
		_posX = 0.0f;
		_posY = 0.0f;
		_posZ = 0.0f;
		
		_rotZ = 0.0f;
		_rotX = 0.0f;
		_rotY = 0.0f;
		
		_scale = 0.5f;
		
		// only render coordination system - testing purpose only
		_renderCoord = false;
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(_coordVertices.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _coordVertexBuffer = vbb.asFloatBuffer();
        
        ByteBuffer ibb = ByteBuffer.allocateDirect(_coordIndices.length * 2);
        ibb.order(ByteOrder.nativeOrder());
        _coordIndexBuffer = ibb.asShortBuffer();
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(_coordColors.length * 4);
        cbb.order(ByteOrder.nativeOrder());
        _coordColorBuffer = cbb.asFloatBuffer();
        
        _coordVertexBuffer.put(_coordVertices);
        _coordIndexBuffer.put(_coordIndices);
        _coordColorBuffer.put(_coordColors);
        
        _coordVertexBuffer.position(0);
        _coordIndexBuffer.position(0);
        _coordColorBuffer.position(0);

	}
	
	public float getPosX() {
		return _posX;
	}

	public void setPosX(float posX) {
		_posX = posX;
	}

	public float getPosY() {
		return _posY;
	}

	public void setPosY(float posY) {
		_posY = posY;
	}

	public float getPosZ() {
		return _posZ;
	}

	public void setPosZ(float posZ) {
		_posZ = posZ;
	}

	public float getRotZ() {
		return _rotZ;
	}

	public void setRotZ(float rotZ) {
		_rotZ = rotZ;
	}

	public float getRotX() {
		return _rotX;
	}

	public void setRotX(float rotX) {
		_rotX = rotX;
	}

	public float getRotY() {
		return _rotY;
	}

	public void setRotY(float rotY) {
		_rotY = rotY;
	}

	public float getScale() {
		return _scale;
	}

	public void setScale(float scale) {
		_scale = scale;
	}

	public abstract void load( Context mContext );
	
	public abstract void render(GL10 gl);
}
