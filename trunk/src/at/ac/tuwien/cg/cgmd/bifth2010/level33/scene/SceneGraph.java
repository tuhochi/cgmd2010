package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;

import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2f;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.Vector2i;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.GeometryLoader;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.ObjModel;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.tools.StopTimer;
/**
 * The Class SceneGraph
 * @author roman hochstoger & christoph fuchs
 */
public class SceneGraph  {

	public static LevelHandler level;

	static Geometry geometry;
	public static Camera camera;
	
	static float deltaTime;
	static float deltaTimeCount;
	static long lastFrameStart;
	static int framesSinceLastSecound=0;
	
	public static long timeInSeconds=0;
	public static int levelEndTimeInSeconds=0;
	private int gameTimeInSeconds=180;
	private float maxTranslation = 5.0f;
	private float startItemAlpha = 0.90f;
	private boolean playingFinalSound=true;
	
	public final static byte GEOMETRY_WALL = 0;
	public final static byte GEOMETRY_WAY = 1;
	public final static byte GEOMETRY_WORLD = 2;
	
	public final static byte ARROW_LEFT = 5;
	public final static byte ARROW_DOWN = 6;
	public final static byte ARROW_RIGHT = 7;
	public final static byte ARROW_UP = 8;
	
	public final static byte ARROW_BOTTOM_TO_RIGHT = 21;
	public final static byte ARROW_RIGHT_TO_TOP = 22;
	public final static byte ARROW_TOP_TO_LEFT = 23;
	public final static byte ARROW_LEFT_TO_BOTTOM = 24;
	
	public final static byte ARROW_BOTTOM_TO_LEFT = 25;
	public final static byte ARROW_LEFT_TO_TOP = 26;
	public final static byte ARROW_TOP_TO_RIGHT = 27;
	public final static byte ARROW_RIGHT_TO_BOTTOM = 28;
	
	public final static byte GEOMETRY_STONE = 11;
	public final static byte GEOMETRY_BARREL = 12;
	public final static byte GEOMETRY_TRASH = 13;
	public final static byte GEOMETRY_MAP = 14;
	public final static byte GEOMETRY_SPRING = 15;
	public final static byte GEOMETRY_CHARACTER = 16;
	
	public final static int EDGE_NONE_SPECIAL_WALL = 0;
	public final static int EDGE_ONE_SPECIAL_WALL = -1;
	public final static int EDGE_TWO_SPECIAL_WALL = -2;
	public final static int EDGE_THREE_SPECIAL_WALL = -3;
	public final static int EDGE_FOUR_SPECIAL_WALL = -4;
	public final static int EDGE_COUNTERPART_SPECIAL_WALL = -5;
	
	public final static int CORNER_NONE_SPECIAL = -6;
	public final static int CORNER_ONE_SPECIAL = -7;
	public final static int CORNER_TWO_SPECIAL = -8;
	public final static int CORNER_THREE_SPECIAL = -9;
	public final static int CORNER_FOUR_SPECIAL = -10;
	public final static int CORNER_COUNTERPART_SPECIAL = -11;
	
	public final static int SPECIAL_ONE_EDGE_ONE_RIGHT_CORNER_WALL = -12;
	public final static int SPECIAL_ONE_EDGE_ONE_LEFT_CORNER_WALL = -13;
	public final static int SPECIAL_ONE_EDGE_TWO_CORNER_WALL = -14;
	public final static int SPECIAL_TWO_EDGE_ONE_CORNER_WALL = -15;
	
	public final static int CONNECTION_NONE_WALL = 0;
	public final static int CONNECTION_ONE_WALL = 1;
	public final static int CONNECTION_TWO_WALL = 2;
	public final static int CONNECTION_THREE_WALL = 3;
	public final static int CONNECTION_FOUR_WALL = 4;
	public final static int CONNECTION_COUNTERPART_WALL = 5;
	
