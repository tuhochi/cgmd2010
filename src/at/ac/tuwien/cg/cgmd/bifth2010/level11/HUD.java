package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;
/**
 * class, displaying the icons of the scoreboard and the attraction circle of the currently placed treasure
 *
 */
public class HUD {
	/**
	 * if true, attraction circle of the currently placed treasure is drawn
	 */
	private boolean drawTouchTreasureCircle;
	/**
	 * position of the attraction circle in world coordinates
	 */
	private Vector2 touchTreasureCirclePositon;
	/**
	 * radius of the attraction  circle
	 */
	private float touchTreasureCircleRadius;
	/**
	 * square onto which the texture is rendered
	 */
	private Square treasureCircle;
	/**
	 * singleton, that points to this
	 */
	public static HUD singleton;
	/**
	 * texture id
	 */
	private static final int circle_texture_id = R.drawable.l11_circle;
	
	public HUD() {
		HUD.singleton = this;
		drawTouchTreasureCircle = true;
		
		touchTreasureCirclePositon = new Vector2();
		
		treasureCircle = new Square();
	}
	/**
	 * if set to  true, attraction circle of the currently placed treasure is drawn
	 * @param b
	 */
	public void setDrawTouchTreasureCircle(boolean b) {
		touchTreasureCircleRadius = 0.0f;
		
		drawTouchTreasureCircle = b;	
	}
	/**
	 * 
	 * @return returns true, if attraction circle of the currently placed treasure is drawn
	 */
	public boolean isDrawTouchTreasureCircle() {
		return drawTouchTreasureCircle;	
	}
	/**
	 * sets the position of the attraction circle in world coordinates
	 * @param x
	 * @param y
	 */
	public void setTouchTreasureCirclePositon(float x, float y) {
		touchTreasureCirclePositon.set(x, y);
	}
	/**
	 * sets the radius of the attraction circle
	 * @param f radius
	 */
	public void setTouchTreasureCircleRadius(float f) {
		touchTreasureCircleRadius = f;
	}
	/**
	 * draws the attraction radius
	 * @param gl
	 */
	public void draw(GL10 gl) {
		// draw touch treasure attraction circle
			gl.glPushMatrix();
				gl.glColor4f(1.0f, 1.0f, 1.0f, 0.3f);
				Textures.tex.setTexture(circle_texture_id);
				gl.glTranslatef(touchTreasureCirclePositon.x, touchTreasureCirclePositon.y, 0.0f);
				gl.glScalef(touchTreasureCircleRadius, touchTreasureCircleRadius, 1.0f);
				treasureCircle.draw(gl);
			gl.glPopMatrix();
			
			//touchTreasureCircleRadius+=1.0f;
		
	}
}
