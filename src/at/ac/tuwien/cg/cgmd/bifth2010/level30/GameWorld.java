package at.ac.tuwien.cg.cgmd.bifth2010.level30;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Date;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level30.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level30.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level30.math.Vector3;

/*
 * Implements game loop, game logic and rendering.
 */
public class GameWorld extends Thread {
	
	//variables to keep track of time:
    private float elapsedSeconds;
    private long oldTime;
    private long time;
    
	//level state
    boolean pause;
	private float progress;
	private boolean isFinished;	
	boolean isInitialized;
	
	/** VBO for graph vertices*/
	private FloatBuffer vertexBuffer[];
	/** VBO for graph normals*/
	private FloatBuffer normalBuffer[];
	
	/** VBO for line indicator vertices*/
	private FloatBuffer vertexBufferIndicatorLine;
	/** VBO for line indicator normals*/
	private FloatBuffer normalBufferIndicatorLine;
	
	/** VBO for current price indicator vertices*/
	private FloatBuffer vertexBufferIndicator;
	/** VBO for current price indicator normals*/
	private FloatBuffer normalBufferIndicator;	
	
	//properties of stockmarket graphs
	private int numGraphs; 
	private int numElements;
	private int elementsPerObject;
	private float graphWidth;
	private float pointDistance = 0.1f;

	//random stockmarket values
	private float stockMarket[][];		
	private float currentMoney;
	
	enum TransactionType {BUY, SELL, NOTHING};	
	private TransactionType transactionType[];
	private float transactionAmount[];
	
	//for callbacks to GUI
	private Handler handler;
	private LevelActivity context;
	private Bundle savedInstance;

	/* Implements the game loop.
	 * (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
    public void run() 
    {
    	while(isFinished==false)	
    	{
    		Framemove(); 
    		try {
				sleep(20);
			} catch (InterruptedException e) {				
			}			
    	}
    	
    	Log.d("L30", "level finished");
    	
    	if (currentMoney<0.0f)
    	{
    		try {
				sleep(1500);
			} catch (InterruptedException e) {				
			}
    	}
    	
    	class FinishRunnable implements Runnable{
        	@Override
            public void run() {
                context.finish();
            }
        };
        
        Runnable finishRunnable = new FinishRunnable();
        handler.post(finishRunnable);	
    }

	/**
	 * Construct a new GameWorld
	 * @param LevelActivity The context for acess to the UI
	 * @param _handler A handler for callbacks
	 * @param _savedInstance Saved instance information
	 */
    public GameWorld(LevelActivity levelActivity, Handler _handler, Bundle _savedInstance)
    {
    	isFinished = false;
    	
    	context = levelActivity;
    	handler = _handler;
    	savedInstance = _savedInstance;
    	isInitialized = false;
    	
		currentMoney = 1000000;
		progress = 0.0f;
		
		//set properties of graphs
		numGraphs = 4;
		numElements = 512;
		elementsPerObject = 6; // two triangles per quad
		graphWidth = 0.15f;
		pointDistance = 0.1f;
		
		transactionType = new TransactionType[numGraphs];
		transactionAmount = new float[numGraphs];

    }	
    
    /*
     * Game logic per frame
     */
	public synchronized void Framemove()
	{		
		if(pause)
			return;
		
		if (isInitialized==false)
			return;
		
		if (isFinished==true)
			return;
		
		oldTime = time;        
        Date date = new Date();
        time = date.getTime();
        elapsedSeconds = (time - oldTime) / 1000.0f;
        
        float oldProgress = progress;
        progress += elapsedSeconds/2.0f;
        	
        //update the money
        
        for (int i=0; i<numGraphs; i++)
        {  
        	float oldPrice = getPrice(i, oldProgress);
        	float newPrice = getPrice(i, progress);
        	
        	if (transactionType[i] == TransactionType.BUY)
        	{
        		currentMoney += (newPrice-oldPrice)*200000.0f;
        	}        
        }		
        
        moneyChanged(currentMoney);
        
        if (progress>((float)numElements)*pointDistance)
        {
        	isFinished = true;
		}
        
        if (currentMoney<0.0f)
        	isFinished = true;
        
	}	
	
