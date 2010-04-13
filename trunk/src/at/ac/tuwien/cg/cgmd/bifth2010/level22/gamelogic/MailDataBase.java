package at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.MailSceneObject;

public class MailDataBase {
	
	public static void init ( String filename, Context context )
	{
		
		init();
		
		InputStream data;
		InputStreamReader myStream;
		BufferedReader stringData;
		String rawData = "";
		
		try
		{
			data = context.getAssets().open( filename );
			myStream = new InputStreamReader( data );
			stringData = new BufferedReader( myStream );
			rawData = stringData.readLine();
		} catch ( IOException maWuascht )
		{
			
			// TODO : Scheiss drauf, gogo for platz nummer 1
		}

		
		String[] mails = rawData.split( ";" );
		
		for ( short index = 0; index < mails.length; index ++ )
		{
			
			String[] mailData = mails[ index ].split( ":" );
			if ( mailData[ 2 ] == "true" )
			{
				
				insertRichMail( mailData[ 0 ] );
			} else
			{
				
				insertViagraMail( mailData[ 0 ], mailData[ 1 ] );
			}
		}
	}
	
	public static void init ()
	{
		
		randomGenerator = new Random();
		MailSceneObject.animationTime = 1000;
		
		mailRepository = new ArrayList< Mail >();
		mailMeshes = new ArrayList< MailSceneObject >();
	}
	
	public static MailSceneObject getRandomMail()
	{
		
		int mailIndex = randomGenerator.nextInt( mailRepository.size() );
		
		MailSceneObject newMesh = new MailSceneObject( mailRepository.get( mailIndex ), 1000 );
		mailMeshes.add( mailMeshes.size(), newMesh );
		
		return newMesh;
	}
	
	public static void iterate ( long dt )
	{
		
		for ( short index = 0; index < mailMeshes.size(); index++ )
		{
			
			mailMeshes.get( index ).updatePosition( dt );
		}
	}
	
	public static MailSceneObject nextMail ()
	{
		
		mailMeshes.remove( 0 );
		
		return mailMeshes.get(0);
	}
	
	public static void insertMail( Mail newMail )
	{
		
		mailRepository.add( mailRepository.size(), newMail );
	}
	
	public static void insertRichMail ( String displayName )
	{
		
		Mail newMail = new Mail( displayName, displayName, true );
		MailDataBase.insertMail( newMail );
	}
	
	public static void insertViagraMail( String displayName, String requiredInput )
	{
		
		Mail newMail = new Mail( displayName, requiredInput, false );
		MailDataBase.insertMail( newMail );
	}

	private static ArrayList< Mail > mailRepository;
	private static ArrayList< MailSceneObject > mailMeshes;
	private static Random randomGenerator;
}
