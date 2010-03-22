package at.ac.tuwien.cg.cgmd.bifth2010.level88;

//Einfache Klasse um die Coordinaten im Game zu beschreiben


/**
 * @author Asperger, Radax
 *
 */
public class Coordinate {

	//nur in 2D
	public int l88x;
	public int l88y;
	
	
	public Coordinate(int newX, int newY) {
		l88x = newX;
		l88y = newY;
	}
	
	public boolean equals(Coordinate other){
		if(l88x == other.l88x && l88y == other.l88y){
			return true;
		}
		
		return false;
	}
	
	//Fuer Debugingzwecke
	public String toString(){
		return "Coordinates: (" + l88x + "," + l88y + ")";
	}
	
}
