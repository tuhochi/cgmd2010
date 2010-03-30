package at.ac.tuwien.cg.cgmd.bifth2010.level88.util;

import javax.microedition.khronos.opengles.GL10;

import android.util.Log;

public class Quad {
	private Vector2 position;
	private Vector2 xDir;
	private Vector2 yDir;
	private Vertexbuffers vbos;
	
	public Quad(Vector2 _position, Vector2 _xDir, Vector2 _yDir) {
		position = _position;
		xDir = _xDir;
		yDir = _yDir;
		
		Vector2[] vertices = {
				new Vector2(position),
				Vector2.add(position, xDir),
				Vector2.add(Vector2.add(position, xDir), yDir),
				Vector2.add(position, yDir)
		};
		/*Short[] indices = {
				0, 1, 2,	2, 3, 0
		};*/
		Vector2[] texCoords = {
				new Vector2(0, 0),
				new Vector2(1, 0),
				new Vector2(1, 1),
				new Vector2(0, 1)
		};
		
		vbos = new Vertexbuffers();
		vbos.setData(Vertexbuffers.Type.POSITION, vertices);
		vbos.setData(Vertexbuffers.Type.TEX_COORD, texCoords);
		//vbos.setData(indices);
	}
	
	public void draw(GL10 gl) {
		vbos.set(gl);
		gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
	}
}
