package at.ac.tuwien.cg.cgmd.bifth2010.level13.old;


import android.app.Activity;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.SessionState;

public class LevelActivity extends Activity {


	private BoozyRenderer boozyRenderer;
	
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		boozyRenderer = new BoozyRenderer(this);
	   // boozyRenderer.setRenderer(new BoozyRenderer(this));
		setContentView(boozyRenderer);
		
	
		
		SessionState sState = new SessionState();
		
		sState.setProgress(13);
	
		
		setResult(RESULT_OK,sState.asIntent());
	
		
		
	}	

    
    
    @Override
    protected void onResume() {
    	super.onResume();
    	boozyRenderer.onResume();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    	boozyRenderer.onPause();
    }

    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	
    }

}
