package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;
import static android.opengl.GLES10.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.TexturePart;

/**
 * The Class TextureAtlas implements the managment of texturecoordinates.
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class TextureAtlas 
{
	int textureID;
	
	Vector2 atlasDimension;
	
	public static TextureAtlas instance = new TextureAtlas();
	
	public TextureAtlas()
	{
		atlasDimension = new Vector2(1024,1024);		
	}
	
	public void loadAtlasTexture()
	{
		int resID = LevelActivity.instance.getResources().getIdentifier("l23_textureatlas", "drawable", "at.ac.tuwien.cg.cgmd.bifth2010");
		textureID = TextureManager.instance.getTextureId(LevelActivity.instance.getResources(), resID);
	}
	
	public void bindAtlasTexture()
	{
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	
	public TexturePart generateTexturePart(Vector2 lowerLeft, Vector2 upperRight)
	{

		FloatBuffer texCoordBuffer;
		float[] textureCoordinates = new float[8];
		
		//normalize pixel coordinates
		Vector2 tempLowerLeft = new Vector2(lowerLeft.x/atlasDimension.x, 1-lowerLeft.y/atlasDimension.y);
		Vector2 tempUpperRight = new Vector2((upperRight.x/atlasDimension.x), 1-(upperRight.y/atlasDimension.y));;
		
		System.out.println("lowerLeft x: " + tempLowerLeft.x + " y: " + tempLowerLeft.y);
		System.out.println("upperRight x: " + tempUpperRight.x + " y: " + tempUpperRight.y);
				
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
	
	public TexturePart getMainCharTextur()
	{
		return generateTexturePart(new Vector2(512,0), new Vector2(512+128,256));
	}
	
	public TexturePart getBackgroundTextur()
	{
		return generateTexturePart(new Vector2(0,0), new Vector2(256,512));
	}
	
	public TexturePart getCloudTextur()
	{
		return generateTexturePart(new Vector2(768,0), new Vector2(768+128,64));
	}
	
	public TexturePart getProgressBarTextur()
	{
		return generateTexturePart(new Vector2(768,515), new Vector2(768+128,512+16));
	}
}
