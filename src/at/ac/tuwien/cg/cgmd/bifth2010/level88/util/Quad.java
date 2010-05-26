package at.ac.tuwien.cg.cgmd.bifth2010.level88.util;


/**
 * Class for representing a quad
 * @author Asperger, Radax
 */
public class Quad {
	/** Vector containing the position*/
	private Vector2 position;
	/** Vector containing the information for the x-direction*/
	private Vector2 xDir;
	/** Vector containing the information for the y-direction*/
	private Vector2 yDir;
	/** Vertexbuffer containg the information of the quad*/
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
