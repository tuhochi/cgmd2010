package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;

public class DrunkBar extends StatusBar{

	DrunkBarBlock[] beerBlocks;
	
	
	public DrunkBar(float objectWidth, float objectHeight) {
		super(objectWidth, objectHeight);
		beerBlocks = new DrunkBarBlock[GameControl.MAX_DRUNK_LEVEL];
		for (int i = 0; i < beerBlocks.length; i++){
			beerBlocks[i] = new DrunkBarBlock(objectWidth/GameControl.MAX_DRUNK_LEVEL,objectHeight);
			beerBlocks[i].position.x = i*objectWidth/GameControl.MAX_DRUNK_LEVEL;
			
		}

		// TODO Auto-generated constructor stub
	}
	@Override
	public void draw(GL10 gl) {

			
	
			//Reset modelview matrix
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			gl.glLoadIdentity();
			
			
			//enable client state
			gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
			
			//bind texture
			gl.glBindTexture(GL10.GL_TEXTURE_2D, this.texture.textures[0]);
			
			//define texture coordinates
			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, this.texture.textureBuffer);
			
			//point to vertex buffer
			gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
			
			gl.glScalef(scaleFactor, 1, 1);
			//translate to correct position
			gl.glTranslatef(this.position.x, this.position.y, 0.0f);
			
			//draw
			gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);
			
			//disable client state
			gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
			gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
			
			
			for (int i = 0; i < GameControl.consumedBeer;i++){
				beerBlocks[i].draw(gl);
				
			}
	}
	
	
	
}
