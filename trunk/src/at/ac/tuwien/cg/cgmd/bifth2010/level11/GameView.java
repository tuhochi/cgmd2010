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
    public GameView(Context context) {
        super(context);
        _renderer = new GameRenderer(context);
        setRenderer(_renderer);
        this.touchedTime = 0;
       

        
    }
    
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
        			200.0f,
        			new Vector2(_x/_renderer._width*Level.sizeX,
        			Level.sizeY-(_y/_renderer._height*Level.sizeY))));

        	((GameActivity)_renderer.context).setTextTreasureSpent(_value);
			
        	
        }
        return true;
    }
    
    @Override
	public void onPause() {
		//if(((GameActivity)_renderer.context)._level != null)
			//((GameActivity)_renderer.context)._level.pause(true);
		

		//((GameActivity)_renderer.context)._level.suspend();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	public void onStart() {
		//if(((GameActivity)_renderer.context)._level != null)
			//((GameActivity)_renderer.context)._level.start();
	}


	public void onStop() {
		//if(((GameActivity)_renderer.context)._level != null)
			//((GameActivity)_renderer.context)._level.stop();
	}
}