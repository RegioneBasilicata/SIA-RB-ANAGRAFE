<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.sian.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.util.performance.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
	String iridePageName = "sianTitoliSingoliCtrl.jsp";
  %>
  	<%@include file = "/include/autorizzazione.inc" %>
  <%

 	String sianTitoliSingoliUrl = "/view/sianTitoliSingoliView.jsp";
 	String action = "../layout/titoliSingoli.htm";
 	String attenderePregoUrl = "/view/attenderePregoView.jsp";
 	String excelUrl = "/servlet/ExcelBuilderServlet";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	String messaggioErrore = null;
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	String operazione = request.getParameter("operazione");

 	// L'utente ha selezionato la voce di menù titoli e io lo mando alla
 	// pagina di attesa per il caricamento dati
 	if("attenderePrego".equalsIgnoreCase(operazione)) {
 		session.removeAttribute("sianTitoloRispostaVO");
 		request.setAttribute("action", action);
   	operazione = null;
   	request.setAttribute("operazione", operazione);
   	%>
      	<jsp:forward page= "<%= attenderePregoUrl %>" />
   	<%
 	}
 	// L'utente ha selezionato la funzione di scarica in excel dei TITOLI PRODUTTORE
 	else if("scarica".equalsIgnoreCase(operazione)) 
  {
 		Vector<TitoloExcelVO> elencoTitoli = new Vector<TitoloExcelVO>();
 		for(int i = 0; i < ((SianTitoloRispostaVO)session.getAttribute("sianTitoloRispostaVO")).getRisposta().length; i++) 
    {
 			SianTitoloVO sianTitoloVO = (SianTitoloVO)((SianTitoloRispostaVO)session.getAttribute("sianTitoloRispostaVO")).getRisposta()[i];
 			TitoloExcelVO titoloExcelVO = new TitoloExcelVO();
 			titoloExcelVO.setAnagAziendaVO(anagAziendaVO);
 			titoloExcelVO.setIdentificativo(sianTitoloVO.getIdentificativo());
 			titoloExcelVO.setValoreTitolo(sianTitoloVO.getValoreTitolo());
 			titoloExcelVO.setSuperficie(sianTitoloVO.getSuperficie());
 			titoloExcelVO.setUBAobbligatori(sianTitoloVO.getUBAobbligatori());
 			titoloExcelVO.setTipoTitolo(sianTitoloVO.getTipoTitolo());
 			titoloExcelVO.setOrigine(sianTitoloVO.getOrigine());
 			titoloExcelVO.setTipoMovimento(sianTitoloVO.getTipoMovimento());
 			titoloExcelVO.setDataMovimento(sianTitoloVO.getDataMovimento());
 			titoloExcelVO.setCodiceValidazioneMovimento(sianTitoloVO.getCodiceValidazioneMovimento());
 			titoloExcelVO.setDataUltimoUtilizzo(sianTitoloVO.getDataUltimoUtilizzo());
 			titoloExcelVO.setDataFinePossesso(sianTitoloVO.getDataFinePossesso());
 			titoloExcelVO.setSALtitolo(sianTitoloVO.getSALtitolo());
 			titoloExcelVO.setCuaaProprietario(sianTitoloVO.getCuaaProprietario());
 			titoloExcelVO.setCUAASoccidario(sianTitoloVO.getCUAASoccidario());
 			titoloExcelVO.setIdTitoloFrazionato(sianTitoloVO.getIdTitoloFrazionato());
 			titoloExcelVO.setZonaPrimoUtilizzo(sianTitoloVO.getZonaPrimoUtilizzo());
 			titoloExcelVO.setCampagnaInizioVali(sianTitoloVO.getCampagnaInizioVali());
 			titoloExcelVO.setCampagnaFineVali(sianTitoloVO.getCampagnaFineVali());
 			elencoTitoli.add(titoloExcelVO);
 		}
 		request.setAttribute("elenco", elencoTitoli);
   	request.setAttribute("foglioExcel", "elencoTitoli");
   	request.setAttribute("fileName", "Titoli");
   	request.setAttribute("anagAziendaVO", anagAziendaVO);
   	// Vado alla servlet per la gestione del foglio excel
   	%>
      <jsp:forward page="<%= excelUrl %>" />
   	<%
 	}
 	else 
  {
	  // Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo
	  StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
	  // START
	  watcher.start();
	
 		// Richiamo il web-service SIAN per l'estrazione dei titoli
   	SianTitoloRispostaVO sianTitoloRispostaVO = null;
   	try 
    {
   		sianTitoloRispostaVO = anagFacadeClient.titoliProduttore(
        anagAziendaVO.getCUAA(), DateUtils.getCurrentYear().toString(), ProfileUtils.getSianUtente(ruoloUtenza));
     		// Lo metto in sessione e non in request per poter implementare la
     		// funzione di scarico in excel in tempi accettabili
   		session.setAttribute("sianTitoloRispostaVO", sianTitoloRispostaVO);
   	}
   	catch(SolmrException se) 
    {
    	request.setAttribute("messaggioErrore", AnagErrors.ERRORE_SERVIZIO_SIAN_NON_DISPONIBILE);
    	%>
    		<jsp:forward page="<%= sianTitoliSingoliUrl %>"/>
    	<%
   	}
	  // Primo monitoraggio
	  watcher.dumpElapsed("sianTitoliSingoliCtrl.jsp", "titoliProduttore", "In titoli method from the beginning to the execution of Sian's method", "List of parameters: cuaa: "+anagAziendaVO.getCUAA()+" annoCampagna: "+DateUtils.getCurrentYear().toString());
	
   	// Se ho reperito dei titoli per l'azienda in questione devo
   	// recuperare le codifiche dei codici che mi arrivano dal SIAN
   	// dal momento che quest'ultimo non le restituisce.
   	if(sianTitoloRispostaVO != null && sianTitoloRispostaVO.getRisposta() != null && sianTitoloRispostaVO.getRisposta().length > 0) 
    {
   		Vector<String> elencoCodiciTipoTitolo = new Vector<String>();
   		Vector<String> elencoCodiciOrigine = new Vector<String>();
   		Vector<String> elencoCodiciMovimento = new Vector<String>();
   		for(int i = 0; i < sianTitoloRispostaVO.getRisposta().length; i++) 
      {
     		SianTitoloVO sianTitoloVO = (SianTitoloVO)sianTitoloRispostaVO.getRisposta()[i];
     		elencoCodiciTipoTitolo.add(sianTitoloVO.getTipoTitolo());
     		elencoCodiciOrigine.add(sianTitoloVO.getOrigine());
     		elencoCodiciMovimento.add(sianTitoloVO.getTipoMovimento());
   		}
   		
   		Vector<String> newElencoCodiciTipoTitolo = StringUtils.getElencoCodiciForLegendaSIAN(elencoCodiciTipoTitolo);
   		Vector<StringcodeDescription> elencoDescTipoTitolo = new Vector<StringcodeDescription>();
   		String descTipoTitolo = null;
   		try 
      {
   			for(int i = 0; i < newElencoCodiciTipoTitolo.size(); i++) 
        {
   				descTipoTitolo = anagFacadeClient.getDescriptionSIANFromCode(SolmrConstants.TAB_SIAN_TIPO_TITOLO, (String)newElencoCodiciTipoTitolo.elementAt(i));
     			StringcodeDescription stringCode = new StringcodeDescription((String)newElencoCodiciTipoTitolo.elementAt(i),descTipoTitolo);
     			elencoDescTipoTitolo.add(stringCode);
   			}
   		}
   		catch(SolmrException se) 
      {
     		messaggioErrore = AnagErrors.ERRORE_KO_DESCRIZIONE_TIPO_TITOLO_SIAN;
     		request.setAttribute("messaggioErrore", messaggioErrore);
     		%>
      		<jsp:forward page="<%= sianTitoliSingoliUrl %>"/>
     		<%
   		}
   		request.setAttribute("elencoDescTipoTitolo", elencoDescTipoTitolo);
   		
   		Vector<String> newElencoCodiciOrigine = StringUtils.getElencoCodiciForLegendaSIAN(elencoCodiciOrigine);
   		Vector<StringcodeDescription> elencoDescOrigineTitolo = new Vector<StringcodeDescription>();
   		String descOrigineTitolo = null;
   		try 
      {
     		for(int i = 0; i < newElencoCodiciOrigine.size(); i++) 
        {
       		descOrigineTitolo = anagFacadeClient.getDescriptionSIANFromCode(SolmrConstants.TAB_SIAN_TIPO_ORIGINE_TITOLO, (String)newElencoCodiciOrigine.elementAt(i));
       		StringcodeDescription stringCode = new StringcodeDescription((String)newElencoCodiciOrigine.elementAt(i),descOrigineTitolo);
       		elencoDescOrigineTitolo.add(stringCode);
     		}
   		}
   		catch(SolmrException se) 
      {
     		messaggioErrore = AnagErrors.ERRORE_KO_DESCRIZIONE_TIPO_ORIGINE_TITOLO_SIAN;
     		request.setAttribute("messaggioErrore", messaggioErrore);
     		%>
      		<jsp:forward page="<%= sianTitoliSingoliUrl %>"/>
     		<%
   		}
   		request.setAttribute("elencoDescOrigineTitolo", elencoDescOrigineTitolo);

   		Vector<String> newElencoCodiciMovimento = StringUtils.getElencoCodiciForLegendaSIAN(elencoCodiciMovimento);
   		Vector<StringcodeDescription> elencoDescMovimento = new Vector<StringcodeDescription>();
   		String descMovimento = null;
   		try 
      {
     		for(int i = 0; i < newElencoCodiciMovimento.size(); i++) 
        {
      		descMovimento = anagFacadeClient.getDescriptionSIANFromCode(SolmrConstants.TAB_SIAN_TIPO_MOVIMENTO_TITOLO, (String)newElencoCodiciMovimento.elementAt(i));
       		StringcodeDescription stringCode = new StringcodeDescription((String)newElencoCodiciMovimento.elementAt(i),descMovimento);
       		elencoDescMovimento.add(stringCode);
     		}
   		}
   		catch(SolmrException se) 
      {
     		messaggioErrore = AnagErrors.ERRORE_KO_DESCRIZIONE_TIPO_MOVIMENTO_TITOLO_SIAN;
     		request.setAttribute("messaggioErrore", messaggioErrore);
     		%>
        	<jsp:forward page="<%= sianTitoliSingoliUrl %>"/>
     		<%
   		}
   		request.setAttribute("elencoDescMovimento", elencoDescMovimento);
   	}
	  // Secondo monitoraggio
	  watcher.dumpElapsed("sianTitoliSingoliCtrl.jsp", "titoliProduttore", "In titoli method from the end of execution of Sian's method to get description", "No parameters");

    %>
      <jsp:forward page="<%= sianTitoliSingoliUrl %>"/>
    <%
  }
%>

