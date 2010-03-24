package at.ac.tuwien.cg.cgmd.bifth2010.level17.math;

public class Matrix4x4
{
	public float[][] m = new float[4][4];

	public Matrix4x4 ()
	{
		setIdentity ();
	}

	public Matrix4x4 (Matrix4x4 copy)
	{
		for (int r=0; r<4; r++)
			for (int c=0; c<4; c++)
				m[r][c] = copy.m[r][c];
	}

	public void setIdentity ()
	{
		for (int r=0; r<4; r++)
			for (int c=0; c<4; c++)
				m[r][c] = (r==c) ? 1.0f : 0.0f;
	}

	public Matrix4x4 setScale (float sx, float sy, float sz)
	{
		m[0][0] = sx; m[0][1] =  0; m[0][2] =  0; m[0][3] =  0;
		m[1][0] =  0; m[1][1] = sy; m[1][2] =  0; m[1][3] =  0;
		m[2][0] =  0; m[2][1] =  0; m[2][2] = sz; m[2][3] =  0;
		m[3][0] =  0; m[3][1] =  0; m[3][2] =  0; m[3][3] =  1;

		return (this);
	}

	public static Matrix4x4 RotateX (float alpha)
	{
		float cosa = (float) Math.cos(alpha), sina = (float) Math.sin(alpha);

		Matrix4x4 retMat = new Matrix4x4();
		retMat.m[0][0] =     1; retMat.m[0][1] =     0; retMat.m[0][2] =     0; retMat.m[0][3] = 0;
		retMat.m[1][0] =     0; retMat.m[1][1] =  cosa; retMat.m[1][2] = -sina; retMat.m[1][3] = 0;
		retMat.m[2][0] =     0; retMat.m[2][1] =  sina; retMat.m[2][2] =  cosa; retMat.m[2][3] = 0;
		retMat.m[3][0] =     0; retMat.m[3][1] =     0; retMat.m[3][2] =     0; retMat.m[3][3] = 1;

		return retMat;
	}

	public static Matrix4x4 RotateY (float alpha)
	{
		float cosa = (float) Math.cos(alpha), sina = (float) Math.sin(alpha);

		Matrix4x4 retMat = new Matrix4x4();
		retMat.m[0][0] =  cosa; retMat.m[0][1] =     0; retMat.m[0][2] =  sina; retMat.m[0][3] = 0;
		retMat.m[1][0] =     0; retMat.m[1][1] =     1; retMat.m[1][2] =     0; retMat.m[1][3] = 0;
		retMat.m[2][0] = -sina; retMat.m[2][1] =     0; retMat.m[2][2] =  cosa; retMat.m[2][3] = 0;
		retMat.m[3][0] =     0; retMat.m[3][1] =     0; retMat.m[3][2] =     0; retMat.m[3][3] = 1;

		return retMat;
	}

	
	public static Matrix4x4 RotateZ (float alpha)
	{
		float cosa = (float) Math.cos(alpha), sina = (float) Math.sin(alpha);

		Matrix4x4 retMat = new Matrix4x4();
		retMat.m[0][0] =  cosa; retMat.m[0][1] = -sina; retMat.m[0][2] =     0; retMat.m[0][3] = 0;
		retMat.m[1][0] =  sina; retMat.m[1][1] =  cosa; retMat.m[1][2] =     0; retMat.m[1][3] = 0;
		retMat.m[2][0] =     0; retMat.m[2][1] =     0; retMat.m[2][2] =     1; retMat.m[2][3] = 0;
		retMat.m[3][0] =     0; retMat.m[3][1] =     0; retMat.m[3][2] =     0; retMat.m[3][3] = 1;

		return retMat;
	}

	public static Matrix4x4 RotateAxis (float alpha, Vector3 axis)
	{
		float cosa = (float) Math.cos(alpha), sina = (float) Math.sin(alpha);

		Matrix4x4 retMat = new Matrix4x4();
		retMat.m[0][0] =  axis.x * axis.x + (1 - axis.x * axis.x) * cosa;	retMat.m[0][1] = axis.x * axis.y * (1 - cosa) - axis.z * sina; 		retMat.m[0][2] = axis.x * axis.z * (1 - cosa) + axis.y * sina;		retMat.m[0][3] = 0;
		retMat.m[1][0] =  axis.x * axis.y * (1 - cosa) + axis.z * sina;		retMat.m[1][1] = axis.y * axis.y + (1 - axis.y * axis.y) * cosa;	retMat.m[1][2] = axis.y * axis.z * (1 - cosa) - axis.x * sina;		retMat.m[1][3] = 0;
		retMat.m[2][0] =  axis.x * axis.z * (1 - cosa) - axis.y * sina;		retMat.m[2][1] = axis.y * axis.z * (1 - cosa) + axis.x * sina;		retMat.m[2][2] = axis.z * axis.z + (1 - axis.z * axis.z) * cosa;	retMat.m[2][3] = 0;
		retMat.m[3][0] =     0; 											retMat.m[3][1] =     0; 											retMat.m[3][2] =     0;												retMat.m[3][3] = 1;

		return retMat;
	}

