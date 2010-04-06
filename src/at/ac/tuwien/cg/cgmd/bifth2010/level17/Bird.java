package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.graphics.GLManager;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.MatrixTrackingGL;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;
import at.ac.tuwien.cg.cgmd.bifth2010.level17.renderables.Renderable;

public class Bird {
	
	private Vector3 mPosition;
	private float mRotation;
	private float mRadius = 3.0f;
	private Renderable mModel;
	
	
	public Bird() {}
	
	public Bird(Vector3 pos, float radius, Renderable model, float rotation)
	{
		mPosition = pos;
		mRadius = radius;
		mModel = model;
		mRotation = rotation;
	}
	
	public void draw() {
		GLManager.getInstance().getTextures().setTexture(R.drawable.l17_bird);
		MatrixTrackingGL gl = GLManager.getInstance().getGLContext();
		gl.glPushMatrix();
		gl.glTranslatef(mPosition);
		gl.glRotatef(mRotation, 0, 1.0f, 0);
		mModel.draw(gl);
		gl.glPopMatrix();
	}
	
	public boolean intersect(Vector3 pos, float radius) {
		float dist = Vector3.diff(mPosition, pos).length();
		if(dist < radius + mRadius) {
			return true;
		}
		return false;
	}
	
	public Vector3 getPosition() {
		return mPosition;
	}
	public void setPosition(Vector3 position) {
		this.mPosition = position;
	}

	public float getRotation() {
		return mRotation;
	}

	public void setRotation(float rotation) {
		this.mRotation = rotation;
	}

	public float getRadius() {
		return mRadius;
	}

	public void setRadius(float radius) {
		this.mRadius = radius;
	}	
	
	public Renderable getModel() {
		return mModel;
	}

	public void setModel(Renderable model) {
		this.mModel = model;
	}
}
