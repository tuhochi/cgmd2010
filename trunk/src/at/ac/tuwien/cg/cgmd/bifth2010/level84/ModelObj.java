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
	int[] tempIndexListfaces = new int[3];
	
	List<float[]> vertexList = new ArrayList<float[]>();
	List<float[]> texcoordsList = new ArrayList<float[]>();
	List<float[]> normalsList = new ArrayList<float[]>();
	List<int[]> faceList = new ArrayList<int[]>();
	
	float[] finalVertexList = new float[vertexList.size()*3];
	float[] finalTexcoordsList = new float[texcoordsList.size()*2];
	float[] finalNormalsList = new float[normalsList.size()*3];
	byte[] finalIndexList = new byte[faceList.size()*3];
	
	FloatBuffer normalBuffer;
	
	public ModelObj()
	{}	
	
	public ModelObj(InputStream is, Context context)
	{
		load(is,context);
		//initBuffers();
	}
	
	public ModelObj(InputStream is, int modelTex, Context context)
	{
		
		Log.i("Obj", "starting loading object");
		load(is,context);
		Log.i("Obj", "init buffers");
		initBuffers();
		this.textureResource = modelTex;
		Log.i("Obj", "finished loading object");
	}

	public void initBuffers()
	{
		numIndices = finalIndexList.length;
		
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

//		byteBuf = ByteBuffer.allocateDirect(finalNormalsList.length * 4);
//		byteBuf.order(ByteOrder.nativeOrder());
//		normalBuffer = byteBuf.asFloatBuffer();
//		normalBuffer.put(finalNormalsList);
//		normalBuffer.position(0);
		
		indexBuffer = ByteBuffer.allocateDirect(finalIndexList.length);
		indexBuffer.put(finalIndexList);
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
		
		int[] tempIndexListfaces = new int[3];
		List<int[]> faceList = new ArrayList<int[]>();
		
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
            		//Log.i("obj:", "vertexvalues: " + values[1] + "/" + values[2] + "/" + values[3]);
            		tempVertices = new float[3];
            		tempVertices[0] = Float.parseFloat(values[1]);
            		tempVertices[1] = Float.parseFloat(values[2]);
            		tempVertices[2] = Float.parseFloat(values[3]);
            		vertexList.add(tempVertices);
            	}
            	else if(line.startsWith("vt "))
            	{	// texture coordinate
    				// e.g. vt 0.499997 0.250000
            		String[] values = line.split(" ");
            		tempTexcoords = new float[2];
            		tempTexcoords[0] = Float.parseFloat(values[1]);
            		tempTexcoords[1] = Float.parseFloat(values[2]);
            		//Log.i("obj:", "tc: " + tempTexcoords[0] + "/" + tempTexcoords[1]);
            		texcoordsList.add(tempTexcoords);
            	}
            	else if(line.startsWith("vn "))
            	{	// normal
    				// e.g. vn 0.000000 0.000000 -1.000000
            		String[] values = line.split(" ");
            		tempNormals = new float[3];
            		tempNormals[0] = Float.parseFloat(values[1]);
            		tempNormals[1]  = Float.parseFloat(values[2]);
            		tempNormals[2]  = Float.parseFloat(values[3]);
            		//Log.i("obj:", "normal: " + tempNormals[0] + "/" + tempNormals[1] + "/" + tempNormals[2]);
            		normalsList.add(tempNormals);
            	}
            	else if(line.startsWith("f "))
            	{	// face (triangle)
        			// e.g. f 5/1/1 1/2/1 4/3/1
            		// f v/vt/vn
            		String[] values = line.split(" ");
            		
            			for (int i=1; i < 4; i++)
            			{
            				String[] indicesStr = values[i].split("/");
//	            			Log.i("obj","faces: " + indicesStr[0] + indicesStr[1]+indicesStr[2]);
	                		tempIndexListfaces = new int[3];
	            			tempIndexListfaces[0] = Integer.valueOf(indicesStr[0]); //vertex 
	                		tempIndexListfaces[1] = Integer.valueOf(indicesStr[1]); //texture coordinate
	                		tempIndexListfaces[2] = Integer.valueOf(indicesStr[2]); //normal
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
        
        
		String f1 = String.valueOf(faceList.size());
		//Log.i("Facelist size", "facelistsize:  " + f1 );
		int Vpos = 0;
		int TCpos = 0;
		int Npos = 0;
		int Indexpos = 0;
		
        for (int i=0; i < faceList.size(); i++)
        {
        	int[] tempface = faceList.get(i);

        	float[] tempVertex = new float[3];
        	float[] tempTC = new float[2];
        	float[] tempNormal = new float[3];
        	tempVertex = vertexList.get(tempface[0]-1);
        	tempTC = texcoordsList.get(tempface[1]-1);
        	tempNormal = normalsList.get(tempface[2]-1);

        	//Log.i("tempface: ",i + " -> " + tempface[0] +"/"+ tempface[1]+"/"+ tempface[2]);
//        	Log.i("tempVertex ",i + " tV-> " + tempVertex[0] +"/"+ tempVertex[1]+"/"+ tempVertex[2]);
//        	Log.i("tempTC ",i + " tTC -> " + tempTC[0] +"/"+ tempTC[1]);
//        	Log.i("tempNormal ",i + " tN -> " + tempNormal[0] +"/"+ tempNormal[1]+"/"+ tempNormal[2]);
        	
        	finalVertexList = new float[faceList.size()*3];
        	finalTexcoordsList = new float[faceList.size()*2];
        	finalNormalsList = new float[faceList.size()*3];
        	finalIndexList = new byte[faceList.size()*3];
        	finalIndexList[Indexpos] = (byte) tempface[0];
        	Indexpos++;
        	//Log.i("indexlist", String.valueOf(tempface[0]));
//        	Log.i("Listsize V", String.valueOf(vertexList.size()));
//        	Log.i("Listsize TC", String.valueOf(texcoordsList.size()));
//        	Log.i("Listsize N", String.valueOf(normalsList.size()));
        	
        	finalVertexList[Vpos] = tempVertex[0]; 
        	finalVertexList[Vpos + 1] = tempVertex[1];
        	finalVertexList[Vpos + 2] = tempVertex[2];        	
        	finalTexcoordsList[TCpos] = tempTC[0];
        	finalTexcoordsList[TCpos + 1] = tempTC[1];
        	finalNormalsList[Npos] = tempNormal[0];
        	finalNormalsList[Npos + 1] = tempNormal[1];
        	finalNormalsList[Npos + 2] = tempNormal[2];
        	Vpos += 3;
        	Npos += 3;
        	TCpos += 2;
        }
        
	}
	/*
	@Override
	public void draw(GL10 gl) {
		gl.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		//Log.i("Draw","drawing obj-Model");
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
	 *//*
	public void update(GL10 gl, double deltaTime) {
		mTrans = Matrix4x4.mult(Matrix4x4.RotateX((float)(1f * deltaTime)), mTrans);

		gl.glPushMatrix();
		gl.glTranslatef(0, 0, -4);
		gl.glMultMatrixf(mTrans.toFloatArray(), 0);
	}*/
}
