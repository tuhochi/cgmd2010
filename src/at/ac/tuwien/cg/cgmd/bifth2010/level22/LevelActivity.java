package at.ac.tuwien.cg.cgmd.bifth2010.level22;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic.GameLogic;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.SpamRenderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.sound.SoundManager;;

/**
 *
 * The activity of Level 22. Manages all Layout associated processes as well as all the threads
 * attached to this activity
 * 
 * @author Sulix
 */
public class LevelActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		myThread = new Handler();
		
        inputManager = ( InputMethodManager ) getSystemService( Context.INPUT_METHOD_SERVICE );
        inputManager.toggleSoftInput( InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY );
	
		SpamRenderer renderer = new SpamRenderer( this );	
		FrameLayout embedLayout = new FrameLayout( this );
		embedLayout.setLayoutParams( new LayoutParams( LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT ) );
		embedLayout.addView( renderer );
		setContentView( embedLayout );
		
		LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        llayout.setOrientation(LinearLayout.HORIZONTAL);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(llayout, params);
        
        TextView typedIn = new TextView(this);
        typedIn.setText("typedIn");
        typedIn.setTextSize(25);
        typedIn.setGravity(Gravity.LEFT);
        typedIn.setTextColor(Color.RED );
        
        TextView remaining = new TextView(this);
        remaining.setText( "remaining" );
        remaining.setTextSize( 25 );
        remaining.setGravity( Gravity.LEFT );
        remaining.setTextColor( Color.BLUE );
        
        TextView money = new TextView(this);
        money.setText("money");
        money.setTextSize(25);
        money.setGravity(Gravity.RIGHT );
        money.setTextColor(Color.GREEN );
        
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams.weight = 0;
        llparams.setMargins( 0, 0, 0, 0);
        llayout.addView( typedIn, llparams);
        LinearLayout.LayoutParams llparams2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        llparams2.weight = 1;
        llparams2.setMargins( 10, 10, 0, 10);
        llayout.addView( remaining, llparams );
        llayout.addView( money, llparams2 );
		
        GameLogic.setTextViews( typedIn, remaining, money );
        GameLogic.setThread( myThread );
        if ( savedInstanceState != null )	{
        	GameLogic.init( savedInstanceState, this, 8000, 100, this );
        } else {	
        	GameLogic.init( null, this, 8000, 100, this );
        }
		
		gameThread = new GameLogic();
		MainThread.setVibrator( (Vibrator) getSystemService( Context.VIBRATOR_SERVICE ) );
		
		SoundManager.init( this );
		SoundManager.continueMusic();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#dispatchKeyEvent(android.view.KeyEvent)
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		
		int keycode = event.getKeyCode();
		
		if ( event.getAction() == KeyEvent.ACTION_UP )
		{
			
			/*KeyData results = new KeyData();
			event.getKeyData(results);
			return GameLogic.putChar( results.number );*/
			
			switch ( keycode )
			{
			
			case KeyEvent.KEYCODE_0 : return GameLogic.putChar( '0' );
			case KeyEvent.KEYCODE_1 : return GameLogic.putChar( '1' );
			case KeyEvent.KEYCODE_2 : return GameLogic.putChar( '2' );
			case KeyEvent.KEYCODE_3 : return GameLogic.putChar( '3' );
			case KeyEvent.KEYCODE_4 : return GameLogic.putChar( '4' );
			case KeyEvent.KEYCODE_5 : return GameLogic.putChar( '5' );
			case KeyEvent.KEYCODE_6 : return GameLogic.putChar( '6' );
			case KeyEvent.KEYCODE_7 : return GameLogic.putChar( '7' );
			case KeyEvent.KEYCODE_8 : return GameLogic.putChar( '8' );
			case KeyEvent.KEYCODE_9 : return GameLogic.putChar( '9' );
			case KeyEvent.KEYCODE_A : return GameLogic.putChar( 'a' );
			case KeyEvent.KEYCODE_B : return GameLogic.putChar( 'b' );
			case KeyEvent.KEYCODE_C : return GameLogic.putChar( 'c' );
			case KeyEvent.KEYCODE_D : return GameLogic.putChar( 'd' );
			case KeyEvent.KEYCODE_E : return GameLogic.putChar( 'e' );
			case KeyEvent.KEYCODE_F : return GameLogic.putChar( 'f' );
			case KeyEvent.KEYCODE_G : return GameLogic.putChar( 'g' );
			case KeyEvent.KEYCODE_H : return GameLogic.putChar( 'h' );
			case KeyEvent.KEYCODE_I : return GameLogic.putChar( 'i' );
			case KeyEvent.KEYCODE_J : return GameLogic.putChar( 'j' );
			case KeyEvent.KEYCODE_K : return GameLogic.putChar( 'k' );
			case KeyEvent.KEYCODE_L : return GameLogic.putChar( 'l' );
			case KeyEvent.KEYCODE_M : return GameLogic.putChar( 'm' );
			case KeyEvent.KEYCODE_N : return GameLogic.putChar( 'n' );
			case KeyEvent.KEYCODE_O : return GameLogic.putChar( 'o' );
			case KeyEvent.KEYCODE_P : return GameLogic.putChar( 'p' );
			case KeyEvent.KEYCODE_Q : return GameLogic.putChar( 'q' );
			case KeyEvent.KEYCODE_R : return GameLogic.putChar( 'r' );
			case KeyEvent.KEYCODE_S : return GameLogic.putChar( 's' );
			case KeyEvent.KEYCODE_T : return GameLogic.putChar( 't' );
			case KeyEvent.KEYCODE_U : return GameLogic.putChar( 'u' );
			case KeyEvent.KEYCODE_V : return GameLogic.putChar( 'v' );
			case KeyEvent.KEYCODE_W : return GameLogic.putChar( 'w' );
			case KeyEvent.KEYCODE_X : return GameLogic.putChar( 'x' );
			case KeyEvent.KEYCODE_Y : return GameLogic.putChar( 'y' );
			case KeyEvent.KEYCODE_Z : return GameLogic.putChar( 'z' );
			case KeyEvent.KEYCODE_AT : return GameLogic.putChar( '@' );
			case KeyEvent.KEYCODE_SPACE : return GameLogic.putChar( ' ' );
			case KeyEvent.KEYCODE_SOFT_LEFT : return GameLogic.putChar( '(' );
			case KeyEvent.KEYCODE_SOFT_RIGHT : return GameLogic.putChar( ')' );
			case KeyEvent.KEYCODE_BACK : quit(); finish();
			}
		}
		
		return super.dispatchKeyEvent(event);
	}

	private GameLogic gameThread;
	private Handler myThread;
	InputMethodManager inputManager;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {
		
		super.onPause();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {

		SpamRenderer.activate();
		
		super.onRestart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {

		SessionState quitState = new SessionState();
		quitState.setProgress( GameLogic.getMoneyState() );
		setResult(Activity.RESULT_OK, quitState.asIntent());
		
		super.onResume();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() 
	{
		
		inputManager.toggleSoftInput( InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY );
		
		if ( !GameLogic.isRunning() )	
		{
		
			gameThread = new GameLogic();
			gameThread.start();
		}
		
		super.onStart();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		
		SpamRenderer.deactivate();
		GameLogic.kill();
		
		SessionState quitState = new SessionState();
		quitState.setProgress( GameLogic.getMoneyState() );
		setResult(Activity.RESULT_OK, quitState.asIntent());
		
		super.onStop();
	}
	

	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		
		GameLogic.makePersistentState( outState );
		outState.putBoolean( "isLoadable", true );
		
		super.onSaveInstanceState(outState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() 
	{
		
		super.onDestroy();
	}
	
	/**
	 * Tells the application to save the current state and stop the game iteration. Call this method
	 * if you want to end the activity
	 */
	public void quit ()
	{
		
		SpamRenderer.deactivate();	
		SoundManager.uninit();
		GameLogic.kill();
		
		SessionState quitState = new SessionState();
		quitState.setProgress( GameLogic.getMoneyState() );
		setResult(Activity.RESULT_OK, quitState.asIntent());
	}
}


