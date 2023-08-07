<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%!
  public static String ATTENDERE_URL="/view/allevamentiVerificaAttendereView.jsp";
%>


<%

  String iridePageName = "allevamentiVerificaAttendereCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  WebUtils.removeUselessFilter(session, null);

  Integer anno = DateUtils.getCurrentYear();
  request.setAttribute("anno",anno);
  %>
    <jsp:forward page="<%=ATTENDERE_URL%>" />
