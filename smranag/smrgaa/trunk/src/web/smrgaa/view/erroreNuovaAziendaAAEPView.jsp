<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%
  SolmrLogger.debug(this, " - erroreNuovaAziendaAAEPView.jsp - INIZIO PAGINA");

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/erroreNuovaAziendaAAEP.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String pageBack=(String)request.getAttribute("pageBack");
  String msgErrore=(String)request.getAttribute("msgErrore");
  String CUAA=(String)request.getAttribute("CUAA");

  htmpl.set("pageBack", pageBack);
  htmpl.set("msgErrore", msgErrore);
  htmpl.set("CUAA", CUAA);
  out.print(htmpl.text());

  SolmrLogger.debug(this, " - erroreNuovaAziendaAAEPView.jsp - FINE PAGINA");
%>
