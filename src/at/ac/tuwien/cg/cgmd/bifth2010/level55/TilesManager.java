package at.ac.tuwien.cg.cgmd.bifth2010.level55;

import java.util.HashMap;

import javax.microedition.khronos.opengles.GL10;

public class TilesManager {	
	final static int TILE_HEIGHT=32;
	final static int TILE_WIDTH=32;
			
	static void init() {
		loadLevel();
		
		createVBOs();
	}
	
	public static int getTypeAt(int x, int y) {
		tempPosition.setPosition(x, y);
		return tiles_hashmap.get(tempPosition);
	}
	
	private static void loadLevel() {
		tiles_hashmap=new HashMap<Position, Integer>();
		
		
		// open file... fill tiles_hashmap
		tiles_hashmap.put(new Position(2,54), 2);
		// *******************************
		
		maxTilePosX=2;
		maxTilePosY=54;
	}
	
	private static void createVBOs() {
		vbo_hashmap=new HashMap<Position, TilesVBO>();
		maxVBOPosX=(int) Math.ceil((double)maxTilePosX/VBO_WIDTH);
		maxVBOPosY=(int) Math.ceil((double)maxTilePosY/VBO_HEIGHT);
		

		for (int i=0; i<maxVBOPosX; i++) {
			for (int j=0; j<maxVBOPosY; j++) {
				tempPosition.setPosition(i, j);
				vbo_hashmap.put(tempPosition, new TilesVBO(i*VBO_WIDTH, j*VBO_HEIGHT, VBO_WIDTH, VBO_HEIGHT));
			}
		}
	}
	
	public static void draw(GL10 gl)
    {
		// bind tilesatlas
		
		// apply view frustum culling (adjust loops)
		for (int i=0; i<maxVBOPosX; i++) {
			for (int j=0; j<maxVBOPosY; j++) {
				tempPosition.setPosition(i, j);
				vbo_hashmap.get(tempPosition).draw(gl);
			}
		}
    }
	
	private final static int VBO_WIDTH=5;
	private final static int VBO_HEIGHT=5;
	
	private static int maxTilePosX=0;
	private static int maxTilePosY=0;
	
	private static int maxVBOPosX=0;
	private static int maxVBOPosY=0;
	
	private static HashMap<Position, Integer> tiles_hashmap;		// coords, tiletype
	private static HashMap<Position, TilesVBO> vbo_hashmap;			// coords, TilesVBO	
	private static Position tempPosition=new Position(0,0);
}
