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
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

/**
 * The Class SceneEntity.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class SceneEntity implements Movable,Persistable
{
	
	/** The name. */
	private String name;
	
	/** The transformation used by opengl. */
	private Matrix44 transformation;
	
	/** The transformation the logic thread writes to. is written to transformation in update(). */
	private Matrix44 transformation_temp;
	
	/** The basic orientation (needed for orbit). */
	private final Matrix44 basicOrientation;
	
	/** The bounding box in model space. */
	private final AxisAlignedBox3 boundingBox;
	
	/** The bounding sphere in model space. */
	private final Sphere boundingSphere;
	
	/** The bounding sphere world space, is updated in each update() call. */
	private final Sphere boundingSphereWorld;
	
	/** The motion. */
	private Motion motion;
	
	/** if this is initialized. */
	private boolean initialized;
	
	/** The models. */
	public final ArrayList<Model> models;
	
	/**
	 * Instantiates a new scene entity.
	 */
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
	
	/**
	 * Inits the Scene entity
	 */
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
	
	/**
	 * De init the Scene entity
	 */
	void deInit()
	{
		initialized = false;
		
		ArrayList<Model> models = this.models;
		int size = models.size();
		for(int i=0; i<size; i++)
			models.get(i).deInit();
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#persist(java.io.DataOutputStream)
	 */
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
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#restore(java.io.DataInputStream)
	 */
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
		else
			motion = null;
	}
	
	/**
	 * Render.
	 *
	 * @param rendermode the rendermode
	 */
	public void render(int rendermode)
	{
		glPushMatrix();
		glMultMatrixf(transformation.getArray16(), 0);
		int numModels = models.size();
		for(int i=0; i<numModels; i++)
			models.get(i).render(rendermode);
		glPopMatrix();
	}
	
	/**
	 * Update.
	 */
	public void update()
	{		
		int numModels = models.size();
		for(int i=0; i<numModels; i++)
			models.get(i).update();

		transformation.copy(transformation_temp);
		transformation.transformSphere(boundingSphere, boundingSphereWorld);
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#getTransformation()
	 */
	public Matrix44 getTransformation()
	{
		return transformation_temp;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#setTransformation(at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44)
	 */
	public void setTransformation(Matrix44 transformation)
	{
		this.transformation_temp = transformation;
	}
	
	/**
	 * Adds a Model to this SceneEntity
	 *
	 * @param model the model
	 */
	public void add(Model model)
	{
		models.add(model);
		boundingBox.include(model.getBoundingBox());
		boundingSphere.include(model.boundingSphere);
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#getName()
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Gets the bounding box.
	 *
	 * @return the bounding box
	 */
	public AxisAlignedBox3 getBoundingBox()
	{
		return boundingBox;
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#getBoundingSphereWorld()
	 */
	public Sphere getBoundingSphereWorld()
	{
		return boundingSphereWorld;
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#getMotion()
	 */
	public Motion getMotion()
	{
		return motion;
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#setMotion(at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion)
	 */
	public void setMotion(Motion motion)
	{
		this.motion = motion;
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#getBasicOrientation()
	 */
	@Override
	public Matrix44 getBasicOrientation()
	{
		basicOrientation.copy(transformation);
		basicOrientation.addTranslate(	-boundingSphereWorld.center.x,
										-boundingSphereWorld.center.y,
										-boundingSphereWorld.center.z);
		return basicOrientation;
	}
}
