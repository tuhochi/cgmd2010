package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHandler;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;

public class CopObject extends EnemyObject{

	
	
	public CopObject(int x, int y) {
		super(x, y);
	}
	
	


	
	@Override
	public void draw(GL10 gl) {
	if (CollisionHandler.checkBackgroundCollision(MyRenderer.map, (GameObject)this))
		super.setRandomDirection();
	
	if (CollisionHandler.checkPlayerObjectCollision((int)this.position.x,(int) this.position.y))
		GameControl.encounterCop(this);
	super.draw(gl);
	}
}
