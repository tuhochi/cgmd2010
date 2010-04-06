package at.ac.tuwien.cg.cgmd.bifth2010.level17;

/**
 * listener for state changes of {@link Action}s
 * @author moe
 */
public interface PlayerStateListener {
	public void playerHPChanged(float damage);
	public void playerMoneyChanged(int money);

}
