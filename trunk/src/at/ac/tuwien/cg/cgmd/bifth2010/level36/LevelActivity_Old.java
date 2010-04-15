//package at.ac.tuwien.cg.cgmd.bifth2010.level36;
//
//import android.app.Activity;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.PorterDuff;
//import android.graphics.PorterDuffXfermode;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.Window;
//import android.view.WindowManager;
//
///**
// * 
// * @author Gruppe 36
// *
// */
//public class LevelActivity_Old extends Activity {
//	public static Paint paint;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		//set fullscreen
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		Window window = getWindow();
//		window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		
//		setContentView(new GameView(this));
//		paint = new Paint();
//		paint.setAntiAlias(true);
//		paint.setDither(true);
//		paint.setColor(Color.RED);
//		paint.setStyle(Paint.Style.STROKE);
//		paint.setStrokeJoin(Paint.Join.ROUND);
//		paint.setStrokeCap(Paint.Cap.ROUND);
//		paint.setStrokeWidth(40);
//		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//	}
//	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		super.onCreateOptionsMenu(menu);
//		return true;
//	}
//
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		super.onPrepareOptionsMenu(menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		return super.onOptionsItemSelected(item);
//	}
//}