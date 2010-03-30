package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

//Einfache Klasse um die Coordinaten im Game zu beschreiben


/**
 * @author Asperger, Radax
 *
 */
public class Coordinate {

	//nur in 2D
	public int x;
	public int y;
	
	
	public Coordinate(int newX, int newY) {
		x = newX;
		y = newY;
	}
	
	public boolean equals(Coordinate other){
		if(x == other.x && y == other.y){
			return true;
		}
		
		return false;
	}
	
	//Fuer Debugingzwecke
	public String toString(){
		return "Coordinates: (" + x + "," + y + ")";
	}
	
}
