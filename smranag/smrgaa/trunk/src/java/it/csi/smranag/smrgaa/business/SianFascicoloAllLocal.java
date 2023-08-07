/**
 * 
 */
package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.ws.sianfa.SianEsito;
import it.csi.solmr.exception.SolmrException;

import javax.ejb.Local;



@Local
public interface SianFascicoloAllLocal {

	public SianEsito getAggiornamentiFascicolo(String cuaa) throws SolmrException;
	
	public Boolean aggiornaFascicoloAziendale(String cuaa, Long idUtente) throws SolmrException;
}
