package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
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
public class LevelActivity extends Activity implements OnTouchListener, OnSeekBarChangeListener {

	/** The main part of the level */
	private ModelStreet street;
	/** Contains the different types of Gems */
	private List<Model> gems;
	
	private HashMap<Integer, ModelDrain> drains;
	
	private ModelGem gemRound;
	private ModelGem gemDiamond;
	private ModelGem gemRect;
	private ModelGem gemOct;
	
	private ModelGemShape gemRoundShape;
	private ModelGemShape gemDiamondShape;
	private ModelGemShape gemRectShape;
	private ModelGemShape gemOctShape;
	
	private int numDrains;
	private float levelWidth;
	private float levelSpeed;
	private float streetPosZ;
	private int moneyToSpend = 0;
	private int gemWorth = 5000;
	
	private TextView tfPoints;
	private TextView tfPointsShadow;
	
	private GLSurfaceView openglview;
	private RenderManager renderManager;
	private Accelerometer accelerometer;
	private ProgressManager progman;
	private SoundManager soundManager;
	
	private Vibrator vibrator;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l84_level);
		
		drains = new HashMap<Integer, ModelDrain>();
		gems = new LinkedList<Model>();
		progman = new ProgressManager();
		soundManager = new SoundManager(this);
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		
		initLevelParams();
		initLevel();
		
		openglview = (GLSurfaceView) findViewById(R.id.l84_openglview);
		accelerometer = new Accelerometer(this);
		
		renderManager = new RenderManager(this, street, gems, accelerometer, progman, soundManager);	
		openglview.setRenderer(renderManager);
		
		initGui();
	}

	/**
	 * init level parameters
	 */
	private void initLevelParams() {
		levelWidth = 240f;
		levelSpeed = 2f;
		numDrains = 30;
		streetPosZ = -10f;
	}

	/**
	 * init input options of the GUi
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
	 * create the street and the drains of the level
	 */
	private void initLevel() {

		//Create drains
		for (int i = 0; i < numDrains;) {
			
			int drainPos = (int)((Math.random() * levelWidth - levelWidth/2f) / 3f) * 3 + 12; //(/ 3f * 3) to get rounded Results.
			int drainType = (int)(Math.random() * 4.99); //4.99 to also get a (int) casting of 4. 4 = gem oct.
			float drainOrientation = (float)Math.random() * 180f - 90f; //reduce to max. of +/-90¡ to improve gameplay.
			
			if (!drains.containsKey(drainPos)) {
				//Log.i("DrainPos","#" + i + ": " + drainPos + " / Orientation: " + drainOrientation);
				drains.put(drainPos, new ModelDrain(drainType, drainPos, drainOrientation));
				i++;
				
				if (drainType > 0)
					moneyToSpend += gemWorth;
			}
		}
		
		//Create street. levelWidth/2f - 8f ... 8f = half horizontal screen offset.
		street = new ModelStreet(levelWidth, 17f, levelWidth/2f - 8f, streetPosZ, 
				levelSpeed, R.drawable.l84_tex_street, drains);
		
		tfPoints = (TextView) findViewById(R.id.l84_Points);
		tfPoints.setText("$" + moneyToSpend);
		tfPointsShadow = (TextView) findViewById(R.id.l84_PointsShadow);
		tfPointsShadow.setText(tfPoints.getText());
		
		progman.setMaxMoney(moneyToSpend);
		
		//Create gems
		gemRound = new ModelGem(ModelDrain.ROUND, R.drawable.l84_tex_gem_round, streetPosZ, drains);
		gemRound.setSoundManager(soundManager);
		gems.add(gemRound);
		gemDiamond = new ModelGem(ModelDrain.DIAMOND, R.drawable.l84_tex_gem_diamond, streetPosZ, drains);
		gemDiamond.setSoundManager(soundManager);
		gems.add(gemDiamond);
		gemRect = new ModelGem(ModelDrain.RECT, R.drawable.l84_tex_gem_rect, streetPosZ, drains);
		gemRect.setSoundManager(soundManager);
		gems.add(gemRect);
		gemOct = new ModelGem(ModelDrain.OCT, R.drawable.l84_tex_gem_oct, streetPosZ, drains);
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
		progman.setProgress(Math.min(Math.max(progman.getPointProgress() + progman.getMoneyProgress(), 0), 100));
		setResult(Activity.RESULT_OK, progman.asIntent());
		super.finish();
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

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				switch(v.getId()) {
					case R.id.l84_ButtonGemRound: gemRoundShape.setVisible(true); break;
					case R.id.l84_ButtonGemDiamond: gemDiamondShape.setVisible(true); break;
					case R.id.l84_ButtonGemRect: gemRectShape.setVisible(true); break;
					case R.id.l84_ButtonGemOct: gemOctShape.setVisible(true); break;
				}
				break;
				
			case MotionEvent.ACTION_UP:
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
