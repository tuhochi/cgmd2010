package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glMultMatrixf;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.GameView;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Cube;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GameCharacter;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry;

public class SceneGraph {

	public static Level level;

	static Geometry g; // private ArrayList<GeometryGroup> renderables;
	static Geometry c;
	public static Camera camera;
	
	static float deltaTime;
	static long lastFrameStart;
	
	public final static byte GEOMETRY_SURFACE_NORMAL =0;
	public final static byte GEOMETRY_WALL_NORMAL =1;

	public static boolean zoomOutView = false; // if false use standard zoom for playing, if true zoom out

	public SceneGraph(Level level) {
		this.level = level;
	}

	/**
	 * This Method will init the Geometry VBO´s
	 * 
	 * @param gl
	 *            OpenGlHandler
	 */
	public static void init(GL10 gl) {
		SceneGraph.camera = new Camera();
		g = new Cube(gl);
		c = new GameCharacter(gl);
		
	}

	/**
	 * This Method represent the main render loop of the SceneGraph
	 * 
	 * @param gl
	 */
	public void render(GL10 gl) {
		
		// start time mesherment
		long currentFrameStart = System.nanoTime();
		deltaTime = (currentFrameStart-lastFrameStart) / 1000000000.0f;
		lastFrameStart = currentFrameStart;		

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
		level.updateLogic();

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		// upadate Camera
		camera.lookAt(gl);

		// now start



		// now render the Scene
		renderScene(gl);

		// ?? sleep rest of time?

	}


	private void renderScene(GL10 gl) {
		//this.level
		
		
		glMatrixMode(GL_MODELVIEW);
		
		// render world
		for(int y=0;y<level.worldDim.y;y++){
			for(int x=0;x<level.worldDim.x;x++){
				
				
				//if wall
				int id=y*level.worldDim.x+x;
				if(level.world[id]==level.wall)
				{
					glPushMatrix();
					gl.glTranslatef(x-(level.worldDim.x/2),(level.worldDim.y/2)-y, 0);
					g.render();					
					glPopMatrix();
				}
			} 
		}
		
		// render GameCaracter
		glPushMatrix();
		gl.glTranslatef(level.gameCharacterPosition.x-(level.worldDim.x/2),(level.worldDim.y/2)-level.gameCharacterPosition.y, 0);
		c.render();					
		glPopMatrix();
		
	}

}
