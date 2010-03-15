package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.io.BufferedReader;
import java.io.FileInputStream;
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

import android.content.Context;


public class OBJModel implements Serializable
{
    /**
	 * Auto Generated serialVersionID
	 */
	private static final long serialVersionUID = -762515591771882029L;
	private List<Vector3> mVertices = new ArrayList<Vector3>();
	private List<Vector2> mTextureCoords = new ArrayList<Vector2>();
    private List<Vector3> mNormals = new ArrayList<Vector3>();
    private List<Short> mIndices = new ArrayList<Short>();
    
    private int mNumVertices;
	
	public OBJModel()
	{}	
	
	public OBJModel(List<Vector3> vertices, List<Vector2> texCoords, List<Vector3> normals, List<Short> indices)
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
    		InputStream fis = context.getAssets().open(fileName);
			ObjectInputStream stream = new ObjectInputStream(fis);
			model = (OBJModel)stream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}
	
    public List<Vector3> getVertices() {
		return mVertices;
	}

	public List<Vector2> getTextureCoords() {
		return mTextureCoords;
	}

	public List<Vector3> getNormals() {
		return mNormals;
	}

	public List<Short> getIndices() {
		return mIndices;
	}

	public int getNumVertices() {
		return mNumVertices;
	}
	
	public static OBJModel load(String fileName, Context context )
	{

        List<Short> indexList = new ArrayList<Short>();
        List<Vector3> vertexList = new ArrayList<Vector3>();
        List<Vector2> texCoordList = new ArrayList<Vector2>();
        List<Vector3> normalsList = new ArrayList<Vector3>();
       
        try {
            InputStream is = context.getAssets().open(fileName);
            is.available();
            InputStreamReader isr = new InputStreamReader(is);            
            BufferedReader textReader = new BufferedReader(isr);
            List<Vector3> vertices = new LinkedList<Vector3>();
            List<Vector2> texCoords = new LinkedList<Vector2>();
            List<Vector3> normals = new LinkedList<Vector3>();
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
            		vertices.add(new Vector3(x,y,z));
            	}
            	else if(line.startsWith("vt "))
            	{
            		String[] values = line.split(" ");
            		float x = Float.parseFloat(values[2]);
            		float y = Float.parseFloat(values[3]);
            		texCoords.add(new Vector2(x,y));
            	}
            	else if(line.startsWith("vn "))
            	{
            		String[] values = line.split(" ");
            		float x = Float.parseFloat(values[2]);
            		float y = Float.parseFloat(values[3]);
            		float z = Float.parseFloat(values[4]);
            		normals.add(new Vector3(x,y,z));
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
