package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.GL_PROJECTION;
import static android.opengl.GLES10.glLoadIdentity;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLU.*;
import android.opengl.GLU;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.GameView;

public class Camera {
	
    float eyeX=0;
    float eyeY=0;
    float eyeZ=1.5f;
	
    float viewX=0;
    float viewY=0;
    float viewZ=0;
    
    float upX=0;
    float upY=1;
    float upZ=0;

    public void init(GL10 gl, int width, int height){
    	
		 float aspectRatio = (float)width / height;
	        GLU.gluPerspective( gl, 45, aspectRatio, 0.1f, 100 );
    	
    }
    
	public void lookAt(GL10 gl) {
      		
        eyeX=GameView.lastTouch.x;
        eyeY=GameView.lastTouch.y;
                
      	gluLookAt(gl,eyeX, eyeY, eyeZ, viewX, viewY, viewZ, upX, upY, upZ  );// momentan nur zum probieren, danach von oben

       

	}

}
