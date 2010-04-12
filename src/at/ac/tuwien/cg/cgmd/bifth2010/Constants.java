package at.ac.tuwien.cg.cgmd.bifth2010;

public final class Constants {
	
	public static final int NUMBER_OF_LEVELS_TO_PLAY = 6;

	public static String getLevelActionString(int id) {
		return getActionString(id)+ ".LAUNCH_LEVEL";
	}
	
	public static String getHelpActionString(int id) {
		return getActionString(id)+ ".LAUNCH_HELP";
	}
	
	private static String getActionString(int id){
		id=Math.min(99,Math.max(id,0));
		String s = "at.ac.tuwien.cg.cgmd.bifth2010.level";
		if(id<10){
			s+="0";
		}
		s+=id;
		return s;
	}

	public static int getLevelIconResource(int id) {
		if(id==0) {
			return R.drawable.l00_coin;
		}
		//TODO add hardcoded level id to icon id mapping here
		//else if (id==1){
		// return R.drawable.l01_levelicon;
		//} else if {...
		
		//shouldn't occur
		return R.drawable.l00_levelicon_start;
	}
	
	public static String[] LEVELIDS = {"11", "12", "13", "17", "20", "22", "23", "30", "33", "36", "42", "44", "50", "55", "60", "66", "70", "77", "83", "84", "88", "99"};

	
}
