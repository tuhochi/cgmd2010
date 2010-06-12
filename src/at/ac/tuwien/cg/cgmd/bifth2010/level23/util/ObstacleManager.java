package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Obstacle;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.TexturePart;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;

/**
 * The Class ObstacleManager manages all obstacles in different size and position.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class ObstacleManager
{
	/** Boolean to indicate if the level is game over */
	private boolean gameOver = false; 
	
	/** Boolean to indicate if the activity was restored from bundle	 */
	private boolean wasRestored = false;
	
	/** the unique serialVersionUID	 */
	private static final long serialVersionUID = -4564339113161027088L;

	/** The instance of ObstacleManager to pass around. */
	public static ObstacleManager instance = new ObstacleManager();
	
	/** The array filled with random numbers. */
	private Integer[] randomArray; 
	
	/** The random generator. */
	private Random randomGenerator;
	
	/** The current position to start generating obstacles. */
	private int currentPosition = Settings.OBSTACLE_START;
	
	//lowest index of a visible obstacle in obstacles list
	/** The least rendered obstacle. */
	private int leastRenderedObstacle;
	
	/** The number of obstacles to be rendered. */
	public int NR_OF_OBSTACLES = 0;
	
	/** The constant for obstacle type FINISH */
	public static final int OBSTACLE_TYPE_FINISH = 0;
	
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
	public final static float HORIZONTAL_SPACING = 15*RenderView.instance.getAspectRatio();
	
	public int finishPosition;
	
	/** The main char. */
	private MainChar mainChar;
	
	/** The vertex buffers for all obstacles. */
	private FloatBuffer[] vertexBuffers;
	
	/** The VBO id for all obstacles. */
	private int[] vboIDs;
	
	/** The texture parts for all obstacles. */
	private TexturePart[] textures;
	
	/** The Geometry Manager. */
	private GeometryManager geometryManager = GeometryManager.instance;
	
	/**
	 * The Class Obstacle represents one single obstacle
	 * @author Markus Ernst
	 * @author Florian Felberbauer
	 */
	
	/** The arraylist of obstacles. */
	private ArrayList<Obstacle> obstacles;
		
	/**
	 * Instantiates a new obstacle manager.
	 */
	public ObstacleManager()
	{
		randomGenerator = new Random(System.currentTimeMillis());
		randomArray = new Integer[100];
		obstacles = new ArrayList<Obstacle>();
		genArrayWithProbability();
		currentPosition = 80;
		leastRenderedObstacle = 0;
		instance = this;
	}
		
	/**
	 * Writes to stream
	 * @param dos Stream to write to 
	 */
	public void writeToStream(DataOutputStream dos) {
		try {
			dos.writeInt(NR_OF_OBSTACLES);
			
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
			NR_OF_OBSTACLES = dis.readInt(); 
			
		} catch(Exception e) {
			System.out.println("Error reading from stream in ObstacleManager.java: "+e.getMessage());
		}
	}
	
	/**
	 * Reads from bundle
	 * @param bundle the bundle to read from
	 */
	@SuppressWarnings("unchecked")
	public void readFromBundle(Bundle bundle) {
		obstacles = (ArrayList<Obstacle>)bundle.getSerializable("obstacles");
		wasRestored = true;
	}
	
	/**
	 * Generates array with probabilities.
	 */
	private void genArrayWithProbability()
	{
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		//distribute values according to probabilities
		for(int i=0; i <TYPE1_PROB;i++)
			tempList.add(OBSTACLE_TYPE1);
		for(int i=TYPE1_PROB; i <TYPE1_PROB+TYPE2_PROB;i++)
			tempList.add(OBSTACLE_TYPE2);
		for(int i=TYPE1_PROB+TYPE2_PROB; i <TYPE1_PROB+TYPE2_PROB+TYPE3_PROB;i++)
			tempList.add(OBSTACLE_TYPE3);
		for(int i=TYPE1_PROB+TYPE2_PROB+TYPE3_PROB; i <TYPE1_PROB+TYPE2_PROB+TYPE3_PROB+TYPE4_PROB;i++)
			tempList.add(OBSTACLE_TYPE4);
		
		Collections.shuffle(tempList);
		randomArray = tempList.toArray(randomArray);
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
		while(currentPosition< Settings.MAX_HEIGHT)
		{
			obstacles.add(new Obstacle(currentPosition, selectRandomType()));
			currentPosition += randomGenerator.nextInt((int)(30f*RenderView.instance.getAspectRatio())) + HORIZONTAL_SPACING;
		}
		
		obstacles.add(new Obstacle(currentPosition, OBSTACLE_TYPE_FINISH));
		NR_OF_OBSTACLES = obstacles.size();
	}
	
	/**
	 * Render visible obstacles.
	 *
	 * @param currentHeight the current height
	 */
	public void renderVisibleObstacles(float currentHeight)
	{
		RenderView renderView = RenderView.instance;
		if(mainChar == null)
			mainChar = renderView.getMainCharInstance();
		
		//calculate current topBounds value for relative position
		float topBounds = currentHeight+(int)renderView.getTopBounds();
		//Log.v("topBounds Height: ", String.valueOf(topBounds));

		int i = leastRenderedObstacle;
		
		while(i != NR_OF_OBSTACLES)
		{
			Obstacle tempObstacle = obstacles.get(i);
			
			//test visibility
			if(tempObstacle.virtualHeight < topBounds && tempObstacle.position.y > -tempObstacle.height)
			{
				//visible
				if(renderView.gameState != 2)
					tempObstacle.update();
				
				//render
				glPushMatrix();		

				glTranslatef(tempObstacle.position.x, tempObstacle.position.y, 0);									
				
				if(!Settings.GLES11Supported) 
				{
					glTexCoordPointer(2, GL10.GL_FLOAT, 0, textures[tempObstacle.type].texCoords);
					glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffers[tempObstacle.type]);
					glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				} 
				else 
				{
					geometryManager.bindVBO(vboIDs[tempObstacle.type]);
					glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				}
				
				glPopMatrix();
				
				if(testCollisionWithMainChar(tempObstacle) && !gameOver)
				{
					RenderView.instance.setGameOver(true);
					LevelActivity.handler.post(
							new Runnable()
							{
						    	@Override
						        public void run() 
						    	{
						            LevelActivity.instance.triggerVibrate(200);
						        }
							});
				}
			}
			i++;
		}
		
	}
	
	/**
	 * Creates the vertex buffer.
	 */
	public void preprocess()
	{
		GeometryManager geometryManager = GeometryManager.instance; 
		vertexBuffers = new FloatBuffer[5];
		vboIDs = new int[5];
		textures = new TexturePart[5];
		float aspectRatio = RenderView.instance.getAspectRatio();
		//wird noch umgebaut nur für testzwecke hier hardcoded
		vertexBuffers[0] = geometryManager.createVertexBufferQuad(100f, 25f);
		vertexBuffers[1] = geometryManager.createVertexBufferQuad(25f*aspectRatio, 25f*aspectRatio/1.333f);
		vertexBuffers[2] = geometryManager.createVertexBufferQuad(17f*aspectRatio,17f*aspectRatio);
		vertexBuffers[3] = geometryManager.createVertexBufferQuad(35f*aspectRatio*0.5f,35f*aspectRatio);
		vertexBuffers[4] = geometryManager.createVertexBufferQuad(25f*aspectRatio*0.5f, 25f*aspectRatio);
		
		textures[0] = TextureAtlas.instance.getFinishTextur();
		textures[1] = TextureAtlas.instance.getCowTextur();
		textures[2] = TextureAtlas.instance.getAmbossTextur();
		textures[3] = TextureAtlas.instance.getChuckTextur();
		textures[4] = TextureAtlas.instance.getLokusTextur();

		if(Settings.GLES11Supported) 
		{

			vboIDs[0] = geometryManager.createVBO(vertexBuffers[0], textures[0].texCoords);
			vboIDs[1] = geometryManager.createVBO(vertexBuffers[1], textures[1].texCoords);
			vboIDs[2] = geometryManager.createVBO(vertexBuffers[2], textures[2].texCoords);
			vboIDs[3] = geometryManager.createVBO(vertexBuffers[3], textures[3].texCoords);
			vboIDs[4] = geometryManager.createVBO(vertexBuffers[4], textures[4].texCoords);
		}
	}
	
	/**
	 * Test collision with main char.
	 *
	 * @param obstacle the obstacle
	 * @param currentHeight the current height
	 * @return true, if a collision happened
	 */
	private boolean testCollisionWithMainChar(Obstacle obstacle)
	{
		float leftMainChar, leftObstacle,
	    	rightMainChar, rightObstacle,
	    	topMainChar, topObstacle,
	    	bottomMainChar, bottomObstacle;

		leftMainChar = mainChar.getPosition().x*1.08f;
		rightMainChar = leftMainChar + mainChar.getWidth()*0.92f;
		bottomMainChar = mainChar.getPosition().y;
		topMainChar = mainChar.getHeight()*0.95f;
		
		leftObstacle = obstacle.position.x;
		rightObstacle = leftObstacle + obstacle.width;
		bottomObstacle = obstacle.position.y;		
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
	
	/**
	 * Reset the ObstacleManager
	 */
	public void reset()
	{
		if(!wasRestored)
		{
			leastRenderedObstacle=0;
			currentPosition=Settings.OBSTACLE_START;
			NR_OF_OBSTACLES = 0;
			obstacles.clear();
			generateObstacles();
		}
		else
			wasRestored=false;
	}
	
}
