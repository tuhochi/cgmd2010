package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

public abstract class Motion implements Persistable
{
	/*
	 * Subclasses of Motion *need* a default constructor!
	 */
	public abstract void update(float dt);
	public abstract Matrix44 getTransform();
	public abstract void setSatTrans(SatelliteTransformation satTrans);
	public abstract SatelliteTransformation getSatTrans();
	public abstract Vector3 getCurrDirectionVec();
	public abstract void persist(DataOutputStream dos) throws IOException;
	public abstract void restore(DataInputStream dis) throws IOException;
	
	public static Motion restore(DataInputStream dis, String className)
	{
		try
		{
			Class<?> c = Class.forName(className);
			Motion m = (Motion)c.newInstance();
			m.restore(dis);
			/*
			 * TODO: uncomment the following line!
			 */
			return m;
		}
		catch (Throwable t)
		{
			Log.e(LevelActivity.TAG, "Could not restore Motion (no default constructor?)", t);
		}
		return null;
	}
}
