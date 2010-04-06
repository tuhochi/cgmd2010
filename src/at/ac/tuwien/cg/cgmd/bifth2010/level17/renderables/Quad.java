package at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.VertexBuffer;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.VertexBufferType;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;


public class Quad implements Renderable {

	private VertexBuffer mVertexBuffer;
	private VertexBuffer mTexCoordBuffer;
	private VertexBuffer mIndexBuffer;
	
    public Quad(float width, float height)
    {
    	float onef = 1.0f;

    	Vector3 vertices[] = {
    			new Vector3( -width / 2.0f, 0, -height / 2.0f),
    			new Vector3(width / 2.0f, 0, -height / 2.0f),
    			new Vector3(width / 2.0f, 0, height / 2.0f),
    			new Vector3(-width / 2.0f, 0, height / 2.0f)
    	};
    	Vector2 texCoords[] = {

    			new Vector2(0, 	0), 
    			new Vector2(onef, 	0), 
    			new Vector2(onef, 	onef), 
    			new Vector2(0, 	onef)
    	};


    	Short indices[] = {
    			3, 1, 0,    3, 2, 1
    	};

    	mVertexBuffer = new VertexBuffer(VertexBufferType.Position);
    	mVertexBuffer.setData(vertices);  

    	mTexCoordBuffer = new VertexBuffer(VertexBufferType.TextureCoordinate);
    	mTexCoordBuffer.setData(texCoords);

    	mIndexBuffer = new VertexBuffer(VertexBufferType.Index);
    	mIndexBuffer.setData(indices);
    }

    public void draw(GL10 gl)
    {
    	

    	
    	mVertexBuffer.set(gl);
    	mTexCoordBuffer.set(gl);
    	gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer.getBuffer());


    }
}
