package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.util.ArrayList;

public class Path {

	ArrayList<PathPoint> mPointList = new ArrayList<PathPoint>();
	ArrayList<Float> mWeights = new ArrayList<Float>();
	
	public Path(){

		mPointList.add(new PathPoint(11.04f, 41.85f));
		mPointList.add(new PathPoint(22.91f, 11.11f));
		mPointList.add(new PathPoint(62.50f, 14.81f));
		mPointList.add(new PathPoint(62.50f, 33.33f));
		mPointList.add(new PathPoint(22.91f, 42.59f));
		mPointList.add(new PathPoint(33.33f, 55.55f));
		mPointList.add(new PathPoint(70.80f, 55.55f));
		mPointList.add(new PathPoint(79.16f, 09.25f));
		mPointList.add(new PathPoint(89.58f, 27.77f));
		mPointList.add(new PathPoint(80.20f, 74.07f));
		mPointList.add(new PathPoint(06.25f, 77.77f));
		mPointList.add(new PathPoint(04.16f, 51.85f));
		//mPointList.add(new PathPoint(11.041667f, 41.851852f));

		mWeights.add(0.f);
		mWeights.add(1.f);
		mWeights.add(2.f);
		mWeights.add(2.2f);
		mWeights.add(3.f);
		mWeights.add(3.2f);
		mWeights.add(4.f);
		mWeights.add(5.f);
		mWeights.add(5.3f);
		mWeights.add(6.f);
		mWeights.add(6.8f);
		mWeights.add(7.f);
	}
	
	public PathPoint interpolate(float fD) {
		
		float fLower = mWeights.get(0);
		if(fD<fLower){
			return mPointList.get(0);
		}
		
		for(int i=1; i<mWeights.size(); i++) {
			float fUpper = mWeights.get(i);
			
			if(fD<=fUpper) {
				float fDist = fUpper-fLower;
				fD -=fLower;
				fD /=fDist;
				float fXLower = mPointList.get(i-1).mX;
				float fYLower = mPointList.get(i-1).mY;
				float fXUpper = mPointList.get(i).mX;
				float fYUpper = mPointList.get(i).mY;
				
				float fX = (1-fD) * fXLower + fD * fXUpper;				
				float fY = (1-fD) * fYLower + fD * fYUpper;
				return new PathPoint(fX, fY);				
			}
			fLower = fUpper;
		}
		return mPointList.get(mPointList.size()-1);
	}
	
	public class PathPoint {
		float mX;
		float mY;
		
		public PathPoint(float fX, float fY){
			mX = fX;
			mY = fY;
		}
	}
}
