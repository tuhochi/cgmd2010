package at.ac.tuwien.cg.cgmd.bifth2010.level70.game;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level70.util.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.util.GeometryFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.util.SpriteTexture;

/**
 * A tile on the play field. The user can select and switch these fields.
 * The animations and states of a tile are managed in this class.

 * @author Christoph Winklhofer
 */
public class Tile {

    // ----------------------------------------------------------------------------------
    // -- Members ----
    
    /** Size of the tile */
	public static final float TILE_SIZE = 1.0f;
	
	/** Duration for the select animation */
	private static final float ANIM_SELECT_DURATION = 0.2f;
	
	/** The possible tile states */
	private enum StateAnimEnum {
		STATE_ANIM_SELECT,
		STATE_ANIM_HIGHLIGHT,
		STATE_ANIM_SWITCH,
		STATE_ANIM_TRAIN,
		STATE_ANIM_COIN
	}
	
	/** The type of the tile */
	private TileEnum type;
	
	/** The type of the next tile - need to switch to tiles */
	private TileEnum typeNext;
	
	/** The sprite texture containing all different tiles */
	private SpriteTexture tex;
	
	/** The geometry to draw the tile */
	private Geometry geom;
	
	/** The geometry to animate or select the tile */
	private Geometry geomAnim;
	
	/** Tile center coordinates */
	private float cx, cy;
	
	/** Total time for animation */
	private float totalDt;
	
	/** The state of the tile */
	private StateAnimEnum stateAnim;
	
	/** Scale factor during the tile select animation */
	private float animScale;
	
	/** Rotation angle during the switch animation */
	private float animAngle;
	
	/** Is the tile a goal - If the train can reach a goal tile the game is completed */
	private boolean isGoal;
	
	
	// ----------------------------------------------------------------------------------
    // -- Ctor ----
	
	/** 
	 * Create the tile.
	 */
	public Tile() {
		stateAnim = null;
		type     = TileEnum.TILE_CLOUD;
		typeNext = null;
		isGoal   = false;
	}
	
	// ----------------------------------------------------------------------------------
    // -- Public methods ----
	
	/**
	 * Create the OpenGL specific part of the tile
	 * @param cx X-coordinate of the center
	 * @param cy Y-coordinate of the center
	 * @param tex Sprite texture containing the tile types
	 */
	public void createOpenGl(float cx, float cy, SpriteTexture tex) {
		this.tex = tex;
		this.cx  = cx;
		this.cy  = cy;
		
		geom = GeometryFactory.createQuad(cx, cy, TILE_SIZE, TILE_SIZE);
		geomAnim = GeometryFactory.createQuad(cx, cy, TILE_SIZE, TILE_SIZE);

		setTexBuffer();
	}
	
	/** 
	 * Set the tile as a goal. The game is complete if the train can reach the goal.
	 * @param flag true if this tile is a goal, otherwise false.
	 */
	public void setIsGoal(boolean flag) {
		isGoal = flag;
	}
	
	
	/**
	 * Return true if this tile as a goal tile.
	 * @return Return true if this tile is a goal tile.
	 */
	public boolean isGoal() {
		return isGoal;
	}
	
	/**
	 * The train has moved already on this tile, so it can not be switched again.
	 * @return Return true if the tile is in train state.
	 */
	public boolean isStateTrain() {
	    return stateAnim == StateAnimEnum.STATE_ANIM_TRAIN;
	}
	
	
	/**
	 * The last wagoon have move on this tile. The player loose one coin for each
	 * of this tiles.
	 * @return Return true if the tile is in coin state.
	 */
	public boolean isStateCoin() {
        return stateAnim == StateAnimEnum.STATE_ANIM_COIN;
    }
	
	
	/** 
	 * Set the type of the tile.
	 * @param type The type of the tile.
	 */
	public void setType(TileEnum type) {
		this.type = type;
	}
	
	
	/**
	 * Set the texture buffer of the corresponding tile.
	 */
	public void setTexBuffer() {
		geom.setTexBuffer(tex.getTexBuffer(type.ordinal()));
		
		if (stateAnim == StateAnimEnum.STATE_ANIM_HIGHLIGHT) {
            if (type == TileEnum.TILE_CLOUD) {
                geomAnim.setTexBuffer(tex.getTexBuffer(TileEnum.TILE_HIGHLIGHT_CLOUD.ordinal()));
            }
            else {
                geomAnim.setTexBuffer(tex.getTexBuffer(TileEnum.TILE_HIGHLIGHT.ordinal()));
            }
        }
        else if (stateAnim == StateAnimEnum.STATE_ANIM_COIN) {
            geomAnim.setTexBuffer(tex.getTexBuffer(TileEnum.TILE_COIN.ordinal()));
        }
	}
	
	/**
	 * Return the type of the tile.
	 * @return Return the type of the tile.
	 */
	public TileEnum getType() {
		return type;
	}
	
	
	/**
	 * Start the state select animation. The tile is highlighted after the animation
	 * finish.
	 */
	public void setStateSelect() {
		if (stateAnim == StateAnimEnum.STATE_ANIM_SELECT) {
			setStateHighlight();
			setTexBuffer();
		}
		else {
			stateAnim = StateAnimEnum.STATE_ANIM_SELECT;
			animScale = 1.0f;
			totalDt   = 0.0f;
		}
	}
	
