package at.ac.tuwien.cg.cgmd.bifth2010.level88;

import android.app.Activity;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

/*
 * Diese Klasse soll der Einstiegspunkt in unser Level bilden, derzeit noch in Entwicklung
 * soll einmal nur ein Skelett bilden
 * TODO - das Level an sich muss immer noch eingebunden werden
 */

/**
 * @author Asperger, Radax
 *
 */
public class LevelActivity extends Activity{

	//Variablendeklarationen
	
	
	
	/**
	 * Called when the activity is first created.
	 */
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//set the specific view
		//TODO level-xml muss noch spezifiert werden
		setContentView(R.layout.l88_level);
		
		//vielleicht hier dem Level noch Daten uebergeben?
		
		
		//TODO
		if(savedInstanceState == null){
			//set up a new game
			
		} else {
			//restored
			
		}
		
		
	}
	
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onPause()
	 */
	protected void onPause() {
		super.onPause();
		//Pause game with the activity
	}
	
	public void onSaveInstanceState(Bundle outState) {
		//Store the game
	}
	
	
	
	
}
