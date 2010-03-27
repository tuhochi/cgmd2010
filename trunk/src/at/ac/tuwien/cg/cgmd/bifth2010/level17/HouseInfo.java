package at.ac.tuwien.cg.cgmd.bifth2010.level17;

import at.ac.tuwien.cg.cgmd.bifth2010.level17.math.Vector3;

public class HouseInfo {

	private int mHouseSize = 0;
	private Vector3 mPosition;
	
	public int getHouseSize() {
		return mHouseSize;
	}
	public void setHouseSize(int houseSize) {
		this.mHouseSize = houseSize;
	}
	public Vector3 getPosition() {
		return mPosition;
	}
	public void setPosition(Vector3 position) {
		this.mPosition = position;
	}
}
