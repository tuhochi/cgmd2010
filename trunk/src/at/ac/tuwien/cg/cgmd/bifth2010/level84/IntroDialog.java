package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class IntroDialog extends AlertDialog implements OnTouchListener {

	private View view;
	private ImageView image;
	private TextView text;
	private int introstage = 1;
	
	public IntroDialog(Context context)
	{
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.l84_customdialog, null);
		
		text = (TextView) view.findViewById(R.id.l84_dialogtext);
		image = (ImageView) view.findViewById(R.id.l84_dialogimage);

		setTitle(R.string.l84_intro_title);
		setView(view);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void show() {
		super.show();
	}

	public void showNextIntroStep()
	{
		switch(introstage) 
		{
			case 1: 
					text.setText(R.string.l84_intro_step1);
					image.setImageResource(R.drawable.l84_tex_gem_oct);
			case 2:
					text.setText(R.string.l84_intro_step2);
					image.setImageResource(R.drawable.l84_tex_gem_diamond);
		}
		introstage++;
		
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			showNextIntroStep();
		}
		return false;
	}

}
