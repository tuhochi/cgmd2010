package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

/**
 * A convenience class that provides several common constants.
 */
public class Constants {
	
	public static float GRID_ELEMENT_WIDTH = 53f;
	public static float GRID_ELEMENT_HEIGHT = 53f;
	
	public static  float R_HEX = GRID_ELEMENT_WIDTH/2f;
	public static  float RI_HEX = R_HEX/2f*(float)Math.sqrt(3);
	public static  float DIF_HEX = R_HEX-RI_HEX;
	
	public static final byte GRID_WALL = 0x00;
	public static final byte GRID_CUSTOM_WALL = 0x10;
	
	public static final byte GRID_DOLLAR = 0x11;
	public static final byte GRID_NULL = 0x01;
	
	public static final int CHARACTER_POSITION = 2;
	
	public static final String GROUP_PREFIX = "l83_";
	
	//Texturen - Rescources - muss noch upgedated werden
	public static final int TEXTURE_HEXAGON = R.drawable.l83_hexagon;
	public static final int TEXTURE_HEXAGON_B = R.drawable.l83_hexagon_blue;
	public static final int TEXTURE_HEXAGON_R = R.drawable.l83_hexagon_r;
	public static final int TEXTURE_TOMB = R.drawable.l83_tomb;
	public static final int TEXTURE_DOLLARSIGN = R.drawable.l83_dollarsign;
	
	public static final int TEXTURE_MAP = R.drawable.l83_map4;
	
	public static final int ANIM_BOMB_1 = R.drawable.l83_anim_bomb_4;
	public static final int ANIM_BOMB_2 = R.drawable.l83_anim_bomb_3;
	public static final int ANIM_BOMB_3 = R.drawable.l83_anim_bomb_2;
	public static final int ANIM_BOMB_4 = R.drawable.l83_anim_bomb_1;
	
	public static final int OVERLAY = R.drawable.l83_overlay70;
	public static final int ITEM_BOMB = R.drawable.l83_bomb;
	public static final int ITEM_BOMB_BW = R.drawable.l83_bomb_bw;
	public static final int ITEM_LASER = R.drawable.l83_laser;
	public static final int ITEM_LASER_BW = R.drawable.l83_laser_bw;
	public static final int ITEM_WALL = R.drawable.l83_hexagon_blue;
	public static final int ITEM_WALL_BW = R.drawable.l83_hexagon_bw;
	public static final int ITEM_DELETEWALL = R.drawable.l83_item_delete;
	public static final int ITEM_DELETEWALL_BW = R.drawable.l83_item_delete_bw;
	
	public static final int TEXTURE_DOLLAR = R.drawable.l83_dollar;
	public static final int TEXTURE_LENNY = R.drawable.l83_lenny;
	
	public static final float SPEED = 0.5f;
	
	public static final float ASP_HEX = 1;
	
	public static float VIEWPORT_HEIGHT=1;
	
	/**
	 * This method can be used to set constants according to the height of the 
	 * display and the number of rows of which the level should consist.
	 * 
	 * @param height	The height of the display.
	 * @param rows		Number of rows for the level.
	 */
	public static void setViewPort(float height, int rows) {
		
		VIEWPORT_HEIGHT = height;

		GRID_ELEMENT_WIDTH = height*2/((float)Math.sqrt(3)*rows*ASP_HEX);
		GRID_ELEMENT_HEIGHT = GRID_ELEMENT_WIDTH*ASP_HEX;
		
		R_HEX = GRID_ELEMENT_WIDTH/2f;
		RI_HEX = R_HEX/2f*(float)Math.sqrt(3)*ASP_HEX;
		//DIF_HEX = (R_HEX-RI_HEX)*(1-ASP_HEX);
		DIF_HEX = R_HEX-RI_HEX;
	}
}
