package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

public interface OrientationListener {
	
	public void onOrientationChanged(float azimuth, float pitch, float roll); 
	
	// Phone is rolling to the right (phone's x-axis relative to horizontal axis) 
	public void onRollRight();
	
	//Phone is rolling to the left (phone's x-axis relative to horizontal axis) 
	public void onRollLeft();

}
