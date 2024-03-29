package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import javax.microedition.khronos.opengles.GL10;

/**
 * Represents the camera
 * @author Martin Knecht
 *
 */
public class Camera {
	public float[] lookAtPos = {0, 0};
	
	protected float[] pos = {0, 0};
	protected float[] offset = {0, 0};
	protected float approxFactor = 5.0f;
	
	protected Level level;
	
	/**
	 * Constructor
	 * @param _level The level object
	 */
	public Camera(Level _level) 
	{
		level = _level;
		offset[0] = (float) Math.floor(MyRenderer.numTilesHorizontal*0.5f);
		offset[1] = (float) Math.floor(MyRenderer.numTilesVertical*0.5f);
		
		pos[0] = offset[0];
		pos[1] = offset[1];
	}
	
	/**
	 * Lets the camera look at a given position
	 * @param x Look at position in the x-axis
	 * @param y Look at position in the y-axis
	 */
	public void lookAt(float x, float y) 
	{
			lookAtPos[0] = Math.min(level.frontLayer.numTilesX-1-offset[0], Math.max(offset[0], x));
			lookAtPos[1] = y;
	}
	
	/**
	 * Updates the camera position
	 * @param dt Delta time
	 */
	public void update(float dt) 
	{
		pos[0] += (lookAtPos[0]-pos[0])*dt*approxFactor;
		pos[1] += (lookAtPos[1]-pos[1])*dt*approxFactor;
		
		level.setPosition(-pos[0] + offset[0], -1.0f);//, -pos[1]+offset[1]);
	}
	
	/**
	 * Does the camera transformation
	 * @param gl The OpenGL context
	 */
	public void draw(GL10 gl)
	{
		gl.glLoadIdentity();
		gl.glTranslatef(-pos[0] + offset[0], -1.0f, 0.0f);//-pos[1] + offset[1], 0.0f);
	}
}
