package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * An object that represents a rectangle that has a color gradient with two
 * different color values (one for the top and one for the bottom color).
 * 
 * This object is used in MireRabbit for the sky background.
 * 
 * @author thp
 */
public class Gradient {
	float width;
	float height;
	float x;
	float y;
	private FloatBuffer vtxCoords;
	private FloatBuffer colorBuffer;

	float r1, g1, b1;
	float r2, g2, b2;

	/**
	 * recalculates the vertex coordinates of the gradient
	 */
	private void recalculateVtxCoords() {
		float[] coords = new float[] { x, y, x, y + height, x + width, y, x + width, y + height, };
		vtxCoords = Util.floatArrayToBuffer(coords);
	}

	/**
	 * recalculates the color values of the gradient
	 */
	private void recalculateColors() {
		float[] colors = new float[] { r1, g1, b1, r2, g2, b2, r1, g1, b1, r2, g2, b2, };
		colorBuffer = Util.floatArrayToBuffer(colors);
	}

	/**
	 * Create a new gradient from white to black with the desired size. The
	 * gradient will be positioned at (0,0).
	 * 
	 * @param width
	 *            The width of the resulting rectangle
	 * @param height
	 *            The height of the resulting rectangle
	 */
	public Gradient(float width, float height) {
		this.width = width;
		this.height = height;
		this.x = 0f;
		this.y = 0f;
		recalculateVtxCoords();

		this.r1 = this.b1 = this.g1 = 1.f;
		this.r2 = this.b2 = this.g2 = 0.f;

		recalculateColors();
	}

	/**
	 * Set the upper (top edge) color of this gradient
	 * 
	 * @param r
	 *            Red value (0..1)
	 * @param g
	 *            Green value (0..1)
	 * @param b
	 *            Blue value (0..1)
	 */
	public void setStartColor(float r, float g, float b) {
		r1 = r;
		g1 = g;
		b1 = b;
		recalculateColors();
	}

	/**
	 * Set the lower (bottom edge) color of this gradient
	 * 
	 * @param r
	 *            Red value (0..1)
	 * @param g
	 *            Green value (0..1)
	 * @param b
	 *            Blue value (0..1)
	 */
	public void setStopColor(float r, float g, float b) {
		r2 = r;
		g2 = g;
		b2 = b;
		recalculateColors();
	}

	/**
	 * draw the gradient on the Scene
	 * @param gl OpenGL
	 */
	public void draw(GL10 gl) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();

		/* Enable vertex array and texture array for drawing */
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisable(GL10.GL_TEXTURE_2D);

		gl.glColorPointer(3, GL10.GL_FLOAT, 0, colorBuffer);
		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vtxCoords);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		/* Disable vertex array and texture array after drawing */
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		/* FIXME: Assumption that GL_TEXTURE_2D was enabled before */
		gl.glEnable(GL10.GL_TEXTURE_2D);

		/* Restore the Model-View matrix */
		gl.glPopMatrix();
	}
}
