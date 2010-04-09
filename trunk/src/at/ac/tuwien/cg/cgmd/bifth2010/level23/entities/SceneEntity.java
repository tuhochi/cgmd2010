package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import java.io.Serializable;

/**
 * @author Markus Ernst
 * @author Florian Felberbauer
 *
 */

/**
 * This interface provides the method render, which is obligatory for every Scene Entity
 */
public interface SceneEntity extends Serializable
{
	public void render();
}
