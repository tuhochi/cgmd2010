package at.ac.tuwien.cg.cgmd.bifth2010.level13;

/**
 * 
 * @author sebastian (group 13)
 *
 */

	//Garbage collector efficient 2D Vector class

	public class Vector2
	{
		public float x, y;

		public Vector2 ()
		{
			x = 0;
			y = 0;
		}

		public Vector2 (float _x, float _y)
		{
			x = _x;
			y = _y;
		}

		public Vector2 (Vector2 v)
		{
			x = v.x;
			y = v.y;
		}

		public void setXY(float x, float y){
			this.x = x;
			this.y = y;
		}

		public float dotProduct (Vector2 v1)
		{
			return x*v1.x + y*v1.y;
		}

		public void add (Vector2 v1)
		{
			this.x+=v1.x;
			this.y+=v1.y;
		}
		
		public void sub (Vector2 other)
		{
			this.x -= other.x;
			this.y -= other.y;
		}
		
		public void mult (float s)
		{
			this.x *=s;
			this.y *=s;
		}
		
		public float length()
		{
			return (float) Math.sqrt (x*x + y*y);
		}

		public void normalize ()
		{
			float thelen = (float) Math.sqrt (x*x + y*y);

			if (thelen != 0) 
			{
				x /= thelen;
				y /= thelen;
			}
		}
		
		public void invert()
		{
			x = -x;
			y = -y;
		}
		
		public float getAngle()
		{
			float angle = 0;
			if(x < 0)
				angle = (float)(Math.atan(y / x) + Math.PI);
			else if(y < 0)
				angle = (float)(Math.atan(y / x) + 2.0 * Math.PI);
			else
				angle = (float)Math.atan(y / x);
			
			return angle;
		}


		public Vector2 clone(){
			Vector2 evilTwin = new Vector2();
			evilTwin.x = x;
			evilTwin.y = y;
			return evilTwin;
			
		}
		
		public int signX(){
			if (this.x >= 0)
				return 1;
			else
				return -1;
		}
		
		public int signY(){
			if (this.y >= 0)
				return 1;
			else
				return -1;
		}
		
		
		
		/**
		 * Debug output.
		 */
		
		public String toString ()
		{
			return ("(" + x + "," + y + ")");
		}
	}
