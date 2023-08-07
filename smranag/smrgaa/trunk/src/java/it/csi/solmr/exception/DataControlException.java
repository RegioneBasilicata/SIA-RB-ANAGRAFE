package it.csi.solmr.exception;

public class DataControlException extends Exception
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8950738306889580326L;
	String message;
	String flagControllo;

	public DataControlException(String message)
	{
		this.flagControllo = message;
		this.message = message;
	}

	public DataControlException(String flagControllo, String message)
	{
		this.flagControllo = flagControllo;
		this.message = message;
	}

	public String getMessage()
	{
		return message;
	}

	public String getFlagControllo()
	{
		return flagControllo;
	}
}