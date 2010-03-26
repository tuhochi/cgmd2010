package at.ac.tuwien.cg.cgmd.bifth2010.level44.io;

public interface InputGesture {
	public float getStartX();
	public float getStartY();
	
	public float getEndX();
	public float getEndY();
	
	public enum DisplayHalf { LEFT, RIGHT }
}
