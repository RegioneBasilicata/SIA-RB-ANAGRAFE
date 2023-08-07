<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "sceltaEsportaDatiNotificheCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  SolmrLogger.debug(this, " - sceltaEsportaDatiNotificheCtrl.jsp - INIZIO PAGINA");
  
  String sceltaEsportaDatiNotificheURL = "/view/sceltaEsportaDatiNotificheView.jsp";
  String excelUrl = "/servlet/ExcelElencoNotificheServlet";
  String excelUvUrl = "/servlet/ExcelElencoNotificheUvServlet";
  String excelParticelleUrl = "/servlet/ExcelElencoNotificheParticelleServlet";
  
  
  final String errMsg = "Impossibile procedere nella sezione esporta dati notifiche. " +
    "Contattare l'assistenza comunicando il seguente messaggio: ";    
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String messaggioErrore = null;
  
  
  
  //sono già nella pagine e richiamo uno scarico
  if(request.getParameter("regimeEsportaDatiNotifiche") != null)
  {   
    
    //Scarico UV
    if(Validator.isNotEmpty(request.getParameter("tipoEsportaDatiNotifiche"))
      && request.getParameter("tipoEsportaDatiNotifiche").equalsIgnoreCase("1"))
    {
      %>
        <jsp:forward page="<%=excelUvUrl %>" />
      <%
      return;
    }
    //Particelle
    else if(Validator.isNotEmpty(request.getParameter("tipoEsportaDatiNotifiche"))
      && request.getParameter("tipoEsportaDatiNotifiche").equalsIgnoreCase("2"))
    {
       %>
         <jsp:forward page="<%=excelParticelleUrl %>" />
       <%
       return;
      
      //Eliminato return per sbloccare il tasto nella pagina
    }
    //Scarico notifiche generale (classico)
    else
    {
    
      %>
        <jsp:forward page="<%=excelUrl %>" />
      <%
      return;      
      
    }
      
  }
  else
  { //prima volta che entro
  }
%>
   <jsp:forward page="<%=sceltaEsportaDatiNotificheURL %>" />