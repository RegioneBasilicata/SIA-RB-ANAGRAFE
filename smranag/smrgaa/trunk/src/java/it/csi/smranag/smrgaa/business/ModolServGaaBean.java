package it.csi.smranag.smrgaa.business;

import it.csi.csi.porte.InfoPortaDelegata;
import it.csi.csi.porte.proxy.PDProxy;
import it.csi.csi.util.xml.PDConfigReader;
import it.csi.csi.wrapper.SystemException;
import it.csi.csi.wrapper.UnrecoverableException;
import it.csi.modol.modolpdfgeneratorsrv.dto.pdfstatic.PdfStaticInputRequest;
import it.csi.modol.modolpdfgeneratorsrv.interfacecsi.ModolPdfGeneratorSrvITF;
import it.csi.modol.modolsrv.dto.Applicazione;
import it.csi.modol.modolsrv.dto.Modello;
import it.csi.modol.modolsrv.dto.Modulo;
import it.csi.modol.modolsrv.dto.RendererModality;
import it.csi.modol.modolsrv.dto.RiferimentoAdobe;
import it.csi.modol.modolsrv.dto.XmlModel;
import it.csi.modol.modolsrv.interfacecsi.ModolSrvITF;
import it.csi.smranag.smrgaa.dto.modol.AttributiModuloVO;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.SolmrLogger;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;


@Stateless(name= ModolServGaaBean.jndiName,mappedName=  ModolServGaaBean.jndiName)
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED )
public class ModolServGaaBean implements ModolServGaaLocal
{  
  
  /**
   * 
   */
  private static final long serialVersionUID = -8912891300176924773L;
  public final static String jndiName="comp/env/solmr/gaa/ModolServ";


  SessionContext sessionContext;
	
	
	private transient InfoPortaDelegata informazioniPortaDelegata;
	private transient InfoPortaDelegata informazioniPortaDelegataPDFGen;

	@Resource
	public void setSessionContext(SessionContext sessionContext)
	{
		this.sessionContext = sessionContext;
		try
		{
			// caricamento porta delegata
			informazioniPortaDelegata = PDConfigReader.read(getClass().getResourceAsStream(SolmrConstants.FILE_PD_MODOLSERV));
			informazioniPortaDelegataPDFGen = PDConfigReader.read(getClass().getResourceAsStream(SolmrConstants.FILE_PD_MODOLPDFGENER));
		}
		catch(Exception e)
		{
			SolmrLogger.fatal(this, "ModolServGaaBean:setSessionContext: "+e.getMessage());
		}
	}

	/**
	 *
	 * @throws Exception
	 */
	private ModolSrvITF getModolServClient() throws Exception
	{	  
	  return (ModolSrvITF) PDProxy.newInstance(informazioniPortaDelegata);
	}
	
	private ModolPdfGeneratorSrvITF getModolPDFGenServClient() throws Exception
  {   
    return (ModolPdfGeneratorSrvITF) PDProxy.newInstance(informazioniPortaDelegataPDFGen);
  }
	
