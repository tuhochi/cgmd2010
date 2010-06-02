package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;



public class MistressObject extends EnemyObject{

        public MistressObject(int x, int y) {
                super(x, y);
                // TODO Auto-generated constructor stub
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

        	//check player collision
        	if (CollisionHelper.checkPlayerObjectCollision((int)this.position.x,(int) this.position.y)) {
        		gameControl.encounterMistress(this);
        	}
        }
        @Override
        public void draw(GL10 gl) {

        	super.draw(gl);
        }
}
