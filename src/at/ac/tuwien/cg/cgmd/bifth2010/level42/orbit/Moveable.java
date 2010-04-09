package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public interface Moveable { 
	public Motion getMotion(); 
	public void setMotion(Motion motion); 
	public Matrix44 getBasicOrientation();
	public void setTransformation(Matrix44 transform);
	public Matrix44 getTransformation();
}