package it.csi.smranag.smrgaa.presentation.client.stampe.modol;

import it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.AllegatiCondizionalitaSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.AllegatiSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.AltriSoggetti;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.AssociazioniSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.Azienda;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.CodaDichiarazioniSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.ConsistenzaZootecnica;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.ContiCorrentiSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.DichCheck2;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.DichiarazioniSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.DocumentiElenco;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.FabbricatiSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.Firma2SR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.FirmaSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.HeadAllegatiSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.HeadCondizionalitaSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.HeaderAllegatiSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.HeaderChecklist;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.HeaderFascicolo;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.HeaderLandscapeSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.IntroDichiarazioniSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.Manodopera;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.MotoriAgricoli;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.TerreniElenco;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.TerreniRiepilogoComune;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.TerreniRiepilogoCond;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.TerreniRiepilogoMacroUso;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.TerreniRiepilogoTipoArea;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.Titolare;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.TrattamentoDatiSR;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.UVElenco;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.UVRiepilogoVitignoIdoneita;
import it.csi.smranag.smrgaa.presentation.client.stampe.modol.fascicolo.Ute;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.services.AgriLogger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.sf.jasperreports.compilers.JRBshCompiler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
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
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
import it.csi.smrcomms.rpparser.util.Validator;

public class StampaFascicoloModolServlet extends ModularFascicoloModol
{
  
  
  /**
   * 
   */
  private static final long serialVersionUID = 2546113835968719936L;
  
  
  final String errMsg = "Problemi nella sezione crea una validazione."+
      "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  
  private ResourceBundle res = ResourceBundle.getBundle("config");
  private String TEMPLATE_RESOURCE = res.getString("templatePdf");
  
  //public static final String CODICE_STAMPA    = "FASCICOLO_NEW";
  
  public StampaFascicoloModolServlet()
  {
    this.nomeFilePdf = "stampaFascicolo.pdf";    
  }
  

  protected byte[] popolaPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, 
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    
    
    HashMap<String, Object> hmParametri = preload(request,
        anagFacadeClient, gaaFacadeClient, ruoloUtenza);
    
    Fascicolo fascicoDigitale = new Fascicolo();
    
    //arrivo da allegati
    //String idDichiarazioneConsistenzaAllegati = request.getParameter("idDichiarazioneConsistenzaAllegati");
    String codiceStampaRicavato = "FASCICOLO_NEW";
    
    /*if(Validator.isNotEmpty(idDichiarazioneConsistenzaAllegati))
    {
      codiceStampaRicavato = "ALLEGATI_NEW";
    }*/
    
    if(Validator.isNotEmpty(request.getParameter("codiceStampaGenerico")))
    {      
      processSubReports(fascicoDigitale, request.getParameter("codiceStampaGenerico"),
          anagFacadeClient, gaaFacadeClient, request, hmParametri);
      
      codiceStampaRicavato = request.getParameter("codiceStampaGenerico");
    }
    else
    {
      processSubReports(fascicoDigitale, codiceStampaRicavato,
          anagFacadeClient, gaaFacadeClient, request, hmParametri);
    }
    
    TipoReportVO tipoReportVO = (TipoReportVO)hmParametri.get("tipoReportVO");
    String nomeFileXDP = tipoReportVO.getNomeFileXDP();
    
    DichiarazioneConsistenzaGaaVO dichCons = 
        (DichiarazioneConsistenzaGaaVO)hmParametri.get("dichConsistenza");
    AttributiModuloVO attributiModuloVO = null;
    if(dichCons != null)
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
    
    //arrivo da allegati
    /*String idDichiarazioneConsistenzaAllegati = request.getParameter("idDichiarazioneConsistenzaAllegati");
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
    
    if(Validator.isNotEmpty(idDichiarazioneConsistenzaAllegati))
    {
      gaaFacadeClient.insertFileValidazioneAllegatiAgriWell(anagAziendaVO, new Long(idDichiarazioneConsistenzaAllegati), arrayPdf, ruoloUtenza, SolmrConstants.ALLEGATI_VALIDAZIONE);
    }*/
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
    
    String dichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
    Date dataDichiarazioneStampa = null;
    Long idDichiarazioneConsistenza = null;
    
    String codiceStampaRicavato = "FASCICOLO_NEW";   
    if(Validator.isNotEmpty(request.getParameter("codiceStampaGenerico")))
    {            
      codiceStampaRicavato = request.getParameter("codiceStampaGenerico");
    }
    