	public static Matrix4x4 Translate (float tx, float ty, float tz)
	{
		Matrix4x4 retMat = new Matrix4x4();
		retMat.m[0][0] = 1; retMat.m[0][1] = 0; retMat.m[0][2] = 0; retMat.m[0][3] = tx;
		retMat.m[1][0] = 0; retMat.m[1][1] = 1; retMat.m[1][2] = 0; retMat.m[1][3] = ty;
		retMat.m[2][0] = 0; retMat.m[2][1] = 0; retMat.m[2][2] = 1; retMat.m[2][3] = tz;
		retMat.m[3][0] = 0; retMat.m[3][1] = 0; retMat.m[3][2] = 0; retMat.m[3][3] = 1;

		return retMat;
	}

	public static Matrix4x4 mult (Matrix4x4 left, Matrix4x4 right)
	{
		Matrix4x4 retMat = new Matrix4x4();
		retMat.m[0][0] = left.m[0][0]*right.m[0][0] + left.m[0][1]*right.m[1][0] + left.m[0][2]*right.m[2][0] + left.m[0][3]*right.m[3][0];
		retMat.m[0][1] = left.m[0][0]*right.m[0][1] + left.m[0][1]*right.m[1][1] + left.m[0][2]*right.m[2][1] + left.m[0][3]*right.m[3][1];
		retMat.m[0][2] = left.m[0][0]*right.m[0][2] + left.m[0][1]*right.m[1][2] + left.m[0][2]*right.m[2][2] + left.m[0][3]*right.m[3][2];
		retMat.m[0][3] = left.m[0][0]*right.m[0][3] + left.m[0][1]*right.m[1][3] + left.m[0][2]*right.m[2][3] + left.m[0][3]*right.m[3][3];
		retMat.m[1][0] = left.m[1][0]*right.m[0][0] + left.m[1][1]*right.m[1][0] + left.m[1][2]*right.m[2][0] + left.m[1][3]*right.m[3][0];
		retMat.m[1][1] = left.m[1][0]*right.m[0][1] + left.m[1][1]*right.m[1][1] + left.m[1][2]*right.m[2][1] + left.m[1][3]*right.m[3][1];
		retMat.m[1][2] = left.m[1][0]*right.m[0][2] + left.m[1][1]*right.m[1][2] + left.m[1][2]*right.m[2][2] + left.m[1][3]*right.m[3][2];
		retMat.m[1][3] = left.m[1][0]*right.m[0][3] + left.m[1][1]*right.m[1][3] + left.m[1][2]*right.m[2][3] + left.m[1][3]*right.m[3][3];
		retMat.m[2][0] = left.m[2][0]*right.m[0][0] + left.m[2][1]*right.m[1][0] + left.m[2][2]*right.m[2][0] + left.m[2][3]*right.m[3][0];
		retMat.m[2][1] = left.m[2][0]*right.m[0][1] + left.m[2][1]*right.m[1][1] + left.m[2][2]*right.m[2][1] + left.m[2][3]*right.m[3][1];
		retMat.m[2][2] = left.m[2][0]*right.m[0][2] + left.m[2][1]*right.m[1][2] + left.m[2][2]*right.m[2][2] + left.m[2][3]*right.m[3][2];
		retMat.m[2][3] = left.m[2][0]*right.m[0][3] + left.m[2][1]*right.m[1][3] + left.m[2][2]*right.m[2][3] + left.m[2][3]*right.m[3][3];
		retMat.m[3][0] = left.m[3][0]*right.m[0][0] + left.m[3][1]*right.m[1][0] + left.m[3][2]*right.m[2][0] + left.m[3][3]*right.m[3][0];
		retMat.m[3][1] = left.m[3][0]*right.m[0][1] + left.m[3][1]*right.m[1][1] + left.m[3][2]*right.m[2][1] + left.m[3][3]*right.m[3][1];
		retMat.m[3][2] = left.m[3][0]*right.m[0][2] + left.m[3][1]*right.m[1][2] + left.m[3][2]*right.m[2][2] + left.m[3][3]*right.m[3][2];
		retMat.m[3][3] = left.m[3][0]*right.m[0][3] + left.m[3][1]*right.m[1][3] + left.m[3][2]*right.m[2][3] + left.m[3][3]*right.m[3][3];

		return retMat;
	}

