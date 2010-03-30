package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.glMultMatrixf;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;

public class SceneEntity
{
	private String name;
	private Matrix44 transformation;
	private final ArrayList<Model> models;
	private final AxisAlignedBox3 boundingBox;
	private final Sphere boundingSphere;
	
	public SceneEntity()
	{
		transformation = new Matrix44();
		models = new ArrayList<Model>();
		boundingBox = new AxisAlignedBox3();
		boundingSphere = new Sphere();
	}
	
	public void render(int rendermode)
	{
		glPushMatrix();
		glMultMatrixf(transformation.getArray16(), 0);
		int numModels = models.size();
		for(int i=0; i<numModels; i++)
			models.get(i).render(rendermode);
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
	
	public void add(Model model)
	{
		models.add(model);
		boundingBox.include(model.getBoundingBox());
		boundingSphere.include(model.getBoundingSphere());
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	public AxisAlignedBox3 getBoundingBox()
	{
		return boundingBox;
	}
	
	public Sphere getBoundingSphere()
	{
		return boundingSphere;
	}
}
