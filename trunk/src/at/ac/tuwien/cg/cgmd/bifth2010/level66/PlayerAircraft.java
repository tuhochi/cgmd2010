package at.ac.tuwien.cg.cgmd.bifth2010.level66;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class PlayerAircraft extends Model {
	
	private int _accX;
	private int _accY;
	
	private final float	ROLL_STEPSIZE = 1.0f;
	private final float PITCH_STEPSIZE = 1.0f;
	
	private final int	ACC_STEPSIZE = 10;
	private final int	ACC_FRAME_STEPSIZE = 1;
	private final int	ROLL_TO_POS_RATIO = 450;
	private final int	PITCH_TO_POS_RATIO = 450;
	private final int	CNT_FRAMES_TILL_ACC_DEC = 10;
	private final int	SPECIAL_ROLL_STEPSIZE = 10;
	
	private final int	SPECIAL_NONE = 0;
	private final int	SPECIAL_ROLL = 1;
	
	private int _cnt_frames_x;
	private int _cnt_frames_y;
	
	private int _special;
	private float _special_roll;
	private int _special_roll_dir;
	
	public PlayerAircraft(Context context)
	{
		super(context);
		
		this.load("l66_baum.obj", context);
		
		// only render coordination system - testing purpose only
		_renderCoord = false;
		
		_accX = 0;
		_accY = 0;
		
		_special = 0;
		
		_posZ = -5.0f;
		
		_scale = 0.1f;
		
		_cnt_frames_x = 0;
		_cnt_frames_y = 0;
	}
	
	private void rollInc()
	{
		_roll += ROLL_STEPSIZE;
		
		if ( _roll > 45.0f)
			_roll = 45.0f;
	}
	
	private void rollDec()
	{
		_roll -= ROLL_STEPSIZE;
		
		if ( _roll < -45.0f )
			_roll = -45.0f;
	}
	
	private void pitchInc()
	{
		_pitch += PITCH_STEPSIZE;
		
		if ( _pitch > 45.0f)
			_pitch = 45.0f;
	}
	
	private void pitchDec()
	{
		_pitch -= PITCH_STEPSIZE;
		
		if ( _pitch < -45.0f )
			_pitch = -45.0f;
	}
	
	public void specialMoveRoll()
	{
		if ( _special == SPECIAL_NONE )
		{
			_special = SPECIAL_ROLL;
			_special_roll = 360;
			
			if ( _roll == 0)
			{
				float rndVal = (float) Math.random() - 0.5f;
				_special_roll_dir = (int) ( rndVal / Math.abs( rndVal ) );
			}
			else
				_special_roll_dir = (int) ( _roll / Math.abs(_roll) );
		}
	}
	
	public void move()
	{
	// X - ACCELERATION UPDATE
		if ( _accX != 0 )
		{
			if ( _accX > 0 )
			{
				rollInc();
			}
			else
			{
				rollDec();
			}
			
			if ( _accX != 0 )
				_accX = _accX + _accX / Math.abs(_accX) * -1 * ACC_FRAME_STEPSIZE;
			
			_cnt_frames_x = CNT_FRAMES_TILL_ACC_DEC;
		}
		else
		{
			if ( _cnt_frames_x > 0 )
			{
				_cnt_frames_x--;
			}
			else
			{
				if ( _roll != 0 )
				{
					if ( _roll > 0 )
						rollDec();
					else
						rollInc();
				}
			}
		}
		
	// Y - ACCELERATION UPDATE
		if ( _accY != 0 )
		{
			if ( _accY > 0 )
			{
				pitchInc();
			}
			else
			{
				pitchDec();
			}
			
			if ( _accY != 0 )
				_accY = _accY + _accY / Math.abs(_accY) * -1 * ACC_FRAME_STEPSIZE;
			
			_cnt_frames_y = CNT_FRAMES_TILL_ACC_DEC;
		}
		else
		{
			if ( _cnt_frames_y > 0 )
			{
				_cnt_frames_y--;
			}
			else
			{
				if ( _pitch != 0 )
				{
					if ( _pitch > 0 )
						pitchDec();
					else
						pitchInc();
				}
			}
		}
		
	// POSITION UPDATE
		
		_posX = _posX + _roll / ROLL_TO_POS_RATIO;
		if ( _posX > 1.5f)
			_posX = 1.5f;
		else if ( _posX < -1.5f)
			_posX = -1.5f;
		
		_posY = _posY + _pitch / PITCH_TO_POS_RATIO;
		
		if ( _posY > 2.5f)
			_posY = 2.5f;
		else if ( _posY < -2.5f)
			_posY = -2.5f;
		
		switch ( _special )
		{
			case SPECIAL_ROLL:
				_special_roll -= SPECIAL_ROLL_STEPSIZE;
				if ( _special_roll <= 0 )
				{
					_special_roll = 0;
					_special = SPECIAL_NONE;
				}
				break;
		}
	}
	
	public void moveLeft()
	{
		_accX -= ACC_STEPSIZE;
	}
	
	public void moveRight()
	{
		_accX += ACC_STEPSIZE;
	}
	
	public void moveUp()
	{
		_accY += ACC_STEPSIZE;
	}
	
	public void moveDown()
	{
		_accY -= ACC_STEPSIZE;
	}
	
	public void render(GL10 gl)
	{
		if( _renderCoord )
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _coordVertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _coordColorBuffer);
	    
		    gl.glLoadIdentity();
		    
		    // set translation
		    gl.glTranslatef(_posX, _posY, _posZ);
		    // set scale
		    gl.glScalef( _scale, _scale, _scale);
		    // set rotation
		    gl.glRotatef( _pitch, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _yaw, 0.0f, 1.0f, 0.0f);
		    
		    if ( _special == SPECIAL_ROLL )
		    	gl.glRotatef(-_roll + _special_roll_dir * _special_roll, 0.0f, 0.0f, 1.0f);
		    else
		    	gl.glRotatef(-_roll, 0.0f, 0.0f, 1.0f);
		    
		    gl.glDrawElements(GL10.GL_LINES, 12, GL10.GL_UNSIGNED_SHORT, _coordIndexBuffer);
		}
		else
		{
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, _vertexBuffer);
	        gl.glColorPointer(4, GL10.GL_FLOAT, 0, _colorBuffer);
	        gl.glNormalPointer(GL10.GL_FLOAT, 0, _normalBuffer);
	        //gl.glTexCoordPointer(3, GL10.GL_FLOAT, 0, _texCoordBuffer);
	    
		    gl.glLoadIdentity();
		    
		    // set translation
		    gl.glTranslatef(_posX, _posY, _posZ);
		    // set scale
		    gl.glScalef(_scale, _scale, _scale);
		    // set rotation
		    //gl.glRotatef( -90, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _pitch, 1.0f, 0.0f, 0.0f);
		    gl.glRotatef( _yaw, 0.0f, 1.0f, 0.0f);
		    
		    if ( _special == SPECIAL_ROLL )
		    	gl.glRotatef(-_roll + _special_roll_dir * _special_roll, 0.0f, 0.0f, 1.0f);
		    else
		    	gl.glRotatef(-_roll, 0.0f, 0.0f, 1.0f);
		    
		    gl.glRotatef( -90, 1.0f, 0.0f, 0.0f);
		    
		    //gl.glDrawElements(GL10.GL_TRIANGLES, _indexBuffer.array().length / 4, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
		    gl.glDrawElements(GL10.GL_TRIANGLES, _cntVertices, GL10.GL_UNSIGNED_SHORT, _indexBuffer);
		}
	}
}
