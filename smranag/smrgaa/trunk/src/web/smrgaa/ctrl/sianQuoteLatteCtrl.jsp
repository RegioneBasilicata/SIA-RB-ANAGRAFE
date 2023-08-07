<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.dto.anag.PersonaFisicaVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

   String iridePageName = "sianQuoteLatteCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

   String sianQuoteLatteUrl = "/view/sianQuoteLatteView.jsp";
   String action = "../layout/quoteLatte.htm";
   String attenderePregoUrl = "/view/attenderePregoView.jsp";
   
   RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   String messaggioErrore = null;
   AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   String operazione = request.getParameter("operazione");

   // L'utente ha selezionato la voce di menù titoli e io lo mando alla
   // pagina di attesa per il caricamento dati
   if("attenderePrego".equalsIgnoreCase(operazione)) {
     request.setAttribute("action", action);
     operazione = null;
     request.setAttribute("operazione", operazione);
     %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
     <%
   }
   else {

     // Se ha un CUAA valido cerco sul SIAN le quote latte corrispondenti
     // relative all'azienda selezionata
     Vector sianQuoteLatteAziendaVO = null;
     try {
       sianQuoteLatteAziendaVO = anagFacadeClient.quoteLatte(
        anagAziendaVO.getCUAA(), null, ProfileUtils.getSianUtente(ruoloUtenza));
       request.setAttribute("sianQuoteLatteAziendaVO", sianQuoteLatteAziendaVO);
     }
     catch(SolmrException se) {
       messaggioErrore = se.getMessage();
       request.setAttribute("messaggioErrore", messaggioErrore);
       %>
          <jsp:forward page="<%= sianQuoteLatteUrl %>"/>
       <%
     }

     %>
        <jsp:forward page="<%= sianQuoteLatteUrl %>"/>
     <%
   }
%>
