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

  java.io.InputStream layout = application.getResourceAsStream("/layout/confermaCF.htm");
  Htmpl htmpl = new Htmpl(layout);

%>
<%= htmpl.text()%>


