package at.ac.tuwien.cg.cgmd.bifth2010.level84;

import android.app.Activity;
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

public class ResultDialog extends AlertDialog implements OnTouchListener {

	private View view;
	private ImageView image;
	private TextView text;
	private Button button;
	
	public ResultDialog(Context context)
	{
		super(context);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.l84_customdialog, null);
		
		text = (TextView) view.findViewById(R.id.l84_dialogtext);
		image = (ImageView) view.findViewById(R.id.l84_dialogimage);
		button = (Button) view.findViewById(R.id.l84_dialogbutton);
		button.setOnTouchListener(this);
		
		setIcon(R.drawable.l84_transparent);
		setTitle(R.string.l84_result_title);
		setView(view);
	}
	
	public void setResultValues(int totalmoney, int remainingmoney)
	{
		int lostmoney = totalmoney - remainingmoney;
		CharSequence resulttext = "  $ " + String.valueOf(totalmoney) + "\n" +
								  "- $ " + String.valueOf(remainingmoney) + "\n" +
								  "------------\n" + 
								  "  $ " + String.valueOf(lostmoney);
		text.setText(resulttext);
		image.setImageResource(R.drawable.l00_coin);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (v.getId() == button.getId())
			{
				dismiss();
			}
			
		}
		return false;
	}

}
