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
    private boolean isBouncing = false;
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
    	if(!((GameActivity)_renderer.context)._level._isStarted){
    		if (event.getAction() == MotionEvent.ACTION_UP){
    			((GameActivity)_renderer.context)._level.start();
    		}
    		return true;
    	}
    	if(((GameActivity)_renderer.context)._level._isFinished){
    		if (event.getAction() == MotionEvent.ACTION_UP){
    			((GameActivity)_renderer.context).finish();
    		}
    		return true;
    	}
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            _x = event.getX();
            _y = event.getY();
            this.touchedTime = System.currentTimeMillis();
            
        }
        
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
        	float x = event.getX();
        	float y = event.getY();
        	float time = (System.currentTimeMillis()-this.touchedTime)/1000.0f;
        	//System.out.println("time: "+time);
        	//System.out.println("deltaX: "+Math.abs(_x-x));
        	if(Math.abs(_x-x)>time*500 || Math.abs(_y-y)>time*500){
        		//System.out.println("in");
        		isBouncing = true;
        		((GameActivity)_renderer.context)._level.bouncePedestrians(x/_renderer._width*Level.sizeX, Level.ratioFix*Level.sizeY-(y/_renderer._height*Level.sizeY), _x/_renderer._width*Level.sizeX, Level.ratioFix*Level.sizeY-(_y/_renderer._height*Level.sizeY), time);
        		_x = x;
                _y = y;
                this.touchedTime = System.currentTimeMillis();
        	}
            /*queueEvent(new Runnable() {
                public void run() {
            
                }
            });*/
        }
        
        if (event.getAction() == MotionEvent.ACTION_UP) {
        	//System.out.println("time: "+time);
        	//System.out.println("deltaX: "+Math.abs(_x-x));
    		//System.out.println("out");
        	if(!isBouncing){
                _x = event.getX();
                _y = event.getY();
	            _value = (System.currentTimeMillis()-this.touchedTime)/100.0f;
	        	((GameActivity)_renderer.context)._level.
	        	addTreasure(new Treasure(_value,
	        			new Vector2(_x/_renderer._width*Level.sizeX,
	        			Level.ratioFix*(Level.sizeY-(_y/_renderer._height*Level.sizeY)))));
	   
	
	
	        	//((GameActivity)_renderer.context).setTextTreasureGrabbed(_value);
        	}else{
        		isBouncing = false;
        	}
        }
        return true;
    }
}