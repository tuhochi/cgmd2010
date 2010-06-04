package at.ac.tuwien.cg.cgmd.bifth2010.level42.util;

import android.util.Log;

public class LogManager
{
	/** The Constant TAG. */
	public static final String TAG = "Signanzorbit";

	public static void v(Object msg)
	{
		if(Config.LOGLEVEL <= Log.VERBOSE)
			Log.v(TAG, msg.toString());
	}

	public static void v(Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.VERBOSE)
			Log.v(TAG, msg.toString(), t);
	}

	public static void vTag(String tag, Object msg)
	{
		if(Config.LOGLEVEL <= Log.VERBOSE)
			Log.v(tag, msg.toString());
	}

	public static void vTag(String tag, Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.VERBOSE)
			Log.v(tag, msg.toString(), t);
	}

	public static void d(Object msg)
	{
		if(Config.LOGLEVEL <= Log.DEBUG)
			Log.d(TAG, msg.toString());
	}

	public static void d(Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.DEBUG)
			Log.d(TAG, msg.toString(), t);
	}

	public static void dTag(String tag, Object msg)
	{
		if(Config.LOGLEVEL <= Log.DEBUG)
			Log.d(tag, msg.toString());
	}

	public static void dTag(String tag, Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.DEBUG)
			Log.d(tag, msg.toString(), t);
	}

	public static void i(Object msg)
	{
		if(Config.LOGLEVEL <= Log.INFO)
			Log.i(TAG, msg.toString());
	}

	public static void i(Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.INFO)
			Log.i(TAG, msg.toString(), t);
	}

	public static void iTag(String tag, Object msg)
	{
		if(Config.LOGLEVEL <= Log.INFO)
			Log.i(tag, msg.toString());
	}

	public static void iTag(String tag, Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.INFO)
			Log.i(tag, msg.toString(), t);
	}

	public static void w(Object msg)
	{
		if(Config.LOGLEVEL <= Log.WARN)
			Log.w(TAG, msg.toString());
	}

	public static void w(Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.WARN)
			Log.w(TAG, msg.toString(), t);
	}

	public static void wTag(String tag, Object msg)
	{
		if(Config.LOGLEVEL <= Log.WARN)
			Log.w(tag, msg.toString());
	}

	public static void wTag(String tag, Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.WARN)
			Log.w(tag, msg.toString(), t);
	}

	public static void e(Object msg)
	{
		if(Config.LOGLEVEL <= Log.ERROR)
			Log.e(TAG, msg.toString());
	}

	public static void e(Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.ERROR)
			Log.e(TAG, msg.toString(), t);
	}

	public static void eTag(String tag, Object msg)
	{
		if(Config.LOGLEVEL <= Log.ERROR)
			Log.e(tag, msg.toString());
	}

	public static void eTag(String tag, Object msg, Throwable t)
	{
		if(Config.LOGLEVEL <= Log.ERROR)
			Log.e(tag, msg.toString(), t);
	}
}