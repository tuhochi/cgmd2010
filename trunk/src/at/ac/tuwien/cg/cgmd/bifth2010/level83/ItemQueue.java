package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

import static at.ac.tuwien.cg.cgmd.bifth2010.level83.Constants.*;

/**
 * A Queue of items which are shown in the toolbox and can be used by the 
 * player. This class is an implementation of the {@link Drawable} interface.
 * @author Manuel Keglevic, Thomas Schulz
 */
public class ItemQueue implements Drawable {

	private MySprite overlay;
	
	/** Prototypes for items */
	private ItemSprite prototypeBomb, prototypeDynamite, prototypeWall;
	private List<ItemSprite> items;
	
	/** Capacity of the Queue and screen dimensions */
	private int size, width, height;
	
	/** Generates a random item */
	public static final int RANDOM = 0;
	/** Generates a Bomb item */
	public static final int BOMB = 1;
	/** Generates an item to delete walls */
	public static final int DELETEWALL = 2;
	/** Generates a wall item */
	public static final int WALL = 3;
	
	/** Number of types which are already implemented */
	private static final double NUM_OF_ITEMTYPES = 3;
	
	/** Dimensions of an item */
	public int itemWidth = 80, itemHeight = 80;
	
	/**
	 * Creates a Queue for items with a capacity of <code>size</code>.
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
	 * Adds a new {@link ItemSprite} with the texture for <code>item</code> to 
	 * the Queue.
	 * 
	 * <p>
	 * If the Queue is full, the first element is removed to make space for the
	 * new <code>ItemSprite</code>.
	 * </p>
	 * 
	 * @param item	The ID of the texture for the <code>ItemSprite</code> to be
	 * 				added.
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
		} else if (item == DELETEWALL) {
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
	
	/**
	 * Sets the position and dimension of the items in the Queue according to 
	 * the screen resolution.
	 */
	private void setItemPositions() {
		itemWidth = itemHeight = width/10;
//		itemHeight = height/6;
		float itemX = width - itemWidth - (overlay.width - itemWidth)/2;
		float itemYStart = itemHeight + (height/4 - itemHeight)/2;
		
		synchronized (items) {
			Iterator<ItemSprite> it = items.iterator();
			
			ItemSprite s2;
			int i = 0;
			while (it.hasNext()) {
				s2 = it.next();
				s2.height = itemHeight;
				s2.width = itemWidth;
				s2.x = itemX;
				s2.y = height - itemYStart - i*(height/size);
				i++;
			}
		}
	}
	
	/**
	 * Moves the first item along the distances <code>dX</code> and 
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
	 * Centers the first item around <code>x</code> and <code>y</code>.
	 * 
	 * <p>
	 * <b>Warning:</b> Seems to be slower than {@link #moveItem(float, float)}.
	 * </p>
	 * 
	 * @param x	XPos for centering.
	 * @param y	YPos for centering.
	 */
	public void centerItem(float x, float y) {
		x -= itemWidth/2;
		y = height - y - itemHeight/2;
		
		synchronized (items) {
			ItemSprite s2 = items.get(0);
			s2.x = x;
			s2.y = y;
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
		prototypeDynamite = new ItemSprite(DELETEWALL, 0, 0, 80, 80, gl);
		prototypeWall = new ItemSprite(WALL, 0, 0, 80, 80, gl);
	}

}
