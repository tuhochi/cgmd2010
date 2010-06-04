package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

/**
 * Class SatelliteTransformation
 * Represents a rotational behavior of an object around his own axis
 * 
 * @author Alex Druml
 * @author Lukas Roessler
 */
public abstract class SatelliteTransformation implements Persistable{
	
	/**
	 * Do the next iteration step 
	 * @param dt delta time between frames for a frame-independent motion
	 */
	public abstract void update(float dt);

	/**
	 * Gets the transformation matrix of the rotation
	 * @return the current transformation matrix
	 */
	public abstract Matrix44 getTransform();
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#persist(java.io.DataOutputStream)
	 */
	public abstract void persist(DataOutputStream dos) throws IOException;
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#restore(java.io.DataInputStream)
	 */
	public abstract void restore(DataInputStream dis) throws IOException;
	
	
	/**
	 * Gets the basic orientation of the object
	 * @return the basic orientation as matrix
	 */
	public abstract Matrix44 getBasicOrientation();

	/**
	 * Sets the basic orientation for the satellite transformation - important
	 * when changing the transformation
	 * @param orientation the new orientation
	 */
	public abstract void setBasicOrientation(Matrix44 orientation);
	
	/**
	 * Reset the rotation (parameter)
	 */
	public abstract void reset();

	/**
	 * Restore a satellite transformation
	 * @param dis the data input stream
	 * @param className the name of the class that should get restored
	 * @return the restored motion
	 */
	public static SatelliteTransformation restore(DataInputStream dis, String className)
	{
		try
		{
			Class<?> c = Class.forName(className);
			SatelliteTransformation m = (SatelliteTransformation)c.newInstance();
			m.restore(dis);
			return m;
		}
		catch (Throwable t)
		{
			Log.e(LevelActivity.TAG, "Could not restore Motion (no default constructor?)", t);
		}
		return null;
	}
}
