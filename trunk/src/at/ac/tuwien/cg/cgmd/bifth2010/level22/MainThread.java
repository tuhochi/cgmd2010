package at.ac.tuwien.cg.cgmd.bifth2010.level22;

import android.os.Vibrator;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic.GameLogic;

public class MainThread extends Thread {
	
	public MainThread()
	{
		
		vibration = false;
	}

	@Override
	public void run() 
	{
		
		GameLogic.updateTextBoxes( typedIn, remaining, money );
		if ( vibration )	vibratorService.vibrate( 100 );
	}
	
	public void setStrings( String typedIn, String remaining, String money )
	{
		
		this.typedIn = typedIn;
		this.remaining = remaining;
		this.money = money;
	}
	
	public void setVibration()
	{
		
		vibration = true;
	}
	
	public static void setVibrator ( Vibrator service)
	{
		
		vibratorService = service;
	}
	
	private String typedIn;
	private String remaining;
	private String money;
	private boolean vibration;
	
	private static Vibrator vibratorService;
}
