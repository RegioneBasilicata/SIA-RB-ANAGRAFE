<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>

<%@ page import="java.text.*"%>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/documentoConfermaProtocollo.htm");

 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 // Setto l'area di provenienza
 htmpl.set("pageFrom", "documentale");
 
 
 if ("stampaConfermaInserisciPdf".equals(request.getParameter("stampaConfermaInserisciPdf")))
 {
   htmpl.set("stampaProtocollo", "stampaProtocolloImmediata()");
 }
 else
 {
   htmpl.set("stampaProtocollo", "");
 }

%>
<%= htmpl.text()%>
