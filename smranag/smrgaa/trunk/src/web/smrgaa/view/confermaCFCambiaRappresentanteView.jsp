  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>


<%

  Htmpl htmpl = HtmplFactory.getInstance(application)
               .getHtmpl("/layout/confermaCFCambioRappresentante.htm");

%>
<%= htmpl.text()%>


