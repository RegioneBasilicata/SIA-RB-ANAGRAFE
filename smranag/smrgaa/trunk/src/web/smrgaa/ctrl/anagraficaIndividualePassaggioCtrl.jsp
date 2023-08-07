<%@ page language="java" contentType="text/html" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%

  String iridePageName = "anagraficaIndividualePassaggioCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String annullaUrl = "/view/anagraficaView.jsp";
  String url = "/view/anagraficaIndividualePassaggioView.jsp";
  String titolareUrl = "/view/anagraficaIndividualePassaggioNuovoTitolareView.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagAziendaVO anagAziendaPassaggioVO = (AnagAziendaVO)session.getAttribute("anagPassaggioVO");

  if (request.getParameter("avanti") != null) {
    // Prelievo dei parametri per la costruzione del value object.
    String dataAvvenutoPassaggio = request.getParameter("dataAvvenutoPassaggio");
    String cuaa = request.getParameter("CUAA");
    String partitaIVA = request.getParameter("partitaIVA");
    String denominazione = request.getParameter("denominazione");

    ValidationErrors errors = new ValidationErrors();

    Integer idTipoAzienda = null;
    if(request.getParameter("tipiAzienda") != null && !request.getParameter("tipiAzienda").equals("")) {
      idTipoAzienda = Integer.decode(request.getParameter("tipiAzienda"));
    }
    String CCIAAProvinciaREA = request.getParameter("CCIAAprovREA");
    Long CCIAANumeroREA = null;
    boolean isNumerico = false;
    if(request.getParameter("strCCIAAnumeroREA") != null && !request.getParameter("strCCIAAnumeroREA").equals("")) {
      isNumerico = Validator.isNumericInteger(request.getParameter("strCCIAAnumeroREA"));
      if(isNumerico == true) {
        try {
          CCIAANumeroREA = Long.decode(request.getParameter("strCCIAAnumeroREA"));
        }
        catch(Exception ex) {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_NUMERO_REA_ERRATO"));
          errors.add("strCCIAAnumeroREA",error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(url).forward(request, response);
          return;
        }
      }
    }
    String codiceOTE = null;
    if(request.getParameter("codiceOTE") != null && !request.getParameter("codiceOTE").equals("")) {
      codiceOTE = request.getParameter("codiceOTE");
    }
    String descrizioneOTE = "";
    if(request.getParameter("descrizioneOTE") != null && !request.getParameter("descrizioneOTE").equals("")) {
      descrizioneOTE = request.getParameter("descrizioneOTE");
    }
    String codiceATECO = null;
    if(request.getParameter("codiceATECO") != null && !request.getParameter("codiceATECO").equals("")) {
      codiceATECO = request.getParameter("codiceATECO");
    }
    String descrizioneATECO = "";
    if(request.getParameter("descrizioneATECO") != null && !request.getParameter("descrizioneATECO").equals("")) {
      descrizioneATECO = request.getParameter("descrizioneATECO");
    }
    String note = request.getParameter("note");
    String CCIAAannoIscrizione = request.getParameter("CCIAAannoIscrizione");
    String CCIAAnumRegImprese = request.getParameter("CCIAAnumRegImprese");

    if(anagAziendaPassaggioVO == null) {
      anagAziendaPassaggioVO = new AnagAziendaVO();
    }

    // Setto il value object
    anagAziendaPassaggioVO.setIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
    anagAziendaPassaggioVO.setIdAzienda(anagAziendaVO.getIdAzienda());
    anagAziendaPassaggioVO.setDataInizioVal(anagAziendaVO.getDataInizioVal());
    anagAziendaPassaggioVO.setDataSituazioneAlStr(anagAziendaVO.getDataSituazioneAlStr());
    anagAziendaPassaggioVO.setDataAvvenutoPassaggio(dataAvvenutoPassaggio);
    anagAziendaPassaggioVO.setCUAA(cuaa.toUpperCase());
    anagAziendaPassaggioVO.setOldCUAA(cuaa.toUpperCase());
    anagAziendaPassaggioVO.setPartitaIVA(partitaIVA);
    anagAziendaPassaggioVO.setDenominazione(denominazione.toUpperCase());
    anagAziendaPassaggioVO.setStrCCIAAnumeroREA(request.getParameter("strCCIAAnumeroREA"));
    anagAziendaPassaggioVO.setTipiFormaGiuridica(String.valueOf(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE));
    anagAziendaPassaggioVO.setProvCompetenza(anagAziendaVO.getProvCompetenza());

    String strFormaGiuridica = null;
    try {
      strFormaGiuridica = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_FORMA_GIURIDICA,
                                                                  SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE);
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    anagAziendaPassaggioVO.setTipoFormaGiuridica(new CodeDescription(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE,strFormaGiuridica));

    if(idTipoAzienda != null) {
      anagAziendaPassaggioVO.setTipiAzienda(String.valueOf(idTipoAzienda));
      String tipiAzienda = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_TIPOLOGIA_AZIENDA, idTipoAzienda);
      anagAziendaPassaggioVO.setTipoTipologiaAzienda(new CodeDescription(idTipoAzienda,tipiAzienda));
    }
    anagAziendaPassaggioVO.setCCIAAprovREA(CCIAAProvinciaREA.toUpperCase());
    anagAziendaPassaggioVO.setCCIAAnumeroREA(CCIAANumeroREA);
    anagAziendaPassaggioVO.setCCIAAnumRegImprese(CCIAAnumRegImprese);
    anagAziendaPassaggioVO.setCCIAAannoIscrizione(CCIAAannoIscrizione);
    if(codiceOTE != null && !codiceOTE.equals("")) {
      Long idCodiceOTE = null;
      try {
        idCodiceOTE = anagFacadeClient.ricercaIdAttivitaOTE(codiceOTE.toString(),descrizioneOTE);
      }
      catch (SolmrException ex) {
        ValidationError error = new ValidationError(ex.getMessage());
        errors.add("codiceOTE", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      try {
        descrizioneOTE = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_ATTIVITA_OTE,new Integer(idCodiceOTE.toString()));
      }
      catch (SolmrException ex) {
        ValidationError error = new ValidationError(ex.getMessage());
        errors.add("codiceOTE", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      CodeDescription descriptionAttivitaOTE = new CodeDescription(Integer.decode(idCodiceOTE.toString()),descrizioneOTE);
      descriptionAttivitaOTE.setSecondaryCode(codiceOTE.toString());
      anagAziendaPassaggioVO.setTipoAttivitaOTE(descriptionAttivitaOTE);
      anagAziendaPassaggioVO.setStrAttivitaOTE(descrizioneOTE);
    }
    if(codiceATECO != null && !codiceATECO.equals("")) {
      Long idCodiceATECO = null;
      try {
        idCodiceATECO = anagFacadeClient.ricercaIdAttivitaATECO(codiceATECO.toString(),descrizioneATECO);
      } catch (SolmrException ex) {
        ValidationError error = new ValidationError(ex.getMessage());
        errors.add("codiceATECO", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      try {
        descrizioneATECO = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_ATTIVITA_ATECO,new Integer(idCodiceATECO.toString()));
      }
      catch (SolmrException ex) {
        ValidationError error = new ValidationError(ex.getMessage());
        errors.add("codiceOTE", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      CodeDescription descriptionAttivitaATECO = new CodeDescription(new Integer(idCodiceATECO.toString()),descrizioneATECO);
      descriptionAttivitaATECO.setSecondaryCode(codiceATECO.toString());
      anagAziendaPassaggioVO.setTipoAttivitaATECO(descriptionAttivitaATECO);
      anagAziendaPassaggioVO.setStrAttivitaOTE(descrizioneATECO);
    }
    anagAziendaPassaggioVO.setNote(note);

    String flagCCIAA = null;

    try {
      flagCCIAA = anagFacadeClient.getFormaGiuridicaFlagCCIAA(new Long(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE.longValue()));
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError("Attenzione: si è verificato un errore durante la ricerca della forma giuridica!");
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
    }
    anagAziendaPassaggioVO.setFlagCCIAA(flagCCIAA);
    String partitaIVAObbligatoria = null;
    try {
      partitaIVAObbligatoria = anagFacadeClient.getFlagPartitaIva(new Long(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE.toString()));
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
    }
    anagAziendaPassaggioVO.setFlagPartitaIva(partitaIVAObbligatoria);
    // Controllo la validità dei dati inseriti dall'utente
    errors = anagAziendaPassaggioVO.validateCambioTitolare();
    // Controllo che non esista già in archivio un'azienda valida con il CUAA e la partita IVA indicati
    try {
      //anagFacadeClient.checkCUAAandCodFiscale(anagAziendaPassaggioVO.getCUAA(), anagAziendaPassaggioVO.getPartitaIVA());
      anagFacadeClient.checkCUAA(anagAziendaPassaggioVO.getCUAA());
      anagFacadeClient.checkPartitaIVA(anagAziendaPassaggioVO.getPartitaIVA(),anagAziendaPassaggioVO.getIdAzienda());
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      if(se.getMessage().equalsIgnoreCase(AnagErrors.CUAA_GIA_ESISTENTE)) {
        errors.add("CUAA", error);
      }
      else if(se.getMessage().equalsIgnoreCase(AnagErrors.PIVA_GIA_ESISTENTE)) {
        errors.add("partitaIVA", error);
      }
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // Controllo la validità della provincia REA
    if(CCIAAProvinciaREA != null && !CCIAAProvinciaREA.equals("")) {
      try {
        boolean isValida = anagFacadeClient.isProvinciaReaValida(CCIAAProvinciaREA.toUpperCase());
        if(!isValida) {
          ValidationError error = new ValidationError(AnagErrors.ERR_PROVINCIA_REA_ERRATA);
          errors.add("CCIAAprovREA",error);
        }
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error",error);
      }
    }
    if(errors != null && errors.size() != 0) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    anagAziendaPassaggioVO.setStrAttivitaOTE(anagAziendaVO.getTipoAttivitaOTE().getDescription());
    anagAziendaPassaggioVO.setStrAttivitaATECO(anagAziendaVO.getTipoAttivitaATECO().getDescription());
    // I controlli relativi ai dati inseriti dall'utente sono stati superati quindi metto il value
    // object in sessione.
    session.removeAttribute("anagPassaggioVO");
    session.setAttribute("anagPassaggioVO",anagAziendaPassaggioVO);
    String ruolo = request.getParameter("radiobutton");
    session.setAttribute("ruolo",ruolo);
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("personaTitolareVO");
    if(personaFisicaVO == null) {
      try {
        personaFisicaVO = anagFacadeClient.getPersonaFisica(cuaa.toUpperCase());
      }
      catch(SolmrException se) {
        if (!se.getMessage().equals(AnagErrors.CUAA_INESISTENTE)) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(url).forward(request, response);
          return;
        }
        else if(se.getMessage().equalsIgnoreCase(AnagErrors.CUAA_INESISTENTE)) {
          personaFisicaVO = new PersonaFisicaVO();
          personaFisicaVO.setNewPersonaFisica(true);
        }
      }
    }
    session.setAttribute("personaTitolareVO",personaFisicaVO);
    %>
      <jsp:forward page="<%= titolareUrl %>"/>
    <%

  }
  else if(request.getParameter("indietro") != null) {
    session.removeAttribute("anagPassaggioVO");
    session.removeAttribute("ruolo");
    session.removeAttribute("personaTitolareVO");
    session.removeAttribute("indietro");
    %>
      <jsp:forward page="<%= annullaUrl %>"/>
    <%
  }

%>
