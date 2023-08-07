<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "cessazioneCausaleAttivitaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String elencoUteUrl = "/view/sediView.jsp";
  String cessazioneCausaleAttivitaUrl = "/view/cessazioneCausaleAttivitaView.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  ValidationErrors errors = new ValidationErrors();

  Long idElemento = Long.decode(request.getParameter("idElemento"));
  request.setAttribute("idElemento", idElemento);
  String operazione = request.getParameter("operazione");
  boolean storico = request.getParameter("storico") != null;

  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  String notifica = "";
  try {
    anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) {
    request.setAttribute("statoAzienda", se);
  }

  // L'utente ha selezionato il pulsante conferma
  if(request.getParameter("conferma") != null) 
  {

    // Recupero la causale cessazione se è stata inserita
    String causaleCessazione = request.getParameter("causaleCessazione");

    // Controllo che se è stata valorizzata non sia più lunga di 100 caratteri
    if(Validator.isNotEmpty(causaleCessazione)) {
      if(causaleCessazione.length() > 100) {
        request.setAttribute("causaleCessazione", causaleCessazione);
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_NOTE_UTILIZZO_PARTICELLA"));
        errors.add("causaleCessazione", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(cessazioneCausaleAttivitaUrl).forward(request, response);
        return;
      }
    }

    // Effettuo la cessazione dell'unità produttiva selezionata
    UteVO uteVO = new UteVO();
    uteVO.setCausaleCessazione(causaleCessazione);
    uteVO.setIdUte(idElemento);
    try {
      anagFacadeClient.cessazioneUTE(uteVO, ruoloUtenza.getIdUtente());
    }
    catch(SolmrException se) {
      ValidationError error=new ValidationError(se.getMessage());
      errors = new ValidationErrors();
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(cessazioneCausaleAttivitaUrl).forward(request, response);
      return;
    }

    // Effettuo nuovamente la ricerca delle unità produttive
    // Rieffettuo la ricerca degli allevamenti
    Vector elencoUte = null;
    try {
      elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(),
                                          new Boolean(storico));
    }
    catch(SolmrException se) {
      if(!se.getMessage().equals((String)AnagErrors.get("RICERCA_TERRENI_UTE"))) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(elencoUteUrl).forward(request, response);
        return;
      }
      else {
        session.removeAttribute("v_ute");
      }
    }

    // Metto il vettore in sessione
    if(elencoUte != null && elencoUte.size() > 0) {
      session.setAttribute("v_ute", elencoUte);
    }

    // Torno alla pagina di elenco delle unità produttive
    %>
      <jsp:forward page="<%= elencoUteUrl %>"/>
    <%

  }
  // L'utente ha selezionato il pulsante annulla
  else if(request.getParameter("annulla") != null) {
    if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_UTE"))) {
      // Rieffettuo la ricerca degli allevamenti
      Vector elencoUte = null;
      try {
        elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(),
                                            new Boolean(storico));
      }
      catch(SolmrException se) {
        if(!se.getMessage().equals((String)AnagErrors.get("RICERCA_TERRENI_UTE"))) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(elencoUteUrl).forward(request, response);
          return;
        }
        else {
          session.removeAttribute("v_ute");
        }
      }

      // Metto il vettore in sessione
      if(elencoUte != null && elencoUte.size() > 0) {
        session.setAttribute("v_ute", elencoUte);
      }

      // Torno alla pagina di elenco delle ute
      %>
        <jsp:forward page="<%= elencoUteUrl %>"/>
      <%
    }
  }
  // L'utente ha selezionato la funzionalità cessazione
  else {
    // L'utente arriva dall'elenco delle ute
    if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_UTE"))) {

      // Controllo che l'unità produttiva selezionata non sia cessata
      UteVO uteVO = null;
      try {
        uteVO = anagFacadeClient.getUteById(idElemento);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(elencoUteUrl).forward(request, response);
        return;
      }

      // Rieffettuo la ricerca delle unità produttive
      Vector elencoUte = null;
      try {
        elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(),
                                            new Boolean(storico));
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(elencoUteUrl).forward(request, response);
        return;
      }

      // Metto il vettore in sessione
      session.setAttribute("v_ute", elencoUte);

      if(uteVO.getDataFineAttivita() != null) {
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_RECORD_STORICIZZATO"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(elencoUteUrl).forward(request, response);
        return;
      }

      // Controllo che, se l'utente ha selezionato la data fine validità, non esistano conduzioni
      // attive altrimenti segnalo l'errore all'utente
      try {
        anagFacadeClient.checkCessaAziendaByConduzioneParticella(idElemento);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(elencoUteUrl).forward(request, response);
        return;
      }
    }
    request.setAttribute("idElemento", idElemento);
    request.setAttribute("storico", new Boolean(storico));
    %>
      <jsp:forward page="<%= cessazioneCausaleAttivitaUrl %>"/>
    <%
  }

%>
