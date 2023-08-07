<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>



<%!
  public static String VIEW="/view/dichiarazioneVerificaOkView.jsp";
%>


<%

  String iridePageName = "dichiarazioneVerificaOkCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
      %><jsp:forward page="<%=VIEW%>" /><%
%>

