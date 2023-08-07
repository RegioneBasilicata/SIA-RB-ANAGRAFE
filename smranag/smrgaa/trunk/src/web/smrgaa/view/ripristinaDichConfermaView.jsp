<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/ripristinaDichConferma.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	Long idDichiarazioneConsistenza = (Long)request.getAttribute("idDichiarazioneConsistenza");
	htmpl.set("idDichiarazioneConsistenza", idDichiarazioneConsistenza.toString());
	
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
