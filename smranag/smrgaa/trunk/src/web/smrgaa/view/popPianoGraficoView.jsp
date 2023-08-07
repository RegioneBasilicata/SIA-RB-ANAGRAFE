<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popPianoGrafico.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
 	
 	// Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);


 	String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
 	String idAccessoPianoGrafico = request.getParameter("idAccessoPianoGrafico");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	
 	
 	
 	htmpl.set("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
 	htmpl.set("idAccessoPianoGrafico", idAccessoPianoGrafico);
 	
 	if(Validator.isNotEmpty(messaggioErrore)) 
 	{
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  else
 	{
 	
 	}
 	
 	

%>
<%=htmpl.text()%>
