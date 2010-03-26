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
    			new Vector3( -width / 2.0f, -height / 2.0f, 0),
    			new Vector3(width / 2.0f, -height / 2.0f, 0),
    			new Vector3(width / 2.0f,  height / 2.0f, 0),
    			new Vector3(-width / 2.0f,  height / 2.0f, 0)
    	};
    	Vector2 texCoords[] = {

    			new Vector2(0, 	0), 
    			new Vector2(onef, 	0), 
    			new Vector2(onef, 	onef), 
    			new Vector2(0, 	onef)
    	};


    	Short indices[] = {
    			3, 0, 1,    3, 1, 2
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
    	gl.glFrontFace(GL10.GL_CCW);
    	mVertexBuffer.set(gl);
    	mTexCoordBuffer.set(gl);
    	gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer.getBuffer());
    }
}
