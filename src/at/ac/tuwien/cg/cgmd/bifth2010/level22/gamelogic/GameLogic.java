package at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic;

import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.MainThread;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.MailSceneObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.sound.SoundManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.MailSceneObject.SuccessState;

/**
 * 
 * This class is responsible for the whole game logic. It keeps track of the current player progress,
 * and delegates important actions like the scene update to the appropriate classes.
 * 
 * @author Sulix
 */
public class GameLogic extends Thread {

	/**
	 * Initializes the game logic. If the activity was stopped and deleted from memory, the previous state
	 * will be reconstructed.
	 * 
	 * @param loadedState The previous state, if there was any
	 * @param context The current view context of the main activity
	 * @param spawnCooldown This variable tells us, how much time needs to pass, until the next mail object will be spawned ( in ms )
	 * @param moneyCount Keeps track of the player progress, in this case the money
	 * @param sourceActivity A link to the calling activity
	 */
	public static void init ( Bundle loadedState, Context context, long spawnCooldown, long moneyCount, LevelActivity sourceActivity )
	{
		
		MailDataBase.init( "res/raw/l22_mails.txt", context );
		if ( loadedState == null ) 	actMail = MailDataBase.getRandomMail( context);
		
		GameLogic.sourceActivity = sourceActivity;
		
		GameLogic.spawnCooldown = spawnCooldown;
		spawnTimer = 0;
		money = moneyCount;
		myContext = context;
		passedMails = 0;
		active = false;
		
		bufferedInput = new ArrayBlockingQueue< Character >( 100 );
		
		if ( loadedState != null )	loadPersistentState( loadedState );
	}
	
