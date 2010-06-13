package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.AnimationDrawable;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level84.SoundManager.SoundFX;

/**
 * Main part of our level. Contains all relevant objects and (level)parameters. Based on the android 
 * @link Activity baseclass, it instances the OpenGL ES rendering view, the necessary @link RenderManager and
 * also establishes the connection to the @link Accelerometer. It is also responsible for the whole gampeplay, 
 * point-handling and timing. 
 * @author Georg, Gerald
 */
public class LevelActivity extends Activity implements OnTouchListener, OnSeekBarChangeListener {

	/** Available overlay animations */
	public static enum AnimationType {SPLASH, DUST};
	/** Base value of any gem. regarding its type, this value is later multiplied by 1 (round) to 4 (diamond) */
	public static final int GEM_BASE_VALUE = 1000;
	
	/** The street */
	private ModelStreet street;
	/** Contains the different types of Gems */
	private List<Model> gems;
	
	/** Contains the drains: key is the discretized horizontal position */
	private HashMap<Integer, ModelDrain> drains;
	
	/** Round gem. shown, when the gem is dropped (finger released button) */
	private ModelGem gemRound;
	/** Diamond gem. shown, when the gem is dropped (finger released button) */
	private ModelGem gemDiamond;
	/** Rectangular gem. shown, when the gem is dropped (finger released button) */
	private ModelGem gemRect;
	/** Eight-sided gem. shown, when the gem is dropped (finger released button) */
	private ModelGem gemOct;
	
	/** Round gem. overlayed, when the gem is selected (finger pressed button) */
	private ModelGemShape gemRoundShape;
	/** Diamond gem. overlayed, when the gem is selected (finger pressed button) */
	private ModelGemShape gemDiamondShape;
	/** Rectangular gem. overlayed, when the gem is selected (finger pressed button) */
	private ModelGemShape gemRectShape;
	/** Eight-sided gem. overlayed, when the gem is selected (finger pressed button) */
	private ModelGemShape gemOctShape;
	
	/** Number of drains */
	private int numDrains;
	/** Width of the street (i.e., level) */
	private float levelWidth;
	/** Speed at which the street passes by */
	private float levelSpeed;
	/** Z-position of the street. the lower, the longer it takes until a gem hits the ground */ 
	private float streetPosZ;
	
	/** Summarized value of all available gems */
	private int moneyToSpend = 0;
	
	/** @link TextView for the points */
	private TextView tfPoints;
	/** @link TextView for the point-text's shadow */ 
	private TextView tfPointsShadow;
	/** @link ImageView that contains the animation of a breaking round gem */ 
	private ImageView aniViewRoundBreak;
	/** @link ImageView that contains the animation of a breaking round gem */ 
	private ImageView aniViewOctBreak;
	/** @link ImageView that contains the animation of a breaking rectangular gem */
	private ImageView aniViewRectBreak;
	/** @link ImageView that contains the animation of a breaking diamond gem */
	private ImageView aniViewDiamondBreak;
	/** @link ImageView that contains the animation of a watersplash */
	private ImageView aniViewWatersplash;
	/** @link ImageView that contains the animation of a dust cloud */
	private ImageView aniViewDust;
		
	/** OpenGL surface view */
	private GLSurfaceView openglview;
	/** Responsible for - right - rendering and timing */
	private RenderManager renderManager;
	/** The link to the device's accelerometer */
	private Accelerometer accelerometer;
	/** Manages the player's progress during the game */
	private ProgressManager progman;
	/** Manages music and sound effects */
	private SoundManager soundManager;
	/** Allows rumble/vibration effects */
	private Vibrator vibrator;
	
	/**
	 * Initializes relevant parts of the level and shows the intro dialogue.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.l84_level);
		
		drains = new HashMap<Integer, ModelDrain>();
		gems = new LinkedList<Model>();
		progman = new ProgressManager(this);
		soundManager = new SoundManager(this);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		initLevelParams();
		initLevel();
		
		openglview = (GLSurfaceView) findViewById(R.id.l84_openglview);
		accelerometer = new Accelerometer(this);	
		renderManager = new RenderManager(this, street, gems, accelerometer, progman, soundManager);	
		openglview.setRenderer(renderManager);
		
		initGui();
		
		IntroDialog introdialog = new IntroDialog(this);
		introdialog.setOnDismissListener(renderManager);
		introdialog.show();
	}
	
	/**
	 * Starts background music and initializes the animation @link ImageView's.
	 */
	@Override
	public void onStart() {
		super.onStart();
		
		soundManager.playMusic();

		aniViewRoundBreak = (ImageView) findViewById(R.id.l84_aniViewRoundBreak);
		aniViewOctBreak = (ImageView) findViewById(R.id.l84_aniViewOctBreak);
		aniViewRectBreak = (ImageView) findViewById(R.id.l84_aniViewRectBreak);
		aniViewDiamondBreak = (ImageView) findViewById(R.id.l84_aniViewDiamondBreak);
		
		aniViewWatersplash = (ImageView) findViewById(R.id.l84_aniViewWatersplash);
		aniViewDust = (ImageView) findViewById(R.id.l84_aniViewDust);
		
		aniViewRoundBreak.setBackgroundResource(R.drawable.l84_ani_gembreak_round);
		aniViewOctBreak.setBackgroundResource(R.drawable.l84_ani_gembreak_oct);
		aniViewRectBreak.setBackgroundResource(R.drawable.l84_ani_gembreak_rect);
		aniViewDiamondBreak.setBackgroundResource(R.drawable.l84_ani_gembreak_diamond);
		
		aniViewWatersplash.setBackgroundResource(R.drawable.l84_ani_watersplash);
		aniViewDust.setBackgroundResource(R.drawable.l84_ani_dust);
	}
	
