package at.ac.tuwien.cg.cgmd.bifth2010.level66;

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
	
	private int cnt_frames_x;
	private int cnt_frames_y;
	
	public PlayerAircraft()
	{
		super();
		
		_accX = 0;
		_accY = 0;
		
		cnt_frames_x = 0;
		cnt_frames_y = 0;
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
			
			cnt_frames_x = CNT_FRAMES_TILL_ACC_DEC;
		}
		else
		{
			if ( cnt_frames_x > 0 )
			{
				cnt_frames_x--;
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
			
			cnt_frames_y = CNT_FRAMES_TILL_ACC_DEC;
		}
		else
		{
			if ( cnt_frames_y > 0 )
			{
				cnt_frames_y--;
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
}
