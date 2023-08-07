package it.csi.smranag.smrgaa.business;

import it.csi.smranag.smrgaa.dto.manuali.ManualeVO;
import it.csi.smranag.smrgaa.integration.ManualiGaaDAO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import java.util.Vector;

import javax.annotation.Resource;
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
@Stateless(name=ManualeGaaBean.jndiName,mappedName=ManualeGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ManualeGaaBean implements ManualeGaaLocal
{
  /**
   * 
   */
  private static final long serialVersionUID = -5460080738314922450L;
  public final static String jndiName="comp/env/solmr/gaa/ManualeGaa";

  SessionContext                   sessionContext;

  private transient ManualiGaaDAO mDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      mDAO = new ManualiGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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

  public Vector<ManualeVO> getElencoManualiFromRuoli(String descRuolo) 
    throws SolmrException
  {
    try
    {
      return mDAO.getElencoManualiFromRuoli(descRuolo);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public ManualeVO getManuale(long idManuale) throws SolmrException
  {
    try
    {
      return mDAO.getManuale(idManuale);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
 
  
}
