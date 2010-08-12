package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Rect;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Rectangle;

import java.security.acl.LastOwnerException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * holds game, level and gameloop
 * @author g11
 */
public class Level extends Thread {

    private static final String LOG_TAG = Level.class.getSimpleName();
    /**
     * true if the thread of the level is running
     */
	public boolean _isRunning;
	/**
	 * true, if level/thread is paused
	 */
	private boolean isPaused;
	/**
	 * true, if level was started previously
	 */
    public boolean _isStarted;
    /**
     * true, if level has finished
     */
    public boolean _isFinished;
    /**
     * textures of the level
     */
	private Textures textures;
	/**
	 * sounds of the level
	 */
	private Sounds sounds;
	/**
	 * square onto which the background texture is rendered
	 */
	private Square background;
	/**
	 * maximal time to play
	 */
	private float maxPlayTime;
	/**
	 * ratio, to correct level ratio to screen ratio
	 */
	public static float ratioFix;
	/**
	 * list containing the treasures, lying on the ground
	 */
	private LinkedList<Treasure> treasureList;
	/**
	 * list containing the pedestrians
	 */
	private LinkedList<Pedestrian> pedestrianList;
	/**
	 * level size x in world coordinates
	 */
	public static float sizeX;
	/**
	 * level size y in world coordinates
	 */
	public static float sizeY;
	/**
	 * gl context
	 */
	private GL10 gl;
	/**
	 * android context
	 */
	private Context context;
	/**
	 * displays attraction circle and scoreboard icons
	 */
	private HUD hud;
	
	//public static SoundFile f;
	/**
	 * timing of the level
	 */
	private Timing timing;
	/**
	 * already grabbed treasure of totally collected treasure. used for score calculation
	 */
	private float grabbedTreasureValueOfDeletedTreasures;
	/**
	 * current score
	 */
	private float grabbedTreasureValue;
	/**
	 * interval in seconds, at which a new pedestrian is added
	 */
	private float intervallOfAddingPed = 10;
	/**
	 * time, when last pedestrian was added
	 */
	private float timeOfLastAddingPed = 0;
	//public Rect bounceRect = new Rect();
	

	private static final int level_music = R.raw.l00_menu;
	
	/**
	 * Level constructor, generates level
	 * @param sizeX
	 * @param sizeY
	 */
	public Level(float sizeX, float sizeY) {
		//Log.i(LOG_TAG, "Level(float, float)");
		this.ratioFix = (sizeY/320.0f)/(sizeX/480.0f);
		Level.sizeX = 480.0f;
		Level.sizeY = 320.0f;
		treasureList = new LinkedList<Treasure>();
		pedestrianList = new LinkedList<Pedestrian>();
		this.grabbedTreasureValueOfDeletedTreasures = 0;
		this.grabbedTreasureValue = 0;
		
		this._isRunning = false;
		this.isPaused = false;
		this._isStarted = false;
		
		this.maxPlayTime = 120.00f;
		timing = new Timing();
		timing.start();
		timeOfLastAddingPed = this.timing.getCurrTime();
		timing.pause();
		this.generatePedestrians(10, 40);
		
		hud = new HUD();

	}
	
	/**
	 * initializes level, set openGL context, initializes textures
	 * @param gl
	 * @param context
	 */
	public void init(GL10 gl, Context context) {
		//Log.i(LOG_TAG, "init()");
		 
		this.gl = gl;
		this.context = context;
		//GameAudio gA = new GameAudio();
		// f = gA.createSoundFile(R.raw.l00_gold01);
		
		this.initTextures();
		this.initSounds();
		
	
		
		
		//this.addTreasure(new Treasure(10.0f, 200.0f, new Vector2(200.0f, 200.0f)));
		
		background = new Square();

		//f.play();
		this._isRunning = true;
	}
	/**
	 * initializes sounds
	 */
	public void initSounds() {
		sounds = new Sounds();
		sounds.add(R.raw.l00_gold01);
		sounds.add(R.raw.l11_mine01);
		sounds.add(R.raw.l11_mine02);
		sounds.add(R.raw.l11_mine03);
		sounds.add(R.raw.l11_mine04);
	}
	
