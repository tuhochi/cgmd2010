package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

import android.util.Log;

/**
 * A Sprite class. This class is an implementation of the {@link Drawable} 
 * interface.
 */
public class MySprite implements Drawable {

	public float x,y,z,width,height;
	protected int textureNum;
	private Animator animator;
	
	public MySprite(int resourceId, float x, float y, float width, float height, GL10 gl) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		textureNum = MyTextureManager.singleton.addTextureFromResources(resourceId, gl);
		Log.d("Spirte","texturenum ="+textureNum);	 
	}
	
	public MySprite(String filename, float x, float y, float width, float height, GL10 gl) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		textureNum = MyTextureManager.singleton.addTextureFromAssets(filename, gl);
		Log.d("Spirte","texturenum ="+textureNum);	 
	}
	
	public void setAnimator(Animator animator){
		this.animator = animator; 
	}
	
	public Animator getAnimator(){
		return animator;
	}
	/**
	 * Creates a copy of the MySprite <code>sprite</code>.
	 * 
	 * @param sprite	The sprite to be copied.
	 */
	public MySprite(MySprite sprite) {
		this.x = sprite.x;
		this.y = sprite.y;
		this.width = sprite.width;
		this.height = sprite.height;
		
		this.textureNum = sprite.textureNum;
	}

	/**
	 * Creates a copy of the MySprite <code>sprite</code> with the new position
	 * <code>x, y</code>.
	 * 
	 * @param sprite	The sprite to be copied.
	 * @param x			XPosition.
	 * @param y			YPosition.
	 */
	public MySprite(MySprite sprite, float x, float y) {
		this(sprite);
		
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void Dispose(GL10 gl) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Draw(GL10 gl) {
		MyTextureManager.singleton.textures[textureNum].Bind(gl);
		
		if((animator != null) && (animator.animationRunning() != Animator.NO_ANIMATION))
				animator.drawAnimated(this, gl);
		else
			((GL11Ext) gl).glDrawTexfOES(x,y,0,width, height);
	}

	@Override
	public void Init(GL10 gl) {
		
		
	}

}
