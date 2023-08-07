<%@ page language="java"
    contentType="text/html"
    isErrorPage="false"
%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors"%>

<%!
  private static final String VIEW="/view/monitoraggioswhttpView.jsp";
%>
<%

  String iridePageName = "monitoraggioswhttpCtrl.jsp";
  %>
  <%@include file = "/include/autorizzazione.inc" %>
  <%
  SolmrLogger.debug(this, "monitoraggioswhttpCtrl.jsp - Begin");

  it.csi.solmr.client.anag.AnagFacadeClient anagFacadeClient = new it.csi.solmr.client.anag.AnagFacadeClient();
  String resultMonitoraggio = AnagErrors.MONITORAGGIO_OK;
  String problemiDB=null;
  try
  {
    problemiDB=anagFacadeClient.testDB();
    if (problemiDB!=null) resultMonitoraggio = AnagErrors.MONITORAGGIO_KO;
  }
  catch(Exception ex)
  {
    resultMonitoraggio = AnagErrors.MONITORAGGIO_KO;  
    problemiDB=ex.getMessage();
  }
  request.setAttribute("resultMonitoraggio", resultMonitoraggio);
  request.setAttribute("problemiDB", problemiDB);
  
  SolmrLogger.debug(this, "monitoraggioswhttpCtrl.jsp - End");
%>
<jsp:forward page="<%=VIEW%>"/>