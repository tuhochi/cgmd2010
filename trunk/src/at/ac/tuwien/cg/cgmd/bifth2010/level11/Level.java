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
	
	

	private LinkedList<Treasure> treasureList;
	private LinkedList<Pedestrian> pedestrianList;
	private float sizeX;
	private float sizeY;
	private GL10 gl;
	private Context context;
	
	public Level(float sizeX, float sizeY) {
		Log.i(LOG_TAG, "Level(float, float)");
		
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		treasureList = new LinkedList<Treasure>();
		pedestrianList = new LinkedList<Pedestrian>();
		
		this.isRunning = true;
		this.isPaused = false;

	}
	
	
	public void init(GL10 gl, Context context) {
		Log.i(LOG_TAG, "init()");
		 
		this.gl = gl;
		this.context = context;
		
		this.initTextures();
		
		this.generatePedestrians(6, 40);
		
		background = new Square();
		
	}
	
	public void initTextures() {
		Log.i(LOG_TAG, "initTextures()");

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
	public synchronized void pause(boolean pause){
		this.isPaused = pause;
	}
	public synchronized void addTreasure(Treasure treasure){
		this.treasureList.add(treasure);
	}
	private void update() {
		synchronized(this){
			for (int i=0; i < pedestrianList.size(); i++) {
				((Pedestrian)pedestrianList.get(i)).update();
			}
		}
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
		gl.glLoadIdentity();
		gl.glRotatef(90.0f, 0.0f, 0.0f, 1.0f);
		gl.glTranslatef(160.0f, -240.0f, 0.0f);
		gl.glScalef(320.0f, 480.0f, 1.0f);
		background.draw(gl);
		
		//gl.glEnable(GL10.GL_BLEND);
		//gl.glDisable(GL10.GL_CULL_FACE);
	
		//gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		//gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		for (int i=0; i < pedestrianList.size(); i++) {
			((Pedestrian)pedestrianList.get(i)).draw(gl);
		}

		gl.glDisable(GL10.GL_BLEND);
	}
	
}
