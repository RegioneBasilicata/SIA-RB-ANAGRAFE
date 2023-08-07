<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.services.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.CodeDescription"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.SuperficieDescription" %>
<%@ page import="it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	
	String iridePageName = "terreniParticellareModificaMultiplaCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
  
  SolmrLogger.debug(this, " - terreniParticellaModificaMultiplaCtrl.jsp - INIZIO PAGINA");
	
	String terreniParticellareModificaMultiplaUrl = "/view/terreniParticellareModificaMultiplaView.jsp";
	String terreniParticellareElencoCtrlUrl = "/ctrl/terreniParticellareElencoCtrl.jsp";
  
  String actionUrl = "../layout/terreniParticellareElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione modifica multipla particella."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	ValidationError error = null;
	ValidationErrors errors = new ValidationErrors();
	String regimeTerreniModificaMultipla = request.getParameter("regimeTerreniModificaMultipla");
	
	// Recupero il valore della pagina dell'elenco da cui ho cliccato il pulsante modifica multipla
	String pagina = request.getParameter("pagina");
	if(Validator.isNotEmpty(pagina)) {
		session.setAttribute("pagina", pagina);
	}
	
	HashMap<String,Vector<Long>> numParticelleSelezionate = (HashMap)session.getAttribute("numParticelleSelezionate");
	HashMap<String,Vector<Long>> numUtilizziSelezionati = (HashMap)session.getAttribute("numUtilizziSelezionati");
	String[] idConduzione = request.getParameterValues("idConduzione");
	String[] elencoIdUtilizzo = request.getParameterValues("idUtilizzo");
	String numPagina = request.getParameter("pagina");
	Vector<Long> elencoIdDaModificare = new Vector<Long>();
	Vector<Long> elencoIdUtilizziDaModificare = new Vector<Long>();
	Long idTipoUtilizzo = null;
	Long idTipoDestinazione = null;
	Long idTipoDettaglioUso = null;
	Long idTipoQualitaUso = null;
	Long idVarieta = null;
	Long idTipoUtilizzoSecondario = null;
	Long idTipoDestinazioneSecondario = null;
	Long idTipoDettaglioUsoSecondario = null;
	Long idTipoQualitaUsoSecondario = null;
	Long idVarietaSecondaria = null;
  
	if(Validator.isNotEmpty(request.getParameter("idTipoUtilizzo"))
	  && Validator.isNotEmpty(regimeTerreniModificaMultipla)) 
  {
		idTipoUtilizzo = Long.decode(request.getParameter("idTipoUtilizzo"));		
		request.setAttribute("idTipoUtilizzo", idTipoUtilizzo);
		
		if(Validator.isNotEmpty(request.getParameter("idTipoDestinazione"))) 
    {
      idTipoDestinazione = Long.decode(request.getParameter("idTipoDestinazione"));
      request.setAttribute("idTipoDestinazione", idTipoDestinazione);
    }
		
		if(Validator.isNotEmpty(request.getParameter("idTipoDettaglioUso"))) 
    {
      idTipoDettaglioUso = Long.decode(request.getParameter("idTipoDettaglioUso"));
      request.setAttribute("idTipoDettaglioUso", idTipoDettaglioUso);
    }
    
    if(Validator.isNotEmpty(request.getParameter("idTipoQualitaUso"))) 
    {
      idTipoQualitaUso = Long.decode(request.getParameter("idTipoQualitaUso"));
      request.setAttribute("idTipoQualitaUso", idTipoQualitaUso);
    }
    
    if(Validator.isNotEmpty(request.getParameter("idVarieta"))) 
    {
      idVarieta = Long.decode(request.getParameter("idVarieta"));
      request.setAttribute("idVarieta", idVarieta);
    }
		
	}
  
	if(Validator.isNotEmpty(request.getParameter("idTipoUtilizzoSecondario"))
	  && Validator.isNotEmpty(regimeTerreniModificaMultipla) ) 
  {
		idTipoUtilizzoSecondario = Long.decode(request.getParameter("idTipoUtilizzoSecondario"));
		request.setAttribute("idTipoUtilizzoSecondario", idTipoUtilizzoSecondario);
		
		if(Validator.isNotEmpty(request.getParameter("idTipoDestinazioneSecondario"))) 
    {
      idTipoDestinazioneSecondario = Long.decode(request.getParameter("idTipoDestinazioneSecondario"));
      request.setAttribute("idTipoDestinazioneSecondario", idTipoDestinazioneSecondario);
    }
		
		if(Validator.isNotEmpty(request.getParameter("idTipoDettaglioUsoSecondario"))) 
    {
      idTipoDettaglioUsoSecondario = Long.decode(request.getParameter("idTipoDettaglioUsoSecondario"));
      request.setAttribute("idTipoDettaglioUsoSecondario", idTipoDettaglioUsoSecondario);
    }
    
    if(Validator.isNotEmpty(request.getParameter("idTipoQualitaUsoSecondario"))) 
    {
      idTipoQualitaUsoSecondario = Long.decode(request.getParameter("idTipoQualitaUsoSecondario"));
      request.setAttribute("idTipoQualitaUsoSecondario", idTipoQualitaUsoSecondario);
    }
    
    if(Validator.isNotEmpty(request.getParameter("idVarietaSecondaria"))) 
    {
      idVarietaSecondaria = Long.decode(request.getParameter("idVarietaSecondaria"));
      request.setAttribute("idVarietaSecondaria", idVarietaSecondaria);
    }
		
	}
	String[] elencoSupUtilizzate = null;
  String[] elencoSupCondotte = null;
	String[] elencoSupUtilizzateSecondarie = null;
	if(session.getAttribute("elencoSupUtilizzate") != null) 
  {
		elencoSupUtilizzate = (String[])session.getAttribute("elencoSupUtilizzate");
		session.removeAttribute("elencoSupUtilizzate");
	}
	else 
  {
		elencoSupUtilizzate = request.getParameterValues("supUtilizzata");
	}  
	if(session.getAttribute("elencoSupUtilizzateSecondarie") != null) 
  {
		elencoSupUtilizzateSecondarie = (String[])session.getAttribute("elencoSupUtilizzateSecondarie");
		session.removeAttribute("elencoSupUtilizzateSecondarie");
	}
	else 
  {
		elencoSupUtilizzateSecondarie = request.getParameterValues("supUtilizzataSecondaria");
	}
	String[] elencoSupAgronomiche = request.getParameterValues("superficieAgronomica");
	
	// Controllo che siano stati selezionati degli elementi dall'elenco
	if((numParticelleSelezionate == null || numParticelleSelezionate.size() == 0) && (idConduzione == null || idConduzione.length == 0)) {
		error = new ValidationError(AnagErrors.ERRORE_NO_ELEMENTI_SELEZIONATI);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" />
		<%
	}
	
	// Gestione della selezione delle particelle
	Vector<Long> elenco = new Vector<Long>();
	Vector<Long> elencoUtilizzi = new Vector<Long>();
	if(numParticelleSelezionate != null && numParticelleSelezionate.size() > 0) 
  {
		numParticelleSelezionate.remove(numPagina);
		if(idConduzione != null && idConduzione.length > 0) 
    {
			for(int i = 0; i < idConduzione.length; i++) 
      {
				elenco.add(Long.decode((String)idConduzione[i]));
				elencoUtilizzi.add(Long.decode((String)elencoIdUtilizzo[i]));
			}
		}
	}
	else 
  {
		if(numParticelleSelezionate == null) 
    {
			numParticelleSelezionate = new HashMap<String,Vector<Long>>();
			numUtilizziSelezionati = new HashMap<String,Vector<Long>>();
		}
		for(int i = 0; i < idConduzione.length; i++) 
    {
			Long idElemento = Long.decode((String)idConduzione[i]);
			elenco.add(idElemento);
			elencoUtilizzi.add(Long.decode((String)elencoIdUtilizzo[i]));
		}
	}
	if(elenco.size() > 0) 
  {
		numParticelleSelezionate.put(numPagina, elenco);
		session.setAttribute("numParticelleSelezionate", numParticelleSelezionate);
		numUtilizziSelezionati.put(numPagina, elencoUtilizzi);
		session.setAttribute("numUtilizziSelezionati", numUtilizziSelezionati);
	}
	
	// Recupero il parametro che mi indica il numero massimo di record selezionabili
	String parametroRTer = null;
	try 
  {
		parametroRTer = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RTER);;
	}
	catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_RTER+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
	
	// Verifico che non siano state selezionate più particelle rispetto a quelle consentite
	Set<String> elencoKeys = numParticelleSelezionate.keySet();
	Iterator<String> iteraKey = elencoKeys.iterator();
	while(iteraKey.hasNext()) 
  {
		Vector<Long> selezioni = (Vector<Long>)numParticelleSelezionate.get((String)iteraKey.next());
		if(selezioni != null && selezioni.size() > 0) 
    {
			for(int a = 0; a < selezioni.size(); a++) 
      {
				elencoIdDaModificare.add((Long)selezioni.elementAt(a));
			}
		}
	}
	if(elencoIdDaModificare.size() > Integer.parseInt(parametroRTer)) 
  {
		error = new ValidationError(AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1+parametroRTer+AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" />
		<%
	}
	
	// Recupero anche gli id_utilizzo_particella selezionati
	Set<String> elencoKeysUtilizzi = numUtilizziSelezionati.keySet();
	Iterator<String> iteraKeyUtilizzi = elencoKeysUtilizzi.iterator();
	while(iteraKeyUtilizzi.hasNext()) 
  {
		Vector<Long> selezioni = (Vector<Long>)numUtilizziSelezionati.get((String)iteraKeyUtilizzi.next());
		if(selezioni != null && selezioni.size() > 0) 
    {
			for(int a = 0; a < selezioni.size(); a++) 
      {
				elencoIdUtilizziDaModificare.add((Long)selezioni.elementAt(a));
			}
		}
	}
	
	// Verifico che tra le particelle selezionate dall'utente non ve ne sia nessuna
	// con data_fine_conduzione valorizzata
	ConduzioneParticellaVO conduzioneParticellaVO = null;
	StoricoParticellaVO storicoParticellaVO = null;
	String[] oldSuperficieAgronomica = new String[elencoIdDaModificare.size()];
	boolean isErrato = false;
	try 
  {
		Long idConduzioneParticella = null;
		for(int i = 0; i < elencoIdDaModificare.size(); i++) 
    {
			idConduzioneParticella = (Long)elencoIdDaModificare.elementAt(i);
			conduzioneParticellaVO = anagFacadeClient.findConduzioneParticellaByPrimaryKey(idConduzioneParticella);
			if(conduzioneParticellaVO.getIdTitoloPossesso()
			  .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
			{
			  oldSuperficieAgronomica[i] = StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta());
			}
			else
			{
			  oldSuperficieAgronomica[i] = StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica());
			}
			if(conduzioneParticellaVO.getDataFineConduzione() != null) 
      {
				isErrato = true;
				break;
			}
		}
		if(isErrato) 
    {
			storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
			String messaggio = "Comune "+storicoParticellaVO.getComuneParticellaVO().getDescom();
			if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
				messaggio += " Sz. " +storicoParticellaVO.getSezione();
			}
			messaggio += " Fgl. "+storicoParticellaVO.getFoglio();
			if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
				messaggio += " Part. " +storicoParticellaVO.getParticella();
			}
			if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
				messaggio += " Sub. " +storicoParticellaVO.getSubalterno();
			}
			error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_MULTIPLA_FOR_CONDUZIONI_STORICIZZATE + messaggio);
			request.setAttribute("error", error);
			%>
				<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" />
			<%
		}
		session.setAttribute("oldSuperficieAgronomica", oldSuperficieAgronomica);
	}
	catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CONDUZIONE_PARTICELLE+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
	
	
	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloPrimario");
  if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0) 
  {
    try 
    {
      elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), null);
      request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_USO_PRIMARIO+".\n"+se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
    }
  }
  else 
  {
    request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
  }
  
  // Destinazione PRIMARIO
  Vector<TipoDestinazioneVO> vTipoDestinazione = null;
  if(Validator.isNotEmpty(idTipoUtilizzo)) 
  {
    try 
    {      
      vTipoDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(idTipoUtilizzo);
      request.setAttribute("vTipoDestinazione", vTipoDestinazione);      
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DEST_USO_PRIMARIA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  
  // Dettaglio Uso PRIMARIO
  Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = null;
  if(Validator.isNotEmpty(idTipoDestinazione)) 
  {
    try 
    {      
      vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoByMatrice(idTipoUtilizzo, idTipoDestinazione);
      request.setAttribute("vTipoDettaglioUso", vTipoDettaglioUso);      
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_USO_PRIMARIO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  
  // Qualita Uso PRIMARIO
  Vector<TipoQualitaUsoVO> vTipoQualitaUso = null;
  if(Validator.isNotEmpty(idTipoDettaglioUso)) 
  {
    try 
    {      
      vTipoQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(idTipoUtilizzo, idTipoDestinazione, idTipoDettaglioUso);
      request.setAttribute("vTipoQualitaUso", vTipoQualitaUso);      
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_QUALITA_USO_PRIMARIO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
	
 	// VARIETA' PRIMARIA
 	Vector<TipoVarietaVO> elencoVarieta = null;
 	if(Validator.isNotEmpty(idTipoQualitaUso)) 
  {
 		try 
    {
			elencoVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(idTipoUtilizzo, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
			request.setAttribute("elencoVarieta", elencoVarieta);      
		}
 		catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_VARIETA_PRIMARIA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
 		}
 	}
 	
 	
 	
 	
 	
 	// USO SECONDARIO
 	// Recupero gli usi del suolo secondari
  Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector)session.getAttribute("elencoTipiUsoSuoloSecondario");
  if(elencoTipiUsoSuoloSecondario == null || elencoTipiUsoSuoloSecondario.size() == 0) 
  {
    try 
    {
      elencoTipiUsoSuoloSecondario = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), SolmrConstants.FLAG_S);
      request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_USO_SECONDARIO+".\n"+se.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
    }
  }
  else 
  {
    request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
  }
 	
 	
 	// Destinazione SECONDARIO
  Vector<TipoDestinazioneVO> vTipoDestinazioneSecondario = null;
  if(Validator.isNotEmpty(idVarietaSecondaria)) 
  {
    try 
    {      
      vTipoDestinazioneSecondario = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(idTipoUtilizzoSecondario);
      request.setAttribute("vTipoDestinazioneSecondario", vTipoDestinazioneSecondario);      
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DEST_USO_SECONDARIO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
 	
 	// Dettaglio Uso secondario
 	Vector<TipoDettaglioUsoVO> vTipoDettaglioUsoSecondario = null;
  if(Validator.isNotEmpty(idTipoDestinazioneSecondario))
  {
    try 
    {      
      vTipoDettaglioUsoSecondario = gaaFacadeClient.getListDettaglioUsoByMatrice(
        idTipoUtilizzoSecondario, idTipoDestinazioneSecondario);
      request.setAttribute("vTipoDettaglioUsoSecondario", vTipoDettaglioUsoSecondario);      
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_USO_SECONDARIO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  
  
  // Qualita Uso Secondario
  Vector<TipoQualitaUsoVO> vTipoQualitaUsoSecondario = null;
  if(idTipoDettaglioUsoSecondario != null) 
  {
    try 
    {      
      vTipoQualitaUsoSecondario = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(idTipoUtilizzoSecondario,
        idTipoDestinazioneSecondario, idTipoDettaglioUsoSecondario);
      request.setAttribute("vTipoQualitaUsoSecondario", vTipoQualitaUsoSecondario);      
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_QUALITA_USO_SECONDARIO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  
  // VARIETA SECONDARIA
  Vector<TipoVarietaVO> elencoVarietaSecondaria = null;
  if(Validator.isNotEmpty(idTipoUtilizzoSecondario)) 
  {
    try 
    {
      elencoVarietaSecondaria = gaaFacadeClient.getElencoTipoVarietaByMatrice(idTipoUtilizzoSecondario, 
        idTipoDestinazioneSecondario, idTipoDettaglioUsoSecondario, idTipoQualitaUsoSecondario);
      request.setAttribute("elencoVarietaSecondaria", elencoVarietaSecondaria);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_VARIETA_SECONDARIA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  
  
 	// TITOLO DI POSSESSO
 	it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = null;
 	try 
  {
		elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
		request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
 	}
 	catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_TITOLO_POSSESSO+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
 	}
 	// UNITA' PRODUTTIVE
 	Vector<UteVO> elencoUte = new Vector<UteVO>();
 	try 
  {
		String[] orderBy = {SolmrConstants.ORDER_BY_DESC_COMUNE, SolmrConstants.ORDER_BY_UTE_INDIRIZZO};
	 	elencoUte = anagFacadeClient.getListUteByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy);
	 	request.setAttribute("elencoUte", elencoUte);
 	}
 	catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_UNITA_PRODUTTIVA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
 	// DOCUMENTI
 	DocumentoVO[] elencoDocumenti = null;
 	try 
  {
		String[] orderBy = {SolmrConstants.ORDER_BY_TIPO_DOCUMENTO_DESCRIPTION, SolmrConstants.ORDER_BY_DATA_PROTOCOLLO};
		elencoDocumenti = anagFacadeClient.getListDocumentiByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy, SolmrConstants.ID_TIPOLOGIA_DOCUMENTO_TERRITORIALE);
	 	request.setAttribute("elencoDocumenti", elencoDocumenti);
 	}
 	catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DOCUMENTI_AZIENDA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
  
  //Dichiarazione uso agronomico
  CodeDescription[] elencoTipoDichiarazioneUsoAgronomico = new CodeDescription[2];
  elencoTipoDichiarazioneUsoAgronomico[0] = new CodeDescription();
  elencoTipoDichiarazioneUsoAgronomico[0].setDescription("di tutta la superficie spandibile");
  elencoTipoDichiarazioneUsoAgronomico[0].setCodeFlag("1");
  elencoTipoDichiarazioneUsoAgronomico[1] = new CodeDescription();
  elencoTipoDichiarazioneUsoAgronomico[1].setDescription("azzera superficie");
  elencoTipoDichiarazioneUsoAgronomico[1].setCodeFlag("2");
  request.setAttribute("elencoTipoDichiarazioneUsoAgronomico", elencoTipoDichiarazioneUsoAgronomico);
  
  //IRRIGABILITA
  // Ricerco i tipi irrigazione
  TipoIrrigazioneVO[] elencoIrrigazione = null;
  try 
  {
    elencoIrrigazione = anagFacadeClient.getListTipoIrrigazione(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION, true);
    request.setAttribute("elencoIrrigazione",elencoIrrigazione);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_TIPO_IRRIGAZIONE+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
 	
 	// Recupero i dati chiave delle particelle selezionate
 	StoricoParticellaVO[] elencoStoricoParticellaVO = null;
 	//Usati per tenere un unica conduzione relativa a più utilizzi per il confornto nell'eleggibiltà gis
  //Usata per prendere gli utilizzi primari inseriti a video legati alla stessa conduzione
 	Hashtable<Long,Vector<String>> hashSupVideo = new Hashtable<Long,Vector<String>>();
  //Usata per prendere gli utilizzi primari inseriti a video legati alla stessa particella
  Hashtable<Long,Vector<String>> hashSupVideoParticella = new Hashtable<Long,Vector<String>>();
  //superficie spandibile relativa alla conduzione
  HashMap<Long,BigDecimal> hSupSpandibile = new HashMap<Long,BigDecimal>();
  
  BigDecimal[] oldSupUtilizzoEleggibile = new BigDecimal[elencoIdDaModificare.size()];
  BigDecimal[] oldSupUtilizzoEleggibileNetta = new BigDecimal[elencoIdDaModificare.size()];
  
  
  //Variabile per allinea percentuale posssesso
  boolean almenoUnaProvvisoria = false;
  boolean almenoUnaSupCatGrafZero = false;
  Vector<Long> vIdConduzione =  new Vector<Long>();
  Vector<Long> vIdUtilizzo = new Vector<Long>();
  //Usata per confrontare utilizzi con percentuale
  HashMap<Long,BigDecimal> hSupCatGraf = new HashMap<Long,BigDecimal>();
  
  Vector<Long> vIdParticella = null;
  
   
 	try 
  {
 		Vector<StoricoParticellaVO> elencoParticelle = new Vector<StoricoParticellaVO>();
    Vector<String> elencoSupUtilPartDB = null;
    for(int i = 0; i < elencoIdDaModificare.size(); i++) 
    {
 			Long idConduzioneParticella = (Long)elencoIdDaModificare.elementAt(i);
 			Long idUtilizzoParticella = (Long)elencoIdUtilizziDaModificare.elementAt(i);
 			StoricoParticellaVO storicoParticellaElencoVO = anagFacadeClient
         .getDettaglioParticellaByIdConduzioneAndIdUtilizzo(
         filtriParticellareRicercaVO, idConduzioneParticella, idUtilizzoParticella, anagAziendaVO.getIdAzienda());
       
      //usato nell'allinea percentuale Possesso / utilizzo  
      if(!almenoUnaProvvisoria)
      {
        if(Validator.isEmpty(storicoParticellaElencoVO.getParticella()))
        {
          almenoUnaProvvisoria = true; 
        }
      }
      if(!almenoUnaSupCatGrafZero)
      {
        if((Validator.validateDouble(storicoParticellaElencoVO.getSupCatastale(), SolmrConstants.FORMAT_SUP_CATASTALE) == null)
         && (Validator.validateDouble(storicoParticellaElencoVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_CATASTALE) == null)) 
        {
          almenoUnaSupCatGrafZero = true; 
        }
      }
      //*******************************************
      
       
      if(vIdParticella == null)
      {
        vIdParticella = new Vector<Long>();
      }
      if(!vIdParticella.contains(storicoParticellaElencoVO.getIdParticella()))
      {
        vIdParticella.add(storicoParticellaElencoVO.getIdParticella());
        
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getFoglioVO())
          && Validator.isNotEmpty(storicoParticellaElencoVO.getFoglioVO().getFlagStabilizzazione())  
          && (storicoParticellaElencoVO.getFoglioVO().getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
          && Validator.validateDouble(storicoParticellaElencoVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
          && Double.parseDouble(storicoParticellaElencoVO.getSuperficieGrafica().replace(',', '.')) > 0)
        {
          hSupCatGraf.put(storicoParticellaElencoVO.getIdParticella(), new BigDecimal(
               storicoParticellaElencoVO.getSuperficieGrafica().replace(',','.')));         
        }
        else
        {      
          if(Validator.isNotEmpty(storicoParticellaElencoVO.getSupCatastale())
            && (new BigDecimal(storicoParticellaElencoVO.getSupCatastale().replace(',','.')).compareTo(new BigDecimal(0)) > 0))
          {
            hSupCatGraf.put(storicoParticellaElencoVO.getIdParticella(), new BigDecimal(
               storicoParticellaElencoVO.getSupCatastale().replace(',','.')));
          }
          else if(Validator.isNotEmpty(storicoParticellaElencoVO.getSuperficieGrafica()))
          {
            hSupCatGraf.put(storicoParticellaElencoVO.getIdParticella(), new BigDecimal(
              storicoParticellaElencoVO.getSuperficieGrafica().replace(',','.')));
          }
        }
      }
      
      
      if(!vIdConduzione.contains(idConduzioneParticella))
      {
        vIdConduzione.add(idConduzioneParticella);
      }
      
      if(!vIdUtilizzo.contains(idUtilizzoParticella))
      {
        vIdUtilizzo.add(idUtilizzoParticella);
      }
      
       
      if(elencoSupAgronomiche != null && elencoSupAgronomiche.length > 0) 
      {
 				storicoParticellaElencoVO.getElencoConduzioni()[0].setSuperficieAgronomica(elencoSupAgronomiche[i]);
 			}
 			else 
 			{
 				storicoParticellaElencoVO.getElencoConduzioni()[0].setSuperficieAgronomica(StringUtils.parseSuperficieField(storicoParticellaElencoVO.getElencoConduzioni()[0].getSuperficieAgronomica()));
 			}
      UtilizzoParticellaVO utilizzoVO = storicoParticellaElencoVO.getElencoConduzioni()[0].getElencoUtilizzi()[0];
 			
 			//la prima volta nn entra poichè nn trova nulla!!!
 			if(elencoSupUtilizzate != null && elencoSupUtilizzate.length > 0) 
      {
 				utilizzoVO.setSuperficieUtilizzata(elencoSupUtilizzate[i]);
 				utilizzoVO.setSupUtilizzataSecondaria(elencoSupUtilizzateSecondarie[i]);
 			}
       
       
       
      //Aggiungo campi supNetta,SupUtilizzoEleggibile,supUtilizzoEleggibileNetta
      //combo utilizzi valorizzata (deve esserlo anche idVarieta) 
      if(Validator.isNotEmpty(idTipoUtilizzo)
        && (idTipoUtilizzo.compareTo(new Long(-1)) !=0)
        && Validator.isNotEmpty(idVarieta)
        && Validator.isNotEmpty(regimeTerreniModificaMultipla))
      {
        utilizzoVO.setIdUtilizzo(idTipoUtilizzo);
        utilizzoVO.setIdVarieta(idVarieta);
        /*TipoVarietaVO tipoVarietaVOSel = anagFacadeClient.findTipoVarietaByPrimaryKey(new Long(idVarieta));
        if(Validator.isNotEmpty(tipoVarietaVOSel.getIdTipoPeriodoSemina()))
          utilizzoVO.setIdTipoPeriodoSemina(tipoVarietaVOSel.getIdTipoPeriodoSemina());
        else
          utilizzoVO.setIdTipoPeriodoSemina(null);*/
        utilizzoVO.setIdTipoDettaglioUso(idTipoDettaglioUso);
        utilizzoVO.setIdUtilizzoSecondario(idTipoUtilizzoSecondario);
        utilizzoVO.setIdVarietaSecondaria(idVarietaSecondaria);
        /*if(Validator.isNotEmpty(idVarietaSecondaria))
        {
          TipoVarietaVO tipoVarietaVOSecSel = anagFacadeClient
            .findTipoVarietaByPrimaryKey(new Long(idVarietaSecondaria));
          if(Validator.isNotEmpty(tipoVarietaVOSecSel.getIdTipoPeriodoSemina()))
	          utilizzoVO.setIdTipoPeriodoSeminaSecondario(tipoVarietaVOSecSel.getIdTipoPeriodoSemina());
	        else
	          utilizzoVO.setIdTipoPeriodoSeminaSecondario(null);
        }*/
        utilizzoVO.setIdTipoDettaglioUsoSecondario(idTipoDettaglioUsoSecondario);
        
        CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient
          .getCatalogoMatriceFromMatrice(idTipoUtilizzo, idVarieta, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);     
      
        //Cambiato l'uso del suolo primario, prendo la prima varietà...
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticellaCertificataVO()))
        {
           
          utilizzoVO.setSupEleggibile(anagFacadeClient.getSupEleggPlSqlTotale(
             storicoParticellaElencoVO.getParticellaCertificataVO().getIdParticellaCertificata(), catalogoMatriceVO.getIdCatalogoMatrice()));
          utilizzoVO.setSupEleggibileNetta(anagFacadeClient.getSupEleggNettaPlSqlTotale(
             storicoParticellaElencoVO.getParticellaCertificataVO().getIdParticellaCertificata(), catalogoMatriceVO.getIdCatalogoMatrice()));
        }
         
         
        //TipoUtilizzoVO tipoUtilizzoEleggibilitaVO = anagFacadeClient.findTipoUtilizzoByPrimaryKey(idTipoUtilizzo);
        BigDecimal  supNetta = catalogoMatriceVO.getCoefficienteRiduzione();
        if(Validator.isEmpty(utilizzoVO.getSuperficieUtilizzata()) 
          || (Validator.validateDouble(utilizzoVO.getSuperficieUtilizzata(), 999999.9999) == null))
        {
          utilizzoVO.setSupNetta(new BigDecimal(0));
        }
        else
        {
          supNetta = supNetta.multiply(new BigDecimal(
             utilizzoVO.getSuperficieUtilizzata().replace(',','.')));
          utilizzoVO.setSupNetta(supNetta);
        }
      }
      else
      {
        if(Validator.isNotEmpty(storicoParticellaElencoVO.getParticellaCertificataVO())
          && Validator.isNotEmpty(utilizzoVO.getTipoVarietaVO())
          && Validator.isNotEmpty(utilizzoVO.getTipoVarietaVO().getIdVarieta()))
        {
          utilizzoVO.setSupEleggibile(anagFacadeClient.getSupEleggPlSqlTotale(
            storicoParticellaElencoVO.getParticellaCertificataVO().getIdParticellaCertificata(), utilizzoVO.getIdCatalogoMatrice()));
          utilizzoVO.setSupEleggibileNetta(anagFacadeClient.getSupEleggNettaPlSqlTotale(
            storicoParticellaElencoVO.getParticellaCertificataVO().getIdParticellaCertificata(), utilizzoVO.getIdCatalogoMatrice()));
          oldSupUtilizzoEleggibile[i] = utilizzoVO.getSupEleggibile();  
          oldSupUtilizzoEleggibileNetta[i] = utilizzoVO.getSupEleggibileNetta(); 
        }
        if(Validator.isNotEmpty(utilizzoVO.getTipoUtilizzoVO()))
        {
          BigDecimal supNetta = utilizzoVO.getTipoUtilizzoVO().getCoefficienteRiduzione();
          if(Validator.isEmpty(utilizzoVO.getSuperficieUtilizzata()) 
            || (Validator.validateDouble(utilizzoVO.getSuperficieUtilizzata(), 999999.9999) == null))
          {
            utilizzoVO.setSupNetta(new BigDecimal(0));
          }
          else
          { 
            supNetta = supNetta.multiply(new BigDecimal(
              utilizzoVO.getSuperficieUtilizzata().replace(',','.')));
            utilizzoVO.setSupNetta(supNetta);
          }        
        }       
      }
       
       
       
       
      elencoSupUtilPartDB = (Vector<String>)hashSupVideoParticella.get(storicoParticellaElencoVO.getIdParticella());
      if(elencoSupUtilPartDB == null) 
      {
        elencoSupUtilPartDB = new Vector<String>();
      }
      elencoSupUtilPartDB.add(StringUtils.parseSuperficieField(storicoParticellaElencoVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].getSuperficieUtilizzata()));
      hashSupVideoParticella.put(storicoParticellaElencoVO.getIdParticella(), elencoSupUtilPartDB);
       
       
       
       
       
       
       
      //Aggiunta supSpandibile
      Long idParticella = storicoParticellaElencoVO.getIdParticella();
      BigDecimal supSpandibile = new BigDecimal(0);
      //La sup spandibile della particella con una conduzione/utilizzo dell'azienda corrente è già presente, 
      //mi trovo su un record con diverso utilizzo ma identica conduzione, 
      //quindi devo prendere la sup già calcolata per la medesima conduzione
      if(hSupSpandibile.get(idConduzioneParticella) != null)
      {
        supSpandibile = (BigDecimal)hSupSpandibile.get(idConduzioneParticella);
      }
      //prima volta passo da qui!!!
      else
      {
        BigDecimal supUtilizzoAgronomicoConduzione = anagFacadeClient
           .getSumSuperficieUtilizzoUsoAgronomico(idConduzioneParticella.longValue());
           
        BigDecimal supUtilizzoAgronomicoParticella = anagFacadeClient
           .getSumSuperficieUtilizzoUsoAgronomicoParticella(idParticella.longValue(), anagAziendaVO.getIdAzienda().longValue());
         
         
        //se questi valori sono null significa che non posso
        //valorizzare nessuna superifice agronomica per la conduzione corrente
        if((supUtilizzoAgronomicoParticella !=null)
          && (supUtilizzoAgronomicoConduzione != null))
        {
          supSpandibile = supUtilizzoAgronomicoParticella;      
           
          //sottraggo la sup spandibile di altre aziende (asservimento e agronomica)
          BigDecimal sumSupPartDichTmp   = anagFacadeClient
               .getSumSuperficieFromParticellaAndLastDichCons(
               storicoParticellaElencoVO.getIdParticella().longValue(),anagAziendaVO.getIdAzienda(), true);
          if(sumSupPartDichTmp !=null)
          {
            supSpandibile = supSpandibile.subtract(sumSupPartDichTmp);
          }
          
          //sottraggo la sup spandibile(asservimento/agronomica) di altre conduzioni dell'azienda corrente
          supSpandibile = supSpandibile.subtract(anagFacadeClient
             .getSumSuperficieAgronomicaAltreconduzioni(idParticella, idConduzioneParticella, anagAziendaVO.getIdAzienda().longValue()));
           
          //Se la supspandibile supera la superficie agronomica della conduzione
          //vuol dire che ho superficie di altre conduzioni quindi la max metto
          //la superficie agronomica possibile della conduzione corrente
          if(supSpandibile.compareTo(supUtilizzoAgronomicoConduzione) > 0)
          {
            supSpandibile = supUtilizzoAgronomicoConduzione;
          }  
           
        }
           
        if(supSpandibile.compareTo(new BigDecimal(0)) <= 0)
        {
          supSpandibile = new BigDecimal(0);
        }
           
        hSupSpandibile.put(idConduzioneParticella, supSpandibile);
      }
       
       
       
       
      storicoParticellaElencoVO.setSupSpandibile(supSpandibile.toString());        
 			elencoParticelle.add(storicoParticellaElencoVO);
 		}
     
     //metto i valori di defult (quelli di quando entro la prima volta)
     if(Validator.isEmpty(idTipoUtilizzo)
       || (Validator.isNotEmpty(idTipoUtilizzo) && (idTipoUtilizzo.compareTo(new Long(-1))) == 0))
     {
       session.setAttribute("oldSupUtilizzoEleggibile", oldSupUtilizzoEleggibile);  
       session.setAttribute("oldSupUtilizzoEleggibileNetta", oldSupUtilizzoEleggibileNetta); 
     }
     
     
     
     //Aggiungo l'eleggibilità fittizia
     HashMap<Long,Vector<SuperficieDescription>> hSupElegFit = null;
     if(Validator.isNotEmpty(vIdParticella))
     {
       hSupElegFit = anagFacadeClient.getEleggibilitaTooltipByIdParticella(vIdParticella);
     }
     
     
     for(int i=0;i<elencoParticelle.size();i++)
     {
       StoricoParticellaVO storicoParticellaTmpVO = elencoParticelle.get(i);
       if(Validator.isNotEmpty(hSupElegFit) 
         && (hSupElegFit.get(storicoParticellaTmpVO.getIdParticella()) != null))
       {
         storicoParticellaTmpVO.setvSupElegFit(hSupElegFit.get(storicoParticellaTmpVO.getIdParticella()));      
       }
     }          
     
 		if(elencoParticelle.size() > 0) {
 			elencoStoricoParticellaVO = (StoricoParticellaVO[])elencoParticelle.toArray(new StoricoParticellaVO[elencoParticelle.size()]);
 		}
 		request.setAttribute("elencoStoricoParticellaVO", elencoStoricoParticellaVO);
 	}
 	catch(SolmrException se) 
   {
     SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
     String messaggio = errMsg+": "+AnagErrors.ERRORE_SEARCH_PARTICELLE_FOR_MODIFICA+".\n"+se.toString();
     request.setAttribute("messaggioErrore",messaggio);
     request.setAttribute("pageBack", actionUrl);
     %>
       <jsp:forward page="<%= erroreViewUrl %>" />
     <%
     return;
 	}
 	//}
  
  
  
  
  //Usato per inserite gli idUtilizzi relativi alla particella presenti a video 
  HashMap<Long,Vector<String>> hIdUtilizzi = new HashMap<Long,Vector<String>>();
  HashMap<Long,Vector<String>> hIdConduzioni = new HashMap<Long,Vector<String>>();
  //mi ricavo gli idUtilizzi e gli idConduzione presenti a video relativi alla particella
  for(int i=0;i<elencoStoricoParticellaVO.length;i++)
  {
    storicoParticellaVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];
    ConduzioneParticellaVO conduzioneVO = storicoParticellaVO.getElencoConduzioni()[0];
    if(hIdConduzioni.get(storicoParticellaVO.getIdParticella()) != null)
    {
      Vector<String> vIdConduzioni = hIdConduzioni.get(storicoParticellaVO.getIdParticella());
      if(!vIdConduzioni.contains(conduzioneVO.getIdConduzioneParticella()))
      {
        vIdConduzioni.add(conduzioneVO.getIdConduzioneParticella().toString());
        hIdConduzioni.put(storicoParticellaVO.getIdParticella(),vIdConduzioni);
      }
    }
    else
    {
      Vector<String> vIdConduzioni = new Vector<String>();
      vIdConduzioni.add(conduzioneVO.getIdConduzioneParticella().toString());
      hIdConduzioni.put(storicoParticellaVO.getIdParticella(),vIdConduzioni);
    }
    
    
    UtilizzoParticellaVO[] utilizzo = storicoParticellaVO.getElencoConduzioni()[0].getElencoUtilizzi();
    if(utilizzo != null && utilizzo.length > 0) 
    {
      for(int a = 0; a < utilizzo.length; a++) 
      {
        UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO)utilizzo[a];
        if(hIdUtilizzi.get(storicoParticellaVO.getIdParticella()) != null)
        {
          Vector<String> vIdUtilizzoStr = hIdUtilizzi.get(storicoParticellaVO.getIdParticella());
          vIdUtilizzoStr.add(utilizzoParticellaVO.getIdUtilizzoParticella().toString());
          hIdUtilizzi.put(storicoParticellaVO.getIdParticella(),vIdUtilizzoStr);
        }
        else
        {
          Vector<String> vIdUtilizzoStr = new Vector<String>();
          vIdUtilizzoStr.add(utilizzoParticellaVO.getIdUtilizzoParticella().toString());
          hIdUtilizzi.put(storicoParticellaVO.getIdParticella(),vIdUtilizzoStr);
        }
      }
    }
  
  }
  
  
  
  
 	
 	// L'utente ha selezionato il tasto conferma
 	if(request.getParameter("conferma") != null) 
  {
 		// Recupero l'operazione selezionata dall'utente
 		String operazione = request.getParameter("funzione");
 		request.setAttribute("operazione", operazione);
 		
		// Recupero le conduzioni da modificare
		Vector<Long> oldConduzioni = new Vector<Long>();
		for(int i = 0; i < elencoIdDaModificare.size(); i++) {
			Long idConduzioneParticella = (Long)elencoIdDaModificare.elementAt(i);
			if(!oldConduzioni.contains(idConduzioneParticella)) {
				oldConduzioni.add(idConduzioneParticella);
			}
		}
 		
 		// L'utente ha selezionato l'operazione "associa uso"
 		if(operazione.equalsIgnoreCase("Associa")) 
    {
 			Vector<String> errori = new Vector<String>();
 			Vector<String> erroriSupSecondaria = new Vector<String>();
 			CatalogoMatriceVO catalogoMatriceVO = null;
 			// Controllo che siano stati inseriti l'uso,la varietà e la superficie primari
 			if(!Validator.isNotEmpty(idTipoUtilizzo)) 
      {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 				errors.add("idTipoUtilizzo", error);
 			}
 			else
 			{
 			  catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(idTipoUtilizzo, idVarieta, idTipoDestinazione, idTipoDettaglioUso, idTipoQualitaUso);
 			  CatalogoMatriceSeminaVO catalogoMatriceSeminaVO = null;
 			  CatalogoMatriceSeminaVO catalogoMatriceSeminaVOSecondario = null;
 			  Vector<Long> vIdPraticaMantenimento = null;
 			  String parametroDataSwapSemina = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DATA_SWAP_SEMINA);
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("dd/MM/yyyy");
        Date dataConfronto = sdf.parse(parametroDataSwapSemina+"/"+DateUtils.getCurrentYear());
 			  
 			  if(Validator.isNotEmpty(catalogoMatriceVO))
 			  {
 			    catalogoMatriceSeminaVO = gaaFacadeClient.getCatalogoMatriceSeminaDefault(catalogoMatriceVO.getIdCatalogoMatrice());
 			    vIdPraticaMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatriceVO.getIdCatalogoMatrice(), "S");
 			  }
 			  
 			  
 			  
 			  
 			  
 			  CatalogoMatriceVO catalogoMatriceVOSecondario = null;
 			  if(Validator.isNotEmpty(idTipoUtilizzoSecondario))
        {
          catalogoMatriceVOSecondario = gaaFacadeClient.getCatalogoMatriceFromMatrice(idTipoUtilizzoSecondario, idVarietaSecondaria, idTipoDestinazioneSecondario, idTipoDettaglioUsoSecondario, idTipoQualitaUsoSecondario);
          if(Validator.isNotEmpty(catalogoMatriceVOSecondario))
          {
            catalogoMatriceSeminaVOSecondario = gaaFacadeClient.getCatalogoMatriceSeminaDefault(catalogoMatriceVOSecondario.getIdCatalogoMatrice());
          }
        }
        
        
 			  
 			  
 			  
 			  //Carico valori utilizzo di default
	      for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
	      {
	        StoricoParticellaVO particellaVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];
	        UtilizzoParticellaVO utilizzoVO = particellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0];
	        utilizzoVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());  
	        if(catalogoMatriceSeminaVO != null)
	        {
	          utilizzoVO.setIdTipoPeriodoSemina(catalogoMatriceSeminaVO.getIdTipoPeriodoSemina());
	          Date dataInizioDestinazione = null;
	          Date dataFineDestinazione = null;
	          if(dataConfronto.after(new Date()))
	          {
	            int anno = DateUtils.getCurrentYear().intValue();
	            anno = anno + catalogoMatriceSeminaVO.getAnnoDecodificaPreData();
	            dataInizioDestinazione = sdf.parse(catalogoMatriceSeminaVO.getInizioDestinazioneDefault()+"/"+anno);
	            dataFineDestinazione = sdf.parse(catalogoMatriceSeminaVO.getFineDestinazioneDefault()+"/"+anno);
	            
	          }
	          else
	          {
	            int anno = DateUtils.getCurrentYear().intValue();
              anno = anno + catalogoMatriceSeminaVO.getAnnoDecodificaPostData();
              dataInizioDestinazione = sdf.parse(catalogoMatriceSeminaVO.getInizioDestinazioneDefault()+"/"+anno);
	            dataFineDestinazione = sdf.parse(catalogoMatriceSeminaVO.getFineDestinazioneDefault()+"/"+anno);
	          }
	          
	          if(Validator.isNotEmpty(dataInizioDestinazione) && Validator.isNotEmpty(dataFineDestinazione))
	          {
	            if(dataInizioDestinazione.after(dataFineDestinazione))
	            {
	              GregorianCalendar gc = new GregorianCalendar();
	              gc.setTime(dataFineDestinazione);
	              gc.roll(Calendar.YEAR, true);
	              dataFineDestinazione = gc.getTime();
	            }
	          }
	          
	          utilizzoVO.setDataInizioDestinazione(dataInizioDestinazione);
	          utilizzoVO.setDataFineDestinazione(dataFineDestinazione);
	        }
	        utilizzoVO.setIdSemina(new Long(1));
	        if(vIdPraticaMantenimento != null)
	          utilizzoVO.setIdPraticaMantenimento(vIdPraticaMantenimento.get(0));
	          
	        
	        
	        if(catalogoMatriceVOSecondario != null)
	          utilizzoVO.setIdCatalogoMatriceSecondario(catalogoMatriceVOSecondario.getIdCatalogoMatrice());
	        else
	          utilizzoVO.setIdCatalogoMatriceSecondario(null); 
	          
	        if(catalogoMatriceSeminaVOSecondario != null)
          {
            utilizzoVO.setIdTipoPeriodoSeminaSecondario(catalogoMatriceSeminaVOSecondario.getIdTipoPeriodoSemina());
            Date dataInizioDestinazione = null;
            Date dataFineDestinazione = null;
            if(dataConfronto.after(new Date()))
            {
              int anno = DateUtils.getCurrentYear().intValue();
              anno = anno + catalogoMatriceSeminaVOSecondario.getAnnoDecodificaPreData();
              dataInizioDestinazione = sdf.parse(catalogoMatriceSeminaVOSecondario.getInizioDestinazioneDefault()+"/"+anno);
              dataFineDestinazione = sdf.parse(catalogoMatriceSeminaVOSecondario.getFineDestinazioneDefault()+"/"+anno);
              
            }
            else
            {
              int anno = DateUtils.getCurrentYear().intValue();
              anno = anno + catalogoMatriceSeminaVOSecondario.getAnnoDecodificaPostData();
              dataInizioDestinazione = sdf.parse(catalogoMatriceSeminaVOSecondario.getInizioDestinazioneDefault()+"/"+anno);
              dataFineDestinazione = sdf.parse(catalogoMatriceSeminaVOSecondario.getFineDestinazioneDefault()+"/"+anno);
            }
            
            if(Validator.isNotEmpty(dataInizioDestinazione) && Validator.isNotEmpty(dataFineDestinazione))
	          {
	            if(dataInizioDestinazione.after(dataFineDestinazione))
	            {
	              GregorianCalendar gc = new GregorianCalendar();
	              gc.setTime(dataFineDestinazione);
	              gc.roll(Calendar.YEAR, true);
	              dataFineDestinazione = gc.getTime();
	            }
	          }
            
            utilizzoVO.setDataInizioDestinazioneSec(dataInizioDestinazione);
            utilizzoVO.setDataFineDestinazioneSec(dataFineDestinazione);
            
            utilizzoVO.setIdSeminaSecondario(new Long(1));
          
          }
          
          
	        
	      }
 			  
 			  
 			}
 			
 			/*if(Validator.isNotEmpty(vTipoDettaglioUso)
 			  &&  (vTipoDettaglioUso.size() > 0))
 			{ 			
 			  if(Validator.isEmpty(request.getParameter("idTipoDettaglioUso")))
 			  {
 			    error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
          errors.add("idTipoDettaglioUso", error);
 			  }
 			}
 			
 			if(Validator.isNotEmpty(vTipoDettaglioUsoSecondario)
        &&  (vTipoDettaglioUsoSecondario.size() > 0))
      {       
        if(Validator.isEmpty(request.getParameter("idTipoDettaglioUsoSecondario")))
        {
          error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
          errors.add("idTipoDettaglioUsoSecondario", error);
        }
      }*/
      
      
      
      
      
      
      
      
      
 			// Se ci sono degli errori li visualizzo
 			if(errors.size() > 0) 
      {
 				request.setAttribute("errors", errors);
 				%>
 					<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
 				<%
 			}
 			// Altrimenti effettuo i controlli di validità e coerenza delle superfici
 			// delle particelle
 			else 
      {
 				Vector<String> elencoSupVideo = null;
 				for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
        {
 					StoricoParticellaVO particellaVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];
 					if(!Validator.isNotEmpty(elencoSupUtilizzate[i])) 
          {
 						errori.add(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 					}
 					else 
          {
 						if(Validator.validateDouble((String)elencoSupUtilizzate[i], 999999.9999) == null) 
            {
 							errori.add(AnagErrors.ERRORE_CAMPO_ERRATO);
 						}
 						else 
            {
 							if(Double.parseDouble(Validator.validateDouble((String)elencoSupUtilizzate[i], 999999.9999)) <= 0) 
              {
 								errori.add(AnagErrors.ERRORE_CAMPO_ERRATO);
 							}
 							else 
              {
 								errori.add(null);
 								elencoSupVideo = (Vector<String>)hashSupVideo.get(particellaVO
                  .getElencoConduzioni()[0].getIdConduzioneParticella());
 								if(elencoSupVideo == null) 
                {
 									elencoSupVideo = new Vector<String>();
 								}
 								elencoSupVideo.add(elencoSupUtilizzate[i]);
 								hashSupVideo.put(particellaVO.getElencoConduzioni()[0]
                  .getIdConduzioneParticella(), elencoSupVideo);
 								if(idTipoUtilizzoSecondario == null 
                  && Validator.isNotEmpty(elencoSupUtilizzateSecondarie[i])) 
                {
 									erroriSupSecondaria.add(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE);
 								}
 								else 
                {
 									if(idTipoUtilizzoSecondario != null 
                    && !Validator.isNotEmpty(elencoSupUtilizzateSecondarie[i])) 
                  {
 										erroriSupSecondaria.add(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 									}
 									else if(Validator.isNotEmpty(elencoSupUtilizzateSecondarie[i]) 
                    && (Validator.validateDouble(elencoSupUtilizzateSecondarie[i], 999999.9999) == null)) 
                  {
 										erroriSupSecondaria.add(AnagErrors.ERRORE_CAMPO_ERRATO);
 									}
 									else if(Validator.isNotEmpty(elencoSupUtilizzateSecondarie[i]) 
                    && Double.parseDouble(Validator.validateDouble(elencoSupUtilizzateSecondarie[i], 999999.9999)) > Double.parseDouble(Validator.validateDouble(elencoSupUtilizzate[i], 999999.9999))) 
                  {
 										erroriSupSecondaria.add(AnagErrors.ERRORE_KO_SUP_UTILIZZATA_SECONDARIA_MAGGIORE_SUP_UTILIZZATA);
 									}
 									else 
                  {
 										erroriSupSecondaria.add(null);
 									}
 								}
 							}
 						}
 					}
 				}
 			}
 			// Se si sono verificati degli errori nei controlli di validità formale
 			//delle superfici li visualizzo
 			boolean isOk = false;
    	boolean isOkSecondaria = false;
    	boolean isOkFruttaGuscio = false;
    	boolean isOkDettaglioUso = false;
    	for(int j = 0; j < errori.size(); j++) 
      {
  			String errore = (String)errori.elementAt(j);
  			if(Validator.isNotEmpty(errore)) {
    			isOk = false;
    			break;
  			}
  			else {
    			isOk = true;
  			}
    	}
  		for(int j = 0; j < erroriSupSecondaria.size(); j++) 
      {
  			String erroreSecondaria = (String)erroriSupSecondaria.elementAt(j);
  			if(Validator.isNotEmpty(erroreSecondaria)) {
    			isOkSecondaria = false;
    			break;
  			}
  			else {
    			isOkSecondaria = true;
  			}
  		}
    	if(!isOk || !isOkSecondaria) 
      {
  			if(!isOk) {
    				request.setAttribute("errori", errori);
  			}
  			if(!isOkSecondaria) {
    				request.setAttribute("erroriSupSecondaria", erroriSupSecondaria);
  			}
  			%>
				<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
				<%
 			}
  		// Se li ho passati allora effettuo i controlli di coerenza dei valori
  		// relativi alle superfici utilizzate inserite onde evitare i casi di "SUPERO"
  		int posizioneErrore = -1;  		
      
      for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
      {
        StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];
        Vector<String> supVideoPart = hashSupVideoParticella.get(storicoVO.getIdParticella());
        Vector<String> vIdUtilizzi = hIdUtilizzi.get(storicoVO.getIdParticella()); 
        double sommaVideo = 0;
        BigDecimal sommaNoVideoBd = gaaFacadeClient.getSumUtilizziPrimariNoIndicati(
          anagAziendaVO.getIdAzienda().longValue(), storicoVO.getIdParticella().longValue(),vIdUtilizzi); 
        double sommaNoVideo = sommaNoVideoBd.doubleValue();
        
        for(int a = 0; a < supVideoPart.size(); a++) 
        {
          String totSupVideo = (String)supVideoPart.elementAt(a);
          sommaVideo += Double.parseDouble(Validator.validateDouble(totSupVideo, 999999.9999));
        }
        posizioneErrore++;
        
        double sommaConfronto = sommaVideo + sommaNoVideo;
        
        /*String supConfronto = AnagUtils.valSupCatGraf(
          storicoVO.getSupCatastale(), storicoVO.getSuperficieGrafica());
        if(Validator.isNotEmpty(storicoVO.getFoglioVO())
          && Validator.isNotEmpty(storicoVO.getFoglioVO().getFlagStabilizzazione())  
	        && (storicoVO.getFoglioVO().getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	      {
	        if(Validator.validateDouble(storicoVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
	          && Double.parseDouble(storicoVO.getSuperficieGrafica().replace(',', '.')) > 0)
	        {
	          supConfronto = storicoVO.getSuperficieGrafica();
	        }
	      }*/  
	      
	      String supConfronto = "0";
	      if(Validator.validateDouble(storicoVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
          && Double.parseDouble(storicoVO.getSuperficieGrafica().replace(',', '.')) > 0)
        {
          supConfronto = storicoVO.getSuperficieGrafica();
        }
	      
        
        if(NumberUtils.arrotonda(sommaConfronto, 4) > NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',', '.')), 4)) 
        {
          while(posizioneErrore > errori.size()) 
          {
              errori.add(errori.size(), null);
          }
          errori.add(posizioneErrore, AnagErrors.ERRORE_SUPERO_SUP_UTILIZZATE + " "
            + StringUtils.parseSuperficieField(supConfronto));
        }
      }
  		for(int j = 0; j < errori.size(); j++) 
      {
  			String errore = (String)errori.elementAt(j);
  			if(Validator.isNotEmpty(errore)) {
    			isOk = false;
    			break;
  			}
  			else {
    			isOk = true;
  			}
  		}
  		
  		// Se si sono verificati degli errori nei controlli del "SUPERO"
      if(!isOk) 
      {
        request.setAttribute("errori", errori);
        %>
          <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
        <%
        return;
      }
  		
  		
  		//Controlli frutta guscio
  		Vector<String> erroriFruttaGuscio = new Vector<String>();
  		if(isOk) 
      {
        
        for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
	      {
	        StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];       
	        
	        TipoUtilizzoVO tipoUtilizzoVOSel = anagFacadeClient.findTipoUtilizzoByPrimaryKey(new Long(idTipoUtilizzo));
          String flagFruttaGuscio = tipoUtilizzoVOSel.getFlagFruttaGuscio();
          
		      ConduzioneParticellaVO conduzioneParticellaGuscioVO = (ConduzioneParticellaVO)storicoVO.getElencoConduzioni()[0];
		      UtilizzoParticellaVO utilizzoParticellGuscioVO = (UtilizzoParticellaVO)conduzioneParticellaGuscioVO.getElencoUtilizzi()[0];
      
          
          boolean errFruttaGuscio = false;          
          if("S".equalsIgnoreCase(flagFruttaGuscio))
          {
            if(Validator.isEmpty(utilizzoParticellGuscioVO.getAnnoImpianto()))
            {
              errFruttaGuscio = true;
            }
   
            if(Validator.isEmpty(utilizzoParticellGuscioVO.getIdImpianto())
              && !errFruttaGuscio)
            {
              errFruttaGuscio = true;        
            }
            
            if(Validator.isEmpty(utilizzoParticellGuscioVO.getSestoSuFile())
              && !errFruttaGuscio)
            {
              errFruttaGuscio = true;        
            }
            
            if(Validator.isEmpty(utilizzoParticellGuscioVO.getSestoTraFile())
              && !errFruttaGuscio)
            {
              errFruttaGuscio = true;        
            }
            
            if(Validator.isEmpty(utilizzoParticellGuscioVO.getNumeroPianteCeppi())
              && !errFruttaGuscio)
            {
              errFruttaGuscio = true;        
            }
          }       
	        
	        
	        if(errFruttaGuscio) 
	        {
	          erroriFruttaGuscio.add(AnagErrors.ERRORE_FRUTTA_GUSCIO_OBBLIGATORIO_MOD_MUL);
	        }
	        else
	        {
	          erroriFruttaGuscio.add(null);
	        }
	      }
	      for(int j = 0; j < erroriFruttaGuscio.size(); j++) 
	      {
          String errore = (String)erroriFruttaGuscio.elementAt(j);
          if(Validator.isNotEmpty(errore)) 
          {
            isOkFruttaGuscio = false;
            break;
          }
          else 
          {
            isOkFruttaGuscio = true;
          }
	      }     
      
  		}
  		
  		//Controlli dettaglio uso
      Vector<String> erroriDettaglioUso = new Vector<String>();
      if(isOk) 
      {
        
        for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
        {
          StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];       
          boolean erroreDettaglioUso = false;
          if(Validator.isNotEmpty(storicoVO.getParticellaCertificataVO())
            && Validator.isNotEmpty(storicoVO.getParticellaCertificataVO().getIdParticellaCertificata()))
          {
            if(Validator.isNotEmpty(catalogoMatriceVO))
            {
	            //TipoDettaglioUsoVO tipoDettaglioUsoVOSel = gaaFacadeClient.findDettaglioUsoByPrimaryKey(idTipoDettaglioUso);
	            String flagPratoPermanente = catalogoMatriceVO.getFlagPratoPermanente();
	              
	            boolean isRegistroPascoliPratoPolifita = gaaFacadeClient.isRegistroPascoliPratoPolifita(
	                storicoVO.getParticellaCertificataVO().getIdParticellaCertificata().longValue());
	            
	            String valore = gaaFacadeClient.getValoreAttivoTipoAreaFromParticellaAndId(storicoVO.getIdParticella(), 4);
	            
	             
	            if(isRegistroPascoliPratoPolifita && Validator.isNotEmpty(valore)
	              && !"1".equalsIgnoreCase(valore))
	            {
	              if(!"S".equalsIgnoreCase(flagPratoPermanente))
	              {
	                erroreDettaglioUso = true;
	              }
	            }
	          }            
          }
          
          if(erroreDettaglioUso) 
          {
            erroriDettaglioUso.add(AnagErrors.ERRORE_CAMPO_VARIETA_REGISTRO_POLIFITA);
          }
          else
          {
            erroriDettaglioUso.add(null);
          }   
        }
        
        for(int j = 0; j < erroriDettaglioUso.size(); j++) 
        {
          String errore = (String)erroriDettaglioUso.elementAt(j);
          if(Validator.isNotEmpty(errore)) 
          {
            isOkDettaglioUso = false;
            break;
          }
          else 
          {
            isOkDettaglioUso = true;
          }
        }     
      
      }
  		
  		
  		
  		// Se si sono verificati degli errori nei controlli della frutta guscio
  		if(!isOk || !isOkFruttaGuscio || !isOkDettaglioUso) 
      {
        if(!isOkFruttaGuscio)
        {
          error = new ValidationError(AnagErrors.ERRORE_FRUTTA_GUSCIO_OBBLIGATORIO_MOD_MUL_JSSP);
	        errors.add("idTipoUtilizzo", error);
	      }
	      
	      request.setAttribute("errors", errors);          
  			request.setAttribute("erroriFruttaGuscio", erroriFruttaGuscio);
  			request.setAttribute("erroriDettaglioUso", erroriDettaglioUso);
        %>
          <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
        <%
  		}
  		// Altrimenti effettuo l'operazione di associazione dell'uso
  		else 
      {
  			try 
        {
  				anagFacadeClient.associaUso(anagAziendaVO.getIdAzienda().longValue(), elencoStoricoParticellaVO, ruoloUtenza);
  			}
  			catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_ASSOCIA_USO+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
  			}
  		}
 		}
    else if(operazione.equalsIgnoreCase("eleggibilitaGIS")) 
    {
      Vector<String> errori = new Vector<String>();
      Vector<String> erroriSupSecondaria = new Vector<String>();
      String[] idUtilizzoSecondario = request.getParameterValues("idUtilizzoSecondario");
      String[] idUtilizzoPrimario = request.getParameterValues("idUtilizzoPrimario");
      //String[] idVarietaSecondario = request.getParameterValues("idVarietaSecondario");
      //String[] idVarietaPrimario = request.getParameterValues("idVarietaPrimario");
      
      // Altrimenti effettuo i controlli di validità e coerenza delle superfici
      // delle particelle
      
      Vector<String> elencoSupVideo = null;
      for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
      {
        StoricoParticellaVO particellaVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];
        //indica che l'allinemaneto nn si può fare se ci sono degli sfrigu!!!
        if(Validator.isEmpty(idUtilizzoPrimario[i]))
        {
          errori.add(AnagErrors.ERRORE_NON_DICHIARATO_USO); 
        }
        else if(!Validator.isNotEmpty(elencoSupUtilizzate[i])) 
        {
          errori.add(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
        }
        else 
        {
          if(Validator.validateDouble((String)elencoSupUtilizzate[i], 999999.9999) == null) 
          {
            errori.add(AnagErrors.ERRORE_CAMPO_ERRATO);
          }
          else 
          {
            if(Double.parseDouble(Validator.validateDouble((String)elencoSupUtilizzate[i], 999999.9999)) <= 0) 
            {
              errori.add(AnagErrors.ERRORE_CAMPO_ERRATO);
            }
            else 
            {
              errori.add(null);
              elencoSupVideo = (Vector<String>)hashSupVideo.get(particellaVO.getElencoConduzioni()[0].getIdConduzioneParticella());
              if(elencoSupVideo == null) 
              {
                elencoSupVideo = new Vector<String>();
              }
              elencoSupVideo.add(elencoSupUtilizzate[i]);
              hashSupVideo.put(particellaVO.getElencoConduzioni()[0].getIdConduzioneParticella(), elencoSupVideo);
              if(!Validator.isNotEmpty(idUtilizzoSecondario[i]) 
                && Validator.isNotEmpty(elencoSupUtilizzateSecondarie[i])) 
              {
                erroriSupSecondaria.add(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE);
              }
              else 
              {
                if(Validator.isNotEmpty(idUtilizzoSecondario[i]) 
                  && !Validator.isNotEmpty(elencoSupUtilizzateSecondarie[i])) 
                {
                  erroriSupSecondaria.add(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
                }
                else if(Validator.isNotEmpty(elencoSupUtilizzateSecondarie[i]) 
                  && (Validator.validateDouble(elencoSupUtilizzateSecondarie[i], 999999.9999) == null)) 
                {
                  erroriSupSecondaria.add(AnagErrors.ERRORE_CAMPO_ERRATO);
                }
                else if(Validator.isNotEmpty(elencoSupUtilizzateSecondarie[i]) 
                  && Double.parseDouble(Validator.validateDouble(elencoSupUtilizzateSecondarie[i], 999999.9999)) > Double.parseDouble(Validator.validateDouble(elencoSupUtilizzate[i], 999999.9999))) 
                {
                  erroriSupSecondaria.add(AnagErrors.ERRORE_KO_SUP_UTILIZZATA_SECONDARIA_MAGGIORE_SUP_UTILIZZATA);
                }
                else 
                {
                  erroriSupSecondaria.add(null);
                }
              }
            }
          }
        }
        
        
        /*if(Validator.isNotEmpty(idUtilizzoPrimario[i]))
        {
          particellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].setIdUtilizzo(new Long(idUtilizzoPrimario[i]));
        }*/
        /*if(Validator.isNotEmpty(idVarietaPrimario[i]))
        {
          particellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].setIdVarieta(new Long(idVarietaPrimario[i]));
        }*/
        /*if(Validator.isNotEmpty(idUtilizzoSecondario[i]))
        {
          particellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].setIdUtilizzoSecondario(new Long(idUtilizzoSecondario[i]));
        }*/
        /*if(Validator.isNotEmpty(idVarietaSecondario[i]))
        {
          particellaVO.getElencoConduzioni()[0].getElencoUtilizzi()[0].setIdVarietaSecondaria(new Long(idVarietaSecondario[i]));
        }*/
        
        
        
      }
      
      // Se si sono verificati degli errori nei controlli di validità formale
      //delle superfici li visualizzo
      boolean isOk = false;
      boolean isOkSecondaria = false;
      for(int j = 0; j < errori.size(); j++) 
      {
        String errore = (String)errori.elementAt(j);
        if(Validator.isNotEmpty(errore)) {
          isOk = false;
          break;
        }
        else {
          isOk = true;
        }
      }
      for(int j = 0; j < erroriSupSecondaria.size(); j++) 
      {
        String erroreSecondaria = (String)erroriSupSecondaria.elementAt(j);
        if(Validator.isNotEmpty(erroreSecondaria)) {
          isOkSecondaria = false;
          break;
        }
        else {
          isOkSecondaria = true;
        }
      }
      if(!isOk || !isOkSecondaria) 
      {
        if(!isOk) {
            request.setAttribute("errori", errori);
        }
        if(!isOkSecondaria) {
            request.setAttribute("erroriSupSecondaria", erroriSupSecondaria);
        }
        %>
        <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
        <%
      }
      // Se li ho passati allora effettuo i controlli di coerenza dei valori
      // relativi alle superfici utilizzate inserite onde evitare i casi di "SUPERO"
      int posizioneErrore = -1;
      
      for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
      {
        StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];
        Vector<String> supVideoPart = hashSupVideoParticella.get(storicoVO.getIdParticella());
        Vector<String> vIdUtilizzi = hIdUtilizzi.get(storicoVO.getIdParticella()); 
        double sommaVideo = 0;
        BigDecimal sommaNoVideoBd = gaaFacadeClient.getSumUtilizziPrimariNoIndicati(
          anagAziendaVO.getIdAzienda().longValue(), storicoVO.getIdParticella().longValue(), vIdUtilizzi); 
        double sommaNoVideo = sommaNoVideoBd.doubleValue();
        
        for(int a = 0; a < supVideoPart.size(); a++) 
        {
          String totSupVideo = (String)supVideoPart.elementAt(a);
          sommaVideo += Double.parseDouble(Validator.validateDouble(totSupVideo, 999999.9999));
          
        }
        posizioneErrore++;
        
        double sommaConfronto = sommaVideo + sommaNoVideo;
        
        
        //Modifica del 17/05/2016
        /*String supConfronto = AnagUtils.valSupCatGraf(
          storicoVO.getSupCatastale(), storicoVO.getSuperficieGrafica());
        if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO())
          && Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())  
          && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
        {
          if(Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
            && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
          {
            supConfronto = storicoParticellaVO.getSuperficieGrafica();
          }
        }*/ 
        
        String supConfronto = "0";
        if(Validator.validateDouble(storicoVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
          && Double.parseDouble(storicoVO.getSuperficieGrafica().replace(',', '.')) > 0)
        {
          supConfronto = storicoVO.getSuperficieGrafica();
        } 
        
        if(NumberUtils.arrotonda(sommaConfronto, 4) > NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',', '.')), 4)) 
        {
          while(posizioneErrore > errori.size()) 
          {
              errori.add(errori.size(), null);
          }
          errori.add(posizioneErrore, AnagErrors.ERRORE_SUPERO_SUP_UTILIZZATE + " "
            + StringUtils.parseSuperficieField(String.valueOf(NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',', '.')), 4))));
        }
      }
        
      
      for(int j = 0; j < errori.size(); j++) 
      {
        String errore = (String)errori.elementAt(j);
        if(Validator.isNotEmpty(errore)) {
          isOk = false;
          break;
        }
        else {
          isOk = true;
        }
      }
      // Se si sono verificati degli errori nei controlli del "SUPERO"
      if(!isOk) {
        request.setAttribute("errori", errori);
        %>
        <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
      <%
      }
      // Altrimenti effettuo l'operazione di associazione dell'uso
      else 
      {
        try 
        {
          anagFacadeClient.associaUsoEleggibilitaGis(anagAziendaVO.getIdAzienda().longValue(), elencoStoricoParticellaVO, ruoloUtenza);
        }
        catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_ASSOCIA_USO+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
      }
    }
 		// L'utente ha selezionato la funzione "cambia titolo possesso"
 		else if(operazione.equalsIgnoreCase("cambia")) 
    {
 			// Controllo che sia stato indicato il nuovo titolo di possesso
 			Long idTitoloPossesso = null;
 			if(!Validator.isNotEmpty(request.getParameter("idTitoloPossesso"))) 
 			{
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 				errors.add("idTitoloPossesso", error);
 				request.setAttribute("errors", errors);
 				%>
					<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
				<%
 			}
 			else 
 			{
 				idTitoloPossesso = Long.decode(request.getParameter("idTitoloPossesso"));
 				request.setAttribute("idTitoloPossesso", idTitoloPossesso);
 				if(!Validator.isNotEmpty(request.getParameter("confermoOperazione"))) {
 					// Controllo se ci sono dei documenti attivi legati alle conduzioni 
 					// selezionate
 					Vector elencoDocumentiConduzioni = null;
 					try 
 					{
 						if(oldConduzioni != null && oldConduzioni.size() > 0) 
 						{
 							for(int a = 0; a < oldConduzioni.size(); a++) 
 							{
 								elencoDocumentiConduzioni = anagFacadeClient.getListDettaglioDocumentoByIdConduzione((Long)oldConduzioni.elementAt(a), false, false);
								// Se ci sono torno alla pagina di modifica e avviso l'utente degli
 			 					// effetti della sua scelta
 			 					if(elencoDocumentiConduzioni != null && elencoDocumentiConduzioni.size() > 0) {
 			 						request.setAttribute("onLoad", "onLoad");
 			 						%>
 										<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
 									<%
 									break;
 			 					}
 							}
 						}
 					}
 					catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DOCUMENTI_PARTICELLE+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;	
    			} 				
 				}
 				try 
        {
 					anagFacadeClient.cambiaTitoloPossesso((Long[])oldConduzioni.toArray(new Long[oldConduzioni.size()]), ruoloUtenza, anagAziendaVO.getIdAzienda(), idTitoloPossesso);
 				}
 				catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CAMBIA_TITOLO_POSSESSO+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return; 
    		}
 			}
 		}
 		// L'utente ha selezionato la funzionalità "cessazione particella"
 		else if(operazione.equalsIgnoreCase("cessazione")) 
    {
 			// Recupero la data inserita
 			String dataCessazioneStr = request.getParameter("dataCessazione");
 			Date dataCessazione = null;
 			request.setAttribute("controlloData", "controlloData");
 			//Controllo che sia stata valorizzata
 			if(!Validator.isNotEmpty(dataCessazioneStr)) 
      {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 				errors.add("dataCessazione", error);
 				request.setAttribute("errors", errors);
 				%>
					<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
				<%
 			}
 			// Se lo è...
 			else 
      {
 				request.setAttribute("dataCessazioneStr", dataCessazioneStr);
 				// ... controllo che sia valida
 				if(!Validator.validateDateF(dataCessazioneStr)) 
        {
 					error = new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO);
 	 				errors.add("dataCessazione", error);
 	 				request.setAttribute("errors", errors);
 	 				%>
 						<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
 					<%
 				}
 				// Recupero la data max inizio conduzione
 				Date dataMax = null;
 				dataCessazione = DateUtils.parseDate(dataCessazioneStr);
 				try 
        {
 					dataMax = anagFacadeClient.getMaxDataInizioConduzioneParticella(oldConduzioni);
 				}
 				catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_MAX_DATA_INIZIO_CONDUZIONE+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return; 
 				}
 				// Controllo che non sia minore della data max inizio conduzione
 				if(dataCessazione.before(dataMax)) 
        {
 					error = new ValidationError(AnagErrors.ERRORE_DATA_CESSAZIONE_MAX_INIZIO_CONDUZIONE);
 	 				errors.add("dataCessazione", error);
 	 				request.setAttribute("errors", errors);
 	 				%>
 						<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
 					<%
 				}
 				// Controllo che non sia maggiore della data odierna
 				else if(dataCessazione.after(new java.util.Date(System.currentTimeMillis()))) 
        {
 					error = new ValidationError(AnagErrors.ERRORE_DATA_CESSAZIONE_POST_SYSDATE);
 	 				errors.add("dataCessazione", error);
 	 				request.setAttribute("errors", errors);
 	 				%>
 						<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
 					<%
 				}
 				if(!Validator.isNotEmpty(request.getParameter("confermoFabbricati"))) 
        {
 					// Se ho passato tutti i controlli verifico se esistono dei fabbricati attivi legati alle
 					// particelle selezionate
 					FabbricatoParticellaVO[] elencoFabbricati = null; 				
 					try 
          {
 						for(int i = 0; i < oldConduzioni.size(); i++) 
            {
 							Long idConduzioneParticella = (Long)oldConduzioni.elementAt(i);
 							elencoFabbricati = anagFacadeClient.getListFabbricatoParticellaByIdAziendaAndIdConduzioneParticella(idConduzioneParticella, anagAziendaVO.getIdAzienda(), null, true);
 							if(elencoFabbricati != null && elencoFabbricati.length > 0) 
              {
 								request.setAttribute("loadFabbricati", "loadFabbricati");
 								%>
 	 								<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
 	 							<%
 	 							break;
 							}
 						}
 					}
 					catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_FABBRICATO_PARTICELLA+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return; 
 					}
 				}       
        
        
        
 				// Passati tutti i controlli e avuto conferma dall'utente di tutte le conseguenze
 				// possibili derivanti dall'operazione effettuo la cessazione
 				try 
        {
 					Long[] elencoIdConduzioneParticella = (Long[])oldConduzioni.toArray(new Long[oldConduzioni.size()]);
 					anagFacadeClient.cessaParticelle(elencoIdConduzioneParticella, 
 					  ruoloUtenza.getIdUtente(), anagAziendaVO.getIdAzienda(), dataCessazione, SolmrConstants.MODIFICA_MULTIPLA);
 				}
 				catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CESSA_PARTICELLE+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
    		}
 			}
 		}
 		// L'utente ha selezionato l'operazione "cambia UTE"
 		else if(operazione.equalsIgnoreCase("cambiaUte")) 
 		{
 			// Recupero l'UTE selezionata
 			String unitaProduttiva = request.getParameter("idUnitaProduttiva");
 			
 			// Verifico che effettivamente sia stata selezionata
 			if(!Validator.isNotEmpty(unitaProduttiva)) {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 				errors.add("idUnitaProduttiva", error);
 				request.setAttribute("errors", errors);
 				%>
					<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
				<%
 			}
 			// Se è stata selezionata effettuo il cambio unità produttiva
 			else 
      {
 				try 
        {
 					anagFacadeClient.cambiaUte((Long[])oldConduzioni.toArray(new Long[oldConduzioni.size()]), ruoloUtenza, Long.decode(unitaProduttiva), anagAziendaVO.getIdAzienda());
 				}
 				catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CAMBIA_UTE+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
    		}
 			}
 		}
 		// L'utente ha selezionato l'operazione "associa documento"
 		else if(operazione.equalsIgnoreCase("associaDocumento")) 
 		{
 			// Recupero il documento selezionato
 			String documento = request.getParameter("idDocumento");
 			
 			// Verifico che effettivamente sia stato selezionato
 			if(!Validator.isNotEmpty(documento)) {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 				errors.add("idDocumento", error);
 				request.setAttribute("errors", errors);
 				%>
					<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
				<%
 			}
 			// Se è stato selezionato associo il documento alle particelle selezionate
 			else 
      {
 				try 
        {
 					anagFacadeClient.associaDocumento((Long[])oldConduzioni.toArray(new Long[oldConduzioni.size()]), Long.decode(documento));
 				}
 				catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_ASSOCIA_DOCUMENTO+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
    		}
 			}
 		}
 		// L'utente ha selezionato l'operazione "dichiarazione uso agronomico"
 		else if(operazione.equalsIgnoreCase("dichiarazioneUsoAgronomico")) 
    {
 			Vector<String> erroriSupAgronomiche = new Vector<String>(); 
 			// Effettuo i controlli di validità e coerenza delle superfici agronomiche
			for(int i = 0; i < elencoStoricoParticellaVO.length; i++) 
      {
				StoricoParticellaVO particellaVO = (StoricoParticellaVO)elencoStoricoParticellaVO[i];
				if(!Validator.isNotEmpty(elencoSupAgronomiche[i])) 
        {
					erroriSupAgronomiche.add(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
				}
				else 
        {
					if(Validator.validateDoubleIncludeZero((String)elencoSupAgronomiche[i], 999999.9999) == null) 
          {
						erroriSupAgronomiche.add(AnagErrors.ERRORE_CAMPO_ERRATO);
					}
					else 
          {
						if(Double.parseDouble(Validator.validateDoubleIncludeZero((String)elencoSupAgronomiche[i], 999999.9999)) < 0) 
						{
							erroriSupAgronomiche.add(AnagErrors.ERRORE_CAMPO_ERRATO);
						}
						else {
							if(Double.parseDouble(Validator.validateDoubleIncludeZero((String)elencoSupAgronomiche[i], 999999.9999)) > Double.parseDouble(Validator.validateDoubleIncludeZero(particellaVO.getElencoConduzioni()[0].getSuperficieCondotta(), 999999.9999))) {
								erroriSupAgronomiche.add(AnagErrors.ERRORE_SUP_AGRONOMICA_MAX_SUP_CONDOTTA);
							}
							else {
								erroriSupAgronomiche.add(null);
							}
						}
					}
				}
				
				if(elencoStoricoParticellaVO[i].getElencoConduzioni()[0].getIdTitoloPossesso()
				  .compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
				{
				  elencoStoricoParticellaVO[i].getElencoConduzioni()[0].setSuperficieAgronomica(SolmrConstants.DEFAULT_SUPERFICIE);
				}
			}
 			// Se si sono verificati degli errori li visualizzo
 			boolean isOk = false;
   		for(int j = 0; j < erroriSupAgronomiche.size(); j++) 
   		{
    			String errore = (String)erroriSupAgronomiche.elementAt(j);
    			if(Validator.isNotEmpty(errore)) 
    			{
      			isOk = false;
      			break;
    			}
    			else 
    			{
      			isOk = true;
    			}
   		}
   		if(!isOk) 
   		{
     		request.setAttribute("erroriSupAgronomiche", erroriSupAgronomiche);
   			%>
				  <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
			  <%
			}
  		// Altrimenti effettuo l'operazione di dichiarazione dell'uso agronomico
  		else 
      {
  			try 
        {
  				anagFacadeClient.dichiaraUsoAgronomico(elencoStoricoParticellaVO, ruoloUtenza, anagAziendaVO.getIdAzienda());
  			}
  			catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DICHIARAZIONE_AGRONOMICA+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
  			}
  		}
 		}
    //Irrigazione
    else if(operazione.equalsIgnoreCase("irrigazione")) 
    {
      
      String irrigata = request.getParameter("irrigata");
      String idTipoIrrigazione = request.getParameter("idTipoIrrigazione");
      boolean flagIrrigabilita = false;
      if(Validator.isNotEmpty(irrigata) && irrigata.equalsIgnoreCase("si"))
      {
        flagIrrigabilita = true;
      }
      Long idIrrigazione = null;
      if(Validator.isNotEmpty(idTipoIrrigazione))
      {
        idIrrigazione = new Long(idTipoIrrigazione);
      }
      
     
      try 
      {
        anagFacadeClient.aggiornaIrrigazione(elencoStoricoParticellaVO, ruoloUtenza, flagIrrigabilita, idIrrigazione);
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_MODIFICA_IRRIGAZIONE+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
    // L'utente ha selezionato l'operazione allineaPercentualePossesso
    else if(operazione.equalsIgnoreCase("allineaPercPoss")) 
    {
      // Recupero l'UTE selezionata
      String percentualePossessoIn = request.getParameter("percentualePossessoIn");
      
      // Verifico che effettivamente sia stata selezionata
      if(!Validator.isNotEmpty(percentualePossessoIn)) 
      {
        error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
        errors.add("percentualePossessoIn", error);
        request.setAttribute("errors", errors);
        %>
          <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
        <%
      }
      // Se è stata selezionata effettuo il cambio unità produttiva
      else 
      {
        if(!Validator.isNumberPercentualeMaggioreZeroDecimali(percentualePossessoIn))
        {
          error = new ValidationError(AnagErrors.ERRORE_CAMPO_PERCENTUALE_ERRATO);
	        errors.add("percentualePossessoIn", error);
	        request.setAttribute("errors", errors);
	        %>
	          <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
	        <%
	        return;
        }
        else
        {
          
          if(almenoUnaProvvisoria)
          {
            error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_PART_PROV);
	          errors.add("percentualePossessoIn", error);
	          request.setAttribute("errors", errors);
	          %>
	            <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
	          <%
	          return;
          }
          
          if(almenoUnaSupCatGrafZero)
          {
            error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_SUPCATGRAF_ZERO);
            errors.add("percentualePossessoIn", error);
            request.setAttribute("errors", errors);
            %>
              <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
            <%
            return;
          }
          
          BigDecimal percentualePossessoBg = new BigDecimal(request.getParameter("percentualePossessoIn").replace(",","."));
          
          for(int j=0;j<vIdParticella.size();j++)
          {
            Vector<Long> vIdConduzioniTutte = gaaFacadeClient
              .getIdConduzioneFromIdAziendaIdParticella(anagAziendaVO.getIdAzienda().longValue(),
              vIdParticella.get(j).longValue());
            for(int k=0;k<vIdConduzioniTutte.size();k++)
            {
              //Devono essere presenti tutte le conduzioni azienda/particella
              //Altrimenti non si può fare
              if(!vIdConduzione.contains(vIdConduzioniTutte.get(k)))
              {
                error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_NO_TUTTI_USI);
		            errors.add("percentualePossessoIn", error);
		            request.setAttribute("errors", errors);
		            %>
		              <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
		            <%
		            return;  
              }
              
              Vector<Long> vIdUtilizziTutte = gaaFacadeClient
                .getIdUtilizzoFromIdIdConduzione(vIdConduzioniTutte.get(k).longValue());
              if(Validator.isNotEmpty(vIdUtilizziTutte))
              {
                for(int z=0;z<vIdUtilizziTutte.size();z++)
		            {
		              //Devono essere presenti tutti gli usi del suolo della conduzione
		              //Altrimenti non si può fare
		              if(!vIdUtilizzo.contains(vIdUtilizziTutte.get(z)))
		              {
		                error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_NO_TUTTI_USI);
		                errors.add("percentualePossessoIn", error);
		                request.setAttribute("errors", errors);
		                %>
		                  <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
		                <%
		                return;  
		              }
		            }
              
              }
            }
            
            BigDecimal sommaUtlizziParticella = gaaFacadeClient
              .getSumSupUtilizzoParticellaAzienda(anagAziendaVO.getIdAzienda().longValue(),
                vIdParticella.get(j).longValue());
            if(Validator.isNotEmpty(sommaUtlizziParticella))
            {            
	            sommaUtlizziParticella = sommaUtlizziParticella.divide(
	              hSupCatGraf.get(vIdParticella.get(j).longValue()),4,BigDecimal.ROUND_HALF_UP);              
	            sommaUtlizziParticella = sommaUtlizziParticella.multiply(new BigDecimal(100));            
	            sommaUtlizziParticella = sommaUtlizziParticella.setScale(2, BigDecimal.ROUND_HALF_UP);
	             
	            if(percentualePossessoBg.compareTo(new BigDecimal(100)) < 0)
	            {  
		            if(sommaUtlizziParticella.compareTo(percentualePossessoBg) > 0)
		            {
		              error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_SUPUTIL_MAG_PERC);
		              errors.add("percentualePossessoIn", error);
		              request.setAttribute("errors", errors);
		              %>
		                <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
		              <%
		              return;
		            }
		          }
		        }
		        
		        BigDecimal sommaConduzioneAsservimento = gaaFacadeClient
              .getSumSupCondottaAsservimentoParticellaAzienda(anagAziendaVO.getIdAzienda().longValue(),
                vIdParticella.get(j).longValue());
		        //sup asservimento
		        if(Validator.isNotEmpty(sommaConduzioneAsservimento))
		        {
		          sommaConduzioneAsservimento = sommaConduzioneAsservimento.divide(
                hSupCatGraf.get(vIdParticella.get(j).longValue()),4,BigDecimal.ROUND_HALF_UP);              
              sommaConduzioneAsservimento = sommaConduzioneAsservimento.multiply(new BigDecimal(100));            
              sommaConduzioneAsservimento = sommaConduzioneAsservimento.setScale(2, BigDecimal.ROUND_HALF_UP);
               
              if(percentualePossessoBg.compareTo(new BigDecimal(100)) < 0)
              {  
                if(sommaConduzioneAsservimento.compareTo(percentualePossessoBg) > 0)
                {
                  error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_SUPUTIL_MAG_PERC_ASS);
                  errors.add("percentualePossessoIn", error);
                  request.setAttribute("errors", errors);
                  %>
                    <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
                  <%
                  return;
                }
              }
		        
		        }
          
          }
          
          try 
          {
            anagFacadeClient.cambiaPercentualePossesso((Long[])oldConduzioni.toArray(new Long[oldConduzioni.size()]), 
              vIdParticella, ruoloUtenza, anagAziendaVO.getIdAzienda(), percentualePossessoBg);
          }
          catch(SolmrException se) 
          {
            SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CAMBIA_PERCENTUALE_POSSESSO+".\n"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
        }
        
      }
    }
    // L'utente ha selezionato l'operazione allinea la Percentuale di Possesso rispetto alla superficie utilizzata
    else if(operazione.equalsIgnoreCase("allineaSupUtilPercPoss")) 
    {          
      if(almenoUnaProvvisoria)
      {
        error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_PART_PROV);
        errors.add("allineaSupUtilPercPoss", error);
        request.setAttribute("errors", errors);
        %>
          <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
        <%
        return;
      }
      
      if(almenoUnaSupCatGrafZero)
      {
        error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_SUPCATGRAF_ZERO);
        errors.add("allineaSupUtilPercPoss", error);
        request.setAttribute("errors", errors);
        %>
          <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
        <%
        return;
      }
      
      for(int j=0;j<vIdParticella.size();j++)
      {
        Vector<Long> vIdConduzioniTutte = gaaFacadeClient
          .getIdConduzioneFromIdAziendaIdParticella(anagAziendaVO.getIdAzienda().longValue(),
          vIdParticella.get(j).longValue());
        for(int k=0;k<vIdConduzioniTutte.size();k++)
        {
          //Devono essere presenti tutte le conduzioni azienda/particella
          //Altrimenti non si può fare
          if(!vIdConduzione.contains(vIdConduzioniTutte.get(k)))
          {
            error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_NO_TUTTI_USI);
            errors.add("allineaSupUtilPercPoss", error);
            request.setAttribute("errors", errors);
            %>
              <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
            <%
            return;  
          }
          
          Vector<Long> vIdUtilizziTutte = gaaFacadeClient
            .getIdUtilizzoFromIdIdConduzione(vIdConduzioniTutte.get(k).longValue());
          if(Validator.isNotEmpty(vIdUtilizziTutte))
          {
            for(int z=0;z<vIdUtilizziTutte.size();z++)
            {
              //Devono essere presenti tutti gli usi del suolo della conduzione
              //Altrimenti non si può fare
              if(!vIdUtilizzo.contains(vIdUtilizziTutte.get(z)))
              {
                error = new ValidationError(AnagErrors.ERRORE_PERCENTUALE_MODMULT_NO_TUTTI_USI);
                errors.add("allineaSupUtilPercPoss", error);
                request.setAttribute("errors", errors);
                %>
                  <jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />
                <%
                return;  
              }
            }
          
          }
        }
      
      }
      
      try 
      {
        anagFacadeClient.cambiaPercentualePossessoSupUtilizzata(oldConduzioni, ruoloUtenza, anagAziendaVO.getIdAzienda());
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - terreniParticellareModificaMultiplaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CAMBIA_PERC_POSS_SUP_UTIL+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
        
        
      
    }
 		
		// Al termine di qualunque operazione selezionata dall'utente torno alla
		// pagina di ricerca/elenco
		String valorePagina = (String)session.getAttribute("pagina");
		%>
			<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" >
				<jsp:param name="pagina" value="<%= valorePagina %>" /> 
			</jsp:forward>
		<%
 	}
 	// L'utente ha selezionato il pulsante "annulla"
 	else if(request.getParameter("annulla") != null) {
 		String valorePagina = (String)session.getAttribute("pagina");
 		// Torno alla pagina di ricerca/elenco
 		%>
			<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" >
				<jsp:param name="pagina" value="<%= valorePagina %>" /> 
			</jsp:forward>
		<%
 	}
 	
 	// Vado alla pagina di modifica
 	%>
		<jsp:forward page="<%= terreniParticellareModificaMultiplaUrl %>" />

