package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;


import javax.microedition.khronos.opengles.GL10;

public class StatusBar extends GameObject{
	
	float scaleFactor = 0.0f;
	
	public StatusBar(float objectWidth,float objectHeight){
		super(objectWidth, objectHeight);
		}
		
	public void updateScale(float scaleFactor){
		this.scaleFactor = scaleFactor;
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
	}
	
	
	
}
