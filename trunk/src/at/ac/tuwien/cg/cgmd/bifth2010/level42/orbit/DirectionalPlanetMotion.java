package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;

/**
 * The Class DirectionalMotion represents a directed linear motion.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class DirectionalPlanetMotion extends DirectionalMotion
{
	public DirectionalPlanetMotion()
	{
		super();
	}
	public DirectionalPlanetMotion(Vector3 startPos,Vector3 directionVec,float speed,Matrix44 basicOrientation)
	{
		super(startPos,directionVec,speed,basicOrientation);
	}
}
