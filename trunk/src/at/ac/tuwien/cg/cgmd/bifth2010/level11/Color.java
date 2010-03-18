package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.util.Log;



public class Color {

    private static final String LOG_TAG = Color.class.getSimpleName();

    public static final Color caucasian = new Color(0.816f, 0.707f, 0.641f, 1.0f);
    public static final Color asian = new Color (0.805f, 0.732f, 0.508f, 1.0f);
    public static final Color african = new Color (0.473f, 0.324f, 0.129f, 1.0f);
    public static final Color blonde = new Color (0.668f, 0.509f, 0.122f, 1.0f);
    public static final Color black = new Color (0.090f, 0.090f, 0.090f, 1.0f);
    public static final Color brown = new Color (0.328f, 0.207f, 0.090f, 1.0f);
    
	float r, g, b, a;
	
	
	public Color() {
		Log.i(LOG_TAG, "Color()");
		
		this.r = 1.0f;
		this.g = 1.0f;
		this.b = 1.0f;
		this.a = 1.0f;
	}
	
	public void init() {
		
		Log.i(LOG_TAG, "init()");
	}
	
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
}
