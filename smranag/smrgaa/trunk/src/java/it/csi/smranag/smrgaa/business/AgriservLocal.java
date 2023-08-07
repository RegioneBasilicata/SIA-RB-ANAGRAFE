package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO;
import it.csi.smranag.smrgaa.dto.RitornoAgriservVO;
import it.csi.smranag.smrgaa.dto.RitornoPraticheCCAgriservVO;
import it.csi.solmr.exception.SolmrException;
import javax.ejb.Local;

/**
 * Interfaccia Local dell'EJB di Agriserv
 * 
 * @author TOBECONFIG
 */

@Local
public interface AgriservLocal
{
  
  public RitornoAgriservVO searchPraticheProcedimento(long idParticella, Long idAzienda, 
      int tipologiaStati, Long idDichiarazioneConsistenza, Long idProcedimento, 
      Long annoCampagna, int tipoOrdinamento) throws SolmrException;
  
  public RitornoPraticheCCAgriservVO searchPraticheContoCorrente(long[] idContoCorrente, 
      int tipologiaStati, Long idDichiarazioneConsistenza, Long idProcedimento, 
      Long annoCampagna, int tipoOrdinamento) throws SolmrException;
  
  public RitornoAgriservUvVO existPraticheEstirpoUV(long[] idParticella, 
      Long idAzienda, Long idDichiarazioneConsistenza, Long idProcedimento, 
      Long annoCampagna, int tipoOrdinamento) throws SolmrException;
}
