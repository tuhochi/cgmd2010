package at.ac.tuwien.cg.cgmd.bifth2010.level36;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level36.gamelogic.GameLogic;
import at.ac.tuwien.cg.cgmd.bifth2010.level36.rendering.GameView_New;
import at.ac.tuwien.cg.cgmd.bifth2010.level36.sound.SoundManager;

/**
 * 
 * @author Gruppe 36
 *
 */
public class LevelActivity extends Activity {
	//public static Paint paint;
	private GLSurfaceView mGLSurfaceView;
	private GameView_New gameView; 
	private Context context;
	private GameLogic gl;
	private Handler handler;
	private TextView output;
	private TextView time;
	private TextView money;
	private SoundManager sm;
	
	public void updateTextViews() {
		output.setText(this.gl.getOutputText()+" |");
		time.setText("| "+this.gl.getTimeText()+" |");
		money.setText("| "+this.gl.getMoneyText());
	}
	
	public void showNotification() {
		Toast toast = Toast.makeText(this.context, this.gl.getNotificationText(), Toast.LENGTH_SHORT);
		toast.show();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("LevelActivity::onCreate", "Start the program.");
		super.onCreate(savedInstanceState);
		context = this;

//		OLD
//		gameView = new GameView_New(this);
//		gameView.setFocusable(true);
//		setContentView(gameView);
		
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					LevelActivity.this.updateTextViews();
				}
				if (msg.what == 2) {
					LevelActivity.this.showNotification();
				}
			}
		};
		
		sm = new SoundManager(this, R.raw.l36_crumbling);
		gameView = new GameView_New(this, sm);
		gameView.setFocusable(true);
		FrameLayout fml = new FrameLayout(this);
		fml.setLayoutParams( new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT ) );
		fml.addView( gameView );
		setContentView(fml);
		
		LinearLayout linear = new LinearLayout(this);
        linear.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(linear, params);
        
        output = new TextView(this);
        output.setText("INFO |");
        output.setTextSize(25);
        output.setGravity(Gravity.LEFT);
        output.setTextColor(Color.RED );
        //typedIn.setText("HALLO1");
        
        time = new TextView(this);
        time.setText( " | TIME |" );
        time.setTextSize( 25 );
        time.setGravity( Gravity.LEFT );
        time.setTextColor( Color.BLUE );
        //remaining.setText("HALLO2");
        
        money = new TextView(this);
        money.setText("| COINS");
        money.setTextSize(25);
        money.setGravity(Gravity.RIGHT );
        money.setTextColor(Color.GREEN );
        //money.setText("HALLO3");
        
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams.weight = 0;
        llparams.setMargins( 0, 0, 0, 0);
        linear.addView( output, llparams);
        LinearLayout.LayoutParams llparams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams2.weight = 0;
        llparams2.setMargins( 0, 0, 0, 0);
        linear.addView( time, llparams );
        LinearLayout.LayoutParams llparams3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams3.weight = 0;
        llparams3.setMargins( 0, 0, 0, 0);
        linear.addView( money, llparams3 );
        
        this.gl = new GameLogic(this, gameView, sm, handler);
		//set fullscreen
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Window window = getWindow();
		//window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		/*setContentView(new GameView_New(this));
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(Color.RED);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(40);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));*/
	}
	
	@Override
	public void onStart() {
		if (!this.gl.isRunning()) {
			this.gl.start();
			
		}
		super.onStart();
	}
	
	@Override
	protected void onPause() {
		this.gl.setPaused(true);
		super.onPause();
	}
	
	@Override
	protected void onRestart() {
		this.gameView.getGameRenderer().resetRenderer();
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		this.gl.setPaused(false);
		SessionState quitState = new SessionState();
		quitState.setProgress( this.gl.getCoins() );
		setResult(Activity.RESULT_OK, quitState.asIntent());
		
		super.onResume();
	}
	
	@Override
	protected void onStop() {		
		SessionState quitState = new SessionState();
		quitState.setProgress( this.gl.getCoins() );
		setResult(Activity.RESULT_OK, quitState.asIntent());
		
		super.onStop();
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		this.gl.stop();
		this.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}
	
	public void quit ()
	{
		SessionState quitState = new SessionState();
		quitState.setProgress( 100-this.gl.getCoins() );
		setResult(Activity.RESULT_OK, quitState.asIntent());
		//this.sm.stopPlayer();
		this.finish();
	}
	
}