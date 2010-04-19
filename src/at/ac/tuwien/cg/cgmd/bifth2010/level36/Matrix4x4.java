package at.ac.tuwien.cg.cgmd.bifth2010.level36;

//import android.opengl.Matrix;

public class Matrix4x4 {
	
	float[] matrix;
	
	public Matrix4x4() {
		this.matrix = new float[16];
	}
	
	//column-major order
	public void setValue(int row, int col, float value) {
		this.matrix[col*4+row] = value;
	}
	
	public float getValue(int row, int col) {
		return this.matrix[col*4+row];
	}
	
	public float[] getArray() {
		return this.matrix;
	}
	
	public void setArray(float[] columnMajor) {
		this.matrix = columnMajor;
	}
	
	public void loadIdentity() {
		setValue(0,0,1.0f);
		setValue(0,1,0.0f);
		setValue(0,2,0.0f);
		setValue(0,3,0.0f);
		setValue(1,0,0.0f);
		setValue(1,1,1.0f);
		setValue(1,2,0.0f);
		setValue(1,3,0.0f);
		setValue(2,0,0.0f);
		setValue(2,1,0.0f);
		setValue(2,2,1.0f);
		setValue(2,3,0.0f);
		setValue(3,0,0.0f);
		setValue(3,1,0.0f);
		setValue(3,2,0.0f);
		setValue(3,3,1.0f);
	}
	
	public void multiplyWith(Matrix4x4 m) {
		float result;

		Matrix4x4 tmp = new Matrix4x4();
		for (int k = 0; k < 4; k++) 
		{
			for (int l = 0; l < 4; l++) 
			{			
				float[] row = getRow(k);
				float[] col = m.getCol(l);
				result = row[0] * col[0] + row[1] * col[1] + row[2] * col[2] + row[3] * col[3];
				tmp.setValue(k, l, result);
			}
		}
		this.matrix = tmp.getArray();
	}
	
	public float[] getRow(int row) {
		float[] result = new float[4];
		result[0] = getValue(row, 0);
		result[1] = getValue(row, 1);
		result[2] = getValue(row, 2);
		result[3] = getValue(row, 3);
		return result;
	}
	
	public float[] getCol(int col) {
		float[] result = new float[4];
		result[0] = getValue(0, col);
		result[1] = getValue(1, col);
		result[2] = getValue(2, col);
		result[3] = getValue(3, col);
		return result;
	}
	
	public void translate(float x, float y, float z) {
		Matrix4x4 tmp = new Matrix4x4();
		tmp.loadIdentity();
		tmp.setValue(0,3, x);
		tmp.setValue(1,3, y);
		tmp.setValue(2,3, z);
		multiplyWith(tmp);
	}
	
	public void printMatrix() {
		int count = 0;
		System.out.println("Matrix:");
		for (int i = 0; i < 16; i++) {
			if(count < 4) { 
				System.out.print(i);
			} else {
				System.out.println(i);
				count = 0;
			}
			count++;
		}
	}
}