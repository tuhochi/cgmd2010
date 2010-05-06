package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

/**
 * The Class Constants.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Constants
{

	/** The Constant TWOPI = Two times PI. */
	public static final float TWOPI = (float)(Math.PI*2);
	public static final float PIHALF = (float)(Math.PI/2);
	/** A few more Constant Vectors. */
	public static final Vector3 X_AXIS = new Vector3(1,0,0), 
								Y_AXIS = new Vector3(0,1,0), 
								Z_AXIS = new Vector3(0,0,1),
								ZERO_VEC = new Vector3(0,0,0),
								DUMMY_VARIATION_VEC = new Vector3(0.00f,0.00f,0.00f),
								DUMMY_INIT_VEC = new Vector3(0,0,0.1f);
}
