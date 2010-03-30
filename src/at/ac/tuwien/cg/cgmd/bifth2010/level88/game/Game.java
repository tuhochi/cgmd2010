package at.ac.tuwien.cg.cgmd.bifth2010.level88.game;

import java.util.Date;

import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.opengl.GLU;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Textures;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Vector2;
import at.ac.tuwien.cg.cgmd.bifth2010.level88.util.Quad;

import javax.microedition.khronos.opengles.GL10;

public class Game {
	private long newTime, oldTime;
	private float elapsedSeconds;
	private int screenWidth, screenHeight;
	private float screenWidthScale, screenHeightScale;
	private Quad quad;
	private Context context;
	private Textures textures;
	private Vector2 cameraPos;
	private float color; // TODO
	
	public boolean newTouch;
	public Vector2 touchPosition;

	public Game(Context _context) {
		context = _context;
		cameraPos = new Vector2();
		
		newTouch = false;
		touchPosition = new Vector2();
		
		color = 0;

		Date date = new Date();
        newTime = date.getTime();
        oldTime = newTime;
        elapsedSeconds = 0;
        
        quad = new Quad(new Vector2(-0.5f, -0.5f), new Vector2(1.0f, 0.0f), new Vector2(0.0f, 1.0f));
	}

	public synchronized boolean hasNewInput() {
		boolean old = newTouch;
		newTouch = false;
		return old;
	}
	
	public synchronized void fingerDown(Vector2 pos) {
		touchPosition = new Vector2(pos);
		newTouch = true;
	}
	
	public synchronized void fingerUp(Vector2 pos) {
		touchPosition = new Vector2(pos);
		newTouch = true;
	}
	
	public synchronized void fingerMove(Vector2 pos) {
		touchPosition = new Vector2(pos);
		newTouch = true;
	}
	
	public synchronized void init(GL10 gl) {
    	gl.glClearColor(0, 0, 0, 1.0f);

    	gl.glEnable(GL10.GL_TEXTURE_2D);			//Enable Texture Mapping
		gl.glShadeModel(GL10.GL_SMOOTH); 			//Enable Smooth Shading
		gl.glClearDepthf(1.0f); 					//Depth Buffer Setup
		gl.glEnable(GL10.GL_DEPTH_TEST); 			//Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); 			//The Type Of Depth Testing To Do
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		textures = new Textures(gl, context);
		textures.addTexture(R.drawable.l88_greenstar);
		textures.loadTextures();
		
		//Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
	}
	
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		screenWidth = width;
		screenHeight = height;
		gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        if( width > height ) {
        	screenWidthScale = (float)width/(float)height;
        	screenHeightScale = 1;
        }
        else {
        	screenHeightScale = (float)height/(float)width;
        	screenWidthScale = 1;
        }
        GLU.gluOrtho2D(gl, -screenWidthScale, screenWidthScale, screenHeightScale, -screenHeightScale);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
	}
	
	public synchronized void update() {
		Date date = new Date();
        oldTime = newTime;
		newTime = date.getTime();
        elapsedSeconds = (newTime - oldTime) / 1000.0f;
        
        if( hasNewInput() ) {
        	cameraPos = new Vector2(touchPosition);
        	
        	cameraPos.mult(2);
        	cameraPos.add(new Vector2(-1,-1));
        	cameraPos.mult(-1);

        	cameraPos.x *= screenWidthScale;
        	cameraPos.y *= screenHeightScale;
        	
        	float angle = cameraPos.getAngle();
        	angle /= Math.PI;
        	angle *= 180;
        	while( angle > 90 ) angle -= 90;
        	if( angle > 30 && angle < 60 ) {
        		color = 1;
        	}
        	else {
        		color = 0;
        	}
        }
	}

	public synchronized void draw(GL10 gl) {
		gl.glClearColor(color, color, color, 1);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        
        gl.glLoadIdentity();
        gl.glTranslatef(-cameraPos.x, -cameraPos.y, -1f);
        
        textures.bind(R.drawable.l88_greenstar);
        quad.draw(gl);

	}
}
