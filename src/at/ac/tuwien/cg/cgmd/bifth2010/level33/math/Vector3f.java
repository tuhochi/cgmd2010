package at.ac.tuwien.cg.cgmd.bifth2010.level33.math;

public class Vector3f {

	public float x, y, z;

	public Vector3f(float x, float y, float z) {
		set(x, y, z);
	}

	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;

	}
	
	public String toString(){
		
		return String.valueOf(x+" "+y+" "+z);
	}

	public void translate(Vector3f translation) {
		this.x+=translation.x;
		this.y+=translation.y;
		this.z+=translation.z;
	}

}
