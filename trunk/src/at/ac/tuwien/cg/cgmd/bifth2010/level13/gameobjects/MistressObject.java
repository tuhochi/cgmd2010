package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;


/**
 * 
 * @author group13
 * 
 * class representing a prostitute
 *
 */
public class MistressObject extends EnemyObject{

	/**
	 * constructor inits mistress
	 * @param x x-position of mistress
	 * @param y y-position of mistress
	 * @param id id of mistress
	 */
	public MistressObject(int x, int y, String id) {
		super(x, y, id);
	}

	
	/**
	 * update mistress position and movement
	 * 
	 * @see GameObject#update()
	 */
	@Override
	public void update() {
		//try movement2
		this.position.add(movement2);
		if (CollisionHelper.checkBackgroundCollision((GameObject)this)) {
			this.position.sub(movement2);

			//try movement1
			this.position.add(movement1);
			if (CollisionHelper.checkBackgroundCollision((GameObject)this)) {
				this.position.sub(movement1);
				//set new random movements
				super.setRandomDirection();
			}
		}
		else {
			Vector2 temp = movement1.clone();
			movement1 = movement2.clone();
			movement2 = temp.clone();
		}

		//check for player collision
		if (CollisionHelper.checkPlayerObjectCollision((int)this.position.x,(int) this.position.y)) {
			GameControl.getInstance().encounterMistress(this);
		}
	}

	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		this.position.x = savedInstanceState.getFloat("l13_mistressObject" + id + "_positionX");
		this.position.y = savedInstanceState.getFloat("l13_mistressObject" + id + "_positionY");
		this.active = savedInstanceState.getBoolean("l13_mistressObject" + id + "_active");
		this.movement1.x = savedInstanceState.getFloat("l13_mistressObject" + id + "_movement1X");
		this.movement1.y = savedInstanceState.getFloat("l13_mistressObject" + id + "_movement1Y");
		this.movement2.x = savedInstanceState.getFloat("l13_mistressObject" + id + "_movement2X");
		this.movement2.y = savedInstanceState.getFloat("l13_mistressObject" + id + "_movement2Y");
	}

	
	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		outState.putFloat("l13_mistressObject" + id + "_positionX", this.position.x);
		outState.putFloat("l13_mistressObject" + id + "_positionY", this.position.y);
		outState.putBoolean("l13_mistressObject" + id + "_active", this.active);
		outState.putFloat("l13_mistressObject" + id + "_movement1X", this.movement1.x);
		outState.putFloat("l13_mistressObject" + id + "_movement1Y", this.movement1.y);
		outState.putFloat("l13_mistressObject" + id + "_movement2X", this.movement2.x);
		outState.putFloat("l13_mistressObject" + id + "_movement2Y", this.movement2.y);
	}
}
