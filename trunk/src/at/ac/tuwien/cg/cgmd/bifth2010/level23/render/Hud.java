package at.ac.tuwien.cg.cgmd.bifth2010.level23.render;

import static android.opengl.GLES10.GL_TRIANGLE_STRIP;
import static android.opengl.GLES10.glDrawArrays;
import static android.opengl.GLES10.glPopMatrix;
import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES10.glTexCoordPointer;
import static android.opengl.GLES10.glTranslatef;
import static android.opengl.GLES10.glVertexPointer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.LevelActivity;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.Button;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.MainChar;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.entities.TexturePart;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.BurnTimer;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.GeometryManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.PermaBoostTimer;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.ProgressVisibilityHandle;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Settings;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.SoundManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TextureAtlas;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.TimeUtil;
import at.ac.tuwien.cg.cgmd.bifth2010.level23.util.Vector2;

/**
 * The Class Hud provides the HUD for activating boni and movement arrows
 *
 * @author Markus Ernst
 * @author Florian Felberbauer
 */
public class Hud 
{
	public static Hud instance;
	
	/** The gold bar button */
	private Button goldButton;
	
	/** The money button. */
	private Button moneyButton;
	
	/** The class handling time utilities. */
	private TimeUtil timeUtil; 
	
	/** The timer for burn boost animation. */
	private BurnTimer burnTimer; 
	
	/** The timer for goldbar animation. */
	private PermaBoostTimer goldTimer;

	/** Indicates if the Hud was restored from Bundle */
	private boolean wasRestored=false;
	
	/** The progress for the boost progressbar 0-1. */
	public float burnProgress = 1;
	
	/** The Handle for setting the progress for the boost progressbar. */
	private ProgressVisibilityHandle progressVisibilityHandle;
	
	/** The texture part for the screen crack. */
	private TexturePart screenCrackTexture;
	/** The vertexbuffer for the screen crack. */
	private FloatBuffer screenCrackVertexBuffer;
	/** The vbo id for the screen crack. */
	private int screenCrackVboId;

	/** Indicates if the screen crack should be rendered. */
	public boolean renderScreenCrack;
	/** The x-position for the screen crack */
	public float screenCrackPosX;
	/** The y-position for the screen crack */
	public float screenCrackPosY;
	
	/** The GeometryManager handling geometry loading. */
	private GeometryManager geometryManager = GeometryManager.instance;;
	
	/**
	 * Instantiates a new hud, including the buttons and the timer for boost operation.
	 */
	public Hud()
	{
		init();
		int boostAudioId = SoundManager.instance.requestPlayer(R.raw.l23_boost_neu,true);
		MainChar.instance.audioIdBoostSound = boostAudioId;
		burnTimer = new BurnTimer(boostAudioId);
		goldTimer = new PermaBoostTimer();
		progressVisibilityHandle = new ProgressVisibilityHandle();
		progressVisibilityHandle.visibility = android.widget.ProgressBar.VISIBLE;
		instance=this;
	}
	
	/**
	 * Initializes the HUD
	 */
	private void init()
	{
		float topBounds = RenderView.instance.getTopBounds();
		float rightBounds = RenderView.instance.getRightBounds();
		goldButton = new Button(15, 15, new Vector2(5,(topBounds-15)/2),TextureAtlas.instance.getGoldButtonTextur());
		moneyButton = new Button(15, 15, new Vector2(rightBounds-20,(topBounds-15)/2),TextureAtlas.instance.getBoostButtonTextur());
		moneyButton.preprocess();
		goldButton.preprocess();
		timeUtil = TimeUtil.instance;
	}
	
	/**
	 * Reading from stream
	 * @param dis Stream to read from
	 */
	public void readFromStream(DataInputStream dis) throws IOException
	{
		moneyButton.setActive(dis.readBoolean());
		goldButton.setActive(dis.readBoolean());
		renderScreenCrack = dis.readBoolean();
		if(renderScreenCrack)
		{
			screenCrackPosX = dis.readFloat();
			screenCrackPosY = dis.readFloat();
		}
	}
	
	/**
	 * Writing to stream 
	 * @param dos Stream to write to
	 */
	public void writeFromStream(DataOutputStream dos) throws IOException
	{
		dos.writeBoolean(moneyButton.isActive());
		dos.writeBoolean(goldButton.isActive());
		dos.writeBoolean(renderScreenCrack);
		if(renderScreenCrack)
		{
			dos.writeFloat(screenCrackPosX);
			dos.writeFloat(screenCrackPosY);
		}
		wasRestored=true;
	}
	
	/**
	 * Checks if the money button is active
	 * @return true if active, else otherwise
	 */
	public boolean isMoneyButtonActive() {
		return moneyButton.isActive();
	}
	
	/**
	 * Sets the active state of the money button 
	 * @param b true if button should be active, false otherwise
	 */
	public void setMoneyButtonActive(boolean b) {
		moneyButton.setActive(b); 
	}
	
	/**
	 * Sets the active state of the gold button 
	 * @param b true if button should be active, false otherwise
	 */
	public void setGoldButtonActive(boolean b) {
		goldButton.setActive(b); 
	}
	
	/**
	 * Tests if pressed
	 *
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return true, if pressed, false otherwise
	 */
	public boolean testPressed(float x, float y)
	{
		if(goldButton.isPressed(x, y))
		{
			if(goldButton.isActive())
			{
				Settings.BALLOON_SPEED *= Settings.GOLD_BOOST;
				goldButton.setActive(false);
				moneyButton.setActive(false);
				timeUtil.scheduleTimer(goldTimer);
				MainChar.instance.playGoldBarAnimation = true;
			}
			return true;
		}
			
		if(moneyButton.isPressed(x, y))
		{
			if(moneyButton.isActive())
			{
				goldButton.setActive(false);
				moneyButton.setActive(false);
				Settings.BALLOON_SPEED *= Settings.BURN_BOOST;
				timeUtil.scheduleTimer(burnTimer);
				LevelActivity.handler.post(progressVisibilityHandle);
				MainChar.instance.playTimeBoostAnimation = true;
			}
			return true;
		}
		
		return false;			
	}
	
	/**
	 * Renders the HUD 
	 */
	public void render()
	{
		goldButton.render();
		moneyButton.render();
		if(renderScreenCrack)
		{
			glPushMatrix();
			glTranslatef(screenCrackPosX, screenCrackPosY, 0);
			if(!Settings.GLES11Supported) 
			{			
				glTexCoordPointer(2, GL10.GL_FLOAT, 0, screenCrackTexture.texCoords);
				glVertexPointer(3, GL10.GL_FLOAT, 0, screenCrackVertexBuffer);
				glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
			
			} 
			else 
			{
				geometryManager.bindVBO(screenCrackVboId);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, 4); // 4 vertices
			}
			glPopMatrix();
		}
	}
	
	
	/**
	 * Loads the vertex buffers for buttons
	 */
	public void preprocess()
	{
		moneyButton.preprocess();
		goldButton.preprocess();
		screenCrackVertexBuffer = geometryManager.createVertexBufferQuad(MainChar.instance.getWidth(), MainChar.instance.getHeight());

		screenCrackTexture = TextureAtlas.instance.getScreenCrackTextur();
		
		if(Settings.GLES11Supported) 
		{
			screenCrackVboId = geometryManager.createVBO(screenCrackVertexBuffer, screenCrackTexture.texCoords);
		}
	}
	
	/**
	 * Resets the HUD
	 */
	public void reset()
	{
		if(!wasRestored)
			init();
		else
			wasRestored=false;
	}
}
