package at.ac.tuwien.cg.cgmd.bifth2010.level60;

public class Rectangle {
	private Point lowerLeft;
	private Point upperRight;
	
	public Rectangle() { lowerLeft = new Point(0, 0); upperRight = new Point(1, 1); }
	public Rectangle(Point lowerLeft, Point upperRight) { this.lowerLeft = lowerLeft; this.upperRight = upperRight; }
	public Rectangle(Rectangle copy) { lowerLeft = copy.getLowerLeft(); upperRight = copy.getUpperRight(); }
	public Rectangle(Point lowerLeft, float width, float height) { this.lowerLeft = lowerLeft; this.upperRight = new Point(lowerLeft.getX()+width, lowerLeft.getY()+height); }
	public Point getLowerLeft() { return lowerLeft; }
	public Point getUpperRight() { return upperRight; }
	public Point getLowerRight() { return Point.add(lowerLeft, new Point(this.getWidth(), 0)); }
	public Point getUpperLeft() { return Point.add(lowerLeft, new Point(0, this.getHeight())); }
	public void setLowerLeft(Point lowerLeft) { this.lowerLeft = lowerLeft; }
	public void setUpperRight(Point upperRight) { this.upperRight = upperRight; }
	public float getWidth() { return Point.diffX(lowerLeft, upperRight); }
	public float getHeight() { return Point.diffY(lowerLeft, upperRight); }
	public boolean pointInside(Point op) {
		if (op.getX() >= lowerLeft.getX() && op.getX() <= upperRight.getX() &&
			op.getY() >= lowerLeft.getY() && op.getY() <= upperRight.getY()) return true;
		else return false;
	}
	public int overlap(Rectangle op) {
		int result = Point.IDENT;
		if (pointInside(op.getLowerLeft())) result |= (Point.LOWER | Point.LEFT);
		if (pointInside(op.getUpperRight())) result |= (Point.UPPER | Point.RIGHT); 
		if (pointInside(op.getUpperLeft())) result |= (Point.UPPER | Point.LEFT);
		if (pointInside(op.getLowerRight())) result |= (Point.LOWER | Point.RIGHT);
		return result;
	}
	public static boolean pointInside(Rectangle r, Point op) {
		if (op.getX() >= r.getLowerLeft().getX() && op.getX() <= r.getUpperRight().getX() &&
			op.getY() >= r.getLowerLeft().getY() && op.getY() <= r.getUpperRight().getY()) return true;
		else return false;
	}
	public static boolean checkOverlap(Rectangle op, Rectangle op2) {
		if (pointInside(op2, op.getLowerLeft()) || pointInside(op2, op.getUpperRight()) || 
				pointInside(op2, op.getUpperLeft()) || pointInside(op2, op.getLowerRight())) return true;
		else return false;
	}
}
