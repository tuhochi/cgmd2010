package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Pair;

//static imports
import static android.opengl.GLES10.*;

public class Model
{
	Matrix44 transformation;
	ArrayList<Geometry> geometries;
	AxisAlignedBox3 boundingBox;
	ArrayList<Pair<Vector3, Model>> distances;
	
	public Model()
	{
		transformation = new Matrix44();
		geometries = new ArrayList<Geometry>();
		boundingBox = new AxisAlignedBox3();
		distances = new ArrayList<Pair<Vector3,Model>>();
	}
	
	public Model(Model other)
	{
		geometries = new ArrayList<Geometry>();
		transformation = new Matrix44(other.transformation);
		boundingBox = new AxisAlignedBox3(other.boundingBox);
		distances = other.distances;
		int numGeoms = other.geometries.size();
		for(int i=0; i<numGeoms; i++)
			geometries.add(other.geometries.get(i));
	}
	
	public void render(int rendermode)
	{
		glPushMatrix();
		glMultMatrixf(transformation.getArray16(), 0);
		int numGeoms = geometries.size();
		for(int i=0; i<numGeoms; i++)
			geometries.get(i).render(rendermode);
		glPopMatrix();
	}
	
	public Matrix44 getTransformation()
	{
		return transformation;
	}
	public void setTransformation(Matrix44 transformation)
	{
		this.transformation = transformation;
	}
	
	public void add(Geometry geometry)
	{
		geometries.add(geometry);
		boundingBox.include(geometry.boundingBox);
	}
	
	public void setDistances(ArrayList<Pair<Vector3, Model>> distances)
	{
		this.distances = distances;
	}
}
