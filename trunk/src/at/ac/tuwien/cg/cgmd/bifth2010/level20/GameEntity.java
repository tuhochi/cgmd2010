package at.ac.tuwien.cg.cgmd.bifth2010.level20;


public class GameEntity extends Clickable {

	protected int id;
	protected float posX;
	protected float posY;
	protected float depth;
	protected float scaleX;
	protected float scaleY;
	protected float angle;			

	public GameEntity()
	{
		id = -1;
		posX = 0f;
		posY = 0f;
		depth = 0f;
		scaleX = 1f;
		scaleY = 1f;
		angle = 0f;				
	}
	
	public int id() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public float posX() {
		return posX;
	}

	public void setPosX(float posX) {
		this.posX = posX;
		bbox.posX = posX;
	}
	
	public void setPos(float x, float y) {
		setPosX(x);
		setPosY(y);
	}

	public float posY() {
		return posY;
	}

	public void setPosY(float posY) {
		this.posY = posY;
		bbox.posY = posY;
	}

	public float depth() {
		return depth;
	}

	public void setDepth(float depth) {
		this.depth = depth;
	}

	public float scaleX() {
		return scaleX;
	}

	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
		bbox.width = scaleX * 0.5f;
	}

	public float scaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
		bbox.height = scaleY * 0.5f;
	}
	
	public void setScale(float x, float y) {
		setScaleX(x);
		setScaleY(y);
	}

	public float angle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}
}
