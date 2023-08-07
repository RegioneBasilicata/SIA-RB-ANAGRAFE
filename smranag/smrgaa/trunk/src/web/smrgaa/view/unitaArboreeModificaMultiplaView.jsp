<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeModificaMultipla.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
 
 	
	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
	StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
	TipoUtilizzoVO[] elencoTipiUsoSuolo = (TipoUtilizzoVO[])request.getAttribute("elencoTipiUsoSuolo");
	Vector<TipoDestinazioneVO> vTipoDestinazione = (Vector<TipoDestinazioneVO>)request.getAttribute("vTipoDestinazione");
  Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = (Vector<TipoDettaglioUsoVO>)request.getAttribute("vTipoDettaglioUso");
  Vector<TipoQualitaUsoVO> vTipoQualitaUso = (Vector<TipoQualitaUsoVO>)request.getAttribute("vTipoQualitaUso");
  Vector<TipoVarietaVO> elencoTipoVarieta = (Vector<TipoVarietaVO>)request.getAttribute("elencoTipoVarieta");
  Vector<TipoTipologiaVinoVO> vTipoTipologiaVino = (Vector<TipoTipologiaVinoVO>)request.getAttribute("vTipoTipologiaVino");
	Hashtable<Integer, Vector<TipoVarietaVO>> elencoVarieta = (Hashtable<Integer,Vector<TipoVarietaVO>>)request.getAttribute("elencoVarieta");
	ValidationErrors elencoErrori = (ValidationErrors)request.getAttribute("elencoErrori");
  Vector<ValidationErrors> elencoErroriMultipli = (Vector<ValidationErrors>)request.getAttribute("elencoErroriMultipli");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  //String parametroBUVP = (String)request.getAttribute("parametroBUVP");
  //String parametroIUVP = (String)request.getAttribute("parametroIUVP");
  String parametroAccessoIdoneita = (String)request.getAttribute("parametroAccessoIdoneita");
  String parametroAccessoVarArea = (String)request.getAttribute("parametroAccessoVarArea");
  String parametroAccessoAltriDati = (String)request.getAttribute("parametroAccessoAltriDati");
  String parametroAccessoAllineaGis = (String)request.getAttribute("parametroAccessoAllineaGis");
  String parametroOtherUVP = (String)request.getAttribute("parametroOtherUVP");
  String[] elencoSupPostVit = request.getParameterValues("supPostVit");
  Vector<Long> vPraticheIdParticella = (Vector<Long>)request.getAttribute("vPraticheIdParticella");
  Vector<Long> vIdStoricoUvModVITI = (Vector<Long>)request.getAttribute("vIdStoricoUvModVITI");
  Vector<Long> vIdStoricoUnitaArboreaDataImpianto = (Vector<Long>)request.getAttribute("vIdStoricoUnitaArboreaDataImpianto");
	
	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
	StringProcessor jssp = new JavaScriptStringProcessor();
  
  boolean almenoUnaValidata = false;
  //Mi dice se esiste almeno una uv validata con un valore di idoneità vaorizzato.
  boolean almenoUnaIdoneitaValidata = false;
  
  
	
  
  //Non si può fare l'allinea uv gis
  if(session.getAttribute("noTutteUvAllGis") != null)
  {
    htmpl.set("noTutteUvAllGis", "true");
  }
  
	htmpl.newBlock("blkDati");
	if(ruoloUtenza.isUtenteRegionale() || ruoloUtenza.isUtenteProvinciale())
	{
		htmpl.set("blkDati.valuePulsante", "conferma e valida");
		htmpl.newBlock("blkDati.blkConfermaPA");
  }
	else htmpl.set("blkDati.valuePulsante", "conferma");
  
  
  String idTipoUtilizzo=request.getParameter("idTipoUtilizzo");
  String idTipoDestinazione=request.getParameter("idTipoDestinazione");
  String idTipoDettaglioUso=request.getParameter("idTipoDettaglioUso");
  String idTipoQualitaUso=request.getParameter("idTipoQualitaUso");
  String idVarieta=request.getParameter("idVarieta");
  String idTipologiaVino=request.getParameter("idTipologiaVino");
  
  
  // DESTINAZIONE_PRODUTTIVA filtro
  if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.length > 0) 
  {
    for(int c = 0; c < elencoTipiUsoSuolo.length; c++) 
    {
      htmpl.newBlock("blkDati.blkTipiUsoSuolo");
      htmpl.set("blkDati.blkTipiUsoSuolo.idTipoUtilizzo", elencoTipiUsoSuolo[c].getIdUtilizzo().toString());
      htmpl.set("blkDati.blkTipiUsoSuolo.descrizione", "["+elencoTipiUsoSuolo[c].getCodice()+"] "+elencoTipiUsoSuolo[c].getDescrizione());
      if(idTipoUtilizzo != null && idTipoUtilizzo.equals(""+elencoTipiUsoSuolo[c].getIdUtilizzo())) 
        htmpl.set("blkDati.blkTipiUsoSuolo.selected", "selected=\"selected\"");
    }
  }
  
  if(Validator.isNotEmpty(vTipoDestinazione))
  {
    for(int i=0;i<vTipoDestinazione.size();i++) 
    {
      TipoDestinazioneVO tipoDestinazioneVO = vTipoDestinazione.get(i);
      htmpl.newBlock("blkDati.blkTipiDestinazione");
      
      htmpl.set("blkDati.blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
      
      if(idTipoDestinazione != null && new Long(tipoDestinazioneVO.getIdTipoDestinazione()).compareTo(new Long(idTipoDestinazione)) == 0) 
      {
        htmpl.set("blkDati.blkTipiDestinazione.selected", "selected=\"selected\"", null);
      }
      
      htmpl.set("blkDati.blkTipiDestinazione.descCompleta", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+tipoDestinazioneVO.getDescrizioneDestinazione());
      String descrizione = null;
      if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
      {
        descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
      }
      else 
      {
        descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
      }
      htmpl.set("blkDati.blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
    }
  }
  
  if(Validator.isNotEmpty(vTipoDettaglioUso))
  {
    for(int i=0;i<vTipoDettaglioUso.size();i++) 
    {
      TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(i);
      htmpl.newBlock("blkDati.blkTipiDettaglioUso");
      
      htmpl.set("blkDati.blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
      
      if(idTipoDettaglioUso != null && new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).compareTo(new Long(idTipoDettaglioUso)) == 0) 
      {
        htmpl.set("blkDati.blkTipiDettaglioUso.selected", "selected=\"selected\"", null);
      }
      
      String descrizione = null;
      if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
      {
        descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
      }
      else 
      {
        descrizione = tipoDettaglioUsoVO.getDescrizione();
      }
      htmpl.set("blkDati.blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
    }
  }
  
  if(Validator.isNotEmpty(vTipoQualitaUso))
  {
    for(int i=0;i<vTipoQualitaUso.size();i++) 
    {
      TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUso.get(i);
      htmpl.newBlock("blkDati.blkTipiQualitaUso");
      
      htmpl.set("blkDati.blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
      
      if(idTipoQualitaUso != null && new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).compareTo(new Long(idTipoQualitaUso)) == 0) 
      {
        htmpl.set("blkDati.blkTipiQualitaUso.selected", "selected=\"selected\"", null);
      }
      
      String descrizione = null;
      if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
      {
        descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
      }
      else 
      {
        descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
      }
      htmpl.set("blkDati.blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
    }
  }
  
  // Combo varietà primaria
  if(Validator.isNotEmpty(elencoTipoVarieta)) 
  {
    for(int i = 0; i < elencoTipoVarieta.size(); i++) 
    {
      TipoVarietaVO tipoVarietaVO = elencoTipoVarieta.get(i);
      
      htmpl.newBlock("blkDati.blkTipiVarieta");
      htmpl.set("blkDati.blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
      htmpl.set("blkDati.blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
      if(idVarieta != null && tipoVarietaVO.getIdVarieta().compareTo(new Long(idVarieta)) == 0) 
      {
        htmpl.set("blkDati.blkTipiVarieta.selected", "selected=\"selected\"", null);
      }
    }
  }
  
  //VITIGNI filtro
  /*if(tipoVarietaFiltri != null && tipoVarietaFiltri.length > 0 && Validator.isNotEmpty(idTipoUtilizzo)) 
  {
    for(int c = 0; c < tipoVarietaFiltri.length; c++) 
    {
      htmpl.newBlock("blkDati.blkTipiVarieta");
      htmpl.set("blkDati.blkTipiVarieta.idVarieta", tipoVarietaFiltri[c].getIdVarieta().toString());
      htmpl.set("blkDati.blkTipiVarieta.descrizione", "["+tipoVarietaFiltri[c].getCodiceVarieta()+"] "+tipoVarietaFiltri[c].getDescrizione());
      if(idVarieta != null)
        if(idVarieta.equals(""+tipoVarietaFiltri[c].getIdVarieta()))
          htmpl.set("blkDati.blkTipiVarieta.selected", "selected=\"selected\"");
    }
  }*/
  
  
  //Tipologia di vino filtro
  if(vTipoTipologiaVino != null)
  { 
    for(TipoTipologiaVinoVO tipoTipologiaVinoVO:vTipoTipologiaVino)
    {
      htmpl.newBlock("blkDati.blkTipologiaVino");        
      htmpl.set("blkDati.blkTipologiaVino.idTipologiaVino", ""+tipoTipologiaVinoVO.getIdTipologiaVino().longValue());
      htmpl.set("blkDati.blkTipologiaVino.descrizione", tipoTipologiaVinoVO.getDescrizione());
      
      if((idTipologiaVino !=null)
        && (idTipologiaVino.equals(""+tipoTipologiaVinoVO.getIdTipologiaVino())))
      {
        htmpl.set("blkDati.blkTipologiaVino.selected", "selected=\"selected\"", null);
        //Entro qui nel caso di errore X
        if("N".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneVigna()))
        {
          htmpl.set("blkDati.disabledFunzioneVignaError","disabled=\"disabled\"", null);
          htmpl.set("varDisabledVigna","true", null);
        }
        
        if("N".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneEtichetta()))
        {
          htmpl.set("blkDati.disabledFunzioneAnnotazioneEtichettaError","disabled=\"disabled\"", null);
          htmpl.set("varDisabledAnnotazioneEtichetta","true", null);
        }        
      }
      
      htmpl.newBlock("blkDati.blkTipologiaHiddenVino");
      
      if(tipoTipologiaVinoVO.getFlagGestioneVigna() != null)
      {
        htmpl.set("blkDati.blkTipologiaHiddenVino.hiddenFlagGestioneVigna", tipoTipologiaVinoVO.getFlagGestioneVigna()); 
      }
      
      if(tipoTipologiaVinoVO.getVignaVO() != null)
      {
        htmpl.set("blkDati.blkTipologiaHiddenVino.hiddenIdVignaPresente", "S"); 
      }
      
      if(tipoTipologiaVinoVO.getFlagGestioneEtichetta() != null)
      {
        htmpl.set("blkDati.blkTipologiaHiddenVino.hiddenFlagGestioneEtichetta", tipoTipologiaVinoVO.getFlagGestioneEtichetta()); 
      }
    }
  }
  
  //Matricola  filtro
  htmpl.set("blkDati.matricolaCCIAA", request.getParameter("matricolaCCIAA"));
  
  //Anno iscrizione albo (aaaa) filtro
  htmpl.set("blkDati.annoIscrizioneAlbo", request.getParameter("annoIscrizioneAlbo"));
  
  //Data impianto* (dd/mm/aaaa) filtro
  htmpl.set("blkDati.dataImpiantoText", request.getParameter("dataImpiantoText"));
  
  //Data prima produzione* (dd/mm/aaaa) filtro
  htmpl.set("blkDati.dataPrimaProduzioneText", request.getParameter("dataPrimaProduzioneText"));
  
  //Vigna filtro
  htmpl.set("blkDati.vignaText", request.getParameter("vignaText"));
  
  //Annotazione in etichetta filtro
  htmpl.set("blkDati.annotazioneEtichettaText", request.getParameter("annotazioneEtichettaText"));
  
 
  if ("cambia".equals(request.getParameter("funzioneCambia"))) htmpl.set("blkDati.checkedCambia", "checked=\"checked\"",null);
  if ("tipologiaVino".equals(request.getParameter("funzioneTipologiaVino"))) htmpl.set("blkDati.checkedtipologiaVino", "checked=\"checked\"",null);
  if ("dataImpianto".equals(request.getParameter("funzioneDataImpianto"))) htmpl.set("blkDati.checkedDataImpianto", "checked=\"checked\"",null);
  if ("vigna".equals(request.getParameter("funzioneVigna"))) htmpl.set("blkDati.checkedVigna", "checked=\"checked\"",null);
  if ("annotazioneEtichetta".equals(request.getParameter("funzioneAnnotazioneEtichetta"))) htmpl.set("blkDati.checkedAnnotazioneEtichetta", "checked=\"checked\"",null);
  if ("allineaUVGIS".equals(request.getParameter("funzioneAllineaUVGIS"))) htmpl.set("blkDati.checkedAllineaUVGIS", "checked=\"checked\"",null);
  
	for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
  {
		htmpl.newBlock("blkDati.blkElenco");
		htmpl.set("blkDati.blkElenco.numero", String.valueOf(i));
		StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
		StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
    
    
    if(Validator.isNotEmpty(vPraticheIdParticella))
    {
      if(vPraticheIdParticella.contains(storicoParticellaVO.getIdParticella()))
      {
        htmpl.set("blkDati.blkElenco.pratiche", MessageFormat.format(htmlStringKO,
          new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT+"'",
          AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT}), null);
      }
    }
    
    
    if(Validator.isNotEmpty(vIdStoricoUvModVITI))
    {
      if(vIdStoricoUvModVITI.contains(storicoUnitaArboreaVO.getIdStoricoUnitaArborea()))
      {
        htmpl.set("blkDati.blkElenco.pratiche", MessageFormat.format(htmlStringKO,
          new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_SINGOLA_JAVASCRIPT+"'",
          AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_SINGOLA}),null);
      }
    }
    
    if(Validator.isNotEmpty(vIdStoricoUnitaArboreaDataImpianto))
    {
      if(vIdStoricoUnitaArboreaDataImpianto.contains(storicoUnitaArboreaVO.getIdStoricoUnitaArborea()))
      {
        htmpl.set("blkDati.blkElenco.pratiche", MessageFormat.format(htmlStringKO,
          new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_UGUALE_CORRENTE_JAVASCRIPT+""+DateUtils.getCurrentYear()+"'",
          AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_UGUALE_CORRENTE_JAVASCRIPT+""+DateUtils.getCurrentYear()}),null);
      }
    }
    
    
    if(!almenoUnaValidata && SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea()))
    {
      almenoUnaValidata = true;
    }
    if(!almenoUnaIdoneitaValidata && storicoUnitaArboreaVO.isBloccaModificaIdoneitaValida())
    {
      almenoUnaIdoneitaValidata = true;
    }
    
		htmpl.set("blkDati.blkElenco.idStoricoUnitaArborea", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
		// COMUNE
		htmpl.set("blkDati.blkElenco.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
		htmpl.set("blkDati.blkElenco.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
		// SEZIONE
		if(Validator.isNotEmpty(storicoParticellaVO.getSezione()))
			htmpl.set("blkDati.blkElenco.sezione", storicoParticellaVO.getSezione());
		// FOGLIO
		htmpl.set("blkDati.blkElenco.foglio", storicoParticellaVO.getFoglio());
		// PARTICELLA
		if(Validator.isNotEmpty(storicoParticellaVO.getParticella()))
			htmpl.set("blkDati.blkElenco.particella", storicoParticellaVO.getParticella());
		// SUBALTERNO
		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno()))
			htmpl.set("blkDati.blkElenco.subalterno", storicoParticellaVO.getSubalterno());
		// SUPERFICIE CATASTALE
		htmpl.set("blkDati.blkElenco.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    // PROG. UNAR
    htmpl.set("blkDati.blkElenco.progrUnar", storicoUnitaArboreaVO.getProgrUnar());
    
    //SUPERFICIE ELEGGIBILE
    if(storicoParticellaVO.getParticellaCertificataVO() != null)
    {
      ParticellaCertificataVO particellaCertificataVO = storicoParticellaVO.getParticellaCertificataVO();
      if(Validator.isNotEmpty(particellaCertificataVO.getVParticellaCertEleg()) 
        && (particellaCertificataVO.getVParticellaCertEleg().size() > 0)) 
      {
        //Per la query è popolato solo il primo elemento
        ParticellaCertElegVO partCertVO = (ParticellaCertElegVO)particellaCertificataVO.getVParticellaCertEleg().get(0);
        if(Validator.isNotEmpty(partCertVO.getSuperficie())) {
          htmpl.set("blkDati.blkElenco.supEleggibile", Formatter.formatDouble4(partCertVO.getSuperficie()));         
        }
        else 
        {
          htmpl.set("blkDati.blkElenco.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
        }
      }
      else
      {
        htmpl.set("blkDati.blkElenco.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
      }
    }
    
    
    // Sup Post Vit
    if(elencoSupPostVit !=null)
    {
      htmpl.set("blkDati.blkElenco.supPostVit",elencoSupPostVit[i]);
    }
    else
    {
      htmpl.set("blkDati.blkElenco.supPostVit", StringUtils.parseSuperficieField(
        storicoUnitaArboreaVO.getSupPostVit()));
    }
    
    
    
    // DESTINAZIONE_PRODUTTIVA
    /*if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.length > 0) 
    {
      for(int c = 0; c < elencoTipiUsoSuolo.length; c++) 
      {
        TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo[c];
        if(storicoUnitaArboreaVO.getIdUtilizzo() != null && 
          storicoUnitaArboreaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
        {
          htmpl.set("blkDati.blkElenco.descUsoSuolo", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
          break;
        }
      }
    }*/
    String descUso = "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] "+storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione();
    if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoDestinazioneVO()))
    {
      descUso += "/["+storicoUnitaArboreaVO.getTipoDestinazioneVO().getCodiceDestinazione()+"] "+storicoUnitaArboreaVO.getTipoDestinazioneVO().getDescrizioneDestinazione();
    }
    if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoDettaglioUsoVO()))
    {
      descUso += "/["+storicoUnitaArboreaVO.getTipoDettaglioUsoVO().getCodiceDettaglioUso()+"] "+storicoUnitaArboreaVO.getTipoDettaglioUsoVO().getDescrizione();
    }
    if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTipoQualitaUsoVO()))
    {
      descUso += "/["+storicoUnitaArboreaVO.getTipoQualitaUsoVO().getCodiceQualitaUso()+"] "+storicoUnitaArboreaVO.getTipoQualitaUsoVO().getDescrizioneQualitaUso();
    }
    
    
    htmpl.set("blkDati.blkElenco.descUsoSuolo", descUso);
    // VITIGNO
    if(elencoVarieta != null && elencoVarieta.size() > 0 && storicoUnitaArboreaVO.getIdUtilizzo() != null) 
    {
      Enumeration<Integer> enumeraVarieta = elencoVarieta.keys();
      while(enumeraVarieta.hasMoreElements()) 
      {
        Integer key = (Integer)enumeraVarieta.nextElement();
        if(key.compareTo(new Integer(i)) == 0) 
        {
          if(Validator.isNotEmpty(elencoVarieta.get(key))) 
          {
            Vector<TipoVarietaVO> tipoVarieta = elencoVarieta.get(key);
            for(int d = 0; d < tipoVarieta.size(); d++) 
            {
              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(d);
              if(storicoUnitaArboreaVO.getIdVarieta() != null) 
              {
                if(storicoUnitaArboreaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
                {
                  htmpl.set("blkDati.blkElenco.descTipiVarieta", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
                  break;
                }
              }
            }
          }
        }
      }
    }
    
    
		// SUPERFICIE VITATA
		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea()))
			htmpl.set("blkDati.blkElenco.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));				
    
    
    //Sup. iscritta (ha)
    //htmpl.set("blkDati.blkElenco.superficieIscrivereAlbo",  StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getSuperficieDaIscrivereAlbo()));
    
    if(storicoUnitaArboreaVO.getTipoTipologiaVinoVO() != null)
    {
     
     //Tipologia Vino
     htmpl.set("blkDati.blkElenco.descrizioneTipologiaVino",  
       storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getDescrizione());
    }
    
    
    //Anno iscrizione albo
    htmpl.set("blkDati.blkElenco.annoIscrizioneAlbo", storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());
    //Matricola
    htmpl.set("blkDati.blkElenco.matricolaCCIAA", storicoUnitaArboreaVO.getMatricolaCCIAA());
    
    
		// GESTIONE ERRORI MULTIPLI
		if(elencoErroriMultipli != null && elencoErroriMultipli.size() > 0) 
    {
			ValidationErrors errors = (ValidationErrors)elencoErroriMultipli.elementAt(i);
			if(errors != null && errors.size() > 0) 
      {
				Iterator<String> iter = htmpl.getVariableIterator();
	 			while(iter.hasNext()) 
        {
	 				String key = (String)iter.next();
 			    if(key.startsWith("err_")) 
          {
 			    	String property = key.substring(4);
 			    	Iterator<ValidationError> errorIterator = errors.get(property);
 			    	if(errorIterator != null) 
            {
 			    		ValidationError error = (ValidationError)errorIterator.next();
 			    		htmpl.set("blkDati.blkElenco.err_"+property,
 			                      MessageFormat.format(htmlStringKO,
 			                      new Object[] {
 			                      pathErrori + "/"+ imko,
 			                      "'"+jssp.process(error.getMessage())+"'",
 			                      error.getMessage()}),
 			                      null);
 			    	}
 			    }
	 			}
			}
		}
    
              
	}
  
 
  //se cosi' mi comporto come se tutte lo fossero nel caso di CAA!!!   
  if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore())
  {
    //almeno una validata
    if(parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_TUTTO)
      && almenoUnaValidata)
    {
      //Non posso modificare i dati dell'idoneità perchè almeno uno è valorizzato!!
      if(almenoUnaIdoneitaValidata)
      {
        htmpl.set("bloccoIdoneita", "true",null);
        htmpl.set("blkDati.disabledIdoneita", "disabled=\"disabled\"",null);
      }
     
	    
    }
    else if((parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)
             && almenoUnaValidata)
           || parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA))
    {
      htmpl.set("bloccoIdoneita", "true",null);
      htmpl.set("blkDati.disabledIdoneita", "disabled=\"disabled\"",null);
    }
    
    if((parametroAccessoAllineaGis.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)
         && almenoUnaValidata)
      || parametroAccessoAllineaGis.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA))
    {
      htmpl.set("bloccoAllineaGis", "true", null);
      htmpl.set("blkDati.disabledAllineaUVGIS", "disabled=\"disabled\"",null); 
    }
    
    if((parametroOtherUVP.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)
         && almenoUnaValidata)
      || parametroOtherUVP.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA))
    {
      htmpl.set("bloccoOtherUVP", "true", null);
      htmpl.set("blkDati.disabledAnnotazione", "disabled=\"disabled\"",null);
      htmpl.set("blkDati.disabledVigna", "disabled=\"disabled\"",null);    
    }
    
   
    if((parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)
         && almenoUnaValidata)
      || parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA))
    {
      htmpl.set("bloccoVarieta", "true",null);
      htmpl.set("blkDati.disabledVarieta", "disabled=\"disabled\"",null);    
    }
    
    
    if((parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)
         && almenoUnaValidata)
      || parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA))
    {
      htmpl.set("bloccoAltriDati", "true",null);
      htmpl.set("blkDati.disabledAltriDati", "disabled=\"disabled\"",null);    
    }
    
    
    
    
    
  }
    
    
   
  
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  
  
  HtmplUtil.setErrors(htmpl, elencoErrori, request, application);

%>
<%= htmpl.text()%>

