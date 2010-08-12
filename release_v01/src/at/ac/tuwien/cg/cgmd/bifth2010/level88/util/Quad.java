package at.ac.tuwien.cg.cgmd.bifth2010.level88.util;


/**
 * Class for representing a Quad
 * 
 * The Quad consists of a position and two base vectors (so
 * the Quad is not axis aligned). The base vectors don't have
 * to be orthogonal, so the Quad represents parallelograms.
 * 
 * @author Asperger, Radax
 */
public class Quad {
	/**
	 * Position of the Quad 
	 */
	private Vector2 position;
	/**
	 * X-Base Vector of the Quad
	 */
	private Vector2 xDir;
	/**
	 * Y-Base Vector of the Quad
	 */
	private Vector2 yDir;
	/** 
	 * Vertexbuffer, used to render the Quad
	 */
	public Vertexbuffers vbos;
	
	
	/**
	 * Constructor
	 * @param _position position of the quad
	 * @param _xDir vector for the x-direction
	 * @param _yDir vector for the y-direction
	 */
	public Quad(Vector2 _position, Vector2 _xDir, Vector2 _yDir) {
		position = _position;
		xDir = _xDir;
		yDir = _yDir;
		
		Vector2[] vertices = {
				new Vector2(position),
				Vector2.add(position, xDir),
				Vector2.add(Vector2.add(position, xDir), yDir),
				Vector2.add(position, yDir)
		};
		/*Short[] indices = {
				0, 1, 2,	2, 3, 0
		};*/
		Vector2[] texCoords = {
				new Vector2(0, 1),
				new Vector2(1, 1),
				new Vector2(1, 0),
				new Vector2(0, 0)
		};
		
		vbos = new Vertexbuffers();
		vbos.setData(Vertexbuffers.Type.POSITION, vertices);
		vbos.setData(Vertexbuffers.Type.TEX_COORD, texCoords);
		//vbos.setData(indices);
	}
}
