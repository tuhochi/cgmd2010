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
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class PlayerAircraft extends Model {
	
	static protected ShortBuffer _indexBuffer;
	static protected FloatBuffer _vertexBuffer;
	static protected FloatBuffer _normalBuffer;
	static protected FloatBuffer _colorBuffer;
	static protected FloatBuffer _texCoordBuffer;
	
	static protected int _cntVertices = 0;
	
	private int _accX;
	private int _accY;
	
	private final float	ROLL_STEPSIZE = 1.0f;
	private final float PITCH_STEPSIZE = 1.0f;
	
	private final int	ACC_STEPSIZE = 10;
	private final int	ACC_FRAME_STEPSIZE = 1;
	private final int	ROLL_TO_POS_RATIO = 450;
	private final int	PITCH_TO_POS_RATIO = 450;
	private final int	CNT_FRAMES_TILL_ACC_DEC = 10;
	private final int	SPECIAL_ROLL_STEPSIZE = 10;
	
	private final int	SPECIAL_NONE = 0;
	private final int	SPECIAL_ROLL = 1;
	
	private int _cnt_frames_x;
	private int _cnt_frames_y;
	
	private int _special;
	private float _special_roll;
	private int _special_roll_dir;
	
	public PlayerAircraft(Context context)
	{
		super(context);
		
		this.load(context);
		
		// only render coordination system - testing purpose only
		_renderCoord = false;
		
		_accX = 0;
		_accY = 0;
		
		_special = 0;
		
		_posZ = -5.0f;
		
		_scale = 0.1f;
		
		_cnt_frames_x = 0;
		_cnt_frames_y = 0;
		
	}
	
	private void rollInc()
	{
		_rotZ += ROLL_STEPSIZE;
		
		if ( _rotZ > 45.0f)
			_rotZ = 45.0f;
	}
	
	private void rollDec()
	{
		_rotZ -= ROLL_STEPSIZE;
		
		if ( _rotZ < -45.0f )
			_rotZ = -45.0f;
	}
	
	private void pitchInc()
	{
		_rotX += PITCH_STEPSIZE;
		
		if ( _rotX > 45.0f)
			_rotX = 45.0f;
	}
	
	private void pitchDec()
	{
		_rotX -= PITCH_STEPSIZE;
		
		if ( _rotX < -45.0f )
			_rotX = -45.0f;
	}
	
	public void specialMoveRoll()
	{
		if ( _special == SPECIAL_NONE )
		{
			_special = SPECIAL_ROLL;
			_special_roll = 360;
			
			if ( _rotZ == 0)
			{
				float rndVal = (float) Math.random() - 0.5f;
				_special_roll_dir = (int) ( rndVal / Math.abs( rndVal ) );
			}
			else
				_special_roll_dir = (int) ( _rotZ / Math.abs(_rotZ) );
		}
	}
	
	public void update()
	{
	// X - ACCELERATION UPDATE
		if ( _accX != 0 )
		{
			if ( _accX > 0 )
			{
				rollInc();
			}
			else
			{
				rollDec();
			}
			
			if ( _accX != 0 )
				_accX = _accX + _accX / Math.abs(_accX) * -1 * ACC_FRAME_STEPSIZE;
			
			_cnt_frames_x = CNT_FRAMES_TILL_ACC_DEC;
		}
		else
		{
			if ( _cnt_frames_x > 0 )
			{
				_cnt_frames_x--;
			}
			else
			{
				if ( _rotZ != 0 )
				{
					if ( _rotZ > 0 )
						rollDec();
					else
						rollInc();
				}
			}
		}
		
	// Y - ACCELERATION UPDATE
		if ( _accY != 0 )
		{
			if ( _accY > 0 )
			{
				pitchInc();
			}
			else
			{
				pitchDec();
			}
			
			if ( _accY != 0 )
				_accY = _accY + _accY / Math.abs(_accY) * -1 * ACC_FRAME_STEPSIZE;
			
			_cnt_frames_y = CNT_FRAMES_TILL_ACC_DEC;
		}
		else
		{
			if ( _cnt_frames_y > 0 )
			{
				_cnt_frames_y--;
			}
			else
			{
				if ( _rotX != 0 )
				{
					if ( _rotX > 0 )
						pitchDec();
					else
						pitchInc();
				}
			}
		}
		
	// POSITION UPDATE
		
		_posX = _posX + _rotZ / ROLL_TO_POS_RATIO;
		if ( _posX > 1.5f)
			_posX = 1.5f;
		else if ( _posX < -1.5f)
			_posX = -1.5f;
		
		_posY = _posY + _rotX / PITCH_TO_POS_RATIO;
		
		if ( _posY > 2.5f)
			_posY = 2.5f;
		else if ( _posY < -2.5f)
			_posY = -2.5f;
		
		switch ( _special )
		{
			case SPECIAL_ROLL:
				_special_roll -= SPECIAL_ROLL_STEPSIZE;
				if ( _special_roll <= 0 )
				{
					_special_roll = 0;
					_special = SPECIAL_NONE;
				}
				break;
		}
	}
	
	public void moveLeft()
	{
		_accX -= ACC_STEPSIZE;
	}
	
	public void moveRight()
	{
		_accX += ACC_STEPSIZE;
	}
	
	public void moveUp()
	{
		_accY += ACC_STEPSIZE;
	}
	
	public void moveDown()
	{
		_accY -= ACC_STEPSIZE;
	}
	
	public void load(Context mContext)
	{
		List<Float> vertices = new LinkedList<Float>();
        List<Float> texCoords = new LinkedList<Float> ();
        List<Float> normals = new LinkedList<Float> ();
        List<Short> indices = new LinkedList<Short>();
        
		try {
			
			InputStream is = mContext.getResources().openRawResource(R.raw.l66_airplane); 
			
	        is.available();
	        InputStreamReader isr = new InputStreamReader(is);            
	        BufferedReader textReader = new BufferedReader(isr);
	       
	        String line = textReader.readLine();
	        
	        while (line != null)
	        {
	        	if(line.startsWith("v "))
	        	{
	        		String[] values = line.split(" ");
	        		float x = Float.parseFloat(values[1]);
	        		float y = Float.parseFloat(values[2]);
	        		float z = Float.parseFloat(values[3]);
	        		vertices.add(x);
	        		vertices.add(y);
	        		vertices.add(z);
	        	}
	        	else if(line.startsWith("vt "))
	        	{
	        		String[] values = line.split(" ");
	        		float x = Float.parseFloat(values[1]);
	        		float y = Float.parseFloat(values[2]);
	        		texCoords.add(x);
	        		texCoords.add(y);
	        	}
	        	else if(line.startsWith("vn "))
	        	{
	        		String[] values = line.split(" ");
	        		float x = Float.parseFloat(values[1]);
	        		float y = Float.parseFloat(values[2]);
	        		float z = Float.parseFloat(values[3]);
	        		normals.add(x);
	        		normals.add(y);
	        		normals.add(z);
	        	}
	        	else if(line.startsWith("f"))
	        	{
	        		String[] values = line.split(" ");
	        		for(int i = 1; i < 4; i++)
	        		{
	        			String[] indicesStr = values[i].split("/");
	            		short vertIndex = Short.parseShort(indicesStr[0]);
	            		short texIndex = Short.parseShort(indicesStr[1]);
	            		short normalIndex = Short.parseShort(indicesStr[2]);
	            		// short[] index = {vertIndex, texIndex, normalIndex};
	            		indices.add((short) (vertIndex - 1));
	        		}
	        	}
	        	
	        	line = textReader.readLine();
	        }
	        textReader.close();
	        isr.close();
	        is.close();
		} catch (IOException e) {
            throw new RuntimeException(e);
        }
		
		
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.toArray().length * 4);
        vbb.order(ByteOrder.nativeOrder());
        _vertexBuffer = vbb.asFloatBuffer();
        
        ByteBuffer ibb = ByteBuffer.allocateDirect(indices.toArray().length * 2);
        ibb.order(ByteOrder.nativeOrder());
        _indexBuffer = ibb.asShortBuffer();
        
        _cntVertices = indices.toArray().length;
        
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.toArray().length * 4);
        nbb.order(ByteOrder.nativeOrder());
        _normalBuffer = nbb.asFloatBuffer();
        
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.toArray().length * 4);
        tbb.order(ByteOrder.nativeOrder());
        _texCoordBuffer = tbb.asFloatBuffer();
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(vertices.toArray().length / 3 * 4 * 4);
        cbb.order(ByteOrder.nativeOrder());
        _colorBuffer = cbb.asFloatBuffer();
        
        float[] tmpVertices = new float[vertices.size()];
        short[] tmpIndices = new short[indices.size()];
        float[] tmpNormals = new float[normals.size()];
        float[] tmpTexCoords = new float[texCoords.size()];
        float[] tmpColors = new float [vertices.size() / 3 * 4];
        
		for(int i=0; i < vertices.size(); i++){
			tmpVertices[i]=vertices.get(i).floatValue();
		}
		
		for(int i=0; i < indices.size(); i++){
			tmpIndices[i]=indices.get(i).shortValue();
		}
		
		for(int i=0; i < normals.size(); i++){
			tmpNormals[i]=normals.get(i).floatValue();
		}
		
		for(int i=0; i < texCoords.size(); i++){
			tmpTexCoords[i]=texCoords.get(i).floatValue();
		}
		
		for(int i=0; i < vertices.size() / 3; i++){
			tmpColors[i * 4]= 1.0f;
			tmpColors[i * 4 + 1]= 0.1f;
			tmpColors[i * 4 + 2]= 0.1f;
			tmpColors[i * 4 + 3]= 1.0f;
		}
		
		_vertexBuffer.put(tmpVertices);
		_indexBuffer.put(tmpIndices);
		_normalBuffer.put(tmpNormals);
		_texCoordBuffer.put(tmpTexCoords);
		_colorBuffer.put(tmpColors);
        
        _vertexBuffer.position(0);
        _indexBuffer.position(0);
        _normalBuffer.position(0);
        _texCoordBuffer.position(0);
        _colorBuffer.position(0);
        
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
		    
		    if ( _special == SPECIAL_ROLL )
		    	gl.glRotatef(-_rotZ + _special_roll_dir * _special_roll, 0.0f, 0.0f, 1.0f);
		    else
		    	gl.glRotatef(-_rotZ, 0.0f, 0.0f, 1.0f);
		    
		    gl.glDrawElements(GL10.GL_LINES, 12, GL10.GL_UNSIGNED_SHORT, _coordIndexBuffer);
		}
		else
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _colorBuffer);
	        gl.glNormalPointer(GL10.GL_FLOAT, 0, _normalBuffer);
	        //gl.glTexCoordPointer(3, GL10.GL_FLOAT, 0, _texCoordBuffer);
	    
		    gl.glLoadIdentity();
		    
		    // set translation
		    gl.glTranslatef(_posX, _posY, _posZ);
		    // set scale
		    gl.glScalef(_scale, _scale, _scale);
		    // set rotation
		    //gl.glRotatef( -90, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _rotX, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _rotY, 0.0f, 1.0f, 0.0f);
		    
		    if ( _special == SPECIAL_ROLL )
		    	gl.glRotatef(-_rotZ + _special_roll_dir * _special_roll, 0.0f, 0.0f, 1.0f);
		    else
		    	gl.glRotatef(-_rotZ, 0.0f, 0.0f, 1.0f);
		    
		    gl.glRotatef( -90, 1.0f, 0.0f, 0.0f);
		    
		    //gl.glDrawElements(GL10.GL_TRIANGLES, _indexBuffer.array().length / 4, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
		    gl.glDrawElements(GL10.GL_TRIANGLES, _cntVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
		}
	}
}