	public static LevelActivity activity;
	public  static Vector2f touchDim = new Vector2f(0, 0);
	public  static Vector2i frustumDim = new Vector2i(3, 5);
	private Vector2i frustumMin = new Vector2i(0, 0);
	private Vector2i frustumMax = new Vector2i(0, 0);
	private long secoundCount;
	private TextView tvLevelFps;
	private ImageView ivFullscreenImage;
	private ProgressBar pbProgressBar;
	private TextView tvLevelTime;
	private TextView tvGoodyCount;
	private ImageView ivGoodyCountIcon;
	private TextView tvMapTime;
	private ImageView ivMapTimeIcon;
	private ImageView ivItemIcon;
	private ImageButton ibGeneralViewButton;
	public static ImageButton ibPathButton;
	public static TextView tvPathCount;
	private static TextView tvLoadingScreen;
	private ProgressBar pbLoadingBar;
	String collectedMapText="";
	
	private LinearLayout lTutorialTop;
	private TableLayout lTtutorialTabel;
	private LinearLayout lTutorialBottom;
	private TableLayout lLevelTableTop;
	private TableLayout lLevelTableMiddel;
	
	private Vector2f lastPos = new Vector2f(0, 0);
	private float lastZoom;
	public static Vector2f cameraTranslation = new Vector2f(0,0);
	
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
		this.tvLevelTime = (TextView)activity.findViewById(R.id.l33_level_time);
		this.tvGoodyCount = (TextView)activity.findViewById(R.id.l33_goodies_Count);
		this.tvMapTime = (TextView)activity.findViewById(R.id.l33_map_timer);
		this.ibGeneralViewButton = (ImageButton)activity.findViewById(R.id.l33_Map_Image_Button);
		this.ivItemIcon = (ImageView)activity.findViewById(R.id.l33_item_icon);
		this.ivMapTimeIcon = (ImageView)activity.findViewById(R.id.l33_map_timer_icon);
		this.ibPathButton = (ImageButton)activity.findViewById(R.id.l33_Path_Image_Button);
		this.tvPathCount = (TextView)activity.findViewById(R.id.l33_path_counter);
		
		this.lTutorialTop = (LinearLayout)activity.findViewById(R.id.l33_TutorialTop);
		this.lTtutorialTabel = (TableLayout)activity.findViewById(R.id.l33_TutorialTableLayout);
		this.lTutorialBottom = (LinearLayout)activity.findViewById(R.id.l33_TutorialBottom);
		this.tvLoadingScreen = (TextView)activity.findViewById(R.id.l33_loadingScreen_statusText);
		this.pbLoadingBar = (ProgressBar)activity.findViewById(R.id.l33_loading_bar);
		
		this.lLevelTableTop = (TableLayout)activity.findViewById(R.id.l33_TableLayoutTop);
		this.lLevelTableMiddel = (TableLayout)activity.findViewById(R.id.l33_TableLayoutMiddle);
		
		ivMapTimeIcon.setImageResource(R.drawable.l33_arrow_small);
		ivItemIcon.setImageResource(R.drawable.l33_stone_small);
		ibGeneralViewButton.setImageResource(R.drawable.l33_overview_map);
		ibPathButton.setImageResource(R.drawable.l33_map);
		pbProgressBar.setMax(100);
		
