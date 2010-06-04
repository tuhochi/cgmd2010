package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
/**
 * This Class represents the configuration for the level 42
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Config
{
	public static boolean GLES11 = false;

	public static long FPS_UPDATE_INTERVAL = 1000;
	public static long GAMETIME_UPDATE_INTERVAL = 1000;
	public static long GAMETIME_WAIT_AFTER_COMPLETE = 2000;
	
	public static String LEVELNAME = "l42_orbit";
	
	// bounding sphere stuff
	/**
	 * Determines how many degrees are between the control points of the bounding sphere.
	 * Must be a divisor of 180.
	 * Higher value = faster rendering, uglier sphere
	 * Formula for calculating the number of faces: 
	 * nrOfFaces = ((360*180)/(BOUNDING_SPHERE_SPACING^2))*2
	 * so: BOUNDING_SPHERE_SPACING = 20 -> 324 Faces
	 */
	public static int BOUNDING_SPHERE_SPACING = 20;
	
	// menu settings
	public static boolean SHOW_SCENEENTITY_BOUNDING_SPHERES = false;
	public static boolean SHOW_MODEL_BOUNDING_SPHERES = false;
	public static boolean EASY_MODE = false;
	public static boolean VIBRATE = true;
	public static int LOGLEVEL = Log.ERROR+1;
	
	/** the total level gametime in ms */
	public static long GAMETIME = 120000;
	
	/** UI */

	/** 0.5f = half the smaller side of the screen */
	public static final float MAX_FORCE_VIS_SIZE = 0.50f;
	
	/** (PI/2)/1000.0f = 180° per second */
	public static final float FORCE_VIS_ROTATION_SPEED = Constants.PIHALF/1000.0f;
	
	/** How long the Vibrator should fire on full power */
	public static final float MAX_VIBRATION_LENGTH = 250;
	
	/** GAMEPLAY */
	
	/** the maximum length of the force vector (in units) */
	public static int MAX_FORCE = 25;
	
	/** how long a force cycle 0->full->0 takes (ms) */
	public static int LONG_PRESS_CYCLE_DURATION = 800;
	
	/** The Constant TOUCH_DEADZONE. */
	public static final int TOUCH_DEADZONE = 30;

	public static final float CAM_DISTANCE = 30f;
	
	/** PHYSICS / UNIVERSE. */

	/** center position of the universe / orbits */
	public static Vector3 UNIVERSE_CENTER = new Vector3(0,0,0);
	
	/** entityname for the planet */
	public static String PLANET_NAME = "GoldPlanet";
	
	public static String SKYSPHERE_NAME = "Skysphere";
	
	/** prefix for the satellite entities */
	public static String SATELLITE_PREFIX = "Satellite_";
	
	/** defines the maximum of the orbit main axis */
	public static float UNIVERSE_CENTERLENGTH_LIMIT = 24;
	
	/** defines the maximum of the orbit secondary axis */
	public static float UNIVERSE_DIRLENGTH_LIMIT = 24;
	
	/** defines the maximum of the orbit main axis */
	public static float FORCEFIELD_CENTERLENGTH_LIMIT = 11;
	
	/** defines the maximum of the orbit secondary axis */
	public static float FORCEFIELD_DIRLENGTH_LIMIT = 11;
	
	public static float FORCEFIELD_NEW_CENTERLENGTH = 15;
	public static float FORCEFIELD_NEW_DIRLENGTH = 15;
	
	public static float FORCEFIELD_CENTERLENGTH_SCALESPEED = 25;
	public static float FORCEFIELD_DIRLENGTH_SCALESPEED = 25;
	
	/** defines the maximum relative speed for satellites */
	public static float UNIVERSE_SPEED_LIMIT = 7.5f;
	
	public static float ORBIT_TEMP_MAXSPEED_FAC = 3;
	public static float ORBIT_MIN_SPEED = 0.5f;

	/** ratio between speed and rotation angle */
	public static float SATELLITE_SPEEDROTA_RATIO = 4f;
	
	/** ratio between speed and rotation angle */
	public static float INTERSATELLITE_SPEEDROTA_RATIO = 6f;
	
	/** the required penetration depth on collisions */
	public static float COLLISION_PENETRATION_DEPTH = 0.2f;
	
	/** minimal force to change the motion into a directional motion */
	public static float MIN_STRENGTH_FOR_DIRECTIONAL = 12f;
	
	/** minimal force to change the motion into a directional motion */
	public static float MIN_SPEED_FOR_UNDAMPED_DIRECTIONAL = 20.5f;
	
	/** the selection vector gets divided by this factor */
	public static float SELECTION_FORCE_FACTOR = 0.8f;
	
	public static final float DIRORBITTRANSFORM_DIRVEC_FACTOR = 2;
	public static final float TRANSFORMATION_DISTANCE = 10;
	public static final float ORBIT_SPEEDMORPH_PUSHVECFACTOR = 0.25f;
	public static final float ORBIT_DYNAMIC_SPEEDFACTOR = 120f;
	public static final float PLANETCOLL_SPEED_FROM_SAT_FACTOR = 0.25f;
	public static final float PLANETPART_CULL_DISTANCE = 30;
	public static final String PLANETPART_SUFFIX = "_unTied";	
	public static final float PLANETPART_REUSE_MINDISTANCE = 1f;
	public static final float PLANETPART_BOUNCE_FAC = 4f;
	
	public static final float SELECTION_BSPHERE_INCREMENT = 0.5f;
	
	
	public static final int SOUND_IMPACT = R.raw.l42_impact;
	public static final int SOUND_HEAVYIMPACT = R.raw.l42_heavyimpact;
	public static final int SOUND_SHOOT = R.raw.l42_shoot;
	public static final int SOUND_YEAH = R.raw.l42_yeah;

	public static int[] SOUND_LIST = 
		new int[] {
			SOUND_IMPACT,
			SOUND_HEAVYIMPACT,
			SOUND_SHOOT,
			SOUND_YEAH
		};


}
