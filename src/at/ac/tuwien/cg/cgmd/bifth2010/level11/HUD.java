package at.ac.tuwien.cg.cgmd.bifth2010.level11;

import javax.microedition.khronos.opengles.GL10;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

public class HUD {
	
	private boolean drawTouchTreasureCircle;
	
	private Vector2 touchTreasureCirclePositon;
	private float touchTreasureCircleRadius;
	
	private Square treasureCircle;
	
	public static HUD singleton;
	private static final int circle_texture_id = R.drawable.l11_circle;
	
	public HUD() {
		HUD.singleton = this;
		drawTouchTreasureCircle = true;
		
		touchTreasureCirclePositon = new Vector2();
		
		treasureCircle = new Square();
	}
	
	public void setDrawTouchTreasureCircle(boolean b) {
		touchTreasureCircleRadius = 0.0f;
		
		drawTouchTreasureCircle = b;	
	}
	
	public boolean isDrawTouchTreasureCircle() {
		return drawTouchTreasureCircle;	
	}
	
	public void setTouchTreasureCirclePositon(Vector2 v) {
		touchTreasureCirclePositon = v;
	}
	
	public void setTouchTreasureCircleRadius(float f) {
		touchTreasureCircleRadius = f;
	}
	
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
