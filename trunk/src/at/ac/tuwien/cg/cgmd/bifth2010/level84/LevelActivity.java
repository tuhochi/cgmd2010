package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class LevelActivity extends Activity implements OnClickListener {

	GLSurfaceView openglview;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.l84_level);
		openglview = (GLSurfaceView) findViewById(R.id.l84_openglview);
		openglview.setRenderer(new L84RenderManager(this));
		
		Button b1 = (Button) findViewById(R.id.l84_GemButton01);
		b1.setOnClickListener(this);
		Button b2 = (Button) findViewById(R.id.l84_GemButton02);
		b2.setOnClickListener(this);
		Button b3 = (Button) findViewById(R.id.l84_GemButton03);
		b3.setOnClickListener(this);
		Button b4 = (Button) findViewById(R.id.l84_GemButton04);
		b4.setOnClickListener(this);
		
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
		if (v.getId() == R.id.l84_GemButton01){selectedButton = 1;}
		if (v.getId() == R.id.l84_GemButton02){selectedButton = 2;}
		if (v.getId() == R.id.l84_GemButton03){selectedButton = 3;}
		if (v.getId() == R.id.l84_GemButton04){selectedButton = 4;}
			
		Context context = getApplicationContext();
		CharSequence text = "Button " + selectedButton +" pressed!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	}
	
}
