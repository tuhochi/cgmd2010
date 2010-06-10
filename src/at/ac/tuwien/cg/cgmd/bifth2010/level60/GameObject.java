package at.ac.tuwien.cg.cgmd.bifth2010.level60;

public class GameObject extends Tablet {
	public static final int OBJECTCLASS_CAR = 1; 
	public static final float BLOW_WIDTH = 60.0f;
	public static final float BLOW_HEIGHT = 60.0f;
	private int objectClass;
	private int framenr;
	private boolean isBeingDestroyed;
	private int maxFrame;
	private String textureBaseName;
	private textureManager texman;
	
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
	
	public int getObjectClass() { return objectClass; }
	
	public void destroy() {
		isBeingDestroyed = true;
	}
	
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
