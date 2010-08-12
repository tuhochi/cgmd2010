package at.ac.tuwien.cg.cgmd.bifth2010.framework;

import java.util.ArrayList;

public class Path {

	ArrayList<PathPoint> mPointList = new ArrayList<PathPoint>();
	ArrayList<Float> mWeights = new ArrayList<Float>();
	
	public Path(){

		float fWHalf = 0.042f;
		float fHHalf = 0.074f;
		mPointList.add(new PathPoint(0.1104f+fWHalf, 0.4185f+fHHalf));
		mPointList.add(new PathPoint(0.2291f+fWHalf, 0.1111f+fHHalf));
		mPointList.add(new PathPoint(0.6250f+fWHalf, 0.1481f+fHHalf));
		mPointList.add(new PathPoint(0.6250f+fWHalf, 0.3333f+fHHalf));
		mPointList.add(new PathPoint(0.2291f+fWHalf, 0.4259f+fHHalf));
		mPointList.add(new PathPoint(0.3333f+fWHalf, 0.5555f+fHHalf));
		mPointList.add(new PathPoint(0.7080f+fWHalf, 0.5555f+fHHalf));
		mPointList.add(new PathPoint(0.7916f+fWHalf, 0.0925f+fHHalf));
		mPointList.add(new PathPoint(0.8958f+fWHalf, 0.2777f+fHHalf));
		mPointList.add(new PathPoint(0.8020f+fWHalf, 0.7407f+fHHalf));
		mPointList.add(new PathPoint(0.0625f+fWHalf, 0.7777f+fHHalf));
		mPointList.add(new PathPoint(0.0416f+fWHalf, 0.5185f+fHHalf));
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