	/*
	 * Initialize graphs and OpenGL
	 * @param gl OpenGL object
	 */
	public synchronized void Init(GL10 gl)
	{				
		//initialize the graphs and vbos		
		for (int i=0; i<numGraphs; i++)
		{
			transactionType[i] = TransactionType.NOTHING;
			transactionAmount[i] = 0.0f;		
		}
		
		//create arrays for vbo data
		int verticesPerObject = elementsPerObject*3;		
		float normals[][] = new float[numGraphs][numElements*verticesPerObject];
		float vertices[][] = new float[numGraphs][numElements*verticesPerObject];		
		vertexBuffer = new FloatBuffer[numElements];
		normalBuffer = new FloatBuffer[numElements];
		       
		stockMarket = new float[numGraphs][numElements+1];
		float maxRange = 0.8f;
		
		//fill with random numbers. numbers are a markov chain.		
		for (int i=0; i<numGraphs; i++)
		{
			float current = ((float) Math.random()- 0.5f)/20.0f;
			
			for (int j=0;j<numElements; j++)
			{		
				stockMarket[i][j] = current;
				
				float next = current + ((float)Math.random()-0.5f)/4.0f;
				
				//clamp to sane values
				next = Math.min(Math.max(next,-maxRange), maxRange);
				
				int cnt = 0;
				int offset = j*verticesPerObject;
				
				//triangle 1:				
				vertices[i][offset+(cnt++)] = 0.0f; //x
				vertices[i][offset+(cnt++)] = current; //y
				vertices[i][offset+(cnt++)] = (float)j * pointDistance; //z
				
				vertices[i][offset+(cnt++)] = graphWidth; //x
				vertices[i][offset+(cnt++)] = current; //y
				vertices[i][offset+(cnt++)] = (float)j * pointDistance; //z
				
				vertices[i][offset+(cnt++)] = graphWidth; //x
				vertices[i][offset+(cnt++)] = next; //y
				vertices[i][offset+(cnt++)] = (float)(j+1) * pointDistance; //z		
				
				//triangle 2:	
				vertices[i][offset+(cnt++)] = graphWidth; //x
				vertices[i][offset+(cnt++)] = next; //y
				vertices[i][offset+(cnt++)] = (float)(j+1) * pointDistance; //z
				
				vertices[i][offset+(cnt++)] = 0.0f; //x
				vertices[i][offset+(cnt++)] = next; //y
				vertices[i][offset+(cnt++)] = (float)(j+1) * pointDistance; //z
				
				vertices[i][offset+(cnt++)] = 0.0f; //x
				vertices[i][offset+(cnt++)] = current; //y
				vertices[i][offset+(cnt++)] = (float)j * pointDistance; //z		

				current = next;
				
			}
			
			stockMarket[i][numElements] = current;
		
			//calc normals
			for (int j=0;j<numElements; j++)
			{
				int cnt = 0;
				int offset = j*verticesPerObject;
				
				Vector3 v1 = new Vector3(0.0f, stockMarket[i][j], 0.0f);
				Vector3 v2 = new Vector3(0.0f, stockMarket[i][j+1], pointDistance);
				
				Vector3 direction = Vector3.diff(v2,v1);
				direction.normalize();
				
				Vector3 right = new Vector3(1.0f, 0.0f, 0.0f);
				
				Vector3 normal = Vector3.crossProduct(direction, right);
				normal.normalize();
				
				for (int k=0; k<6; k++)
				{
					normals[i][offset+(cnt++)] = normal.x; //x
					normals[i][offset+(cnt++)] = normal.y; //y
					normals[i][offset+(cnt++)] = normal.z; //z
				}
			}
		
		
			//copy to VBO
			ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices[i].length*4);
			byteBuf.order(ByteOrder.nativeOrder());
			vertexBuffer[i] = byteBuf.asFloatBuffer();
			vertexBuffer[i].put(vertices[i]);
			vertexBuffer[i].position(0);	

			byteBuf = ByteBuffer.allocateDirect(normals[i].length*4);
			byteBuf.order(ByteOrder.nativeOrder());
			normalBuffer[i] = byteBuf.asFloatBuffer();
			normalBuffer[i].put(normals[i]);
			normalBuffer[i].position(0);
		
		}
		
