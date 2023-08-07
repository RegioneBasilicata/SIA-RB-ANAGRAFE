<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page  import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page  import="it.csi.smranag.smrgaa.dto.manuali.ManualeVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "elencoManualiCtrl.jsp"; 
  %>
     <%@include file = "/include/autorizzazione.inc" %>
  <%

  String elencoManualiUrl = "/view/elencoManualiView.jsp";
  String actionUrl = "../layout/index.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione Manuali."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  
  try 
  {
    Vector<ManualeVO> vManuali = gaaFacadeClient.getElencoManualiFromRuoli(
      ruoloUtenza.getCodiceRuolo());
      
    request.setAttribute("vManuali", vManuali);      
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - elencoManualiCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

%>

<jsp:forward page= "<%= elencoManualiUrl %>" />
