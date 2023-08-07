package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO;
import it.csi.smranag.smrgaa.integration.SchedulazioneFascicoliDAO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.Validator;

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
@Stateless(name=ConsistenzaGaaBean.jndiName,mappedName=ConsistenzaGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ConsistenzaGaaBean implements ConsistenzaGaaLocal
{ 
  

  /**
   * 
   */
  private static final long serialVersionUID = 8034592016083139280L;
  public final static String jndiName="comp/env/solmr/gaa/ConsistenzaGaa";

  SessionContext                   sessionContext;

  private transient SchedulazioneFascicoliDAO sfDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      sfDAO = new SchedulazioneFascicoliDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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

  public InvioFascicoliVO getLastSchedulazione(long idDichiarazioneConsistenza)
    throws SolmrException
  {
    try
    {
      return sfDAO.getLastSchedulazione(idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public boolean trovaSchedulazioneAttiva(long idAzienda, long idDichiarazioneConsistenza)
      throws SolmrException
  {
    try
    {
      return sfDAO.trovaSchedulazioneAttiva(idAzienda, idDichiarazioneConsistenza);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  
  public void insertSchedulazione(InvioFascicoliVO invioFascicoliVO, long idUtente) 
      throws SolmrException
  {
    try
    {
      //Se valorizzato vuol dire che è già preente una schedulazione in stato
      //schedulazione
      if(Validator.isNotEmpty(invioFascicoliVO.getIdInvioFascicoli()))
      {
        sfDAO.deleteInvioFascicoli(invioFascicoliVO.getIdInvioFascicoli().longValue());
      }
      
      sfDAO.insertInvioFascicoli(invioFascicoliVO, idUtente);      
           
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  public void deleteSchedulazione(long idInvioFascicoli) 
      throws SolmrException
  {
    try
    {
      sfDAO.deleteInvioFascicoli(idInvioFascicoli);     
           
    }
    catch (DataAccessException ex)
    {
      sessionContext.setRollbackOnly();
      throw new SolmrException(ex.getMessage());
    }    
  }
  
  
  
}
