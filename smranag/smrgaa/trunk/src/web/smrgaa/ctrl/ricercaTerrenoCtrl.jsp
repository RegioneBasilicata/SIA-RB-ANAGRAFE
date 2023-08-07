<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "ricercaTerrenoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String ricercaTerrenoUrl = "/view/ricercaTerrenoView.jsp";
  String elencoTerreniUrl = "/ctrl/elencoTerreniCtrl.jsp";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  ValidationErrors errors = new ValidationErrors();

  // L'utente ha premuto il pulsante ricerca
  if(request.getParameter("ricerca") != null) {

    // Recupero il VO
    FiltriRicercaTerrenoVO filtriRicercaTerrenoVO = new FiltriRicercaTerrenoVO();

    // Recupero i parametri inseriti dall'utente
    String siglaProvinciaParticella = request.getParameter("siglaProvinciaParticella");
    String descComuneParticella = request.getParameter("descComuneParticella");
    String descStatoEsteroParticella = request.getParameter("descStatoEsteroParticella");
    String sezione = request.getParameter("sezione");
    String strFoglioRicerca = request.getParameter("strFoglioRicerca");
    String strParticella = request.getParameter("strParticella");
    boolean isParticellaProvvisoria = request.getParameter("particellaProvvisoria") != null;
    String subalterno = request.getParameter("subalterno");
    boolean isParticellaAttiva = request.getParameter("isParticellaAttiva") != null;

    // Setto i valori all'interno del VO
    filtriRicercaTerrenoVO.setSiglaProvinciaParticella(toUpper(siglaProvinciaParticella));
    filtriRicercaTerrenoVO.setDescComuneParticella(toUpper(descComuneParticella));
    filtriRicercaTerrenoVO.setDescStatoEsteroParticella(toUpper(descStatoEsteroParticella));
    filtriRicercaTerrenoVO.setSezione(toUpper(sezione));
    filtriRicercaTerrenoVO.setFoglio(strFoglioRicerca);
    filtriRicercaTerrenoVO.setParticella(strParticella);
    filtriRicercaTerrenoVO.setParticellaProvvisoria(isParticellaProvvisoria);
    filtriRicercaTerrenoVO.setSubalterno(toUpper(subalterno));
    filtriRicercaTerrenoVO.setParticellaAttiva(isParticellaAttiva);

    session.setAttribute("filtriRicercaTerrenoVO", filtriRicercaTerrenoVO);

    // Effettuo la validazione formale dei dati
    errors = filtriRicercaTerrenoVO.validateRicercaTerreno();

    // Se ci sono errori formali li visualizzo
    if(errors != null && errors.size() > 0) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(ricercaTerrenoUrl).forward(request, response);
      return;
    }

    // Effettuo la validazione logica dei dati
    // Se sono stati valorizzati la provincia e il comune controllo che esistano
    String istatComune = null;
    if(Validator.isNotEmpty(filtriRicercaTerrenoVO.getSiglaProvinciaParticella()) &&
       Validator.isNotEmpty(filtriRicercaTerrenoVO.getDescComuneParticella())) {
       try {
         istatComune = anagFacadeClient.ricercaCodiceComuneFlagEstinto(filtriRicercaTerrenoVO.getDescComuneParticella(),
                                                                      filtriRicercaTerrenoVO.getSiglaProvinciaParticella(), null);
       }
       catch(SolmrException se) {
         ValidationError error = new ValidationError(se.getMessage());
         errors.add("descComuneParticella",error);
         request.setAttribute("errors", errors);
         request.getRequestDispatcher(ricercaTerrenoUrl).forward(request, response);
         return;
       }
    }
    // Altrimenti controllo che lo stato estero inserito sia esistente
    else {
      try {
        istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(filtriRicercaTerrenoVO.getDescStatoEsteroParticella(),
                                                                     "");
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_INESISTENTE"));
        errors.add("descStatoEsteroParticella",error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(ricercaTerrenoUrl).forward(request, response);
        return;
      }
    }

    // Setto il valore all'interno del VO
    filtriRicercaTerrenoVO.setIstatComuneParticella(istatComune);
    
    session.setAttribute("filtriRicercaTerrenoVO", filtriRicercaTerrenoVO);

    %>
      <jsp:forward page= "<%= elencoTerreniUrl %>" />
    <%
  }
  // L'utente ha selezionato la funzione ricerca terreno
  else {
    // Dal momento che si tratta di un nuovo nodo di partenza ripulisco la sessione
    // da tutti gli oggetti
    WebUtils.removeUselessAttributes(session);

    // Creo l'oggetto per la ricerca
    FiltriRicercaTerrenoVO filtriRicercaTerrenoVO = new FiltriRicercaTerrenoVO();

    // Se l'utente che si è collegato è un funzionario provinciale, setto la
    // provincia della particella uguale a quella dell'utente
    if(ruoloUtenza.isUtenteProvinciale()) {
      // Ricerco le informazioni dell'utente per ottenerne la provincia
      filtriRicercaTerrenoVO.setSiglaProvinciaParticella(ruoloUtenza.getSiglaProvincia());
    }

    // Inoltro setto il valore di particella attiva a true
    filtriRicercaTerrenoVO.setParticellaAttiva(true);

    // Metto l'oggetto in sessione
    session.setAttribute("filtriRicercaTerrenoVO", filtriRicercaTerrenoVO);

    // Vado alla pagina di ricerca terreno
    %>
      <jsp:forward page= "<%= ricercaTerrenoUrl %>" />
    <%
  }


%><%!   
  public static String toUpper(String str)
  {
    return str==null?"":str.toUpperCase();
  }%>