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
	
	protected ShortBuffer _indexBuffer;
	protected FloatBuffer _vertexBuffer;
	protected FloatBuffer _normalBuffer;
	protected FloatBuffer _colorBuffer;
	protected FloatBuffer _texCoordBuffer;
	
	protected int _cntVertices;
	
	protected boolean _renderCoord;
	
	protected ShortBuffer _coordIndexBuffer;
	protected FloatBuffer _coordVertexBuffer;
	protected FloatBuffer _coordColorBuffer;
	
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

	//@SuppressWarnings("static-access")	
	public Model(String filename, Context context)
	{
		this(context);
	}

	public Model(Context context)
	{

		this.mContext = context;
		
		// set initial position
		_posX = 0.0f;
		_posY = 0.0f;
		_posZ = 0.0f;
		
		_roll = 0.0f;
		_pitch = 0.0f;
		_yaw = 0.0f;
		
		_scale = 0.5f;
		
		_cntVertices = 0;
		
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
	
	public void load(String filename, Context mContext)
	{
		// TODO: load from file
		//OBJRenderable load = new OBJRenderable(OBJModel.load(filename, mContext));
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
	        		//vertices.add(new OBJVector(x, y, z));
	        	}
	        	else if(line.startsWith("vt "))
	        	{
	        		String[] values = line.split(" ");
	        		float x = Float.parseFloat(values[1]);
	        		float y = Float.parseFloat(values[2]);
	        		texCoords.add(x);
	        		texCoords.add(y);
	        		//texCoords.add(new OBJVector(x, y));
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
	        		//normals.add(new OBJVector(x, y, z));
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
	            		short[] index = {vertIndex, texIndex, normalIndex};
	            		indices.add(vertIndex);
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
		
		//vertices.toArray(_coordVertices);
		
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
		
		for(int i=0; i < vertices.size() / 3 * 4; i++){
			tmpColors[i]= 1.0f;
		}
		
		_vertexBuffer.put(tmpVertices);
		_indexBuffer.put(tmpIndices);
		_normalBuffer.put(tmpNormals);
		_texCoordBuffer.put(tmpTexCoords);
		_colorBuffer.put(tmpColors);
        
        /*
        float[] tmp = new float[vertices.size()];
        tmp = vertices.toArray( tmp );
        _vertexBuffer.put( vertices.toArray( tmp ) );
        _vertexBuffer.put
        _indexBuffer.put(indices);
        _normalBuffer.put(normals);
        _texCoordBuffer.put(texCoords);
        */
        _vertexBuffer.position(0);
        _indexBuffer.position(0);
        _normalBuffer.position(0);
        _texCoordBuffer.position(0);
        _colorBuffer.position(0);
        
        /*
		_coordVertices = new float[vertices.size()];
		for(int i=0; i < vertices.size(); i++){
			_coordVertices[i]=vertices.get(i).floatValue();
		}
		//_coordIndices
		_coordIndices = new short[indices.size()];
		for(int i=0; i < indices.size(); i++){
			_coordIndices[i]=indices.get(i).shortValue();
		}
		
		*/
		//.toArray(_coordVertices);
		//System.out.print("lol");
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
		    gl.glRotatef( _pitch, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _yaw, 0.0f, 1.0f, 0.0f);
		    gl.glRotatef(-_roll, 0.0f, 0.0f, 1.0f);
		    
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
		    gl.glTranslatef(_posX, _posY, _posZ);
		    // set scale
		    gl.glScalef(_scale, _scale, _scale);
		    // set rotation
		    gl.glRotatef( _pitch, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _yaw, 0.0f, 1.0f, 0.0f);
		    gl.glRotatef(-_roll, 0.0f, 0.0f, 1.0f);
		    
		    gl.glDrawElements(GL10.GL_TRIANGLES, _cntVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
		}
	}
}
