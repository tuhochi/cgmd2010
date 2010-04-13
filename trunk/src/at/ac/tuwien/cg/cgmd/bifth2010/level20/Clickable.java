package at.ac.tuwien.cg.cgmd.bifth2010.level20;

public abstract class Clickable {
	BoundingBox bbox = new BoundingBox(0,0,0,0);
	boolean hitTest(float x, float y)
	{
		return bbox.hitTest(x,y);
	}
}
