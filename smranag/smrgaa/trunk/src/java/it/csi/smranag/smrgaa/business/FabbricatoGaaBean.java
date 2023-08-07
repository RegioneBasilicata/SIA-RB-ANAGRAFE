package it.csi.smranag.smrgaa.business;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import it.csi.smranag.smrgaa.dto.fabbricati.FabbricatoBioVO;
import it.csi.smranag.smrgaa.integration.FabbricatoGaaDAO;
import it.csi.solmr.dto.anag.fabbricati.TipoFormaFabbricatoVO;
import it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.DataAccessException;
import it.csi.solmr.exception.ResourceAccessException;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.exception.services.ErrorTypes;
import it.csi.solmr.util.SolmrLogger;

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

@Stateless(name=FabbricatoGaaBean.jndiName,mappedName=FabbricatoGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class FabbricatoGaaBean implements FabbricatoGaaLocal
{

  /**
   * 
   */
  private static final long serialVersionUID = -5384478612014169725L;

  SessionContext                   sessionContext;
  public final static String jndiName="comp/env/solmr/gaa/FabbricatoGaa";

  private transient FabbricatoGaaDAO fDAO;

  private void initializeDAO() throws EJBException
  {
    try
    {
      fDAO = new FabbricatoGaaDAO(SolmrConstants.JNDI_ANAG_RESOURCE_REFERENCE);
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

  public TipoTipologiaFabbricatoVO getInfoTipologiaFabbricato(
      Long idTipologiaFabbricato) throws SolmrException
  {
    if (idTipologiaFabbricato == null)
        
    {
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    }
    
    try
    {
      return fDAO.getInfoTipologiaFabbricato(idTipologiaFabbricato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public TipoFormaFabbricatoVO getTipoFormaFabbricato(
      Long idFormaFabbricato) throws SolmrException
  {
    if (idFormaFabbricato == null)
        
    {
      throw new SolmrException(ErrorTypes.STR_INVALID_PARAMETER,
          ErrorTypes.INVALID_PARAMETER);
    }
    
    try
    {
      return fDAO.getTipoFormaFabbricato(idFormaFabbricato);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  public Vector<TipoTipologiaFabbricatoVO> getListTipoFabbricatoStoccaggio() 
    throws SolmrException
  {
    try
    {
      return fDAO.getListTipoFabbricatoStoccaggio();
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public FabbricatoBioVO getFabbricatoBio(long idFabbricato, long idAzienda, Date dataInserimentoDichiarazione)
    throws SolmrException
  {
    try
    {
      return fDAO.getFabbricatoBio(idFabbricato, idAzienda, dataInserimentoDichiarazione);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
  public HashMap<Long,String> esisteFabbricatoAttivoByConduzioneAndAzienda(
      Vector<Long> elencoConduzioni, Long idAzienda)
  throws SolmrException
  {
    try
    {
      return fDAO.esisteFabbricatoAttivoByConduzioneAndAzienda(elencoConduzioni, idAzienda);
    }
    catch (DataAccessException ex)
    {
      throw new SolmrException(ex.getMessage());
    }
  }
  
  
}
