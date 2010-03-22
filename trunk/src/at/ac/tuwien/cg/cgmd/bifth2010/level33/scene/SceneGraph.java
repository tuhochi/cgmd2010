package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.GameView;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Cube;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry;

public class SceneGraph {

	Boolean init = false;
	
	Geometry g;// private ArrayList<GeometryGroup> renderables;
	
	Camera camera;
	
	public SceneGraph() {

	}

	// init with gl
	public void init(GL10 gl) {
		camera= new Camera();
		
		
//        g = new Geometry( gl, 6, true, false, false );           
//        
//        g.color( 0, 1, 0, 1 );
//        g.vertex( 0f, -0.5f, -5 );
//        g.color( 0, 1, 0, 1 );
//        g.vertex( 1f, -0.5f, -5 );
//        g.color( 0, 1, 0, 1 );
//        g.vertex( 0.5f, 0.5f, -5 );
//        
//        g.color( 1, 0, 0, 1 );
//        g.vertex( -0.5f, -0.5f, -2 );
//        g.color( 1, 0, 0, 1 );
//        g.vertex( 0.5f, -0.5f, -2 );
//        g.color( 1, 0, 0, 1 );
//        g.vertex( 0, 0.5f, -2);
		g = new Cube(gl);

		
		this.init=true;
	}

	
	public void render(GL10 gl) {
		
		// init?
		if(!init)
			init(gl);
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		// updateLogic
		updateLogic();
		
		// upadate Camera
		camera.update(gl);

		// the main game shod start here
	
		// START DEMO
		
		g.render();
		
		//gl.glClearColor(GameView.lastTouch.x, 0.0f, 0.0f, 1.0f);
		//gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		// END DEMO
		
		// ?? sleep rest of time?

	}

	public void updateLogic() {
		// TODO

	}

}
