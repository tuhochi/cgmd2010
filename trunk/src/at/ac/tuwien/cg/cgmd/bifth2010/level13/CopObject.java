package at.ac.tuwien.cg.cgmd.bifth2010.level13;

import javax.microedition.khronos.opengles.GL10;

public class CopObject extends EnemyObject{

	public CopObject(int x, int y) {
		super(x, y);
		// TODO Auto-generated constructor stub
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
