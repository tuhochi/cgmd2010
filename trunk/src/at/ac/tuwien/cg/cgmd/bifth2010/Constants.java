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
		case 0:
			return R.drawable.l00_coin;
		case 11:
			return R.drawable.l11_icon;
		case 12:
			return R.drawable.l12_icon;
		case 13:
			return R.drawable.l13_icon;
		case 17:
			return R.drawable.l17_icon;
		case 20:
			return R.drawable.l20_icon;
		case 22:
			return R.drawable.l22_icon;
		case 23:
			return R.drawable.l23_icon;
		case 30:
			return R.drawable.l30_icon;
		case 33:
			return R.drawable.l33_icon;
		case 36:
			return R.drawable.l36_icon;
		case 42:
			return R.drawable.l42_icon;
		case 44:
			return R.drawable.l44_icon;
		case 50:
			return R.drawable.l50_icon;
		case 55:
			return R.drawable.l55_icon;
		case 60:
			return R.drawable.l60_icon;
		case 66:
			return R.drawable.l66_icon;
		case 70:
			return R.drawable.l70_icon;
		case 77:
			return R.drawable.l77_icon;
		case 83:
			return R.drawable.l83_icon;
		case 84:
			return R.drawable.l84_icon;
		case 88:
			return R.drawable.l88_icon;
		/*case 99:
			return R.drawable.l99_icon;*/
			
		}
		//shouldn't occur
		return R.drawable.l00_levelicon_start;
	}



public static int getLevelTitleResource(int id) {
	switch(id){
	case 0:
		return R.string.l00_nameofthegame_short;
	case 11:
		return R.string.l11_title;
	case 12:
		return R.string.l12_title;
	case 13:
		return R.string.l13_GameName;
	case 17:
		return R.string.l17_nameofthegame_full;
	case 20:
		return R.string.l20_help_head;
	case 22:
		return R.string.L22HelpTitle;
	case 23:
		return R.string.l23_nameofthegame_full;
	case 30:
		return R.string.l30_nameofthegame_full;
	case 33:
		return R.string.l33_help_headline;
	case 36:
		return R.string.l36_help_header;
	case 42:
		return R.string.l42_nameofthegame_full;
	case 44:
		return R.string.l44_GameName;
	case 50:
		return R.string.l50_GameName;
	case 55:
		return R.string.l55_HelpTitle;
	case 60:
		return R.string.l60_HelpActivity_Title;
	case 66:
		return R.string.l66_help_title;
	case 70:
		return R.string.l70_GameName;
	case 77:
		return R.string.l77_title;
	case 83:
		return R.string.l83_title;
	case 84:
		return R.string.l84_help_title;
	case 88:
		return R.string.l88_help_title;
		
	}
	//shouldn't occur
	return R.string.l00_nameofthegame_short;
}
	
	public static String[] LEVELIDS = {
		"11", "12", "13", "17", "20", "22", "23", "30", "33", "36", "42", 
		"44", "50", "55", "60", "66", "70", "77", "83", "84", "88"};//, "99"};

}
