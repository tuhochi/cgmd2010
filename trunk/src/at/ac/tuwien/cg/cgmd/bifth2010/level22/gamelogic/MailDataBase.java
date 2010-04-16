package at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

import android.content.Context;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering.MailSceneObject;

public class MailDataBase {
	
	public static void init ( String filename, Context context )
	{
		
		MailDataBase.init();
		
		InputStream data = context.getResources().openRawResource( R.raw.l22_mails );
		InputStreamReader myStream = new InputStreamReader( data );
		BufferedReader stringData = new BufferedReader( myStream );
		
		try
		{
			
			String rawData = stringData.readLine();
			
			while ( rawData != null )
			{
				
				String[] mailData = rawData.split( ":" );
				if ( mailData[ 2 ].equals( "true" ) )
				{
					
					insertRichMail( mailData[ 0 ] );
				} else
				{
					
					insertViagraMail( mailData[ 0 ], mailData[ 1 ] );
				}
				
				rawData = stringData.readLine();
			}
		} catch ( IOException maWuascht )
		{
			
		}
		
		try { stringData.close(); } catch ( IOException bla ){};
	}
	
	public static void init ()
	{
		
		randomGenerator = new Random( new Date().getTime() );
		MailSceneObject.animationTime = 25000;
		
		mailRepository = new ArrayList< Mail >();
		mailMeshes = new ArrayList< MailSceneObject >();
	}
	
	public static MailSceneObject getRandomMail( Context creationContext )
	{
		
		int mailIndex = randomGenerator.nextInt( mailRepository.size() );
		
		MailSceneObject newMesh = new MailSceneObject( mailRepository.get( mailIndex ), MailSceneObject.animationTime, creationContext );
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
		
		if ( ! mailMeshes.isEmpty() )	return mailMeshes.get( 0 );
		
		return null;
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
	
	public static void displayMails ( GL10 renderContext )
	{
		
		for ( short index = 0; index < mailMeshes.size(); index ++ )
		{
			
			mailMeshes.get( index ).display( renderContext );
		}
	}
	
	public static synchronized void makePersistentState ( Bundle target )
	{
		
		target.putInt( "mailcount", mailMeshes.size() );
		
		String[] mailNames = new String[ mailMeshes.size() ];
		float[] accTimes = new float[ mailMeshes.size() ];
		float[] scaleFactors = new float[ mailMeshes.size() ];
		short[] checkingIndices = new short[ mailMeshes.size() ];
		
		for ( int index = 0; index < mailMeshes.size(); index++ )
		{
			
			MailSceneObject actMesh = mailMeshes.get( index );
			mailNames[ index ] = actMesh.getMailName();
			accTimes[ index ] = actMesh.getAccTime();
			scaleFactors[ index ] = actMesh.getScaleFactor();
			checkingIndices[ index ] = actMesh.getActCheckingIndex();
		}
		
		target.putStringArray( "mailnames", mailNames );
		target.putFloatArray( "acctimes", accTimes );
		target.putFloatArray( "scalefactors", scaleFactors );
		target.putShortArray( "checkingindices", checkingIndices );
	}
	
	public static void loadPersistentState ( Bundle target )
	{
		
		for ( int index = 0; index < target.getInt( "mailcount"); index++ )
		{
			
			MailSceneObject loadedObj = new MailSceneObject( new Mail( "invalid", "invalid", false ), 0, null );
			String mailName = target.getStringArray( "mailnames" )[ index ];
			Mail targetMail = null;

			for ( int mailIndex = 0; mailIndex < mailRepository.size(); mailIndex++ )
				if ( mailRepository.get( mailIndex ).getDisplayName().equals( mailName ) )
					targetMail = mailRepository.get( mailIndex );
			
			loadedObj.loadPersistentState( target, targetMail, index );
			
			mailMeshes.set( index, loadedObj );
		}
	}
	
	public static MailSceneObject getMailMesh ( int index )
	{
		
		return mailMeshes.get( index );
	}

	private static ArrayList< Mail > mailRepository;
	private static ArrayList< MailSceneObject > mailMeshes;
	private static Random randomGenerator;
}
