package at.ac.tuwien.cg.cgmd.bifth2010.level11;

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

/**
 * Dialog, that shows up at game start
 * @author g11
 */
public class IntroDialog extends AlertDialog implements OnTouchListener {
	/**
	 * android view of the text
	 */
	private View view;
	/**
	 * android image view, holding the dialog icon
	 */
	private ImageView image;
	/**
	 * android view, holding the dialog text
	 */
	private TextView text;
	/**
	 * button of dialog
	 */
	private Button button;
	/**
	 * current page of the dialog
	 */
	private int introstage = 0;
	/**
	 * GameActivity handle to start level, when dialog is closed
	 */
	private GameActivity ga;
	
	public IntroDialog(Context context)
	{
		super(context);
		
		ga = (GameActivity)context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.l11_customdialog, null);
		
		text = (TextView) view.findViewById(R.id.l11_dialogtext);
		image = (ImageView) view.findViewById(R.id.l11_dialogimage);
		button = (Button) view.findViewById(R.id.l11_dialogbutton);
		button.setOnTouchListener(this);
		
		setIcon(R.drawable.l11_transparent);
		setTitle(R.string.l11_intro_title);
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
	/**
	 * set to first page
	 */
	public void showFirstIntroStep()
	{
		introstage = 0;
		text.setText(R.string.l11_intro_01);
		image.setImageResource(R.drawable.l11_icon);
	}
	/**
	 * set to next page
	 */
	public void showNextIntroStep()
	{
		introstage++;
		switch(introstage) 
		{
			case 1: text.setText(R.string.l11_intro_02);
					break;
			case 2: dismiss();
					ga._level.start();
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
