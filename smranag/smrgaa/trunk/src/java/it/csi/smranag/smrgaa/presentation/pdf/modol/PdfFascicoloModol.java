package it.csi.smranag.smrgaa.presentation.pdf.modol;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>Title: Aziende trasformatrici</p>
 * <p>Description: Servizi on-line per le aziende vinicole di trasformazione</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TOBECONFIG</p>
 * @author TOBECONFIG
 * @version 1.0
 */

import it.csi.smranag.smrgaa.dto.AllegatoDichiarazioneVO;
import it.csi.smranag.smrgaa.dto.jaxb.fascicolo.Fascicolo;
import it.csi.smranag.smrgaa.dto.modol.AttributiModuloVO;
import it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO;
import it.csi.smranag.smrgaa.dto.stampe.DichiarazioneConsistenzaGaaVO;
import it.csi.smranag.smrgaa.dto.stampe.InfoFascicoloVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoAllegatoVO;
import it.csi.smranag.smrgaa.dto.stampe.TipoReportVO;
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
import it.csi.smrcomms.rpparser.util.Validator;
import it.csi.smrcomms.siapcommws.AllegatoInvioVO;
import it.csi.smrcomms.siapcommws.DatiMailVO;
import it.csi.smrcomms.siapcommws.InvioPostaCertificata;
import it.csi.solmr.client.anag.AnagFacadeClient;
import it.csi.solmr.dto.anag.AnagAziendaVO;
import it.csi.solmr.dto.anag.ConsistenzaVO;
import it.csi.solmr.dto.anag.PersonaFisicaVO;
import it.csi.solmr.dto.profile.RuoloUtenza;
import it.csi.solmr.etc.SolmrConstants;
import it.csi.solmr.etc.anag.AnagErrors;
import it.csi.solmr.exception.SolmrException;
import it.csi.solmr.util.DateUtils;
import it.csi.solmr.util.SolmrLogger;
import it.csi.solmr.util.services.AgriLogger;
import net.sf.jasperreports.compilers.JRBshCompiler;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

public class PdfFascicoloModol extends PdfModularFascicoloModol
{
  
  
	private ResourceBundle res = ResourceBundle.getBundle("config");
	 private String TEMPLATE_RESOURCE = res.getString("templatePdf");
  
  
  final String errMsg = "Problemi nella sezione crea una validazione."+
      "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  
  //public static final String CODICE_STAMPA    = "FASCICOLO_NEW";
  
  public PdfFascicoloModol() throws Exception
  {
    super();
  }

  

  protected byte[] popolaPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient, 
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    
    
    HashMap<String, Object> hmParametri = preload(request,
        anagFacadeClient, gaaFacadeClient, ruoloUtenza);
    
    Fascicolo fascicoDigitale = new Fascicolo();
    
    //arrivo da validazione
    //Long idDichiarazioneConsistenzaStampa = (Long)request.getSession().getAttribute("idDichiarazioneConsistenzaStampa");
    //arrivo da allegati
    //String idDichiarazioneConsistenzaAllegati = request.getParameter("idDichiarazioneConsistenzaAllegati");
    String codiceStampaRicavato = "FASCICOLO_NEW";
    /*if(Validator.isNotEmpty(idDichiarazioneConsistenzaStampa))
    {
      codiceStampaRicavato = gaaFacadeClient.getTipoReportByValidazioneEAllegato(idDichiarazioneConsistenzaStampa.longValue(),
          SolmrConstants.VALIDAZIONE_ALLEGATO);
    }*/
    /*else if(Validator.isNotEmpty(idDichiarazioneConsistenzaAllegati))
    {
      codiceStampaRicavato = "ALLEGATI_NEW";
    }*/
    
    /*if(Validator.isNotEmpty(request.getParameter("codiceStampaGenerico")))
    {      
      processSubReports(fascicoDigitale, request.getParameter("codiceStampaGenerico"),
          anagFacadeClient, gaaFacadeClient, request, hmParametri);
    }
    else
    {
      processSubReports(fascicoDigitale, codiceStampaRicavato,
          anagFacadeClient, gaaFacadeClient, request, hmParametri);
    }*/
    processSubReports(fascicoDigitale, codiceStampaRicavato,
        anagFacadeClient, gaaFacadeClient, request, hmParametri);
    TipoReportVO tipoReportVO = (TipoReportVO)hmParametri.get("tipoReportVO");
    String nomeFileXDP = tipoReportVO.getNomeFileXDP();
    
