package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.Hud;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.SoundManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureAtlas;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;


/**
 * The Class MainChar handles the protagonist of the level.
 * It handles the movement, the rising and the collision detection with the walls 
 *
 * @author Markus Ernst
 * @author Florian Felberbauer
 */

public class MainChar implements SceneEntity {
	
	/** Table that contains the texture shifts for animation in *width or *height. */
	private final float[][] animationTable = {{0,0},{0,1},{0,2},{0,3},
											{1,0},{1,1},{1,2},{1,3}};
	
	/** the instance to pass around (like a Singleton) */
	public static MainChar instance;
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8661277209785862751L;
	
	/**Indicates if the MainChar was restored from Bundle **/
	private boolean wasRestored=false;
	
	/** The width. */
	private float width;
	
	/** The height. */
	private float height;
	
	/** The position. */
	private Vector2 position;
	
	/** The vertex buffer. */
	private FloatBuffer vertexBuffer;
	
	/** The texturePart for the mainChar. */
	private TexturePart texture;
	
	/** The unique vertex buffer id. */
	private int vboId; 
		
	/** The texturePart for the time boost animation. */
	private TexturePart timeBoostTexture;
	
	/** The unique vertex buffer id for time boost animation. */
	private int timeBoostVboId; 
	
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
	
	/** GeometryManager creating vertexbuffers */
	private GeometryManager geometryManager = GeometryManager.instance;
	
	/** The relativ animation time. */
	private float animationTime;
	/** The currently shown animation frame. */
	private int animationFrame;
	
	/** Indicates if the time boost animation is playing. */
	public boolean playTimeBoostAnimation = false;
	
	/** Indicates if the time goldbar animation is playing. */
	public boolean playGoldBarAnimation = false;

	/** The texture part for the goldbar animation (first frame). */
	private TexturePart goldBoostTexture;

	/** The vbo id for the goldbar animation (first frame). */
	private int goldBarVboId;
	
	/** id for the audio for the glass break in the goldbar animation. */
	private int audioIdGlassBreakSound; 
	
	/** id for the audio for the glass break in the boost animation. */
	public int audioIdBoostSound; 
	
	/**
	 * Default constructor
	 * Instantiates a new main char.
	 */
	public MainChar()
	{
		//create Default MainChar
		this.height = Math.round(Settings.MAINCHAR_HEIGHT*RenderView.instance.getAspectRatio());
		this.width = Settings.MAINCHAR_WIDTH;
		
		gameOverAngle= (float)Math.PI*3f/2f;
		gameOverScale=1;
		
		position = new Vector2();		
		position.x = Settings.MAINCHAR_STARTPOSX;
		position.y = Settings.MAINCHAR_STARTPOSY*RenderView.instance.getAspectRatio();
		
		instance = this;
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
			dos.writeBoolean(playGoldBarAnimation);
			dos.writeBoolean(playTimeBoostAnimation);
			dos.writeInt(animationFrame);
			dos.writeFloat(animationTime);
			
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
			playGoldBarAnimation = dis.readBoolean();
			playTimeBoostAnimation = dis.readBoolean();
			animationFrame = dis.readInt();
			animationTime = dis.readFloat();
			
		} catch (Exception e) {
			System.out.println("Error reading from stream in MainChar.java: "+e.getMessage());
		}
		
	}
	/**
	 * Preprocesses, before the main character starts working 
	 * creates the vertex and texture coordinate buffer and the vbo id 
	 */
	public void preprocess() 
	{		
		vertexBuffer = geometryManager.createVertexBufferQuad(width, height);

		texture = TextureAtlas.instance.getMainCharTextur();
		timeBoostTexture = TextureAtlas.instance.getBoostAnimationTextur();
		goldBoostTexture = TextureAtlas.instance.getGoldBarAnimationTextur();
		
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texture.texCoords);
			timeBoostVboId = geometryManager.createVBO(vertexBuffer, timeBoostTexture.texCoords);
			goldBarVboId = geometryManager.createVBO(vertexBuffer, goldBoostTexture.texCoords);
		}
		
		soundPlayed = false; 
		soundManager = SoundManager.instance; 
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
		if(playTimeBoostAnimation || playGoldBarAnimation)
			animationTime+=dt;
		else
		{
			animationTime=0;
			animationFrame=0;
		}
		
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
		if(playTimeBoostAnimation || playGoldBarAnimation)
		{
			if(animationTime >= 100)
			{
				animationTime -=100;
				if(animationFrame == 7)
				{
					if(playGoldBarAnimation)
					{
						Hud hud = Hud.instance;
						playGoldBarAnimation = false;
						hud.screenCrackPosX = position.x;
						hud.screenCrackPosY = position.y;
						hud.renderScreenCrack = true;
						soundManager.startPlayer(audioIdGlassBreakSound);
												
					}
					if(playTimeBoostAnimation)
					{
						if(!soundManager.isPlaying(audioIdBoostSound))
						{
							soundManager.startPlayer(audioIdBoostSound);
						}
						animationFrame--;
					}
				}
				else				
					animationFrame++;
			}
			if(playTimeBoostAnimation || playGoldBarAnimation)
			{
				glMatrixMode(GL_TEXTURE);
				glPushMatrix();
				glTranslatef(texture.dimension.x*animationTable[animationFrame][0], -texture.dimension.y*animationTable[animationFrame][1], 0);	
				glMatrixMode(GL_MODELVIEW);
			}
		}	
			
		glPushMatrix();
		
		glTranslatef(position.x, position.y, 0);
		
		if(!playTimeBoostAnimation)
		{
			if(!playGoldBarAnimation)
			{
				if(!Settings.GLES11Supported) 
				{			
					glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.texCoords);
					glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
					glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
					
				} 
				else 
				{
					geometryManager.bindVBO(vboId);
					glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
				}
			}
			else
			{
				if(!Settings.GLES11Supported) 
				{			
					glTexCoordPointer(2, GL10.GL_FLOAT, 0, goldBoostTexture.texCoords);
					glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
					glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				
				} 
				else 
				{
					geometryManager.bindVBO(goldBarVboId);
					glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
				}
				

				glMatrixMode(GL_TEXTURE);
				glPopMatrix();
				glMatrixMode(GL_MODELVIEW);
			}
		}
		else
		{
			if(!Settings.GLES11Supported) 
			{			
				glTexCoordPointer(2, GL10.GL_FLOAT, 0, timeBoostTexture.texCoords);
				glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
				glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
			} 
			else 
			{
				geometryManager.bindVBO(timeBoostVboId);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
			}
			

			glMatrixMode(GL_TEXTURE);
			glPopMatrix();
			glMatrixMode(GL_MODELVIEW);
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
						
			if(!Settings.GLES11Supported) 
			{
				glTexCoordPointer(2, GL10.GL_FLOAT, 0, texture.texCoords);
				glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
				glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
			} 
			else 
			{
				geometryManager.bindVBO(vboId);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
			}
			
			glPopMatrix();
		}
		else
		{
			soundManager.pausePlayer(audioIdGameOverSound);
			LevelActivity.instance.setScore(RenderView.instance.score);
		}
	}
	
	/**
	 * Resets the MainChar
	 */
	public void reset()
	{
		audioIdGameOverSound = soundManager.requestPlayer(R.raw.l23_crashsound, false);
		audioIdGlassBreakSound = soundManager.requestPlayer(R.raw.l23_glassbreak, false);
		if(!wasRestored)
		{
			position.x = Settings.MAINCHAR_STARTPOSX;
			position.y = Settings.MAINCHAR_STARTPOSY;
		}
		else
			wasRestored = false;
	}
		
}
