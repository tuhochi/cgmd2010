package at.ac.tuwien.cg.cgmd.bifth2010.level20;


/**
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class GameEntity {
	
	/**	This counter increases every time a new entity is created and is assigned as unique ID. */ 	 
	protected static int count = 0;
	/** The Id of the entity. */
	protected int id;
	/** The x-position of the entity. */
	protected float x;
	/** The y-position of the entity. */
	protected float y;
	/** The z-position of the entity. */
	protected float z;
	/** The width of the entity. */
	protected float width;
	/** The height of the entity. */
	protected float height;
	/** The angle of the entity. */
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
