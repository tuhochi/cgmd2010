package at.ac.tuwien.cg.cgmd.bifth2010.level20;


/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class GameEntity {

	protected int id;
	protected float x;
	protected float y;
	protected float z;
	protected float width;
	protected float height;
	protected float angle;			

	public GameEntity()
	{
		id = -1;
		x = 0;
		y = 0;
		z = 0;
		width = 1;
		height = 1;
		angle = 0;				
	}
	
	public int id() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public float x() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
//		bbox.posX = posX;
	}

	public float posY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
//		bbox.posY = posY;
	}
	
	public void setPos(float x, float y) {
		setX(x);
		setY(y);
	}

	public float z() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public float width() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
//		bbox.width = scaleX * 0.5f;
	}

	public float height() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
//		bbox.height = scaleY * 0.5f;
	}
	
	public void setDim(float width, float height) {
		setWidth(width);
		setHeight(height);
	}

	public float angle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
	
	public void setRadians(float radians) {
		setAngle(radians * 180f / (float)Math.PI); 
	}

//	@Override
//	public boolean hitTest(float x, float y) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void render(GL10 gl) {
//		// TODO Auto-generated method stub
//		
//	}
}
