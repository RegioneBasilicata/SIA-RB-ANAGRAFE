<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.uma.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/servizi.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String tipoServizio = (String)request.getAttribute("tipoServizio");
  if(!Validator.isNotEmpty(tipoServizio) || tipoServizio.equalsIgnoreCase((String)SolmrConstants.get("SERVIZI_TRASFERIMENTO_UFFICIO"))) {
    htmpl.set("blkTipoServizioTrasferimento.checkedT", "checked=\"checked\"");
  }

  HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);
  out.print(htmpl.text());

%>