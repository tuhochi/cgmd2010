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

/**
 * Class which shows results in form of a dialog at the end of the level.
 * This class is a extension of {@link AlertDialog}.
 * 
 * @author Gerald
 */

public class ResultDialog extends AlertDialog implements OnTouchListener {

	/** view **/
	private View view;
	/** ImageView component for showing images **/
	private ImageView image;
	/** TextView component for showing text **/
	private TextView text;
	/** Button for switching between different ResultDialog pages **/
	private Button button;
	/** ProgressManager for getting the results **/
	private ProgressManager progman;
	/** recent page which is shown **/
	private int resultstage = 0;
	/** variables for localized strings **/
	private String sHit;
	private String sBreak;
	private String sMoneyTotal;
	private String sMoneyRemaining;
	private String sMoneyLost;
	
	
	/**
	 * creates a new ResultDialog
	 * @param context {@link Context}
	 * @param progman {@link ProgressManager}
	 */
	public ResultDialog(Context context, ProgressManager progman)
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
	
	
	/**
	 * show next result step - depends on the variable resultstage
	 */
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
	
	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
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