	/**
	 * Cleanup @link ImageView resources.
	 */
	private void cleanUpViews()
	{
		aniViewRoundBreak.setBackgroundResource(0);
		aniViewOctBreak.setBackgroundResource(0);
		aniViewRectBreak.setBackgroundResource(0);
		aniViewDiamondBreak.setBackgroundResource(0);
		aniViewWatersplash.setBackgroundResource(0);
		aniViewDust.setBackgroundResource(0);
	}

	/**
	 * Init level parameters.
	 */
	private void initLevelParams() {
		levelWidth = 240f;
		levelSpeed = 2f;
		numDrains = 30;
		streetPosZ = -10f;
	}

	/**
	 * Init input options of the GUI.
	 */
	private void initGui() {
		ImageButton btnGemRound = (ImageButton) findViewById(R.id.l84_ButtonGemRound);
		btnGemRound.setOnTouchListener(this);
		ImageButton btnGemDiamond = (ImageButton) findViewById(R.id.l84_ButtonGemDiamond);
		btnGemDiamond.setOnTouchListener(this);
		ImageButton btnGemRect = (ImageButton) findViewById(R.id.l84_ButtonGemRect);
		btnGemRect.setOnTouchListener(this);
		ImageButton btnGemOct = (ImageButton) findViewById(R.id.l84_ButtonGemOct);
		btnGemOct.setOnTouchListener(this);
		
		SeekBar accelBar = (SeekBar) findViewById(R.id.l84_AccelBar);
		
		//Hide seekbar, if there is no accelerometer available.
		if (!accelerometer.isOrientationAvailable())
			accelBar.setOnSeekBarChangeListener(this);
		else
			accelBar.setVisibility(View.GONE);
	}
	
	
	/**
	 * Create the street and init the drains of the level.
	 */
	private void initLevel() {

		//Create street. levelWidth/2f - 8f ... 8f = half horizontal screen offset.
		street = new ModelStreet(levelWidth, 15f, levelWidth/2f - 8f, streetPosZ, 
				levelSpeed, R.drawable.l84_tex_street, drains);

		//Create drains
		for (int i = 0; i < numDrains;) {
			
			int drainPos = (int)((Math.random() * levelWidth - levelWidth/2f) / 3f) * 3 + 17; //(/ 3f * 3) to get rounded Results.
			
			//4.49 to also get a (int) casting of 4. 4 = gem oct. 
			//+ 0.5 to reduce the amount of draintype "closed" by 50%.
			int drainType = (int)(Math.random() * 4.49 + 0.5); 
			float drainOrientation = (float)Math.random() * 180f - 90f; //reduce to max. of +/-90¡ to improve gameplay.
			
			if (!drains.containsKey(drainPos)) {
				ModelDrain drain = new ModelDrain(drainType, drainPos, drainOrientation);
				drains.put(drainPos, drain);
				i++;
				
				if (drainType > 0)
					moneyToSpend += drainType * LevelActivity.GEM_BASE_VALUE;
			}
		}
		
		tfPoints = (TextView) findViewById(R.id.l84_Points);
		tfPoints.setText("$" + moneyToSpend);
		tfPointsShadow = (TextView) findViewById(R.id.l84_PointsShadow);
		tfPointsShadow.setText(tfPoints.getText());
		
		progman.setMaxMoney(moneyToSpend);
		
		//Create gems
		gemRound = new ModelGem(ModelDrain.ROUND, R.drawable.l84_tex_gem_round, streetPosZ, drains, this);
		gemRound.setSoundManager(soundManager);
		gems.add(gemRound);
		gemDiamond = new ModelGem(ModelDrain.DIAMOND, R.drawable.l84_tex_gem_diamond, streetPosZ, drains, this);
		gemDiamond.setSoundManager(soundManager);
		gems.add(gemDiamond);
		gemRect = new ModelGem(ModelDrain.RECT, R.drawable.l84_tex_gem_rect, streetPosZ, drains, this);
		gemRect.setSoundManager(soundManager);
		gems.add(gemRect);
		gemOct = new ModelGem(ModelDrain.OCT, R.drawable.l84_tex_gem_oct, streetPosZ, drains, this);
		gemOct.setSoundManager(soundManager);
		gems.add(gemOct);
		
		//Create gem shapes
		gemRoundShape = new ModelGemShape(R.drawable.l84_tex_gemshape_round);
		gemDiamondShape = new ModelGemShape(R.drawable.l84_tex_gemshape_diamond);
		gemRectShape = new ModelGemShape(R.drawable.l84_tex_gemshape_rect);
		gemOctShape = new ModelGemShape(R.drawable.l84_tex_gemshape_oct);
		gems.add(gemRoundShape);
		gems.add(gemDiamondShape);
		gems.add(gemRectShape);
		gems.add(gemOctShape);
		
		if (vibrator != null) {
			gemRound.setVibrator(vibrator);
			gemDiamond.setVibrator(vibrator);
			gemRect.setVibrator(vibrator);
			gemOct.setVibrator(vibrator);
		}
	}
	
