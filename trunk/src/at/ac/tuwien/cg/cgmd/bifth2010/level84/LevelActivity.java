package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Bundle;

public class LevelActivity extends Activity {

Square sq = new Square();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GLSurfaceView view = new GLSurfaceView(this);
		view.setRenderer(new OpenGLRenderer());
		setContentView(view);
	}
	
	private class OpenGLRenderer implements Renderer{

		float rotation = 5.0f;
		
		@Override
		public void onDrawFrame(GL10 arg0) {
			arg0.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
			arg0.glColor4f(0.0f, 0.0f, 1.0f, 1.0f);
			arg0.glLoadIdentity();
			arg0.glTranslatef(0, 0, -4);
			arg0.glPushMatrix();
			if (rotation > 360.0f) rotation = 0;
			arg0.glRotatef(rotation, 0.0f, 1.0f, 0.0f);
			rotation += 10.0f;
			sq.draw(arg0);
			arg0.glPopMatrix();
			
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();	
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			gl.glClearColor(1.0f, 1.0f, 1.0f, 0.5f);
			//gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glClearDepthf(1.0f);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glDepthFunc(GL10.GL_LEQUAL);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
				
		}
	
		
		
	}
	
	private class Square {
		// Our vertices.
		private float vertices[] = {
			      -1.0f,  1.0f, 0.0f,  // 0, Top Left
			      -1.0f, -1.0f, 0.0f,  // 1, Bottom Left
			       1.0f, -1.0f, 0.0f,  // 2, Bottom Right
			       1.0f,  1.0f, 0.0f,  // 3, Top Right
			};

		// The order we like to connect them.
		private short[] indices = { 0, 1, 2, 0, 2, 3 };

		// Our vertex buffer.
		private FloatBuffer vertexBuffer;

		// Our index buffer.
		private ShortBuffer indexBuffer;

		public Square() {
			// a float is 4 bytes, therefore we multiply the number if
			// vertices with 4.
			ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
			vbb.order(ByteOrder.nativeOrder());
			vertexBuffer = vbb.asFloatBuffer();
			vertexBuffer.put(vertices);
			vertexBuffer.position(0);

			// short is 2 bytes, therefore we multiply the number if
			// vertices with 2.
			ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
			ibb.order(ByteOrder.nativeOrder());
			indexBuffer = ibb.asShortBuffer();
			indexBuffer.put(indices);
			indexBuffer.position(0);
		}
		/**
		 * This function draws our square on screen.
		 * @param gl
		 */
		public void draw(GL10 gl) {
			// Counter-clockwise winding.
			gl.glFrontFace(GL10.GL_CCW); // OpenGL docs
			// Enable face culling.
			gl.glEnable(GL10.GL_CULL_FACE); // OpenGL docs
			// What faces to remove with the face culling.
			gl.glCullFace(GL10.GL_BACK); // OpenGL docs

			// Enabled the vertices buffer for writing and to be used during
			// rendering.
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);// OpenGL docs.
			// Specifies the location and data format of an array of vertex
			// coordinates to use when rendering.
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, // OpenGL docs
	                                 vertexBuffer);

			gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,// OpenGL docs
					  GL10.GL_UNSIGNED_SHORT, indexBuffer);

			// Disable the vertices buffer.
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs
			// Disable face culling.
			gl.glDisable(GL10.GL_CULL_FACE); // OpenGL docs
		}
	}
	
}
