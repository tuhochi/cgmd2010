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
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;


//just testing stuff, very primitive version
public class ObstacleManager 
{
	private static ObstacleManager instance;
	private int[] randomArray; 
	private Random randomGenerator;
	
	//current position for generating obstacle, 80 = start position
	private int currentPosition = 80;
	
	//lowest index of a visible obstacle in obstacles list
	private int leastRenderedObstacle;
	
	public final int NR_OF_OBSTACLES = 50;
	
	public final int OBSTACLE_TYPE1 = 1;
	public final int OBSTACLE_TYPE2 = 2;
	public final int OBSTACLE_TYPE3 = 3;
	public final int OBSTACLE_TYPE4 = 4;
	
	//in numbers per 100, must sum up to 100
	public final static int TYPE1_PROB = 25;
	public final static int TYPE2_PROB = 25;
	public final static int TYPE3_PROB = 25;
	public final static int TYPE4_PROB = 25;
	
	//horizontal spacing between obstacles, do more advanced stuff with it (random?)
	public final static int HORIZONTAL_SPACING = 100;
	
	public class Obstacle
	{	
		public int width;
		public int height;
		public Vector2 position;
		public int type;
		
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
	
	private ArrayList<Obstacle> obstacles;
	private ShortBuffer indexBuffer;
	private FloatBuffer vertexBuffer;
	
	public ObstacleManager()
	{
		randomGenerator = new Random(System.currentTimeMillis());
		randomArray = new int[100];
		obstacles = new ArrayList<Obstacle>();
		createVertexBuffer();
		genArrayWithProbability();
		instance = this;
	}
	
	public static ObstacleManager getInstance()
	{
		if(instance==null)
			instance = new ObstacleManager();
		return instance;		
	}
	
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
	
	private int selectRandomType()
	{
		return randomArray[randomGenerator.nextInt(99)];
	}
	
	public void generateObstacles()
	{
		for(int i=0; i < NR_OF_OBSTACLES;i++)
		{
			obstacles.add(new Obstacle(currentPosition, selectRandomType()));
			currentPosition += HORIZONTAL_SPACING;
		}
	}
	
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
			
			//do more advanced stuff here
//			switch(tempObstacle.type)
//			{
//				case(OBSTACLE_TYPE1):
//					posX=0;
//					break;
//				case(OBSTACLE_TYPE2):
//					posX=20;
//					break;
//				case(OBSTACLE_TYPE3):
//					posX=40;
//					break;
//				case(OBSTACLE_TYPE4):
//					posX=80;
//					break;			
//			}
			
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
//				glTranslatef(tempObstacle.width/2,tempObstacle.height/2,0);		
				glScalef(tempObstacle.width,tempObstacle.height,1);	
				glTranslatef(tempObstacle.position.x/(float)tempObstacle.width, (posY - currentHeight)/(float)tempObstacle.height, 0);
									
				glDrawElements(GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, indexBuffer);
				glPopMatrix();
				
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
	
}
