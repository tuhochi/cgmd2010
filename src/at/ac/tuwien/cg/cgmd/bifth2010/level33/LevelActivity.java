package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);  

            
        //Setting up my GameView 
        GameView gameView = new GameView(this);       
        //gameView.enableSplashScreen(R.drawable.logo, 4000, true);           
        //gameView.setTransition(new FadingTransition(R.drawable.blank, GameView.WIDTH,GameView.HEIGHT, 0,0,0));                 
 
        //Starting 
        setContentView(gameView);   
        //gameView.startGame(); 
	}
}
