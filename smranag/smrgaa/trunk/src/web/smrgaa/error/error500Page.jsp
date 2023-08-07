<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*"%>
<%@ page import="it.csi.jsf.htmpl.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@page import="it.csi.smranag.smrgaa.exception.ErrorPageException"%>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(
      "layout/erroreAutCompetenzaDato.htm");
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  Exception errorReport = (Exception) request.getAttribute("errorReport");

  String msgErrore = it.csi.solmr.etc.anag.AnagErrors.ERRORE_500;
  int gobackCount = -2;
  if (errorReport != null)
  {
    exception = errorReport;
  }

  if (exception != null)
  {
    if (exception instanceof ErrorPageException)
    {
      msgErrore = exception.getMessage();
      gobackCount = ((ErrorPageException) exception).getGobackCount();
    }
    else
    {
      msgErrore += SolmrLogger.getStackTrace(exception);
    }
  }
  htmpl.set("errore", msgErrore);
  htmpl.set("gobackCount", String.valueOf(gobackCount));
  out.print(htmpl.text());
%>