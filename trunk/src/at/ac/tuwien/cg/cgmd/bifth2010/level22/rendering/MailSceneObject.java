package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Date;
import java.util.Random;

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
		
		position = new Vector3f( 0, 0, - maxDist );
		
		characterRefs = new MailCharacter[ myMail.getDisplayName().length() ];
		float horSize = ( float ) characterRefs.length / 5.0f;
		
		pullPositionRadius = horSize * 1.35f;
		nextCheckPoint = pullDt;
		maxPullRadius = new float[ 2 ];
		
		float u, v;
		seed = new Random( new Date().getTime() );		
		pullPositions = new Vector3f[ 2 ];
		
		for ( int pullIndex = 0; pullIndex < 2; pullIndex++ )
		{
			
			u = seed.nextFloat() * (float) Math.PI;
			v = seed.nextFloat() * (float) Math.PI;
			
			pullPositions[ pullIndex ] = new Vector3f( 	(float) Math.sin( u ) * (float) Math.cos( v ) * pullPositionRadius,
														(float) Math.sin( u ) * (float) Math.sin( v ) * pullPositionRadius,
														(float) Math.cos( u ) * pullPositionRadius / 2.0f );
		}
		
		int charSubdivisionCount = Math.max( 2, (int) ( 12 - characterRefs.length * 1.5f ) );
		verResolution = charSubdivisionCount + 2;
		subDivisionCount = charSubdivisionCount * myMail.getDisplayName().length() + 2;
		
		vertexData = new float[ subDivisionCount * verResolution * 3 + 3 ];
		float[] vertexNorm = new float[ subDivisionCount * verResolution * 3 + 3 ];
		float[] uvCoords = new float[ subDivisionCount * verResolution * 2 + 2 ];
		float[] uvCoordsCharacter = new float[ subDivisionCount * verResolution * 2 + 2 ];
		
		for ( int subDivIndexVer = 0; subDivIndexVer < verResolution; subDivIndexVer++ )
		{
			
			for ( int subDivIndexHor = 0; subDivIndexHor < subDivisionCount; subDivIndexHor++ )
			{
				
				vertexData[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 ] = 
					2.0f * horSize * ( float ) subDivIndexHor / ( float ) ( subDivisionCount - 1 ) - horSize;
				vertexData[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 + 1 ] =
					1.2f * ( float ) subDivIndexVer / ( float ) ( verResolution - 1 ) - 0.6f;
				vertexData[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 + 2 ] = 0;
				
				vertexNorm[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 ] = 0;
				vertexNorm[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 + 1 ] = 0;
				vertexNorm[ subDivIndexVer * subDivisionCount * 3 + subDivIndexHor * 3 + 2 ] = 1.0f;
				
				uvCoords[ subDivIndexVer * subDivisionCount * 2 + subDivIndexHor * 2 ] = 
					( float ) subDivIndexHor / ( float ) ( subDivisionCount - 1 );
				uvCoords[ subDivIndexVer * subDivisionCount * 2 + subDivIndexHor * 2 + 1 ] =
					1.0f - 0.63f * ( float ) subDivIndexVer / ( float ) ( verResolution - 1 );
			}
		}
		
		vertexData[ subDivisionCount * verResolution * 3 ] = 0;
		vertexData[ subDivisionCount * verResolution * 3 + 1 ] = 1;
		vertexData[ subDivisionCount * verResolution * 3 + 2 ] = 0;
		
		vertexNorm[ subDivisionCount * verResolution * 3 ] = 0;
		vertexNorm[ subDivisionCount * verResolution * 3 + 1 ] = 0;
		vertexNorm[ subDivisionCount * verResolution * 3 + 2 ] = 1.0f;
		
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
		
		ByteBuffer byteBuf = ByteBuffer.allocateDirect( vertexData.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexPositions = byteBuf.asFloatBuffer();
		vertexPositions.put( vertexData );
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
		
		getMaxPullRadius();
		
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
	public synchronized boolean updatePosition ( float deltaT )
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
		position.z = relativeTime * ( maxDist - minDist ) - maxDist;
		
		alpha = (float) Math.sin( relativeTime * 100.0f );
		scale = 1.0f + alpha / 25.0f;
		alpha /= 10.0f;
		
		if ( relativeTime > nextCheckPoint )
		{
			
			nextCheckPoint += pullDt;
			pullPositions[ 0 ] = pullPositions[ 1 ];
			
			float u = seed.nextFloat() * (float) Math.PI;
			float v = seed.nextFloat() * (float) Math.PI;
			
			pullPositions[ 1 ] = new Vector3f( 	(float) Math.sin( u ) * (float) Math.cos( v ) * pullPositionRadius,
												(float) Math.sin( u ) * (float) Math.sin( v ) * pullPositionRadius,
												(float) Math.cos( u ) * pullPositionRadius / 2.0f );
			
			getMaxPullRadius();
		}
		
		calculateMesh( ( nextCheckPoint - relativeTime ) / pullDt );
		
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
	public synchronized void display ( GL10 renderContext )
	{
		
		renderContext.glPushMatrix();
		renderContext.glTranslatef( position.x, position.y, position.z );
		renderContext.glRotatef( alpha * 180, 0.0f, 1.0f, 0.0f );
		renderContext.glScalef( scale, scale, scale );
		
		renderContext.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		renderContext.glEnableClientState(GL10.GL_NORMAL_ARRAY);
		
		float colors[] = { 1, 1, 1, 1 };
		renderContext.glMaterialfv( GL10.GL_FRONT_AND_BACK, GL_DIFFUSE, colors, 0 );

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
	private void calculateMesh( float weight )
	{
		
		vertexPositions.position( 0 );
		
		float actPosition[] = new float[ 3 ];
		float modifiedPosition[] = new float[ 3 ];
		float distanceVec[] = { 0, 0, 0 };
		float weights[] = { weight, 1.0f - weight };
		
		for ( int vIndex = 0; vIndex < vertexData.length; vIndex += 3 )
		{
			
			actPosition[ 0 ] = vertexData[ vIndex ];
			actPosition[ 1 ] = vertexData[ vIndex + 1 ];
			actPosition[ 2 ] = vertexData[ vIndex + 2 ];
			
			modifiedPosition[ 0 ] = actPosition[ 0 ];
			modifiedPosition[ 1 ] = actPosition[ 1 ];
			modifiedPosition[ 2 ] = actPosition[ 2 ];
			
			for ( int pullIndex = 0; pullIndex < 2; pullIndex++ )
			{
				
				distanceVec[ 0 ] = pullPositions[ pullIndex ].x - actPosition[ 0 ];
				distanceVec[ 1 ] = pullPositions[ pullIndex ].y - actPosition[ 1 ];
				distanceVec[ 2 ] = pullPositions[ pullIndex ].z - actPosition[ 2 ];
				
				float distance = (float) Math.sqrt(	Math.pow( distanceVec[ 0 ], 2.0f ) + 
													Math.pow( distanceVec[ 1 ], 2.0f ) +
													Math.pow( distanceVec[ 2 ], 2.0f ) );
				
				distanceVec[ 0 ] /= distance;
				distanceVec[ 1 ] /= distance;
				distanceVec[ 2 ] /= distance;
				
				distance /= maxPullRadius[ pullIndex ];
				distance = (float) Math.pow( ( 1.0f - distance ) * weights[ pullIndex ], 2.0f );
				
				modifiedPosition[ 0 ] -= distanceVec[ 0 ] * distance;
				modifiedPosition[ 1 ] -= distanceVec[ 1 ] * distance; 
				modifiedPosition[ 2 ] -= distanceVec[ 2 ] * distance; 
			}
			
			vertexPositions.put( modifiedPosition[ 0 ] );
			vertexPositions.put( modifiedPosition[ 1 ] );
			vertexPositions.put( modifiedPosition[ 2 ] );
		}
		
		vertexNormals.position( 0 );
		
		calculateNormals();
		
		vertexPositions.position( 0 );
		vertexNormals.position( 0 );
		indices.position( 0 );
	}
	
	private void calculateNormals()
	{
		
		vertexNormals.position( 0 );
		
		for ( int nIndex = 0; nIndex < vertexNormals.capacity(); nIndex++ )		
			vertexNormals.put( 0.0f );
		
		for ( int triangleIndex = 0; triangleIndex < indices.capacity(); triangleIndex += 3 )
		{
			
			indices.position( triangleIndex );
			int[] tIndices = { indices.get(), indices.get(), indices.get() };

			getTriangleNormal( tIndices );
		}
		
		for ( int letterIndex = 0; letterIndex < characterRefs.length; letterIndex++ )
		{
			
			ShortBuffer actIndices = characterRefs[ letterIndex ].indices;
		
			for ( int triangleIndex = 0; triangleIndex < actIndices.capacity(); triangleIndex += 3 )
			{
				
				actIndices.position( triangleIndex );
				int[] tIndices = { 	actIndices.get(), actIndices.get(),	actIndices.get() };
				
				getTriangleNormal( tIndices );
			}
			
			actIndices.position( 0 );
		}
	}
	
	private float distance( float x1, float y1, float z1, float x2, float y2, float z2 )
	{
		
		return (float) Math.sqrt( Math.pow( x1 - x2, 2.0f ) + Math.pow( y1 - y2, 2.0f ) + Math.pow( z1 - z2, 2.0f ) );
	}
	
	private void getMaxPullRadius()
	{
		
		for ( int pullIndex = 0; pullIndex < 2; pullIndex++ )
		{
			
			maxPullRadius[ pullIndex ] = 0.0f;
			
			float actDist = distance( vertexData[ 0 ], vertexData[ 1 ], vertexData[ 2 ],
										pullPositions[ pullIndex ].x, pullPositions[ pullIndex ].y, pullPositions[ pullIndex ].z );
			maxPullRadius[ pullIndex ] = Math.max( maxPullRadius[ pullIndex ], actDist );
			
			actDist = distance( vertexData[ subDivisionCount * 3 - 3 ], 
								vertexData[ subDivisionCount * 3 - 2 ], 
								vertexData[ subDivisionCount * 3 - 1 ],
								pullPositions[ pullIndex ].x, pullPositions[ pullIndex ].y, pullPositions[ pullIndex ].z );
			maxPullRadius[ pullIndex ] = Math.max( maxPullRadius[ pullIndex ], actDist );
			
			actDist = distance( vertexData[ ( verResolution - 1 ) * subDivisionCount * 3 ], 
								vertexData[ ( verResolution - 1 ) * subDivisionCount * 3 + 1 ], 
								vertexData[ ( verResolution - 1 ) * subDivisionCount * 3 + 2 ],
								pullPositions[ pullIndex ].x, pullPositions[ pullIndex ].y, pullPositions[ pullIndex ].z );
			maxPullRadius[ pullIndex ] = Math.max( maxPullRadius[ pullIndex ], actDist );
			
			actDist = distance( vertexData[ ( ( verResolution - 1 ) * subDivisionCount + subDivisionCount - 1 ) * 3 ], 
								vertexData[ ( ( verResolution - 1 ) * subDivisionCount + subDivisionCount - 1 ) * 3 + 1 ], 
								vertexData[ ( ( verResolution - 1 ) * subDivisionCount + subDivisionCount - 1 ) * 3 + 2 ],
								pullPositions[ pullIndex ].x, pullPositions[ pullIndex ].y, pullPositions[ pullIndex ].z );
			maxPullRadius[ pullIndex ] = Math.max( maxPullRadius[ pullIndex ], actDist );
			
			actDist = distance( vertexData[ verResolution * subDivisionCount * 3 ], 
								vertexData[ verResolution * subDivisionCount * 3 + 1 ], 
								vertexData[ verResolution * subDivisionCount * 3 + 2 ],
								pullPositions[ pullIndex ].x, pullPositions[ pullIndex ].y, pullPositions[ pullIndex ].z );
			maxPullRadius[ pullIndex ] = Math.max( maxPullRadius[ pullIndex ], actDist );
		}
	}
	private void getTriangleNormal( int[] index_ )
	{
	
		vertexPositions.position( index_[ 0 ] * 3 );
		float[] vertex1 = new float[ 3 ];
		vertex1[ 0 ] = vertexPositions.get();
		vertex1[ 1 ] = vertexPositions.get();
		vertex1[ 2 ] = vertexPositions.get();
		
		vertexPositions.position( index_[ 1 ] * 3 );
		float[] vertex2 = new float[ 3 ];
		vertex2[ 0 ] = vertexPositions.get();
		vertex2[ 1 ] = vertexPositions.get();
		vertex2[ 2 ] = vertexPositions.get();
		
		vertexPositions.position( index_[ 2 ] * 3 );
		float[] vertex3 = new float[ 3 ];
		vertex3[ 0 ] = vertexPositions.get();
		vertex3[ 1 ] = vertexPositions.get();
		vertex3[ 2 ] = vertexPositions.get();
		
		float[] s1 = { vertex2[ 0 ] - vertex1[ 0 ], vertex2[ 1 ] - vertex1[ 1 ], vertex2[ 2 ] - vertex1[ 2 ] };
		float[] s2 = { vertex3[ 0 ] - vertex1[ 0 ], vertex3[ 1 ] - vertex1[ 1 ], vertex3[ 2 ] - vertex1[ 2 ] };
		
		float[] normal = { 	s1[ 1 ] * s2[ 2 ] - s1[ 2 ] * s2[ 1 ],
							s1[ 2 ] * s2[ 0 ] - s1[ 0 ] * s2[ 2 ],
							s1[ 0 ] * s2[ 1 ] - s1[ 1 ] * s2[ 0 ] };
		
		float normalLength = (float) Math.sqrt( Math.pow( normal[ 0 ], 2.0 ) + Math.pow( normal[ 1 ], 2.0 ) + Math.pow( normal[ 2 ], 2.0 ) );
		normal[ 0 ] /= normalLength;
		normal[ 1 ] /= normalLength;
		normal[ 2 ] /= normalLength;
		
		for ( int iIndex = 0; iIndex < 3; iIndex++ )
		{
			
			vertexNormals.position( index_[ iIndex ] * 3 );
			
			float[] vNormal = new float[ 3 ];
			vNormal[ 0 ] = vertexNormals.get() + normal[ 0 ];
			vNormal[ 1 ] = vertexNormals.get() + normal[ 1 ];
			vNormal[ 2 ] = vertexNormals.get() + normal[ 2 ];
			
			vertexNormals.position( index_[ iIndex ] * 3 );
			
			vertexNormals.put( vNormal[ 0 ] );
			vertexNormals.put( vNormal[ 1 ] );
			vertexNormals.put( vNormal[ 2 ] );
		}
	}
	
	private Vector3f position;
	private float scale;
	private float alpha;
	
	private float[] vertexData;
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
	private final float minDist = 10.0f;
	private final float maxDist = 30.0f;
	
	private float pullPositionRadius;
	private Vector3f[] pullPositions;
	private float nextCheckPoint;
	private final float pullDt = 0.05f;
	private float[] maxPullRadius;
	
	private int subDivisionCount;
	private int verResolution;
	
	private Random seed;
	public static long animationTime;
	private static int texture[];
	private static boolean initialized = false;
}
