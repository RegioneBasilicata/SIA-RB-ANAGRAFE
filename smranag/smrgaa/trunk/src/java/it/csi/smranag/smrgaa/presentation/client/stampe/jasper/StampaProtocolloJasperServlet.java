package it.csi.smranag.smrgaa.presentation.client.stampe.jasper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smranag.smrgaa.dto.stampe.protocollo.Protocollo;
import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento.DocAltroParticelleJasper;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento.DocDichiarazioniJasper;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento.DocFirmaJasper;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento.DocSottoscrittoJasper;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento.DocTrattamentoDatiJasper;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.documento.HeaderStampaDocJasper;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo.DocumentoAnagraficoDett;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo.DocumentoContoCorrente;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo.DocumentoCorrettivaTerreni;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo.DocumentoIdentificazione;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo.DocumentoProprietari;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo.DocumentoTerreni;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo.DocumentoZootecnicoDett;
import it.csi.smranag.smrgaa.presentation.client.stampe.jasper.protocollo.HeaderDocumentoProtocollo;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.DocumentoVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.Validator;
import it.csi.solmr.util.services.AgriLogger;
import net.sf.jasperreports.compilers.JRBshCompiler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class StampaProtocolloJasperServlet extends ModularJasperProtocollo
{
  
  /**
   * 
   */
  private static final long serialVersionUID = 188029137264310431L;
  private ResourceBundle res = ResourceBundle.getBundle("config");
  private String TEMPLATE_RESOURCE = res.getString("templatePdf");
  
  public StampaProtocolloJasperServlet()
  {
    this.nomeFilePdf = "StampaProtocollo.pdf";
  }
  

  private HashMap<String, Object> preload(HttpServletRequest request,
       AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    HashMap<String, Object> hmParametri = new HashMap<String, Object>();
    
    // Dati anagrafici dell'azienda
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");
    
    String idTipoReport = request.getParameter("idTipoReport");
    hmParametri.put("idTipoReport", idTipoReport);
    
    String cuaa = request.getParameter("cuaa");
    hmParametri.put("cuaa", cuaa);
    
    hmParametri.put("anagAziendaVO", anagAziendaVO);
    
    PersonaFisicaVO personaFisicaVO = anagFacadeClient
        .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(anagAziendaVO
        .getIdAnagAzienda().longValue(), null);    
    hmParametri.put("personaFisicaVO", personaFisicaVO);
    
    return hmParametri;
  }
 
  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new DocumentoAnagraficoDett());
    addSubReport(new DocumentoContoCorrente());
    addSubReport(new DocumentoCorrettivaTerreni()); 
    addSubReport(new DocumentoIdentificazione());
    addSubReport(new DocumentoProprietari()); 
    addSubReport(new DocumentoTerreni());
    addSubReport(new DocumentoZootecnicoDett());
    addSubReport(new HeaderDocumentoProtocollo());
    addSubReport(new HeaderStampaDocJasper());
    addSubReport(new DocSottoscrittoJasper()); 
    addSubReport(new DocAltroParticelleJasper());
    addSubReport(new DocDichiarazioniJasper());
    addSubReport(new DocFirmaJasper());
    addSubReport(new DocTrattamentoDatiJasper());
  }

