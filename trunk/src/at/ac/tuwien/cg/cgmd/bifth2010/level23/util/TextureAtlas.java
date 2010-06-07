package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;
import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.TexturePart;

/**
 * The Class TextureAtlas implements the management of texturecoordinates.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class TextureAtlas 
{	
	/** The texture id for the atlas texture. */
	int textureIdAtlas;
	/** The texture id for the background texture. */
	int textureIdAtlasBg;
	/** The dimension of the atlas texture. */
	Vector2 atlasDimension;
	/** The instance of TextureAtlas to pass around. */
	public static TextureAtlas instance = new TextureAtlas();
		
	/**
	 * Default Constructor
	 */
	public TextureAtlas()
	{
		atlasDimension = new Vector2(1024,1024);		
	}

	/**
	 * Loads the textures used in the TextureAtlas
	 */
	public void loadAtlasTexture()
	{
		int resID = LevelActivity.instance.getResources().getIdentifier("l23_textureatlas", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		textureIdAtlas = TextureManager.instance.getTextureId(LevelActivity.instance.getResources(), resID);
		resID = LevelActivity.instance.getResources().getIdentifier("l23_textureatlasbg", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		textureIdAtlasBg = TextureManager.instance.getTextureId(LevelActivity.instance.getResources(), resID);
	}
	
	/**
	 * Binds the TextureAtlas texture
	 */
	public void bindAtlasTexture()
	{
		glBindTexture(GL_TEXTURE_2D, textureIdAtlas);
	}
	
	/**
	 * Binds the texture used for the background
	 */
	public void bindAtlasBgTexture()
	{
		glBindTexture(GL_TEXTURE_2D, textureIdAtlasBg);
	}
	
	/**
	 * Returns a texture part for the given pixelvalues
	 * @param lowerLeft lower left corner of the texture part (in pixels)
	 * @param upperRight upper right corner of the texture part (in pixels)
	 * @return the chosen texturepart
	 */
	public TexturePart generateTexturePart(Vector2 lowerLeft, Vector2 upperRight)
	{

		FloatBuffer texCoordBuffer;
		float[] textureCoordinates = new float[8];
		
		//normalize pixel coordinates
		Vector2 tempLowerLeft = new Vector2(lowerLeft.x/atlasDimension.x, 1-lowerLeft.y/atlasDimension.y);
		Vector2 tempUpperRight = new Vector2((upperRight.x/atlasDimension.x), 1-(upperRight.y/atlasDimension.y));;

		textureCoordinates[0] = tempLowerLeft.x;
		textureCoordinates[1] = tempLowerLeft.y;

		textureCoordinates[2] = tempUpperRight.x;
		textureCoordinates[3] = tempLowerLeft.y;

		textureCoordinates[4] = tempLowerLeft.x;
		textureCoordinates[5] = tempUpperRight.y;

		textureCoordinates[6] = tempUpperRight.x;
		textureCoordinates[7] = tempUpperRight.y;
		
		ByteBuffer tcbb = ByteBuffer.allocateDirect(textureCoordinates.length * 4);
		tcbb.order(ByteOrder.nativeOrder());
		texCoordBuffer = tcbb.asFloatBuffer();
		texCoordBuffer.put(textureCoordinates);
		texCoordBuffer.position(0);		
		
		return new TexturePart(tempUpperRight, tempLowerLeft, new Vector2(tempUpperRight.x-tempLowerLeft.x,tempLowerLeft.y-tempUpperRight.y), texCoordBuffer); 
	}
	
	/**
	 * Returns the part of the texture for the mainchar
	 * @return the texture part for the mainchar
	 */
	public TexturePart getMainCharTextur()
	{
		return generateTexturePart(new Vector2(896,256), new Vector2(896+128,256+256));
	}
	
	/**
	 * Returns the part of the texture for a cloud
	 * @return the texture part for a cloud
	 */
	public TexturePart getCloudTextur()
	{
		return generateTexturePart(new Vector2(768,0), new Vector2(768+128,64));
	}
	
	/**
	 * Returns the part of the texture for the progressbar
	 * @return the texture part for the progressbar
	 */
	public TexturePart getProgressBarTextur()
	{
		return generateTexturePart(new Vector2(768,512), new Vector2(768+128,512+16));
	}
	
	/**
	 * Returns the part of the texture for the cow obstacle
	 * @return the texture part for the cow obstacle
	 */
	public TexturePart getCowTextur()
	{
		return generateTexturePart(new Vector2(0,512), new Vector2(0+256,512+192));
	}
	
	/**
	 * Returns the part of the texture for the amboss obstacle
	 * @return the texture part for the mainchar
	 */
	public TexturePart getAmbossTextur()
	{
		return generateTexturePart(new Vector2(896,640), new Vector2(896+64,640+64));
	}
	
	/**
	 * Returns the part of the texture for the boost button
	 * @return the texture part for the boost button
	 */
	public TexturePart getBoostButtonTextur()
	{
		return generateTexturePart(new Vector2(768,512), new Vector2(768+128,512+128));
	}
	
	/**
	 * Returns the part of the texture for the gold button
	 * @return the texture part for the gold button
	 */
	public TexturePart getGoldButtonTextur()
	{
		return generateTexturePart(new Vector2(768,768), new Vector2(768+128,768+128));
	}
	
	/**
	 * Returns the part of the texture for the mountain decoration
	 * @return the texture part for the mountain decoration
	 */
	public TexturePart getMountainTextur()
	{
		return generateTexturePart(new Vector2(0,0), new Vector2(256,256-0.5f));
	}
	
	/**
	 * Returns the part of the texture for the tree decoration
	 * @return the texture part for the tree decoration
	 */
	public TexturePart getTreeTextur()
	{
		return generateTexturePart(new Vector2(0,256), new Vector2(256,256+256));
	}
	
	/**
	 * Returns the part of the texture for the chuck obstacle
	 * @return the texture part for the chuck obstacle
	 */
	public TexturePart getChuckTextur()
	{
		return generateTexturePart(new Vector2(128 + 0.5f,704), new Vector2(128+128,704+256));
	}
	
	/**
	 * Returns the part of the texture for the lokus obstacle
	 * @return the texture part for the lokus obstacle
	 */
	public TexturePart getLokusTextur()
	{
		return generateTexturePart(new Vector2(0,704), new Vector2(0+128,704+256));
	}
	
	/**
	 * Returns the part of the texture for the elk decoration
	 * @return the texture part for the elk obstacle
	 */
	public TexturePart getElkTextur()
	{
		return generateTexturePart(new Vector2(768,256), new Vector2(798+96,256+128));
	}
	
	/**
	 * Returns the part of the texture for the boost animation (first frame)
	 * @return the texture part for the boost animation (first frame)
	 */
	public TexturePart getBoostAnimationTextur()
	{
		return generateTexturePart(new Vector2(512,0), new Vector2(512+128,0+256));
	}
	
	/**
	 * Returns the part of the texture for the goldbar animation (first frame)
	 * @return the texture part for the goldbar animation (first frame)
	 */
	public TexturePart getGoldBarAnimationTextur()
	{
		return generateTexturePart(new Vector2(256,0), new Vector2(256+128,0+256));
	}
	
	/**
	 * Returns the part of the texture for the screen crack
	 * @return the texture part for the screen crack
	 */
	public TexturePart getScreenCrackTextur()
	{
		return generateTexturePart(new Vector2(896,768), new Vector2(896+128,768+256));
	}
	
	/**
	 * Returns the part of the texture for the finish
	 * @return the texture part for the finish
	 */
	public TexturePart getFinishTextur()
	{
		return generateTexturePart(new Vector2(768,128), new Vector2(768+256,128+64));
	}
}
