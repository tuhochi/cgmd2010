package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import java.io.Serializable;

/**
 * This interface provides the method render, which is obligatory for every Scene Entity
 *
 * @author Markus Ernst
 * @author Florian Felberbauer
 *
 */
public interface SceneEntity extends Serializable
{
	/**
	 * Rendering of the SceneEntity should be implemented in this method
	 */
	public void render();
}
