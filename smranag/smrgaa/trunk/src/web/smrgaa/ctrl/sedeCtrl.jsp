<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.util.*" %>

<%

  String iridePageName = "sedeCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  SolmrLogger.debug(this, "- sedeCtrl.jsp -  INIZIO PAGINA");
  %>
    <jsp:forward page="/view/sedeView.jsp"/>
  <%
  SolmrLogger.debug(this, "- sedeCtrl.jsp -  INIZIO PAGINA");
%>