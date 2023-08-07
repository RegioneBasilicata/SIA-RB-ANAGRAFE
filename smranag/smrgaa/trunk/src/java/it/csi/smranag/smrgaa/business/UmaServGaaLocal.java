package it.csi.smranag.smrgaa.business;

import it.csi.smruma.umaserv.dto.AssegnazioneVO;
import it.csi.smruma.umaserv.dto.DittaUmaVO;
import it.csi.solmr.dto.uma.MacchinaVO;
import it.csi.solmr.dto.uma.UtilizzoVO;
import it.csi.solmr.exception.SolmrException;

import java.util.Vector;

import javax.ejb.Local;

@Local
public interface UmaServGaaLocal
{
	
  public DittaUmaVO[] umaservGetAssegnazioniByIdAzienda(
      long idAzienda, String[] arrCodiceStatoAnag)  throws SolmrException;
  
  public AssegnazioneVO[] umaservGetDettAssegnazioneByRangeIdDomAss(
      long[] arrIdDomandaAssegnazione)  throws SolmrException;
  
  public Vector<MacchinaVO> serviceGetElencoMacchineByIdAzienda(Long idAzienda,
      Boolean storico, Long idGenereMacchina)  throws SolmrException;
  
  public Vector<Long> serviceGetElencoAziendeUtilizzatrici(Long idMacchina)
      throws SolmrException;
  
  public UtilizzoVO serviceGetUtilizzoByIdMacchinaAndIdAzienda(
      Long idMacchina, Long idAzienda) throws SolmrException;
	
}
