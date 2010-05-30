package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * This class is used to show the start dialog of the level.
 * @author Manuel Keglevic, Thomas Schulz
 *
 */
public class StartDialog extends AlertDialog implements OnTouchListener, OnDismissListener {
	
	private Context context;

	/**
	 * Creates a new StartDialog
	 * @param context Activity context
	 */
	protected StartDialog(Context context) {
		super(context);
		
		this.context = context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View layout = inflater.inflate(R.layout.l83_startdialog, null);
		
		((Button) layout.findViewById(R.id.l83_Button_start)).setOnTouchListener(this);
		((Button) layout.findViewById(R.id.l83_Button_tutorial)).setOnTouchListener(this);
		
		setView(layout);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			switch(v.getId()) {
			
				case R.id.l83_Button_start: 
					this.dismiss();
					break;
					
				case R.id.l83_Button_tutorial:
					this.hide();
					
					TutorialDialog tutorial = new TutorialDialog(context);
			        tutorial.setOnDismissListener(this);
			        tutorial.show();
					break;
			}
		}
		return false;
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		this.dismiss();
	}

}
