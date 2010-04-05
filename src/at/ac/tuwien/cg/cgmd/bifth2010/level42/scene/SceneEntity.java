package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.glMultMatrixf;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import java.util.ArrayList;

import android.text.method.MovementMethod;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Moveable;

public class SceneEntity implements Moveable
{
	private String name;
	private Matrix44 transformation;
	private Matrix44 transformation_temp;
	private final ArrayList<Model> models;
	private final AxisAlignedBox3 boundingBox;
	private final Sphere boundingSphere;
	private final Sphere boundingSphereWorld;
	private Motion motion;
	
	public SceneEntity()
	{
		transformation = new Matrix44();
		transformation_temp = new Matrix44();
		models = new ArrayList<Model>();
		boundingBox = new AxisAlignedBox3();
		boundingSphere = new Sphere();
		boundingSphereWorld = new Sphere();
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
	
	public void update()
	{		
		int numModels = models.size();
		for(int i=0; i<numModels; i++)
			models.get(i).update();

		transformation.copy(transformation_temp);
		transformation.transformSphere(boundingSphere, boundingSphereWorld);
	}
	
	public Matrix44 getTransformation()
	{
		return transformation_temp;
	}
	public void setTransformation(Matrix44 transformation)
	{
		this.transformation_temp = transformation;
	}
	
	public void add(Model model)
	{
		models.add(model);
		boundingBox.include(model.getBoundingBox());
		boundingSphere.include(model.boundingSphere);
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
	
	public Sphere getBoundingSphereWorld()
	{
		return boundingSphereWorld;
	}

	public Motion getMotion() {
		return motion;
	}

	public void setMotion(Motion motion) {
		this.motion = motion;
	}

	@Override
	public Matrix44 getBasicOrientation() {
		transformation_temp.addTranslate(	-boundingSphereWorld.center.x,
											-boundingSphereWorld.center.y,
											-boundingSphereWorld.center.z);
		return transformation_temp;
	}
}
