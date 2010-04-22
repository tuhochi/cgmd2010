package at.ac.tuwien.cg.cgmd.bifth2010.level42.scene;

import static android.opengl.GLES10.*;

import java.nio.FloatBuffer;

import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.OGLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level42.util.SceneLoader;

public class HUD
{
	private final FloatBuffer quad;
	private final OGLManager oglManager = OGLManager.instance;
	
	public HUD()
	{
		quad = SceneLoader.instance.arrayToBuffer(new float[]{
				100,200,0,		// lower left
				200,200,0,		// lower right
				100,100,0,		// upper left
				100,100,0,		// upper left
				200,200,0,		// lower right
				200,100,0		// upper right
		});
	}
	
	public void render()
	{
		int[] viewport = oglManager.getViewport();
		glColor4f(1, 0, 0, 1);
		oglManager.clientState(true, false, false);
		
		// push modelview
		glPushMatrix();
		
		// switch to projection
		glMatrixMode(GL_PROJECTION);
		
		// push projection
		glPushMatrix();
		
		// reset projection
		glLoadIdentity();
		
		// make projection ortho
		glOrthox(0, viewport[2], viewport[3], 0, 0, 1);
		
		// switch to modelview again
		glMatrixMode(GL_MODELVIEW);
		
		// reset modelview
		glLoadIdentity();
		
		// render a simple quad
		glVertexPointer(3, GL_FLOAT, 0, quad);
		glDrawArrays(GL_TRIANGLES, 0, 6);
		
		// switch to projection
		glMatrixMode(GL_PROJECTION);
		
		// pop projection
		glPopMatrix();
		
		// switch to modelview
		glMatrixMode(GL_MODELVIEW);
		
		// pop modelview
		glPopMatrix();
	}
}
