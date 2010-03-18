package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

//static imports
import static android.opengl.GLES10.*;

public class Model
{
	Matrix44 transformation;
	ArrayList<Geometry> geometries;
	AxisAlignedBox3 boundingBox;
	
	public Model()
	{
		transformation = new Matrix44();
		geometries = new ArrayList<Geometry>();
		boundingBox = new AxisAlignedBox3();
	}
	
	public Model(Model other)
	{
		transformation = new Matrix44(other.transformation);
		boundingBox = new AxisAlignedBox3(other.boundingBox);
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
}
