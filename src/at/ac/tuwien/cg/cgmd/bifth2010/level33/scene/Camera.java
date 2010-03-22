package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import javax.microedition.khronos.opengles.GL10;
import static android.opengl.GLU.*;
import android.opengl.GLU;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.GameView;

public class Camera {
	
    float eyeX=0;
    float eyeY=0;
    float eyeZ=30;
	
    float viewX=0;
    float viewY=0;
    float viewZ=0;
    
    float upX=0;
    float upY=1;
    float upZ=0;

	public void update(GL10 gl) {
	        
        gl.glMatrixMode( GL10.GL_PROJECTION );
        gl.glLoadIdentity();
        
        
        eyeX=GameView.lastTouch.x;
        eyeY=GameView.lastTouch.y;

        
        float aspectRatio = (float)GameView.width / GameView.height;
        GLU.gluPerspective( gl, 45, aspectRatio, 1f, 100 );
        
    	gluLookAt(gl,eyeX, eyeY, eyeZ, viewX, viewY, viewZ, upX, upY, upZ  );// momentan nur zum probieren, danach von oben


	}

}
