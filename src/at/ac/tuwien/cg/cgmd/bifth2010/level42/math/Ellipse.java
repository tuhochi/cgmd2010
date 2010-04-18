package at.ac.tuwien.cg.cgmd.bifth2010.level42.math;

/**
 * The Class Ellipse
 * Encapsulates the parametric form of the ellipse
 * 
 * @author Alex Druml
 * @author Lukas R�ssler
 */
public class Ellipse
{
	
	/** The center of the ellipse */
	public Vector3 center;
	
	/** The major axis (t=0) */
	public Vector3 a;
	
	/** The secondary axis */
	public Vector3 b;
	
	/** The position along the ellipse */
	public Vector3 pos;

	/** The perimeter of the ellipse */
	public float perimeter;
	
	//temp vars
	private Vector3 aCost,bSint,sumAB;
	private float sint,cost,lambda;

	/**
	 * Instantiates a new ellipse.
	 *
	 * @param center the ellipse
	 * @param a the major axis (t=0)
	 * @param b the secondary axis
	 */
	public Ellipse(Vector3 center, Vector3 a, Vector3 b)
	{
		//init
		this.pos = new Vector3();
		this.aCost = new Vector3();
		this.bSint = new Vector3();
		this.sumAB = new Vector3();
		this.lambda = 0;
		
		this.center = center;
		this.a = a;
		this.b = b;
	
		calcPerimeter();
	}

	/**
	 * Compute a approximation for the perimeter of the ellipse
	 */
	public void calcPerimeter()
	{
		lambda = (a.length()-b.length())/(a.length()+b.length());
		perimeter = (float)Math.PI * (a.length()+b.length()) * (1 + ((3*(float)Math.pow(lambda, 2))/(10 + ((float)Math.sqrt(4 - 3*((float)Math.pow(lambda, 2)))))));
	}
	
	/**
	 * Gets the point on the ellipse for a given parameter t
	 * @param t the parameter
	 * @return the corresponding point on the ellipse
	 */
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