package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.hardware.Camera.Size;
import android.opengl.GLES10;
import android.opengl.GLES11;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.scene.Scene;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Config;


public class MainChar implements SceneEntity {

	private float width;
	private float height;
	private Vector2 position;
	private Vector3 translation;
	
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
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
		createVertexBuffer();
		createIndexBuffer();
		createTexCoordBuffer();
//		if (vbo)
			setupVBO();
	}
	
	public MainChar(float width, float height, Vector2 position)
	{
		this.height = height;
		this.width = width;
		this.position = position;
		this.translation = new Vector3(0,0,0);
		createVertexBuffer();
		createIndexBuffer();
		createTexCoordBuffer();
//		if(vbo)
			setupVBO();
	}
	
	private void setupVBO() {
		
		int[] ids = new int[1];
		GLES11.glGenBuffers(1, ids, 0);
		vboId = ids[0];
		
		GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboId);
		int size = (vertexBuffer.capacity()+texCoordBuffer.capacity())* 4; //4*3 Vertices, 4*2 Texcoords, 4 Byte je Float
		GLES11.glBufferData(GLES11.GL_ARRAY_BUFFER, size, null, GLES11.GL_STATIC_DRAW);
		
		GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, 0, vertexBuffer.capacity()*4, vertexBuffer);
		GLES11.glBufferSubData(GLES11.GL_ARRAY_BUFFER, vertexBuffer.capacity()*4, texCoordBuffer.capacity()*4, texCoordBuffer);
		GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, 0);
		
		//GLES11.glBufferSubData(GLES11.GL_ELEMENT_ARRAY_BUFFER, (vertexBuffer.capacity() + texCoordBuffer.capacity())*4, indexBuffer.capacity()*2, indexBuffer);
	
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
	private void createVertexBuffer()
	{
		float x = position.x;
		float y = position.y;
		float[] vertices = new float[12];
		
		//origin = 0 0
		//bottom left
		vertices[0] = 0f + x;
		vertices[1] = 0f + y;
		vertices[2] = 0f;
		//bottom right
		vertices[3] = width + x;
		vertices[4] = 0f + y;
		vertices[5] = 0f;
		//top left
		vertices[6] = 0f + x;
		vertices[7]= height + y;
		vertices[8]= 0f;
		//top right
		vertices[9] = width + x;
		vertices[10] = height + y;
		vertices[11] = 0f;
				
		ByteBuffer vertexBBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		vertexBBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = vertexBBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);	
	}
	
	private void createIndexBuffer()
	{
		short[] indices = { 0, 1, 2, 3 };
		ByteBuffer indexBBuffer = ByteBuffer.allocateDirect(indices.length * 2);
		indexBBuffer.order(ByteOrder.nativeOrder());
		indexBuffer = indexBBuffer.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);				
	}
	
	private void createTexCoordBuffer()
	{
		float[] textureCoordinates = new float[8];
		//flipping the y-coordinate here
		//bottom left
		textureCoordinates[0] = 0.f;
		textureCoordinates[1] = 1.f;
		//bottom right
		textureCoordinates[2] = 1.f;
		textureCoordinates[3] = 1.f;
		//top left
		textureCoordinates[4] = 0.f;
		textureCoordinates[5] = 0.f;
		//top right
		textureCoordinates[6] = 1.f;
		textureCoordinates[7] = 0.f;
				
		ByteBuffer tcbb = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		tcbb.order(ByteOrder.nativeOrder());
		texCoordBuffer = tcbb.asFloatBuffer();
		texCoordBuffer.put(textureCoordinates);
		texCoordBuffer.position(0);
		
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
			glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, indexBuffer);
		
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
