package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/*
 * Implementierung des Levels
 * 
 * TODO
 */

public class HeistLevel extends HeistView{

	
	//Variablendeklarationen
	private static final String TAG = "BunnyHeist";
	
	//Welcher Mode
	private int l88Mode = READY;
	private static final int READY = 0;
	private static final int PAUSE = 1;
	private static final int RUNNING = 2;
	private static final int LOOSE = 3;
	
	//Richtung in die der Wagen unterwegs ist
	private int l88Direction = NORTH;
	private int l88DirectionNext = NORTH;
	private static final int NORTH = 1;
	private static final int SOUTH = 2;
	private static final int WEST = 3;
	private static final int EAST = 4;
	
	
	//TODO Labels fuer die titels die geladen werden
	private static final int RED = 1;
	private static final int YELLOW = 2;
	private static final int GREEN = 3;
	
	//Vars fuer den Score und einen moeglichen BewegungsDelay
	private long l88Score = 0;
	private long l88Delay = 0;
	
	//speichert die absulute Zeit wann die letzte Bewegung war und ob man sich wieder weiter
	//bewegen kann
	private long l88MoveLast;
	
	//Var um den User gewisse Ausgaben anzuzeigen
	private TextView status;
	
	
	//the location of the hide-outs
	private ArrayList<Coordinate> l88Hides = new ArrayList<Coordinate>();
	
	
	//ein simpler Handler fuer die Animation/Bewegung.
	private RefreshHandler l88RedrawHandler = new RefreshHandler();
	
	//Hier die Klassendefinition des Handlers
	class RefreshHandler extends Handler{
		
		public void handleMessage(Message msg){
			HeistLevel.this.update();
			//TODO wird diese Methode benoetigt bzw in Konflikt mit OpenGL?
			HeistLevel.this.invalidate();
		}
		
		public void sleep(long delayMillis){
			this.removeMessages(0);
			sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	}
	
	/*
	 * Aufbau des Levels mit dem zugrunde liegenden XML-File	
	 */
	
	
	public HeistLevel(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	public HeistLevel(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init();
	}


	private void init(){
		setFocusable(true);
		
		//TODO laden der Modelle
		
	}


	private void initNewLevel(){
		l88Hides.clear();
		
		l88DirectionNext = NORTH;
		
		l88Score = 0;
		l88Delay = 500;
		
		//zwei Verstecke fuer den Beginn an einer beliebigen Stelle
		addRandomHide();
		addRandomHide();
	}
	
	
	private void addRandomHide() {
		// TODO Auto-generated method stub
		//Hier sollten die Pl‰tze der Verstecke erzeugt werden - 
		//immer jedoch auch ein Check ob nicht mit Geb‰uden kolidiert
		
		
		
		
		
		
	}

	
	public static void update() {
		// TODO Auto-generated method stub
		//update des gesamten Screens
		
		
		
		
		
		
		
	}
	
	
	//Update in welche Richtung sich der Hase bewegt, stoﬂt er irgendwo
	//dagegen
	private void updateBunny(){
		
	}

	
	
	/*
	 * Handle the key events in the game. Update the direction of the car
	 */
	public boolean onKeyDown(int keyCode, KeyEvent msg){
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
			//At the start of the level, we should start the new one
			if(l88Mode == READY | l88Mode == LOOSE){
				initNewLevel();
				setMode(RUNNING);
				update();
				return true;
			}
			
			//if the game was paused, continue
			if(l88Mode == PAUSE){
				setMode(RUNNING);
				update();
				return true;
			}
			
			//Change direction
			if(l88Direction != SOUTH){
				l88DirectionNext = NORTH;
			}
			
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
			if(l88Direction != NORTH){
				l88DirectionNext = SOUTH;
			}
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
			if(l88Direction != EAST){
				l88DirectionNext = WEST;
			}
			return true;
		}
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
			if(l88Direction != WEST){
				l88DirectionNext = EAST;
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, msg);
	}

	
	private void setMode(int newMode) {
		// TODO Auto-generated method stub
		int oldMode = l88Mode;
		l88Mode = newMode;
		
		if(newMode == RUNNING & oldMode != RUNNING){
			status.setVisibility(View.INVISIBLE);
			update();
			return;
		}
		
		Resources res = getContext().getResources();
		CharSequence str = "";
		if(newMode == PAUSE){
			str = res.getText(R.string.l88_mode_pause);
		}
		else if(newMode == READY){
			str = res.getText(R.string.l88_mode_ready);
		}
		else if(newMode == LOOSE){
			str = res.getText(R.string.l88_mode_loose) + Long.toString(l88Score);
		}
		
		status.setText(str);
		status.setVisibility(View.VISIBLE);
		
	}
	
	
	//Set the text that will give the user information
	public void setText(TextView newText){
		status = newText;
	}
	
	
}
