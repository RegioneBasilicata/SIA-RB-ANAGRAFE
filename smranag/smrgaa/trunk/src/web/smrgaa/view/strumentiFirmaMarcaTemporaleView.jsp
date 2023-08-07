<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors"%>
<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.dto.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.etc.*"%>

<%
	java.io.InputStream layout = application.getResourceAsStream("/layout/strumentiFirmaMarca.htm");
	Htmpl htmpl = new Htmpl(layout);
%>

<%@include file="/view/remoteInclude.inc"%>

<%
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session.getAttribute("anagAziendaVO");

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);

	String sourceImage = null;
	if (pathToFollow.equalsIgnoreCase("rupar")) {
		sourceImage = application.getInitParameter("erroriRupar");
	} else if (pathToFollow.equalsIgnoreCase("sispie")) {
		sourceImage = application.getInitParameter("erroriSispie");
	} else if (pathToFollow.equalsIgnoreCase("TOBECONFIG")) {
		sourceImage = application.getInitParameter("erroriTOBECONFIG");
	}
	
	if((String)session.getAttribute("fileName")!= null){
		htmpl.set("fileName",(String)session.getAttribute("fileName"));
	}else{
		htmpl.set("fileName","Nessun file caricato");
	}
	
	
	if((String)session.getAttribute("fileName2")!= null){
		htmpl.set("fileName2",(String)session.getAttribute("fileName2"));
	}else{
		htmpl.set("fileName2","Nessun file caricato");
	}
	
	

%>
<%=htmpl.text()%>
