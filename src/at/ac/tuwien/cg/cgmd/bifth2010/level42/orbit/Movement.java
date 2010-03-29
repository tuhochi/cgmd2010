package at.ac.tuwien.cg.cgmd.bifth2010.level42.orbit;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.math.Matrix44;

public interface Movement {
	public void update(float dt);
	public Matrix44 getTransform();
	public void setSatTrans(SatelliteTransformation satTrans);
}
