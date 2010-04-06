package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.HouseModel;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Quad;


public class Level {

	private HouseModel[] mHouseModels = new HouseModel[5];
	//private Vector3 mPosition = new Vector3(0, 0, 0);
	private List<House> mHouses = new ArrayList<House>();
	private List<Bird> mBirds = new ArrayList<Bird>();
	private Vector3 mSpeed = new Vector3(0, -40.0f, 0);
	private float mBlockSize = 5.0f;
	private float mNextHouse = 0;
	private float mNextBird = 0;
	private Player mPlayer = new Player(new Vector3(0f, 30f, 0f), 1f, 3, 100);
	private Quad mBird;
	//private NormalModeWorld mWorld;
	/**
	 * Level Class for Rendering Houses and other Objects.
	 */
	public Level(NormalModeWorld world)
	{
		//mWorld = world;
		for(int i = 0; i < 5; i++)
			mHouseModels[i] = new HouseModel(mBlockSize, mBlockSize * ((float)i + 1.0f), mBlockSize);
		
		mBird = new Quad(3.0f,3.0f);
		
        GLManager.getInstance().getTextures().add(R.drawable.l17_crate);
        GLManager.getInstance().getTextures().add(R.drawable.l17_bird);
        GLManager.getInstance().getTextures().add(R.drawable.l17_bier);
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
		
		gl.glPushMatrix();
		for (House house : mHouses) {
			house.draw();
		}		
		
    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glEnable(GL10.GL_BLEND);
    	gl.glDepthMask(false);
    	//gl.glDisable(GL10.GL_DEPTH_TEST);
		
		//for (Bird bird : mBirds) {
			//bird.draw();
		//}
		for(int i = mBirds.size() - 1; i >= 0; i-- )
		{
			mBirds.get(i).draw();
		}
		
    	gl.glDisable(GL10.GL_BLEND);

    	gl.glDepthMask(true);
    	//gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glPopMatrix();
		
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
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
		moveDelta.y = Vector3.mult(mSpeed, elapsedSeconds).y;
		updatePlayerPosition(moveDelta);
		mNextHouse -= elapsedSeconds;
		mNextBird -= elapsedSeconds;
		if(mNextHouse < 0)
		{
			House newHouse = new House();
			int size = (int)(Math.floor(Math.random() * 5.0));
			size = (size==5)?4:size;
			newHouse.setHouseSize(size, new Vector3(mBlockSize, mBlockSize * ((float)size + 1.0f), mBlockSize));
			int xpos = (int)(Math.floor(Math.random() * 10.0)) - 4;
			int zpos = (int)(Math.floor(Math.random() * 18.0)) - 8;
			Vector3 newPos = new Vector3(xpos * mBlockSize + playerPos.x, playerPos.y - 131.0f - (newHouse.getSize().y / 2.0f), zpos * mBlockSize + playerPos.z);
			newPos.x -= newPos.x % mBlockSize;
			newPos.z -= newPos.z % mBlockSize;
			newHouse.setPosition(newPos);
			newHouse.setModel(mHouseModels[newHouse.getHouseSize()]);
			mHouses.add(newHouse);
			mNextHouse = (float)Math.random() * 0.2f;
		}
		if(mNextBird < 0)
		{	
			Bird newbird = new Bird();
			float birdxpos = (float)((Math.floor(Math.random() * 5.0)) - 2) * mBlockSize;
			float birdzpos = (float)((Math.floor(Math.random() * 9.0)) - 4) * mBlockSize;
			Vector3 newPos = new Vector3(birdxpos + playerPos.x, playerPos.y - 131.0f, birdzpos + playerPos.z);
			newbird.setPosition(newPos);
			newbird.setModel(mBird);
			float rotation = (float)(Math.random() * Math.PI) * 2.0f;
			newbird.setRotation(rotation);
			mBirds.add(newbird);
			
			mNextBird = (float)Math.random() * 0.5f;
		}
		
		List<House> remove = new ArrayList<House>();
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
	}
	
	private void updatePlayerPosition(Vector3 moveDelta)
	{
		Vector3 newPos = Vector3.add(mPlayer.getPosition(), moveDelta);
			
		
		List<House> remove = new ArrayList<House>();
		for(House house:mHouses){
			if(house.intersect(newPos, mPlayer.getRadius())) {
				if(mPlayer.getPosition().y > house.getPosition().y + house.getSize().y / 2.0f ){
					mPlayer.hitHouse();
					remove.add(house);
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
			}
		}
		mBirds.removeAll(removeBirds);
		
		mPlayer.setPosition(Vector3.add(mPlayer.getPosition(), moveDelta));

	}

	public Player getPlayer() {
		return mPlayer;
	}
}
