package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class SpriteContainer extends Sprite {
	protected Vector<Sprite> children;
	protected Vector<Sprite> childrenFront;
	
	public SpriteContainer(TexturePart texturePart) {
		this(texturePart, texturePart.getWidth(), texturePart.getHeight());
	}
	
	public SpriteContainer(TexturePart texturePart, float width, float height) {
		this(texturePart, width, height, 0, 0);
	}

	public SpriteContainer(TexturePart texturePart, float width, float height,
			float x, float y) {
		super(texturePart, width, height, x, y);
		children = new Vector<Sprite>();
		childrenFront = new Vector<Sprite>();
	}

	public void addChild(Sprite child) {
		children.addElement(child);
	}

	public void addChildFront(Sprite child) {
		childrenFront.addElement(child);
	}

	@Override
	protected void onBeforeDraw(GL10 gl) {
		/* Draw all children */
		for (Sprite i: children) {
			i.draw(gl);
		}
	}

	@Override
	protected void onAfterDraw(GL10 gl) {
		for (Sprite i: childrenFront) {
			i.draw(gl);
		}
	}

}
