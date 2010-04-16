package at.ac.tuwien.cg.cgmd.bifth2010.level30;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLU;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level30.math.Vector2;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.Cube;

public class GameWorld {

	Cube cube;
	float mAngle = 0.0;
	
	public synchronized void Framemove()
	{
		
	}	

	public synchronized void Draw(GL10 gl)
	{		
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        /*
         * Now we're ready to draw some 3D objects
         */

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -3.0f);
        gl.glRotatef(mAngle,        0, 1, 0);
        gl.glRotatef(mAngle*0.25f,  1, 0, 0);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        cube.draw(gl, 0);

        gl.glRotatef(mAngle*2.0f, 0, 1, 1);
        gl.glTranslatef(0.5f, 0.5f, 0.5f);

        cube.draw(gl, 0);

        mAngle += 1.2f;
	}
	
	public synchronized void onSurfaceChanged(GL10 gl, int width, int height)
	{
		if(height == 0) { 						
			height = 1; 						
		}
		gl.glViewport(0, 0, width, height); 	
		gl.glMatrixMode(GL10.GL_PROJECTION); 	
		gl.glLoadIdentity(); 
		GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 200.0f);	
	}

	public synchronized void Init(GL10 gl)
	{		
		
		cube = new Cube();
		
	}
	

	public synchronized void InputDown(Vector2 pos)
	{
		
	}
	

	public synchronized void InputMove(Vector2 pos)
	{
		
	}
	

	public synchronized void InputUp(Vector2 pos)
	{
		
	}

	public synchronized void SetPause(boolean pause)
	{
	}

	public void onSaveInstanceState(Bundle outState)
	{

		
	}
	
}
