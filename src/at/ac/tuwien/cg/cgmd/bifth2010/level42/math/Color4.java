package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

public class Color4
{
	public final float r,g,b,a;
	public final float[] asArray;
	
	public static final Color4 BLACK = new Color4(0,0,0);
	public static final Color4 WHITE = new Color4(1,1,1);
	public static final Color4 RED = new Color4(1,0,0);
	public static final Color4 GREEN = new Color4(0,1,0);
	public static final Color4 BLUE = new Color4(0,0,1);
	public static final Color4 CYAN = new Color4(0,1,1);
	public static final Color4 MAGENTA = new Color4(1,0,1);
	public static final Color4 YELLOW = new Color4(1,1,0);
	
	public Color4(float r, float g, float b, float a)
	{
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
		asArray = new float[] {r,g,b,a};
	}
	
	public Color4(float r, float g, float b)
	{
		this(r,g,b,1);
	}
	
	public Color4(Color4 other)
	{
		this(other.r, other.g, other.b, other.a);
	}

	public Color4()
	{
		this(0,0,0,1);
	}
}
