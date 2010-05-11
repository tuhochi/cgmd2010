package at.ac.tuwien.cg.cgmd.bifth2010.level12;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameUI {
	
	private static TextView mFPSTV;
	private static TextView mGoldTV;
	private static ImageButton mBasicTowerButton;
	private static ImageButton mAdvancedTowerButton;
	private static ImageButton mHyperTowerButton;
	
	private static LinearLayout mSingleton;

	public static LinearLayout getSingleton( Context context, int height, int width) {
		if( mSingleton == null ){
			
			int elementWidth = (int)width / 7;
			
			mSingleton = new LinearLayout( context );
			mSingleton.setOrientation(LinearLayout.HORIZONTAL);
			mSingleton.setGravity(Gravity.CENTER_HORIZONTAL);
	        mFPSTV = new TextView( context );
	        String text = FPSCounter.getSingleton().getFPS() + " FPS ";
	        mFPSTV.setText( text );
	        mFPSTV.setBackgroundColor( Color.GRAY);
	        mFPSTV.setHeight( height );
	        mFPSTV.setWidth( elementWidth );
	        mSingleton.addView( mFPSTV );
	        
	        mBasicTowerButton = new ImageButton( context );
	        mBasicTowerButton.setMinimumHeight(height);
	        mBasicTowerButton.setMaxHeight(height);
	        mBasicTowerButton.setMinimumWidth( elementWidth );
	        mBasicTowerButton.setMaxWidth( elementWidth );
	        mBasicTowerButton.setImageResource(R.drawable.l12_basic_tower);
	        mSingleton.addView( mBasicTowerButton );
	        
	        ImageButton mAdvancedTowerButton = new ImageButton( context );
	        mAdvancedTowerButton.setMinimumHeight(height);
	        mAdvancedTowerButton.setMaxHeight(height);
	        mAdvancedTowerButton.setMinimumWidth( elementWidth );
	        mAdvancedTowerButton.setMaxWidth( elementWidth );
	        mAdvancedTowerButton.setImageResource(R.drawable.l12_advanced_tower);
	        mSingleton.addView( mAdvancedTowerButton );
	        
	        ImageButton mHyperTowerButton = new ImageButton( context );
	        mHyperTowerButton.setMinimumHeight(height);
	        mHyperTowerButton.setMaxHeight(height);
	        mHyperTowerButton.setMinimumWidth( elementWidth );
	        mHyperTowerButton.setMaxWidth( elementWidth );
	        mHyperTowerButton.setImageResource(R.drawable.l12_hyper_tower);
	        mSingleton.addView( mHyperTowerButton );
	        
	        mGoldTV = new TextView( context );
	        text =GameMechanics.getSingleton().getMoney() + " Money";
	        mGoldTV.setText( text );
	        mGoldTV.setBackgroundColor( Color.GRAY);
	        mGoldTV.setHeight( height );
	        mGoldTV.setWidth( elementWidth );
	        mSingleton.addView( mGoldTV );  
	        
	        //basicTowerButtonPressed();
		}
		return mSingleton;
	}
	
	
	public static void basicTowerButtonPressed(){
		mBasicTowerButton.setPressed(true);
		mAdvancedTowerButton.setPressed(false);
		mHyperTowerButton.setPressed(false);
		GameMechanics.getSingleton().setBasicTowerSelected();
	}
	
	public static void advancedTowerButtonPressed(){
		mBasicTowerButton.setPressed(false);
		mAdvancedTowerButton.setPressed(true);
		mHyperTowerButton.setPressed(false);
		GameMechanics.getSingleton().setAdvancedTowerSelected();
	}
	
	public static void hyperTowerButtonPressed(){
		mBasicTowerButton.setPressed(false);
		mAdvancedTowerButton.setPressed(false);
		mHyperTowerButton.setPressed(true);
		GameMechanics.getSingleton().setHyperTowerSelected();
	}
	
	public static void updateFPS( int fps ){
		mFPSTV.setText( fps + " FPS ");
	}
	
	

}
