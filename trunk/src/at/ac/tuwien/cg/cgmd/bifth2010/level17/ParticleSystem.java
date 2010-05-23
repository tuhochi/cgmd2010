package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
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
	public ParticleSystem(Vector3 position, int textureID, Quad quad)
	{
		mPosition = position;
		mTextureID = textureID;
		mQuad = quad;
		Random r = new Random();
		int numParticles = 10 + r.nextInt(5);
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
		List<Particle> removeParticles = new ArrayList<Particle>();
		mPosition = currentPlayerPosition;
		for(Particle particle: mParticles)
		{
			particle.update(elapsedSeconds);
			if (!particle.isActive())
				removeParticles.add(particle);
		}
		for(Particle particle: removeParticles)
			mParticles.remove(particle);
	}
	
	/**
	 * draws all particles of the system
	 */
	public void draw()
	{
		MatrixTrackingGL gl = GLManager.getInstance().getGLContext();
        gl.glDisable(GL10.GL_CULL_FACE);
		GLManager.getInstance().getTextures().setTexture(mTextureID);
		for(Particle particle: mParticles)
			particle.draw(mPosition, mQuad);
        gl.glEnable(GL10.GL_CULL_FACE);
	}
	
	/**
	 * Returns if the particle system is still active
	 * @return true if there is at least one active particle
	 */
	public boolean isActive()
	{
		return (!mParticles.isEmpty());
	}
}
