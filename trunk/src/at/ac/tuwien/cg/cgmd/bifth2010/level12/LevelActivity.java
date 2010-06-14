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


public class LevelActivity extends Activity{
	private Display mDisplay = null;
	private GLRenderer mRenderer = null;
	private boolean mTutShowed = false;
	private LinearLayout mL;
	private Context mContext = this;
	private Handler mFinish;
	private boolean mFinished = false;
	
	
    /** Called when the activity is first created. */
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
    
    
    
    @Override
    protected void onResume() {
    	SoundHandler.setContext(this);
    	
		Intent callingIntent = getIntent();
		SessionState state = new SessionState(callingIntent.getExtras());
		if(state!=null){
			boolean isMusicAndSoundOn = state.isMusicAndSoundOn(); 
			SoundHandler.getSingleton().setSound(isMusicAndSoundOn);
			System.out.println("SETTING SOUND TO: "+isMusicAndSoundOn);
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
    
    
    @Override
    protected void onPause() {
    	GameMechanics.getSingleton().pause();
    	SoundHandler.getSingleton().stop();
        super.onPause();
    }
    
    @Override
	protected void onStop() {
		//we finish this activity;
		super.onStop();
    	//Debug.stopMethodTracing();
    }
    
    public void showFinishDialog(){
    	if( !mFinished ){
    		mFinished = true;
        	mFinish.sendEmptyMessage(0);
    	}
    }
    
	@Override
	public void finish() {
		System.out.println("FINISH");
		
		System.out.println("REMOVE VIEWS");
		//mL.removeAllViews();
		System.out.println("REMOVE VIEWS1");
		//mL.removeAllViewsInLayout();
		System.out.println("REMOVE VIEWS2");
		
		/*TextView t = new TextView(this);
		t.setText(R.string.l12_enemies_fended);
		mL.addView(t);
		TextView t1 = new TextView(this);
		t1.setText(mKilledEnemies + " / "+mSpawnedEnemies+"\n\n" );
		mL.addView(t1);
		this.setContentView(mL);
		System.out.println("Content View Set!");*/
		
		//the SessionState is a convenience class to set a result
		SessionState s = new SessionState();
		//we set the progress the user has made (must be between 0-100)
		int gainedMoney = GameMechanics.getSingleton().getMoney();
		int burnedMoney = GameMechanics.getSingleton().getBurnedMoney();
		float totalMoneyPercent = (gainedMoney + burnedMoney)*0.01f;
		if(totalMoneyPercent == 0.0f) s.setProgress(0);
		else s.setProgress((int)(burnedMoney/totalMoneyPercent));
		
		//System.out.println("burned: " + burnedMoney + " |gained: " + gainedMoney + "score: " + (int)(burnedMoney/totalMoneyPercent));
		//we call the activity's setResult method 
		setResult(Activity.RESULT_OK, s.asIntent());
		GameWorld.destroySingleton();
    	GameMechanics.destroySingleton();
    	SoundHandler.getSingleton().stop();
		super.finish();
		mDisplay = null;
	}
	
	@Override
	public void onDestroy(){
	   	super.onDestroy();
	   	mDisplay = null;
	}
	
	    
	
	
	private class StartDialog extends AlertDialog implements android.view.View.OnClickListener{

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
			
			Button btn = new Button(context);
			btn.setText("Start");
			btn.setId(1);
			btn.setOnClickListener(this);
			l.addView(btn);
				
			sc.addView(l);	
			this.setView(sc);
		}

		@Override
		public void onClick(View v) {
			GameMechanics.getSingleton().unpause();
			this.dismiss();	
		}
	}
	
	private class FinishDialog  extends AlertDialog  implements android.view.View.OnClickListener{
		Button mBtn;

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
			float proz = (gainedMoney + burnedMoney)*0.01f; 
			t2.setText( context.getString(R.string.l12_points)+"	"+proz+ "	 /		100\n");
			l.addView(t2);
			
			
			LinearLayout l3 = new LinearLayout(context);
			ImageView ib = new ImageView(context);
			ib.setImageResource(R.drawable.l12_bunny1_icon);
			l3.addView(ib);
			TextView t4 = new TextView(context);
			t4.setText(context.getString(R.string.l12_built)+" "+GameMechanics.getSingleton().getBasicTowerBuilt()+"	\n");
			l3.addView(t4);

			ImageView ia = new ImageView(context);
			ia.setImageResource(R.drawable.l12_bunny2_icon);
			l3.addView(ia);
			TextView t5 = new TextView(context);
			t5.setText( context.getString(R.string.l12_built)+" "+GameMechanics.getSingleton().getAdvancedTowerBuilt()+"	\n");
			l3.addView(t5);
			l.addView(l3);
			
			LinearLayout l5 = new LinearLayout(context);
			ImageView ih = new ImageView(context);
			ih.setImageResource(R.drawable.l12_bunny3_icon);
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

		@Override
		public void onClick(View v) {
			if( v.getId() == mBtn.getId() ) this.dismiss();
		}
	}
		  
}
