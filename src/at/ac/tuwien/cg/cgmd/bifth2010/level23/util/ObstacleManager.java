package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;


/**
 * The Class ObstacleManager manages all obstacles in different size and position.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class ObstacleManager 
{
	
	/** The instance of ObstacleManager to pass around. */
	private static ObstacleManager instance;
	
	/** The array filled with random numbers. */
	private int[] randomArray; 
	
	/** The random generator. */
	private Random randomGenerator;
	
	//current position for generating obstacle, 80 = start position
	/** The current position to start generationg obstacles. */
	private int currentPosition = 80;
	
	//lowest index of a visible obstacle in obstacles list
	/** The least rendered obstacle. */
	private int leastRenderedObstacle;
	
	/** The number of obstacles to be rendered. */
	public final int NR_OF_OBSTACLES = 50;
	
	/** The constant for obstacle type 1  */
	public final int OBSTACLE_TYPE1 = 1;
	
	/** The constant for obstacle type 2 */
	public final int OBSTACLE_TYPE2 = 2;
	
	/** The constant for obstacle type 3 */
	public final int OBSTACLE_TYPE3 = 3;
	
	/** the constant for obstacle type 4. */
	public final int OBSTACLE_TYPE4 = 4;
	
	//in numbers per 100, must sum up to 10
	/** The probability for obstacle type 1. */
	public final static int TYPE1_PROB = 25;
	
	/** The probability for obstacle type 2. */
	public final static int TYPE2_PROB = 25;
	
	/** The probability for obstacle type 3. */
	public final static int TYPE3_PROB = 25;
	
	/** The probability for obstacle type 4. */
	public final static int TYPE4_PROB = 25;
	
	//horizontal spacing between obstacles, do more advanced stuff with it (random?)
	/** The Constant HORIZONTAL_SPACING to define the horizontal spacing between obstacles. */
	public final static int HORIZONTAL_SPACING = 100;
	
	/** The main char. */
	private MainChar mainChar;
	
	/**
	 * The Class Obstacle represents one single obstacle
	 * @author Markus Ernst
	 * @author Florian Felberbauer
	 */
	public class Obstacle
	{	
		
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
		 * @param y the y position of the obtacle
		 * @param type the type (1-4) 
		 */
		public Obstacle(int y, int type)
		{
			position = new Vector2();
			position.y = y;
			this.type = type;
			switch(type)
			{
				case(OBSTACLE_TYPE1):
					position.x=0;
					width = 20;
					height = 10;
					break;
				case(OBSTACLE_TYPE2):
					position.x=20;
					width = 20;
					height = 5;
					break;
				case(OBSTACLE_TYPE3):
					position.x=40;
					width = 20;
					height = 20;
					break;
				case(OBSTACLE_TYPE4):
					position.x=80;
					width = 20;
					height = 5;
					break;			
			}
		}
		
	}
	
	/** The arraylist of obstacles. */
	private ArrayList<Obstacle> obstacles;
	
	/** The index buffer. */
	private ShortBuffer indexBuffer;
	
	/** The vertex buffer. */
	private FloatBuffer vertexBuffer;
	
	/**
	 * Instantiates a new obstacle manager.
	 */
	public ObstacleManager()
	{
		randomGenerator = new Random(System.currentTimeMillis());
		randomArray = new int[100];
		obstacles = new ArrayList<Obstacle>();
		createVertexBuffer();
		genArrayWithProbability();
		mainChar = RenderView.getInstance().getMainChar();
		instance = this;
	}
	
	/**
	 * Gets the singleton of ObstacleManager.
	 *
	 * @return singleton of ObstacleManager
	 */
	public static ObstacleManager getInstance()
	{
		if(instance==null)
			instance = new ObstacleManager();
		return instance;		
	}
	
	/**
	 * Generates array with probabilities.
	 */
	private void genArrayWithProbability()
	{
		//distribute values according to probabilities
		for(int i=0; i <TYPE1_PROB;i++)
			randomArray[i] = OBSTACLE_TYPE1;
		for(int i=TYPE1_PROB; i <TYPE1_PROB+TYPE2_PROB;i++)
			randomArray[i] = OBSTACLE_TYPE2;
		for(int i=TYPE1_PROB+TYPE2_PROB; i <TYPE1_PROB+TYPE2_PROB+TYPE3_PROB;i++)
			randomArray[i] = OBSTACLE_TYPE3;
		for(int i=TYPE1_PROB+TYPE2_PROB+TYPE3_PROB; i <TYPE1_PROB+TYPE2_PROB+TYPE3_PROB+TYPE4_PROB;i++)
			randomArray[i] = OBSTACLE_TYPE4;
	}
	
	/**
	 * Selects random type.
	 *
	 * @return one random int from randomArray
	 */
	private int selectRandomType()
	{
		return randomArray[randomGenerator.nextInt(99)];
	}
	
	/**
	 * Generates obstacles.
	 */
	public void generateObstacles()
	{
		for(int i=0; i < NR_OF_OBSTACLES;i++)
		{
			obstacles.add(new Obstacle(currentPosition, selectRandomType()));
			currentPosition += HORIZONTAL_SPACING;
		}
	}
	
	/**
	 * Render visible obstacles.
	 *
	 * @param currentHeight the current height
	 */
	public void renderVisibleObstacles(int currentHeight)
	{
		//calculate current topBounds value for relative position
		int topBounds = currentHeight+(int)RenderView.getInstance().getTopBounds();
		
		boolean renderNext = true;
		boolean leastRenderedFound = false;
		int i = leastRenderedObstacle;
		
		//disable textures just for testing
		glDisable(GL_TEXTURE_2D);
		glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		while(renderNext && i != NR_OF_OBSTACLES)
		{
			Obstacle tempObstacle = obstacles.get(i);
			int posY = (int)tempObstacle.position.y;
			
			
			//test visibility
			if(posY > currentHeight && posY < topBounds)
			{
				//visible
				
				//find lowest index of a visible obstacle
				if(!leastRenderedFound)
				{
					leastRenderedFound = !leastRenderedFound;
					leastRenderedObstacle = i;
				}
				
				//render
				glPushMatrix();		
	
					glScalef(tempObstacle.width,tempObstacle.height,1);	
					glTranslatef(tempObstacle.position.x/(float)tempObstacle.width, (posY - currentHeight)/(float)tempObstacle.height, 0);
									
					glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, indexBuffer);
					
				glPopMatrix();
				
				if(testCollisionWithMainChar(tempObstacle, currentHeight))
					System.out.println("BAMM");
				
				i++;
			}
			else
			{
				//invisible
				
				if(leastRenderedFound)
					renderNext = false;
				else
					i++;
			}
		}
		
		glEnable(GL_TEXTURE_2D);
	}
	
	/**
	 * Creates the vertex buffer.
	 */
	private void createVertexBuffer()
	{
		float[] vertices = new float[12];
		
		//just a sample
		//bottom left
		vertices[0] = 0.f;
		vertices[1] = 0.f;
		vertices[2] = 0.f;
		//bottom right
		vertices[3] = 1.f;
		vertices[4] = 0.f;
		vertices[5] = 0.f;
		//top left
		vertices[6] = 0.f;
		vertices[7]= 1.f;
		vertices[8]= 0.f;
		//top right
		vertices[9] = 1.f;
		vertices[10] = 1.f;
		vertices[1] = 0.f;		
		
		ByteBuffer vertexBBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		vertexBBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = vertexBBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);	
		
		short[] indices = { 0, 1, 2, 3 };
		ByteBuffer indexBBuffer = ByteBuffer.allocateDirect(indices.length * 2);
		indexBBuffer.order(ByteOrder.nativeOrder());
		indexBuffer = indexBBuffer.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);	
	}
	
	/**
	 * Test collision with main char.
	 *
	 * @param obstacle the obstacle
	 * @param currentHeight the current height
	 * @return true, if a collision happened
	 */
	private boolean testCollisionWithMainChar(Obstacle obstacle, float currentHeight)
	{
		float leftMainChar, leftObstacle,
	    	rightMainChar, rightObstacle,
	    	topMainChar, topObstacle,
	    	bottomMainChar, bottomObstacle;

		leftMainChar = mainChar.getPosition().x;
		rightMainChar = leftMainChar + mainChar.getWidth();
		bottomMainChar = 0.0f;
		topMainChar = mainChar.getHeight();
		
		leftObstacle = obstacle.position.x;
		rightObstacle = leftObstacle + obstacle.width;
		bottomObstacle = obstacle.position.y - currentHeight;		
		topObstacle = bottomObstacle + obstacle.height;
		
	    if(bottomMainChar > topObstacle || topMainChar < bottomObstacle 
	    		|| rightMainChar < leftObstacle || leftMainChar > rightObstacle) 
	    	return false;


		return true;
	}
	
}
