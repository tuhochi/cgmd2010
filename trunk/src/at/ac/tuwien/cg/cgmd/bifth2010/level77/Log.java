package at.ac.tuwien.cg.cgmd.bifth2010.level77;

/**
 * Log is a wrapper for android.util.Log. Logging can be switched off by seting enabled to false;
 * 
 * @author gerd
 * 
 */
public class Log
{
	private static boolean	enabled	= true;

	/**
	 * @see android.util.Log#d(String, String)
	 * @param tag
	 * @param msg
	 * @return
	 */
	public static int d(String tag, String msg)
	{
		if (enabled)
			return android.util.Log.d(tag, msg);
		return 0;
	}
	
	/**
	 * @see android.util.Log#e(String, String)
	 * @param tag
	 * @param msg
	 * @return
	 */
	public static int e(String tag, String msg)
	{
		if (enabled)
			return android.util.Log.e(tag, msg);
		return 0;
	}
	
	/**
	 * @see android.util.Log#w(String, String)
	 * @param tag
	 * @param msg
	 * @return
	 */
	public static int w(String tag, String msg)
	{
		if (enabled)
			return android.util.Log.w(tag, msg);
		return 0;
	}
	/**
	 * @see android.util.Log#i(String, String)
	 * @param tag
	 * @param msg
	 * @return
	 */
	public static int i(String tag, String msg)
	{
		if (enabled)
			return android.util.Log.i(tag, msg);
		return 0;
	}
	
	/**
	 * @see android.util.Log#v(String, String)
	 * @param tag
	 * @param msg
	 * @return
	 */
	public static int v(String tag, String msg)
	{
		if (enabled)
			return android.util.Log.v(tag, msg);
		return 0;
	}
}
