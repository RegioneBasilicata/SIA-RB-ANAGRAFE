<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.ComuneVO"%>
<%@ page import="it.csi.solmr.dto.ProvinciaVO"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "terreniUnitaArboreeElencoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String terreniUnitaArboreeElencoUrl = "/view/terreniUnitaArboreeElencoView.jsp";
	String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
	String attenderePregoUrl = "/view/attenderePregoView.jsp";
	String erroreViewUrl = "/view/erroreView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	String operazione = request.getParameter("operazione");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
	ValidationErrors errors = new ValidationErrors();
	StoricoParticellaVO[] elencoParticelleArboree = null;
	String[] orderBy = null;
	String messaggioErrore = null;
	ValidationError errori = (ValidationError)request.getAttribute("error");
	// Parametro che mi certifica se si sono verificati errori durante il richiamo
	// alla modifica delle UV.
	String erroreModificaUv = (String)request.getAttribute("erroreModificaUv");
  
  //Parametro presente nelle pagine di modofica e di modifca multipla
  //se arrivo da esse devo deselezionare tutti i checkbox
  String remove = request.getParameter("remove");
	if(errori != null) {
		errors.add("error", errori);
		request.setAttribute("errors", errors);
	}
	else 
  {
    if(Validator.isNotEmpty(remove)) 
    {
		  session.removeAttribute("numUnitaArboreeSelezionate");
    }
	}
	// Rimuovo dalla sessione il valore della pagina usato nelle fuzioni di modifica
	session.removeAttribute("pagina");
	
	String paginaCorrente = request.getParameter("pagina");
	// Se pagina corrente è null o "" significa che è la prima volta che accedo e
	// quindi lo setto de default a 1
	if(!Validator.isNotEmpty(paginaCorrente)) 
  {
		paginaCorrente = "1";
	}
	request.setAttribute("paginaCorrente", paginaCorrente);
	
	// Rimuovo dalla sessione il filtro dei terreni in modo da consentire il corretto
	// funzionamento del GIS
	session.removeAttribute("filtriParticellareRicercaVO");
	// Pulisco la sessione da oggetti creati eventualmente durante la navigazione
	// nelle pagine di modifica
	session.removeAttribute("elencoStoricoParticellaArboreaVO");
	session.removeAttribute("storicoParticellaVO");
	session.removeAttribute("elencoUnitaArboree");
	session.removeAttribute("storicoParticellaArboreaVisualizza");
	session.removeAttribute("oldElencoAltriVitigni");
	session.removeAttribute("elencoAltriVitigni");
	session.removeAttribute("elencoIdUnitaArboreaDichiarata");
	session.removeAttribute("storicoParticellaVODup");
	
	// Se è la prima volta che accedo alla funzionalità istanzio il filtro, setto
	// i valori di default e azzero le precedenti selezioni
	if(filtriUnitaArboreaRicercaVO == null) 
  {
		filtriUnitaArboreaRicercaVO = new FiltriUnitaArboreaRicercaVO();
    
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    Long idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), "unitaVitate"); 
    
    
		filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
    session.removeAttribute("numUnitaArboreeSelezionate");
 	}
	
	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
 	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
 	// in modo che venga sempre effettuato
 	try 
  {
 		anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
 	}
 	catch(SolmrException se) 
  {
 		request.setAttribute("statoAzienda", se);
 	}
 	HashMap<String,Vector<Long>> numUnitaArboreeSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numUnitaArboreeSelezionate");
 	if(numUnitaArboreeSelezionate == null) {
 		numUnitaArboreeSelezionate = new HashMap<String,Vector<Long>>();
 	}
 	Vector<Long> elencoUnitaArboreeSelezionate = new Vector<Long>();
 	String paginaChiave = request.getParameter("paginaCorrente");
  
  
  
  // Recupero il parametro UVAG da comune
  String parametroUVAG = null;
  try 
  {
    parametroUVAG = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_UVAG);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_UVAG;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%= terreniUnitaArboreeElencoUrl %>" />
    <%
  }
  
  
 	
	// L'utente ha premuto il pulsante esegui controlli e lo mando
  // alla pagina di attesa
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("attenderePrego")) 
  {
  	request.setAttribute("action", actionUrl);
    String operation = "eseguiControlli";
    request.setAttribute("operazione", operation);
    %>
		  <jsp:forward page= "<%= attenderePregoUrl %>" />
    <%
  }
	
 	
	// Recupero i valori relativi alla destinazione produttiva
	TipoUtilizzoVO[] elencoTipiUsoSuolo = null;
	try 
  {
		//String[] ordinamento = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
 		elencoTipiUsoSuolo = anagFacadeClient.getListTipoUtilizzoByIdAzienda(SolmrConstants.TIPO_UTILIZZO_VIGNETO, 
      anagAziendaVO.getIdAzienda().longValue());
 		request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
	}
	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA);
		errors.add("idTipoUtilizzoElenco", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
   	return;
	}
	
	// Recupero il vitigno in funzione della destinazione produttiva selezionata
 	TipoVarietaVO[] elencoVarieta = null;
 	try 
  {
		elencoVarieta = anagFacadeClient.getListTipoVarietaByIdAzienda(
      anagAziendaVO.getIdAzienda().longValue());
		request.setAttribute("elencoVarieta", elencoVarieta);
 	}
 	catch(SolmrException se) 
  {
 		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_VITIGNO);
		errors.add("idVarieta", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
   	return;
	}
  
  
  // Recupero i valori relativi alle provincie su cui insistono le particelle
  Vector<ProvinciaVO> elencoProvincie = null;
  try 
  {
    elencoProvincie = gaaFacadeClient.getListProvincieParticelleByIdAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("elencoProvincie", elencoProvincie);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("istatProvinciaConduzioniParticelle", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
    return;
  }
	
	// Recupero i valori relativi ai comuni su cui insistono le particelle
 	Vector<ComuneVO> elencoComuni = null;
 	try 
  {
		String[] ordinamento = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE};
	 	elencoComuni = anagFacadeClient.getListComuniParticelleByIdAzienda(anagAziendaVO.getIdAzienda(), false, ordinamento);
	 	request.setAttribute("elencoComuni", elencoComuni);
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("istatComuniConduzioniParticelle", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
   	return;
 	}
  
  // Recupero i valori relativi alla tipologia Vino
  TipoTipologiaVinoVO[] elencoTipiTipologiaVino = null;
  try 
  {
    TipoTipologiaVinoVO[] elencoTipiVino = null;
    elencoTipiTipologiaVino = anagFacadeClient.getListTipoTipologiaVinoForAzienda(anagAziendaVO
      .getIdAzienda().longValue());
    int dim = elencoTipiTipologiaVino.length;
    
    elencoTipiVino = new TipoTipologiaVinoVO[dim+2];
    TipoTipologiaVinoVO ttvAll = new TipoTipologiaVinoVO();
    TipoTipologiaVinoVO ttvNn = new TipoTipologiaVinoVO();
    ttvAll.setIdTipologiaVino(new Long(-1));
    ttvAll.setDescrizione("qualunque tipologia di vino");
    ttvNn.setIdTipologiaVino(new Long(0));
    ttvNn.setDescrizione("senza tipologia di vino");
    
    elencoTipiVino[0] = ttvAll;
    elencoTipiVino[1] = ttvNn;
    
    for(int i=2; i<dim+2; i++)
      elencoTipiVino[i] = elencoTipiTipologiaVino[i-2];
      
    request.setAttribute("elencoTipiTipologiaVino", elencoTipiVino);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_TIPOLOGIA_VINO);
    errors.add("idTipologiaVino", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
    return;
  }
  
  
  // Recupero i valori del genere iscrizione
  Vector<TipoGenereIscrizioneVO> vTipoGenereIscrizione = null;
  try 
  {
    vTipoGenereIscrizione = anagFacadeClient.getListTipoGenereIscrizione();
      
    request.setAttribute("vTipoGenereIscrizione", vTipoGenereIscrizione);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_GENERE_ISCRIZIONE);
    errors.add("idGenereIscrizione", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
    return;
  }
  
  //Notifiche
  Vector<CodeDescription> vTipologiaNotifica = null;
  try 
  {
    vTipologiaNotifica = anagFacadeClient.getTipiTipologiaNotificaFromEntita(SolmrConstants.COD_TIPO_ENTITA_UV);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPOLOGIA_NOTIFICA);
    errors.add("idTipologiaNotifica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
    return;
  }
  // Metto il vettore in request
  request.setAttribute("vTipologiaNotifica", vTipologiaNotifica);
  
  
  Vector<TipoCategoriaNotificaVO> vCategoriaNotifica = null;
  try 
  {
    vCategoriaNotifica = anagFacadeClient.
      getTipiCategoriaNotificaFromEntita(SolmrConstants.COD_TIPO_ENTITA_UV);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_NOTIFICA);
    errors.add("idCategoriaNotifica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
    return;
  }
  // Metto il vettore in request
  request.setAttribute("vCategoriaNotifica", vCategoriaNotifica);
  
  //Notifiche fine
  
  
  // Recupero i valori della causale modifica
  Vector<TipoCausaleModificaVO> vTipoCausaleModifica = null;
  try 
  {
    vTipoCausaleModifica = anagFacadeClient.getListTipoCuasaleModificaByIdAzienda(anagAziendaVO
      .getIdAzienda().longValue());      
    request.setAttribute("vTipoCausaleModifica", vTipoCausaleModifica);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_CAUSALE_MODIFICA);
    errors.add("idCausaleModifica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
    return;
  }
  
  // Recupero i valori relativi al tipo controllo
  TipoControlloVO[] elencoTipiControllo = null;
  try 
  {
    String[] orderByControlli = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
    elencoTipiControllo = anagFacadeClient.getListTipoControlloByIdGruppoControllo(SolmrConstants.ID_GRUPPO_CONTROLLO_PARTICELLARE, orderByControlli);
    request.setAttribute("elencoTipiControllo", elencoTipiControllo);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("idControllo", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
    return;
  }
  
  
  // Recupero i dati della compensazione azienale
  /*CompensazioneAziendaVO compensazioneAziendaVO = null;
  try 
  {
    compensazioneAziendaVO = gaaFacadeClient.getCompensazioneAzienda(anagAziendaVO.getIdAzienda().longValue());
    request.setAttribute("compensazioneAziendaVO", compensazioneAziendaVO);
  }
  catch(SolmrException se) 
  {
    messaggioErrore = AnagErrors.ERRORE_KO_COMPENSAZIONE_AZIENDA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%= terreniUnitaArboreeElencoUrl %>" />
    <%
  }*/
 	
	// L'utente ha selezionato il tasto ricerca
	if(request.getParameter("ricerca") != null) 
  {
		// Setto i parametri di ricerca impostati dall'utente
		Long idPianoRiferimento = Long.decode(request.getParameter("idDichiarazioneConsistenza"));
		filtriUnitaArboreaRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
		String idUtilizzoElenco = request.getParameter("idTipoUtilizzoElenco");
		if(Validator.isNotEmpty(idUtilizzoElenco)) {
			filtriUnitaArboreaRicercaVO.setIdUtilizzo(Long.decode(idUtilizzoElenco));
		}
		else {
			filtriUnitaArboreaRicercaVO.setIdUtilizzo(null);
		}
		String idVarieta = request.getParameter("idVarieta");
		if(Validator.isNotEmpty(idVarieta)) {
			filtriUnitaArboreaRicercaVO.setIdVarieta(Long.decode(idVarieta));
		}
		else {
			filtriUnitaArboreaRicercaVO.setIdVarieta(null);
		}
    String idTipologiaVino = request.getParameter("idTipologiaVino");
    if(Validator.isNotEmpty(idTipologiaVino)) {
      filtriUnitaArboreaRicercaVO.setIdTipologiaVino(Long.decode(idTipologiaVino));
    }
    else {
      filtriUnitaArboreaRicercaVO.setIdTipologiaVino(null);
    }
    
    String idGenereIscrizione = request.getParameter("idGenereIscrizione");
    if(Validator.isNotEmpty(idGenereIscrizione)) {
      filtriUnitaArboreaRicercaVO.setIdGenereIscrizione(Long.decode(idGenereIscrizione));
    }
    else {
      filtriUnitaArboreaRicercaVO.setIdGenereIscrizione(null);
    }
    
    String idTipologiaNotifica = request.getParameter("idTipologiaNotifica");
    if(Validator.isNotEmpty(idTipologiaNotifica)) {
      filtriUnitaArboreaRicercaVO.setIdTipologiaNotifica(Long.decode(idTipologiaNotifica));
    }
    else {
      filtriUnitaArboreaRicercaVO.setIdTipologiaNotifica(null);
    }
    
    String idCategoriaNotifica = request.getParameter("idCategoriaNotifica");
    if(Validator.isNotEmpty(idCategoriaNotifica)) {
      filtriUnitaArboreaRicercaVO.setIdCategoriaNotifica(Long.decode(idCategoriaNotifica));
    }
    else {
      filtriUnitaArboreaRicercaVO.setIdCategoriaNotifica(null);
    }
    
    String notificaChiusa = request.getParameter("notificaChiusa");
    if(Validator.isNotEmpty(notificaChiusa)) {
      filtriUnitaArboreaRicercaVO.setFlagNotificheChiuse(notificaChiusa);
    }
    else {
      filtriUnitaArboreaRicercaVO.setFlagNotificheChiuse(null);
    }
    
    String idCausaleModifica = request.getParameter("idCausaleModifica");
    if(Validator.isNotEmpty(idCausaleModifica)) {
      filtriUnitaArboreaRicercaVO.setIdCausaleModifica(Long.decode(idCausaleModifica));
    }
    else {
      filtriUnitaArboreaRicercaVO.setIdCausaleModifica(null);
    }
    
    String idControllo = request.getParameter("idControllo");
    if(Validator.isNotEmpty(idControllo)) {
      filtriUnitaArboreaRicercaVO.setIdControllo(Long.decode(idControllo));
    }
    else {
      filtriUnitaArboreaRicercaVO.setIdControllo(null);
    }
    
    filtriUnitaArboreaRicercaVO.setIstatProvincia(request.getParameter("istatProvinciaConduzioniParticelle"));
		filtriUnitaArboreaRicercaVO.setIstatComune(request.getParameter("istatComuniConduzioniParticelle"));
		filtriUnitaArboreaRicercaVO.setSezione(request.getParameter("sezione"));
		filtriUnitaArboreaRicercaVO.setFoglio(request.getParameter("foglio"));
		filtriUnitaArboreaRicercaVO.setParticella(request.getParameter("particella"));
		filtriUnitaArboreaRicercaVO.setSubalterno(request.getParameter("subalterno"));
		filtriUnitaArboreaRicercaVO.setTipoSegnalazioneBloccante(request.getParameter("segnalazioneBloccante"));
		filtriUnitaArboreaRicercaVO.setTipoSegnalazioneWarning(request.getParameter("segnalazioneWarning"));
		filtriUnitaArboreaRicercaVO.setTipoSegnalazioneOk(request.getParameter("segnalazioneOk"));
		
		// Rimetto la pagina di partenza a 1
		paginaCorrente = "1";
		request.setAttribute("paginaCorrente", paginaCorrente);
	}
	else 
  {
		if(Validator.isNotEmpty(operazione)) 
    {
      String parametroCCAV = null;
    
    
			// Se l'utente ha scelto di effettuare i controlli di validità delle unità arboree
			if(operazione.equalsIgnoreCase("eseguiControlli")) 
      {
	  			// Richiamo la procedura PL-SQL che si occupa di aggiornare i controlli sulle unità vitate
	  			try 
          {
            parametroCCAV = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_CCAV);
            
            // Richiamare il servizio erogato dalla CCIAA per l'elenco albo vigneti:
            // solamente se il parametro CCAV='S'.
            // Indipendentemente dall'esito di quanto sopra procedere comunque.
            if(parametroCCAV.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
            {
              // Richiamo il web-service per l'aggiornamento dell'albo vigneti
              try 
              {
               anagFacadeClient.sianAggiornaDatiAlboVigneti(anagAziendaVO);
              }
              catch(SolmrException se) {
              // Intercetto l'eccezione ma non blocco l'operazione
              }
            }
                       
	  				anagFacadeClient.controlliVerificaPLSQL(
	  				  anagAziendaVO.getIdAzienda(), DateUtils.getCurrentYear(), SolmrConstants.ID_GRUPPO_CONTROLLO_PARTICELLARE.intValue(),
	  				  ruoloUtenza.getIdUtente());
	      	}
	  			catch(SolmrException se) 
          {
	    	   	String messaggio = se.getMessage();
	        	request.setAttribute("messaggioErrore",messaggio);
	    	   	request.setAttribute("pageBack", actionUrl);
	    	   	%>
	    	   		<jsp:forward page="<%= erroreViewUrl %>" />
	    	   	<%
	    	   	return;
	  			}
			}
      
			// L'utente ha cliccato su uno dei numeri della paginazione
			if(operazione.equalsIgnoreCase("paginazione")) {
				String numPagina = request.getParameter("pagina");
				request.setAttribute("paginaCorrente", numPagina);
			}
			// L'utente ha selezionato l'ordinamento per comune decrescente
			else if(operazione.equalsIgnoreCase("comuneDisc")) {
				filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_DESC);
				request.setAttribute("paginaCorrente", "1");
			}
			// L'utente ha selezionato l'ordinamento per comune ascendente
			else if(operazione.equalsIgnoreCase("comuneAsc")) {
				filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC);
				request.setAttribute("paginaCorrente", "1");
			}
			// L'utente ha selezionato l'ordinamento progressivo decrescente
			else if(operazione.equalsIgnoreCase("progrDisc")) {
				filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_PROGR_UNAR_DESC);
				request.setAttribute("paginaCorrente", "1");
			}
			// L'utente ha selezionato l'ordinamento progressivo ascendente
			else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("progrAsc")) {
				filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_PROGR_UNAR_ASC);
				request.setAttribute("paginaCorrente", "1");
			}
      // L'utente ha selezionato l'ordinamento data impianto decrescente
      else if(operazione.equalsIgnoreCase("dataImpiantoDisc")) {
        filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DATA_IMPIANTO_DESC);
        request.setAttribute("paginaCorrente", "1");
      }
      // L'utente ha selezionato l'ordinamento data impianto ascendente
      else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("dataImpiantoAsc")) {
        filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DATA_IMPIANTO_ASC);
        request.setAttribute("paginaCorrente", "1");
      }
			// L'utente ha selezionato l'ordinamento destinazione produttiva decrescente
			else if(operazione.equalsIgnoreCase("descUsoDisc")) {
				filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_UTILIZZO_UNAR_DESC);
				request.setAttribute("paginaCorrente", "1");
			}
			// L'utente ha selezionato l'ordinamento destinazione produttiva ascendente
			else if(operazione.equalsIgnoreCase("descUsoAsc")) {
				filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_UTILIZZO_UNAR_ASC);
				request.setAttribute("paginaCorrente", "1");
			}
			// L'utente ha selezionato l'ordinamento varietà decrescente
			else if(operazione.equalsIgnoreCase("descVarietaDisc")) {
				filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_VARIETA_UNAR_DESC);
				request.setAttribute("paginaCorrente", "1");
			}
			// L'utente ha selezionato l'ordinamento varietà ascendente
			else if(operazione.equalsIgnoreCase("descVarietaAsc")) {
				filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_VARIETA_UNAR_ASC);
				request.setAttribute("paginaCorrente", "1");
			}
      // L'utente ha selezionato l'ordinamento tipologia vino decrescente
      else if(operazione.equalsIgnoreCase("descVinoDisc")) {
        filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_TIPOLOGIA_VINO_DESC);
        request.setAttribute("paginaCorrente", "1");
      }
      // L'utente ha selezionato l'ordinamento tipologia vino ascendente
      else if(operazione.equalsIgnoreCase("descVinoAsc")) {
        filtriUnitaArboreaRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_TIPOLOGIA_VINO_ASC);
        request.setAttribute("paginaCorrente", "1");
      }
		}
		
		// Metto l'oggetto in sessione
		session.setAttribute("filtriUnitaArboreaRicercaVO", filtriUnitaArboreaRicercaVO);
	}
	
	// Se non è stato scelto nessun criterio di ordinamento imposto il default
	if(!Validator.isNotEmpty(operazione)) 
  {
		if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) 
    {
			orderBy = new String[]{SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY, SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC, SolmrConstants.ORDER_BY_PROGR_UNAR_ASC, SolmrConstants.ORDER_BY_UV_DATA_FINE_VALIDITA_DESC};
		}
		else 
    {
			orderBy = new String[]{SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY, SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC, SolmrConstants.ORDER_BY_PROGR_UNAR_ASC, SolmrConstants.ORDER_BY_UV_DICHIARATA_DATA_FINE_VALIDITA_DESC};
		}
		filtriUnitaArboreaRicercaVO.setOrderBy(null);
	}
	else 
  {
		if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getOrderBy())) 
    {
			if(filtriUnitaArboreaRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_DESC) 
        || filtriUnitaArboreaRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC)) 
      {
				if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) 
        {
					orderBy = new String[]{filtriUnitaArboreaRicercaVO.getOrderBy().concat(", ")+SolmrConstants.ORDER_BY_PARTIAL_STORICO_PARTICELLA_LOGIC_KEY, SolmrConstants.ORDER_BY_UV_DATA_FINE_VALIDITA_DESC};
				}
				else 
        {
					orderBy = new String[]{filtriUnitaArboreaRicercaVO.getOrderBy().concat(", ")+SolmrConstants.ORDER_BY_PARTIAL_STORICO_PARTICELLA_LOGIC_KEY, SolmrConstants.ORDER_BY_UV_DICHIARATA_DATA_FINE_VALIDITA_DESC};
				}
			}
			else 
      {
				if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) 
        {
					orderBy = new String[]{filtriUnitaArboreaRicercaVO.getOrderBy().concat(", ")+SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY.concat(", ")+SolmrConstants.ORDER_BY_UV_DATA_FINE_VALIDITA_DESC};
				}
				else 
        {
					orderBy = new String[]{filtriUnitaArboreaRicercaVO.getOrderBy().concat(", ")+SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY.concat(", ")+SolmrConstants.ORDER_BY_UV_DICHIARATA_DATA_FINE_VALIDITA_DESC};
				}
			}
		}
		else 
    {
			if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) 
      {
				orderBy = new String[]{SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY, SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC, SolmrConstants.ORDER_BY_PROGR_UNAR_ASC, SolmrConstants.ORDER_BY_UV_DATA_FINE_VALIDITA_DESC};
			}
			else 
      {
				orderBy = new String[]{SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY, SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC, SolmrConstants.ORDER_BY_PROGR_UNAR_ASC, SolmrConstants.ORDER_BY_UV_DICHIARATA_DATA_FINE_VALIDITA_DESC};
			}
		}
	}
	
	// Se l'utente ha selezionato il pulsante vai
	if(request.getParameter("vai") != null || (Validator.isNotEmpty(request.getParameter("operazioneDefault")) && request.getParameter("operazioneDefault").equalsIgnoreCase("operazioneDefault"))) {
		String numPagina = request.getParameter("paginaCorrente");
		paginaChiave = request.getParameter("pagina");
		// Controllo che sia stato indicato il numero di pagina
		if(!Validator.isNotEmpty(numPagina)) {
			ValidationError error = new ValidationError(AnagErrors.ERRORE_OBBLIGO_NUM_PAGINAZIONE_PARTICELLE);
		 	errors.add("paginaCorrente", error);
	     	request.setAttribute("errors", errors);
		}
		// Controllo che sia un numero intero e positivo
		else if(!Validator.isNumericInteger(numPagina) || Integer.parseInt(numPagina) <= 0) {
			ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_NUM_PAGINAZIONE_PARTICELLE);
		 	errors.add("paginaCorrente", error);
	     	request.setAttribute("errors", errors);
		}
		// Controllo che non sia superiore al rapporto con il numero totale di pagine possibili
		else if(Integer.parseInt(numPagina) > Integer.parseInt(request.getParameter("numeroTotale"))) {
			ValidationError error = new ValidationError(AnagErrors.ERRORE_NUM_PAGINAZIONE_PARTICELLE_SUP_NUM_TOT);
		 	errors.add("paginaCorrente", error);
	     	request.setAttribute("errors", errors);
		}
		// Se passo i controlli metto il valore in request
		if(errors.size() == 0) {
			request.setAttribute("paginaCorrente", numPagina);
		}
		else {
			request.setAttribute("paginaCorrente", paginaCorrente);
		}
	}
	
	String[] idUnita = request.getParameterValues("idUnita");
	// Gestisco la selezione delle particelle prima di effettuare la ricerca ed 
	// andare alla pagina selezionata: se sono nella prima di default ovviamente
	// non farò nulla: tutto ciò viene fatto solo se sto navigando tra le pagine
	// della ricerca e non se arrivo qui dai controller di modifica delle UV
	if(!Validator.isNotEmpty(erroreModificaUv)) 
  {
		if(idUnita != null && idUnita.length > 0) {
			for(int i = 0; i < idUnita.length; i++) {
				Long idUnitaLg = Long.decode((String)idUnita[i]);
				elencoUnitaArboreeSelezionate.add(idUnitaLg);
			}
			numUnitaArboreeSelezionate.put(paginaChiave, elencoUnitaArboreeSelezionate);
			session.setAttribute("numUnitaArboreeSelezionate", numUnitaArboreeSelezionate);
		}
		else {
			numUnitaArboreeSelezionate.remove(paginaChiave);
			session.setAttribute("numUnitaArboreeSelezionate", numUnitaArboreeSelezionate);
		}
	}
  
  
  String paginaFiltro = (String)request.getAttribute("paginaCorrente");
  
  try 
  {
    filtriUnitaArboreaRicercaVO.setPaginaCorrente(Integer.parseInt(paginaFiltro));
  } 
  catch (Exception e) 
  {
    filtriUnitaArboreaRicercaVO.setPaginaCorrente(1);
  }
	
	try 
  {
		// Vado a reperire l'elenco delle unità arboree relative all'azienda selezionata
		elencoParticelleArboree = anagFacadeClient.searchStoricoUnitaArboreaByParameters(
      parametroUVAG, anagAziendaVO.getIdAzienda(), filtriUnitaArboreaRicercaVO, orderBy);
    //utilizzato pr il consolidamento (visualizzazione tolleranza!!!)
    if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().compareTo(new Long(0)) > 0)
    {
      ConsistenzaVO consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento());
      request.setAttribute("consistenzaVO", consistenzaVO);
    }
    
		if(elencoParticelleArboree != null && elencoParticelleArboree.length > 0) 
    {
			// Controllo, se arrivo dalle pagine di modifica, che la pagina precedentemente
			// selezionata sia ancora esistente dopo la modifica delle particelle che potrebbe
			// aver portato ad un numero diverso di records restituiti dalla query
			int numTotalePagine = elencoParticelleArboree.length / Integer.parseInt(SolmrConstants.NUMBER_RECORDS_FOR_PAGE_TERRENI);
			int restoPagina = elencoParticelleArboree.length % Integer.parseInt(SolmrConstants.NUMBER_RECORDS_FOR_PAGE_TERRENI);
			if(restoPagina != 0) 
      {
				numTotalePagine++;
			}
			if(Integer.parseInt((String)request.getAttribute("paginaCorrente")) > numTotalePagine) {
				request.setAttribute("paginaCorrente", String.valueOf(numTotalePagine));
			}
      
      //Inserisco nel filtro della ricerca delle unità arboree il valore del flagProvinciaCompetenza.
      //true se tra le unità arboree ne esiste almeno una della provincia
      //del ruolo utente provinciale!
      //false se non ne esitono
      if(ruoloUtenza.isUtenteProvinciale()
        && (ruoloUtenza.getCodiceEnte() !=null))
      {
        for(int i=0;i<elencoParticelleArboree.length;i++)
        {
          StoricoParticellaVO stVO = (StoricoParticellaVO)elencoParticelleArboree[i];
          if(ruoloUtenza.getCodiceEnte()
            .equals(stVO.getComuneParticellaVO().getIstatProvincia()))
          {
            filtriUnitaArboreaRicercaVO.setFlagProvinciaCompetenza(true);
            break;
          }
          
        }
      }
      
          
		}
		request.setAttribute("elencoParticelleArboree", elencoParticelleArboree);
	}
	catch(SolmrException se) 
  {
		messaggioErrore = AnagErrors.ERRORE_KO_UNITA_ARBOREE;
		request.setAttribute("messaggioErrore",messaggioErrore);
		%>
  			<jsp:forward page="<%=terreniUnitaArboreeElencoUrl%>" />
		<%
	}
 
	// Recupero la data ultima esecuzione controlli
 	String dataEsecuzioneControlli = null;
 	try 
  {
		dataEsecuzioneControlli = anagFacadeClient.getMaxDataEsecuzioneControlliUnitaArborea(anagAziendaVO.getIdAzienda());
	 	if(Validator.isNotEmpty(dataEsecuzioneControlli)) 
    {
	 		filtriUnitaArboreaRicercaVO.setDataEsecuzioneControlli(StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, dataEsecuzioneControlli));
	 	}  
 	}
 	catch(SolmrException se) {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_UNITA_ARBOREA);
	 	errors.add("dataEsecuzioneControlli", error);
     	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(terreniUnitaArboreeElencoUrl).forward(request, response);
     	return;
 	}

	// Vado alla pagina di elenco delle unità arboree
	%>
		<jsp:forward page= "<%= terreniUnitaArboreeElencoUrl %>" />
	<%

%>

