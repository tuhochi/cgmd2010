package at.ac.tuwien.cg.cgmd.bifth2010.level36;

public class Vektor3 {
	
	float[] vektor;

	public Vektor3() {
		vektor = new float[3]; 
	}
	
	public float getX() {
		return vektor[0];
	}
	
	public void setX(float x) {
		vektor[0] = x;
	}
	
	public float getY() {
		return vektor[1];
	}
	
	public void setY(float y) {
		vektor[1] = y;
	}
	
	public float getZ() {
		return vektor[2];
	}
	
	public void setZ(float z) {
		vektor[2] = z;
	}
	
	public float[] getVektor() {
		return vektor;
	}
	
	public void setVektor(float[] tmp) {
		vektor = tmp;
	}
	
	public float[] getNormalized() {
		float[] normal = new float[3];
		float sum = vektor[0] + vektor[1] + vektor[2];
		normal[0] = vektor[0]/sum;
		normal[1] = vektor[1]/sum;
		normal[2] = vektor[2]/sum;
		return normal;
	}
	
	public float[] getCrossed(float[] vektor2) {
		float[] cross = new float[3];
		cross[0] = vektor[1] * vektor2[2] - vektor[2] * vektor2[1];
		cross[1] = vektor[2] * vektor2[0] - vektor[0] * vektor2[2];
		cross[2] = vektor[0] * vektor2[1] - vektor[1] * vektor2[0];
		return cross;
	}
	
	public String toString() {
		return "Vektor: x = " + vektor[0] + " y = " + vektor[1] + " z = " + vektor[2];
	}
}
