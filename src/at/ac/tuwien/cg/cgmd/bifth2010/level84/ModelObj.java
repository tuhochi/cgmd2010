package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
	private int mNumVertices;
	
    private float[] vertexList;
	private float[] texcoordList;
	private byte[] indexList;
	private byte[] indexListVertices;
	private byte[] indexListTexCoords;
	private byte[] indexListNormals;
	
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

	public void initBuffers()
	{
		numIndices = indexList.length;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertexList.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertexList);
		vertexBuffer.position(0);

		byteBuf = ByteBuffer.allocateDirect(texcoordList.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(texcoordList);
		textureBuffer.position(0);

		indexBuffer = ByteBuffer.allocateDirect(indexList.length);
		indexBuffer.put(indexList);
		indexBuffer.position(0);
	}
	
	
	public void load(InputStream is, Context context )
	{
		float[] vertexList = new float[400];
		float[] texcoordList = new float[400];
		float[] normalsList = new float[400];
		byte[] indexList = new byte[400];
		byte[] indexListVertices = new byte[400];
		byte[] indexListTexCoords = new byte[400];
		byte[] indexListNormals = new byte[400];
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
            		vertexList[vertexPos] = Float.parseFloat(values[1]);
            		vertexList[vertexPos + 1] = Float.parseFloat(values[2]);
            		vertexList[vertexPos + 2] = Float.parseFloat(values[3]);
            		vertexPos += 3;
            	}
            	else if(line.startsWith("vt "))
            	{	// texture coordinate
    				// e.g. vt 0.499997 0.250000
            		String[] values = line.split(" ");
            		texcoordList[texcoordPos] = Float.parseFloat(values[1]);
            		texcoordList[texcoordPos + 1] = Float.parseFloat(values[2]);
            		texcoordPos += 2;
            	}
            	else if(line.startsWith("vn "))
            	{	// normal
    				// e.g. vn 0.000000 0.000000 -1.000000
            		String[] values = line.split(" ");
            		normalsList[normalsPos] = Float.parseFloat(values[1]);
            		normalsList[normalsPos + 1]  = Float.parseFloat(values[2]);
            		normalsList[normalsPos + 2]  = Float.parseFloat(values[3]);
            		normalsPos += 3;
            	}
            	else if(line.startsWith("f"))
            	{	// face (triangle)
        			// e.g. f 5/1/1 1/2/1 4/3/1
            		// f v/vt/vn
            		String[] values = line.split(" ");
            		for(int i = 1; i < 4; i++)
            		{
            			String[] indicesStr = values[i].split("/");
                		indexListVertices[indexPos] = Byte.parseByte(indicesStr[0]); //vertex 
                		indexListTexCoords[indexPos] = Byte.parseByte(indicesStr[1]); //texture coordinate
                		indexListNormals[indexPos] = Byte.parseByte(indicesStr[2]); //normal
                		indexPos ++;
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
