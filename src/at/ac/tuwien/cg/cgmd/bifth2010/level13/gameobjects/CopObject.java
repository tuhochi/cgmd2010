package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import android.os.Bundle;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;

/**
 * 
 * @author group13
 * 
 * class representing a cop object
 *
 */
public class CopObject extends EnemyObject{

	/**
	 * constructor initializes members
	 * 
	 * @param x x-position of cop
	 * @param y y-position of cop
	 * @param id id of cop
	 */
	public CopObject(int x, int y, String id) {
		super(x, y, id);
	}


	/**
	 * updates cop position and checks for player collision
	 * 
	 * @see GameObject#update()
	 */
	@Override
	public void update() {
		//try movement2
		this.position.add(movement2);
		if(CollisionHelper.checkBackgroundCollision((GameObject)this)) {
			this.position.sub(movement2);

			//try movement1
			this.position.add(movement1);
			if(CollisionHelper.checkBackgroundCollision((GameObject)this)) {
				this.position.sub(movement1);
				//generate new random movements
				super.setRandomDirection();
			}
		}
		else {
			Vector2 temp = movement1.clone();
			movement1 = movement2.clone();
			movement2 = temp.clone();
		}

		//check player collision
		if(CollisionHelper.checkPlayerObjectCollision((int)this.position.x,(int) this.position.y)) {
			GameControl.getInstance().encounterCop(this);
		}
	}


	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#restore(Bundle)
	 */
	@Override
	public void restore(Bundle savedInstanceState) {
		this.position.x = savedInstanceState.getFloat("l13_copObject" + id + "_positionX");
		this.position.y = savedInstanceState.getFloat("l13_copObject" + id + "_positionY");
		this.movement1.x = savedInstanceState.getFloat("l13_copObject" + id + "_movement1X");
		this.movement1.y = savedInstanceState.getFloat("l13_copObject" + id + "_movement1Y");
		this.movement2.x = savedInstanceState.getFloat("l13_copObject" + id + "_movement2X");
		this.movement2.y = savedInstanceState.getFloat("l13_copObject" + id + "_movement2Y");

	}


	/**
	 * @see at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence#save(Bundle)
	 */
	@Override
	public void save(Bundle outState) {
		outState.putFloat("l13_copObject" + id + "_positionX", this.position.x);
		outState.putFloat("l13_copObject" + id + "_positionY", this.position.y);
		outState.putFloat("l13_copObject" + id + "_movement1X", this.movement1.x);
		outState.putFloat("l13_copObject" + id + "_movement1Y", this.movement1.y);
		outState.putFloat("l13_copObject" + id + "_movement2X", this.movement2.x);
		outState.putFloat("l13_copObject" + id + "_movement2Y", this.movement2.y);
	}
}
