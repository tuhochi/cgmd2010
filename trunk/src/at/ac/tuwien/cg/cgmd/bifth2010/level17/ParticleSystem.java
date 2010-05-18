package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Quad;

/**
 * A class that manages a particle system
 * @author Johannes Scharl
 *
 */
public class ParticleSystem {
	private Vector3 mPosition;
	private Quad mQuad;
	private int mTextureID;
	private List<Particle> mParticles = new ArrayList<Particle>();
	
	/**
	 * constructs a new particle system
	 * @param position the initial position of the particle system
	 * @param textureID the texture that is used for the particles
	 */
	public ParticleSystem(Vector3 position, int textureID)
	{
		mPosition = position;
		mTextureID = textureID;
		mQuad = new Quad(0.2f, 0.2f);
		Random r = new Random();
		int numParticles = 5 + r.nextInt(3);
		for (int i=0; i<numParticles; i++)
			mParticles.add(new Particle(r));
	}
	
	/**
	 * Updates the particle system
	 * @param elapsedSeconds the elapsed seconds sice the last update
	 * @param currentPlayerPosition the current player position
	 */
	public void update(float elapsedSeconds, Vector3 currentPlayerPosition)
	{
		mPosition.y = currentPlayerPosition.y;
		for(Particle particle: mParticles)
			particle.update(elapsedSeconds);
	}
	
	/**
	 * draws all particles of the system
	 */
	public void draw()
	{
		GLManager.getInstance().getTextures().setTexture(mTextureID);
		for(Particle particle: mParticles)
			particle.draw(mPosition, mQuad);
	}
}
