package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import static android.opengl.GLES10.glBindTexture;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ShortBuffer;
import java.util.HashMap;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic.IntRef;


public class MailCharacter {

	public MailCharacter( Context mainContext, IntRef texID, ShortBuffer indices )
	{
		
		textureID = texID;
		
		createIndexBuffer( indices );
		
		pending = true;
	}
	
	private void createIndexBuffer( ShortBuffer indexRaw )
	{
		
		indices = indexRaw;
		indices.position( 0 );
	}
	
	public void bindAndDraw( GL10 renderContext )
	{
		
		float color[] = { 0.0f, 0.3f, 0.8f, 1.0f };
		if ( pending )
		{
			
			color[ 0 ] = 0.35f;
			color[ 1 ] = 0.0f;
			color[ 2 ] = 0.0f;
			color[ 3 ] = 1.0f;
		}
		
		renderContext.glTexEnvfv( GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_COLOR, color, 0 );
		
		renderContext.glBindTexture( GL10.GL_TEXTURE_2D, textureID.myContainment[ 0 ] );
		
		renderContext.glDrawElements(GL10.GL_TRIANGLES, indices.capacity(), GL10.GL_UNSIGNED_SHORT, indices );
	}
	
	public void setDone ()
	{
		
		pending = false;
	}
	
	public static MailCharacter getMailCharacter( char requ, Context refContext, ShortBuffer indices )
	{
		
		if ( characterRepository.isEmpty() )
		{
			
			String validValues = "abcdefghijklmnopqrstuvwxyz1234567890@%! ";
			
			for ( int charIndex = 0; charIndex < validValues.length(); charIndex++ )
				characterRepository.put( String.valueOf( validValues.charAt( charIndex ) ), new IntRef() );
		}
		
		return new MailCharacter( refContext, characterRepository.get( String.valueOf( requ ) ), indices );
	}
	
	public static void init ( GL10 renderContext, Context context )
	{
		
		String validValues = "abcdefghijklmnopqrstuvwxyz1234567890@%! ";
		
		for ( int charIndex = 0; charIndex < validValues.length(); charIndex++ )
		{
			
			InputStream imagestream = getImageStream( validValues.charAt( charIndex ), context );
				
			Bitmap bitmap = BitmapFactory.decodeStream( imagestream );

			int[] textureIDs = new int[ 1 ];
			
			renderContext.glGenTextures( 1, textureIDs, 0 );
			renderContext.glBindTexture(GL10.GL_TEXTURE_2D, textureIDs[ 0 ] );
			renderContext.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
			renderContext.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

			bitmap.recycle();
			
			characterRepository.get( String.valueOf( validValues.charAt( charIndex ) ) ).myContainment[ 0 ] = textureIDs[ 0 ];
			
			try {
				imagestream.close();
				imagestream = null;
			} catch (IOException e) {
			}
		}
	}
	
	public static void uninit( GL10 renderContext )
	{
		
		Iterator< IntRef > texIter = characterRepository.values().iterator();
		while ( texIter.hasNext() )
		{
			
			renderContext.glDeleteTextures( 1, texIter.next().myContainment, 0 );
		}
		
		characterRepository.clear();
	}
	
	private static InputStream getImageStream( char requestChar, Context refContext )
	{
		
		InputStream targetStream = null;
		
		if ( requestChar == 'a' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_a );
		} else if ( requestChar == 'b' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_b );
		} else if ( requestChar == 'c' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_c );
		} else if ( requestChar == 'd' )
		{
					
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_d );
		} else if ( requestChar == 'e' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_e );
		} else if ( requestChar == 'f' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_f );
		} else if ( requestChar == 'g' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_g );
		} else if ( requestChar == 'h' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_h );
		} else if ( requestChar == 'i' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_i );
		} else if ( requestChar == 'j' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_j );
		} else if ( requestChar == 'k' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_k );
		} else if ( requestChar == 'l' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_l );
		} else if ( requestChar == 'm' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_m );
		} else if ( requestChar == 'n' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_n );
		} else if ( requestChar == 'o' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_o );
		} else if ( requestChar == 'p' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_p );
		} else if ( requestChar == 'q' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_q );
		} else if ( requestChar == 'r' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_r );
		} else if ( requestChar == 's' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_s );
		} else if ( requestChar == 't' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_t );
		} else if ( requestChar == 'u' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_u );
		} else if ( requestChar == 'v' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_v );
		} else if ( requestChar == 'w' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_w );
		} else if ( requestChar == 'x' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_x );
		} else if ( requestChar == 'y' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_y );
		} else if ( requestChar == 'z' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_z );
		} else if ( requestChar == '0' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_0 );
		} else if ( requestChar == '1' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_1 );
		} else if ( requestChar == '2' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_2 );
		} else if ( requestChar == '3' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_3 );
		} else if ( requestChar == '4' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_4 );
		} else if ( requestChar == '5' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_5 );
		} else if ( requestChar == '6' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_6 );
		} else if ( requestChar == '7' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_7 );
		} else if ( requestChar == '8' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_8 );
		} else if ( requestChar == '9' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_9 );
		} else if ( requestChar == '@' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_at );
		} else if ( requestChar == '%' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_perc );
		} else if ( requestChar == '!' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_ruf );
		} else if ( requestChar == ' ' )
		{
			
			targetStream = refContext.getResources().openRawResource( R.drawable.l22_space );
		}
		
		return targetStream;
	}
	
	private IntRef textureID;
	private static HashMap< String, IntRef > characterRepository = new HashMap< String, IntRef >();
	
	private ShortBuffer indices;
	private boolean pending;
}