		//create a quad to indicate the current price
		float[] vertices2 = new float[3*6];
		float[] normals2 = new float[3*6];
		
		float quadSize = graphWidth/2.0f;
		int cnt=0;
		
		//triangle 1
		vertices2[cnt++] = -quadSize; //x;
		vertices2[cnt++] = -quadSize; //y;
		vertices2[cnt++] = 0.0f; //z;
		
		vertices2[cnt++] = quadSize; //x;
		vertices2[cnt++] = -quadSize; //y;
		vertices2[cnt++] = 0.0f; //z;	
		
		vertices2[cnt++] = quadSize; //x;
		vertices2[cnt++] = quadSize; //y;
		vertices2[cnt++] = 0.0f; //z;	
		
		//triangle 2
		vertices2[cnt++] = quadSize; //x;
		vertices2[cnt++] = quadSize; //y;
		vertices2[cnt++] = 0.0f; //z;	
		
		vertices2[cnt++] = -quadSize; //x;
		vertices2[cnt++] = quadSize; //y;
		vertices2[cnt++] = 0.0f; //z;	
		
		vertices2[cnt++] = -quadSize; //x;
		vertices2[cnt++] = -quadSize; //y;
		vertices2[cnt++] = 0.0f; //z;
		
		cnt = 0;
		for (int i=0; i<6; i++)
		{
			normals2[cnt++] = 0.0f;
			normals2[cnt++] = 0.0f;
			normals2[cnt++] = 1.0f;
		}
		
		//copy to VBO
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices2.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBufferIndicatorLine = byteBuf.asFloatBuffer();
		vertexBufferIndicatorLine.put(vertices2);
		vertexBufferIndicatorLine.position(0);

		byteBuf = ByteBuffer.allocateDirect(normals2.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBufferIndicatorLine = byteBuf.asFloatBuffer();
		normalBufferIndicatorLine.put(normals2);
		normalBufferIndicatorLine.position(0);
		
		//just a looong line
		cnt = 0;
		vertices2 = new float[3*2];
		normals2 = new float[3*2];
		vertices2[cnt++] = 0; //x;
		vertices2[cnt++] = 0; //y;
		vertices2[cnt++] = -100.0f; //z;
		
		vertices2[cnt++] = 0; //x;
		vertices2[cnt++] = 0; //y;
		vertices2[cnt++] = 100.0f; //z;		

		cnt = 0;
		for (int i=0; i<2; i++)
		{
			normals2[cnt++] = 0.0f;
			normals2[cnt++] = 0.0f;
			normals2[cnt++] = 1.0f;
		}
		
		//copy to VBO
		byteBuf = ByteBuffer.allocateDirect(vertices2.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBufferIndicator = byteBuf.asFloatBuffer();
		vertexBufferIndicator.put(vertices2);
		vertexBufferIndicator.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(normals2.length*4);
		byteBuf.order(ByteOrder.nativeOrder());
		normalBufferIndicator = byteBuf.asFloatBuffer();
		normalBufferIndicator.put(normals2);
		normalBufferIndicator.position(0);		
		
		//init GL
		
		//set camera position
		gl.glMatrixMode(GL10.GL_MODELVIEW); 	
		gl.glLoadIdentity();		
		GLU.gluLookAt(gl, 0.0f, 1.0f, -3, 0, 0, 1, 0, 1, 0);
		
		//initialize lights
		float[] lightAmbient = {0.5f, 0.5f, 0.5f, 1.0f};
		float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] lightPosition = {0.0f, 4.0f, 2.0f, 1.0f};			
		initLight(gl, GL10.GL_LIGHT0, lightAmbient, lightDiffuse, lightPosition);        		
        gl.glEnable(GL10.GL_LIGHT0);        
		
        float[] lightAmbient2 = {0.5f, 0.5f, 0.5f, 1.0f};
		float[] lightDiffuse2 = {1.0f, 1.0f, 1.0f, 1.0f};
		float[] lightPosition2 = {2.0f, -4.0f, 2.0f, 1.0f};	
		initLight(gl, GL10.GL_LIGHT1, lightAmbient2, lightDiffuse2, lightPosition2);        		
        gl.glEnable(GL10.GL_LIGHT1);
        
		gl.glEnable(GL10.GL_LIGHTING);
		 
        Date date = new Date();
        time = date.getTime();
        oldTime = time;
        
        isInitialized = true;
        
	}
	
