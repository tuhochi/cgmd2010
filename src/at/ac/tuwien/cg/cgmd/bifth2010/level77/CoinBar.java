package at.ac.tuwien.cg.cgmd.bifth2010.level77;

import java.util.LinkedList;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.framework.Cylinder;

/**
 * A progress bar consisting of BIFTH gold coins.
 * @author gerd
 *
 */
public class CoinBar
{
	private float				width, height, variance, rotation, coinHeight;

	private Cylinder			coin = null;
	private Context				context;
	private GL10				gl;

	private LinkedList<Float>	coinPositions;

	/**
	 * Creates a new CoinBar. The default coinHeight is a tenth of the width.
	 * @param width The width of the bar
	 * @param height The height of the bar
	 * @param variance Maximum x-offset of the coins on the coin stack
	 * @param rotation Rotation of the coins
	 * @param context The android context
	 * @param gl The opengl context
	 */
	public CoinBar(float width, float height, float variance, float rotation, Context context, GL10 gl)
	{
		this.width = width;
		this.height = height;
		this.variance = variance;
		this.rotation = rotation;
		this.context = context;
		this.gl = gl;

		this.coinHeight = width / 10;

		initializeBar();
	}

	private void initializeBar()
	{
		// Initialize the coinBar
		if (coin == null)
		{
			coin = new Cylinder(width / 2, coinHeight, (short) 10);
			coin.setTexture(gl, context.getResources(), R.drawable.l00_coin);
			coin.setColor(0.85f, 0.68f, 0.22f, 1.f);
		}

		initializeVariance();
	}
	private void initializeVariance()
	{

		// Initialize the coin Positions
		int coinCount = getCointCount();
		coinPositions = new LinkedList<Float>();

		for (int i = 0; i < coinCount; ++i)
			coinPositions.add(new Float((Math.random() - 0.5) * variance));
	}

	public int getCointCount()
	{
		return (int) (height / coinHeight);
	}
	
	public void draw(double percent)
	{
		int i = 0;		
		int limit = (int) Math.ceil(percent * getCointCount());
		for (Float f : coinPositions)
		{
			if (i >= limit)
				break;
			gl.glPushMatrix();
			gl.glTranslatef(f, coinHeight * i, -coinHeight * i);
			gl.glRotatef(rotation, 1.0f, 0, 0);
			coin.draw(gl);
			gl.glPopMatrix();
			i++;
		}
	}

	/**
	 * @return the width
	 */
	public float getWidth()
	{
		return width;
	}

	/**
	 * @param width
	 *            the width to set
	 */
	public void setWidth(float width)
	{
		this.width = width;
		initializeBar();
	}

	/**
	 * @return the height
	 */
	public float getHeight()
	{
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(float height)
	{
		this.height = height;
		initializeBar();
	}

	/**
	 * @return the variance
	 */
	public float getVariance()
	{
		return variance;
	}

	/**
	 * @param variance
	 *            the variance to set
	 */
	public void setVariance(float variance)
	{
		this.variance = variance;
		initializeVariance();
	}

	/**
	 * @return the rotation
	 */
	public float getRotation()
	{
		return rotation;
	}

	/**
	 * @param rotation
	 *            the rotation to set
	 */
	public void setRotation(float rotation)
	{
		this.rotation = rotation;
	}

	/**
	 * @return the coinHeight
	 */
	public float getCoinHeight()
	{
		return coinHeight;
	}

	/**
	 * @param coinHeight
	 *            the coinHeight to set
	 */
	public void setCoinHeight(float coinHeight)
	{
		this.coinHeight = coinHeight;
		initializeBar();
	}
}
