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
	private Activity activity;
	
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
	
	@Override
	public void dismiss() {
		super.dismiss();
	}

	@Override
	public void show() {
		super.show();
	}
	
	public void setActivity(Activity activity)
	{
		this.activity = activity;
	}

	public void setResults(int totalmoney, int remainingmoney)
	{
		int lostmoney = totalmoney - remainingmoney;
		text.setText(R.string.l84_result_totalmoney + "$ " + String.valueOf(totalmoney) + "\n" +
				R.string.l84_result_remainingmoney + "$ " + String.valueOf(remainingmoney) + "\n" +
				R.string.l84_result_lostmoney + "$ " + String.valueOf(lostmoney));
		image.setImageResource(R.drawable.l00_coin);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (v.getId() == button.getId())
			{
				this.activity.finish();
			}
			
		}
		return false;
	}

}
