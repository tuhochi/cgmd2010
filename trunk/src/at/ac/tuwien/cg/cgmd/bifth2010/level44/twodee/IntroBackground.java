package at.ac.tuwien.cg.cgmd.bifth2010.level44.twodee;


public class IntroBackground extends Sprite {
	private float screenWidth;
	private float screenHeight;

	public IntroBackground(Texture texture, float screenWidth, float screenHeight) {
		super(TextureParts.makeIntroBackground(texture));

		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		setPosition(0.f, 0.f);
	}
}