@Override
protected byte[] popolaPdf(HttpServletRequest request, AnagFacadeClient anagFacadeclient,
		GaaFacadeClient gaaFacadeClient, RuoloUtenza ruoloUtenza) throws Exception {
	
	HashMap<String, Object> hmParametri = preload(request, anagFacadeclient, gaaFacadeClient, ruoloUtenza);
	Protocollo protocollo = new Protocollo();
	
	long[] idDocumento = (long[])request.getSession().getAttribute("idDocumentoStampaProtocollo");
    
    for(int i=0;i<idDocumento.length;i++)
    {
      Long idTipoReport = null; 
      //Arrivo dalla scelta stampa
      if(Validator.isNotEmpty(hmParametri.get("idTipoReport")))
      {
        idTipoReport = new Long((String)hmParametri.get("idTipoReport"));
      }
      //Stampa direta ho solo un tipo di stampa (scelta di default)
      else
      {
        Vector<TipoReportVO> vTipoStampaReport = gaaFacadeClient.getListTipiDocumentoStampaProtocollo(idDocumento);
        idTipoReport = new Long(vTipoStampaReport.get(0).getIdTipoReport());
      }
      
      String tipoDocumento = gaaFacadeClient.getTipoDocumentoStampaProtocollo(idTipoReport);
      //Metto il flag a false poiche Sergio ha dettoche bisogna prendere sempre tutto
      //quindi utilizzare la query col flag a false
      DocumentoVO documentoVO = anagFacadeclient.getDettaglioDocumento(idDocumento[i], false);      
      hmParametri.put("idDocumento", new Long(idDocumento[i]));
      hmParametri.put("documentoVO", documentoVO);
      hmParametri.put("numDocumento", new Integer(i));
       processSubReports(protocollo, tipoDocumento,
    		   anagFacadeclient, gaaFacadeClient, request, hmParametri);
      
       
       
     //call jasper 
   	String template =	TEMPLATE_RESOURCE+"/protocollo/TemplateProtocollo.jrxml";
       AgriLogger.debug(this, "-- template ="+template);
   	JasperDesign mainFascicoloJasper = JRXmlLoader.load(template);
   	
   	JRBshCompiler compiler = new JRBshCompiler();
   	JasperReport protocolloReport = compiler.compileReport(mainFascicoloJasper);

   	ArrayList<Protocollo> list = new ArrayList<Protocollo>();
   	list.add(protocollo);
   	HashMap<String, Object> parameters = new HashMap<String, Object>();
   	parameters.put("protocolloDS", list);
   	
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocumentoAnagraficoDett.jrxml", "DocumentoAnagraficoDett", compiler);
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocumentoIdentificazione.jrxml", "DocumentoIdentificazione", compiler);
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocumentoProprietari.jrxml", "DocumentoProprietari", compiler);
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocumentoTerreni.jrxml", "DocumentoTerreni", compiler);
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocumentoZootecnicoDett.jrxml", "DocumentoZootecnicoDett", compiler);

   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DichiarazioniSubReport.jrxml", "DichiarazioniSubReport", compiler);
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocSottoscrittoJasper.jrxml", "DocSottoscrittoJasper", compiler);
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocAltroParticelleJasper.jrxml", "DocAltroParticelleJasper", compiler);
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocFirmaJasper.jrxml", "DocFirmaJasper", compiler);
   	compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/DocTrattamentoDatiJasper.jrxml", "DocTrattamentoDatiJasper", compiler);

   	//compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/HeaderDocumentoProtocollo.jrxml", "HeaderDocumentoProtocollo", compiler);
   	//compileSubreport(parameters, TEMPLATE_RESOURCE+"/protocollo/HeaderStampaDocJasper.jrxml", "HeaderStampaDocJasper", compiler);
   	
   	byte[] xmlComplete = JasperRunManager.runReportToPdf(protocolloReport,parameters, new JRBeanCollectionDataSource(list, false));
    
    return xmlComplete;   
       
    }   
	
	return null;
}



private void compileSubreport(HashMap<String, Object> parameters, String urlFile, String name,JRBshCompiler compiler) throws FileNotFoundException, JRException{
	FileInputStream quadroFstream = new FileInputStream(new File(urlFile));
	JasperDesign poDesignQuadroF = JRXmlLoader.load(quadroFstream);
	JasperReport quadroBSubReport = compiler.compileReport(poDesignQuadroF);
	parameters.put(name, quadroBSubReport);
  }

@Override
protected void scriviPdf(HttpServletRequest request, AnagFacadeClient anagFacadeclient, GaaFacadeClient gaaFacadeClient,
		byte[] arrayPdf, RuoloUtenza ruoloUtenza) throws Exception {
	// TODO Auto-generated method stub
	
}

@Override
protected void inviaMailPdf(HttpServletRequest request, AnagFacadeClient anagFacadeclient,
		GaaFacadeClient gaaFacadeClient, byte[] arrayPdf, RuoloUtenza ruoloUtenza) throws Exception {
	// TODO Auto-generated method stub
	
}
}