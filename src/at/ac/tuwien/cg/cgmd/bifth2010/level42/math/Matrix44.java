package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable;

/**
 * The Class Matrix44.
 *
 * @author Alex Druml
 * @author Lukas Roessler
 */
public class Matrix44 implements Persistable
{
	/**
	 * [row][col]
	 */
	public final float m[][] = new float[4][4];
	/**
	 * column major
	 */
	private final float m16[] = new float[16];
	private Matrix44 temp;
	private Vector3 tempV = new Vector3();

	/**
	 * Instantiates a new matrix44 as identity
	 */
	public Matrix44()
	{
		setIdentity();
	}
	
	/**
	 * copy constructor
	 *
	 * @param other the other
	 */
	public Matrix44(Matrix44 other)
	{
		set(other.m);
	}
	
	/**
	 * Instantiates a new matrix44.
	 *
	 * @param m16 the m16 (column major)
	 */
	public Matrix44(float[] m16)
	{
		set(m16);
	}
	
	/**
	 * Instantiates a new matrix44.
	 *
	 * @param m00 the m00
	 * @param m10 the m10
	 * @param m20 the m20
	 * @param m30 the m30
	 * @param m01 the m01
	 * @param m11 the m11
	 * @param m21 the m21
	 * @param m31 the m31
	 * @param m02 the m02
	 * @param m12 the m12
	 * @param m22 the m22
	 * @param m32 the m32
	 * @param m03 the m03
	 * @param m13 the m13
	 * @param m23 the m23
	 * @param m33 the m33
	 */
	public Matrix44(
			float m00, float m10, float m20, float m30, 
			float m01, float m11, float m21, float m31, 
			float m02, float m12, float m22, float m32, 
			float m03, float m13, float m23, float m33)
	{
		set(m00, m10, m20, m30, m01, m11, m21, m31, m02, m12, m22, m32, m03, m13, m23, m33);
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#persist(java.io.DataOutputStream)
	 */
	public void persist(DataOutputStream dos) throws IOException
	{
		for(int c=0; c<4; c++)
			for(int r=0; r<4; r++)
				dos.writeFloat(m[r][c]);
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#restore(java.io.DataInputStream)
	 */
	public void restore(DataInputStream dis) throws IOException
	{
		for(int c=0; c<4; c++)
			for(int r=0; r<4; r++)
				m[r][c] = dis.readFloat();
	}
	
	/**
	 * Gets the array16.
	 *
	 * @return this as a column major float[16]
	 */
	public float[] getArray16()
	{
		int i = 0;
		for(int c=0; c<4; c++)
			for(int r=0; r<4; r++)
				m16[i++] = m[r][c];
		return m16;
	}
	
	/**
	 * Sets this from a [row][col] array
	 *
	 * @param other the array
	 */
	public void set(float[][] other)
	{
		for(int r=0; r<4; r++)
			for(int c=0; c<4; c++)
				m[r][c] = other[r][c];
	}
	
	/**
	 * Sets this from a column major array
	 *
	 * @param m16 the array
	 */
	public void set(float[] m16)
	{
		int i = 0;
		for(int c=0; c<4; c++)
			for(int r=0; r<4; r++)
				m[r][c] = m16[i++];
	}
	
	/**
	 * Sets this from 16 floats
	 *
	 * @param m00 the m00
	 * @param m10 the m10
	 * @param m20 the m20
	 * @param m30 the m30
	 * @param m01 the m01
	 * @param m11 the m11
	 * @param m21 the m21
	 * @param m31 the m31
	 * @param m02 the m02
	 * @param m12 the m12
	 * @param m22 the m22
	 * @param m32 the m32
	 * @param m03 the m03
	 * @param m13 the m13
	 * @param m23 the m23
	 * @param m33 the m33
	 */
	public void set(
			float m00, float m10, float m20, float m30, 
			float m01, float m11, float m21, float m31, 
			float m02, float m12, float m22, float m32, 
			float m03, float m13, float m23, float m33)
	{
		m[0][0] = m00;
		m[1][0] = m10;
		m[2][0] = m20;
		m[3][0] = m30;
		m[0][1] = m01;
		m[1][1] = m11;
		m[2][1] = m21;
		m[3][1] = m31;
		m[0][2] = m02;
		m[1][2] = m12;
		m[2][2] = m22;
		m[3][2] = m32;
		m[0][3] = m03;
		m[1][3] = m13;
		m[2][3] = m23;
		m[3][3] = m33;
	}
	
	/**
	 * Sets this to an identity.
	 */
	public void setIdentity()
	{
		for(int r=0; r<4; r++)
			for(int c=0; c<4; c++)
				m[r][c] = (r==c) ? 1 : 0;
	}
	
	/**
	 * Copy method
	 *
	 * @param other the other
	 */
	public void copy(Matrix44 other)
	{
		set(other.m);
	}

	/**
	 * Returns a new rotation matrix corresponding to a quaternion
	 * specified by x,y,z,w
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param w the w
	 * @return the matrix44
	 */
	public static Matrix44 fromQuaternion(float x, float y, float z, float w)
	{
		return new Matrix44().setFromQuaternion(x, y, z, w);
	}
	
	/**
	 * Sets this to be a rotation matrix corresponding to a quaternion
	 * specified by x,y,z,w
	 *
	 * @param x the x
	 * @param y the y
	 * @param z the z
	 * @param w the w
	 * @return the matrix44
	 */
	public Matrix44 setFromQuaternion(float x, float y, float z, float w)
	{
	    m[0][0] = 1.0f - 2.0f * ( y * y + z * z );
	    m[0][1] = 2.0f * ( x * y - w * z );	
	    m[0][2] = 2.0f * ( x * z + w * y );
	    m[0][3] = 0.0f;

	    m[1][0] = 2.0f * ( x * y + w * z );
	    m[1][1] = 1.0f - 2.0f * ( x * x + z * z );
	    m[1][2] = 2.0f * ( y * z - w * x );
	    m[1][3] = 0.0f;
	    
	    m[2][0] = 2.0f * ( x * z - w * y );
	    m[2][1] = 2.0f * ( y * z + w * x );
	    m[2][2] = 1.0f - 2.0f * ( x * x + y * y );
	    m[2][3] = 0.0f;

	    m[3][0] = 0.0f;
	    m[3][1] = 0.0f;
	    m[3][2] = 0.0f;
	    m[3][3] = 1.0f;

	    return this;
	}

	/**
	 * Sets this to be a scale matrix
	 *
	 * @param sx the sx
	 * @param sy the sy
	 * @param sz the sz
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
	
	/**
	 * Gets a scale Matrix44
	 *
	 * @param sx the sx
	 * @param sy the sy
	 * @param sz the sz
	 * @return a new Matrix44, set to be a scale matrix
	 */
	public static Matrix44 getScale(float sx, float sy, float sz)
	{
		return new Matrix44().setScale(sx, sy, sz);
	}
	
	/**
	 * Generates a ScaleMatrix from sx,sy,sz and sets this=ScaleMatrix*this
	 *
	 * @param sx the sx
	 * @param sy the sy
	 * @param sz the sz
	 * @return this
	 */
	public Matrix44 addScale(float sx, float sy, float sz)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setScale(sx, sy, sz).mult(this);
		set(temp.m);
		return this;
	}
	
	/**
	 * Sets this to be a scale matrix
	 *
	 * @param scale the scale
	 * @return this
	 */
	public Matrix44 setScale(Vector3 scale)
	{
		return setScale(scale.x, scale.y, scale.z);
	}
	
	/**
	 * Gets a scale matrix
	 *
	 * @param scale the scale
	 * @return a new Matrix44, set to be a scale matrix
	 */
	public static Matrix44 getScale(Vector3 scale)
	{
		return getScale(scale.x, scale.y, scale.z);
	}
	
	/**
	 * Generates a ScaleMatrix from scale and sets this=ScaleMatrix*this
	 *
	 * @param scale the scale
	 * @return this
	 */
	public Matrix44 addScale(Vector3 scale)
	{
		return addScale(scale.x, scale.y, scale.z);
	}

	/**
	 * Sets this to be a rotateX matrix
	 *
	 * @param alpha the angle
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

	/**
	 * Gets a rotateX matrix.
	 *
	 * @param alpha the alpha
	 * @return a new Matrix44, set to be a rotateX matrix
	 */
	public static Matrix44 getRotateX(float alpha)
	{
		return new Matrix44().setRotateX(alpha);
	}
	
	/**
	 * Generates a RotateXMatrix from alpha and sets this=RotateXMatrix*this
	 *
	 * @param alpha the angle
	 * @return this
	 */
	public Matrix44 addRotateX(float alpha)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setRotateX(alpha).mult(this);
		set(temp.m);
		return this;
	}

