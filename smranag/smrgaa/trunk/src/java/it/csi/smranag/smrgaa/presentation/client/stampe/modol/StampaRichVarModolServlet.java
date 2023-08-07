package it.csi.smranag.smrgaa.presentation.client.stampe.modol;

/**
 * <p>Title: Aziende trasformatrici</p>
 * <p>Description: Servizi on-line per le aziende vinicole di trasformazione</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.modol.AttributiModuloVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.Titolare;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.AziendaRV;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.ElAssRichVar;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.Firma2RV;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.FirmaRV;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.HeaderRichVar;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.LettAccRichVar;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.MaccRichVar;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.SoggRichVar;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.richiestaVariazione.TrattamentoDatiRV;
import it.csi.smranag.smrgaa.util.JaxbUtils;
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.services.AgriLogger;
import net.sf.jasperreports.compilers.JRBshCompiler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

public class StampaRichVarModolServlet extends ModularFascicoloModol
{
  
  
 
	private ResourceBundle res = ResourceBundle.getBundle("config");
	 private String TEMPLATE_RESOURCE = res.getString("templatePdf");
  
  /**
   * 
   */
  private static final long serialVersionUID = 4677225262729632627L;
  final String errMsg = "Problemi nella sezione crea una stampa richeista variazione."+
      "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  
  //public static final String CODICE_STAMPA    = "FASCICOLO_NEW";
  
  public StampaRichVarModolServlet()
  {
    this.nomeFilePdf = "stampaRichiestaVariazione.pdf";
  }

  

  protected byte[] popolaPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, 
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    
    
    HashMap<String, Object> hmParametri = preload(request,
        anagFacadeClient, gaaFacadeClient, ruoloUtenza);
    
    Fascicolo fascicoDigitale = new Fascicolo();
    
    String codiceStampaRicavato = "RICHIESTA_VARIAZIONE";
    
    processSubReports(fascicoDigitale, codiceStampaRicavato,
      anagFacadeClient, gaaFacadeClient, request, hmParametri);
    
    TipoReportVO tipoReportVO = (TipoReportVO)hmParametri.get("tipoReportVO");
    String nomeFileXDP = tipoReportVO.getNomeFileXDP();
    
    String param = request.getParameter("param");
    
    AttributiModuloVO attributiModuloVO = null;
    if(Validator.isNotEmpty(param)
        && param.equalsIgnoreCase("firma"))
    {
      attributiModuloVO = new AttributiModuloVO(SolmrConstants.CODICE_APPLICAZIONE_MODOL,
        SolmrConstants.DESCRIZIONE_APPLICAZIONE_MODOL, nomeFileXDP,
        nomeFileXDP, SolmrConstants.RIF_ADOBE_MODOL+""+nomeFileXDP+".xdp");
    }
    else
    {
      nomeFileXDP = nomeFileXDP+"_bozza";
      attributiModuloVO = new AttributiModuloVO(SolmrConstants.CODICE_APPLICAZIONE_MODOL,
          SolmrConstants.DESCRIZIONE_APPLICAZIONE_MODOL, nomeFileXDP,
          nomeFileXDP, SolmrConstants.RIF_ADOBE_MODOL+""+nomeFileXDP+".xdp");      
    }
    
    //byte[] xmlCompleteFirst = JaxbUtils.marshal(fascicoDigitale, "it.csi.smranag.smrgaa.dto.jaxb.fascicolo", null, null, SolmrConstants.ENCODING_MODOL);
    //byte[] xmlComplete = gaaFacadeClient.callModol(xmlCompleteFirst, attributiModuloVO);   
    
  //call jasper 
  	String template =	TEMPLATE_RESOURCE+"/fascicolo/Fascicolo.jrxml";
      AgriLogger.debug(this, "-- template ="+template);
  	JasperDesign mainFascicoloJasper = JRXmlLoader.load(template);
  	
  	JRBshCompiler compiler = new JRBshCompiler();
  	JasperReport fascicoloReport = compiler.compileReport(mainFascicoloJasper);

  	ArrayList<Fascicolo> list = new ArrayList<Fascicolo>();
  	list.add(fascicoDigitale);
  	HashMap<String, Object> parameters = new HashMap<String, Object>();
  	parameters.put("fascicoloDS", list);
	parameters.put("check_cheked", TEMPLATE_RESOURCE+"/fascicolo/check_cheked.png");
	parameters.put("check_uncheked", TEMPLATE_RESOURCE+"/fascicolo/check_uncheked.png");
	
	//Header
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/header/HeaderSubreport.jrxml", "HeaderSubreport", compiler);
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/header/HeaderLandscapeSubreport.jrxml", "HeaderLandscapeSubreport", compiler);
		

	//Quadro A subreport
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroA/QuadroAAssociazioniSubreport.jrxml", "QuadroAAssociazioniSubreport", compiler);
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroA/QuadroAContiSubreport.jrxml", "QuadroAContiSubreport", compiler);
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroA/QuadroASoggettiSubreport.jrxml", "QuadroASoggettiSubreport", compiler);
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroA/QuadroASubreport.jrxml", "QuadroASubreport", compiler);
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroA/QuadroATitolareSubreport.jrxml", "QuadroATitolareSubreport", compiler);
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroA/AssociazioneSubreport.jrxml", "AssociazioneSubreport", compiler);
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroA/ContiSubreport.jrxml", "ContiSubreport", compiler);
		compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroA/SoggettiSubreport.jrxml", "SoggettiSubreport", compiler);
	
	//Quadro B subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroB/QuadroBSubreport.jrxml", "quadroBSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroB/UteSubreport.jrxml", "uteSubReport", compiler);
		
	//Quadro F subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroF/QuadroF.jrxml", "quadroFSubReport", compiler);
	
	//Quadro G subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroG/QuadroGSubreport.jrxml", "quadroGSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroG/CategorieSubreport.jrxml", "categorieSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroG/AllevamentoSubreport.jrxml", "allevamentoSubReport", compiler);
	
	//Quadro I subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroI/QuadroISubreport.jrxml", "quadroISubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroI/RiepilogoDettTerreniSubreport.jrxml", "RiepilogoDettTerreniSubreport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroI/RiepilogoTerreniSubreport.jrxml", "riepilogoTerreniSubreport", compiler);

	//Quadro I2 subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroI2/QuadroI2Subreport.jrxml", "quadroI2SubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroI2/RiepiloghiUNitaVitateSubreport.jrxml", "RiepiloghiUnitaVitateSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroI2/RiepilogoSubreport.jrxml", "RiepilogoI2SubReport", compiler);

	//Quadro L subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroL/ElencoParticelleSubreport.jrxml", "ElencoParticelleSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroL/FabbricatoSubreport.jrxml", "FabbricatoSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroL/quadroFabbricatiSubreport.jrxml", "quadroLSubReport", compiler);

	//Quadro M subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroM/MacchineSubreport.jrxml", "MacchineSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroM/QuadroMSottoreport.jrxml", "QuadroMSubReport", compiler);

	//Quadro O subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroO/DocumentiSubreport.jrxml", "DocumentiSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroO/QuadroOSubreport.jrxml", "QuadroOSubReport", compiler);

	
	//Quadro Dichiarazioni subreport
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/QuadroDichSubReport.jrxml", "QuadroDichSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/AllegatiCondSubReport.jrxml", "AllegatiCondSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/AllegatiCondSubReport.jrxml", "AllegatiCondCheckSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/AllegatiNormaliSubReport.jrxml", "AllegatiNormaliSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/AllegatiNormaliCheckSubReport.jrxml", "AllegatiNormaliCheckSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/PrimoBloccoSubReport.jrxml", "PrimoBloccoSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/DichCheckSubReport.jrxml", "DichCheckSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/BloccoFirma2SubReport.jrxml", "BloccoFirma2SubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/DichFinaliSubReport.jrxml", "DichFinaliSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/DichSecondoSubReport.jrxml", "DichSecondoSubReport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/quadroDichiarazioni/DichCheck2SubReport.jrxml", "DichCheck2SubReport", compiler);
	
	//Blocco firma
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/bloccoFirma/BloccoFirmaSubreport.jrxml", "BloccoFirmaSubreport", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/bloccoFirma/DichFinali.jrxml", "DichFinali", compiler);
	compileSubreport(parameters, TEMPLATE_RESOURCE+"/fascicolo/bloccoFirma/FirmaSubreport.jrxml", "FirmaSubreport", compiler);
	
    byte[] xmlComplete = JasperRunManager.runReportToPdf(fascicoloReport,parameters, new JRBeanCollectionDataSource(list, false));
    
    return xmlComplete;     
  }
  
  private void compileSubreport(HashMap<String, Object> parameters, String urlFile, String name,JRBshCompiler compiler) throws FileNotFoundException, JRException{
		FileInputStream quadroFstream = new FileInputStream(new File(urlFile));
		JasperDesign poDesignQuadroF = JRXmlLoader.load(quadroFstream);
		JasperReport quadroBSubReport = compiler.compileReport(poDesignQuadroF);
		parameters.put(name, quadroBSubReport);
	  }
  
  protected void scriviPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeclient,
      GaaFacadeClient gaaFacadeClient,
      byte[] arrayPdf, 
      RuoloUtenza ruoloUtenza) throws Exception
  { 
    AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
    gaaFacadeClient.insertFileStampa(aziendaNuovaVO.getIdRichiestaAzienda().longValue(), arrayPdf);
  }
  
  protected void inviaMailPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeClient,
      GaaFacadeClient gaaFacadeClient,
      byte[] arrayPdf, 
      RuoloUtenza ruoloUtenza) throws Exception
  {}
      


  private HashMap<String, Object> preload(HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    HashMap<String, Object> hmParametri = new HashMap<String, Object>();
    
    
    String codiceStampaRicavato = "RICHIESTA_VARIAZIONE"; 
    TipoReportVO tipoReportVO = gaaFacadeClient.getTipoReportByCodice(codiceStampaRicavato);
    hmParametri.put("tipoReportVO", tipoReportVO);
    if(Validator.isNotEmpty(tipoReportVO.getIdTipoAllegato()))
    {
      TipoAllegatoVO tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(tipoReportVO.getIdTipoAllegato().intValue());
      hmParametri.put("tipoAllegatoVO", tipoAllegatoVO);
    }
    
    String idTipoRichiesta = request.getParameter("idTipoRichiesta");    
    String idAzienda = request.getParameter("idAzienda");
    
    AziendaNuovaVO aziendaNuovaVO = gaaFacadeClient.getRichAzByIdAzienda(new Long(idAzienda).longValue(), 
        new Long(idTipoRichiesta).longValue());
    hmParametri.put("aziendaNuovaVO", aziendaNuovaVO);
    
    hmParametri.put("dataConferma", new Date());   
    
    AnagAziendaVO anagAziendaVO = anagFacadeClient.findAziendaAttiva(new Long(idAzienda));
    hmParametri.put("anagAziendaVO", anagAziendaVO);
    
    //Dati Persona
    PersonaFisicaVO personaFisicaVO = anagFacadeClient
      .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(anagAziendaVO.getIdAnagAzienda()
      .longValue(), new Date());    
    hmParametri.put("personaFisicaVO", personaFisicaVO);
    
    
    return hmParametri;
  }
  
  
  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new LettAccRichVar());
    addSubReport(new Firma2RV());
    addSubReport(new HeaderRichVar());    
    addSubReport(new AziendaRV());   
    addSubReport(new Titolare());
    addSubReport(new ElAssRichVar());
    addSubReport(new SoggRichVar());
    addSubReport(new MaccRichVar());
    addSubReport(new FirmaRV());
    addSubReport(new TrattamentoDatiRV());
    
  }


  
}