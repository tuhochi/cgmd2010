package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Color4;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Model;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.SceneEntity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager.Material;

/*
# AndroidModel format (*.amo) layout. The whole thing is usually gZipped.
# Entities:
#	- Material
#		A Material is a set of ambient, diffuse and specular colors, a ks and an
#		optional textureFilename; identified by a name
#	- Geometry
#		A Geometry is a set of Vertices (forming Triangles), along with their normals
#		and (optionally) their texcoords. Each Geometry has a Material and is identified by a name
#	- Model
#		A Model is a set of Geometries, identified by a name
#	- SceneEntity
#		A SceneEntity is a set of Models, each having it's own Transformation Matrix. Furthermore
#		the SceneEntity as whole has a Transformation Matrix as well, and is identified by a name

#----------
# Header
#----------
4ndr01dM0d3l				String			Magic Bytes
version						int				The current Version of this Format

#----------
# Materials
#----------
numMaterials				int				Number of Materials in this file
for(numMaterials)
{
	name					String			Name of this Material (must be unique)
	ambient					float[4]		ambient color
	diffuse					float[4]		diffuse color
	specular				float[4]		specular color
	ks						float			specular reflecion coefficient
	textureFilename			String			texture filename, "" if no texture present
}

#----------
# Geometries
#----------
numGeometries				int				Number of Geometries in this file
for(numGeometries)
{
	geometryName			String			Name of this Geometry (must be unique)
	materialName			String			Material of this Geometry
	boundingBoxMin			float[3]		Minimum Point of the bounding box
	boundingBoxMax			float[3]		Maximum Point of the bounding box
	numVertices				int				Number of Vertices of this Geometry
	for(numVertices)	
	{
		vertex				float[3]		A Vertex
	}
	for(numVertices)	
	{
		normal				float[3]		A Normal
	}
	hasTexcoords			boolean			If we have Texcoords
	if(hasTexcoords)
	{
		for(numTexcoords)
		{
			texcoord		float[2]		A Texcoord
		}
	}
}

#----------
# Models
#----------
numModels					int				Number of Models in this file
for(numModels)
{
	modelName				String			Name of this Model (must be unique)
	numGeoms				int				Number of Geometries in this Model
	for(numGeoms)
	{
		geometry			String			A Geometry, identified by its name
	}
}

#----------
# SceneEntities
#----------
numSceneEntities			int				Number of Scene Entities in this file
for(numSceneEntities)
{
	SceneEntityName			String			Name of this SceneEntity (must be unique)
	sceneTransformation		float[16]		Transformation matrix of this SceneEntity (regarding world)
	numModls				int				Number of Models in this SceneEntity
	for(numModls)
	{
		modelTransformation	float[16]		Transformation matrix of this Model (regarding the Scene Entity)
		model				String			A Model, identified by its name
	}
}
*/

public class SceneLoader
{
	private static final int CURRENT_VERSION = 1;
	private static SceneLoader instance = new SceneLoader();
	
	private SceneLoader()
	{
		
	}
	
	public static SceneLoader getInstance()
	{
		return instance;
	}
	
