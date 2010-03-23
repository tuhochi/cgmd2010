package at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector4;

public class VertexBuffer {
	private FloatBuffer mBuffer;
	private ShortBuffer mIndexBuffer;
	private int mNumElements;
	private VertexBufferType mType;
	
	public VertexBuffer(VertexBufferType type)
	{
		mType = type;
	}
	
	@SuppressWarnings("unchecked")
	public void setData(List vertices)
	{
		if(vertices.get(0).getClass() == Vector2.class)
		{
			Vector2[] arr = new Vector2[vertices.size()];
			List<Vector2> list = vertices;
			setData(list.toArray(arr));
		}
		else if(vertices.get(0).getClass() == Vector3.class)
		{
			Vector3[] arr = new Vector3[vertices.size()];
			List<Vector3> list = vertices;
			setData(list.toArray(arr));
		}
		else if(vertices.get(0).getClass() == Vector4.class)
		{
			Vector4[] arr = new Vector4[vertices.size()];
			List<Vector4> list = vertices;
			setData(list.toArray(arr));
		}
		else if(vertices.get(0).getClass() == Short.class)
		{
			Short[] arr = new Short[vertices.size()];
			List<Short> list = vertices;
			setData(list.toArray(arr));
		}
	}
	
	public void setData(Vector2[] vertices)
	{
		float[] data = new float[vertices.length * 2];
		for(int i = 0; i < vertices.length; i++)
		{
			data[i * 2] = vertices[i].x;
			data[i * 2 + 1] = vertices[i].y;
		}
		
        ByteBuffer vbb = ByteBuffer.allocateDirect(data.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mBuffer = vbb.asFloatBuffer();
        mBuffer.put(data);
        mBuffer.position(0);
        mNumElements = 2;
	}
	
	public void setData(Vector3[] vertices)
	{
		float[] data = new float[vertices.length * 3];
		for(int i = 0; i < vertices.length; i++)
		{
			data[i * 3] = vertices[i].x;
			data[i * 3 + 1] = vertices[i].y;
			data[i * 3 + 2] = vertices[i].z;
		}
		
        ByteBuffer vbb = ByteBuffer.allocateDirect(data.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mBuffer = vbb.asFloatBuffer();
        mBuffer.put(data);
        mBuffer.position(0);
        mNumElements = 3;
	}
	
	public void setData(Vector4[] vertices)
	{
		float[] data = new float[vertices.length * 4];
		for(int i = 0; i < vertices.length; i++)
		{
			data[i * 4] = vertices[i].x;
			data[i * 4 + 1] = vertices[i].y;
			data[i * 4 + 2] = vertices[i].z;
			data[i * 4 + 3] = vertices[i].w;
		}
		
        ByteBuffer vbb = ByteBuffer.allocateDirect(data.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mBuffer = vbb.asFloatBuffer();
        mBuffer.put(data);
        mBuffer.position(0);
        mNumElements = 4;
	}

	
	public void setData(Short[] vertices)
	{		
		short[] data = new short[vertices.length];
		for(int i = 0; i < vertices.length; i++)
			data[i] = vertices[i];
	
		ByteBuffer vbb = ByteBuffer.allocateDirect(data.length*2);
		vbb.order(ByteOrder.nativeOrder());
        mIndexBuffer = vbb.asShortBuffer();
        mIndexBuffer.put(data);
        mIndexBuffer.position(0);
        mNumElements = 1;
	}
	
	public void set(GL10 gl)
	{
		switch(mType)
		{
		case Position:
	        gl.glVertexPointer(mNumElements, GL10.GL_FLOAT, 0, mBuffer);
	        break;
		case TextureCoordinate:
	        gl.glTexCoordPointer(mNumElements, GL10.GL_FLOAT, 0, mBuffer);
	        break;
		case Color:
	        gl.glColorPointer(mNumElements, GL10.GL_FLOAT, 0, mBuffer);
	        break;
		case Normal:
	        gl.glNormalPointer(GL10.GL_FLOAT, 0, mBuffer);
	        break;
	    default:
	    	gl.glVertexPointer(mNumElements, GL10.GL_FLOAT, 0, mBuffer);
        break;
	        
		}
	}
	
	public Buffer getBuffer()
	{
		if(mType == VertexBufferType.Index)
			return mIndexBuffer;
		else
			return mBuffer;
	}

	public VertexBufferType getType() {
		return mType;
	}

	public void setType(VertexBufferType type) {
		this.mType = type;
	}
}
