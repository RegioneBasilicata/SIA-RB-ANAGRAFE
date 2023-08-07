package it.csi.smranag.smrgaa.business;

import javax.ejb.Local;

import it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO;
import it.csi.solmr.exception.SolmrException;



/**
 * <p>
 * Title: S.O.L.M.R.
 * </p>
 * <p>
 * Description: Servizi On-Line per il Mondo Rurale
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: TOBECONFIG
 * </p>
 * 
 * @author TOBECONFIG
 * @version 1.0
 */

@Local
public interface ConsistenzaGaaLocal 
{
  public InvioFascicoliVO getLastSchedulazione(long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public boolean trovaSchedulazioneAttiva(long idAzienda, long idDichiarazioneConsistenza)
      throws SolmrException;
  
  public void insertSchedulazione(InvioFascicoliVO invioFascicoliVO, long idUtente)
      throws SolmrException;
  
  public void deleteSchedulazione(long idInvioFascicoli) 
      throws SolmrException;
  
}
