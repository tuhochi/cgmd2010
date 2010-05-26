package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.FPSCounter;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;

/**
 * 
 * @author arthur (group 13)
 *
 */
public class BackgroundObject extends GameObject {


	/**
	 * constructor calls super() with object's dimensions
	 */
	public BackgroundObject() {
		//object is 1024*1024 pixels
		super(1024, 1024);
		
		
	}

	/**
	 * @see GameObject#draw(GL10)
	 */
	@Override
	public void draw(GL10 gl) {
		
		//Reset modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		
		//test for collision with solid tiles
		gameControl.setCurrentMovement(gameControl.getMovement().clone());
		if(CollisionHelper.checkBackgroundCollision(MyRenderer.map)) {
			
			//reset offset
			//GameObject.offset.sub(gameControl.getMovement());
			//check if old movement is possible (only at corners)
			//if((gameControl.getMovement().x == 0 && gameControl.getOldMovement().x != 0)|| (gameControl.getMovement().y == 0 && gameControl.getOldMovement().y != 0)) {
				GameObject.offset.x += gameControl.getOldMovement().x * (FPSCounter.getInstance().getDt() / 1000f);
				GameObject.offset.y += gameControl.getOldMovement().y * (FPSCounter.getInstance().getDt() / 1000f);
				gameControl.setCurrentMovement(gameControl.getOldMovement().clone());
				//gameControl.setMovement(gameControl.getOldMovement().clone());
				if(CollisionHelper.checkBackgroundCollision(MyRenderer.map)) {
					//GameObject.offset.sub(gameControl.getOldMovement());
					//stop movement
					gameControl.setOldMovement(new Vector2(0, 0));
				}
			//}
		}
		else {
			gameControl.setOldMovement(gameControl.getMovement().clone());
		}
		//enable client state
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

		//define texture coordinates
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.texture.textureBuffer);

		//point to vertex buffer
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		
		//bind texture
		gl.glBindTexture(GL10.GL_TEXTURE_2D, this.texture.textures[0]);

		//translate whole background (instead of moving player)
		gl.glTranslatef(-GameObject.offset.x, -GameObject.offset.y, 0f);
		
		//draw
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		
		
		//disable client state
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
