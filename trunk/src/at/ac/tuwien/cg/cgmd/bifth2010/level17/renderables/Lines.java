package at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables;

import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.VertexBuffer;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.VertexBufferType;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Matrix4x4;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;

public class Lines implements Renderable {

	private List<Vector3> mPoints = new LinkedList<Vector3>();
	private VertexBuffer mVertices;
	private VertexBuffer mTexCoords;
	private VertexBuffer mIndices;
	private float mWidth;
	
	public Lines()
	{
		mVertices = new VertexBuffer(VertexBufferType.Position);
		mTexCoords = new VertexBuffer(VertexBufferType.TextureCoordinate);
		mIndices = new VertexBuffer(VertexBufferType.Index);
	}
	
	public void addPoint(Vector3 point)
	{
		mPoints.add(point);
	}
	
	public void update()
	{		
		Vector3[] vertices = new Vector3[(mPoints.size() - 1) * 6];
		Vector2[] texCoords = new Vector2[(mPoints.size() - 1) * 6];
		Short[] indices = new Short[(mPoints.size() - 1) * 6];
		
		Vector3 sp1 = mPoints.get(0);
		Vector3 sp2 = mPoints.get(1);
		Vector3 dir = Vector3.diff(sp2, sp1);
		dir.normalize();
		dir = Vector3.mult(dir, mWidth / 2.0f);
		Vector3 p1 = new Vector3(dir.y, -dir.x, 0f);
		Vector3 p2 = new Vector3(-dir.y, dir.x, 0f);
		p1 = Vector3.add(sp1, p1);
		p2 = Vector3.add(sp1, p2);
		
		for(int i = 1; i < mPoints.size(); i++)
		{

			sp1 = mPoints.get(i-1);
			sp2 = mPoints.get(i);
			dir = Vector3.diff(sp2, sp1);
			dir.normalize();
			dir = Vector3.mult(dir, mWidth / 2.0f);
			Vector3 p3;
			Vector3 p4;
			if(mPoints.size() - 1 > i)
			{
				Vector3 sp3 = mPoints.get(i+1);
				Vector3 dir2 = Vector3.diff(sp3, sp2);
				Vector3 angDir1 = new Vector3(dir);
				angDir1.normalize();
				angDir1.invert();
				Vector3 angDir2 = new Vector3(dir2);
				angDir2.normalize();
				float angle = (float)Math.acos(Vector3.dotProduct(angDir1, angDir2)) / 2.0f;
				float length = mWidth/((float)Math.sin(angle) * 2.0f);
				Matrix4x4 rotMat = Matrix4x4.RotateZ(-angle);
				dir2.normalize();
				dir2 = Vector3.mult(rotMat.transformPoint(dir2), length);
				p3 = dir2;
				p4 = new Vector3(-dir2.x, -dir2.y, dir2.z);
			}
			else
			{
				p3 = new Vector3(dir.y, -dir.x, 0f);
				p4 = new Vector3(-dir.y, dir.x, 0f);
			}

			p3 = Vector3.add(sp2, p3);
			p4 = Vector3.add(sp2, p4);
			
			vertices[(i - 1) * 6] = p1;
			vertices[(i - 1) * 6 + 1] = p2;
			vertices[(i - 1) * 6 + 2] = p3;
			vertices[(i - 1) * 6 + 3] = p2;
			vertices[(i - 1) * 6 + 4] = p3;
			vertices[(i - 1) * 6 + 5] = p4;

			texCoords[(i - 1) * 6] = new Vector2(0,0);
			texCoords[(i - 1) * 6 + 1] = new Vector2(1,0);
			texCoords[(i - 1) * 6 + 2] = new Vector2(0,1);
			texCoords[(i - 1) * 6 + 3] = new Vector2(1,0);
			texCoords[(i - 1) * 6 + 4] = new Vector2(0,1);
			texCoords[(i - 1) * 6 + 5] = new Vector2(1,1);
			
			for(int j = 0; j < 6; j++)
			{
				indices[(i - 1) * 6 + j] = (short)((i - 1) * 6 + j);
			}
			
			p1 = p3;
			p2 = p4;
		}
		mVertices.setData(vertices);
		mTexCoords.setData(texCoords);
		mIndices.setData(indices);
	}
	
	@Override
	public void draw(GL10 gl) {
		gl.glFrontFace(GL10.GL_CW);
		mVertices.set(gl);
		mTexCoords.set(gl);
		gl.glDrawElements(GL10.GL_TRIANGLES, (mPoints.size() - 1) * 6, GL10.GL_UNSIGNED_SHORT, mIndices.getBuffer());
	}

	public float getWidth() {
		return mWidth;
	}

	public void setWidth(float width) {
		this.mWidth = width;
	}
}
