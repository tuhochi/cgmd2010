package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

public class Matrix44
{
	public float m[][] = new float[4][4];
	private float m16[] = new float[16];
	private Matrix44 temp;

	public Matrix44()
	{
		setIdentity();
	}
	
	public Matrix44(Matrix44 other)
	{
		setFromArray(other.m);
	}
	
	public Matrix44(float[] m16)
	{
		int i = 0;
		for(int c=0; c<4; c++)
			for(int r=0; r<4; r++)
				m[r][c] = m16[i++];
	}
	
	public float[] getArray16()
	{
		int i = 0;
		for(int c=0; c<4; c++)
			for(int r=0; r<4; r++)
				m16[i++] = m[r][c];
		return m16;
	}
	
	private void setFromArray(float[][] other)
	{
		for(int r=0; r<4; r++)
			for(int c=0; c<4; c++)
				m[r][c] = other[r][c];
	}
	
	public void setIdentity()
	{
		for(int r=0; r<4; r++)
			for(int c=0; c<4; c++)
				m[r][c] = (r==c) ? 1 : 0;
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
	 * @return a scale matrix
	 */
	public static Matrix44 getScale(float sx, float sy, float sz)
	{
		return new Matrix44().setScale(sx, sy, sz);
	}
	
	/*
	 * Generates a ScaleMatrix from sx,sy,sz and sets this=ScaleMatrix*this
	 * @return this 
	 */
	public Matrix44 addScale(float sx, float sy, float sz)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setScale(sx, sy, sz).mult(this);
		setFromArray(temp.m);
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
	 * @return a rotateX matrix
	 */
	public static Matrix44 getRotateX(float alpha)
	{
		return new Matrix44().setRotateX(alpha);
	}
	
	/*
	 * Generates a RotateXMatrix from alpha and sets this=RotateXMatrix*this
	 * @return this 
	 */
	public Matrix44 addRotateX(float alpha)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setRotateX(alpha).mult(this);
		setFromArray(temp.m);
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
	 * @return a rotateY matrix
	 */
	public static Matrix44 getRotateY(float alpha)
	{
		return new Matrix44().setRotateY(alpha);
	}
	
	/*
	 * Generates a RotateYMatrix from alpha and sets this=RotateYMatrix*this
	 * @return this 
	 */
	public Matrix44 addRotateY(float alpha)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setRotateY(alpha).mult(this);
		setFromArray(temp.m);
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
	 * @return a rotateZ matrix
	 */
	public static Matrix44 getRotateZ(float alpha)
	{
		return new Matrix44().setRotateZ(alpha);
	}
	
	/*
	 * Generates a RotateZMatrix from alpha and sets this=RotateZMatrix*this
	 * @return this 
	 */
	public Matrix44 addRotateZ(float alpha)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setRotateZ(alpha).mult(this);
		setFromArray(temp.m);
		return this;
	}
	
	/*
	 * Sets this to a RotateMatrix around axis with angle alpha
	 * @return this;
	 */
	public Matrix44 setRotate(Vector3 axis, float alpha)
	{
		axis.normalize();
		float cosa = (float) Math.cos(alpha), sina = (float) Math.sin(alpha);
		m[0][0] =  axis.x * axis.x + (1 - axis.x * axis.x) * cosa;	m[0][1] = axis.x * axis.y * (1 - cosa) - axis.z * sina; 	m[0][2] = axis.x * axis.z * (1 - cosa) + axis.y * sina;		m[0][3] = 0;
		m[1][0] =  axis.x * axis.y * (1 - cosa) + axis.z * sina;	m[1][1] = axis.y * axis.y + (1 - axis.y * axis.y) * cosa;	m[1][2] = axis.y * axis.z * (1 - cosa) - axis.x * sina;		m[1][3] = 0;
		m[2][0] =  axis.x * axis.z * (1 - cosa) - axis.y * sina;	m[2][1] = axis.y * axis.z * (1 - cosa) + axis.x * sina;		m[2][2] = axis.z * axis.z + (1 - axis.z * axis.z) * cosa;	m[2][3] = 0;
		m[3][0] =     0; 											m[3][1] =     0; 											m[3][2] =     0;											m[3][3] = 1;
		return this;
	}
	
	/*
	 * @return a RotateMatrix around axis with angle alpha
	 */
	public static Matrix44 getRotate(Vector3 axis, float alpha)
	{
		return new Matrix44().setRotate(axis, alpha);
	}
	
	/*
	 * Generates a RotateMatrix around axis with angle alpha and sets this=RotateMatrix*this
	 * @return this;
	 */
	public Matrix44 addRotate(Vector3 axis, float alpha)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setRotate(axis, alpha).mult(this);
		setFromArray(temp.m);
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
	 * @return a translate matrix
	 */
	public static Matrix44 getTranslate(float tx, float ty, float tz)
	{
		return new Matrix44().setTranslate(tx, ty, tz);
	}
	
	/*
	 * Generates a TranslateMatrix from tx,ty,tz and sets this=TranslateMatrix*this
	 * @return this 
	 */
	public Matrix44 addTranslate(float tx, float ty, float tz)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setTranslate(tx, ty, tz).mult(this);
		setFromArray(temp.m);
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
	 * @return left*right
	 */
	public static Matrix44 mult(Matrix44 left, Matrix44 right)
	{
		return new Matrix44(left).mult(right);
	}
	
	/*
	 * @return this*in
	 */
	public void transformPoint(Vector3 in)
	{
		float x = m[0][0]*in.x + m[0][1]*in.y + m[0][2]*in.z;
		float y = m[1][0]*in.x + m[1][1]*in.y + m[1][2]*in.z;
		float z = m[2][0]*in.x + m[2][1]*in.y + m[2][2]*in.z;
		in.x = x;
		in.y = y;
		in.z = z;
	}
	
	/*
	 * @return this*in
	 */
	public void transformPoint(Vector4 in)
	{
		float x = m[0][0]*in.x + m[0][1]*in.y + m[0][2]*in.z + m[0][3]*in.w;
		float y = m[1][0]*in.x + m[1][1]*in.y + m[1][2]*in.z + m[1][3]*in.w;
		float z = m[2][0]*in.x + m[2][1]*in.y + m[2][2]*in.z + m[2][3]*in.w;
		float w = m[3][0]*in.x + m[3][1]*in.y + m[3][2]*in.z + m[3][3]*in.w;
		in.x = x;
		in.y = y;
		in.z = z;
		in.w = w;
	}
}
