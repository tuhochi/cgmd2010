package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;

public class CopObject extends EnemyObject{

	
	
	public CopObject(int x, int y) {
		super(x, y);
	}
	
	


	
	@Override
	public void update() {
		//try movevec2
    	this.position.add(moveVec2);
    	if (CollisionHelper.checkBackgroundCollision(MyRenderer.map, (GameObject)this)) {
    		this.position.sub(moveVec2);
    		
    		//try movevec
    		this.position.add(moveVec);
    		if (CollisionHelper.checkBackgroundCollision(MyRenderer.map, (GameObject)this)) {
        		this.position.sub(moveVec);
        		super.setRandomDirection();
    		}
    	}
    	else {
    		Vector2 temp = moveVec.clone();
    		moveVec = moveVec2.clone();
    		moveVec2 = temp.clone();
    	}
		
		if (CollisionHelper.checkPlayerObjectCollision((int)this.position.x,(int) this.position.y))
			gameControl.encounterCop(this);
	}
	@Override
	public void draw(GL10 gl) {
	super.draw(gl);
	}
}
