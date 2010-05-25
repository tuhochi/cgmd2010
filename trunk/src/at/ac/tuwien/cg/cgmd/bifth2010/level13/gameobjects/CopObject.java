package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;

public class CopObject extends EnemyObject{

	
	
	public CopObject(int x, int y) {
		super(x, y);
	}
	
	


	
	@Override
	public void draw(GL10 gl) {
	if (CollisionHelper.checkBackgroundCollision(MyRenderer.map, (GameObject)this))
		super.setRandomDirection();
	
	if (CollisionHelper.checkPlayerObjectCollision((int)this.position.x,(int) this.position.y))
		gameControl.encounterCop(this);
	super.draw(gl);
	}
}
