package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.GL_FLOAT;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glBindTexture;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;


/**
 * The Class MainChar handles the protagonist of the level.
 * It handles the movement, the rising and the collision detection with the walls 
 */
public class MainChar implements SceneEntity {

	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -8661277209785862751L;
	
	/** The width. */
	private float width;
	
	/** The height. */
	private float height;
	
	/** The position. */
	private Vector2 position;
	
	/** The translation. */
	private Vector3 translation;
	
	/** The geometry manager. */
	private GeometryManager geometryManager; 
	
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
		
	/**
	 * Default constructor
	 * Instantiates a new main char.
	 */
	public MainChar()
	{
		//create Default MainChar
		this.height = 200f;
		this.width = 100f;
		this.position = new Vector2(200,0);
		this.translation = new Vector3(0,0,0);
		preprocess();
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
		this.position = position;
		this.translation = new Vector3(0,0,0);
		preprocess(); 
	}
	
	/**
	 * Preprocesses, before the main character starts working 
	 * creates the vertex and texture coordinate buffer and the vbo id 
	 */
	private void preprocess() {
		
		geometryManager = GeometryManager.getInstance(); 
		vertexBuffer = geometryManager.createVertexBufferQuad(width, height);
		texCoordBuffer = geometryManager.createTexCoordBufferQuad();
		if(Settings.GLES11Supported) 
		{
			vboId = geometryManager.createVBO(vertexBuffer, texCoordBuffer);
		}
		
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
	 * Gets the translation.
	 *
	 * @return the translation
	 */
	public Vector3 getTranslation() {
		return this.translation; 
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
	
//	public void moveLeftRight(float translate)
//	{
//		translation.x += translate;
//		position.x += translate; 
//	}
//	
/**
 * Handles the movement up and down
 *
 * @param translate the vertical translation
 */
public void moveUpDown(float translate)
	{
		translation.y += translate;
	}
	
	/**
	 * Moves back and forward.
	 *
	 * @param translate the translation in z-direction
	 */
	public void moveBackFront(float translate)
	{
		translation.z += translate;
	}	
	
	/**
	 * Resets the translation.
	 */
	public void resetTranslation()
	{
		translation.x = 0f;
		translation.y = 0f;
		translation.z = 0f;
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
	    	if (position.x + width + stepWidth <= RenderView.getInstance().getRightBounds()) //application width instead of fixed value
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
		RenderView renderer = RenderView.getInstance();
		
		if(moveDir == 0 || moveDir >0 && position.x == renderer.getRightBounds() - width 
				|| moveDir<1 && position.x == 0)
			return;
		
		float step = dt * Settings.MOVE_SPEED * moveDir;
		
		if(!isInboundsAfterStep(moveDir,step))
		{
			if(moveDir > 0 && position.x != renderer.getRightBounds() - width)
				step = renderer.getRightBounds() - width - position.x;
			if(moveDir < 0 && position.x != 0.0f)
				step = -position.x;
		}
		
		translation.x = step;
		position.x += translation.x;
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
		
		if(!Settings.GLES11Supported) 
		{
			
			glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
		
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		} 
		else 
		{
	
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);
			
			glBindTexture(GL10.GL_TEXTURE_2D, textureID);
			GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);
			
			GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12*4); // 4 vertices with 3 coordinates, 4 bytes per float

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, 0);
		}
		
		glPopMatrix();
	}
	
	
}
