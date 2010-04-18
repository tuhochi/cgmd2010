package at.ac.tuwien.cg.cgmd.bifth2010.level83;

import at.ac.tuwien.cg.cgmd.bifth2010.R;

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
	
	public static final int TEXTURE_MAP = R.drawable.l83_map3;
	
	public static final int OVERLAY = R.drawable.l83_overlay70;
	public static final int ITEM_BOMB = R.drawable.l83_bomb;
	public static final int ITEM_BOMB_BW = R.drawable.l83_bomb_bw;
	public static final int ITEM_LASER = R.drawable.l83_laser;
	public static final int ITEM_LASER_BW = R.drawable.l83_laser_bw;
	public static final int ITEM_DYNAMITE = 123;
	public static final int ITEM_DYNAMITE_BW = 123;
	public static final int ITEM_WALL = 123;
	public static final int ITEM_WALL_BW = 123;
	
	public static final int TEXTURE_DOLLAR = R.drawable.l83_dollar;
	public static final int TEXTURE_LENNY = R.drawable.l83_kenny1;
	
	public static final float SPEED = 1.0f;
	
	public static final float ASP_HEX = 1;
	
	public static float VIEWPORT_HEIGHT=1;
	
	public static void setViewPort(float height,int rows){
		
		VIEWPORT_HEIGHT = height;

		GRID_ELEMENT_WIDTH = height*2/((float)Math.sqrt(3)*rows*ASP_HEX);
		GRID_ELEMENT_HEIGHT = GRID_ELEMENT_WIDTH*ASP_HEX;
		
		R_HEX = GRID_ELEMENT_WIDTH/2f;
		RI_HEX = R_HEX/2f*(float)Math.sqrt(3)*ASP_HEX;
		//DIF_HEX = (R_HEX-RI_HEX)*(1-ASP_HEX);
		DIF_HEX = R_HEX-RI_HEX;
	}
}
