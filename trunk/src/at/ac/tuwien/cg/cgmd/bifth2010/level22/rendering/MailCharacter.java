package at.ac.tuwien.cg.cgmd.bifth2010.level22.rendering;

import static android.opengl.GLES10.glBindTexture;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class MailCharacter {

	public MailCharacter( Context mainContext, GL10 renderContext, char myChar, byte[] indices )
	{
		
		textureID = new int[ 1 ];
		
		InputStream imagestream = this.getImageStream( myChar, mainContext );
			
		Bitmap bitmap = BitmapFactory.decodeStream( imagestream );

		renderContext.glBindTexture(GL10.GL_TEXTURE_2D, textureID[ 0 ] );
		renderContext.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		renderContext.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

		bitmap.recycle();
		
		try {
			imagestream.close();
			imagestream = null;
		} catch (IOException e) {
		}
		
		characterRepository.put( String.valueOf( myChar ), Integer.valueOf( textureID[ 0 ] ) );
		
		createIndexBuffer( indices );
		
		pending = true;
	}
	
	public MailCharacter( Context mainContext, GL10 renderContext, int texID, byte[] indices )
	{
		
		textureID = new int[ 1 ];
		
		textureID[ 0 ] = texID;
		
		createIndexBuffer( indices );
		
		pending = true;
	}
	
	private void createIndexBuffer( byte[] indexRaw )
	{
		
		indices = ByteBuffer.wrap( indexRaw );
		indices.position( 0 );
	}
	
	public void bindAndDraw( GL10 renderContext )
	{
		
		renderContext.glActiveTexture( GL10.GL_TEXTURE1 );
		renderContext.glClientActiveTexture( GL10.GL_TEXTURE1 );
		renderContext.glEnable( GL10.GL_TEXTURE_2D );
		
		float color[] = { 0.3f, 0.7f, 0.1f, 1.0f };
		if ( pending )
		{
			
			color[ 0 ] = 0.7f;
			color[ 1 ] = 0.3f;
			color[ 2 ] = 0.8f;
			color[ 3 ] = 1.0f;
		}
		
		renderContext.glTexEnvfv( GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_COLOR, color, 0 );
		renderContext.glTexEnvx( GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_BLEND );
		
		glBindTexture( GL10.GL_TEXTURE_2D, textureID[ 0 ] );
		
		renderContext.glDrawElements(GL10.GL_TRIANGLES, indices.capacity(), GL10.GL_UNSIGNED_BYTE, indices );
	}
	
	public static MailCharacter getMailCharacter( char requ, Context refContext, GL10 renderContext, byte[] indices )
	{
		
		int texID = characterRepository.get( String.valueOf( requ ) ).intValue();
		
		if ( texID >= 0 )	return new MailCharacter( refContext, renderContext, texID, indices );
		
		return new MailCharacter( refContext, renderContext, requ, indices );
	}
	
	private InputStream getImageStream( char requestChar, Context refContext )
	{
		
		InputStream targetStream;
		
		switch ( requestChar )
		{
		case 'a' :
			
			refContext.getResources().openRawResource( R.drawable.l22_rich ); // TODO : Create all the appropriate images
		default:
			
			targetStream = null;
		}
		
		return targetStream;
	}
	
	private int textureID[];
	private static HashMap< String, Integer > characterRepository = new HashMap< String, Integer >();
	
	private ByteBuffer indices;
	private boolean pending;
}
