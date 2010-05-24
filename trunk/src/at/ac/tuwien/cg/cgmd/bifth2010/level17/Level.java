package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Random;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.HouseModel;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Quad;
import at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee.Util;

/**
 * Represents the level in the game. Holds the player, houses and birds
 * @author MaMa
 *
 */
public class Level {

	private HouseModel[] mHouseModels = new HouseModel[5];
	//private Vector3 mPosition = new Vector3(0, 0, 0);
	private List<House> mHouses = new ArrayList<House>();
	private List<House> mFadeHouses = new ArrayList<House>();
	private List<Bird> mBirds = new ArrayList<Bird>();
	private List<ParticleSystem> mParticleSystems = new ArrayList<ParticleSystem>();
	private Vector3 mSpeed = new Vector3(0, -20.0f, 0);
	private float mBlockSize = 5.0f;
	private float mNextHouse = 0;
	private float mNextBird = 0;
	private Player mPlayer;
	private Quad mBird;
	private Quad mParticle;
	private ForceField mForceField1;
	private ForceField mForceField2;
	//private NormalModeWorld mWorld;
	private Random mRandom = new Random();
	private List<Integer> mHouseTex = new ArrayList<Integer>();
	
	public static final String PLAYER_LIFES = "feelGood";
	public static final String PLAYER_MONEY = "muney";
	public static final String LEVEL_SPEED = "whuuuiii";
	
	
	/**
	 * Level Class for Rendering Houses and other Objects.
	 */
	public Level(NormalModeWorld world, Bundle savedInstance)
	{
		int money = 0;
		int lifes = 30;
		
		if(savedInstance != null)
		{
			if(savedInstance.containsKey(PLAYER_MONEY))
				money = savedInstance.getInt(PLAYER_MONEY);
			if(savedInstance.containsKey(PLAYER_LIFES))
				lifes = savedInstance.getInt(PLAYER_LIFES);
			if(savedInstance.containsKey(LEVEL_SPEED))
				mSpeed.y = savedInstance.getFloat(LEVEL_SPEED);
		}

        mPlayer = new Player(new Vector3(0f, 30f, 0f), 1f, lifes, money);
		//mWorld = world;
		for(int i = 0; i < 5; i++)
			mHouseModels[i] = new HouseModel(mBlockSize, mBlockSize * ((float)i + 1.0f), mBlockSize, i+1);
		
		mBird = new Quad(3.0f,3.0f);
        GLManager.getInstance().getTextures().add(R.drawable.l17_house0);
        GLManager.getInstance().getTextures().add(R.drawable.l17_house1);
        GLManager.getInstance().getTextures().add(R.drawable.l17_house2);
        GLManager.getInstance().getTextures().add(R.drawable.l17_house3);
        GLManager.getInstance().getTextures().add(R.drawable.l17_vogel);
        GLManager.getInstance().getTextures().add(R.drawable.l17_bg);
        GLManager.getInstance().getTextures().add(R.drawable.l17_forcefield);
        GLManager.getInstance().getTextures().add(R.drawable.l17_coin);
        GLManager.getInstance().getTextures().add(R.drawable.l17_bricks);

        mHouseTex.add(R.drawable.l17_house0);
        mHouseTex.add(R.drawable.l17_house1);
        mHouseTex.add(R.drawable.l17_house2);
        mHouseTex.add(R.drawable.l17_house3);
        
        mForceField1 = new ForceField(50f, new Vector3(0,0,0), 200f, 30f);
        mForceField2 = new ForceField(50f, new Vector3(0,0,0), 200f, -30f);
		mParticle = new Quad(0.2f, 0.2f);
        
	}
	
