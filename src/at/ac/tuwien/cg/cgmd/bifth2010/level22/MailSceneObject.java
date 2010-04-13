package at.ac.tuwien.cg.cgmd.bifth2010.level22;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.*;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.model.*;

public class MailSceneObject {
	
	public MailSceneObject ( Mail myMail, GL10 context, float lifeTime )
	{
		
		position = new Vector3f( 0, 0, 0 );
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
	}
	
	public boolean updatePosition ( float deltaT )
	{
		
		float scaledT = deltaT * scaleFactor;
		accTime += scaledT;
		
		if ( accTime > animationTime )	return false;
		
		// TODO : Use kubic spline interpolation for the animation
		
		return true;
	}
	
	public void display ()
	{
		
		// TODO : Set translation matrix
		myMesh.render( Geometry.Type.Triangles );
	}

	private Vector3f position;
	private Geometry myMesh;
	private Mail myMail;
	private float accTime;
	private float scaleFactor;
	
	public static long animationTime;
}
