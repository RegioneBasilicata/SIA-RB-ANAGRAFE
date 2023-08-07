package it.csi.solmr.business.anag;

import it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni;
import it.csi.smranag.smrgaa.integration.ConduzioneParticellaGaaDAO;
import it.csi.smranag.smrgaa.integration.DocumentoGaaDAO;
import it.csi.solmr.dto.anag.ElencoNotificheVO;
import it.csi.solmr.dto.anag.NotificaEntitaVO;
import it.csi.solmr.dto.anag.NotificaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.NotificaDAO;
import it.csi.solmr.integration.anag.UnitaArboreaDichiarataDAO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/Notifica",mappedName="comp/env/solmr/anag/Notifica")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class NotificaBean implements NotificaLocal
{

  /**
   * 
   */
  private static final long serialVersionUID = -1421590110206500785L;
  SessionContext sessionContext;
  private transient NotificaDAO notificaDAO = null;
  private transient DocumentoGaaDAO documentoGaaDAO = null;
  private transient UnitaArboreaDichiarataDAO unitaArboreaDichiarataDAO = null;
  private transient ConduzioneParticellaGaaDAO conduzioneParticellaGaaDAO = null;

 
  @Resource
  public void setSessionContext(SessionContext sessionContext)
  {
    this.sessionContext = sessionContext;
    initializeDAO();
  }

  private void initializeDAO() throws EJBException
  {
    try
    {
      notificaDAO = new NotificaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      documentoGaaDAO = new DocumentoGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      unitaArboreaDichiarataDAO = new UnitaArboreaDichiarataDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      conduzioneParticellaGaaDAO = new ConduzioneParticellaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  // Metodo per effettuare la ricerca delle notifiche in relazione a dei
  // parametri di ricerca
  public ElencoNotificheVO ricercaNotificheByParametri(NotificaVO notificaVO,
      UtenteAbilitazioni utenteAbilitazioni, RuoloUtenza ruoloUtenza, 
      Boolean storico, int maxRecord) throws Exception, SolmrException
  {
    ElencoNotificheVO elencoNotificheVO = null;
    try
    {
      elencoNotificheVO = notificaDAO.ricercaNotificheByParametri(notificaVO,
          utenteAbilitazioni, ruoloUtenza, storico, maxRecord);
      
      if(elencoNotificheVO.getvElencoNotifiche() != null)
      {
        for(int i=0;i<elencoNotificheVO.getvElencoNotifiche().size();i++)
        {
          elencoNotificheVO.getvElencoNotifiche().get(i).setvAllegatoDocumento(
            documentoGaaDAO.getElencoFileAllegatiNotifica(elencoNotificheVO.getvElencoNotifiche().get(i)
                .getIdNotifica().longValue()));
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoNotificheVO;
  }

  // Metodo per effettuare la ricerca della notifica a partire dalla chiave
  // primaria
  public NotificaVO findNotificaByPrimaryKey(Long idNotifica, String provenienza)
      throws SolmrException, Exception
  {
    NotificaVO notificaVO = null;
    try
    {
      notificaVO = notificaDAO.findNotificaByPrimaryKey(idNotifica);
      if(notificaVO != null)
      {
        notificaVO.setvAllegatoDocumento(
            documentoGaaDAO.getElencoFileAllegatiNotifica(notificaVO
                .getIdNotifica().longValue()));
        if("dettaglio".equalsIgnoreCase(provenienza))
        {
          notificaVO.setvNotificaEntita(notificaDAO.getElencoNotificheEntitaByIdNotifica(idNotifica, true));
        }
        else
        {
          notificaVO.setvNotificaEntita(notificaDAO.getElencoNotificheEntitaByIdNotifica(notificaVO.getIdNotifica().longValue(), false));
        }
        
        if(notificaVO.getvNotificaEntita() != null)
        {
          //E' per tutti uguale
          Long idDichiarazioneConsistenza = notificaVO.getvNotificaEntita().get(0).getIdDichiarazioneConsistenza();
          if(notificaVO.getIdTipoEntita().compareTo(new Integer(SolmrConstants.TIPO_ENTITA_UV)) == 0)
          {
            if("dettaglio".equalsIgnoreCase(provenienza))
            {
              notificaVO.setElencoParticelle(unitaArboreaDichiarataDAO.getElencoStoricoUnitaArboreaByNotifica(idNotifica, idDichiarazioneConsistenza, true));
            }
            else
            {
              notificaVO.setElencoParticelle(unitaArboreaDichiarataDAO.getElencoStoricoUnitaArboreaByNotifica(idNotifica, idDichiarazioneConsistenza, false));
            }
            for(int i=0;i<notificaVO.getvNotificaEntita().size();i++)
            {
              for(int j=0;j<notificaVO.getElencoParticelle().size();j++)
              {
                if(notificaVO.getElencoParticelle().get(j).getStoricoUnitaArboreaVO()
                  .getIdUnitaArborea().compareTo(notificaVO.getvNotificaEntita().get(i).getIdentificativo()) == 0)
                {
                  notificaVO.getElencoParticelle().get(j).getUnitaArboreaDichiarataVO().setNote(
                    notificaVO.getvNotificaEntita().get(i).getNote());
                  break;
                }
              }
            }
          }
          //particelle
          else
          {            
            if("dettaglio".equalsIgnoreCase(provenienza))
            {
              if(Validator.isNotEmpty(idDichiarazioneConsistenza))
              {
                notificaVO.setElencoParticelle(conduzioneParticellaGaaDAO.getElencoStoricoParticellaByNotifica(idNotifica, idDichiarazioneConsistenza, true));
              }
              else
              {
                notificaVO.setElencoParticelle(conduzioneParticellaGaaDAO.getElencoStoricoParticellaByNotificaNoDich(idNotifica, true));
              }
            }
            else
            {
              if(Validator.isNotEmpty(idDichiarazioneConsistenza))
              {
                notificaVO.setElencoParticelle(conduzioneParticellaGaaDAO.getElencoStoricoParticellaByNotifica(idNotifica, idDichiarazioneConsistenza, false));
              }
              else
              {
                notificaVO.setElencoParticelle(conduzioneParticellaGaaDAO.getElencoStoricoParticellaByNotificaNoDich(idNotifica, false));
              }
            }
            
            for(int i=0;i<notificaVO.getvNotificaEntita().size();i++)
            {
              for(int j=0;j<notificaVO.getElencoParticelle().size();j++)
              {
                if(notificaVO.getElencoParticelle().get(j).getIdParticella()
                  .compareTo(notificaVO.getvNotificaEntita().get(i).getIdentificativo()) == 0)
                {
                  notificaVO.getElencoParticelle().get(j).setNote(
                    notificaVO.getvNotificaEntita().get(i).getNote());
                  break;
                }
              }
            }            
          }
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return notificaVO;
  }

  // Metodo per recuperare l'elenco delle notifiche in relazione ad un'azienda
  // agricola e ad
  // una situazione(attuale o storica) relative ad un procedimento specificato
  public Vector<NotificaVO> getElencoNotificheByIdAzienda(
      NotificaVO notificaVO, Boolean storico, String ordinamento)
      throws Exception, SolmrException
  {
    Vector<NotificaVO> elencoNotifiche = null;
    try
    {
      elencoNotifiche = notificaDAO.getElencoNotificheByIdAzienda(notificaVO,
          storico, ordinamento);
      
      
      if(elencoNotifiche != null)
      {
        for(int i=0;i<elencoNotifiche.size();i++)
        {
          elencoNotifiche.get(i).setvAllegatoDocumento(
            documentoGaaDAO.getElencoFileAllegatiNotifica(elencoNotifiche.get(i)
                .getIdNotifica().longValue()));
        }
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
    return elencoNotifiche;
  }
  
  public Vector<NotificaVO> getElencoNotifichePopUp(NotificaVO notificaVO) 
    throws Exception
  {
    try
    {
      return notificaDAO.getElencoNotifichePopUp(notificaVO);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per effettuare l'inserimento di una notifica
  public Long insertNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    Long idNotifica = null;
    try
    {
      idNotifica = notificaDAO
          .insertNotifica(notificaVO, idUtenteAggiornamento);
      if(Validator.isNotEmpty(notificaVO.getvNotificaEntita()))
      {
        for(int i=0;i<notificaVO.getvNotificaEntita().size();i++)
        {
          NotificaEntitaVO notificaEntitaVO = notificaVO.getvNotificaEntita().get(i);
          notificaEntitaVO.setIdNotifica(idNotifica);
          notificaEntitaVO.setDataInizioValidita(new Date());
          notificaEntitaVO.setDataAggiornamento(new Date());
          notificaEntitaVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
          notificaEntitaVO.setIdUtenteInserimento(idUtenteAggiornamento);
          notificaDAO.insertNotificaEntita(notificaEntitaVO);
        }
        
      }
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
    return idNotifica;
  }
  
  
  public void updateNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    try
    {
      notificaDAO.updateNotifica(notificaVO, idUtenteAggiornamento);
      Vector<NotificaEntitaVO> vNotificheEntitaDB = notificaDAO.getElencoNotificheEntitaByIdNotifica(notificaVO.getIdNotifica().longValue(), false);
      //nn ho niente su db solo insert
      if(Validator.isEmpty(vNotificheEntitaDB)
        && Validator.isNotEmpty(notificaVO.getvNotificaEntita()))
      {
        for(int i=0;i<notificaVO.getvNotificaEntita().size();i++)
        {
          NotificaEntitaVO notificaEntitaVO = notificaVO.getvNotificaEntita().get(i);
          notificaEntitaVO.setIdNotifica(notificaVO.getIdNotifica());
          notificaEntitaVO.setDataInizioValidita(new Date());
          notificaEntitaVO.setDataAggiornamento(new Date());
          notificaEntitaVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
          notificaDAO.insertNotificaEntita(notificaEntitaVO);
        }
      }
      //non ho niente nell'inserimento ma ho su DB
      else if(Validator.isNotEmpty(vNotificheEntitaDB)
        && Validator.isEmpty(notificaVO.getvNotificaEntita()))
      {
        notificaDAO.storicizzaNotificaEntita(notificaVO.getIdNotifica().longValue(),
            idUtenteAggiornamento);        
      }
      else if(Validator.isNotEmpty(vNotificheEntitaDB)
        && Validator.isNotEmpty(notificaVO.getvNotificaEntita()))
      {
        for(int i=0;i<notificaVO.getvNotificaEntita().size();i++)
        {
          NotificaEntitaVO notificaEntitaVOIns = notificaVO.getvNotificaEntita().get(i);
          //Già registrata su db
          
          for(int j=0;j<vNotificheEntitaDB.size();j++)
          {
            NotificaEntitaVO notificaEntitaVODB = vNotificheEntitaDB.get(j);
            if(notificaEntitaVOIns.getIdentificativo()
              .compareTo(notificaEntitaVODB.getIdentificativo()) == 0)
            {
              boolean flagDiversi = false;
              notificaEntitaVODB.setInserimento(true);
              notificaEntitaVOIns.setInserimento(true);
              if(Validator.isNotEmpty(notificaEntitaVOIns.getNote())
                && Validator.isEmpty(notificaEntitaVODB.getNote()))
              {
                flagDiversi = true;
              }
              else if(Validator.isEmpty(notificaEntitaVOIns.getNote())
                && Validator.isNotEmpty(notificaEntitaVODB.getNote()))
              {
                 flagDiversi = true;
              }
              else if(Validator.isNotEmpty(notificaEntitaVOIns.getNote())
                && Validator.isNotEmpty(notificaEntitaVODB.getNote()))
              {
                if(!notificaEntitaVOIns.getNote().trim().equalsIgnoreCase(
                    notificaEntitaVODB.getNote().trim()))
                {
                  flagDiversi = true;
                }                  
              }
              //Tutti e due vuoti...nn faccio nulla
              else
              {}
              
              if(flagDiversi)
              {
                notificaDAO.storicizzaNotificaEntitaByPrimaryKey(notificaEntitaVODB.getIdNotificaEntita().longValue(),
                    idUtenteAggiornamento);
                
                NotificaEntitaVO notificaEntitaVO = notificaEntitaVODB;
                notificaEntitaVO.setDataInizioValidita(new Date());
                notificaEntitaVO.setDataAggiornamento(new Date());
                notificaEntitaVO.setIdUtenteInserimento(idUtenteAggiornamento);
                notificaEntitaVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
                notificaEntitaVO.setNote(notificaEntitaVOIns.getNote());
                notificaDAO.insertNotificaEntita(notificaEntitaVO);                  
              }
              
              break;
            }
          }            
          
        }
        
        //Chiusura
        for(int i=0;i<vNotificheEntitaDB.size();i++)
        {
          if(!vNotificheEntitaDB.get(i).isInserimento())
          {
            notificaDAO.storicizzaNotificaEntitaByPrimaryKey(vNotificheEntitaDB.get(i)
                .getIdNotificaEntita().longValue(), idUtenteAggiornamento);            
          }          
        }
        
        //Nuovi
        for(int i=0;i<notificaVO.getvNotificaEntita().size();i++)
        {
          if(!notificaVO.getvNotificaEntita().get(i).isInserimento())
          {
            NotificaEntitaVO notificaEntitaVO = notificaVO.getvNotificaEntita().get(i);
            notificaEntitaVO.setIdNotifica(notificaVO.getIdNotifica());
            notificaEntitaVO.setDataInizioValidita(new Date());
            notificaEntitaVO.setDataAggiornamento(new Date());
            notificaEntitaVO.setIdUtenteAggiornamento(idUtenteAggiornamento);
            notificaEntitaVO.setIdUtenteInserimento(idUtenteAggiornamento);
            notificaDAO.insertNotificaEntita(notificaEntitaVO);            
          }          
        }
        
        
        
      }
      //entrambi nulla nn faccio nulla
      else
      {}
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }

  // Metodo per effettuare la chiusura di una notifica
  public void closeNotifica(NotificaVO notificaVO, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    try
    {
      //Notifiche con uv/particelle
      if(Validator.isNotEmpty(notificaVO.getIdTipoEntita())) 
      {
        //significa che ho chiuso tutto le uv/particelle
        //e devo chiudere anche la notifica
        if(Validator.isNotEmpty(notificaVO.getNoteChisura()))
        {
          notificaDAO.closeNotifica(notificaVO, idUtenteAggiornamento);
        }
        if(Validator.isNotEmpty(notificaVO.getvNotificaEntita()))
        {
          for(int i=0;i<notificaVO.getvNotificaEntita().size();i++)
          {
            if(Validator.isNotEmpty(notificaVO.getvNotificaEntita().get(i).getNoteChiusuraEntita()))
            {
              notificaDAO.chiudiNotificaEntita(notificaVO.getvNotificaEntita().get(i), idUtenteAggiornamento);
            }
          }
        }
      }
      else
      {
        notificaDAO.closeNotifica(notificaVO, idUtenteAggiornamento);
      }
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Long getIdTipologiaNotificaFromCategoria(Long idCategoriaNotifica)
      throws Exception
  {
    try
    {
      return notificaDAO.getIdTipologiaNotificaFromCategoria(idCategoriaNotifica);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public boolean isChiusuraNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws Exception
  {
    try
    {
      return notificaDAO.isChiusuraNotificaRuoloPossibile(ruoloUtenza, idCategoriaNotifica);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public Vector<NotificaVO> getElencoNotificheForIdentificato(long identificativo, 
      String codiceTipo, long idAzienda, Long idDichiarazioneConsistenza) 
    throws Exception
  {
    try
    {
      return notificaDAO.getElencoNotificheForIdentificato(identificativo, codiceTipo, 
          idAzienda, idDichiarazioneConsistenza);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public boolean isModificaNotificaRuoloPossibile(RuoloUtenza ruoloUtenza, long idCategoriaNotifica)
      throws Exception
  {
    try
    {
      return notificaDAO.isModificaNotificaRuoloPossibile(ruoloUtenza, idCategoriaNotifica);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaUvFromIdNotifica(
      long ids[]) throws Exception
  {
    try
    {
      return notificaDAO.getNotificheEntitaUvFromIdNotifica(ids);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  public HashMap<Long,Vector<NotificaEntitaVO>> getNotificheEntitaParticellaFromIdNotifica(
      long ids[]) throws Exception
  {
    try
    {
      return notificaDAO.getNotificheEntitaParticellaFromIdNotifica(ids);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  

}
