package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import java.nio.FloatBuffer;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;

/**
 * The Class TexturePart represents a part of a Texture.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class TexturePart 
{
	/** The upper right corner of the texture part. */
	public Vector2 upperRight;
	/** The lower left corner of the texture part. */
	public Vector2 lowerLeft;
	
	/** The dimension of the texture part. */
	public Vector2 dimension;
	
	/** The texture coordinates of the texture part. */
	public FloatBuffer texCoords;

	
	/**
	 * TexturePart Constructor
	 * @param upperRight the upper right corner of the texture part
	 * @param lowerLeft the lower left corner of the texture part
	 * @param dimension the dimension of the texture part
	 * @param texCoords the texture coordinates of the texture part
	 */
	public TexturePart(Vector2 upperRight, Vector2 lowerLeft, Vector2 dimension, FloatBuffer texCoords)
	{
		this.upperRight = upperRight;
		this.lowerLeft = lowerLeft;
		this.dimension = dimension;
		this.texCoords = texCoords;
	}
}
