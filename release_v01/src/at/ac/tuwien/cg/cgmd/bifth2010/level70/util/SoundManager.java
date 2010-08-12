package at.ac.tuwien.cg.cgmd.bifth2010.level70.util;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;

/**
 * SoundManager to load and play sound effects. SoundManager is an singleton
 * to have an easy global access.
 * 
 * @author Christoph Winklhofer
 */
public class SoundManager {

    // ----------------------------------------------------------------------------------
    // -- Members ----
    
    /** Instance of the sound manager */
    private static SoundManager instance;
    
    /** The sound pool to manage the sound effects */
    private SoundPool soundPool;
    
    /** Is the music turned on in the main game menu */
    private boolean isMusicOn;
    
    /** List of all sound effects */
    ArrayList<Integer> soundIds;
    
    // ----------------------------------------------------------------------------------
    // -- Static methods ----
    
    /** 
     * Initialize the SoundManager. Sound Manager is a singleton.
     */
    public static void initialize() {
        instance = new SoundManager();
    }
    
    
    /**
     * Return the SoundManager singleton instance.
     * @return Instance of SoundManager.
     */
    public static SoundManager getInstance() {
        return instance;
    }
    
    
    // ----------------------------------------------------------------------------------
    // -- Public methods ----
    
    /**
     * Play a sound effect. Do not play if music is off.
     * @param ind Index of the sound effect to play.
     */
    public void play(int ind) {
        
        if (!isMusicOn) {
            return;
        }
        int soundID = soundIds.get(ind);
        soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    
    
    /**
     * Stop all playing sound effects.
     */
    public void stopAll() {
        for (Integer it : soundIds) {
            soundPool.stop(it);
        }
    }
    
    
    // ----------------------------------------------------------------------------------
    // -- Private methods ----
    
    /**
     * Create the sound manager. Load the sound effects and store them in a list.
     */
    private SoundManager() {
        soundIds = new ArrayList<Integer>();
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundIds.add(soundPool.load(LevelActivity.getInstance(), R.raw.l70_select, 1));
        soundIds.add(soundPool.load(LevelActivity.getInstance(), R.raw.l70_switch, 1));
        soundIds.add(soundPool.load(LevelActivity.getInstance(), R.raw.l70_train, 1));
        soundIds.add(soundPool.load(LevelActivity.getInstance(), R.raw.l00_gold03, 1));
        
        // check if sounds effects are set
        SharedPreferences settings = LevelActivity.getInstance().getSharedPreferences(
                at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
        isMusicOn = settings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
    }  
}
