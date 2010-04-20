package at.ac.tuwien.cg.cgmd.bifth2010.level70.game;

/**
 * All possible tiles.
 */
public enum TileEnum {

	TILE_EMPTY,
	TILE_OBSTACLE,
	TILE_HORIZONTAL,
	TILE_VERTICAL,
	TILE_LEFT_UP,
	TILE_LEFT_DOWN,
	TILE_RIGHT_UP,
	TILE_RIGHT_DOWN,
	TILE_SOURCE_SEL,
	TILE_TARGET_SEL,
	TILE_START_GOAL;
	
	
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
		return TILE_EMPTY;
	}
}
