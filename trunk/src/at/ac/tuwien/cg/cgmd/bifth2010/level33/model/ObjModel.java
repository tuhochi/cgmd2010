package at.ac.tuwien.cg.cgmd.bifth2010.level33.model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry.Type;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SceneGraph;

/**
 * this Class represent a primitive ObjModel and can read/write them self to the File System
 */
public class ObjModel implements Serializable{

	private static final long serialVersionUID = 1L;
	float vertices[];
	int vertexCount = 0;

	float colors[];
	int colorCount = 0;

	float texCoords[];
	int texCount = 0;

	float normals[];
	int normalCount = 0;
	
	int[] geometryOffset;
	int numVertices=0;;
	Type[] type;
	
	/**
	 * this constructor init the ObjModel
	 * @param type Geometry Type
	 * @param geometryOffset Geometry-Group Offset 
	 * @param vertices Vertices Array
	 * @param colors Colors Array
	 * @param texCoords Textures Array
	 * @param normals Normals Array
	 * @param numVertices 
	 */
	public ObjModel(Type[] type, int[] geometryOffset, float[] vertices,
			float[] colors, float[] texCoords, float[] normals, int numVertices) {
		this.type=type;
		this.geometryOffset=geometryOffset;
		
		this.vertices=vertices;
		this.colors=colors;
		this.texCoords=texCoords;
		this.normals=normals;

		this.numVertices = numVertices;
	}

	/**
	 * this method write this ObjModel as a Stream to the given File Path 
	 * need permission: android.permission.WRITE_EXTERNAL_STORAGE
	 * @param file is the desired File Path
	 */
	public void write(String file)
	{
		try {
		
			FileOutputStream fos = new FileOutputStream(file,true);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			fos.close();
			Log.d("write ObjModel to:", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * this method read the stored ObjModel from the File Path
	 * @param fileName File Name and Path
	 * @param context the main Context eq.: GameView
	 * @return the stored ObjModel
	 */
	public static ObjModel read(String fileName, Context context)
	{
		ObjModel model = null;
    	try {
    		InputStream fis = new FileInputStream(fileName);// context.getAssets().open(fileName);
			ObjectInputStream stream = new ObjectInputStream(fis);
			model = (ObjModel)stream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}

	public static ObjModel read(int resource, LevelActivity activity) {
		ObjModel model = null;
    	try {
    		
    		InputStream fis = SceneGraph.activity.getResources().openRawResource(resource);
			ObjectInputStream stream = new ObjectInputStream(fis);
			model = (ObjModel)stream.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return model;
	}

}
