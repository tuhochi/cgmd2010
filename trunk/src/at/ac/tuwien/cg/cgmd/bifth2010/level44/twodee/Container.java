package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class Container extends Item {
	private Vector<Item> children;
	private Vector<Item> childrenFront;
	
	public Container(TexturePart texturePart) {
		this(texturePart, texturePart.getWidth(), texturePart.getHeight());
	}
	
	public Container(TexturePart texturePart, float width, float height) {
		this(texturePart, width, height, 0, 0);
	}

	public Container(TexturePart texturePart, float width, float height,
			float x, float y) {
		super(texturePart, width, height, x, y);
		children = new Vector<Item>();
		childrenFront = new Vector<Item>();
	}

	public void addChild(Item child) {
		children.addElement(child);
	}

	public void addChildFront(Item child) {
		childrenFront.addElement(child);
	}

	@Override
	protected void onBeforeDraw(GL10 gl) {
		/* Draw all children */
		for (Item i: children) {
			i.draw(gl);
		}
	}

	@Override
	protected void onAfterDraw(GL10 gl) {
		for (Item i: childrenFront) {
			i.draw(gl);
		}
	}

}
