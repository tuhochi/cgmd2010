package at.ac.tuwien.cg.cgmd.bifth2010.level12;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ActivityInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;


/**
 * Activity which starts and ends the level
 * @see android.app.Activity
 */

public class LevelActivity extends Activity{
	
	private Display mDisplay = null; /** display to operate on */
	private GLRenderer mRenderer = null; /** OpenGL Renderer class */
	private boolean mTutShowed = false; /** Was the Tutorial dialog shown? */
	private LinearLayout mL; /** Layout which holds the GLSurfaceView and the GameUI */  
	private Context mContext = this; /** pointer to the level activity itself which is also the app context */  
	private Handler mFinish; /** Handler for showing the finish dialog and finishing the game */
	private boolean mFinished = false; /** is the game finished? */
	private int score = 0;
	
    /** Called when the activity is first created. 
     * Inherited from the android.app.Activiy class, sets the level to fullscreen and created the handler which gets called when the game is finished.
     * 
     *@param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Debug.startMethodTracing("Tracefile");
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);      
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if( mDisplay == null) mDisplay = wm.getDefaultDisplay(); 
        mFinish = new Handler() {
    	 	@Override
        	public void handleMessage(Message msg) {
        		FinishDialog fd = new FinishDialog(mContext);
        		
        		fd.setOnDismissListener(new OnDismissListener() {
    				@Override
    				public void onDismiss(DialogInterface resultdialog) {
    					finish();
    				}
    			});
        		fd.show();	
        	}
        };
        
    }
    
    /**
     * Inherited from android.app.Activity
     * Shows the Tutorial dialog
     */
    @Override
    public void onStart(){   
        super.onStart();
        if( mTutShowed == false ) {
        	GameMechanics.getSingleton().pause();
        	AlertDialog tutorial = new StartDialog(this);
        	tutorial.show();
        	 mTutShowed = true;	
        }     
    }
    
