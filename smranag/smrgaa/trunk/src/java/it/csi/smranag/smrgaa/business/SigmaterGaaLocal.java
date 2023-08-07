package it.csi.smranag.smrgaa.business;

import it.csi.sigmater.sigtersrv.dto.daticatastali.Titolarita;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.sigmater.ParticellaSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.RichiestaSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.SoggettoSigmaterVO;
import it.csi.smranag.smrgaa.dto.sigmater.TitolaritaParticellaSigVO;
import it.csi.smranag.smrgaa.dto.sigmater.TitolaritaSigmaterVO;
import it.csi.solmr.exception.SolmrException;

import java.util.Vector;

import javax.ejb.EJBLocalObject;
import javax.ejb.Local;

/**
 * Interfaccia Local dell'EJB di SIGMATER
 * 
 * @author TOBECONFIG
 */

@Local
public interface SigmaterGaaLocal
{
	
  public Vector<BaseCodeDescription> getComuneAndSezioneForSigmater(long idAzienda) 
      throws SolmrException;
  
  public RichiestaSigmaterVO getRichiestaSigmater(RichiestaSigmaterVO richSigVO)
      throws SolmrException;
  
  public void updateRichiestaSigmater(RichiestaSigmaterVO richSigVO)
      throws SolmrException;
  
  public Long insertRichiestaSigmater(RichiestaSigmaterVO richSigVO)
      throws SolmrException;
  
  public SoggettoSigmaterVO getSoggettoSigmater(long idRichiestaSigmater)
      throws SolmrException;
  
  public void updateSoggettoSigmater(SoggettoSigmaterVO soggSigVO)
      throws SolmrException;
  
  public Long insertSoggettoSigmater(SoggettoSigmaterVO soggSigVO)
      throws SolmrException;
  
  public boolean esisteTitolaritaSigmaterFromIdRichiesta(long idRichiestaSigmater)
      throws SolmrException;
  
  public void deleteTitolaritaParticellaSigFromPadre(long idRichiestaSigmater)
      throws SolmrException;
  
  public void deleteTitolaritaSigmater(long idRichiestaSigmater)
      throws SolmrException;
  
  public Long getIdTipoDiritto(String codice)
      throws SolmrException;
  
  public Long insertTitolaritaSigmater(TitolaritaSigmaterVO titSigVO)
      throws SolmrException;
  
  public ParticellaSigmaterVO getParticellaSigmater(ParticellaSigmaterVO partSigVO)
      throws SolmrException;
  
  public void updateParticellaSigmater(ParticellaSigmaterVO partSigVO)
      throws SolmrException;
  
  public boolean esisteTitolaritaParticellaSig(TitolaritaParticellaSigVO titPartSigVO)
      throws SolmrException;
  
  public Long insertTitolaritaParticellaSig(TitolaritaParticellaSigVO titPartSigVO)
      throws SolmrException;
  
  public String getIstatComuneNonEstinfoFromCodFisc(String codFisc)
      throws SolmrException;
  
  public Long insertParticellaSigmater(ParticellaSigmaterVO partSigVO)
      throws SolmrException;
  
  public void importaTitolaritaSigmater(long idParticella, 
      Titolarita[] titolarita, long idUtente) throws SolmrException;
	
	
	
}
