package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;


/**
 * This class represents an object motion
 * Subclasses of Motion *need* a default constructor!
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public abstract class Motion implements Persistable
{	
	boolean isInsidePlanet = false;
	boolean filterPlanetColl = false;
	
	/**
	 * Do the next iteration step 
	 * @param dt delta time between frames for a frame-independent motion
	 */
	public abstract void update(float dt);
	
	/**
	 * Gets the generated transformation matrix 
	 * @return the generated transformation matrix 
	 */
	public abstract Matrix44 getTransform();
	
	/**
	 * Sets the transformation matrix 
	 * @param transform the new transformation matrix
	 */
	public abstract void setTransform(Matrix44 transform);
	
	/**
	 * Gets the satellite transformation
	 * @return the satellite transformation
	 */
	public abstract SatelliteTransformation getSatTrans();
	
	/**
	 * Sets the satellite transformation
	 * @param satTrans the new satellite transformation
	 */
	public abstract void setSatTrans(SatelliteTransformation satTrans);
	
	/**
	 * Get an approximation for the direction vector at the current position
	 * @return approximation for the direction vector
	 */
	public abstract Vector3 getCurrDirectionVec();
	
	/**
	 * Gets the motion speed
	 * @return the speed of the motion
	 */
	public abstract float getSpeed();
	
	/**
	 * Dynamic change of the motion by the given push vector
	 * @param pushVec the change of the motion
	 */
	public abstract void morph(Vector3 pushVec);
	
	public boolean isInsidePlanet(){
		return isInsidePlanet;		
	}
	
	public void setInsidePlanet(boolean isInsidePlanet){
		this.isInsidePlanet = isInsidePlanet;
	}
	
	public boolean getFilterPlanetColl(){
		return filterPlanetColl;		
	}
	public void setFilterPlanetColl(boolean filterPlanetColl){
		this.filterPlanetColl = filterPlanetColl;		
	}
	
	public abstract Matrix44 getBasicOrientation();
	public abstract void setBasicOrientation(Matrix44 basicOrientation);
	
	public abstract void persist(DataOutputStream dos) throws IOException;
	public abstract void restore(DataInputStream dis) throws IOException;

	/**
	 * Restore a Motion
	 * @param dis the data input stream
	 * @param className the name of the class that should get restored
	 * @return the restored motion
	 */
	public static Motion restore(DataInputStream dis, String className)
	{
		try
		{
			Class<?> c = Class.forName(className);
			Motion m = (Motion)c.newInstance();
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
