package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;

public class HouseInfo {

	private int mHouseSize = 0;
	private Vector3 mPosition;
	private Vector3 mSize = new Vector3();
	
	public boolean intersect(Vector3 pos, float radius) {
		if(Math.abs(pos.x - mPosition.x) < radius + mSize.x / 2.0f &&
		   Math.abs(pos.y - mPosition.y) < radius + mSize.y / 2.0f &&
		   Math.abs(pos.z - mPosition.z) < radius + mSize.z / 2.0f) {
			return true;
		}
		return false;
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
}
