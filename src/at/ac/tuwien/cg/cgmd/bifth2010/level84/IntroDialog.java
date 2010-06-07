package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class IntroDialog extends AlertDialog implements OnTouchListener {

	private View view;
	private ImageView image;
	private TextView text;
	private Button button;
	private int introstage = 0;
	
	
	public IntroDialog(Context context)
	{
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.l84_customdialog, null);
		
		text = (TextView) view.findViewById(R.id.l84_dialogtext);
		image = (ImageView) view.findViewById(R.id.l84_dialogimage);
		button = (Button) view.findViewById(R.id.l84_dialogbutton);
		button.setOnTouchListener(this);
		
		setIcon(R.drawable.l84_transparent);
		setTitle(R.string.l84_intro_title);
		setView(view);
		
		showFirstIntroStep();
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void show() {
		super.show();
	}

	public void showFirstIntroStep()
	{
		introstage = 0;
		text.setText(R.string.l84_intro_step1);
		image.setImageResource(R.drawable.l84_button_gem_oct);
	}
	
	public void showNextIntroStep()
	{
		introstage++;
		switch(introstage) 
		{
			case 1: text.setText(R.string.l84_intro_step2);
					image.setImageResource(R.drawable.l84_tex_gemshape_oct);
					break;
			case 2: dismiss();
					break;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (v.getId() == button.getId())
			{
				showNextIntroStep();
			}
			
		}
		return false;
	}

}
