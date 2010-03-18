package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GameView extends GLSurfaceView {

	private static final String LOG_TAG = GameView.class.getSimpleName();
    private GameRenderer _renderer;
    
    private float _x = 0;
    private float _y = 0;
    
    public GameView(Context context) {
        super(context);
        _renderer = new GameRenderer(context);
        setRenderer(_renderer);
    }
    
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _x = event.getX();
            _y = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            final float xdiff = (_x - event.getX());
            final float ydiff = (_y - event.getY());
            queueEvent(new Runnable() {
                public void run() {
                //  _renderer.setXAngle(_renderer.getXAngle() + ydiff);
                //  _renderer.setYAngle(_renderer.getYAngle() + xdiff);
                }
            });
            _x = event.getX();
            _y = event.getY();
        }
        return true;
    }
}