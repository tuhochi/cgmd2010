/**
 * 
 */
package at.ac.tuwien.cg.cgmd.bifth2010.level20;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;


/**
 * Used to create a simple particle effect.
 * 
 * @author Ferdinand Pilz
 * @author Reinhard Sprung
 */
public class ParticleEngine {

	/** The list of existing particle systems. */
	static private ArrayList<ParticleSystem> particleSystems;		
	/** The textures to use for the particles. */
	protected int[] textureIds;
	/** Common speed variable for the particles. */
	protected float speed;
	/** Size of the particles. */
	protected float size;
	/** Number of particles to create. */
	protected int nParticles;
	/** Particle lifetime. */
	protected float lifetime;
	
	ParticleEngine() {
		particleSystems = new ArrayList<ParticleSystem>();
		speed = 1f;
		textureIds = new int[0];
		nParticles = 10; 
		size = 16f;
		lifetime = 1500;
	}
	
	public void createParticleSystem(float posX, float posY) {
		particleSystems.add(new ParticleSystem(nParticles, posX, posY, size, speed));
	}
	
	public void update(float dt) {
		// Go over particle systems.
		// Cycle through the ArrayList backwards b/c we are deleting
		for (int i = particleSystems.size() - 1; i >= 0; i--) {						 
			particleSystems.get(i).update(dt);
			if (particleSystems.get(i).dead()) {
				particleSystems.remove(i);
			}
		}
	}
	
	public void render(GL10 gl) {
		for (int i = 0; i < particleSystems.size(); i++) {						 
			particleSystems.get(i).render(gl);
		}
	}
	
	public class ParticleSystem {
		/** The list of particles. */
		private ArrayList<ParticleEntity> particles;
		
		ParticleSystem(int numParticles, float posX, float posY, float size, float speed) {
			particles = new ArrayList<ParticleEntity>();

			for (int i = 0; i < numParticles; i++) {
				ParticleEntity particle = new ParticleEntity(posX, posY, 5, size,
						size, speed);
				int texId = ((int)(Math.random() * 10)) % textureIds.length;
				particle.texture = textureIds[texId];
				particle.lifetime = lifetime;
				particles.add(particle);
			}	
		}
		
		public void update(float dt) {
			// Cycle through the ArrayList backwards b/c we are deleting
			for (int j = particles.size() - 1; j >= 0; j--) {
				ParticleEntity particle = particles.get(j);
				particle.update(dt);
				if (particle.dead()) {
					particles.remove(j);
				}
			}
		}
		
		/** Checks if the particle system still has particles. */
		public boolean dead() {
			if (particles.isEmpty()) {
				return true;
			} else {
				return false;
			}
		}

		public void render(GL10 gl) {
			for (int i = 0; i < particles.size(); i++) {
				particles.get(i).render(gl);				
			}			
		}		
	}
	

}
