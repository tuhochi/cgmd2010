package at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.VertexBuffer;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.VertexBufferType;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;

/**
 * A model of a house
 * @author MaMa
 *
 */
public class FFModel implements Renderable {

	private VertexBuffer mVertexBuffer;
	private VertexBuffer mTexCoordBuffer;
	private VertexBuffer mIndexBuffer;
    private int mSides = 10;
    private Vector2 mTexOffset = new Vector2();
    private int mRepeat = 1;
    private Vector2 mTexCoords[] = new Vector2[6 * mSides];
    /**
     * A Cylinder with varing dimensions
     * @param radius radius of the cylinder
     * @param height height of the cylinder
     * @param repeat how often the texture repeats on the height
     */
    public FFModel(float radius, float height, int repeat)
    {

    	mRepeat = repeat;
    	Vector3 vertices[] = new Vector3[6 * mSides];
    	Short indices[] = new Short[6* mSides];
    	
    	for(float i = 0; i < mSides; i++)
    	{
    		vertices[(int)i * 6] = new Vector3((float)Math.cos(i/mSides * Math.PI * 2.0f) * radius, -height / 2.0f, (float)Math.sin(i/mSides * Math.PI * 2.0f) * radius );
    		vertices[(int)i * 6 + 1] = new Vector3((float)Math.cos((i+1)/mSides * Math.PI * 2.0f) * radius, height / 2.0f, (float)Math.sin((i+1)/mSides * Math.PI * 2.0f) * radius );
    		vertices[(int)i * 6 + 2] = new Vector3((float)Math.cos(i/mSides * Math.PI * 2.0f) * radius, height / 2.0f, (float)Math.sin(i/mSides * Math.PI * 2.0f) * radius );

    		vertices[(int)i * 6 + 3] = new Vector3((float)Math.cos(i/mSides * Math.PI * 2.0f) * radius, -height / 2.0f, (float)Math.sin(i/mSides * Math.PI * 2.0f) * radius );
    		vertices[(int)i * 6 + 4] = new Vector3((float)Math.cos((i+1)/mSides * Math.PI * 2.0f) * radius, -height / 2.0f, (float)Math.sin((i+1)/mSides * Math.PI * 2.0f) * radius );
    		vertices[(int)i * 6 + 5] = new Vector3((float)Math.cos((i+1)/mSides * Math.PI * 2.0f) * radius, height / 2.0f, (float)Math.sin((i+1)/mSides * Math.PI * 2.0f) * radius );
    		
    	
    		mTexCoords[(int)i * 6] = new Vector2(0,0);
    		mTexCoords[(int)i * 6 + 1] = new Vector2(1,repeat);
    		mTexCoords[(int)i * 6 + 2] = new Vector2(0,repeat);
    		
    		mTexCoords[(int)i * 6 + 3] = new Vector2(0,0);
    		mTexCoords[(int)i * 6 + 4] = new Vector2(1,0);
    		mTexCoords[(int)i * 6 + 5] = new Vector2(1,repeat);
    	
    	}
    	
    	for(Short i = 0; i < mSides * 6; i++)
    		indices[i] = i;

    	mVertexBuffer = new VertexBuffer(VertexBufferType.Position);
    	mVertexBuffer.setData(vertices);  

    	mTexCoordBuffer = new VertexBuffer(VertexBufferType.TextureCoordinate);
    	mTexCoordBuffer.setData(mTexCoords);

    	mIndexBuffer = new VertexBuffer(VertexBufferType.Index);
    	mIndexBuffer.setData(indices);
    }

    /**
     * Draw the Cylinder
     */
    public void draw(GL10 gl)
    {		
    	mVertexBuffer.set(gl);
    	mTexCoordBuffer.set(gl);
    	gl.glDrawElements(GL10.GL_TRIANGLES, mSides * 6, GL10.GL_UNSIGNED_SHORT, mIndexBuffer.getBuffer());
    }

	public Vector2 getTexOffset() {
		return mTexOffset;
	}

	public void setTexOffset(Vector2 texOffset) {
		this.mTexOffset = texOffset;
		
    	for(float i = 0; i < mSides; i++)
    	{
    		mTexCoords[(int)i * 6] = Vector2.add(new Vector2(0,0) , mTexOffset);
    		mTexCoords[(int)i * 6 + 1] = Vector2.add(new Vector2(1,mRepeat) , mTexOffset);
    		mTexCoords[(int)i * 6 + 2] = Vector2.add(new Vector2(0,mRepeat) , mTexOffset);
    		
    		mTexCoords[(int)i * 6 + 3] = Vector2.add(new Vector2(0,0) , mTexOffset);
    		mTexCoords[(int)i * 6 + 4] = Vector2.add(new Vector2(1,0) , mTexOffset);
    		mTexCoords[(int)i * 6 + 5] = Vector2.add(new Vector2(1,mRepeat) , mTexOffset);
    		mTexCoordBuffer.setData(mTexCoords);
    	}
	}
    

}