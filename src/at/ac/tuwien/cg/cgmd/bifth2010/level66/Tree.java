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

public class Tree extends Model {
	
	static protected ShortBuffer _indexBuffer;
	static protected FloatBuffer _vertexBuffer;
	static protected FloatBuffer _normalBuffer;
	static protected FloatBuffer _colorBuffer;
	static protected FloatBuffer _texCoordBuffer;
	
	static protected int _cntVertices = 0;

	public Tree(Context context) {
		
		super(context);
		
		_rotX = -90.0f;
		
		if( _cntVertices == 0 )
			this.load(context);
	}
	
	public void load( Context mContext )
	{
		List<Float> vertices = new LinkedList<Float>();
        List<Float> texCoords = new LinkedList<Float> ();
        List<Float> normals = new LinkedList<Float> ();
        List<Short> indices = new LinkedList<Short>();
        
		try {
			
			InputStream is = mContext.getResources().openRawResource(R.raw.l66_baum);
				
			
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
			tmpColors[i * 4]= 0.0f;
			tmpColors[i * 4 + 1]= 1.0f;
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
		    gl.glRotatef(-_rotZ, 0.0f, 0.0f, 1.0f);
		    
		    gl.glDrawElements(GL10.GL_LINES, 12, GL10.GL_UNSIGNED_SHORT, _coordIndexBuffer);
		}
		else
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _colorBuffer);
	        gl.glNormalPointer(GL10.GL_FLOAT, 0, _normalBuffer);
	        //gl.glTexCoordPointer(3, GL10.GL_FLOAT, 0, _texCoordBuffer);
	    
		    //gl.glLoadIdentity();
	        gl.glPushMatrix();
		    
	        // set translation
		    gl.glTranslatef(_posX, _posY, _posZ);
		    // set scale
		    gl.glScalef(_scale, _scale, _scale);
		    // set rotation
		    gl.glRotatef( _rotX, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _rotY, 0.0f, 1.0f, 0.0f);
		    gl.glRotatef(-_rotZ, 0.0f, 0.0f, 1.0f);
		    
		    gl.glDrawElements(GL10.GL_TRIANGLES, _cntVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
		    
		    gl.glPopMatrix();
		}
	}
}
