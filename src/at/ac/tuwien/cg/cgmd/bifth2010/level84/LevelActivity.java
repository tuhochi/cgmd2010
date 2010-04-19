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

	private ModelStreet street;
	private List<ModelDrain> drains;
	private List<Model> gems;
	
	private ModelGem gemRound;
	private ModelGem gemDiamond;
	private ModelGem gemRect;
	private ModelGem gemOct;
	
	private GLSurfaceView openglview;
	private RenderManager renderManager;
	private Accelerometer accelerometer;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.l84_level);
		
		drains = new LinkedList<ModelDrain>();
		gems = new LinkedList<Model>();
		loadModels();
		
		openglview = (GLSurfaceView) findViewById(R.id.l84_openglview);
		accelerometer = new Accelerometer(this);
		renderManager = new RenderManager(this, street, gems, accelerometer);
		
		openglview.setRenderer(renderManager);
		
		ImageButton b1 = (ImageButton) findViewById(R.id.l84_GemButton01);
		b1.setOnClickListener(this);    
		ImageButton b2 = (ImageButton) findViewById(R.id.l84_GemButton02);
		b2.setOnClickListener(this);
		ImageButton b3 = (ImageButton) findViewById(R.id.l84_GemButton03);
		b3.setOnClickListener(this);
		ImageButton b4 = (ImageButton) findViewById(R.id.l84_GemButton04);
		b4.setOnClickListener(this);
	}

	private void loadModels() {
		
		//Create drains
		drains.add(new ModelDrain(0,2.0f));
		drains.add(new ModelDrain(1,12.0f));
		drains.add(new ModelDrain(2,22.0f));
		drains.add(new ModelDrain(3,32.0f));
		drains.add(new ModelDrain(4,42.0f));
		
		//Create street
		street = new ModelStreet(60.0f, 9.0f, R.drawable.l84_tex_street, drains);
		
		//Create gems
		gemRound = new ModelGem(R.drawable.l84_gem_round);
		gems.add(gemRound);
		gemDiamond = new ModelGem(R.drawable.l84_gem_diamond);
		gems.add(gemDiamond);
		gemRect = new ModelGem(R.drawable.l84_gem_rect);
		gems.add(gemRect);
		gemOct = new ModelGem(R.drawable.l84_gem_oct);
		gems.add(gemOct);
		
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
		switch(v.getId()) {
		case R.id.l84_GemButton01: gemRound.startFall(); break;
		case R.id.l84_GemButton02: gemDiamond.startFall(); break;
		case R.id.l84_GemButton03: gemRect.startFall(); break;
		case R.id.l84_GemButton04: gemOct.startFall(); break;
		}
	}
}
