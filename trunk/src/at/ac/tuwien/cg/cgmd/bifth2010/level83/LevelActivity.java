package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;

import android.os.Bundle;

import at.ac.tuwien.cg.cgmd.bifth2010.level83.common.*;


public class LevelActivity extends GameActivity implements GameListener{

	private FloatBuffer vBuffer;
	private FloatBuffer cBuffer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setGameListener(this);
	}

	@Override
	public void mainLoopIteration(GameActivity activity, GL10 gl) {
		gl.glViewport(0, 0, activity.getViewportWidth(), activity.getViewportHeight());
		
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, cBuffer);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuffer);
		
		gl.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
	}

	@Override
	public void setup(GameActivity gameActivity, GL10 gl) {	
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(3*3*4);
		buffer.order(ByteOrder.nativeOrder());
		vBuffer = buffer.asFloatBuffer();
		
		vBuffer.put( -0.5f );
		vBuffer.put( -0.5f );
		vBuffer.put( 0 );		
		vBuffer.put( 0.5f );
		vBuffer.put( -0.5f );
		vBuffer.put( 0 );	
		vBuffer.put( 0 );
		vBuffer.put( 0.5f );
		vBuffer.put( 0 );
		
		vBuffer.position(0);
		
		ByteBuffer b = ByteBuffer.allocateDirect( 3 * 4 * 4 );
		b.order(ByteOrder.nativeOrder());
		cBuffer = b.asFloatBuffer();	
		cBuffer.put( 1 );
		cBuffer.put( 0 );
		cBuffer.put( 0 );
		cBuffer.put( 1 );
		cBuffer.put( 0 );
		cBuffer.put( 1 );
		cBuffer.put( 0 );
		cBuffer.put( 1 );	
		cBuffer.put( 0 );
		cBuffer.put( 0 );
		cBuffer.put( 1 );
		cBuffer.put( 1 );	
		cBuffer.rewind();
	}
}
