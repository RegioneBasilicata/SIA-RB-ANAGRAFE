<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
   String iridePageName = "contitolariATCtrl.jsp";
   %>
      <%@include file = "/include/autorizzazione.inc" %>
   <%

   String contitolariATUrl = "/view/contitolariATView.jsp";
   String action = "../layout/contitolariAT.htm";
   String attenderePregoUrl = "/view/attenderePregoView.jsp";
   
   RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   String operazione = request.getParameter("operazione");
   String CUAAselezionato = request.getParameter("CUAAselezionato");

   // L'utente ha selezionato la voce di menù anagrafe tributaria e io lo mando alla
   // pagina di attesa per il caricamento dati
   if("attenderePrego".equalsIgnoreCase(operazione))
   {
     request.setAttribute("action", action);
     operazione = null;
     request.setAttribute("operazione", operazione);
     request.setAttribute("CUAAselezionato", CUAAselezionato);
     %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
     <%
   }
   else
   {
     // Pulisco la sessione
     session.removeAttribute("anagTrib");

     // Se ha un CUAA valido cerco nell'anagrafe tributaria l'azienda
     // corrispondente
     SianAnagTributariaVO anagTrib = null;
     try 
     {
       anagTrib = anagFacadeClient.ricercaAnagrafica(CUAAselezionato, ProfileUtils.getSianUtente(ruoloUtenza));
     }
     catch(SolmrException se) {
       request.setAttribute("messaggioErrore", se.getMessage());
       %>
          <jsp:forward page="<%= contitolariATUrl %>"/>
       <%
     }
     session.setAttribute("anagTrib", anagTrib);
     }

     %>
      <jsp:forward page="<%= contitolariATUrl %>"/>
     <%
%>

