package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.tools.StopTimer;
/**
 * The Class SceneGraph
 * @author roman hochstoger & christoph fuchs
 */
public class SceneGraph  {

	public static LevelHandler level;

	static Geometry geometry ;
	public static Camera camera;
	
	static float deltaTime;
	static float deltaTimeCount;
	static long lastFrameStart;
	static int framesSinceLastSecound=0;
	
	public static long timeInSeconds=0;
	
	public final static byte GEOMETRY_WALL = 0;
	public final static byte GEOMETRY_WAY = 1;
	public final static byte GEOMETRY_WORLD = 2;
	
	public final static byte LEFT_ARROW = 5;
	public final static byte DOWN_ARROW = 6;
	public final static byte RIGHT_ARROW = 7;
	public final static byte UP_ARROW = 8;
	
	public final static byte GEOMETRY_STONE = 11;
	public final static byte GEOMETRY_BARREL = 12;
	public final static byte GEOMETRY_TRASH = 13;
	public final static byte GEOMETRY_MAP = 14;
	public final static byte GEOMETRY_SPRING = 15;
	public final static byte GEOMETRY_CHARACTER = 16;
	
	public final static int NONE_SPECIAL_WALL_EDGE = 0;
	public final static int ONE_SPECIAL_WALL_EDGE = -1;
	public final static int TWO_SPECIAL_WALL_EDGE = -2;
	public final static int THREE_SPECIAL_WALL_EDGE = -3;
	public final static int FOUR_SPECIAL_WALL_EDGE = -4;
	public final static int COUNTERPART_SPECIAL_WALL_EDGE = -5;
	
	public final static int NONE_SPECIAL_CORNER = -6;
	public final static int ONE_SPECIAL_CORNER = -7;
	public final static int TWO_SPECIAL_CORNER = -8;
	public final static int THREE_SPECIAL_CORNER = -9;
	public final static int FOUR_SPECIAL_CORNER = -10;
	public final static int COUNTERPART_SPECIAL_CORNER = -11;
	
	public final static int ONE_EDGE_ONE_RIGHT_SPECIAL_CORNER_WALL = -12;
	public final static int ONE_EDGE_ONE_LEFT_SPECIAL_CORNER_WALL = -13;
	public final static int ONE_EDGE_TWO_SPECIAL_CORNER_WALL = -14;
	public final static int TWO_EDGE_ONE_SPECIAL_CORNER_WALL = -15;
	
	public final static int NONE_CONNECTION_WALL = 0;
	public final static int ONE_CONNECTION_WALL = 1;
	public final static int TWO_CONNECTION_WALL = 2;
	public final static int THREE_CONNECTION_WALL = 3;
	public final static int FOUR_CONNECTION_WALL = 4;
	public final static int COUNTERPART_CONNECTION_WALL = 5;
	
	public static LevelActivity activity;
	public  static Vector2f touchDim = new Vector2f(0, 0);
	public  static Vector2i frustumDim = new Vector2i(3, 5);
	private Vector2i frustumMin = new Vector2i(0, 0);
	private Vector2i frustumMax = new Vector2i(0, 0);
	private long secoundCount;
	private TextView tvLevelFps;
	private ImageView ivFullscreenImage;
	private ProgressBar pbProgressBar;
	
	private Vector2f lastPos = new Vector2f(0, 0);
	
	StopTimer frameTimer = new StopTimer();
	
	/**
	 * constructor
	 * @param level is the level to play
	 * @param activity is the GameView 
	 * @param tvLevelFps 
	 */
	
	public SceneGraph(LevelHandler level, LevelActivity activity) {
		this.level = level;
		this.activity = activity;
		this.tvLevelFps = (TextView)activity.findViewById(R.id.l33_level_fps);
		this.ivFullscreenImage = (ImageView)activity.findViewById(R.id.l33_FullscreenImage);
		this.pbProgressBar = (ProgressBar)activity.findViewById(R.id.l33_ProgressBar);
		pbProgressBar.setMax(100);
		


	}

