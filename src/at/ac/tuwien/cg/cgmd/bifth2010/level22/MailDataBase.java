package at.ac.tuwien.cg.cgmd.bifth2010.level22;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

public class MailDataBase {
	
	public static void init ()
	{
		
		randomGenerator = new Random();
		MailSceneObject.animationTime = 1000;
	}
	
	public static Mail getRandomMail()
	{
		
		int mailIndex = randomGenerator.nextInt( mailRepository.length );
		
		Mail theChosenOne = mailRepository[ mailIndex ];
		MailSceneObject newMesh = new MailSceneObject( theChosenOne, context, 1000 );
		mailMeshes[ mailMeshes.length ] = newMesh;
		
		return theChosenOne;
	}
	
	public static void iterate ( float deltaT )
	{
		
		for ( short index = 0; index < mailMeshes.length; index++ )
		{
			
			mailMeshes[ index ].updatePosition( deltaT );
			mailMeshes[ index ].display();
		}
	}
	
	public static void insertMail( Mail newMail )
	{
		
		mailRepository[ mailRepository.length ] = newMail;
	}
	
	public static void insertRichMail ( String displayName )
	{
		
		Mail newMail = new Mail( displayName, displayName, true );
		MailDataBase:insertMail( newMail );
	}
	
	public static void insertViagraMail( String displayName, String requiredInput )
	{
		
		Mail newMail = new Mail( displayName, requiredInput, false );
		MailDataBase:insertMail( newMail );
	}

	private static Mail[] mailRepository;
	private static MailSceneObject[] mailMeshes;
	private static Random randomGenerator;
	private static GL10 context;
}
