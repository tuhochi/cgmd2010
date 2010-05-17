package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import java.io.Serializable;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.ObstacleManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.TimeManager;

public class Obstacle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8841834994514009996L;

	/** The width. */
	public int width;
	
	/** The height. */
	public int height;
	
	/** The position. */
	public Vector2 position;
	
	/** The type. */
	public int type;
	
	public float virtualHeight;
	
	public final float accel = 0.000005f;
	
	public float constantSpeed = 0.5f*Settings.BALLOON_SPEED;
	
	public float time = 0;
	
	public float initialHeight;
	
	/**
	 * Instantiates a new obstacle.
	 *
	 * @param y the y position of the obstacle
	 * @param type the type (1-4) 
	 */
	public Obstacle(int y, int type)
	{
		position = new Vector2();
		position.y = RenderView.instance.getTopBounds();
		virtualHeight = y;
		this.type = type; 
		
		switch(type)
		{
			case(ObstacleManager.OBSTACLE_TYPE_FINISH):
				position.x=0;
				width = 100;
				height = 5;
				break;
			case(ObstacleManager.OBSTACLE_TYPE1):
				position.x=0;
				width = 40;
				height = 30;
				break;
			case(ObstacleManager.OBSTACLE_TYPE2):
				position.x=20;
				width = 20;
				height = 20;
				break;
			case(ObstacleManager.OBSTACLE_TYPE3):
				position.x=40;
				width = 20;
				height = 20;
				break;
			case(ObstacleManager.OBSTACLE_TYPE4):
				position.x=80;
				width = 20;
				height = 5;
				break;			
		}
	}
	
	public boolean update()
	{
		float dt = TimeUtil.instance.getDt();
		time += dt;
		constantSpeed -= dt*0.25f*Settings.BALLOON_SPEED;
		position.y -= 0.5f*accel*(time*time) + Settings.BALLOON_SPEED*0.5*dt;
		return position.y < -height;
	}
}
