<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "popDocumentoDettaglioCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String popDocumentoDettaglioUrl = "/view/popDocumentoDettaglioView.jsp";
	String erroreUrl = "/view/erroreView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	Long idConduzioneParticella = null;
	Long idConduzioneDichiarata = null;
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	// Per capire quali tabelle interrogare devo verificare qual è il piano di riferimento
	// selezionaro dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
	// di errore
	if(filtriParticellareRicercaVO == null 
	  || !Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento())) 
	{
		String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_FIND_TERRENI_PARAMETER;
 		request.setAttribute("messaggioErrore",messaggio);
 		session.setAttribute("chiudi", "chiudi");
 		%>
  		<jsp:forward page="<%=erroreUrl%>" />
 		<%
 		return;
	}
	else 
	{
		if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
		{
			idConduzioneParticella = Long.decode(request.getParameter("idConduzione"));
   	}
   	else 
   	{
			idConduzioneDichiarata = Long.decode(request.getParameter("idConduzione"));
   	}
	}

	// Vado a reperire i dati in fuzione di id_conduzione_particella...
	StoricoParticellaVO storicoParticellaVO = null;
	Vector elencoDocumenti = null;
	if(idConduzioneParticella != null) 
	{
		try 
		{
			storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
			// Se sono stato in grado di recuperare i dati...
			if(storicoParticellaVO != null) 
			{
				ConduzioneParticellaVO conduzioneParticellaVO = null;
				conduzioneParticellaVO = anagFacadeClient.findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
				if(conduzioneParticellaVO != null) 
				{
					ConduzioneParticellaVO[] elencoConduzioni = {conduzioneParticellaVO};
					storicoParticellaVO.setElencoConduzioni(elencoConduzioni);
				}
				else 
				{
					String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_FIND_TERRENI_PARAMETER;
 	   			request.setAttribute("messaggioErrore",messaggio);
 	   			session.setAttribute("chiudi", "chiudi");
 	   			%>
 	      			<jsp:forward page="<%=erroreUrl%>" />
 	   			<%
 	   			return;
				}
 				request.setAttribute("storicoParticellaVO", storicoParticellaVO);
 				// Ricerco i documenti
 				try 
 				{
 					elencoDocumenti = anagFacadeClient.getListDettaglioDocumentoByIdConduzionePopUp(idConduzioneParticella);
 				}
 				catch(SolmrException se) 
 				{
		    	String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_PARTICELLE;
 	   			request.setAttribute("messaggioErrore",messaggio);
 	   			session.setAttribute("chiudi", "chiudi");
 	   			%>
 	      			<jsp:forward page="<%=erroreUrl%>" />
 	   			<%
 	   			return;
 			  }
			}
			// ... altrimenti segnalo messaggio di errore
			else 
			{
				String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_FIND_TERRENI_PARAMETER;
   			request.setAttribute("messaggioErrore",messaggio);
   			session.setAttribute("chiudi", "chiudi");
   			%>
      			<jsp:forward page="<%=erroreUrl%>" />
   			<%
   			return;
			}
		}
		catch(SolmrException se) 
		{
			String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_FIND_TERRENI_PARAMETER;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
  				<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
	// ... o di id_conduzione_dichiarata
	else 
	{
 		try 
 		{
 			storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneDichiarata(idConduzioneDichiarata);
			// Se sono stato in grado di recuperare i dati...
 			if(storicoParticellaVO != null) 
 			{
 				ConduzioneDichiarataVO conduzioneDichiarataVO = null;
				conduzioneDichiarataVO = anagFacadeClient.findConduzioneDichiarataByPrimaryKey(idConduzioneDichiarata);
				if(conduzioneDichiarataVO != null) 
				{
					ConduzioneDichiarataVO[] elencoConduzioni = {conduzioneDichiarataVO};
					storicoParticellaVO.setElencoConduzioniDichiarate(elencoConduzioni);
				}
				else 
				{
					String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_FIND_TERRENI_PARAMETER;
 	   			request.setAttribute("messaggioErrore",messaggio);
 	   			session.setAttribute("chiudi", "chiudi");
 	   			%>
 	      			<jsp:forward page="<%=erroreUrl%>" />
 	   			<%
 	   			return;
				}
 				request.setAttribute("storicoParticellaVO", storicoParticellaVO);
				// Ricerco i documenti
 				try 
 				{
 					elencoDocumenti = anagFacadeClient.getListDettaglioDocumentoByIdConduzioneDichiarataPopUp(idConduzioneDichiarata);
 				}
 				catch(SolmrException se) 
 				{
 					String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_PARTICELLE;
 	   			request.setAttribute("messaggioErrore",messaggio);
 	   			session.setAttribute("chiudi", "chiudi");
 	   			%>
 	      			<jsp:forward page="<%=erroreUrl%>" />
 	   			<%
 	   			return;
 				}
			}
			// ... altrimenti segnalo messaggio di errore
			else 
			{
				String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_FIND_TERRENI_PARAMETER;
   			request.setAttribute("messaggioErrore",messaggio);
   			session.setAttribute("chiudi", "chiudi");
   			%>
      			<jsp:forward page="<%=erroreUrl%>" />
   			<%
   			return;
			}
		}
		// ... altrimenti segnalo messaggio di errore
		catch(SolmrException se) 
		{
			String messaggio = (String)AnagErrors.ERRORE_KO_DOCUMENTI_FIND_TERRENI_PARAMETER;;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
   	   			<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
	
	// Metto il vettore in request se sono stati recuperati elementi da visualizzare
	if(elencoDocumenti != null && elencoDocumenti.size() > 0) 
	{
		request.setAttribute("elencoDocumenti", elencoDocumenti);
	}
	else 
	{
		request.setAttribute("messaggio", (String)AnagErrors.get("ERRORE_NO_DOCUMENTI_PARTICELLE"));
    %>
    	<jsp:forward page= "<%= popDocumentoDettaglioUrl %>" />
    <%
	}
	

	// Vado alla pop-up
	%>
		<jsp:forward page= "<%= popDocumentoDettaglioUrl %>" />

