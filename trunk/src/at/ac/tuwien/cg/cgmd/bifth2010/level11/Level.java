package at.ac.tuwien.cg.cgmd.bifth2010.level11;


import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.util.Log;


public class Level extends Thread {

    private static final String LOG_TAG = Level.class.getSimpleName();
	private boolean isRunning;
	private boolean isPaused; 
	
	private Textures textures;
	
	private Pedestrian pedestrian;
	
	private Square background;
	
	private float maxPlayTime;
	

	private LinkedList<Treasure> treasureList;
	private LinkedList<Pedestrian> pedestrianList;
	public float sizeX;
	public float sizeY;
	private GL10 gl;
	private Context context;
	
	private Timing timing;
	private float grabbedTreasureValueOfDeletedTreasures;
	private float grabbedTreasureValue;
	
	public Level(float sizeX, float sizeY) {
		//Log.i(LOG_TAG, "Level(float, float)");
		
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		treasureList = new LinkedList<Treasure>();
		pedestrianList = new LinkedList<Pedestrian>();
		this.grabbedTreasureValueOfDeletedTreasures = 0;
		this.grabbedTreasureValue = 0;
		
		this.isRunning = true;
		this.isPaused = false;
		
		timing = new Timing();
		this.maxPlayTime = 60;

	}
	
	
	public void init(GL10 gl, Context context) {
		//Log.i(LOG_TAG, "init()");
		 
		this.gl = gl;
		this.context = context;
		
		this.initTextures();
		
		this.generatePedestrians(5, 40);
		//this.addTreasure(new Treasure(10.0f, 200.0f, new Vector2(200.0f, 200.0f)));
		
		background = new Square();
		
	}
	
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
	
	
	public void run() {
		
		while (isRunning) {
			while (isPaused && isRunning) {
				/*try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
			update();
		}
	}
	public  void pause(boolean pause){
		this.isPaused = pause;
	}
	public  void addTreasure(Treasure treasure){
		this.treasureList.add(treasure);
	}
	private  void update() {
		//synchronized(this){
			timing.update();
			for (int i=0; i < pedestrianList.size(); i++) {//for every pedestrian
				Pedestrian pedestrian = ((Pedestrian)pedestrianList.get(i));
				pedestrian.update(timing.getCurrTime());
				float bestRating = Float.MAX_VALUE;
				float tempDist = 0;
				float rating = 0;
				for (int j=0; j < treasureList.size(); j++){//search the cuurent target
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
						if(tempDist < pedestrian.getAttractionRadius()+treasure.getAttracktionRadius()){
							pedestrian.setTargetTreasure(treasure);
							bestRating = rating;
						}
					}
				}
				for (int j=0; j < pedestrianList.size(); j++) {//check if another one is too close
					Pedestrian otherPedestrian = ((Pedestrian)pedestrianList.get(j));
					if(otherPedestrian != pedestrian){
						//System.out.println(otherPedestrian.getPosition().distance(pedestrian.getPosition()));
						if(otherPedestrian.getPosition().distance(pedestrian.getPosition()) < 20.0f){
							//System.out.println("Too close");
							pedestrian.setTargetTreasure(otherPedestrian);
						}
					}
				}
			}
			//calc already grabbed treasure value
			this.grabbedTreasureValue = this.grabbedTreasureValueOfDeletedTreasures;
			for (int j=0; j < treasureList.size(); j++){
				this.grabbedTreasureValue += ((Treasure)treasureList.get(j)).getGrabbedValue();
			}
		//}
		if(this.timing.getCurrTime() > this.maxPlayTime)
			this.isRunning = false;
	}
	private void generatePedestrians(int amount, float minDist){
		Random rand = new Random();
		Vector2 pos = new Vector2();
		while(this.pedestrianList.size() < amount){
			pos.x = rand.nextFloat() * this.sizeX;
			pos.y = rand.nextFloat() * this.sizeY;
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
	}
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
	public float getGrabbedTreasureValue(){
		return this.grabbedTreasureValue;
	}
	public float getRemainigTime(){
		return this.maxPlayTime-this.timing.getCurrTime();
	}
}
