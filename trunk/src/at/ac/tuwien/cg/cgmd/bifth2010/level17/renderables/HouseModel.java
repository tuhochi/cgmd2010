package at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.VertexBuffer;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.VertexBufferType;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;


public class HouseModel implements Renderable
{

	private VertexBuffer mVertexBuffer;
	private VertexBuffer mTexCoordBuffer;
	private VertexBuffer mIndexBuffer;
    
    /**
     * A House with varing dimensions
     * @param width width of the cube
     * @param height height of the cube
     * @param depth depth of the cube
     */
    public HouseModel(float width, float height, float depth)
    {

    	Vector3 vertices[] = {						//Vertices according to faces
    			new Vector3(-width / 2.0f, -height / 2.0f, depth / 2.0f), //v0
    			new Vector3(width / 2.0f, -height / 2.0f, depth / 2.0f), 	//v1
    			new Vector3(-width / 2.0f, height / 2.0f, depth / 2.0f), 	//v2
    			new Vector3(width / 2.0f, height / 2.0f, depth / 2.0f), 	//v3

    			new Vector3(width / 2.0f, -height / 2.0f, depth / 2.0f), 	// ...
    			new Vector3(width / 2.0f, -height / 2.0f, -depth / 2.0f), 
    			new Vector3(width / 2.0f, height / 2.0f, depth / 2.0f), 
    			new Vector3(width / 2.0f, height / 2.0f, -depth / 2.0f),
	
    			new Vector3(width / 2.0f, -height / 2.0f, -depth / 2.0f), 
    			new Vector3(-width / 2.0f, -height / 2.0f, -depth / 2.0f), 
    			new Vector3(width / 2.0f, height / 2.0f, -depth / 2.0f), 
    			new Vector3(-width / 2.0f, height / 2.0f, -depth / 2.0f),
	
    			new Vector3(-width / 2.0f, -height / 2.0f, -depth / 2.0f), 
    			new Vector3(-width / 2.0f, -height / 2.0f, depth / 2.0f), 
    			new Vector3(-width / 2.0f, height / 2.0f, -depth / 2.0f), 
    			new Vector3(-width / 2.0f, height / 2.0f, depth / 2.0f),
	
    			new Vector3(-width / 2.0f, -height / 2.0f, -depth / 2.0f), 
    			new Vector3(width / 2.0f, -height / 2.0f, -depth / 2.0f), 
    			new Vector3(-width / 2.0f, -height / 2.0f, depth / 2.0f), 
    			new Vector3(width / 2.0f, -height / 2.0f, depth / 2.0f),
	
    			new Vector3(-width / 2.0f, height / 2.0f, depth / 2.0f), 
    			new Vector3(width / 2.0f, height / 2.0f, depth / 2.0f), 
    			new Vector3(-width / 2.0f, height / 2.0f, -depth / 2.0f), 
    			new Vector3(width / 2.0f, height / 2.0f, -depth / 2.0f), 
    			/*new Vector3(-width / 2.0f, 0,-height / 2.0f),
    			new Vector3(width / 2.0f, 0,-height / 2.0f),
    			new Vector3(width / 2.0f, 0, height / 2.0f),
    			new Vector3(-width / 2.0f, 0,height / 2.0f)*/
    	};
    	Vector2 texCoords[] = {

    			new Vector2(0.0f, 0.0f), 
    			new Vector2(0.0f, 1.0f), 
    			new Vector2(1.0f, 0.0f), 
    			new Vector2(1.0f, 1.0f),
	
    			new Vector2(0.0f, 0.0f),
    			new Vector2(0.0f, 1.0f), 
    			new Vector2(1.0f, 0.0f),
    			new Vector2(1.0f, 1.0f),
	
    			new Vector2(0.0f, 0.0f), 
    			new Vector2(0.0f, 1.0f), 
    			new Vector2(1.0f, 0.0f), 
    			new Vector2(1.0f, 1.0f),
	
    			new Vector2(0.0f, 0.0f), 
    			new Vector2(0.0f, 1.0f), 
    			new Vector2(1.0f, 0.0f), 
    			new Vector2(1.0f, 1.0f),
	
    			new Vector2(0.0f, 0.0f), 
    			new Vector2(0.0f, 1.0f), 
    			new Vector2(1.0f, 0.0f), 
    			new Vector2(1.0f, 1.0f),
	
    			new Vector2(0.0f, 0.0f), 
    			new Vector2(0.0f, 1.0f), 
    			new Vector2(1.0f, 0.0f), 
    			new Vector2(1.0f, 1.0f),
    			/*new Vector2(0, 	0), 
    			new Vector2(onef, 	0), 
    			new Vector2(onef, 	onef), 
    			new Vector2(0, 	onef)*/
    	};


    	Short indices[] = {
				// Faces definition
				0, 1, 3, 0, 3, 2, 		// Face front
				4, 5, 7, 4, 7, 6, 		// Face right
				8, 9, 11, 8, 11, 10, 	// ...
				12, 13, 15, 12, 15, 14, 
				16, 17, 19, 16, 19, 18, 
				20, 21, 23, 20, 23, 22,
    	};

    	mVertexBuffer = new VertexBuffer(VertexBufferType.Position);
    	mVertexBuffer.setData(vertices);  

    	mTexCoordBuffer = new VertexBuffer(VertexBufferType.TextureCoordinate);
    	mTexCoordBuffer.setData(texCoords);

    	mIndexBuffer = new VertexBuffer(VertexBufferType.Index);
    	mIndexBuffer.setData(indices);
    }

    /**
     * Draw the House
     */
    public void draw(GL10 gl)
    {		

    	
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnable(GL10.GL_TEXTURE_2D);
    	
    	gl.glFrontFace(GL10.GL_CCW);
    	mVertexBuffer.set(gl);
    	mTexCoordBuffer.set(gl);
    	gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_SHORT, mIndexBuffer.getBuffer());

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}