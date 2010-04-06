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


public class MainChar implements SceneEntity {

	
	private static final long serialVersionUID = -8661277209785862751L;
	private float width;
	private float height;
	private Vector2 position;
	private Vector3 translation;
	private GeometryManager geometryManager; 
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer texCoordBuffer;
	private int vboId; 
	private boolean vbo = true;
	public int textureID = -1;
	
	public static final int MOVE_LEFT = -1;
	public static final int MOVE_RIGHT = 1;
	public static final int NO_MOVEMENT = 0;
	//units per millisecond
	public static final float MOVE_SPEED = 0.05f;
	
	private int moveDirection = 0;
		
	public MainChar()
	{
		//create Default MainChar
		this.height = 200f;
		this.width = 100f;
		this.position = new Vector2(200,0);
		this.translation = new Vector3(0,0,0);
		preprocess();
	}
	
	public MainChar(float width, float height, Vector2 position)
	{
		this.height = height;
		this.width = width;
		this.position = position;
		this.translation = new Vector3(0,0,0);
		preprocess(); 
	}
	
	private void preprocess() {
		
		geometryManager = GeometryManager.getInstance(); 
		vertexBuffer = geometryManager.createVertexBufferQuad(width, height);
		texCoordBuffer = geometryManager.createTexCoordBufferQuad();
		vboId = geometryManager.createVBO(vertexBuffer, texCoordBuffer);
		
	}
	

	/** 
	 * @return the moveDirection
	 */
	public int getMoveDirection() {
		return moveDirection;
	}

	/**
	 * @param moveDirection the moveDirection to set
	 */
	public void setMoveDirection(int moveDirection) {
		this.moveDirection = moveDirection;
	}

	public Vector2 getPosition() {
		return this.position; 
	}
	
	public Vector3 getTranslation() {
		return this.translation; 
	}
	
	public float getWidth() {
		return this.width; 
	}
	
	public float getHeight() {
		return this.height; 
	}
	
	
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
	public void moveUpDown(float translate)
	{
		translation.y += translate;
	}
	
	public void moveBackFront(float translate)
	{
		translation.z += translate;
	}	
	
	public void resetTranslation()
	{
		translation.x = 0f;
		translation.y = 0f;
		translation.z = 0f;
	}
	
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
	
	public void update(float dt, int moveDir)
	{
		RenderView renderer = RenderView.getInstance();
		
		if(moveDir == 0 || moveDir >0 && position.x == renderer.getRightBounds() - width 
				|| moveDir<1 && position.x == 0)
			return;
		
		float step = dt * MOVE_SPEED * moveDir;
		
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
	
	@Override
	public void render() 
	{
		glPushMatrix();
		
		glTranslatef(position.x, 0, 0);
		
		if (!vbo) {
			if(textureID != -1)
			{	
				glBindTexture(GL10.GL_TEXTURE_2D, textureID);
				glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
			}		
		
			glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
		
		} else {
	
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);
			
			if (textureID != -1) {
				glBindTexture(GL10.GL_TEXTURE_2D, textureID);
				GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);
			}
			
			GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12*4); // 4 vertices with 3 coordinates, 4 bytes per float

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
			GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, 0);
		}
		
		glPopMatrix();
	}
	
	
}
