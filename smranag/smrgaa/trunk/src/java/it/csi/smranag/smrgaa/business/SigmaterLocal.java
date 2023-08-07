package it.csi.smranag.smrgaa.business;

import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioFabbricato;
import it.csi.sigmater.sigtersrv.dto.daticatastali.DettaglioTerreno;
import it.csi.sigmater.sigtersrv.dto.daticatastali.SoggettoFisico;
import it.csi.sigmater.sigtersrv.dto.daticatastali.SoggettoGiuridico;
import it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita;
import it.csi.solmr.exception.SolmrException;

import javax.ejb.EJBLocalObject;
import javax.ejb.Local;

/**
 * Interfaccia Local dell'EJB di SIGMATER
 * 
 * @author TOBECONFIG
 */

@Local
public interface SigmaterLocal
{
	public DettaglioTerreno cercaTerreno(String codIstatComune,
										 String codBelfioreComune,
										 String sezione,
										 String foglio,
										 String numero,
										 String subalterno,
										 String progressivo)
	throws SolmrException;
	
	public DettaglioFabbricato cercaFabbricato(String codIstatComune,
											   String codBelfioreComune,
											   String sezione,
											   String foglio,
											   String numero,
											   String subalterno,
											   String progressivo)
	throws SolmrException;
	
	public Titolarita[] cercaTitolaritaOggettoCatastale(String codIstatComune,
														 String codBelfioreComune,
														 String sezione,
														 String idImmobile,
														 String tipoImmobile,
														 String dataDa,
														 String dataA)
	throws SolmrException;
	
	
	public SoggettoFisico[] cercaSoggettoFisico(String codIstatComune, String codBelfioreComune,
      String nome, String cognome, String codFiscale, String ricercaEsatta)
  throws SolmrException;
	
	public SoggettoGiuridico[] cercaSoggettoGiuridico(String codIstatComune, String codBelfioreComune,
      String partitaIva, String denominazione, String ricercaEsatta)
  throws SolmrException;
	
	public Titolarita[] cercaTitolaritaSoggettoCatastale(
      String codIstatComune, String codBelfioreComune, String sezione, String idSoggetto, 
      String tipoSoggetto, String dataDa, String dataA)
  throws SolmrException;
	
	
	
}
