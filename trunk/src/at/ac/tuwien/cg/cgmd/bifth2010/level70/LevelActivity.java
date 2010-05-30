package at.ac.tuwien.cg.cgmd.bifth2010.level70;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer.RendererView;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.util.SoundManager;

/**
 * Level Activity.
 * 
 * @author Christoph Winklhofer
 */
public class LevelActivity extends Activity {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
    /** Instance for global access */
	private static LevelActivity instance;
	
	/** Renderer view */
	private RendererView rendererView;
	
	/** TextView to display the game complete and game over text */
	public TextView textview;	
	
	/** Handler needed to display the text */
	public Handler handler;
	
	/** Display game over text */
	public Runnable displayGameOver;
	
	/** Display game complete text */
	public Runnable displayGameComplete;
	
	
	// ----------------------------------------------------------------------------------
	// -- Static methods ----
	
	/**
	 * Return instance.
	 * @return LevelActivity instance
	 */
	public static LevelActivity getInstance() {
		return instance;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	/**
	 * Create level activity.
	 * @param Bundle.
	 */
	public void onCreate(Bundle savedInstanceState) {
		
	    displayGameOver = new Runnable() {
			public void run() {
			    textview.setText(R.string.l70_GameOver);
			    textview.setVisibility(View.VISIBLE);
			}
		};
		
		displayGameComplete = new Runnable() {
            public void run() {
                textview.setText(R.string.l70_GameComplete);
                textview.setVisibility(View.VISIBLE);
            }
        };
		
		// Set instance for global access
   		instance = this;
   		handler = new Handler();
   		
		super.onCreate(savedInstanceState);
		
		// Set fullscreen
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       
        // Get display
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display d = wm.getDefaultDisplay();
        
        // Create renderer view
   		rendererView = new RendererView(this, savedInstanceState, d.getWidth(), d.getHeight());
   		setContentView(rendererView);
   		
   		createTextLayout();
   		
   		// Create sound manager
   		SoundManager.initialize();
	}
	
	
	/**
	 * Set the game progress after the level is finished.
	 * @param progress The progress must be between 0 and 100.
	 */
	public void setStateProgress(int progress) {
        SessionState s = new SessionState();
        s.setProgress(progress);
        setResult(Activity.RESULT_OK, s.asIntent());
        finish();
    }
	
	
	// ----------------------------------------------------------------------------------
    // -- Protected methods ----
	
	/**
	 * {@inheritDoc}
	 */
	@Override
    protected void onStart() {
        super.onStart();
        rendererView.onStart();
    }
	
	/**
     * {@inheritDoc}
     */
	@Override
	protected void onResume() {
        super.onResume();
        rendererView.onResume();
    }

	
	/**
     * {@inheritDoc}
     */
    @Override
    protected void onPause() {
    	super.onPause();
        rendererView.onPause();
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onStop() {
    	super.onStop();
		rendererView.onStop();
    }

	
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onDestroy() {
		super.onDestroy();
		rendererView.onDestroy();
	}
    
    
    /**
     * {@inheritDoc}
     */
    @Override
	protected void onSaveInstanceState(Bundle outState) {
        rendererView.onPause();
    	rendererView.onSaveState(outState);
    }
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	rendererView.onRestoreState(savedInstanceState);
    }
    
    
    // ----------------------------------------------------------------------------------
    // -- Protected methods ----
    
    /**
     * Create the text layout to display the game complete /
     * game over text.
     */
    private void createTextLayout() {
        LinearLayout llayout = new LinearLayout(this);
        llayout.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT );
        addContentView(llayout, params);
        
        textview = new TextView(this);
        textview.setTextSize(30);
        textview.setGravity(Gravity.CENTER);
        textview.setTextColor(Color.BLACK);
        textview.setTypeface(Typeface.DEFAULT_BOLD);
        
        LinearLayout.LayoutParams llparams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        
        llparams.weight = 0;
        llparams.setMargins(10, 10, 10, 10);
        llayout.addView(textview, llparams);
        textview.setVisibility(View.INVISIBLE);
    }
}
