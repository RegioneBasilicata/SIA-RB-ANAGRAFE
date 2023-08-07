<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/erroreAutCompetenzaDato.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String msgErrore="Errore!" +(String)request.getAttribute("errorMessage");

  htmpl.set("errore",msgErrore);

  out.print(htmpl.text());

%>

