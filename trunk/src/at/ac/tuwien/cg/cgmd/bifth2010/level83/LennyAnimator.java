package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11Ext;

/**
 * A convenience class for adding animation to {@link MySprite}. This class is an implementation of
 * the {@link Animatable} and the {@link Animator} interface.
 */
public class LennyAnimator extends Animatable implements Animator {

	public static final int BEAM_ANIMATION_OUT = 0;
	public static final int BEAM_ANIMATION_IN = 1;
	public static final int BEAM_ANIMATION_INVISIBLE = 2;
	
	private float currentTime = 0,endTime;
	private float xDiff=0,yDiff=0,size=1;
	
	private int animate = -1;
	
	public int animationRunning(){
		return animate;
	}
	
	public void initBeamAnimation(float time){
		endTime = time;
		currentTime = 0;
		xDiff = 0;
		yDiff = 0;
		size = 1;
	}
	
	@Override
	public void animate(float deltaTime) {
		
		switch (animate){
		case BEAM_ANIMATION_OUT:
			//Beam out
			size = 1f-currentTime/endTime;
			currentTime += deltaTime;
			if(currentTime >= endTime)
				animate = BEAM_ANIMATION_INVISIBLE;
			break;
			
		case BEAM_ANIMATION_IN:
			size = currentTime/endTime;
			currentTime += deltaTime;
			if(currentTime >= endTime)
				animate = NO_ANIMATION;
			break;
			
		}
		
	}

	@Override
	public void drawAnimated(MySprite sprite, GL10 gl) {
		
		if(animate != BEAM_ANIMATION_INVISIBLE){
			xDiff = (1f-size)*sprite.width/2f;
			yDiff = (1f-size)*sprite.height/2f;
			
			((GL11Ext) gl).glDrawTexfOES(sprite.x+xDiff,sprite.y+yDiff,0,sprite.width*size, sprite.height*size);
		}
	}

	@Override
	public void startAnimation(int animationID, float value) {
		switch(animationID){
		case BEAM_ANIMATION_IN:
			initBeamAnimation(value);
			SoundManager.singleton.play(Constants.SOUND_BEAM_IN, false, 1f, 1f);
			break;
			
		case BEAM_ANIMATION_OUT:
			initBeamAnimation(value);
			SoundManager.singleton.play(Constants.SOUND_BEAM_OUT, false, 1f, 1f);
			break;
		}
		
		animate = animationID;
	}

}