    /**
     * Inherited from android.app.Activity
     * Receives the touch events done by the user on the touchscreen and forwards them to the GameWorld singleton.
     * @param event the touch event
     */
    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			GameWorld.getSingleton().setXYpos((int)event.getX(), (int)(mDisplay.getHeight() - event.getY()));
			//Log.d(LOG_TAG, "width: " + d.getWidth() + " height: " + d.getHeight() + " standard y: " + event.getY());
            //Log.d(LOG_TAG, "screen --> x: " + (event.getX()) + "y: " + (d.getHeight() - event.getY()));
        }
    	return super.onTouchEvent(event);
    }
    
    
    /**
     * Inherited from android.app.Activity
     * Resumes the level activity by setting the sound to on/off depending on the framework boolean , 
     * setting the screen to landscape mode, setting the height for the GameUI and the GLSurfaceView,
     *  forwarding the display to the GameWorld, (re)creating the OpenGL VBOs, (re)loading the soundsamples, and initialising and showing the View (=Linear Layout).
     * 
     */
    @Override
    protected void onResume() {
    	SoundHandler.setContext(this);
    	
		Intent callingIntent = getIntent();
		SessionState state = new SessionState(callingIntent.getExtras());
		if(state!=null){
			boolean isMusicAndSoundOn = state.isMusicAndSoundOn(); 
			SoundHandler.getSingleton().setSound(isMusicAndSoundOn);
		}
    	
    	//SoundHandler.getSingleton().addResource(R.raw.l12_music);
    	TextureManager.getSingletonObject().initializeContext(this);
    	TextureManager.getSingletonObject().add(R.drawable.l12_icon);			
 
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE );
    	int fieldheight = (int)( mDisplay.getHeight() * 0.9 ) ;
    	int menuheight = mDisplay.getHeight() - fieldheight;
    	GameWorld.setDisplay( fieldheight, mDisplay.getWidth());
    	GameWorld.getSingleton().initVBOs();
    	SoundHandler.getSingleton().reloadSamples();
	
 	   	GLSurfaceView glview = new GLSurfaceView(this);
 	   	if( mRenderer == null ) mRenderer = new GLRenderer();
 	   	glview.setRenderer(mRenderer);
    	GameMechanics.getSingleton().setGameContext(this);
    	    
        mL = new LinearLayout( this );
        mL.setLayoutParams( new LayoutParams( LayoutParams.FILL_PARENT,   LayoutParams.FILL_PARENT));
        mL.setOrientation(LinearLayout.VERTICAL);
        GameUI.createSingleton( this, menuheight, mDisplay.getWidth() );
        mL.addView( GameUI.getSingleton() );
        mL.addView( glview );
        
        setContentView( mL );
    	//SoundHandler.getSingleton().playLoop(R.raw.l12_music);
        super.onResume();
    }
    
    /**
     * Inherited from android.app.Activity
     * Pausing the game by forwarting the pause call to the GameMechanics singleton and the SoundHandler singleton
     */
    @Override
    protected void onPause() {
    	GameMechanics.getSingleton().pause();
    	SoundHandler.getSingleton().stop();
        super.onPause();
    }
    
    /**
     * Inherited from android.app.Activity
     * Stopping the app
     */
    @Override
	protected void onStop() {
		//we finish this activity;
		super.onStop();
    	//Debug.stopMethodTracing();
    }
    
    
    /**
     * Method responsible for showing the finish dialog, sends an empty message to the Handler which handles creation, shows and dismisses the dialog
     */
    public void showFinishDialog(){
    	if( !mFinished ){
    		mFinished = true;
        	mFinish.sendEmptyMessage(0);
    	}
    }
    
    /**
     * Inherited from android.app.Activity
     * Finishes the level activity and returns the level result to the framework
     */
	@Override
	public void finish() {
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		int gainedMoney = GameMechanics.getSingleton().getMoney();
		int burnedMoney = GameMechanics.getSingleton().getBurnedMoney();
		float totalMoneyPercent = (gainedMoney )*0.01f;
		float progress = GameMechanics.getSingleton().getRoundNumber()/(float)(Definitions.MAX_ROUND_NUMBER+1);
		if(totalMoneyPercent == 0.0f) s.setProgress(0);
		else{
			score = (int)(burnedMoney/totalMoneyPercent);
			s.setProgress((int)(score));//burnedMoney/totalMoneyPercent
		}
		
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
		GameWorld.destroySingleton();
    	GameMechanics.destroySingleton();
    	SoundHandler.getSingleton().stop();
		super.finish();
		mDisplay = null;
	}
	
	
	/**
	 * Inherited from android.app.Activity
	 * Destroying the game context
	 */
	@Override
	public void onDestroy(){
	   	super.onDestroy();
	   	mDisplay = null;
	}
	
	    
	
	/**
	 * The Tutorial dialog, inherity from AlertDialog and implements an OnClickListener for the start button.
	 * @see android.app.AlertDialog
	 * @see  android.view.View.OnClickListener
	 */
	private class StartDialog extends AlertDialog implements android.view.View.OnClickListener{
		private Button mBtn; /** start button */
		
		/** Creates and shows the Layout */
		protected StartDialog(Context context) {
			super(context);
			this.setTitle(R.string.l12_tutorialtitle);
			this.setIcon(R.drawable.l12_icon);
			
			
			ScrollView sc = new ScrollView(context);
			LinearLayout l = new LinearLayout(context);
			l.setOrientation(LinearLayout.VERTICAL);
			
			TextView t = new TextView(context);
			t.setText(R.string.l12_intro);
			l.addView(t);
			
			LinearLayout li = new LinearLayout(context);
			li.setOrientation(LinearLayout.HORIZONTAL);
			ImageView i1 = new ImageView(context);
			i1.setImageResource(R.drawable.l12_bunny1_icon);
			li.addView(i1);
			TextView t1 = new TextView(context);
			t1.setText(R.string.l12_basic_tower);
			li.addView(t1);
			l.addView(li);
			
			LinearLayout lii = new LinearLayout(context);
			lii.setOrientation(LinearLayout.HORIZONTAL);
			ImageView i2 = new ImageView(context);
			i2.setImageResource(R.drawable.l12_bunny3_icon);
			lii.addView(i2);
			TextView t2 = new TextView(context);
			t2.setText(R.string.l12_advanced_tower);
			lii.addView(t2);
			l.addView(lii);
			
			LinearLayout liii = new LinearLayout(context);
			liii.setOrientation(LinearLayout.HORIZONTAL);
			ImageView i3 = new ImageView(context);
			i3.setImageResource(R.drawable.l12_bunny2_icon);
			liii.addView(i3);
			TextView t3 = new TextView(context);
			t3.setText(R.string.l12_hyper_tower);
			liii.addView(t3);
			l.addView(liii);
			
			LinearLayout liv = new LinearLayout(context);
			liv.setOrientation(LinearLayout.HORIZONTAL);
			ImageView i4 = new ImageView(context);
			i4.setImageResource(R.drawable.l12_bunny4_icon);
			liv.addView(i4);
			TextView t4 = new TextView(context);
			t4.setText(R.string.l12_freeze_tower);
			liv.addView(t4);
			l.addView(liv);
			
			mBtn = new Button(context);
			mBtn.setText("Start");
			mBtn.setId(1);
			mBtn.setOnClickListener(this);
			l.addView(mBtn);
				
			sc.addView(l);	
			this.setView(sc);
		}

		/** received the click events, starts the game (unpauses it) and dismisses the dialog */
		@Override
		public void onClick(View v) {
			if( v.getId() == mBtn.getId() ){
				GameMechanics.getSingleton().unpause();
				this.dismiss();	
			}
		}
	}
	
	/**
	 * Dialog shown at the end of the game, inherits from AlertDialog, implements an OnClickListener for the End button
	 * @see android.app.AlertDialog
	 * @see  android.view.View.OnClickListener
	 */
	private class FinishDialog  extends AlertDialog  implements android.view.View.OnClickListener{
		Button mBtn; /** end button */

		/**
		 * creates and shows the layout for the dialog
		 */
		protected FinishDialog(Context context) {
			super(context);
			this.setIcon(R.drawable.l12_icon);
			this.setTitle(R.string.l12_end_game);
			
			LinearLayout l = new LinearLayout(context);
			ScrollView sc = new ScrollView(context);
			l.setOrientation(LinearLayout.VERTICAL);
			
			TextView t = new TextView(context);
			t.setText( context.getString(R.string.l12_enemies_fended)+"		"+GameMechanics.getSingleton().getKilledEnemies() + "	 / 	"+GameMechanics.getSingleton().getSpawnedEnemies()+"\n" );
			l.addView(t);
			
			TextView t2 = new TextView(context);
			
			int gainedMoney = GameMechanics.getSingleton().getMoney();
			int burnedMoney = GameMechanics.getSingleton().getBurnedMoney();
			float totalMoneyPercent = gainedMoney*0.01f;
			if(totalMoneyPercent == 0.0f) score = 0;
			else score = (int)(burnedMoney/totalMoneyPercent);
			
			t2.setText( context.getString(R.string.l12_points)+"	"+score+ "%\n");
			l.addView(t2);
			
			
			LinearLayout l3 = new LinearLayout(context);
			ImageView ib = new ImageView(context);
			ib.setImageResource(R.drawable.l12_bunny1_icon);
			l3.addView(ib);
			TextView t4 = new TextView(context);
			t4.setText(context.getString(R.string.l12_built)+" "+GameMechanics.getSingleton().getBasicTowerBuilt()+"	\n");
			l3.addView(t4);

			ImageView ia = new ImageView(context);
			ia.setImageResource(R.drawable.l12_bunny3_icon);
			l3.addView(ia);
			TextView t5 = new TextView(context);
			t5.setText( context.getString(R.string.l12_built)+" "+GameMechanics.getSingleton().getAdvancedTowerBuilt()+"	\n");
			l3.addView(t5);
			l.addView(l3);
			
			LinearLayout l5 = new LinearLayout(context);
			ImageView ih = new ImageView(context);
			ih.setImageResource(R.drawable.l12_bunny2_icon);
			l5.addView(ih);
			TextView t6 = new TextView(context);
			t6.setText( context.getString(R.string.l12_built)+" "+GameMechanics.getSingleton().getHyperTowerBuild()+"	\n");
			l5.addView(t6);
		
			ImageView ig = new ImageView(context);
			ig.setImageResource(R.drawable.l12_bunny4_icon);
			l5.addView(ig);
			TextView t7 = new TextView(context);
			t7.setText( context.getString(R.string.l12_built)+" "+GameMechanics.getSingleton().getFreezeTowerBuilt()+"	\n");
			l5.addView(t7);
			l.addView(l5);
			
			TextView tsum = new TextView(context);
			int sum = GameMechanics.getSingleton().getBasicTowerBuilt() + GameMechanics.getSingleton().getAdvancedTowerBuilt() + GameMechanics.getSingleton().getHyperTowerBuild()+GameMechanics.getSingleton().getFreezeTowerBuilt();
			tsum.setText( context.getString(R.string.l12_you_built)+" "+sum+" "+context.getString(R.string.l12_towers_destroyed)+" "+GameMechanics.getSingleton().getTowerDestroyed()+" "+context.getString(R.string.l12_have_been)+" \n");
			l.addView(tsum);
			
			
			mBtn = new Button(context);
			mBtn.setText("End");
			mBtn.setId(1);
			mBtn.setOnClickListener( this );
			l.addView(mBtn);
			sc.addView(l);
			this.setView(sc);
			
		}
		
		/** receives and handles the click event on the button and dismisses the dialog */
		@Override
		public void onClick(View v) {
			if( v.getId() == mBtn.getId() ) this.dismiss();
		}
	}
		  
}