    //DichiarazioneConsistenzaGaaVO dichCons = 
      //  (DichiarazioneConsistenzaGaaVO)hmParametri.get("dichConsistenza");
    AttributiModuloVO attributiModuloVO = new AttributiModuloVO(SolmrConstants.CODICE_APPLICAZIONE_MODOL,
        SolmrConstants.DESCRIZIONE_APPLICAZIONE_MODOL, nomeFileXDP,
        nomeFileXDP, SolmrConstants.RIF_ADOBE_MODOL+""+nomeFileXDP+".xdp");
    
    
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
    //arrivo da validazione  
    Long idDichiarazioneConsistenzaStampa = (Long)request.getSession().getAttribute("idDichiarazioneConsistenzaStampa");
    //arrivo da allegati
    //String idDichiarazioneConsistenzaAllegati = request.getParameter("idDichiarazioneConsistenzaAllegati");
    
    TipoAllegatoVO tipoAllegatoVO = (TipoAllegatoVO)request.getAttribute("tipoAllegatoVO");
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
    int idTipoAllegato = tipoAllegatoVO.getIdTipoAllegato();
    
    AllegatoDichiarazioneVO allegatoDichiarazioneStor = 
        gaaFacadeClient.getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
            idDichiarazioneConsistenzaStampa, idTipoAllegato);
    //esiste già un allegato valido quindi storicizzo su index
    if(Validator.isNotEmpty(allegatoDichiarazioneStor)
        && Validator.isNotEmpty(allegatoDichiarazioneStor.getExtIdDocumentoIndex()))
    {
      gaaFacadeClient.insertFileValidazioneRigeneraAgriWell(anagAziendaVO, idDichiarazioneConsistenzaStampa, arrayPdf, ruoloUtenza, idTipoAllegato);
    }
    //Inserisco la prima volta....
    else
    {
      gaaFacadeClient.insertFileValidazioneAgriWell(anagAziendaVO, idDichiarazioneConsistenzaStampa, arrayPdf, ruoloUtenza);
    }
  }
  
  protected void inviaMailPdf(HttpServletRequest request,
      AnagFacadeClient anagFacadeClient,
      GaaFacadeClient gaaFacadeClient,
      byte[] arrayPdf, 
      RuoloUtenza ruoloUtenza) throws Exception
  {
    //arrivo da validazione  
    Long idDichiarazioneConsistenzaStampa = (Long)request.getSession().getAttribute("idDichiarazioneConsistenzaStampa");
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
    AziendaNuovaVO aziendaNuovaVO  = (AziendaNuovaVO)request.getSession().getAttribute("aziendaNuovaDichiarazioneVO");
    request.getSession().removeAttribute("aziendaNuovaDichiarazioneVO");
    String flagInvioMail = (String)request.getSession().getAttribute("flagInvioMail");
    request.getSession().removeAttribute("flagInvioMail");
    
    String parametroAbilitaMail = "";
    try 
    {
       parametroAbilitaMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ABILITA_INVIO_MAIL);
    }
    catch(SolmrException se) 
    {
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ABILITA_INVIO_MAIL+".\n"+se.toString();
      throw new Exception(messaggio);
    }
    SolmrLogger.debug(this, " --- ABILITA MAIL ="+parametroAbilitaMail);
      
    //Andato tutto ok mando la mail!!!
    //L'azienda è passata dalla richiesta azienda...
    if("S".equalsIgnoreCase(parametroAbilitaMail)
        && Validator.isNotEmpty(aziendaNuovaVO))
    {
      try 
      {
        //SendMail invioMail = new SendMail();
        //Vector<DatiMail> datiMailVect = new Vector<DatiMail>();
           
        // ** Costruisco gli oggetti con i dati per l'invio mail      
        SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");
           
           
        //se sono qui la pec o la mail sono per forza uno dei due valorizzati
        String destinatarioMail = "";
        if(Validator.isNotEmpty(anagAziendaVO.getPec()))
        {
          destinatarioMail = anagAziendaVO.getPec();
        }
        else
        {
          destinatarioMail = anagAziendaVO.getMail();
        }
           
        SolmrLogger.debug(this, " --- sono stati trovati dei DESTINATARIO :"+destinatarioMail);
             
        // ----- Mittente -> VTMA di DB_ALTRI_DATI       
        String mittente = "";
        try 
        {
          mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_RICH_AZ);
        }
        catch(SolmrException se) 
        {
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MITT_MAIL_RICH_AZ+".\n"+se.toString();
          throw new Exception(messaggio);
        }
        SolmrLogger.debug(this, " --- MITTENTE ="+mittente);
           
        String mittenteReplyTo = "";
        try 
        {
          mittenteReplyTo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_REPLY_TO_MAIL);
        }
        catch(SolmrException se) 
        {
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_REPLY_TO_MAIL+".\n"+se.toString();
          throw new Exception(messaggio);
        }
        SolmrLogger.debug(this, " --- MITTENTE REPLYTO="+mittenteReplyTo);
               
        // ---- Oggetto
        String oggettoMail = "";
        try 
        {
          oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_VAL_RICH_VALID);
        }
        catch(SolmrException se) 
        {
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_VAL_RICH_VALID+".\n"+se.toString();
          throw new Exception(messaggio);
        }            
        SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
               
        // ----- Testo
        String testo = "";
        try 
        {
          testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_TXT_VAL_RICH_VALID);
        }
        catch(SolmrException se) 
        {
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_TXT_VAL_RICH_VALID+".\n"+se.toString();
          throw new Exception(messaggio);
        }
           
        if(Validator.isNotEmpty(testo))
        {              
          String denAzienda = anagAziendaVO.getCUAA()+" - "+anagAziendaVO.getDenominazione();         
          testo = testo.replaceAll("<<AZIENDA>>", denAzienda);
          testo = testo.replaceAll("<<DATA_RICHIESTA>>", DateUtils.formatDate(aziendaNuovaVO.getDataAggiornamentoIter()));
          testo = testo.replaceAll("<<MOTIVO_VALIDAZIONE>>", aziendaNuovaVO.getDescMotivoDichiarazione());
        }
        SolmrLogger.debug(this, " --- TESTO ="+testo);
        
        
        InvioPostaCertificata invioPosta = new InvioPostaCertificata();
        DatiMailVO[] arrDatiMail = new DatiMailVO[1]; 
        DatiMailVO datiMailVO = new DatiMailVO(); 
        
        String[] arrDestinatari = new String[1];
        arrDestinatari[0] = destinatarioMail;
        datiMailVO.setDestinatariA(arrDestinatari);
        datiMailVO.setMittente(mittente);
        datiMailVO.setMittenteDisplayName(mittenteReplyTo);
        datiMailVO.setOggetto(oggettoMail);
        datiMailVO.setTesto(testo);
        
        AllegatoInvioVO[] arrAllegato = new AllegatoInvioVO[1];
        AllegatoInvioVO allegato = new AllegatoInvioVO();
        allegato.setAllegato(arrayPdf);
        allegato.setNomeFile("Validazione.pdf");
        allegato.setMimeType("application/pdf");
        arrAllegato[0] = allegato;
        datiMailVO.setAllegati(arrAllegato);
        
        arrDatiMail[0] = datiMailVO;
        invioPosta.setInput(arrDatiMail);
        
        // Setto i valori per le mail da inviare
        //Vector<String> destinatariMailA = new Vector<String>(); 
        //destinatariMailA.add(destinatarioMail);           
           
        //DatiMail datiMail = new DatiMail();
        //datiMail.setDestinatariA(destinatariMailA);
        //datiMail.setMittente(mittente);
        //datiMail.setMittenteReplyTo(mittenteReplyTo);           
        //datiMail.setOggetto(oggettoMail);           
        //datiMail.setTesto(testo);
        /*Vector<AllegatoInvioMail> vAllegati = new Vector<AllegatoInvioMail>();
        AllegatoInvioMail allegato = new AllegatoInvioMail();
        allegato.setAllegato(arrayPdf);
        //da verificare.....
        allegato.setNomeFile("Validazione");
        vAllegati.add(allegato);
        datiMail.setAllegati(vAllegati);
        datiMailVect.add(datiMail); 
             
        // Se ci sono destinatari per l'invio mail, invio MAIL
        SolmrLogger.debug(this, " --- controllo se devono essere inviate delle mail");
        if(datiMailVect != null && datiMailVect.size()>0)
        {
          SolmrLogger.error(this, " --- ******** INVIO DELLE MAIL dopo la TRASMISSIONE *****-----");
          SolmrLogger.error(this, " -- numero di mail da inviare ="+datiMailVect.size());
          invioMail.sendMail(datiMailVect);     
                     
        }*/
        
        anagFacadeClient.serviceInviaPostaCertificata(invioPosta);
      }
      catch (Exception e) 
      {
        String messaggio = AnagErrors.ERRORE_KO_INVIO_MAIL+".\n"+e.toString();
        throw new Exception(messaggio);
      }
    }
    else if("S".equalsIgnoreCase(flagInvioMail)
      && "S".equalsIgnoreCase(parametroAbilitaMail))
    {
      try 
      {
        //SendMail invioMail = new SendMail();
        //Vector<DatiMail> datiMailVect = new Vector<DatiMail>();
          
        // ** Costruisco gli oggetti con i dati per l'invio mail      
        SolmrLogger.debug(this, " -- ** Ricerca dei destinatari delle mail da inviare");
          
        //se sono qui la pec o la mail sono per forza uno dei due valorizzati
        String destinatarioMail = "";
        if(Validator.isNotEmpty(anagAziendaVO.getPec()))
        {
          destinatarioMail = anagAziendaVO.getPec();
        }
        else
        {
          destinatarioMail = anagAziendaVO.getMail();
        }         
        SolmrLogger.debug(this, " --- sono stati trovati dei DESTINATARIO :"+destinatarioMail);
            
        // ----- Mittente -> VTMA di DB_ALTRI_DATI       
        String mittente = "";
        try 
        {
          mittente = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MITT_MAIL_PER_DICH);
        }
        catch(SolmrException se) 
        {
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MITT_MAIL_PER_DICH+".\n"+se.toString();
          throw new Exception(messaggio);
        }
        SolmrLogger.debug(this, " --- MITTENTE ="+mittente);
          
        String mittenteReplyTo = "";
        try 
        {
          mittenteReplyTo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_REPLY_TO_MAIL);
        }
        catch(SolmrException se) 
        {
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_REPLY_TO_MAIL+".\n"+se.toString();
          throw new Exception(messaggio);
        }
        SolmrLogger.debug(this, " --- MITTENTE REPLYTO="+mittenteReplyTo);
              
        // ---- Oggetto
        String oggettoMail = "";
        try 
        {
          oggettoMail = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_OGG_MAIL_PER_DICH);
        }
        catch(SolmrException se) 
        {
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_OGG_MAIL_PER_DICH+".\n"+se.toString();
          throw new Exception(messaggio);
        }
        SolmrLogger.debug(this, " --- OGGETTO ="+oggettoMail);
                 
        // ----- Testo
        String testo = "";
        try 
        {
          testo = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_MAIL_PER_DICH);
        }
        catch(SolmrException se) 
        {
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_MAIL_PER_DICH+".\n"+se.toString();
          throw new Exception(messaggio);
        }
        
        
        String nomeFile = "Validazione_";
        if(Validator.isNotEmpty(testo))
        {
          ConsistenzaVO lastConsistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenzaStampa);
                     
          testo = testo.replaceAll("<<DATA_VALIDAZIONE>>", lastConsistenzaVO.getData());
          testo = testo.replaceAll("<<CUAA>>",anagAziendaVO.getCUAA());
          testo = testo.replaceAll("<<DENOMINAZIONE>>",anagAziendaVO.getDenominazione());
          String strProtocollo = "";
          if(Validator.isNotEmpty(lastConsistenzaVO.getNumeroProtocollo()))
          {
        	  strProtocollo += "con numero repertorio "+lastConsistenzaVO.getNumeroProtocollo(); 
            strProtocollo += " del "+DateUtils.formatDateTimeNotNull(lastConsistenzaVO.getDataProtocollo());
            nomeFile += lastConsistenzaVO.getNumeroProtocollo();
          }
          else
          {
        	strProtocollo += "privo di repertoriazione";
            nomeFile += lastConsistenzaVO.getIdDichiarazioneConsistenza();
          }
          testo = testo.replaceAll("<<PROTOCOLLO>>", strProtocollo);
        }
        SolmrLogger.debug(this, " --- TESTO ="+testo);
              
        // Setto i valori per le mail da inviare
        InvioPostaCertificata invioPosta = new InvioPostaCertificata();
        DatiMailVO[] arrDatiMail = new DatiMailVO[1]; 
        DatiMailVO datiMailVO = new DatiMailVO(); 
        
        String[] arrDestinatari = new String[1];
        arrDestinatari[0] = destinatarioMail;
        datiMailVO.setDestinatariA(arrDestinatari);
        datiMailVO.setMittente(mittente);
        datiMailVO.setMittenteDisplayName(mittenteReplyTo);
        datiMailVO.setOggetto(oggettoMail);
        datiMailVO.setTesto(testo);
        
        AllegatoInvioVO[] arrAllegato = new AllegatoInvioVO[1];
        AllegatoInvioVO allegato = new AllegatoInvioVO();
        allegato.setAllegato(arrayPdf);
        allegato.setNomeFile(nomeFile+".pdf");
        allegato.setMimeType("application/pdf");
        arrAllegato[0] = allegato;
        datiMailVO.setAllegati(arrAllegato);
        
        arrDatiMail[0] = datiMailVO;
        invioPosta.setInput(arrDatiMail);
        
        
        anagFacadeClient.serviceInviaPostaCertificata(invioPosta);
        
        
        
        
        
        /*Vector<String> destinatariMailA = new Vector<String>(); 
        destinatariMailA.add(destinatarioMail);           
          
        DatiMail datiMail = new DatiMail();
        datiMail.setDestinatariA(destinatariMailA);
        datiMail.setMittente(mittente);
        datiMail.setMittenteReplyTo(mittenteReplyTo);           
        datiMail.setOggetto(oggettoMail);           
        datiMail.setTesto(testo);
        Vector<AllegatoInvioMail> vAllegati = new Vector<AllegatoInvioMail>();
        AllegatoInvioMail allegato = new AllegatoInvioMail();
        allegato.setAllegato(arrayPdf);
        allegato.setNomeFile(nomeFile);
        vAllegati.add(allegato);
        datiMail.setAllegati(vAllegati);
        datiMailVect.add(datiMail); 
                         
        // Se ci sono destinatari per l'invio mail, invio MAIL
        SolmrLogger.debug(this, " --- controllo se devono essere inviate delle mail");
        if(datiMailVect != null && datiMailVect.size()>0)
        {
          SolmrLogger.error(this, " --- ******** INVIO DELLE MAIL dopo la TRASMISSIONE *****-----");
          SolmrLogger.error(this, " -- numero di mail da inviare ="+datiMailVect.size());
          invioMail.sendMail(datiMailVect);     
        }*/
      }
      catch (Exception e) 
      {
        String messaggio = AnagErrors.ERRORE_KO_INVIO_MAIL+".\n"+e.toString();
        throw new Exception(messaggio);
      }  
    }
      
    
  }
      


  private HashMap<String, Object> preload(HttpServletRequest request,
      AnagFacadeClient anagFacadeClient, GaaFacadeClient gaaFacadeClient,
      RuoloUtenza ruoloUtenza)
      throws Exception
  {
    HashMap<String, Object> hmParametri = new HashMap<String, Object>();
    
    String dichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
    Date dataDichiarazioneStampa = null;
    Long idDichiarazioneConsistenza = null;
    
    //arrivo da validazione  
    Long idDichiarazioneConsistenzaStampa = (Long)request.getSession().getAttribute("idDichiarazioneConsistenzaStampa");    
    
    //arrivo da allegati
    //String idDichiarazioneConsistenzaAllegati = request.getParameter("idDichiarazioneConsistenzaAllegati");
    
    
    //Sono in una dichiarazione di consistenza
    if(!"-1".equalsIgnoreCase(dichiarazioneConsistenza))
    {
      if(Validator.isNotEmpty(idDichiarazioneConsistenzaStampa))
        idDichiarazioneConsistenza = idDichiarazioneConsistenzaStampa;
      else
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
    
    
    TipoReportVO tipoReportVO = gaaFacadeClient.getTipoReportByCodice("FASCICOLO_NEW");
    hmParametri.put("tipoReportVO", tipoReportVO);
    if(Validator.isNotEmpty(tipoReportVO.getIdTipoAllegato()))
    {
      TipoAllegatoVO tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(tipoReportVO.getIdTipoAllegato().intValue());
      hmParametri.put("tipoAllegatoVO", tipoAllegatoVO);
    }
    
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