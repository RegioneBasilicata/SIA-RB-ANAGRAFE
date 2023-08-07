le$<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "elencoRicercheCtrl.jsp"; 
  %>
     <%@include file = "/include/autorizzazione.inc" %>
  <%

  String elencoRicercheUrl = "/view/elencoRicercheView.jsp";
  String mandatiUrl = "/view/mandatiView.jsp";
  String fascicoliUrl = "/view/fascicoliView.jsp";
  String reportisticaValidazioniUrl = "/view/reportisticaValidazioniView.jsp";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Vector elencoMandati = null;
  Vector elencoCAA = null;
  Vector elencoUfficiZonaIntermediario = null;

  // L'utente ha cliccato sul pulsante conferma
  if(Validator.isNotEmpty(request.getParameter("conferma"))) {
    // Recupero il tipo ricerca selezionato dall'utente
    String tipoRicerca = request.getParameter("tipoRicerca");
    // L'utente ha selezionato il riepilogo per mandati e validazioni
    /***********************************************************************
    	Gestione Unificazione dei report : "Riepilogo Mandati"
    									   "Riepilogo Validazioni"
    	@Author : Garonzi Alessio
    	@since : 17 Ott 2007
    	@description : Unificazione dei 2 report in un unico con scarico in
    				   Excel dei dati visualizzati
    ************************************************************************/
    if(tipoRicerca.equalsIgnoreCase((String)SolmrConstants.get("RIEPILOGO_PER_MANDATO"))) {

      try {
       
      
        String dal = "01/01/"+DateUtils.getCurrentYear();
        Date dataDal = DateUtils.parseDate(dal);
        Date dataAl = DateUtils.parseDate(DateUtils.getCurrent());

        elencoMandati = anagFacadeClient.getMandatiByUtente(utenteAbilitazioni, false,dataDal,dataAl);

      
      }
      catch(SolmrException se) {
        request.setAttribute("messaggioErrore", se.getMessage());
        %>
          <jsp:forward page= "<%= mandatiUrl %>" />
        <%
      }
      request.setAttribute("isDefault","Y");
      request.setAttribute("elencoMandati",elencoMandati);
      %>
        <jsp:forward page= "<%= mandatiUrl %>" />
      <%
    }
    // L'utente ha selezionato l'elenco fascicoli
    else if(tipoRicerca.equalsIgnoreCase((String)SolmrConstants.get("ELENCO_FASCICOLI"))) {

      // Ripulisco la sessione dai dati che posso aver inserito nel caso di operazioni precedenti e inseriti
      // in funzione dello scarico dei dati su file excel
      session.removeAttribute("elencoAziende");
      session.removeAttribute("indice");

      // Ricerco l'elenco dei CAA in funzione dell'utente loggato
      try {
        elencoCAA = anagFacadeClient.getElencoCAAByUtente(utenteAbilitazioni);
        if(elencoCAA != null && elencoCAA.size() == 1) {
          CodeDescription code = (CodeDescription)elencoCAA.elementAt(0);
          Long idIntermediario = Long.decode(code.getCode().toString());
          elencoUfficiZonaIntermediario = anagFacadeClient
            .getElencoUfficiZonaIntermediarioByIdIntermediario(utenteAbilitazioni);
        }
      }
      catch(SolmrException se) {
        request.setAttribute("messaggioErrore", se.getMessage());
        %>
          <jsp:forward page= "<%= fascicoliUrl %>" />
        <%
      }
      request.setAttribute("elencoCAA", elencoCAA);
      request.setAttribute("elencoUfficiZonaIntermediario", elencoUfficiZonaIntermediario);
      %>
        <jsp:forward page= "<%= fascicoliUrl %>" />
      <%
    }
    // L'utente ha selezionato la funzione "riepilogo validazioni"
    else if(tipoRicerca.equalsIgnoreCase((String)SolmrConstants.get("RIEPILOGO_VALIDAZIONI"))) {
      String dal = "01/01/"+DateUtils.getCurrentYear();
      Date dataDal = DateUtils.parseDate(dal);
      Date dataAl = DateUtils.parseDate(DateUtils.getCurrent());

      // Metto le date in request per visualizzarle come default nella pagina di elenco
      request.setAttribute("dal", dataDal);
      request.setAttribute("al", dataAl);

      try {
        elencoMandati = anagFacadeClient.getMandatiValidatiByUtente(utenteAbilitazioni, false, dataDal, dataAl);
      }
      catch(SolmrException se) {
        request.setAttribute("messaggioErrore", se.getMessage());
        %>
          <jsp:forward page= "<%= reportisticaValidazioniUrl %>" />
        <%
      }
      request.setAttribute("elencoMandati",elencoMandati);
      %>
        <jsp:forward page= "<%= reportisticaValidazioniUrl %>" />
      <%
    }
  }
  // L'utente ha selezionato la funzionalità "altre ricerche"
  else {
    // Vado alla pagina di elenco delle notifiche
    %>
      <jsp:forward page= "<%= elencoRicercheUrl %>" />
    <%
  }

%>
