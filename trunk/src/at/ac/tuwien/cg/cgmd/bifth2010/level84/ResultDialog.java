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

public class ResultDialog extends AlertDialog implements OnTouchListener {

	private View view;
	private ImageView image;
	private TextView text;
	private Button button;
	private ProgressManager progman;
	private int resultstage = 0;
	
	public ResultDialog(Context context, ProgressManager progman)
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
		
		this.progman = progman;
		showNextResultStep();
	}
	
	
	public void showNextResultStep()
	{
		resultstage++;
		switch(resultstage) 
		{
			case 1: text.setText(progman.getGemStatsHit(1) + " Hits\n\n" + progman.getGemStatsMiss(1) + " Breaks");
					image.setImageResource(R.drawable.l84_tex_gem_round);
					break;
			case 2: text.setText(progman.getGemStatsHit(2) + " Hits\n\n" + progman.getGemStatsMiss(2) + " Breaks");
					image.setImageResource(R.drawable.l84_tex_gem_oct);
					break;
			case 3: text.setText(progman.getGemStatsHit(3) + " Hits\n\n" + progman.getGemStatsMiss(3) + " Breaks");
					image.setImageResource(R.drawable.l84_tex_gem_rect);
					break;
			case 4: text.setText(progman.getGemStatsHit(4) + " Hits\n\n" + progman.getGemStatsMiss(4) + " Breaks");
					image.setImageResource(R.drawable.l84_tex_gem_diamond);
					break;
			case 5: text.setText("  $ " + progman.getStartValue() + " - $ " + progman.getRemainingValue() +
									"\n" + "  -----------------------\n" + "  $ " + (progman.getStartValue()-progman.getRemainingValue()));
					image.setImageResource(R.drawable.l00_coin);
					break;
			case 6: dismiss();
					break;
		}
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (v.getId() == button.getId())
			{
				showNextResultStep();
			}
			
		}
		return false;
	}

}
