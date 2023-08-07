<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "cessazioneAttivitaCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

  	String elencoAllevamentiCtrlUrl = "/ctrl/allevamentiCtrl.jsp"; //allevamentiCtrl.jsp
  	String cessazioneAttivitaUrl = "/view/cessazioneAttivitaView.jsp";
  	String elencoFabbricatiCtrlUrl = "/ctrl/fabbricatiCtrl.jsp";  //fabbricatiCtrl.jsp
  	String elencoManodoperaUrl = "/ctrl/manodoperaCtrl.jsp";

  	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  	ValidationErrors errors = new ValidationErrors();

  	String elemento = request.getParameter("idElemento");
  	Long idElemento = null;
  	if(!Validator.isNotEmpty(elemento)) {
    	idElemento = Long.decode(request.getParameter("radiobutton"));
  	}
  	else {
    	idElemento = Long.decode(request.getParameter("idElemento"));
  	}

  	request.setAttribute("idElemento", idElemento);

  	String operazione = request.getParameter("operazione");
  	String annoRicerca = request.getParameter("idAnno");

 	// L'utente ha selezionato il pulsante conferma
  	if(request.getParameter("conferma") != null) {
    	// Se l'utente arriva dall'elenco degli allevamenti
    	if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_ALLEVAMENTO"))) {
      		if(idElemento == null) {
        		idElemento = Long.decode(request.getParameter("idAllevamento"));
      		}
      		// Effettuo la cessazione dell'allevamento selezionato
      		AllevamentoAnagVO allevamentoAnagVO = new AllevamentoAnagVO();
      		allevamentoAnagVO.setIdAllevamentoLong(idElemento);
      		try {
        		anagFacadeClient.storicizzaAllevamento(allevamentoAnagVO, ruoloUtenza.getIdUtente());
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
        		request.getRequestDispatcher(cessazioneAttivitaUrl).forward(request, response);
        		return;
      		}

     	 	// Torno alla pagina di elenco degli allevamenti
      		%>
        		<jsp:forward page="<%= elencoAllevamentiCtrlUrl %>"/>
      		<%
    	}
    	// Se l'utente arriva dall'elenco dei fabbricati
    	else if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_FABBRICATO"))) {

      		// Cesso il record selezionato sia su DB_FABBRICATO che su DB_FABBRICATO_PARTICELLA
      		try {
        		anagFacadeClient.cessaFabbricato(idElemento, ruoloUtenza.getIdUtente());
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
        		request.getRequestDispatcher(cessazioneAttivitaUrl).forward(request, response);
        		return;
      		}
      		// Torno alla pagina di elenco dei fabbricati
      		%>
        		<jsp:forward page="<%= elencoFabbricatiCtrlUrl %>"/>
      		<%
    	}
    	// L'utente arriva dalla pagina di elenco della manodopera
    	else if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_MANODOPERA"))) 
      {
      		// Creo l'oggetto manodopera
      		ManodoperaVO manodoperaVO = new ManodoperaVO();
      		manodoperaVO.setIdManodoperaLong(idElemento);
      		UtenteIrideVO utenteIrideVO = new UtenteIrideVO();
      		manodoperaVO.setUtenteAggiornamento(utenteIrideVO);
     	 	  manodoperaVO.getUtenteAggiornamento().setIdUtente(ruoloUtenza.getIdUtente());
      		manodoperaVO.setIdAziendaLong(anagAziendaVO.getIdAzienda());
      		manodoperaVO.setDataSituazioneAl("01/01/"+annoRicerca);

      		// Effettuo la cessazione del record selezionato
      		try {
        		anagFacadeClient.storicizzaManodopera(manodoperaVO);
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
        		request.getRequestDispatcher(cessazioneAttivitaUrl).forward(request, response);
        		return;
      		}

     	 	// Effettuo nuovamente la ricerca
      		Vector elencoManodopera = null;
      		try {
        		elencoManodopera = anagFacadeClient.getManodoperaAnnua(manodoperaVO);
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
       			request.getRequestDispatcher(cessazioneAttivitaUrl).forward(request, response);
       	 		return;
      		}

      		// Metto il vettore in request
     		request.setAttribute("elencoManodopera", elencoManodopera);

      		iridePageName = "manodoperaCtrl.jsp";
      		%>
      			<%@include file = "/include/autorizzazione.inc" %>
      		<%

      		// Torno alla pagina di elencoManodopera
      		%>
        		<jsp:forward page="<%= elencoManodoperaUrl %>"/>
      		<%
    	}
  	}
  	// L'utente ha selezionato il pulsante annulla
  	else if(request.getParameter("annulla") != null) {
    	// L'utente arriva dalla pagina di elencoAllevamenti
    	if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_ALLEVAMENTO"))) {
      		// Torno alla pagina di elenco degli allevamenti
      		%>
        		<jsp:forward page="<%= elencoAllevamentiCtrlUrl %>"/>
      		<%
    	}
    	// L'utente arriva dalla pagina di elenco fabbricati
    	else if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_FABBRICATO"))) {
      		// Torno alla pagina di elenco dei fabbricati
      		%>
        		<jsp:forward page="<%= elencoFabbricatiCtrlUrl %>"/>
      		<%
    	}
    	// L'utente arriva dalla pagina di elenco delle manodopera
    	else if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_MANODOPERA"))) {
      		// Creo l'oggetto manodopera
      		ManodoperaVO manodoperaVO = new ManodoperaVO();
      		manodoperaVO.setIdAziendaLong(anagAziendaVO.getIdAzienda());
      		manodoperaVO.setDataSituazioneAl("01/01/"+annoRicerca);
      		// Effettuo la ricerca delle manodopere
      		Vector elencoManodopera = null;
      		try {
        		elencoManodopera = anagFacadeClient.getManodoperaAnnua(manodoperaVO);
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
        		request.getRequestDispatcher(cessazioneAttivitaUrl).forward(request, response);
        		return;
      		}

      		// Metto il vettore in request
      		request.setAttribute("elencoManodopera", elencoManodopera);

      		iridePageName = "manodoperaCtrl.jsp";
      		%>
      			<%@include file = "/include/autorizzazione.inc" %>
      		<%

      		// Torno alla pagina di elencoManodopera
      		%>
        		<jsp:forward page="<%= elencoManodoperaUrl %>"/>
      		<%
    	}
  	}
  	// L'utente ha selezionato la funzionalità cessazione
  	else {
    	// L'utente arriva dall'elenco degli allevamenti
    	if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_ALLEVAMENTO"))) {
      		// Controllo che l'allevamento selezionato non sia cessato
      		AllevamentoAnagVO allevamentoAnagVO = null;
      		try {
        		allevamentoAnagVO = anagFacadeClient.getAllevamento(idElemento);
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
       	 		request.getRequestDispatcher(elencoAllevamentiCtrlUrl).forward(request, response);
        		return;
      		}
      		if(allevamentoAnagVO.getDataFineDate() != null) {
        		ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_UPDATE_ALLEVAMENTO_STORICIZZATO);
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
                request.getRequestDispatcher(elencoAllevamentiCtrlUrl).forward(request, response);
        		return;
      		}
    	}
    	// L'utente ha selezionato la funzionalità cessazione dall'elenco dei fabbricati
    	else if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_FABBRICATO"))) {
      		// Recupero il fabbricato selezionato
      		FabbricatoVO fabbricatoVO = null;
      		try {
        		fabbricatoVO = anagFacadeClient.findFabbricatoByPrimaryKey(idElemento);
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
       	 		errors.add("error", error);
        		request.setAttribute("errors", errors);
        		iridePageName = "fabbricatiCtrl.jsp";
        		%>
        			<%@include file = "/include/autorizzazione.inc" %>
        		<%
        		request.getRequestDispatcher(elencoFabbricatiCtrlUrl).forward(request, response);
        		return;
      		}
      		// Controllo che il record selezionato non sia storicizzato
      		if(fabbricatoVO.getDataFineValiditaFabbricato() != null) {
        		ValidationError error = new ValidationError((String)AnagErrors.get("ERR_RECORD_STORICIZZATO"));
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
        		iridePageName = "fabbricatiCtrl.jsp";
        		%>
        			<%@include file = "/include/autorizzazione.inc" %>
        		<%
        		request.getRequestDispatcher(elencoFabbricatiCtrlUrl).forward(request, response);
        		return;
      		}
    	}
    	// L'utente ha selezionato la funzionalità cessazione dall'elenco della manodopera
    	// Se ho passato tutti i controlli vado alla pagina di richiesta conferma cessazione
    	else if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_MANODOPERA"))) {
      		// Recupero la manodopera selezionata
      		ManodoperaVO manodoperaVO = null;
      		try {
        		manodoperaVO = anagFacadeClient.dettaglioManodopera(idElemento);
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
        		errors.add("error", error);
       	 		request.setAttribute("errors", errors);
        		iridePageName = "manodoperaCtrl.jsp";
        		%>
        			<%@include file = "/include/autorizzazione.inc" %>
        		<%
        		request.getRequestDispatcher(elencoManodoperaUrl).forward(request, response);
        		return;
      		}
      		manodoperaVO.setIdAziendaLong(anagAziendaVO.getIdAzienda());
      		manodoperaVO.setDataSituazioneAl("01/01/"+annoRicerca);
      		// Effettuo la ricerca delle manodopere
      		Vector elencoManodopera = null;
      		try {
        		elencoManodopera = anagFacadeClient.getManodoperaAnnua(manodoperaVO);
      		}
      		catch(SolmrException se) {
        		ValidationError error = new ValidationError(se.getMessage());
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
        		request.getRequestDispatcher(cessazioneAttivitaUrl).forward(request, response);
        		return;
      		}
      		// Metto il vettore in request
      		request.setAttribute("elencoManodopera", elencoManodopera);
      		// Controllo che il record selezionato non sia storicizzato
      		if(manodoperaVO.getDataFineValidita() != null) {
        		ValidationError error = new ValidationError((String)AnagErrors.get("ERR_RECORD_STORICIZZATO"));
        		errors.add("error", error);
        		request.setAttribute("errors", errors);
        		iridePageName = "manodoperaCtrl.jsp";
        		%>
        			<%@include file = "/include/autorizzazione.inc" %>
        		<%
        		request.getRequestDispatcher(elencoManodoperaUrl).forward(request, response);
        		return;
      		}
    	}
    	request.setAttribute("idElemento", idElemento);
    	request.setAttribute("annoRicerca", annoRicerca);
    	%>
      		<jsp:forward page="<%= cessazioneAttivitaUrl %>"/>
    	<%
  	}

%>