	/**
	 * This Method will initialize the Geometry VBO´s
	 * 
	 * @param gl  OpenGlHandler
	 */
	public static void init(GL10 gl) {

		
		
		Log.d("SceneGraph inti","0");
		
		StopTimer t = new StopTimer();
		SceneGraph.camera = new Camera();
		// load Objects
		InputStream is = SceneGraph.activity.getResources().openRawResource(R.raw.l33_models);
		geometry= GeometryLoader.loadObj(gl, is,R.drawable.l33_textur);
//		ObjModel obj = geometry.GetObjModel();
//		obj.write();
//		
//		ObjModel obj = ObjModel.read("/sdcard/test.out", context);
//		Geometry geometry = new Geometry(gl, obj,R.drawable.l33_textur);

				geometry.render();

		t.logTime("Geometry laden und init dauerte:");
		
	}
	

	

	/**
	 * This Method represent the main render loop of the SceneGraph
	 * 
	 * @param gl
	 */
	public void render(GL10 gl) {
		
		// start time measurement
		framesSinceLastSecound++;
		long currentFrameStart = System.nanoTime();
		deltaTime = (currentFrameStart-lastFrameStart) / 1000000000.0f;
		deltaTimeCount+=deltaTime;
		lastFrameStart = currentFrameStart;		
		if (deltaTimeCount >= 1) {

			
			
			
			activity.runOnUiThread(new Runnable() {public void run() {
					tvLevelFps.setText("fps: "+String.valueOf(framesSinceLastSecound));		
					ivFullscreenImage.setBackgroundResource(R.drawable.l33_nix);
					
					
					
			
			}});
			
			
			timeInSeconds++;
			framesSinceLastSecound = 0;
			deltaTimeCount = 0f;
			secoundCount++;
		}
		
		
		//TODO: Hier noch Gold/Geld/Progress setzen
		pbProgressBar.setProgress((int) timeInSeconds%100);
		
		// Clears the screen and depth buffer.
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
//		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_CULL_FACE);
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		

        gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glDepthMask(true);
		// The type of depth testing to do.
		//gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
//		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);

		
		// updateLogic
		level.updateLogic();

		// upadate Camera
		camera.lookAt(gl);
		
		// now start

		// now render the Scene
		renderScene(gl);
	}

