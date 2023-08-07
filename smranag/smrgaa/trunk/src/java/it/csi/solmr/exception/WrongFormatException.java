package it.csi.solmr.exception;

/**
 * <p>Title: Utilità di gestione delle targhe</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: CSI</p>
 * @author TOBECONFIG
 * @version 1.0
 */

public class WrongFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4691598023630079129L;

	/**
	 * Costruttore standard
	 */
	public WrongFormatException()
	{
	}

	/**
	 * Costruisce un eccezione WrongFormatException con uno specifico messaggio
	 * di errore.
	 * @param msg Messaggio di errore
	 */
	public WrongFormatException(String msg)
	{
		super(msg);
	}
}