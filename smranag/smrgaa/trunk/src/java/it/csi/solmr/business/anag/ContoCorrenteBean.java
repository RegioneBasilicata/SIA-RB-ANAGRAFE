package it.csi.solmr.business.anag;

import it.csi.solmr.dto.anag.BancaSportelloVO;
import it.csi.solmr.dto.anag.ContoCorrenteVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.DataControlException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.integration.anag.ContoCorrenteDAO;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;

import java.sql.Timestamp;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name="comp/env/solmr/anag/ContoCorrente",mappedName="comp/env/solmr/anag/ContoCorrente")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.REQUIRED)
public class ContoCorrenteBean implements ContoCorrenteLocal
{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  SessionContext sessionContext;
  private transient ContoCorrenteDAO contoCorrenteDAO = null;

 
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
      contoCorrenteDAO = new ContoCorrenteDAO(
          SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
    }
    catch (ResourceAccessException ex)
    {
      SolmrLogger.fatal(this, ex.getMessage());
      throw new EJBException(ex);
    }
  }

  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad
  // un'azienda
  // Il parametro soloAnno indica se nella dataSituazioneAl deve essere
  // considerato significativo solo l'anno (valore true) o tutta la data (valore
  // false)
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      java.util.Date dataSituazioneAl, Boolean soloAnno) throws Exception
  {
    try
    {
      return contoCorrenteDAO.getContiCorrenti(idAzienda, dataSituazioneAl,
          soloAnno.booleanValue());
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Restituisce l'elenco (ContoCorrenteVO) dei conti correnti legati ad
  // un'azienda
  public Vector<ContoCorrenteVO> getContiCorrenti(Long idAzienda,
      boolean estinto) throws Exception
  {
    try
    {
      if (estinto)
        return contoCorrenteDAO.getContiCorrentiStorico(idAzienda);
      else
        return contoCorrenteDAO.getContiCorrentiAttivi(idAzienda);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
  }

  // Metodo per effettuare una cancellazione logica da un conto corrente
  public void deleteContoCorrente(Long idConto, long idUtenteAggiornamento)
      throws Exception, SolmrException
  {
    try
    {
      contoCorrenteDAO.deleteContoCorrente(idConto, idUtenteAggiornamento);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
    catch (DataControlException d)
    {
      throw new SolmrException(d.getMessage());
    }
  }

  /**
   * Inserisce un nuovo conto corrente
   * 
   */
  public void insertContoCorrente(ContoCorrenteVO conto,
      long idUtenteAggiornamento) throws Exception, SolmrException
  {
    try
    {
      contoCorrenteDAO.insertContoCorrente(conto, idUtenteAggiornamento);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  /**
   * Carica le informazioni su un conto corrente
   * 
   * @param idContoCorrente
   * @return
   * @throws DataAccessException
   */
  public ContoCorrenteVO getContoCorrente(String idContoCorrente)
      throws Exception
  {
    try
    {
      return contoCorrenteDAO.getContoCorrente(new Long(idContoCorrente));
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
  }

  public BancaSportelloVO[] searchBanca(String abi, String denominazione)
      throws SolmrException, Exception
  {
    try
    {
      return contoCorrenteDAO.searchBanca(abi, denominazione);
    }
    catch (DataAccessException d)
    {
      throw new Exception(d.getMessage());
    }
    catch (DataControlException d)
    {
      throw new SolmrException(ErrorTypes.STR_MAX_RECORD, ErrorTypes.MAX_RECORD);
    }
  }

  public BancaSportelloVO[] searchSportello(String abi, String cab,
      String comune) throws Exception, SolmrException
  {
    try
    {
      return contoCorrenteDAO.searchSportello(abi, cab, comune);
    }
    catch (DataAccessException ex)
    {
      throw new Exception(ex.getMessage());
    }
    catch (DataControlException d)
    {
      throw new SolmrException(ErrorTypes.STR_MAX_RECORD, ErrorTypes.MAX_RECORD);
    }
  }

  // Metodo per effettuare l'estinzione dei conti correnti collegati ad
  // un'azienda agricola
  public void desistsAccountCorrent(Long idAzienda, Long idUtente)
      throws SolmrException, Exception
  {
    try
    {
      contoCorrenteDAO.desistsAccountCorrent(idAzienda, idUtente);
    }
    catch (DataAccessException dae)
    {
      throw new Exception(dae.getMessage());
    }
  }
  
  /**
   * Storicizza il conto corrente
   * nella modifica
   * 
   * 
   * @param conto
   * @param idUtente
   * @throws Exception
   * @throws SolmrException
   */
  public void storicizzaContoCorrente(ContoCorrenteVO conto, Long idUtente) 
    throws Exception, SolmrException
  {
    try
    {      
      ContoCorrenteVO contoOld = contoCorrenteDAO.getContoCorrente(conto.getIdContoCorrente());
      java.util.Date dataCorrente = new java.util.Date(new Timestamp(System
          .currentTimeMillis()).getTime());
      if((conto.getflagValidato() != null)
          && conto.getflagValidato().equalsIgnoreCase("N"))
      {
        //update
        contoOld.setDataFineValiditaContoCorrente(dataCorrente);
        contoCorrenteDAO.updateContoCorrente(contoOld);
        //insert
        conto.setDataInizioValiditaContoCorrente(dataCorrente);
        conto.setDataAggiornamento(dataCorrente);
        conto.setIdUtenteAggiornamento(idUtente);
        conto.setflagValidato("S");
        conto.setIdTipoCausaInvalidazione(null);
        conto.setNote(null);
        contoCorrenteDAO.insertContoCorrente(conto, idUtente);
      }
      else
      {
        if(!conto.equalsDatiContiCorrenti(contoOld))
        {            
          // Se la data di aggiornamento è uguale a SYSDATE
          if (DateUtils.formatDate(
              contoOld.getDataAggiornamento()).equalsIgnoreCase(
              DateUtils.formatDate(dataCorrente)))
          {
            //update
            contoOld.setCin(conto.getCin());
            contoOld.setIntestazione(conto.getIntestazione());
            contoOld.setDataAggiornamento(dataCorrente);
            contoOld.setDataEstinzione(conto.getDataEstinzione());
            contoOld.setIdUtenteAggiornamento(idUtente);
            contoOld.setIban(conto.getIban());
            contoOld.setCifraCtrl(conto.getCifraCtrl());            
            contoOld.setflagValidato("S");
            contoOld.setIdTipoCausaInvalidazione(null);
            contoOld.setNote(null);
            contoOld.setFlagContoGf(conto.getFlagContoGf());
            contoCorrenteDAO.updateContoCorrente(contoOld);
          }
          else
          {
            //update
            contoOld.setDataFineValiditaContoCorrente(dataCorrente);
            contoCorrenteDAO.updateContoCorrente(contoOld);
            //insert
            conto.setDataInizioValiditaContoCorrente(dataCorrente);
            conto.setDataAggiornamento(dataCorrente);
            conto.setIdUtenteAggiornamento(idUtente);
            conto.setflagValidato("S");
            conto.setIdTipoCausaInvalidazione(null);
            conto.setNote(null);
            conto.setMotivoRivalidazione(contoOld.getMotivoRivalidazione());
            contoCorrenteDAO.insertContoCorrente(conto, idUtente);
          }
        }
      }      
      
    }
    catch (DataAccessException dae)
    {
      sessionContext.setRollbackOnly();
      throw new Exception(dae.getMessage());
    }
  }
  
  

}
