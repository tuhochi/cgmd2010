package at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic;

import java.util.ArrayList;
import java.util.Date;
import java.util.Queue;
import java.util.Stack;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.MailSceneObject;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.MailSceneObject.SuccessState;

public class GameLogic {

	static void init ( Context context, long spawnCooldown, long moneyCount )
	{
		
		MailDataBase.init( "l22_mails.txt", context );
		actMail = MailDataBase.getRandomMail();
		
		GameLogic.spawnCooldown = spawnCooldown;
		spawnTimer = 0;
		money = moneyCount;
	}
	
	static void loop ()
	{
		
		active = true;
		prevTime = new Date().getTime();
		
		while ( active )
		{
			
			update();
		}
	}
	
	static synchronized void stop ()
	{
		
		active = false;
	}
	
	static synchronized void update ()
	{
		
		long actTime = new Date().getTime();
		long dt = actTime - prevTime;
		prevTime = actTime;
		spawnTimer += dt;
		
		if ( spawnTimer >= spawnCooldown )
		{
			
			spawnTimer = 0;
			MailDataBase.getRandomMail();
		}
		
		MailDataBase.iterate(dt);
		
		if ( bufferedInput.isEmpty() && !actMail.isAlive() )
		{
			
			actMail.kill();
			actMail = MailDataBase.nextMail();
			money += 1;
		} else
		{
			
			MailSceneObject.SuccessState success = actMail.checkLetter( bufferedInput.poll() );
			if ( success != SuccessState.Pending )
			{
				
				actMail.kill();
				actMail = MailDataBase.nextMail();
				if ( success == SuccessState.Win )		money -= 1;
				if ( success == SuccessState.Loose )	money += 1;
			}
		}
	}
	
	public static synchronized boolean putChar ( char input )
	{
		
		return bufferedInput.offer( input );
	}
	
	private static long prevTime;
	private static boolean active;
	private static long spawnCooldown;
	private static long spawnTimer;
	
	private static MailSceneObject actMail;
	private static long money;
	private static Queue< Character > bufferedInput;
}
