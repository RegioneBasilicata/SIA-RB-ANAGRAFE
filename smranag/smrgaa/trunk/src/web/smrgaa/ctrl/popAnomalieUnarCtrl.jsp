<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%

	String iridePageName = "popAnomalieUnarCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

  	String popAnomaliaUnarUrl = "/view/popAnomalieUnarView.jsp";
  	String erroreUrl = "/view/erroreView.jsp";

  	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   	FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
   	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   	Long idStoricoUnitaArborea = null;
   	// Per capire come reperire i dati della particella devo verificare qual è il piano di riferimento
   	// selezionaro dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
   	// di errore
   	if(filtriUnitaArboreaRicercaVO == null || !Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento())) {
		String messaggio = AnagErrors.ERRORE_KO_ANOMALIE_FIND_UNAR_PARAMETER;
	   	request.setAttribute("messaggioErrore",messaggio);
	   	session.setAttribute("chiudi", "chiudi");
	   	%>
			<jsp:forward page="<%=erroreUrl%>" />
	   	<%
	   	return;
   	}
   	else {
		idStoricoUnitaArborea = Long.decode(request.getParameter("idStoricoUnitaArborea"));
   	}

	// Vado a reperire i dati in fuzione di id_storico_unita_arborea...
 	StoricoParticellaVO storicoParticellaVO = null;
 	EsitoControlloUnarVO[] elencoAnomalie = null;
 	Vector elencoAnomalieDichiarate = null;
 	if(idStoricoUnitaArborea != null) {
 		try {
 			storicoParticellaVO = anagFacadeClient.findStoricoParticellaArborea(idStoricoUnitaArborea);
 			// Se non sono stato in grado di recuperare i dati...
 			if(storicoParticellaVO == null) {
 				String messaggio = AnagErrors.ERRORE_KO_ANOMALIE_FIND_UNAR_PARAMETER;
 	   			request.setAttribute("messaggioErrore",messaggio);
 	   			session.setAttribute("chiudi", "chiudi");
 	   			%>
 	      			<jsp:forward page="<%=erroreUrl%>" />
 	   			<%
 	   			return;
 			}
 			else {
				// Ricerco le segnalazioni
	 			try {
	 				if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) {
	 					String[] orderBy = {SolmrConstants.ORDER_BY_BLOCCANTE_UNAR_ASC};
	 					elencoAnomalie = anagFacadeClient.getListEsitoControlloUnarByIdStoricoUnitaArborea(idStoricoUnitaArborea, orderBy);
	 				}
	 				else {
	 					String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_BLOCCANTE_ASC};
	 					elencoAnomalieDichiarate = anagFacadeClient.getListDichiarazioneSegnalazioniUnar(anagAziendaVO.getIdAzienda(), filtriUnitaArboreaRicercaVO.getIdPianoRiferimento(), idStoricoUnitaArborea, orderBy);
	 				}
	 			}
	 			catch(SolmrException se) {
 			    	String messaggio = AnagErrors.ERRORE_KO_ANOMALIE_UNAR;
 	 	   			request.setAttribute("messaggioErrore",messaggio);
 	 	   			session.setAttribute("chiudi", "chiudi");
 	 	   			%>
 	 	      			<jsp:forward page="<%=erroreUrl%>" />
 	 	   			<%
 	 	   			return;
 			    }
 			}
 		}
 		catch(SolmrException se) {
   			String messaggio = AnagErrors.ERRORE_KO_ANOMALIE_FIND_UNAR_PARAMETER;
   			request.setAttribute("messaggioErrore",messaggio);
   			session.setAttribute("chiudi", "chiudi");
   			%>
      			<jsp:forward page="<%=erroreUrl%>" />
   			<%
   			return;
 		}
 	}
 	// Metto in request l'oggetto particella
 	request.setAttribute("storicoParticellaVO", storicoParticellaVO);
 	// Metto il vettore in request se sono stati recuperati elementi da visualizzare
 	if(elencoAnomalie != null && elencoAnomalie.length > 0) {
		request.setAttribute("elencoAnomalie", elencoAnomalie);
 	}
 	else {
 		if(elencoAnomalieDichiarate != null && elencoAnomalieDichiarate.size() > 0) {
 			request.setAttribute("elencoAnomalieDichiarate", elencoAnomalieDichiarate);
 	 	}	
 	}

  	// Vado alla pop-up
  	%>
    	<jsp:forward page= "<%= popAnomaliaUnarUrl %>" />
  	<%

%>

