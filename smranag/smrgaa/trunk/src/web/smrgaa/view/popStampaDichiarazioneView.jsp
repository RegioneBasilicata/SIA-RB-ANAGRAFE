<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>

<%

	SolmrLogger.debug(this, " - popStampaDichiarazioneView.jsp - INIZIO PAGINA");

 	java.io.InputStream layout = application.getResourceAsStream("/layout/popStampaDichiarazione.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
   	<%@include file = "/view/remoteInclude.inc" %>
 	<%
 	
 	String messaggio = (String)request.getAttribute("messaggioErrore");
 	if(Validator.isNotEmpty(messaggio))
 	{
 	  htmpl.newBlock("blkErrore");
 	  htmpl.set("blkErrore.messaggioErrore", messaggio);
 	}
  		
  SolmrLogger.debug(this, " - popStampaDichiarazioneView.jsp - FINE PAGINA");
%>

<%= htmpl.text()%>

