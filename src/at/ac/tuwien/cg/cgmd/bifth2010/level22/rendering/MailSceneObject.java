package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

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

/**
 * A mail instance which encapsulates it's state together with it's scene state, game state and rendering informations.
 * 
 * @author Sulix
 *
 */
public class MailSceneObject 
{
	
	public enum SuccessState { Pending, Win, Loose };
	
	/**
	 * Initializes the common data for all mail scene objects. Initializes the textures
	 * 
	 * @param renderContext the actual render context
	 * @param context the main activities context
	 */
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
		
		MailCharacter.init( renderContext, context );
	}
	
	/**
	 * Frees the texture memory
	 */
	public static synchronized void uninit ()
	{
		
		initialized = false;
		
		SpamRenderer.getActContext().glDeleteTextures( 2, texture, 0 );
		
		MailCharacter.uninit( SpamRenderer.getActContext() );
	}

	/**
	 * Constructor
	 * 
	 * @param myMail the maildata associated with this scene object
	 * @param lifeTime the lifetime of this mail in ms, also specifying the animation time
	 * @param context the main activities context
	 */
	public MailSceneObject ( Mail myMail, float lifeTime, Context context )
	{
		
		position = new Vector3f( 0, 0, -30.0f );
		
		characterRefs = new MailCharacter[ myMail.getDisplayName().length() ];
		
		int charSubdivisionCount = 3;
		int verResolution = charSubdivisionCount + 2;
		int subDivisionCount = charSubdivisionCount * myMail.getDisplayName().length() + 2;
		
		float[] vertexPos = new float[ subDivisionCount * verResolution * 3 + 3 ];
		float[] vertexNorm = new float[ subDivisionCount * verResolution * 3 + 3 ];
		float[] uvCoords = new float[ subDivisionCount * verResolution * 2 + 2 ];
		float[] uvCoordsCharacter = new float[ subDivisionCount * verResolution * 2 + 2 ];
		
		float horSize = ( float ) characterRefs.length / 5.0f;
		
		for ( int subDivIndexVer = 0; subDivIndexVer < verResolution; subDivIndexVer++ )
		{
			
			for ( int subDivIndexHor = 0; subDivIndexHor < subDivisionCount; subDivIndexHor++ )
			{
				
				vertexPos[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 ] = 
					2.0f * horSize * ( float ) subDivIndexHor / ( float ) ( subDivisionCount - 1 ) - horSize;
				vertexPos[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 + 1 ] =
					1.2f * ( float ) subDivIndexVer / ( float ) ( verResolution - 1 ) - 0.6f;
				vertexPos[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 + 2 ] = 0;
				
				vertexNorm[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 ] = 0;
				vertexNorm[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 + 1 ] = 0;
				vertexNorm[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 + 2 ] = - 1.0f;
				
				uvCoords[ subDivIndexVer * subDivisionCount * 2 + subDivIndexHor * 2 ] = 
					( float ) subDivIndexHor / ( float ) ( subDivisionCount - 1 );
				uvCoords[ subDivIndexVer * subDivisionCount * 2 + subDivIndexHor * 2 + 1 ] =
					1.0f - 0.63f * ( float ) subDivIndexVer / ( float ) ( verResolution - 1 );
			}
		}
		
		vertexPos[ subDivisionCount * verResolution * 3 ] = 0;
		vertexPos[ subDivisionCount * verResolution * 3 + 1 ] = 1;
		vertexPos[ subDivisionCount * verResolution * 3 + 2 ] = 0;
		
		vertexNorm[ subDivisionCount * verResolution * 3 ] = 0;
		vertexNorm[ subDivisionCount * verResolution * 3 + 1 ] = 0;
		vertexNorm[ subDivisionCount * verResolution * 3 + 2 ] = -1.0f;
		
		uvCoords[ subDivisionCount * verResolution * 2 ] = 0.5f;
		uvCoords[ subDivisionCount * verResolution * 2 + 1 ] = 0.0f;
		
		for ( int charIndex = 0; charIndex < myMail.getDisplayName().length(); charIndex++ )
		{
			
			int meshIndex = subDivisionCount + charIndex * charSubdivisionCount + 1;
			
			for ( int subDivIndexVer = 0; subDivIndexVer < charSubdivisionCount; subDivIndexVer++ )
			{
				
				for ( int subDivIndexHor = 0; subDivIndexHor < charSubdivisionCount; subDivIndexHor++ )
				{
				
					uvCoordsCharacter[ ( meshIndex + subDivIndexVer * subDivisionCount + subDivIndexHor ) * 2 ] = 
						0.02f + 0.96f * ( float ) subDivIndexHor / ( float ) ( charSubdivisionCount - 1 );
					uvCoordsCharacter[ ( meshIndex + subDivIndexVer * subDivisionCount + subDivIndexHor ) * 2 + 1 ] =
						0.98f - 0.96f * ( float ) subDivIndexVer / ( float ) ( charSubdivisionCount - 1 );
				}
			}
			
			int actCpyIndex = 0;
			// quadcount * triangles per quad * vertices per triangle
			short[] localIndexSrc = new short[ ( charSubdivisionCount - 1 ) * ( charSubdivisionCount - 1 ) * 2 * 3 ];
			
			for ( int subDivIndexVer = 0; subDivIndexVer < charSubdivisionCount - 1; subDivIndexVer++ )
			{
				
				for ( int subDivIndexHor = 0; subDivIndexHor < charSubdivisionCount - 1; subDivIndexHor++ )
				{
					
					int actIndex = meshIndex + subDivIndexVer * subDivisionCount + subDivIndexHor;
					
					localIndexSrc[ actCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount );
					localIndexSrc[ actCpyIndex++ ] = ( short ) ( actIndex );
					localIndexSrc[ actCpyIndex++ ] = ( short ) ( actIndex + 1 );
					
					localIndexSrc[ actCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount + 1 );
					localIndexSrc[ actCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount );
					localIndexSrc[ actCpyIndex++ ] = ( short ) ( actIndex + 1 );
				}
			}
			
			ByteBuffer ind_ = ByteBuffer.allocateDirect( localIndexSrc.length * 2 );
			ind_.order( ByteOrder.nativeOrder() );
			ShortBuffer indices = ind_.asShortBuffer();
			indices.put( localIndexSrc );
			indices.position( 0 );
			
			characterRefs[ charIndex ] = MailCharacter.getMailCharacter( myMail.getDisplayName().charAt( charIndex ), context, indices );
		}
		
		int letterTriangleCount = ( ( subDivisionCount - 1 ) * ( verResolution - 1 ) - 
									( charSubdivisionCount - 1 ) * ( charSubdivisionCount - 1 ) * 
									myMail.getDisplayName().length() ) * 2 + subDivisionCount;

		short[] indexSrc = new short[ letterTriangleCount * 3 ];
		int actIndexCpyIndex = 0;
		
		for ( int subDivIndexHor = 0; subDivIndexHor < subDivisionCount - 1; subDivIndexHor++ )
		{
			
			int actIndex = subDivIndexHor;
			
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + 1 );
			
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount + 1 );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + 1 );
		}
		
		for ( int subDivIndexHor = 0; subDivIndexHor < subDivisionCount - 1; subDivIndexHor++ )
		{
			
			int actIndex = ( verResolution - 1 ) * subDivisionCount + subDivIndexHor;
			
			// First Quadhalf
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex - subDivisionCount );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex - subDivisionCount + 1 );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex );
			
			// Second Quadhalf
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex - subDivisionCount + 1 );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + 1 );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex );
			
			// Envelope top
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + 1 );
			indexSrc[ actIndexCpyIndex++ ] = ( short ) ( subDivisionCount * verResolution );
		}
		
		for ( int subDivIndexVer = 1; subDivIndexVer < verResolution - 2; subDivIndexVer++ )
		{
			
			for ( int subDivIndexHor = 0; subDivIndexHor < subDivisionCount - 1; subDivIndexHor++ )
			{
			
				if ( subDivIndexHor % charSubdivisionCount == 0 )
				{
					
					int actIndex = subDivIndexVer * subDivisionCount + subDivIndexHor;
					
					indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount );
					indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex );
					indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + 1 );
					
					indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount + 1 );
					indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + subDivisionCount );
					indexSrc[ actIndexCpyIndex++ ] = ( short ) ( actIndex + 1 );
				}
			}
		}
		
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
		
		byteBuf = ByteBuffer.allocateDirect( uvCoordsCharacter.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		uvCoordinatesCharacter = byteBuf.asFloatBuffer();
		uvCoordinatesCharacter.put( uvCoordsCharacter );
		uvCoordinatesCharacter.position( 0 );

		byteBuf = ByteBuffer.allocateDirect( indexSrc.length * 2 );
		byteBuf.order( ByteOrder.nativeOrder() );
		indices = byteBuf.asShortBuffer();
		indices.put( indexSrc );
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
	
	/**
	 * Updates the position of the current mail scene object
	 * 
	 * @param deltaT the passed time between the current and the previous frame
	 * @return is false, if the mail passed it's lifetime
	 */
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
		
		position.x = (float) Math.sin( relativeTime * 20.0f ) / 3.0f;
		position.y = (float) Math.cos( relativeTime * 50.0f + 13.0f ) / 3.0f + 0.35f;
		position.z = relativeTime * 20 - 30;
		
		alpha = (float) Math.sin( relativeTime * 100.0f ) / ( float ) Math.PI;
		scale = 1.0f + alpha / 8.0f;
		alpha /= 4.0f;
		
		return true;
	}
	
	/**
	 * @return the depth component of the current mail scene objects position
	 */
	float getDepth()
	{
		
		return position.z;
	}
	
	/**
	 * Renders the current mail scene object
	 * 
	 * @param renderContext the current rendering context
	 */
	public void display ( GL10 renderContext )
	{
		
		renderContext.glPushMatrix();
		renderContext.glTranslatef( position.x, position.y, position.z );
		renderContext.glRotatef( alpha * 180, 0.0f, 1.0f, 0.0f );
		renderContext.glScalef( scale, scale, scale );
		
		renderContext.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		renderContext.glEnableClientState(GL10.GL_NORMAL_ARRAY);

		renderContext.glFrontFace(GL10.GL_CCW );
		
		renderContext.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexPositions );
		renderContext.glNormalPointer(GL10.GL_FLOAT, 0, vertexNormals);
		
		renderContext.glActiveTexture( GL10.GL_TEXTURE0 );
		renderContext.glClientActiveTexture( GL10.GL_TEXTURE0 );
		renderContext.glEnable( GL10.GL_TEXTURE_2D );
		renderContext.glBindTexture( GL10.GL_TEXTURE_2D, texture[ texIndex ] );
		renderContext.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		renderContext.glTexCoordPointer(2, GL10.GL_FLOAT, 0, uvCoordinates );
		
		renderContext.glDrawElements(GL10.GL_TRIANGLES, indices.capacity(), GL10.GL_UNSIGNED_SHORT, indices );
		
		renderContext.glActiveTexture( GL10.GL_TEXTURE1 );
		renderContext.glClientActiveTexture( GL10.GL_TEXTURE1 );
		renderContext.glEnable( GL10.GL_TEXTURE_2D );
		renderContext.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		renderContext.glTexCoordPointer( 2, GL10.GL_FLOAT, 0, uvCoordinatesCharacter );
		
		renderContext.glTexEnvx( GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND );
		
		for ( int actCharIndex = 0; actCharIndex < characterRefs.length; actCharIndex++ )
			characterRefs[ actCharIndex ].bindAndDraw( renderContext );
		
		renderContext.glTexEnvx( GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE );
		
		renderContext.glDisable( GL10.GL_TEXTURE_2D );
		renderContext.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		
		renderContext.glActiveTexture( GL10.GL_TEXTURE0 );
		renderContext.glClientActiveTexture( GL10.GL_TEXTURE0 );
		renderContext.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		renderContext.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		renderContext.glDisableClientState(GL10.GL_NORMAL_ARRAY);
		
		renderContext.glPopMatrix();
	}
	
	/**
	 * @return the encapsulated mail object
	 */
	public Mail getMail()
	{
		
		return myMail;
	}
	
	/**
	 * End this mail scene objects lifetime
	 */
	public void kill ()
	{
		
		isAlive = false;
	}
	
	/**
	 * Check if the previously entered letter fulfills the input requirements of the encapsulated mail object
	 * 
	 * @param input the next character
	 * @return Loose - the current characters do not match, Win - All characters did match, Pending - the current characters do match
	 */
	public SuccessState checkLetter ( char input )
	{
		
		if ( !isAlive )	return SuccessState.Loose;
		 
		if ( myMail.getRequiredInput().charAt( actCheckingIndex ) != input )	return SuccessState.Loose;
		characterRefs[ actCheckingIndex++ ].setDone();
		if ( actCheckingIndex == myMail.getRequiredInput().length() )	return SuccessState.Win;
		 
		return SuccessState.Pending;
	}
	
	/**
	 * @return true, if this mail scene object is still active
	 */
	public boolean isAlive ()
	{
		
		return isAlive;
	}
	
	/**
	 * @return the already typed in portion of the encapsulated mails required input string
	 */
	public String getTypedIn ()
	{
		
		return myMail.getDisplayName().substring( 0, actCheckingIndex );
	}
	
	/**
	 * @return the still remaining portion of the encapsulated mails required input string
	 */
	public String getRemaining ()
	{
		
		return myMail.getRequiredInput().substring( actCheckingIndex );
	}

	/**
	 * @return the time this mail scene object is already alive in ms
	 */
	public float getAccTime() {
		
		return accTime;
	}

	/**
	 * @return the scale factor, which the time interval between two iterations is multiplied with
	 */
	public float getScaleFactor() {
		
		return scaleFactor;
	}

	/**
	 * @return the index, which indicates which character of the required input string should be compared with the users input next
	 */
	public short getActCheckingIndex() {
		
		return actCheckingIndex;
	}

	/**
	 * @return the display name of the encapsulated mail object
	 */
	public String getMailName()
	{
		
		return myMail.getDisplayName();
	}
	
	/**
	 * Restores the previous state of this mail scene object
	 * 
	 * @param target the bundle where the state should be read from
	 * @param sourceMail the previously encapsulated mail object
	 * @param index the index position, at which the state should be fetched from the appropriate bundle arrays
	 */
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
	private float scale;
	private float alpha;
	
	private FloatBuffer vertexPositions;
	private FloatBuffer vertexNormals;
	private FloatBuffer uvCoordinates;
	private FloatBuffer uvCoordinatesCharacter;
	private ShortBuffer indices;
	private Mail myMail;
	private MailCharacter[] characterRefs;
	private float accTime;
	private float scaleFactor;
	private boolean isAlive;
	private short actCheckingIndex;
	private int texIndex;
	
	public static long animationTime;
	private static int texture[];
	private static boolean initialized = false;
}
