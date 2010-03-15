package at.ac.tuwien.cg.cgmd.bifth2010.level42;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.Vector4;

public class Matrix44
{
	public float m[][];

	public Matrix44()
	{
		setIdentity();
	}
	
	public Matrix44(Matrix44 other)
	{
		m = new float[4][4];
		setFromArray(other.m);
	}
	
	private void setFromArray(float[][] other)
	{
		for(int r=0; r<4; r++)
			for(int c=0; c<4; c++)
				m[r][c] = other[r][c];
	}
	
	public void setIdentity()
	{
		m = new float[][] { {1,0,0,0}, {0,1,0,0}, {0,0,1,0}, {0,0,0,1} };
	}
	
	/*
	 * Sets this to be a scale matrix
	 * @return this
	 */
	public Matrix44 setScale(float sx, float sy, float sz)
	{
		m[0][0] = sx; m[0][1] =  0; m[0][2] =  0; m[0][3] =  0;
		m[1][0] =  0; m[1][1] = sy; m[1][2] =  0; m[1][3] =  0;
		m[2][0] =  0; m[2][1] =  0; m[2][2] = sz; m[2][3] =  0;
		m[3][0] =  0; m[3][1] =  0; m[3][2] =  0; m[3][3] =  1;

		return this;
	}
	
	/*
	 * Generates a ScaleMatrix from sx,sy,sz and sets this=ScaleMatrix*this
	 * @return this 
	 */
	public Matrix44 addScale(float sx, float sy, float sz)
	{
		Matrix44 newM = new Matrix44().setScale(sx, sy, sz).mult(this);
		setFromArray(newM.m);
		return this;
	}

	/*
	 * Sets this to be a rotateX matrix
	 * @return this
	 */
	public Matrix44 setRotateX(float alpha)
	{
		float cosa = (float) Math.cos(alpha), sina = (float) Math.sin(alpha);

		m[0][0] =     1; m[0][1] =     0; m[0][2] =     0; m[0][3] = 0;
		m[1][0] =     0; m[1][1] =  cosa; m[1][2] = -sina; m[1][3] = 0;
		m[2][0] =     0; m[2][1] =  sina; m[2][2] =  cosa; m[2][3] = 0;
		m[3][0] =     0; m[3][1] =     0; m[3][2] =     0; m[3][3] = 1;

		return this;
	}
	
	/*
	 * Generates a RotateXMatrix from alpha and sets this=RotateXMatrix*this
	 * @return this 
	 */
	public Matrix44 addRotateX(float alpha)
	{
		Matrix44 newM = new Matrix44().setRotateX(alpha).mult(this);
		setFromArray(newM.m);
		return this;
	}

	/*
	 * Sets this to be a rotateY matrix
	 * @return this
	 */
	public Matrix44 setRotateY(float alpha)
	{
		float cosa = (float) Math.cos(alpha), sina = (float) Math.sin(alpha);

		m[0][0] =  cosa; m[0][1] =     0; m[0][2] =  sina; m[0][3] = 0;
		m[1][0] =     0; m[1][1] =     1; m[1][2] =     0; m[1][3] = 0;
		m[2][0] = -sina; m[2][1] =     0; m[2][2] =  cosa; m[2][3] = 0;
		m[3][0] =     0; m[3][1] =     0; m[3][2] =     0; m[3][3] = 1;

		return this;
	}
	
	/*
	 * Generates a RotateYMatrix from alpha and sets this=RotateYMatrix*this
	 * @return this 
	 */
	public Matrix44 addRotateY(float alpha)
	{
		Matrix44 newM = new Matrix44().setRotateY(alpha).mult(this);
		setFromArray(newM.m);
		return this;
	}

	/*
	 * Sets this to be a rotateZ matrix
	 * @return this
	 */
	public Matrix44 setRotateZ(float alpha)
	{
		float cosa = (float) Math.cos(alpha), sina = (float) Math.sin(alpha);

		m[0][0] =  cosa; m[0][1] = -sina; m[0][2] =     0; m[0][3] = 0;
		m[1][0] =  sina; m[1][1] =  cosa; m[1][2] =     0; m[1][3] = 0;
		m[2][0] =     0; m[2][1] =     0; m[2][2] =     1; m[2][3] = 0;
		m[3][0] =     0; m[3][1] =     0; m[3][2] =     0; m[3][3] = 1;

		return this;
	}
	
	/*
	 * Generates a RotateZMatrix from alpha and sets this=RotateZMatrix*this
	 * @return this 
	 */
	public Matrix44 addRotateZ(float alpha)
	{
		Matrix44 newM = new Matrix44().setRotateZ(alpha).mult(this);
		setFromArray(newM.m);
		return this;
	}
	
	/*
	 * Sets this to be a translate matrix
	 * @return this
	 */
	public Matrix44 setTranslate(float tx, float ty, float tz)
	{
		m[0][0] = 1; m[0][1] = 0; m[0][2] = 0; m[0][3] = tx;
		m[1][0] = 0; m[1][1] = 1; m[1][2] = 0; m[1][3] = ty;
		m[2][0] = 0; m[2][1] = 0; m[2][2] = 1; m[2][3] = tz;
		m[3][0] = 0; m[3][1] = 0; m[3][2] = 0; m[3][3] = 1;

		return this;
	}
	
