package at.ac.tuwien.cg.cgmd.bifth2010.level20;

/** A 2D bounding box class. */ 
public class BoundingBox {
	// Center position of the bounding box.
	float posX;
	float posY;
	// Half measurements from the center of the box.
	float width;
	float height;
	
	public BoundingBox(float x, float y, float width, float height)
	{
		posX = x;
		posY = y;
		this.width = width;
		this.height = height;
	}
	
	boolean hitTest(float x, float y)
	{
		return (x > posX-width) && (x < posX+width) && (y > posY-height) && (y < posY+height);	
	}
}