	/**
	 * Draw function to draw the Level
	 * @param gl The gl Paramter to get the Context
	 */
	public void draw(MatrixTrackingGL gl)
	{
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	gl.glFrontFace(GL10.GL_CCW);
		
    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glEnable(GL10.GL_BLEND);
    	gl.glDepthMask(false);
    	
    	//gl.glDisable(GL10.GL_FOG);
    	mForceField1.draw();
    	mForceField2.draw();
    	
    	gl.glEnable(GL10.GL_FOG);
    	/*
    	//Define GLfloat arrays to hold light data
    	FloatBuffer light_direction = Util.floatArrayToBuffer(new float[] { -0.577f, -0.577f, -0.577f, 0.0f });
    	FloatBuffer light_ambient = Util.floatArrayToBuffer(new float[] { 0.3f, 0.3f, 0.3f, 1.0f });
    	FloatBuffer light_diffuse = Util.floatArrayToBuffer(new float[] { 1.0f, 1.0f, 1.0f, 1.0f });
    	FloatBuffer light_specular = Util.floatArrayToBuffer(new float[] { 0.0f, 0.0f, 0.0f, 1.0f });

    	//Apply the light properties to LIGHT0
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, light_direction);
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, light_ambient);
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, light_diffuse);
    	gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, light_specular); 
    	gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_AMBIENT, light_diffuse);
    	gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_DIFFUSE, light_diffuse);
    	gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SPECULAR, light_specular);
    	gl.glMaterialfv(GL10.GL_FRONT, GL10.GL_SHININESS, Util.floatArrayToBuffer(new float[] { 0.0f }));
    	gl.glEnable(GL10.GL_LIGHTING);
    	gl.glEnable(GL10.GL_LIGHT0);
		*/
		gl.glPushMatrix();

		for (House house : mFadeHouses) {
			float dist = (float)Math.abs(mPlayer.getPosition().y - house.getPosition().y) - (house.getSize().y / 2.0f);
			float alpha = (dist - 100.0f) / 30.0f;
	    	gl.glColor4f(1.0f,1.0f,1.0f, 1.0f - alpha);
			house.draw();
		}	
    	gl.glColor4f(1.0f,1.0f,1.0f, 1.0f);
    	gl.glDisable(GL10.GL_BLEND);
    	gl.glDepthMask(true);
		
		for (House house : mHouses) {
			house.draw();
		}		
		
    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glEnable(GL10.GL_BLEND);
    	gl.glDepthMask(false);
		for(int i = mBirds.size() - 1; i >= 0; i-- ){
			mBirds.get(i).draw();
		}
		
		for (ParticleSystem pSystem : mParticleSystems) {
			pSystem.draw();
		}	
		
    	gl.glDisable(GL10.GL_BLEND);

    	gl.glDepthMask(true);
		gl.glPopMatrix();
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    	gl.glDisable(GL10.GL_LIGHTING);
    	gl.glDisable(GL10.GL_LIGHT0);
	}
	
	/**
	 * Update Function for the level at the moment it moves the level upwards for every frame
	 * @param elapsedSeconds the time since the last update call
	 */
	public void update(float elapsedSeconds, Vector3 moveDelta)
	{
		//mPosition = Vector3.add(mPosition, Vector3.mult(mSpeed, elapsedSeconds));	
		//mPosition = Vector3.add(mPosition, moveDelta);

		
		Vector3 playerPos = mPlayer.getPosition();
		mSpeed.y -= elapsedSeconds * 1.5f;
		mSpeed.y = (mSpeed.y < -40f) ? -40f : mSpeed.y;
		moveDelta.y = Vector3.mult(mSpeed, elapsedSeconds).y;
		updatePlayerPosition(moveDelta);
		playerPos = mPlayer.getPosition();
		mForceField1.setPosition(new Vector3(0,playerPos.y - mForceField1.getHeight() / 2.0f, 0));
		mForceField1.update(elapsedSeconds);
		mForceField2.setPosition(new Vector3(0,playerPos.y - mForceField2.getHeight() / 2.0f, 0));
		mForceField2.update(elapsedSeconds);
		mNextHouse -= elapsedSeconds;
		mNextBird -= elapsedSeconds;
		if(mHouses.size() < 40 && mNextHouse <= 0)
		{
			int houseSize = (int)(Math.floor(Math.random() * 5.0));
			houseSize = (houseSize==5)?4:houseSize;
			Vector3 size =  new Vector3(mBlockSize, mBlockSize * ((float)houseSize + 1.0f), mBlockSize);
			int xpos = (int)(Math.floor(Math.random() * 5.0)) - 2;
			int zpos = (int)(Math.floor(Math.random() * 9.0)) - 4;
			Vector3 newPos = new Vector3(xpos * mBlockSize + playerPos.x, playerPos.y - 130.0f - (size.y / 2.0f), zpos * mBlockSize + playerPos.z);
			newPos.x -= newPos.x % mBlockSize;
			newPos.z -= newPos.z % mBlockSize;
			House newHouse = new House(mHouseModels[houseSize], mHouseTex.get(mRandom.nextInt(4)), newPos);
			newHouse.setHouseSize(houseSize, size);
			mFadeHouses.add(newHouse);
			mNextHouse = (float)Math.random() * 0.1f;
		}
		if(mNextBird < 0)
		{	
			float birdxpos = (float)((Math.floor(Math.random() * 5.0)) - 2) * mBlockSize;
			float birdzpos = (float)((Math.floor(Math.random() * 9.0)) - 4) * mBlockSize;
			Vector3 newPos = new Vector3(birdxpos + playerPos.x, playerPos.y - 130.0f, birdzpos + playerPos.z);
			float rotation = (float)(Math.random() * 360);

			Bird newbird = new Bird(mBird, newPos, rotation);
			mBirds.add(newbird);
			
			mNextBird = (float)Math.random() * 1.0f;
		}
		
		for(Bird bird: mBirds){
			bird.update(elapsedSeconds);
		}
		
		List<House> remove = new ArrayList<House>();
		for (House house : mFadeHouses) {
			if((float)Math.abs(playerPos.y - house.getPosition().y) - (house.getSize().y / 2.0f) < 100.0f)
				remove.add(house);
		}
		mHouses.addAll(remove);
		mFadeHouses.removeAll(remove);
		
		remove.clear();
		for (House house : mHouses) {
			if(house.getPosition().y - playerPos.y  + (house.getHouseSize() * mBlockSize / 2.0f) > 10.0f)
				remove.add(house);
		}
		mHouses.removeAll(remove);
		
		
		List<Bird> removeBird = new ArrayList<Bird>();
		for (Bird bird : mBirds) {
			if(bird.getPosition().y - playerPos.y > 10.0f)
				removeBird.add(bird);
		}
		mBirds.removeAll(removeBird);
		
		List<ParticleSystem> removePSystems = new ArrayList<ParticleSystem>();
		for (ParticleSystem pSystem : mParticleSystems) 
		{
			pSystem.update(elapsedSeconds, playerPos);
			if (!pSystem.isActive())
				removePSystems.add(pSystem);
		}
		mParticleSystems.removeAll(removePSystems);
	}
	
