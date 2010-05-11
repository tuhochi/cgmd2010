package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import static android.opengl.GLES10.glTranslatef;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

public class Cloud 
{
	/** Constant for cloud type1. */
	public final static int CLOUD_TYPE1 = 0;
	/** Constant for cloud type2. */
	public final static int CLOUD_TYPE2 = 1;
	/** Constant for cloud type3. */
	public final static int CLOUD_TYPE3 = 2;
	/** Constant for cloud type4. */
	public final static int CLOUD_TYPE4 = 3;
	
	/** Constant for Cloud Speed (horizontal). */
	public final static float CLOUD_SPEED = 0.06f;
	
	/** The cloud type. */
	public int type;
	
	/** The cloud position. */
	public Vector2 position;
	
	/** The cloud dimension. */
	public Vector2 dimensions;
	
	/** Indicates if the cloud starts right or left. */
	public boolean startRight;
	
	/** The cloud height offset. */
	public int heightOffset;
	
	/** Indicates if the cloud is visible. */
	public boolean isVisible=false;
	
	/** The slowdown factor (vertical). */
	public float slowDownFactor;
	
	/**
	 * Constructor
	 * @param positionY the clouds y position
	 * @param type	the clouds type
	 * @param startsRight indicates if the cloud starts left or right
	 * @param heightOffset the clouds height offset
	 * @param slowDownFactor the clouds slowdown factor (vertical)
	 */
	public Cloud(int positionY,int type,boolean startsRight, int heightOffset, float slowDownFactor)
	{
		position = new Vector2(0,positionY);
		this.type = type;
		dimensions = new Vector2(50, 20*RenderView.instance.getAspectRatio());
		this.startRight = startsRight;
		if(startsRight)
			position.x = RenderView.instance.getRightBounds();
		else
			position.x = 0-dimensions.x;
		this.heightOffset = heightOffset;
		this.slowDownFactor = slowDownFactor;
	}
	
	/**
	 * Updates the clouds position
	 * @return false if cloud is out of the view else true
	 */
	public boolean update()
	{
		if(slowDownFactor!=1)
			position.y += (Settings.BALLOON_SPEED/slowDownFactor)*TimeUtil.instance.getDt();
		if(startRight)
		{
			position.x -= CLOUD_SPEED*TimeUtil.instance.getDt();
			if(position.x < 0-dimensions.x)
			{
				return false;
			}
		}
		else
		{
			position.x += CLOUD_SPEED*TimeUtil.instance.getDt();
			if(position.x > 100)
			{
				return false;
			}
		}

		return true;
	}	

}
