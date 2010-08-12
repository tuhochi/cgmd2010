package at.ac.tuwien.cg.cgmd.bifth2010.level83;

/**
 * A convenience class to generate unique IDs.
 * @author Manuel Keglevic, Thomas Schulz
 */
public class IDGenerator {

	private static int count=0;
	
	/**
	 * Generates a new unique ID.
	 * 
	 * @return	A unique ID.
	 */
	public static int generateID(){
		count++;
		return count;
	}
}
