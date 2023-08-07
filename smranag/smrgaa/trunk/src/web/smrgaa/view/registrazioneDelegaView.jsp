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
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<jsp:useBean id="delegaVO" scope="request" class="it.csi.solmr.dto.anag.DelegaVO"/>

<%

	SolmrLogger.debug(this, " - registrazioneDelegaView.jsp - INIZIO PAGINA");

  	Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/registrazioneDelega.htm");

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%

  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);
    
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  	String dataInizioMandato = (String)request.getAttribute("dataInizioMandato");

  	htmpl.set("CUAA",anagAziendaVO.getCUAA() );
  	htmpl.set("partitaIVA",anagAziendaVO.getPartitaIVA() );
  	htmpl.set("denominazione",anagAziendaVO.getDenominazione());

  	String sedeLegale = "";
  	if(anagAziendaVO.getSedelegComune() != null) {
    	if(Validator.isNotEmpty(anagAziendaVO.getSedelegIndirizzo())) {
      		sedeLegale += anagAziendaVO.getSedelegIndirizzo() + " ";
    	}
    	if(Validator.isNotEmpty(anagAziendaVO.getDescComune())) {
      		sedeLegale += anagAziendaVO.getDescComune() + " (" +anagAziendaVO.getSedelegProv() + ")";
    	}
  	}
  	else {
    	if(anagAziendaVO.getSedelegEstero() != null) {
      		sedeLegale += anagAziendaVO.getSedelegEstero();
    	}
    	if(anagAziendaVO.getSedelegCittaEstero() != null) {
      		sedeLegale += " (" + anagAziendaVO.getSedelegCittaEstero() + ")";
    	}
  	}
  	htmpl.set("sedeLegale", sedeLegale);
	
  	if(Validator.isNotEmpty(dataInizioMandato)) {
  		htmpl.set("dataInizioMandato", dataInizioMandato);
  	}
  	
  	Vector elencoUfficiZonaIntermediario = (Vector)request.getAttribute("elencoUfficiZonaIntermediario");
  	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  	UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = (UfficioZonaIntermediarioVO)request.getAttribute("ufficioZonaIntermediarioVO");
  	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  	if(Validator.isNotEmpty(messaggioErrore)) {
    	htmpl.newBlock("blkErrore");
    	htmpl.set("blkErrore.messaggio", messaggioErrore);
  	}

  	if(elencoUfficiZonaIntermediario != null && elencoUfficiZonaIntermediario.size() > 0) {
  		Iterator iteraUffici = elencoUfficiZonaIntermediario.iterator();
    	while(iteraUffici.hasNext()) {
    		CodeDescription code = (CodeDescription)iteraUffici.next();
      		htmpl.newBlock("blkElencoUfficiZonaIntermediario");
      		htmpl.set("blkElencoUfficiZonaIntermediario.codice",String.valueOf(code.getCode().intValue()));
      		htmpl.set("blkElencoUfficiZonaIntermediario.descrizione", code.getDescription());
      		if(ufficioZonaIntermediarioVO != null) {
        		if(code.getCode().toString().compareTo(ufficioZonaIntermediarioVO.getIdUfficioZonaIntermediario().toString()) == 0) {
          			htmpl.set("blkElencoUfficiZonaIntermediario.selected" , "selected=\"selected\"");
        		}
      		}
    	}
  	}

  	if(ufficioZonaIntermediarioVO != null) {
    	htmpl.set("codiceAgea", StringUtils.parseCodiceAgea(ufficioZonaIntermediarioVO.getCodiceAgea()));
    	htmpl.set("denominazioneUfficio", ufficioZonaIntermediarioVO.getDenominazione());
    	HtmplUtil.setValues(htmpl, ufficioZonaIntermediarioVO);
  	}
    
    //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
    //al ruolo!
    String dateUlt = "";
    if(anagAziendaVO.getDataUltimaModifica() !=null)
    {
      dateUlt = DateUtils.formatDate(anagAziendaVO.getDataUltimaModifica());
    }
    ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
      "ultimaModificaVw", dateUlt, ruoloUtenza.getDenominazione(),
      ruoloUtenza.getDescrizioneEnte(), null);

  	HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
