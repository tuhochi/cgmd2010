package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import static android.opengl.GLES10.GL_TEXTURE_2D;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.GL_UNSIGNED_SHORT;
import static android.opengl.GLES10.glDisable;
import static android.opengl.GLES10.glDrawElements;
import static android.opengl.GLES10.glEnable;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glScalef;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Obstacle;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;


/**
 * The Class ObstacleManager manages all obstacles in different size and position.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class ObstacleManager
{
	/**
	 * Boolean to indicate if the level is game over
	 */
	private boolean gameOver = false; 
	
	/**
	 * the unique serialVersionUID 
	 */
	private static final long serialVersionUID = -4564339113161027088L;

	/** The instance of ObstacleManager to pass around. */
	private static ObstacleManager instance;
	
	/** The array filled with random numbers. */
	private int[] randomArray; 
	
	/** The random generator. */
	private Random randomGenerator;
	
	//current position for generating obstacle, 80 = start position
	/** The current position to start generating obstacles. */
	private int currentPosition = 80;
	
	//lowest index of a visible obstacle in obstacles list
	/** The least rendered obstacle. */
	private int leastRenderedObstacle;
	
	/** The number of obstacles to be rendered. */
	public final int NR_OF_OBSTACLES = 50;
	
	/** The constant for obstacle type 1  */
	public static final int OBSTACLE_TYPE1 = 1;
	
	/** The constant for obstacle type 2 */
	public static final int OBSTACLE_TYPE2 = 2;
	
	/** The constant for obstacle type 3 */
	public static final int OBSTACLE_TYPE3 = 3;
	
	/** the constant for obstacle type 4. */
	public static final int OBSTACLE_TYPE4 = 4;
	
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
		currentPosition = 80;
		leastRenderedObstacle = 0;
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
	 * Sets the instance
	 * @param manager Instance to set to singleton
	 */
	public static void setInstance(ObstacleManager manager) {
		instance = manager;
	}
	
	/**
	 * Writes to stream
	 * @param dos Stream to write to 
	 */
	public void writeToStream(DataOutputStream dos) {
		try {
			dos.writeInt(currentPosition);
			dos.writeInt(leastRenderedObstacle);
			
		} catch(Exception e) {
			System.out.println("Error writing to stream in ObstacleManager.java: "+e.getMessage());
		}
		
	}
	/**
	 * Writes to bundle
	 * @param bundle bundle to write to 
	 */
	public void writeToBundle(Bundle bundle) {
		try {
			bundle.putSerializable("obstacles", obstacles);
		} catch (Throwable t) {
			Log.e("ObstacleManager.java", t.getMessage());
		}
	}
	
	/**
	 * Reads from stream
	 * @param dis Stream to read from 
	 */
	public void readFromStream(DataInputStream dis) {
		try {
			currentPosition = dis.readInt(); 
			leastRenderedObstacle = dis.readInt(); 
			
		} catch(Exception e) {
			System.out.println("Error reading from stream in ObstacleManager.java: "+e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	public void readFromBundle(Bundle bundle) {
		obstacles = (ArrayList<Obstacle>)bundle.getSerializable("obstacles");
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
		if(mainChar == null)
			mainChar = RenderView.getInstance().getMainCharInstance();
		
		//calculate current topBounds value for relative position
		int topBounds = currentHeight+(int)RenderView.getInstance().getTopBounds();
		//Log.v("topBounds Height: ", String.valueOf(topBounds));
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
				
				if(testCollisionWithMainChar(tempObstacle, currentHeight) && !gameOver)
				{
					RenderView.getInstance().setGameOver(true); 
					LevelActivity.handler.post(
							new Runnable()
							{
						    	@Override
						        public void run() 
						    	{
						            LevelActivity.instance.triggerVibrate(1000);
						        }
							});
				}
					//System.out.println("BAMM");
				
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
	
	/** Indicates if the level is game over
	 * 
	 * @return true if game over, false otherwise
	 */
	public boolean isGameOver() {
		return gameOver; 
	}
	
	/**
	 * Sets the state if the level is game over
	 * @param gameOver state of the level if game over 
	 */
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver; 
	}
	
}
