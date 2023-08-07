<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/confermaEliminaFabbricato.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String idFabbricato = request.getParameter("idFabbricato");
  htmpl.set("idFabbricato", idFabbricato);

  String pagina = request.getParameter("pagina");
  if(pagina != null) {
    htmpl.set("pagina", pagina);
  }
  
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
