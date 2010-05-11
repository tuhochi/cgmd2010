package at.ac.tuwien.cg.cgmd.bifth2010.level23.entities;

import java.nio.FloatBuffer;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;

public class TexturePart 
{
	public Vector2 upperRight;
	public Vector2 lowerLeft;
	
	public Vector2 dimension;
	
	public FloatBuffer texCoords;

	public TexturePart(Vector2 upperRight, Vector2 lowerLeft, Vector2 dimension, FloatBuffer texCoords)
	{
		this.upperRight = upperRight;
		this.lowerLeft = lowerLeft;
		this.dimension = dimension;
		this.texCoords = texCoords;
	}
}
