package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaDettaglioVO;
import it.csi.smranag.smrgaa.dto.polizze.PolizzaVO;
import it.csi.smranag.smrgaa.integration.PolizzaGaaDAO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.util.TreeMap;
import java.util.Vector;

import javax.annotation.Resource;
import javax.ejb.EJBException;
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
@Stateless(name=PolizzaGaaBean.jndiName,mappedName=PolizzaGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class PolizzaGaaBean implements PolizzaGaaLocal
{

  /**
   * 
   */
  private static final long serialVersionUID = -8079349939011947124L;
  public final static String jndiName="comp/env/solmr/gaa/PolizzaGaa";
  
  
  

  SessionContext                   sessionContext;

  private transient PolizzaGaaDAO pDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      pDAO = new PolizzaGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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

  public Vector<Integer> getElencoAnnoCampagnaByIdAzienda(long idAzienda) 
    throws SolmrException
  {
    try
    {
      return pDAO.getElencoAnnoCampagnaByIdAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<BaseCodeDescription> getElencoInterventoByIdAzienda(long idAzienda)
    throws SolmrException
  {
    try
    {
      return pDAO.getElencoInterventoByIdAzienda(idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<PolizzaVO> getElencoPolizze(long idAzienda, Integer annoCampagna, Long idIntervento)
    throws SolmrException
  {
    try
    {
      return pDAO.getElencoPolizze(idAzienda, annoCampagna, idIntervento);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public PolizzaVO getDettaglioPolizza(long idPolizzaAssicurativa)
    throws SolmrException
  {
    try
    {
      return pDAO.getDettaglioPolizza(idPolizzaAssicurativa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaColtura(long idPolizzaAssicurativa)
    throws SolmrException
  {
    try
    {
      return pDAO.getDettaglioPolizzaColtura(idPolizzaAssicurativa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaStruttura(long idPolizzaAssicurativa)
    throws SolmrException
  {
    try
    {
      return pDAO.getDettaglioPolizzaStruttura(idPolizzaAssicurativa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public TreeMap<Long,Vector<PolizzaDettaglioVO>> getDettaglioPolizzaZootecnia(long idPolizzaAssicurativa)
   throws SolmrException
  {
    try
    {
      return pDAO.getDettaglioPolizzaZootecnia(idPolizzaAssicurativa);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
}
