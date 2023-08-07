<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>


<%
  String iridePageName = "serviziCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  SolmrLogger.debug(this, " - serviziCtrl.jsp - INIZIO PAGINA");
  
  String serviziViewUrl = "/view/serviziView.jsp";
  String trasferimentoUfficioCtrlUrl = "/ctrl/trasferimentoUfficioCtrl.jsp";
  
  String prova = request.getParameter("conferma");
  
  
  // L'utente ha cliccato sul pulsante conferma
  if(Validator.isNotEmpty(request.getParameter("conferma"))) 
  {
    // Recupero il tipo ricerca selezionato dall'utente
    String tipoServizio = request.getParameter("tipoServizio");
    
    if(tipoServizio.equalsIgnoreCase((String)SolmrConstants.get("SERVIZI_TRASFERIMENTO_UFFICIO"))) 
    {
      SolmrLogger.debug(this, " - serviziCtrl.jsp - FINE PAGINA");
      %> <jsp:forward page= "<%= trasferimentoUfficioCtrlUrl %>" />
      <%
    }
    
  }
  else
  {
    SolmrLogger.debug(this, " - serviziCtrl.jsp - FINE PAGINA");
    %> <jsp:forward page= "<%= serviziViewUrl %>" />
    <%
  }
%>