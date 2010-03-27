package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.Random;

public class Level {
	
	int world[];
	int worldDimX;
	int worldDimY;
	
	byte surface;
	byte wall;
	Random rand= new Random();

	public Level() {

		generateLevel();
		
	}

	/**
	 * this method generate the Level 
	 */
	private void generateLevel() {
		
		// dimension
		worldDimX=10;
		worldDimY=10;
		
		
		// generate wold
		
		world = new int [worldDimX*worldDimY];
		for (int i=0; i<(worldDimX*worldDimY);i++)
			world[i] = Math.abs( rand.nextInt()) % 2;
		
		

		// surface
		surface=SceneGraph.GEOMETRY_SURFACE_NORMAL;
		
		// wall
		wall=SceneGraph.GEOMETRY_WALL_NORMAL;
	}
	
	
	

}
