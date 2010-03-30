package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;

public class Background implements SceneEntity 
{

	private FloatBuffer vertexBuffer1;
	private ShortBuffer indexBuffer;
	private int textureID;
	private FloatBuffer[] texCoordBuffer;
	private float scrollSpeed = 0.05f;
	private float positionY;
	private RenderView renderView; 
	
	//hack only needed now for only 2 textures
	private boolean switchTexture = false;
	
	public Background()
	{
		renderView = RenderView.getInstance();	
		texCoordBuffer = new FloatBuffer[2];
		generateTiles();
		createIndexBuffer();
		createTexCoordBuffer();
		
	}
	
	private void generateTiles()
	{
		
		float[] vertices = new float[12];
		float tb = renderView.getTopBounds();
		float rb = renderView.getRightBounds(); 
		//origin = 0 0
		//bottom left
		vertices[0] = 0f;
		vertices[1] = 0f;
		vertices[2] = 0f;
		//bottom right
		vertices[3] = rb;
		vertices[4] = 0f;
		vertices[5] = 0f;
		//top left
		vertices[6] = 0f;
		vertices[7]= tb;
		vertices[8]= 0f;
		//top right
		vertices[9] = rb;
		vertices[10] = tb;
		vertices[11] = 0f;
				
		ByteBuffer vertexBBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		vertexBBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer1 = vertexBBuffer.asFloatBuffer();
		vertexBuffer1.put(vertices);
		vertexBuffer1.position(0);
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
		textureCoordinates[1] = 0.5f;
		//bottom right
		textureCoordinates[2] = 1.f;
		textureCoordinates[3] = 0.5f;
		//top left
		textureCoordinates[4] = 0.f;
		textureCoordinates[5] = 0.f;
		//top right
		textureCoordinates[6] = 1.f;
		textureCoordinates[7] = 0.f;
				
		ByteBuffer tcbb = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		tcbb.order(ByteOrder.nativeOrder());
		texCoordBuffer[0] = tcbb.asFloatBuffer();
		texCoordBuffer[0].put(textureCoordinates);
		texCoordBuffer[0].position(0);
		
		//flipping the y-coordinate here
		//bottom left
		textureCoordinates[0] = 0.f;
		textureCoordinates[1] = 1.f;
		//bottom right
		textureCoordinates[2] = 1.f;
		textureCoordinates[3] = 1.f;
		//top left                   
		textureCoordinates[4] = 0.f;
		textureCoordinates[5] = 0.5f;
		//top right
		textureCoordinates[6] = 1.f;
		textureCoordinates[7] = 0.5f;
				
		ByteBuffer tcbb2 = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		tcbb2.order(ByteOrder.nativeOrder());
		texCoordBuffer[1] = tcbb2.asFloatBuffer();
		texCoordBuffer[1].put(textureCoordinates);
		texCoordBuffer[1].position(0);
	}
	
	public void setTextureID(int texID)
	{
		textureID = texID;
	}
	
	public void update(float dt)
	{
		positionY -= dt*scrollSpeed;
		
		if(positionY <= renderView.getTopBounds()*-1)
		{
			positionY += renderView.getTopBounds(); 
			switchTexture = !switchTexture;
		}
	}

	@Override
	public void render() 
	{
		glPushMatrix();
		
		glTranslatef(0, positionY, 0);
		//render first BG quad
		glBindTexture(GL10.GL_TEXTURE_2D, textureID);
		glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer[switchTexture?1:0]);
		glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer1);
		glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, indexBuffer);
		
		//render second BG quad
		glTranslatef(0,renderView.getTopBounds(),0);
		glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer[switchTexture?0:1]);
		glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, indexBuffer);
		
		glPopMatrix();
	}

}