	/**
	 * initializes textures
	 */
	public void initTextures() {
		//Log.i(LOG_TAG, "initTextures()");

		if(this.context == null || this.gl == null)
		{
			 Log.i(LOG_TAG, "error: gl or context not set");
		}
		
		textures = new Textures(gl, context);
		
    	textures.add(R.drawable.l11_street_bg);
    	textures.add(R.drawable.l11_pedestrian_arm);
    	textures.add(R.drawable.l11_pedestrian_hand);
    	textures.add(R.drawable.l11_pedestrian_leg);
    	textures.add(R.drawable.l11_pedestrian_torso);
    	textures.add(R.drawable.l11_pedestrian_shadow);
    	textures.add(R.drawable.l11_pedestrian_head);
    	textures.add(R.drawable.l11_pedestrian_hair_01);
    	textures.add(R.drawable.l11_treasure);
    	textures.add(R.drawable.l11_circle);
    	
    	textures.loadTextures();
    	
	}
	
	/**
	 * runs level main loop
	 */
	public void run() {
		this._isStarted = true;
		this.timing.resume();
		GameMusic.play(level_music, true);
		while (_isRunning) {
			while (isPaused) {
			}
			update();
		}
	}
	
	/**
	 * pauses level
	 * @param pause
	 */
	public void pause_level() {
		this.isPaused = true;
		this.timing.pause();
		GameMusic.pause();
	}
	
	/**
	 * resumes level
	 * @param pause
	 */
	public void resume_level() {
		if(this.isPaused)
			this.timing.resume();
		this.isPaused = false;	
		GameMusic.resume();
	}
	
