package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.glMultMatrixf;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Moveable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

public class SceneEntity implements Moveable,Persistable
{
	private String name;
	private Matrix44 transformation;
	private Matrix44 transformation_temp;
	private final Matrix44 basicOrientation;
	private final ArrayList<Model> models;
	private final AxisAlignedBox3 boundingBox;
	private final Sphere boundingSphere;
	private final Sphere boundingSphereWorld;
	private Motion motion;
	private boolean initialized;
	
	public SceneEntity()
	{
		transformation = new Matrix44();
		transformation_temp = new Matrix44();
		basicOrientation = new Matrix44();
		models = new ArrayList<Model>();
		boundingBox = new AxisAlignedBox3();
		boundingSphere = new Sphere();
		boundingSphereWorld = new Sphere();
		initialized = false;
	}
	
	void init()
	{
		if(!initialized)
		{
			ArrayList<Model> models = this.models;
			int size = models.size();
			for(int i=0; i<size; i++)
				models.get(i).init();
			
			initialized = true;
		}
	}
	
	void deInit()
	{
		initialized = false;
		
		ArrayList<Model> models = this.models;
		int size = models.size();
		for(int i=0; i<size; i++)
			models.get(i).deInit();
	}
	
	public void persist(DataOutputStream dos) throws IOException
	{
		ArrayList<Model> models = this.models;
		int size = models.size();
		for(int i=0; i<size; i++)
			models.get(i).persist(dos);
		transformation.persist(dos);
		
		if(motion != null)
		{
			dos.writeBoolean(true);
			String className = motion.getClass().getName();
			dos.writeUTF(className);
			motion.persist(dos);
		}
		else
			dos.writeBoolean(false);
	}
	
	public void restore(DataInputStream dis) throws IOException
	{
		ArrayList<Model> models = this.models;
		int size = models.size();
		for(int i=0; i<size; i++)
			models.get(i).restore(dis);
		transformation_temp.restore(dis);
		if(dis.readBoolean())
		{
			motion = Motion.restore(dis, dis.readUTF());
			if(motion != null)
				MotionManager.instance.addMotion(motion, this);
		}
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
		basicOrientation.copy(transformation);
		basicOrientation.addTranslate(	-boundingSphereWorld.center.x,
										-boundingSphereWorld.center.y,
										-boundingSphereWorld.center.z);
		return basicOrientation;
	}
}
