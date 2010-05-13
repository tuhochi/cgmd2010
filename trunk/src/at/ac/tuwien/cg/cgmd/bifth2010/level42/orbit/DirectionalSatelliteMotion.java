package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

/**
 * The Class DirectionalMotion represents a directed linear motion.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class DirectionalSatelliteMotion extends DirectionalMotion
{
	public DirectionalSatelliteMotion()
	{
		super();
	}
	public DirectionalSatelliteMotion(Vector3 startPos,Vector3 directionVec,float speed,Matrix44 basicOrientation)
	{
		super(startPos,directionVec,speed,basicOrientation);
	}
}
