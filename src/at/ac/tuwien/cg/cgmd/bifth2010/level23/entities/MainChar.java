package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.SoundManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;


/**
 * The Class MainChar handles the protagonist of the level.
 * It handles the movement, the rising and the collision detection with the walls 
 */
public class MainChar implements SceneEntity {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8661277209785862751L;
	
	private boolean wasRestored=false;
	
	/** The width. */
	private float width;
	
	/** The height. */
	private float height;
	
	/** The position. */
	private Vector2 position;
	
	/** The vertex buffer. */
	private FloatBuffer vertexBuffer;
	
	/** The texture coordinate buffer. */
	private FloatBuffer texCoordBuffer;
	
	/** The unique vertex buffer id. */
	private int vboId; 
	
	/** The texture id. */
	public int textureID = -1;
	
	/** The Constant MOVE_LEFT. */
	public static final int MOVE_LEFT = -1;
	
	/** The Constant MOVE_RIGHT. */
	public static final int MOVE_RIGHT = 1;
	
	/** The Constant NO_MOVEMENT. */
	public static final int NO_MOVEMENT = 0;
	
	/** The move direction. */
	private int moveDirection = 0;
		
	/** Boolean if main char is dead */
	private boolean gameOver = false; 
	 	
	/** x-coordinate for gameover animation */
	private float xCoord; 
	
	/** y-coordinate for gameover animation */
	private float yCoord; 
	
	/** circle angle (in radiant) for gameover animation */
	private float gameOverAngle;
	
	/** scale for gameover animation */
	private float gameOverScale;
	
	/** id for the audio for the game over sound */
	private int audioIdGameOverSound; 
	
	/** boolean indicating whether the sound has been already played or not*/
	private boolean soundPlayed; 
	
	/** SoundManager for handling audio */
	private SoundManager soundManager; 
	
	/**
	 * Default constructor
	 * Instantiates a new main char.
	 */
	public MainChar()
	{
		//create Default MainChar
		this.height = Settings.MAINCHAR_HEIGHT*RenderView.instance.getAspectRatio();
		this.width = Settings.MAINCHAR_WIDTH;
		
		gameOverAngle= (float)Math.PI*3f/2f;
		gameOverScale=1;
		
		position = new Vector2();		
		position.x = Settings.MAINCHAR_STARTPOSX;
		position.y = Settings.MAINCHAR_STARTPOSY;	
	}
	
	/**
	 * Instantiates a new main char.
	 *
	 * @param width the width of the main character
	 * @param height the height of the main character
	 * @param position the position of the main character
	 */
	public MainChar(float width, float height, Vector2 position)
	{
		this.height = height;
		this.width = width;
		
		gameOverAngle= (float)Math.PI*3f/2f;
		gameOverScale=1;
		
		this.position = position;
		
		preprocess(); 
		
	}
	
	/**
	 * Writing to stream 
	 * @param dos Stream to write to
	 */
	public void writeToStream(DataOutputStream dos) {
		try {
			dos.writeFloat(position.x); 
			dos.writeFloat(position.y);
			dos.writeBoolean(gameOver);
			if(gameOver)
			{
				dos.writeFloat(gameOverAngle);
				dos.writeFloat(gameOverScale);
			}
			
		} catch (Exception e) {
			System.out.println("Error writing to stream in MainChar.java: "+e.getMessage());
		}
		
	}
	
