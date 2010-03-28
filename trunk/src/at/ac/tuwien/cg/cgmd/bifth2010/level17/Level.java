package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.HouseModel;


public class Level {

	private HouseModel[] mHouseModels = new HouseModel[5];
	private Vector3 mPosition = new Vector3(0, 0, 0);
	private List<HouseInfo> mHouses = new ArrayList<HouseInfo>();
	private List<HouseInfo> mFadeHouses = new ArrayList<HouseInfo>();
	private Vector3 mSpeed = new Vector3(0, 40.0f, 0);
	private float mBlockSize = 5.0f;
	
	/**
	 * Level Class for Rendering Houses and other Objects.
	 */
	public Level()
	{
		for(int i = 0; i < 5; i++)
			mHouseModels[i] = new HouseModel(mBlockSize, mBlockSize * ((float)i + 1.0f), mBlockSize);
		
		HouseInfo house = new HouseInfo();
		house.setPosition(new Vector3(mBlockSize,-70.0f,0));
		house.setHouseSize(1);
		mHouses.add(house);
		house = new HouseInfo();
		house.setPosition(new Vector3(-mBlockSize,-30.0f,0));
		house.setHouseSize(2);
		mHouses.add(house);
		house = new HouseInfo();
		house.setPosition(new Vector3(0.0f,-50.0f,mBlockSize));
		house.setHouseSize(3);
		mHouses.add(house);
		house = new HouseInfo();
		house.setPosition(new Vector3(0.0f,-10.0f,-mBlockSize));
		house.setHouseSize(4);
		mHouses.add(house);
		
	}
	
	/**
	 * Draw function to draw the Level
	 * @param gl The gl Paramter to get the Context
	 */
	public void draw(MatrixTrackingGL gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef(mPosition);
		for (HouseInfo house : mHouses) {
			gl.glPushMatrix();
			gl.glTranslatef(house.getPosition());
			mHouseModels[house.getHouseSize()].draw(gl);
			gl.glPopMatrix();
		}

    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glEnable(GL10.GL_BLEND);
    	gl.glDisable(GL10.GL_DEPTH_TEST);
    	
		for (HouseInfo house : mFadeHouses) {
			gl.glPushMatrix();
			gl.glTranslatef(house.getPosition());
			float yPos = house.getPosition().y + mPosition.y  + (house.getHouseSize() * mBlockSize / 2.0f);
        	gl.glColor4f(1.0f,1.0f,1.0f, 1.0f - (yPos / 10.0f));
			mHouseModels[house.getHouseSize()].draw(gl);	
			gl.glPopMatrix();
		}
    	gl.glColor4f(1.0f,1.0f,1.0f, 1.0f);
    	gl.glDisable(GL10.GL_BLEND);
    	gl.glEnable(GL10.GL_DEPTH_TEST);
    	
		gl.glPopMatrix();
	}
	
	/**
	 * Update Function for the level at the moment it moves the level upwards for every frame
	 * @param elapsedSeconds the time since the last update call
	 */
	public void update(float elapsedSeconds)
	{
		mPosition = Vector3.add(mPosition, Vector3.mult(mSpeed, elapsedSeconds));	
		
		List<HouseInfo> remove = new ArrayList<HouseInfo>();
		List<HouseInfo> add = new ArrayList<HouseInfo>();
		
		for (HouseInfo house : mHouses) {
			if(house.getPosition().y + mPosition.y  + (house.getHouseSize() * mBlockSize / 2.0f)> 0)
			{
				remove.add(house);
				HouseInfo newHouse = new HouseInfo();
				int size = (int)(Math.floor(Math.random() * 5.0));
				size = (size==5)?4:size;
				newHouse.setHouseSize(size);

				int xpos = (int)(Math.floor(Math.random() * 5.0)) - 2;
				xpos = (xpos==3)?2:xpos;
				int ypos = (int)(Math.floor(Math.random() * 3.0)) - 1;
				ypos = (ypos==2)?1:ypos;
				newHouse.setPosition(new Vector3(xpos * mBlockSize, -mPosition.y - 100.0f - (house.getHouseSize() * mBlockSize / 2.0f), ypos * mBlockSize));
				add.add(newHouse);
			}
		}
		
		mHouses.removeAll(remove);
		mFadeHouses.addAll(remove);
		mHouses.addAll(add);
		remove.clear();
		add.clear();
		
		for (HouseInfo house : mFadeHouses) {
			if(house.getPosition().y + mPosition.y  + (house.getHouseSize() * mBlockSize / 2.0f) > 10.0f)
				remove.add(house);
		}
		mFadeHouses.removeAll(remove);
		
	}
}
