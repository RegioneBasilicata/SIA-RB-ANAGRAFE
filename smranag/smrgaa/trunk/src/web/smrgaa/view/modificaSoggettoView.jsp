<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/contitolari_mod.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  PersonaFisicaVO personaModificaVO = (PersonaFisicaVO)session.getAttribute("personaModificaVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Se l'utente ha selezionato un titolare/rappresentante legale non permetto
  // all'utente di modificare la parte relativa ai ruoli
  if(personaModificaVO.getTipiRuoloNonTitolareAndNonSpecificato() != null &&
     personaModificaVO.getTipiRuoloNonTitolareAndNonSpecificato().compareTo(Long.decode((String)SolmrConstants.get("TIPORUOLO_TITOL_RAPPR_LEG"))) ==0) {
    htmpl.newBlock("blkTitolare");
    htmpl.set("blkTitolare.descRuolo", personaModificaVO.getRuolo());
    htmpl.set("blkTitolare.tipiRuoloNonTitolareAndNonSpecificato", personaModificaVO.getTipiRuoloNonTitolareAndNonSpecificato().toString());
    
    htmpl.set("blkTitolare.strDataInizioRuolo", DateUtils.formatDate(personaModificaVO.getDataInizioRuolo()));
    if(Validator.isNotEmpty(personaModificaVO.getDataFineRuolo())) {
      htmpl.set("blkTitolare.strDataFineRuolo", DateUtils.formatDate(personaModificaVO.getDataFineRuolo()));
    }
    
    // Data inizio ruolo mod
    if(Validator.isNotEmpty(personaModificaVO.getDataInizioRuoloMod())) {
      htmpl.set("blkTitolare.strDataInizioRuoloMod", DateUtils.formatDate(personaModificaVO.getDataInizioRuoloMod()));
    }
    else {
      if(Validator.isNotEmpty(personaModificaVO.getStrDataInizioRuoloMod())) {
        htmpl.set("blkTitolare.strDataInizioRuoloMod", personaModificaVO.getStrDataInizioRuoloMod());
      }
    }
    
    if(Validator.isNotEmpty(personaModificaVO.getDataFineRuoloMod())) {
      htmpl.set("blkTitolare.strDataFineRuoloMod", DateUtils.formatDate(personaModificaVO.getDataFineRuoloMod()));
    }
  }
  else {
    htmpl.newBlock("blkNoTitolare");
    // Popolo la combo dei ruoli che non deve contenere rappresentante legale/titolare
    // e ruolo non specificato
    Vector<CodeDescription> elencoRuoli = null;
    try {
      elencoRuoli = anagFacadeClient.getTipiRuoloNonTitolareAndNonSpecificato();
    }
    catch(SolmrException se) {}

    if(elencoRuoli != null && elencoRuoli.size() > 0) {
      Iterator<CodeDescription> iteraRuoli = elencoRuoli.iterator();
      while(iteraRuoli.hasNext()) {
        htmpl.newBlock("blkNoTitolare.elencoRuoli");
        CodeDescription code = (CodeDescription)iteraRuoli.next();
        if(personaModificaVO.getTipiRuoloNonTitolareAndNonSpecificato() != null) {
          if(personaModificaVO.getTipiRuoloNonTitolareAndNonSpecificato().compareTo(Long.decode(code.getCode().toString())) == 0) {
            htmpl.set("blkNoTitolare.elencoRuoli.check", "selected");
          }
        }
        htmpl.set("blkNoTitolare.elencoRuoli.idCodice", code.getCode().toString());
        htmpl.set("blkNoTitolare.elencoRuoli.descrizione", code.getDescription());
      }
    }

    // Data inizio ruolo
    if(Validator.isNotEmpty(personaModificaVO.getDataInizioRuolo())) {
      htmpl.set("blkNoTitolare.strDataInizioRuolo", DateUtils.formatDate(personaModificaVO.getDataInizioRuolo()));
    }

    // Data fine ruolo
    if(Validator.isNotEmpty(personaModificaVO.getDataFineRuolo())) {
      htmpl.set("blkNoTitolare.strDataFineRuolo", DateUtils.formatDate(personaModificaVO.getDataFineRuolo()));
    }
    
    
    // Data inizio ruolo mod
    if(Validator.isNotEmpty(personaModificaVO.getDataInizioRuoloMod())) {
      htmpl.set("blkNoTitolare.strDataInizioRuoloMod", DateUtils.formatDate(personaModificaVO.getDataInizioRuoloMod()));
    }
    else {
      if(Validator.isNotEmpty(personaModificaVO.getStrDataInizioRuoloMod())) {
        htmpl.set("blkNoTitolare.strDataInizioRuoloMod", personaModificaVO.getStrDataInizioRuoloMod());
      }
    }

    // Data fine ruolo mod
    if(Validator.isNotEmpty(personaModificaVO.getDataFineRuoloMod())) {
      htmpl.set("blkNoTitolare.strDataFineRuoloMod", DateUtils.formatDate(personaModificaVO.getDataFineRuoloMod()));
    }
    else {
      if(Validator.isNotEmpty(personaModificaVO.getStrDataFineRuoloMod())) {
        htmpl.set("blkNoTitolare.strDataFineRuoloMod", personaModificaVO.getStrDataFineRuoloMod());
      }
    }
  }

  // Setto il parametro relativo al codice fiscale. Se è corretto lo rendo disable
  // in caso contrario lo rendo modificabile
  String isOk = (String)session.getAttribute("isOk");
  if(Validator.isNotEmpty(personaModificaVO.getCodiceFiscale())) {
    htmpl.set("codiceFiscale", personaModificaVO.getCodiceFiscale().toUpperCase());
    if(Validator.isNotEmpty(isOk)) {
      if(isOk.equalsIgnoreCase("true")) {
        htmpl.set("readOnly", "readOnly");
      }
    }
  }

  // Sesso
  if(personaModificaVO.getSesso() != null) {
    if(personaModificaVO.getSesso().equalsIgnoreCase("M")) {
      htmpl.set("checkedM", "checked");
    }
    else {
      if(personaModificaVO.getSesso().equalsIgnoreCase("F")) {
        htmpl.set("checkedF", "checked");
      }
    }
  }

	if(personaModificaVO.getdesNumeroCellulare() != null)
	 	htmpl.set("CellulareNumero", personaModificaVO.getdesNumeroCellulare());
	 	
  // Informazioni relative al titolo di studio
  Vector elencoTitoliStudio = null;
  Long idTitoloStudio = (Long)session.getAttribute("idTitoloStudio");
  try {
    elencoTitoliStudio = anagFacadeClient.getTitoliStudio();
  }
  catch(SolmrException se) {}

  if(elencoTitoliStudio != null) {
    Iterator iteraTitoliStudio = elencoTitoliStudio.iterator();
    while(iteraTitoliStudio.hasNext()) {
      htmpl.newBlock("elencoTitoliStudio");
      CodeDescription code = (CodeDescription)iteraTitoliStudio.next();
      if(personaModificaVO.getIdTitoloStudio() != null) {
        if(personaModificaVO.getIdTitoloStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
          htmpl.set("elencoTitoliStudio.check","selected");
        }
      }
      htmpl.set("elencoTitoliStudio.idCodice", code.getCode().toString());
      htmpl.set("elencoTitoliStudio.descrizione", code.getDescription());
    }

    Vector elencoIndirizziStudio = null;
    if(personaModificaVO.getIdTitoloStudio() != null) {
      try {
        elencoIndirizziStudio = anagFacadeClient.getIndirizzoStudioByTitolo(personaModificaVO.getIdTitoloStudio());
      }
      catch(SolmrException se) {}
      if(elencoIndirizziStudio != null) {
        Iterator iteraIndirizziStudio = elencoIndirizziStudio.iterator();
        while(iteraIndirizziStudio.hasNext()) {
          htmpl.newBlock("elencoIndirizziStudio");
          CodeDescription codeIndirizzo = (CodeDescription)iteraIndirizziStudio.next();
          if(personaModificaVO.getIdIndirizzoStudio() != null) {
            if(personaModificaVO.getIdIndirizzoStudio().compareTo(Long.decode(codeIndirizzo.getCode().toString())) == 0) {
              htmpl.set("elencoIndirizziStudio.check","selected");
            }
          }
          htmpl.set("elencoIndirizziStudio.idCodice",codeIndirizzo.getCode().toString());
          htmpl.set("elencoIndirizziStudio.descrizione",codeIndirizzo.getDescription());
        }
      }
    }
  }

  HtmplUtil.setValues(htmpl, personaModificaVO);
  HtmplUtil.setErrors(htmpl, errors ,request, application);

%>

<%= htmpl.text()%>
