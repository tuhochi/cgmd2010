package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;

public class MyAnimatedSprite implements Animatable, Drawable {

	private MySprite sprite;
	private float startPosX;
	private float startPosY;
	private boolean up;
	
	public MyAnimatedSprite(MySprite sprite) {
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
