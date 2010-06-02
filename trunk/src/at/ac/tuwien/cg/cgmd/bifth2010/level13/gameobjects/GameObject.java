package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Texture;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;

/**
 * 
 * @author arthur/sebastian (group 13)
 *
 */
public abstract class GameObject {
	//smallest size of an object (= player size, = tile size)
	public static final int BLOCKSIZE = 32;
	
	//offset of background and beer due to movement
	public static Vector2 offset = new Vector2(0, 0);
	
	//position of object ((0,0) is bottom left)
	public Vector2 position;
	public static float drunkenRotation = 0;
	//vertex coordinates
	protected FloatBuffer vertexBuffer;
	protected float[] vertices;
	protected ShortBuffer indexBuffer;
	
	public Vector2 moveVec;
	public Vector2 moveVec2;
	//texture of object (=singleton)
	protected Texture texture;
	
	protected GameControl gameControl;
	
	
	public boolean isActive = true;
	
	public static void reset() {
		GameObject.offset = new Vector2(0, 0);
	}
	
	public static void setStartTile(Vector2 tile) {
		//set offset
		float centerX = ((MyRenderer.screenWidth / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		float centerY = ((MyRenderer.screenHeight / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		//move starting tile to center
		float startingTileX = tile.x * GameObject.BLOCKSIZE;
		float startingTileY = tile.y * GameObject.BLOCKSIZE;
		float offsetX = centerX - startingTileX;
		float offsetY = centerY - startingTileY;
		GameObject.offset = new Vector2(-offsetX, -offsetY);
	}
	/**
	 * constructor sets up object (used by subtypes)
	 * @param objectWidth
	 * @param objectHeight
	 */
	public GameObject(float objectWidth, float objectHeight) {
		this.gameControl = GameControl.getInstance();
		//set position
		this.position = new Vector2(0, 0);
		
		//define vertices coordinates
		vertices = new float[12];
		//bottem left
		vertices[0] = 0.0f;
		vertices[1] = 0.0f;
		vertices[2] = 0f;
		//bottom right
		vertices[3] = objectWidth;
		vertices[4] = 0.0f;
		vertices[5] = 0f;
		//top right
		vertices[6] = objectWidth;
		vertices[7] = objectHeight;
		vertices[8] = 0f;
		//top left
		vertices[9] = 0.0f;
		vertices[10] = objectHeight;
		vertices[11] = 0f;
		
		//set up vertex-buffer
		ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuf.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		//set up index buffer
		short[] indices = { 0, 1, 2, 0, 2, 3 };
		ByteBuffer indexBBuffer = ByteBuffer.allocateDirect(indices.length * 2);
		indexBBuffer.order(ByteOrder.nativeOrder());
		indexBuffer = indexBBuffer.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
		
		//set texture
		this.texture = TextureSingletons.getTexture(this.getClass().getSimpleName());
	}
	
	/**
	 * draws the object onto the screen
	 * @param gl
	 */
	public void draw(GL10 gl){
		
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
		
		//translate to correct position
		
		gl.glTranslatef(this.position.x, this.position.y, 0.0f);
		
		gl.glRotatef(drunkenRotation, 0, 0, 1);
		//draw
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);
		
		//disable client state
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	};

	/**
	 * updates offset with movement
	 * @param movement
	 */
	public static void updateOffset(Vector2 movement) {
		if(GameControl.getInstance().isInJail() || GameControl.getInstance().isSexState()) {
			return;
		}
		//offset.add(new Vector2(movement.x * MyRenderer.STEP / 1000, movement.y * MyRenderer.STEP / 1000));
		offset.add(movement);
		
		//test for collision with solid tiles
		if(CollisionHelper.checkBackgroundCollision(MyRenderer.map)) {
			//reset offset
			GameObject.offset.sub(GameControl.getInstance().getMovement());
			//check if old movement is possible (only at corners)
			if((GameControl.getInstance().getMovement().x == 0 && GameControl.getInstance().getOldMovement().x != 0)|| (GameControl.getInstance().getMovement().y == 0 && GameControl.getInstance().getOldMovement().y != 0)) {
				GameObject.offset.add(GameControl.getInstance().getOldMovement());
				if(CollisionHelper.checkBackgroundCollision(MyRenderer.map)) {
					GameObject.offset.sub(GameControl.getInstance().getOldMovement());
					//stop movement
					GameControl.getInstance().setOldMovement(new Vector2(0, 0));
				}
			}
		}
		else {
			GameControl.getInstance().setOldMovement(GameControl.getInstance().getMovement().clone());
		}
	}
	
	/**
	 * Execute whatever the sub class should do here
	 */
	
	
	public void update(){
	}
	
	
}
