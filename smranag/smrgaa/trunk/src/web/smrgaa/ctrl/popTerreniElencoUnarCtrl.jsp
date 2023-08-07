<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%

	String iridePageName = "popTerreniElencoUnarCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String popTerreniElencoUnarUrl = "/view/popTerreniElencoUnarView.jsp";
	String erroreUrl = "/view/erroreView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	Long idConduzioneParticella = null;
	Long idConduzioneDichiarata = null;
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	
	// Per capire quali tabelle interrogare devo verificare qual è il piano di riferimento
	// selezionaro dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
	// di errore
	if(filtriParticellareRicercaVO == null || !Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento())) {
		String messaggio = AnagErrors.ERRORE_KO_UNITA_ARBOREE_FIND_TERRENI_PARAMETER;
   		request.setAttribute("messaggioErrore",messaggio);
   		session.setAttribute("chiudi", "chiudi");
   		%>
			<jsp:forward page="<%=erroreUrl%>" />
   		<%
   		return;
	}
	else {
		if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) {
			idConduzioneParticella = Long.decode(request.getParameter("idConduzione"));
   		}
   		else {
			idConduzioneDichiarata = Long.decode(request.getParameter("idConduzione"));
   		}
	}

	// Vado a reperire i dati in fuzione di id_conduzione_particella...
	StoricoParticellaVO storicoParticellaVO = null;
	Object[] elencoUnitaArboree = null;
	if(idConduzioneParticella != null) {
		try {
			storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
			// Se non sono stato in grado di recuperare i dati...
			if(storicoParticellaVO == null) {
				String messaggio = AnagErrors.ERRORE_KO_UNITA_ARBOREE_FIND_TERRENI_PARAMETER;
	   			request.setAttribute("messaggioErrore",messaggio);
	   			session.setAttribute("chiudi", "chiudi");
	   			%>
	      			<jsp:forward page="<%=erroreUrl%>" />
	   			<%
	   			return;
			}
		}
		catch(SolmrException se) {
			String messaggio = AnagErrors.ERRORE_KO_UNITA_ARBOREE_FIND_TERRENI_PARAMETER;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
  				<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
	// ... o di id_conduzione_dichiarata
	else {
 		try {
 			storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneDichiarata(idConduzioneDichiarata);
			// Se non sono stato in grado di recuperare i dati...
 			if(storicoParticellaVO == null) {
				String messaggio = AnagErrors.ERRORE_KO_UNITA_ARBOREE_FIND_TERRENI_PARAMETER;
	   			request.setAttribute("messaggioErrore",messaggio);
	   			session.setAttribute("chiudi", "chiudi");
	   			%>
	      			<jsp:forward page="<%=erroreUrl%>" />
	   			<%
	   			return;
			}
		}
		// ... altrimenti segnalo messaggio di errore
		catch(SolmrException se) {
			String messaggio = AnagErrors.ERRORE_KO_UNITA_ARBOREE_FIND_TERRENI_PARAMETER;;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
   	   			<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
	
	// Metto in request l'oggetto particella trovato
	request.setAttribute("storicoParticellaVO", storicoParticellaVO);
		
	// Ricerco le unità arboree
	try {
		String[] orderBy = {SolmrConstants.ORDER_BY_ID_TIPOLOGIA_UNAR_ASC, SolmrConstants.ORDER_BY_PROGR_UNAR_ASC};
		if(idConduzioneParticella != null) {
			elencoUnitaArboree = anagFacadeClient.getListStoricoUnitaArboreaByLogicKey(storicoParticellaVO.getIdParticella(), filtriParticellareRicercaVO.getIdPianoRiferimento(), anagAziendaVO.getIdAzienda(), orderBy);
		}
		else {
			elencoUnitaArboree = anagFacadeClient.getListUnitaArboreaDichiarataByPianoRiferimentoAndIdStoricoUnitaArborea(storicoParticellaVO.getIdStoricoParticella(), anagAziendaVO.getIdAzienda(),filtriParticellareRicercaVO.getIdPianoRiferimento(),orderBy);
		}
	}
	catch(SolmrException se) {
    	String messaggio = AnagErrors.ERRORE_KO_UNITA_ARBOREE;
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
  			<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
    }
	
	// Metto il vettore in request se sono stati recuperati elementi da visualizzare
	if(elencoUnitaArboree != null && elencoUnitaArboree.length > 0) {
		request.setAttribute("elencoUnitaArboree", elencoUnitaArboree);
	}
	else {
		request.setAttribute("messaggio", AnagErrors.ERRORE_NO_UNITA_ARBOREE_FOUND);
	    %>
	    	<jsp:forward page= "<%= popTerreniElencoUnarUrl %>" />
	    <%
	}
	

	// Vado alla pop-up
	%>
		<jsp:forward page= "<%= popTerreniElencoUnarUrl %>" />
	<%

%>

