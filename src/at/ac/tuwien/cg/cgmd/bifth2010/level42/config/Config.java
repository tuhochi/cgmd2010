package at.ac.tuwien.cg.cgmd.bifth2010.level42.config;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This Class represents the configuration for the level 42.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Config
{
	
	/** The Loglevel VERBOSE. */
	public static final int VERBOSE = 0;
	
	/** The Loglevel DEBUG. */
	public static final int DEBUG = 1;
	
	/** The Loglevel INFO. */
	public static final int INFO = 2;
	
	/** The Loglevel WARN. */
	public static final int WARN = 3;
	
	/** The Loglevel ERROR. */
	public static final int ERROR = 4;
	
	/** The Loglevel NONE. */
	public static final int NONE = 5;
	
	/** Whether or not OpenGL ES 1.1 is available (set on runtime). */
	public static boolean GLES11 = false;

	/** The FPS update interval in ms. */
	public static long FPS_UPDATE_INTERVAL = 1000;
	
	/** The Gametime update interval in ms. */
	public static long GAMETIME_UPDATE_INTERVAL = 1000;
	
	/** How long to wait before closing the game after it has been finished in ms. */
	public static long GAMETIME_WAIT_AFTER_COMPLETE = 2000;
	
	/** The name of the level resource */
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
	/** Whether or not to show the FPS */
	public static boolean SHOW_FPS = false;
	
	/** Whether or not to show SceneEntity Bounding Spheres */
	public static boolean SHOW_SCENEENTITY_BOUNDING_SPHERES = false;
	
	/** Whether or not to show the Model Bounding Spheres */
	public static boolean SHOW_MODEL_BOUNDING_SPHERES = false;
	
	/** Whether the easy mode is turned on */
	public static boolean EASY_MODE = false;
	
	/** Whether or not the vibrator is turned on */
	public static boolean VIBRATE = true;
	
	/** The minimum loglevel to display */
	public static int LOGLEVEL = DEBUG;
	
	/** the total level gametime in ms. */
	public static long GAMETIME = 120000;
	
	/** UI. */

	/** 0.5f = half the smaller side of the screen */
	public static final float MAX_FORCE_VIS_SIZE = 0.50f;
	
	/** (PI/2)/1000.0f = 180° per second */
	public static final float FORCE_VIS_ROTATION_SPEED = Constants.PIHALF/1000.0f;
	
	/** How long the vibrator should fire on full power. */
	public static final float MAX_VIBRATION_LENGTH = 250;
	
	/** GAMEPLAY. */
	
	/** the maximum length of the force vector (in units) */
	public static int MAX_FORCE = 25;
	
	/** how long a force cycle 0->full->0 takes (ms). */
	public static int LONG_PRESS_CYCLE_DURATION = 800;
	
	/** The minimum distance in Pixels a Finger has to move to be recognised as a move. */
	public static final int TOUCH_DEADZONE = 30;

	/** The Distance between the Planet and the Camera. */
	public static final float CAM_DISTANCE = 30f;
	
	/** PHYSICS / UNIVERSE. */

	/** center position of the universe / orbits */
	public static Vector3 UNIVERSE_CENTER = new Vector3(0,0,0);
	
	/** The name of the Planet SceneEntity. */
	public static String PLANET_NAME = "GoldPlanet";
	
	/** The name of the Skysphere SceneEntity Name. */
	public static String SKYSPHERE_NAME = "Skysphere";
	
	/** prefix for the satellite SceneEntities. */
	public static String SATELLITE_PREFIX = "Satellite_";
	
	/** defines the maximum of the orbit main axis. */
	public static float UNIVERSE_CENTERLENGTH_LIMIT = 24;
	
	/** defines the maximum of the orbit secondary axis. */
	public static float UNIVERSE_DIRLENGTH_LIMIT = 24;
	
	/** defines the maximum of the orbit main axis. */
	public static float FORCEFIELD_CENTERLENGTH_LIMIT = 11;
	
	/** defines the maximum of the orbit secondary axis. */
	public static float FORCEFIELD_DIRLENGTH_LIMIT = 11;
	
	/** The force fields new centerlength. */
	public static float FORCEFIELD_NEW_CENTERLENGTH = 15;
	
	/** The force fields new direction length. */
	public static float FORCEFIELD_NEW_DIRLENGTH = 15;
	
	/** The force fields center length scale speed. */
	public static float FORCEFIELD_CENTERLENGTH_SCALESPEED = 25;
	
	/** The force fields direction length scale speed. */
	public static float FORCEFIELD_DIRLENGTH_SCALESPEED = 25;
	
	/** defines the maximum relative speed for satellites. */
	public static float UNIVERSE_SPEED_LIMIT = 7.5f;
	
	/** The orbit temp maxspeed factor. */
	public static float ORBIT_TEMP_MAXSPEED_FAC = 3;
	
	/** The minimal speed of an orbit. */
	public static float ORBIT_MIN_SPEED = 0.5f;

	/** ratio between speed and rotation angle. */
	public static float SATELLITE_SPEEDROTA_RATIO = 4f;
	
	/** ratio between speed and rotation angle. */
	public static float INTERSATELLITE_SPEEDROTA_RATIO = 6f;
	
	/** the required penetration depth on collisions. */
	public static float COLLISION_PENETRATION_DEPTH = 0.2f;
	
	/** minimal force to change the motion into a directional motion. */
	public static float MIN_STRENGTH_FOR_DIRECTIONAL = 0.4f * MAX_FORCE;
	
	/** minimal force to change the motion into a directional motion. */
	public static float MIN_SPEED_FOR_UNDAMPED_DIRECTIONAL = 0.6f * MAX_FORCE;
	
	/** the selection vector gets divided by this factor. */
	public static float SELECTION_FORCE_FACTOR = 1f;
	
	/** The directional orbit transform direction vector factor. */
	public static final float DIRORBITTRANSFORM_DIRVEC_FACTOR = 2;
	
	/** The Transformation Distance. */
	public static final float TRANSFORMATION_DISTANCE = 10;
	
	/** The Orbit speedmorph push vector factor. */
	public static final float ORBIT_SPEEDMORPH_PUSHVECFACTOR = 0.25f;
	
	/** The orbit dynamic speed factor. */
	public static final float ORBIT_DYNAMIC_SPEEDFACTOR = 120f;
	
	/** The planet collision speed from satellite factor. */
	public static final float PLANETCOLL_SPEED_FROM_SAT_FACTOR = 0.25f;
	
	/** The Planetpart culling distance. */
	public static final float PLANETPART_CULL_DISTANCE = 30;
	
	/** The suffix of untied planet parts. */
	public static final String PLANETPART_SUFFIX = "_unTied";	
	
	/** The minimal distance to re-use planet parts. */
	public static final float PLANETPART_REUSE_MINDISTANCE = 1f;
	
	/** The planet part bounce factor. */
	public static final float PLANETPART_BOUNCE_FAC = 3.5f;
	
	/** The damped max collision count factor. */
	public static final float DAMPED_MAX_COLLISION_COUNT_FACTOR = 0.3333f;
	
	/** The undamped speed decrement. */
	public static final float UNDAMPED_SPEED_DECREMENT = 0.3f*MAX_FORCE;
	
	/** The selection bounding sphere increment. */
	public static final float SELECTION_BSPHERE_INCREMENT = 0.5f;
	
	/** The normal impact sound. */
	public static final int SOUND_IMPACT = R.raw.l42_impact;
	
	/** The heavy impact sound. */
	public static final int SOUND_HEAVYIMPACT = R.raw.l42_heavyimpact;
	
	/** The shoot sound. */
	public static final int SOUND_SHOOT = R.raw.l42_shoot;
	
	/** The finish sound. */
	public static final int SOUND_YEAH = R.raw.l42_yeah;

	/** All used sounds. */
	public static int[] SOUND_LIST = 
		new int[] {
			SOUND_IMPACT,
			SOUND_HEAVYIMPACT,
			SOUND_SHOOT,
			SOUND_YEAH
		};
}
