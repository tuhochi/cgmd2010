package at.ac.tuwien.cg.cgmd.bifth2010.level13;

/**
 * 
 * @author group13
 * @author group23
 *
 * class for measuring drawn frames per second
 */
public class FPSCounter {

	/** refresh interval */
	private static final int FPSREFRESHINTERVAL = 3 * 1000;

	/** singleton object */ 
	private static FPSCounter instance;

	/** time between two frames */
	private float dt;

	/** accumulated frame time */
	private float accFrameTime;

	/** number of rendered frames */
	private float nrRenderedFrames;
	
	/** time of last frame */
	private long lastFrameTime;

	/** calculated frames per second */
	private float fps;

	/**
	 * resets the singleton object
	 */
	public static void reset() {
		instance = null;
	}

	/**
	 * creates/returns the singleton object
	 * 
	 * @return singleton object of this class
	 */
	public static FPSCounter getInstance() {
		if(instance == null) {
			instance = new FPSCounter();
		}
		return instance;
	}

	/**
	 * updates the fps
	 */
	public void update() {
		long currentTime = System.currentTimeMillis();

		//check if fps should be refreshed
		if(accFrameTime >= FPSREFRESHINTERVAL) {
			fps = (nrRenderedFrames / accFrameTime) * 1000;
			accFrameTime = 0;
			nrRenderedFrames = 0;
		}

		//first frame
		if(lastFrameTime == 0) {
			lastFrameTime = currentTime;
		}
		else {
			dt = currentTime - lastFrameTime;
			accFrameTime += dt;
			nrRenderedFrames++;
			lastFrameTime = currentTime;
		}
	}

	/**
	 * getter for member dt
	 * 
	 * @return member dt
	 */
	public float getDt() {
		return dt;
	}

	/**
	 * getter for member fps
	 * 
	 * @return member fps
	 */
	public float getFPS() {
		return fps;
	}
}
