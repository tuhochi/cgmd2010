package at.ac.tuwien.cg.cgmd.bifth2010.level11;

public class Treasure {
	private float value;
	private float attractionRadius;
	public Treasure(float value, float attractionRadius){
		this.value = value;
		this.attractionRadius = attractionRadius;
	}
	public float getAttracktionRadius(){
		return this.attractionRadius;
	}
	/**
	 * subtracts value from the trasure and returns false if treasure is empty
	 * @param value
	 * @return
	 */
	public boolean grabValue(float value){
		this.value -= value;
		if(this.value > 0)
			return true;
		else
			return false;
	}
}
