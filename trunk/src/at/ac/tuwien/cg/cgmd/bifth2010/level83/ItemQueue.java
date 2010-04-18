package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import static at.ac.tuwien.cg.cgmd.bifth2010.level83.Constants.*;

public class ItemQueue implements Drawable {

	private MySprite overlay;
	private ItemSprite prototypeBomb, prototypeLaser, prototypeDynamite, prototypeWall;
	private List<ItemSprite> items;
	private int size, width, height;
	
	public static final int RANDOM = 0;
	public static final int BOMB = 1;
	public static final int LASER = 2;
	public static final int DYNAMITE = 3;
	public static final int WALL = 4;
	
	/** Number of types which are already implemented */
	private static final double NUM_OF_ITEMTYPES = 2;
	
	public int itemWidth = 80, itemHeight = 80;
	
	/**
	 * Creates a Queue for Items with a capacity of <code>size</code>.
	 * 
	 * @param size	The capacity of the Queue.
	 * @param gl
	 */
	public ItemQueue(int size, GL10 gl) {
		overlay = new MySprite(OVERLAY, 200, 0, 80, 80, gl);
		items = Collections.synchronizedList(new LinkedList<ItemSprite>());
		this.size = size;
		Init(gl);
	}
	
	/**
	 * Adds a new ItemSprite with the texture for <code>item</code> to the
	 * Queue.
	 * 
	 * <p>
	 * If the Queue is full, the first element is removed to make space for the
	 * new ItemSprite.
	 * </p>
	 * 
	 * @param item	The ID of the texture for the ItemSprite to be added.
	 */
	public void put(int item) {
		if (items.size() >= size)
			items.remove(0);
		
		if (item == RANDOM)
			item = (int)Math.ceil(Math.random()*NUM_OF_ITEMTYPES);
		Log.d("ItemQueue", "Item: " + item);
		
		if (item == BOMB) {
			if (prototypeBomb != null)
				items.add(new ItemSprite(prototypeBomb));
		} else if (item == LASER) {
			if (prototypeLaser != null)
				items.add(new ItemSprite(prototypeLaser));
		} else if (item == DYNAMITE) {
			if (prototypeDynamite != null)
				items.add(new ItemSprite(prototypeDynamite));
		} else if (item == WALL) {
			if (prototypeWall != null)
				items.add(new ItemSprite(prototypeWall));
		}
		
		setItemPositions();
	}
	
	/**
	 * Changes size and position of the overlay and its items to fit the screen
	 * size.
	 * 
	 * @param width		Width of the viewport.
	 * @param height	Height of the viewport.
	 */
	public void updateViewport(int width, int height) {
		this.width = width;
		this.height = height;

		synchronized (overlay) {
			overlay.width = width/8;
			overlay.height = height;
			overlay.x = width - overlay.width;
		}
		
		setItemPositions();
	}
	
	private void setItemPositions() {
		itemWidth = width*2/30;
		itemHeight = height/6;
		float itemX = width - itemWidth - (overlay.width - itemWidth)/2;
		
		synchronized (items) {
			Iterator<ItemSprite> it = items.iterator();
			
			ItemSprite s2;
			int i = 1;
			while (it.hasNext()) {
				s2 = it.next();
				s2.height = itemHeight;
				s2.width = itemWidth;
				s2.x = itemX;
				s2.y = height - i*(itemHeight + 20);
				i++;
			}
		}
	}
	
	/**
	 * Moves the first item the along the distances <code>dX</code> and 
	 * <code>dY</code>.
	 * 
	 * @param dX	Distance in X-direction.
	 * @param dY	Distance in Y-direction.
	 */
	public void moveItem(float dX, float dY) {
		synchronized (items) {
			ItemSprite s2 = items.get(0);
			s2.x -= dX;
			s2.y += dY;
		}
	}
	
	/**
	 * Resets the position of the first item.
	 */
	public void resetItem() {
		synchronized (items) {
		ItemSprite s2 = items.get(0);
			s2.x = width - s2.width - (overlay.width - s2.width)/2;
			s2.y = height - s2.height - 20;
		}
	}
	
	/**
	 * Returns the type of the first item in the Queue.
	 * 
	 * @return The type of the current item.
	 */
	public int getItemType() {
		return items.get(0).type;
	}

	@Override
	public void Dispose(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Draw(GL10 gl) {
		overlay.Draw(gl);
		
		synchronized (items) {
			Iterator<ItemSprite> it = items.iterator();
			if (it.hasNext())
				it.next().Draw(gl, true);
			while (it.hasNext()) {
				it.next().Draw(gl, false);
			}
		}
	}

	@Override
	public void Init(GL10 gl) {
		prototypeBomb = new ItemSprite(BOMB, 0, 0, 80, 80, gl);
		prototypeLaser = new ItemSprite(LASER, 0, 0, 80, 80, gl);
//		prototypeDynamite = new ItemSprite(DYNAMITE, 0, 0, 80, 80, gl);
//		prototypeWall = new ItemSprite(WALL, 0, 0, 80, 80, gl);
	}

}
