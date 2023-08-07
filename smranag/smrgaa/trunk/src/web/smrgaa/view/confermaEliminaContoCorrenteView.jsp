<%@ page language="java" contentType="text/html" isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/confermaEliminaContoCorrente.htm");

  HtmplUtil.setErrors(htmpl, (ValidationErrors) request.getAttribute("errors"), request, application);
  htmpl.set("idContoCorrente",request.getParameter("idContoCorrente"));
  String referer=request.getHeader("referer");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  if (referer!=null && referer.toLowerCase().endsWith("conti_correnti_det.htm"))
  {
    // Arrivo dal dettaglio
    htmpl.set("pageFrom","dettaglio");
    htmpl.set("chiudiURL","../layout/conti_correnti_det.htm");
  }
  else
  {
    // Arrivo dall'elenco
    htmpl.set("pageFrom","elenco");
    htmpl.set("chiudiURL","../layout/conti_correnti.htm");
  }

  out.print(htmpl.text());
%>
