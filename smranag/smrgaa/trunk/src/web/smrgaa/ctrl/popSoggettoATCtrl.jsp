<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.SianAnagTributariaVO" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "popSoggettoATCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String popSoggettoATUrl = "/view/popSoggettoATView.jsp";
	String erroreUrl = "/view/erroreView.jsp";
  
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

	// Recupero il CUAA
	String cuaa = request.getParameter("cuaa");

	// Ne verifico la correttezza
	if(cuaa.length() == 11) {
		if(!Validator.controlloPIVA(cuaa)) {
			String messaggio = AnagErrors.ERRORE_PIVA_FOR_SIAN_ERRATO;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
					<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
	else if(cuaa.length() == 16) {
		if(!Validator.controlloCf(cuaa)) {
			String messaggio = AnagErrors.ERRORE_CUAA_FOR_SIAN_ERRATO;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
					<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
	else {
		String messaggio = AnagErrors.ERRORE_CUAA_PIVA_FOR_SIAN_ERRATI;
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
				<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
	}

	// Richiamo il WEB-SERVICE SIAN per recuperari i dati relativi all'anagrafe tributaria
	SianAnagTributariaVO anagTrib = null;
	try 
	{
		anagTrib = anagFacadeClient.ricercaAnagrafica(cuaa.toUpperCase(), 
		  ProfileUtils.getSianUtente(ruoloUtenza));
		request.setAttribute("anagTrib", anagTrib);
	}
	catch(SolmrException se) 
	{
		String messaggio = se.getMessage();
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
				<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
	}

	// Vado alla pop-up
	%>
		<jsp:forward page= "<%= popSoggettoATUrl %>" />

