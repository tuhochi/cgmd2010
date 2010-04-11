package at.ac.tuwien.cg.cgmd.bifth2010.level70.renderer;

import java.util.ArrayList;

import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GeometryFactory;

/**
 * Game scene.
 * @author herrjohann
 */
public class GameScene {

	// ----------------------------------------------------------------------------------
	// -- Members ----
	
	/** List of game objects */
	private ArrayList<Geometry> geoms;
	
	
	// ----------------------------------------------------------------------------------
	// -- Ctor / Dtor ----
	
	/**
	 * Ctor. Create game scene.
	 */
	public GameScene() 
	{
		
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Public methods ----
	
	public void create(int windowWidth, int windowHeight)
	{
		createGeometry();
	}
	
	/**
	 * Return geometry
	 */
	public ArrayList<Geometry> getGeometry() {
		return geoms;
	}
	
	
	// ----------------------------------------------------------------------------------
	// -- Private methods ----
	
	/**
	 * Create game objects
	 */
	private void createGeometry() {
		geoms = new ArrayList<Geometry>();
		GeometryFactory gf = new GeometryFactory();
		Geometry geom = gf.createQuad(1, 1);
		geoms.add(geom);
	}
}
