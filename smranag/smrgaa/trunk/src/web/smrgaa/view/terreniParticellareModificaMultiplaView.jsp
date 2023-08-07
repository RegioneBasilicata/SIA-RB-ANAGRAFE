<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>
<%@ page import="java.math.BigDecimal"%>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareModificaMultipla.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
	
	// Nuova gestione fogli di stile:(Eclipse da errore a video ma funziona perchè
	// le variabili sono presenti dentro il file include)
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
 	
	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuolo");
 	Vector<TipoVarietaVO> elencoVarieta = (Vector<TipoVarietaVO>)request.getAttribute("elencoVarieta");
 	Vector<TipoDestinazioneVO> vTipoDestinazione = (Vector<TipoDestinazioneVO>)request.getAttribute("vTipoDestinazione");
 	Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = (Vector<TipoDettaglioUsoVO>)request.getAttribute("vTipoDettaglioUso");
 	Vector<TipoQualitaUsoVO> vTipoQualitaUso = (Vector<TipoQualitaUsoVO>)request.getAttribute("vTipoQualitaUso");
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector<TipoUtilizzoVO>)request.getAttribute("elencoTipiUsoSuoloSecondario");
 	Vector<TipoVarietaVO> elencoVarietaSecondaria = (Vector<TipoVarietaVO>)request.getAttribute("elencoVarietaSecondaria");
 	Vector<TipoDestinazioneVO> vTipoDestinazioneSecondario = (Vector<TipoDestinazioneVO>)request.getAttribute("vTipoDestinazioneSecondario");
 	Vector<TipoDettaglioUsoVO> vTipoDettaglioUsoSecondario = (Vector<TipoDettaglioUsoVO>)request.getAttribute("vTipoDettaglioUsoSecondario");
 	Vector<TipoQualitaUsoVO> vTipoQualitaUsoSecondario = (Vector<TipoQualitaUsoVO>)request.getAttribute("vTipoQualitaUsoSecondario");
 	it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = (it.csi.solmr.dto.CodeDescription[])request.getAttribute("elencoTipoTitoloPossesso");
 	
 	Vector<UteVO> elencoUte = (Vector<UteVO>)request.getAttribute("elencoUte");
 	DocumentoVO[] elencoDocumenti = (DocumentoVO[])request.getAttribute("elencoDocumenti");
 	StoricoParticellaVO[] elencoStoricoParticellaVO = (StoricoParticellaVO[])request.getAttribute("elencoStoricoParticellaVO");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	Long idTipoUtilizzo = (Long)request.getAttribute("idTipoUtilizzo");
 	Long idVarieta = (Long)request.getAttribute("idVarieta");
 	Long idTipoDestinazione = (Long)request.getAttribute("idTipoDestinazione");
 	Long idTipoDettaglioUso = (Long)request.getAttribute("idTipoDettaglioUso");
 	Long idTipoQualitaUso = (Long)request.getAttribute("idTipoQualitaUso");
 	Long idTipoUtilizzoSecondario = (Long)request.getAttribute("idTipoUtilizzoSecondario");
 	Long idVarietaSecondaria = (Long)request.getAttribute("idVarietaSecondaria");
 	Long idTipoDestinazioneSecondario = (Long)request.getAttribute("idTipoDestinazioneSecondario");
 	Long idTipoDettaglioUsoSecondario = (Long)request.getAttribute("idTipoDettaglioUsoSecondario");
 	Long idTipoQualitaUsoSecondario = (Long)request.getAttribute("idTipoQualitaUsoSecondario");
 	Vector errori = (Vector)request.getAttribute("errori");
 	Vector erroriSupSecondaria = (Vector)request.getAttribute("erroriSupSecondaria");
 	Vector erroriSupAgronomiche = (Vector)request.getAttribute("erroriSupAgronomiche");
 	Vector erroriFruttaGuscio = (Vector)request.getAttribute("erroriFruttaGuscio");
 	Vector<String> erroriDettaglioUso = (Vector<String>)request.getAttribute("erroriDettaglioUso");
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	String onLoad = (String)request.getAttribute("onLoad");
 	String operazione = (String)request.getAttribute("operazione");
 	Long idTitoloPossesso = (Long)request.getAttribute("idTitoloPossesso");
 	String dataCessazioneStr = (String)request.getAttribute("dataCessazioneStr");
 	String loadFabbricati = (String)request.getAttribute("loadFabbricati");
  //TipoUtilizzoVO tipoUtilizzoEleggibilitaVO = (TipoUtilizzoVO)request.getAttribute("tipoUtilizzoEleggibilitaVO");
  Vector<Long> vPraticheIdParticella = (Vector<Long>)request.getAttribute("vPraticheIdParticella");
  Vector<Long> vIdConduzioniModProcVITIUV = (Vector<Long>)request.getAttribute("vIdConduzioniModProcVITIUV");
  
 	if(Validator.isNotEmpty(loadFabbricati)) {
 		htmpl.set("loadFabbricati", loadFabbricati);
 	}
  
  boolean primaVolta = true;
  if(Validator.isNotEmpty(request.getParameter("regimeTerreniModificaMultipla")))
  {
    primaVolta = false;
  }
  String[] arrOldSupUtilizzata = request.getParameterValues("oldSupUtilizzata");
  String[] arrOldSupUtilizzataSecondaria = request.getParameterValues("oldSupUtilizzataSecondaria");
  String[] arrOldSupUtilizzoEleggibile = request.getParameterValues("oldSupUtilizzoEleggibile");
  String[] arrOldSupUtilizzoEleggibileNetta = request.getParameterValues("oldSupUtilizzoEleggibileNetta");
  
  
  
  CodeDescription[] elencoTipoDichiarazioneUsoAgronomico = (CodeDescription[])request.getAttribute("elencoTipoDichiarazioneUsoAgronomico");
  String idTipoDichiarazioneUsoAgronomico = request.getParameter("idTipoDichiarazioneUsoAgronomico");
  TipoIrrigazioneVO[] elencoIrrigazione = (TipoIrrigazioneVO[])request.getAttribute("elencoIrrigazione");
 	String idTipoIrrigazione = request.getParameter("idTipoIrrigazione");
  
 	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
                          "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
	StringProcessor jssp = new JavaScriptStringProcessor();
	
 	
	htmpl.newBlock("blkOperazioniModifica");
	
	if(Validator.isNotEmpty(onLoad)) {
		htmpl.set("onLoad", onLoad);
	}
	
	// Gestisco la selezione dell'operazione selezionata
	if(Validator.isNotEmpty(operazione)) 
	{
		if(operazione.equalsIgnoreCase("cambia")) {
			htmpl.set("blkOperazioniModifica.checkedCambia", "checked=\"checked\"");
		}
    else if(operazione.equalsIgnoreCase("eleggibilitaGIS")) {
      htmpl.set("blkOperazioniModifica.checkedEleggibilitaGIS", "checked=\"checked\"");
    }
		else if(operazione.equalsIgnoreCase("cessazione")) {
			htmpl.set("blkOperazioniModifica.checkedCessa", "checked=\"checked\"");
		}
		else if(operazione.equalsIgnoreCase("cambiaUte")) {
			htmpl.set("blkOperazioniModifica.checkedUte", "checked=\"checked\"");
		}
		else if(operazione.equalsIgnoreCase("associaDocumento")) {
			htmpl.set("blkOperazioniModifica.checkedDocumento", "checked=\"checked\"");
		}
		else if(operazione.equalsIgnoreCase("dichiarazioneUsoAgronomico")) {
			htmpl.set("blkOperazioniModifica.checkedDichiarazioneUsoAgronomico", "checked=\"checked\"");
		}
    else if(operazione.equalsIgnoreCase("irrigazione")) {
      htmpl.set("blkOperazioniModifica.checkedIrrigazione", "checked=\"checked\"");
    }
    else if(operazione.equalsIgnoreCase("allineaPercPoss")) {
      htmpl.set("blkOperazioniModifica.checkedAllineaPercPoss", "checked=\"checked\"");
    }
    else if(operazione.equalsIgnoreCase("allineaSupUtilPercPoss")) {
      htmpl.set("blkOperazioniModifica.checkedAllineaSupUtilPercPoss", "checked=\"checked\"");
    }
	}
	
	// Gestisco gli errori relativi ai dati dell'operazione selezionata
	if(errors != null && errors.size() > 0) {
		Iterator iter = htmpl.getVariableIterator();
		while(iter.hasNext()) {
			String key = (String)iter.next();
		    if(key.startsWith("err_")) {
		    	String property = key.substring(4);
		    	Iterator errorIterator = errors.get(property);
		    	if(errorIterator != null) {
		    		ValidationError error = (ValidationError)errorIterator.next();
		    		htmpl.set("blkOperazioniModifica.err_"+property,
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
	
	// Combo uso primario
	if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.size() > 0) 
  {
		for(int i = 0; i < elencoTipiUsoSuolo.size(); i++) 
    {
			TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo.elementAt(i);
			htmpl.newBlock("blkOperazioniModifica.blkTipiUsoSuolo");
			htmpl.set("blkOperazioniModifica.blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
			htmpl.set("blkOperazioniModifica.blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
			if(idTipoUtilizzo != null && tipoUtilizzoVO.getIdUtilizzo().compareTo(idTipoUtilizzo) == 0) {
				htmpl.set("blkOperazioniModifica.blkTipiUsoSuolo.selected", "selected=\"selected\"");
			}
		}
	}
	
	
	if(Validator.isNotEmpty(vTipoDestinazione))
  {
    for(int i=0;i<vTipoDestinazione.size();i++) 
    {
      TipoDestinazioneVO tipoDestinazioneVO = vTipoDestinazione.get(i);
      htmpl.newBlock("blkOperazioniModifica.blkTipiDestinazione");
      
      htmpl.set("blkOperazioniModifica.blkTipiDestinazione.idTipoDestinazione", ""+tipoDestinazioneVO.getIdTipoDestinazione());
      
      if(idTipoDestinazione != null && new Long(tipoDestinazioneVO.getIdTipoDestinazione()).compareTo(idTipoDestinazione) == 0) 
      {
        htmpl.set("blkOperazioniModifica.blkTipiDestinazione.selected", "selected=\"selected\"", null);
      }
      
      htmpl.set("blkOperazioniModifica.blkTipiDestinazione.descCompleta", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+tipoDestinazioneVO.getDescrizioneDestinazione());
      String descrizione = null;
      if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
      {
        descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
      }
      else 
      {
        descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
      }
      htmpl.set("blkOperazioniModifica.blkTipiDestinazione.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
    }
  }
	
	if(Validator.isNotEmpty(vTipoDettaglioUso))
  {
    for(int i=0;i<vTipoDettaglioUso.size();i++) 
    {
      TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUso.get(i);
      htmpl.newBlock("blkOperazioniModifica.blkTipiDettaglioUso");
      
      htmpl.set("blkOperazioniModifica.blkTipiDettaglioUso.idTipoDettaglioUso", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
      
      if(idTipoDettaglioUso != null && new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).compareTo(idTipoDettaglioUso) == 0) 
      {
        htmpl.set("blkOperazioniModifica.blkTipiDettaglioUso.selected", "selected=\"selected\"", null);
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
      htmpl.set("blkOperazioniModifica.blkTipiDettaglioUso.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
    }
  }
  
  if(Validator.isNotEmpty(vTipoQualitaUso))
  {
    for(int i=0;i<vTipoQualitaUso.size();i++) 
    {
      TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUso.get(i);
      htmpl.newBlock("blkOperazioniModifica.blkTipiQualitaUso");
      
      htmpl.set("blkOperazioniModifica.blkTipiQualitaUso.idTipoQualitaUso", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
      
      if(idTipoQualitaUso != null && new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).compareTo(idTipoQualitaUso) == 0) 
      {
        htmpl.set("blkOperazioniModifica.blkTipiQualitaUso.selected", "selected=\"selected\"", null);
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
      htmpl.set("blkOperazioniModifica.blkTipiQualitaUso.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
    }
  }
  
  // Combo varietà primaria
  if(Validator.isNotEmpty(elencoVarieta)) 
  {
    for(int i = 0; i < elencoVarieta.size(); i++) 
    {
      TipoVarietaVO tipoVarietaVO = elencoVarieta.get(i);
      
      htmpl.newBlock("blkOperazioniModifica.blkTipiVarieta");
      htmpl.set("blkOperazioniModifica.blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
      htmpl.set("blkOperazioniModifica.blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
      if(idVarieta != null && tipoVarietaVO.getIdVarieta().compareTo(idVarieta) == 0) 
      {
        htmpl.set("blkOperazioniModifica.blkTipiVarieta.selected", "selected=\"selected\"", null);
      }
    }
  }
	
	// Combo uso secondario
	if(elencoTipiUsoSuoloSecondario != null && elencoTipiUsoSuoloSecondario.size() > 0) {
		for(int i = 0; i < elencoTipiUsoSuoloSecondario.size(); i++) {
			TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuoloSecondario.elementAt(i);
			htmpl.newBlock("blkOperazioniModifica.blkTipiUsoSuoloSecondario");
			htmpl.set("blkOperazioniModifica.blkTipiUsoSuoloSecondario.idTipoUtilizzoSecondario", tipoUtilizzoVO.getIdUtilizzo().toString());
			htmpl.set("blkOperazioniModifica.blkTipiUsoSuoloSecondario.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
			if(idTipoUtilizzoSecondario != null && tipoUtilizzoVO.getIdUtilizzo().compareTo(idTipoUtilizzoSecondario) == 0) {
				htmpl.set("blkOperazioniModifica.blkTipiUsoSuoloSecondario.selected", "selected=\"selected\"");
			}
		}
	}
	
	if(Validator.isNotEmpty(vTipoDestinazioneSecondario))
  {
    for(int i=0;i<vTipoDestinazioneSecondario.size();i++) 
    {
      TipoDestinazioneVO tipoDestinazioneVO = vTipoDestinazioneSecondario.get(i);
      htmpl.newBlock("blkOperazioniModifica.blkTipiDestinazioneSecondario");
      
      htmpl.set("blkOperazioniModifica.blkTipiDestinazioneSecondario.idTipoDestinazioneSecondario", ""+tipoDestinazioneVO.getIdTipoDestinazione());
      
      if(idTipoDestinazioneSecondario != null && new Long(tipoDestinazioneVO.getIdTipoDestinazione()).compareTo(idTipoDestinazioneSecondario) == 0) 
      {
        htmpl.set("blkOperazioniModifica.blkTipiDestinazioneSecondario.selected", "selected=\"selected\"", null);
      }
      
      htmpl.set("blkOperazioniModifica.blkTipiDestinazioneSecondario.descCompleta", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+tipoDestinazioneVO.getDescrizioneDestinazione());
      String descrizione = null;
      if(tipoDestinazioneVO.getDescrizioneDestinazione().length() > 20) 
      {
        descrizione = tipoDestinazioneVO.getDescrizioneDestinazione().substring(0, 20);
      }
      else 
      {
        descrizione = tipoDestinazioneVO.getDescrizioneDestinazione();
      }
      htmpl.set("blkOperazioniModifica.blkTipiDestinazioneSecondario.descrizione", "["+tipoDestinazioneVO.getCodiceDestinazione()+"] "+descrizione);
    }
  }
	
	if(Validator.isNotEmpty(vTipoDettaglioUsoSecondario))
  {
    for(int i=0;i<vTipoDettaglioUsoSecondario.size();i++) 
    {
      TipoDettaglioUsoVO tipoDettaglioUsoVO = vTipoDettaglioUsoSecondario.get(i);
      htmpl.newBlock("blkOperazioniModifica.blkTipiDettaglioUsoSecondario");
      
      htmpl.set("blkOperazioniModifica.blkTipiDettaglioUsoSecondario.idTipoDettaglioUsoSecondario", ""+tipoDettaglioUsoVO.getIdTipoDettaglioUso());
      
      if(idTipoDettaglioUsoSecondario != null && new Long(tipoDettaglioUsoVO.getIdTipoDettaglioUso()).compareTo(idTipoDettaglioUsoSecondario) == 0) 
      {
        htmpl.set("blkOperazioniModifica.blkTipiDettaglioUsoSecondario.selected", "selected=\"selected\"", null);
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
      htmpl.set("blkOperazioniModifica.blkTipiDettaglioUsoSecondario.descrizione", "["+tipoDettaglioUsoVO.getCodiceDettaglioUso()+"] "+descrizione);
    }
  }
  
  if(Validator.isNotEmpty(vTipoQualitaUsoSecondario))
  {
    for(int i=0;i<vTipoQualitaUsoSecondario.size();i++) 
    {
      TipoQualitaUsoVO tipoQualitaUsoVO = vTipoQualitaUsoSecondario.get(i);
      htmpl.newBlock("blkOperazioniModifica.blkTipiQualitaUsoSecondario");
      
      htmpl.set("blkOperazioniModifica.blkTipiQualitaUsoSecondario.idTipoQualitaUsoSecondario", ""+tipoQualitaUsoVO.getIdTipoQualitaUso());
      
      if(idTipoQualitaUsoSecondario != null && new Long(tipoQualitaUsoVO.getIdTipoQualitaUso()).compareTo(idTipoQualitaUsoSecondario) == 0) 
      {
        htmpl.set("blkOperazioniModifica.blkTipiQualitaUsoSecondario.selected", "selected=\"selected\"", null);
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
      htmpl.set("blkOperazioniModifica.blkTipiQualitaUsoSecondario.descrizione", "["+tipoQualitaUsoVO.getCodiceQualitaUso()+"] "+descrizione);
    }
  }
  
  // Combo varietà secondaria
  if(Validator.isNotEmpty(elencoVarietaSecondaria)) 
  {
    for(int i = 0; i < elencoVarietaSecondaria.size(); i++) 
    {
      TipoVarietaVO tipoVarietaVO = elencoVarietaSecondaria.get(i);
      htmpl.newBlock("blkOperazioniModifica.blkTipiVarietaSecondaria");
      htmpl.set("blkOperazioniModifica.blkTipiVarietaSecondaria.idVarietaSecondaria", tipoVarietaVO.getIdVarieta().toString());
      htmpl.set("blkOperazioniModifica.blkTipiVarietaSecondaria.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+tipoVarietaVO.getDescrizione());
      if(idVarietaSecondaria != null && tipoVarietaVO.getIdVarieta().compareTo(idVarietaSecondaria) == 0) 
      {
        htmpl.set("blkOperazioniModifica.blkTipiVarietaSecondaria.selected", "selected=\"selected\"", null);
      }
    }
  }
	
	
	
	// Combo titolo possesso
	if(elencoTipoTitoloPossesso != null && elencoTipoTitoloPossesso.length > 0) {
		for(int i = 0; i < elencoTipoTitoloPossesso.length; i++) {
			CodeDescription codeDescription = (CodeDescription)elencoTipoTitoloPossesso[i];
			htmpl.newBlock("blkOperazioniModifica.blkTitoliPossesso");
			htmpl.set("blkOperazioniModifica.blkTitoliPossesso.idTitoloPossesso", codeDescription.getCode().toString());
			htmpl.set("blkOperazioniModifica.blkTitoliPossesso.descrizione", codeDescription.getDescription());
			if(idTitoloPossesso != null && idTitoloPossesso.compareTo(Long.decode(codeDescription.getCode().toString())) == 0) {
				htmpl.set("blkOperazioniModifica.blkTitoliPossesso.selected", "selected=\"selected\"");
			}
		}
	}
	
	// Data cessazione
	if(request.getAttribute("controlloData") != null) {
		htmpl.set("controlloData", (String)request.getAttribute("controlloData"));
	}
	if(Validator.isNotEmpty(dataCessazioneStr)) {
		htmpl.set("blkOperazioniModifica.dataCessazione", dataCessazioneStr);
	}
	
	
	// Combo unità produttive
	if(elencoUte != null && elencoUte.size() > 0) {
		for(int i = 0; i < elencoUte.size(); i++) {
			UteVO uteVO = (UteVO)elencoUte.elementAt(i);
			htmpl.newBlock("blkOperazioniModifica.blkUte");
			htmpl.set("blkOperazioniModifica.blkUte.idUnitaProduttiva", uteVO.getIdUte().toString());
			String descrizione = uteVO.getComuneUte().getDescom()+" - "+uteVO.getIndirizzo();
			htmpl.set("blkOperazioniModifica.blkUte.descrizione", descrizione);
		}
	}
	
	// Combo documenti
	if(elencoDocumenti != null && elencoDocumenti.length > 0) 
	{
		String descrizione = null;
		for(int i = 0; i < elencoDocumenti.length; i++) 
		{
			DocumentoVO documentoVO = (DocumentoVO)elencoDocumenti[i];
			if(!"S".equalsIgnoreCase(documentoVO.getFlagIstanzaRiesame()))
      {
				htmpl.newBlock("blkOperazioniModifica.blkElencoDocumenti");
				htmpl.set("blkOperazioniModifica.blkElencoDocumenti.idDocumento", documentoVO.getIdDocumento().toString());
				descrizione = documentoVO.getTipoDocumentoVO().getDescrizione();
				if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) {
					descrizione += " Prot. "+StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo());
				}
				if(documentoVO.getDataProtocollo() != null) {
					descrizione += " del "+DateUtils.formatDate(documentoVO.getDataProtocollo());
				}
				htmpl.set("blkOperazioniModifica.blkElencoDocumenti.descrizione", descrizione);
		  }
		}
	}
  
  //Combo uso agronomico
  if(elencoTipoDichiarazioneUsoAgronomico != null && elencoTipoDichiarazioneUsoAgronomico.length > 0) {
    for(int i = 0; i < elencoTipoDichiarazioneUsoAgronomico.length; i++) {
      CodeDescription codeVO = elencoTipoDichiarazioneUsoAgronomico[i];
      htmpl.newBlock("blkOperazioniModifica.blkTipoDichiarazioneUsoAgronomico");
      htmpl.set("blkOperazioniModifica.blkTipoDichiarazioneUsoAgronomico.idTipoDichiarazioneUsoAgronomico", 
        codeVO.getCodeFlag());
      htmpl.set("blkOperazioniModifica.blkTipoDichiarazioneUsoAgronomico.descrizione", 
        codeVO.getDescription());
        
      if(idTipoDichiarazioneUsoAgronomico != null && codeVO.getCodeFlag().equalsIgnoreCase(idTipoDichiarazioneUsoAgronomico)) {
        htmpl.set("blkOperazioniModifica.blkTipoDichiarazioneUsoAgronomico.selected", "selected=\"selected\"");
      }
    }
  }
  
  //Combo irrigazione
  if(elencoIrrigazione != null && elencoIrrigazione.length > 0) {
    for(int i = 0; i < elencoIrrigazione.length; i++) {
      TipoIrrigazioneVO tipoIrrVO = elencoIrrigazione[i];
      htmpl.newBlock("blkOperazioniModifica.blkIrrigazione");
      htmpl.set("blkOperazioniModifica.blkIrrigazione.idTipoIrrigazione", 
        tipoIrrVO.getIdIrrigazione().toString());
      htmpl.set("blkOperazioniModifica.blkIrrigazione.descrizione", 
        tipoIrrVO.getDescrizione());
        
      if(idTipoIrrigazione != null && tipoIrrVO.getIdIrrigazione().toString().equalsIgnoreCase(idTipoIrrigazione)) {
        htmpl.set("blkOperazioniModifica.blkIrrigazione.selected", "selected=\"selected\"");
      }
    }
  }
  
  //Percentuale Possesso
  if(Validator.isNotEmpty(request.getParameter("percentualePossessoIn"))) 
  {
    htmpl.set("blkOperazioniModifica.percentualePossessoIn", request.getParameter("percentualePossessoIn"));
  }
	
	// Elenco particelle selezionate
  int contaElementi = 0;
	if(elencoStoricoParticellaVO != null && elencoStoricoParticellaVO.length > 0) 
  {
		for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
    {
			StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];
			ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO)storicoParticellaVO.getElencoConduzioni()[0];
			UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO)conduzioneParticellaVO.getElencoUtilizzi()[0];
			htmpl.newBlock("blkOperazioniModifica.blkElencoParticelle");
			
      
      if(Validator.isNotEmpty(vPraticheIdParticella))
      {
        if(vPraticheIdParticella.contains(storicoParticellaVO.getIdParticella()))
        {
          htmpl.set("blkOperazioniModifica.blkElencoParticelle.pratiche", MessageFormat.format(htmlStringKO,
            new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT+"'",
            AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_JAVASCRIPT}), null);
        }
      }
      
      
      if(Validator.isNotEmpty(vIdConduzioniModProcVITIUV))
      {
        if(vIdConduzioniModProcVITIUV.contains(conduzioneParticellaVO.getIdConduzioneParticella()))
        {
          htmpl.set("blkOperazioniModifica.blkElencoParticelle.pratiche", MessageFormat.format(htmlStringKO,
            new Object[] {pathErrori + "/"+ imko,"'"+AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_ELIMINA+"'",
            AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_ELIMINA}), null);
        }
      }
      
      
      
      
      
      
      
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.idConduzioneParticella", conduzioneParticellaVO.getIdConduzioneParticella().toString());
			htmpl.set("blkOperazioniModifica.blkElencoParticelle.indice", String.valueOf(i));
			htmpl.set("blkOperazioniModifica.blkElencoParticelle.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
			if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia())) {
				htmpl.set("blkOperazioniModifica.blkElencoParticelle.siglaProvinciaParticella", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
				htmpl.set("blkOperazioniModifica.blkElencoParticelle.sezione", storicoParticellaVO.getSezione());					
			}
			htmpl.set("blkOperazioniModifica.blkElencoParticelle.foglio", storicoParticellaVO.getFoglio());
			if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
				htmpl.set("blkOperazioniModifica.blkElencoParticelle.particella", storicoParticellaVO.getParticella());					
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
				htmpl.set("blkOperazioniModifica.blkElencoParticelle.subalterno", storicoParticellaVO.getSubalterno());					
			}
			htmpl.set("blkOperazioniModifica.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
			
      
      //Aggiunta eleggibilità fittizia come tooltip
      String tooltipDescElegFit = "";
      String classiEleggibilita = "";
      if(Validator.isNotEmpty(storicoParticellaVO.getvSupElegFit()))
      {
        for(int k=0;k<storicoParticellaVO.getvSupElegFit().size();k++)
        {
           tooltipDescElegFit += storicoParticellaVO.getvSupElegFit().get(k).getDescrizione()+": ";
           classiEleggibilita += storicoParticellaVO.getvSupElegFit().get(k).getDescrizione()+": ";
           tooltipDescElegFit += Formatter.formatDouble4(storicoParticellaVO
            .getvSupElegFit().get(k).getSuperficie()) +" ha\n";
           classiEleggibilita += Formatter.formatDouble4(storicoParticellaVO
            .getvSupElegFit().get(k).getSuperficie()) +" ha<br>";
        }          
      }
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.tooltipDescElegFit", tooltipDescElegFit);
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.classiEleggibilita", classiEleggibilita, null);
      
      
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.idTitoloPossesso", conduzioneParticellaVO.getIdTitoloPossesso().toString());
      BigDecimal percentualePossessoTmp = conduzioneParticellaVO.getPercentualePossesso();
      if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
      {
        percentualePossessoTmp = new BigDecimal(1);
      }
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
			htmpl.set("blkOperazioniModifica.blkElencoParticelle.supCondotta", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
			if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieAgronomica())) {
				if(erroriSupAgronomiche == null || erroriSupAgronomiche.size() == 0) {
					htmpl.set("blkOperazioniModifica.blkElencoParticelle.superficieAgronomica", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica()));
				}
				else {
					htmpl.set("blkOperazioniModifica.blkElencoParticelle.superficieAgronomica", conduzioneParticellaVO.getSuperficieAgronomica());
				}
			}
			if(session.getAttribute("oldSuperficieAgronomica") != null) {
				htmpl.set("blkOperazioniModifica.blkElencoParticelle.oldSuperficieAgronomica", StringUtils.parseSuperficieField(((String[])session.getAttribute("oldSuperficieAgronomica"))[i]));
			}
      
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.supSpandibile", StringUtils.parseSuperficieField(storicoParticellaVO.getSupSpandibile()));
			// Costruisco gli eventuali errori relativi alla superficie agronomica
			if(erroriSupAgronomiche != null && erroriSupAgronomiche.size() > 0 && i < erroriSupAgronomiche.size()) 
      {
	    	String errore = (String)erroriSupAgronomiche.elementAt(i);
       	if(errore != null) 
        {
        	htmpl.set("blkOperazioniModifica.blkElencoParticelle.err_superficieAgronomica",
                      MessageFormat.format(htmlStringKO,
                      new Object[] {
                      pathErrori + "/"+ imko,
                      "'"+jssp.process(errore)+"'",
                      errore}),
                      null);
        }
		  }
      
      BigDecimal supEleggibile = new BigDecimal("0");
      if((storicoParticellaVO.getParticellaCertificataVO() != null)
        && (storicoParticellaVO.getParticellaCertificataVO().getVParticellaCertEleg() != null))
      {
        ParticellaCertElegVO partCertVO = (ParticellaCertElegVO)storicoParticellaVO
          .getParticellaCertificataVO().getVParticellaCertEleg().get(0);
        supEleggibile = partCertVO.getSuperficie();        
      }
      
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.supEleggibile", 
        Formatter.formatDouble4(supEleggibile));
      
      
			if(utilizzoParticellaVO.getIdUtilizzoParticella().longValue() != -1) 
      {
				String usoPrimario = "["+utilizzoParticellaVO.getTipoUtilizzoVO().getCodice()+"] "+utilizzoParticellaVO.getTipoUtilizzoVO().getDescrizione();
				if(utilizzoParticellaVO.getTipoDestinazione() != null && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDestinazione().getDescrizioneDestinazione())) 
        {
          usoPrimario += " - ["+utilizzoParticellaVO.getTipoDestinazione().getCodiceDestinazione()+"] "+utilizzoParticellaVO.getTipoDestinazione().getDescrizioneDestinazione();
        }
				if(utilizzoParticellaVO.getTipoDettaglioUso() != null && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDettaglioUso().getDescrizione())) 
        {
          usoPrimario += " - ["+utilizzoParticellaVO.getTipoDettaglioUso().getCodiceDettaglioUso()+"] "+utilizzoParticellaVO.getTipoDettaglioUso().getDescrizione();
        }
        if(utilizzoParticellaVO.getTipoQualitaUso() != null && Validator.isNotEmpty(utilizzoParticellaVO.getTipoQualitaUso().getDescrizioneQualitaUso())) 
        {
          usoPrimario += " - ["+utilizzoParticellaVO.getTipoQualitaUso().getCodiceQualitaUso()+"] "+utilizzoParticellaVO.getTipoQualitaUso().getDescrizioneQualitaUso();
        }
        if(utilizzoParticellaVO.getTipoVarietaVO() != null && Validator.isNotEmpty(utilizzoParticellaVO.getTipoVarietaVO().getDescrizione())) 
        {
          usoPrimario += " - ["+utilizzoParticellaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+utilizzoParticellaVO.getTipoVarietaVO().getDescrizione();
          //htmpl.set("blkOperazioniModifica.blkElencoParticelle.idVarietaPrimario", ""+utilizzoParticellaVO.getTipoVarietaVO().getIdVarieta().longValue());
        }
				htmpl.set("blkOperazioniModifica.blkElencoParticelle.usoPrimario", usoPrimario);
        htmpl.set("blkOperazioniModifica.blkElencoParticelle.idUtilizzoPrimario", ""+utilizzoParticellaVO.getTipoUtilizzoVO().getIdUtilizzo().longValue());
				if(utilizzoParticellaVO.getTipoUtilizzoSecondarioVO() != null) 
				{
					String usoSecondario = "["+utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getCodice()+"] "+utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getDescrizione();
					if(utilizzoParticellaVO.getTipoDestinazioneSecondario() != null && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione())) 
          {
            usoSecondario += " - ["+utilizzoParticellaVO.getTipoDestinazioneSecondario().getCodiceDestinazione()+"] "+utilizzoParticellaVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione();
          }
					if(utilizzoParticellaVO.getTipoDettaglioUsoSecondario() != null && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getDescrizione())) 
	        {
	          usoSecondario += " - ["+utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getCodiceDettaglioUso()+"] "+utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getDescrizione();
	        }
	        if(utilizzoParticellaVO.getTipoQualitaUsoSecondario() != null && Validator.isNotEmpty(utilizzoParticellaVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso())) 
          {
            usoSecondario += " - ["+utilizzoParticellaVO.getTipoQualitaUsoSecondario().getCodiceQualitaUso()+"] "+utilizzoParticellaVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso();
          }
          if(utilizzoParticellaVO.getTipoVarietaSecondariaVO() != null && Validator.isNotEmpty(utilizzoParticellaVO.getTipoVarietaSecondariaVO().getDescrizione())) 
          {
            usoSecondario += " - ["+utilizzoParticellaVO.getTipoVarietaSecondariaVO().getCodiceVarieta()+"] "+utilizzoParticellaVO.getTipoVarietaSecondariaVO().getDescrizione();
            //htmpl.set("blkOperazioniModifica.blkElencoParticelle.idVarietaSecondario", ""+utilizzoParticellaVO.getTipoVarietaSecondariaVO().getIdVarieta().longValue());
          }
          htmpl.set("blkOperazioniModifica.blkElencoParticelle.idUtilizzoSecondario", ""+utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getIdUtilizzo().longValue());
					htmpl.set("blkOperazioniModifica.blkElencoParticelle.usoSecondario", usoSecondario);
				}
			}
			if(conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) != 0)
			{
			  htmpl.set("blkOperazioniModifica.blkElencoParticelle.supUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
      }
      // Costruisco gli eventuali errori relativi alla superficie agronomica
      if(erroriFruttaGuscio != null && erroriFruttaGuscio.size() > 0 && i < erroriFruttaGuscio.size()) 
      {
        String errore = (String)erroriFruttaGuscio.elementAt(i);
        if(errore != null) 
        {
          htmpl.set("blkOperazioniModifica.blkElencoParticelle.err_usoPrimario",
                      MessageFormat.format(htmlStringKO,
                      new Object[] {
                      pathErrori + "/"+ imko,
                      "'"+jssp.process(errore)+"'",
                      errore}),
                      null);
        }
      }
      
      if(erroriDettaglioUso != null && erroriDettaglioUso.size() > 0 && i < erroriDettaglioUso.size()) 
      {
        String errore = (String)erroriDettaglioUso.elementAt(i);
        if(errore != null) 
        {
          htmpl.set("blkOperazioniModifica.blkElencoParticelle.err_dettUso",
                      MessageFormat.format(htmlStringKO,
                      new Object[] {
                      pathErrori + "/"+ imko,
                      "'"+jssp.process(errore)+"'",
                      errore}),
                      null);
        }
      }
      
      
      
      
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.numTotaleUtilizzo", ""+contaElementi);
      if(Validator.isNotEmpty( utilizzoParticellaVO.getTipoUtilizzoVO()))
      {
        htmpl.set("blkOperazioniModifica.blkElencoParticelle.coefficienteRiduzione", Formatter.formatDouble2(
          utilizzoParticellaVO.getTipoUtilizzoVO().getCoefficienteRiduzione()));
      }
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.supNetta", Formatter.formatDouble4(utilizzoParticellaVO.getSupNetta()));
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.supUtilizzoEleggibile", Formatter.formatDouble4(utilizzoParticellaVO.getSupEleggibile()));
      htmpl.set("blkOperazioniModifica.blkElencoParticelle.supUtilizzoEleggibileNetta", Formatter.formatDouble4(utilizzoParticellaVO.getSupEleggibileNetta()));
      
      
      
      if(conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) != 0)
      {
	      if(primaVolta)
	      {
	        htmpl.set("blkOperazioniModifica.blkElencoParticelle.oldSupUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
	      }
	      else
	      {
	        htmpl.set("blkOperazioniModifica.blkElencoParticelle.oldSupUtilizzata", StringUtils.parseSuperficieField(arrOldSupUtilizzata[i]));
	      }
	    }
      
      
      
      if(session.getAttribute("oldSupUtilizzoEleggibile") != null) 
      {
        htmpl.set("blkOperazioniModifica.blkElencoParticelle.oldSupUtilizzoEleggibile", Formatter.formatDouble4(((BigDecimal[])session.getAttribute("oldSupUtilizzoEleggibile"))[i]));
      }
      
      if(session.getAttribute("oldSupUtilizzoEleggibileNetta") != null) 
      {
        htmpl.set("blkOperazioniModifica.blkElencoParticelle.oldSupUtilizzoEleggibileNetta", Formatter.formatDouble4(((BigDecimal[])session.getAttribute("oldSupUtilizzoEleggibileNetta"))[i]));
      }
      
      
      
			// Costruisco gli eventuali errori relativi alla superficie ad uso primario
			if(errori != null && errori.size() > 0 && i < errori.size()) 
      {
	    	String errore = (String)errori.elementAt(i);
       	if(errore != null) 
        {
        	htmpl.set("blkOperazioniModifica.blkElencoParticelle.err_supUtilizzata",
                      MessageFormat.format(htmlStringKO,
                      new Object[] {
                      pathErrori + "/"+ imko,
                      "'"+jssp.process(errore)+"'",
                      errore}),
                      null);
        }        
        
		  }
			htmpl.set("blkOperazioniModifica.blkElencoParticelle.supUtilizzataSecondaria", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
      if(primaVolta)
      {
        htmpl.set("blkOperazioniModifica.blkElencoParticelle.oldSupUtilizzataSecondaria", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
      }
      else
      {
        htmpl.set("blkOperazioniModifica.blkElencoParticelle.oldSupUtilizzataSecondaria", StringUtils.parseSuperficieField(arrOldSupUtilizzataSecondaria[i]));
      }
			// Costruisco gli eventuali errori relativi alla superficie ad uso primario
			if(erroriSupSecondaria != null && erroriSupSecondaria.size() > 0 && i < erroriSupSecondaria.size()) 
      {
		   	String errore = (String)erroriSupSecondaria.elementAt(i);
		   	if(errore != null) 
        {
		     	htmpl.set("blkOperazioniModifica.blkElencoParticelle.err_supUtilizzataSecondaria",
		                MessageFormat.format(htmlStringKO,
                    new Object[] {
                    pathErrori + "/"+ imko,
                    "'"+jssp.process(errore)+"'",
                    errore}),
                    null);
		     }
		   }
        
        
      contaElementi++;
		}
	}
    
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }	

%>
<%= htmpl.text()%>