	/**
	 * adds treasure to the level (treasure list)
	 * @param treasure
	 */
	public void addTreasure(Treasure treasure){
		this.treasureList.add(treasure);
	}
	/**
	 * bounces a pedestrian with the given vector
	 * @param x1 first component of beginning of the bounce vector
	 * @param y1 second component of beginning of the bounce vector
	 * @param x2 first component of ending of the bounce vector
	 * @param y2 second component of ending of the bounce vector
	 * @param time delta time of the vector
	 */
	public void bouncePedestrians(float x1, float y1, float x2, float y2, float time){
		//calculate implicit linear equation constants
		//float a = (y2-y1)/(x2-x1);
		//float b = 1;
		//float c = y1-(y2-y1)/(x2-x1)*x1;
		/*bounceRect.left = (int)(Math.min(x1, x2)-pedestrian.getBounceRadius());
		bounceRect.right = (int)(Math.max(x1, x2)+pedestrian.getBounceRadius());
		bounceRect.bottom = (int)(Math.min(y1, y2)-pedestrian.getBounceRadius());
		bounceRect.top = (int)(Math.max(y1, y2)+pedestrian.getBounceRadius());*/
		for (int i=0; i < pedestrianList.size(); i++) {
			Pedestrian pedestrian = pedestrianList.get(i);
			//check if it is in bounding box
			//System.out.println("start: "+x1+"; "+y1+"; end: "+x2+"; "+y2);
			//System.out.println("ped: "+pedestrian.getPosition().toString());
			if(pedestrian.getPosition().x > Math.min(x1, x2)-pedestrian.getBounceRadius()&&
			   pedestrian.getPosition().x < Math.max(x1, x2)+pedestrian.getBounceRadius()&&
			   pedestrian.getPosition().y > Math.min(y1, y2)-pedestrian.getBounceRadius()&&
			   pedestrian.getPosition().y < Math.max(y1, y2)+pedestrian.getBounceRadius()){
				//System.out.println("in bounding box");
				//TODO: distance calculation does not work properly
				//use hesse'sche normal form to calculate distance from pedestrian to line
				//float dist = (float)((a*pedestrian.getPosition().x+b*pedestrian.getPosition().y+c)/
				//				Math.sqrt(a*a+b*b));
				//System.out.println("distance: "+dist);
				//if(pedestrian.getBounceRadius() > dist){
					//System.out.println("hit pedestrian");
					pedestrian.bounce(x1, y1, x2, y2, time);
				//}
			}
		}
	}
	/**
	 * updates level-objects states, such as timing or pedestrians
	 */
	private  void update() {
		//synchronized(this){
		timing.update();
		
		addPedestrian();
		
		updateObjects();

		updateStats();
		
	}
	/**
	 * adds a pedestrain to the level, if intervallOfAddingPed is past
	 */
	private void addPedestrian(){
		if(timeOfLastAddingPed + intervallOfAddingPed < timing.getCurrTime()){
			Pedestrian pedestrian = new Pedestrian(this.gl, this.context);
			if(Math.random()>Level.sizeX/Level.sizeY){
				if(Math.random()>0.5)
					pedestrian.setPosition(new Vector2((float)Math.random()*Level.sizeX, -10.f));
				else
					pedestrian.setPosition(new Vector2((float)Math.random()*Level.sizeX+10.0f, Level.sizeY+10.0f));
			}else{
				if(Math.random()>0.5)
					pedestrian.setPosition(new Vector2(-10.0f, (float)Math.random()*Level.sizeY+10.0f));
				else
					pedestrian.setPosition(new Vector2(Level.sizeX+10.0f, (float)Math.random()*Level.sizeY+10.0f));
				
			}
			timeOfLastAddingPed = timing.getCurrTime();
			pedestrian.update(0, 0);
			this.pedestrianList.add(pedestrian);
		}
	}
	/**
	 * updates all objects positions and states
	 */
	private void updateObjects(){
		
		//System.out.println(timing.getDeltaTime());
		for (int i=0; i < pedestrianList.size(); i++) {//for every pedestrian
			boolean targetChange = false;
			Pedestrian pedestrian = ((Pedestrian)pedestrianList.get(i));
			pedestrian.update(timing.getCurrTime(), timing.getDeltaTime());
			float bestRating = Float.MAX_VALUE;
			float tempDist = 0;
			float rating = 0;
			Treasure bestTreasure = null;
			for (int j=0; j < treasureList.size(); j++){//search the current target
				Treasure treasure = ((Treasure)treasureList.get(j));
				if(treasure.getValue() == 0.0f){
					this.grabbedTreasureValueOfDeletedTreasures += treasure.getStartingValue();
					treasure = null;
					treasureList.remove(j);
					j--;
					
					continue;
				}
				if((rating =
					(tempDist = pedestrian.getPosition().distance(treasure.getPosition()))
					/ (treasure.getValue()+1)) //TODO: determine, how to rate a target
						< bestRating){
					//if(tempDist < pedestrian.getAttractionRadius()+treasure.getAttractionRadius()){
					if (tempDist < treasure.getAttractionRadius()/2) {
						bestTreasure = treasure;
						bestRating = rating;
					}
				}
			}
			if ((pedestrian.getTarget() instanceof Treasure || pedestrian.getTarget() == null) && pedestrian.getTarget() != bestTreasure) {
				if(bestTreasure != null)
					pedestrian.setTarget(bestTreasure);
				targetChange = true;
				pedestrian.playSound();
			}
			if(pedestrian.getTarget() != null){
				for (int j=0; j < pedestrianList.size(); j++) {//check if another one is too close
					Pedestrian otherPedestrian = ((Pedestrian)pedestrianList.get(j));
					if(otherPedestrian != pedestrian){
						//System.out.println(otherPedestrian.getPosition().distance(pedestrian.getPosition()));
						if(otherPedestrian.getPosition().distance(pedestrian.getPosition()) < 20.0f){
							//System.out.println("Too close");
							targetChange = true;
							pedestrian.setTarget(otherPedestrian);
						}
					}
				}
			}
		}
		
	}
	/**
	 * updates game states
	 */
	private void updateStats(){
		//calculate already grabbed treasure value
		this.grabbedTreasureValue = this.grabbedTreasureValueOfDeletedTreasures;
		for (int j=0; j < treasureList.size(); j++){
			this.grabbedTreasureValue += ((Treasure)treasureList.get(j)).getGrabbedValue();
		}
		//}
		((GameActivity)context).setResult(this.getGrabbedTreasureValue());
		if(this.timing.getCurrTime() > this.maxPlayTime || this.grabbedTreasureValue>100) {
			this._isRunning = false;
			this._isFinished = true;
			showOutroDialog.sendEmptyMessage(0);
		}
		
	}
	/**
	 * generates specific amount of pedestrians with a minimal distance to each other
	 * @param amount
	 * @param minDist
	 */
	private void generatePedestrians(int amount, float minDist){
		Random rand = new Random();
		Vector2 pos = new Vector2();
		while(this.pedestrianList.size() < amount){
			pos.x = rand.nextFloat() * Level.sizeX;
			pos.y = rand.nextFloat() * Level.sizeY;
			Iterator<Pedestrian>it = this.pedestrianList.iterator();
			boolean posAccepted = true;
			while(it.hasNext()){
				if(pos.distance(it.next().getPosition()) < minDist){
					posAccepted = false;
					break;
				}
			}	
			if(posAccepted){
				Pedestrian pedestrian = new Pedestrian(this.gl, this.context);
				pedestrian.setPosition(pos.clone());
				this.pedestrianList.add(pedestrian);
			}
		}
		updateObjects();
	}
	