	/**
	 * Sets this to be a rotateY matrix
	 *
	 * @param alpha the angle
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

	/**
	 * Gets a rotateY matrix.
	 *
	 * @param alpha the alpha
	 * @return a new Matrix44, set to be a rotateY matrix
	 */
	public static Matrix44 getRotateY(float alpha)
	{
		return new Matrix44().setRotateY(alpha);
	}
	
	/**
	 * Generates a RotateYMatrix from alpha and sets this=RotateYMatrix*this
	 *
	 * @param alpha the angle
	 * @return this
	 */
	public Matrix44 addRotateY(float alpha)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setRotateY(alpha).mult(this);
		set(temp.m);
		return this;
	}

	/**
	 * Sets this to be a rotateZ matrix
	 *
	 * @param alpha the alpha
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

	/**
	 * Gets a rotateZ matrix.
	 *
	 * @param alpha the alpha
	 * @return a new Matrix44, set to be a rotateZ matrix
	 */
	public static Matrix44 getRotateZ(float alpha)
	{
		return new Matrix44().setRotateZ(alpha);
	}

	/**
	 * Generates a RotateZMatrix from alpha and sets this=RotateZMatrix*this
	 *
	 * @param alpha the alpha
	 * @return this
	 */
	public Matrix44 addRotateZ(float alpha)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setRotateZ(alpha).mult(this);
		set(temp.m);
		return this;
	}
	
	/**
	 * Sets this to a RotateMatrix around axis with angle alpha
	 *
	 * @param axis the axis
	 * @param alpha the angle
	 * @return this
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

	/**
	 * Gets the rotate Matrix44
	 *
	 * @param axis the axis
	 * @param alpha the alpha
	 * @return a RotateMatrix around axis with angle alpha
	 */
	public static Matrix44 getRotate(Vector3 axis, float alpha)
	{
		return new Matrix44().setRotate(axis, alpha);
	}

	/**
	 * Generates a RotateMatrix around axis with angle alpha and sets this=RotateMatrix*this
	 *
	 * @param axis the axis
	 * @param alpha the alpha
	 * @return this
	 */
	public Matrix44 addRotate(Vector3 axis, float alpha)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setRotate(axis, alpha).mult(this);
		set(temp.m);
		return this;
	}

	/**
	 * Sets this to be a translate matrix
	 *
	 * @param tx the tx
	 * @param ty the ty
	 * @param tz the tz
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

	/**
	 * Gets a translate Matrix
	 *
	 * @param tx the tx
	 * @param ty the ty
	 * @param tz the tz
	 * @return new Matrix, set to be a translate matrix
	 */
	public static Matrix44 getTranslate(float tx, float ty, float tz)
	{
		return new Matrix44().setTranslate(tx, ty, tz);
	}

	/**
	 * Generates a TranslateMatrix from tx,ty,tz and sets this=TranslateMatrix*this
	 *
	 * @param tx the tx
	 * @param ty the ty
	 * @param tz the tz
	 * @return this
	 */
	public Matrix44 addTranslate(float tx, float ty, float tz)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.setTranslate(tx, ty, tz).mult(this);
		set(temp.m);
		return this;
	}

	/**
	 * Sets this = this*right and returns this
	 *
	 * @param right the right
	 * @return this
	 */
	public Matrix44 mult(Matrix44 right)
	{
		if(temp == null)
			temp = new Matrix44();
		temp.copy(this);
		m[0][0] = temp.m[0][0]*right.m[0][0] + temp.m[0][1]*right.m[1][0] + temp.m[0][2]*right.m[2][0] + temp.m[0][3]*right.m[3][0];
		m[0][1] = temp.m[0][0]*right.m[0][1] + temp.m[0][1]*right.m[1][1] + temp.m[0][2]*right.m[2][1] + temp.m[0][3]*right.m[3][1];
		m[0][2] = temp.m[0][0]*right.m[0][2] + temp.m[0][1]*right.m[1][2] + temp.m[0][2]*right.m[2][2] + temp.m[0][3]*right.m[3][2];
		m[0][3] = temp.m[0][0]*right.m[0][3] + temp.m[0][1]*right.m[1][3] + temp.m[0][2]*right.m[2][3] + temp.m[0][3]*right.m[3][3];
		m[1][0] = temp.m[1][0]*right.m[0][0] + temp.m[1][1]*right.m[1][0] + temp.m[1][2]*right.m[2][0] + temp.m[1][3]*right.m[3][0];
		m[1][1] = temp.m[1][0]*right.m[0][1] + temp.m[1][1]*right.m[1][1] + temp.m[1][2]*right.m[2][1] + temp.m[1][3]*right.m[3][1];
		m[1][2] = temp.m[1][0]*right.m[0][2] + temp.m[1][1]*right.m[1][2] + temp.m[1][2]*right.m[2][2] + temp.m[1][3]*right.m[3][2];
		m[1][3] = temp.m[1][0]*right.m[0][3] + temp.m[1][1]*right.m[1][3] + temp.m[1][2]*right.m[2][3] + temp.m[1][3]*right.m[3][3];
		m[2][0] = temp.m[2][0]*right.m[0][0] + temp.m[2][1]*right.m[1][0] + temp.m[2][2]*right.m[2][0] + temp.m[2][3]*right.m[3][0];
		m[2][1] = temp.m[2][0]*right.m[0][1] + temp.m[2][1]*right.m[1][1] + temp.m[2][2]*right.m[2][1] + temp.m[2][3]*right.m[3][1];
		m[2][2] = temp.m[2][0]*right.m[0][2] + temp.m[2][1]*right.m[1][2] + temp.m[2][2]*right.m[2][2] + temp.m[2][3]*right.m[3][2];
		m[2][3] = temp.m[2][0]*right.m[0][3] + temp.m[2][1]*right.m[1][3] + temp.m[2][2]*right.m[2][3] + temp.m[2][3]*right.m[3][3];
		m[3][0] = temp.m[3][0]*right.m[0][0] + temp.m[3][1]*right.m[1][0] + temp.m[3][2]*right.m[2][0] + temp.m[3][3]*right.m[3][0];
		m[3][1] = temp.m[3][0]*right.m[0][1] + temp.m[3][1]*right.m[1][1] + temp.m[3][2]*right.m[2][1] + temp.m[3][3]*right.m[3][1];
		m[3][2] = temp.m[3][0]*right.m[0][2] + temp.m[3][1]*right.m[1][2] + temp.m[3][2]*right.m[2][2] + temp.m[3][3]*right.m[3][2];
		m[3][3] = temp.m[3][0]*right.m[0][3] + temp.m[3][1]*right.m[1][3] + temp.m[3][2]*right.m[2][3] + temp.m[3][3]*right.m[3][3];
		return this;
	}
	
	/**
	 * Multiplies two Matrix44
	 *
	 * @param left the left
	 * @param right the right
	 * @return a new Matrix, set to left*right
	 */
	public static Matrix44 mult(Matrix44 left, Matrix44 right)
	{
		return new Matrix44(left).mult(right);
	}
	
	/**
	 * Transforms a point.
	 *
	 * @param in the point to transform
	 */
	public void transformPoint(Vector3 in)
	{
		float x = m[0][0]*in.x + m[0][1]*in.y + m[0][2]*in.z + m[0][3];
		float y = m[1][0]*in.x + m[1][1]*in.y + m[1][2]*in.z + m[1][3];
		float z = m[2][0]*in.x + m[2][1]*in.y + m[2][2]*in.z + m[2][3];
		float w = m[3][0]*in.x + m[3][1]*in.y + m[3][2]*in.z + m[3][3];
		x /= w;
		y /= w;
		z /= w;
		in.x = x;
		in.y = y;
		in.z = z;
	}
	
	/**
	 * Transforms a point.
	 *
	 * @param in the point to transform
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
	
	/**
	 * Transforms a sphere.
	 *
	 * @param in the sphere to transform
	 */
	public void transformSphere(Sphere inout)
	{
		// make tempV a point on the hull
		tempV.set(1,0,0).multiply(inout.radius).add(inout.center);
		
		// transform both points
		transformPoint(inout.center);
		transformPoint(tempV);
		
		// make tempV a vector from center to hull again
		tempV.subtract(inout.center);

		inout.radius = tempV.length();
	}
	
	/**
	 * Transforms a sphere.
	 *
	 * @param in the sphere to transform
	 */
	public void transformSphere(Sphere in, Sphere out)
	{
		out.center.set(in.center);
		out.radius = in.radius;
		transformSphere(out);
	}
}
