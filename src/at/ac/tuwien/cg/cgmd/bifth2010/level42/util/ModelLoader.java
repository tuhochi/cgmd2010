package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Model;

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

4ndr01dM0d3l				String			Magic Bytes

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
	hasTexture				boolean			If this Geometry has a Texture
	numVertices				int				Number of Vertices of this Geometry
	for(numVertices)	
	{
		vertex				float[3]		A Vertex
	}
	for(numVertices)	
	{
		normal				float[3]		A Normal
	}
	if(hasTexture)
	{
		for(numVertices)
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
	numGeometries			int				Number of Geometries in this Model
	for(numGeometries)
	{
		geometry			String			A Geometry, identified by its name
	}
}

#----------
# Scene entities
#----------
numSceneEntities			int				Number of Scene Entities in this file
for(numSceneEntities)
{
	SceneEntityName			String			Name of this SceneEntity (must be unique)
	transformation			float[16]		Transformation matrix of this SceneEntity (regarding world)
	numModels				int				Number of Models in this SceneEntity
	for(numModels)
	{
		transformation		float[16]		Transformation matrix of this Model (regarding the Scene Entity)
		model				String			A Model, identified by its name
	}
}
*/

public class ModelLoader
{
	private static ModelLoader instance = new ModelLoader();
	
	private ModelLoader()
	{
		
	}
	
	public static ModelLoader getInstance()
	{
		return instance;
	}
	
	public Model readModel(String filename)
	{
		return null;
	}
	
}
