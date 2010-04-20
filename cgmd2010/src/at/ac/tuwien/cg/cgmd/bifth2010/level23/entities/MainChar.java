package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;
import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.Vector2;


public class MainChar implements SceneEntity {

	private float width;
	private float height;
	private Vector2 position;
	private FloatBuffer vertexBuffer;
	private ShortBuffer indexBuffer;
	
	public MainChar()
	{
		//create Default MainChar
		this.height = 200f;
		this.width = 100f;
		this.position = new Vector2(200,0);
		createVertexBuffer();
	}
	
	public MainChar(float width, float height, Vector2 position)
	{
		this.height = height;
		this.width = width;
		this.position = position;
		createVertexBuffer();
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
		//top right
		vertices[6] = width + x;
		vertices[7] = height + y;
		vertices[8] = 0f;
		//top left
		vertices[9] = 0f + x;
		vertices[10]= height + y;
		vertices[11]= 0f;
		
		ByteBuffer vertexBBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		vertexBBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = vertexBBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);	
		
		short[] indices = { 0, 1, 2, 0, 2, 3 };
		ByteBuffer indexBBuffer = ByteBuffer.allocateDirect(indices.length * 2);
		indexBBuffer.order(ByteOrder.nativeOrder());
		indexBuffer = indexBBuffer.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}
	
	@Override
	public void render() 
	{
		glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		glDrawElements(GL10.GL_TRIANGLES, 6, GL_UNSIGNED_SHORT, indexBuffer);
	}

}
