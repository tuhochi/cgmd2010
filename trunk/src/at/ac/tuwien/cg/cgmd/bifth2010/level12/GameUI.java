package at.ac.tuwien.cg.cgmd.bifth2010.level12;


import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;

import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


public class GameUI extends LinearLayout implements OnClickListener{
	
	private Context mContext = null;
	//private static TextView mTV;
	private static TextView mIron;
	private static TextView mIrontxt;
	private static TextView mMoney;
	private static TextView mMoneytxt;
	private static TextView mRoundsleft;
	private static TextView mRoundslefttxt;
	private static TextView mCountdown;
	private static TextView mCountdowntxt;
	private static ImageButton mBasicTowerButton;
	//private static TextView mBasicTowerTextView;
	private static ImageButton mAdvancedTowerButton;
	private static ImageButton mHyperTowerButton;
	private static ImageButton mFreezeTowerButton;
	private static final Handler mHandler = new Handler();
	
	private static GameUI mSingleton = null;
	
	private GameUI(Context context, int height, int width) {
		super(context);	
		mContext = context;
		int elementWidth = (int)width / 7;		
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
	    	
		//LinearLayout ll = new LinearLayout( mContext );
		//ll.setGravity(Gravity.CENTER_HORIZONTAL);
		//ll.setOrientation(VERTICAL);
		mBasicTowerButton = new ImageButton( context );
	    mBasicTowerButton.setMaxHeight(height - 10);
	    mBasicTowerButton.setMaxWidth( elementWidth );
	    mBasicTowerButton.setId( 1 );
	    mBasicTowerButton.setImageResource(R.drawable.l12_bunny1_icon);
	    mBasicTowerButton.setOnClickListener(this);
	    mBasicTowerButton.setEnabled(false);
	    this.addView( mBasicTowerButton );
	    //mBasicTowerTextView = new TextView(mContext);
	    //mBasicTowerTextView.setText(GameMechanics.getSingleton().getPossibleBasicTowerCount());
	    //ll.addView(mBasicTowerTextView);
	    //this.addView(ll);
	        
	    mAdvancedTowerButton = new ImageButton( context );
	    mAdvancedTowerButton.setMaxHeight(height);
	    mAdvancedTowerButton.setMaxWidth( elementWidth );
	    mAdvancedTowerButton.setImageResource(R.drawable.l12_bunny3_icon);
	    mAdvancedTowerButton.setId( 2 );
	    mAdvancedTowerButton.setOnClickListener(this);
	    this.addView( mAdvancedTowerButton );
	        
	    mHyperTowerButton = new ImageButton( context );
	    mHyperTowerButton.setMaxHeight(height);
	    mHyperTowerButton.setMaxWidth( elementWidth );
	    mHyperTowerButton.setImageResource(R.drawable.l12_bunny2_icon);
	    mHyperTowerButton.setId( 3 );
	    mHyperTowerButton.setOnClickListener(this);
	    this.addView( mHyperTowerButton );
	    
	    mFreezeTowerButton = new ImageButton( context );
	    mFreezeTowerButton.setMaxHeight(height);
	    mFreezeTowerButton.setMaxWidth( elementWidth );
	    mFreezeTowerButton.setImageResource(R.drawable.l12_bunny4_icon);
	    mFreezeTowerButton.setId( 4 );
	    mFreezeTowerButton.setOnClickListener(this);
	    this.addView( mFreezeTowerButton );
	        
	    //mTV = new TextView( context );
	    //mTV.setHeight( height );
	    //updateText();
	    //this.addView(mTV);
	    LinearLayout ltxt = new LinearLayout(context);
	    ltxt.setOrientation(LinearLayout.VERTICAL);
	    
	    LinearLayout li = new LinearLayout(context);
	    li.setOrientation(LinearLayout.HORIZONTAL);
	    
	    mIron = new TextView(context);
	    mIron.setText(R.string.l12_iron);
	    mIrontxt = new TextView(context);
	    li.addView(mIron);
	    li.addView(mIrontxt);
	    
	    mMoney = new TextView(context);
	    mMoney.setText(R.string.l12_points);
	    mMoneytxt = new TextView(context);
	    li.addView(mMoney);
	    li.addView(mMoneytxt);
	    
	    
	    LinearLayout lr = new LinearLayout(context);
	    lr.setOrientation(LinearLayout.HORIZONTAL);
	    
	    mRoundsleft = new TextView(context);
	    mRoundsleft.setText(R.string.l12_roundsleft);
	    mRoundslefttxt = new TextView(context);
	    lr.addView(mRoundsleft);
	    lr.addView(mRoundslefttxt);
	    
	    mCountdown = new TextView(context);
	    mCountdown.setText(R.string.l12_countdown);
	    mCountdowntxt = new TextView(context);
	    lr.addView(mCountdown);
	    lr.addView(mCountdowntxt);
	    
	    ltxt.addView(li);
	    ltxt.addView(lr);
	    
	    this.addView(ltxt);
	    
	    updateText();
	}
	
