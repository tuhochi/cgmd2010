package at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry;

import java.util.ArrayList;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.LevelActivity;

public class SoundManager {

    private static SoundManager instance;
    private SoundPool soundPool;
    private boolean isMusicOn;
    ArrayList<Integer> soundIds;
    
    public static void initialize() {
        instance = new SoundManager();
    }
    
    public static SoundManager getInstance() {
        return instance;
    }
    
    // ----------------------------------------------------------------------------------
    // -- Public methods ----
    
    
    public void play(int ind) {
        if (!isMusicOn) {
            return;
        }
        
        int soundID = soundIds.get(ind);
        soundPool.play(soundID, 1.0f, 1.0f, 1, 0, 1.0f);
    }
    
    public void stopAll() {
        for (Integer it : soundIds) {
            soundPool.stop(it);
        }
    }
    
    // ----------------------------------------------------------------------------------
    // -- Private methods ----
    
    private SoundManager() {
        soundIds = new ArrayList<Integer>();
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        soundIds.add(soundPool.load(LevelActivity.getInstance(), R.raw.l70_select, 1));
        soundIds.add(soundPool.load(LevelActivity.getInstance(), R.raw.l70_switch, 1));
        soundIds.add(soundPool.load(LevelActivity.getInstance(), R.raw.l70_train, 1));
        
        // check if sounds effects are set
        SharedPreferences settings = LevelActivity.getInstance().getSharedPreferences(
                at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.SHAREDPREFERENCES_FRAMEWORK_SETTINGS_FILE, 0);
        isMusicOn = settings.getBoolean(at.ac.tuwien.cg.cgmd.bifth2010.framework.MenuActivity.PREFERENCE_MUSIC, true);
    }  
}
