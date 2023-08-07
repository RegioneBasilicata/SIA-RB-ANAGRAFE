<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.util.performance.*"%>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.solmr.dto.ComuneVO"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.BaseCodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>

<%
	
	//Pulisco la sessione dall'oggetto contenente i filtri di ricerca relativi alle
	// altre sezioni
	session.removeAttribute("documentoRicercaVO");
	session.removeAttribute("filtriUnitaArboreaRicercaVO");
	session.removeAttribute("oldSuperficieAgronomica");
	//filtri pratiche
	session.removeAttribute("filtroAnniCombo");
  session.removeAttribute("filtroProcedimenti");
	
	// Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo
	StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
	// START
	watcher.start();	
	
	// Pulisco la sessione dall'oggetto contenente i filtri di ricerca relativi ai documenti
	session.removeAttribute("documentoRicercaVO");
	
	String iridePageName = "terreniParticellareElencoCtrl.jsp";
  %>
		<%@include file="/include/autorizzazione.inc"%>
	<%

	String terreniParticellareElencoUrl = "/view/terreniParticellareElencoView.jsp";
	String attenderePregoUrl = "/view/attenderePregoView.jsp";
	String actionUrl = "../layout/terreniParticellareElenco.htm";
	String erroreViewUrl = "/view/erroreView.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione Terreni."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  ValidationErrors errors = new ValidationErrors();
  ValidationError errori = (ValidationError)request.getAttribute("error");
  //Parametro presente nelle pagine di modifica e di modifca multipla
  //se arrivo da esse devo deselezionare tutti i checkbox
  String remove = request.getParameter("remove");
  if(errori != null) 
  {
    errors.add("error", errori);
    request.setAttribute("errors", errors);
  }
  else 
  {
    if(Validator.isNotEmpty(remove)) 
    {
      session.removeAttribute("numParticelleSelezionate");
      session.removeAttribute("numUtilizziSelezionati");
    }
  }
  
  
  
  //Controllo se è stato selezionato lo scarico del file excel
  if("excel".equals(request.getParameter("operazione")))
  {
  
    HashMap<Long,ConsistenzaVO> elencoPianiRiferimento = new HashMap<Long,ConsistenzaVO>();
    try 
    {
      String[] orderBy = {SolmrConstants.ORDER_BY_ANNO_CONSISTENZA_DESC, SolmrConstants.ORDER_BY_DATA_CONSISTENZA_DESC};
      ConsistenzaVO[] elencoDateConsistenza = anagFacadeClient.getListDichiarazioniConsistenzaByIdAzienda(anagAziendaVO.getIdAzienda(), orderBy);
      // Inserisco a mano i due riferimenti non reperibili da DB richiesti dal dominio
      ConsistenzaVO consistenzaVO = new ConsistenzaVO();
      consistenzaVO.setIdDichiarazioneConsistenza("0");
      ConsistenzaVO consistenzaStorVO = new ConsistenzaVO();
      consistenzaStorVO.setIdDichiarazioneConsistenza("-1");
      elencoPianiRiferimento.put(new Long(-1), consistenzaStorVO);
      elencoPianiRiferimento.put(new Long(0), consistenzaVO);     
      if(elencoDateConsistenza != null && elencoDateConsistenza.length > 0) 
      {
        for(int i = 0; i < elencoDateConsistenza.length; i++) 
        {
          ConsistenzaVO consistenzaElencoVO = (ConsistenzaVO)elencoDateConsistenza[i];
          elencoPianiRiferimento.put(Long.decode(consistenzaElencoVO.getIdDichiarazioneConsistenza()), consistenzaElencoVO);
        }
      }
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_PIANO_RIFERIMENTO);
      errors.add("idPianoRiferimento", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
      return;
    }
      
    String excelBrogliaccioUrl = "/servlet/ExcelBrogliaccioServlet";
    String descrizionePiano = "";
    if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == 0)
    {
      descrizionePiano = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione (con conduzioni storicizzate)";
    }
    else if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == -1)
    {
      descrizionePiano = DateUtils.getCurrent(SolmrConstants.DATE_EUROPEAN_STANDARD_FORMAT)+" in lavorazione";
    }
    else
    {
      ConsistenzaVO consVO = elencoPianiRiferimento.get(filtriParticellareRicercaVO.getIdPianoRiferimento());
      descrizionePiano = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, consVO.getData());
    }
    request.setAttribute("descrizionePiano", descrizionePiano); 
  
    %>
      <jsp:forward page="<%=excelBrogliaccioUrl%>" />
    <%
    return;
  }
  
  
  
	
	// Pulisco la sessione da oggetti creati eventualmente durante la navigazione
	// nelle pagine di modifica
	session.removeAttribute("elencoTipiUsoSuoloPrimario");
	session.removeAttribute("elencoTipiUsoSuoloSecondario");
	session.removeAttribute("idTipoUtilizzoPrimario");
	session.removeAttribute("idVarieta");
	session.removeAttribute("idTipoUtilizzoSecondario");
	session.removeAttribute("idVarietaSecondaria");
	session.removeAttribute("storicoParticellaVO");
	session.removeAttribute("elencoStoricoParticella");
	session.removeAttribute("indice");
	session.removeAttribute("elencoSupUtilizzate");
	session.removeAttribute("elencoSupUtilizzateSecondarie");
	session.removeAttribute("isCertificata");
	session.removeAttribute("storicoParticellaArboreaVisualizza");
	session.removeAttribute("oldElencoAltriVitigni");

	
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
	String operazione = request.getParameter("operazione");
	
	// Se è la prima volta che accedo alla funzionalità istanzio il filtro, setto
	// i valori di default e azzero le precedenti selezioni
	if(filtriParticellareRicercaVO == null) 
  {
		filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
    
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    Long idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null); 
    
	 	filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
	 	filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
	 	filtriParticellareRicercaVO.setCheckUsoPrimario(SolmrConstants.FLAG_S);
	 	filtriParticellareRicercaVO.setCheckEscludiAsservimento(SolmrConstants.FLAG_S);
	 	filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
	 	filtriParticellareRicercaVO.setCheckSoloConferite(SolmrConstants.FLAG_N);
    filtriParticellareRicercaVO.setCheckEscludiConferimento(SolmrConstants.FLAG_N);
    session.removeAttribute("numParticelleSelezionate");
	 	session.removeAttribute("numUtilizziSelezionati");
 	}
	else
 	{
 	  if(Validator.isNotEmpty(request.getParameter("idDichiarazioneConsistenza")))
 	    filtriParticellareRicercaVO.setIdPianoRiferimento(Long.decode(request.getParameter("idDichiarazioneConsistenza")));
 	}

 	Vector<StoricoParticellaVO> elencoParticelle = null;
 	HashMap<String,Vector<Long>> numParticelleSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numParticelleSelezionate");
 	HashMap<String,Vector<Long>> numUtilizziSelezionati = (HashMap<String,Vector<Long>>)session.getAttribute("numUtilizziSelezionati");
 	if(numParticelleSelezionate == null) 
  {
 		numParticelleSelezionate = new HashMap<String,Vector<Long>>();
 		numUtilizziSelezionati = new HashMap<String,Vector<Long>>();
 	}
 	Vector<Long> elencoPartSelezionate = new Vector<Long>();
 	Vector<Long> elencoUtilizziSelezionati = new Vector<Long>();
 	String paginaChiave = request.getParameter("paginaCorrente");
 	
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
 	
 	// Elimino l'oggetto di AAEP presente in sessione che potrebbe creare problemi
 	session.removeAttribute("common");

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
 
 	// Recupero i valori relativi alle unità produttive
 	Vector<UteVO> elencoUte = new Vector<UteVO>();
 	try 
  {
		String[] orderBy = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE, (String)SolmrConstants.ORDER_BY_UTE_INDIRIZZO};
	 	elencoUte = anagFacadeClient.getListUteByIdAzienda(anagAziendaVO.getIdAzienda(), false, orderBy);
	 	request.setAttribute("elencoUte", elencoUte);
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("idUnitaProduttiva", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
	}
 
 	// Recupero i valori relativi ai comuni su cui insistono le particelle
 	Vector<ComuneVO> elencoComuni = null;
 	try 
  {
		String[] orderBy = {(String)SolmrConstants.ORDER_BY_DESC_COMUNE};
	 	elencoComuni = anagFacadeClient.getListComuniParticelleByIdAzienda(anagAziendaVO.getIdAzienda(), false, orderBy);
	 	request.setAttribute("elencoComuni", elencoComuni);
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("istatComuniConduzioniParticelle", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
 	}
 	
 	// Recupero i valori relativi ai macro uso associati all'azienda
 	TipoMacroUsoVO[] elencoMacroUso = null;
 	try 
  {
 		String[] orderBy = new String[]{SolmrConstants.ORDER_BY_GENERIC_DESCRIZIONE_TIPO_MACRO_USO_ASC, SolmrConstants.ORDER_BY_GENERIC_CODICE_TIPO_MACRO_USO_ASC};
 		elencoMacroUso = anagFacadeClient.getListTipoMacroUsoByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy);
 		request.setAttribute("elencoMacroUso", elencoMacroUso);
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("idMacroUso", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
   	return;
 	}
 
 	// Recupero i valori relativi ai titoli di possesso
 	it.csi.solmr.dto.CodeDescription[] elencoTitoliPossesso = null;
 	try 
  {
		elencoTitoliPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
	 	request.setAttribute("elencoTitoliPossesso", elencoTitoliPossesso);
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("idTitoloPossesso", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
   	return;
 	}
  
  // Recupero i valori relativi alle categorie del documento
  Vector<BaseCodeDescription> elencoTipoDocumento = null;
  try 
  {
    elencoTipoDocumento = gaaFacadeClient.getCategoriaDocumentiTerritorialiAzienda(anagAziendaVO.getIdAzienda().longValue());
    request.setAttribute("elencoTipoDocumento", elencoTipoDocumento);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("idTipoDocumento", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
  
  // Recupero i valori relativi del documento
  Vector<BaseCodeDescription> elencoDocumento = null;
  try 
  {
    elencoDocumento = gaaFacadeClient.getDocumentiTerritorialiAzienda(anagAziendaVO.getIdAzienda().longValue());
    request.setAttribute("elencoDocumento", elencoDocumento);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("idDocumento", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
  
  // Recupero i valori relativi al protocollo del documento
  Vector<BaseCodeDescription> elencoProtocolloDocumento = null;
  try {
    elencoProtocolloDocumento = gaaFacadeClient.getProtocolliDocumentiTerritorialiAzienda(anagAziendaVO.getIdAzienda().longValue());
    request.setAttribute("elencoProtocolloDocumento", elencoProtocolloDocumento);
  }
  catch(SolmrException se) {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("idProtocolloDocumento", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
 
 	// Recupero i valori relativi ai tipi uso suolo(attivi e non) utilizzati dall'azienda in esame
 	
 	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = new Vector<TipoUtilizzoVO>();
 	try 
  {
	 	if((filtriParticellareRicercaVO != null)
	 	  && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento()))
    {
      if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() == 0)
      {
        elencoTipiUsoSuolo = anagFacadeClient.getListUtilizziElencoTerrPianoLavorazioneStor(anagAziendaVO.getIdAzienda());
      }
      else if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() < 0)
      {
        elencoTipiUsoSuolo = anagFacadeClient.getListUtilizziElencoTerrPianoLavorazione(anagAziendaVO.getIdAzienda());
      }   
      else if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() > 0)
      {
        elencoTipiUsoSuolo = anagFacadeClient.getListUtilizziElencoTerrValidazione(filtriParticellareRicercaVO.getIdPianoRiferimento());
      }      
    }
    else
    {
 		  elencoTipiUsoSuolo = anagFacadeClient.getListUtilizziElencoTerrPianoLavorazione(anagAziendaVO.getIdAzienda());
   	}	
	 	request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("idTipoUtilizzo", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
 	}
 	
 	// Recupero i valori relativi al tipo controllo
 	TipoControlloVO[] elencoTipiControllo = null;
 	try 
  {
		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
		elencoTipiControllo = anagFacadeClient.getListTipoControlloByIdGruppoControllo(SolmrConstants.ID_GRUPPO_CONTROLLO_PARTICELLARE, orderBy);
	 	request.setAttribute("elencoTipiControllo", elencoTipiControllo);
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("idControllo", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
   	return;
 	}
 	
 	Vector<TipoAreaVO> elencoTipiArea = new Vector<TipoAreaVO>();
  try 
  {
    if((filtriParticellareRicercaVO != null)
      && Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento()))
    {
      if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() <= 0)
      {
        elencoTipiArea = gaaFacadeClient.getValoriTipoAreaFiltroElenco(null);
      }   
      else if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() > 0)
      {
        elencoTipiArea = gaaFacadeClient.getValoriTipoAreaFiltroElenco(filtriParticellareRicercaVO.getIdPianoRiferimento());
      }      
    }
    else
    {
      elencoTipiArea = gaaFacadeClient.getValoriTipoAreaFiltroElenco(null);
    } 
    request.setAttribute("elencoTipiArea", elencoTipiArea);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("idTipoValoraArea", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
 	
 	
  
  // Ricerco i tipo zona altimetrica
  it.csi.solmr.dto.CodeDescription[] elencoZonaAltimetrica = null;
  try 
  {
    elencoZonaAltimetrica = anagFacadeClient.getListTipoZonaAltimetrica(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
    request.setAttribute("elencoZonaAltimetrica", elencoZonaAltimetrica);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("idZonaAltimetrica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
  
  // Ricerco i tipo zona altimetrica
  it.csi.solmr.dto.CodeDescription[] elencoCasoParticolare = null;
  try 
  {
    elencoCasoParticolare = anagFacadeClient.getListTipoCasoParticolare(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
    request.setAttribute("elencoCasoParticolare", elencoCasoParticolare);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
    errors.add("idCasoParticolare", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
  
  // Ricerca tipo efa
  Vector<TipoEfaVO>  vTipoEfa = null;
  try 
  {
    vTipoEfa = gaaFacadeClient.getElencoTipoEfaForAzienda(anagAziendaVO.getIdAzienda());
    request.setAttribute("vTipoEfa", vTipoEfa);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_EFA);
    errors.add("idTipoEfa", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
  
  
  //Notifiche
  Vector<CodeDescription> vTipologiaNotifica = null;
  try 
  {
    vTipologiaNotifica = anagFacadeClient.getTipiTipologiaNotificaFromEntita(SolmrConstants.COD_TIPO_ENTITA_PARTICELLE);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_TIPOLOGIA_NOTIFICA);
    errors.add("idTipologiaNotifica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
  // Metto il vettore in request
  request.setAttribute("vTipologiaNotifica", vTipologiaNotifica);
  
  
  Vector<TipoCategoriaNotificaVO> vCategoriaNotifica = null;
  try 
  {
    vCategoriaNotifica = anagFacadeClient.
      getTipiCategoriaNotificaFromEntita(SolmrConstants.COD_TIPO_ENTITA_PARTICELLE);
  }
  catch(SolmrException se) 
  {
    ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_NOTIFICA);
    errors.add("idCategoriaNotifica", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
    return;
  }
  // Metto il vettore in request
  request.setAttribute("vCategoriaNotifica", vCategoriaNotifica);
  
  //Notifiche fine
  
  
  
  
  
  
 	
 	// Metto l'oggetto in sessione
 	session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
 	// L'utente ha selezionato il tasto ricerca
	if(request.getParameter("ricerca") != null) 
  {
		
		// Rimetto la pagina di partenza a 1
		paginaCorrente = "1";
		request.setAttribute("paginaCorrente", paginaCorrente);
		
		// Rimuovo il filtro, lo rigenero e setto i parametri
	 	session.removeAttribute("filtriParticellareRicercaVO");
	 	filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
	 	filtriParticellareRicercaVO.setAndValidateParameter(request);
	 	// Lo rimetto in sessione
	 	session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
		
	 	// Rimuovo le particelle precedentemente selezionate
	 	session.removeAttribute("numParticelleSelezionate");
	 	session.removeAttribute("numUtilizziSelezionati");
	 	
    try 
    {
      filtriParticellareRicercaVO.setPaginaCorrente(Integer.parseInt(paginaCorrente));
    } 
    catch (Exception e) 
    {
      filtriParticellareRicercaVO.setPaginaCorrente(1);
    }
    
		// Effettuo la ricerca in relazione ai nuovi parametri di ricerca inseriti
		// dall'utente
		try 
    {
			elencoParticelle = anagFacadeClient.searchListParticelleByParameters(filtriParticellareRicercaVO, anagAziendaVO.getIdAzienda());
		} 
		catch (SolmrException se) 
    {
			String messaggioErrore = (String)AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= terreniParticellareElencoUrl %>" />
			<%
		}
		
		// Se non sono state trovate delle particelle per i criteri di ricerca impostati
		// avviso l'utente con opportuno messaggio di errore
		if(elencoParticelle == null || elencoParticelle.size() == 0) 
    {
			String messaggioErrore = (String) AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= terreniParticellareElencoUrl %>" />
			<%
		}
		// Altrimenti
		else 
    {
			// nel caso siano state trovate e l'utente abbia scelto di selezionare
			// solo le particelle asservite e/o solo quelle conferite
			HashMap<Long,AnagAziendaVO[]> elencoAziende = new HashMap<Long,AnagAziendaVO[]>();
      HashMap<Long,AnagAziendaVO[]> elencoAziendeConferite = new HashMap<Long,AnagAziendaVO[]>();
			if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        //mi ricavo la data della dichiarazione di consistenza!!
        Date dataInserimentoDichiarazione = null;
        if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() > 0)
        {
          ConsistenzaVO consVOTmp = anagFacadeClient.getDichiarazioneConsistenza(
            filtriParticellareRicercaVO.getIdPianoRiferimento());
          dataInserimentoDichiarazione = consVOTmp.getDataInserimentoDichiarazione();
        }
				// Estraggo i dati relativi alle aziende su cui insistono in asservimento le particelle estratte
				// dalla precedente query di ricerca
				Long idStoricoParticella = null;
				for(int i = 0; i < elencoParticelle.size(); i++) 
				{
					StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle.elementAt(i);
					if(idStoricoParticella == null 
					  || idStoricoParticella.compareTo(storicoParticellaVO.getIdStoricoParticella()) != 0) 
					{
						try 
						{
							AnagAziendaVO[] elenco = anagFacadeClient.getListAziendeParticelleAsservite(
							  storicoParticellaVO.getIdStoricoParticella(), 
							  anagAziendaVO.getIdAnagAzienda(), 
							  SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO,
							  dataInserimentoDichiarazione);
							elencoAziende.put(storicoParticellaVO.getIdStoricoParticella(), elenco);
							request.setAttribute("elencoAziende", elencoAziende);
						}
						catch (SolmrException se) 
						{
							String messaggioErrore = AnagErrors.ERRORE_KO_AZIENDE_PARTICELLE_ASSERVITE;
							request.setAttribute("messaggioErrore", messaggioErrore);
							%>
								<jsp:forward page="<%= terreniParticellareElencoUrl %>" />
							<%
						}
					}
					idStoricoParticella = storicoParticellaVO.getIdStoricoParticella();
				}
			}
      if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        //mi ricavo la data della dichiarazione di consistenza!!
        Date dataInserimentoDichiarazione = null;
        if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() > 0)
        {
          ConsistenzaVO consVOTmp = anagFacadeClient.getDichiarazioneConsistenza(
            filtriParticellareRicercaVO.getIdPianoRiferimento());
          dataInserimentoDichiarazione = consVOTmp.getDataInserimentoDichiarazione();
        }
        // Estraggo i dati relativi alle aziende su cui insistono in conferimento le particelle estratte
        // dalla precedente query di ricerca
        Long idStoricoParticella = null;
        for(int i = 0; i < elencoParticelle.size(); i++) 
        {
          StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle.elementAt(i);
          if(idStoricoParticella == null 
            || idStoricoParticella.compareTo(storicoParticellaVO.getIdStoricoParticella()) != 0) 
          {
            try 
            {
              AnagAziendaVO[] elenco = anagFacadeClient.getListAziendeParticelleAsservite(
                storicoParticellaVO.getIdStoricoParticella(), 
                anagAziendaVO.getIdAnagAzienda(), 
                SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO,
                dataInserimentoDichiarazione);
              elencoAziendeConferite.put(storicoParticellaVO.getIdStoricoParticella(), elenco);
              request.setAttribute("elencoAziendeConferite", elencoAziendeConferite);
            }
            catch (SolmrException se) 
            {
              String messaggioErrore = AnagErrors.ERRORE_KO_AZIENDE_PARTICELLE_CONFERITE;
              request.setAttribute("messaggioErrore", messaggioErrore);
              %>
                <jsp:forward page="<%= terreniParticellareElencoUrl %>" />
              <%
            }
          }
          idStoricoParticella = storicoParticellaVO.getIdStoricoParticella();
        }
      }
		}
		request.setAttribute("elencoParticelle", elencoParticelle);
	}
	// L'utente ha selezionato la voce di menù "terreni" e quindi effettuo la ricerca di default
	else 
  {
		// Recupero inoltre tutti gli elementi selezionati in modo da poterli
		// gestire nelle funzionalità multiple di modifica del dato
		String[] elencoIdConduzione = request.getParameterValues("idConduzione");
		String[] elencoIdUtilizzo = request.getParameterValues("idUtilizzo");
		
		// Se l'utente ha scelto di effettuare i controlli di validità delle particelle
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("eseguiControlli")) 
    {
      String parametroCCAV = null;
  		// Richiamo la procedura PL-SQL che si occupa di aggiornare i controlli
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
      
    		anagFacadeClient.controlliParticellarePLSQL(anagAziendaVO.getIdAzienda(), DateUtils.getCurrentYear(),
    		  ruoloUtenza.getIdUtente());
    	}
  		catch(SolmrException se) {
    	    String messaggio = se.getMessage();
      	request.setAttribute("messaggioErrore",messaggio);
    	    request.setAttribute("pageBack", actionUrl);
    	    %>
    	    	<jsp:forward page="<%= erroreViewUrl %>" />
    	    <%
    	    return;
  		}
		}
		// Se l'utente ha selezionato il pulsante vai
		if(request.getParameter("vai") != null 
      || (Validator.isNotEmpty(request.getParameter("operazioneDefault")) 
      && request.getParameter("operazioneDefault").equalsIgnoreCase("operazioneDefault"))) 
    {
			String numPagina = request.getParameter("paginaCorrente");
			paginaChiave = request.getParameter("pagina");
			// Controllo che sia stato indicato il numero di pagina
			if(!Validator.isNotEmpty(numPagina)) 
      {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_OBBLIGO_NUM_PAGINAZIONE_PARTICELLE);
			 	errors.add("paginaCorrente", error);
		    request.setAttribute("errors", errors);
			}
			// Controllo che sia un numero intero e positivo
			else if(!Validator.isNumericInteger(numPagina) || Integer.parseInt(numPagina) <= 0) 
      {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_NUM_PAGINAZIONE_PARTICELLE);
			 	errors.add("paginaCorrente", error);
		    request.setAttribute("errors", errors);
			}
			// Controllo che non sia superiore al rapporto con il numero totale di pagine possibili
			else if(Integer.parseInt(numPagina) > Integer.parseInt(request.getParameter("numeroTotale"))) 
      {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NUM_PAGINAZIONE_PARTICELLE_SUP_NUM_TOT);
			 	errors.add("paginaCorrente", error);
		    request.setAttribute("errors", errors);
			}
			// Se passo i controlli metto il valore in request
			if(errors.size() == 0) 
      {
				request.setAttribute("paginaCorrente", numPagina);
        paginaCorrente=numPagina;
			}
			else 
      {
				request.setAttribute("paginaCorrente", paginaCorrente);
			}
		}
		// L'utente ha cliccato su uno dei numeri della paginazione
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("paginazione")) 
    {
			String numPagina = request.getParameter("pagina");
      paginaCorrente=numPagina;
			request.setAttribute("paginaCorrente", numPagina);
		}
    
    // Ricerca per piano di lavorazione corrente o comprensivo di conduzioni
    // storicizzate
    boolean pianoAttuale=false;
    if (filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() <= 0)
    {
      pianoAttuale=true;
    }
    
    
		// L'utente ha selezionato l'ordinamento per comune decrescente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("comuneDisc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_COMUNE_DESC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_COMUNE_DESC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento per comune ascendente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("comuneAsc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_COMUNE_ASC_PIANO_ATTUALE);
      }
      else 
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_COMUNE_ASC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento per titolo possesso decrescente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("idTitoloPossessoDisc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_ID_TITOLO_POSSESSO_DESC_PIANO_ATTUALE);
      }
      else 
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_ID_TITOLO_POSSESSO_DESC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento per titolo possesso ascendente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("idTitoloPossessoAsc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_ID_TITOLO_POSSESSO_ASC_PIANO_ATTUALE);
      }
      else 
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_ID_TITOLO_POSSESSO_ASC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento per codice macro uso decrescente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("codiceMacroUsoDisc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CODICE_TIPO_MACRO_USO_DESC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_CODICE_TIPO_MACRO_USO_DESC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento per codice macro uso ascendente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("codiceMacroUsoAsc")) 
    { 
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CODICE_TIPO_MACRO_USO_ASC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_CODICE_TIPO_MACRO_USO_ASC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento uso primario decrescente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("descUtilizzoDisc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy((String)SolmrConstants.ORDER_BY_DESC_UTILIZZO_DESC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy((String)SolmrConstants.ORDER_BY_CHAR_DESC_UTILIZZO_DESC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento uso primario ascendente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("descUtilizzoAsc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy((String)SolmrConstants.ORDER_BY_DESC_UTILIZZO_ASC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy((String)SolmrConstants.ORDER_BY_CHAR_DESC_UTILIZZO_ASC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento varietà primaria decrescente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("descVarietaDisc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_VARIETA_DESC_PIANO_ATTUALE);
      }
      else 
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_VARIETA_DESC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento varietà primaria ascendente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("descVarietaAsc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_VARIETA_ASC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_VARIETA_ASC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento uso secondario decrescente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("descUtilizzoSecondarioDisc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_UTILIZZO_SECONDARIO_DESC_PIANO_ATTUALE);
      }
      else 
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_UTILIZZO_SECONDARIO_DESC);
      } 
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento uso secondario ascendente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("descUtilizzoSecondarioAsc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_UTILIZZO_SECONDARIO_ASC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_UTILIZZO_SECONDARIO_ASC);
      }
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento varieta secondaria decrescente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("descVarietaSecondariaDisc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_VARIETA_SECONDARIA_DESC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_VARIETA_SECONDARIA_DESC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento varietà secondaria ascendente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("descVarietaSecondariaAsc")) 
    {
      if (pianoAttuale)
      {
			  filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_DESC_VARIETA_SECONDARIA_ASC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy(SolmrConstants.ORDER_BY_CHAR_VARIETA_SECONDARIA_ASC);
      }   
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento caso particolare decrescente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("idCasoParticolareDisc")) 
    {
      if (pianoAttuale)
      {
	 		  filtriParticellareRicercaVO.setOrderBy((String)SolmrConstants.ORDER_BY_ID_CASO_PARTICOLARE_DESC_PIANO_ATTUALE);
      }
      else
      {
        filtriParticellareRicercaVO.setOrderBy((String)SolmrConstants.ORDER_BY_CHAR_ID_CASO_PARTICOLARE_DESC);
      }  
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// L'utente ha selezionato l'ordinamento caso particolare ascendente
		if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("idCasoParticolareAsc")) 
    {
      if (pianoAttuale)
      {
        filtriParticellareRicercaVO.setOrderBy((String)SolmrConstants.ORDER_BY_ID_CASO_PARTICOLARE_ASC_PIANO_ATTUALE);
      }
      else
      {  
        filtriParticellareRicercaVO.setOrderBy((String)SolmrConstants.ORDER_BY_CHAR_ID_CASO_PARTICOLARE_ASC);
      }
			paginaCorrente="1";
      request.setAttribute("paginaCorrente", "1");
		}
		// Rimetto in sessione l'oggetto aggiornato
		session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
		
		// Gestisco la selezione delle particelle prima di effettuare la ricerca ed 
		// andare alla pagina selezionata: se sono nella prima di default ovviamente
		// non farò nulla
		if(elencoIdConduzione != null && elencoIdConduzione.length > 0) 
    {
			for(int i = 0; i < elencoIdConduzione.length; i++) 
      {
				Long idConduzione = Long.decode((String)elencoIdConduzione[i]);
				Long idUtilizzo = Long.decode((String)elencoIdUtilizzo[i]);
				elencoPartSelezionate.add(idConduzione) ;
				elencoUtilizziSelezionati.add(idUtilizzo);
			}
			numParticelleSelezionate.put(paginaChiave, elencoPartSelezionate);
			session.setAttribute("numParticelleSelezionate", numParticelleSelezionate);
			numUtilizziSelezionati.put(paginaChiave, elencoUtilizziSelezionati);
			session.setAttribute("numUtilizziSelezionati", numUtilizziSelezionati);
		}
		else 
    {
			numParticelleSelezionate.remove(paginaChiave);
			session.setAttribute("numParticelleSelezionate", numParticelleSelezionate);
			numUtilizziSelezionati.remove(paginaChiave);
			session.setAttribute("numUtilizziSelezionati", numUtilizziSelezionati);
		}
    
		try 
    {
      filtriParticellareRicercaVO.setPaginaCorrente(Integer.parseInt(paginaCorrente));
    } 
    catch (Exception e) 
    {
      filtriParticellareRicercaVO.setPaginaCorrente(1);
    }
    
		// Effettuo la ricerca delle particelle in base al filtro di default impostato o a quello recuperato
		// dalla sessione
		try 
    {
			elencoParticelle = anagFacadeClient.searchListParticelleByParameters(filtriParticellareRicercaVO, anagAziendaVO.getIdAzienda());
		} 
		catch (SolmrException se) 
    {
			String messaggioErrore = (String) AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= terreniParticellareElencoUrl %>" />
			<%
		}
		
		// Se non sono state trovate delle particelle per i criteri di ricerca impostati
		// avviso l'utente con opportuno messaggio di errore
		if(elencoParticelle == null || elencoParticelle.size() == 0) 
    {
			String messaggioErrore = (String) AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= terreniParticellareElencoUrl %>" />
			<%
		}
		// Altrimenti
		else 
    {
			// nel caso siano state trovate e l'utente abbia scelto di selezionare
			// solo le particelle asservite e/o quelle conferite
			HashMap<Long,AnagAziendaVO[]> elencoAziende = new HashMap<Long,AnagAziendaVO[]>();
      HashMap<Long,AnagAziendaVO[]> elencoAziendeConferite = new HashMap<Long,AnagAziendaVO[]>();
			if(filtriParticellareRicercaVO.getCheckSoloAsservite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        //mi ricavo la data della dichiarazione di consistenza!!
        Date dataInserimentoDichiarazione = null;
        if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() > 0)
        {
          ConsistenzaVO consVOTmp = anagFacadeClient.getDichiarazioneConsistenza(
            filtriParticellareRicercaVO.getIdPianoRiferimento());
          dataInserimentoDichiarazione = consVOTmp.getDataInserimentoDichiarazione();
        }
				// Estraggo i dati relativi alle aziende su cui insistono in asservimento le particelle estratte
				// dalla precedente query di ricerca
				Long idStoricoParticella = null;
				for(int i = 0; i < elencoParticelle.size(); i++) 
				{
					StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle.elementAt(i);
					if(idStoricoParticella == null 
					  || idStoricoParticella.compareTo(storicoParticellaVO.getIdStoricoParticella()) != 0) 
					{
						try 
						{
							AnagAziendaVO[] elenco = anagFacadeClient.getListAziendeParticelleAsservite(
							  storicoParticellaVO.getIdStoricoParticella(), 
							  anagAziendaVO.getIdAnagAzienda(), 
							  SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO,
							  dataInserimentoDichiarazione);
							elencoAziende.put(storicoParticellaVO.getIdStoricoParticella(), elenco);
						}
						catch (SolmrException se) 
						{
							String messaggioErrore = AnagErrors.ERRORE_KO_AZIENDE_PARTICELLE_ASSERVITE;
							request.setAttribute("messaggioErrore", messaggioErrore);
							%>
								<jsp:forward page="<%= terreniParticellareElencoUrl %>" />
							<%
						}
					}
					idStoricoParticella = storicoParticellaVO.getIdStoricoParticella();
				}
				request.setAttribute("elencoAziende", elencoAziende);
			}
      if(filtriParticellareRicercaVO.getCheckSoloConferite().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        //mi ricavo la data della dichiarazione di consistenza!!
        Date dataInserimentoDichiarazione = null;
        if(filtriParticellareRicercaVO.getIdPianoRiferimento().longValue() > 0)
        {
          ConsistenzaVO consVOTmp = anagFacadeClient.getDichiarazioneConsistenza(
            filtriParticellareRicercaVO.getIdPianoRiferimento());
          dataInserimentoDichiarazione = consVOTmp.getDataInserimentoDichiarazione();
        }
        // Estraggo i dati relativi alle aziende su cui insistono in conferimento le particelle estratte
        // dalla precedente query di ricerca
        Long idStoricoParticella = null;
        for(int i = 0; i < elencoParticelle.size(); i++) 
        {
          StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle.elementAt(i);
          if(idStoricoParticella == null 
            || idStoricoParticella.compareTo(storicoParticellaVO.getIdStoricoParticella()) != 0) 
          {
            try 
            {
              AnagAziendaVO[] elenco = anagFacadeClient.getListAziendeParticelleAsservite(
                storicoParticellaVO.getIdStoricoParticella(), 
                anagAziendaVO.getIdAnagAzienda(), 
                SolmrConstants.ID_TITOLO_POSSESSO_CONFERIMENTO,
                dataInserimentoDichiarazione);
              elencoAziendeConferite.put(storicoParticellaVO.getIdStoricoParticella(), elenco);
            }
            catch (SolmrException se) 
            {
              String messaggioErrore = AnagErrors.ERRORE_KO_AZIENDE_PARTICELLE_CONFERITE;
              request.setAttribute("messaggioErrore", messaggioErrore);
              %>
                <jsp:forward page="<%= terreniParticellareElencoUrl %>" />
              <%
            }
          }
          idStoricoParticella = storicoParticellaVO.getIdStoricoParticella();
        }
        request.setAttribute("elencoAziendeConferite", elencoAziendeConferite);
      }
		}
		request.setAttribute("elencoParticelle", elencoParticelle);
	}
 	
	// Recupero la data ultima esecuzione controlli
 	String dataEsecuzioneControlli = null;
 	try 
  {
		dataEsecuzioneControlli = anagFacadeClient.getMaxDataEsecuzioneControlliConduzioneParticella(anagAziendaVO.getIdAzienda());
	 	if(Validator.isNotEmpty(dataEsecuzioneControlli)) 
    {
	 		filtriParticellareRicercaVO.setDataEsecuzioneControlli(StringUtils.parseDateFieldToEuropeStandard((String)SolmrConstants.get("FULL_DATE_ORACLE_FORMAT"), (String)SolmrConstants.get("FULL_DATE_EUROPE_FORMAT"), dataEsecuzioneControlli));
	 	}
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("dataEsecuzioneControlli", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
   	return;
 	}
	
 	// Recupero la data massima dei controlli dichiarati eseguiti
 	String dataEsecuzioneControlliDichiarati = null;
 	try 
  {
 		dataEsecuzioneControlliDichiarati = anagFacadeClient.getMaxDataEsecuzioneControlliConduzioneDichiarata(anagAziendaVO.getIdAzienda());
 		if(Validator.isNotEmpty(dataEsecuzioneControlliDichiarati)) 
    {
 			filtriParticellareRicercaVO.setDataEsecuzioneControlliDichiarati(StringUtils.parseDateFieldToEuropeStandard((String)SolmrConstants.get("FULL_DATE_ORACLE_FORMAT"), (String)SolmrConstants.get("FULL_DATE_EUROPE_FORMAT"), dataEsecuzioneControlliDichiarati));
 		}
 	}
 	catch(SolmrException se) 
  {
		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_FILTRO_PARTICELLARE);
	 	errors.add("dataEsecuzioneControlliDichiarati", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniParticellareElencoUrl).forward(request, response);
   	return;
 	}
 	
	//  Monitoraggio
    watcher.dumpElapsed("terreniParticellareElencoCtrl.jsp", "controller of terreni search area", "In terreniParticellareElencoCtrl.jsp from the beginning to the end", null);
 	
	%>
		<jsp:forward page="<%= terreniParticellareElencoUrl %>" />

