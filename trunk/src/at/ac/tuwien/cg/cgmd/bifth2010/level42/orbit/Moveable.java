package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Sphere;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Vector3;

public interface Moveable { 
	public Motion getMotion(); 
	public void setMotion(Motion motion); 
	public Matrix44 getBasicOrientation();
	public void setTransformation(Matrix44 transform);
	public Matrix44 getTransformation();
	public Sphere getBoundingSphereWorld();
	public String getName();
}