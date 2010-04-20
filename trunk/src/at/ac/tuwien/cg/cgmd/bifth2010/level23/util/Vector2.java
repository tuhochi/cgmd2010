package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.Serializable;

public class Vector2 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7505462190307942882L;
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
