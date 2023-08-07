<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%@ page import="java.sql.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  	String iridePageName = "modificaGestoreFascicoloCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%
    String modificaGestoreFascicoloURL = "/view/modificaGestoreFascicoloView.jsp";
  	String gestoreFascicoloCtrlURL = "/ctrl/gestoreFascicoloCtrl.jsp";

  	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    String messaggioErrore = "";
    DelegaVO delegaVO = null;
    UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = null;
    String messaggioErroreData = "";
    String messaggioErroreDataChiusura = "";

    // Ricerco nuovamente l'azienda per poter avere la situazione aggiornata in relazione
    // alla delega
    try {
    	anagAziendaVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
    	session.setAttribute("anagAziendaVO", anagAziendaVO);
    }
    catch(SolmrException se) {
    	messaggioErrore = AnagErrors.ERRORE_KO_ANAGRAFICA_AZIENDA;
   		request.setAttribute("messaggioErrore", messaggioErrore);
   		%>
			<jsp:forward page="<%= modificaGestoreFascicoloURL %>"/>
   		<%
    }
    // Ricerco i dati relativi alla delega dell'azienda agricola
    if(anagAziendaVO.isPossiedeDelegaAttiva()) {
    	try {
        	delegaVO = anagFacadeClient.getDelegaByAziendaAndIdProcedimento(anagAziendaVO.getIdAzienda(), Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)));
        	ufficioZonaIntermediarioVO = anagFacadeClient.findUfficioZonaIntermediarioVOByPrimaryKey(delegaVO.getIdUfficioZonaIntermediario());
      	}
      	catch(SolmrException se) {
      		messaggioErrore = AnagErrors.ERRORE_KO_DELEGA;
       		request.setAttribute("messaggioErrore", messaggioErrore);
       		%>
          		<jsp:forward page="<%= modificaGestoreFascicoloURL %>"/>
       		<%
      	}
      	request.setAttribute("ufficioZonaIntermediarioVO", ufficioZonaIntermediarioVO);
      	request.setAttribute("delegaVO",delegaVO);
    }
    
    // Recupero l'elenco degli OPR
    it.csi.solmr.dto.DoubleStringcodeDescription[] elencoOPR = null;
    try {
    	String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
    	elencoOPR = anagFacadeClient.getListSianTipoOpr(true, orderBy);
    	request.setAttribute("elencoOPR", elencoOPR);
    }
    catch(SolmrException se) {
  		messaggioErrore = AnagErrors.ERRORE_KO_TIPO_SIAN_OPR;
   		request.setAttribute("messaggioErrore", messaggioErrore);
   		%>
      		<jsp:forward page="<%= modificaGestoreFascicoloURL %>"/>
   		<%
  	}
    
    // Recupero l'eventuale OPR a cui potrebbe essere stato affidato il fascicolo
    RespAnagFascicoloVO respAnagFascicoloVO = null;
    try {
    	respAnagFascicoloVO = anagFacadeClient.getRespAnagFascicolo(anagAziendaVO.getIdAzienda());
    	request.setAttribute("respAnagFascicoloVO", respAnagFascicoloVO);
    	request.setAttribute("idOpr", anagAziendaVO.getIdOpr());
    }
    catch(SolmrException se) {
  		messaggioErrore = AnagErrors.ERRORE_KO_OPR_RESP_FASCICOLO;
   		request.setAttribute("messaggioErrore", messaggioErrore);
   		%>
      		<jsp:forward page="<%= modificaGestoreFascicoloURL %>"/>
   		<%
  	}
    
    // L'utente ha selezionato il pulsante annulla
    if(request.getParameter("annulla") != null) {
    	// Torno alla pagina di gestore del fascicolo
    	%>
			<jsp:forward page="<%= gestoreFascicoloCtrlURL %>"/>
		<%
    }
	//  L'utente ha selezionato il pulsante conferma
	else if(request.getParameter("conferma") != null) {
    	// Recupero i parametri
    	String opr = request.getParameter("idOpr");
    	String dataAperturaFascicolo = request.getParameter("dataAperturaFascicolo");
    	String dataChiusuraFascicolo = request.getParameter("dataChiusuraFascicolo");
		
    	if(Validator.isNotEmpty(opr)) {
    		request.setAttribute("idOpr", Long.decode(opr));
	    	if(Validator.isNotEmpty(dataAperturaFascicolo)) {
	    		request.setAttribute("dataAperturaFascicolo", dataAperturaFascicolo);
	    		// Controllo che sia una data valida
	    		if(!Validator.validateDateF(dataAperturaFascicolo)) {
	    			messaggioErroreData = AnagErrors.ERRORE_CAMPO_ERRATO;
	    		}
	    		// Se lo è...
	    		else {
	    			// ... controllo che non sia maggiore della data di sistema
	    			if(DateUtils.parseDate(dataAperturaFascicolo).after(new java.util.Date(System.currentTimeMillis()))) {
	    				messaggioErroreData = AnagErrors.ERRORE_DATA_APERTURA_FASCICOLO_POST_SYSDATE;
	    			}
	    			else {
		    			// Recupero la min data inizio validita dell'azienda
		    			java.util.Date dataMin = null;
		    			try {
		    				dataMin = anagFacadeClient.getMinDataInizioValiditaAnagraficaAzienda(anagAziendaVO.getIdAzienda());
		    			}
		    			catch(SolmrException se) {
		    		  		messaggioErrore = AnagErrors.ERRORE_KO_DATA_MIN_ANAG_AZIENDA;
		    		   		request.setAttribute("messaggioErrore", messaggioErrore);
		    		   		%>
		    		      		<jsp:forward page="<%= modificaGestoreFascicoloURL %>"/>
		    		   		<%
		    		  	}
		    			// La data apertura deve essere >= della min data inizio validita dell'azienda
		    			if(DateUtils.parseDate(dataAperturaFascicolo).before(dataMin)) {
		    				messaggioErroreData = AnagErrors.ERRORE_KO_DATA_APERTURA_FASCICOLO;
		    			}
		    			else {
		    				// Controllo che non sia maggiore della data chiusura fascicolo
		    				if(Validator.isNotEmpty(dataChiusuraFascicolo) && Validator.validateDateF(dataChiusuraFascicolo)) {
		    					if(DateUtils.parseDate(dataAperturaFascicolo).after(DateUtils.parseDate(dataChiusuraFascicolo))) {
		    						messaggioErroreData = AnagErrors.ERRORE_KO_DATA_APERTURA_FASCICOLO_MAX_CHIUSURA;
		    					}
		    				}
		    			}
	    			}
	    		}
	    	}
	    	else {
	    		messaggioErroreData = AnagErrors.ERRORE_CAMPO_OBBLIGATORIO;
	    	}
	    	if(Validator.isNotEmpty(dataChiusuraFascicolo)) {
	    		request.setAttribute("dataChiusuraFascicolo", dataChiusuraFascicolo);
				// Controllo che sia una data valida
	    		if(!Validator.validateDateF(dataChiusuraFascicolo)) {
	    			messaggioErroreDataChiusura = AnagErrors.ERRORE_CAMPO_ERRATO;
	    		}
				// Se lo è...
	    		else {
	    			// ... Controllo che non sia maggiore della data odierna
	    			// ... controllo che non sia maggiore della data di sistema
	    			if(DateUtils.parseDate(dataChiusuraFascicolo).after(new java.util.Date(System.currentTimeMillis()))) {
	    				messaggioErroreDataChiusura = AnagErrors.ERRORE_DATA_CHIUSURA_FASCICOLO_POST_SYSDATE;
	    			}
	    			else {
		    			if(Validator.isNotEmpty(dataAperturaFascicolo) && Validator.validateDateF(dataAperturaFascicolo)) {
			    			// La data chiusuta deve essere >= della data apertura
			    			if(DateUtils.parseDate(dataChiusuraFascicolo).before(DateUtils.parseDate(dataAperturaFascicolo))) {
			    				messaggioErroreDataChiusura = AnagErrors.ERRORE_KO_DATA_CHIUSURA_FASCICOLO_MIN_APERTURA;
			    			}
		    			}
	    			}
	    		}
	    	}
    	}
    	
    	// Se ci sono errori li visualizzo
    	if(Validator.isNotEmpty(messaggioErroreData) || (Validator.isNotEmpty(messaggioErroreDataChiusura))) {
    		request.setAttribute("messaggioErroreData", messaggioErroreData);
    		request.setAttribute("messaggioErroreDataChiusura", messaggioErroreDataChiusura);
    	}
    	// Altrimenti...
    	else {
    		// Effettuo la modifica
    		if(Validator.isNotEmpty(opr)) {
    			anagAziendaVO.setIdOpr(Long.decode(opr));
    			anagAziendaVO.setDataAperturaFascicolo(DateUtils.parseDate(dataAperturaFascicolo));
    		}
    		else {
    			anagAziendaVO.setIdOpr(null);
    			anagAziendaVO.setDataAperturaFascicolo(null);    			
    		}
    		if(Validator.isNotEmpty(dataChiusuraFascicolo)) {
    			anagAziendaVO.setDataChiusuraFascicolo(DateUtils.parseDate(dataChiusuraFascicolo));
    		}
    		else {
    			anagAziendaVO.setDataChiusuraFascicolo(null);
    		}
    		anagAziendaVO.setDataAggiornamentoOpr(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
    		anagAziendaVO.setIdUtenteAggiornamentoOpr(ruoloUtenza.getIdUtente());
    		try {
    			anagFacadeClient.modificaGestoreFascicolo(anagAziendaVO);
    		}
    		catch(SolmrException se) {
		  		messaggioErrore = AnagErrors.ERRORE_KO_MODIFICA_GESTORE_FASCICOLO;
		   		request.setAttribute("messaggioErrore", messaggioErrore);
		   		%>
		      		<jsp:forward page="<%= modificaGestoreFascicoloURL %>"/>
		   		<%
		  	}
			// Torno alla pagina di gestore del fascicolo
        	%>
    			<jsp:forward page="<%= gestoreFascicoloCtrlURL %>"/>
    		<%
    	}
    }
    
    %>
		<jsp:forward page="<%= modificaGestoreFascicoloURL %>"/>
	<%

%>
