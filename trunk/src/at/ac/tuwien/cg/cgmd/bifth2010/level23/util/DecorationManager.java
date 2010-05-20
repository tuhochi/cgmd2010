package at.ac.tuwien.cg.cgmd.bifth2010.level23.util;

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

import android.os.Bundle;
import android.util.Log;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Cloud;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.TexturePart;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.render.RenderView;

/**
 * The Class DecorationManager manages rendering of decoration objects (clouds etc.).
 *
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
	private int cloudVboID;
	
	/** The vertexBuffer for clouds. */
	private FloatBuffer cloudVertexBuffer;
	
	/** The TexturePart for clouds. */
	private TexturePart cloudTexture;

	private boolean wasRestored;
	
	private GeometryManager geometryManager = GeometryManager.instance;
	
	/** The vertexBuffer for mountain. */
	private FloatBuffer mountainVertexBuffer;
	
	/** The TexturePart for mountain. */
	private TexturePart mountainTexture;
	
	/** The position of the tree. */
	private Vector2 mountainPosition;
	
	/** The novement direction for tree. */
	private float mountainMoveDir=1;
	
	/** The vboID for mountains. */
	private int mountainVboID;	
	
	/** The vertexBuffer for tree. */
	private FloatBuffer treeVertexBuffer;
	
	/** The TexturePart for tree. */
	private TexturePart treeTexture;
	
	/** The TexturePart of the tree. */
	private Vector2 treePosition;
	
	/** The vboID for tree. */
	private int treeVboID;	
	
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
		
		mountainVertexBuffer = geometryManager.createVertexBufferQuad(RenderView.instance.getRightBounds(), RenderView.instance.getTopBounds()/2f);
		mountainTexture = TextureAtlas.instance.getMountainTextur();
		mountainPosition = new Vector2(0,-RenderView.instance.getTopBounds()/4f);		
		
		treeVertexBuffer = geometryManager.createVertexBufferQuad(RenderView.instance.getRightBounds(), RenderView.instance.getTopBounds()/2f);
		treeTexture = TextureAtlas.instance.getTreeTextur();
		treePosition = new Vector2(0,0);
		
		if(Settings.GLES11Supported) 
		{
			cloudVboID = geometryManager.createVBO(cloudVertexBuffer, cloudTexture.texCoords);
			mountainVboID = geometryManager.createVBO(mountainVertexBuffer, mountainTexture.texCoords);
			treeVboID = geometryManager.createVBO(treeVertexBuffer, treeTexture.texCoords);
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
							geometryManager.bindVBO(cloudVboID);

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
	
	public void renderMountains()
	{		
		glPushMatrix();
		
		glTranslatef(0, mountainPosition.y, 0);
		
		if (!Settings.GLES11Supported) {
			glTexCoordPointer(2, GL10.GL_FLOAT, 0,
					cloudTexture.texCoords);
			glVertexPointer(3, GL10.GL_FLOAT, 0, mountainVertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		} else {
			geometryManager.bindVBO(mountainVboID);

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
		}
		
		glPopMatrix();
	}
	
	public void renderTree()
	{				
		glPushMatrix();
		
		glTranslatef(0, treePosition.y, 0);
		
		if (!Settings.GLES11Supported) {
			glTexCoordPointer(2, GL10.GL_FLOAT, 0,
					treeTexture.texCoords);
			glVertexPointer(3, GL10.GL_FLOAT, 0, treeVertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		} else {
			geometryManager.bindVBO(treeVboID);

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
		}
		
		glPopMatrix();
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
		{
			currentCloudHeight = 0;
			clouds.clear();
			generateRandomCloudPosition();
		}
		else
			wasRestored=false;
	}
	
	public void update(float dt)
	{
		treePosition.y -= TimeUtil.instance.getDt()*Settings.BALLOON_SPEED/8f;
		if(mountainPosition.y >= 0)
			mountainMoveDir =-1;
		
			mountainPosition.y += mountainMoveDir*TimeUtil.instance.getDt()*Settings.BALLOON_SPEED/16f;
	}
}
