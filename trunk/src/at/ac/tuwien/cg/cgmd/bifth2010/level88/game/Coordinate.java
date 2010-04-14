package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

//Einfache Klasse um die Coordinaten im Game zu beschreiben


/**
 * Class for coordinates
 * @author Asperger, Radax
 */
public class Coordinate {

	//nur in 2D
	public int x;
	public int y;
	
	
	/**
	 * Constructor
	 * @param newX x-coordinate
	 * @param newY y-coordinate
	 */
	public Coordinate(int newX, int newY) {
		x = newX;
		y = newY;
	}
	
	/**
	 * Comparison of coordinates
	 * @param other coordinate to compare with
	 * @return false if the coordinates are not equal
	 */
	public boolean equals(Coordinate other){
		if(x == other.x && y == other.y){
			return true;
		}
		
		return false;
	}
	
	//Fuer Debugingzwecke
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Coordinates: (" + x + "," + y + ")";
	}
	
}
