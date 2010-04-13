package at.ac.tuwien.cg.cgmd.bifth2010.level77;

/**
 * General callback class
 * @author Gerd Katzenbeisser
 *
 * @param <T>
 */
public interface Callback<T>
{
	/**
	 * This method is called if the call completely failed
	 * @param caught
	 */
	void onFailure(Throwable caught);
	
	/**
	 * This method is called if the call was succesfull
	 * @param result
	 */
	void onSucces(T result);
}
