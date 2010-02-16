package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import at.ac.tuwien.cg.cgmd.bifth2010.Constants;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This activity lets the user choose which action to perform
 * @author Peter Rautek
 */

public class MenuActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.l00_menu);
        Button buttonNewGame = (Button) findViewById(R.id.l00_ButtonNewGame);
        buttonNewGame.setOnClickListener(mStartClickListener);
        Button buttonContinueGame = (Button) findViewById(R.id.l00_ButtonContinueGame);
        buttonContinueGame.setOnClickListener(mStartClickListener);
        Button buttonHelp = (Button) findViewById(R.id.l00_ButtonHelp);
        buttonHelp.setOnClickListener(mStartClickListener);
        Button buttonAbout = (Button) findViewById(R.id.l00_ButtonAbout);
        buttonAbout.setOnClickListener(mStartClickListener);
        Button buttonCredit = (Button) findViewById(R.id.l00_ButtonCredits);
        buttonCredit.setOnClickListener(mStartClickListener);
    }
    
    private OnClickListener mStartClickListener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(v.getId()== R.id.l00_ButtonNewGame){
				Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
				intentMap.putExtra(Constants.EXTRA_STARTNEW, true);
				startActivity(intentMap);
			} else if(v.getId()== R.id.l00_ButtonContinueGame){
				Intent intentMap = new Intent(getApplicationContext(), MapActivity.class);
				intentMap.putExtra(Constants.EXTRA_STARTNEW, false);
				startActivity(intentMap);
			}  else if(v.getId()== R.id.l00_ButtonCredits){
				Intent intentCredits = new Intent(getApplicationContext(), CreditsActivity.class);
				startActivity(intentCredits);
			}  else if(v.getId()== R.id.l00_ButtonHelp){
				Intent intentHelp = new Intent(getApplicationContext(), HelpActivity.class);
				startActivity(intentHelp);
			}  else if(v.getId()== R.id.l00_ButtonAbout){
				Intent intentAbout = new Intent(getApplicationContext(), AboutActivity.class);
				startActivity(intentAbout);
			} 
		}
    };
}