package at.ac.tuwien.cg.cgmd.bifth2010.level22;

public class Mail {
	
	public Mail( String displayName, String requiredInput, boolean isRich )
	{
		
		this.displayName = displayName;
		this.requiredInput = requiredInput;
		this.isRich = isRich;
	}
	
	public String getDisplayName()
	{
		
		return displayName;
	}
	
	public String getRequiredInput()
	{
		
		return requiredInput;
	}
	
	public boolean isViagra()
	{
		
		return !isRich;
	}
	
	public boolean isRich()
	{
		
		return isRich;
	}

	private String displayName;
	private String requiredInput;
	private boolean isRich;
}
