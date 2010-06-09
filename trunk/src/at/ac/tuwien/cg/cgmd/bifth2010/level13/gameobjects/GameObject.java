package at.ac.tuwien.cg.cgmd.bifth2010.level13.gameobjects;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level13.CollisionHelper;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.GameControl;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.IPersistence;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.MyRenderer;
import at.ac.tuwien.cg.cgmd.bifth2010.level13.Vector2;

/**
 * 
 * @author group13
 * 
 * abstract class representing a game object
 *
 */
public abstract class GameObject implements IPersistence {
	/** smallest size of an object (= player size, = tile size) */
	public static final int BLOCKSIZE = 32;

	/** offset of background, beer, etc. due to player movement */
	public static Vector2 offset = new Vector2(0, 0);

	/** position of object ((0,0) is bottom left) */
	protected Vector2 position;

	/** vertex buffer */
	protected FloatBuffer vertexBuffer;

	/** vertices **/
	protected float[] vertices;

	/** index buffer **/
	protected ShortBuffer indexBuffer;

	/** texture of object (=singleton) */
	protected Texture texture;

	/** flag indicating if object is active (should be drawn) */
	protected boolean active = true;

	/**
	 * resets offset
	 */
	public static void reset() {
		GameObject.offset = new Vector2(0, 0);
	}

	/**
	 * sets the starting position of the player
	 * 
	 * @param tile starting position of player
	 */
	public static void setStartTile(Vector2 tile) {
		//calculcate center of screen (=player position)
		int centerX = ((MyRenderer.getScreenWidth() / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;
		int centerY = ((MyRenderer.getScreenHeight() / GameObject.BLOCKSIZE) / 2) * GameObject.BLOCKSIZE;

		//move starting tile to center
		int startingTileX = tile.x * GameObject.BLOCKSIZE;
		int startingTileY = tile.y * GameObject.BLOCKSIZE;
		int offsetX = centerX - startingTileX;
		int offsetY = centerY - startingTileY;
		//set offset
		GameObject.offset = new Vector2(-offsetX, -offsetY);
	}


	/**
	 * default constructor initializes members with default values
	 */
	public GameObject() {
		this(GameObject.BLOCKSIZE, GameObject.BLOCKSIZE);
	}


	/**
	 * constructor sets up object (used by subtypes)
	 * 
	 * @param objectWidth width of object
	 * @param objectHeight height of object
	 */
	public GameObject(float objectWidth, float objectHeight) {
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
	 * 
	 * @param gl gl
	 */
	public void draw(GL10 gl) {
		//reset modelview matrix
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
		gl.glTranslatef(this.position.x - GameObject.offset.x, this.position.y - GameObject.offset.y, 0.0f);

		//draw
		gl.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, indexBuffer);

		//disable client state
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}

	/**
	 * updates offset due to player movement
	 * 
	 * @param movement movement of player
	 */
	public static void updateOffset() {
		//dont move if player is in jail or has sex
		if(GameControl.getInstance().isJailState() || GameControl.getInstance().isSexState()) {
			return;
		}

		//try movement
		Vector2 tempOffset = GameObject.offset.clone();
		tempOffset.add(GameControl.getInstance().getMovement());		
		//test for collision with solid tiles
		if(CollisionHelper.checkBackgroundCollision(tempOffset)) {
			//reset offset
			tempOffset.sub(GameControl.getInstance().getMovement());
			//check if old movement is possible (only at corners)
			if((GameControl.getInstance().getMovement().x == 0 && GameControl.getInstance().getOldMovement().x != 0)|| (GameControl.getInstance().getMovement().y == 0 && GameControl.getInstance().getOldMovement().y != 0)) {
				tempOffset.add(GameControl.getInstance().getOldMovement());
				if(CollisionHelper.checkBackgroundCollision(tempOffset)) {
					//stop movement
					GameControl.getInstance().setOldMovement(new Vector2(0, 0));
				}
				else {
					GameObject.offset.add(GameControl.getInstance().getOldMovement());
				}
			}
		}
		else {
			GameObject.offset.add(GameControl.getInstance().getMovement());
			GameControl.getInstance().setOldMovement(GameControl.getInstance().getMovement().clone());
		}
	}

	/**
	 * execute whatever updates the sub classes need
	 */
	public void update() {

	}

	/**
	 * getter for position member-variable
	 * 
	 * @return position member-variable
	 */
	public Vector2 getPosition() {
		return position;
	}


	/**
	 * setter for position member-variable
	 * 
	 * @param position value position should be set to
	 */
	public void setPosition(Vector2 position) {
		this.position = position;
	}


	/**
	 * getter for active member-variable
	 * 
	 * @return active member-variable
	 */
	public boolean isActive() {
		return active;
	}


	/**
	 * setter for active member-variable
	 * 
	 * @param active value active should be set to
	 */
	public void setActive(boolean active) {
		this.active = active;
	}


	/**
	 * setter for texture member-variable
	 * 
	 * @param texture value texture should be set to
	 */
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
