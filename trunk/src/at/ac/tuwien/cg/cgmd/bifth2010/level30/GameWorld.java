package at.ac.tuwien.cg.cgmd.bifth2010.level30;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
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
	/** VBO for graph texcoords*/
	private FloatBuffer texcoordsBuffer[];
	
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
	
	//stockmarket state
	enum TransactionType {BUY, SELL, NOTHING};	
	private TransactionType transactionType[];
	private float transactionAmount[];
	
	//for callbacks to GUI
	private Handler handler;
	private LevelActivity context;
	private Bundle savedInstance;
	
	//for particle system
	private List<ParticleSystem> particleSystems = new ArrayList<ParticleSystem>();
	private RenderableQuad quad;
	private int texCoin; //store texture id
	private int texSteel; //store texture id
	private int texTable; //store texture id
	private Map<Integer,ArrayList<ParticleSystem>> graphToParticleSystems;
	
	//keep track of the level state
	private int gameState = 0;
	boolean quitThread;	
	boolean soundEnabled = true;

	//cylinder for stockmarket texture
	FFModel cylinder;
	
	//speed of level
	float progressScale = 0.5f;

	/*
	 * When quitThread==true the gameloop whil exit
	 */
	void SetQuitThread(boolean _quitThread)
	{
		quitThread = _quitThread;
	}
	
	//for the sounds
	private SoundPool soundPool;
	private HashMap<Integer, Integer> soundPoolMap;
	private boolean playedOutOfTimeWarning = false;

	/*
	 * Initialize all sounds into a soundpool
	 */
	private void initSounds() {
	     soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 100);
	     soundPoolMap = new HashMap<Integer, Integer>();
	     soundPoolMap.put(0, soundPool.load(context, R.raw.l30_yeah, 1));
	     soundPoolMap.put(1, soundPool.load(context, R.raw.l30_ohno, 1));
	     soundPoolMap.put(2, soundPool.load(context, R.raw.l30_win, 2));
	     soundPoolMap.put(3, soundPool.load(context, R.raw.l30_outoftime, 3));
	     soundPoolMap.put(4, soundPool.load(context, R.raw.l30_click, 1));
	     soundPoolMap.put(5, soundPool.load(context, R.raw.l30_lose, 3));
	     soundPoolMap.put(6, soundPool.load(context, R.raw.l30_beep, 2));
	}
	          
	/*
	 * Play one sound
	 */
	public void playSound(int sound) {
		if (soundEnabled==false)
			return;
		
	    /* Updated: The next 4 lines calculate the current volume in a scale of 0.0 to 1.0 */
	    AudioManager mgr = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	    float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
	    float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);    
	    float volume = streamVolumeCurrent / streamVolumeMax;
	    
	    /* Play the sound with the correct volume */
	    soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f);     
	}

 
	  
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
			
			if (quitThread==true)
				return;
    	}    	
    	
    	//Log.d("l30", "level finished");    	
    	
    	if (currentMoney<0.0f)
    	{
    		playSound(2);
    		try {
				sleep(5000);
			} catch (InterruptedException e) {				
			}
    	}
    	else
    	{
    		playSound(5);  
    	}
    	
    	class FinishRunnable implements Runnable{
        	@Override
            public void run() {
                context.prepareFinish();
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
    public GameWorld(LevelActivity levelActivity, Handler _handler, Bundle _savedInstance,
    		boolean _soundEnabled)
    {
    	quitThread = false;
    	
    	//Log.d("l30", "GameWorld constructor");
    	
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
		pointDistance = 0.136f;
		
		transactionType = new TransactionType[numGraphs];
		transactionAmount = new float[numGraphs];
		
		for (int i=0; i<numGraphs; i++)
		{
			transactionType[i] = TransactionType.NOTHING;
			transactionAmount[i] = 0.0f;		
		}
		
		//fill with random numbers. numbers are a markov chain.		
		stockMarket = new float[numGraphs][numElements+1];
		float maxRange = 0.8f;
		
		//fill with random numbers. numbers are a markov chain.		
		for (int i=0; i<numGraphs; i++)
		{
			float current = ((float) Math.random()- 0.5f)/20.0f;
			
			for (int j=0;j<numElements; j++)
			{		
				stockMarket[i][j] = current;
				
				float next = current + ((float)Math.random()-0.5f)/3.0f;
				
				//clamp to sane values
				next = Math.min(Math.max(next,-maxRange), maxRange);
				
				current = next;
			}
			stockMarket[i][numElements] = current;
		}
		
		//init particle systems
		graphToParticleSystems = new  HashMap<Integer,ArrayList<ParticleSystem>>();		
		for (int i=0; i<numGraphs; i++)
			graphToParticleSystems.put(i, new ArrayList<ParticleSystem>());
		
		//init timesteps
        Date date = new Date();
        time = date.getTime();
        oldTime = time;
        
        //init sounds
        initSounds();        
    	soundEnabled = _soundEnabled;
    	
    	cylinder = new FFModel(1.0f,1.0f,2);
       
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
		
		//calc timestep
		oldTime = time;        
        Date date = new Date();
        time = date.getTime();
        elapsedSeconds = (time - oldTime) / 1000.0f;
        
        //add to level progress
        float oldProgress = progress;
        progress += elapsedSeconds*progressScale;
        	
        //update the money        
        for (int i=0; i<numGraphs; i++)
        {  
        	float oldPrice = getPrice(i, oldProgress);
        	float newPrice = getPrice(i, progress);
        	
        	if (transactionType[i] == TransactionType.BUY)
        	{
        		currentMoney += (newPrice-oldPrice)*130000.0f;
        	}        
        }		
        
        //notify the gui
        moneyChanged(currentMoney);
        
        //progress near the end? -> play a warning
        if ((progress+8.5)>((float)numElements)*pointDistance)
        {
        	if (playedOutOfTimeWarning==false)
        	{
        		//reduce music volume to make warning audible
        		playSound(3);
        	}
        	playedOutOfTimeWarning = true;
		}
        
        //finished?
        if (progress>((float)numElements)*pointDistance)
        {
        	isFinished = true;
		}
        
        //all money lost?
        if (currentMoney<0.0f)
        	isFinished = true;
		
        //update particle systems
        List<ParticleSystem> particleSystemsToDelete = new ArrayList<ParticleSystem>();
        
        for(ParticleSystem particleSys: particleSystems)
		{
        	particleSys.Update(elapsedSeconds);
        	
        	//remove particle systems that have exploded
        	if (particleSys.ExplosionFinished()==true)
        	{        		
        		particleSystemsToDelete.add(particleSys);        		
        	}
		}
        
        //remove particle systems after explosion
        for(ParticleSystem particleSys: particleSystemsToDelete)
		{
        	particleSystems.remove(particleSys);
        	
        	for (ArrayList<ParticleSystem> systems: graphToParticleSystems.values())
        	{        		
        		systems.remove(particleSys);     	
        	}
        	
		}
        
        //change level states
        if ( (gameState==0) && (progress/progressScale > 15.0f) )
        {
        	//start "more" blinking
        	gameState = 1;
        	playSound(6);
        	ChangeGameState(gameState);
        }
        else if ( (gameState==1) && (progress/progressScale > 17.0f) )
        {
        	//start "more" phase
        	gameState = 2;
        	ChangeGameState(gameState);        	
        }
        else if ( (gameState==2) && (progress/progressScale > 70.0f) )
        {
        	//start "faster" blinking
        	playSound(6);
        	gameState = 3;
        	ChangeGameState(gameState);        	
        }      	
        else if ( (gameState==3) && (progress/progressScale > 72.0f) )
        {
        	//start "faster" phase
        	gameState = 4;
        	progressScale = 1.2f;
        	ChangeGameState(gameState);        	
        }   
        else if ( (gameState==4) && ((progress+5.0)>((float)numElements)*pointDistance) )
        {
        	//start "hurry up" blinking
        	playSound(6);
        	gameState = 5;        	
        	ChangeGameState(gameState);        	
        }         
	}	
	
	/*
	 * Load one texture from resources to opengl
	 */
	public int LoadOneTextures(GL10 gl, int resourceID)
	{	
		InputStream is = context.getResources().openRawResource(resourceID);
		
		int[] tex = new int[1];		
		gl.glGenTextures(1, tex, 0);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, tex[0]); 
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR); 
		gl.glTexParameterx(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR); 
        
		Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
            } catch(IOException e) {
            }
        }

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();	
        
        return tex[0];
	}
	
	
	/*
	 * Load all the textures
	 */
	public void LoadTextures(GL10 gl)
	{	
		texCoin = LoadOneTextures(gl, R.drawable.l30_coin);
		texSteel = LoadOneTextures(gl, R.drawable.l30_steel);
		texTable = LoadOneTextures(gl, R.drawable.l30_table);	
	}
	
	/*
	 * Initialize graphs and OpenGL
	 * @param gl OpenGL object
	 */
	public synchronized void Init(GL10 gl)
	{				
		//Log.d("l30", "GameWorld init");
		
		//initialize the graphs and vbos
		//create arrays for vbo data
		int verticesPerObject = elementsPerObject*3;		
		float normals[][] = new float[numGraphs][numElements*verticesPerObject];
		float vertices[][] = new float[numGraphs][numElements*verticesPerObject];
		float texcoords[][] = new float[numGraphs][numElements*elementsPerObject*2];
		vertexBuffer = new FloatBuffer[numElements];
		normalBuffer = new FloatBuffer[numElements];
		texcoordsBuffer = new FloatBuffer[numElements];		
		
		//put stockmarket into vbo
		for (int i=0; i<numGraphs; i++)
		{
			for (int j=0;j<numElements; j++)
			{		
				float current = stockMarket[i][j];
				float next = stockMarket[i][j+1];
				
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
			
			
		
			//calc normals&texcoords
			float texcoordV = 0.0f;
			for (int j=0;j<numElements; j++)
			{
				int cnt = 0;
				int offset = j*verticesPerObject;
				
				Vector3 v1 = new Vector3(0.0f, stockMarket[i][j], 0.0f);
				Vector3 v2 = new Vector3(0.0f, stockMarket[i][j+1], pointDistance);
				
				Vector3 direction = Vector3.diff(v2,v1);
				float texcoordVdelta = direction.length();
				float texcoordVnext = texcoordV+texcoordVdelta;
				direction.normalize();
				
				Vector3 right = new Vector3(1.0f, 0.0f, 0.0f);
				
				Vector3 normal = Vector3.crossProduct(direction, right);
				normal.normalize();
				
				for (int k=0; k<elementsPerObject; k++)
				{
					normals[i][offset+(cnt++)] = normal.x; //x
					normals[i][offset+(cnt++)] = normal.y; //y
					normals[i][offset+(cnt++)] = normal.z; //z
				}
				
				int offsetTextures = j*elementsPerObject*2;
				cnt = 0;
				//triangle 1
				texcoords[i][offsetTextures+(cnt++)] = 0.0f;  //u
				texcoords[i][offsetTextures+(cnt++)] = texcoordV;  //v
				
				texcoords[i][offsetTextures+(cnt++)] = 1.0f;  //u
				texcoords[i][offsetTextures+(cnt++)] = texcoordV;  //v	
				
				texcoords[i][offsetTextures+(cnt++)] = 1.0f;  //u
				texcoords[i][offsetTextures+(cnt++)] = texcoordVnext;  //v	
				
				//triangle 2
				texcoords[i][offsetTextures+(cnt++)] = 1.0f;  //u
				texcoords[i][offsetTextures+(cnt++)] = texcoordVnext;  //v
				
				texcoords[i][offsetTextures+(cnt++)] = 0.0f;  //u
				texcoords[i][offsetTextures+(cnt++)] = texcoordVnext;  //v
				
				texcoords[i][offsetTextures+(cnt++)] = 0.0f;  //u
				texcoords[i][offsetTextures+(cnt++)] = texcoordV;  //v				

				texcoordV = texcoordVnext;
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
			
			byteBuf = ByteBuffer.allocateDirect(texcoords[i].length*4);
			byteBuf.order(ByteOrder.nativeOrder());
			texcoordsBuffer[i] = byteBuf.asFloatBuffer();
			texcoordsBuffer[i].put(texcoords[i]);
			texcoordsBuffer[i].position(0);			
		
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
		GLU.gluLookAt(gl, 0.0f, 1.0f, -3, 0, -0.2f, 1, 0, 1, 0);
		
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
       

           
        quad = new RenderableQuad(1.0f,1.0f);        
        LoadTextures(gl);  
        
        isInitialized = true;
        
        //make sure there is a sane timestep
        Date date = new Date();
        time = date.getTime();
        oldTime = time;
        
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
	 * Calculate the vertical drawing offset for one graph
	 */
	private float CalcGraphXCoord(int i)
	{		
		float totalWidth = 3.0f;
		float graphXcoord = ((float)i/(float)(numGraphs-1))*totalWidth - totalWidth/2.0f;
		graphXcoord = -graphXcoord - graphWidth/2.0f;
		
		//move graphs more outward => easier to see
		if (i<(numGraphs/2))
		{				
			graphXcoord+=0.3f;
		}
		else
			graphXcoord-=0.3f;
		
		return graphXcoord;
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
		
		float ambient = 0.2f;

		float[] red = {1.0f,ambient,ambient, 1.0f};
		float[] green = {ambient,1.0f,ambient, 1.0f};
		float[] blue = {ambient,ambient,1.0f, 1.0f};
		float[] orange = {1.0f,0.5f,ambient, 1.0f};		
		float[] black = {ambient,ambient,ambient, 1.0f};
		float[] grey = {0.1f,0.1f,0.1f, 1.0f};
		
    	gl.glEnable(GL10.GL_TEXTURE_2D);
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, texTable);  
    	
    	gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    	
    	gl.glPushMatrix();
		//draw the rotating background
    	
    	gl.glRotatef((float) Math.PI, 0.0f, 1.0f, 0.0f);
		//cylinder.draw(gl);
		
		gl.glPopMatrix();
		gl.glDisable(GL10.GL_TEXTURE_2D);

		for (int i=0; i<numGraphs; i++)
		{
			if ((gameState<2) && (i>0))
			{
				break;
			}
			
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
		
	    	gl.glEnable(GL10.GL_TEXTURE_2D);
	    	gl.glBindTexture(GL10.GL_TEXTURE_2D, texSteel);  
	    	
			//calc offset for graph position		 
			float graphXcoord = CalcGraphXCoord(i);			
			
			gl.glPushMatrix();			
			gl.glTranslatef(graphXcoord, 0.0f, -progress);
			
			//Point to our buffers
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer[i]);			
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBuffer[i]);
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texcoordsBuffer[i]);
			
			//Draw the vertices as triangles, based on the Index Buffer information
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, numElements*elementsPerObject);
			
			gl.glPopMatrix();			
			gl.glPushMatrix();
			
			gl.glDisable(GL10.GL_TEXTURE_2D);

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
			
			gl.glTranslatef(graphXcoord  + graphWidth/2.0f,price, 0.0f);
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferIndicatorLine);			
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBufferIndicatorLine);	
			
			gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 6);
			
			//draw line of current price or transaction amount
			gl.glLineWidth(2.0f);			
			gl.glPopMatrix();	
			gl.glPushMatrix();
			
			if (transactionType[i]==TransactionType.BUY)
				gl.glTranslatef(graphXcoord+graphWidth/2.0f,transactionAmount[i], 0.0f);
			else
				gl.glTranslatef(graphXcoord+graphWidth/2.0f,price, 0.0f);					
			
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBufferIndicator);			
			gl.glNormalPointer(GL10.GL_FLOAT, 0, normalBufferIndicator);						
			gl.glDrawArrays(GL10.GL_LINES, 0, 2);			
			gl.glPopMatrix();		
			
			//update particle system positions&sizes		
	        for(ParticleSystem particleSys: graphToParticleSystems.get(i))
			{
	        	particleSys.SetCenter(new Vector3(graphXcoord, price, progress));
	        	
	        	//scale size according to ammount of money lost	        	
	        	float deltaMoney = price-transactionAmount[i];	        	
	        	particleSys.SetSize(0.05f + Math.min(Math.abs(deltaMoney)*0.2f,0.1f));
	        	
	        	//set color
	        	float deltaMoneyScaled = Math.min(Math.abs(deltaMoney*2.0f),1.0f);
	        	Vector3 white = new Vector3(1,1,1);
	        	if (deltaMoney>=0)
	        	{
	        		//interpolate between red and white
	        		Vector3 redV = new Vector3(1,0,0);	 
	        		particleSys.SetColor(Vector3.interpolate(redV,white, deltaMoneyScaled));
	        		
	        	}
	        	else
	        	{
	        		//interpolate between green and white
	        		Vector3 greenV = new Vector3(0,1,0);	 
	        		particleSys.SetColor(Vector3.interpolate(greenV,white, deltaMoneyScaled));
	        	}
			}
	        
	        
		}
		
		//draw the particles
		//init gl state
    	gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    	gl.glEnable(GL10.GL_BLEND);    	
    	gl.glEnable(GL10.GL_ALPHA_TEST);
    	gl.glAlphaFunc(GL10.GL_GREATER, 0.1f);
    	gl.glDisable(GL10.GL_LIGHTING);
    	gl.glEnable(GL10.GL_TEXTURE_2D);
    	gl.glBindTexture(GL10.GL_TEXTURE_2D, texCoin);    	
    	
    	//transform according to graph movement
    	gl.glPushMatrix();
    	gl.glTranslatef(0, 0, -progress);   	
   	
    	//draw all particle systems
        for(ParticleSystem particleSys: particleSystems)
		{
        	particleSys.Draw(quad, gl);
		}   
        
        gl.glPopMatrix();
		
        //reset state
        gl.glDisable(GL10.GL_ALPHA_TEST);
		gl.glDisable(GL10.GL_TEXTURE_2D);
		gl.glBindTexture(GL10.GL_TEXTURE_2D, 0);		
		gl.glEnable(GL10.GL_LIGHTING);		
		gl.glDisable(GL10.GL_BLEND);
		
		
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
				playSound(4);
				transactionType[graphNum]=TransactionType.BUY;	
				transactionAmount[graphNum] = getCurrentPrice(graphNum);
				
				//add a particle system
				
		        ParticleSystem particleSys = new ParticleSystem(
		        		new Vector3(0,0,0), new Vector3(0.05f,0.05f,0.0f), /* position */
		        		0.1f, 0.05f, /* speed */
		        		0.1f, 0.05f, /* size */
		        		0.00f, 0.00f, /* sizeIncrease */
		        		3.0f, 1.5f, /* lifetime */
		        		20.0f,/* particlesPerSecond */
		        		new Vector3(1.0f,1.0f,1.0f) /* color */
		        		); 
		        
		        particleSys.SetCenter(new Vector3(CalcGraphXCoord(graphNum),transactionAmount[graphNum],progress));
		        
		        particleSystems.add(particleSys);
		        graphToParticleSystems.get(graphNum).add(particleSys);
		        
			}
			
			break;
		case SELL:
			if (transactionType[graphNum]==TransactionType.BUY)
				{
					transactionType[graphNum]=TransactionType.SELL;	
					
					if (getCurrentPrice(graphNum)>transactionAmount[graphNum])
						playSound(1);
					else
						playSound(0);
					
					transactionAmount[graphNum] = 0.0f;					
					//make associated particle system explode	
					
			        for(ParticleSystem particleSys: graphToParticleSystems.get(graphNum))
					{
			        	particleSys.StartExplosion();
					}   
					
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
		//change from pause to non-pause? make sure timestep is sane...
		if ((pause==true)&&(_pause==false))
		{		
	        Date date = new Date();
	        time = date.getTime();
	        oldTime = time;
		}
		
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
	 * Set background music volume
	 */
	public void setMusicVolume(float volume)
	{	
		class VolumeRunnable implements Runnable{
	    	float  volume;
	    	public VolumeRunnable(float  _volume)
	    	{
	    		 volume =  _volume;
	    	}
	    	@Override
	        public void run() {
	            context.setMusicVolume(volume);
	        }
	    };        
	    Runnable volumeRunnable = new VolumeRunnable(volume);
	    handler.post(volumeRunnable);
	}
	

	/*
	 * Inform the GUI of a game state change
	 * @state state The current game state
	 */
	public void ChangeGameState(int state)
	{
		class StateRunnable implements Runnable{
        	int state;
        	public StateRunnable(int _state)
        	{
        		state = _state;
        	}
        	@Override
            public void run() {
                context.changeLevelState(state);
            }
        };        
        Runnable stateRunnable = new StateRunnable(state);
        handler.post(stateRunnable);        
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
