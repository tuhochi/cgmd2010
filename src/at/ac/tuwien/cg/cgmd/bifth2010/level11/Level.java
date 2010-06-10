package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level84.ResultDialog;

import java.security.acl.LastOwnerException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * holds game, level and gameloop
 * @author felix.fleisz
 */
public class Level extends Thread {

    private static final String LOG_TAG = Level.class.getSimpleName();
	public boolean _isRunning;
	private boolean isPaused; 
    public boolean _isActive;
    public boolean _isStarted;
    public boolean _isFinished;
    
	private Textures textures;
	private Sounds sounds;
	
	
	private Pedestrian pedestrian;
	
	private Square background;
	
	private float maxPlayTime;
	
	public static float ratioFix;
	

	private LinkedList<Treasure> treasureList;
	private LinkedList<Pedestrian> pedestrianList;
	public static float sizeX;
	public static float sizeY;
	private GL10 gl;
	private Context context;
	
	//public static SoundFile f;
	
	private Timing timing;
	private float grabbedTreasureValueOfDeletedTreasures;
	private float grabbedTreasureValue;
	private float intervallOfAddingPed = 10;
	private float timeOfLastAddingPed = 0;
	
	/**
	 * Level constructor
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
		this._isActive = true;
		this._isStarted = false;
		
		this.maxPlayTime = 120;
		timing = new Timing();
		timing.start();
		timing.pause();
		this.generatePedestrians(10, 40);

	}
	
	/**
	 * inits level, generates level, set opengl contex, init textures
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
	
	public void initSounds() {
		sounds = new Sounds();
		sounds.add(R.raw.l00_gold01);
		sounds.add(R.raw.l00_menu);
	}
	
	/**
	 * inits textures
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
    	
    	//textures.add(pedestrian.hair.hair_02_texture_id);
    	//textures.add(pedestrian.hair.hair_03_texture_id);
	}
	
	/**
	 * runs level main loop
	 */
	public void run() {
		this._isStarted = true;
		this.timing.resume();
		this.timing.update();
		timeOfLastAddingPed = this.timing.getCurrTime();
		while (_isActive) {
			while (_isRunning) {
				while (isPaused) {
					/*try {
						sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}*/
				}
				update();
				/*try {
					sleep(0);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
	}
	
	/**
	 * pauses level
	 * @param pause
	 */
	public void pause_level() {
		this.isPaused = true;
		this.timing.pause();
	}
	
	/**
	 * resumes level
	 * @param pause
	 */
	public void resume_level() {
		//this.timing.resume();
		this.isPaused = false;	
	}
	
	/**
	 * adds treasure to the treasurelist
	 * @param treasure
	 */
	public  void addTreasure(Treasure treasure){
		this.treasureList.add(treasure);
	}
	public void bouncePedestrians(float x1, float y1, float x2, float y2, float time){
		//calculate implicit linear equation constants
		//float a = (y2-y1)/(x2-x1);
		//float b = 1;
		//float c = y1-(y2-y1)/(x2-x1)*x1;
		for (int i=0; i < pedestrianList.size(); i++) {
			Pedestrian pedestrian = pedestrianList.get(i);
			//check if it is in bounding box
			System.out.println("start: "+x1+"; "+y1+"; end: "+x2+"; "+y2);
			System.out.println("ped: "+pedestrian.getPosition().toString());
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
					System.out.println("hit pedestrian");
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
	private void addPedestrian(){
		if(timeOfLastAddingPed + intervallOfAddingPed < timing.getCurrTime()){
			Pedestrian pedestrian = new Pedestrian(this.gl, this.context);
			this.pedestrianList.add(pedestrian);
			if(Math.random()>Level.sizeX/Level.sizeY){
				if(Math.random()>0.5)
					pedestrian.setPosition(new Vector2((float)Math.random()*Level.sizeX, 0));
				else
					pedestrian.setPosition(new Vector2((float)Math.random()*Level.sizeX, Level.sizeY));
			}else{
				if(Math.random()>0.5)
					pedestrian.setPosition(new Vector2(0, (float)Math.random()*Level.sizeY));
				else
					pedestrian.setPosition(new Vector2(Level.sizeX, (float)Math.random()*Level.sizeY));
				
			}
			timeOfLastAddingPed = timing.getCurrTime();
		}
	}
	private void updateObjects(){
		//System.out.println(timing.getDeltaTime());
		for (int i=0; i < pedestrianList.size(); i++) {//for every pedestrian
			Pedestrian pedestrian = ((Pedestrian)pedestrianList.get(i));
			pedestrian.update(timing.getCurrTime(), timing.getDeltaTime());
			float bestRating = Float.MAX_VALUE;
			float tempDist = 0;
			float rating = 0;
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
					if(tempDist < pedestrian.getAttractionRadius()+treasure.getAttractionRadius()){
						pedestrian.setTarget(treasure);
						bestRating = rating;
					}
				}
			}
			if(pedestrian.getTarget() != null){
				for (int j=0; j < pedestrianList.size(); j++) {//check if another one is too close
					Pedestrian otherPedestrian = ((Pedestrian)pedestrianList.get(j));
					if(otherPedestrian != pedestrian){
						//System.out.println(otherPedestrian.getPosition().distance(pedestrian.getPosition()));
						if(otherPedestrian.getPosition().distance(pedestrian.getPosition()) < 20.0f){
							//System.out.println("Too close");
							pedestrian.setTarget(otherPedestrian);
						}
					}
				}
			}
		}
	}
	private void updateStats(){
		//calc already grabbed treasure value
		this.grabbedTreasureValue = this.grabbedTreasureValueOfDeletedTreasures;
		for (int j=0; j < treasureList.size(); j++){
			this.grabbedTreasureValue += ((Treasure)treasureList.get(j)).getGrabbedValue();
		}
		//}
		((GameActivity)context).setResult(this.getGrabbedTreasureValue());
		if(this.timing.getCurrTime() > this.maxPlayTime || this.grabbedTreasureValue>100) {
			this._isRunning = false;
			this._isActive = false;
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
	 * draws the level
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

		for (int i=0; i < treasureList.size(); i++){
			((Treasure)treasureList.get(i)).draw(gl);
		}	
		for (int i=0; i < pedestrianList.size(); i++) {
			((Pedestrian)pedestrianList.get(i)).draw(gl);
		}
		gl.glDisable(GL10.GL_BLEND);
		
	}

	
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
