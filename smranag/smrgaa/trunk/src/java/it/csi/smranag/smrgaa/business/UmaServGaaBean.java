package it.csi.smranag.smrgaa.business;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.smruma.umaserv.dto.AssegnazioneVO;
import it.csi.smruma.umaserv.dto.DittaUmaVO;
import it.csi.smruma.umaserv.interfacecsi.IUmaservCSIInterface;
import it.csi.solmr.dto.uma.MacchinaVO;
import it.csi.solmr.dto.uma.UtilizzoVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.interfaceCSI.uma.UmaCSIInterface;
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


@Stateless(name= UmaServGaaBean.jndiName,mappedName=  UmaServGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED )
public class UmaServGaaBean implements UmaServGaaLocal
{  
  

  

  /**
   * 
   */
  private static final long serialVersionUID = -2923488791815838102L;
  public final static String jndiName="comp/env/solmr/gaa/UmaServ";


  SessionContext sessionContext;
	
	
	private transient InfoPortaDelegata informazioniPortaDelegata;
	private transient InfoPortaDelegata informazioniPortaDelegataOld;

	@Resource
	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
		try
		{
			// caricamento porta delegata
			informazioniPortaDelegata = PDConfigReader.read(getClass().getResourceAsStream(SolmrConstants.FILE_PD_UMASERV));
			informazioniPortaDelegataOld = PDConfigReader.read(getClass().getResourceAsStream(SolmrConstants.FILE_PD_UMA_SERVICE));
		}
		catch(Exception e)
		{
			SolmrLogger.fatal(this, "UmaServBean:setSessionContext: "+e.getMessage());
		}
	}

	/**
	 *
	 * @throws Exception
	 */
	private IUmaservCSIInterface getUmaServClient() throws Exception
	{	  
	  return (IUmaservCSIInterface) PDProxy.newInstance(informazioniPortaDelegata);
	}
	
	private UmaCSIInterface getUmaServClientOld() throws Exception
  {   
    return (UmaCSIInterface) PDProxy.newInstance(informazioniPortaDelegataOld);
  }
	
	private SolmrException checkServicesUnavailable(SystemException ex)
    throws SolmrException
  {
    SolmrLogger.fatal(this,
      "SystemException rilevata su chiamata a servizio di WsBridge");
    SolmrLogger.dumpStackTrace(this, "Dump exception", ex);

    if (SolmrConstants.NAME_NOT_FOUND_EXCEPTION.equals(
          ex.getNestedExcClassName()) ||
          SolmrConstants.COMMUNICATION_EXCEPTION.equals(
          ex.getNestedExcClassName()))
    {
      return new SolmrException(AnagErrors.ERRORE_UMASERV_NON_DISPONIBILE,
          SolmrException.CODICE_ERRORE_UMASERV_NON_DISPONIBILE);
    }
    else
    {
      return new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
  } 
	

	
	public DittaUmaVO[] umaservGetAssegnazioniByIdAzienda(
      long idAzienda, String[] arrCodiceStatoAnag)  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[UmaServGaaBean::umaservGetAssegnazioniByIdAzienda] BEGIN.");

    DittaUmaVO[] arrDittaUmaVO = null;
    try
    {
      arrDittaUmaVO = getUmaServClient().umaservGetAssegnazioniByIdAzienda(
          idAzienda, arrCodiceStatoAnag);
    }
    catch (SystemException ex)
    {
      throw checkServicesUnavailable(ex);
    }
    catch (UnrecoverableException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(ex.getMessage());
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[UmaServGaaBean::umaservGetAssegnazioniByIdAzienda] END.");
    }
    
    return arrDittaUmaVO;
  }
	
	
	public AssegnazioneVO[] umaservGetDettAssegnazioneByRangeIdDomAss(
      long[] arrIdDomandaAssegnazione)  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[UmaServGaaBean::umaservGetDettAssegnazioneByRangeIdDomAss] BEGIN.");

    AssegnazioneVO[] arrAssegnazioneVO = null;
    try
    {
      arrAssegnazioneVO = getUmaServClient().umaservGetDettAssegnazioneByRangeIdDomAss(arrIdDomandaAssegnazione);
    }
    catch (SystemException ex)
    {
      throw checkServicesUnavailable(ex);
    }
    catch (UnrecoverableException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(ex.getMessage());
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[UmaServGaaBean::umaservGetDettAssegnazioneByRangeIdDomAss] END.");
    }
    
    return arrAssegnazioneVO;
  }
	
	
	@SuppressWarnings("unchecked")
	public Vector<MacchinaVO> serviceGetElencoMacchineByIdAzienda(Long idAzienda,
	    Boolean storico, Long idGenereMacchina)  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[UmaServGaaBean::serviceGetElencoMacchineByIdAzienda] BEGIN.");

    Vector<MacchinaVO> vMacchine = null;
    try
    {
      MacchinaVO[] result = getUmaServClientOld().serviceGetElencoMacchineByIdAzienda(idAzienda, storico, idGenereMacchina);
      if(result != null && result.length >0){
    	  vMacchine = new Vector<MacchinaVO>();
          for (int i = 0; i < result.length; i++) {
        	  vMacchine.add(result[i]);
  		  }
      }	
    }
    catch (SystemException ex)
    {
      throw checkServicesUnavailable(ex);
    }
    catch (UnrecoverableException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(ex.getMessage());
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[UmaServGaaBean::serviceGetElencoMacchineByIdAzienda] END.");
    }
    
    return vMacchine;
  }
	
	@SuppressWarnings("unchecked")
	public Vector<Long> serviceGetElencoAziendeUtilizzatrici(Long idMacchina)
	  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[UmaServGaaBean::serviceGetElencoAziendeUtilizzatrici] BEGIN.");

    Vector<Long> vIdAzienda = null;
    try
    {
      long[] result = getUmaServClientOld().serviceGetElencoAziendeUtilizzatrici(idMacchina);
      if(result != null && result.length >0){
        vIdAzienda = new Vector<Long>();
        for (int i = 0; i < result.length; i++) {
        	vIdAzienda.add(result[i]);
		}
      }		  
    }
    catch (SystemException ex)
    {
      throw checkServicesUnavailable(ex);
    }
    catch (UnrecoverableException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(ex.getMessage());
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[UmaServGaaBean::serviceGetElencoAziendeUtilizzatrici] END.");
    }
    
    return vIdAzienda;
  }
	
	public UtilizzoVO serviceGetUtilizzoByIdMacchinaAndIdAzienda(
	    Long idMacchina, Long idAzienda) throws SolmrException
  {
    SolmrLogger.debug(this,
      "[UmaServGaaBean::serviceGetUtilizzoByIdMacchinaAndIdAzienda] BEGIN.");

    UtilizzoVO utilizzoVO = null;
    try
    {
      utilizzoVO = getUmaServClientOld()
          .serviceGetUtilizzoByIdMacchinaAndIdAzienda(idMacchina, idAzienda);
    }
    catch (SystemException ex)
    {
      throw checkServicesUnavailable(ex);
    }
    catch (UnrecoverableException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(ex.getMessage());
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_UMASERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_UMASERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_UMASERV);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[UmaServGaaBean::serviceGetUtilizzoByIdMacchinaAndIdAzienda] END.");
    }
    
    return utilizzoVO;
  }
	
	
	
	
	
	

}
