package at.ac.tuwien.cg.cgmd.bifth2010.level11;

public class Color {

	float r, g, b, a;
	
	public static SkinColor skin_color;
	//public static StandardColor skin_color;
	
	public Color() {
		this.r = 1.0f;
		this.g = 1.0f;
		this.b = 1.0f;
		this.a = 1.0f;
	}
	
	public static void init() {
		skin_color = skin_color.new SkinColor();
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
	
	public class SkinColor extends Color {

		
		Color caucasian, asian, african;
		
		public SkinColor() {
			
			caucasian = new Color (0.816f, 0.707f, 0.641f, 1.0f);
			asian = new Color (0.805f, 0.732f, 0.508f, 1.0f);
			african = new Color (0.473f, 0.324f, 0.129f, 1.0f);
			
		}
		
	}
	
}
