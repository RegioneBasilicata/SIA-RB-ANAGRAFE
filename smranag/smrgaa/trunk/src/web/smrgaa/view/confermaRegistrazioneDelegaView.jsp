<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/confermaRegistrazioneDelega.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String pageBack=(String)request.getAttribute("page");
  String domanda=(String)request.getAttribute("domanda");

  htmpl.set("page", pageBack);
  htmpl.set("domanda", domanda);

%>
<%= htmpl.text()%>
