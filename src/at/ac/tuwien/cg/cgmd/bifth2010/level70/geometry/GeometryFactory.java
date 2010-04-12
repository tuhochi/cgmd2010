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
		// Origin is upper left
		float texcoords[] = { 0, 1,  1, 1,  1, 0,  0, 0 };
		short indices[] = { 0, 1, 2,  0, 2, 3 };
		return new Geometry(positions, texcoords, indices);
	}
	
	
	/**
	 * Create quad.
	 * @param width width of the quad
	 * @param height height of the quad
	 * @return Quad geometry.
	 */
	public Geometry createSprite(int width, int height) {
		float w = width / 2.0f;
		float h = height / 2.0f;
		
		float positions[] = { -w, -h,  0.0f,
			      	           w, -h,  0.0f,
			      			   w,  h,  0.0f,
			      			  -w,  h,  0.0f };
		// Origin is upper left
		float texWidth   = 512.0f;
		float texHeight  = 128.0f;
		float tileWidth  = 40.0f;
		float tileHeight = 40.0f;
		
		float ix = 0;
		float iy = 0;
		float tw = tileWidth / texWidth;
		float th = tileHeight /texHeight;
		
		float texcoords[] = { ix,      iy + th,  
				              ix + tw, iy + th,  
				              ix + tw, iy,
				              ix,      iy       };
		short indices[] = { 0, 1, 2,  0, 2, 3 };
		return new Geometry(positions, texcoords, indices);
	}
}