	/**
	 * this method will render the scene elements
	 * @param gl GL10
	 */
	private void renderScene(GL10 gl) {
				
		glMatrixMode(GL_MODELVIEW);
		
		// render world
		
		if(!lastPos.equals(level.gameCharacterPosition)){
			lastPos.set(level.gameCharacterPosition);
			Log.d("pos",lastPos.x+" "+lastPos.y);
		}
		
		// view culling
		if(Camera.zoom==Camera.standardZoom){
		
			frustumMin.set(Math.round(level.gameCharacterPosition.x-frustumDim.x),Math.round(level.gameCharacterPosition.y-frustumDim.y));
			frustumMax.set(frustumMin.x+frustumDim.x*2+1, frustumMin.y+frustumDim.y*2+1);


			// rest
			for(int y=frustumMin.y;y<frustumMax.y;y++){
				for(int x=frustumMin.x;x<frustumMax.x;x++){
					
					int type[] = level.getWorldEntry(x, y);
					if(type[0]!=GEOMETRY_CHARACTER)
					{
					glPushMatrix();
					gl.glTranslatef(x-level.gameCharacterPosition.x,0,y-level.gameCharacterPosition.y);
					
					
					if(type[0]<=GEOMETRY_WALL)
					{
						if(type[2]==ONE_CONNECTION_WALL){
							
							if(type[3]==0)
							geometry.render(25);
							
							else if(type[3]==90)
								geometry.render(26);
							
							else if(type[3]==180)
								geometry.render(27);
							
							else
								geometry.render(28);
						}
						
						if(type[2]==TWO_CONNECTION_WALL){
							
							if(type[3]==0)
							{
								geometry.render(25);
								geometry.render(27);
							}
							else
							{
								geometry.render(26);
								geometry.render(28);
							}	
						}
						
						if(type[2]==THREE_CONNECTION_WALL){
							
							if(type[3]==0)
							{
								geometry.render(26);
								geometry.render(27);
								geometry.render(28);
							}
							else if(type[3]==90)
							{
								geometry.render(25);
								geometry.render(27);
								geometry.render(28);
							}	
							else if(type[3]==180)
							{
								geometry.render(25);
								geometry.render(26);
								geometry.render(28);
							}	
							else
							{
								geometry.render(25);
								geometry.render(26);
								geometry.render(27);
							}	
						}
						
						if(type[2]==FOUR_CONNECTION_WALL){
								geometry.render(25);
								geometry.render(26);
								geometry.render(27);
								geometry.render(28);
						}
							
						
						if(type[1]!=0)
						{
							glPushMatrix();
							gl.glRotatef(type[1], 0, 1, 0);
						}

							 if (type[0]==NONE_SPECIAL_WALL_EDGE)
							geometry.render(10);
						else if (type[0]==ONE_SPECIAL_WALL_EDGE)
							geometry.render(11);
						else if (type[0]==COUNTERPART_SPECIAL_WALL_EDGE)
							geometry.render(12);
						else if (type[0]==THREE_SPECIAL_WALL_EDGE)
							geometry.render(13);
						else if (type[0]==FOUR_SPECIAL_WALL_EDGE)
							geometry.render(14);
						else if (type[0]==TWO_SPECIAL_WALL_EDGE)
							geometry.render(15);
						else if (type[0]==ONE_SPECIAL_CORNER)
							geometry.render(16);
						else if (type[0]==TWO_SPECIAL_CORNER)
							geometry.render(17);
						else if (type[0]==THREE_SPECIAL_CORNER)
							geometry.render(18);
						else if (type[0]==FOUR_SPECIAL_CORNER)
							geometry.render(19);
						else if (type[0]==COUNTERPART_SPECIAL_CORNER)
							geometry.render(20);
						else if (type[0]==ONE_EDGE_ONE_RIGHT_SPECIAL_CORNER_WALL)
							geometry.render(21);
						else if (type[0]==ONE_EDGE_ONE_LEFT_SPECIAL_CORNER_WALL)
							geometry.render(22);
						else if (type[0]==ONE_EDGE_TWO_SPECIAL_CORNER_WALL)
							geometry.render(23);
						else if (type[0]==TWO_EDGE_ONE_SPECIAL_CORNER_WALL)
							geometry.render(24);
							 
							 
						if(type[1]!=0)
							glPopMatrix();
						
					}
					else{

						if(type[0]==GEOMETRY_STONE)
						{
						
							geometry.render(0);
							geometry.render(7);
						}
						else if(type[0]==GEOMETRY_BARREL)
						{
							geometry.render(1);
							geometry.render(7);
						}
						else if(type[0]==GEOMETRY_TRASH)
						{
							geometry.render(2);
							geometry.render(9);
						}
						else if(type[0]==GEOMETRY_MAP)
						{ 	geometry.render(7);
							glPushMatrix();
							gl.glRotatef((System.nanoTime()/50000000.0f)%360, 0, 1, 0);
							geometry.render(3);
							glPopMatrix();
						}
						else if(type[0]==GEOMETRY_SPRING)
						{
							geometry.render(4);
							geometry.render(8);
						}	
						// normal Way
						else {
								geometry.render(6);
								
								// links
								if(type[0]==UP_ARROW){
									geometry.render(29);	
								}
								else if(type[0]==LEFT_ARROW){
									glPushMatrix();
									gl.glRotatef(90, 0, 1, 0);
									geometry.render(29);	
									glPopMatrix();
								}
								else if(type[0]==RIGHT_ARROW){
									glPushMatrix();
									gl.glRotatef(-90, 0, 1, 0);
									geometry.render(29);
									glPopMatrix();
								}
								else if(type[0]==DOWN_ARROW){
									glPushMatrix();
									gl.glRotatef(180, 0, 1, 0);
									geometry.render(29);	
									glPopMatrix();
								}
							
						}
							
					
						
						}
					glPopMatrix();
					}
				}
			}
			
			
			
		}
		
//		// render the whole world
//		else
//		{
//		//	glPushMatrix();
//		//	gl.glTranslatef(level.worldDim.x/2,0,level.worldDim.y/2);
//			
//			
//			for(int y=0;y<level.worldDim.y;y++){
//				for(int x=0;x<level.worldDim.x;x++){
//					//if wall
//					int type = level.getWorldEntry(x, y);
//					
//					glPushMatrix();
//					gl.glTranslatef(x-level.gameCharacterPosition.x,0,y-level.gameCharacterPosition.y);
//						
//							geometry[type].render();	
//							// render way if no wall
//							if(type!=GEOMETRY_WALL)
//								geometry[GEOMETRY_WAY].render();	
//							
//					glPopMatrix();
//					}
//			}
//			
//			geometry[GEOMETRY_CHARACTER].render();
//			
//			//glPopMatrix();
//			return;
//		}

		// render the character
		glPushMatrix();
		geometry.render(5);
		glPopMatrix();
		
	}



}
