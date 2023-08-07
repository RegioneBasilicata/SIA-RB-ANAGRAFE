<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.text.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/modificaGestoreFascicolo.htm");
  	Htmpl htmpl = new Htmpl(layout);

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%

    // Nuova gestione fogli di stile
    htmpl.set("head", head, null);
    htmpl.set("header", header, null);
    htmpl.set("footer", footer, null);
	
	// Gestione immagini errori per i blocchi html
 	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
    					  "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
	StringProcessor jssp = new JavaScriptStringProcessor();
    
    String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  	DelegaVO delegaVO = (DelegaVO)request.getAttribute("delegaVO");
  	UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = (UfficioZonaIntermediarioVO)request.getAttribute("ufficioZonaIntermediarioVO");
  	DoubleStringcodeDescription[] elencoOPR = (DoubleStringcodeDescription[])request.getAttribute("elencoOPR");
  	Long idOpr = (Long)request.getAttribute("idOpr");
  	RespAnagFascicoloVO respAnagFascicoloVO = (RespAnagFascicoloVO)request.getAttribute("iSWSRespAnagFascicolo");
  	String messaggioErroreData = (String)request.getAttribute("messaggioErroreData");
  	String messaggioErroreDataChiusura = (String)request.getAttribute("messaggioErroreDataChiusura");
  	
  	if(Validator.isNotEmpty(messaggioErrore)) {
  		htmpl.newBlock("blkGestioneErrore");
  		htmpl.set("blkGestioneErrore.messaggioErrore", messaggioErrore);
  	}
  	else {
  		htmpl.newBlock("blkFunzioneOk");
  		if(delegaVO == null) {
  			htmpl.newBlock("blkFunzioneOk.blkKo");
  		}
  		else {
  			htmpl.newBlock("blkFunzioneOk.blkDatiIntermediario");
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.denomIntermediario", delegaVO.getDenomIntermediario());
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.dataInizioMandato", DateUtils.formatDate(delegaVO.getDataInizioMandato()));
        
        //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
        //al ruolo!
        String dateUlt = "";
        if(delegaVO.getDataInizio() !=null)
        {
          dateUlt = DateUtils.formatDate(delegaVO.getDataInizio());
        } 
        ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
          "blkFunzioneOk.ultimaModificaVw", dateUlt, delegaVO.getNomeUtenIrideIndDelega(),
          delegaVO.getEnteUtenIrideIndDelega(), null);
        
  			//htmpl.set("blkFunzioneOk.blkDatiIntermediario.dataInizio", DateUtils.formatDate(delegaVO.getDataInizio()));
  			//htmpl.set("blkFunzioneOk.blkDatiIntermediario.nomeUtenIrideIndDelega", delegaVO.getNomeUtenIrideIndDelega());
  			//htmpl.set("blkFunzioneOk.blkDatiIntermediario.enteUtenIrideIndDelega", delegaVO.getEnteUtenIrideIndDelega());
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.codiceAgea", ufficioZonaIntermediarioVO.getCodiceAgea());
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.denominazioneUfficio", ufficioZonaIntermediarioVO.getDenominazione());
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.indirizzo", ufficioZonaIntermediarioVO.getIndirizzo());
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.siglaProvincia", ufficioZonaIntermediarioVO.getSiglaProvincia());
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.descrizioneComune", ufficioZonaIntermediarioVO.getDescrizioneComune());
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.cap", ufficioZonaIntermediarioVO.getCap());
  			htmpl.set("blkFunzioneOk.blkDatiIntermediario.recapito", ufficioZonaIntermediarioVO.getRecapito());
  			for(int i = 0; i < elencoOPR.length; i++) {
  				DoubleStringcodeDescription doubleStringcodeDescription = (DoubleStringcodeDescription)elencoOPR[i];
  				htmpl.newBlock("blkFunzioneOk.blkElencoTipiOpr");
  				htmpl.set("blkFunzioneOk.blkElencoTipiOpr.codice", doubleStringcodeDescription.getFirstCode());
  				htmpl.set("blkFunzioneOk.blkElencoTipiOpr.descrizione", doubleStringcodeDescription.getFirstDescription());
  				if(idOpr != null) {
  					if(idOpr.toString().equalsIgnoreCase(doubleStringcodeDescription.getFirstCode())) {
  						htmpl.set("blkFunzioneOk.blkElencoTipiOpr.selected", "selected=\"selected\"");
  					}
  				}
  			}
  			if(respAnagFascicoloVO != null)  {
  				if(!Validator.isNotEmpty(messaggioErroreData) && !Validator.isNotEmpty(messaggioErroreDataChiusura)) {
	  				htmpl.set("blkFunzioneOk.dataAperturaFascicolo", respAnagFascicoloVO.getDataAperturaFascicolo());
	  				htmpl.set("blkFunzioneOk.dataChiusuraFascicolo", respAnagFascicoloVO.getDataChiusuraFascicolo());
  				}
  				else {
  					if(request.getAttribute("dataAperturaFascicolo") != null) {
  						htmpl.set("blkFunzioneOk.dataAperturaFascicolo", (String)request.getAttribute("dataAperturaFascicolo"));
  					}
  					if(request.getAttribute("dataChiusuraFascicolo") != null) {
	  					htmpl.set("blkFunzioneOk.dataChiusuraFascicolo", (String)request.getAttribute("dataChiusuraFascicolo"));
  					}
  					// Gestione dei messaggi di errore
  					if(Validator.isNotEmpty(messaggioErroreData)) {
  						htmpl.set("blkFunzioneOk.err_dataAperturaFascicolo", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, "'"+jssp.process(messaggioErroreData)+"'", messaggioErroreData}),null);
  					}
  					if(Validator.isNotEmpty(messaggioErroreDataChiusura)) {
  						htmpl.set("blkFunzioneOk.err_dataChiusuraFascicolo", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, "'"+jssp.process(messaggioErroreDataChiusura)+"'", messaggioErroreDataChiusura}),null);
  					}
  				}
  			}
  		}
  	}


%>
<%= htmpl.text()%>