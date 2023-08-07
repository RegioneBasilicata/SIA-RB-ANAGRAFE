package it.csi.smranag.smrgaa.business;


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

import it.csi.smranag.smrgaa.dto.BaseCodeDescription;
import it.csi.smranag.smrgaa.dto.log.Parametro;
import it.csi.smranag.smrgaa.integration.DecodificheDAO;
import it.csi.smranag.smrgaa.util.LoggerUtils;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

/**
 * Classe Bean EJB con l'implementazione dei metodi di accesso alle informazioni
 * dei documenti
 * 
 * @author TOBECONFIG
 */

@Stateless(name=DecodificheBean.jndiName,mappedName=DecodificheBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class DecodificheBean implements DecodificheLocal
{

  /**
   * 
   */
  private static final long serialVersionUID = 497074867104320548L;
  public static final String jndiName="comp/env/solmr/gaa/Decodifiche";
  
  
  /** The session context */
  SessionContext sessionContext;
  private DecodificheDAO decodificheDAO = null;

  public DecodificheBean()
  {
  }

  

  /**
   * Setta il sessionContext dell'EJB e crea i DAO
   */
  @Resource
  public void setSessionContext(SessionContext sessionContext)
      throws EJBException
  {
    SolmrLogger.debug(this, "[DecodificheBean::setSessionContext] BEGIN.");
    this.sessionContext = sessionContext;
    initializeDAO();
    SolmrLogger.debug(this, "[DecodificheBean::setSessionContext] END.");
  }

  public void initializeDAO() throws EJBException
  {
    SolmrLogger.debug(this, "[DecodificheBean::initializeDAO] BEGIN.");
    try
    {
      decodificheDAO = new DecodificheDAO();
    }
    catch (Exception e)
    {
      SolmrLogger
          .fatal(
              this,
              "[DecodificheBean::initializeDAO] Eccezione nella creazione del DAO DecodificheDAO. Eccezione: "
                  + LoggerUtils.getStackTrace(e));
    }
    SolmrLogger.debug(this, "[DecodificheBean::initializeDAO] END.");
  }

  public BaseCodeDescription[] baseDecodeUtilizzoByIdRange(long ids[])
      throws SolmrException
  {
    try
    {
      SolmrLogger.debug(this,
          "[DecodificheBean::baseDecodeUtilizzoByIdRange] BEGIN.");
      return decodificheDAO.baseDecode("UTILIZZO", ids, "CODICE", Boolean.TRUE);
    }
    catch (Exception e)
    {
      // Log dell'errore
      Parametro parametri[] = new Parametro[]
      { new Parametro("ids", ids) };
      LoggerUtils.logEJBError(this,
          "[DecodificheBean::baseDecodeUtilizzoByIdRange]", e, null, parametri);
      throw new SolmrException(e.getMessage());
    }
    finally
    {
      SolmrLogger.debug(this,
          "[DecodificheBean::baseDecodeUtilizzoByIdRange] END.");
    }
  }
}
