package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.PlSqlCodeDescription;
import it.csi.smranag.smrgaa.dto.comunicazione10R.AcquaExtraVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.Comunicazione10RVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteCesAcqVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteStocExtVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.EffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.RefluoEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoAcquaAgronomicaVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoCausaleEffluenteVO;
import it.csi.smranag.smrgaa.dto.comunicazione10R.TipoEffluenteVO;
import it.csi.smranag.smrgaa.integration.Comunicazione10RGaaDAO;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.integration.anag.AnagrafeDAO;
import it.csi.solmr.integration.anag.ConsistenzaDAO;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
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

@Stateless(name=Comunicazione10RGaaBean.jndiName,mappedName=Comunicazione10RGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class Comunicazione10RGaaBean implements Comunicazione10RGaaLocal
{

  
  

  /**
   * 
   */
  private static final long serialVersionUID = -3912078387049848522L;

  SessionContext                   sessionContext;
  public final static String jndiName="comp/env/solmr/gaa/Comunicazione10R";
  

  private transient Comunicazione10RGaaDAO com10rDAO;
  private transient ConsistenzaDAO         consDAO;
  private transient AnagrafeDAO         aDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      com10rDAO = new Comunicazione10RGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      consDAO = new ConsistenzaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
      aDAO = new AnagrafeDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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
  
  public Comunicazione10RVO getComunicazione10RByIdUteAndPianoRifererimento(long idUte, long idPianoRiferimento) 
    throws SolmrException
  {
    
    try
    {
      return com10rDAO.getComunicazione10RByIdUteAndPianoRifererimento(idUte, idPianoRiferimento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<EffluenteVO> getListEffluenti(long idComunicazione10R[]) 
    throws SolmrException
  {
    
    try
    {
      return com10rDAO.getListEffluenti(idComunicazione10R); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<EffluenteVO> getListEffluentiStampa(long idComunicazione10R[]) 
    throws SolmrException
  {
    
    try
    {
      return com10rDAO.getListEffluentiStampa(idComunicazione10R); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public Vector<TipoCausaleEffluenteVO> getListTipoCausaleEffluente() 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListTipoCausaleEffluente();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoEffluenteVO> getListTipoEffluente()
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListTipoEffluente();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public TipoEffluenteVO getTipoEffluenteById(long idEffluente) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getTipoEffluenteById(idEffluente);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoAcquaAgronomicaVO> getListTipoAcquaAgronomica()
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListTipoAcquaAgronomica();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquByidComunicazione(long idComunicazione10R[], Long idTipoCausale) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListEffluentiCessAcquByidComunicazione(idComunicazione10R, idTipoCausale);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<EffluenteCesAcqVO> getListEffluentiCessAcquPerStampa(long idComunicazione10R[]) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListEffluentiCessAcquPerStampa(idComunicazione10R);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<EffluenteStocExtVO> getListStoccaggiExtrAziendali(long idComunicazione10R[]) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListStoccaggiExtrAziendali(idComunicazione10R);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<AcquaExtraVO> getListAcquaExtra(long idComunicazione10R[]) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListAcquaExtra(idComunicazione10R);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  /**
   * Metodo che si occupa di storicizzare la comunicazione 10R.
   * Se esiste una dichiarazione di consistenza sucessiva alla comunicazione 10R,
   * si storicizza il record corrente della tabella DB_COMUNICAZIONE_10R
   * e poi si fa la insert di tutte le tabelle.
   * Se non esiste si fa l'update della tabella DB_COMUNICAZIONE_10R e la delete
   * e la insert delle altre tabelle. 
   * 
   * @param profile
   * @param vAnagAziendaCollegateVO 
   * @throws SolmrException
   */
  public PlSqlCodeDescription storicizzaComunicazione10R(long idUtenteAggiornamento, long idAzienda,
      Vector<Comunicazione10RVO> vCom10r, Vector<EffluenteVO> vEffluentiTratt, 
      Vector<EffluenteCesAcqVO> vCessioniAcquisizioni, Vector<EffluenteCesAcqVO> vCessioni,
      Vector<EffluenteStocExtVO> vStoccaggi, Vector<AcquaExtraVO> vAcqueExtra) throws SolmrException
  {

    try
    {
      Vector<ConsistenzaVO> vDichCons = consDAO.getDichiarazioniConsistenza(new Long(idAzienda));
      boolean flagTrovata = false;
      // Record corrente
      // Se trovo una dichiarazione di consistenza che ha la data di
      // inserimento posteriore
      // alla data di creazione dell'associazione storicizzo
      
      //Prendo una comunicazione 10R a caso tanto la data inizio validita è uguale per tutte
      //le dich relative alla stessa azienda
      Comunicazione10RVO com10rCom = (Comunicazione10RVO)vCom10r.get(0); 
      if ((vDichCons != null) && (vDichCons.size() > 0))
      {
        for (int j = 0; j < vDichCons.size(); j++)
        {
          ConsistenzaVO consVO = (ConsistenzaVO) vDichCons.get(j);
          if (com10rCom.getDataInizioValidita().before(
              consVO.getDataInserimentoDichiarazione()))
          {
            flagTrovata = true;
            break;
          }
        }
      }
      
      for(int k=0;k<vCom10r.size();k++)
      {
        
        Comunicazione10RVO com10RVO = (Comunicazione10RVO)vCom10r.get(k);
        //per impedire a due in conteporanea di modificare i dati.
        //com10rDAO.lockTableComunicazione10R(com10RVO.getIdUte());
        //***********************************************************
        
        Comunicazione10RVO com10rTmp = com10rDAO
          .getComunicazione10RByIdUteAndPianoRifererimento(com10RVO.getIdUte(), -1);
        
        if(flagTrovata)
        { //storicizzo
          com10rDAO.storicizzaComunicazione10R(com10rTmp.getIdComunicazione10R());
          com10rTmp.setDataFineValidita(null);
          com10rTmp.setDataInizioValidita(new Date());
          com10rTmp.setDataAggiornamento(new Date());
          com10rTmp.setIdUtenteAggiornamento(idUtenteAggiornamento);
          com10rTmp.setVolumeSottogrigliato(com10RVO.getVolumeSottogrigliato());
          com10rTmp.setVolumeRefluoAzienda(com10RVO.getVolumeRefluoAzienda());
          com10rTmp.setNote(com10RVO.getNote());
          com10rTmp.setAdesioneDeroga(com10RVO.getAdesioneDeroga());
          Long idComunicazione10R = com10rDAO.insertComunicazione10R(com10rTmp);
          
          if(vEffluentiTratt !=null)
          {
            for(int i=0;i<vEffluentiTratt.size();i++)
            {
              EffluenteVO effVO = vEffluentiTratt.get(i);
              if(effVO.getIdComunicazione10R() == com10RVO.getIdComunicazione10R())
              {
                effVO.setIdComunicazione10R(idComunicazione10R.longValue());
                if(effVO.getVolumePostDichiarato() == null)
                {
                  effVO.setVolumePostDichiarato(new BigDecimal(0));
                }
                if(effVO.getAzotoPostDichiarato() == null)
                {
                  effVO.setAzotoPostDichiarato(new BigDecimal(0));
                }
                               
                Long idEffluenteTrattato = com10rDAO.getIdEffluenteByLegameAndTipo(
                  effVO.getIdEffluenteOrigine(), "S");
                if(idEffluenteTrattato == null)
                {
                  throw new SolmrException("Impossibile procedere si sta cercando di inserire un trattamento che non origina palabili");
                }
                effVO.setIdEffluente(idEffluenteTrattato);
                com10rDAO.insertEffluente10R(effVO);
                
                if(effVO.getVolumePostDichiaratoNonPal() == null)
                {
                  effVO.setVolumePostDichiaratoNonPal(new BigDecimal(0));
                }
                if(effVO.getAzotoPostDichiaratoNonPal() == null)
                {
                  effVO.setAzotoPostDichiaratoNonPal(new BigDecimal(0));
                }
                
                idEffluenteTrattato = com10rDAO.getIdEffluenteByLegameAndTipo(
                   effVO.getIdEffluenteOrigine(), "N");
                effVO.setVolumePostDichiarato(effVO.getVolumePostDichiaratoNonPal());
                effVO.setAzotoPostDichiarato(effVO.getAzotoPostDichiaratoNonPal());
                effVO.setVolumePostTrattamento(effVO.getVolumePostTrattamentoNonPal());
                effVO.setAzotoPostTrattamento(effVO.getAzotoPostTrattamentoNonPal());
                if(idEffluenteTrattato == null)
                {
                  throw new SolmrException("Impossibile procedere si sta cercando di inserire un trattamento che non origina non palabili");
                }
                effVO.setIdEffluente(idEffluenteTrattato);
                com10rDAO.insertEffluente10R(effVO);             
                
              }
            }
          }
          
          if(vCessioniAcquisizioni !=null)
          {
            for(int i=0;i<vCessioniAcquisizioni.size();i++)
            {
              EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(i);
              //inserisco solo quelli relativi alla prorpia dichiarazione 10R
              if(effVO.getIdComunicazione10R() == com10RVO.getIdComunicazione10R())
              {
                effVO.setIdComunicazione10R(idComunicazione10R.longValue());
                //Se valorizzato l'id_azienda non devo inserire cuaa e denomnazione
                if(Validator.isNotEmpty(effVO.getIdAzienda()))
                {
                  //Controllo che l'azienda non sia cessata
                  AnagAziendaVO anagAziendaVO = aDAO.findAziendaAttiva(effVO.getIdAzienda());
                  //Se anagAziendaVO è null significa che l'azienda è cessata
                  //per cui la tratto al pari di quelle che non sono censite in anagrafe.               
                  if(Validator.isNotEmpty(anagAziendaVO))
                  {                  
                    effVO.setCuaa(null);
                    effVO.setDenominazione(null);
                  }
                }
                 
                com10rDAO.insertEffluenteCesAcq10R(effVO);
              }
            }
          }
          
          if(vCessioni !=null)
          {
            for(int i=0;i<vCessioni.size();i++)
            {
              EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioni.get(i);
              //inserisco solo quelli relativi alla prorpia dichiarazione 10R
              if(effVO.getIdComunicazione10R() == com10RVO.getIdComunicazione10R())
              {
                effVO.setIdComunicazione10R(idComunicazione10R.longValue());
                //Se valorizzato l'id_azienda non devo inserire cuaa e denomnazione
                if(Validator.isNotEmpty(effVO.getIdAzienda()))
                {
                  //Controllo che l'azienda non sia cessata
                  AnagAziendaVO anagAziendaVO = aDAO.findAziendaAttiva(effVO.getIdAzienda());
                  //Se anagAziendaVO è null significa che l'azienda è cessata
                  //per cui la tratto al pari di quelle che non sono censite in anagrafe.               
                  if(Validator.isNotEmpty(anagAziendaVO))
                  {                  
                    effVO.setCuaa(null);
                    effVO.setDenominazione(null);
                  }
                }
                 
                com10rDAO.insertEffluenteCesAcq10R(effVO);
              }
            }
          }
          
          if(vStoccaggi !=null)
          {
            for(int i=0;i<vStoccaggi.size();i++)
            {
              EffluenteStocExtVO effVO = (EffluenteStocExtVO)vStoccaggi.get(i);
              //inserisco solo quelli relativi alla prorpia dichiarazione 10R
              if(effVO.getIdComunicazione10R() == com10RVO.getIdComunicazione10R())
              {
                effVO.setIdComunicazione10R(idComunicazione10R.longValue());                            
                //Se valorizzato l'id_azienda non devo inserire cuaa e denomnazione
                if(Validator.isNotEmpty(effVO.getIdAzienda()))
                {
                  //Controllo che l'azienda non sia cessata
                  AnagAziendaVO anagAziendaVO = aDAO.findAziendaAttiva(effVO.getIdAzienda());
                  //Se anagAziendaVO è null significa che l'azienda è cessata
                  //per cui la tratto al pari di quelle che non sono censite in amagrafe.               
                  if(Validator.isNotEmpty(anagAziendaVO))
                  {                  
                    effVO.setCuaa(null);
                    effVO.setDenominazione(null);
                  }
                }        
                
                com10rDAO.insertEffluenteStocExtAcq10R(effVO);
              }
            }
          }
          
          if(vAcqueExtra !=null)
          {
            for(int i=0;i<vAcqueExtra.size();i++)
            {
              AcquaExtraVO acquVO = (AcquaExtraVO)vAcqueExtra.get(i);
              //inserisco solo quelli relativi alla prorpia dichiarazione 10R
              if(acquVO.getIdComunicazione10R() == com10RVO.getIdComunicazione10R())
              {
                acquVO.setIdComunicazione10R(idComunicazione10R.longValue());
                com10rDAO.insertAcquaExtra10R(acquVO);
              }
            }
          }
          
          
        }
        else
        {
          //tab comunicazione10r
          com10rTmp.setDataAggiornamento(new Date());
          com10rTmp.setIdUtenteAggiornamento(idUtenteAggiornamento);
          com10rTmp.setVolumeSottogrigliato(com10RVO.getVolumeSottogrigliato());
          com10rTmp.setVolumeRefluoAzienda(com10RVO.getVolumeRefluoAzienda());
          com10rTmp.setNote(com10RVO.getNote());
          com10rTmp.setAdesioneDeroga(com10RVO.getAdesioneDeroga());
          com10rDAO.updateComunicazione10R(com10rTmp);
          
          //elimino
          long idtemp[]={com10rTmp.getIdComunicazione10R()};
          Vector<Long> vEffluentiElim = com10rDAO.getListTrattamentiElim(idtemp); 
          if(vEffluentiElim !=null)
          {
            for(int i=0;i<vEffluentiElim.size();i++)
            {
              com10rDAO.deleteEffluente10R(vEffluentiElim.get(i).longValue());
            }
          }
          
          Vector<EffluenteCesAcqVO> vAcquisizioniElim = com10rDAO
            .getListEffluentiCessAcquByidComunicazione(idtemp, SolmrConstants.ID_TIPO_CAUS_EFF_ACQUISIZIONE);
          if(vAcquisizioniElim !=null)
          {
            for(int i=0;i<vAcquisizioniElim.size();i++)
            {
              EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vAcquisizioniElim.get(i);
              com10rDAO.deleteEffluenteCesAcq10R(effVO.getIdEffluenteCesAcq10R());
            }
          }
          
          Vector<EffluenteCesAcqVO> vCessioniElim = com10rDAO
            .getListEffluentiCessAcquByidComunicazione(idtemp, SolmrConstants.ID_TIPO_CAUS_EFF_CESSIONE);
          if(vCessioniElim !=null)
          {
            for(int i=0;i<vCessioniElim.size();i++)
            {
              EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioniElim.get(i);
              com10rDAO.deleteEffluenteCesAcq10R(effVO.getIdEffluenteCesAcq10R());
            }
          }
          
          Vector<EffluenteStocExtVO> vStoccaggiElim = com10rDAO
            .getListStoccaggiExtrAziendali(idtemp);
          if(vStoccaggiElim !=null)
          {
            for(int i=0;i<vStoccaggiElim.size();i++)
            {
              EffluenteStocExtVO effVO = (EffluenteStocExtVO)vStoccaggiElim.get(i);
              com10rDAO.deleteEffluenteStocExt10R(effVO.getIdEffluenteStocExt10R());
            }
          }
          
          Vector<AcquaExtraVO> vAcqueExtraElim = com10rDAO
            .getListAcquaExtra(idtemp);
          if(vAcqueExtraElim !=null)
          {
            for(int i=0;i<vAcqueExtraElim.size();i++)
            {
              AcquaExtraVO acqVO = (AcquaExtraVO)vAcqueExtraElim.get(i);
              com10rDAO.deleteAcquaExtra10R(acqVO.getIdAcquaExtra10R());
            }
          }
          
          //aggiungo
          if(vEffluentiTratt !=null)
          {
            for(int i=0;i<vEffluentiTratt.size();i++)
            {
              EffluenteVO effVO = vEffluentiTratt.get(i);
              if(effVO.getIdComunicazione10R() == com10rTmp.getIdComunicazione10R())
              {
                effVO.setIdComunicazione10R(com10rTmp.getIdComunicazione10R());
                
                if(effVO.getVolumePostDichiarato() == null)
                {
                  effVO.setVolumePostDichiarato(new BigDecimal(0));
                }
                if(effVO.getAzotoPostDichiarato() == null)
                {
                  effVO.setAzotoPostDichiarato(new BigDecimal(0));
                }
                
                Long idEffluenteTrattato = com10rDAO.getIdEffluenteByLegameAndTipo(
                    effVO.getIdEffluenteOrigine(), "S");
                if(idEffluenteTrattato == null)
                {
                  throw new SolmrException("Impossibile procedere si sta cercando di inserire un trattamento che non origina palabili");
                }
                effVO.setIdEffluente(idEffluenteTrattato);
                com10rDAO.insertEffluente10R(effVO);
                
                
                if(effVO.getVolumePostDichiaratoNonPal() == null)
                {
                  effVO.setVolumePostDichiaratoNonPal(new BigDecimal(0));
                }
                if(effVO.getAzotoPostDichiaratoNonPal() == null)
                {
                  effVO.setAzotoPostDichiaratoNonPal(new BigDecimal(0));
                }
                
                idEffluenteTrattato = com10rDAO.getIdEffluenteByLegameAndTipo(
                    effVO.getIdEffluenteOrigine(), "N");
                effVO.setVolumePostDichiarato(effVO.getVolumePostDichiaratoNonPal());
                effVO.setAzotoPostDichiarato(effVO.getAzotoPostDichiaratoNonPal());
                effVO.setVolumePostTrattamento(effVO.getVolumePostTrattamentoNonPal());
                effVO.setAzotoPostTrattamento(effVO.getAzotoPostTrattamentoNonPal());
                if(idEffluenteTrattato == null)
                {
                  throw new SolmrException("Impossibile procedere si sta cercando di inserire un trattamento che non origina non palabili");
                }
                effVO.setIdEffluente(idEffluenteTrattato);
                com10rDAO.insertEffluente10R(effVO);
                
              
                
              }
            }
          }
          
          if(vCessioniAcquisizioni !=null)
          {
            for(int i=0;i<vCessioniAcquisizioni.size();i++)
            {
              EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioniAcquisizioni.get(i);
              if(effVO.getIdComunicazione10R() == com10rTmp.getIdComunicazione10R())
              {
                effVO.setIdComunicazione10R(com10rTmp.getIdComunicazione10R());
                //Se valorizzato l'id_azienda non devo inserire cuaa e denomnazione
                if(Validator.isNotEmpty(effVO.getIdAzienda()))
                {
                  //Controllo che l'azienda non sia cessata
                  AnagAziendaVO anagAziendaVO = aDAO.findAziendaAttiva(effVO.getIdAzienda());
                  //Se anagAziendaVO è null significa che l'azienda è cessata
                  //per cui la tratto al pari di quelle che non sono censite in amagrafe.               
                  if(Validator.isNotEmpty(anagAziendaVO))
                  {                  
                    effVO.setCuaa(null);
                    effVO.setDenominazione(null);
                  }
                }
                com10rDAO.insertEffluenteCesAcq10R(effVO);
              }
            }
          }
          
          if(vCessioni !=null)
          {
            for(int i=0;i<vCessioni.size();i++)
            {
              EffluenteCesAcqVO effVO = (EffluenteCesAcqVO)vCessioni.get(i);
              if(effVO.getIdComunicazione10R() == com10rTmp.getIdComunicazione10R())
              {
                effVO.setIdComunicazione10R(com10rTmp.getIdComunicazione10R());
                //Se valorizzato l'id_azienda non devo inserire cuaa e denomnazione
                if(Validator.isNotEmpty(effVO.getIdAzienda()))
                {
                  //Controllo che l'azienda non sia cessata
                  AnagAziendaVO anagAziendaVO = aDAO.findAziendaAttiva(effVO.getIdAzienda());
                  //Se anagAziendaVO è null significa che l'azienda è cessata
                  //per cui la tratto al pari di quelle che non sono censite in amagrafe.               
                  if(Validator.isNotEmpty(anagAziendaVO))
                  {                  
                    effVO.setCuaa(null);
                    effVO.setDenominazione(null);
                  }
                }
                com10rDAO.insertEffluenteCesAcq10R(effVO);
              }
            }
          }
          
          if(vStoccaggi !=null)
          {
            for(int i=0;i<vStoccaggi.size();i++)
            {
              EffluenteStocExtVO effVO = (EffluenteStocExtVO)vStoccaggi.get(i);
              if(effVO.getIdComunicazione10R() == com10rTmp.getIdComunicazione10R())
              {
                effVO.setIdComunicazione10R(com10rTmp.getIdComunicazione10R());
                //Se valorizzato l'id_azienda non devo inserire cuaa e denomnazione
                if(Validator.isNotEmpty(effVO.getIdAzienda()))
                {
                  //Controllo che l'azienda non sia cessata
                  AnagAziendaVO anagAziendaVO = aDAO.findAziendaAttiva(effVO.getIdAzienda());
                  //Se anagAziendaVO è null significa che l'azienda è cessata
                  //per cui la tratto al pari di quelle che non sono censite in amagrafe.               
                  if(Validator.isNotEmpty(anagAziendaVO))
                  {                  
                    effVO.setCuaa(null);
                    effVO.setDenominazione(null);
                  }
                }
                com10rDAO.insertEffluenteStocExtAcq10R(effVO);
              }
            }
          }
          
          if(vAcqueExtra !=null)
          {
            for(int i=0;i<vAcqueExtra.size();i++)
            {
              AcquaExtraVO acqVO = (AcquaExtraVO)vAcqueExtra.get(i);
              if(acqVO.getIdComunicazione10R() == com10rTmp.getIdComunicazione10R())
              {
                acqVO.setIdComunicazione10R(com10rTmp.getIdComunicazione10R());
                com10rDAO.insertAcquaExtra10R(acqVO);
              }
            }
          }
        }
      }
      
      
      return com10rDAO.ricalcolaPlSql(idAzienda, idUtenteAggiornamento);
    }
    catch (DataAccessException e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
    catch (Exception e)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(e.getMessage());
    }
  }
  
  public boolean hasEffluenteProdotto(long idEffluente, long idUte) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.hasEffluenteProdotto(idEffluente, idUte); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Comunicazione10RVO[] getComunicazione10RByPianoRifererimento(long idAzienda, long idPianoRiferimento) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getComunicazione10RByPianoRifererimento(idAzienda, idPianoRiferimento); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public PlSqlCodeDescription controlloQuantitaEffluentePlSql(long idUte, long idEffluente) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.controlloQuantitaEffluentePlSql(idUte, idEffluente); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public PlSqlCodeDescription calcolaQuantitaAzotoPlSql(long idUte, long idComunicazione10r, 
      long idCausaleEffluente, long idEffluente, BigDecimal quantita) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.calcolaQuantitaAzotoPlSql(idUte, idComunicazione10r, 
          idCausaleEffluente, idEffluente, quantita); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public PlSqlCodeDescription ricalcolaPlSql(long idAzienda, 
      long idUtente) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.ricalcolaPlSql(idAzienda, idUtente); 
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<BigDecimal> getSommaEffluentiCessAcquPerStampa(long idComunicazione10R) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getSommaEffluentiCessAcquPerStampa(idComunicazione10R); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public BigDecimal getSommaEffluenti10RPerStampa(long idComunicazione10R, boolean palabile) 
   throws SolmrException
  {
    try
    {
      return com10rDAO.getSommaEffluenti10RPerStampa(idComunicazione10R, palabile); 
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public PlSqlCodeDescription calcolaM3EffluenteAcquisitoPlSql(long idAzienda, 
      long idAziendaCess, long idCausaleEffluente, long idEffluente) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.calcolaM3EffluenteAcquisitoPlSql(idAzienda, idAziendaCess, idCausaleEffluente, idEffluente);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public boolean controlloRefluoPascolo(long idUte) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.controlloRefluoPascolo(idUte);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteTrattamenti(long idUte)
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListTipoEffluenteTrattamenti(idUte);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteByLegameId(long idEffluente) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListTipoEffluenteByLegameId(idEffluente);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoEffluenteVO> getListTipoEffluenteAndValueByLegameId(
      long idComunicazione10R, long idEffluente) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListTipoEffluenteAndValueByLegameId(idComunicazione10R, idEffluente);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  public Vector<EffluenteVO> getListTrattamenti(long idComunicazione10R[]) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListTrattamenti(idComunicazione10R);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<EffluenteVO> getListEffluentiNoTratt(long idComunicazione10R[]) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListEffluentiNoTratt(idComunicazione10R);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<RefluoEffluenteVO> getRefluiComunocazione10r(long idUte, long idComunicazione10r, 
      Date dataInserimentoDichiarazione) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getRefluiComunocazione10r(idUte, idComunicazione10r, 
          dataInserimentoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<RefluoEffluenteVO> getListRefluiStampa(long idComunicazione10R[]) 
    throws SolmrException
  {
    try
    {
      return com10rDAO.getListRefluiStampa(idComunicazione10R);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public PlSqlCodeDescription calcolaVolumePioggeM3PlSql(long idUte)
      throws SolmrException
  {
    try
    {
      return com10rDAO.calcolaVolumePioggeM3PlSql(idUte);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public PlSqlCodeDescription calcolaAcqueMungituraPlSql(long idUte)
      throws SolmrException
  {
    try
    {
      return com10rDAO.calcolaAcqueMungituraPlSql(idUte);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public PlSqlCodeDescription calcolaCesAcquisizionePlSql(long idComunicazione10r) 
      throws SolmrException
  {
    try
    {
      return com10rDAO.calcolaCesAcquisizionePlSql(idComunicazione10r);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }

  
}
