package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;


public class ModelObj extends Model implements Serializable
{
    /**
	 * Auto Generated serialVersionID
	 */
	private static final long serialVersionUID = -762515591771882029L;
	
	float[] tempVertices = new float[3];
	float[] tempTexcoords = new float[2];
	float[] tempNormals = new float[3];
	byte[] tempIndexListfaces = new byte[3];
	
	List<float[]> vertexList = new ArrayList<float[]>();
	List<float[]> texcoordsList = new ArrayList<float[]>();
	List<float[]> normalsList = new ArrayList<float[]>();
	List<byte[]> faceList = new ArrayList<byte[]>();
	
	float[] finalVertexList = new float[vertexList.size()*3];
	float[] finalTexcoordsList = new float[texcoordsList.size()*2];
	float[] finalNormalsList = new float[normalsList.size()*3];
	byte[] indexList = new byte[faceList.size()*3];
	
	public ModelObj()
	{}	
	
	public ModelObj(InputStream is, Context context)
	{
		load(is,context);
		//initBuffers();
	}
	
	public ModelObj(InputStream is, int modelTex, Context context)
	{
		
	//	Log.i("Obj", "starting loading object");
		load(is,context);
	//	Log.i("Obj", "init buffers");
		//initBuffers();
		this.textureResource = modelTex;
		//Log.i("Obj", "finished loading object");
	}

	FloatBuffer normalBuffer;
	
	public void initBuffers()
	{
		numIndices = indexList.length;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(finalVertexList.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(finalVertexList);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(finalTexcoordsList.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(finalTexcoordsList);
		textureBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(finalNormalsList.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBuffer = byteBuf.asFloatBuffer();
		normalBuffer.put(finalNormalsList);
		normalBuffer.position(0);
		
		indexBuffer = ByteBuffer.allocateDirect(indexList.length);
		indexBuffer.put(indexList);
		indexBuffer.position(0);
	}
	
	
	public void load(InputStream is, Context context )
	{
		float[] tempVertices = new float[3];
		float[] tempTexcoords = new float[2];
		float[] tempNormals = new float[3];
		
		List<float[]> vertexList = new ArrayList<float[]>();
		List<float[]> texcoordsList = new ArrayList<float[]>();
		List<float[]> normalsList = new ArrayList<float[]>();
		
		byte[] tempIndexListfaces = new byte[3];
		List<byte[]> faceList = new ArrayList<byte[]>();
		
		int vertexPos = 0;
		int texcoordPos = 0;
		int normalsPos = 0;
		int indexPos = 0;

		Log.i("Obj", "before loading object");
        try {
            InputStreamReader isr = new InputStreamReader(is);            
            BufferedReader textReader = new BufferedReader(isr);
            
            String line = textReader.readLine();
            while (line != null)
            {
            	if(line.startsWith("v "))
            	{ 	// vertex
    				// e.g. v 1.000000 -1.000000 -1.000000
            		String[] values = line.split(" ");
            		tempVertices[vertexPos] = Float.parseFloat(values[1]);
            		tempVertices[vertexPos + 1] = Float.parseFloat(values[2]);
            		tempVertices[vertexPos + 2] = Float.parseFloat(values[3]);
            		vertexList.add(tempVertices);
            	}
            	else if(line.startsWith("vt "))
            	{	// texture coordinate
    				// e.g. vt 0.499997 0.250000
            		String[] values = line.split(" ");
            		tempTexcoords[texcoordPos] = Float.parseFloat(values[1]);
            		tempTexcoords[texcoordPos + 1] = Float.parseFloat(values[2]);
            		texcoordsList.add(tempTexcoords);
            	}
            	else if(line.startsWith("vn "))
            	{	// normal
    				// e.g. vn 0.000000 0.000000 -1.000000
            		String[] values = line.split(" ");
            		tempNormals[normalsPos] = Float.parseFloat(values[1]);
            		tempNormals[normalsPos + 1]  = Float.parseFloat(values[2]);
            		tempNormals[normalsPos + 2]  = Float.parseFloat(values[3]);
            		normalsList.add(tempNormals);
            	}
            	else if(line.startsWith("f"))
            	{	// face (triangle)
        			// e.g. f 5/1/1 1/2/1 4/3/1
            		// f v/vt/vn
            		String[] values = line.split(" ");
            		indexPos = 0;
            		for(int i = 1; i < 4; i++)
            		{
            			String[] indicesStr = values[i].split("/");
                		tempIndexListfaces[indexPos] = Byte.parseByte(indicesStr[0]); //vertex 
                		tempIndexListfaces[indexPos + 1] = Byte.parseByte(indicesStr[1]); //texture coordinate
                		tempIndexListfaces[indexPos + 2] = Byte.parseByte(indicesStr[2]); //normal
                		faceList.add(tempIndexListfaces);
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
        
        //build our arrays
        
        
		int pos = 0;
		Log.i("Facelist size", ": " + faceList.size());
        for (int i=0; i < faceList.size(); i++)
        {
        	byte[] tempface = faceList.get(i);
        	float[] tempVertex = vertexList.get(tempface[0]-1);
        	float[] tempTC = texcoordsList.get(tempface[1]-1);
        	float[] tempNormal = normalsList.get(tempface[2]-1);
        
//        	finalVertexList[pos] = tempVertex[0]; 
//        	finalTexcoordsList[pos] = tempTC[0];
//        	finalNormalsList[pos] = tempNormal[0];
//        	finalVertexList[pos+1] = tempVertex[1];
//        	finalTexcoordsList[pos+1] = tempTC[1];
//        	finalNormalsList[pos+1] = tempNormal[1];
//        	finalVertexList[pos+2] = tempVertex[2];
//        	finalTexcoordsList[pos+2] = tempTC[2];
//        	finalNormalsList[pos+2] = tempNormal[2];
//        	pos += 3;
        }
        
	}
	
	public void drawMesh(GL10 gl) {
		if (vertexBuffer != null && indexBuffer != null && numIndices > 0) {
			
			//Bind our only previously generated texture in this case
			gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
			
			//Point to our buffers
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);
	
			//Set the face rotation
			gl.glFrontFace(GL10.GL_CCW);
			
			//Enable the vertex and texture state
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);
			gl.glNormalPointer(3, GL10.GL_FLOAT, normalBuffer);
			
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawElements(GL10.GL_TRIANGLES, numIndices, GL10.GL_UNSIGNED_BYTE, indexBuffer);
			
			//Disable the client state before leaving
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		}
		
		gl.glPopMatrix();
	}
	
	/**
	 * Update the model's transformations.
	 */
	public void update(GL10 gl, double deltaTime) {
		mTrans = Matrix4x4.mult(Matrix4x4.RotateX((float)(1f * deltaTime)), mTrans);

		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -4);
		//gl.glMultMatrixf(mTrans.toFloatArray(), 0);
	}
}
