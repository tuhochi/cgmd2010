package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * Main part of our level. Contains all relevant objects and (level)parameters. Based on the android 
 * Activity baseclass, it instances the OpenGL ES rendering view, the necessary RenderManager and
 * also establishes the connection to the accelerometers. 
 * @author Georg, Gerald
 */
public class LevelActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {

	/** The main part of the level */
	private ModelStreet street;
	/** Contains the different types of Gems */
	private List<Model> gems;
	
	private HashMap<Integer, ModelDrain> drains;
	
	private ModelGem gemRound;
	private ModelGem gemDiamond;
	private ModelGem gemRect;
	private ModelGem gemOct;
	
	private int numDrains;
	private float levelWidth;
	private float levelSpeed;
	private int moneyToSpend = 0;
	private int gemWorth = 5000;
	
	private GLSurfaceView openglview;
	private RenderManager renderManager;
	private Accelerometer accelerometer;
	private ProgressManager progman;
	private SoundManager soundManager;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l84_level);
		
		drains = new HashMap<Integer, ModelDrain>();
		gems = new LinkedList<Model>();
		soundManager = new SoundManager(this);
		
		initGui();
		initLevelParams();
		initLevel();
		
		openglview = (GLSurfaceView) findViewById(R.id.l84_openglview);
		accelerometer = new Accelerometer(this);
		progman = new ProgressManager();
		renderManager = new RenderManager(this, street, gems, accelerometer, progman, soundManager);	
		openglview.setRenderer(renderManager);
	}

	/**
	 * init level parameters
	 */
	private void initLevelParams() {
		levelWidth = 240f;
		levelSpeed = 2f;
		numDrains = 50;
	}

	/**
	 * init input options of the GUi
	 */
	private void initGui() {
		ImageButton btnGemRound = (ImageButton) findViewById(R.id.l84_ButtonGemRound);
		btnGemRound.setOnClickListener(this);    
		ImageButton btnGemDiamond = (ImageButton) findViewById(R.id.l84_ButtonGemDiamond);
		btnGemDiamond.setOnClickListener(this);
		ImageButton btnGemRect = (ImageButton) findViewById(R.id.l84_ButtonGemRect);
		btnGemRect.setOnClickListener(this);
		ImageButton btnGemOct = (ImageButton) findViewById(R.id.l84_ButtonGemOct);
		btnGemOct.setOnClickListener(this);
		
		//TODO: only show seekbar, if no HW-accelerator is available 
		SeekBar accelBar = (SeekBar) findViewById(R.id.l84_AccelBar);
		accelBar.setOnSeekBarChangeListener(this);
	}

	/**
	 * create the street and the drains of the level
	 */
	private void initLevel() {
		//Create street
		street = new ModelStreet(levelWidth, 17f, -levelWidth/2f + 8f, levelSpeed, R.drawable.l84_tex_street, drains);
		
		//Create drains
		for (int i = 0; i < numDrains;) {
			int drainPos = (int)((Math.random() * levelWidth - levelWidth/2f - 5f) / 3f) * 3;
			int drainType = (int)(Math.random() * 4.0);
			float drainOrientation = (float)Math.random() * 360f;
			
			
			if (!drains.containsKey(drainPos)) {
				//Log.i("DrainPos","#" + i + ": " + drainPos + " / Orientation: " + drainOrientation);
				drains.put(drainPos, new ModelDrain(drainType, drainPos, drainOrientation));
				i++;
				
				if (drainType > 0)
					moneyToSpend += gemWorth;
			}
		}
		TextView tfPoints = (TextView) findViewById(R.id.l84_Points);
		tfPoints.setText("$" + moneyToSpend);
		
		//Create gems
		gemRound = new ModelGem(1,R.drawable.l84_tex_gem_round);
		gemRound.setSoundManager(soundManager);
		gems.add(gemRound);
		gemDiamond = new ModelGem(2,R.drawable.l84_tex_gem_diamond);
		gemDiamond.setSoundManager(soundManager);
		gems.add(gemDiamond);
		gemRect = new ModelGem(3,R.drawable.l84_tex_gem_rect);
		gemRect.setSoundManager(soundManager);
		gems.add(gemRect);
		gemOct = new ModelGem(4,R.drawable.l84_tex_gem_oct);
		gemOct.setSoundManager(soundManager);
		gems.add(gemOct);
		
		//TODO: loading obj-files
		//InputStream modelFile1 = getApplicationContext().getResources().openRawResource(R.raw.l84_quad);
		//gemOct = new ModelObj(modelFile1, R.drawable.l84_gem_oct);
		//gems.add(gemOct);
	}

	
	@Override
	protected void onPause() {
		super.onPause();
		openglview.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		openglview.onResume();
	}

	@Override
	public void finish() {
		
		//next line is for testing only
		progman.addPoints(20);
		progman.setProgress(Math.min(Math.max(progman.getPoints(), 0), 100));
		setResult(Activity.RESULT_OK, progman.asIntent());
		super.finish();
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.l84_ButtonGemRound: gemRound.startFall(drains); break;
		case R.id.l84_ButtonGemDiamond: gemDiamond.startFall(drains); break;
		case R.id.l84_ButtonGemRect: gemRect.startFall(drains); break;
		case R.id.l84_ButtonGemOct: gemOct.startFall(drains); break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		accelerometer.setOrientation(progress);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {	
	}
}
