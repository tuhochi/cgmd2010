package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;

import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class Gradient {
	float width;
	float height;
	float x;
	float y;
	private FloatBuffer vtxCoords;
	private FloatBuffer colorBuffer;
	
	float r1, g1, b1;
	float r2, g2, b2;

	private void recalculateVtxCoords() {
		float [] coords = new float[] {
				x, y,
				x, y+height,
				x+width, y,
				x+width, y+height,
		};
		vtxCoords = Util.floatArrayToBuffer(coords);
	}
	
	private void recalculateColors() {
		float [] colors = new float[] {
				r1, g1, b1,
				r2, g2, b2,
				r1, g1, b1,
				r2, g2, b2,
		};
		colorBuffer = Util.floatArrayToBuffer(colors);
	}
	
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
	
	public void setStartColor(float r, float g, float b) {
		r1 = r;
		g1 = g;
		b1 = b;
		recalculateColors();
	}
	
	public void setStopColor(float r, float g, float b) {
		r2 = r;
		g2 = g;
		b2 = b;
		recalculateColors();
	}

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
