package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.Serializable;

/**
 * This class implements an 2-dimensional vector
 * @author Markus Ernst
 * @author Florian Felberbauer
 *
 */
public class Vector2 implements Serializable {

	private static final long serialVersionUID = -7505462190307942882L;
	/**
	 * x and y data fields
	 */
	public float x,y; 
	
	/**
	 * Default Constructor
	 */
	public Vector2() {
		
	}
	
	/**
	 * Constructor
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 */
	public Vector2(float x, float y) {
		this.x = x; 
		this.y = y; 
	}
}
