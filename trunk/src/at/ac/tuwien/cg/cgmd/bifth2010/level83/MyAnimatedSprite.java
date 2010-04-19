package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;

/**
 * A convenience class for animated Sprites. This class is an implementation of
 * the {@link Animatable} and the {@link Drawable} interface.
 */
public class MyAnimatedSprite extends Animatable implements Drawable {

	private MySprite sprite;
	private float startPosX;
	private float startPosY;
	private boolean up;
	
	/**
	 * Creates an animated Sprite from a {@link MySprite}.
	 * 
	 * @param sprite	The <code>MySprite</code> for the new animated Sprite.
	 */
	public MyAnimatedSprite(MySprite sprite) {
		super();
		this.sprite = sprite;
		startPosX = sprite.x;
		startPosY = sprite.y;
		up = true;
	}

	@Override
	public void animate(float deltaTime) {
		if (up) {
			sprite.y += deltaTime *30f;
		} else {
			sprite.y -= deltaTime *30f;
		}
		
		if (sprite.y > startPosY+30) {
			up = false;
			sprite.y = startPosY+30;
		} else if (sprite.y < startPosY) {
			up = true;
			sprite.y = startPosY;
		}
	}

	@Override
	public void Dispose(GL10 gl) {
		sprite.Dispose(gl);
	}

	@Override
	public void Draw(GL10 gl) {
		sprite.Draw(gl);
	}

	@Override
	public void Init(GL10 gl) {
		
	}

}
