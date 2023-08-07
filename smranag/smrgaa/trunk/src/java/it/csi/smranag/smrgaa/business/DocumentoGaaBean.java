package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO;
import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.TipoFirmaVO;
import it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO;
import it.csi.smranag.smrgaa.dto.terreni.ParticellaIstanzaRiesameVO;
import it.csi.smranag.smrgaa.integration.DocumentoGaaDAO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.TipoDocumentoVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.util.Date;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

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

@Stateless(name="comp/env/solmr/anag/DocumentoGaa",mappedName="comp/env/solmr/anag/DocumentoGaa")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class DocumentoGaaBean implements DocumentoGaaLocal
{
  

  /**
   * 
   */
  private static final long serialVersionUID = 7644970206413435210L;

  SessionContext                   sessionContext;

  private transient DocumentoGaaDAO dDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      dDAO = new DocumentoGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

 
  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }
  
  
  
  public Vector<BaseCodeDescription> getCategoriaDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException
  {
    try
    {
      return dDAO.getCategoriaDocumentiTerritorialiAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public Vector<BaseCodeDescription> getDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException
  {
    try
    {
      return dDAO.getDocumentiTerritorialiAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public Vector<BaseCodeDescription> getProtocolliDocumentiTerritorialiAzienda(
      long idAzienda) throws SolmrException
  {
    try
    {
      return dDAO.getProtocolliDocumentiTerritorialiAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  public boolean isDocumentoIstanzaRiesame(
      long idDocumento) throws SolmrException
  {
    try
    {
      return dDAO.isDocumentoIstanzaRiesame(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
    
  }
  
  
  public Vector<BaseCodeDescription> getCausaleModificaDocumentoValid() throws SolmrException
  {
    try
    {
      return dDAO.getCausaleModificaDocumentoValid();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<Long> esisteDocumentoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
  throws SolmrException
  {
    try
    {
      return dDAO.esisteDocumentoAttivoByConduzioneAndAzienda(elencoConduzioni, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegati(
      long idDocumento)
  throws SolmrException
  {
    try
    {
      return dDAO.getElencoFileAllegati(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public void deleteFileAllegato(long idAllegato, long idDocumento) 
      throws SolmrException
  {
    try
    {
      int numOccurs = dDAO.getNumFileAllegato(idAllegato);
      dDAO.deleteAllegatoDocumento(idAllegato, idDocumento);
      //Significa che l'allegato è legato solo al documento corrente
      //Quindi posso eliminarlo da DB
      if(numOccurs <= 1)
      {
        dDAO.deleteFileAllegato(idAllegato);
      }
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public void deleteFileAllegatoRichiesta(long idAllegato, long idRichiestaDocumento) 
      throws SolmrException
  {
    try
    {
      dDAO.deleteAllegatoRichiesta(idAllegato, idRichiestaDocumento);
      dDAO.deleteFileAllegato(idAllegato);
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public void deleteFileAllegatoNotifica(long idAllegato, long idNotifica) 
      throws SolmrException
  {
    try
    {
      dDAO.deleteAllegatoNotifica(idAllegato, idNotifica);
      dDAO.deleteFileAllegato(idAllegato);
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Long insertFileAllegato(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException
  {
    try
    {
      Long idAllegato = dDAO.insertFileAllegato(allegatoDocumentoVO);      
      dDAO.insertAllegatoDocumento(allegatoDocumentoVO.getIdDocumento().longValue(),
          idAllegato.longValue());
      
      return idAllegato;          
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Long insertFileAllegatoSemplice(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException
  {
    try
    {      
      return dDAO.insertFileAllegato(allegatoDocumentoVO);          
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Long insertFileAllegatoRichiesta(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException
  {
    try
    {
      Long idAllegato = dDAO.insertFileAllegato(allegatoDocumentoVO);      
      dDAO.insertAllegatoRichiesta(allegatoDocumentoVO.getIdDocumento().longValue(),
          idAllegato.longValue());
      
      return idAllegato;          
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Long insertFileAllegatoNotifica(AllegatoDocumentoVO allegatoDocumentoVO) 
      throws SolmrException
  {
    try
    {
      Long idAllegato = dDAO.insertFileAllegato(allegatoDocumentoVO);      
      dDAO.insertAllegatoNotifica(allegatoDocumentoVO.getIdDocumento().longValue(),
          idAllegato.longValue());
      
      return idAllegato;          
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public AllegatoDocumentoVO getFileAllegato(
      long idAllegato)
  throws SolmrException
  {
    try
    {
      return dDAO.getFileAllegato(idAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public Date getFirstDataInserimentoDocumento(long idDocumento)
     throws SolmrException
  {
    try
    {
      return dDAO.getFirstDataInserimentoDocumento(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public boolean isIstanzaRiesameFotoInterpretataByDocumento(long idDocumento) 
      throws SolmrException
  {
    try
    {
      return dDAO.isIstanzaRiesameFotoInterpretataByDocumento(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean exitsOtherDocISForParticellaAndAzienda(long idParticella, long idAzienda, Long idDocumento) 
      throws SolmrException
  {
    try
    {
      return dDAO.exitsOtherDocISForParticellaAndAzienda(idParticella, idAzienda, idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  /*public boolean isFaseIstanzaRiesameEvasa(long idAzienda, int fase, Vector<Long> vIdParticella, String parametro) 
      throws SolmrException
  {
    try
    {
      return dDAO.isFaseIstanzaRiesameEvasa(idAzienda, fase, vIdParticella, parametro);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }*/
  
  public boolean exsitsDocFaseIstanzaRiesameFasePrec(long idAzienda, int fase, 
      long idParticella, String parametro) throws SolmrException
  {
    try
    {
      return dDAO.exsitsDocFaseIstanzaRiesameFasePrec(idAzienda, fase, 
          idParticella, parametro);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isPossCreateDocFaseIstanzaRiesameFaseSucc(long idAzienda, long idParticella,
      int idFase, String parametro) throws SolmrException
  {
    try
    {
      return dDAO.isPossCreateDocFaseIstanzaRiesameFaseSucc(idAzienda, idParticella, idFase, parametro);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public Date getDateLastBatchIstanzaRiesameOk() throws SolmrException
  {
    try
    {
      return dDAO.getDateLastBatchIstanzaRiesameOk();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isSitiConvocaValid(long idAzienda, int anno, int fase) throws SolmrException
  {
    try
    {
      return dDAO.isSitiConvocaValid(idAzienda, anno, fase);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isDataSospensioneScaduta(long idAzienda, long idParticella, int anno) throws SolmrException
  {
    try
    {
      return dDAO.isDataSospensioneScaduta(idAzienda, idParticella, anno);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isParticellaEvasa(long idAzienda, long idParticella, int fase, int anno) 
      throws SolmrException
  {
    try
    {
      return dDAO.isParticellaEvasa(idAzienda, idParticella, fase, anno);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isVisibleTastoElimina(long idAzienda, int anno, int fase)  throws SolmrException
  {
    try
    {
      return dDAO.isVisibleTastoElimina(idAzienda, anno, fase);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public Vector<DocumentoVO> getListDocFromRicerca(String strDescDocumento, boolean attivi) 
      throws SolmrException
  {
    try
    {
      return dDAO.getListDocFromRicerca(strDescDocumento, attivi);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  
  public Vector<ParticellaIstanzaRiesameVO> getLisParticellaFromIstanzaFoto(long idAzienda, int anno)  
      throws SolmrException
  {
    try
    {
      return dDAO.getLisParticellaFromIstanzaFoto(idAzienda, anno);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  
  public String annullaIstanzaRiesame(Vector<Long> vIdIstanzaRiesame, long idAzienda, int anno, 
      RuoloUtenza ruoloUtenza) throws SolmrException
  {
    try
    {
      String msgErrore = "";
      Vector<Long> vIstanzaRiesameAgg = null;
      for(int i=0;i<vIdIstanzaRiesame.size();i++)
      {
        ParticellaIstanzaRiesameVO particellaVO = dDAO.getParticellaFromIstanza(
          vIdIstanzaRiesame.get(i).longValue());
        //Controllo se esiste una fase successiva
        if(dDAO.existFaseSucessivaFotoPraticella(idAzienda, particellaVO.getIdParticella()))
        {
          msgErrore+="Non e' stata aggiornata la particella "+componiNomeParticella(particellaVO)+
          ", presenza di successiva fase istanza di riesame.<br>";
          continue;
        }
        //Controllo se esiste un'altra fase fotointepretazione di anno successivo
        if(dDAO.existAltraFaseFotoParticellaPerAnnulla(idAzienda, particellaVO.getIdParticella(), anno))
        {
          msgErrore+="Non e' stata aggiornata la particella "+componiNomeParticella(particellaVO)+
          ", presenza di fase di fotointepretazione piu' recente.<br>";
          continue;
        }
        
        if(vIstanzaRiesameAgg == null)
        {
          vIstanzaRiesameAgg = new Vector<Long>();
        }
        
        vIstanzaRiesameAgg.add(vIdIstanzaRiesame.get(i));        
        
      }
      
      
      //esegui l'update
      if(vIstanzaRiesameAgg != null)
      {
        dDAO.annullaIstanzaFromId(vIstanzaRiesameAgg, ruoloUtenza.getIdUtente());        
      }
      
      
      
      
      if(Validator.isEmpty(msgErrore))
      {
        msgErrore = "Aggiornamento di tutte le particelle avvenuto con successo!!";
      }
      
      return msgErrore;          
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  
  private String componiNomeParticella(ParticellaIstanzaRiesameVO particellaVO)
  {
    String nomeParticella = "Comune-"+particellaVO.getDescComune();
    if(Validator.isNotEmpty(particellaVO.getSezione()))
    {
      nomeParticella = nomeParticella+" Sez-"+(particellaVO.getSezione());
    }
    if(Validator.isNotEmpty(particellaVO.getFoglio()))
    {
      nomeParticella = nomeParticella+" Fgl-"+(particellaVO.getFoglio());
    }
    if(Validator.isNotEmpty(particellaVO.getParticella()))
    {
      nomeParticella = nomeParticella+" Part-"+(particellaVO.getParticella());
    }
    if(Validator.isNotEmpty(particellaVO.getSubalterno()))
    {
      nomeParticella = nomeParticella+" Sub-"+(particellaVO.getSubalterno());
    }
    
    
    return nomeParticella;
  }
  
  
  public boolean existAltraFaseFotoParticella(long idAzienda, long idParticella, int anno, long idFase)
      throws SolmrException
  {
    try
    {
      return dDAO.existAltraFaseFotoParticella(idAzienda, idParticella, anno, idFase);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public Vector<TipoDocumentoVO> getDocumentiNuovaIscrizione() 
      throws SolmrException
  {
    try
    {
      return dDAO.getDocumentiNuovaIscrizione();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiRichiesta(
      long idRichiestaDocumento) throws SolmrException
  {
    try
    {
      return dDAO.getElencoFileAllegatiRichiesta(idRichiestaDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public Vector<AllegatoDocumentoVO> getElencoFileAllegatiNotifica(
      long idNotifica) throws SolmrException
  {
    try
    {
      return dDAO.getElencoFileAllegatiNotifica(idNotifica);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isIstanzaAttiva(long idAzienda) throws SolmrException
  {
    try
    {
      return dDAO.isIstanzaAttiva(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isParticellaIstRiesameCancellabile(long idAzienda, 
      Vector<Long> vIdConduzioneParticella) throws SolmrException
  {
    try
    {
      return dDAO.isParticellaIstRiesameCancellabile(idAzienda, vIdConduzioneParticella);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean existIstanzaEsameAttivaFase(long idAzienda, 
      Long idParticella, long idFase) throws SolmrException
  {
    try
    {
      return dDAO.existIstanzaEsameAttivaFase(idAzienda, idParticella, idFase);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public FaseRiesameDocumentoVO getFaseRiesameDocumentoByIdDocumento(long idDocumento) 
      throws SolmrException
  {
    try
    {
      return dDAO.getFaseRiesameDocumentoByIdDocumento(idDocumento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isParticellaInPotenziale(long idAzienda, 
      long idParticella, int anno) throws SolmrException
  {
    try
    {
      return dDAO.isParticellaInPotenziale(idAzienda, idParticella, anno);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public boolean isParticellaInPotenzialeContra(long idAzienda, 
      long idParticella, int anno) throws SolmrException
  {
    try
    {
      return dDAO.isParticellaInPotenzialeContra(idAzienda, idParticella, anno);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  
  public void insertAllegatoDocumento(long idDocumento, long idAllegato) 
      throws SolmrException
  {
    try
    {
      dDAO.insertAllegatoDocumento(idDocumento, idAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }   
  }
  
  public Long insertFileAllegatoNoFile(AllegatoDocumentoVO allegatoDocumentoVO)
      throws SolmrException
  {
    try
    {
      return dDAO.insertFileAllegatoNoFile(allegatoDocumentoVO);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
    
  }
  
  public void insertAllegatoDichiarazione(long idDichiarazioneConsistenza, long idAllegato)
      throws SolmrException
  {
    try
    {
      dDAO.insertAllegatoDichiarazione(idDichiarazioneConsistenza, idAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
    
  }
  
  public boolean isNotPossibleIstanzaRiesameFaseSuccessiva(long idAzienda, 
      long idParticella, int idFase, int anno, String parametro) 
    throws SolmrException
  {
    try
    {
      return dDAO.isNotPossibleIstanzaRiesameFaseSuccessiva(
          idAzienda, idParticella, idFase, anno, parametro);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
    
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws SolmrException
  {
    try
    {
      return dDAO.getAllegatoDichiarazioneFromIdDichiarazione(
          idDichiarazioneConsistenza, idTipoAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
      long idDichiarazioneConsistenza, long idTipoAllegato) 
    throws SolmrException
  {
    try
    {
      return dDAO.getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
          idDichiarazioneConsistenza, idTipoAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromIdAllegato(
      long idAllegato)
    throws SolmrException
  {
    try
    {
      return dDAO.getAllegatoDichiarazioneFromIdAllegato(idAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  
  public AllegatoDichiarazioneVO getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(
      long idDichiarazioneConsistenza, String tipoStampa)
    throws SolmrException
  {
    try
    {
      return dDAO.getAllegatoDichiarazioneFromTipoStampaAndIdDichiarazione(idDichiarazioneConsistenza, tipoStampa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatoDichiarazioneFromIdDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException
  {
    try
    {
      return dDAO.getElencoAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }  
    
  }
  
  public void storicizzaAllegatoDichiarazione(long idAllegatoDichiarazione)
      throws SolmrException
  {
    try
    {
      dDAO.storicizzaAllegatoDichiarazione(idAllegatoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public void storicizzaAllegatiAutomaticiDichiarazione(long idDichiarazioneConsistenza)
      throws SolmrException
  {
    try
    {
      dDAO.storicizzaAllegatiAutomaticiDichiarazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public void updateAllegatoForFirma(long idAllegato, long idUtente, Long idTipoFirma)
      throws SolmrException
  {
    try
    {
      dDAO.updateAllegatoForFirma(idAllegato, idUtente, idTipoFirma);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public void updateAllegatoForExtIdDocIndex(long idAllegato, long idUtente, Long extIdDocumentoIndex) 
      throws SolmrException
  {
    try
    {
      dDAO.updateAllegatoForExtIdDocIndex(idAllegato, idUtente, extIdDocumentoIndex);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public void deleteAllegatoFromVId(Vector<Long> vIdAllegato)
    throws SolmrException
  {
    try
    {
      dDAO.deleteAllegatoFromVId(vIdAllegato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public void deleteAllegatoDichiarazioneFromVId(Vector<Long> vIdAllegatoDichiarazione) 
      throws SolmrException
  {
    try
    {
      dDAO.deleteAllegatoDichiarazioneFromVId(vIdAllegatoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiDichiarazioneDefault(
      int idMotivoDichiarazione) 
    throws SolmrException
  {
    try
    {
      return dDAO.getElencoAllegatiDichiarazioneDefault(idMotivoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  public Vector<AllegatoDichiarazioneVO> getElencoAllegatiAttiviDichiarazione(
      long idDichiarazioneConsistenza) 
    throws SolmrException
  {
    try
    {
      return dDAO.getElencoAllegatiAttiviDichiarazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  
  public Vector<AllegatoDichiarazioneVO> getAllElencoAllegatiDichiarazione(
      long idDichiarazioneConsistenza) 
     throws SolmrException
  {
    try
    {
      return dDAO.getAllElencoAllegatiDichiarazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
  
  
  public Vector<TipoFirmaVO> getElencoTipoFirma()
    throws SolmrException
  {
    try
    {
      return dDAO.getElencoTipoFirma();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }      
  }
}
