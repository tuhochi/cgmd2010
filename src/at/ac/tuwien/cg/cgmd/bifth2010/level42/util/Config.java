package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public class Config
{
	public static boolean GLES11 = false;
	public static long FPS_UPDATE_INTERVAL = 1000;
	public static long GAMETIME_UPDATE_INTERVAL = 1000;
	public static long GAMETIME = 120000;
	
	public static Vector3 UNIVERSE_CENTER = new Vector3(0,0,0);
	public static String PLANET_NAME = "GoldPlanet";
	public static String SATELLITE_PREFIX = "Satellite_";
	public static float UNIVERSE_CENTERLENGTH_LIMIT = 10;
	public static float UNIVERSE_DIRLENGTH_LIMIT = 10;
	public static float UNIVERSE_SPEED_LIMIT = 10;
	
	public static float SATELLITE_ROTASPEED_RATIO = 0.25f;
	public static float COLLISION_PENETRATION_DEPTH = 0.2f;
}
