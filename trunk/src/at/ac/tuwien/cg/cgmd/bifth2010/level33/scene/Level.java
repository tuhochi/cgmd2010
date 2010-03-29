package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;

public class Level {
	
	int world[];
	Vector2i worldDim;
	Vector2i gameCharacterPosition;
	
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
		worldDim = new Vector2i(5,7);
		
		
		// generate radndom wold
		world = new int [worldDim.area()];
		
//		for (int i=0; i<(worldDimX*worldDimY);i++)
//			world[i] = Math.abs( rand.nextInt()) % 2;
		
		// start with hardcoded World
		int x=0;
		
		world[x++]=1;	world[x++]=1;	world[x++]=1;	world[x++]=1; 	world[x++]=1;
		world[x++]=1; 	world[x++]=0;	world[x++]=0;	world[x++]=0; 	world[x++]=1;
		world[x++]=1;	world[x++]=0;	world[x++]=1;	world[x++]=1; 	world[x++]=1;
		world[x++]=1; 	world[x++]=0;	world[x++]=0;	world[x++]=0; 	world[x++]=1;
		world[x++]=1; 	world[x++]=1;	world[x++]=1;	world[x++]=0; 	world[x++]=1;
		world[x++]=1; 	world[x++]=0;	world[x++]=0;	world[x++]=0; 	world[x++]=1;
		world[x++]=1;	world[x++]=1;	world[x++]=1;	world[x++]=1; 	world[x++]=1;
		
		
		gameCharacterPosition = new Vector2i(3,1);

		// surface
		surface=SceneGraph.GEOMETRY_SURFACE_NORMAL;
		
		// wall
		wall=SceneGraph.GEOMETRY_WALL_NORMAL;
	}
	
	
	

}
