package at.ac.tuwien.cg.cgmd.bifth2010.level70.util;

/**
 * Factory method to create various geometry types. Currently the only
 * supported geometry is a quad build with two triangles.
 * 
 * @author Christoph Winklhofer
 */
public class GeometryFactory {

    // ----------------------------------------------------------------------------------
    // -- Public methods ----
    
	/**
	 * Create geometry for a quad.
	 * @param cx Center coordinate x
	 * @param cy Center coordinate y
	 * @param w Width of the quad
	 * @param h Height of the quad
	 * @return Quad geometry.
	 */
	public static Geometry createQuad(float cx, float cy, float w, float h) {
		
	    float whalf = w / 2.0f;
		float hhalf = h / 2.0f;
		
		float positions[] = { cx - whalf, cy - hhalf, 0.0f,
			      	          cx + whalf, cy - hhalf, 0.0f,
			      			  cx + whalf, cy + hhalf, 0.0f,
			      			  cx - whalf, cy + hhalf, 0.0f };
		// Origin is upper left
		float texcoords[] = { 0, 1,  1, 1,  1, 0,  0, 0 };
		short indices[] = { 0, 1, 2,  0, 2, 3 };
		return new Geometry(positions, texcoords, indices);
	}
}