		//EventListener
		//GeneralView
		ibGeneralViewButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	
	            	SceneGraph.camera.switchZoom();
	            }
	        });
		//ShortestPath
		ibPathButton.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	            	
	            	if(LevelHandler.numberOfGoodGoodies<=0)
	            		return;
	            	LevelHandler.mapCalculationThread = new MapCalculationThread(SceneGraph.level, LevelHandler.worldDim.x);
					LevelHandler.mapCalculationThread.setStartProperties(LevelHandler.gameCharacterField, LevelHandler.goodiesIndex, LevelHandler.worldEntry);
					LevelHandler.mapCalculationThread.start();
					LevelHandler.isMapThreadStarted = true;
					
					LevelHandler.collectedMap--;
					if(LevelHandler.collectedMap==0)
					{
						SceneGraph.activity.runOnUiThread(new Runnable() {public void run() {
							SceneGraph.ibPathButton.setVisibility(ImageButton.INVISIBLE);
							SceneGraph.tvPathCount.setVisibility(TextView.INVISIBLE);	
							SceneGraph.tvPathCount.setText(collectedMapText);
						}});
					}
					else
					{
						if(LevelHandler.collectedMap<10)
							collectedMapText=" x 0"+LevelHandler.collectedMap;
						else
							collectedMapText=" x "+LevelHandler.collectedMap;
						SceneGraph.activity.runOnUiThread(new Runnable() {public void run() {	
							SceneGraph.tvPathCount.setText(collectedMapText);
						}});
					}
	            }
	        });

		levelEndTimeInSeconds = (int)timeInSeconds+gameTimeInSeconds;

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
		SceneGraph.camera.reset();
		// load Objects
		ObjModel obj;
		
		StopTimer tload = new StopTimer();

		obj = ObjModel.read(R.raw.l33_model, activity);
		tload.logTime("Geometry laden dauerte:");
		geometry = new Geometry(gl, obj,R.drawable.l33_textur);
		tload.logTime("Textur laden dauerte:");
		
	

	
