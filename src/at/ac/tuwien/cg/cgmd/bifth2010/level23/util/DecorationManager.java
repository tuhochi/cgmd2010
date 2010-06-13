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

import java.io.DataInputStream;
import java.io.DataOutputStream;
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

	/** Indicates if the paramters where restored from bundle */
	private boolean wasRestored;
	
	private GeometryManager geometryManager = GeometryManager.instance;
	
	/** The vertexBuffer for mountain. */
	private FloatBuffer mountainVertexBuffer;
	
	/** The TexturePart for mountain. */
	private TexturePart mountainTexture;
	
	/** The position of the tree. */
	private float mountainPositionY;
	
	/** The novement direction for tree. */
	private float mountainMoveDir=1;
	
	/** The vboID for mountains. */
	private int mountainVboID;	
	
	/** The delay how long the mountain stays still before it starts to move down. */
	private float mountainDelay = 400;
	
	/** The vertexBuffer for tree. */
	private FloatBuffer treeVertexBuffer;
	
	/** The TexturePart for tree. */
	private TexturePart treeTexture;
	
	/** The TexturePart of the tree. */
	private float treePositionY;
	
	/** The vboID for tree. */
	private int treeVboID;	
	
	/** The vbo id for elk. */
	private int elkVboID;
	
	/** The texture part for elk. */
	private TexturePart elkTexture;
	
	/** The vertexBuffer for elk. */
	private FloatBuffer elkVertexBuffer;
	
	/** The animation time for elk. */
	private float elkAnimationTime;
	
	/** Switch for the 2 frames in the elk animation. */
	private boolean elkTexSwitch = false;
	
	/** The times when the elk animation should switch frames. */
	private int[] elkAnimationSwitchTimes;
	
	/** Counts how often the elk animation has switched frames. */
	private int elkAnimationSteps;
	
	/**
	 * Default Constructor
	 */
	public DecorationManager()
	{
		clouds = new ArrayList<Cloud>(NR_OF_CLOUDS);
		randomGenerator = new Random(System.currentTimeMillis());
		elkAnimationSwitchTimes = new int[]{2000,750,750,500};		
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
		
		treeVertexBuffer = geometryManager.createVertexBufferQuad(RenderView.instance.getRightBounds(), RenderView.instance.getTopBounds()/2f);
		treeTexture = TextureAtlas.instance.getTreeTextur();
		
		elkVertexBuffer = geometryManager.createVertexBufferQuad(RenderView.instance.getTopBounds()*0.15f*0.75f, RenderView.instance.getTopBounds()*0.15f);
		elkTexture = TextureAtlas.instance.getElkTextur();
		
		if(Settings.GLES11Supported) 
		{
			cloudVboID = geometryManager.createVBO(cloudVertexBuffer, cloudTexture.texCoords);
			mountainVboID = geometryManager.createVBO(mountainVertexBuffer, mountainTexture.texCoords);
			treeVboID = geometryManager.createVBO(treeVertexBuffer, treeTexture.texCoords);
			elkVboID = geometryManager.createVBO(elkVertexBuffer, elkTexture.texCoords);
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
	
	/**
	 * Renders the mountain decoration.
	 */
	public void renderMountains()
	{		
		glPushMatrix();
		
		glTranslatef(0, mountainPositionY, 0);
		
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
	
	/**
	 * Renders the tree decoration.
	 */
	public void renderTree()
	{				
		glPushMatrix();
		
		glTranslatef(0, treePositionY, 0);
		
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
	 * Renders the elk decoration (with animation).
	 */
	public void renderElk()
	{		
		if(elkAnimationTime >= elkAnimationSwitchTimes[elkAnimationSteps])
		{
			elkAnimationTime -= elkAnimationSwitchTimes[elkAnimationSteps];
			elkAnimationSteps++;
			elkTexSwitch = !elkTexSwitch;
			if(elkAnimationSteps == 4)
				elkAnimationSteps = 0;
		}
		
		if(elkTexSwitch)
		{
			glMatrixMode(GL_TEXTURE);
			glPushMatrix();
		
			glTranslatef(0, -elkTexture.dimension.y, 0);
		
			glMatrixMode(GL_MODELVIEW);
		}
		glPushMatrix();
		
		glTranslatef(75f, treePositionY+Settings.MAINCHAR_STARTPOSY*RenderView.instance.getAspectRatio(), 0);
		
		if (!Settings.GLES11Supported) {
			glTexCoordPointer(2, GL10.GL_FLOAT, 0,
					cloudTexture.texCoords);
			glVertexPointer(3, GL10.GL_FLOAT, 0, elkVertexBuffer);
			glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		} else {
			geometryManager.bindVBO(elkVboID);

			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
		}
		if(elkTexSwitch)
		{
			glMatrixMode(GL_TEXTURE);
			glPopMatrix();
			glMatrixMode(GL_MODELVIEW);
		}
		glPopMatrix();
	}
	
	/**
	 * Renders the whole background decoration (mountains,tree,elk).
	 */
	public void renderBackgroundDecoration()
	{
		renderMountains();
		renderTree();
		renderElk();
	}
	
	/**
	 * Writing to stream 
	 * @param dos Stream to write to
	 */
	public void writeToStream(DataOutputStream dos) {
		try {
			dos.writeFloat(mountainPositionY); 
			dos.writeFloat(mountainMoveDir);
			dos.writeFloat(mountainDelay);
			dos.writeFloat(treePositionY);
			
		} catch (Exception e) {
			System.out.println("Error writing to stream in DecorationManager.java: "+e.getMessage());
		}
		
	}
	
	/**
	 * Reading from stream
	 * @param dis Stream to read from
	 */
	public void readFromStream(DataInputStream dis) {
		
		try {
			mountainPositionY = dis.readFloat();
			mountainMoveDir = dis.readFloat();
			mountainDelay = dis.readFloat();
			treePositionY = dis.readFloat();
			wasRestored=true;
			
		} catch (Exception e) {
			System.out.println("Error reading from stream in DecorationManager.java: "+e.getMessage());
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
	
	/**
	 * Resets the DecorationManager.
	 */
	public void reset()
	{
		if(!wasRestored)
		{
		currentCloudHeight = 0;
		clouds.clear();
		generateRandomCloudPosition();
		mountainPositionY = -RenderView.instance.getTopBounds()/4f;
		treePositionY = 0f;
		mountainTexture = TextureAtlas.instance.getMountainTextur();
		mountainDelay = 400;
		}
		wasRestored=false;
	}
	
	/**
	 * Updates the DecorationManager (needed for animation and parallax effect).
	 * @param dt the time between the last frame and the current frame
	 */
	public void update(float dt)
	{
		elkAnimationTime +=dt;
		if(RenderView.instance.gameState == RenderView.INGAME)
		{
		treePositionY -= dt*Settings.BALLOON_SPEED/8f;
		if(mountainPositionY >= 0)
			mountainMoveDir =0;
		if(mountainMoveDir == 0)
			mountainDelay -= dt;
		if(mountainDelay <= 0)
			mountainMoveDir=-1;
		
			mountainPositionY += mountainMoveDir*TimeUtil.instance.getDt()*Settings.BALLOON_SPEED/16f;
		}
	}
}
