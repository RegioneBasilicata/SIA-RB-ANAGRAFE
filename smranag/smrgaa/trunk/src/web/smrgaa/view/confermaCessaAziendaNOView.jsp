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
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/confermaCessaAziendaNO.htm");

  HtmplUtil.setErrors(htmpl, (ValidationErrors) request.getAttribute("errors"), request, application);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  htmpl.set("p_msg",(String)request.getAttribute("p_msg"),null);

  out.print(htmpl.text());

%>