	/**
	 * Highlight the selected tile.
	 */
	public void setStateHighlight() {
		animScale = 1.0f;
		stateAnim = StateAnimEnum.STATE_ANIM_HIGHLIGHT;
	}
	
	
	/**
	 * Deselct a tile. Switch the tiles if there is a next tile.
	 */
	public void setStateDeselect() {
		stateAnim = null;
		if (typeNext != null) {
			type     = typeNext;
			typeNext = null;
			setTexBuffer();
		}
	}
	
	
	/**
	 * Start the switch animation.
	 * @param next The next type of the tile.
	 */
	public void setStateSwitch(TileEnum next) {
		if (typeNext != null) {
			return;
		}
		stateAnim = StateAnimEnum.STATE_ANIM_SWITCH;
		typeNext = next;
		animAngle = 0.0f;
		totalDt   = 0.0f;
		geomAnim.setTexBuffer(tex.getTexBuffer(typeNext.ordinal()));
	}
	
	
	/**
	 * The train moved along this tile. Tile can not be selected or switched.
	 */
	public void setStateTrain() {
	    setStateDeselect();
		stateAnim = StateAnimEnum.STATE_ANIM_TRAIN;
	}
	
	/**
	 * The last wagoon moved along this tile. Display a coin.
	 */
	public void setStateCoin() {
	    stateAnim = StateAnimEnum.STATE_ANIM_COIN;
	}
	
		
	/**
	 * Update the tile during an animatinon sequence.
	 * @param dt The delta frame time.
	 */
	public void update(float dt) {
		
		// Select animation - Scale down and up, afterwards set new state
		// to highlight the selected tile
		if (stateAnim == StateAnimEnum.STATE_ANIM_SELECT) {
			if (totalDt >= ANIM_SELECT_DURATION * 2.0) {
				setStateHighlight();
				setTexBuffer();
			}
			else if (totalDt >= ANIM_SELECT_DURATION) {
				animScale += 0.5f * dt;
			}
			else {
				animScale -= 0.5f * dt;
			}
			totalDt += dt;
		}
		else if (stateAnim == StateAnimEnum.STATE_ANIM_SWITCH) {
			animAngle -= 200 * dt;
			if (animAngle <= -180) {
				setStateDeselect();
			}
		}
	}
	
	
	/**
	 * Draw the tile.
	 * @param gl The OpenGL context.
	 */
	public void draw(GL10 gl) {
		
		if (stateAnim == StateAnimEnum.STATE_ANIM_SELECT) {
			// Move tile to origin, scale and move back to original position.
			gl.glPushMatrix();
			gl.glTranslatef(cx, cy, 0);
			gl.glScalef(animScale, animScale, 1);
			gl.glTranslatef(-cx, -cy, 0);
			geom.draw(gl);
			gl.glPopMatrix();
		}
		else if (stateAnim == StateAnimEnum.STATE_ANIM_HIGHLIGHT) {
			// Draw tile and the highlight. Blending must be active.
			geom.draw(gl);
			geomAnim.draw(gl);
		}
		else if (stateAnim == StateAnimEnum.STATE_ANIM_SWITCH) {
			// Draw next tile - Move tile to origin, rotate and move back 
			// to orginal position.
			gl.glPushMatrix();
			gl.glTranslatef(cx, cy, 0);
			gl.glRotatef(180 - animAngle, 1, 0, 0);
			gl.glTranslatef(-cx, -cy, 0);
			geomAnim.draw(gl);
			gl.glPopMatrix();
			
			// Draw current tile - Move tile to origin, rotate and move back 
			// to orginal position.
			gl.glPushMatrix();
			gl.glTranslatef(cx, cy, 0);
			gl.glRotatef(animAngle, 1, 0, 0);
			gl.glTranslatef(-cx, -cy, 0);
			geom.draw(gl);
			gl.glPopMatrix();
		}
		else if (stateAnim == StateAnimEnum.STATE_ANIM_COIN) {
            // Draw tile and the coin. Blending must be active.
            geom.draw(gl);
            geomAnim.draw(gl);
        }
		else {
			geom.draw(gl);
		}
	}
	
	
	/** 
	 * Return the center of the tile.
	 * @return The x coordinate of the tile center.
	 */
	public float getCenterX() {
		return cx;
	}
	
	  
	/** 
     * Return the center of the tile.
     * @return The y coordinate of the tile center.
     */
	public float getCenterY() {
		return cy;
	}
	
	
	/**
	 * Is the tile in the initial state.
	 * @return Return true if the tile is in the initial state.
	 */
	public boolean isInitial() {
		return type == TileEnum.TILE_CLOUD;
	}
	
	
	/**
	 * Is the tile selectable.
	 * @return Return true if the tile can be selected.
	 */
	public boolean isSelectable() {
		if (stateAnim == StateAnimEnum.STATE_ANIM_SWITCH ||
			stateAnim == StateAnimEnum.STATE_ANIM_TRAIN  ||
			stateAnim == StateAnimEnum.STATE_ANIM_COIN   ||
			isGoal) {
			return false;
		}
		else {
			return type == TileEnum.TILE_CLOUD      ||
			       type == TileEnum.TILE_HORIZONTAL ||
			       type == TileEnum.TILE_VERTICAL   ||
			       type == TileEnum.TILE_LEFT_DOWN  ||
			       type == TileEnum.TILE_LEFT_UP    ||
			       type == TileEnum.TILE_RIGHT_DOWN ||
			       type == TileEnum.TILE_RIGHT_UP;
		}
	}
}
