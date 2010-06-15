package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import android.content.Context;
import android.util.Log;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
/**
 * class for touch screen inputs
 *
 */
public class GameView extends GLSurfaceView {

	private static final String LOG_TAG = GameView.class.getSimpleName();
	/**
	 * renders the game
	 */
    private GameRenderer _renderer;
    /**
     * time, when screen has been touched
     */
    private long touchedTime;
    /**
     * time, when finger was moved on screen
     */
    private long moveTime =0;
    /**
     * x coordinate of previous touch/move
     */
    private float _x = 0;
    /**
     * y coordinate of previous touch/move
     */
    private float _y = 0;
    /**
     * true, if finger move was accepted as bouncing. then no treasure placing is possible
     */
    private boolean isBouncing = false;
    /**
     * threshold. minimal time the screen has to be touched to drop a treasure
     */
    private int minTouchTimeToDropTreasure = 500;
    /**
     * gold amount dropped per second
     */
    private float treasureDroppedPerSecond = 2.0f;
    /**
     * boolean to simulate not existing ACTION_TOUCH
     */
    private boolean isDown;
    
    public GameView(Context context, Vector2 resolution) {
        super(context);
        _renderer = new GameRenderer(context, resolution);
        setRenderer(_renderer);
        this.touchedTime = 0;  
        isDown = false;
    }
    /**
     * places a treasure by touch and hold and release display at the release positions.
     * bounces the pedestrian by moves the finger over the screen.
     * the value of the treasure is proportional to the time the display is touched.
     */
    public boolean onTouchEvent(final MotionEvent event) {
    	//enables attraction radius drawing
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
        	isDown = true;
            _x = event.getX();
            _y = event.getY();
            this.touchedTime = System.currentTimeMillis();
            HUD.singleton.setDrawTouchTreasureCircle(true);
        }
        //bounces pedestrian, when moving fast enough
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
        	Vector2 position = new Vector2(event.getX(), event.getY());
        	float x = event.getX();
        	float y = event.getY();
        	
        	float time = 0;
        	if(moveTime > 0){
        		time = (System.currentTimeMillis()-this.moveTime)/1000.0f;
        	}else{
        		moveTime = System.currentTimeMillis();
        		time = (System.currentTimeMillis()-this.touchedTime)/1000.0f;
        	}
        	double delta = Math.sqrt(Math.pow(Math.abs(_x-x),2)+Math.pow(Math.abs(_y-y), 2)); 
        	if(delta/time>50 && (delta > 10 || isBouncing)){
        		isBouncing = true;
        		((GameActivity)_renderer.context)._level.bouncePedestrians(x/_renderer._width*Level.sizeX, Level.ratioFix*Level.sizeY-(y/_renderer._height*Level.sizeY), _x/_renderer._width*Level.sizeX, Level.ratioFix*Level.sizeY-(_y/_renderer._height*Level.sizeY), time);
        		_x = x;
                _y = y;
                this.moveTime = System.currentTimeMillis();
        	}
        }
        //places treasure, if not bouncing
        if (event.getAction() == MotionEvent.ACTION_UP) {
        	isDown = false;
            float value = (System.currentTimeMillis()-this.touchedTime)/1000.0f*treasureDroppedPerSecond;
        	if(!isBouncing){
        		if(touchedTime>minTouchTimeToDropTreasure){
	                _x = event.getX();
	                _y = event.getY();
		        	((GameActivity)_renderer.context)._level.
		        	addTreasure(new Treasure(value,
		        			new Vector2(_x/_renderer._width*Level.sizeX,
		        			Level.ratioFix*(Level.sizeY-(_y/_renderer._height*Level.sizeY)))));
        		}
        	}else{
        		isBouncing = false;
        	}
        	HUD.singleton.setDrawTouchTreasureCircle(false);
        	this.moveTime = 0;
        }
        //sets radius of attraction of the treasure 
        if (isDown && !isBouncing) {
            _x = event.getX();
            _y = event.getY();
            float value = (System.currentTimeMillis()-this.touchedTime)/1000.0f*treasureDroppedPerSecond;
            HUD.singleton.setTouchTreasureCircleRadius(value*Treasure.attractionRadiusMultiplacator);
            HUD.singleton.setTouchTreasureCirclePositon(_x/_renderer._width*Level.sizeX,
        			Level.ratioFix*(Level.sizeY-(_y/_renderer._height*Level.sizeY)));
            
        
        }
        
        return true;
    }
}