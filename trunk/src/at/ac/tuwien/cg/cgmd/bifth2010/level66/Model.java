package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.os.Handler;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class Model {

	private Context mContext;

	protected float _posX;
	protected float _posY;
	protected float _posZ;
	
	protected float _roll;
	protected float _pitch;
	protected float _yaw;
	
	protected float _scale;
	
	protected float _boundX;
	protected float _boundY;
	protected float _boundZ;
	
	private ShortBuffer _indexBuffer;
	private FloatBuffer _vertexBuffer;
	private FloatBuffer _normalBuffer;
	private FloatBuffer _colorBuffer;
	private FloatBuffer _texCoordBuffer;
	
	private boolean _renderCoord;
	
	private ShortBuffer _coordIndexBuffer;
	private FloatBuffer _coordVertexBuffer;
	private FloatBuffer _coordColorBuffer;
	
	private static float _coordVertices[] = { 0.0f, 0.0f, 0.0f,
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
	
	private static short _coordIndices[] = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
	
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

	@SuppressWarnings("static-access")	
	public Model(String filename, Context context)
	{
		this.mContext = context;
		//load Model
		this.load(ffilename, mContext);
		// set initial position
		_posX = 0.0f;
		_posY = 0.0f;
		_posZ = -5.0f;
		
		_roll = 0.0f;
		_pitch = 0.0f;
		_yaw = 0.0f;
		
		_scale = 0.5f;
		
		// only render coordination system - testing purpose only
		_renderCoord = true;
		
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

	public Model()
	{

		// set initial position
		_posX = 0.0f;
		_posY = 0.0f;
		_posZ = -5.0f;
		
		_roll = 0.0f;
		_pitch = 0.0f;
		_yaw = 0.0f;
		
		_scale = 0.5f;
		
		// only render coordination system - testing purpose only
		_renderCoord = true;
		
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
	
	public static void load(String filename, Context mContext)
	{
		// TODO: load from file
		@SuppressWarnings("unused")
		OBJRenderable load = new OBJRenderable(OBJModel.load(filename, mContext));
		System.out.print("lol");
	}
	
	public void render(GL10 gl)
	{
		if( _renderCoord )
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _coordVertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _coordColorBuffer);
	    
		    gl.glLoadIdentity();
		    
		    // set translation
		    gl.glTranslatef(_posX, _posY, _posZ);
		    // set scale
		    gl.glScalef( _scale, _scale, _scale);
		    // set rotation
		    gl.glRotatef( _rotX, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _rotY, 0.0f, 1.0f, 0.0f);
		    gl.glRotatef( _rotZ, 0.0f, 0.0f, 1.0f);
		    
		    gl.glDrawElements(GL10.GL_LINES, 12, GL10.GL_UNSIGNED_SHORT, _coordIndexBuffer);
		}
		else
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _colorBuffer);
	        gl.glNormalPointer(GL10.GL_FLOAT, 0, _normalBuffer);
	        gl.glTexCoordPointer(3, GL10.GL_FLOAT, 0, _texCoordBuffer);
	    
		    gl.glLoadIdentity();
		    
		 // set translation
		    gl.glTranslatef(-_posX, -_posY, -_posZ);
		    // set scale
		    gl.glScalef(_scale, _scale, _scale);
		    // set rotation
		    gl.glRotatef(-_rotX, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef(-_rotY, 0.0f, 1.0f, 0.0f);
		    gl.glRotatef(-_rotZ, 0.0f, 0.0f, 1.0f);
		    
		    gl.glDrawElements(GL10.GL_TRIANGLES, _indexBuffer.array().length, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
		}
	}
}
