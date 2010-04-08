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
import at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit.Moveable;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Pair;

//static imports
import static android.opengl.GLES10.*;

public class Model implements Moveable
{
	private Matrix44 transformation;
	private Matrix44 transformation_temp;
	private final ArrayList<Geometry> geometries;
	private final AxisAlignedBox3 boundingBox;
	protected final Sphere boundingSphere;
	private final Sphere boundingSphereWorld;
	private ArrayList<Pair<Vector3, Model>> distances;
	private Motion motion;
	
	public Model()
	{
		transformation = new Matrix44();
		geometries = new ArrayList<Geometry>();
		boundingBox = new AxisAlignedBox3();
		boundingSphere = new Sphere();
		boundingSphereWorld = new Sphere();
		distances = new ArrayList<Pair<Vector3,Model>>();
	}
	
	public void persist(DataOutputStream dos) throws IOException
	{
		ArrayList<Geometry> geometries = this.geometries;
		int size = geometries.size();
		for(int i=0; i<size; i++)
			geometries.get(i).persist(dos);
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
		ArrayList<Geometry> geometries = this.geometries;
		int size = geometries.size();
		for(int i=0; i<size; i++)
			geometries.get(i).restore(dis);
		transformation_temp.restore(dis);
		if(dis.readBoolean())
		{
			motion = Motion.restore(dis, dis.readUTF());
			if(motion != null)
				MotionManager.instance.addMotion(motion, this);
		}
	}
	
	public Model(Model other)
	{
		geometries = new ArrayList<Geometry>();
		transformation = new Matrix44(other.transformation);
		boundingBox = new AxisAlignedBox3(other.boundingBox);
		boundingSphere = new Sphere(other.boundingSphere);
		boundingSphereWorld = new Sphere(other.boundingSphereWorld);
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
	
	public void update()
	{
		int numGeoms = geometries.size();
		for(int i=0; i<numGeoms; i++)
			geometries.get(i).update();

		transformation.copy(transformation_temp);
		transformation.transformSphere(boundingSphere, boundingSphereWorld);
	}
	
	public AxisAlignedBox3 getBoundingBox()
	{
		return boundingBox;
	}
	
	public Sphere getBoundingSphereWorld()
	{
		return boundingSphereWorld;
	}
	
	public Matrix44 getTransformation()
	{
		return transformation_temp;
	}
	
	public void setTransformation(Matrix44 transformation)
	{
		this.transformation_temp = transformation;
	}
	
	public void add(Geometry geometry)
	{
		geometries.add(geometry);
		boundingBox.include(geometry.getBoundingBox());
		boundingSphere.include(geometry.getBoundingSphere());
	}
	
	public void setDistances(ArrayList<Pair<Vector3, Model>> distances)
	{
		this.distances = distances;
	}

	@Override
	public Motion getMotion() {
		return motion;
	}

	@Override
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
