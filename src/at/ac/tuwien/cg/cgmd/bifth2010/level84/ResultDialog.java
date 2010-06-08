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
import at.ac.tuwien.cg.cgmd.bifth2010.level33.tools.StopTimer;

public class ResultDialog extends AlertDialog implements OnTouchListener {

	private View view;
	private ImageView image;
	private TextView text;
	private Button button;
	private ProgressManager progman;
	private int resultstage = 0;
	private String sHit;
	private String sBreak;
	private String sMoneyTotal;
	private String sMoneyRemaining;
	private String sMoneyLost;
	
	
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
		
		//read out localized strings
		sHit = context.getString(R.string.l84_result_hits);
		sBreak = context.getString(R.string.l84_result_breaks);
		sMoneyTotal = context.getString(R.string.l84_result_mtotal);
		sMoneyRemaining = context.getString(R.string.l84_result_mremaining);
		sMoneyLost = context.getString(R.string.l84_result_mlost);
		
		this.progman = progman;
		showNextResultStep();
	}
	
	
	public void showNextResultStep()
	{
		resultstage++;
		switch(resultstage) 
		{
			case 1: text.setText(progman.getGemStatsHit(1) + " " + sHit + "\n\n" + progman.getGemStatsBreak(1) + " " + sBreak);
					image.setImageResource(R.drawable.l84_tex_gem_round);
					break;
			case 2: text.setText(progman.getGemStatsHit(2) + " " +  sHit + "\n\n" + progman.getGemStatsBreak(2) + " " + sBreak);
					image.setImageResource(R.drawable.l84_tex_gem_oct);
					break;
			case 3: text.setText(progman.getGemStatsHit(3) + " " +  sHit + "\n\n" + progman.getGemStatsBreak(3) + " " + sBreak);
					image.setImageResource(R.drawable.l84_tex_gem_rect);
					break;
			case 4: text.setText(progman.getGemStatsHit(4) + " " +  sHit + "\n\n" + progman.getGemStatsBreak(4) + " " + sBreak);
					image.setImageResource(R.drawable.l84_tex_gem_diamond);
					break;
			case 5: text.setText(sMoneyTotal + " $ " + progman.getStartValue() + "\n" +
								 sMoneyRemaining + " $ " + progman.getRemainingValue() + "\n" +
								 "----------------------------\n" +
								 sMoneyLost + " $ " + (progman.getStartValue()-progman.getRemainingValue()));
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
