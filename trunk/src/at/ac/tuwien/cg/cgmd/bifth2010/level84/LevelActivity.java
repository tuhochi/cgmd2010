package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelActivity extends Activity implements OnClickListener {

	private List<Model> models;
	private List<ModelDrain> drainList; //list for collision detection
	private GLSurfaceView openglview;
	private RenderManager renderManager;
	private Accelerometer accelerometer;
	
	private ModelDrain drain;
	
	private ModelGem gem1;
	private ModelGem gem2;
	private ModelGem gem3;
	private ModelGem gem4;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l84_level);
		models = new LinkedList<Model>();
		drainList = new LinkedList<ModelDrain>();
		openglview = (GLSurfaceView) findViewById(R.id.l84_openglview);
		accelerometer = new Accelerometer(this);
		renderManager = new RenderManager(this, models, drainList, accelerometer);
		
		openglview.setRenderer(renderManager);
		
		ImageButton b1 = (ImageButton) findViewById(R.id.l84_GemButton01);
		b1.setOnClickListener(this);    
		ImageButton b2 = (ImageButton) findViewById(R.id.l84_GemButton02);
		b2.setOnClickListener(this);
		ImageButton b3 = (ImageButton) findViewById(R.id.l84_GemButton03);
		b3.setOnClickListener(this);
		ImageButton b4 = (ImageButton) findViewById(R.id.l84_GemButton04);
		b4.setOnClickListener(this);
		
		loadModels();
		
	}

	private void loadModels() {
		//add street
		models.add(new ModelQuad(R.drawable.l00_coin));
		//add drains
		drain = new ModelDrain(0,2.0f);
		models.add(drain);
		drainList.add(drain);
		drain = new ModelDrain(1,4.0f);
		models.add(drain);
		drainList.add(drain);
		drain = new ModelDrain(2,5.0f);
		models.add(drain);
		drainList.add(drain);
		
		//add gems
		gem1 = new ModelGem(R.drawable.l84_gem1);
		models.add(gem1);
		gem2 = new ModelGem(R.drawable.l84_gem2);
		models.add(gem2);
		gem3 = new ModelGem(R.drawable.l84_gem3);
		models.add(gem3);
		gem4 = new ModelGem(R.drawable.l84_gem4);
		models.add(gem4);
		
		//TODO: loading obj-files
		//InputStream modelFile1 = getApplicationContext().getResources().openRawResource(R.raw.l33_stone);
		//models.add(new ModelObj(modelFile1, R.drawable.l00_coin));
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
	public void onClick(View v) {
		int selectedButton = 0;
		
		switch(v.getId()) {
		case R.id.l84_GemButton01: selectedButton = 1; gem1.changeVisibility(); break;
		case R.id.l84_GemButton02: selectedButton = 2; gem2.changeVisibility();break;
		case R.id.l84_GemButton03: selectedButton = 3; gem3.changeVisibility();break;
		case R.id.l84_GemButton04: selectedButton = 4; gem4.changeVisibility();break;
//		case R.id.l84_GemButton03: selectedButton = 3; gem1.rotateLeft(); break;
//		case R.id.l84_GemButton04: selectedButton = 4; gem1.rotateRight(); break;
		}
			
//		Context context = getApplicationContext();
//		CharSequence text = "You pressed Button #" + selectedButton;
//		int duration = Toast.LENGTH_SHORT;
//
//		Toast toast = Toast.makeText(context, text, duration);
//		toast.show();
	}

	public void setModels(List<Model> models) {
		this.models = models;
	}

	public List<Model> getModels() {
		return models;
	}
}