	private SolmrException checkServicesUnavailable(SystemException ex)
    throws SolmrException
  {
    SolmrLogger.fatal(this,
      "SystemException rilevata su chiamata a servizio di Modolsrv");
    SolmrLogger.dumpStackTrace(this, "Dump exception", ex);

    if (SolmrConstants.NAME_NOT_FOUND_EXCEPTION.equals(
          ex.getNestedExcClassName()) ||
          SolmrConstants.COMMUNICATION_EXCEPTION.equals(
          ex.getNestedExcClassName()))
    {
      return new SolmrException(AnagErrors.ERRORE_MODOLSERV_NON_DISPONIBILE,
          SolmrException.CODICE_ERRORE_MODOLSERV_NON_DISPONIBILE);
    }
    else
    {
      return new SolmrException(AnagErrors.ERRORE_ACCESSO_A_MODOLSERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_MODOLSERV);
    }
  } 
	

	
	public byte[] callModol(byte[] xmlInput, AttributiModuloVO attributiModuloVO)  
	  throws SolmrException
  {
    SolmrLogger.debug(this,
      "[ModolServGaaBean::callModol] BEGIN.");

    byte[] bXmlModol = null;
    try
    {
      /* imposto la modalità di rendering da utilizzare per la restituzione dei dati */
      RendererModality rm = new RendererModality();
      rm.setIdRendererModality(new Integer(3)); // PDF
      RendererModality[] rmArray = new RendererModality[] 
      { rm };
      rmArray[0].setSelezionataPerRendering(true);

      /* imposto il percorso di memorizzazione del template interno al server LiveCycle */
      RiferimentoAdobe rifAdobe = new RiferimentoAdobe();
      rifAdobe.setXdpURI(attributiModuloVO.getRifAdobe());

      /* definisco il Modello da utilizzare */
      Modello modello = new Modello();
      modello.setCodiceModello(attributiModuloVO.getCodiceModello());
      modello.setRendererModality(rmArray);
      modello.setRiferimentoAdobe(rifAdobe);

      /* definisco il Modulo da utilizzare */
      Modulo modulo = new Modulo();
      modulo.setCodiceModulo(attributiModuloVO.getCodiceModulo());
      modulo.setModello(modello);

      /* definisco l'Applicazione da utilizzare */
      Applicazione applicazione = new Applicazione();
      applicazione.setCodiceApplicazione(attributiModuloVO.getCodiceApplicazione());
      applicazione.setDescrizioneApplicazione(attributiModuloVO.getDescrizioneApplicazione());

      /* predispongo l'oggetto con i dati da associare al modulo e all'applicazione */
      XmlModel xml = new XmlModel();
      xml.setXmlContent(xmlInput);

      /* finalmente invoco il servizio tramite la PD già istanziata in precedenza */
      Modulo moduloMerged = getModolServClient().mergeModulo(applicazione, null, modulo, xml);

      /* recupero l'array di byte contenente il PDF e lo restituisco al chiamante */
      bXmlModol = moduloMerged.getDataContent();
    }
    catch (SystemException ex)
    {
      throw checkServicesUnavailable(ex);
    }
    catch (UnrecoverableException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_MODOLSERV_EXCEPTION, ex);
      throw new SolmrException(ex.getMessage());
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_MODOLSERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_MODOLSERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_MODOLSERV);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_MODOLSERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_MODOLSERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_MODOLSERV);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[ModolServGaaBean::callModol] END.");
    }
    
    return bXmlModol;
  }
	
	
	
	public byte[] trasformStaticPDF(byte[] xmlInput, AttributiModuloVO attributiModuloVO)  
    throws SolmrException
  {
    SolmrLogger.debug(this,
      "[ModolServGaaBean::trasformStaticPDF] BEGIN.");

    byte[] bXmlModol = null;
    try
    {

      /* definisco l'Applicazione da utilizzare */
      it.csi.modol.modolpdfgeneratorsrv.dto.Applicazione applicazione = new it.csi.modol.modolpdfgeneratorsrv.dto.Applicazione();
      applicazione.setCodiceApplicazione(attributiModuloVO.getCodiceApplicazione());
      applicazione.setDescrizioneApplicazione(attributiModuloVO.getDescrizioneApplicazione());

      /* predispongo l'oggetto con i dati da associare al modulo e all'applicazione */
      XmlModel xml = new XmlModel();
      xml.setXmlContent(xmlInput);

      

      /* recupero l'array di byte contenente il PDF e lo restituisco al chiamante */
      PdfStaticInputRequest pdfStatic = new PdfStaticInputRequest();
      pdfStatic.setPdfInput(xmlInput);      
      
      bXmlModol = getModolPDFGenServClient().toStaticPdf(applicazione, null, pdfStatic);
    }
    catch (SystemException ex)
    {
      throw checkServicesUnavailable(ex);
    }
    catch (UnrecoverableException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_MODOLSERV_EXCEPTION, ex);
      throw new SolmrException(ex.getMessage());
    }
    catch (EJBException ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_MODOLSERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_MODOLSERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_MODOLSERV);
    }
    catch (Exception ex)
    {
      SolmrLogger.dumpStackTrace(this, AnagErrors.ERRORE_MODOLSERV_EXCEPTION, ex);
      throw new SolmrException(AnagErrors.ERRORE_ACCESSO_A_MODOLSERV,
          SolmrException.CODICE_ERRORE_DI_ACCESSO_A_MODOLSERV);
    }
    finally
    {
      SolmrLogger.debug(this,
        "[ModolServGaaBean::trasformStaticPDF] END.");
    }
    
    return bXmlModol;
  }
	
	
	
	
	
	
	
	
	

}
