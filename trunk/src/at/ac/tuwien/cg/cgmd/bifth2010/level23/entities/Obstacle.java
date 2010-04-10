package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import java.io.Serializable;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.ObstacleManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;

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
	
	/**
	 * Instantiates a new obstacle.
	 *
	 * @param y the y position of the obstacle
	 * @param type the type (1-4) 
	 */
	public Obstacle(int y, int type)
	{
		position = new Vector2();
		position.y = y;
		this.type = type; 
		
		switch(type)
		{
			case(ObstacleManager.OBSTACLE_TYPE1):
				position.x=0;
				width = 20;
				height = 10;
				break;
			case(ObstacleManager.OBSTACLE_TYPE2):
				position.x=20;
				width = 20;
				height = 5;
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
	
}