	/**
	 * Recalculates the new Playerposition and do the collision detection
	 * @param moveDelta A vector representing the distance the user dragged the view
	 */
	private void updatePlayerPosition(Vector3 moveDelta)
	{
		Vector3 newPos = Vector3.add(mPlayer.getPosition(), moveDelta);
			
		if(Math.sqrt(newPos.x * newPos.x + newPos.z * newPos.z) + mPlayer.getRadius() * 5.0f > mForceField1.getForceFieldRadius())
		{
			newPos.x = mPlayer.getPosition().x;
			newPos.z = mPlayer.getPosition().z;
			moveDelta.x = 0;
			moveDelta.z = 0;
		}
		List<House> remove = new ArrayList<House>();
		for(House house:mHouses){
			if(house.intersect(newPos, mPlayer.getRadius())) {
				if(mPlayer.getPosition().y > house.getPosition().y + house.getSize().y / 2.0f ){
					mPlayer.hitHouse();
					remove.add(house);
					mParticleSystems.add(new ParticleSystem(mPlayer.getPosition(), R.drawable.l17_bricks, mParticle));
				}
				else {
					moveDelta.x = 0;
					moveDelta.z = 0;
				}
			}
		}
		mHouses.removeAll(remove);
		
		List<Bird> removeBirds = new ArrayList<Bird>();
		for(Bird bird:mBirds){
			if(bird.intersect(newPos, mPlayer.getRadius())) {
				mPlayer.hitBird();
				removeBirds.add(bird);
				mParticleSystems.add(new ParticleSystem(mPlayer.getPosition(), R.drawable.l17_coin, mParticle));
			}
		}
		mBirds.removeAll(removeBirds);
		
		mPlayer.setPosition(Vector3.add(mPlayer.getPosition(), moveDelta));

	}
	
	/**
	 * Save the actual gamestate
	 * @param outState The bundle to save to
	 */
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(PLAYER_MONEY, mPlayer.getMoney());
		outState.putInt(PLAYER_LIFES, mPlayer.getLives());
		outState.putFloat(LEVEL_SPEED, mSpeed.y);
	}
	
	/**
	 * Getter for the Player
	 * @return The Player in the Level
	 */
	public Player getPlayer() {
		return mPlayer;
	}
}
