/**
 * 
 */
package it.csi.smranag.smrgaa.business;

import it.csi.solmr.exception.SolmrException;

import javax.ejb.Local;

/**
 * @author Stefania Prudente
 *
 * 
 */

@Local
public interface MarcaTemporaleGaaLocal {

	public byte[] getMarcaTemporale(byte[] fileToMark) 
		      throws SolmrException;
}
