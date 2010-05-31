package at.ac.tuwien.cg.cgmd.bifth2010.level22;

import android.os.Vibrator;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic.GameLogic;

/**
 * A thread used for communication between the gamelogic and the activity thread.
 * Handles tasks like vibration and updating the text boxes.
 * 
 * @author Sulix
 *
 */
public class MainThread extends Thread {
	
	/**
	 * Constructs a new MainThread
	 */
	public MainThread()
	{
		
		vibration = false;
		typedIn = "Good ";
		remaining = "luck!";
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() 
	{
		
		if ( finalizeTextBoxes )	GameLogic.finalizeTextBoxes();
		GameLogic.updateTextBoxes( typedIn, remaining, money );
		if ( vibration )	vibratorService.vibrate( 100 );
	}
	
	/**
	 * Tells the main thread with which strings the text boxes should be updated with
	 * 
	 * @param typedIn the already typed in mail letters
	 * @param remaining the remaining mail letters
	 * @param money the players current money
	 */
	public void setStrings( String typedIn, String remaining )
	{
		
		this.typedIn = typedIn;
		this.remaining = remaining;
	}
	
	public void setMoney( String money )
	{
		
		this.money = money;
	}
	
	/**
	 * Tells the thread to trigger the vibration ( 100 ms )
	 */
	public void setVibration()
	{
		
		vibration = true;
	}
	
	public void finalizeTextBoxes ()
	{
		
		finalizeTextBoxes = true;
	}
	
	/**
	 * Initiates the vibration service
	 * 
	 * @param service the appropriate vibrator object
	 */
	public static void setVibrator ( Vibrator service)
	{
		
		vibratorService = service;
	}
	
	private String typedIn;
	private String remaining;
	private String money;
	private boolean vibration;
	private boolean finalizeTextBoxes;
	
	private static Vibrator vibratorService;
}