	/**
	 * Displays a specific @ImageView-based gem-break animation.
	 * @param type defines the type of animation (ROUND, OCT, RECT, DIAMOND)
	 */
	public void showBreakAni(int type) {

		AnimationDrawable ani = null;
		
		switch(type) {
		case ModelDrain.ROUND: ani = (AnimationDrawable)aniViewRoundBreak.getBackground(); break;
		case ModelDrain.OCT: ani = (AnimationDrawable)aniViewOctBreak.getBackground(); break;
		case ModelDrain.RECT: ani = (AnimationDrawable)aniViewRectBreak.getBackground(); break;
		case ModelDrain.DIAMOND: ani = (AnimationDrawable)aniViewDiamondBreak.getBackground(); break;
		}
		
		if (ani != null) {
			ani.stop();
			ani.start();
		}
	}
	
	/**
	 * Displays a specific @ImageView-based fx-animation.
	 * @param type defines the type of animation (SPLASH or DUST)
	 */
	public void showAni(AnimationType type) {
		
		AnimationDrawable ani = null;
		
		switch(type) {
		case SPLASH: ani = (AnimationDrawable)aniViewWatersplash.getBackground(); break;
		case DUST: ani = (AnimationDrawable)aniViewDust.getBackground(); break;
		}
		
		if (ani != null) {
			ani.stop();
			ani.start();
		}
	}
	
	/**
	 * Game is paused.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		openglview.onPause();
		soundManager.pauseBackground();
		soundManager.pauseNature();
	}
	
	/**
	 * Game is resumed.
	 */
	@Override
	protected void onResume() {
		super.onResume();
		openglview.onResume();
		soundManager.resumeBackground();
		soundManager.resumeNature();
	}

	/**
	 * Game is finished! Stop sounds, return progress, cleanup views and set result.
	 */
	@Override
	public void finish() {
		progman.setProgress(Math.min(Math.max(progman.getProgress(), 0), 100));
		soundManager.releaseSounds();
		cleanUpViews();
		setResult(Activity.RESULT_OK, progman.asIntent());
		super.finish();
	}
	
	/**
	 * Is called when the user changes the value of the orientation @link SeekBar. This SeekBar is only 
	 * available if the used device does not provide an accelerometer for orientation sensing.
	 */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		accelerometer.setOrientation(progress);
	}

	/**
	 * Just a stub. Nothing happens here.
	 */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	/**
	 * Just a stub. Nothing happens here.
	 */
	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {	
	}

	/**
	 * Responsible for button touch and release events. A touch displays the current gem's shape. A release
	 * hides the gem's shape and starts the gem's drop-animation. This method also handles the related sound effects.
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				soundManager.playSound(SoundFX.BUTTON, 1.0f, 1.0f, 0);
				switch(v.getId()) {
					case R.id.l84_ButtonGemRound: gemRoundShape.setVisible(true); break;
					case R.id.l84_ButtonGemDiamond: gemDiamondShape.setVisible(true); break;
					case R.id.l84_ButtonGemRect: gemRectShape.setVisible(true); break;
					case R.id.l84_ButtonGemOct: gemOctShape.setVisible(true); break;
				}
				break;
				
			case MotionEvent.ACTION_UP:
				soundManager.playSound(SoundFX.SWOOSH, 1.0f, 1.0f, 0);
				switch(v.getId()) {
					case R.id.l84_ButtonGemRound: gemRound.startFall(); gemRoundShape.setVisible(false); break;
					case R.id.l84_ButtonGemDiamond: gemDiamond.startFall(); gemDiamondShape.setVisible(false); break;
					case R.id.l84_ButtonGemRect: gemRect.startFall(); gemRectShape.setVisible(false); break;
					case R.id.l84_ButtonGemOct: gemOct.startFall(); gemOctShape.setVisible(false); break;
				}
				break;	
		}
		return true;
	}
}