	/**
	 * Reading from stream
	 * @param dis Stream to read from
	 */
	public void readFromStream(DataInputStream dis) {
		
		try {
			position.x = dis.readFloat(); 
			position.y = dis.readFloat(); 
			gameOver = dis.readBoolean();
			if(gameOver)
			{
				gameOverAngle = dis.readFloat();
				gameOverScale = dis.readFloat();
			}
			wasRestored=true;
			
		} catch (Exception e) {
			System.out.println("Error reading from stream in MainChar.java: "+e.getMessage());
		}
		
	}
	/**
	 * Preprocesses, before the main character starts working 
	 * creates the vertex and texture coordinate buffer and the vbo id 
	 */
	public void preprocess() {
		
		GeometryManager geometryManager = GeometryManager.instance; 
		vertexBuffer = geometryManager.createVertexBufferQuad(width, height);
		texCoordBuffer = geometryManager.createTexCoordBufferQuad();
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texCoordBuffer);
		}
		
		soundPlayed = false; 
		soundManager = SoundManager.instance; 
		audioIdGameOverSound = soundManager.requestPlayer(R.raw.l23_crashsound, false);
		
			
	}
	

	/**
	 * Gets the move direction.
	 *
	 * @return the moveDirection
	 */
	public int getMoveDirection() {
		return moveDirection;
	}

	/**
	 * Sets the move direction.
	 *
	 * @param moveDirection the moveDirection to set
	 */
	public void setMoveDirection(int moveDirection) {
		this.moveDirection = moveDirection;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public Vector2 getPosition() {
		return this.position; 
	}
	
	/**
	 * Sets the position
	 * @param position the position to set
	 */
	public void setPosition(Vector2 position) {
		this.position = position; 
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public float getWidth() {
		return this.width; 
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public float getHeight() {
		return this.height; 
	}
	
	
	/**
	 * Sets the texture id.
	 *
	 * @param texID the new texture id
	 */
	public void setTextureID(int texID)
	{
		this.textureID = texID;
	}

	/**
	 * Checks if main character is inbounds after step.
	 *
	 * @param moveDir the move dir (static final int) 
	 * @param stepWidth the step width
	 * @return true, if is inbounds after step
	 */
	public boolean isInboundsAfterStep(int moveDir, float stepWidth) {
		if (moveDir > 0)
		{
	    	if (position.x + width + stepWidth <= RenderView.instance.getRightBounds()) //application width instead of fixed value
	    	{ 
	    		return true; 
	    	}
		}
		else 
		{
			if (position.x + stepWidth >= 0.0f)
				return true; 
		}
		
		return false; 
	}	
	
	/**
	 * Updates the horizontal position of the main character
	 *
	 * @param dt time unit 
	 * @param moveDir the movement direction (static final int) 
	 */
	public void update(float dt, int moveDir)
	{
		RenderView renderer = RenderView.instance;
		float rightBounds = renderer.getRightBounds();
		
		if(moveDir == 0 || moveDir >0 && position.x == rightBounds - width 
				|| moveDir<1 && position.x == 0)
			return;
		
		float step = dt * Settings.MOVE_SPEED * moveDir;
		
		if(!isInboundsAfterStep(moveDir,step))
		{
			if(moveDir > 0 && position.x != rightBounds - width)
				step = rightBounds - width - position.x;
			if(moveDir < 0 && position.x != 0.0f)
				step = -position.x;
		}
		
		position.x += step;
	}
	
	
	/**
	 * Indicates if the level is game over
	 * @return true if the level is game over, false otherwise
	 */
	public boolean isGameOver() {
		return gameOver; 
	}
	
	/**
	 * Sets the state of the level to game over or not
	 * @param isGameOver the boolean indicating if the level is game over
	 */
	public void setGameOver(boolean isGameOver) {
		this.gameOver = isGameOver; 
		/*	if (gameOver)
				if (RenderView.getInstance().isUseSensor())
					RenderView.getInstance().switchSensor();*/
		
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.SceneEntity#render()
	 */
	/**
	 * Renders the main character
	 */
	@Override
	public void render() 
	{
		glPushMatrix();
		
		glTranslatef(position.x, 0, 0);
		
		glBindTexture(GL10.GL_TEXTURE_2D, textureID);
		
		if(!Settings.GLES11Supported) 
		{			
			glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
		
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		} 
		else 
		{
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);
			
			GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);
			
			GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12*4); // 4 vertices with 3 coordinates, 4 bytes per float

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
		}
		
		glPopMatrix();
	}
	
	/**
	 * Renders the gameover animation
	 */
	public void renderGameOver(float dt) {
		
		if(!soundPlayed) {
			soundManager.startPlayer(audioIdGameOverSound);
			soundPlayed = true; 
		}
		
		if (soundManager.isPlaying(audioIdGameOverSound))
			soundManager.setVolumeForPlayer(audioIdGameOverSound, gameOverScale);
		
		gameOverAngle += dt*0.005;
		
		gameOverScale -= dt*0.0003;
		
		//animation over
		if(gameOverScale > 0)
		{
			float radius = 25f;
		
			glPushMatrix();
	
			xCoord = position.x  + radius*(float)Math.cos(gameOverAngle); 
			yCoord = radius + radius*(float)Math.sin(gameOverAngle);
			
			glTranslatef(xCoord, yCoord, 0);
			glScalef(gameOverScale, gameOverScale, 1.0f);
			
			glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			
			if(!Settings.GLES11Supported) 
			{
				glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
				glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
				glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
			} 
			else 
			{
				GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);
				
				GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);
				
				GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12*4); // 4 vertices with 3 coordinates, 4 bytes per float
	
				glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
			}
			
			glPopMatrix();
		}
		else
		{
			soundManager.pausePlayer(audioIdGameOverSound);
			LevelActivity.instance.setScore((int)(RenderView.instance.balloonHeight*Settings.SCOREHEIGHT_MODIFIER));
		}
	}
	
	public void reset()
	{
		if(!wasRestored)
		{
			position.x = Settings.MAINCHAR_STARTPOSX;
			position.y = Settings.MAINCHAR_STARTPOSY;
		}
		else
			wasRestored = false;
	}
		
}