	public Scene readScene(String filename)
	{
		DataInputStream dis = null;
		Scene scene = new Scene();
		Context context = LevelActivity.getInstance();
		MaterialManager materialManager = MaterialManager.getInstance();
		float[] temp3 = new float[3], temp4 = new float[4], temp16 = new float[16];
		
		try
		{
			dis = new DataInputStream(new GZIPInputStream(context.getResources().openRawResource(context.getResources().getIdentifier(filename, "raw", "at.ac.tuwien.cg.cgmd.bifth2010"))));
			
			HashMap<String, Material> materials = new HashMap<String, Material>();
			HashMap<String, Geometry> geometries = new HashMap<String, Geometry>();
			HashMap<String, Model> models = new HashMap<String, Model>();
			
			/*
			 * Header
			 */
			if(!dis.readUTF().equals("4ndr01dM0d3l"))
				throw new IOException("Invalid AndroidModel File: " + filename);
			
			int version = dis.readInt();
			if(version != CURRENT_VERSION)
				throw new IOException("Invalid AndroidModel Version: need " + CURRENT_VERSION + ", found " + version);
			
			/*
			 * Materials
			 */
			int numMaterials = dis.readInt();
			for(int i=0; i<numMaterials; i++)
			{
				String name = dis.readUTF();
				
				readFloatArray(temp4, dis);
				Color4 ambient = new Color4(temp4);
				
				readFloatArray(temp4, dis);
				Color4 diffuse = new Color4(temp4);
				
				readFloatArray(temp4, dis);
				Color4 specular = new Color4(temp4);
			
				float ks = dis.readFloat();
				
				String textureFilename = dis.readUTF();
				
				materials.put(name, materialManager.addMaterial(name, ambient, diffuse, specular, ks, textureFilename));
			}
			
			/*
			 * Geometries
			 */
			int numGeometries = dis.readInt();
			for(int i=0; i<numGeometries; i++)
			{
				String name = dis.readUTF();
				
				String materialName = dis.readUTF();
				Material m = materials.get(materialName);
				if(m==null)
					throw new IOException("Geometry " + name + " specifies an invalid Material " + materialName);
				
				readFloatArray(temp3, dis);
				Vector3 boundingBoxMin = new Vector3(temp3);
				readFloatArray(temp3, dis);
				Vector3 boundingBoxMax = new Vector3(temp3);
				AxisAlignedBox3 boundingBox = new AxisAlignedBox3(boundingBoxMin, boundingBoxMax);
				
				int numVertices = dis.readInt();
				
				float[] vertices = new float[numVertices*3];
				readFloatArray(vertices, dis);

				float[] normals = new float[numVertices*3];
				readFloatArray(normals, dis);
				
				boolean hasTexcoords = dis.readBoolean();
				float[] texcoords = null;
				if(hasTexcoords)
				{
					texcoords = new float[numVertices*2];
					readFloatArray(texcoords, dis);
				}
				
				geometries.put(name, new Geometry(m, arrayToBuffer(vertices), arrayToBuffer(normals), arrayToBuffer(texcoords), boundingBox, numVertices));
			}
			
			/*
			 * Models
			 */
			int numModels = dis.readInt();
			for(int i=0; i<numModels; i++)
			{
				Model m = new Model();
				
				String name = dis.readUTF();
				
				int numGeoms = dis.readInt();
				for(int j=0; j<numGeoms; j++)
				{
					String geometryName = dis.readUTF();
					Geometry geometry = geometries.get(geometryName);
					if(geometry == null)
						throw new IOException("Model " + name + " specifies an invalid Geometry " + geometryName);
					m.add(geometry);
				}
				
				models.put(name, m);
			}
			
			/*
			 * SceneEntities
			 */
			int numSceneEntities = dis.readInt();
			for(int i=0; i<numSceneEntities; i++)
			{
				SceneEntity sceneEntity = new SceneEntity();
				
				String name = dis.readUTF();
				
				readFloatArray(temp16, dis);
				Matrix44 sceneTransformation = new Matrix44(temp16);
				
				int numModls = dis.readInt();
				for(int j=0; j<numModls; j++)
				{
					readFloatArray(temp16, dis);
					Matrix44 modelTransformation = new Matrix44(temp16);
					
					String modelName = dis.readUTF();
					if(!models.containsKey(modelName))
						throw new IOException("SceneEntity " + name + " specifies an invalid Model " + modelName);
					
					//Model gets copied because it holds its own transformation, but still references the same Geometry
					Model m = new Model(models.get(modelName));
					m.setTransformation(modelTransformation);
					sceneEntity.add(m);
				}
				
				sceneEntity.setTransformation(sceneTransformation);
				
				scene.add(sceneEntity);
			}
			
		}
		catch(Throwable t)
		{
			scene = null;
			Log.e(LevelActivity.TAG, "Error when reading Scene:", t);
		}
		finally
		{
			if(dis != null)
				try{dis.close();}catch(IOException e){}
		}
		
		return scene;
	}
	
	private void readFloatArray(float[] arr, DataInputStream dis) throws IOException
	{
		for(int i=0; i<arr.length; i++)
			arr[i] = dis.readFloat();
	}
	
	private FloatBuffer arrayToBuffer(float[] data)
	{
		if(data == null)
			return null;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(data.length*4);
        byteBuffer.order(ByteOrder.nativeOrder());
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(data);
        floatBuffer.position(0);
        return floatBuffer;
	}
}
