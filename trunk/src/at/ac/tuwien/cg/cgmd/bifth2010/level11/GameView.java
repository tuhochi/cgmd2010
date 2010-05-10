package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GameView extends GLSurfaceView {

	private static final String LOG_TAG = GameView.class.getSimpleName();
    private GameRenderer _renderer;
    private long touchedTime;
    private float _x = 0;
    private float _y = 0;
    private float _value = 0.0f;
    public GameView(Context context, Vector2 resolution) {
        super(context);
        _renderer = new GameRenderer(context, resolution);
        setRenderer(_renderer);
        this.touchedTime = 0;
       

        
    }
    /**
     * places a treasure by touch and hold and release display at the release positions.
     * the value of the treasure is proportional to the time the display is touched.
     */
    public boolean onTouchEvent(final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _x = event.getX();
            _y = event.getY();
            this.touchedTime = System.currentTimeMillis();

        }
        
        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            queueEvent(new Runnable() {
                public void run() {
            
                }
            });
        }
        
        if (event.getAction() == MotionEvent.ACTION_UP) {
            _x = event.getX();
            _y = event.getY();
            _value = (System.currentTimeMillis()-this.touchedTime)/100.0f;
        	((GameActivity)_renderer.context)._level.
        	addTreasure(new Treasure(_value,
        			new Vector2(_x/_renderer._width*Level.sizeX,
        			Level.sizeY-(_y/_renderer._height*Level.sizeY))));


        	((GameActivity)_renderer.context).setTextTreasureGrabbed(_value);
        	
        }
        return true;
    }
}