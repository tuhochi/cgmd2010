package at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry;

public class GeometryFactory {

	/**
	 * Create quad.
	 * @param width width of the quad
	 * @param height height of the quad
	 * @return Quad geometry.
	 */
	public Geometry createQuad(int width, int height) {
		float w = width / 2.0f;
		float h = height / 2.0f;
		
		float positions[] = { -w, -h,  0.0f,
			      	           w, -h,  0.0f,
			      			   w,  h,  0.0f,
			      			  -w,  h,  0.0f };
		short indices[] = { 0, 1, 2, 0, 2, 3 };
		return new Geometry(positions, indices);
	}
}
