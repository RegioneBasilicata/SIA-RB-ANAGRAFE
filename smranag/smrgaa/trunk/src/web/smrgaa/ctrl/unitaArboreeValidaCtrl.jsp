<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%
	
	String iridePageName = "unitaArboreeValidaCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
	String confirmDelViewUrl = "/view/confirmDelView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
	ValidationError error = null;
	
	// Controllo che sia stata effettivamente fatta una ricerca alla dichiarazione di consistenza
	if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) {
		error = new ValidationError(AnagErrors.ERRORE_KO_VALIDA_UV);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
	}
	
	// Recupero l'elenco delle UV selezionate
	String[] elencoUvSelezionate = request.getParameterValues("idUnita");
	Long[] elencoIdUnitaArboreaDichiarata = null;
	if(elencoUvSelezionate != null && elencoUvSelezionate.length > 0) {
		elencoIdUnitaArboreaDichiarata = new Long[elencoUvSelezionate.length];
		for(int i = 0; i < elencoUvSelezionate.length; i++) {
			elencoIdUnitaArboreaDichiarata[i] = Long.decode(elencoUvSelezionate[i]);
		}
		session.setAttribute("elencoIdUnitaArboreaDichiarata", elencoIdUnitaArboreaDichiarata);
	}
	
 	if(request.getParameter("submit") != null) {
 		
 		// Effettuo la validazione delle UV
 		try {
 			anagFacadeClient.validaUVPlSql((Long[])session.getAttribute("elencoIdUnitaArboreaDichiarata"));
 		}
 		catch(SolmrException se) {
 			error = new ValidationError(se.getMessage());
 			request.setAttribute("error", error);
 			%>
 				<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
 			<%
 		}
 		// Torno alla pagina di ricerca/elenco delle UV
 		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
 	}
 	// L'utente ha selezionato il pulsante "annulla"
 	else if(request.getParameter("submit2") != null) {
 		// Torno alla pagina di ricerca/elenco
 		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
 	}
 	
 	// Vado alla pagina di richiesta conferma dell'operazione
 	%>
		<jsp:forward page="<%= confirmDelViewUrl %>" />
	<%
 	
%>

