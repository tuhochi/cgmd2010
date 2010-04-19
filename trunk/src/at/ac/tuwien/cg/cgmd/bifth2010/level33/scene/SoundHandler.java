package at.ac.tuwien.cg.cgmd.bifth2010.level33.scene;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;


public class SoundHandler {
	
	private Context context = null;
	
	/**
	 * the name of the preference file that stores global user settings 
	 */
	public static final String SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE = "l00_settings";

	/**
	 * global user setting of type boolean that determines wether sound and music is allowed 
	 */
	public static final String PREFERENCE_MUSIC = "music";
	
	//Level-Music
	public static final int LEVEL_MUSIC1 = R.raw.l00_menu;
	public static final int LEVEL_MUSIC2 = R.raw.l00_map;
	
	//Activity-Music
	public static final int ACTIVITY_MUSIC_STONE = R.raw.l00_gold02;;
	public static final int ACTIVITY_MUSIC_BARREL = R.raw.l00_gold03;
	public static final int ACTIVITY_MUSIC_TRASH = R.raw.l00_gold01;
	public static final int ACTIVITY_MUSIC_SPRING = R.raw.l00_unallowed;
	public static final int ACTIVITY_MUSIC_MAP = R.raw.l00_unallowed;
	
	
	private MediaPlayer levelAudioPlayer = null;
	private MediaPlayer activityAudioPlayer1 = null;
	private MediaPlayer activityAudioPlayer2 = null;
	
	
	public SoundHandler(Context context)
	{
		this.context = context;
		
		//LEVEL MUSIC
		initLevelSound();
		
	}
	
	/**
	 * Creates an Audio-Player for the Music during a level
	 */
	private void initLevelSound() {
		
		levelAudioPlayer = MediaPlayer.create(context, LEVEL_MUSIC2);
		levelAudioPlayer.setOnErrorListener(new OnErrorListener(){

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				//on any error we create a new media player
				levelAudioPlayer.release();
				levelAudioPlayer = MediaPlayer.create(context, LEVEL_MUSIC2);
				levelAudioPlayer.setOnErrorListener(this);
				return true;
			}
        });
		levelAudioPlayer.setLooping(true);	
	}
	
	/**
	 * Releases the Audio-Player which plays the level-music
	 */
	public void releaseLevelAudioPlayer() {
		
		//Stop music if it's running
		if(levelAudioPlayer!=null){
			if(levelAudioPlayer.isPlaying()){
				levelAudioPlayer.stop();
			}
			levelAudioPlayer.release();
			levelAudioPlayer=null;
		}
		
		//Check if other players are not released
		//TODO
		if(activityAudioPlayer1 != null)
		{
			if(activityAudioPlayer1.isPlaying())
				activityAudioPlayer1.stop();
		}
		if(activityAudioPlayer2 != null)
		{
			if(activityAudioPlayer2.isPlaying())
				activityAudioPlayer2.stop();
		}
		releaseActivityAudioPlayer();
		
	}
	
	/**
	 * Starts the Audio-Player which plays the level-music
	 */
	public void startLevelAudioPlayer() {
		
		if(levelAudioPlayer != null && !levelAudioPlayer.isPlaying() )
		{
			levelAudioPlayer.start();
		}
	}
	
	/**
	 * Creates an Audio-Player and plays the current sound-activity
	 */
	public void playActivitySound(int soundIndex) {
			
		
		
		if(activityAudioPlayer1==null)
		{
			initActivityPlayer(1, soundIndex);
			activityAudioPlayer1.start();
		} else
		if(activityAudioPlayer2==null)
		{
			initActivityPlayer(2, soundIndex);
			activityAudioPlayer2.start();
		}
		
		releaseActivityAudioPlayer();
	}
	
	/**
	 * Inits an free player 
	 */
	private void initActivityPlayer(int actPlayer, int soundIndex) {
		
		if(actPlayer==1)
		{
			activityAudioPlayer1 = MediaPlayer.create(context, soundIndex);
			/*activityAudioPlayer1.setOnErrorListener(new OnErrorListener(){

				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					//on any error we create a new media player
					activityAudioPlayer1.release();
					activityAudioPlayer1 = MediaPlayer.create(context, soundIndex);
					activityAudioPlayer1.setOnErrorListener(this);
					return true;
				}
	        });
			*/
			activityAudioPlayer1.setLooping(false);
			
		}
		else
		{
			activityAudioPlayer2 = MediaPlayer.create(context, soundIndex);
			/*activityAudioPlayer2.setOnErrorListener(new OnErrorListener(){
				
				
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					//on any error we create a new media player
					activityAudioPlayer2.release();
					activityAudioPlayer2 = MediaPlayer.create(context, soundIndex);
					activityAudioPlayer2.setOnErrorListener(this);
					return true;
				}
	        });
	        */
			activityAudioPlayer2.setLooping(false);
		}
	}
	
	
	
	/**
	 * Releases the activityAudioPlayer
	 */
	public void releaseActivityAudioPlayer() {
		
		//Release Player if it`s not playing
		if(activityAudioPlayer1!=null){
			//??? wenn audioFile zu ende, ist der status noch immer auf playing?
			if(!activityAudioPlayer1.isPlaying()){
				activityAudioPlayer1.release();
				activityAudioPlayer1=null;
			}
		}
		if(activityAudioPlayer2!=null){
			//??? wenn audioFile zu ende, ist der status noch immer auf playing?
			if(!activityAudioPlayer2.isPlaying()){
				activityAudioPlayer2.release();
				activityAudioPlayer2=null;
			}
		}
		
	}
}