	/**
	 * draws the level and all objects (treasure and pedestrians)
	 * @param gl
	 */
	public void draw(GL10 gl) {
		// draw floor background image
		textures.setTexture(R.drawable.l11_street_bg);

		gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		gl.glPushMatrix();
			gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
			gl.glTranslatef(160.0f, -240.0f, 0.0f);
			gl.glScalef(320.0f, 480.0f, 1.0f);
			background.draw(gl);
		gl.glPopMatrix();
		
	
		gl.glEnable(GL10.GL_BLEND);
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		if(HUD.singleton.isDrawTouchTreasureCircle())
		 HUD.singleton.draw(gl);

		for (int i=0; i < treasureList.size(); i++){
			((Treasure)treasureList.get(i)).draw(gl);
		}	
		for (int i=0; i < pedestrianList.size(); i++) {
			((Pedestrian)pedestrianList.get(i)).draw(gl);
		}
		
		HUD.singleton.draw_ontop(gl);

		gl.glDisable(GL10.GL_BLEND);
		
	}

	/**
	 * a handler to be able to show the outro dialog
	 */
	 private Handler showOutroDialog = new Handler() {
		 	@Override
	    	public void handleMessage(Message msg) {
		 		OutroDialog resultdialog = new OutroDialog(context, grabbedTreasureValue >100 );
	    		resultdialog.show();	
	    	}
	    };
	/**
	 * returns amount of grabbed treasures
	 * @return
	 */
	public float getGrabbedTreasureValue(){
		return this.grabbedTreasureValue;
	}
	
	/**
	 * returns level time left in seconds
	 * @return
	 */
	public float getRemainigTime(){
		return this.maxPlayTime-this.timing.getCurrTime();
	}
	/**
	 * sets the maximal play time to time. You can also use it to limit the time after resuming the game.
	 * @param time
	 */
	public void setMaxTime(float time){
		this.maxPlayTime = time;
	}
	/**
	 * returns the level's treasure list for purpose of saving
	 * @return
	 */
	public LinkedList<Treasure> getTreasureList(){
		return this.treasureList;
	}
	/**
	 * sets the level's treasure list for the purpose of loading
	 * @param tl
	 */
	public void setTreasureList(LinkedList<Treasure> tl){
		this.treasureList = tl;
	}
	/**
	 * returns the level's pedestrian list for purpose of saving
	 * @return
	 */
	public LinkedList<Pedestrian> getPedestrianList(){
		return this.pedestrianList;
	}
	/**
	 * sets the level's pedestrian list for the purpose of loading
	 * @param tl
	 */
	public void setPedestrianList(LinkedList<Pedestrian> pl){
		this.pedestrianList = pl;
	}
	/**
	 * returns the level's grabbedTreasureValueOfDeletedTreasures for purpose of saving
	 * @return
	 */
	public float getGrabbedTreasureValueOfDeletedTreasures() {
		return grabbedTreasureValueOfDeletedTreasures;
	}
	/**
	 * sets the level's grabbedTreasureValueOfDeletedTreasures for the purpose of loading
	 * @param grabbedTreasureValueOfDeletedTreasures
	 */
	public void setGrabbedTreasureValueOfDeletedTreasures(
			float grabbedTreasureValueOfDeletedTreasures) {
		this.grabbedTreasureValueOfDeletedTreasures = grabbedTreasureValueOfDeletedTreasures;
	}

	/**
	 * returns the level's timing for purpose of saving
	 * @return
	 */
	public Timing getTiming() {
		return timing;
	}
	/**
	 * sets the level's timing for the purpose of loading
	 * @param timing
	 */
	public void setTiming(Timing timing) {
		this.timing = timing;
	}

	/**
	 * returns the level's gl for purpose of load
	 * @return
	 */
	public GL10 getGL(){
		return this.gl;
	}
}
