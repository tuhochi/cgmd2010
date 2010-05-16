package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

import static android.opengl.GLES10.GL_FLOAT;
import static android.opengl.GLES10.GL_MODELVIEW;
import static android.opengl.GLES10.GL_TEXTURE;
import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glMatrixMode;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES11;
import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Cloud;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.TexturePart;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;

/**
 * The Class DecorationManager manages rendering of decoration objects (clouds etc.).
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class DecorationManager 
{
	/** The DecorationManager instance. */
	public static DecorationManager instance = new DecorationManager();
	
	public final static int NR_OF_CLOUDS = 30;
	
	/** The random generator. */
	private Random randomGenerator;
	
	/** The cloud height for cloud generation. */
	private int currentCloudHeight;
	
	/** The Clouds ArrazList. */
	private ArrayList<Cloud> clouds;
		
	/** The vboID for clouds. */
	private int vboID;
	
	/** The vertexBuffer for clouds. */
	private FloatBuffer cloudVertexBuffer;
	
	/** The TexturePart for clouds. */
	private TexturePart cloudTexture;

	private boolean wasRestored;
	
	/**
	 * Default Constructor
	 */
	public DecorationManager()
	{
		clouds = new ArrayList<Cloud>(NR_OF_CLOUDS);
		randomGenerator = new Random(System.currentTimeMillis());
	}
	
	/**
	 * Loads Geometrz and TextureCoordinates
	 */
	public void preprocess()
	{
		GeometryManager geometryManager = GeometryManager.instance; 
		cloudVertexBuffer = geometryManager.createVertexBufferQuad(50, 20*RenderView.instance.getAspectRatio());
		cloudTexture = TextureAtlas.instance.getCloudTextur();
		
		if(Settings.GLES11Supported) 
		{
			vboID = geometryManager.createVBO(cloudVertexBuffer, cloudTexture.texCoords);
		}
	}
	
	/**
	 * Generates the Clouds
	 */
	public void generateRandomCloudPosition()
	{
		for(int i=0;i<NR_OF_CLOUDS;i++)
		{

			currentCloudHeight += randomGenerator.nextInt(200)+150;	
			clouds.add(new Cloud(currentCloudHeight,randomGenerator.nextInt(4),
					randomGenerator.nextBoolean(),randomGenerator.nextInt((int)(40*RenderView.instance.getAspectRatio())),
					randomGenerator.nextInt(2)+1));
		}
	}
	
	/**
	 * Renders all visible clouds (in the foreground)
	 * @param gameover indicates if the game is over
	 */
	public void renderForegroundClouds(boolean gameover)
	{
		RenderView renderView = RenderView.instance;
		float balloonHeight = renderView.balloonHeight;
		for(int i=0;i<clouds.size();i++)
		{
			Cloud tempCloud = clouds.get(i);
			float posY = tempCloud.virtualHeight;
			int topBounds = (int)balloonHeight+(int)renderView.getTopBounds();
			//test visibility
			if(tempCloud.isVisible || posY > balloonHeight-tempCloud.dimensions.y && posY < topBounds)
			{		
				tempCloud.isVisible = true;
				
				if(!gameover)
				{
					if(!tempCloud.update())
					{
						clouds.remove(i);
					}
					else
					{
						glMatrixMode(GL_TEXTURE);
						
						glPushMatrix();
						
						switch(tempCloud.type)
						{
							case Cloud.CLOUD_TYPE1:
								glTranslatef(cloudTexture.dimension.x, 0, 0);
								break;
							case Cloud.CLOUD_TYPE2:
								glTranslatef(cloudTexture.dimension.x,-cloudTexture.dimension.y, 0);
								break;
							case Cloud.CLOUD_TYPE3:
								glTranslatef(0, -cloudTexture.dimension.y, 0);
								break;
//							case Cloud.CLOUD_TYPE4:
//								break;		
						}
												
						glMatrixMode(GL_MODELVIEW);
						
						glPushMatrix();
						
						glTranslatef(tempCloud.position.x, tempCloud.position.y, 0);

						if (!Settings.GLES11Supported) {
							glTexCoordPointer(2, GL10.GL_FLOAT, 0,
									cloudTexture.texCoords);
							glVertexPointer(3, GL10.GL_FLOAT, 0, cloudVertexBuffer);
							glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

						} else {
							GLES11.glBindBuffer(GLES11.GL_ARRAY_BUFFER, vboID);

							GLES11.glVertexPointer(3, GL_FLOAT, 0, 0);

							GLES11.glTexCoordPointer(2, GL_FLOAT, 0, 12 * 4); 

							glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
						}

						glMatrixMode(GL_TEXTURE);
						glPopMatrix();
						glMatrixMode(GL_MODELVIEW);
						glPopMatrix();						
					}
				}
				return;
			}
		}
	}
	
	/**
	 * Writes to bundle
	 * @param bundle bundle to write to 
	 */
	public void writeToBundle(Bundle bundle) {
		try {
			bundle.putSerializable("clouds", clouds);
		} catch (Throwable t) {
			Log.e("DecorationManager.java", t.getMessage());
		}
	}
	
	
	/**
	 * Reads from bundle
	 * @param bundle the bundle to read from
	 */
	@SuppressWarnings("unchecked")
	public void readFromBundle(Bundle bundle) {
		clouds = (ArrayList<Cloud>)bundle.getSerializable("clouds");
		wasRestored = true;
	}
	
	public void reset()
	{
		if(!wasRestored)
			generateRandomCloudPosition();
		else
			wasRestored=false;
	}
}
