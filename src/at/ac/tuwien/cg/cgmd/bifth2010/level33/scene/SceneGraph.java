package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GameCharacter;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryBarrel;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryMap;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometrySpring;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryStone;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryTrash;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryWall;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryWay;

public class SceneGraph {

	public static LevelHandler level;

	static Geometry[] geometry ;
	public static Camera camera;
	
	static float deltaTime;
	static long lastFrameStart;
	
	public final static byte GEOMETRY_WALL = 0;
	public final static byte GEOMETRY_WAY = 1;
	
	public final static byte GEOMETRY_STONE = 2;
	public final static byte GEOMETRY_BARREL = 3;
	public final static byte GEOMETRY_TRASH = 4;
	public final static byte GEOMETRY_MAP = 5;
	public final static byte GEOMETRY_SPRING = 6;
	public final static byte GEOMETRY_CHARACTER = 7;
	
	private boolean N;
	private boolean O;
	private boolean S;
	private boolean W;
	static Context context;
	
	public static boolean zoomOutView = false; // if false use standard zoom for playing, if true zoom out

	public SceneGraph(LevelHandler level, Context context) {
		this.level = level;
		this.context = context;
	}

	/**
	 * This Method will init the Geometry VBO´s
	 * 
	 * @param gl  OpenGlHandler
	 */
	public static void init(GL10 gl) {
		SceneGraph.camera = new Camera();
		
		geometry = new Geometry[8];
//		geometry[GEOMETRY_WALL]= new GeometryWall(gl);
		
		
		
		// object loader
		InputStream is = SceneGraph.context.getResources().openRawResource(R.raw.l33_steinmauer);
		InputStream isImage = SceneGraph.context.getResources().openRawResource(R.drawable.l33_steinmauer);
 		Geometry steinmauer = GeometryLoader.loadObj(gl, is,isImage);
 		geometry[GEOMETRY_WALL]= steinmauer;
 		//System.out.println("ok");
		
		
		
		
		geometry[GEOMETRY_CHARACTER]=  new GameCharacter(gl);
		
		geometry[GEOMETRY_WAY]=  new GeometryWay(gl);
		
		geometry[GEOMETRY_STONE] = new GeometryStone(gl,geometry[GEOMETRY_WAY]);
		geometry[GEOMETRY_BARREL] = new GeometryBarrel(gl,geometry[GEOMETRY_WAY]);
		geometry[GEOMETRY_TRASH] = new GeometryTrash(gl,geometry[GEOMETRY_WAY]);
		geometry[GEOMETRY_MAP] = new GeometryMap(gl,geometry[GEOMETRY_WAY]);
		geometry[GEOMETRY_SPRING] = new GeometrySpring(gl,geometry[GEOMETRY_WAY]);
		
		

		
		
		
		
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
		
		// if Caracter is near the end of the world render word twice, quatro
		int viewRange= 6;
		
		
		for(int y=0;y<level.worldDim.y;y++){
			for(int x=0;x<level.worldDim.x;x++){
				//if wall
				int id=y*level.worldDim.x+x;
//				if(level.world[id]<=GEOMETRY_STONE)
//				{
				// move the word
				glPushMatrix();
				gl.glTranslatef(-(level.gameCharacterPosition.x-(level.worldDim.x/2)),-((level.worldDim.y/2)-level.gameCharacterPosition.y), 0);
					// move the wall position
					glPushMatrix();
					
						// center world
						gl.glTranslatef(x-(level.worldDim.x/2),(level.worldDim.y/2)-y, 0);
						geometry[level.world[id]].render();	
						
						if(true)//if(camera.zoom==camera.standardZoom)
						{
							
							// endless copy clipping
							N = level.gameCharacterTargetPosition.y<viewRange-1;
							O = level.gameCharacterTargetPosition.x>level.worldDim.x-viewRange;
							S = level.gameCharacterTargetPosition.y>level.worldDim.y-viewRange;
							W = level.gameCharacterTargetPosition.x<viewRange-1;
							
							if(N)
							{
								// N
								glPushMatrix();
								gl.glTranslatef(0,level.worldDim.y, 0);
								geometry[level.world[id]].render();	
								glPopMatrix();
							}
							
							if(N&&O)
							{
							// N-O
							glPushMatrix();
							gl.glTranslatef(level.worldDim.x,level.worldDim.y, 0);
							geometry[level.world[id]].render();	
							glPopMatrix();
							}
							
							if(O)
							{
								// O
								glPushMatrix();
								gl.glTranslatef(level.worldDim.x,0, 0);
								geometry[level.world[id]].render();	
								glPopMatrix();
							}
							
							if(O&&S)
							{
							// O-S
							glPushMatrix();
							gl.glTranslatef(level.worldDim.x,-level.worldDim.y, 0);
							geometry[level.world[id]].render();	
							glPopMatrix();
							}
							
							if(S)
							{
								// S
								glPushMatrix();
								gl.glTranslatef(0,-level.worldDim.y, 0);
								geometry[level.world[id]].render();	
								glPopMatrix();
							}
						
							if(S&&W)
							{
							// S-W
							glPushMatrix();
							gl.glTranslatef(-level.worldDim.x,-level.worldDim.y, 0);
							geometry[level.world[id]].render();	
							glPopMatrix();
							}
							
							if(W)
							{
								// W
								glPushMatrix();
								gl.glTranslatef(-level.worldDim.x,0, 0);
								geometry[level.world[id]].render();	
								glPopMatrix();
							}
							
							if(N&&W)
							{
							// N-W
							glPushMatrix();
							gl.glTranslatef(-level.worldDim.x,level.worldDim.y, 0);
							geometry[level.world[id]].render();	
							glPopMatrix();
							}
							
						}		
					glPopMatrix();
				glPopMatrix();
//				}
			} 
		}
		
		// render GameCaracter
		glPushMatrix();
		//gl.glTranslatef(level.gameCharacterPosition.x-(level.worldDim.x/2),(level.worldDim.y/2)-level.gameCharacterPosition.y, 0);
		geometry[GEOMETRY_CHARACTER].render();			
		glPopMatrix();
		
	}

}