//			// save Object
//			String path="/sdcard/modelOut.out";
//			InputStream is = SceneGraph.activity.getResources().openRawResource(R.raw.l33_models);
//			geometry= GeometryLoader.loadObj(gl, is,R.drawable.l33_textur);
//			obj = geometry.GetObjModel();
//			obj.write(path);
//			Log.e("write","ok");
//		
//		}
			
			
		
	
		//geometry.render();
		tload.logTime("und die Initialisierung der Geometry dauerte:");

		t.logTime("Geometry laden und init dauerte:");
		
	}
	

	public void loadingComplete() {
		activity.runOnUiThread(new Runnable() {public void run() {
			tvLoadingScreen.setText(R.string.l33_tutorial_startGame);
			//pbLoadingBar.setVisibility(ProgressBar.INVISIBLE);
		}});
	}
	
	
	public void switchView() {
		
		activity.runOnUiThread(new Runnable() {public void run() {
			lTutorialTop.setVisibility(LinearLayout.INVISIBLE);
			lTtutorialTabel.setVisibility(TableLayout.INVISIBLE);
			lTutorialBottom.setVisibility(LinearLayout.INVISIBLE);
			
			lLevelTableTop.setVisibility(TableLayout.VISIBLE);
			lLevelTableMiddel.setVisibility(TableLayout.VISIBLE);
		}});
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

			timeInSeconds++;
			
			deltaTimeCount = 0f;
			secoundCount++;
			gameTimeInSeconds--;
			if(LevelHandler.mapIsActiveTimer>0)
				LevelHandler.mapIsActiveTimer--;
			if(timeInSeconds<=levelEndTimeInSeconds || !LevelActivity.progressHandler.isLevelCompleted || LevelHandler.numberOfGoodGoodies!=0)
			{
				activity.runOnUiThread(new Runnable() {public void run() {
					//FPS
					tvLevelFps.setText("fps: "+String.valueOf(framesSinceLastSecound));		
					ivFullscreenImage.setBackgroundResource(R.drawable.l33_nix);
					
					//Time 
					int minutes=gameTimeInSeconds/60;
					int seconds=gameTimeInSeconds%60;
					int goodyIconCount = gameTimeInSeconds%10;
					
					if(minutes<0 || seconds<0)
					{
						minutes=0;
						seconds=0;
					}
					String text_LevelTime;
					if(seconds<10)
						text_LevelTime = minutes+" min 0"+seconds+" sec";
					else
						text_LevelTime = minutes+" min "+seconds+" sec";
								
					tvLevelTime.setText(text_LevelTime);
					
					//Goodies-Count
					String text_GoodyCount="";
					if(goodyIconCount==9 || goodyIconCount==0)
					{
						if(LevelGenration.numberOfStone<10)
							text_GoodyCount = "x 0"+LevelGenration.numberOfStone;
						else
							text_GoodyCount = "x "+LevelGenration.numberOfStone;
						//StoneIcon
						ivItemIcon.setImageResource(R.drawable.l33_stone_small);
					}
					else if(goodyIconCount==8 || goodyIconCount==7)
					{
						if(LevelGenration.numberOfBarrel<10)
							text_GoodyCount = "x 0"+LevelGenration.numberOfBarrel;
						else
							text_GoodyCount = "x "+LevelGenration.numberOfBarrel;
						//BarrelIcon
						ivItemIcon.setImageResource(R.drawable.l33_barrel_small);
					}
					else if(goodyIconCount==6 || goodyIconCount==5)
					{
						if(LevelGenration.numberOfSpring<10)
							text_GoodyCount = "x 0"+LevelGenration.numberOfSpring;
						else
							text_GoodyCount = "x "+LevelGenration.numberOfSpring;
						//SpringIcon
						ivItemIcon.setImageResource(R.drawable.l33_spring_small);
					}
					else if(goodyIconCount==4 || goodyIconCount==3)
					{
						if(LevelGenration.numberOfTrashes<10)
							text_GoodyCount = "x 0"+LevelGenration.numberOfTrashes;
						else
							text_GoodyCount = "x "+LevelGenration.numberOfTrashes;
						//TrashIcon
						ivItemIcon.setImageResource(R.drawable.l33_trash_small);
					}
					else if(goodyIconCount==2 || goodyIconCount==1)
					{
						if(LevelGenration.numberOfMaps<10)
							text_GoodyCount = "x 0"+LevelGenration.numberOfMaps;
						else
							text_GoodyCount = "x "+LevelGenration.numberOfMaps;
						//MapIcon
						ivItemIcon.setImageResource(R.drawable.l33_map_small);
					}
					tvGoodyCount.setText(text_GoodyCount);
					
					//Path-Time
					if(LevelHandler.mapIsActive)
					{
						String text_MapTimer;
						
						if(LevelHandler.mapIsActiveTimer<10)
							text_MapTimer = " 0"+LevelHandler.mapIsActiveTimer;
						else
							text_MapTimer = " "+LevelHandler.mapIsActiveTimer;
						tvMapTime.setText(text_MapTimer);
						tvMapTime.setVisibility(TextView.VISIBLE);
						ivMapTimeIcon.setVisibility(ImageView.VISIBLE);
					}
					else
					{
						tvMapTime.setVisibility(TextView.INVISIBLE);
						ivMapTimeIcon.setVisibility(ImageView.INVISIBLE);
					}
						
				}});
			}
			framesSinceLastSecound = 0;
		}
		
		
		if(LevelHandler.isFirstMap && LevelHandler.collectedMap==-1)
		{
			activity.runOnUiThread(new Runnable() {public void run() {
				ibGeneralViewButton.setVisibility(ImageButton.VISIBLE);		
			}});
			
			LevelHandler.collectedMap=0;
		}
		
		
		
		//Set the progress
		
		pbProgressBar.setProgress(LevelActivity.progressHandler.getActualllyGold());
		
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
		
		//BlendFunc
		gl.glBlendFunc(gl.GL_SRC_ALPHA, gl.GL_ONE_MINUS_SRC_ALPHA);
		
		

        gl.glDepthFunc(GL10.GL_LEQUAL);
		gl.glDepthMask(true);
		// The type of depth testing to do.
		//gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
