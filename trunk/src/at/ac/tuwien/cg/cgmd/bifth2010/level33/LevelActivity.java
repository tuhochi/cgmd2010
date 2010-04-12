package at.ac.tuwien.cg.cgmd.bifth2010.level33;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.scene.SoundHandler;

public class LevelActivity extends Activity {
	
	/**
	 * the name of the preference file that stores global user settings 
	 */
	public static final String SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE = "l00_settings";

	/**
	 * global user setting of type boolean that determines wether sound and music is allowed 
	 */
	public static final String PREFERENCE_MUSIC = "music";
	
	public static boolean IS_MUSIC_ON = false;
	
	private MediaPlayer mAudioPlayer = null;
	public static SoundHandler soundHandler = null;
	public static Vibrator vibrator;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);  

        
        //Setting up the soundHandler and mainSoundSettings
        soundHandler = new SoundHandler(this);
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
		IS_MUSIC_ON = settings.getBoolean(PREFERENCE_MUSIC, true);
        
        //Setting up my GameView 
        GameView gameView = new GameView(this);       
        //gameView.enableSplashScreen(R.drawable.logo, 4000, true);           
        //gameView.setTransition(new FadingTransition(R.drawable.blank, GameView.WIDTH,GameView.HEIGHT, 0,0,0));                 
        
        
		//Starts the sound
        if(IS_MUSIC_ON)
        	soundHandler.startLevelAudioPlayer();
        
        //Starting 
        setContentView(gameView);   
        //gameView.startGame(); 
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	
	@Override
	protected void onDestroy() {
		
		//Stop music if it's running
		
		soundHandler.releaseLevelAudioPlayer();
		
		super.onDestroy();
		
	}
}
