package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class OBJRenderable {

    private VertexBuffer mVertexBuffer;
    private VertexBuffer mTexCoordBuffer;
    private VertexBuffer mNormalsBuffer;
    private VertexBuffer mIndexBuffer;
    private int mNumVertices;
    
    public OBJRenderable(OBJModel model)
    {    	
        mVertexBuffer = new VertexBuffer(VertexBufferType.Position);
        mVertexBuffer.setData(model.getVertices());
        mTexCoordBuffer = new VertexBuffer(VertexBufferType.TextureCoordinate);
        mTexCoordBuffer.setData(model.getTextureCoords());
        mNormalsBuffer = new VertexBuffer(VertexBufferType.Normal);
        mNormalsBuffer.setData(model.getNormals());
        mIndexBuffer = new VertexBuffer(VertexBufferType.Index);
        mIndexBuffer.setData(model.getIndices());
        mNumVertices = model.getNumVertices();
    }
    
	public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CCW);
        mVertexBuffer.set(gl);
        mNormalsBuffer.set(gl);
        mTexCoordBuffer.set(gl);
        gl.glDrawElements(GL10.GL_TRIANGLES, mNumVertices, GL10.GL_UNSIGNED_SHORT, mIndexBuffer.getBuffer());
	}
	
	public static OBJRenderable loadFromSerialized(String file, Context context)
	{		
		return new OBJRenderable(OBJModel.read(file, context));
	}
	
	public static OBJRenderable loadFromOBJ(String file, Context context)
	{
		return new OBJRenderable(OBJModel.load(file, context));
	}
	
	
}
