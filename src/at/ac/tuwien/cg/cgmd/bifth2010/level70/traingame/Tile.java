package at.ac.tuwien.cg.cgmd.bifth2010.level70.traingame;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.Geometry;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.GeometryFactory;
import at.ac.tuwien.cg.cgmd.bifth2010.level70.geometry.SpriteTexture;

public class Tile {

	public static final float TILE_SIZE = 1.0f;
	private static final float ANIM_SELECT_DURATION = 0.2f;
	
	private enum StateAnimEnum {
		STATE_ANIM_SELECT,
		STATE_ANIM_HIGHLIGHT,
		STATE_ANIM_SWITCH,
		STATE_ANIM_TRAIN
	}
	
	private TrainGame game;
	private TileEnum type;
	private TileEnum typeNext;
	private SpriteTexture tex;
	
	private Geometry geom;
	private Geometry geomAnim;
	
	private float cx, cy;
	private float totalDt;
	
	private StateAnimEnum stateAnim;
	
	
	private float animScale;
	private float animAngle;
	
	private boolean isGoal;
	
	
	public Tile(TrainGame game) {
		this.game = game;
		stateAnim = null;
		type     = TileEnum.TILE_CLOUD;
		typeNext = null;
		isGoal   = false;
	}
	
	
	public void createOpenGl(float cx, float cy, SpriteTexture tex) {
		this.tex = tex;
		this.cx  = cx;
		this.cy  = cy;
		
		geom = GeometryFactory.createQuad(cx, cy, TILE_SIZE, TILE_SIZE);
		geom.setTexBuffer(tex.getTexBuffer(type.ordinal()));
		
		geomAnim = GeometryFactory.createQuad(cx, cy, TILE_SIZE, TILE_SIZE);
		geomAnim.setTexBuffer(tex.getTexBuffer(4));
	}
	
	public void setIsGoal(boolean flag) {
		isGoal = flag;
	}
	
	public boolean isGoal() {
		return isGoal;
	}
	
	public void setType(TileEnum type) {
		this.type = type;
	}
	
	public void setTexBuffer() {
		geom.setTexBuffer(tex.getTexBuffer(type.ordinal()));
	}
	
	public TileEnum getType() {
		return type;
	}
	
	public void setStateSelect() {
		if (stateAnim == StateAnimEnum.STATE_ANIM_SELECT) {
			setStateHighlight();
		}
		else {
			stateAnim = StateAnimEnum.STATE_ANIM_SELECT;
			animScale = 1.0f;
			totalDt   = 0.0f;
		}
	}
	
	public void setStateHighlight() {
		animScale = 1.0f;
		stateAnim = StateAnimEnum.STATE_ANIM_HIGHLIGHT;
		if (type == TileEnum.TILE_CLOUD) {
			geomAnim.setTexBuffer(tex.getTexBuffer(TileEnum.TILE_HIGHLIGHT_CLOUD.ordinal()));
		}
		else {
			geomAnim.setTexBuffer(tex.getTexBuffer(TileEnum.TILE_HIGHLIGHT.ordinal()));
		}
	}
	
	public void setStateDeselect() {
		stateAnim = null;
		if (typeNext != null) {
			type     = typeNext;
			typeNext = null;
			setTexBuffer();
		}
	}
	
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
	
	
	public void setStateTrain() {
	    setStateDeselect();
		stateAnim = StateAnimEnum.STATE_ANIM_TRAIN;
	}
	
		
	public void update(float dt) {
		
		// Select animation - Scale down and up, afterwards set new state
		// to highlight the selected tile
		if (stateAnim == StateAnimEnum.STATE_ANIM_SELECT) {
			if (totalDt >= ANIM_SELECT_DURATION * 2.0) {
				setStateHighlight();
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
				game.onCheckGameState();
			}
			
		}
	}
	
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
		else {
			geom.draw(gl);
		}
	}
	
	public float getCenterX() {
		return cx;
	}
	
	
	public float getCenterY() {
		return cy;
	}
	
	
	
	public boolean isInitial() {
		return type == TileEnum.TILE_CLOUD;
	}
	
	public boolean isSelectable() {
		if (stateAnim == StateAnimEnum.STATE_ANIM_SWITCH ||
			stateAnim == StateAnimEnum.STATE_ANIM_TRAIN) {
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
