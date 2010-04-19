package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Coin {
	
	boolean active;
	int x;
	int y;
	
	Mesh activeCoin=new Mesh();
	Mesh inactiveCoin=new Mesh();
	
	public Coin(GL10 gl, int _x, int _y, boolean _active, float x1, float y1, float x2, float y2,
			float texX1_A, float texY1_A, float texX2_A, float texY2_A,
			float texX1_I, float texY1_I, float texX2_I, float texY2_I) {
		active=_active;
		x=_x;
		y=_y;
		
		FloatBuffer vertices = ByteBuffer.allocateDirect(12*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		FloatBuffer texCoords = ByteBuffer.allocateDirect(8*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices = ByteBuffer.allocateDirect(6*2).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		vertices.put(0,x1);
		vertices.put(1,y1);
		vertices.put(2,-1.0f);
		texCoords.put(0,texX1_A);
		texCoords.put(1,texY1_A);
		
		vertices.put(3,x2);
		vertices.put(4,y1);
		vertices.put(5,-1.0f);
		texCoords.put(2,texX2_A);
		texCoords.put(3,texY1_A);
		
		vertices.put(6,x2);
		vertices.put(7,y2);
		vertices.put(8,-1.0f);
		texCoords.put(4,texX2_A);
		texCoords.put(5,texY2_A);
		
		vertices.put(9,x1);
		vertices.put(10,y2);
		vertices.put(11,-1.0f);
		texCoords.put(6,texX1_A);
		texCoords.put(7,texY2_A);
		
		
		indices.put(0,(short) (0));
		indices.put(1,(short) (1));
		indices.put(2,(short) (2));
		
		indices.put(3,(short) (0));
		indices.put(4,(short) (2));
		indices.put(5,(short) (3));
		
		activeCoin.init(gl, vertices, texCoords, indices);
		
		FloatBuffer vertices_I = ByteBuffer.allocateDirect(12*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		FloatBuffer texCoords_I = ByteBuffer.allocateDirect(8*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ShortBuffer indices_I = ByteBuffer.allocateDirect(6*2).order(ByteOrder.nativeOrder()).asShortBuffer();
		
		vertices_I.put(0,x1);
		vertices_I.put(1,y1);
		vertices_I.put(2,-1.0f);
		texCoords_I.put(0,texX1_I);
		texCoords_I.put(1,texY1_I);
		
		vertices_I.put(3,x2);
		vertices_I.put(4,y1);
		vertices_I.put(5,-1.0f);
		texCoords_I.put(2,texX2_I);
		texCoords_I.put(3,texY1_I);
		
		vertices_I.put(6,x2);
		vertices_I.put(7,y2);
		vertices_I.put(8,-1.0f);
		texCoords_I.put(4,texX2_I);
		texCoords_I.put(5,texY2_I);
		
		vertices_I.put(9,x1);
		vertices_I.put(10,y2);
		vertices_I.put(11,-1.0f);
		texCoords_I.put(6,texX1_I);
		texCoords_I.put(7,texY2_I);
		
		
		indices_I.put(0,(short) (0));
		indices_I.put(1,(short) (1));
		indices_I.put(2,(short) (2));
		
		indices_I.put(3,(short) (0));
		indices_I.put(4,(short) (2));
		indices_I.put(5,(short) (3));
		
		inactiveCoin.init(gl, vertices_I, texCoords_I, indices_I);
	}
	
	public void draw(GL10 gl) {
		if (active) {
			activeCoin.draw(gl);
		} else {
			inactiveCoin.draw(gl);
		}
	}
	
	public int changeActiveState() {
		active=!active;
		
		if (active) {
			return -1;
		}
		return 1;
	}

}
