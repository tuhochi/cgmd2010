package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

import android.view.MotionEvent;
import android.view.SurfaceView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class BoozyControl {

	private int gameTime = 300;

	private GameObject avatar;
	private List<GameObject> gameObjects;
	private List<Puke> pukeObjects;
	private Random rnd ;
	
	private int beerTicker = 0;
	private boolean dry = true;
	
	public boolean isDrunk(){
		return !this.dry;
	}
	
	public BoozyControl(){
		gameObjects = new LinkedList<GameObject>();
		rnd = new Random();
		
	}
	
	
	public void initialSetup(){
		avatar = new Player();
		gameObjects.add(avatar);
		createInitalBeer();
		createPuke();
	}
	
	public void createPuke(){
		pukeObjects = new ArrayList<Puke>();
		for(int i = 0; i < 3; i++){
			
			Puke pukeObject = new Puke();
			pukeObject.setPos(avatar.getPos().x, avatar.getPos().y);
			pukeObject.setActive(false);
			pukeObjects.add(pukeObject);
			gameObjects.add(pukeObject);
		}
	}
	
	public void activatePuke(){
		for (int i = 0; i < pukeObjects.size(); i++){
			if (pukeObjects.get(i).getActive() == false){
				pukeObjects.get(i).setActive(true);
				pukeObjects.get(i).reset();
				pukeObjects.get(i).setPos(avatar.getPos().x, avatar.getPos().y);
			}
		}
	}
	
	
	public void createInitalBeer(){
		
		for (int i = 0; i < 10; i++){
			
			GameObject beerObj = new Beer();
			float posX = 1.5f+rnd.nextFloat()*2;
			float posY = 1.5f+rnd.nextFloat()*3;
			boolean xPositive = rnd.nextBoolean();
			boolean yPositive = rnd.nextBoolean();
			
			
			//beer.setPos(3, 4);
			if (xPositive)
				posX *= -1;
			if (yPositive)
				posY *= -1;
			beerObj.setPos(posX,posY);
			gameObjects.add(beerObj);
		}
	}
	
	
	
	public void run(GL10 gl){
		
		
		
		CollisionHandler.handleScreenBoundaryCheck(avatar);
		CollisionHandler.responseLoop(gameObjects);
		
		beerTicker+= 5*CollisionHandler.checkBeerPlayerCollisionCount();
		
		if(dry == false)
			beerTicker--;
		if (beerTicker > 40)
			dry = false;
			
		if(beerTicker == 0)
			dry = true;
		
		for (int i = 0; i < pukeObjects.size();i++){
			if(pukeObjects.get(i).getActive()){
				pukeObjects.get(i).increaseAge();
				if(pukeObjects.get(i).getAge() == 0){
					pukeObjects.get(i).setActive(false);
				}
			}
		}
			
		
		drawAndUpdate(gl, gameObjects);
	}
	
	public void drawAndUpdate(GL10 gl,List<GameObject> gameObjects){
		for (int i = 0; i < gameObjects.size(); i++){
			GameObject cObj = gameObjects.get(i);
			//check if object is still active
			if (cObj.getActive()){
				cObj.update();
				
				cObj.draw(gl);
			}else if(cObj instanceof Beer){
				generateRandomMoneyLossEvent(cObj);
				}
			}
		
		gameTime -= 1;
		if (gameTime == 0){
			//Exit
		
		}
	}
	
	public void generateRandomMoneyLossEvent(GameObject obj){
		
		//TODO: Random event generation
			
				GameObject gameObj = obj;
			
			float posX = 1.5f+rnd.nextFloat()*2;
			float posY = 1.5f+rnd.nextFloat()*3;
			boolean xPositive = rnd.nextBoolean();
			boolean yPositive = rnd.nextBoolean();
			
			
			if (xPositive)
				posX *= -1;
			if (yPositive)
				posY *= -1;
			
			gameObj.setPos(posX, posY);
			gameObj.setActive(true);
			
		}
	
	public void handleAvatarControls(MotionEvent event,SurfaceView surfView){
		
		float currentX = event.getX();
		float currentY = event.getY();
		
		if(event.getAction() == MotionEvent.ACTION_UP){
			int upAreaY = surfView.getHeight()/10;
			int downAreaY = surfView.getHeight()-upAreaY;
			int leftAreaX = surfView.getWidth()/10;
			int rightAreaX = surfView.getWidth()-leftAreaX;
			
			if (dry){		
				if (currentY < upAreaY){
					avatar.setSpeed(0, 0.1f);
				}else if(currentY > downAreaY){
					avatar.setSpeed(0, -0.1f);
				}else if(currentX < leftAreaX){
					avatar.setSpeed(-0.1f, 0);
				}else if(currentX > rightAreaX){
					avatar.setSpeed(0.1f, 0);
				}else{
					avatar.stop();
				}
			}else{
			
				if (currentY < upAreaY){
					avatar.setSpeed(0, -0.1f);
				}else if(currentY > downAreaY){
					avatar.setSpeed(0, 0.1f);
				}else if(currentX < leftAreaX){
					avatar.setSpeed(0.1f, 0);	
				}else if(currentX > rightAreaX){
					avatar.setSpeed(-0.1f, 0);
					
				}
					activatePuke();
			
				
				
			}
			
			
	}
	}	

	//Refactor when GameObject is refactored
	public void loadTexturesByObjectTypes(GL10 gl,Context context){
	for(int i = 0; i < gameObjects.size();i++){
		GameObject curObj = gameObjects.get(i);
		if (curObj instanceof Player)
			curObj.loadGLTexture(gl, context, R.drawable.l00_rabit_256);
		else if (curObj instanceof Beer)
			curObj.loadGLTexture(gl, context, R.drawable.l13_beer);
		else if (curObj instanceof Puke)
			curObj.loadGLTexture(gl, context,R.drawable.l13_puke);
	}
	}
	
	
}
