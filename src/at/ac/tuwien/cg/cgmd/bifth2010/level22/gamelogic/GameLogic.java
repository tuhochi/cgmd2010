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
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.MailSceneObject.SuccessState;

public class GameLogic extends Thread {

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
	
	public static void setTextViews ( TextView typ_, TextView rem_, TextView mon_ )
	{
		
		typedIn = typ_;
		remaining = rem_;
		currentMoney = mon_;
	}
	
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
	
	public static synchronized void kill ()
	{
		
		active = false;
	}
	
	public static synchronized void destroy_ ()
	{
		
		GameLogic.kill();
		
		MailSceneObject.uninit();
	}
	
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
		} else if ( !bufferedInput.isEmpty() )
		{
			
			MailSceneObject.SuccessState success = actMail.checkLetter( bufferedInput.poll() );
			if ( success != SuccessState.Pending )
			{
				
				actMail.kill();
				actMail = MailDataBase.nextMail();
				if ( success == SuccessState.Win )		money -= 1;
				if ( success == SuccessState.Loose )	money += 1;
				
				passedMails++;
			}
		}
		
		uiThread.setStrings( getTypedIn(), getRemaining(), getMoney() );
		mainThread.post( uiThread );
	}
	
	public static synchronized boolean putChar ( char input )
	{
		
		return bufferedInput.offer( input );
	}
	
	public static synchronized String getTypedIn ()
	{
		
		if ( actMail != null )	return actMail.getTypedIn();
		
		return "";
	}
	
	public static synchronized String getRemaining ()
	{
		
		if ( actMail != null )	return actMail.getRemaining();
		
		return "";
	}
	
	public static synchronized String getMoney ()
	{
		
		return Integer.toString( ( int ) money );
	}
	
	public static synchronized int getMoneyState ()
	{
		
		return ( int ) money;
	}
	
	public static void updateTextBoxes ( String tI, String rem, String mon)
	{
		
		typedIn.setText( tI );
		remaining.setText( rem );
		currentMoney.setText( mon );
	}
	
	public static synchronized void setThread ( Handler threadGen )
	{
		
		mainThread  = threadGen;
	}
	
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
