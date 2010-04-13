package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import javax.microedition.khronos.opengles.GL10;
import static android.opengl.GLES10.*;

import at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic.Mail;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.*;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.*;

public class MailSceneObject 
{
	
	public enum SuccessState { Pending, Win, Loose };
	
	public MailSceneObject ( Mail myMail, float lifeTime )
	{
		
		position = new Vector3f( 0, 0, 0 );
		
		vertexPos = new Vector3f[ 6 ];
		uvCoords = new Vector2f[ 6 ];
		
		vertexPos[ 0 ].set( -2, -1, 0);
		vertexPos[ 1 ].set( -2, 1, 0);
		vertexPos[ 2 ].set( 2, 1, 0);
		vertexPos[ 3 ].set( 2, 1, 0);
		vertexPos[ 4 ].set( 2, -1, 0);
		vertexPos[ 5 ].set( -2, -1, 0);
		
		uvCoords[ 0 ].set( 0, 0 );
		uvCoords[ 1 ].set( 0, 1 );
		uvCoords[ 2 ].set( 1, 1 );
		uvCoords[ 3 ].set( 1, 1 );
		uvCoords[ 4 ].set( 1, 0 );
		uvCoords[ 5 ].set( 0, 0 );
		
		this.myMail = myMail;
		
		if ( myMail.isRich() )
		{
			
			/*
			 * TODO : Implement geometry loading
			 * myMesh = GeometryLoader:loadObjectFromString( context, modelpath, imageinputstream );
			 */
		} else
		{
			
			/*
			 * TODO : Implement geometry loading
			 * myMesh = GeometryLoader:loadObjectFromString( context, modelpath, imageinputstream );
			 */
		}
		
		this.accTime = 0;
		this.scaleFactor = animationTime / lifeTime;
		
		isAlive = true;
		actCheckingIndex = 0;
	}
	
	public boolean updatePosition ( float deltaT )
	{
		
		if ( !isAlive )	return false;
		
		float scaledT = deltaT * scaleFactor;
		accTime += scaledT;
		
		if ( accTime > animationTime )
		{
		
			return isAlive = false;
		}
		
		// TODO : Use kubic spline interpolation for the animation
		
		return true;
	}
	
	float getDepth()
	{
		
		return position.z;
	}
	
	public void display ( GL10 renderContext )
	{
		
		renderContext.glTranslatef( position.x, position.y, position.z );
	}
	
	public Mail getMail()
	{
		
		return myMail;
	}
	
	public void kill ()
	{
		
		isAlive = false;
	}
	
	public SuccessState checkLetter ( char input )
	{
		
		if ( !isAlive )	return SuccessState.Loose;
		
		if ( actCheckingIndex == myMail.getRequiredInput().length() )	return SuccessState.Win;
		 
		if ( myMail.getRequiredInput().charAt( actCheckingIndex++ ) != input )	return SuccessState.Loose;
		 
		return SuccessState.Pending;
	}
	
	public boolean isAlive ()
	{
		
		return isAlive;
	}

	private Vector3f position;
	private Vector3f vertexPos[];
	private Vector2f uvCoords[];
	private Mail myMail;
	private float accTime;
	private float scaleFactor;
	private boolean isAlive;
	private short actCheckingIndex;
	
	public static long animationTime;
}
