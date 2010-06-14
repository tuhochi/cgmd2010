package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

/**
 * Mirror modes for textures
 * 
 * This is a simple enum that will determine if a texture
 * is to be mirrored when drawn to the screen. The default
 * is "NONE". If mirroring is active, the textures will
 * simply exchange the X and/or Y coordinates, which will
 * result in the mirroring effect.
 * 
 * @author Thomas Perl
 */
public enum Mirror {
	NONE,       /** Don't mirror */
	HORIZONTAL, /** Mirror horizontally */
	VERTICAL,   /** Mirror vertically */
	BOTH,       /** Mirror along both axes */
}
