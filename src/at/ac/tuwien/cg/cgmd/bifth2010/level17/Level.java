package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.ArrayList;
import java.util.List;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.HouseModel;


public class Level {

	private HouseModel[] mHouseModels = new HouseModel[5];
	//private Vector3 mPosition = new Vector3(0, 0, 0);
	private List<HouseInfo> mHouses = new ArrayList<HouseInfo>();
	private Vector3 mSpeed = new Vector3(0, -40.0f, 0);
	private float mBlockSize = 5.0f;
	private float mNextHouse = 0;
	private Player mPlayer = new Player(new Vector3(0f, 30f, 0f), 1f, 3, 100);
	
	/**
	 * Level Class for Rendering Houses and other Objects.
	 */
	public Level()
	{
		for(int i = 0; i < 5; i++)
			mHouseModels[i] = new HouseModel(mBlockSize, mBlockSize * ((float)i + 1.0f), mBlockSize);
	}
	
	/**
	 * Draw function to draw the Level
	 * @param gl The gl Paramter to get the Context
	 */
	public void draw(MatrixTrackingGL gl)
	{
		gl.glPushMatrix();
		for (HouseInfo house : mHouses) {
			gl.glPushMatrix();
			gl.glTranslatef(house.getPosition());
			mHouseModels[house.getHouseSize()].draw(gl);
			gl.glPopMatrix();
		}
    	
		gl.glPopMatrix();
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
		if(mNextHouse < 0)
		{
			HouseInfo newHouse = new HouseInfo();
			int size = (int)(Math.floor(Math.random() * 5.0));
			size = (size==5)?4:size;
			newHouse.setHouseSize(size, new Vector3(mBlockSize, mBlockSize * ((float)size + 1.0f), mBlockSize));
			int xpos = (int)(Math.floor(Math.random() * 9.0)) - 4;
			int ypos = (int)(Math.floor(Math.random() * 5.0)) - 2;
			Vector3 newPos = new Vector3(xpos * mBlockSize + playerPos.x, playerPos.y - 131.0f - (newHouse.getSize().y / 2.0f), ypos * mBlockSize + playerPos.z);
			newPos.x -= newPos.x % mBlockSize;
			newPos.z -= newPos.z % mBlockSize;
			newHouse.setPosition(newPos);
			mHouses.add(newHouse);
			mNextHouse = (float)Math.random() * 0.1f;
		}
		
		List<HouseInfo> remove = new ArrayList<HouseInfo>();
		
		for (HouseInfo house : mHouses) {
			if(house.getPosition().y - playerPos.y  + (house.getHouseSize() * mBlockSize / 2.0f) > 10.0f)
				remove.add(house);
		}
		
		mHouses.removeAll(remove);
		
	}
	
	private void updatePlayerPosition(Vector3 moveDelta)
	{
		Vector3 newPos = Vector3.add(mPlayer.getPosition(), moveDelta);
		
		
		
		/*HouseInfo testHouse = new HouseInfo();
		testHouse.setHouseSize(0, new Vector3(5.0f,5.0f,5.0f));
		testHouse.setPosition(new Vector3(30.0f,-400.0f,30.0f));
		if(testHouse.intersect(new Vector3(30.0f,-402.0f,30.0f), 3.0f)) {
			if(-399.0f > testHouse.getPosition().y + testHouse.getSize().y / 2.0f ){
				mPlayer.hit();
			}
			else {
				moveDelta.x = 0;
				moveDelta.z = 0;
			}
		}*/
		
		
		
		
		
		List<HouseInfo> remove = new ArrayList<HouseInfo>();
		for(HouseInfo house:mHouses){
			if(house.intersect(newPos, mPlayer.getRadius())) {
				if(mPlayer.getPosition().y > house.getPosition().y + house.getSize().y / 2.0f ){
					mPlayer.hit();
					remove.add(house);
				}
				else {
					moveDelta.x = 0;
					moveDelta.z = 0;
				}
			}
		}
		mHouses.removeAll(remove);
		mPlayer.setPosition(Vector3.add(mPlayer.getPosition(), moveDelta));
		
		

	}

	public Player getPlayer() {
		return mPlayer;
	}
}
