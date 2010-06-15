package at.ac.tuwien.cg.cgmd.bifth2010.level12;


import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;

import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * class creating, showing the game UI
 * @see android.view.LinearLayout
 * @see android.view.View.OnClickListener
 */
public class GameUI extends LinearLayout implements OnClickListener{
	
	private static TextView mIron; /** Textview showing how much iron the player has got */
	private static TextView mIrontxt;
	private static TextView mMoney; /** How much money has the player "lost" */
	private static TextView mMoneytxt;
	private static TextView mRoundsleft; /** rounds left before the game is finished */
	private static TextView mRoundslefttxt;
	private static TextView mCountdown; /** the countdown */
	private static TextView mCountdowntxt;
	private static ImageButton mBasicTowerButton; /** the basic tower button */
	private static ImageButton mAdvancedTowerButton; /** the advanced tower button */
	private static ImageButton mHyperTowerButton; /** the hyper tower button */
	private static ImageButton mFreezeTowerButton; /** the freeze tower button */
	private static final Handler mHandler = new Handler(); /** the handler which is responsible for updating the texts */
	
	private static GameUI mSingleton = null; /** the UI singleton */
	
	/** 
	 * Constructor, creates and initializes the TextViews and Buttons, the Handler responsible for the text's to update, and adds all to a LinearLayout
	 */
	private GameUI(Context context, int height, int width) {
		super(context);	
		int elementWidth = (int)width / 7;		
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
	    	
		mBasicTowerButton = new ImageButton( context );
	    mBasicTowerButton.setMaxHeight(height - 10);
	    mBasicTowerButton.setMaxWidth( elementWidth );
	    mBasicTowerButton.setId( 1 );
	    mBasicTowerButton.setImageResource(R.drawable.l12_bunny1_icon);
	    mBasicTowerButton.setOnClickListener(this);
	    mBasicTowerButton.setEnabled(false);
	    this.addView( mBasicTowerButton );
        
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
	
	/** creates the UI singleton with a specified width and height */
	public static void createSingleton( Context context, int height, int width ){
		mSingleton = new GameUI( context, height, width);
	}

	/** returns the UI Singleton */
	public static LinearLayout getSingleton() {
		return mSingleton;
	}
	
	/** setting the basic tower to active an the others to inactive */
	public void basicTowerButtonPressed(){
		mBasicTowerButton.isPressed();
		mBasicTowerButton.setEnabled(false);
		mAdvancedTowerButton.setEnabled(true);
		mHyperTowerButton.setEnabled(true);
		mFreezeTowerButton.setEnabled(true);
		GameMechanics.getSingleton().setBasicTowerSelected();
	}
	
	/** setting the advanced tower to active an the others to inactive */
	public void advancedTowerButtonPressed(){
		mAdvancedTowerButton.isPressed();
		mBasicTowerButton.setEnabled(true);
		mAdvancedTowerButton.setEnabled(false);
		mHyperTowerButton.setEnabled(true);
		mFreezeTowerButton.setEnabled(true);
		GameMechanics.getSingleton().setAdvancedTowerSelected();
	}
	
	/** setting the hyper tower to active an the others to inactive */
	public void hyperTowerButtonPressed(){
		mHyperTowerButton.isPressed();
		mBasicTowerButton.setEnabled(true);
		mAdvancedTowerButton.setEnabled(true);
		mHyperTowerButton.setEnabled(false);
		mFreezeTowerButton.setEnabled(true);
		GameMechanics.getSingleton().setHyperTowerSelected();
	}
	
	/** setting the freeze tower to active an the others to inactive */
	public void freezeTowerButtonPressed(){
		mFreezeTowerButton.isPressed();
		mBasicTowerButton.setEnabled(true);
		mAdvancedTowerButton.setEnabled(true);
		mHyperTowerButton.setEnabled(true);
		mFreezeTowerButton.setEnabled(false);
		GameMechanics.getSingleton().setFreezeTowerSelected();
	}
	
	/** a own thread updating the UI texts*/
	private static Runnable mTVUpdater = new Runnable(){
		public void run(){
			String it = new String();
			it=" "+GameMechanics.getSingleton().getIron()+" ";
			mIrontxt.setText(it);
			String mt = new String();
			mt=" "+(GameMechanics.getSingleton().calculateTotalMoney()-GameMechanics.getSingleton().getBurnedMoney())+" ";
			mMoneytxt.setText(mt);
			String ct = new String();
			if((Definitions.MAX_ROUND_NUMBER - GameMechanics.getSingleton().getRoundNumber() + 1) == 0) ct=" -- ";
			else ct=" "+GameMechanics.getSingleton().getRemainingWaitTime()+" ";
			mCountdowntxt.setText(ct);
			String rt = new String();
			rt=" "+String.valueOf(Definitions.MAX_ROUND_NUMBER - GameMechanics.getSingleton().getRoundNumber() +1)+" ";
			mRoundslefttxt.setText(rt);
		}
	};
	
	/** lets the handler update the UI text */
	public static void updateText(){
		mHandler.post( mTVUpdater );
	}
	
	/**
	 * The method inherited by the android.view.View.OnClickListener, checks which tower button is active and sets the towerID in the gamemechanics
	 */
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
