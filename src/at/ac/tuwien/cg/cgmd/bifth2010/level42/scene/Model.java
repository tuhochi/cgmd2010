package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.AxisAlignedBox3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.MotionManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.MaterialManager.Material;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

//static imports
import static android.opengl.GLES10.*;

/**
 * The Class Model.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Model implements Movable,Persistable
{
	
	/** The material manager. */
	private final MaterialManager materialManager = MaterialManager.instance;

	/** The transformation used by opengl. */
	private Matrix44 transformation;

	/** The transformation the logic thread writes to. is written to transformation in update(). */
	private Matrix44 transformation_temp;

	/** The basic orientation (needed for orbit). */
	private final Matrix44 basicOrientation;
	
	/** The bounding box in model space. */
	private final AxisAlignedBox3 boundingBox;
	
	/** The bounding sphere in model space. */
	protected final Sphere boundingSphere;
	
	/** The bounding sphere in world space. */
	private final Sphere boundingSphereWorld;
	
	/** The motion. */
	private Motion motion;

	/** if this is initialized. */
	private boolean initialized;
	
	/** The geometries. */
	private final ArrayList<Geometry> geometries;
	
	/** The materials (in the same order as the corresponding geometries). */
	private final ArrayList<Material> materials;
	
	/** The current position */
	private final Vector3 currentPos;
	
	/**
	 * Instantiates a new model.
	 */
	public Model()
	{
		transformation = new Matrix44();
		basicOrientation = new Matrix44();
		geometries = new ArrayList<Geometry>();
		materials = new ArrayList<Material>();
		boundingBox = new AxisAlignedBox3();
		boundingSphere = new Sphere();
		boundingSphereWorld = new Sphere();
		initialized = false;
		currentPos = new Vector3();
	}
	
	/**
	 * Copy Constructor
	 *
	 * @param other the other
	 */
	public Model(Model other)
	{
		geometries = new ArrayList<Geometry>();
		materials = new ArrayList<Material>();
		basicOrientation = new Matrix44();
		transformation = new Matrix44(other.transformation);
		boundingBox = new AxisAlignedBox3(other.boundingBox);
		boundingSphere = new Sphere(other.boundingSphere);
		boundingSphereWorld = new Sphere(other.boundingSphereWorld);
		currentPos = new Vector3();
		
		int numGeoms = other.geometries.size();
		
		for(int i=0; i<numGeoms; i++)
		{
			geometries.add(other.geometries.get(i));
			materials.add(other.materials.get(i));
		}
	}
	
	/**
	 * Inits this Model
	 */
	void init()
	{
		if(!initialized)
		{
			ArrayList<Geometry> geometries = this.geometries;
			int size = geometries.size();
			for(int i=0; i<size; i++)
			{
				geometries.get(i).init();
				materials.get(i).init();
			}
			
			initialized = true;
		}
	}
	
	/**
	 * De-init this Model
	 */
	void deInit()
	{
		initialized = false;
		
		ArrayList<Geometry> geometries = this.geometries;
		int size = geometries.size();
		for(int i=0; i<size; i++)
		{
			geometries.get(i).deInit();
			materials.get(i).deInit();
		}
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#persist(java.io.DataOutputStream)
	 */
	public void persist(DataOutputStream dos) throws IOException
	{
		ArrayList<Geometry> geometries = this.geometries;
		int size = geometries.size();
		for(int i=0; i<size; i++)
		{
			geometries.get(i).persist(dos);
			materials.get(i).persist(dos);
		}
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
		ArrayList<Geometry> geometries = this.geometries;
		int size = geometries.size();
		for(int i=0; i<size; i++)
		{
			geometries.get(i).restore(dis);
			materials.get(i).restore(dis);
		}
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
		int numGeoms = geometries.size();
		for(int i=0; i<numGeoms; i++)
		{
			materialManager.bindMaterial(materials.get(i));
			geometries.get(i).render(rendermode);
		}
		glPopMatrix();
	}
	
	/**
	 * Update.
	 */
	public void update()
	{
		int numGeoms = geometries.size();
		for(int i=0; i<numGeoms; i++)
			geometries.get(i).update();

		transformation.copy(transformation_temp);
		transformation.transformSphere(boundingSphere, boundingSphereWorld);
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
	 * Adds a Geometry and its Material to the Model
	 *
	 * @param geometry the geometry
	 * @param material the material
	 */
	public void add(Geometry geometry, Material material)
	{
		geometries.add(geometry);
		materials.add(material);
		boundingBox.include(geometry.getBoundingBox());
		boundingSphere.include(geometry.getBoundingSphere());
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#getMotion()
	 */
	@Override
	public Motion getMotion()
	{
		return motion;
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#setMotion(at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Motion)
	 */
	@Override
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
		basicOrientation.copy(transformation_temp);
		basicOrientation.addTranslate(	-basicOrientation.m[0][3],
										-basicOrientation.m[1][3],
										-basicOrientation.m[2][3]);
		return basicOrientation;
	}

	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Movable#getName()
	 */
	@Override
	public String getName()
	{
		//TODO hack
		return "";
	}
	
	@Override
	public Vector3 getCurrentPosition() {
		currentPos.set( transformation.m[0][3],
						transformation.m[1][3],
						transformation.m[2][3]);
		
		return currentPos;
	}
}
