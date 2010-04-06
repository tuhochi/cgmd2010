package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Renderable;

public class House {

	private int mHouseSize = 0;
	private Vector3 mPosition;
	private Vector3 mSize = new Vector3();
	private Renderable mModel;
	
	public boolean intersect(Vector3 pos, float radius) {
		if(Math.abs(pos.x - mPosition.x) < radius + mSize.x / 2.0f &&
		   Math.abs(pos.y - mPosition.y) < radius + mSize.y / 2.0f &&
		   Math.abs(pos.z - mPosition.z) < radius + mSize.z / 2.0f) {
			return true;
		}
		return false;
	}
	
	public void draw()
	{
		GLManager.getInstance().getTextures().setTexture(R.drawable.l17_crate);
		MatrixTrackingGL gl = GLManager.getInstance().getGLContext();
		gl.glPushMatrix();
		gl.glTranslatef(mPosition);
		mModel.draw(gl);
		gl.glPopMatrix();
	}
	
	public int getHouseSize() {
		return mHouseSize;
	}
	public void setHouseSize(int houseSize, Vector3 size) {
		this.mHouseSize = houseSize;
		mSize = size;
	}
	public Vector3 getPosition() {
		return mPosition;
	}
	public void setPosition(Vector3 position) {
		this.mPosition = position;
	}

	public Vector3 getSize() {
		return mSize;
	}

	public void setSize(Vector3 size) {
		this.mSize = size;
	}

	public Renderable getModel() {
		return mModel;
	}

	public void setModel(Renderable model) {
		this.mModel = model;
	}
}
