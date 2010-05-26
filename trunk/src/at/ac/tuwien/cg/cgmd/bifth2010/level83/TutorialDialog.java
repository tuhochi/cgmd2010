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


public class TutorialDialog extends AlertDialog implements OnGestureListener{

	AlertDialog.Builder builder;
	private int stage = 2;
	private GestureDetector detector;
	private TextView text;
	private ImageView image;
	private View layout;
	private Context context;
	
	public TutorialDialog(Context context) {
		super(context);
		
		this.context = context;
		
		detector = new GestureDetector(context,this);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		layout = inflater.inflate(R.layout.l83_tutorial,null);
		
		text = (TextView)layout.findViewById(R.id.l83_tutorial_text);
		text.setText(context.getString(R.string.l83_screentut_1));
		
		image = (ImageView)layout.findViewById(R.id.l83_tutorial_image);
		image.setImageResource(Constants.TEXTURE_LENNY);
		
		setView(layout);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return detector.onTouchEvent(event);
	}
	
	@Override
	public boolean onDown(MotionEvent e) {
		switch(stage){
		
		case 2:
			text.setText(context.getString(R.string.l83_screentut_2));
			image.setImageResource(Constants.TEXTURE_HEXAGON);
			break;
			
		case 3:
			text.setText(context.getString(R.string.l83_screentut_3));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
			
		case 4:
			text.setText(context.getString(R.string.l83_screentut_4));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
			
		case 5:
			text.setText(context.getString(R.string.l83_screentut_5));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
			
		case 6:
			text.setText(context.getString(R.string.l83_screentut_6));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
			
		case 7:
			text.setText(context.getString(R.string.l83_screentut_7));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
			
		case 8:
			text.setText(context.getString(R.string.l83_screentut_8));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
			
		case 9:
			text.setText(context.getString(R.string.l83_screentut_9));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
		
		case 10:
			text.setText(context.getString(R.string.l83_screentut_10));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
			
		case 11:
			text.setText(context.getString(R.string.l83_screentut_11));
			image.setImageResource(Constants.TEXTURE_HEXAGON_R);
			break;
			
		default:
			this.dismiss();
			break;
		}
		
		stage++;
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
