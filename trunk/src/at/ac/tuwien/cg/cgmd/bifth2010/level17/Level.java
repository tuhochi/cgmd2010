package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.House;


public class Level {

	private House[] mHouses = new House[5];
	private Vector3 mPosition = new Vector3(0,0,0);
	private List<House> mCity = new ArrayList<House>();
	
	/**
	 * Constructor for LEvel.
	 * Level Class for Rendering Houses and other Objects.
	 */
	public Level()
	{

		for(int i = 0; i < 5; i++)
		{
			mHouses[i] = new House(5.0f, 5.0f * ((float)i + 1.0f), 5.0f);
		}
		
	}
	
	/**
	 * Draw function to draw the Level
	 * @param gl The gl Paramter to get the Context
	 */
	public void draw(MatrixTrackingGL gl)
	{
		gl.glPushMatrix();
		gl.glTranslatef(mPosition);
		mHouses[0].draw(gl);
		gl.glPopMatrix();
	}
	
	/**
	 * Update Function for the level at the moment it moves the level upwards for every frame
	 * @param elapsedSeconds the time since the last update call
	 */
	public void update(float elapsedSeconds)
	{
		mPosition = Vector3.add(mPosition, Vector3.mult(new Vector3(0,1.0f,0), elapsedSeconds));
		
		
	}
}
