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

public class ResourceAccessException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3945025332665890755L;

	public ResourceAccessException(String message) {
		super(message);
	}

	public ResourceAccessException() {
		super(SolmrErrors.EXC_RESOURCE_ACCESS);
	}
}