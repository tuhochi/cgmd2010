package at.ac.tuwien.cg.cgmd.bifth2010.level11;

/**
 * Color Object, predefined skin colors are caucasian, asian, african, p
 * redefined hair colors are blonde black and brown;
 * @author g11
 *
 */
public class Color {
	/**
	 * skin color
	 */
    public static final Color caucasian = new Color(0.816f, 0.707f, 0.641f, 1.0f);
	/**
	 * skin color
	 */
    public static final Color asian = new Color (0.805f, 0.732f, 0.508f, 1.0f);
	/**
	 * skin color
	 */
    public static final Color african = new Color (0.473f, 0.324f, 0.129f, 1.0f);
	/**
	 * hair color
	 */
    public static final Color blonde = new Color (0.668f, 0.509f, 0.122f, 1.0f);
	/**
	 * hair color
	 */
    public static final Color black = new Color (0.090f, 0.090f, 0.090f, 1.0f);
	/**
	 * hair color
	 */
    public static final Color brown = new Color (0.328f, 0.207f, 0.090f, 1.0f);
    
	float r, g, b, a;
	
	/**
	 * Creates a white color.
	 */
	public Color() {
		this.r = 1.0f;
		this.g = 1.0f;
		this.b = 1.0f;
		this.a = 1.0f;
	}
	
	/**
	 * Creates a specific color
	 * @param r red 0.0 to 1.0
	 * @param g green 0.0 to 1.0
	 * @param b blue 0.0 to 1.0
	 * @param a alpha 0.0 to 1.0
	 */
	public Color(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	/**
	 * Sets r, g, b and a to a specific value.
	 * @param r red 0.0 to 1.0
	 * @param g green 0.0 to 1.0
	 * @param b blue 0.0 to 1.0
	 * @param a alpha 0.0 to 1.0
	 */
	public void set(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}	
}