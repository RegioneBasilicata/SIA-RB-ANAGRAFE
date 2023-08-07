<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/confirmModResidenza.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  String form = (String)request.getAttribute("urlForm");
  htmpl.set("form", form);

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("personaVO");


%>

<%= htmpl.text()%>
