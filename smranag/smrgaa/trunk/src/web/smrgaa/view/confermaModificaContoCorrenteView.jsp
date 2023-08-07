<%@ page language="java" contentType="text/html" isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/confermaModificaContoCorrente.htm");

  HtmplUtil.setErrors(htmpl, (ValidationErrors) request.getAttribute("errors"), request, application);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  htmpl.set("idContoCorrente",request.getParameter("idContoCorrente"));
  htmpl.set("cin",request.getParameter("cin"));
  htmpl.set("abi",request.getParameter("abi"));
  htmpl.set("cab",request.getParameter("cab"));
  htmpl.set("numeroContoCorrente",request.getParameter("numeroContoCorrente"));
  htmpl.set("cctrl",request.getParameter("cctrl"));
  htmpl.set("iban",request.getParameter("iban"));
  htmpl.set("intestatario",request.getParameter("intestatario"));
  htmpl.set("motivoValidazione",request.getParameter("motivoValidazione"));
  htmpl.set("flagContoGf",request.getParameter("flagContoGf"));

  out.print(htmpl.text());

%>
