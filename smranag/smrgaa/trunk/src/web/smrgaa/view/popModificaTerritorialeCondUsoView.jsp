<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.math.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popModificaTerritorialeCondUso.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	//  Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	UteVO uteVO = (UteVO)request.getAttribute("uteVO");
 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
 	ParticellaCertificataVO particellaCertificataVO = (ParticellaCertificataVO)request.getAttribute("particellaCertificataVO");
 	it.csi.solmr.dto.CodeDescription[] elencoTitoliPossesso = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoTitoliPossesso");
 	TipoIrrigazioneVO[] elencoIrrigazione = (TipoIrrigazioneVO[])request.getAttribute("elencoIrrigazione");
  TipoRotazioneColturaleVO[] elencoRotazioneColturale = (TipoRotazioneColturaleVO[])request.getAttribute("elencoRotazioneColturale");
  TipoTerrazzamentoVO[] elencoTerrazzamenti = (TipoTerrazzamentoVO[])request.getAttribute("elencoTerrazzamenti");
  Vector<TipoMetodoIrriguoVO> vMetodoIrriguo = (Vector<TipoMetodoIrriguoVO>)request.getAttribute("vMetodoIrriguo");
  Vector<TipoAreaVO> vTipoArea = (Vector<TipoAreaVO>)request.getAttribute("vTipoArea");
  
  
  Vector<TipoFaseAllevamentoVO> vTipoFaseAllev = (Vector<TipoFaseAllevamentoVO>)request.getAttribute("vTipoFaseAllev");
 	DocumentoVO[] elencoDocumenti = (DocumentoVO[])request.getAttribute("elencoDocumenti");
 	it.csi.solmr.dto.CodeDescription[] elencoCausaliModParticella = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoCausaliModParticella");
 	Vector<TipoPiantaConsociataVO> elencoPianteConsociate = (Vector<TipoPiantaConsociataVO>)request.getAttribute("elencoPianteConsociate");
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuolo");
 	Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazione = (Hashtable<Integer,Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazione");
  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUso = (Hashtable<Integer,Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettUso");
  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = (Hashtable<Integer,Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUso");
  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = (Hashtable<Integer,Vector<TipoVarietaVO>>)request.getAttribute("elencoVarieta");
  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSemina = (Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>)request.getAttribute("elencoPerSemina");
  Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>> elencoPerMantenim = (Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>>)request.getAttribute("elencoPerMantenim");
  Hashtable<Integer,String> elencoFaseAllev = (Hashtable<Integer,String>)request.getAttribute("elencoFaseAllev");
  Vector<TipoSeminaVO> vTipoSemina = (Vector<TipoSeminaVO>)request.getAttribute("vTipoSemina");
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuoloSecondario");
 	Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioneSecondario = (Hashtable<Integer,Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazioneSecondario");
  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUsoSecondario = (Hashtable<Integer,Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettUsoSecondario");
  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUsoSecondario = (Hashtable<Integer,Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUsoSecondario");
  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarietaSecondaria = (Hashtable<Integer,Vector<TipoVarietaVO>>)request.getAttribute("elencoVarietaSecondaria");
  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSeminaSecondario = (Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>)request.getAttribute("elencoPerSeminaSecondario");
 	TipoImpiantoVO[] elencoTipoImpianto = (TipoImpiantoVO[])request.getAttribute("elencoTipoImpianto");
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	Hashtable<Integer,ValidationErrors> erroriUtilizzi = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriUtilizzi");
 	Hashtable<Integer,ValidationErrors> erroriUtilizziEfa = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriUtilizziEfa");
 	//Hashtable<Integer,ValidationErrors> erroriDettaglioUso = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriDettaglioUso");
 	Hashtable<Integer,ValidationErrors> erroriPianteConsociate = (Hashtable<Integer,ValidationErrors>)request.getAttribute("erroriPianteConsociate");
 	String confermaSup = (String)request.getAttribute("confermaSup");
 	String isPiemontese = (String)request.getAttribute("isPiemontese");
 	String regimePopModificaTerritorialeCondUso = request.getParameter("regimePopModificaTerritorialeCondUso");
 	
 	//efa
 	Vector<TipoEfaVO> vTipoEfa = (Vector<TipoEfaVO>)request.getAttribute("vTipoEfa");
 	Hashtable<Integer, Vector<TipoUtilizzoVO>> elencoUtilizziEfa = (Hashtable<Integer, Vector<TipoUtilizzoVO>>)request.getAttribute("elencoUtilizziEfa");
 	Hashtable<Integer, Vector<TipoDestinazioneVO>> elencoDestinazioneEfa = (Hashtable<Integer, Vector<TipoDestinazioneVO>>)request.getAttribute("elencoDestinazioneEfa");
  Hashtable<Integer, Vector<TipoDettaglioUsoVO>> elencoDettaglioUsoEfa = (Hashtable<Integer, Vector<TipoDettaglioUsoVO>>)request.getAttribute("elencoDettaglioUsoEfa");
  Hashtable<Integer, Vector<TipoQualitaUsoVO>> elencoQualitaUsoEfa = (Hashtable<Integer, Vector<TipoQualitaUsoVO>>)request.getAttribute("elencoQualitaUsoEfa");
  Hashtable<Integer, Vector<TipoVarietaVO>> elencoVarietaEfa = (Hashtable<Integer, Vector<TipoVarietaVO>>)request.getAttribute("elencoVarietaEfa");
 	
 	String[] arrIdTipoEfa = request.getParameterValues("idTipoEfa");  
  String[] elencoUtilizziSelezionatiEfa = request.getParameterValues("idTipoUtilizzoEfa");
  String[] elencoDestinazioneSelezionateEfa = request.getParameterValues("idTipoDestinazioneEfa");
  String[] elencoDetUsoSelezionateEfa = request.getParameterValues("idTipoDettaglioUsoEfa");
  String[] elencoQualitaUsoSelezionateEfa = request.getParameterValues("idTipoQualitaUsoEfa");
  String[] elencoVarietaSelezionateEfa = request.getParameterValues("idVarietaEfa");
  String[] elencoAbbPonderazioneVarieta = request.getParameterValues("abbPonderazioneVarieta");
  String[] elencoValoreOriginale = request.getParameterValues("valoreOriginale");
  
  //mi servono per calcolare l'abb ponderazione deafult
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance(); 
  Long idTipoEfa = null;
  Long idTipoUtilizzoEfa = null;
  Long idTipoDestinazioneEfa = null;
  Long idTipoDettaglioUsoEfa = null;
  Long idTipoQualitaUsoEfa = null;
  Long idVarietaEfa = null;
 	
 	//numero della conduzione selezionato nell'elenco della modifica
 	String numero = request.getParameter("numero");
 	int numeroInt = 0;
 	if(Validator.isNotEmpty(numero))
 	{
 	  numeroInt = new Integer(numero).intValue();
 	}
 	
  
  //indice degli usi nell'elenco modifica
  /*Integer valorePartenza = (Integer)request.getAttribute("valorePartenza");
  int indiceUsiInt = 0;
  if(Validator.isNotEmpty(valorePartenza))
  {
    indiceUsiInt = valorePartenza.intValue();
  }*/
 	
 	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String popupError = "alert({0})";
	StringProcessor jssp = new JavaScriptStringProcessor();
 	
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	else 
  {
 		if(Validator.isNotEmpty(confermaSup)) 
 		{
 			htmpl.set("confermaSup", confermaSup);
 		}
    ConduzioneParticellaVO conduzioneParticellaVO = null;
    if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()))
    {
   		conduzioneParticellaVO = storicoParticellaVO.getElencoConduzioni()[0];
   		htmpl.set("idConduzioneParticella", conduzioneParticellaVO.getIdConduzioneParticella().toString());
    }
 		htmpl.newBlock("blkElencoDati");
 		
		// Gestisco gli errori relativi ai dati dell'operazione selezionata
 		if(errors != null && errors.size() > 0) 
 		{
 			Iterator iter = htmpl.getVariableIterator();
 			while(iter.hasNext()) 
 			{
 				String key = (String)iter.next();
 				if(key.equals("err_error")) 
 				{
 					String property = key.substring(4);
		    	Iterator errorIterator = errors.get(property);
		    	if(errorIterator != null) 
		    	{
		    		ValidationError error = (ValidationError)errorIterator.next();
		    		htmpl.set(key, MessageFormat.format(popupError, new Object[] {"'"+jssp.process(error.getMessage())+"'"}), null);
		    	}
 				}
 				else if(key.startsWith("err_")) 
 				{
		    	String property = key.substring(4);
		    	Iterator errorIterator = errors.get(property);
		    	if(errorIterator != null) 
		    	{
		    		ValidationError error = (ValidationError)errorIterator.next();
		    		htmpl.set("blkElencoDati.err_"+property,
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
 		
 		htmpl.set("blkElencoDati.descComuneUte", uteVO.getComuneUte().getDescom());
 		if(Validator.isNotEmpty(uteVO.getComuneUte().getSiglaProv())) 
 		{
 			htmpl.set("blkElencoDati.siglaProvinciaUte", "("+uteVO.getComuneUte().getSiglaProv()+")");
 		}
 		if(Validator.isNotEmpty(uteVO.getIndirizzo())) 
 		{
 			htmpl.set("blkElencoDati.indirizzoUte", " - "+uteVO.getIndirizzo());
 		}
 		if(particellaCertificataVO.isCertificata() && particellaCertificataVO.isUnivoca()) 
 		{
 			htmpl.set("blkElencoDati.descFonteCertificata", particellaCertificataVO.getFonteDato().getDescription());
 			htmpl.set("blkElencoDati.dataValidazioneFonteCatasto", "del "+DateUtils.formatDate(particellaCertificataVO.getDataValidazioneFonteCatasto()));
 			htmpl.set("blkElencoDati.supCatastaleCertificata", StringUtils.parseSuperficieField(particellaCertificataVO.getSupCatastaleCertificata()));
 		}
 		else if(!particellaCertificataVO.isCertificata()) {
 			htmpl.set("blkElencoDati.descFonteCertificata", SolmrConstants.FONTE_DATI_PARTICELLA_CERTIFICATA_DEFAULT);
 			htmpl.set("blkElencoDati.supCatastaleCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
 		}
 		else if(!particellaCertificataVO.isUnivoca()) {
 			htmpl.set("blkElencoDati.descFonteCertificata", SolmrConstants.FONTE_DATI_PARTICELLA_CERTIFICATA_DEFAULT);
 			htmpl.set("blkElencoDati.supCatastaleCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_UNIVOCA);
 		}
 		htmpl.set("blkElencoDati.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
 		if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia())) {
 			htmpl.set("blkElencoDati.siglaProvinciaParticella", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
 			htmpl.set("blkElencoDati.sezione", storicoParticellaVO.getSezione());
 		}
 		htmpl.set("blkElencoDati.foglio", storicoParticellaVO.getFoglio());
 		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
 			htmpl.set("blkElencoDati.particella", storicoParticellaVO.getParticella());
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
 			htmpl.set("blkElencoDati.subalterno", storicoParticellaVO.getSubalterno());
 		}
 		htmpl.set("blkElencoDati.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    htmpl.set("blkElencoDati.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
 		for(int i = 0; i < elencoTitoliPossesso.length; i++) 
    {
 			it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoTitoliPossesso[i];
 			htmpl.newBlock("blkElencoDati.blkTitoliPossesso");
 			htmpl.set("blkElencoDati.blkTitoliPossesso.idTitoloPossesso", code.getCode().toString());
 			htmpl.set("blkElencoDati.blkTitoliPossesso.descrizione", code.getCode().toString());
 			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
 			{
 				if(Validator.isNotEmpty(request.getParameter("idTitoloPossesso")) 
 				  && Long.decode(request.getParameter("idTitoloPossesso")).compareTo(Long.decode(code.getCode().toString())) == 0) 
 				{
 					htmpl.set("blkElencoDati.blkTitoliPossesso.selected", "selected=\"selected\"");
 				}
 			}
 			//Caso appena apro la popup
 			else 
 			{
 			  if(Validator.isNotEmpty(request.getParameterValues("idTitoloPossesso"))
 			   && Validator.isNotEmpty(request.getParameterValues("idTitoloPossesso")[numeroInt]) 
          && Long.decode(request.getParameterValues("idTitoloPossesso")[numeroInt]).compareTo(Long.decode(code.getCode().toString())) == 0) 
        {
          htmpl.set("blkElencoDati.blkTitoliPossesso.selected", "selected=\"selected\"");
        }
 			}
 		}
    
    
 		if(errors == null || errors.size() == 0) 
    {
 			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
      {
 				if(Validator.isNotEmpty(request.getParameter("supCondotta"))) 
        {
 					htmpl.set("blkElencoDati.supCondotta", request.getParameter("supCondotta"));
 				}
 			}
 			//prima volta che entro nella popup
 			else 
 			{
 			  if(Validator.isNotEmpty(request.getParameterValues("supCondotta"))
 			   && Validator.isNotEmpty(request.getParameterValues("supCondotta")[numeroInt])) 
        {
          htmpl.set("blkElencoDati.supCondotta", request.getParameterValues("supCondotta")[numeroInt]);
        }
 			}
 			
 			
 			
 			
 			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
      {
        if(Validator.isNotEmpty(request.getParameter("percentualePossesso"))) 
        {
          htmpl.set("blkElencoDati.percentualePossesso", request.getParameter("percentualePossesso"));
        }
      }
      else 
      {
        if(Validator.isNotEmpty(request.getParameterValues("percentualePossesso"))
         && Validator.isNotEmpty(request.getParameterValues("percentualePossesso")[numeroInt])) 
        {
          htmpl.set("blkElencoDati.percentualePossesso", request.getParameterValues("percentualePossesso")[numeroInt]);
        }   
      }
 			
 		}
 		else 
 		{
 			htmpl.set("blkElencoDati.supCondotta", request.getParameter("supCondotta"));
 			htmpl.set("blkElencoDati.percentualePossesso", request.getParameter("percentualePossesso"));        
 		}
    
    if(Validator.isNotEmpty(storicoParticellaVO.getPercentualePendenzaMedia()))
	  {
	    htmpl.set("blkElencoDati.descPendenzaMedia", Formatter.formatDouble2(storicoParticellaVO.getPercentualePendenzaMedia())+"%");       
	  }
	  if(Validator.isNotEmpty(storicoParticellaVO.getMetriAltitudineMedia())) 
	  {
	    htmpl.set("blkElencoDati.descAltitudineMedia", Formatter.formatDouble(storicoParticellaVO.getMetriAltitudineMedia())+"m");       
	  }		
 		
 		if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) {
      if(Validator.isNotEmpty(request.getParameter("flagIrrigabile"))) {
        htmpl.set("blkElencoDati.checkedIrrigabile", "checked=\"checked\"", null);
      }
    }
    else {
      if(Validator.isNotEmpty(storicoParticellaVO.getFlagIrrigabile()) && storicoParticellaVO.getFlagIrrigabile().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        htmpl.set("blkElencoDati.checkedIrrigabile", "checked=\"checked\"", null);
      }
    }
 		for(int i = 0; i < elencoIrrigazione.length; i++) 
 		{
 			TipoIrrigazioneVO tipoIrrigazioneVO = (TipoIrrigazioneVO)elencoIrrigazione[i];
 			htmpl.newBlock("blkElencoDati.blkTipiIrrigazione");
 			htmpl.set("blkElencoDati.blkTipiIrrigazione.idIrrigazione", tipoIrrigazioneVO.getIdIrrigazione().toString());
 			htmpl.set("blkElencoDati.blkTipiIrrigazione.descrizione", tipoIrrigazioneVO.getDescrizione());
 			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) {
 				if(Validator.isNotEmpty(request.getParameter("idIrrigazione"))) {
 					if(Long.decode(request.getParameter("idIrrigazione")).compareTo(tipoIrrigazioneVO.getIdIrrigazione()) == 0) {
 						htmpl.set("blkElencoDati.blkTipiIrrigazione.selected", "selected=\"selected\"", null);
 					}
 				}
 			}
 			else {
 				if(storicoParticellaVO.getIdIrrigazione() != null) {
 					if(storicoParticellaVO.getIdIrrigazione().compareTo(tipoIrrigazioneVO.getIdIrrigazione()) == 0) {
 						htmpl.set("blkElencoDati.blkTipiIrrigazione.selected", "selected=\"selected\"", null);
 					}
 				}
 			}
 		}
    
    for(int i = 0; i < elencoRotazioneColturale.length; i++) {
      TipoRotazioneColturaleVO tipoRotazioneColturaleVO = (TipoRotazioneColturaleVO)elencoRotazioneColturale[i];
      htmpl.newBlock("blkElencoDati.blkTipiRotazioneColturale");
      htmpl.set("blkElencoDati.blkTipiRotazioneColturale.idRotazioneColturale", tipoRotazioneColturaleVO.getIdRotazioneColturale().toString());
      htmpl.set("blkElencoDati.blkTipiRotazioneColturale.descrizione", tipoRotazioneColturaleVO.getDescrizione());
      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) {
        if(Validator.isNotEmpty(request.getParameter("idRotazioneColturale"))) {
          if(Long.decode(request.getParameter("idRotazioneColturale")).compareTo(tipoRotazioneColturaleVO.getIdRotazioneColturale()) == 0) {
            htmpl.set("blkElencoDati.blkTipiRotazioneColturale.selected", "selected=\"selected\"", null);
          }
        }
      }
      else {
        if(storicoParticellaVO.getIdRotazioneColturale() != null) {
          if(storicoParticellaVO.getIdRotazioneColturale().compareTo(tipoRotazioneColturaleVO.getIdRotazioneColturale()) == 0) {
            htmpl.set("blkElencoDati.blkTipiRotazioneColturale.selected", "selected=\"selected\"", null);
          }
        }
      }
    }
    for(int i = 0; i < elencoTerrazzamenti.length; i++) 
    {
      TipoTerrazzamentoVO tipoTerrazzamentoVO = (TipoTerrazzamentoVO)elencoTerrazzamenti[i];
      htmpl.newBlock("blkElencoDati.blkTipiTerrazzamenti");
      htmpl.set("blkElencoDati.blkTipiTerrazzamenti.idTerrazzamento", tipoTerrazzamentoVO.getIdTerrazzamento().toString());
      htmpl.set("blkElencoDati.blkTipiTerrazzamenti.descrizione", tipoTerrazzamentoVO.getDescrizione());
      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) {
        if(Validator.isNotEmpty(request.getParameter("idTerrazzamento"))) {
          if(Long.decode(request.getParameter("idTerrazzamento")).compareTo(tipoTerrazzamentoVO.getIdTerrazzamento()) == 0) {
            htmpl.set("blkElencoDati.blkTipiTerrazzamenti.selected", "selected=\"selected\"", null);
          }
        }
      }
      else {
        if(storicoParticellaVO.getIdTerrazzamento() != null) {
          if(storicoParticellaVO.getIdTerrazzamento().compareTo(tipoTerrazzamentoVO.getIdTerrazzamento()) == 0) {
            htmpl.set("blkElencoDati.blkTipiTerrazzamenti.selected", "selected=\"selected\"", null);
          }
        }
      }
    }
    
 		
 		if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) {
 			if(Validator.isNotEmpty(request.getParameter("flagCaptazionePozzi"))) {
 				htmpl.set("blkElencoDati.checkedCaptazionePozzi", "checked=\"checked\"", null);
 			}
 		}
 		else {
 			if(Validator.isNotEmpty(storicoParticellaVO.getFlagCaptazionePozzi()) && storicoParticellaVO.getFlagCaptazionePozzi().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
 				htmpl.set("blkElencoDati.checkedCaptazionePozzi", "checked=\"checked\"", null);
 			}
 		}
 
 		
 		if(Validator.isNotEmpty(vMetodoIrriguo))
	  {
	    for(int i=0;i<vMetodoIrriguo.size();i++) 
	    {
	      TipoMetodoIrriguoVO tipoMetodoIrriguoVO = vMetodoIrriguo.get(i);
	      htmpl.newBlock("blkElencoDati.blkTipiMetodoIrriguo");
	      htmpl.set("blkElencoDati.blkTipiMetodoIrriguo.idMetodoIrriguo", ""+tipoMetodoIrriguoVO.getIdMetodoIrriguo());
	      htmpl.set("blkElencoDati.blkTipiMetodoIrriguo.descrizione", tipoMetodoIrriguoVO.getDescrizioneMetodoIrriguo());
	      
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
	      {
	        if(Validator.isNotEmpty(request.getParameter("idMetodoIrriguo"))) 
	        {
            if(Long.decode(request.getParameter("idMetodoIrriguo")).compareTo(tipoMetodoIrriguoVO.getIdMetodoIrriguo()) == 0) 
            {
              htmpl.set("blkElencoDati.blkTipiMetodoIrriguo.selected", "selected=\"selected\"", null);
            }
          }
	      }
	      else
	      {
		      if(Validator.isNotEmpty(storicoParticellaVO.getIdMetodoIrriguo())) 
		      {
		        if(storicoParticellaVO.getIdMetodoIrriguo()
		          .compareTo(new Long(tipoMetodoIrriguoVO.getIdMetodoIrriguo())) == 0) 
		        {
		          htmpl.set("blkElencoDati.blkTipiMetodoIrriguo.selected", "selected=\"selected\"", null);
		        }
		      }
		    }       
	    }
	  }
	  
	  
	  String[] arrValoreArea1 = request.getParameterValues("valoreArea1");
	  String[] arrValoreArea2 = request.getParameterValues("valoreArea2");
	  int numValore = 0;
	  if(Validator.isNotEmpty(vTipoArea))
	  {
	    for(int i=0;i<vTipoArea.size();i++)
	    {
	      TipoAreaVO tipoAreaVO = vTipoArea.get(i);
	      TipoAreaVO valore = null;
	      if(storicoParticellaVO.getvValoriTipoArea() != null)
	        valore = storicoParticellaVO.getvValoriTipoArea().get(i); 
	        
	      boolean comboPiena = false;
	      //combo valorizzata se modifcabile
	      if(!Validator.isNotEmpty(isPiemontese) || "S".equalsIgnoreCase(tipoAreaVO.getFlagAreaModificabile()))
	      {
	        comboPiena = true;
	      }
	      //o se presnete valore su db
	      else if(Validator.isNotEmpty(valore) && Validator.isNotEmpty(valore.getvTipoValoreArea())
	        && Validator.isNotEmpty(valore.getvTipoValoreArea().get(0).getValore()))
	      {
	        comboPiena = true;
	      }
	      if(i%2 == 0)
	      {
	        htmpl.newBlock("blkElencoDati.blkRiga");
	        htmpl.set("blkElencoDati.blkRiga.flagEsclusivoFoglio1", tipoAreaVO.getFlagEsclusivoFoglio());
	        htmpl.set("blkElencoDati.blkRiga.desc1", ""+tipoAreaVO.getDescrizione());
	        htmpl.set("blkElencoDati.blkRiga.idTipoArea1", ""+tipoAreaVO.getIdTipoArea());
	        String valore1 = "";
	        String valore1First = tipoAreaVO.getvTipoValoreArea().get(0).getValore();
	        
	        if(comboPiena)
	        {
	          for(int j=0;j<tipoAreaVO.getvTipoValoreArea().size();j++)
	          {
	            htmpl.newBlock("blkElencoDati.blkRiga.blkTipiValoreArea1");
	            TipoValoreAreaVO tipoValoreAreaVO = tipoAreaVO.getvTipoValoreArea().get(j);
	            htmpl.set("blkElencoDati.blkRiga.blkTipiValoreArea1.descrizione",tipoValoreAreaVO.getDescrizione());
	            htmpl.set("blkElencoDati.blkRiga.blkTipiValoreArea1.valoreArea1",tipoValoreAreaVO.getValore());
	            if(Validator.isEmpty(regimePopModificaTerritorialeCondUso))
	            {
	              if((valore != null) && (valore.getvTipoValoreArea() != null)
	                && (valore.getvTipoValoreArea().get(0).getValore() != null))
	              {
	                if(tipoValoreAreaVO.getValore().equalsIgnoreCase(valore.getvTipoValoreArea().get(0).getValore())) 
	                {
	                  htmpl.set("blkElencoDati.blkRiga.blkTipiValoreArea1.selected", "selected=\"selected\"", null);
	                  valore1 = tipoValoreAreaVO.getValore();
	                }          
	              }
	            }
	            else
	            {
	              if(Validator.isNotEmpty(arrValoreArea1[numValore]))
	              {
	                if(tipoValoreAreaVO.getValore().equalsIgnoreCase(arrValoreArea1[numValore])) 
	                {
	                  htmpl.set("blkElencoDati.blkRiga.blkTipiValoreArea1.selected", "selected=\"selected\"", null);
	                  valore1 = arrValoreArea1[numValore];
	                }  
	              }         
	            }           
	          }
	        }
	        
	        if(!(!Validator.isNotEmpty(isPiemontese) || "S".equalsIgnoreCase(tipoAreaVO.getFlagAreaModificabile())))
	        {
	          htmpl.set("blkElencoDati.blkRiga.disabledValore1", "disabled=\"true\"", null);
	          htmpl.newBlock("blkElencoDati.blkRiga.blkHiddenValoreArea1");
	          if(comboPiena)
	          {
	            if(Validator.isNotEmpty(valore1))
	              htmpl.set("blkElencoDati.blkRiga.blkHiddenValoreArea1.valoreArea1", valore1);
	            else
	              htmpl.set("blkElencoDati.blkRiga.blkHiddenValoreArea1.valoreArea1", valore1First);
	          }
	        }
	      }
	      else
	      {
	        htmpl.set("blkElencoDati.blkRiga.flagEsclusivoFoglio2", tipoAreaVO.getFlagEsclusivoFoglio());
	        htmpl.set("blkElencoDati.blkRiga.desc2", ""+tipoAreaVO.getDescrizione());
	        htmpl.set("blkElencoDati.blkRiga.idTipoArea2", ""+tipoAreaVO.getIdTipoArea());
	        String valore2 = "";
	        String valore2First = tipoAreaVO.getvTipoValoreArea().get(0).getValore();
	        
	        if(comboPiena)
	        {
	          for(int j=0;j<tipoAreaVO.getvTipoValoreArea().size();j++)
	          {
	            htmpl.newBlock("blkElencoDati.blkRiga.blkTipiValoreArea2");
	            TipoValoreAreaVO tipoValoreAreaVO = tipoAreaVO.getvTipoValoreArea().get(j);
	            htmpl.set("blkElencoDati.blkRiga.blkTipiValoreArea2.descrizione",tipoValoreAreaVO.getDescrizione());
	            htmpl.set("blkElencoDati.blkRiga.blkTipiValoreArea2.valoreArea2",tipoValoreAreaVO.getValore());
	            if(Validator.isEmpty(regimePopModificaTerritorialeCondUso))
	            {
	              if(Validator.isNotEmpty(valore) && Validator.isNotEmpty(valore.getvTipoValoreArea())
	                && Validator.isNotEmpty(valore.getvTipoValoreArea().get(0).getValore()))
	              {
	                if(tipoValoreAreaVO.getValore().equalsIgnoreCase(valore.getvTipoValoreArea().get(0).getValore())) 
	                {
	                  htmpl.set("blkElencoDati.blkRiga.blkTipiValoreArea2.selected", "selected=\"selected\"", null);
	                  valore2 = tipoValoreAreaVO.getValore();
	                }          
	              }
	            }
	            else
	            {
	              if(Validator.isNotEmpty(arrValoreArea2[numValore]))
	              {
	                if(tipoValoreAreaVO.getValore().equalsIgnoreCase(arrValoreArea2[numValore])) 
	                {
	                  htmpl.set("blkElencoDati.blkRiga.blkTipiValoreArea2.selected", "selected=\"selected\"", null);
	                  valore2 = arrValoreArea2[numValore];
	                }  
	              }  
	            }
	          }
	        }
	        
	        if(!(!Validator.isNotEmpty(isPiemontese) || "S".equalsIgnoreCase(tipoAreaVO.getFlagAreaModificabile())))
	        {
	          htmpl.set("blkElencoDati.blkRiga.disabledValore2", "disabled=\"true\"", null);
	          htmpl.newBlock("blkElencoDati.blkRiga.blkHiddenValoreArea2");
	          if(comboPiena)
	          {
	            if(Validator.isNotEmpty(valore2))
	              htmpl.set("blkElencoDati.blkRiga.blkHiddenValoreArea2.valoreArea2", valore2);
	            else
	              htmpl.set("blkElencoDati.blkRiga.blkHiddenValoreArea2.valoreArea2", valore2First);
	          }
	        }
	        numValore++;
	      }      
	    }
	  
	  }
 		
 		
 		
 		String descrizione = null;
 		for(int i = 0; i < elencoDocumenti.length; i++) 
 		{
 			DocumentoVO documentoVO = (DocumentoVO)elencoDocumenti[i];
 			//Faccio vedere solo quelli che non sono istanza di riesame
 			if(!"S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
 			{
	 			htmpl.newBlock("blkElencoDati.blkElencoDocumenti");
	 			htmpl.set("blkElencoDati.blkElencoDocumenti.idDocumentoProtocollato", documentoVO.getIdDocumento().toString());
	 			descrizione = documentoVO.getTipoDocumentoVO().getDescrizione();
				if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) {
					descrizione += " Prot. "+StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo());
				}
				if(documentoVO.getDataProtocollo() != null) {
					descrizione += " del "+DateUtils.formatDate(documentoVO.getDataProtocollo());
				}
	 			htmpl.set("blkElencoDati.blkElencoDocumenti.descrizione", descrizione);
	 			if(Validator.isNotEmpty(request.getParameter("idDocumentoProtocollato")) && Long.decode(request.getParameter("idDocumentoProtocollato")).compareTo(documentoVO.getIdDocumento()) == 0) {
	 				htmpl.set("blkElencoDati.blkElencoDocumenti.selected", "selected=\"selected\"", null); 				
	 			}
	 			else {
	 				if(storicoParticellaVO.getIdDocumentoProtocollato() != null && storicoParticellaVO.getIdDocumentoProtocollato().compareTo(documentoVO.getIdDocumento()) == 0) {
	 					htmpl.set("blkElencoDati.blkElencoDocumenti.selected", "selected=\"selected\"", null);
	 				}
	 			}
	 	  }
 		}
 		
 		for(int i = 0; i < elencoCausaliModParticella.length; i++) 
 		{
 			it.csi.solmr.dto.CodeDescription code = (it.csi.solmr.dto.CodeDescription)elencoCausaliModParticella[i];
 			htmpl.newBlock("blkElencoDati.blkCausaleModParticella");
 			htmpl.set("blkElencoDati.blkCausaleModParticella.idCausaleModParticella", code.getCode().toString());
 			htmpl.set("blkElencoDati.blkCausaleModParticella.descrizione", code.getDescription());
 			if(Validator.isNotEmpty(request.getParameter("idCausaleModParticella")) && Long.decode(request.getParameter("idCausaleModParticella")).compareTo(Long.decode(code.getCode().toString())) == 0) {
 				htmpl.set("blkElencoDati.blkCausaleModParticella.selected", "selected=\"selected\"", null);
 			}
 			else {
 				if(storicoParticellaVO.getIdCausaleModParticella() != null && storicoParticellaVO.getIdCausaleModParticella().compareTo(Long.decode(code.getCode().toString())) == 0) {
 					htmpl.set("blkElencoDati.blkCausaleModParticella.selected", "selected=\"selected\"", null);
 				}
 			}
 		}
 		if(Validator.isNotEmpty(request.getParameter("motivoModifica"))) {
 			htmpl.set("blkElencoDati.motivoModifica", request.getParameter("motivoModifica"));
 		}
 		else {
 			if(Validator.isNotEmpty(storicoParticellaVO.getMotivoModifica())) {
 				htmpl.set("blkElencoDati.motivoModifica", storicoParticellaVO.getMotivoModifica());
 			}
 		}
 		
 		
 	
 		
 		//Utilizzi normali		
 		Vector<UtilizzoParticellaVO> vUtilizziVO = (Vector<UtilizzoParticellaVO>)request.getAttribute("vUtilizziVO");
 		int numeroUtilizzi = 0;
 		if(Validator.isNotEmpty(vUtilizziVO)
 		  && Validator.isEmpty(regimePopModificaTerritorialeCondUso))
 		{
 		  numeroUtilizzi = vUtilizziVO.size();
 		}
 		//conto il numero di idUtilizzi presenti
 		else
 		{
 		  if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")))
 		    numeroUtilizzi = request.getParameterValues("idTipoUtilizzo").length;
 		}
 		//if(elencoUtilizzi != null && elencoUtilizzi.length > 0) 
 		//{
 		for(int i = 0; i < numeroUtilizzi; i++) 
 		{
 			
 			//prima volta che entro è valorizzato!!!
 			UtilizzoParticellaVO utilizzoParticellaVO = null;
 			if(Validator.isNotEmpty(vUtilizziVO)
 			  && Validator.isEmpty(regimePopModificaTerritorialeCondUso))
 			{
 			  utilizzoParticellaVO = vUtilizziVO.get(i);
 			}
 				
		  htmpl.newBlock("blkElencoDati.blkElencoUtilizzi");
			htmpl.set("blkElencoDati.blkElencoUtilizzi.numeroUtilizzi", String.valueOf(i));
			htmpl.set("blkElencoDati.blkElencoUtilizzi.contatore", String.valueOf(i));
			htmpl.set("blkElencoDati.blkElencoUtilizzi.idConduzione", conduzioneParticellaVO.getIdConduzioneParticella().toString());
			
			for(int a = 0; a < elencoTipiUsoSuolo.size(); a++) 
			{
				TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo.elementAt(a);
				htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuolo");
				htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
				htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuolo.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
				if(tipoUtilizzoVO.getDescrizione().length() > 20) 
				{
					descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
				}
				else 
				{
					descrizione = tipoUtilizzoVO.getDescrizione();
				}
				htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
				if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
				{
					if(request.getParameterValues("idTipoUtilizzo") != null && i < request.getParameterValues("idTipoUtilizzo").length) 
					{
						if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i]) 
						  && Long.decode(request.getParameterValues("idTipoUtilizzo")[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
						{
							htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
						}
					}
				}
				//prima volta che entro...
				else 
				{
					if(utilizzoParticellaVO != null) 
	        {
	          if(Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()) 
	            && utilizzoParticellaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
	          {
						  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuolo.selected", "selected=\"selected\"", null);
						}
					}
				}
			}
			
			
			if(elencoDestinazione != null && elencoDestinazione.size() > 0)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoUtilizzo") != null)
	          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
	        {
	          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazione.get(new Integer(i));
	          if(vDestinazione != null)
	          {
	            for(int d = 0; d < vDestinazione.size(); d++) 
	            {
	              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazione");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
	              descrizione = null;
	              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
	              {
	                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
	              }
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
	               
	              if(request.getParameterValues("idTipoDestinazione") != null && i < request.getParameterValues("idTipoDestinazione").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")[i]) 
	                  && request.getParameterValues("idTipoDestinazione")[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazione.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDestinazione");
			      }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDestinazione");
		      }
	      }
	      else
	      {
	        Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazione.get(i);
	        if((vDestinazione != null)
	          && (vDestinazione.size() > 0))
	        {
	          for(int d = 0; d < vDestinazione.size(); d++) 
	          {
	            TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazione");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
	            descrizione = null;
	            if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
	            {
	              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
	            }
	            else 
	            {
	              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
	            }
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
	            if(utilizzoParticellaVO != null               
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDestinazione()))
	            {
	              if(utilizzoParticellaVO.getIdTipoDestinazione().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazione.selected", "selected=\"selected\"",null);
	              }
	            }
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDestinazione");
		      }
	      }         
	    }
	    else
	    {
	      htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDestinazione");
	    }
	    
	    
	    if(elencoDettUso != null && elencoDettUso.size() > 0)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoDestinazione") != null)
	          && (request.getParameterValues("idTipoDestinazione")[i] != null))
	        {
	          Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUso.get(new Integer(i));
	          if(vDettaglioUso != null)
	          {
	            for(int d = 0; d < vDettaglioUso.size(); d++) 
	            {
	              TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUso");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
	              descrizione = null;
	              if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
	              {
	                descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoDettaglioUsoVO.getDescrizione();
	              }
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
	               
	              if(request.getParameterValues("idTipoDettaglioUso") != null && i < request.getParameterValues("idTipoDettaglioUso").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i]) 
	                  && request.getParameterValues("idTipoDettaglioUso")[i].equalsIgnoreCase(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUso.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDettaglioUso");
			      }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDettaglioUso");
		      }
	      }
	      else
	      {
	        Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUso.get(i);
	        if((vDettaglioUso != null)
	          && (vDettaglioUso.size() > 0))
	        {
	          for(int d = 0; d < vDettaglioUso.size(); d++) 
	          {
	            TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUso");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
	            descrizione = null;
	            if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
	            {
	              descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
	            }
	            else 
	            {
	              descrizione = tipoDettaglioUsoVO.getDescrizione();
	            }
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDettaglioUso()))
	            {
	              if(utilizzoParticellaVO.getIdTipoDettaglioUso().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUso.selected", "selected=\"selected\"",null);
	              }
	            }
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDettaglioUso");
		      }
	      }         
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDettaglioUso");
      }
	    
	    
	    if(elencoQualitaUso != null && elencoQualitaUso.size() > 0)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoDettaglioUso") != null)
	          && (request.getParameterValues("idTipoDettaglioUso")[i] != null))
	        {
	          Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUso.get(new Integer(i));
	          if(vQualitaUso != null)
	          {
	            for(int d = 0; d < vQualitaUso.size(); d++) 
	            {
	              TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUso");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
	              descrizione = null;
	              if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
	              {
	                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
	              }
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
	               
	              if(request.getParameterValues("idTipoQualitaUso") != null && i < request.getParameterValues("idTipoQualitaUso").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")[i]) 
	                  && request.getParameterValues("idTipoQualitaUso")[i].equalsIgnoreCase(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUso.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiQualitaUso");
			      }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiQualitaUso");
		      }
	      }
	      else
	      {
	        Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUso.get(i);
	        if((vQualitaUso != null)
	          && (vQualitaUso.size() > 0))
	        {
	          for(int d = 0; d < vQualitaUso.size(); d++) 
	          {
	            TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUso");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
	            descrizione = null;
	            if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
	            {
	              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
	            }
	            else 
	            {
	              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
	            }
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoQualitaUso()))
	            {
	              if(utilizzoParticellaVO.getIdTipoQualitaUso().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUso.selected", "selected=\"selected\"",null);
	              }
	            }
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiQualitaUso");
		      }
	      }         
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiQualitaUso");
      }
	    
	    // Carico la combo della varietà solo se l'utente ha selezionato il tipo
	    // uso suolo corrispondente
	    if(elencoVarieta != null && elencoVarieta.size() > 0) 
	    {  
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoQualitaUso") != null)
	          && (request.getParameterValues("idTipoQualitaUso")[i] != null))
	        {
	          Vector<TipoVarietaVO> tipoVarieta = (Vector<TipoVarietaVO>)elencoVarieta.get(new Integer(i));
	          if(tipoVarieta != null)
	          {
	            for(int l = 0; l < tipoVarieta.size(); l++) 
	            {
	              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(l);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiVarieta");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
	              if(tipoVarietaVO.getDescrizione().length() > 20) 
	              {
	                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoVarietaVO.getDescrizione();
	              }
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
	             
	              if(request.getParameterValues("idVarieta") != null && i < request.getParameterValues("idVarieta").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idVarieta")[i]) 
	                  && request.getParameterValues("idVarieta")[i].equalsIgnoreCase(tipoVarietaVO.getIdVarieta().toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarieta.selected", "selected=\"selected\"", null);
	                }
	              }               
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiVarieta");
			      } 
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiVarieta");
		      } 
	      }
	      //prima volta che entro nella popup.
	      else
	      {         
	        Vector<TipoVarietaVO> tipoVarieta = (Vector<TipoVarietaVO>)elencoVarieta.get(i);
	        if(tipoVarieta != null)
	        {
	          for(int l = 0; l < tipoVarieta.size(); l++) 
	          {
	            TipoVarietaVO tipoVarietaVO = tipoVarieta.get(l);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiVarieta");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
	            if(tipoVarietaVO.getDescrizione().length() > 20) 
	            {
	              descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
	            }
	            else 
	            {
	              descrizione = tipoVarietaVO.getDescrizione();
	            }
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
	            
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdVarieta())) 
	            {
	              if(utilizzoParticellaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarieta.selected", "selected=\"selected\"", null);
	              }
	            }             
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiVarieta");
		      }            
	      }       
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiVarieta");
      } 
 				
 				
			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
			{
				if(request.getParameterValues("supUtilizzata") != null 
				  && i <  request.getParameterValues("supUtilizzata").length 
				  && Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[i])) 
				{
					htmpl.set("blkElencoDati.blkElencoUtilizzi.supUtilizzata", request.getParameterValues("supUtilizzata")[i]);
				}
			}
			else
			{
			  if((utilizzoParticellaVO != null)
          && Validator.isNotEmpty(utilizzoParticellaVO.getSuperficieUtilizzata())) 
			  {
					htmpl.set("blkElencoDati.blkElencoUtilizzi.supUtilizzata", utilizzoParticellaVO.getSuperficieUtilizzata());
				}	
			}
			
			
			
			// Carico la combo del perido semina solo se l'utente ha selezionato il tipo
	    // uso suolo corrispondente
	    if(elencoPerSemina != null && elencoPerSemina.size() > 0) 
	    {  
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoUtilizzo") != null)
	          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
	        {
	          Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSemina.get(new Integer(i));
	          if(vTipoPeriodoSemina != null)
	          {
	            for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
	            {
	              TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSemina");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSemina.idTipoPeriodoSemina", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSemina.descrizione", tipoPeriodoSeminaVO.getDescrizione());
	             
	              if(request.getParameterValues("idTipoPeriodoSemina") != null && i < request.getParameterValues("idTipoPeriodoSemina").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSemina")[i]) 
	                  && request.getParameterValues("idTipoPeriodoSemina")[i].equalsIgnoreCase(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSemina.selected", "selected=\"selected\"", null);
	                }
	              }               
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPeriodoSemina");
			      } 
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPeriodoSemina");
		      } 
	      }
	      //prima volta che entro nella popup.
	      else
	      {         
	        Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSemina.get(i);
	        if(vTipoPeriodoSemina != null)
	        {
	          for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
	          {
	            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSemina");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSemina.idTipoPeriodoSemina", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSemina.descrizione", tipoPeriodoSeminaVO.getDescrizione());
	            
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoPeriodoSemina())) 
	            {
	              if(utilizzoParticellaVO.getIdTipoPeriodoSemina().compareTo(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSemina.selected", "selected=\"selected\"", null);
	              }
	            }             
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPeriodoSemina");
		      }            
	      }       
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPeriodoSemina");
      } 
	    
	    if(elencoPerMantenim != null && elencoPerMantenim.size() > 0) 
	    {  
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoUtilizzo") != null)
	          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
	        {
	          Vector<TipoPraticaMantenimentoVO> vTipoPraticMantenim = (Vector<TipoPraticaMantenimentoVO>)elencoPerMantenim.get(new Integer(i));
	          if(vTipoPraticMantenim != null)
	          {
	            for(int l = 0; l < vTipoPraticMantenim.size(); l++) 
	            {
	              TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = vTipoPraticMantenim.get(l);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoPraticaMantenimento");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPraticaMantenimento.idPraticaMantenimento", ""+tipoPraticaMantenimentoVO.getIdPraticaMantenimento());
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPraticaMantenimento.descrizione", tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim());
	             
	              if(request.getParameterValues("idPraticaMantenimento") != null && i < request.getParameterValues("idPraticaMantenimento").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idPraticaMantenimento")[i]) 
	                  && request.getParameterValues("idPraticaMantenimento")[i].equalsIgnoreCase(new Long(tipoPraticaMantenimentoVO.getIdPraticaMantenimento()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPraticaMantenimento.selected", "selected=\"selected\"", null);
	                }
	              }               
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
			      }  
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
		      }  
	      }
	      //prima volta che entro nella popup.
	      else
	      {         
	        Vector<TipoPraticaMantenimentoVO> vTipoPraticMantenim = (Vector<TipoPraticaMantenimentoVO>)elencoPerMantenim.get(new Integer(i));
	        if(vTipoPraticMantenim != null)
	        {
	          for(int l = 0; l < vTipoPraticMantenim.size(); l++) 
	          {
	            TipoPraticaMantenimentoVO tipoPraticaMantenimentoVO = vTipoPraticMantenim.get(l);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoPraticaMantenimento");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPraticaMantenimento.idPraticaMantenimento", ""+tipoPraticaMantenimentoVO.getIdPraticaMantenimento());
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPraticaMantenimento.descrizione", tipoPraticaMantenimentoVO.getDescrizionePraticaMantenim());
	            
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdPraticaMantenimento())) 
	            {
	              if(utilizzoParticellaVO.getIdPraticaMantenimento().compareTo(new Long(tipoPraticaMantenimentoVO.getIdPraticaMantenimento())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPraticaMantenimento.selected", "selected=\"selected\"", null);
	              }
	            }             
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
		      }             
	      }       
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPraticaMantenimento");
      }  
	    
	    
	    
	    if(elencoFaseAllev != null && elencoFaseAllev.size() > 0) 
	    {  
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoUtilizzo") != null)
	          && (request.getParameterValues("idTipoUtilizzo")[i] != null))
	        {
	          if("S".equalsIgnoreCase(elencoFaseAllev.get(new Integer(i))))
	          {
	            if(vTipoFaseAllev != null)
	            {
	              for(int l = 0; l < vTipoFaseAllev.size(); l++) 
	              {
	                TipoFaseAllevamentoVO tipoFaseAllevamentoVO = vTipoFaseAllev.get(l);
	                htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoFaseAllevamento");
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoFaseAllevamento.idFaseAllevamento", ""+tipoFaseAllevamentoVO.getIdFaseAllevamento());
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoFaseAllevamento.descrizione", tipoFaseAllevamentoVO.getDescrizioneFaseAllevamento());
	               
	                if(request.getParameterValues("idFaseAllevamento") != null && i < request.getParameterValues("idFaseAllevamento").length) 
	                {
	                  if(Validator.isNotEmpty(request.getParameterValues("idFaseAllevamento")[i]) 
	                    && request.getParameterValues("idFaseAllevamento")[i].equalsIgnoreCase(new Long(tipoFaseAllevamentoVO.getIdFaseAllevamento()).toString())) 
	                  {
	                    htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoFaseAllevamento.selected", "selected=\"selected\"", null);
	                  }
	                }               
	              }
	            }
	            else
		          {
		            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento");
		            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
		            htmpl.set("blkElencoDati.blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
		          } 
	          }
	          else
	          {
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
	          }
	        }
	        else
          {
            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento");
            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
            htmpl.set("blkElencoDati.blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
          } 
	      }
	      //prima volta che entro nella popup.
	      else
	      {         
	        if("S".equalsIgnoreCase(elencoFaseAllev.get(new Integer(i))))
	        {
	          if(vTipoFaseAllev != null)
	          {
	            for(int l = 0; l < vTipoFaseAllev.size(); l++) 
	            {
	              TipoFaseAllevamentoVO tipoFaseAllevamentoVO = vTipoFaseAllev.get(l);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoFaseAllevamento");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoFaseAllevamento.idFaseAllevamento", ""+tipoFaseAllevamentoVO.getIdFaseAllevamento());
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoFaseAllevamento.descrizione", tipoFaseAllevamentoVO.getDescrizioneFaseAllevamento());
	              
	              if((utilizzoParticellaVO != null)
	                && Validator.isNotEmpty(utilizzoParticellaVO.getIdFaseAllevamento())) 
	              {
	                if(utilizzoParticellaVO.getIdFaseAllevamento().compareTo(new Long(tipoFaseAllevamentoVO.getIdFaseAllevamento())) == 0) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoFaseAllevamento.selected", "selected=\"selected\"", null);
	                }
	              }             
	            }
	          }
	          else
	          {
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
	          } 
	        }
	        else
	        {
	          htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento");
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
	        }           
	      }       
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento");
        htmpl.set("blkElencoDati.blkElencoUtilizzi.blkHiddenFaseAllevamento.idFaseAllevamento", "");
        htmpl.set("blkElencoDati.blkElencoUtilizzi.disabledFaseAlleavamento", "disabled=\"true\"", null);          
      }               
	     
	    
	    //abilita semina
	    boolean abilitaSemina = false;
	    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	    {
	      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo"))
	          && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i]))
	      {
	        abilitaSemina = true;
	      }
	    }
	    else
	    {
	      if((utilizzoParticellaVO != null)
	        && Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzo()))
	      {
	        abilitaSemina = true;
	      } 
	    }
	    
	    
	    if(abilitaSemina)
	    {
	      for(int e=0;e<vTipoSemina.size(); e++) 
	      {
	        TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
	        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoSemina");
	        htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoSemina.idTipoSemina", ""+tipoSeminaVO.getIdTipoSemina());
	        htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoSemina.descrizione", tipoSeminaVO.getDescrizioneSemina());
	        if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
	        {
	          if(request.getParameterValues("idTipoSemina") != null 
	            && i < request.getParameterValues("idTipoSemina").length) 
	          {
	            if(Validator.isNotEmpty(request.getParameterValues("idTipoSemina")[i]) 
	              && Long.decode(request.getParameterValues("idTipoSemina")[i]).compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0) 
	            {
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoSemina.selected", "selected=\"selected\"", null);
	            }
	          }         
	        }
	        else 
	        {
	          if((utilizzoParticellaVO != null)
	            && Validator.isNotEmpty(utilizzoParticellaVO.getIdSemina())
	            && utilizzoParticellaVO.getIdSemina().compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0)
	          {
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoSemina.selected", "selected=\"selected\"", null);
	          }
	        }
	      }
	    }
	    else
	    {
	      htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoSemina");
	    } 
	    
	    
	    if(abilitaSemina)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
	      {
	        if(request.getParameterValues("dataInizioDestinazione") != null 
	          && (i < request.getParameterValues("dataInizioDestinazione").length) 
	          && Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazione")[i]))
	        { 
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.dataInizioDestinazione", request.getParameterValues("dataInizioDestinazione")[i]);
	        }
	        
	        if(request.getParameterValues("dataFineDestinazione") != null 
	          && (i < request.getParameterValues("dataFineDestinazione").length) 
	          && Validator.isNotEmpty(request.getParameterValues("dataFineDestinazione")[i]))
	        { 
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.dataFineDestinazione", request.getParameterValues("dataFineDestinazione")[i]);
	        }
	      }
	      else
	      {
	        if((utilizzoParticellaVO != null)
	          && Validator.isNotEmpty(utilizzoParticellaVO.getDataInizioDestinazioneStr()))
	        { 
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.dataInizioDestinazione", vUtilizziVO.get(i).getDataInizioDestinazioneStr());
	        }
	        
	        if((utilizzoParticellaVO != null)
	          && Validator.isNotEmpty(utilizzoParticellaVO.getDataFineDestinazioneStr()))
	        { 
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.dataFineDestinazione",  vUtilizziVO.get(i).getDataFineDestinazioneStr());
	        }     
	      }
	    }
	    else
	    {
	      htmpl.set("blkElencoDati.blkElencoUtilizzi.readOnlyDataInizioDestinazione", "readOnly=\"true\"", null);
	      htmpl.set("blkElencoDati.blkElencoUtilizzi.readOnlyDataFineDestinazione", "readOnly=\"true\"", null);
	    }                   
				
						
				
			if(elencoTipiUsoSuoloSecondario != null && elencoTipiUsoSuoloSecondario.size() > 0) 
			{
			  for(int c = 0; c < elencoTipiUsoSuoloSecondario.size(); c++) 
			  {
				  TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuoloSecondario.elementAt(c);
				  htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuoloSecondario");
					htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuoloSecondario.idTipoUtilizzoSecondario", tipoUtilizzoVO.getIdUtilizzo().toString());
					htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuoloSecondario.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
					if(tipoUtilizzoVO.getDescrizione().length() > 20) 
					{
						descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
					}
					else 
					{
						descrizione = tipoUtilizzoVO.getDescrizione();
					}
					htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuoloSecondario.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
					if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
					{
						if(request.getParameterValues("idTipoUtilizzoSecondario") != null 
						  && i < request.getParameterValues("idTipoUtilizzoSecondario").length) 
						{
							if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i]) 
							  && Long.decode(request.getParameterValues("idTipoUtilizzoSecondario")[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) 
							{
								htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuoloSecondario.selected", "selected=\"selected\"", null);
							}
						}
					}
					else 
					{
	          if((utilizzoParticellaVO != null) 
	            && Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario()) 
	            && (utilizzoParticellaVO.getIdUtilizzoSecondario().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0)) 
	          {
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiUsoSuoloSecondario.selected", "selected=\"selected\"", null);
	          }	          
					}
			  }
		  }
		  
		  
		  if(elencoDestinazioneSecondario != null && elencoDestinazioneSecondario.size() > 0)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoUtilizzoSecondario") != null)
	          && (request.getParameterValues("idTipoUtilizzoSecondario")[i] != null))
	        {
	          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneSecondario.get(new Integer(i));
	          if(vDestinazione != null)
	          {
	            for(int d = 0; d < vDestinazione.size(); d++) 
	            {
	              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazioneSecondario");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazioneSecondario.idTipoDestinazioneSecondario", ""+tipoDestinazioneVO.getIdTipoDestinazione());
	              descrizione = null;
	              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
	              {
	                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
	              }
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazioneSecondario.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
	               
	              if(request.getParameterValues("idTipoDestinazioneSecondario") != null && i < request.getParameterValues("idTipoDestinazioneSecondario").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazioneSecondario")[i]) 
	                  && request.getParameterValues("idTipoDestinazioneSecondario")[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazioneSecondario.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
			      }
				  }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
		      }
	      }
	      else
	      {
	        Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneSecondario.get(i);
	        if((vDestinazione != null)
	          && (vDestinazione.size() > 0))
	        {
	          for(int d = 0; d < vDestinazione.size(); d++) 
	          {
	            TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazioneSecondario");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazioneSecondario.idTipoDestinazioneSecondario", ""+tipoDestinazioneVO.getIdTipoDestinazione());
	            descrizione = null;
	            if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
	            {
	              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
	            }
	            else 
	            {
	              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
	            }
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazioneSecondario.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDestinazioneSecondario()))
	            {
	              if(utilizzoParticellaVO.getIdTipoDestinazioneSecondario().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDestinazioneSecondario.selected", "selected=\"selected\"",null);
	              }
	            }
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
		      }
	      }         
	    }
	    else
	    {
	      htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDestinazioneSecondario");
	    }
    
	    if(elencoDettUsoSecondario != null && elencoDettUsoSecondario.size() > 0)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoDestinazioneSecondario") != null)
	          && (request.getParameterValues("idTipoDestinazioneSecondario")[i] != null))
	        {
	          Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUsoSecondario.get(new Integer(i));
	          if(vDettaglioUso != null)
	          {
	            for(int d = 0; d < vDettaglioUso.size(); d++) 
	            {
	              TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUsoSecondario");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.idTipoDettaglioUsoSecondario", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
	              descrizione = null;
	              if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
	              {
	                descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoDettaglioUsoVO.getDescrizione();
	              }
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
	               
	              if(request.getParameterValues("idTipoDettaglioUsoSecondario") != null && i < request.getParameterValues("idTipoDettaglioUsoSecondario").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUsoSecondario")[i]) 
	                  && request.getParameterValues("idTipoDettaglioUsoSecondario")[i].equalsIgnoreCase(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
			      }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
		      }
	      }
	      else
	      {
	        Vector<TipoDettaglioUsoVO>  vDettaglioUso = elencoDettUsoSecondario.get(i);
	        if((vDettaglioUso != null)
	          && (vDettaglioUso.size() > 0))
	        {
	          for(int d = 0; d < vDettaglioUso.size(); d++) 
	          {
	            TipoDettaglioUsoVO tipoDettaglioUsoVO = vDettaglioUso.get(d);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUsoSecondario");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.idTipoDettaglioUsoSecondario", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
	            descrizione = null;
	            if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
	            {
	              descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
	            }
	            else 
	            {
	              descrizione = tipoDettaglioUsoVO.getDescrizione();
	            }
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario()))
	            {
	              if(utilizzoParticellaVO.getIdTipoDettaglioUsoSecondario().compareTo(new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiDettaglioUsoSecondario.selected", "selected=\"selected\"",null);
	              }
	            }
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
		      }
	      }         
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiDettaglioUsoSecondario");
      }
    
    
	    if(elencoQualitaUsoSecondario != null && elencoQualitaUsoSecondario.size() > 0)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoDettaglioUsoSecondario") != null)
	          && (request.getParameterValues("idTipoDettaglioUsoSecondario")[i] != null))
	        {
	          Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUsoSecondario.get(new Integer(i));
	          if(vQualitaUso != null)
	          {
	            for(int d = 0; d < vQualitaUso.size(); d++) 
	            {
	              TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUsoSecondario");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUsoSecondario.idTipoQualitaUsoSecondario", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
	              descrizione = null;
	              if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
	              {
	                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
	              }
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUsoSecondario.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
	               
	              if(request.getParameterValues("idTipoQualitaUsoSecondario") != null && i < request.getParameterValues("idTipoQualitaUsoSecondario").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoSecondario")[i]) 
	                  && request.getParameterValues("idTipoQualitaUsoSecondario")[i].equalsIgnoreCase(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUsoSecondario.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
			      }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
		      }
	      }
	      else
	      {
	        Vector<TipoQualitaUsoVO>  vQualitaUso = elencoQualitaUsoSecondario.get(i);
	        if((vQualitaUso != null)
	          && (vQualitaUso.size() > 0))
	        {
	          for(int d = 0; d < vQualitaUso.size(); d++) 
	          {
	            TipoQualitaUsoVO tipoQualitaUsoVO = vQualitaUso.get(d);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUsoSecondario");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUsoSecondario.idTipoQualitaUsoSecondario", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
	            descrizione = null;
	            if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
	            {
	              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
	            }
	            else 
	            {
	              descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
	            }
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUsoSecondario.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoQualitaUsoSecondario()))
	            {
	              if(utilizzoParticellaVO.getIdTipoQualitaUsoSecondario().compareTo(new Long(tipoQualitaUsoVO.getIdTipoQualitaUso())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiQualitaUsoSecondario.selected", "selected=\"selected\"",null);
	              }
	            }
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
		      }
	      }         
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiQualitaUsoSecondario");
      }
    
    
	    if(elencoVarietaSecondaria != null && elencoVarietaSecondaria.size() > 0 ) 
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
	      {
	        if((request.getParameterValues("idTipoQualitaUsoSecondario") != null)
	          && Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUsoSecondario")[i]))
	        {
	          Vector<TipoVarietaVO> tipoVarieta = (Vector<TipoVarietaVO>)elencoVarietaSecondaria.get(new Integer(i));
	          if(tipoVarieta != null) 
	          {
	            for(int h = 0; h < tipoVarieta.size(); h++) 
	            {
	              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(h);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiVarietaSecondaria");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarietaSecondaria.idVarietaSecondaria", tipoVarietaVO.getIdVarieta().toString());
	              if(tipoVarietaVO.getDescrizione().length() > 20) 
	              {
	                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoVarietaVO.getDescrizione();
	              }
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarietaSecondaria.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
	              if(request.getParameterValues("idVarietaSecondaria") != null 
	                && i < request.getParameterValues("idVarietaSecondaria").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idVarietaSecondaria")[i]) 
	                  && request.getParameterValues("idVarietaSecondaria")[i].equalsIgnoreCase(tipoVarietaVO.getIdVarieta().toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarietaSecondaria.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
			      }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
		      }
	      }
	      else
	      {
	        Vector<TipoVarietaVO> tipoVarieta = elencoVarietaSecondaria.get(i);
          if(tipoVarieta != null) 
          {
            for(int h = 0; h < tipoVarieta.size(); h++) 
            {
              TipoVarietaVO tipoVarietaVO = tipoVarieta.get(h);
              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiVarietaSecondaria");
              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarietaSecondaria.idVarietaSecondaria", tipoVarietaVO.getIdVarieta().toString());
              if(tipoVarietaVO.getDescrizione().length() > 20) 
              {
                descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
              }
              else 
              {
                descrizione = tipoVarietaVO.getDescrizione();
              }
              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarietaSecondaria.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
              if((utilizzoParticellaVO != null)
                && Validator.isNotEmpty(utilizzoParticellaVO.getIdVarietaSecondaria()))  
              {
                if(utilizzoParticellaVO.getIdVarietaSecondaria().compareTo(tipoVarietaVO.getIdVarieta()) == 0) 
                {
                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiVarietaSecondaria.selected", "selected=\"selected\"",null);
                }
              }
            }
          }
          else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
		      }
	        
	      }
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipiVarietaSecondaria");
      }
	    
	    
	    
	    // Carico la combo del perido semina solo se l'utente ha selezionato il tipo
	    // uso suolo corrispondente
	    if(elencoPerSeminaSecondario != null && elencoPerSeminaSecondario.size() > 0) 
	    {  
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((request.getParameterValues("idTipoUtilizzoSecondario") != null)
	          && (request.getParameterValues("idTipoUtilizzoSecondario")[i] != null))
	        {
	          Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSeminaSecondario.get(new Integer(i));
	          if(vTipoPeriodoSemina != null)
	          {
	            for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
	            {
	              TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
	              htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario");
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.idTipoPeriodoSeminaSecondario", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.descrizione", tipoPeriodoSeminaVO.getDescrizione());
	             
	              if(request.getParameterValues("idTipoPeriodoSeminaSecondario") != null && i < request.getParameterValues("idTipoPeriodoSeminaSecondario").length) 
	              {
	                if(Validator.isNotEmpty(request.getParameterValues("idTipoPeriodoSeminaSecondario")[i]) 
	                  && request.getParameterValues("idTipoPeriodoSeminaSecondario")[i].equalsIgnoreCase(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.selected", "selected=\"selected\"", null);
	                }
	              }               
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
			      } 
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
		      } 
	      }
	      //prima volta che entro nella popup.
	      else
	      {         
	        Vector<TipoPeriodoSeminaVO> vTipoPeriodoSemina = (Vector<TipoPeriodoSeminaVO>)elencoPerSeminaSecondario.get(i);
	        if(vTipoPeriodoSemina != null)
	        {
	          for(int l = 0; l < vTipoPeriodoSemina.size(); l++) 
	          {
	            TipoPeriodoSeminaVO tipoPeriodoSeminaVO = vTipoPeriodoSemina.get(l);
	            htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario");
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.idTipoPeriodoSeminaSecondario", ""+tipoPeriodoSeminaVO.getIdTipoPeriodoSemina());
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.descrizione", tipoPeriodoSeminaVO.getDescrizione());
	            
	            if((utilizzoParticellaVO != null)
	              && Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoPeriodoSeminaSecondario())) 
	            {
	              if(utilizzoParticellaVO.getIdTipoPeriodoSeminaSecondario().compareTo(new Long(tipoPeriodoSeminaVO.getIdTipoPeriodoSemina())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoPeriodoSeminaSecondario.selected", "selected=\"selected\"", null);
	              }
	            }             
	          }
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
		      }            
	      }       
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoPeriodoSeminaSecondario");
      }    
    
    
    
	    //abilita semina
	    boolean abilitaSeminaSec = false;
	    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	    {
	      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario"))
	          && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[i]))
	      {
	        abilitaSeminaSec = true;
	      }
	    }
	    else
	    {
	      if((utilizzoParticellaVO != null)
	        && Validator.isNotEmpty(utilizzoParticellaVO.getIdUtilizzoSecondario()))
	      {
	        abilitaSeminaSec = true;
	      } 
	    }
	    
	    if(abilitaSeminaSec)
	    {
	      for(int e=0;e<vTipoSemina.size(); e++) 
	      {
	        TipoSeminaVO tipoSeminaVO = vTipoSemina.get(e);
	        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipoSeminaSecondario");
	        htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoSeminaSecondario.idTipoSeminaSecondario", ""+tipoSeminaVO.getIdTipoSemina());
	        htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoSeminaSecondario.descrizione", tipoSeminaVO.getDescrizioneSemina());
	        if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
	        {
	          if(request.getParameterValues("idTipoSeminaSecondario") != null 
	            && i < request.getParameterValues("idTipoSeminaSecondario").length) 
	          {
	            if(Validator.isNotEmpty(request.getParameterValues("idTipoSeminaSecondario")[i]) 
	              && Long.decode(request.getParameterValues("idTipoSeminaSecondario")[i]).compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0) 
	            {
	              htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoSeminaSecondario.selected", "selected=\"selected\"", null);
	            }
	          }
	          
	        }
	        else 
	        {
	          if((utilizzoParticellaVO != null)
	            && Validator.isNotEmpty(utilizzoParticellaVO.getIdSeminaSecondario())
	            && utilizzoParticellaVO.getIdSeminaSecondario().compareTo(new Long(tipoSeminaVO.getIdTipoSemina())) == 0)
	          {
	            htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipoSeminaSecondario.selected", "selected=\"selected\"", null);
	          }
	        }
	      }
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkNoTipoSeminaSecondario");
      } 
	    
	    if(abilitaSeminaSec)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
	      {
	        if(request.getParameterValues("dataInizioDestinazioneSec") != null 
	          && (i < request.getParameterValues("dataInizioDestinazioneSec").length) 
	          && Validator.isNotEmpty(request.getParameterValues("dataInizioDestinazioneSec")[i]))
	        { 
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.dataInizioDestinazioneSec", request.getParameterValues("dataInizioDestinazioneSec")[i]);
	        }
	        
	        if(request.getParameterValues("dataFineDestinazioneSec") != null 
	          && (i < request.getParameterValues("dataFineDestinazioneSec").length) 
	          && Validator.isNotEmpty(request.getParameterValues("dataFineDestinazioneSec")[i]))
	        { 
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.dataFineDestinazioneSec", request.getParameterValues("dataFineDestinazioneSec")[i]);
	        }
	      }
	      else
	      {
	        if((utilizzoParticellaVO != null)
	          && Validator.isNotEmpty(utilizzoParticellaVO.getDataInizioDestinazioneSecStr()))
	        { 
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.dataInizioDestinazioneSec", vUtilizziVO.get(i).getDataInizioDestinazioneSecStr());
	        }
	        
	        if((utilizzoParticellaVO != null)
	          && Validator.isNotEmpty(utilizzoParticellaVO.getDataFineDestinazioneSecStr()))
	        { 
	          htmpl.set("blkElencoDati.blkElencoUtilizzi.dataFineDestinazioneSec",  vUtilizziVO.get(i).getDataFineDestinazioneSecStr());
	        }     
	      }
	    }
	    else
	    {
	      htmpl.set("blkElencoDati.blkElencoUtilizzi.readOnlyDataInizioDestinazioneSec", "readOnly=\"true\"", null);
	      htmpl.set("blkElencoDati.blkElencoUtilizzi.readOnlyDataFineDestinazioneSec", "readOnly=\"true\"", null);
	    } 			
			
      
      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
      {
        if(request.getParameterValues("supUtilizzataSecondaria") != null 
          && i < request.getParameterValues("supUtilizzataSecondaria").length) 
        {
          if(Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[i])) 
          {
            htmpl.set("blkElencoDati.blkElencoUtilizzi.supUtilizzataSecondaria", request.getParameterValues("supUtilizzataSecondaria")[i]);
          }
        }
      }
      else
      {
        if((utilizzoParticellaVO != null)
          && Validator.isNotEmpty(utilizzoParticellaVO.getSupUtilizzataSecondaria())) 
        {
          htmpl.set("blkElencoDati.blkElencoUtilizzi.supUtilizzataSecondaria", utilizzoParticellaVO.getSupUtilizzataSecondaria());
        }
      }
				
			
				
				
			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
			{
				if(request.getParameterValues("annoImpianto") != null && i < request.getParameterValues("annoImpianto").length) 
				{
					if(Validator.isNotEmpty(request.getParameterValues("annoImpianto")[i])) 
					{
						htmpl.set("blkElencoDati.blkElencoUtilizzi.annoImpianto", request.getParameterValues("annoImpianto")[i]);
					}
				}
			}
			//prima volta che entro
			else 
			{
				if(Validator.isNotEmpty(utilizzoParticellaVO)
				  && Validator.isNotEmpty(utilizzoParticellaVO.getAnnoImpianto())) 
				{
					htmpl.set("blkElencoDati.blkElencoUtilizzi.annoImpianto", utilizzoParticellaVO.getAnnoImpianto());
				}
			}
				
			for(int e = 0; e < elencoTipoImpianto.length; e++) 
			{
				TipoImpiantoVO tipoImpiantoVO = (TipoImpiantoVO)elencoTipoImpianto[e];
				htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkTipiImpianto");
				htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiImpianto.idImpianto", tipoImpiantoVO.getIdImpianto().toString());
				htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiImpianto.descrizione", tipoImpiantoVO.getDescrizione());
				if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
				{
					if(request.getParameterValues("idImpianto") != null 
					  && i < request.getParameterValues("idImpianto").length) 
					{
						if(Validator.isNotEmpty(request.getParameterValues("idImpianto")[i]) 
						  && Long.decode(request.getParameterValues("idImpianto")[i]).compareTo(tipoImpiantoVO.getIdImpianto()) == 0) 
						{
							htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiImpianto.selected", "selected=\"selected\"", null);
						}
					}
				}
				else 
				{
					if(Validator.isNotEmpty(utilizzoParticellaVO)
					  && Validator.isNotEmpty(utilizzoParticellaVO.getIdImpianto()) 
					  && utilizzoParticellaVO.getIdImpianto().compareTo(tipoImpiantoVO.getIdImpianto()) == 0)
					{
						htmpl.set("blkElencoDati.blkElencoUtilizzi.blkTipiImpianto.selected", "selected=\"selected\"", null);
					}
				}
			}
 				
			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
			{
				if(request.getParameterValues("sestoSuFile") != null 
				  && i < request.getParameterValues("sestoSuFile").length) 
				{
					if(Validator.isNotEmpty(request.getParameterValues("sestoSuFile")[i])) 
					{
						htmpl.set("blkElencoDati.blkElencoUtilizzi.sestoSuFile", request.getParameterValues("sestoSuFile")[i]);
					}
				}
			}	
			else 
			{
				if(Validator.isNotEmpty(utilizzoParticellaVO)
				  && Validator.isNotEmpty(utilizzoParticellaVO.getSestoSuFile())) 
				{
					htmpl.set("blkElencoDati.blkElencoUtilizzi.sestoSuFile", utilizzoParticellaVO.getSestoSuFile());
				}
			}
 				
			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
			{
				if(request.getParameterValues("sestoTraFile") != null 
				  && i < request.getParameterValues("sestoTraFile").length) 
				{
					if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) 
					{
						htmpl.set("blkElencoDati.blkElencoUtilizzi.sestoTraFile", request.getParameterValues("sestoTraFile")[i]);
					}
				}
			}	
			else 
			{
				if(Validator.isNotEmpty(utilizzoParticellaVO)
				  && Validator.isNotEmpty(utilizzoParticellaVO.getSestoTraFile())) 
				{
					htmpl.set("blkElencoDati.blkElencoUtilizzi.sestoTraFile", utilizzoParticellaVO.getSestoTraFile());
				}
			}
 				
			if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
			{
				if(request.getParameterValues("numeroPianteCeppi") != null 
				  && i < request.getParameterValues("numeroPianteCeppi").length) 
				{
					if(Validator.isNotEmpty(request.getParameterValues("numeroPianteCeppi")[i])) 
					{
						htmpl.set("blkElencoDati.blkElencoUtilizzi.numeroPianteCeppi", request.getParameterValues("numeroPianteCeppi")[i]);
					}
				}
			}
			else 
			{
				if(Validator.isNotEmpty(utilizzoParticellaVO)
				  && Validator.isNotEmpty(utilizzoParticellaVO.getNumeroPianteCeppi()))  
        {
					htmpl.set("blkElencoDati.blkElencoUtilizzi.numeroPianteCeppi", utilizzoParticellaVO.getNumeroPianteCeppi());
				}
			}
			// Sezione relativa alle piante e agli utilizzi consociati
			
			if(Validator.isNotEmpty(elencoPianteConsociate)) 
      {
        int numPianteConsociate = elencoPianteConsociate.size();
				boolean isNewConsociato = false;
				for(int a = 0; a < numPianteConsociate; a++) 
				{
					//UtilizzoConsociatoVO utilizzoConsociatoVO = (UtilizzoConsociatoVO)elencoUtilizziConsociati[a];
					htmpl.newBlock("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate");
					if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso)) 
					{
						if(i == 0) 
						{
							if(request.getParameterValues("numeroPianteConsociate") != null 
							  && Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[a])) 
							{
	 							htmpl.set("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", request.getParameterValues("numeroPianteConsociate")[a]);
	 						}
							else 
							{
								htmpl.set("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
							}
						}
						else 
						{
							int valoreSelected = (i * numPianteConsociate) + a;
							if(valoreSelected < request.getParameterValues("numeroPianteConsociate").length) 
							{
								if(Validator.isNotEmpty(request.getParameterValues("numeroPianteConsociate")[valoreSelected])) 
								{
	 								htmpl.set("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", request.getParameterValues("numeroPianteConsociate")[valoreSelected]);
	 							}
								else 
								{
									htmpl.set("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
								}
							}
							else 
							{
								isNewConsociato = true;
							}
						}
					}
					else 
					{
					  if(Validator.isNotEmpty(utilizzoParticellaVO)
              && Validator.isNotEmpty(utilizzoParticellaVO.getElencoUtilizziConsociati()))
            {
						  UtilizzoConsociatoVO utilizzoConsociatoVO = utilizzoParticellaVO.getElencoUtilizziConsociati()[a];
							if(Validator.isNotEmpty(utilizzoConsociatoVO.getNumeroPiante()) 
							  && !utilizzoConsociatoVO.getNumeroPiante().equalsIgnoreCase("0")) 
							{
								htmpl.set("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", utilizzoConsociatoVO.getNumeroPiante());
							}
							else 
							{
								htmpl.set("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
							}
					  }
					}
					// Gestione errori
	 				if(erroriPianteConsociate != null && erroriPianteConsociate.size() > 0) 
	 				{
	 					ValidationErrors errorsConsociate = null;
	 					if(i == 0) 
	 					{
	 						errorsConsociate = (ValidationErrors)erroriPianteConsociate.get(new Integer(a));
	 					}
	 					else 
	 					{
	 						errorsConsociate = (ValidationErrors)erroriPianteConsociate.get(new Integer((i*numPianteConsociate)+a));
	 					}
	 					if(errorsConsociate != null && errorsConsociate.size() > 0) 
	 					{
	 			 			Iterator iter = htmpl.getVariableIterator();
	 			 			while(iter.hasNext()) 
	 			 			{
	 			 				String chiave = (String)iter.next();
	 			 				if(chiave.startsWith("err_")) 
	 			 				{
 			 		    		String property = chiave.substring(4);
 			 		    		Iterator errorIterator = errorsConsociate.get(property);
 			 		    		if(errorIterator != null) 
 			 		    		{
 			 		    			ValidationError error = (ValidationError)errorIterator.next();
 			 		    			htmpl.set("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate.err_"+property,
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
				} //for utilizzi consociati..
				if(request.getParameterValues("numeroPianteConsociate") != null && isNewConsociato) 
				{
					if(request.getParameterValues("numeroPianteConsociate").length < (numeroUtilizzi * numPianteConsociate)) 
					{
					  int valorePartenzaCons = (numeroUtilizzi * numPianteConsociate) - request.getParameterValues("numeroPianteConsociate").length;
					  for(int j = 0; j < valorePartenzaCons; j++) 
					  {
						  htmpl.set("blkElencoDati.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "");
					  }
				  }
				}
			}
			
			
			// Gestione errori
			if(erroriUtilizzi != null && erroriUtilizzi.size() > 0) 
			{
				ValidationErrors errorsUtilizzi = (ValidationErrors)erroriUtilizzi.get(new Integer(i));
				if(errorsUtilizzi != null && errorsUtilizzi.size() > 0) 
				{
			 		Iterator iter = htmpl.getVariableIterator();
			 		while(iter.hasNext()) 
			 		{
			 			String chiave = (String)iter.next();
			 			if(chiave.startsWith("err_")) 
			 			{
		 			    String property = chiave.substring(4);
		 			    Iterator errorIterator = errorsUtilizzi.get(property);
		 			    if(errorIterator != null) 
		 			    {
		 			    	ValidationError error = (ValidationError)errorIterator.next();
		 			    	htmpl.set("blkElencoDati.blkElencoUtilizzi.err_"+property,
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
 				
			// Gestione errori
      /*if(erroriDettaglioUso != null && erroriDettaglioUso.size() > 0) 
      {
        ValidationErrors errorsVarieta = (ValidationErrors)erroriDettaglioUso.get(new Integer(i));
        if(errorsVarieta != null && errorsVarieta.size() > 0) 
        {
          Iterator iter = htmpl.getVariableIterator();
          while(iter.hasNext()) 
          {
            String chiave = (String)iter.next();
            if(chiave.startsWith("err_")) 
            {
              String property = chiave.substring(4);
              Iterator errorIterator = errorsVarieta.get(property);
              if(errorIterator != null) 
              {
                ValidationError error = (ValidationError)errorIterator.next();
                htmpl.set("blkElencoDati.blkElencoUtilizzi.err_"+property,
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
      }	*/
 			 
 		} //for utilizzi normali
 		// Labels relative ai dati delle piante consociate
    if(elencoPianteConsociate != null && elencoPianteConsociate.size() > 0) {
      htmpl.set("blkElencoDati.colspan", String.valueOf(elencoPianteConsociate.size()));
      for(int i = 0; i < elencoPianteConsociate.size(); i++) {
        TipoPiantaConsociataVO tipoPiantaConsociataVO = (TipoPiantaConsociataVO)elencoPianteConsociate.elementAt(i);
        htmpl.newBlock("blkElencoDati.blkEtichettaPianteConsociate");
        htmpl.set("blkElencoDati.blkEtichettaPianteConsociate.descPianteConsociate", tipoPiantaConsociataVO.getDescrizione());
      }
    }
 		
 		
 		Vector<UtilizzoParticellaVO> vUtilizziEfaVO = (Vector<UtilizzoParticellaVO>)request.getAttribute("vUtilizziEfaVO");
    int numeroUtilizziEfa = 0;
    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
    {
      if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoEfa")))
        numeroUtilizziEfa = request.getParameterValues("idTipoUtilizzoEfa").length;
    }
    else if(Validator.isNotEmpty(vUtilizziEfaVO))
    {
      numeroUtilizziEfa = vUtilizziEfaVO.size();
    }
    
    for(int i=0;i<numeroUtilizziEfa;i++)
    {
      UtilizzoParticellaVO utilizzoParticellaEfaVO = null;
      if(Validator.isEmpty(regimePopModificaTerritorialeCondUso))
      {
        if(Validator.isNotEmpty(vUtilizziEfaVO))
          utilizzoParticellaEfaVO = vUtilizziEfaVO.get(i);
      }
        
      TipoEfaVO tipoEfaVOSel = null;
        
	 		htmpl.newBlock("blkElencoDati.blkElencoEfa");
	 		htmpl.set("blkElencoDati.blkElencoEfa.numeroUtilizziEfa", String.valueOf(i));
		  // Combo tipo efa
      if(vTipoEfa != null) 
      {
        for(int j=0;j<vTipoEfa.size();j++) 
        {
          TipoEfaVO tipoEfaVO = vTipoEfa.get(j);
          htmpl.newBlock("blkElencoDati.blkElencoEfa.blkTipoEfa");
          htmpl.set("blkElencoDati.blkElencoEfa.blkTipoEfa.idTipoEfa", ""+tipoEfaVO.getIdTipoEfa());
          htmpl.set("blkElencoDati.blkElencoEfa.blkTipoEfa.descCompleta", tipoEfaVO.getDescrizioneEstesaTipoEfa());
          htmpl.set("blkElencoDati.blkElencoEfa.blkTipoEfa.descrizione", tipoEfaVO.getDescrizioneTipoEfa());
          //prima volta che entro
          if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
          {
            if(Validator.isNotEmpty(arrIdTipoEfa)
              && (i < arrIdTipoEfa.length)
              && Validator.isNotEmpty(arrIdTipoEfa[i])
              && new Long(arrIdTipoEfa[i]).longValue() == tipoEfaVO.getIdTipoEfa()) 
            {
              htmpl.set("blkElencoDati.blkElencoEfa.blkTipoEfa.selected", "selected=\"selected\"", null);
              htmpl.set("blkElencoDati.blkElencoEfa.descUnitaMisura", tipoEfaVO.getDescUnitaMisura());
              htmpl.set("blkElencoDati.blkElencoEfa.fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
              htmpl.set("blkElencoDati.blkElencoEfa.fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
              
              tipoEfaVOSel = tipoEfaVO;
            }
          }
          else
          {
            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoEfa())
              && (utilizzoParticellaEfaVO.getIdTipoEfa() == tipoEfaVO.getIdTipoEfa())) 
            {
              htmpl.set("blkElencoDati.blkElencoEfa.blkTipoEfa.selected", "selected=\"selected\"", null);
              htmpl.set("blkElencoDati.blkElencoEfa.descUnitaMisura", tipoEfaVO.getDescUnitaMisura());
              htmpl.set("blkElencoDati.blkElencoEfa.fattoreConversione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiConversione()));
              htmpl.set("blkElencoDati.blkElencoEfa.fattorePonderazione", Formatter.formatDouble2(tipoEfaVO.getFattoreDiPonderazione()));
              idTipoEfa = utilizzoParticellaEfaVO.getIdTipoEfa();
            } 
          }
        }
      }
      
      
      if(Validator.isNotEmpty(elencoUtilizziEfa))
      {
	      Vector<TipoUtilizzoVO> vTipoUtilizzo = elencoUtilizziEfa.get(new Integer(i));
	      Long idUtilizzoEfaPrimo = null;
	      
	      if(Validator.isNotEmpty(vTipoUtilizzo)
	        && (vTipoUtilizzo.size() > 0))
	      {
	        for(int j=0;j<vTipoUtilizzo.size();j++) 
	        {
	          TipoUtilizzoVO tipoUtilizzoVO = vTipoUtilizzo.get(j);
	          htmpl.newBlock("blkElencoDati.blkElencoEfa.blkTipiUsoSuoloEfa");
	          
	          if(j==0)
	            idUtilizzoEfaPrimo = tipoUtilizzoVO.getIdUtilizzo();
	          
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiUsoSuoloEfa.idTipoUtilizzoEfa", ""+tipoUtilizzoVO.getIdUtilizzo());
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiUsoSuoloEfa.descCompleta", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
	          
	          descrizione = null;
	          if(tipoUtilizzoVO.getDescrizione().length() > 20) 
	          {
	            descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
	          }
	          else 
	          {
	            descrizione = tipoUtilizzoVO.getDescrizione();
	          }
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiUsoSuoloEfa.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
	          
	          if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	          {
		          if(Validator.isNotEmpty(elencoUtilizziSelezionatiEfa)
                && (i < elencoUtilizziSelezionatiEfa.length)
                && Validator.isNotEmpty(elencoUtilizziSelezionatiEfa[i]))
              {
                if(new Long(elencoUtilizziSelezionatiEfa[i]).compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0)
                { 
                  htmpl.set("blkElencoDati.blkElencoEfa.blkTipiUsoSuoloEfa.selected", "selected=\"selected\"", null);
	              }
	            }
	          }
	          else
	          {
	            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
	              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdUtilizzo())
	              && utilizzoParticellaEfaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0)
              { 
                htmpl.set("blkElencoDati.blkElencoEfa.blkTipiUsoSuoloEfa.selected", "selected=\"selected\"", null);
                idTipoUtilizzoEfa = utilizzoParticellaEfaVO.getIdUtilizzo();
              }
	          }	          
	        }
	        
	        if(idTipoUtilizzoEfa == null)
	          idTipoUtilizzoEfa = idUtilizzoEfaPrimo;
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiUsoSuoloEfa");
	      } 
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiUsoSuoloEfa");
      } 
	    
	    
	    if(elencoDestinazioneEfa != null && elencoDestinazioneEfa.size() > 0)
	    {
	      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	      {
	        if((elencoUtilizziSelezionatiEfa != null)
	          && (i < elencoUtilizziSelezionatiEfa.length)
	          && (Validator.isNotEmpty(elencoUtilizziSelezionatiEfa[i])))
	        {
	          Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneEfa.get(new Integer(i));
	          if(vDestinazione != null)
	          {
	            for(int d = 0; d < vDestinazione.size(); d++) 
	            {
	              TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
	              htmpl.newBlock("blkElencoDati.blkElencoEfa.blkTipiDestinazioneEfa");
	              htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDestinazioneEfa.idTipoDestinazioneEfa", ""+tipoDestinazioneVO.getIdTipoDestinazione());
	              descrizione = "";
	              if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
	              {
	                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
	              }
	              else 
	              {
	                descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
	              }
	              htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDestinazioneEfa.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
	               
	              if(Validator.isNotEmpty(elencoDestinazioneSelezionateEfa)
                  && (i < elencoDestinazioneSelezionateEfa.length)
                  && Validator.isNotEmpty(elencoDestinazioneSelezionateEfa[i]))
	              {
	                if(elencoDestinazioneSelezionateEfa[i].equalsIgnoreCase(new Long(tipoDestinazioneVO.getIdTipoDestinazione()).toString())) 
	                {
	                  htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDestinazioneEfa.selected", "selected=\"selected\"",null);
	                }
	              }
	            }
	          }
	          else
			      {
			        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiDestinazioneEfa");
			      } 
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiDestinazioneEfa");
		      } 
	      }
	      else
	      {
	        Vector<TipoDestinazioneVO> vDestinazione = elencoDestinazioneEfa.get(i);
	        if((vDestinazione != null)
	          && (vDestinazione.size() > 0))
	        {
	          Long idTipoDestinazioneEfaPrimo = null;
	          for(int d = 0; d < vDestinazione.size(); d++) 
	          {
	            TipoDestinazioneVO tipoDestinazioneVO = vDestinazione.get(d);
	            if(d==0)
	              idTipoDestinazioneEfaPrimo = new Long(tipoDestinazioneVO.getIdTipoDestinazione());
	            
	            htmpl.newBlock("blkElencoDati.blkElencoEfa.blkTipiDestinazioneEfa");
	            htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDestinazioneEfa.idTipoDestinazioneEfa", ""+tipoDestinazioneVO.getIdTipoDestinazione());
	            descrizione = "";
	            if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
	            {
	              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
	            }
	            else 
	            {
	              descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
	            }
	            htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDestinazioneEfa.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
	            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
	              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoDestinazione()))
	            {
	              if(utilizzoParticellaEfaVO.getIdTipoDestinazione().compareTo(new Long(tipoDestinazioneVO.getIdTipoDestinazione())) == 0) 
	              {
	                htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDestinazioneEfa.selected", "selected=\"selected\"",null);
	                idTipoDestinazioneEfa = utilizzoParticellaEfaVO.getIdTipoDestinazione();
	              }
	            }
	          }
	          
	          if(idTipoDestinazioneEfa == null);
	            idTipoDestinazioneEfa = idTipoDestinazioneEfaPrimo;
	        }
	        else
		      {
		        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiDestinazioneEfa");
		      } 
	      }         
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiDestinazioneEfa");
      } 
	    
	    if(Validator.isNotEmpty(elencoDettaglioUsoEfa))
	    {
	      Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = elencoDettaglioUsoEfa.get(new Integer(i));
	      if(Validator.isNotEmpty(vTipoDettaglioUso)
	        && vTipoDettaglioUso.size() > 0)
	      {
	        Long idTipoDettaglioUsoEfaPrimo = null;
	        for(int j=0;j<vTipoDettaglioUso.size();j++) 
	        {
	          TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(j);
	          if(j==0)
	            idTipoDettaglioUsoEfaPrimo = new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso());
	            
	          htmpl.newBlock("blkElencoDati.blkElencoEfa.blkTipiDettaglioUsoEfa");
	          
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDettaglioUsoEfa.idTipoDettaglioUsoEfa", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
	          
	          descrizione = "";
	          if(tipoDettaglioUsoVO.getDescrizione().length() > 20) 
	          {
	            descrizione = tipoDettaglioUsoVO.getDescrizione().substring(0, 20);
	          }
	          else 
	          {
	            descrizione = tipoDettaglioUsoVO.getDescrizione();
	          }
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDettaglioUsoEfa.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
	          
	          if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	          {
	            if(Validator.isNotEmpty(elencoDetUsoSelezionateEfa)
	              && (i < elencoDetUsoSelezionateEfa.length)
	              && Validator.isNotEmpty(elencoDetUsoSelezionateEfa[i]))
	            {
	              if(new Long(elencoDetUsoSelezionateEfa[i]).compareTo(tipoDettaglioUsoVO.getIdTipoDettaglioUso()) == 0)
	              { 
	                htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDettaglioUsoEfa.selected", "selected=\"selected\"", null);
	              }       
	            }
	          }
	          else
	          {
	            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
	              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoDettaglioUso())
	              && utilizzoParticellaEfaVO.getIdTipoDettaglioUso().compareTo(tipoDettaglioUsoVO.getIdTipoDettaglioUso()) == 0)
	            { 
	              htmpl.set("blkElencoDati.blkElencoEfa.blkTipiDettaglioUsoEfa.selected", "selected=\"selected\"", null);
	              idTipoDettaglioUsoEfa = utilizzoParticellaEfaVO.getIdTipoDettaglioUso();
	            }
	          }         
	        }
	        
	        if(idTipoDettaglioUsoEfa == null)
	          idTipoDettaglioUsoEfa = idTipoDettaglioUsoEfaPrimo;
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiDettaglioUsoEfa");
	      }   
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiDettaglioUsoEfa");
      } 
	    
	    
	    if(Validator.isNotEmpty(elencoQualitaUsoEfa))
	    {
	      Vector<TipoQualitaUsoVO> vTipoQualitaUso = elencoQualitaUsoEfa.get(new Integer(i));
	      if(Validator.isNotEmpty(vTipoQualitaUso)
	        && vTipoQualitaUso.size() > 0)
	      {
	        Long idTipoQualitaUsoEfaPrimo = null;
	        for(int j=0;j<vTipoQualitaUso.size();j++) 
	        {
	          TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUso.get(j);
	          if(j==0)
	            idTipoQualitaUsoEfaPrimo = new Long(tipoQualitaUsoVO.getIdTipoQualitaUso());
	          
	          
	          htmpl.newBlock("blkElencoDati.blkElencoEfa.blkTipiQualitaUsoEfa");
	          
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiQualitaUsoEfa.idTipoQualitaUsoEfa", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
	          
	          descrizione = "";
	          if(tipoQualitaUsoVO.getDescrizioneQualitaUso().length() > 20) 
	          {
	            descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso().substring(0, 20);
	          }
	          else 
	          {
	            descrizione = tipoQualitaUsoVO.getDescrizioneQualitaUso();
	          }
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiQualitaUsoEfa.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
	          
	          if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	          {
	            if(Validator.isNotEmpty(elencoQualitaUsoSelezionateEfa)
	              && (i < elencoQualitaUsoSelezionateEfa.length)
	              && Validator.isNotEmpty(elencoQualitaUsoSelezionateEfa[i]))
	            {
	              if(new Long(elencoQualitaUsoSelezionateEfa[i]).compareTo(tipoQualitaUsoVO.getIdTipoQualitaUso()) == 0)
	              { 
	                htmpl.set("blkElencoDati.blkElencoEfa.blkTipiQualitaUsoEfa.selected", "selected=\"selected\"", null);
	              }       
	            }
	          }
	          else
	          {
	            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
	              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoQualitaUso())
	              && utilizzoParticellaEfaVO.getIdTipoQualitaUso().compareTo(tipoQualitaUsoVO.getIdTipoQualitaUso()) == 0)
	            { 
	              htmpl.set("blkElencoDati.blkElencoEfa.blkTipiQualitaUsoEfa.selected", "selected=\"selected\"", null);
	              idTipoQualitaUsoEfa = utilizzoParticellaEfaVO.getIdTipoQualitaUso();
	            }
	          }         
	        }
	        
	        if(idTipoQualitaUsoEfa == null)
	          idTipoQualitaUsoEfa = idTipoQualitaUsoEfaPrimo;
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiQualitaUsoEfa");
	      }   
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiQualitaUsoEfa");
      } 
	    
	    //varieta
	    if(Validator.isNotEmpty(elencoVarietaEfa))
	    {
	      Vector<TipoVarietaVO> vTipoVarieta = elencoVarietaEfa.get(new Integer(i));
	      if(Validator.isNotEmpty(vTipoVarieta)
	        && vTipoVarieta.size() > 0)
	      {
	        Long idVarietaEfaPrimo = null;
	        for(int j=0;j<vTipoVarieta.size();j++) 
	        {
	          TipoVarietaVO tipoVarietaVO = vTipoVarieta.get(j);
	          if(j==0)
	            idVarietaEfaPrimo = tipoVarietaVO.getIdVarieta();
	          
	          htmpl.newBlock("blkElencoDati.blkElencoEfa.blkTipiVarietaEfa");
	          
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiVarietaEfa.idVarietaEfa", tipoVarietaVO.getIdVarieta().toString());
	          
	          descrizione = "";
	          if(tipoVarietaVO.getDescrizione().length() > 20) 
	          {
	            descrizione = tipoVarietaVO.getDescrizione().substring(0, 20);
	          }
	          else 
	          {
	            descrizione = tipoVarietaVO.getDescrizione();
	          }
	          htmpl.set("blkElencoDati.blkElencoEfa.blkTipiVarietaEfa.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
	          
	          
	          if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	          {
	            if(Validator.isNotEmpty(elencoVarietaSelezionateEfa)
	              && (i < elencoVarietaSelezionateEfa.length)
	              && Validator.isNotEmpty(elencoVarietaSelezionateEfa[i]))
	            {
	              if(new Long(elencoVarietaSelezionateEfa[i]).compareTo(tipoVarietaVO.getIdVarieta()) == 0)
	              { 
	                htmpl.set("blkElencoDati.blkElencoEfa.blkTipiVarietaEfa.selected", "selected=\"selected\"", null);
	              }       
	            }
	          }
	          else
	          {
	            if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
	              && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdVarieta())
	              && utilizzoParticellaEfaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0)
	            { 
	              htmpl.set("blkElencoDati.blkElencoEfa.blkTipiVarietaEfa.selected", "selected=\"selected\"", null);         
	              idVarietaEfa = utilizzoParticellaEfaVO.getIdVarieta();
	            } 
	          }         
	        }
	        
	        if(idVarietaEfa == null)
	          idVarietaEfa = idVarietaEfaPrimo;
	      }
	      else
	      {
	        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiVarietaEfa");
	      }  
	    }
	    else
      {
        htmpl.newBlock("blkElencoDati.blkElencoEfa.blkNoTipiVarietaEfa");
      } 
	    
       
      
      
      Integer abbaPonderazione = new Integer(1);
	    if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
	    {
	      if(Validator.isNotEmpty(arrIdTipoEfa)
	         && (i < arrIdTipoEfa.length)
	         && Validator.isNotEmpty(arrIdTipoEfa[i])
	         && Validator.isNotEmpty(elencoAbbPonderazioneVarieta[i]))
	      {
	        htmpl.set("blkElencoDati.blkElencoEfa.abbPonderazioneVarieta", elencoAbbPonderazioneVarieta[i]);
	        abbaPonderazione = new Integer(elencoAbbPonderazioneVarieta[i]);
	      }    
	    }
	    else
	    {
	      if(Validator.isNotEmpty(utilizzoParticellaEfaVO)
	        && Validator.isNotEmpty(utilizzoParticellaEfaVO.getIdTipoEfa()))
	      {  
	        if(Validator.isNotEmpty(utilizzoParticellaEfaVO.getAbbaPonderazione()))
	        { 
	          htmpl.set("blkElencoDati.blkElencoEfa.abbPonderazioneVarieta", ""+utilizzoParticellaEfaVO.getAbbaPonderazione());
	          abbaPonderazione = utilizzoParticellaEfaVO.getAbbaPonderazione();
	        }
	        else
	        {
	          //Me lo calcolo coi valori che si vedono a video poichè dalla query nn ho trovato nulla!!!!
	          //caso estremo di qualche id cessato su matrice.....
	          if(Validator.isNotEmpty(idTipoEfa) && Validator.isNotEmpty(idTipoUtilizzoEfa)
	            && Validator.isNotEmpty(idTipoDestinazioneEfa) && Validator.isNotEmpty(idTipoDettaglioUsoEfa)
	            && Validator.isNotEmpty(idTipoQualitaUsoEfa) & Validator.isNotEmpty(idVarietaEfa))
	          {
	            try
	            {
	              abbaPonderazione = gaaFacadeClient
	                .getAbbPonderazioneByMatrice(idTipoEfa, idTipoUtilizzoEfa, idTipoDestinazioneEfa, 
	                  idTipoDettaglioUsoEfa, idTipoQualitaUsoEfa, idVarietaEfa);
	              htmpl.set("blkElencoDati.blkElencoEfa.abbPonderazioneVarieta", ""+abbaPonderazione);
               
	            }
	            catch(Exception ex)
	            {}
	          }
	        
	        }
	      }    
	    }    
      
      /*Integer abbaPonderazione = new Integer(1);
      if(Validator.isNotEmpty(tipoVarietaEfaSelVO)
        || Validator.isNotEmpty(tipoDettaglioUsoEfaVOSel))
      {
        if(Validator.isNotEmpty(tipoDettaglioUsoEfaVOSel))
        {
          htmpl.set("blkElencoDati.blkElencoEfa.abbPonderazioneVarieta",""+tipoDettaglioUsoEfaVOSel.getAbbattimentoPonderazione());
          abbaPonderazione = tipoDettaglioUsoEfaVOSel.getAbbattimentoPonderazione();
        }
        else
        {
	        htmpl.set("blkElencoDati.blkElencoEfa.abbPonderazioneVarieta",""+tipoVarietaEfaSelVO.getAbbattimentoPonderazione());
	        abbaPonderazione = tipoVarietaEfaSelVO.getAbbattimentoPonderazione();
	      }
      }*/
      
      
      if(Validator.isNotEmpty(regimePopModificaTerritorialeCondUso))
      {
	      if(Validator.isNotEmpty(elencoValoreOriginale)
	        && (i < elencoValoreOriginale.length)
	        && Validator.isNotEmpty(elencoValoreOriginale[i]))
	      {
	        htmpl.set("blkElencoDati.blkElencoEfa.valoreOriginale", elencoValoreOriginale[i]);
	        
	        if(Validator.isNotEmpty(tipoEfaVOSel))
	        {
	          BigDecimal valoreOriginale = null;
	          BigDecimal valoreDopoConversione = null;
	          BigDecimal valoreDopoPonderazione = null;
	          try
	          {
	            valoreOriginale = new BigDecimal(elencoValoreOriginale[i].replace(",", "."));
	          }
	          catch(Exception ex){}
	          
	          if(Validator.isNotEmpty(valoreOriginale))
	          {
	            valoreDopoConversione = valoreOriginale.multiply(tipoEfaVOSel.getFattoreDiConversione());
	            valoreDopoConversione = valoreDopoConversione.divide(new BigDecimal(10000), 4,BigDecimal.ROUND_HALF_UP);
	            valoreDopoPonderazione = valoreDopoConversione.multiply(tipoEfaVOSel.getFattoreDiPonderazione());
	            valoreDopoPonderazione = valoreDopoPonderazione.multiply(new BigDecimal(abbaPonderazione.intValue()));
	          }
	          
	          
	          if(Validator.isNotEmpty(valoreDopoConversione))
	            htmpl.set("blkElencoDati.blkElencoEfa.valoreDopoConversione", Formatter.formatDouble4(valoreDopoConversione));
	            
	          if(Validator.isNotEmpty(valoreDopoPonderazione))
	            htmpl.set("blkElencoDati.blkElencoEfa.valoreDopoPonderazione", Formatter.formatDouble4(valoreDopoPonderazione));
	        } 
	        
	               
	      }
	    }
	    else
	    {
	      if(Validator.isNotEmpty(utilizzoParticellaEfaVO))
	      {
	        if(Validator.isNotEmpty(utilizzoParticellaEfaVO.getValoreOriginale()))
		        htmpl.set("blkElencoDati.blkElencoEfa.valoreOriginale",  Formatter.formatDouble4(utilizzoParticellaEfaVO.getValoreOriginale()));
		      
		      if(Validator.isNotEmpty(utilizzoParticellaEfaVO.getValoreDopoConversione()))
	          htmpl.set("blkElencoDati.blkElencoEfa.valoreDopoConversione", Formatter.formatDouble4(utilizzoParticellaEfaVO.getValoreDopoConversione()));
	          
	        if(Validator.isNotEmpty(utilizzoParticellaEfaVO.getValoreDopoPonderazione()))
	          htmpl.set("blkElencoDati.blkElencoEfa.valoreDopoPonderazione", Formatter.formatDouble4(utilizzoParticellaEfaVO.getValoreDopoPonderazione()));
	      }
	    
	    } 
      
      
      
      
      // Gestione errori
      if(erroriUtilizziEfa != null && erroriUtilizziEfa.size() > 0) 
      {
        ValidationErrors errorsUtilizziEfa = (ValidationErrors)erroriUtilizziEfa.get(new Integer(i));
        if(errorsUtilizziEfa != null && errorsUtilizziEfa.size() > 0) 
        {
          Iterator iter = htmpl.getVariableIterator();
          while(iter.hasNext()) 
          {
            String chiave = (String)iter.next();
            if(chiave.startsWith("err_")) 
            {
              String property = chiave.substring(4);
              Iterator errorIterator = errorsUtilizziEfa.get(property);
              if(errorIterator != null) 
              {
                ValidationError error = (ValidationError)errorIterator.next();
                htmpl.set("blkElencoDati.blkElencoEfa.err_"+property,
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
      
      
		} // for utilizzi efa
 	
 	}

%>
<%=htmpl.text()%>
