package at.ac.tuwien.cg.cgmd.bifth2010.level12;

public class FPSCounter {
	private int mFPSLastSecond = 0;
	private int mFPSnow = 0;
	private long mTimeStarted = 0;
	long dt;
	
	private static FPSCounter mSingleton = null;
	
	private FPSCounter(){
		mTimeStarted = System.currentTimeMillis();
	}
	
	public static FPSCounter getSingleton(){
		if( mSingleton == null ) mSingleton = new FPSCounter();
		return mSingleton;
	}
	
	public void addFrame(){	
		mFPSnow++;
		dt = System.currentTimeMillis() - mTimeStarted;
		if( dt >= 1000 ){
			mFPSLastSecond = mFPSnow;
			mFPSnow = 0;
			mTimeStarted = System.currentTimeMillis();
		}
	}
	
	public int getFPS(){
		return mFPSLastSecond;
	}
}
