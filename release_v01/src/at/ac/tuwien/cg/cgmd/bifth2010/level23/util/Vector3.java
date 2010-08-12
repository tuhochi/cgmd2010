package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import java.io.Serializable;
/**
 * This class implements an 3-dimensional vector
 * @author Markus Ernst
 * @author Florian Felberbauer
 *
 */
public class Vector3 implements Serializable {

	
	private static final long serialVersionUID = 4672024666143605699L;
	
	/**
	 * the x,y and z data fields
	 */
	public float x,y,z; 
	
	/**
	 * Default Constructor
	 */
	public Vector3() {
		
	}
	
	/**
	 * Constructor
	 * @param x the x-coordinate
	 * @param y the y-coordinate
	 * @param z the z-coordinate
	 */
	public Vector3(float x, float y, float z) {
		this.x = x; 
		this.y = y; 
		this.z = z; 
	}
}