	/**
	 * Sets the appropriate text view targets, required for displaying the current games state
	 * 
	 * @param typ_ Textview for the typed in text
	 * @param rem_ Textview for the required text
	 * @param mon_ Textview for the money state
	 */
	public static void setTextViews ( TextView typ_, TextView rem_, TextView mon_ )
	{
		
		typedIn = typ_;
		remaining = rem_;
		currentMoney = mon_;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run ()
	{
		
		GameLogic.active = true;
		GameLogic.prevTime = new Date().getTime();
		
		while ( GameLogic.active && passedMails < 25 )
		{
			
			GameLogic.update();
			
			try
			{
				
				sleep( 30 );
			} catch ( InterruptedException interrupted_ )
			{
			}
		}
		
		if ( passedMails >= 25 )
		{
		
			sourceActivity.quit();
			sourceActivity.finish();
		}
	}
	
	/**
	 * Stops the game logic and the associated thread
	 */
	public static synchronized void kill ()
	{
		
		active = false;
		
		MailSceneObject.uninit();
	}
	
	/**
	 * Updates the current game state
	 */
	public static synchronized void update ()
	{
		
		MainThread uiThread = new MainThread();
		
		long actTime = new Date().getTime();
		long dt = actTime - prevTime;
		prevTime = actTime;
		spawnTimer += dt;
		
		if ( spawnTimer >= spawnCooldown )
		{
			
			spawnTimer = 0;
			MailSceneObject newMail = MailDataBase.getRandomMail( myContext );
			if ( actMail == null )	actMail = newMail;
		}
		
		MailDataBase.iterate(dt);
		if ( actMail == null )	actMail = MailDataBase.getRandomMail( myContext );
		
		if ( !actMail.isAlive() )
		{
			
			actMail.kill();
			actMail = MailDataBase.nextMail();
			money += 1;
			
			passedMails++;
			uiThread.setVibration();
			SoundManager.playLoseSound();
		} else if ( !bufferedInput.isEmpty() )
		{
			
			MailSceneObject.SuccessState success = actMail.checkLetter( bufferedInput.poll() );
			if ( success != SuccessState.Pending )
			{
				
				actMail.kill();
				actMail = MailDataBase.nextMail();
				if ( success == SuccessState.Win )
				{
					
					money -= 1;
					SoundManager.playWinSound();
				}
				if ( success == SuccessState.Loose )
				{
				
					money += 1;
					SoundManager.playLoseSound();
				}
				
				passedMails++;
			}
		}
		
		uiThread.setStrings( getTypedIn(), getRemaining(), getMoney() );
		mainThread.post( uiThread );
	}
	
	/**
	 * Puts a new character into the character queue
	 * 
	 * @param the actual key input
	 * @return true on success
	 */
	public static synchronized boolean putChar ( char input )
	{
		
		return bufferedInput.offer( input );
	}
	
	/**
	 * @return The part of the mail string, which was already typed in
	 */
	public static synchronized String getTypedIn ()
	{
		
		if ( actMail != null )	return actMail.getTypedIn();
		
		return "";
	}
	
	/**
	 * @return the part of the mail string, which still has to be typed in
	 */
	public static synchronized String getRemaining ()
	{
		
		if ( actMail != null )	return actMail.getRemaining();
		
		return "";
	}
	
	/**
	 * @return the current money state, as string
	 */
	public static synchronized String getMoney ()
	{
		
		return Integer.toString( ( int ) money );
	}
	
	/**
	 * @return the current money state, as integer
	 */
	public static synchronized int getMoneyState ()
	{
		
		return ( int ) money;
	}
	
	/** 
	 * Updates the display with the current values
	 * 
	 * @param tI Represents the part of the current mail string, which was already typed in
	 * @param rem Represents the part of the current mail string, which still has to be typed in
	 * @param mon The players money
	*/
	public static void updateTextBoxes ( String tI, String rem, String mon)
	{
		
		typedIn.setText( tI );
		remaining.setText( rem );
		currentMoney.setText( mon );
	}
	
	/**
	 * Sets the handler, required for the generation of a thread running in the same process as the main activity
	 * 
	 * @param threadGen the appropriate handler
	 */
	public static synchronized void setThread ( Handler threadGen )
	{
		
		mainThread  = threadGen;
	}
	
	/**
	 * Save the current game state for persistency
	 * 
	 * @param target the target bundle where the state should be saved in
	 */
	public static synchronized void makePersistentState ( Bundle target )
	{
		
		target.putLong( "spawncooldown", spawnCooldown );
		char[] charArray = new char[ bufferedInput.size() ];
		int actIndex = 0;
		while ( !bufferedInput.isEmpty() )	charArray[ actIndex++ ] = bufferedInput.poll();
		target.putCharArray( "bufferedKeys", charArray );
		target.putLong( "money", money );
		target.putInt( "passedMails", passedMails );
		
		MailDataBase.makePersistentState( target );
	}
	
	/**
	 * Load the previous state of the GameLogic
	 * 
	 * @param target the source boundle where the state should be loaded from
	 */
	public static synchronized void loadPersistentState ( Bundle target )
	{
		
		spawnCooldown = target.getLong( "spawncooldown" );
		char[] inputs = target.getCharArray( "bufferedKeys" );
		for ( int index = 0; index < inputs.length; index++ )	bufferedInput.add( inputs[ index ] );
		money = target.getLong( "money" );
		passedMails = target.getInt( "passedmails" );
		
		MailDataBase.loadPersistentState( target );
		
		actMail = MailDataBase.getMailMesh( 0 );
	}
	
	/**
	 * @return is true, when the game logic is still running
	 */
	public static boolean isRunning ()
	{
		
		return active;
	}
	
	private static long prevTime;
	private static boolean active;
	private static long spawnCooldown;
	private static long spawnTimer;
	
	private static MailSceneObject actMail;
	private static long money;
	private static ArrayBlockingQueue< Character > bufferedInput;
	private static int passedMails;
	
	private static Context myContext;
	
	private static TextView typedIn;
	private static TextView remaining;
	private static TextView currentMoney;
	
	private static Handler mainThread;
	private static LevelActivity sourceActivity;
}
