<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>

<%
	
	String iridePageName = "terreniParticellareDettaglioConduzioneCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String terreniParticellareDettaglioConduzioneUrl = "/view/terreniParticellareDettaglioConduzioneView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	StoricoParticellaVO storicoParticellaVO = null;
	Long idConduzione = Long.decode(request.getParameter("idConduzione"));
	String operazione = request.getParameter("operazione");
	
	// Per capire quali tabelle interrogare devo verificare qual è il piano di riferimento
	// selezionato dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
	// di errore
	if(filtriParticellareRicercaVO == null 
    || !Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento())) 
  {
		String messaggio = (String)AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
  	request.setAttribute("messaggioErrore",messaggio);
  	session.setAttribute("chiudi", "chiudi");
  	%>
   		<jsp:forward page="<%=terreniParticellareDettaglioConduzioneUrl%>" />
  	<%
  	return;
	}
	else 
  {		
		// Cerco i dati della particella in relazione all'id conduzione selezionato
		try 
    {
			storicoParticellaVO = anagFacadeClient.getDettaglioParticella(filtriParticellareRicercaVO, idConduzione);
		}
		catch(SolmrException se) 
    {
			String messaggio = (String)AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
	  	request.setAttribute("messaggioErrore",messaggio);
	  	session.setAttribute("chiudi", "chiudi");
	  	%>
	   		<jsp:forward page="<%=terreniParticellareDettaglioConduzioneUrl%>" />
	  	<%
	  	return;
		}
		request.setAttribute("storicoParticellaVO", storicoParticellaVO);
		
		// Una volta recuperati, cerco le informazioni relative all'UTE associata
		// alla conduzione
		if(storicoParticellaVO != null) 
    {
			UteVO uteVO = null;
			try 
      {
				if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
        {
					uteVO = anagFacadeClient.findUteByPrimaryKey(storicoParticellaVO.getElencoConduzioni()[0].getIdUte());
				}
				else 
        {
					uteVO = anagFacadeClient.findUteByPrimaryKey(storicoParticellaVO.getElencoConduzioniDichiarate()[0].getIdUte());
				}
			}
			catch(SolmrException se) 
      {
				String messaggio = (String)AnagErrors.ERRORE_KO_UNITA_PRODUTTIVA;
		   	request.setAttribute("messaggioErrore",messaggio);
		   	session.setAttribute("chiudi", "chiudi");
		   	%>
		    	<jsp:forward page="<%=terreniParticellareDettaglioConduzioneUrl%>" />
		   	<%
		   	return;
			}	
			request.setAttribute("uteVO", uteVO);
		}
	 	
	 	// Recupero l'elenco delle piante consociate attive censite sul DB
	 	Vector<TipoPiantaConsociataVO> elencoPianteConsociate = null;
	 	try 
    {
	 		elencoPianteConsociate = anagFacadeClient.getListPianteConsociate(true);
	 	}
	 	catch(SolmrException se) {
	 		String messaggio = (String)AnagErrors.ERRORE_KO_PIANTE_CONSOCIATE;
	   		request.setAttribute("messaggioErrore",messaggio);
	   		session.setAttribute("chiudi", "chiudi");
	   		%>
	    		<jsp:forward page="<%=terreniParticellareDettaglioConduzioneUrl%>" />
	   		<%
	   		return;
	 	}
		
	 	if(elencoPianteConsociate != null && elencoPianteConsociate.size() > 0) 
    {
	 		request.setAttribute("elencoPianteConsociate", elencoPianteConsociate);
	 	}
	 	
	 	// Se l'utente ha modificato il valore della combo
	 	if(Validator.isNotEmpty(operazione)) 
    {
	 		Object[] elencoUtilizzi = null;
	 		// In relazione al valore impostato nella combo vado a recuperare
	 		// l'elenco degli utilizzi relativi al piano di riferimento indicato
	 		Long idPianoRiferimento = Long.decode(request.getParameter("idDichiarazioneConsistenza"));
	 		request.setAttribute("idPianoRiferimento", idPianoRiferimento);
	 		Long idConduzioneParticella = null;
	 		if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
      {
	 			idConduzioneParticella = storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella();
	 		}
	 		else 
      {
	 			idConduzioneParticella = storicoParticellaVO.getElencoConduzioniDichiarate()[0].getIdConduzioneParticella();
	 		}
	 		
	 		try 
      {
	 			// Piano di riferimento corrente
	 			if(idPianoRiferimento.intValue() < 0) 
        {
	 				elencoUtilizzi = anagFacadeClient.getListUtilizzoParticellaVOByIdConduzioneParticella(idConduzioneParticella, null, true);
	 			}
	 			// Piano di riferimento comprensivo di conduzioni storicizzate
	 			else if(idPianoRiferimento.intValue() == 0) 
        {
	 				elencoUtilizzi = anagFacadeClient.getListUtilizzoParticellaVOByIdConduzioneParticella(idConduzioneParticella, null, false);
	 			}
	 			// Piano di riferimento relativo ad una dichiarazione di consistenza
	 			else 
        {
	 				elencoUtilizzi = anagFacadeClient.getListUtilizzoDichiaratoVOByIdDichiarazioneConsistenzaAndIdConduzioneParticella(idPianoRiferimento, idConduzioneParticella, null);
	 			}
	 			request.setAttribute("elencoUtilizzi", elencoUtilizzi);
	 		}
	 		catch(SolmrException se) 
      {
	 			String messaggio = (String)AnagErrors.ERRORE_KO_SEARCH_UTILIZZI;
		   	request.setAttribute("messaggioErrore",messaggio);
		   	session.setAttribute("chiudi", "chiudi");
		   	%>
		    	<jsp:forward page="<%=terreniParticellareDettaglioConduzioneUrl%>" />
		   	<%
		   	return;
	 		}
	 	}
	}
	
%>
	<jsp:forward page="<%=terreniParticellareDettaglioConduzioneUrl%>" />
