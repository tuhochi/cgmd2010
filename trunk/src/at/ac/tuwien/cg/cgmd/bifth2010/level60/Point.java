package at.ac.tuwien.cg.cgmd.bifth2010.level60;

/**
 * Point class for level 60. Simple class which facilitates usage of coordinates.
 * 
 * @author      Martin Schenk
 * @author      Tiare Feuchtner
 */
public class Point {
	private float x;
	private float y;

	public static final int IDENT = 0;
	public static final int UPPER = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 4;
	public static final int LOWER = 8;

	/**
	 * Empty point constructor
	 */
	public Point() {
		x=0;
		y=0; 
	}

	/**
	 * Overrides the point constructor setting its coordinates
	 * @param x
	 * @param y
	 */
	public Point(float x, float y) {
		this.x = x; 
		this.y = y; 
	}

	/**
	 * Copies a point
	 * @param copy
	 */
	public Point(Point copy) { this.x = copy.getX(); this.y = copy.getY(); }
	
	/**
	 * Sets the y coordinate
	 * @param y
	 */
	public void setY(float y) { this.y = y; }
	
	/**
	 * Sets the x coordinate
	 * 
	 * @param x
	 */
	public void setX(float x) { this.x = x; }
	
	/**
	 * Gets the x coordinate
	 * @return
	 */
	public float getX() { return x; }
	
	/**
	 * Gets the y coordinate
	 * @return
	 */
	public float getY() { return y; }
	
	/**
	 * Adds up the coordinates of two points
	 * @param op
	 * @param op2
	 * @return
	 */
	public static Point add(Point op, Point op2) { return new Point(op.getX()+op2.getX(), op.getY()+op2.getY()); }
	
	/**
	 * Subtracts two points
	 * @param op
	 * @param op2
	 * @return
	 */
	public static Point sub(Point op, Point op2) { return new Point(op.getX()-op2.getX(), op.getY()-op2.getY()); }
	public void add(Point op) { x += op.getX(); y += op.getY(); }
	public void sub(Point op) { x -= op.getX(); y -= op.getY(); }
	
	/**
	 * Returns the distance between two points
	 * @param op
	 * @param op2
	 * @return
	 */
	public static float distance(Point op, Point op2) { return (float)Math.sqrt(Math.pow(Math.abs(op.getX()-op2.getX()), (double)2)+Math.pow(Math.abs(op.getY()-op2.getY()), (double)2)); }
	public float distance(Point op) { return (float)Math.sqrt(Math.pow(Math.abs(op.getX()-x), (double)2)+Math.pow(Math.abs(op.getY()-y), (double)2)); }
	public static float diffX(Point op, Point op2) { return (float)Math.abs(op.getX()-op2.getX()); }
	public static float diffY(Point op, Point op2) { return (float)Math.abs(op.getY()-op2.getY()); }
	public float diffX(Point op) { return (float)Math.abs(op.getX()-x); }
	public float diffY(Point op) { return (float)Math.abs(op.getY()-y); }

	public static int getRelation(Point op, Point op2) {
		if (op.getX() > op2.getX()) {
			if (op.getY() > op2.getY()) return (UPPER | RIGHT);
			else if (op.getY() < op2.getY()) return (LOWER | RIGHT);
			else return RIGHT;
		} else if (op.getX() < op2.getX()) {
			if (op.getY() > op2.getY()) return (UPPER | LEFT);
			else if (op.getY() < op2.getY()) return (LOWER | LEFT);
			else return LEFT;
		} else {
			if (op.getY() > op2.getY()) return UPPER;
			else if (op.getY() < op2.getY()) return LOWER;
			else return IDENT;
		}
	}

	public int getRelation(Point op) {	// returns "i am " ...
		if (x > op.getX()) {
			if (y > op.getY()) return (UPPER | RIGHT);
			else if (y < op.getY()) return (LOWER | RIGHT);
			else return RIGHT;
		} else if (x < op.getX()) {
			if (y > op.getY()) return (UPPER | LEFT);
			else if (y < op.getY()) return (LOWER | LEFT);
			else return LEFT;
		} else {
			if (y > op.getY()) return UPPER;
			else if (y < op.getY()) return LOWER;
			else return IDENT;
		}
	}
	public Point tileIndices() { 
		int retx, rety;
		retx = (int)Math.floor((float)x/(float)LevelRenderer.LEVEL_TILESIZE);
		rety = (int)Math.floor((float)y/(float)LevelRenderer.LEVEL_TILESIZE);
		if (retx < 0 || retx >= LevelRenderer.LEVEL_WIDTH) retx = -1;
		if (rety < 0 || rety >= LevelRenderer.LEVEL_HEIGHT) rety = -1;
		return new Point(retx, rety);
	}
	public static Point tileIndices(Point op) {
		int retx, rety;
		retx = (int)Math.floor((float)op.getX()/(float)LevelRenderer.LEVEL_TILESIZE);
		rety = (int)Math.floor((float)op.getY()/(float)LevelRenderer.LEVEL_TILESIZE);
		if (retx < 0 || retx >= LevelRenderer.LEVEL_WIDTH) retx = -1;
		if (rety < 0 || rety >= LevelRenderer.LEVEL_HEIGHT) rety = -1;
		return new Point(retx, rety);
	}
}
