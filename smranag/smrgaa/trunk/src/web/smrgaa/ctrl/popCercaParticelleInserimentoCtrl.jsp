<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "popCercaParticelleInserimentoCtrl.jsp";
  %>
  	<%@include file = "/include/autorizzazione.inc" %>
  <%

  String popCercaParticelleInserimentoUrl = "/view/popCercaParticelleInserimentoView.jsp";
  String terreniParticellareInserisciCtrlUrl = "/ctrl/terreniParticellareInserisciCtrl.jsp";
  String erroreUrl = "/view/erroreView.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  // Recupero i parametri dalla pagina di inserimento
  StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
	//  Recupero i parametri
	String ute = request.getParameter("idUte");
	request.setAttribute("ute", ute);
	String siglaProvincia = request.getParameter("siglaProvincia");
	request.setAttribute("siglaProvincia", siglaProvincia);
	String descComune = request.getParameter("descComune");
	request.setAttribute("descComune", descComune);
	String descStatoEstero = request.getParameter("descStatoEstero");
	request.setAttribute("descStatoEstero", descStatoEstero);
	String sezione = request.getParameter("sezione");
	request.setAttribute("sezione", sezione);
	String foglio = request.getParameter("foglio");
	request.setAttribute("foglio", foglio);
	String particella = request.getParameter("particella");
	request.setAttribute("particella", particella);
	String provvisoria = request.getParameter("provvisoria");
	request.setAttribute("provvisoria", provvisoria);
	String subalterno = request.getParameter("subalterno");
	request.setAttribute("subalterno", subalterno);
	String evento = request.getParameter("idEvento");
	request.setAttribute("evento", evento);
  // Recupero i parametri necessari per la ricerca delle particelle
  String provinciaEvento = request.getParameter("provinciaEvento");
  request.setAttribute("provinciaEvento", provinciaEvento);
  String comuneEvento = request.getParameter("comuneEvento");
  request.setAttribute("comuneEvento", comuneEvento);
  String sezioneEvento = request.getParameter("sezioneEvento");
  request.setAttribute("sezioneEvento", sezioneEvento);
  String foglioEvento = request.getParameter("foglioEvento");
  request.setAttribute("foglioEvento", foglioEvento);
  String particellaEvento = request.getParameter("particellaEvento");
  request.setAttribute("particellaEvento", particellaEvento);
  String operazione = request.getParameter("operazione");
  boolean noAziende = request.getParameter("noAzienda") != null;
  if(noAziende) {
  	request.setAttribute("noAziende", SolmrConstants.FLAG_S);
  }
  String inserimento = request.getParameter("inserimento");
  	
  // Cerco il comune in funzione dei dati arrivati
  ComuneVO comuneVO = null;
  try 
  {
  	comuneVO = anagFacadeClient.getComuneByParameters(comuneEvento, provinciaEvento, null, SolmrConstants.FLAG_S, null);
  }
  catch(SolmrException se) 
  {
  	String messaggio = AnagErrors.ERRORE_KO_COMUNE;
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
  			<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
  }
  	
  // Controllo la correttezza dei dati
  if((Validator.isNotEmpty(foglioEvento) && !Validator.isNumericInteger(foglioEvento)) 
    || (Validator.isNotEmpty(particellaEvento) 
    && !Validator.isNumericInteger(particellaEvento)) || comuneVO == null) 
  {
  	String messaggio = AnagErrors.ERRORE_FILTRO_PARTICELLARE_ERRATO;
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
  			<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
  }
  // Se lo sono vado a ricercare le particelle in funzione dei filtri di ricerca indicati
  else 
  {
  	// L'utente ha selezionato il tasto inserisci dalla pop-up
  	if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("inserisci")) 
  	{
  		String[] elencoStorico = request.getParameterValues("idStoricoParticella");
  		StoricoParticellaVO[] elencoStoricoEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");
  		Vector temp = new Vector();
  	  if(elencoStoricoEvento != null && elencoStoricoEvento.length > 0) 
  	  {
  		 	for(int i = 0; i < elencoStoricoEvento.length; i++) 
  		 	{
  		  	temp.add((StoricoParticellaVO)elencoStoricoEvento[i]);
  		  }
  		}
			// Ricerco i dati richiesti dall'utente
  		boolean isCensite = false;
  		for(int i = 0; i < elencoStorico.length; i++) 
  		{
  			int countParticelleGiaCensite = 0;
  			String storico = (String)elencoStorico[i];
  			Long idStoricoParticella = Long.decode(storico);
  			try 
  			{
  				StoricoParticellaVO storicoParticellaEventoVO = anagFacadeClient.findStoricoParticellaByPrimaryKey(idStoricoParticella);
  				if(temp.size() == 0) 
  				{
  		    	temp.add(storicoParticellaEventoVO);
  		    }
  				else 
  				{
  		    	for(int j = 0; j < temp.size(); j++) 
  		    	{
  		      	StoricoParticellaVO storicoParticellaElencoVO = (StoricoParticellaVO)temp.elementAt(j);
  		        if(storicoParticellaElencoVO.getIdStoricoParticella().compareTo(storicoParticellaEventoVO.getIdStoricoParticella()) == 0) 
  		        {
  		        	countParticelleGiaCensite++;
  		        	isCensite = true;
  		        	break;
  		        }
  		      }
  		      if(countParticelleGiaCensite == 0) 
  		      {
  		      	temp.add(storicoParticellaEventoVO);
  		      }
  		    }
  			}
  			catch(SolmrException se) 
  			{
  				String messaggio = AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
  				request.setAttribute("messaggioErrore",messaggio);
  				session.setAttribute("chiudi", "chiudi");
  				%>
  					<jsp:forward page="<%=erroreUrl%>" />
  				<%
  				return;
  			}
  		}
  		// Setto i parametri nella pagina chiamante
  		if(storicoParticellaVO == null) 
  		{
 				storicoParticellaVO = new StoricoParticellaVO();
 				storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{new ConduzioneParticellaVO()});
 			}
 			comuneVO = new ComuneVO();
 			ProvinciaVO provinciaVO = new ProvinciaVO();
 			String flagEstero = null;
 			if(Validator.isNotEmpty(ute)) 
 			{
 				storicoParticellaVO.getElencoConduzioni()[0].setIdUte(Long.decode(ute));
 			}
 			flagEstero = SolmrConstants.FLAG_N;
 			provinciaVO.setSiglaProvincia(siglaProvincia);
 			comuneVO.setProvinciaVO(provinciaVO);
 			comuneVO.setDescom(descComune);
 			comuneVO.setFlagEstero(flagEstero);
 			storicoParticellaVO.setComuneParticellaVO(comuneVO);
 			storicoParticellaVO.setSezione(sezione);
 			storicoParticellaVO.setFoglio(foglio);
 			storicoParticellaVO.setParticella(particella);
 			storicoParticellaVO.setSubalterno(subalterno);
			// Inserisco l'oggetto in sessione nel caso in cui sia la prima volta
 			// che acceda alla pagina senza aver ancora mai confermato i dati
 			session.setAttribute("storicoParticellaVO", storicoParticellaVO);
  		session.setAttribute("elencoStoricoEvento", (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]));
			// Comunico l'eventuale selezione di particelle precedentemente selezionate
  		if(isCensite) 
  		{
  		 	ValidationError error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_DOCUMENTO_GIA_INSERITO);
  		 	ValidationErrors errors = new ValidationErrors();
  		 	errors.add("error", error);
  		 	request.setAttribute("errors", errors);
  		 	// Rimuovo dalla request i filtri di ricerca perchè non devono
  		 	// essere settati a video
  		 	request.removeAttribute("provinciaEvento");
  		 	request.removeAttribute("comuneEvento");
  		 	request.removeAttribute("sezioneEvento");
  		 	request.removeAttribute("foglioEvento");
  		 	request.removeAttribute("particellaEvento");
  		}
			// Torno alla pagina di inserimento
  		%>
  		 	<jsp:forward page= "<%= terreniParticellareInserisciCtrlUrl %>" >
  		 		<jsp:param name="descStatoEstero" value="<%= descStatoEstero %>"/>
  		 		<jsp:param name="idEvento" value="<%= evento %>"/>
  		 		<jsp:param name="indietro" value="true"/>
  		 		<jsp:param name="inserimento" value="<%= inserimento %>"/>
  		 	</jsp:forward>
  		<%
  		return;
  	}
  	else 
  	{
	  	StoricoParticellaVO[] elencoParticelle = null;
		  try 
		  {
		   	if(Validator.isNotEmpty(operazione) 
		   	  && operazione.equalsIgnoreCase("aggiorna") && noAziende) 
		   	{
		      elencoParticelle = anagFacadeClient.getListStoricoParticellaVOByParameters(comuneVO.getIstatComune(), sezioneEvento, foglioEvento, particellaEvento, null, true, SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY, null);
		    }
		    else 
		    {
		    	elencoParticelle = anagFacadeClient.getListStoricoParticellaVOByParameters(comuneVO.getIstatComune(), sezioneEvento, foglioEvento, particellaEvento, null, true, SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY, anagAziendaVO.getIdAzienda());
		    }
		    if(elencoParticelle == null || elencoParticelle.length == 0) 
		    {
		    	String messaggio = AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND;
		    	request.setAttribute("messaggioErrore",messaggio);
		    }
		    else 
		    {
		    	request.setAttribute("elencoParticelleEvento", elencoParticelle);
		    }
		  }
		  catch(SolmrException se) 
		  {
		   	String messaggio = "";
		   	if(se.getMessage().equalsIgnoreCase(AnagErrors.ERRORE_FILTRI_GENERICI)) 
		   	{
		    	messaggio = se.getMessage();
		    }
		    else 
		    {
		    	messaggio = AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
		    }
				request.setAttribute("messaggioErrore",messaggio);
				session.setAttribute("chiudi", "chiudi");
				%>
		  		<jsp:forward page="<%=erroreUrl%>" />
				<%
				return;
		  }
    }
  }

  // Vado alla pop-up
  %>
   	<jsp:forward page= "<%= popCercaParticelleInserimentoUrl %>" />
  	

