package at.ac.tuwien.cg.cgmd.bifth2010.level70.game;

/**
 * All possible tiles on the playing field. The order must correspond to the 
 * sprite index (order of the tiles in the texture image file).
 * 
 * @author Christoph Winklhofer
 */
public enum TileEnum {

	TILE_CLOUD,
	TILE_GRASS,
	TILE_TREE,
	TILE_ROCK,
	TILE_LAKE,
	
	TILE_HORIZONTAL,
	TILE_VERTICAL,
	TILE_LEFT_UP,
	TILE_LEFT_DOWN,
	TILE_RIGHT_UP,
	TILE_RIGHT_DOWN,
	
	TILE_HIGHLIGHT,
	TILE_HIGHLIGHT_CLOUD,
	TILE_COIN;
	
	
	/**
	 * Convert int to TileEnum
	 * @param i The index of the tile.
	 * @return Return TileEnum corresponding to the index.
	 */
	static public TileEnum valueOf(int i) {
		for (TileEnum it : values()) {
			if (it.ordinal() == i) {
				return it;
			}
		}
		return TILE_CLOUD;
	}
}
