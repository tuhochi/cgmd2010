package at.ac.tuwien.cg.cgmd.bifth2010.level60;

/**
 * GameObject class for level 60. This is a special type of Tablet with which the player can interact.
 * This object is randomly placed across the level and not positioned in the level map like the
 * other tablets.
 * 
 * @author      Martin Schenk
 * @author      Tiare Feuchtner
 */
public class GameObject extends Tablet {
	public static final int OBJECTCLASS_CAR = 1; 
	public static final float BLOW_WIDTH = 60.0f;
	public static final float BLOW_HEIGHT = 60.0f;
	private int objectClass;
	public int framenr;
	private boolean isBeingDestroyed;
	private int maxFrame;
	private String textureBaseName;
	private textureManager texman;
	
	/**
	 * Constructor of the GameObject which assigns it a texture manager and a texture depending
	 * on the objectClass. In addition the height, width and position are defined.
	 * 
	 * @param objectClass
	 * @param width
	 * @param height
	 * @param x
	 * @param y
	 * @param texture
	 * @param texman textureManager
	 */
	public GameObject(int objectClass, int width, int height, float x, float y, int texture, textureManager texman) {
		super(width, height, x, y, texture);
		this.objectClass = objectClass;
		this.texman = texman;
		isBeingDestroyed = false;
		framenr = 0;
		switch (objectClass) {
		case OBJECTCLASS_CAR:
			maxFrame = 6;
			textureBaseName = "car";
			break;
		default: maxFrame = 0;
		}
	}
	
	/**
	 * Returns the object class.
	 * @return objectClass
	 */
	public int getObjectClass() { return objectClass; }
	
	/**
	 * Sets the flag isBeingDestroyed to true. Since the interaction in the game results in the
	 * destruction of an object, this flag triggers the animation of  it.
	 */
	public void destroy() {
		isBeingDestroyed = true;
	}
	
	/**
	 * Called to display the explosion when an object is destruyed. Textures of flame and smoke are
	 * overlaid, finally revealing the remnants of the destroyed object.
	 * 
	 * @return true if the object has been destroyed
	 */
	public boolean update() {
		if (isBeingDestroyed) {
			framenr++;
			if (framenr <= maxFrame) {
				if (framenr == maxFrame) {
					this.changeTexture(texman.getTexture(textureBaseName + "1"));
					overlays.clear();
				} else {
					if (!this.overlays.isEmpty()) overlays.clear();
					overlays.add(new Tablet(BLOW_WIDTH, BLOW_HEIGHT, this.getX()+(this.getWidth()/2.0f)-BLOW_WIDTH/2.0f, this.getY()+(this.getHeight()/2.0f)-BLOW_HEIGHT/2.0f, texman.getTexture("blow"+(framenr-1))));
				}
			}
			else {
				isBeingDestroyed = false;
				return false;
			}
		}
		return true;
	}
}
