  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>

<%
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/ricercaPersona.htm");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  //Valorizza segnaposto htmpl
  HtmplUtil.setValues(htmpl, request);
  //Valorizza segnaposto errore htmpl
  HtmplUtil.setErrors(htmpl, errors, request, application);

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  if ("true".equals(request.getAttribute("primaRicerca")))
  {
    htmpl.set("checkedPersonaAttiva", "checked");
  }
  else
    if ( request.getParameter("personaAttiva") != null && request.getParameter("personaAttiva").trim().equalsIgnoreCase("si") )
      htmpl.set("checkedPersonaAttiva", "checked");

%>
<%= htmpl.text()%>
