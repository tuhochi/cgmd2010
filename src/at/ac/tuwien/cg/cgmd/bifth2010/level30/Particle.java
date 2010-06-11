package at.ac.tuwien.cg.cgmd.bifth2010.level30;
import at.ac.tuwien.cg.cgmd.bifth2010.level30.math.Vector3;

/*
 * Stores information of one particle
 */
public class Particle {

	public float lifeTime;
	public float maxLifeTime;	
	public Vector3 position;
	public Vector3 speed;
	public Vector3 color;
	public  float size;
	public  float sizeIncrease;
	
	public Particle()
	{
		position = new Vector3(0,0,0);
		speed = new Vector3(0,0,0);	
		color = new Vector3(1,1,1);
	}
}