	public static void createSingleton( Context context, int height, int width ){
		mSingleton = new GameUI( context, height, width);
	}

	public static LinearLayout getSingleton() {
		return mSingleton;
	}
	
	
	public void basicTowerButtonPressed(){
		mBasicTowerButton.isPressed();
		mBasicTowerButton.setEnabled(false);
		mAdvancedTowerButton.setEnabled(true);
		mHyperTowerButton.setEnabled(true);
		mFreezeTowerButton.setEnabled(true);
		GameMechanics.getSingleton().setBasicTowerSelected();
	}
	
	public void advancedTowerButtonPressed(){
		mAdvancedTowerButton.isPressed();
		mBasicTowerButton.setEnabled(true);
		mAdvancedTowerButton.setEnabled(false);
		mHyperTowerButton.setEnabled(true);
		mFreezeTowerButton.setEnabled(true);
		GameMechanics.getSingleton().setAdvancedTowerSelected();
	}
	
	public void hyperTowerButtonPressed(){
		mHyperTowerButton.isPressed();
		mBasicTowerButton.setEnabled(true);
		mAdvancedTowerButton.setEnabled(true);
		mHyperTowerButton.setEnabled(false);
		mFreezeTowerButton.setEnabled(true);
		GameMechanics.getSingleton().setHyperTowerSelected();
	}
	
	public void freezeTowerButtonPressed(){
		mFreezeTowerButton.isPressed();
		mBasicTowerButton.setEnabled(true);
		mAdvancedTowerButton.setEnabled(true);
		mHyperTowerButton.setEnabled(true);
		mFreezeTowerButton.setEnabled(false);
		GameMechanics.getSingleton().setFreezeTowerSelected();
	}
	
	private static Runnable mTVUpdater = new Runnable(){
		public void run(){
			/*mTV.setText( 
				FPSCounter.getSingleton().getFPS()+" FPS " +
				"Money: "+GameMechanics.getSingleton().getMoney()+" " +
				"Iron: "+GameMechanics.getSingleton().getIron()+" "+
				"Rounds left: "+(Definitions.MAX_ROUND_NUMBER - GameMechanics.getSingleton().getRoundNumber()) +" " +
				" Countdown: "+(int)(GameMechanics.getSingleton().getRemainingWaitTime()));
			//mBasicTowerTextView.setText(GameMechanics.getSingleton().getPossibleBasicTowerCount());*/
			String it = new String();
			it=" "+GameMechanics.getSingleton().getIron()+" ";
			mIrontxt.setText(it);
			String mt = new String();
			mt=" "+GameMechanics.getSingleton().getBurnedMoney()+" ";
			mMoneytxt.setText(mt);
			String ct = new String();
			ct=" "+GameMechanics.getSingleton().getRemainingWaitTime()+" ";
			mCountdowntxt.setText(ct);
			String rt = new String();
			rt=" "+String.valueOf(Definitions.MAX_ROUND_NUMBER - GameMechanics.getSingleton().getRoundNumber())+" ";
			mRoundslefttxt.setText(rt);
		}
	};
	
	public static void updateText(){
		mHandler.post( mTVUpdater );
	}
	

	@Override
	public void onClick(View v) {
		if( v.getId() == mBasicTowerButton.getId() ){
			basicTowerButtonPressed();
		} else if( v.getId() == mAdvancedTowerButton.getId() ){
			advancedTowerButtonPressed();
		} else if( v.getId() == mHyperTowerButton.getId() ){
			hyperTowerButtonPressed();
		} else if( v.getId() == mFreezeTowerButton.getId() ){
			freezeTowerButtonPressed();
		}
	}

	

}
