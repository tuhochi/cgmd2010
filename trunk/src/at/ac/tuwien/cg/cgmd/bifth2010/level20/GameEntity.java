package at.ac.tuwien.cg.cgmd.bifth2010.level20;


/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class GameEntity {
	
	/**
	 * This counter increases by 1 everytime a new entity is created. 
	 */
	protected static int count = 0;

	protected int id;
	protected float x;
	protected float y;
	protected float z;
	protected float width;
	protected float height;
	protected float angle;			

	/**
	 * Creates a primitive GameEntity. GameEntities have an id, x-, y-, z-position, width, height and angle which are set to standard values
	 */
	public GameEntity()
	{
		id = count++;
		x = 0;
		y = 0;
		z = 0;
		width = 1;
		height = 1;
		angle = 0;				
	}
		
	/**
	 * Sets the position in a comfortable way
	 * @param x
	 * @param y
	 */
	public void setPos(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Sets the dimension in a comfortable way
	 * @param width
	 * @param height
	 */
	public void setDim(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Let's you set radians if you need them
	 * 
	 * @param radians
	 */
	public void setRadians(float radians) {
		angle = radians * 180f / (float)Math.PI; 
	}
}
