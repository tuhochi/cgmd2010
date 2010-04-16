package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.*;

//import android.R;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic.Mail;
import at.ac.tuwien.cg.cgmd.bifth2010.level33.math.*;

public class MailSceneObject 
{
	
	public enum SuccessState { Pending, Win, Loose };
	
	public static void init ( GL10 renderContext, Context context )
	{
		
		if ( initialized )	return;
		initialized = true;
		
		texture = new int[ 2 ];
		renderContext.glGenTextures( 2, texture, 0);
		
		InputStream imagestream = null;
		
		for ( short texIndex = 0; texIndex < 2; texIndex++ )
		{
			
			if ( texIndex == 0 )	imagestream = context.getResources().openRawResource( R.drawable.l22_rich );
			if ( texIndex == 1 )	imagestream = context.getResources().openRawResource( R.drawable.l22_viagra );
				
			Bitmap bitmap = BitmapFactory.decodeStream( imagestream );

			renderContext.glBindTexture(GL10.GL_TEXTURE_2D, texture[ texIndex ] );
			renderContext.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			renderContext.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

			bitmap.recycle();
		}
		
		try {
			imagestream.close();
			imagestream = null;
		} catch (IOException e) {
		}
	}
	
	public static synchronized void uninit ()
	{
		
		initialized = false;
		
		SpamRenderer.getActContext().glDeleteTextures( 2, texture, 0 );
	}

	public MailSceneObject ( Mail myMail, float lifeTime, Context context )
	{
		
		position = new Vector3f( 0, 0, -100.0f );
		
		float[] vertexPos = {
								-1, -0.6f, 0,
								-1, 0.6f, 0,
								1, 0.6f, 0,
								1, -0.6f, 0,
								0, 1, 0
							};
		float[] vertexNorm =	{
								0, 0, -1,
								0, 0, -1,
								0, 0, -1,
								0, 0, -1,
								0, 0, -1
								};
		float[] uvCoords = {
								0.0f, 1.0f,
								0.0f, 0.37f,
								1.0f, 0.37f,
								1.0f, 1.0f,
								0.5f, 0.0f
							};
		
		byte[] ind = 	{
							0, 1, 2, 
							2, 3, 0,
							1, 4, 2
						};
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect( vertexPos.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexPositions = byteBuf.asFloatBuffer();
		vertexPositions.put( vertexPos );
		vertexPositions.position( 0 );
		
		byteBuf = ByteBuffer.allocateDirect( vertexNorm.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexNormals = byteBuf.asFloatBuffer();
		vertexNormals.put( vertexNorm );
		vertexNormals.position( 0 );
		
		byteBuf = ByteBuffer.allocateDirect( uvCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		uvCoordinates = byteBuf.asFloatBuffer();
		uvCoordinates.put( uvCoords );
		uvCoordinates.position( 0 );
		
		indices = ByteBuffer.wrap( ind );
		indices.position( 0 );
		
		this.myMail = myMail;

		if ( myMail.isRich() )
		{
			
			texIndex = 0;
		} else
		{
			
			texIndex = 1;
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
		
			isAlive = false;
			return isAlive;
		}
		
		float relativeTime = accTime / animationTime;
		
		position.z = relativeTime * 100 - 100;
		
		// TODO : Use kubic spline interpolation for the animation
		
		return true;
	}
	
	float getDepth()
	{
		
		return position.z;
	}
	
	public void display ( GL10 renderContext )
	{
		
		renderContext.glPushMatrix();
		renderContext.glTranslatef( position.x, position.y, position.z );
		
		renderContext.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		renderContext.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		renderContext.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		renderContext.glFrontFace(GL10.GL_CW );
		
		renderContext.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexPositions );
		renderContext.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvCoordinates );
		renderContext.glNormalPointer(GL10.GL_FLOAT, 0, vertexNormals);
		
		glBindTexture( GL10.GL_TEXTURE_2D, texture[ texIndex ] );
		
		renderContext.glDrawElements(GL10.GL_TRIANGLES, indices.capacity(), GL10.GL_UNSIGNED_BYTE, indices );
		
		renderContext.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		renderContext.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		renderContext.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		
		renderContext.glPopMatrix();
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
		 
		if ( myMail.getRequiredInput().charAt( actCheckingIndex++ ) != input )	return SuccessState.Loose;
		if ( actCheckingIndex == myMail.getRequiredInput().length() )	return SuccessState.Win;
		 
		return SuccessState.Pending;
	}
	
	public boolean isAlive ()
	{
		
		return isAlive;
	}
	
	public String getTypedIn ()
	{
		
		return myMail.getDisplayName().substring( 0, actCheckingIndex );
	}
	
	public String getRemaining ()
	{
		
		return myMail.getDisplayName().substring( actCheckingIndex );
	}

	public float getAccTime() {
		
		return accTime;
	}

	public float getScaleFactor() {
		
		return scaleFactor;
	}

	public short getActCheckingIndex() {
		
		return actCheckingIndex;
	}

	public String getMailName()
	{
		
		return myMail.getDisplayName();
	}
	
	public void loadPersistentState ( Bundle target, Mail sourceMail, int index )
	{
		
		myMail = sourceMail;
		
		if ( myMail.isRich() )
		{
			
			texIndex = 0;
		} else
		{
			
			texIndex = 1;
		}
		
		accTime = target.getFloatArray( "accTimes" )[ index ];
		scaleFactor = target.getFloatArray( "scalefactors" )[ index ];
		actCheckingIndex = target.getShortArray( "checkingindices" )[ index ];
	}
	
	private Vector3f position;
	private FloatBuffer vertexPositions;
	private FloatBuffer vertexNormals;
	private FloatBuffer uvCoordinates;
	private ByteBuffer indices;
	private Mail myMail;
	private float accTime;
	private float scaleFactor;
	private boolean isAlive;
	private short actCheckingIndex;
	private int texIndex;
	
	public static long animationTime;
	private static int texture[];
	private static boolean initialized = false;
}
