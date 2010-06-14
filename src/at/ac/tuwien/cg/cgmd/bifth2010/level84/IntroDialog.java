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

/**
 * Class which shows a dialog at the beginning of the level with short instructions how to play the game.
 * This class is a extension of {@link AlertDialog}.
 * @author Gerald
 */

public class IntroDialog extends AlertDialog implements OnTouchListener {

	
	/** view **/
	private View view;
	/** ImageView component for showing images **/
	private ImageView image;
	/** TextView component for showing text **/
	private TextView text;
	/** Button for switching between different IntroDialog pages **/
	private Button button;
	/** recent page which is shown **/
	private int introstage = 0;
	
	
	/**
	 * creates a new IntroDialog
	 * @param context {@link Context}
	 */
	public IntroDialog(Context context)
	{
		super(context);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.l84_customdialog, null);
		
		//get the components from the layout
		text = (TextView) view.findViewById(R.id.l84_dialogtext);
		image = (ImageView) view.findViewById(R.id.l84_dialogimage);
		button = (Button) view.findViewById(R.id.l84_dialogbutton);
		button.setOnTouchListener(this);
		
		//set initial params for the dialog
		setIcon(R.drawable.l84_transparent);
		setTitle(R.string.l84_intro_title);
		setView(view);
		
		//show the first page
		showFirstIntroStep();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Dialog#dismiss()
	 */
	@Override
	public void dismiss() {
		super.dismiss();
	}

	/* (non-Javadoc)
	 * @see android.app.Dialog#show()
	 */
	@Override
	public void show() {
		super.show();
	}

	/**
	 * show first intro step - depends on the variable introstage
	 */
	public void showFirstIntroStep()
	{
		introstage = 0;
		text.setText(R.string.l84_intro_step1);
		image.setImageResource(R.drawable.l84_button_gem_oct);
	}
	
	/**
	 * show next intro step - depends on the variable introstage
	 */
	public void showNextIntroStep()
	{
		introstage++;
		switch(introstage) 
		{
			case 1: text.setText(R.string.l84_intro_step2);
					image.setImageResource(R.drawable.l84_tex_gemshape_oct);
					break;
			case 2: text.setText(R.string.l84_intro_step3);
					image.setImageResource(R.drawable.l84_tex_gem_oct);
					break;
			case 3: dismiss();
					break;
		}
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
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
