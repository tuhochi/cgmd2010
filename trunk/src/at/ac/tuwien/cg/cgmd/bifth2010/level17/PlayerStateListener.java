package at.ac.tuwien.cg.cgmd.bifth2010.level17;

/**
 * listener for state changes of {@link Player}s
 * @author mama
 */
public interface PlayerStateListener {
	/**
	 * The health of the player has changed
	 * @param damage the new health of the player
	 */
	public void playerHPChanged(float damage);
	
	/**
	 * The money of the player has changed
	 * @param money The new money of the player
	 */
	public void playerMoneyChanged(int money);

}