//		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		
		
		
		
		/* TODO
		if(timeInSeconds>levelEndTimeInSeconds || LevelActivity.progressHandler.isLevelCompleted || LevelHandler.numberOfGoodGoodies==0)
		{
			//GAME OVER
			if(playingFinalSound)
			{
				LevelActivity.soundHandler.playSoundEffect(SoundHandler.SoundEffect.FINAL);
				playingFinalSound=false;
			}
			//TODO
			//Anzeige
		}
		else
		{
			// updateLogic
			level.updateLogic();
		}*/
		
		// updateLogic
		if (Camera.zoom == Camera.standardZoom)
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
		gl.glPushMatrix();
		
        if(Camera.zoom==Camera.outZoom)
        {
                	//float y = ((LevelActivity.lastTouch.y*2)-1)*25;
                	//float x = ((LevelActivity.lastTouch.x*2)-1)*25;
                	gl.glTranslatef(cameraTranslation.x, 0, cameraTranslation.y);
                //	Log.d("out","zoom");
        }
		
		// render world
		
		// if Position or frustumDim changed -> renew frustum min/max
		if(!lastPos.equals(level.gameCharacterPosition)){

			// renew frustum-min/max
			frustumMin.set(Math.round(level.gameCharacterPosition.x-frustumDim.x),Math.round(level.gameCharacterPosition.y-frustumDim.y));
			frustumMax.set(frustumMin.x+frustumDim.x*2+1, frustumMin.y+frustumDim.y*2+1);
			
			Log.d("pos",lastPos.x+" "+lastPos.y);
		}
		
		// frustum/view culling
		if (Camera.zoom == Camera.standardZoom) {

			// render Entry«s
			for (int y = frustumMin.y; y < frustumMax.y; y++) {
				for (int x = frustumMin.x; x < frustumMax.x; x++) {

					renderEntry(x, y, gl);
				}
			}
			
			// render Character
			glPushMatrix();
			gl.glRotatef(level.characterRotaion, 0, 1, 0);
			geometry.render(5);
			glPopMatrix();

			// render collected Items
			renderItems(gl);

		}

		// render overview
		else {
			
//			if(lastZoom==Camera.zoom)
//				return;
			
		//	Log.d("_","render overview");

			for (int y = 0; y < level.worldDim.y; y++) {
				for (int x = 0; x < level.worldDim.x; x++) {

					renderEntry(x, y, gl);

					
				}
			}
			// render Character
			glPushMatrix();
			gl.glRotatef(level.characterRotaion, 0, 1, 0);
			geometry.render(5);
			glPopMatrix();
			
			// render collected Items
			renderItems(gl);

		}
		
		lastPos.set(level.gameCharacterPosition);
		lastZoom=Camera.zoom;
		
		gl.glPopMatrix();

	}

	/**
	 * this method render a Entry on given Position x,y
	 * @param x Position
	 * @param y Position
	 * @param gl OpenGL GL10
	 */
	private void renderEntry(int x, int y, GL10 gl) {
	
		
		int type[] = level.getWorldEntry(x, y);
		
		glPushMatrix();
		
		gl.glTranslatef(x-level.gameCharacterPosition.x,0,y-level.gameCharacterPosition.y);
		
		
		if(type[0]<=GEOMETRY_WALL)
		{
			if(type[2]==CONNECTION_ONE_WALL){
				
				if(type[3]==0)
				geometry.render(25);
				
				else if(type[3]==90)
					geometry.render(26);
				
				else if(type[3]==180)
					geometry.render(27);
				
				else
					geometry.render(28);
			}
			
			if(type[2]==CONNECTION_TWO_WALL){
				
				if(type[3]==0)
				{
					geometry.render(25);
					geometry.render(28);
				}
				else if(type[3]==90)
				{
					geometry.render(26);
					geometry.render(25);
				}
				else if(type[3]==180)
				{
					geometry.render(26);
					geometry.render(27);
				}
				else 
				{
					geometry.render(27);
					geometry.render(28);
				}
			}
			
			if(type[2]==CONNECTION_COUNTERPART_WALL){
				
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
			
			if(type[2]==CONNECTION_THREE_WALL){
				
				if(type[3]==0)
				{
					geometry.render(25);
					geometry.render(27);
					geometry.render(28);
				}
				else if(type[3]==90)
				{
					geometry.render(25);
					geometry.render(26);
					geometry.render(28);
				}	
				else if(type[3]==180)
				{
					geometry.render(25);
					geometry.render(26);
					geometry.render(27);
				}	
				else
				{
					geometry.render(26);
					geometry.render(28);
					geometry.render(27);
				}	
			}
			
			if(type[2]==CONNECTION_FOUR_WALL){
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
	
				 if (type[0]==EDGE_NONE_SPECIAL_WALL)
				geometry.render(10);
			else if (type[0]==EDGE_ONE_SPECIAL_WALL)
				geometry.render(11);
			else if (type[0]==EDGE_COUNTERPART_SPECIAL_WALL)
				geometry.render(12);
			else if (type[0]==EDGE_THREE_SPECIAL_WALL)
				geometry.render(13);
			else if (type[0]==EDGE_FOUR_SPECIAL_WALL)
				geometry.render(14);
			else if (type[0]==EDGE_TWO_SPECIAL_WALL)
				geometry.render(15);
			else if (type[0]==CORNER_ONE_SPECIAL)
				geometry.render(16);
			else if (type[0]==CORNER_TWO_SPECIAL)
				geometry.render(17);
			else if (type[0]==CORNER_THREE_SPECIAL)
				geometry.render(18);
			else if (type[0]==CORNER_FOUR_SPECIAL)
				geometry.render(19);
			else if (type[0]==CORNER_COUNTERPART_SPECIAL)
				geometry.render(20);
			else if (type[0]==SPECIAL_ONE_EDGE_ONE_RIGHT_CORNER_WALL)
				geometry.render(21);
			else if (type[0]==SPECIAL_ONE_EDGE_ONE_LEFT_CORNER_WALL)
				geometry.render(22);
			else if (type[0]==SPECIAL_ONE_EDGE_TWO_CORNER_WALL)
				geometry.render(23);
			else if (type[0]==SPECIAL_TWO_EDGE_ONE_CORNER_WALL)
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
					if(type[0]==ARROW_UP){
						geometry.render(29);	
					}
					else if(type[0]==ARROW_LEFT){
						glPushMatrix();
						gl.glRotatef(90, 0, 1, 0);
						geometry.render(29);	
						glPopMatrix();
					}
					else if(type[0]==ARROW_RIGHT){
						glPushMatrix();
						gl.glRotatef(-90, 0, 1, 0);
						geometry.render(29);
						glPopMatrix();
					}
					else if(type[0]==ARROW_DOWN){
						glPushMatrix();
						gl.glRotatef(180, 0, 1, 0);
						geometry.render(29);	
						glPopMatrix();
					}
					//Corners
					else if(type[0]==ARROW_BOTTOM_TO_RIGHT){
						geometry.render(31);	
					}
					else if(type[0]==ARROW_RIGHT_TO_TOP){
						glPushMatrix();
						gl.glRotatef(90, 0, 1, 0);
						geometry.render(31);	
						glPopMatrix();
					}
					else if(type[0]==ARROW_TOP_TO_LEFT){
						glPushMatrix();
						gl.glRotatef(180, 0, 1, 0);
						geometry.render(31);	
						glPopMatrix();
					}
					else if(type[0]==ARROW_LEFT_TO_BOTTOM){
						glPushMatrix();
						gl.glRotatef(270, 0, 1, 0);
						geometry.render(31);	
						glPopMatrix();
					}
					else if(type[0]==ARROW_BOTTOM_TO_LEFT){
						geometry.render(30);	
					}
					else if(type[0]==ARROW_RIGHT_TO_BOTTOM){
						glPushMatrix();
						gl.glRotatef(90, 0, 1, 0);
						geometry.render(30);	
						glPopMatrix();
					}
					else if(type[0]==ARROW_TOP_TO_RIGHT){
						glPushMatrix();
						gl.glRotatef(180, 0, 1, 0);
						geometry.render(30);	
						glPopMatrix();
					}
					else if(type[0]==ARROW_LEFT_TO_TOP){
						glPushMatrix();
						gl.glRotatef(270, 0, 1, 0);
						geometry.render(30);	
						glPopMatrix();
					}
				
			}
				
		
			
			}
		glPopMatrix();
	
		
	}

	/**
	 * this method render the collected Items
	 * @param gl OpenGl GL10
	 */
	private void renderItems(GL10 gl) {
		
		// if nothing to render -> return
		if (LevelHandler.collectedItemList == null)
			return;
		
		for(int i=0;i<LevelHandler.collectedItemList.size();i++)
		{
			float[] translatedItem = LevelHandler.collectedItemList.get(i);
			float itemHigh = translatedItem[2];
			float itemAlpha = startItemAlpha-itemHigh*(startItemAlpha/maxTranslation);
			if(level.isFieldInFrustum((int)translatedItem[0], frustumMin, frustumMax))
			{
				Vector2i position = level.getWorldCoordinate((int)translatedItem[0]);
				glPushMatrix();
				
				//Blending
				gl.glEnable(GL10.GL_BLEND);
				gl.glColor4f(1.0f, 1.0f, 1.0f, itemAlpha); 
				
				gl.glTranslatef(position.x-level.gameCharacterPosition.x,itemHigh,position.y-level.gameCharacterPosition.y);
				gl.glRotatef((System.nanoTime()/25000000.0f)%360, 0, 1, 0);
				
				if(translatedItem[1]==GEOMETRY_STONE)
				{
					geometry.render(0);
				}
				else if(translatedItem[1]==GEOMETRY_BARREL)
				{
					geometry.render(1);
				}
				else if(translatedItem[1]==GEOMETRY_TRASH)
				{
					geometry.render(2);
				}
				else if(translatedItem[1]==GEOMETRY_MAP)
				{
					glPushMatrix();
					gl.glRotatef((System.nanoTime()/50000000.0f)%360, 0, 1, 0);
					geometry.render(3);
					glPopMatrix();
				}
				else if(translatedItem[1]==GEOMETRY_SPRING)
				{
					geometry.render(4);
				}	
				gl.glDisable(GL10.GL_BLEND);
				glPopMatrix();
			}
			itemHigh = itemHigh+(2*deltaTime);
			translatedItem[2]=itemHigh;
			if(LevelHandler.collectedItemList.get(i)[2]>maxTranslation)
				LevelHandler.collectedItemList.remove(i);
		}
	}

	/**
	 * this method moves the Camera in the overvie map view
	 * @param x translation diff
	 * @param y translation diff
	 */
	public static void moveCamera(float x, float y) {
		
		// calculate visible view dim xx yy
		float xx =(float) (Camera.zoom/Math.sqrt(2)*(LevelActivity.resolution.x/LevelActivity.resolution.y));
		float yy = xx/(LevelActivity.resolution.x/LevelActivity.resolution.y);
		
		SceneGraph.cameraTranslation.subtract(new Vector2f(x*xx,y*xx));

		float dx = (int) (xx/2);
		float dy = (int) (yy/2);
		
		Log.e("cameraTranlation",""+(SceneGraph.cameraTranslation.x));
		Log.e("gameCharacterPosition",""+(level.gameCharacterPosition.x));
		Log.e("xx",""+(xx));
		Log.e("WorldDim",""+(level.worldDim.x));
		
		Log.e("diff",""+(level.gameCharacterPosition.x-SceneGraph.cameraTranslation.x));
		
		// left stop position
		if(level.gameCharacterPosition.x-SceneGraph.cameraTranslation.x<dx)
			SceneGraph.cameraTranslation.x=-dx+level.gameCharacterPosition.x;
		// right stop position
		if(level.gameCharacterPosition.x-SceneGraph.cameraTranslation.x>level.worldDim.x-dx-1)
			SceneGraph.cameraTranslation.x=-level.worldDim.x+dx+level.gameCharacterPosition.x+1;
		// upper stop position
		if(level.gameCharacterPosition.y-SceneGraph.cameraTranslation.y<dy)
			SceneGraph.cameraTranslation.y=-dy+level.gameCharacterPosition.y;
		// lover stop position
		if(level.gameCharacterPosition.y-SceneGraph.cameraTranslation.y>level.worldDim.y-dy-1)
			SceneGraph.cameraTranslation.y=-level.worldDim.y+dy+level.gameCharacterPosition.y+1;

	}



}
