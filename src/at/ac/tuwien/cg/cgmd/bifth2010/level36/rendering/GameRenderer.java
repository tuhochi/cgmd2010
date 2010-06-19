package at.ac.tuwien.cg.cgmd.bifth2010.level36.rendering;

import static android.opengl.GLES10.*;
import static android.opengl.GLES10Ext.*;
import static android.opengl.GLU.*;
import static android.opengl.GLUtils.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class GameRenderer implements GLSurfaceView.Renderer {
    private boolean mTranslucentBackground;

    private Plane mPlane;
    private Plane mPlane2;
    private Point mPoint;
    private Symbol mSymbol;
    private float mAngle;
    private InputStream is;
	private Context context;
	private int texId;
	private int texId2;
	private int texId3;
	private int texId4;
	private GL10 gl;

	private int randNumber;
	private boolean inActivePlane;
	private Bitmap bitmap1;
	private Bitmap backupBitmap1;
	private int clickedX = 0;
	private int clickedY = 0;
	
	private int viewWidth;
	private int viewHeight;
	private HashMap<String, float[]> hittedPoints;
	float[] mCheckA;
	float[] mCurrentMatrix;
	ByteBuffer mByteBuffer;
	FloatBuffer mFloatBuffer;
	private boolean onSurfaceCreated = false;
	private float perCentDiscovered;
	private boolean drawing;
	
	private float[] projectionMatrix;
	private float[] modelViewMatrix;
	
    public GameRenderer(boolean useTranslucentBackground, Context context, int initRandNumber) {
        this.mTranslucentBackground = useTranslucentBackground;
        this.context = context;
        this.randNumber = initRandNumber;
        this.inActivePlane = false;
        this.hittedPoints = new HashMap<String, float[]>();
        this.perCentDiscovered = 0f;
        this.drawing = true;
        this.projectionMatrix = new float[16];
        this.modelViewMatrix = new float[16];
        //this.cam = new Camera();
       	//this.cam.lookAt(0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
       
        //is = this.context.getResources().openRawResource(R.drawable.l36_los);
    }
    
    public void disableDrawing() {
    	this.drawing = false;
    }
    
    public float getPerCentDiscovered() {
    	return Math.round(this.perCentDiscovered*100);
    }
    
    public void resetRenderer() {
    	this.clickedX = 0;
    	this.clickedY = 0;
    	this.hittedPoints.clear();
    	this.bitmap1 = this.backupBitmap1.copy(Bitmap.Config.ARGB_4444, true);
    }
    
    public boolean getOnSurfaceCreated() {
    	return this.onSurfaceCreated;
    }
    
    public void setClickedXY(float x, float y) {
    	this.clickedX = (int) x;
    	this.clickedY = (int) y;
    }
    
    public float[] getCameraMatrix(	float eyeX, float eyeY, float eyeZ,
            						float centerX, float centerY, float centerZ, 
            						float upX, float upY, float upZ	) {
        // See the OpenGL GLUT documentation for gluLookAt for a description
        // of the algorithm. We implement it in a straightforward way:

        float fx = centerX - eyeX;
        float fy = centerY - eyeY;
        float fz = centerZ - eyeZ;

        // Normalize f
        float rlf = 1.0f / Matrix.length(fx, fy, fz);
        fx *= rlf;
        fy *= rlf;
        fz *= rlf;

        // Normalize up
        float rlup = 1.0f / Matrix.length(upX, upY, upZ);
        upX *= rlup;
        upY *= rlup;
        upZ *= rlup;

        // compute s = f x up (x means "cross product")

        float sx = fy * upZ - fz * upY;
        float sy = fz * upX - fx * upZ;
        float sz = fx * upY - fy * upX;

        // compute u = s x f
        float ux = sy * fz - sz * fy;
        float uy = sz * fx - sx * fz;
        float uz = sx * fy - sy * fx;

        float[] m = new float[16];
        m[0] = sx;
        m[1] = ux;
        m[2] = -fx;
        m[3] = 0.0f;

        m[4] = sy;
        m[5] = uy;
        m[6] = -fy;
        m[7] = 0.0f;

        m[8] = sz;
        m[9] = uz;
        m[10] = -fz;
        m[11] = 0.0f;

        m[12] = 0.0f;
        m[13] = 0.0f;
        m[14] = 0.0f;
        m[15] = 1.0f;
        return m;
    }

    
    public float[] project(	float objX, float objY, float objZ,
            			float[] model, int modelOffset, 
            			float[] project, int projectOffset,
            			int[] view, int viewOffset 
            			) {
        float[] m = new float[16];
        Matrix.multiplyMM(m, 0, project, projectOffset, model, modelOffset);

        float[] v = new float[4];

        v[0] = objX;
        v[1] = objY;
        v[2] = objZ;
        v[3] = 1.0f;

        float[] v2 = new float[4];

        Matrix.multiplyMV(v2, 0, m, 0, v, 0);

        float w = v2[3];
        if (w == 0.0f) {
            return null;
        }

        float rw = 1.0f / w;

        float[] win = new float[3];
        int winOffset = 0;
        win[winOffset] =
                view[viewOffset] + view[viewOffset + 2] * (v2[0] * rw + 1.0f)
                        * 0.5f;
        win[winOffset + 1] =
                view[viewOffset + 1] + view[viewOffset + 3]
                        * (v2[1] * rw + 1.0f) * 0.5f;
        win[winOffset + 2] = (v2[2] * rw + 1.0f) * 0.5f;
        
        //Log.d("xxxxxxxxx", win[0]+" "+win[1]+" "+win[2]);

        return win;
    }

    
    public int unProject(	float winX, float winY, float winZ,
            				float[] model, int modelOffset, 
            				float[] project, int projectOffset,
            				int[] view, int viewOffset, 
            				float[] obj, int objOffset	) {
        float[] pm = new float[16];
        Matrix.multiplyMM(pm, 0, project, projectOffset, model, modelOffset);

        float[] invPM = new float[16];
        if (!Matrix.invertM(invPM, 0, pm, 0)) {
            return GL10.GL_FALSE;
        }
        
//        float[] test = new float[16];
//        Matrix.multiplyMM(test, 0, pm, 0, invPM, 0);
//        Log.d("----", "-----");
//        for (int i = 0; i < 16; i++) {
//        	Log.d("AAA", ""+test[i]);
//        }

        float[] v = new float[4];

        v[0] = 2.0f * (winX - (float) view[viewOffset + 0]) / (float) view[viewOffset + 2] - 1.0f;
        v[1] = 2.0f * (winY - (float) view[viewOffset + 1]) / (float) view[viewOffset + 3] - 1.0f;
        v[2] = 2.0f * winZ - 1.0f;
        v[3] = 1.0f;

        float[] v2 = new float[4];

        Matrix.multiplyMV(v2, 0, invPM, 0, v, 0);
        
        obj[objOffset + 0] = v2[0];
        obj[objOffset + 1] = v2[1];
        obj[objOffset + 2] = v2[2];
        obj[objOffset + 3] = v2[3];
       
        //Log.d("xxxxxxx", obj[0]+" "+obj[1]+" "+obj[2]);
        //float[] tmp = new float[4];
        //float[] tmp2 = new float[4];
        //tmp2[0] = obj[0];
        //tmp2[1] = obj[1];
        //tmp2[2] = obj[2];
        //tmp2[3] = 1.0f;
        //Matrix.multiplyMV(tmp, 0, pm, 0, tmp2, 0);
        //Log.d("yyyyyyy", tmp[0]+" "+tmp[1]+" "+tmp[2]);

        return GL10.GL_TRUE;
    }

    
    public float[] getRubberArea() {
    	float[] model = getMatrix(GL_MODELVIEW);
    	float[] project = getMatrix(GL_PROJECTION);
    	
    	int[] view = new int[4];
    	view[0] = 0;
    	view[1] = 0;
    	view[2] = viewWidth;
    	view[3] = viewHeight;

    	//float[] objCoords1 = new float[4];
//    	Log.d("------", "-------");
//    	Log.d("###", ""+model.length);
//    	Log.d("###", ""+project.length);
//    	Log.d("###", ""+view.length);
//    	Log.d("###", ""+objCoords.length);
    	//unProject(289.0f, 85.0f, 0.0f, model, 0, project, 0, view, 0, objCoords1, 0);
    	
//    	try {
//    		gluUnProject(160.0f, 210.0f, 1.0f, model, 0, project, 0, view, 0, objCoords, 0);
//    	} catch (Exception e) {
//    		Log.d("xxxxxxxxx", e.toString());
//    	}
    	//Log.d("xxxxxxxxx", objCoords[0]+" "+objCoords[1]+" "+objCoords[2]);
    	//Log.d("------", "-------");
    	//float[] winCoords = new float[3];
    	
    	//float[] coords_X_Y = project(-5.0f, -5.0f, -0.2f, model, 0, project, 0, view, 0);
    	float[] coords_X_Y = project(-5.0f, -5.0f, -0.2f, this.modelViewMatrix, 0, this.projectionMatrix, 0, view, 0);
    	
//    	project(-5.0f, +5.0f, -0.2f, model, 0, project, 0, view, 0, winCoords, 0);
//    	float[] coords_XY = winCoords;
//    	project(+5.0f, -5.0f, -0.2f, model, 0, project, 0, view, 0, winCoords, 0);
//    	float[] coordsX_Y = winCoords;
    	
    	//float[] coordsXY = project(+5.0f, +5.0f, -0.2f, model, 0, project, 0, view, 0);
    	float[] coordsXY = project(+5.0f, +5.0f, -0.2f, this.modelViewMatrix, 0, this.projectionMatrix, 0, view, 0);
    	
    	float[] field = new float[4]; //minXminY,maxXmaxY Paar
    	//Log.d("++++++++", coords_X_Y[0]+" "+coordsXY[0]);
    	//Log.d("++++++++", coords_X_Y[1]+" "+coordsXY[1]);
    	if (coords_X_Y[0] < coordsXY[0]) {
    		field[0] = coords_X_Y[0];
    		field[2] = coordsXY[0];
    	} else {
    		field[0] = coordsXY[0];
    		field[2] = coords_X_Y[0];
    	}
    	if (coords_X_Y[1] < coordsXY[1]) {
    		field[1] = coords_X_Y[1];
    		field[3] = coordsXY[1];
    	} else {
    		field[1] = coords_X_Y[1];
    		field[3] = coordsXY[1];
    	}
    	return field;
    }
    
    public boolean isInArea(int x, int y, float[] area) {
    	//Log.d("area", area[0]+" "+area[1]+" "+area[2]+" "+area[3]);
    	//Log.d("aaaaaaa", this.clickedX+" "+this.clickedY);
    	if (x > area[0] && x < area[2] && y > area[1] && y < area[3]) {
			//Log.d("#########", "JUHU");
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public float[] calcHittedTexel(int x, int y, float[] area) {
    	int width = bitmap1.getWidth();
    	int height = bitmap1.getHeight();
    	float areaWidth = area[2]-area[0];
    	float areaHeight = area[3]-area[1];
    	//Log.d("area", area[0]+" "+area[1]+" "+area[2]+" "+area[3]);
    	
    	float widthRatio = height / areaWidth; 
    	float heightRatio = width / areaHeight;
    	//Log.d("Ratio", widthRatio+" "+heightRatio);
    	
    	float cleanedX = x - area[0];
    	float cleanedY = y - area[1];
    	
    	float cleanedXInTexel = cleanedX * widthRatio;
    	float cleanedYInTexel = cleanedY * heightRatio;
    	
    	float[] texelCoords = new float[2]; 
    	texelCoords[0] = height - cleanedXInTexel;
    	texelCoords[1] = width - cleanedYInTexel;
    	//Log.d("TexCoords", texelCoords[0]+" "+texelCoords[1]);
    	
    	return texelCoords;
    }
    
    public void setHittedPoint(int texCoordX, int texCoordY) {
    	String hittedPoint = texCoordX+"_"+texCoordY;
    	float[] tmp = new float[2];
    	tmp[0] = texCoordX;
    	tmp[1] = texCoordY;
    	if (!this.hittedPoints.containsKey(hittedPoint)) {
    		this.hittedPoints.put(hittedPoint, tmp);
    	}
    }
    
    public int getNumOfHittedPoints() {
    	return this.hittedPoints.size();
    }
    
    public int getNumOfTexels() {
    	return this.bitmap1.getWidth()*this.bitmap1.getHeight();
    }
    
    public boolean isDiscovered() {
    	perCentDiscovered = getNumOfTexels() != 0 ? (float)getNumOfHittedPoints() / (float)getNumOfTexels() : 0.0f;
////		Log.d("RATIO:", ""+perCentDiscovered);
////    	Log.d("HittedPoints:", ""+getNumOfHittedPoints());
////    	Log.d("NumOfTexels:", ""+getNumOfTexels());
    	if (perCentDiscovered > 0.036f) {
//    		//Log.d("JUHUJUHUJUHU", "---------");
    		return true;
    	} else {
//    		//Log.d("NOT_DISCOVERED", "---------");
    		return false;
    	}
    }
    
    public float[] getMatrix(int mMatrixMode) {
    	//GL_MODELVIEW GL10.GL_MODELVIEW
    	//GL_PROJECTION GL10.GL_PROJECTION
    	//GL_TEXTURE GL10.GL_TEXTURE
    	float[] mCurrent = new float[16];

	    int oesMode;
	    switch (mMatrixMode) {
	    case GL_MODELVIEW:
	    	//Log.d("IIIII", ""+GL11.GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES);
	        oesMode = GL11.GL_MODELVIEW_MATRIX_FLOAT_AS_INT_BITS_OES;
	        break;
	    case GL_PROJECTION:
	        oesMode = GL11.GL_PROJECTION_MATRIX_FLOAT_AS_INT_BITS_OES;
	        break;
	    case GL_TEXTURE:
	        oesMode = GL11.GL_TEXTURE_MATRIX_FLOAT_AS_INT_BITS_OES;
	        break;
	    default:
	        throw new IllegalArgumentException("Unknown matrix mode");
	    }
	    if ( mByteBuffer == null) {
	        //mCheckA = new float[16];
	        //mCheckB = new float[16];
	        mByteBuffer = ByteBuffer.allocateDirect(64);
	        mByteBuffer.order(ByteOrder.nativeOrder());
	        mFloatBuffer = mByteBuffer.asFloatBuffer();
	    }
	    
	    glGetIntegerv(oesMode, mByteBuffer.asIntBuffer());
	    
//	    if (mMatrixMode == GL_MODELVIEW) {
//	    	Log.d("-------", "ModelMatrix ");
//	    }
//	    if (mMatrixMode == GL_PROJECTION) {
//	    	Log.d("-------", "ProjectionMatrix");
//	    }
	    for(int i = 0; i < 16; i++) {
	    	//Log.d("#######", ""+mFloatBuffer.get(i));
	        mCurrent[i] = mFloatBuffer.get(i);
	    }
	    return mCurrent;
    }
       
    public void setRandNumber(int randNumber) {
    	this.randNumber = randNumber;
    	mSymbol.getNumber(randNumber);
    }
    
    public void setInactivePlane(boolean b) {
    	this.inActivePlane = b;
    }
    
    public void resetColor() {
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBindTexture(GL_TEXTURE_2D, this.texId);
        int w = bitmap1.getWidth();
        int h = bitmap1.getHeight();
        for (int i = 0; i < w; i++) {
        	for (int j = 0; j < h; j++) {
        		bitmap1.setPixel(i, j, Color.argb(255, 255, 0, 0));
        	}
        }
        texSubImage2D(GL_TEXTURE_2D, 0, 0, 0, bitmap1);
    }
    
    public void setColorXY(int xPos, int yPos) {
        //gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBindTexture(GL_TEXTURE_2D, this.texId);
        
        if ((xPos+5) > bitmap1.getHeight()) 
        	xPos = xPos-5;
        if ((xPos-5) < 0)
        	xPos = xPos+5;
        if ((yPos+5) > bitmap1.getWidth())
        	yPos = yPos-5;
        if ((yPos-5) < 0)
        	yPos = yPos+5;
        
        for (int i = xPos-5; i < xPos+5; i++) {
        	for (int j = yPos-5; j < yPos+5; j++) {
        		String hittedPoint = i+"_"+j;
        		if(!this.hittedPoints.containsKey(hittedPoint))
        			bitmap1.setPixel(i, j, Color.argb(0, 0, 0, 0));
        		setHittedPoint(i, j); //Zur Berechnung wie viel aufgedeckt ist
        	}
        }
        texSubImage2D(GL_TEXTURE_2D, 0, 0, 0, bitmap1);
    }
    
    public void onDrawFrame(GL10 gl) {		
        /*
         * Usually, the first thing one might want to do is to clear
         * the screen. The most efficient way of doing this is to use
         * glClear().
         */
    	//System.out.println(gl.getClass());  	
    	if (this.drawing) {
       
    	glEnable(GL_STENCIL_TEST);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);  
        //glClearStencil(0);
        
        /*
         * Now we're ready to draw some 3D objects
         */
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        //Camera
        //glMultMatrixf(this.cam.getMatrix().getArray(), 0);
        gluLookAt(gl, 0, 0, -20, 0, 0, 0, 0, 1, 0  ); 
//        getMatrix(GL_MODELVIEW);
        Matrix.setIdentityM(this.modelViewMatrix, 0);
        this.modelViewMatrix[0] = -1.0f;
        this.modelViewMatrix[5] = 1.0f;
        this.modelViewMatrix[10] = -1.0f;
        this.modelViewMatrix[14] = -20.0f;
        this.modelViewMatrix[15] = 1.0f;
        
     
        gl.glBlendFunc(GL_ONE, GL_ZERO);

        glEnableClientState(GL_VERTEX_ARRAY);
//        glEnableClientState(GL_COLOR_ARRAY);
        //gl.glClientActiveTexture(GL10.GL_TEXTURE0);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
        
//        glStencilFunc(GL_NEVER, 0x0, 0x0);
//        glStencilOp(GL_INCR, GL_INCR, GL_INCR);
          
        glBindTexture(GL_TEXTURE_2D, this.texId4);
        mPlane.draw(gl);
        
        glBindTexture(GL_TEXTURE_2D, this.texId2);
        mSymbol.getNumber(randNumber);
        mSymbol.draw(gl);
        
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        //gl.glBlendFunc(GL_DST_ALPHA, GL_ONE_MINUS_DST_ALPHA); //param1 = src color -> letzte farbe die in den buffer kommt param 2 = destcolor farbe im buffer
        float[] area = getRubberArea();
        boolean inArea = isInArea(this.clickedX, this.clickedY, area);
        if (inArea) {
        	float[] texelCoords = calcHittedTexel(this.clickedX, this.clickedY, area);
        	setColorXY((int)texelCoords[1], (int)texelCoords[0]); //Da die Textur um 90 Grad gedreht ist
        } else {
        	setColorXY(6, 6);
        }
        
//        glBindTexture(GL_TEXTURE_2D, this.texId);
//        int w = bitmap1.getWidth();
//        int h = bitmap1.getHeight();
//        for (int i = 0; i < w; i++) {
//        	for (int j = 0; j < h; j++) {
//        		bitmap1.setPixel(i, j, Color.argb(255, 255, 0, 0));
//        	}
//        }
//        texSubImage2D(GL_TEXTURE_2D, 0, 0, 0, bitmap1);    
        
//        if (!this.inActivePlane) {
//        	glBindTexture(GL_TEXTURE_2D, this.texId);
      	mPlane2.draw(gl);
//        }

        
//        glDisableClientState(GL_COLOR_ARRAY);
      	glDisableClientState(GL_TEXTURE_COORD_ARRAY);
      	glDisableClientState(GL_NORMAL_ARRAY);
//              
//      	glEnableClientState(GL_VERTEX_ARRAY);
//      	glEnableClientState(GL_COLOR_ARRAY);
////        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
//      	glEnableClientState(GL_NORMAL_ARRAY);
//        
////        gl.glBlendFunc(GL_ONE, GL_ZERO);
////        gl.glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
////        glStencilFunc(GL_NOTEQUAL, 0x1, 0x1);
////        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
//      	//gl.glBlendFunc(GL_ONE, GL_ONE);
//      	mPoint.draw(gl);
//        //float a = mPoint.getX() + 0.01f;
//        //float b = mPoint.getY() + 0.01f;
//        //mPoint.setXY(a, b);
//      	glDisableClientState(GL_COLOR_ARRAY);
////        glDisableClientState(GL_TEXTURE_COORD_ARRAY);
//        glDisableClientState(GL_NORMAL_ARRAY);
                    
        //glColor4f(1.0f, 1.0f, 1.0f, 0.0f);             
        //mCube.draw(gl);

//        gl.glRotatef(mAngle*2.0f, 0, 1, 1);
//        gl.glTranslatef(0.5f, 0.5f, 0.5f);
//
//        mPlane.draw(gl);
        //mCube.draw(gl);
        
        //getMatrix(GL_MODELVIEW);
      	

        
//        mAngle += 1.2f;
    	}
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    	glViewport(0, 0, width, height);
    	viewWidth = width;
    	viewHeight = height;
//    	if (glIsEnabled(GL_VIEWPORT)) {
//    		System.out.println("VIEWPORT IS ENABLED.");
//    		int[] view = new int[4];
//    		glGetIntegerv(GL_VIEWPORT, view, 0);
//    		for (int i = 0; i < view.length; i++) {
//    			System.out.println("View "+i+" "+view[i]);
//    		}
//    		
//    		IntBuffer ib = IntBuffer.allocate(4);
//    		glGetIntegerv(GL_VIEWPORT, view, 0);
//    		for (int i = 0; i < ib.array().length; i++) {
//    			System.out.println("View 2 "+i+" "+ib.get(i));
//    		}
//    	}

         /*
          * Set our projection matrix. This doesn't have to be done
          * each time we draw, but usually a new projection needs to
          * be set when the viewport is resized.
          */
        float ratio = (float) width / (float) height;
        //this.showNotification("Width: "+width + " Height: "+height + " Ratio: "+ratio);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        
        //gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        gluPerspective(gl, 45.0f, ratio, 0.9f, 100.0f);
        //gluOrtho2D(gl, -100.0f, 100.0f, -100.0f/ratio, 100.0f/ratio);
        Matrix.setIdentityM(this.projectionMatrix, 0);
        if (ratio < 1) {
	        this.projectionMatrix[0] = 3.244096f;
	        this.projectionMatrix[5] = 2.4142134f;
	        this.projectionMatrix[10] = -1.0181636f;
	        this.projectionMatrix[11] = -1.0f;
	        this.projectionMatrix[14] = -1.8163472f;
	        this.projectionMatrix[15] = 0.0f;
        } else {
	        this.projectionMatrix[0] = 1.357995f;
	        this.projectionMatrix[5] = 2.4142134f;
	        this.projectionMatrix[10] = -1.0181636f;
	        this.projectionMatrix[11] = -1.0f;
	        this.projectionMatrix[14] = -1.8163472f;
	        this.projectionMatrix[15] = 0.0f;
        }
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.mPlane = new Plane(1);
        this.mPlane2 = new Plane(2);
        this.mSymbol = new Symbol();
        this.mPoint = new Point(gl);
        /*
         * By default, OpenGL enables features that improve quality
         * but reduce performance. One might want to tweak that
         * especially on software renderer.
         */
        
        glDisable(GL_DITHER);
//        this.tm = new TextureManager(this.context);
//        this.texId = tm.loadTexture(gl, R.drawable.l36_los);

        /*
         * Some one-time OpenGL initialization can be made here
         * probably based on features of this particular context
         */
         glHint(GL_PERSPECTIVE_CORRECTION_HINT,
        		 GL_FASTEST);

         if (mTranslucentBackground) {
             gl.glClearColor(0,0,0,0);
         } else {
             gl.glClearColor(1,1,1,1);
         }
         gl.glEnable(GL_CULL_FACE);
         gl.glShadeModel(GL_SMOOTH);
         gl.glEnable(GL_DEPTH_TEST);
         gl.glEnable(GL_STENCIL_TEST);
         gl.glEnable(GL_TEXTURE_2D);
         gl.glEnable(GL_BLEND);
        
        InputStream is = this.context.getResources().openRawResource(R.drawable.l36_los_new);
 		//Bitmap bitmap = null;
 		try
 		{
 			bitmap1 = BitmapFactory.decodeStream(is);
 		}
 		catch(Throwable t)
 		{
 			//Log.e("TextureLoader::loadTexture", "Could not load Texture.");
 			bitmap1 = null;
 		}
 		finally
 		{
 			//Always clear and close
 			try
 			{
 				is.close();
 				is = null;
 			}
 			catch (IOException e)
 			{
 			}
 		}
 		
 		//bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true);
 		bitmap1 = bitmap1.copy(Bitmap.Config.ARGB_4444, true);
 		backupBitmap1 = bitmap1.copy(Bitmap.Config.ARGB_4444, true);
 		
 		int width = bitmap1.getWidth();
 		int height = bitmap1.getHeight();
 		//System.err.println("Loaded texture: " + width + "x" + height);

 		int[] textures = new int[1];
		glGenTextures(1, textures, 0);
		this.texId = textures[0];
		glBindTexture(GL_TEXTURE_2D, this.texId);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, android.opengl.GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
		texImage2D(GL_TEXTURE_2D, 0, bitmap1, 0);
		
 		int[] textures4 = new int[1];
		glGenTextures(1, textures4, 0);
		this.texId4 = textures4[0];
		glBindTexture(GL_TEXTURE_2D, this.texId4);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
		glTexParameterf(GL_TEXTURE_2D, android.opengl.GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
		texImage2D(GL_TEXTURE_2D, 0, bitmap1, 0);

         //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
         //GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
   	
         //bitmap.recycle();
    
         
         //2nd texture
        is = this.context.getResources().openRawResource(R.drawable.l36_font);
  		Bitmap bitmap = null;
  		try
  		{
  			bitmap = BitmapFactory.decodeStream(is);
  		}
  		catch(Throwable t)
  		{
  			//Log.e("TextureLoader::loadTexture", "Could not load Texture.");
  			bitmap = null;
  		}
  		finally
  		{
  			//Always clear and close
  			try
  			{
  				is.close();
  				is = null;
  			}
  			catch (IOException e)
  			{
  			}
  		}
  		
  		//bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true);
  		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
  		
  		width = bitmap.getWidth();
  		height = bitmap.getHeight();
  		System.err.println("Loaded texture: " + width + "x" + height);

  		int[] textures2 = new int[1];
 		glGenTextures(1, textures2, 0);
 		this.texId2 = textures2[0];
 		glBindTexture(GL_TEXTURE_2D, this.texId2);
 		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
 		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
 		glTexParameterf(GL_TEXTURE_2D, android.opengl.GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
 		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

          //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
          //GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
    	
          bitmap.recycle();
          
          
          
          //3nd texture
          is = this.context.getResources().openRawResource(R.drawable.l36_number);
    		bitmap = null;
    		try
    		{
    			bitmap = BitmapFactory.decodeStream(is);
    		}
    		catch(Throwable t)
    		{
    			//Log.e("TextureLoader::loadTexture", "Could not load Texture.");
    			bitmap = null;
    		}
    		finally
    		{
    			//Always clear and close
    			try
    			{
    				is.close();
    				is = null;
    			}
    			catch (IOException e)
    			{
    			}
    		}
    		
    		//bitmap = Bitmap.createScaledBitmap(bitmap, 1024, 1024, true);
    		bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);
    		
    		width = bitmap.getWidth();
    		height = bitmap.getHeight();
    		System.err.println("Loaded texture: " + width + "x" + height);

    		int[] textures3 = new int[1];
	   		glGenTextures(1, textures3, 0);
	   		this.texId3 = textures2[0];
	   		glBindTexture(GL_TEXTURE_2D, this.texId3);
	   		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	   		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST);
	   		glTexParameterf(GL_TEXTURE_2D, android.opengl.GLES11.GL_GENERATE_MIPMAP, GL_TRUE);
	   		texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);

            //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_GENERATE_MIPMAP, GL11.GL_TRUE);
            //GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
      	
            bitmap.recycle();
          
            this.onSurfaceCreated = true;
    }
    
    public Point getPoint() {
    	return this.mPoint;    	
    }

}