	public Vector3 transformPoint (Vector3 in)
	{
		Vector3 retVec = new Vector3();
		retVec.x = m[0][0]*in.x + m[0][1]*in.y + m[0][2]*in.z;
		retVec.y = m[1][0]*in.x + m[1][1]*in.y + m[1][2]*in.z;
		retVec.z = m[2][0]*in.x + m[2][1]*in.y + m[2][2]*in.z;
		return retVec;
	}
	
	public Vector4 transformPoint (Vector4 in)
	{
		Vector4 retVec = new Vector4();
		retVec.x = m[0][0]*in.x + m[0][1]*in.y + m[0][2]*in.z + m[0][3]*in.w;
		retVec.y = m[1][0]*in.x + m[1][1]*in.y + m[1][2]*in.z + m[1][3]*in.w;
		retVec.z = m[2][0]*in.x + m[2][1]*in.y + m[2][2]*in.z + m[2][3]*in.w;
		retVec.w = m[3][0]*in.x + m[3][1]*in.y + m[3][2]*in.z + m[3][3]*in.w;
		return retVec;
	}
	
	public float[] toFloatArray()
	{
		float[] array = new float[16];
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				array[i*4 + j] = m[i][j];
			}
		}

		return array;
	}
	
	public void fromFloatArray(float[] array)
	{
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				m[i][j] = array[j*4 + i];
			}
		}
	}

	public String toString ()
	{
		String ret = "";
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
			{
				ret += m[i][j] + ", ";
			}
		}
		
		return ret;
	}
	
	public Matrix4x4 InvertedCopy()
	{
        // http://www.cvl.iis.u-tokyo.ac.jp/~miyazaki/tech/teche23.html
		Matrix4x4 ret = new Matrix4x4();
        float det = (m[0][0]*m[1][1]*m[2][2]*m[3][3]) + (m[0][0]*m[1][2]*m[2][3]*m[3][1]) + (m[0][0]*m[1][3]*m[2][1]*m[3][2])
        +(m[0][1]*m[1][0]*m[2][3]*m[3][2]) + (m[0][1]*m[1][2]*m[2][0]*m[3][3]) + (m[0][1]*m[1][3]*m[2][2]*m[3][0])
        +(m[0][2]*m[1][0]*m[2][1]*m[3][3]) + (m[0][2]*m[1][1]*m[2][3]*m[3][0]) + (m[0][2]*m[1][3]*m[2][0]*m[3][1])
        +(m[0][3]*m[1][0]*m[2][2]*m[3][1]) + (m[0][3]*m[1][1]*m[2][0]*m[3][2]) + (m[0][3]*m[1][2]*m[2][1]*m[3][0])
        -(m[0][0]*m[1][1]*m[2][3]*m[3][2]) - (m[0][0]*m[1][2]*m[2][1]*m[3][3]) - (m[0][0]*m[1][3]*m[2][2]*m[3][1])
        -(m[0][1]*m[1][0]*m[2][2]*m[3][3]) - (m[0][1]*m[1][2]*m[2][3]*m[3][0]) - (m[0][1]*m[1][3]*m[2][0]*m[3][2])
        -(m[0][2]*m[1][0]*m[2][3]*m[3][1]) - (m[0][2]*m[1][1]*m[2][0]*m[3][3]) - (m[0][2]*m[1][3]*m[2][1]*m[3][0])
        -(m[0][3]*m[1][0]*m[2][1]*m[3][2]) - (m[0][3]*m[1][1]*m[2][2]*m[3][0]) - (m[0][3]*m[1][2]*m[2][0]*m[3][1]);

        float invDet = 1f / det;

		ret.m[0][0] = invDet * ((m[1][1]*m[2][2]*m[3][3]) + (m[1][2]*m[2][3]*m[3][1]) + (m[1][3]*m[2][1]*m[3][2]) - (m[1][1]*m[2][3]*m[3][2]) - (m[1][2]*m[2][1]*m[3][3]) - (m[1][3]*m[2][2]*m[3][1]));
		ret.m[0][1] = invDet * ((m[0][1]*m[2][3]*m[3][2]) + (m[0][2]*m[2][1]*m[3][3]) + (m[0][3]*m[2][2]*m[3][1]) - (m[0][1]*m[2][2]*m[3][3]) - (m[0][2]*m[2][3]*m[3][1]) - (m[0][3]*m[2][1]*m[3][2]));
		ret.m[0][2] = invDet * ((m[0][1]*m[1][2]*m[3][3]) + (m[0][2]*m[1][3]*m[3][1]) + (m[0][3]*m[1][1]*m[3][2]) - (m[0][1]*m[1][3]*m[3][2]) - (m[0][2]*m[1][1]*m[3][3]) - (m[0][3]*m[1][2]*m[3][1]));
		ret.m[0][3] = invDet * ((m[0][1]*m[1][3]*m[2][2]) + (m[0][2]*m[1][1]*m[2][3]) + (m[0][3]*m[1][2]*m[2][1]) - (m[0][1]*m[1][2]*m[2][3]) - (m[0][2]*m[1][3]*m[2][1]) - (m[0][3]*m[1][1]*m[2][2]));
		
		ret.m[1][0] = invDet * ((m[1][0]*m[2][3]*m[3][2]) + (m[1][2]*m[2][0]*m[3][3]) + (m[1][3]*m[2][2]*m[3][0]) - (m[1][0]*m[2][2]*m[3][3]) - (m[1][2]*m[2][3]*m[3][0]) - (m[1][3]*m[2][0]*m[3][2]));
		ret.m[1][1] = invDet * ((m[0][0]*m[2][2]*m[3][3]) + (m[0][2]*m[2][3]*m[3][0]) + (m[0][3]*m[2][0]*m[3][2]) - (m[0][0]*m[2][3]*m[3][2]) - (m[0][2]*m[2][0]*m[3][3]) - (m[0][3]*m[2][2]*m[3][0]));
		ret.m[1][2] = invDet * ((m[0][0]*m[1][3]*m[3][2]) + (m[0][2]*m[1][0]*m[3][3]) + (m[0][3]*m[1][2]*m[3][0]) - (m[0][0]*m[1][2]*m[3][3]) - (m[0][2]*m[1][3]*m[3][0]) - (m[0][3]*m[1][0]*m[3][2]));
		ret.m[1][3] = invDet * ((m[0][0]*m[1][2]*m[2][3]) + (m[0][2]*m[1][3]*m[2][0]) + (m[0][3]*m[1][0]*m[2][2]) - (m[0][0]*m[1][3]*m[2][2]) - (m[0][2]*m[1][0]*m[2][3]) - (m[0][3]*m[1][2]*m[2][0]));
		
		ret.m[2][0] = invDet * ((m[1][0]*m[2][1]*m[3][3]) + (m[1][1]*m[2][3]*m[3][0]) + (m[1][3]*m[2][0]*m[3][1]) - (m[1][0]*m[2][3]*m[3][1]) - (m[1][1]*m[2][0]*m[3][3]) - (m[1][3]*m[2][1]*m[3][0]));
		ret.m[2][1] = invDet * ((m[0][0]*m[2][3]*m[3][1]) + (m[0][1]*m[2][0]*m[3][3]) + (m[0][3]*m[2][1]*m[3][0]) - (m[0][0]*m[2][1]*m[3][3]) - (m[0][1]*m[2][3]*m[3][0]) - (m[0][3]*m[2][0]*m[3][1]));
		ret.m[2][2] = invDet * ((m[0][0]*m[1][1]*m[3][3]) + (m[0][1]*m[1][3]*m[3][0]) + (m[0][3]*m[1][0]*m[3][1]) - (m[0][0]*m[1][3]*m[3][1]) - (m[0][1]*m[1][0]*m[3][3]) - (m[0][3]*m[1][1]*m[3][0]));
		ret.m[2][3] = invDet * ((m[0][0]*m[1][3]*m[2][1]) + (m[0][1]*m[1][0]*m[2][3]) + (m[0][3]*m[1][1]*m[2][0]) - (m[0][0]*m[1][1]*m[2][3]) - (m[0][1]*m[1][3]*m[2][0]) - (m[0][3]*m[1][0]*m[2][1]));
		
		ret.m[3][0] = invDet * ((m[1][0]*m[2][2]*m[3][1]) + (m[1][1]*m[2][0]*m[3][1]) + (m[1][2]*m[2][1]*m[3][0]) - (m[1][0]*m[2][1]*m[3][2]) - (m[1][1]*m[2][2]*m[3][0]) - (m[1][2]*m[2][0]*m[3][1]));
		ret.m[3][1] = invDet * ((m[0][0]*m[2][1]*m[3][2]) + (m[0][1]*m[2][2]*m[3][0]) + (m[0][2]*m[2][0]*m[3][1]) - (m[0][0]*m[2][2]*m[3][1]) - (m[0][1]*m[2][0]*m[3][2]) - (m[0][2]*m[2][1]*m[3][0]));
		ret.m[3][2] = invDet * ((m[0][0]*m[1][2]*m[3][1]) + (m[0][1]*m[1][0]*m[3][2]) + (m[0][2]*m[1][1]*m[3][0]) - (m[0][0]*m[1][1]*m[3][2]) - (m[0][1]*m[1][2]*m[3][0]) - (m[0][2]*m[1][0]*m[3][1]));
		ret.m[3][3] = invDet * ((m[0][0]*m[1][1]*m[2][2]) + (m[0][1]*m[1][2]*m[2][0]) + (m[0][2]*m[1][0]*m[2][1]) - (m[0][0]*m[1][2]*m[2][1]) - (m[0][1]*m[1][0]*m[2][2]) - (m[0][2]*m[1][1]*m[2][0]));
		
		return ret;
	}
}