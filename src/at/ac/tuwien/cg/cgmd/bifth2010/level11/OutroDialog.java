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

public class OutroDialog extends AlertDialog implements OnTouchListener {

	private View view;
	private ImageView image;
	private TextView text;
	private Button button;
	private GameActivity ga;
	
	public OutroDialog(Context context, boolean hasWon)
	{
		super(context);
		ga = (GameActivity)context;
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.l84_customdialog, null);
		
		text = (TextView) view.findViewById(R.id.l84_dialogtext);
		image = (ImageView) view.findViewById(R.id.l84_dialogimage);
		button = (Button) view.findViewById(R.id.l84_dialogbutton);
		button.setOnTouchListener(this);
		
		setIcon(R.drawable.l84_transparent);
		setTitle(R.string.l11_outro_title);
		setView(view);
		if(hasWon)
			text.setText(R.string.l11_outro_02);
		else
			text.setText(R.string.l11_outro_01);
		image.setImageResource(R.drawable.l11_icon);
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void show() {
		super.show();
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			ga.finish();			
		}
		return false;
	}

}
