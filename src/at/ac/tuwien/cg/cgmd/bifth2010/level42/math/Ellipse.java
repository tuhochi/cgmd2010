package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

public class Ellipse
{
	public final Vector3 center,a,b,pos;

	public float perimeter;
	//temp vars
	private Vector3 aCost,bSint,sumAB;
	private float sint,cost,lambda;

	public Ellipse(Vector3 center, Vector3 a, Vector3 b)
	{
		this.center = center;
		this.a = a;
		this.b = b;

		this.pos = new Vector3();
		
		this.aCost = new Vector3();
		this.bSint = new Vector3();
		this.sumAB = new Vector3();
		this.lambda = 0;
		
		calcPerimeter();
	}

	public void calcPerimeter()
	{
		lambda = (a.length()-b.length())/(a.length()+b.length());
		perimeter = (float)Math.PI * (a.length()+b.length()) * (1 + ((3*(float)Math.pow(lambda, 2))/(10 + ((float)Math.sqrt(4 - 3*((float)Math.pow(lambda, 2)))))));
	}
	
	public Vector3 getPoint(float t)
	{
        //pos = center + a cos(t) + b sin(t) - thx to dr. math 

		aCost.copy(a);
		bSint.copy(b);

		cost = (float)Math.cos(t);
		sint = (float)Math.sin(t);

		aCost.multiply(cost);
		bSint.multiply(sint);
		
		//... a cos(t) + b sin(t)
		sumAB.copy(aCost);
		sumAB.add(bSint);
		
		//center + a cos(t) + b sin(t)
		pos.copy(center);
		pos.add(sumAB);
		
		return pos;
	}
}