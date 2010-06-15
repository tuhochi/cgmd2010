package at.ac.tuwien.cg.cgmd.bifth2010.level60;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

/**
 * Cop class for level 60. Cop instances are game objects which go after the bunny
 * when it has committed a crime. Apart from animation the cop also has a collision
 * detection and intelligence about where it can walk and which direction it should
 * go to follow the bunny.
 * 
 * @author      Martin Schenk
 * @author      Tiare Feuchtner
 */
public class Cop extends Tablet {
	private textureManager texman;
	
	/**
	 * Constructor of the cop class.
	 * 
	 * @param initPosX	x-coordinate at which the cop is set originally
	 * @param initPosY	y-coordinate at which the cop is set originally
	 * @param context	activity context
	 * @param gl	the GL interface
	 * @param man	texture manager used to load the different textures for animation
	 */
	public Cop(int initPosX, int initPosY, Context context, GL10 gl, textureManager man) {
		super(50, 50, initPosX, initPosY, man.getTexture("cop_front_l"));
		texman = man;
	}
}
