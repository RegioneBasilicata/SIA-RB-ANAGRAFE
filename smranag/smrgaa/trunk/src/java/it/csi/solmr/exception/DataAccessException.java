package it.csi.solmr.exception;

/**
 * <p>Title: S.O.L.M.R.</p>
 * <p>Description: Servizi On-Line per il Mondo Rurale</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: TOBECONFIG</p>
 * @author Luca Romanello
 * @version 1.0
 */

import it.csi.solmr.etc.*;

public class DataAccessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6632334945474238788L;

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException() {
		super(SolmrErrors.EXC_DATA_ACCESS);
	}
}