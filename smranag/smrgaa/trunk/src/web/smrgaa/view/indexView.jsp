  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%@ page import="java.net.*" %>

<%

  String iridePageName = "indexView.jsp";
  request.setAttribute("iridePageNameForCU", iridePageName);
  %>
     <%@include file = "/include/autorizzazione.inc" %>
  <%

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/index.htm");
  WebUtils.removeUselessAttributes(session);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  htmpl.set("head", head, null);


%>
<%= htmpl.text() %>
