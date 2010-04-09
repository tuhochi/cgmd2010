package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector3f;
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
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry.Type;

public class SceneGraph {

	public static LevelHandler level;

	static Geometry[] geometry ;
	public static Camera camera;
	
	static float deltaTime;
	static float deltaTimeCount;
	static long lastFrameStart;
	static int framesSinceLastSecound=0;
	
	public final static byte GEOMETRY_WALL = 0;
	public final static byte GEOMETRY_WAY = 1;
	public final static byte GEOMETRY_WORLD = 2;
	
	public final static byte GEOMETRY_STONE = 11;
	public final static byte GEOMETRY_BARREL = 12;
	public final static byte GEOMETRY_TRASH = 13;
	public final static byte GEOMETRY_MAP = 14;
	public final static byte GEOMETRY_SPRING = 15;
	public final static byte GEOMETRY_CHARACTER = 16;
	
	static Context context;
	private Vector2i frustumDim = new Vector2i(3, 5);
	private Vector2i frustumMin = new Vector2i(0, 0);
	private Vector2i frustumMax = new Vector2i(0, 0);
	private static boolean init = false;
	
	private Vector2f lastPos = new Vector2f(0, 0);

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
		
		
		
//		if(SceneGraph.init)
//			return;
//		SceneGraph.init=true;
			
		
		SceneGraph.camera = new Camera();
		geometry = new Geometry[17];

		// load Objects
		InputStream is = SceneGraph.context.getResources().openRawResource(R.raw.l33_steinmauer);
		InputStream isImage = SceneGraph.context.getResources().openRawResource(R.drawable.l33_steinmauer);
		geometry[GEOMETRY_WALL]= GeometryLoader.loadObj(gl, is,isImage);
		is = SceneGraph.context.getResources().openRawResource(R.raw.l33_way);
		geometry[GEOMETRY_WAY]= GeometryLoader.loadObj(gl, is,isImage);
		
		
		//
	

//			
//		
//		
// 		//System.out.println("ok");
//		
//		
		
		is = SceneGraph.context.getResources().openRawResource(R.raw.l33_character);
		isImage = SceneGraph.context.getResources().openRawResource(R.drawable.l33_character);
		
		geometry[GEOMETRY_CHARACTER]=  GeometryLoader.loadObj(gl, is,isImage);
		
		//geometry[GEOMETRY_WAY]=  new GeometryWay(gl);
		geometry[GEOMETRY_STONE] = new GeometryStone(gl);
		geometry[GEOMETRY_BARREL] = new GeometryBarrel(gl);
		//geometry[GEOMETRY_TRASH] = new GeometryTrash(gl);
		
		
		
		
		
		isImage = SceneGraph.context.getResources().openRawResource(R.drawable.l33_schatz);
		is = SceneGraph.context.getResources().openRawResource(R.raw.l33_schatz);
		geometry[GEOMETRY_TRASH]= GeometryLoader.loadObj(gl, is,isImage);
		
		
		
		
		
		geometry[GEOMETRY_MAP] = new GeometryMap(gl);
		geometry[GEOMETRY_SPRING] = new GeometrySpring(gl);
		
		// init geometry
		for (int i=0;i<geometry.length;i++){
			
			if(geometry[i]!=null)
				geometry[i].render();
		}
//		
	}

	/**
	 * This Method represent the main render loop of the SceneGraph
	 * 
	 * @param gl
	 */
	public void render(GL10 gl) {
	//	Log.d("Frame", "--");
		
		// start time mesherment
		framesSinceLastSecound++;
		long currentFrameStart = System.nanoTime();
		deltaTime = (currentFrameStart-lastFrameStart) / 1000000000.0f;
		deltaTimeCount+=deltaTime;
		lastFrameStart = currentFrameStart;		
		if (deltaTimeCount >= 1) {
			Log.d("fps", String.valueOf(framesSinceLastSecound));
			framesSinceLastSecound = 0;
			deltaTimeCount = 0f;
		}

		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
//		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
//		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		// updateLogic
		level.updateLogic();


		// upadate Camera
		camera.lookAt(gl);

		// now start

		// now render the Scene
		renderScene(gl);

		// ?? sleep rest of time?

	}


	private void renderScene(GL10 gl) {
				
		glMatrixMode(GL_MODELVIEW);
		
		
		
		// render world
		
		if(!lastPos.equals(level.gameCharacterPosition)){
			lastPos.set(level.gameCharacterPosition);
			Log.d("pos",lastPos.x+" "+lastPos.y);
		}
		
		
		
		// easy culling
		//if(Camera.zoom==Camera.standardZoom){
		if(true){
			frustumMin.set(Math.round(level.gameCharacterPosition.x-frustumDim.x),Math.round(level.gameCharacterPosition.y-frustumDim.y));
			frustumMax.set(frustumMin.x+frustumDim.x*2+1, frustumMin.y+frustumDim.y*2+1);

			// whole world
//			for(int y=0;y<level.worldDim.y;y++){
//			for(int x=0;x<level.worldDim.x;x++){
			
			// frustum
			for(int y=frustumMin.y;y<frustumMax.y;y++){
				for(int x=frustumMin.x;x<frustumMax.x;x++){
					
					int type = level.getWorldEntry(x, y);
					
				glPushMatrix();

				gl.glTranslatef(x-level.gameCharacterPosition.x,0,y-level.gameCharacterPosition.y);
			
				geometry[type].render();	
				// render way if no wall
				if(type!=GEOMETRY_WALL)
					geometry[GEOMETRY_WAY].render();
				
				glPopMatrix();
				}
			}
			
			
			
		}
		
		// render the whol world
		else
		for(int y=0;y<level.worldDim.y;y++){
			for(int x=0;x<level.worldDim.x;x++){
				//if wall
				int type = level.getWorldEntry(x, y);
				
				glPushMatrix();
				gl.glTranslatef(x-level.gameCharacterPosition.x,0,y-level.gameCharacterPosition.y);
					
						geometry[type].render();	
						// render way if no wall
						if(type!=GEOMETRY_WALL)
							geometry[GEOMETRY_WAY].render();	
						
				glPopMatrix();
				}
		}
		
		// render GameCaracter
		glPushMatrix();
		//gl.glTranslatef(level.gameCharacterPosition.x-(level.worldDim.x/2),(level.worldDim.y/2)-level.gameCharacterPosition.y, 0);
		geometry[GEOMETRY_CHARACTER].render();			
		glPopMatrix();
		
	}


}
