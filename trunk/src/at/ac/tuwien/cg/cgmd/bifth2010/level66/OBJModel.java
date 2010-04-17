package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.R.string;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
/**
 * Class to load an .obj model
 * @author MaMa
 *
 */
@SuppressWarnings("unchecked")
public class OBJModel implements Serializable
{
    /**
	 * Auto Generated serialVersionID
	 */
	private static final long serialVersionUID = -762515591771882029L;
	public List mVertices = new ArrayList();
	public List mTextureCoords = new ArrayList();
    public List mNormals = new ArrayList();
    public List<Short> mIndices = new ArrayList<Short>();
    
    private int mNumVertices;
	
	public OBJModel()
	{}	
	
	public OBJModel(List vertices, List texCoords, List normals, List<Short> indices)
	{
		mVertices = vertices;
		mTextureCoords = texCoords;
		mNormals = normals;
		mIndices = indices;
		mNumVertices = indices.size();
	}
	
	public void write(String file)
	{
		try {
			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static OBJModel read(String fileName, Context context)
	{
		OBJModel model = null;
    	try {
    		//InputStream fis = context.openRawResource(R.raw.l66_baum);
    		InputStream fis = context.getAssets().open(fileName);
			ObjectInputStream stream = new ObjectInputStream(fis);
			model = (OBJModel)stream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	

	public List getVertices() {
		return mVertices;
	}

	public List getTextureCoords() {
		return mTextureCoords;
	}

	public List getNormals() {
		return mNormals;
	}

	public List<Short> getIndices() {
		return mIndices;
	}

	public int getNumVertices() {
		return mNumVertices;
	}
	
	public static OBJModel load(String fileName, Context context)
	{

        List<Short> indexList = new ArrayList<Short>();
        List vertexList = new ArrayList();
        List texCoordList = new ArrayList();
        List normalsList = new ArrayList();
       
        try {
        	InputStream is = context.getResources().openRawResource(R.raw.l66_baum); 			
            is.available();
            InputStreamReader isr = new InputStreamReader(is);            
            BufferedReader textReader = new BufferedReader(isr);
            List vertices = new LinkedList();
            List texCoords = new LinkedList();
            List normals = new LinkedList();
            List<short[]> indices = new LinkedList<short[]>();
            String line = textReader.readLine();
            while (line != null)
            {
            	if(line.startsWith("v "))
            	{
            		String[] values = line.split(" ");
            		float x = Float.parseFloat(values[2]);
            		float y = Float.parseFloat(values[3]);
            		float z = Float.parseFloat(values[4]);
            		//FIXME: KA was hier falsch is 
            		vertices.add(x);
            		vertices.add(y);
            		vertices.add(z);
            	}
            	else if(line.startsWith("vt "))
            	{
            		String[] values = line.split(" ");
            		float x = Float.parseFloat(values[2]);
            		float y = Float.parseFloat(values[3]);
            		texCoords.add(x);
            		texCoords.add(y);
            	}
            	else if(line.startsWith("vn "))
            	{
            		String[] values = line.split(" ");
            		float x = Float.parseFloat(values[2]);
            		float y = Float.parseFloat(values[3]);
            		float z = Float.parseFloat(values[4]);
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
                		short[] index = {vertIndex, texIndex, normalIndex};
                		indices.add(index);
            		}
            	}
            	
            	line = textReader.readLine();
            }
            textReader.close();
            isr.close();
            is.close();
            
            int vertex = 0;
            
            while(vertex < indices.size())
            {
            	vertexList.add(vertices.get(indices.get(vertex)[0] - 1));
            	texCoordList.add(texCoords.get(indices.get(vertex)[1] - 1));
            	normalsList.add(normals.get(indices.get(vertex)[2] - 1));
            	indexList.add((short)vertex);
            	vertex++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        OBJModel retModel = new OBJModel(vertexList, texCoordList, normalsList, indexList);
        return retModel;
	}
}
