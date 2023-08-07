<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
  Vector dichiarazioniConsistenza=null;
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/terreniVerifica_ok.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagFacadeClient client = new AnagFacadeClient();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  HtmplUtil.setErrors(htmpl, (ValidationErrors)request.getAttribute("errors"), request, application);

  htmpl.set("denominazione", anagVO.getDenominazione());

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  out.print(htmpl.text());
%>
