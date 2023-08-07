<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

  String referer=request.getHeader("referer");
  boolean isFromricercaTerreno = referer != null && referer.toLowerCase().endsWith("ricercaterrenodettagliovalidazioni.htm");
  String iridePageName = null;
  // Modificato da 70399 (Einaudi) il 09/09/08 per permettere l'accesso anche dal dettaglio
  // della ricerca terreno 
  if (isFromricercaTerreno) // Se arrivo dall ricerca dettaglio devo eseguire controlli diversi
  {
    iridePageName = "ricercaTerrenoDettaglioValidazioniCtrl.jsp";
  }
  else
  {
    iridePageName = "popAnomalieParticellaCtrl.jsp";
  }
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

 	String popAnomaliaParticellaUrl = "/view/popAnomalieParticellaView.jsp";
 	String erroreUrl = "/view/erroreView.jsp";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	Long idConduzioneParticella = null;
 	Long idConduzioneDichiarata = null;
 	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	// Per capire quali tabelle interrogare devo verificare qual è il piano di riferimento
 	// selezionaro dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
 	// di errore
 	if (!isFromricercaTerreno 
 	  && (filtriParticellareRicercaVO == null 
 	      || !Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento()))) 
 	{
	  String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_FIND_TERRENI_PARAMETER;
	  request.setAttribute("messaggioErrore",messaggio);
	  session.setAttribute("chiudi", "chiudi");
	  %>
	  	<jsp:forward page="<%=erroreUrl%>" />
	  <%
	  return;
  }
  else 
  {
	  if(filtriParticellareRicercaVO!=null && filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
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
 	Vector elencoAnomalie = null;
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
 				  String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_FIND_TERRENI_PARAMETER;
 	 	   		request.setAttribute("messaggioErrore",messaggio);
 	 	   		session.setAttribute("chiudi", "chiudi");
 	 	   		%>
 	 	      	<jsp:forward page="<%=erroreUrl%>" />
 	 	   		<%
 	 	   		return;
 				}
	 			request.setAttribute("storicoParticellaVO", storicoParticellaVO);
	 			// Ricerco le segnalazioni
	 			try 
	 			{
	 				elencoAnomalie = anagFacadeClient.getElencoEsitoControlloParticella(idConduzioneParticella);	 				
	 			}
	 			catch(SolmrException se) 
	 			{
	 				if(se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NESSUN_CONTROLLO_PARTICELLA_TROVATO"))) 
	 				{
	 			   	// Metto in request il messaggio di errore
	 			   	request.setAttribute("messaggio", (String)AnagErrors.get("ERR_NESSUN_CONTROLLO_PARTICELLA_TROVATO"));
	 			   	// Vado alla pagina dove "eventualmente" mostrerò il messaggio di errore
	 			   	%>
	 			     	<jsp:forward page= "<%= popAnomaliaParticellaUrl %>" />
	 			   	<%
	 			  }
	 			  else 
	 			  {
	 			   	String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_PARTICELLE;
	 	 	   		request.setAttribute("messaggioErrore",messaggio);
	 	 	   		session.setAttribute("chiudi", "chiudi");
	 	 	   		%>
	 	 	    		<jsp:forward page="<%=erroreUrl%>" />
	 	 	   		<%
	 	 	   		return;
	 			  }
	 			}
 			}
 			// ... altrimenti segnalo messaggio di errore
 			else 
 			{
 				String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_FIND_TERRENI_PARAMETER;
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
   		String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_FIND_TERRENI_PARAMETER;
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
 				  String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_FIND_TERRENI_PARAMETER;
 	 	   		request.setAttribute("messaggioErrore",messaggio);
 	 	   		session.setAttribute("chiudi", "chiudi");
 	 	   		%>
 	 	      	<jsp:forward page="<%=erroreUrl%>" />
 	 	   		<%
 	 	   		return;
 				}
	 			request.setAttribute("storicoParticellaVO", storicoParticellaVO);
				// Ricerco le segnalazioni
	 			try 
	 			{
	 			  Long idAzienda=null;
	 			  if (anagAziendaVO==null)
	 			  {
	 			    idAzienda=new Long(request.getParameter("idAzienda"));
	 			  }
	 			  else
	 			  {
	 			    idAzienda=anagAziendaVO.getIdAzienda();
	 			  }
	 			  Long idDichiarazioneConsistenza=null;
	 			  if (filtriParticellareRicercaVO!=null)
	 			  {
	 			    idDichiarazioneConsistenza=filtriParticellareRicercaVO.getIdPianoRiferimento();
	 			  }
	 			  else
	 			  {
	 			    idDichiarazioneConsistenza=new Long(request.getParameter("idDichiarazioneConsistenza"));
	 			  }
	 				elencoAnomalie = anagFacadeClient.getListDichiarazioneSegnalazioni(idAzienda, idDichiarazioneConsistenza, storicoParticellaVO.getIdStoricoParticella());
	 				// Se non ne trovo avviso l'utente
	 				if(elencoAnomalie == null || elencoAnomalie.size() == 0) 
	 				{
	 			   	request.setAttribute("messaggio", (String)AnagErrors.get("ERR_NESSUN_CONTROLLO_PARTICELLA_TROVATO"));
	 			   	%>
	 			     	<jsp:forward page= "<%= popAnomaliaParticellaUrl %>" />
	 			   	<%
	 				}
	 			}
	 			catch(SolmrException se) 
	 			{
	 				String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_PARTICELLE;
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
 				String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_FIND_TERRENI_PARAMETER;
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
    	String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_FIND_TERRENI_PARAMETER;;
    	request.setAttribute("messaggioErrore",messaggio);
    	session.setAttribute("chiudi", "chiudi");
    	%>
     		<jsp:forward page="<%=erroreUrl%>" />
    	<%
    	return;
  	}
 	}
 	
 	// Metto il vettore in request se sono stati recuperati elementi da visualizzare
 	if(elencoAnomalie != null && elencoAnomalie.size() > 0) 
 	{
	  request.setAttribute("elencoAnomalie", elencoAnomalie);
 	}

  // Vado alla pop-up
  %>
   	<jsp:forward page= "<%= popAnomaliaParticellaUrl %>" />
  

