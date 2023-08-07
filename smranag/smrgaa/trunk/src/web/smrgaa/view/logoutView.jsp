<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/logout.htm");
 Htmpl htmpl = new Htmpl(layout);

%>
<%= htmpl.text()%>
