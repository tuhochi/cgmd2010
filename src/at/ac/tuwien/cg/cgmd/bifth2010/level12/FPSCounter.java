package at.ac.tuwien.cg.cgmd.bifth2010.level12;

/** simple class counting frames per seconds */
public class FPSCounter {
	private int mFPSLastSecond = 0; /** counted fps in the last second */
	private int mFPSnow = 0; /** fps this second couting */
	private long mTimeStarted = 0; /** this count started when */
	long dt; /** time difference from the last counted point */
	
	private static FPSCounter mSingleton = null; /** singleton object of the counter */
	
	/** private constructor creating the counter */
	private FPSCounter(){
		mTimeStarted = System.currentTimeMillis();
	}
	
	/** delivers the singleton, and creates it on the first call */
	public static FPSCounter getSingleton(){
		if( mSingleton == null ) mSingleton = new FPSCounter();
		return mSingleton;
	}
	
	/** adds a frame to the counter, if a new second starts the counter gets reseted and the member for displaying gets set with the counter amound of the last second. gets called from the GLRenderer.draw() as last call */
	public void addFrame(){	
		mFPSnow++;
		dt = System.currentTimeMillis() - mTimeStarted;
		if( dt >= 1000 ){
			mFPSLastSecond = mFPSnow;
			mFPSnow = 0;
			mTimeStarted = System.currentTimeMillis();
		}
	}
	
	/** return the amount of frames drawn in the last second */
	public int getFPS(){
		return mFPSLastSecond;
	}
}
