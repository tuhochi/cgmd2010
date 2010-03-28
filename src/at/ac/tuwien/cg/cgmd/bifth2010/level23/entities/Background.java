package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;

public class Background implements SceneEntity 
{

	private FloatBuffer vertexBuffer1;
	private FloatBuffer vertexBuffer2;
	private ShortBuffer indexBuffer;
	private ArrayList<Integer> textureIDs;
	private FloatBuffer texCoordBuffer;
	private float scrollSpeed = 0.05f;
	private float positionY;
	
	//hack only needed now for only 2 textures
	private boolean switchTexture = false;
	
	public Background()
	{
		textureIDs = new ArrayList<Integer>();		
		generateTiles();
		createIndexBuffer();
		createTexCoordBuffer();
	}
	
	private void generateTiles()
	{
		float[] vertices = new float[12];
		
		//origin = 0 0
		//bottom left
		vertices[0] = 0f;
		vertices[1] = 0f;
		vertices[2] = 0.f;
		//bottom right
		vertices[3] = RenderView.getInstance().getRightBounds();
		vertices[4] = 0f;
		vertices[5] = 0f;
		//top left
		vertices[6] = 0f;
		vertices[7]= RenderView.getInstance().getTopBounds();
		vertices[8]= 0f;
		//top right
		vertices[9] = RenderView.getInstance().getRightBounds();
		vertices[10] = RenderView.getInstance().getTopBounds();
		vertices[11] = 0f;
				
		ByteBuffer vertexBBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		vertexBBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer1 = vertexBBuffer.asFloatBuffer();
		vertexBuffer1.put(vertices);
		vertexBuffer1.position(0);
		
		vertices = new float[12];
		
		//origin = 0 0
		//bottom left
		vertices[0] = 0f;
		vertices[1] = RenderView.getInstance().getTopBounds();
		vertices[2] = 0f;
		//bottom right
		vertices[3] = RenderView.getInstance().getRightBounds();
		vertices[4] = RenderView.getInstance().getTopBounds();
		vertices[5] = 0f;
		//top left
		vertices[6] = 0f;
		vertices[7]= RenderView.getInstance().getTopBounds()*2;
		vertices[8]= 0f;
		//top right
		vertices[9] = RenderView.getInstance().getRightBounds();
		vertices[10] = RenderView.getInstance().getTopBounds()*2;
		vertices[11] = 0f;
				
		vertexBBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		vertexBBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer2 = vertexBBuffer.asFloatBuffer();
		vertexBuffer2.put(vertices);
		vertexBuffer2.position(0);	
		
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
	
	public void addTextureID(int texID)
	{
		textureIDs.add(texID);
	}
	
	public void update(float dt)
	{
		positionY -= dt*scrollSpeed;
		
		if(positionY <= RenderView.getInstance().getTopBounds()*-1)
		{
			positionY += RenderView.getInstance().getTopBounds(); 
			switchTexture = !switchTexture;
		}
	}
	
	@Override
	public void render() 
	{
		glPushMatrix();
		
		glTranslatef(0, positionY, 0);
		//render first BG quad
		glBindTexture(GL10.GL_TEXTURE_2D, textureIDs.get(switchTexture?1:0));
		glTexCoordPointer(2, GL10.GL_FLOAT, 0, texCoordBuffer);
		glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer1);
		glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, indexBuffer);
		//render second BG quad
		glBindTexture(GL10.GL_TEXTURE_2D, textureIDs.get(switchTexture?0:1));
		glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer2);
		glDrawElements(GL10.GL_TRIANGLE_STRIP, 4, GL_UNSIGNED_SHORT, indexBuffer);
		
		glPopMatrix();
	}

}
