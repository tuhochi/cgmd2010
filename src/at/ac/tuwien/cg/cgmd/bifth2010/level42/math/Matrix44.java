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
	 * column major
	 */
	public final float m[] = new float[16];
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
	 * @param m the m (column major)
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
		for(int i=0; i<16; i++)
			dos.writeFloat(m[i]);
	}
	
	/* (non-Javadoc)
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level42.util.Persistable#restore(java.io.DataInputStream)
	 */
	public void restore(DataInputStream dis) throws IOException
	{
		for(int i=0; i<16; i++)
			m[i] = dis.readFloat();
	}
	
	/**
	 * Gets the array16.
	 *
	 * @return this as a column major float[16]
	 */
	public float[] getArray16()
	{
		return m;
	}
	
	/**
	 * Sets this from a [row][col] array
	 *
	 * @param other the array
	 */
	public void set(float[][] other)
	{
		for(int c=0; c<4; c++)
			for(int r=0; r<4; r++)
				m[4*c+r] = other[r][c];
	}
	
	/**
	 * Sets this from a column major array
	 *
	 * @param m the array
	 */
	public void set(float[] m16)
	{
		for(int i=0; i<16; i++)
			this.m[i] = m16[i];
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
		m[ 0] = m00;
		m[ 1] = m10;
		m[ 2] = m20;
		m[ 3] = m30;
		m[ 4] = m01;
		m[ 5] = m11;
		m[ 6] = m21;
		m[ 7] = m31;
		m[ 8] = m02;
		m[ 9] = m12;
		m[10] = m22;
		m[11] = m32;
		m[12] = m03;
		m[13] = m13;
		m[14] = m23;
		m[15] = m33;
	}
	
	/**
	 * Sets this to an identity.
	 */
	public void setIdentity()
	{
		m[ 0] = 1;
		m[ 1] = 0;
		m[ 2] = 0;
		m[ 3] = 0;
		
		m[ 4] = 0;
		m[ 5] = 1;
		m[ 6] = 0;
		m[ 7] = 0;
		
		m[ 8] = 0;
		m[ 9] = 0;
		m[10] = 1;
		m[11] = 0;
		
		m[12] = 0;
		m[13] = 0;
		m[14] = 0;
		m[15] = 1;
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
	    m[ 0] = 1.0f - 2.0f * ( y * y + z * z );
	    m[ 4] = 2.0f * ( x * y - w * z );	
	    m[ 8] = 2.0f * ( x * z + w * y );
	    m[12] = 0.0f;

	    m[ 1] = 2.0f * ( x * y + w * z );
	    m[ 5] = 1.0f - 2.0f * ( x * x + z * z );
	    m[ 9] = 2.0f * ( y * z - w * x );
	    m[13] = 0.0f;
	    
	    m[ 2] = 2.0f * ( x * z - w * y );
	    m[ 6] = 2.0f * ( y * z + w * x );
	    m[10] = 1.0f - 2.0f * ( x * x + y * y );
	    m[14] = 0.0f;

	    m[ 3] = 0.0f;
	    m[ 7] = 0.0f;
	    m[11] = 0.0f;
	    m[15] = 1.0f;

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
		m[ 0] = sx; m[ 4] =  0; m[ 8] =  0; m[12] =  0;
		m[ 1] =  0; m[ 5] = sy; m[ 9] =  0; m[13] =  0;
		m[ 2] =  0; m[ 6] =  0; m[10] = sz; m[14] =  0;
		m[ 3] =  0; m[ 7] =  0; m[11] =  0; m[15] =  1;
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
		return setScale(scale.v[0], scale.v[1], scale.v[2]);
	}
	
	/**
	 * Gets a scale matrix
	 *
	 * @param scale the scale
	 * @return a new Matrix44, set to be a scale matrix
	 */
	public static Matrix44 getScale(Vector3 scale)
	{
		return getScale(scale.v[0], scale.v[1], scale.v[2]);
	}
	
	/**
	 * Generates a ScaleMatrix from scale and sets this=ScaleMatrix*this
	 *
	 * @param scale the scale
	 * @return this
	 */
	public Matrix44 addScale(Vector3 scale)
	{
		return addScale(scale.v[0], scale.v[1], scale.v[2]);
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
		m[ 0] =     1; m[ 4] =     0; m[ 8] =     0; m[12] = 0;
		m[ 1] =     0; m[ 5] =  cosa; m[ 9] = -sina; m[13] = 0;
		m[ 2] =     0; m[ 6] =  sina; m[10] =  cosa; m[14] = 0;
		m[ 3] =     0; m[ 7] =     0; m[11] =     0; m[15] = 1;
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
		m[ 0] =  cosa; m[ 4] =     0; m[ 8] =  sina; m[12] = 0;
		m[ 1] =     0; m[ 5] =     1; m[ 9] =     0; m[13] = 0;
		m[ 2] = -sina; m[ 6] =     0; m[10] =  cosa; m[14] = 0;
		m[ 3] =     0; m[ 7] =     0; m[11] =     0; m[15] = 1;
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
		m[ 0] =  cosa; m[ 4] = -sina; m[ 8] =     0; m[12] = 0;
		m[ 1] =  sina; m[ 5] =  cosa; m[ 9] =     0; m[13] = 0;
		m[ 2] =     0; m[ 6] =     0; m[10] =     1; m[14] = 0;
		m[ 3] =     0; m[ 7] =     0; m[11] =     0; m[15] = 1;
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
		m[ 0] = axis.v[0] * axis.v[0] + (1 - axis.v[0] * axis.v[0]) * cosa;
		m[ 1] = axis.v[0] * axis.v[1] * (1 - cosa) + axis.v[2] * sina;
		m[ 2] = axis.v[0] * axis.v[2] * (1 - cosa) - axis.v[1] * sina;
		m[ 3] = 0; 
		m[ 4] = axis.v[0] * axis.v[1] * (1 - cosa) - axis.v[2] * sina;
		m[ 5] = axis.v[1] * axis.v[1] + (1 - axis.v[1] * axis.v[1]) * cosa;
		m[ 6] = axis.v[1] * axis.v[2] * (1 - cosa) + axis.v[0] * sina;
		m[ 7] = 0; 
		m[ 8] = axis.v[0] * axis.v[2] * (1 - cosa) + axis.v[1] * sina;
		m[ 9] = axis.v[1] * axis.v[2] * (1 - cosa) - axis.v[0] * sina;
		m[10] = axis.v[2] * axis.v[2] + (1 - axis.v[2] * axis.v[2]) * cosa;
		m[11] = 0;
		m[12] = 0;
		m[13] = 0;
		m[14] = 0;
		m[15] = 1;
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
		m[ 0] = 1; m[ 4] = 0; m[ 8] = 0; m[12] = tx;
		m[ 1] = 0; m[ 5] = 1; m[ 9] = 0; m[13] = ty;
		m[ 2] = 0; m[ 6] = 0; m[10] = 1; m[14] = tz;
		m[ 3] = 0; m[ 7] = 0; m[11] = 0; m[15] = 1;
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
		m[ 0] = temp.m[ 0]*right.m[ 0] + temp.m[ 4]*right.m[ 1] + temp.m[ 8]*right.m[ 2] + temp.m[12]*right.m[ 3];
		m[ 4] = temp.m[ 0]*right.m[ 4] + temp.m[ 4]*right.m[ 5] + temp.m[ 8]*right.m[ 6] + temp.m[12]*right.m[ 7];
		m[ 8] = temp.m[ 0]*right.m[ 8] + temp.m[ 4]*right.m[ 9] + temp.m[ 8]*right.m[10] + temp.m[12]*right.m[11];
		m[12] = temp.m[ 0]*right.m[12] + temp.m[ 4]*right.m[13] + temp.m[ 8]*right.m[14] + temp.m[12]*right.m[15];
		m[ 1] = temp.m[ 1]*right.m[ 0] + temp.m[ 5]*right.m[ 1] + temp.m[ 9]*right.m[ 2] + temp.m[13]*right.m[ 3];
		m[ 5] = temp.m[ 1]*right.m[ 4] + temp.m[ 5]*right.m[ 5] + temp.m[ 9]*right.m[ 6] + temp.m[13]*right.m[ 7];
		m[ 9] = temp.m[ 1]*right.m[ 8] + temp.m[ 5]*right.m[ 9] + temp.m[ 9]*right.m[10] + temp.m[13]*right.m[11];
		m[13] = temp.m[ 1]*right.m[12] + temp.m[ 5]*right.m[13] + temp.m[ 9]*right.m[14] + temp.m[13]*right.m[15];
		m[ 2] = temp.m[ 2]*right.m[ 0] + temp.m[ 6]*right.m[ 1] + temp.m[10]*right.m[ 2] + temp.m[14]*right.m[ 3];
		m[ 6] = temp.m[ 2]*right.m[ 4] + temp.m[ 6]*right.m[ 5] + temp.m[10]*right.m[ 6] + temp.m[14]*right.m[ 7];
		m[10] = temp.m[ 2]*right.m[ 8] + temp.m[ 6]*right.m[ 9] + temp.m[10]*right.m[10] + temp.m[14]*right.m[11];
		m[14] = temp.m[ 2]*right.m[12] + temp.m[ 6]*right.m[13] + temp.m[10]*right.m[14] + temp.m[14]*right.m[15];
		m[ 3] = temp.m[ 3]*right.m[ 0] + temp.m[ 7]*right.m[ 1] + temp.m[11]*right.m[ 2] + temp.m[15]*right.m[ 3];
		m[ 7] = temp.m[ 3]*right.m[ 4] + temp.m[ 7]*right.m[ 5] + temp.m[11]*right.m[ 6] + temp.m[15]*right.m[ 7];
		m[11] = temp.m[ 3]*right.m[ 8] + temp.m[ 7]*right.m[ 9] + temp.m[11]*right.m[10] + temp.m[15]*right.m[11];
		m[15] = temp.m[ 3]*right.m[12] + temp.m[ 7]*right.m[13] + temp.m[11]*right.m[14] + temp.m[15]*right.m[15];
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
		float x = m[ 0]*in.v[0] + m[ 4]*in.v[1] + m[ 8]*in.v[2] + m[12];
		float y = m[ 1]*in.v[0] + m[ 5]*in.v[1] + m[ 9]*in.v[2] + m[13];
		float z = m[ 2]*in.v[0] + m[ 6]*in.v[1] + m[10]*in.v[2] + m[14];
		float w = m[ 3]*in.v[0] + m[ 7]*in.v[1] + m[11]*in.v[2] + m[15];
		x /= w;
		y /= w;
		z /= w;
		in.v[0] = x;
		in.v[1] = y;
		in.v[2] = z;
	}
	
	/**
	 * Transforms a point.
	 *
	 * @param in the point to transform
	 */
	public void transformPoint(Vector4 in)
	{
		float x = m[ 0]*in.v[0] + m[ 4]*in.v[1] + m[ 8]*in.v[2] + m[12]*in.v[3];
		float y = m[ 1]*in.v[0] + m[ 5]*in.v[1] + m[ 9]*in.v[2] + m[13]*in.v[3];
		float z = m[ 2]*in.v[0] + m[ 6]*in.v[1] + m[10]*in.v[2] + m[14]*in.v[3];
		float w = m[ 3]*in.v[0] + m[ 7]*in.v[1] + m[11]*in.v[2] + m[15]*in.v[3];
		in.v[0] = x;
		in.v[1] = y;
		in.v[2] = z;
		in.v[3] = w;
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
