package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.ParserAdapter;
import org.xml.sax.helpers.ParserFactory;
import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.opengl.GLDebugHelper;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.GLWrapper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Transformation;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class AboutActivity extends Activity {

	private static final String CLASS_TAG = AboutActivity.class.getName();
	private static final boolean FLAG_DEBUG_OPENGL = false;
	protected static final String EXTRA_MUSIC_ON = "music";

	private GLSurfaceView mGLSurfaceView=null;
	
	private long mLastTime = 0;
	private int mFrameCounter = 0; 
	
	ArrayList<Label3D> mLabels = new ArrayList<Label3D>(); 



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mGLSurfaceView = new GLSurfaceView(this);

		MyRenderer renderer = new MyRenderer();
		mGLSurfaceView.setRenderer(renderer);
		setContentView(mGLSurfaceView);


		if(FLAG_DEBUG_OPENGL) {

			mGLSurfaceView.setGLWrapper(new GLWrapper(){

				private Writer logger = new Writer(){
					String s = "";
					@Override
					public void close() throws IOException {
					}

					@Override
					public void flush() throws IOException {
						Log.d("OPENGL", s);	
						s = "";
					}

					@Override
					public void write(char[] buf, int offset, int count)
					throws IOException {
						s += new String(buf,offset,count);

					}
				};

				@Override
				public GL wrap(GL gl) {
					return GLDebugHelper.wrap(gl, GLDebugHelper.CONFIG_CHECK_GL_ERROR|GLDebugHelper.CONFIG_LOG_ARGUMENT_NAMES|GLDebugHelper.CONFIG_CHECK_THREAD, logger);			
				}
			});
		}

	}
	

	@Override
	protected void onResume() {
		super.onResume();
		if(mGLSurfaceView!=null)
			mGLSurfaceView.onResume();
		
		// TODO play music if sound is allowed
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mGLSurfaceView!=null)
			mGLSurfaceView.onPause();
		
		// TODO Pause music if it's running
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.l00_about, menu);
	    MenuItem menuItemCredits = menu.findItem(R.id.l00_credits);
	    menuItemCredits.setIntent(new Intent(this, CreditsActivity.class));
	    
	    return true;
	}
	
	private class MyRenderer implements GLSurfaceView.Renderer {

		float mAngle=0;
		Cylinder mCoin = null;
		private float mScroll = 0;
		private float mBannerHeight = 0;
		private long mLastFrame = 0;

		@Override
		public void onDrawFrame(GL10 gl) {
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			//camera transform
			gl.glTranslatef(0, 0, -3.0f);

			long now = System.currentTimeMillis();
			if(mLastFrame==0) {
				mLastFrame = now; 
			}
			long elapsedTime = mLastFrame  - now;
			mLastFrame =now;
			mScroll -= 0.3f * (float)elapsedTime * 0.001f;
			if(mScroll>mBannerHeight+10){
				mScroll=0;
			}
			
			gl.glRotatef(-40, 1, 0, 0);
			gl.glTranslatef(0, -3+mScroll, 0);
			
			gl.glPushMatrix();
			gl.glTranslatef(0, 1, 0);
			gl.glRotatef(mAngle, 0, 1, 0);
			mCoin.draw(gl);
			gl.glPopMatrix();
			
			gl.glColor4f(1,1,1,1);
			for(Label3D label:mLabels) {
				float fH = label.getHeight()*0.5f;
				gl.glTranslatef(0, -fH, 0);
				label.draw(gl);
				gl.glTranslatef(0, -fH, 0);
			}
			
			mAngle += 1.2f;
			
			mFrameCounter++;
			
			long elapsed = now-mLastTime; 
			if(elapsed > 3000){
				float millisPerFrame = (float)elapsed / (float)mFrameCounter;
				float fps = 1000.f / millisPerFrame;
				Log.d(CLASS_TAG, "fps: "+fps);
				mFrameCounter=0;
				mLastTime = System.currentTimeMillis();
			}
		}

		
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			//the glEnable(GL10.GL_TEXTURE_2D) call here fixes an android bug (textures are not correctly displayed after resume)
			//do not remove it!
			gl.glEnable(GL10.GL_TEXTURE_2D);
			
			
			gl.glViewport(0, 0, width, height);
			float ratio = (float) width / height;
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glFrustumf(-ratio, ratio, -1, 1, 1, 10);

		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			
			
			mLabels.clear();
			
			mCoin = new Cylinder(0.5f, 0.10f, (short) 10);
			mCoin.setTexture(gl, getResources(), R.drawable.l00_coin);
			mCoin.setColor(0.85f, 0.68f, 0.22f, 1.f);
			
			
			
			Paint p = new Paint();
			p.setARGB(255, 252, 252, 78);
			p.setAntiAlias(true);
			p.setTextScaleX(1.5f);
			p.setFakeBoldText(true);
			
			p.setTextSize(24);
			p.setTextAlign(Align.CENTER);
			
			String[] s1 = new String[14];
			s1[0]= getResources().getString(R.string.l00_nameofthegame_middle);
			s1[1]= getResources().getString(R.string.l00_about_01);
			s1[2]= getResources().getString(R.string.l00_about_02);
			s1[3]="";
			s1[4]= getResources().getString(R.string.l00_about_03);
			s1[5]= getResources().getString(R.string.l00_about_04);
			s1[6]= getResources().getString(R.string.l00_about_05);
			s1[7]= getResources().getString(R.string.l00_about_06);
			s1[8]= getResources().getString(R.string.l00_about_07);
			s1[9]= getResources().getString(R.string.l00_about_08);
			s1[10]= getResources().getString(R.string.l00_about_09);
			s1[11]="";
			s1[12]= getResources().getString(R.string.l00_about_10);
			s1[13]="";
			Label3D label = new Label3D(3.5f);
			label.setText(gl, p, s1, 6);
			mLabels.add(label);
			 
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

			gl.glClearColor(0,0,0,1);
			gl.glEnable(GL10.GL_CULL_FACE);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glEnable(GL10.GL_DEPTH_TEST);
			gl.glEnable(GL10.GL_TEXTURE_2D);

			float height = 0;
			for(Label3D l:mLabels) {
				height += l.getHeight();
			}
			
			mBannerHeight = height;

		}

	};


}