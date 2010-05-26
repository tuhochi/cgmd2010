package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import android.app.AlertDialog;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.widget.ImageView;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class FinishDialog extends AlertDialog implements OnGestureListener {

	private GestureDetector detector;
	private View view;
	private TextView text;
	private ImageView image;
	
	public FinishDialog(Context context, GameStats stats) {
		super(context);
		
		detector = new GestureDetector(context,this);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		view = inflater.inflate(R.layout.l83_finshdialog,null);
		
		text = (TextView)view.findViewById(R.id.l83_finishdialog_text);
		text.setText("This is the End");
		
		image = (ImageView)view.findViewById(R.id.l83_finishdialog_image);
		image.setImageResource(Constants.TEXTURE_LENNY);
		
		setView(view);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		dismiss();
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
