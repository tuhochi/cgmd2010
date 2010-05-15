package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameUI extends LinearLayout implements OnClickListener{
	

	private static TextView mTV;
	private static TextView mFPSTV;
	private static TextView mGoldTV;
	private static TextView mCountdownTV;
	private static TextView mRoundsLeftTV;
	private static ImageButton mBasicTowerButton;
	private static ImageButton mAdvancedTowerButton;
	private static ImageButton mHyperTowerButton;
	
	private static GameUI mSingleton = null;
	
	private GameUI(Context context, int height, int width) {
		super(context);	
		int elementWidth = (int)width / 7;		
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_HORIZONTAL);
	       
		mBasicTowerButton = new ImageButton( context );
	    mBasicTowerButton.setMinimumHeight(height);
	    mBasicTowerButton.setMaxHeight(height);
	    mBasicTowerButton.setMinimumWidth( elementWidth );
	    mBasicTowerButton.setMaxWidth( elementWidth );
	    mBasicTowerButton.setImageResource(R.drawable.l12_basic_tower);
	    mBasicTowerButton.setId( 1 );
	    mBasicTowerButton.setOnClickListener(this);
	    this.addView( mBasicTowerButton );
	        
	    mAdvancedTowerButton = new ImageButton( context );
	    mAdvancedTowerButton.setMinimumHeight(height);
	    mAdvancedTowerButton.setMaxHeight(height);
	    mAdvancedTowerButton.setMinimumWidth( elementWidth );
	    mAdvancedTowerButton.setMaxWidth( elementWidth );
	    mAdvancedTowerButton.setImageResource(R.drawable.l12_advanced_tower);
	    mAdvancedTowerButton.setId( 2 );
	    mAdvancedTowerButton.setOnClickListener(this);
	    this.addView( mAdvancedTowerButton );
	        
	    mHyperTowerButton = new ImageButton( context );
	    mHyperTowerButton.setMinimumHeight(height);
	    mHyperTowerButton.setMaxHeight(height);
	    mHyperTowerButton.setMinimumWidth( elementWidth );
	    mHyperTowerButton.setMaxWidth( elementWidth );
	    mHyperTowerButton.setImageResource(R.drawable.l12_hyper_tower);
	    mHyperTowerButton.setId( 3 );
	    mHyperTowerButton.setOnClickListener(this);
	    this.addView( mHyperTowerButton );
	        
	    mTV = new TextView( context );
	    mTV.setHeight( height );
	    updateText();
	    basicTowerButtonPressed();
	    this.addView(mTV);
	}
	
	public static void createSingleton( Context context, int height, int width ){
		mSingleton = new GameUI( context, height, width);
	}

	public static LinearLayout getSingleton() {
		return mSingleton;
	}
	
	
	public void basicTowerButtonPressed(){
		mBasicTowerButton.setPressed(true);
		mAdvancedTowerButton.setPressed(false);
		mHyperTowerButton.setPressed(false);
		mBasicTowerButton.setColorFilter(Color.DKGRAY, Mode.MULTIPLY);
		mAdvancedTowerButton.setColorFilter(Color.WHITE, Mode.MULTIPLY);
		mHyperTowerButton.setColorFilter(Color.WHITE, Mode.MULTIPLY);
		GameMechanics.getSingleton().setBasicTowerSelected();
	}
	
	public void advancedTowerButtonPressed(){
		mBasicTowerButton.setPressed(false);
		mAdvancedTowerButton.setPressed(true);
		mHyperTowerButton.setPressed(false);
		mAdvancedTowerButton.setColorFilter(Color.DKGRAY, Mode.MULTIPLY);
		mBasicTowerButton.setColorFilter(Color.WHITE, Mode.MULTIPLY);
		mHyperTowerButton.setColorFilter(Color.WHITE, Mode.MULTIPLY);
		GameMechanics.getSingleton().setAdvancedTowerSelected();
	}
	
	public void hyperTowerButtonPressed(){
		mBasicTowerButton.setPressed(false);
		mAdvancedTowerButton.setPressed(false);
		mHyperTowerButton.setPressed(true);
		mHyperTowerButton.setColorFilter(Color.DKGRAY, Mode.MULTIPLY);
		mAdvancedTowerButton.setColorFilter(Color.WHITE, Mode.MULTIPLY);
		mBasicTowerButton.setColorFilter(Color.WHITE, Mode.MULTIPLY);
		GameMechanics.getSingleton().setHyperTowerSelected();
	}
	
	public static void updateText(){
		mSingleton.mTV.setText( 
				FPSCounter.getSingleton().getFPS()+" FPS | " +
				"Money: "+GameMechanics.getSingleton().getMoney()+" | " +
				"Rounds left: "+(Definitions.MAX_ROUND_NUMBER - GameMechanics.getSingleton().getRoundNumber()) +" |" +
				" Countdown: "+(int)(GameMechanics.getSingleton().getRemainingWaitTime()*0.001));
	}

	@Override
	public void onClick(View v) {
		System.out.println("On Click Listener!!!!! ID: "+v.getId());
		if( v.getId() == mBasicTowerButton.getId() ){
			System.out.println("Basic Tower Button");
			basicTowerButtonPressed();
		} else if( v.getId() == mAdvancedTowerButton.getId() ){
			advancedTowerButtonPressed();
			System.out.println("Advanced Tower Button");
		} else if( v.getId() == mHyperTowerButton.getId() ){
			hyperTowerButtonPressed();
			System.out.println("Hyper Tower Button");
		}
	}

	

}