	/*
	 * Initialize one light	 
	 */
	private void initLight(GL10 gl, int lightId,  float[] lightAmbient, float[] lightDiffuse, float[] lightPosition)
	{
		/* The buffers for our light values ( NEW ) */
		FloatBuffer lightAmbientBuffer;
		FloatBuffer lightDiffuseBuffer;
		FloatBuffer lightPositionBuffer;
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(lightAmbient.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightAmbientBuffer = byteBuf.asFloatBuffer();
		lightAmbientBuffer.put(lightAmbient);
		lightAmbientBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightDiffuse.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightDiffuseBuffer = byteBuf.asFloatBuffer();
		lightDiffuseBuffer.put(lightDiffuse);
		lightDiffuseBuffer.position(0);
		
		byteBuf = ByteBuffer.allocateDirect(lightPosition.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		lightPositionBuffer = byteBuf.asFloatBuffer();
		lightPositionBuffer.put(lightPosition);
		lightPositionBuffer.position(0);		
        
		gl.glLightfv(lightId, GL10.GL_AMBIENT, lightAmbientBuffer);
        gl.glLightfv(lightId, GL10.GL_DIFFUSE, lightDiffuseBuffer);
        gl.glLightfv(lightId, GL10.GL_POSITION, lightPositionBuffer);
	
	}
	

	/*
	 * Draw scene
	 * @param gl OpenGL object
	 */
	public synchronized void Draw(GL10 gl)
	{	
	
		
		if (isInitialized==false)
			return;
	
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

		//Enable the vertex, texture and normal state
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		//Set the face rotation
		gl.glFrontFace(GL10.GL_CCW);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW); 	

		
		/*float[] fogColor = {1.0f,1.0f,1.0f,1.0f};
		gl.glFogfv(GL10.GL_FOG_COLOR, fogColor, 0);
		gl.glFogf(GL10.GL_FOG_START, 6.0f);
		gl.glFogf(GL10.GL_FOG_END, 15.0f);
		gl.glFogf(GL10.GL_FOG_DENSITY, 0.5f);
		gl.glFogx(GL10.GL_FOG_MODE, GL10.GL_LINEAR);
		gl.glEnable(GL10.GL_FOG);*/
		
		float[] red = {1.0f,0.0f,0.0f, 1.0f};
		float[] green = {0.0f,1.0f,0.0f, 1.0f};
		float[] blue = {0.0f,0.0f,1.0f, 1.0f};
		float[] orange = {1.0f,0.5f,0.0f, 1.0f};		
		float[] black = {0.0f,0.0f,0.0f, 1.0f};
		float[] grey = {0.1f,0.1f,0.1f, 1.0f};
		
		float totalWidth = 3.0f;
		
		for (int i=0; i<numGraphs; i++)
		{
			//draw graph
			//1st: set material
			switch (i%4)
			{
			case 0: gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, red,0); break;
			case 1: gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, green,0);break;
			case 2: gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, blue,0);break;
			case 3: gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, orange,0);	break;		
			}
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, grey,0);
		
			//calc offset for graph position		 
			float graphXcoord = ((float)i/(float)(numGraphs-1))*totalWidth - totalWidth/2.0f;
			graphXcoord = -graphXcoord - graphWidth/2.0f;
			
			//move graphs more outward => easier to see
			if (i<(numGraphs/2))
			{				
				graphXcoord+=0.3f;
			}
			else
				graphXcoord-=0.3f;
				
			
			float graphYcoord = 0.0f;
			
			gl.glPushMatrix();			
			gl.glTranslatef(graphXcoord, graphYcoord, -progress);
			
			//Point to our buffers
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer[i]);			
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer[i]);
			
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, numElements*elementsPerObject);
			
			gl.glPopMatrix();			
			gl.glPushMatrix();

			//set material for price indicators
			switch (i%4)
			{
			case 0: gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, red,0); break;
			case 1: gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, green,0);break;
			case 2: gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, blue,0);break;
			case 3: gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, orange,0);	break;		
			}
			gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, black,0);
			
			//draw quads at current price
			float price = getCurrentPrice(i);
			
			gl.glTranslatef(graphXcoord  + graphWidth/2.0f,price+graphYcoord, 0.0f);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferIndicatorLine);			
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBufferIndicatorLine);			
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
			
			//draw line of current price or transaction amount
			gl.glLineWidth(2.0f);			
			gl.glPopMatrix();	
			gl.glPushMatrix();
			
			if (transactionType[i]==TransactionType.BUY)
				gl.glTranslatef(graphXcoord+graphWidth/2.0f,transactionAmount[i]+graphYcoord, 0.0f);
			else
				gl.glTranslatef(graphXcoord+graphWidth/2.0f,price+graphYcoord, 0.0f);					
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferIndicator);			
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBufferIndicator);						
			gl.glDrawArrays(GL10.GL_LINES, 0, 2);			
			gl.glPopMatrix();				
			
			
		}
		
		
		//Disable the client state before leaving
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);


	}
	
	/*
	 * Drawing surface has changed => reinitialize
	 */
	public synchronized void onSurfaceChanged(GL10 gl, int width, int height)
	{
		if(height == 0) { 						
			height = 1; 						
		}
		gl.glViewport(0, 0, width, height); 	
		gl.glMatrixMode(GL10.GL_PROJECTION); 	
		gl.glLoadIdentity(); 
		GLU.gluPerspective(gl, 60.0f, (float)width / (float)height, 0.1f, 200.0f);	
	}

	/*
	 * Get price of the specified graph at a specific progress
	 */
	float getPrice(int graphNum, float time)
	{
		//scale time with drawing distance of points
		float timeScaled = time/pointDistance;
		
		if ((int)timeScaled < (numElements-1))	
		{
			//linear interpolate this and next point
			float a = stockMarket[graphNum][(int)timeScaled];
			float b = stockMarket[graphNum][((int)timeScaled)+1];			
			float frac = timeScaled - (float)Math.floor(timeScaled);			
			return b*frac + a*(1.0f-frac);
			
		}
		else
			return stockMarket[graphNum][numElements];		
		
	}
	
	/*
	 * Get price of specified graph at the current progress
	 */
	float getCurrentPrice(int graphNum)
	{
		return getPrice(graphNum, progress);
	}

	/*
	 * Perform one transaction on the stock market.
	 */
	public synchronized void StockMarketTransaktion(int graphNum, TransactionType type)
	{
		if (isInitialized==false)
			return;
		
		switch (type)
		{
		case BUY:			
			if ((transactionType[graphNum]==TransactionType.SELL)||
				(transactionType[graphNum]==TransactionType.NOTHING)) //safety guards. only buy one time
			{
				transactionType[graphNum]=TransactionType.BUY;	
				transactionAmount[graphNum] = getCurrentPrice(graphNum);
			}
			
			break;
		case SELL:
			if (transactionType[graphNum]==TransactionType.BUY)
				{
					transactionType[graphNum]=TransactionType.SELL;	
					transactionAmount[graphNum] = 0.0f;
				}			
			break;		
		}
	}

	/*public synchronized void InputDown(Vector2 pos)
	{		
	}	

	public synchronized void InputMove(Vector2 pos)
	{		
	}	

	public synchronized void InputUp(Vector2 pos)
	{		
	}*/

	/*
	 * Pause our level thread
	 */
	public synchronized void SetPause(boolean _pause)
	{
		pause = _pause;
	}

	/*
	 * As our level is completely random, it does not make any sense
	 * to save the instance state. Level is simply restarted.
	 */
	public void onSaveInstanceState(Bundle outState)
	{	
	}
	
	/*
	 * Call the GUI when the money changes through handlers
	 * @param money The current amount of money
	 */
	public void moneyChanged(float money) {		
		class MoneyRunnable implements Runnable{
        	float money;
        	public MoneyRunnable(float _money)
        	{
        		money = _money;
        	}
        	@Override
            public void run() {
                context.playerMoneyChanged(money);
            }
        };        
        Runnable moneyrunnable = new MoneyRunnable(money);
        handler.post(moneyrunnable);		
	}
	
}
