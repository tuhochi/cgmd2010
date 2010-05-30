package at.ac.tuwien.cg.cgmd.bifth2010.level83;

/**
 * A convenience class for defining which path the character can take.
 * @author Manuel Keglevic, Thomas Schulz
 */
public class MyWaypoint {

	public static MyWaypoint[][] waypoints = { 
								{new MyWaypoint(0, 1, false),
									new MyWaypoint(1,1,false),
									new MyWaypoint(1, 0, true),
									new MyWaypoint(1, -1, true),
									new MyWaypoint(1, -2, true),
									new MyWaypoint(1, -3, true),
									new MyWaypoint(1, -4, true)},
								
								{new MyWaypoint(0, 2, false),
									new MyWaypoint(1, 1, true),
									new MyWaypoint(1, 0, false),
									new MyWaypoint(2, 0, true)},
								
								{new MyWaypoint(0, 2, false),
									new MyWaypoint(1, 2, false),
									new MyWaypoint(2, 1, true),
									new MyWaypoint(1, 0, false),
									new MyWaypoint(3, 0, true)},
									
								{new MyWaypoint(0, 3, false),
									new MyWaypoint(1, 2, true)},
									
								{new MyWaypoint(0, 3, false),
									new MyWaypoint(1, 3, false),
									new MyWaypoint(2, 2, true)},
									
								{new MyWaypoint(0, 4, false),
									new MyWaypoint(1, 3, true)}		};
																
	
	public int x,y;
	public boolean endpoint;

	/**
	 * Creates a new waypoint.
	 * @param x - x map coordinate 
	 * @param y - y map coordinate
	 * @param endpoint - true if walkable, false otherwise
	 */
	public MyWaypoint(int x, int y, boolean endpoint) {
		this.x = x;
		this.y = y;
		this.endpoint = endpoint;
	}

	public String toString(){
		return "("+x+","+y+") "+endpoint;
	}
}
