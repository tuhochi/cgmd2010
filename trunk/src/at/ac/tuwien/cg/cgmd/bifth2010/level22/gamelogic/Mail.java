package at.ac.tuwien.cg.cgmd.bifth2010.level22.gamelogic;

/**
 * Encapsulates the state of one single mail data entry in the database
 * 
 * @author Sulix
 *
 */
public class Mail {
	
	/**
	 * Mail Constructor
	 * 
	 * @param displayName The string which should be displayed
	 * @param requiredInput The string which should be entered by the user
	 * @param isRich true, if the mail is of the type richmail, else the mailtype will be a viagramail
	 */
	public Mail( String displayName, String requiredInput, boolean isRich )
	{
		
		this.displayName = displayName;
		this.requiredInput = requiredInput;
		this.isRich = isRich;
	}
	
	/**
	 * @return the current mails display name
	 */
	public String getDisplayName()
	{
		
		return displayName;
	}
	
	/**
	 * @return the current mails required input
	 */
	public String getRequiredInput()
	{
		
		return requiredInput;
	}
	
	/**
	 * @return true if the current mail is a viagra mail
	 */
	public boolean isViagra()
	{
		
		return !isRich;
	}
	
	/**
	 * @return true, if the current mail is a rich mail
	 */
	public boolean isRich()
	{
		
		return isRich;
	}

	private String displayName;
	private String requiredInput;
	private boolean isRich;
}
