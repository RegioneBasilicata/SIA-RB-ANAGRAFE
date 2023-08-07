package it.csi.smranag.smrgaa.business;

import java.util.ResourceBundle;

import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellDocumentoDaAggiornareVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoDocumentoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoFolderVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoIdDocVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoVO;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.SmrcommsrvAgriWellServiceLocator;
import it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.SmrcommsrvAgriWell_PortType;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;


@Stateless(name="comp/env/solmr/anag/AgriWellGaa",mappedName="comp/env/solmr/anag/AgriWellGaa")
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(value=javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
public class AgriWellGaaBean implements AgriWellGaaLocal
{  
  

  /**
   * 
   */
  private static final long serialVersionUID = -8710547842181169076L;

  SessionContext sessionContext;
	
	
	private transient SmrcommsrvAgriWell_PortType agriwellBinding;

	
	
	@Resource
	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
		try
		{
			ResourceBundle res = ResourceBundle.getBundle("config");
			String endPoint = res.getString("agriwell_service_endpoint_url");
			SmrcommsrvAgriWellServiceLocator locator=new SmrcommsrvAgriWellServiceLocator();
			locator.setsmrcommsrvAgriWellEndpointAddress(endPoint);
			agriwellBinding=locator.getsmrcommsrvAgriWell();
		}
		catch(Exception e)
		{
			SolmrLogger.fatal(this, "AgriWellBean:setSessionContext: "+e.getMessage());
		}
		
	}

	

	
	public AgriWellEsitoVO agriwellServiceScriviDoquiAgri(
      AgriWellDocumentoVO agriWellDocumentoVO)  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[AgriWellGaaBean::agriwellServiceScriviDoquiAgri] BEGIN.");

    AgriWellEsitoVO agriWellEsitoVO = null;
    try
    {
    	agriWellEsitoVO = agriwellBinding.agriwellServiceScriviDoquiAgri(agriWellDocumentoVO);
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[AgriWellGaaBean::agriwellServiceScriviDoquiAgri] END.");
    }
    
    return agriWellEsitoVO;
  }
	
	
	public AgriWellEsitoVO agriwellServiceLeggiDoquiAgri(
      long idDocumentoIndex)  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[AgriWellGaaBean::agriwellServiceLeggiDoquiAgri] BEGIN.");

    AgriWellEsitoVO agriWellEsitoVO = null;
    try
    {
      agriWellEsitoVO = agriwellBinding.agriwellServiceLeggiDoquiAgri(idDocumentoIndex);
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[AgriWellGaaBean::agriwellServiceLeggiDoquiAgri] END.");
    }
    
    return agriWellEsitoVO;
  }
	
	
	public AgriWellEsitoVO agriwellServiceCancellaDoquiAgri(
      long idDocumentoIndex)  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[AgriWellGaaBean::agriwellServiceCancellaDoquiAgri] BEGIN.");

    AgriWellEsitoVO agriWellEsitoVO = null;
    try
    {
      agriWellEsitoVO = agriwellBinding.agriwellServiceCancellaDoquiAgri(idDocumentoIndex);
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[AgriWellGaaBean::agriwellServiceCancellaDoquiAgri] END.");
    }
    
    return agriWellEsitoVO;
  }
	
	
	public AgriWellEsitoFolderVO agriwellFindFolderByPadreProcedimentoRuolo(int idProcedimento,
      String codRuoloUtente, Long idFolderMadre, boolean noEmptyFolder, Long idAzienda)  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[AgriWellGaaBean::agriwellFindFolderByPadreProcedimentoRuolo] BEGIN.");

    AgriWellEsitoFolderVO agriWellEsitoFolderVO = null;
    try
    {
      agriWellEsitoFolderVO = agriwellBinding.agriwellFindFolderByPadreProcedimentoRuolo(idProcedimento, 
          codRuoloUtente, idFolderMadre, noEmptyFolder, idAzienda);
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[AgriWellGaaBean::agriwellFindFolderByPadreProcedimentoRuolo] END.");
    }
    
    return agriWellEsitoFolderVO;
  }
	
	public AgriWellEsitoIdDocVO agriwellFindListaDocumentiByIdFolder(long idFolder, int idProcedimento,
      String codRuoloUtente, Long idAzienda)  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[AgriWellGaaBean::agriwellFindListaDocumentiByIdFolder] BEGIN.");

    AgriWellEsitoIdDocVO agriWellEsitoIdDocVO = null;
    try
    {
      agriWellEsitoIdDocVO = agriwellBinding.agriwellFindListaDocumentiByIdFolder(idFolder, 
          idProcedimento, codRuoloUtente, idAzienda);
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[AgriWellGaaBean::agriwellFindListaDocumentiByIdFolder] END.");
    }
    
    return agriWellEsitoIdDocVO;
  }
	
	public AgriWellEsitoDocumentoVO agriwellFindDocumentoByIdRange(long[] idDoc)  
	    throws SolmrException
  {
    SolmrLogger.debug(this,
      "[AgriWellGaaBean::agriwellFindDocumentoByIdRange] BEGIN.");

    AgriWellEsitoDocumentoVO agriWellEsitoDocumentoVO = null;
    try
    {
      agriWellEsitoDocumentoVO = agriwellBinding.agriwellFindDocumentoByIdRange(idDoc);
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[AgriWellGaaBean::agriwellFindDocumentoByIdRange] END.");
    }
    
    return agriWellEsitoDocumentoVO;
  }
	
	
	public AgriWellEsitoVO agriwellServiceUpdateDoquiAgri(
	  AgriWellDocumentoDaAggiornareVO agriWellDocumentoDaAggiornareVO)  
      throws SolmrException
  {
    SolmrLogger.debug(this,
      "[AgriWellGaaBean::agriwellServiceUpdateDoquiAgri] BEGIN.");

    AgriWellEsitoVO agriWellEsitoVO = null;
    try
    {
      agriWellEsitoVO = agriwellBinding.agriwellServiceUpdateDoquiAgri(agriWellDocumentoDaAggiornareVO);
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_AGRIWELL_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_AGRIWELL,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_AGRIWELL);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[AgriWellGaaBean::agriwellServiceUpdateDoquiAgri] END.");
    }
    
    return agriWellEsitoVO;
  }

}
