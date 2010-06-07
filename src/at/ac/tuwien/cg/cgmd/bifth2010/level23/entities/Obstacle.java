package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import java.io.Serializable;
import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.ObstacleManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;

/**
 * The Class Obstacle is a container for the falling obstacles.
 *
 * @author Markus Ernst
 * @author Florian Felberbauer
 */

public class Obstacle implements Serializable 
{
	/** The serialization id */
	private static final long serialVersionUID = 8841834994514009996L;

	/** The width. */
	public float width;
	
	/** The height. */
	public float height;
	
	/** The position. */
	public Vector2 position;
	
	/** The type. */
	public int type;
	
	/** The virtual height, used to decide when an obstacle is visible. */
	public float virtualHeight;
	
	/** The obstacles acceleration. */
	public final float accel = 0.000005f;
	
	/** The time the obstacle since the obstacle. */
	public float time = 0;
	
	private static float lastObstaclePosX=-1;
	private static float lastObstacleWidth=-1;
	
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
		Random randomGenerator = new Random(System.currentTimeMillis());
		float aspectRatio = RenderView.instance.getAspectRatio();
		switch(type)
		{
			case(ObstacleManager.OBSTACLE_TYPE_FINISH):
				position.x=0;
				width = 100;
				height = 5;
				return;
			case(ObstacleManager.OBSTACLE_TYPE1):
				width = 25f*aspectRatio;
				height = 25f*aspectRatio/1.333f;
				break;
			case(ObstacleManager.OBSTACLE_TYPE2):
				width = 17f*aspectRatio;
				height = 17f*aspectRatio;
				break;
			case(ObstacleManager.OBSTACLE_TYPE3):
				width = 35f*aspectRatio*0.5f;
				height = 35f*aspectRatio;
				break;
			case(ObstacleManager.OBSTACLE_TYPE4):
				width = 25f*aspectRatio*0.5f;
				height = 25f*aspectRatio;
				break;			
		}
		
		if(lastObstaclePosX ==-1)
		{
			position.x = randomGenerator.nextInt((int)RenderView.instance.getRightBounds()-(int)width);
		}
		else
		{
			float leftSpace = 0+lastObstaclePosX;
			float rightSpace = 100-lastObstaclePosX-lastObstacleWidth;
			float mainCharWidth = MainChar.instance.getWidth();

				if(leftSpace<mainCharWidth)
				{
					position.x = lastObstaclePosX+lastObstacleWidth + randomGenerator.nextInt((int)RenderView.instance.getRightBounds()-(int)width);
					if(position.x > (int)RenderView.instance.getRightBounds()-(int)width)
						position.x = RenderView.instance.getRightBounds()-width;
				}
				else
				{
					if(rightSpace<mainCharWidth)
					{
						if((int)lastObstaclePosX-(int)width <= 0)
							position.x = 0;
						else
							position.x = randomGenerator.nextInt((int)lastObstaclePosX-(int)width);
					}
					else
					{
						boolean chooseOne = randomGenerator.nextBoolean();
						if(chooseOne)
						{
							position.x = lastObstaclePosX+lastObstacleWidth + randomGenerator.nextInt((int)RenderView.instance.getRightBounds()-(int)width);
							if(position.x > (int)RenderView.instance.getRightBounds()-(int)width)
								position.x = RenderView.instance.getRightBounds()-width;
						
						}
						else
						{
							if((int)lastObstaclePosX-(int)width <= 0)
								position.x = 0;
							else
								position.x = randomGenerator.nextInt((int)lastObstaclePosX-(int)width);
						}
					}
				}                  
		}
		lastObstaclePosX = position.x;
		lastObstacleWidth = width;
	}
	
	public void update()
	{
		float dt = TimeUtil.instance.getDt();
		time += dt;
		position.y -= (0.35f*accel*(time*time) + Settings.BALLOON_SPEED*dt);
	}
}
