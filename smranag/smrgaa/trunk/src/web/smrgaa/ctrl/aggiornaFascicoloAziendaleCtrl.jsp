<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>

<%

	String iridePageName = "aggiornaFascicoloAziendaleCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	SolmrLogger.debug(this, "BEGIN aggiornaFascicoloAziendaleCtrl");
	   
	  
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	   
	String cuaa = request.getParameter("cuaa");
	SolmrLogger.debug(this, "-- cuaa ="+cuaa);
	

	try {
		RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
		SolmrLogger.debug(this, "-- idUtente loggato ="+ruoloUtenza.getIdUtente());
		
		SolmrLogger.debug(this, "---- Chiamata al servizio aggiornaFascicoloAziendale per l'aggiornamento del fascicolo aziendale");
		SolmrLogger.debug(this, "-- cuaa in input ="+cuaa);
		Boolean esitoAggiornaFascicolo = gaaFacadeClient.aggiornaFascicoloAziendale(cuaa, ruoloUtenza.getIdUtente());
		
		response.reset();
		response.getWriter().write("success");	
		response.flushBuffer();
		return;
	
	}
	catch(SolmrException se) {
		SolmrLogger.error(this, "-- SolmrException in fase di chiamata a aggiornaFascicoloAziendale ="+se.getMessage());
		
		response.reset();		
		response.getWriter().write("failed");	
		response.flushBuffer();
		SolmrLogger.debug(this, "  END aggiornaFascicoloAziendaleCtrl");		
	}
%>
	
	

  

