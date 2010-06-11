package at.ac.tuwien.cg.cgmd.bifth2010.level30;
import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level30.math.Vector3;

/*
 * One particle system with update methods
 */
public class ParticleSystem {
	
	private List<Particle> particles;
	
	/*
	 * properties of particle system
	 */	
	private Vector3 center;
	private Vector3 centerDeviation;	
	private float speed;
	private float speedDeviation;	
	private float size;
	private float sizeDeviation;
	private float sizeIncrease;
	private float sizeIncreaseDeviation;
	private float lifeTime;
	private float lifeTimeDeviation;	
	private float particlesPerSecond;
	private Vector3 color;
	
	private float particlesToCreate = 0.0f;
	
	private boolean isExploding = false;
		
	public void SetCenter(Vector3 _center)
	{
		center = _center;
	}
	
	public void SetSize(float _size)
	{
		size = _size;
	}
	
	public void SetColor(Vector3 _color)
	{
		color = _color;
	}

	public 	ParticleSystem(Vector3 _center,Vector3 _centerDeviation,
			float _speed, float _speedDeviation,
			float _size, float _sizeDeviation,
			float _sizeIncrease, float _sizeIncreaseDeviation,
			float _lifeTime, float _lifeTimeDeviation,
			float _particlesPerSecond,
			Vector3 _color)
	{
		center = _center ;
		centerDeviation = _centerDeviation;		
		speed = _speed;
		speedDeviation = _speedDeviation;		
		size = _size;
		sizeDeviation = _sizeDeviation;	
		sizeIncrease = _sizeIncrease;
		sizeIncreaseDeviation = _sizeIncreaseDeviation;
		lifeTime = _lifeTime;
		lifeTimeDeviation = _lifeTimeDeviation;
		particlesPerSecond = _particlesPerSecond;
		color = _color;
		
		particles = new ArrayList<Particle>();
		
	}
	
	/*
	 * Get a random number in the interval [-1,1]
	 */
	private float RandM11()
	{
		return ((float)Math.random()-0.5f)*2.0f;		
	}
	
	/*
	 * Get a random number centered around "center" with deviation of "deviation"
	 */
	private float getRandom(float center, float deviation)
	{
		float randDeviation = RandM11()*deviation;		
		return center+randDeviation;
	}
	
	/*
	 * Add and init "amount" particles
	 */
	public void AddParticles(int amount)
	{
		for (int i=0; i<amount; i++)
		{
			//safety guard
			if (particles.size()>100)
				return;
				
			Particle newParticle = new Particle();
			
			//set lifetimes
			newParticle.lifeTime = 0.0f;
			newParticle.maxLifeTime = getRandom(lifeTime, lifeTimeDeviation);
			
			//set position
			newParticle.position.x = getRandom(center.x, centerDeviation.x);
			newParticle.position.y = getRandom(center.y, centerDeviation.y);
			newParticle.position.z = getRandom(center.z, centerDeviation.z);
			
			//set size
			newParticle.size = getRandom(size, sizeDeviation);
			newParticle.sizeIncrease = getRandom(sizeIncrease, sizeIncreaseDeviation);

			//get a random direction
			newParticle.speed.x = RandM11();
			newParticle.speed.y = RandM11();
			newParticle.speed.z = RandM11();

			//normalize this direction
			if (newParticle.speed.length()>0.00001f)				
				newParticle.speed.normalize();
			else
			{
				newParticle.speed = new Vector3(1,0,0);
			}
			
			newParticle.color = color;
			
			//set length of speed to desired speed
			float desiredSpeed = getRandom(speed, speedDeviation);			
			newParticle.speed = Vector3.mult(newParticle.speed,desiredSpeed);
			
			particles.add(newParticle);
		}
	}
	
	public void StartExplosion()
	{		
		if (isExploding==false)
		{
			isExploding = true;
			particlesPerSecond = 0.0f;		
			
			//change the velocities of the particles to create an explosion
			for(Particle particle: particles)
			{		
				particle.speed.normalize();
				particle.speed = Vector3.mult(particle.speed,speed*50.0f);
				particle.speed.y = Math.abs(particle.speed.y); 
				
			}		
			
		}
	}
	
	public boolean ExplosionFinished()
	{
		if ((isExploding==true)&&(particles.size()==0))
		{
			return true;
		}
		else
			return false;
			
	}
		
	public void Update(float deltaTime)
	{	
		List<Particle> particlesToRemove = new ArrayList<Particle>();
		
		for(Particle particle: particles)
		{			
			particle.lifeTime += deltaTime;
			if (particle.lifeTime > particle.maxLifeTime)
			{
				particlesToRemove.add(particle);
				continue;
			}
			
			//integrate position over time
			particle.position = Vector3.add(Vector3.mult(particle.speed, deltaTime), particle.position);
			particle.size += particle.sizeIncrease*deltaTime;	
			
			//also add gravity to explosions
			if (isExploding)
			{
				particle.speed.y -= 0.1;
			}
		}	
		
		for(Particle particle: particlesToRemove)
		{
			particles.remove(particle);
		}
		
		particlesToCreate += particlesPerSecond*deltaTime;
		
		if (particlesToCreate>1.0f)
		{
			AddParticles((int)particlesToCreate);
			particlesToCreate -= Math.floor(particlesToCreate);
		}
		
	}
	
	public void Draw(RenderableQuad quad, GL10 gl)
	{			
		for(Particle particle: particles)
		{
	    	gl.glColor4f(particle.color.x, particle.color.y, particle.color.z, 0.8f);
	    	
			gl.glPushMatrix();			
			gl.glTranslatef(particle.position.x,particle.position.y,particle.position.z);
			gl.glScalef(particle.size, particle.size, particle.size);
			quad.draw(gl);
			gl.glPopMatrix();		
		}	
						
	}
	

}
