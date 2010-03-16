package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class Level extends Thread {

	private boolean isRunning;
	private boolean isPaused;
	private LinkedList<Treasure> treasureList;
	private LinkedList<Pedestrian> pedestrianList;
	private float sizeX;
	private float sizeY;
	private GL10 gl;
	private Context context;
	public Level(float sizeX, float sizeY) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		treasureList = new LinkedList<Treasure>();
		pedestrianList = new LinkedList<Pedestrian>();
	}
	
	public void run() {
		while (isRunning) {
			while (isPaused && isRunning) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
	private void draw() {
		
	}
}