    TipoReportVO tipoReportVO = gaaFacadeClient.getTipoReportByCodice(codiceStampaRicavato);
    hmParametri.put("tipoReportVO", tipoReportVO);
    if(Validator.isNotEmpty(tipoReportVO.getIdTipoAllegato()))
    {
      TipoAllegatoVO tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(tipoReportVO.getIdTipoAllegato().intValue());
      hmParametri.put("tipoAllegatoVO", tipoAllegatoVO);
    }
    
    
    //arrivo da allegati
    //String idDichiarazioneConsistenzaAllegati = request.getParameter("idDichiarazioneConsistenzaAllegati");
    
    
    //Sono in una dichiarazione di consistenza
    if(!"-1".equalsIgnoreCase(dichiarazioneConsistenza))
    {
      
      /*if(Validator.isNotEmpty(idDichiarazioneConsistenzaAllegati))
        idDichiarazioneConsistenza = new Long(idDichiarazioneConsistenzaAllegati);
      else*/
      
      idDichiarazioneConsistenza = new Long(dichiarazioneConsistenza);
      
      ConsistenzaVO consistenzaVO = anagFacadeClient
        .findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
      dataDichiarazioneStampa =  consistenzaVO.getDataDichiarazione();
    }
    else
    {
      dataDichiarazioneStampa = new Date();
    }
    
    hmParametri.put("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
    hmParametri.put("dataDichiarazioneStampa", dataDichiarazioneStampa);
    
    Date dataInserimentoDichiarazione = null;
    Long codiceFotografia = null;
    if(idDichiarazioneConsistenza !=null)
    {    
      DichiarazioneConsistenzaGaaVO  dichConsistenza = gaaFacadeClient
        .getDatiDichiarazioneConsistenza(idDichiarazioneConsistenza.longValue());
      
      codiceFotografia = dichConsistenza.getCodiceFotografia();
      dataInserimentoDichiarazione = dichConsistenza.getDataInserimentoDichiarazione();
      
      hmParametri.put("dichConsistenza", dichConsistenza);
    } 
    else
    {
      dataInserimentoDichiarazione = new Date();
    }
    
    hmParametri.put("dataInserimentoDichiarazione", dataInserimentoDichiarazione);
    hmParametri.put("codiceFotografia", codiceFotografia);
    
    // Dati anagrafici dell'azienda
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO) request.getSession()
        .getAttribute("anagAziendaVO");    
    AnagAziendaVO anagAziendaTmpVO = null;
    anagAziendaTmpVO = gaaFacadeClient.getAnagraficaAzienda(
        anagAziendaVO.getIdAzienda(),
        dataInserimentoDichiarazione);
    //Per evitare nullPointer
    //metto in sessione i dati corretti dell'azienda alla validazione..
    if(anagAziendaTmpVO == null)
    {
      anagAziendaTmpVO = new AnagAziendaVO();
    }   
    hmParametri.put("anagAziendaVO", anagAziendaTmpVO);
    
    //Dati Persona
    PersonaFisicaVO personaFisicaVO = anagFacadeClient
      .getRappresentanteLegaleFromIdAnagAziendaAndDichCons(anagAziendaTmpVO
      .getIdAnagAzienda().longValue(), dataInserimentoDichiarazione);    
    hmParametri.put("personaFisicaVO", personaFisicaVO);
    
    //Dati fascicolo    
    InfoFascicoloVO infoFascicoloVO = gaaFacadeClient
      .getInfoFascicolo(anagAziendaVO.getIdAzienda(), dataInserimentoDichiarazione, codiceFotografia);
    hmParametri.put("infoFascicoloVO", infoFascicoloVO);
    
    
    return hmParametri;
  }
  
  
  protected void preloadSubReports() throws SolmrException, IOException
  {
    addSubReport(new HeaderFascicolo());
    addSubReport(new Azienda());
    addSubReport(new Titolare());
    addSubReport(new AltriSoggetti());
    addSubReport(new AssociazioniSR());
    addSubReport(new ContiCorrentiSR());
    addSubReport(new Ute());
    addSubReport(new Manodopera());
    addSubReport(new ConsistenzaZootecnica());
    addSubReport(new TerreniElenco());
    addSubReport(new TerreniRiepilogoCond());
    addSubReport(new TerreniRiepilogoMacroUso());
    addSubReport(new TerreniRiepilogoTipoArea());
    addSubReport(new TerreniRiepilogoComune());
    addSubReport(new UVElenco());
    addSubReport(new UVRiepilogoVitignoIdoneita());
    addSubReport(new FabbricatiSR());
    addSubReport(new DocumentiElenco());
    addSubReport(new MotoriAgricoli());
    addSubReport(new DichiarazioniSR());
    addSubReport(new AllegatiSR());
    addSubReport(new AllegatiCondizionalitaSR());
    addSubReport(new HeaderAllegatiSR());
    addSubReport(new HeadAllegatiSR());
    addSubReport(new HeadCondizionalitaSR());
    addSubReport(new IntroDichiarazioniSR());
    addSubReport(new TrattamentoDatiSR());
    addSubReport(new FirmaSR());
    addSubReport(new CodaDichiarazioniSR());
    addSubReport(new HeaderLandscapeSR());
    addSubReport(new HeaderChecklist());
    addSubReport(new DichCheck2());
    addSubReport(new Firma2SR());
  }


  
}