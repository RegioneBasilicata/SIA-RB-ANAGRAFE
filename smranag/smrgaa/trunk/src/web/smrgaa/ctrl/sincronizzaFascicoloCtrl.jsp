<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.ws.sianfa.SianEsitoAggFascicolo"%>
<%@ page import="it.csi.smranag.smrgaa.ws.sianfa.SianEsito"%>

<%

  String iridePageName = "sincronizzaFascicoloCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %>

<%
		  
  SolmrLogger.debug(this, "BEGIN sincronizzaFascicoloCtrl.jsp");

  String url = "/view/sincronizzaFascicoloView.jsp";

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  ValidationError error = null;
  ValidationErrors errors = null;
  
    
  // Chiamata al servizio getAggiornamentiFascicolo per avere lo storico degli aggiornamenti del fascicolo dell'azienda
  String cuaa = anagAziendaVO.getCUAA();
 
  try{
	SolmrLogger.debug(this, "---- Chiamata al servizio getAggiornamentiFascicolo per avere lo storico degli aggiornamenti del fascicolo dell'azienda");
	SolmrLogger.debug(this, "-- cuaa in input ="+cuaa);
	SianEsito esito = gaaFacadeClient.getAggiornamentiFascicolo(cuaa);
	List<SianEsitoAggFascicolo> esitoAggFascicolo = null;
	if(esito != null){
		esitoAggFascicolo = esito.getEsitiAggFascicolo();
	}		
  	
  	request.setAttribute("esitoAggFascicolo", esitoAggFascicolo);
  }	
  catch(SolmrException se){
	SolmrLogger.error(this, "-- SolmrException in fase di chiamata a getAggiornamentiFascicolo ="+se.getMessage());
	error = new ValidationError(se.getMessage());
  	errors = new ValidationErrors();
  	errors.add("error", error);
  	request.setAttribute("errors", errors);
  	request.getRequestDispatcher(url).forward(request, response);
  	return;  
  }
  
  
  
  
  SolmrLogger.debug(this, "END sincronizzaFascicoloCtrl.jsp");

%>
<jsp:forward page="<%=url%>"/>
