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

public class NotFoundException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2189266855700390590L;

	public NotFoundException(String message) {
		super(message);
	}

	public NotFoundException() {
		super(SolmrErrors.EXC_NOT_FOUND);
	}
}