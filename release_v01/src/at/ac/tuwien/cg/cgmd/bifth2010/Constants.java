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
		
		switch(id){
		
		case 17:
			return R.drawable.l17_icon;
		case 23:
			return R.drawable.l23_icon;
		case 42:
			return R.drawable.l42_icon;
		case 44:
			return R.drawable.l44_icon;
		case 55:
			return R.drawable.l55_icon;
		case 84:
			return R.drawable.l84_icon;
		
		}
		//shouldn't occur
		return R.drawable.l00_levelicon_start;
	}



public static int getLevelTitleResource(int id) {
	switch(id){
	case 17:
		return R.string.l17_nameofthegame_full;
	case 23:
		return R.string.l23_nameofthegame_full;
	case 42:
		return R.string.l42_nameofthegame_full;
	case 44:
		return R.string.l44_GameName;
	case 55:
		return R.string.l55_HelpTitle;
	case 84:
		return R.string.l84_help_title;
		
	}
	//shouldn't occur
	return R.string.l00_nameofthegame_short;
}
	
	public static String[] LEVELIDS = {
		"42", "55", "84", "44", "17", "23"};

}