	/*
	 * Generates a TranslateMatrix from tx,ty,tz and sets this=TranslateMatrix*this
	 * @return this 
	 */
	public Matrix44 addTranslate(float tx, float ty, float tz)
	{
		Matrix44 newM = new Matrix44().setTranslate(tx,ty,tz).mult(this);
		setFromArray(newM.m);
		return this;
	}
	
	/*
	 * Sets this = this*right and returns this
	 * @return this
	 */
	public Matrix44 mult(Matrix44 right)
	{
		Matrix44 left = new Matrix44(this);
		m[0][0] = left.m[0][0]*right.m[0][0] + left.m[0][1]*right.m[1][0] + left.m[0][2]*right.m[2][0] + left.m[0][3]*right.m[3][0];
		m[0][1] = left.m[0][0]*right.m[0][1] + left.m[0][1]*right.m[1][1] + left.m[0][2]*right.m[2][1] + left.m[0][3]*right.m[3][1];
		m[0][2] = left.m[0][0]*right.m[0][2] + left.m[0][1]*right.m[1][2] + left.m[0][2]*right.m[2][2] + left.m[0][3]*right.m[3][2];
		m[0][3] = left.m[0][0]*right.m[0][3] + left.m[0][1]*right.m[1][3] + left.m[0][2]*right.m[2][3] + left.m[0][3]*right.m[3][3];
		m[1][0] = left.m[1][0]*right.m[0][0] + left.m[1][1]*right.m[1][0] + left.m[1][2]*right.m[2][0] + left.m[1][3]*right.m[3][0];
		m[1][1] = left.m[1][0]*right.m[0][1] + left.m[1][1]*right.m[1][1] + left.m[1][2]*right.m[2][1] + left.m[1][3]*right.m[3][1];
		m[1][2] = left.m[1][0]*right.m[0][2] + left.m[1][1]*right.m[1][2] + left.m[1][2]*right.m[2][2] + left.m[1][3]*right.m[3][2];
		m[1][3] = left.m[1][0]*right.m[0][3] + left.m[1][1]*right.m[1][3] + left.m[1][2]*right.m[2][3] + left.m[1][3]*right.m[3][3];
		m[2][0] = left.m[2][0]*right.m[0][0] + left.m[2][1]*right.m[1][0] + left.m[2][2]*right.m[2][0] + left.m[2][3]*right.m[3][0];
		m[2][1] = left.m[2][0]*right.m[0][1] + left.m[2][1]*right.m[1][1] + left.m[2][2]*right.m[2][1] + left.m[2][3]*right.m[3][1];
		m[2][2] = left.m[2][0]*right.m[0][2] + left.m[2][1]*right.m[1][2] + left.m[2][2]*right.m[2][2] + left.m[2][3]*right.m[3][2];
		m[2][3] = left.m[2][0]*right.m[0][3] + left.m[2][1]*right.m[1][3] + left.m[2][2]*right.m[2][3] + left.m[2][3]*right.m[3][3];
		m[3][0] = left.m[3][0]*right.m[0][0] + left.m[3][1]*right.m[1][0] + left.m[3][2]*right.m[2][0] + left.m[3][3]*right.m[3][0];
		m[3][1] = left.m[3][0]*right.m[0][1] + left.m[3][1]*right.m[1][1] + left.m[3][2]*right.m[2][1] + left.m[3][3]*right.m[3][1];
		m[3][2] = left.m[3][0]*right.m[0][2] + left.m[3][1]*right.m[1][2] + left.m[3][2]*right.m[2][2] + left.m[3][3]*right.m[3][2];
		m[3][3] = left.m[3][0]*right.m[0][3] + left.m[3][1]*right.m[1][3] + left.m[3][2]*right.m[2][3] + left.m[3][3]*right.m[3][3];
		return this;
	}
	
	/*
	 * @return this*in
	 */
	public Vector3 transformPoint(Vector3 in)
	{
		Vector3 result = new Vector3();
		result.x = m[0][0]*in.x + m[0][1]*in.y + m[0][2]*in.z;
		result.y = m[1][0]*in.x + m[1][1]*in.y + m[1][2]*in.z;
		result.z = m[2][0]*in.x + m[2][1]*in.y + m[2][2]*in.z;
		return result;
	}
	
	/*
	 * @return this*in
	 */
	public Vector4 transformPoint(Vector4 in)
	{
		Vector4 result = new Vector4();
		result.x = m[0][0]*in.x + m[0][1]*in.y + m[0][2]*in.z + m[0][3]*in.w;
		result.y = m[1][0]*in.x + m[1][1]*in.y + m[1][2]*in.z + m[1][3]*in.w;
		result.z = m[2][0]*in.x + m[2][1]*in.y + m[2][2]*in.z + m[2][3]*in.w;
		result.w = m[3][0]*in.x + m[3][1]*in.y + m[3][2]*in.z + m[3][3]*in.w;
		return result;
	}
}
