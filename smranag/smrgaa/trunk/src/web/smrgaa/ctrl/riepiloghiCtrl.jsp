<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="java.math.*"%>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.PlSqlCodeDescription"%>

<%

	String iridePageName = "riepiloghiCtrl.jsp";
  %>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	
	final String errMsg = "Impossibile procedere nella sezione riepiloghi."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

	String riepiloghiUrl = "/view/riepiloghiView.jsp";
	String terreniParticellaElencoCtrlUrl = "/ctrl/terreniParticellareElencoCtrl.jsp";
	String actionUrl = "../layout/terreniParticellareElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String regimeRiepiloghi = request.getParameter("regimeRiepiloghi");

	ValidationErrors errors = new ValidationErrors();

	// Pulisco la sessione dall'oggetto che potrebbe essere stato caricato se l'utente
	// ha cliccato su AAEP
	session.removeAttribute("common");

	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
	// in modo che venga sempre effettuato
	try 
  {
  	anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
	}
	catch(SolmrException se) {
  	request.setAttribute("statoAzienda", se);
	}
	
	//prima volta che entro!!
	if(Validator.isEmpty(regimeRiepiloghi))
	{
	  PlSqlCodeDescription plCode = null;
      
      
    try 
    {
      plCode = gaaFacadeClient.calcolaEfaPlSql(
        anagAziendaVO.getIdAzienda().longValue(), null, ruoloUtenza.getIdUtente());
    }
    catch (SolmrException se) 
    {
      SolmrLogger.info(this, " - riepiloghiCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    if (plCode==null) 
    {
      SolmrLogger.info(this, " - riepiloghiCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+" pl null";
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    else if (plCode.getDescription()!=null) 
    {
      SolmrLogger.info(this, " - riepiloghiCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+" "+plCode.getDescription();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }

	}
  
  
  // Recupero il piano di riferimento selezionato
  String pianoRiferimento = request.getParameter("idDichiarazioneConsistenza");
  Long idPianoRiferimento = null;
  // Nel caso in cui non sia valorizzato vuol dire che è la prima volta che accedo alla pagina e quindi imposto il piano di riferimento alla data odierna
  if(!Validator.isNotEmpty(pianoRiferimento)) 
  {
    
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null);
  }
  else 
  {
    idPianoRiferimento = Long.decode(pianoRiferimento);
  }
  
  
  Vector<TipoRiepilogoVO> vTipoRiepilogo = null;
  try 
  {
    vTipoRiepilogo = gaaFacadeClient.getTipoRiepilogo(SolmrConstants.RIEPILOGO_TERRITORIALE, 
      ruoloUtenza.getCodiceRuolo());
    request.setAttribute("vTipoRiepilogo", vTipoRiepilogo);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - riepiloghiCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_TIPO_RIEPILOGHI+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  	
	int numeroProblemi = 0;
	String escludiAsservimento = request.getParameter("escludiAsservimento");
	if(!Validator.isNotEmpty(escludiAsservimento)) {
		escludiAsservimento = SolmrConstants.FLAG_N;
	}
	request.setAttribute("escludiAsservimento", escludiAsservimento);
	// L'utente ha cliccato il pulsante "elenco particelle"
	if(request.getParameter("elencoParticelle") != null) 
  {
		// Recupero i parametri
		Long tipoRicerca = Long.decode(request.getParameter("idTipoRiepilogo"));
		
		// RIEPILOGO PER TITOLO POSSESSO
		if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TITOLO_POSSESSO) == 0) 
		{
			String titoloPossesso = request.getParameter("idTitoloPossesso");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(titoloPossesso)) {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
	      		errors.add("error", error);
	      		request.setAttribute("errors", errors);
	      		numeroProblemi++;
			}
			else 
			{
				// Rimuovo dalla sessione il precedente filtro del particellare
				session.removeAttribute("filtriParticellareRicercaVO");
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
				filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
				filtriParticellareRicercaVO.setIdTitoloPossesso(Long.decode(titoloPossesso));
				filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
				filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
				filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
				// Rimetto il filtro in sessione
				session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
			}
		}
		// RIEPILOGO TITOLO POSSESSO-COMUNE
		else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TITOLO_POSSESSO_COMUNE) == 0) {
			String indice = request.getParameter("indice");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(indice)) {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
      			errors.add("error", error);
      			request.setAttribute("errors", errors);
      			numeroProblemi++;
			}
			else {
				// Rimuovo dalla sessione il precedente filtro del particellare
				session.removeAttribute("filtriParticellareRicercaVO");
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
				filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
				filtriParticellareRicercaVO.setIdTitoloPossesso(Long.decode(request.getParameterValues("idTitoloPossesso")[Integer.parseInt(indice)]));
				filtriParticellareRicercaVO.setIstatComune(request.getParameterValues("istatComuneParticella")[Integer.parseInt(indice)]);
				filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
				filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
				filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
				// Rimetto il filtro in sessione
				session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
			}
		}
		// RIEPILOGO COMUNE
		else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_COMUNE) == 0) {
			String indice = request.getParameter("indice");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(indice)) {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
      			errors.add("error", error);
      			request.setAttribute("errors", errors);
      			numeroProblemi++;
			}
			else {
				// Rimuovo dalla sessione il precedente filtro del particellare
				session.removeAttribute("filtriParticellareRicercaVO");
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
				filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
				filtriParticellareRicercaVO.setIstatComune(request.getParameterValues("istatComuneParticella")[Integer.parseInt(indice)]);
				filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
				filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
				filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
				// Rimetto il filtro in sessione
				session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
			}
		}
		// RIEPILOGO DESTINAZIONE D'USO
		else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTINAZIONE_USO) == 0) 
    {
			String utilizzo = request.getParameter("idUtilizzo");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(utilizzo)) {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
      			errors.add("error", error);
      			request.setAttribute("errors", errors);
      			numeroProblemi++;
			}
			else 
      {
				// Rimuovo dalla sessione il precedente filtro del particellare
				session.removeAttribute("filtriParticellareRicercaVO");
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
				filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        //caso senza uso del suolo/asservimento
        if(Validator.isNotEmpty(idPianoRiferimento) 
          && (idPianoRiferimento.longValue() > 0) && utilizzo.equalsIgnoreCase("-1"))
        {
          filtriParticellareRicercaVO.setIdTitoloPossesso(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO);
        }
				filtriParticellareRicercaVO.setIdUtilizzo(Long.decode(utilizzo));
				filtriParticellareRicercaVO.setCheckUsoPrimario(SolmrConstants.FLAG_S);
				filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
				filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
				// Rimetto il filtro in sessione
				session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
			}
		}
		// RIEPILOGO USO SECONDARIO
		else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_USO_SECONDARIO) == 0) {
			String utilizzoSecondario = request.getParameter("idUtilizzoSecondario");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(utilizzoSecondario)) {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
      			errors.add("error", error);
      			request.setAttribute("errors", errors);
      			numeroProblemi++;
			}
			else {
				// Rimuovo dalla sessione il precedente filtro del particellare
				session.removeAttribute("filtriParticellareRicercaVO");
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
				filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
				filtriParticellareRicercaVO.setIdUtilizzo(Long.decode(utilizzoSecondario));
				filtriParticellareRicercaVO.setCheckUsoSecondario(SolmrConstants.FLAG_S);
				filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
				filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
				// Rimetto il filtro in sessione
				session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
			}
		}
		// RIEPILOGO MACRO USO
		else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_MACRO_USO) == 0) {
			String macroUso = request.getParameter("idMacroUso");
			// Controllo che sia stata selezionata una voce dall'elenco
			if(!Validator.isNotEmpty(macroUso)) {
				ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
      			errors.add("error", error);
      			request.setAttribute("errors", errors);
      			numeroProblemi++;
			}
			else {
				// Rimuovo dalla sessione il precedente filtro del particellare
				session.removeAttribute("filtriParticellareRicercaVO");
				session.removeAttribute("filtriUnitaArboreaRicercaVO");
				// Lo ricreo
				FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
				filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
				filtriParticellareRicercaVO.setIdMacroUso(Long.decode(macroUso));
				filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
				filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
				filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
				// Rimetto il filtro in sessione
				session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
			}
		}
    // RIEPILOGO Zona Altimetrica
    else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_ZONA_ALTIMETRICA) == 0) 
    {
      String idZonaAltimetrica = request.getParameter("idZonaAltimetrica");
      // Controllo che sia stata selezionata una voce dall'elenco
      if(!Validator.isNotEmpty(idZonaAltimetrica)) {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
            errors.add("error", error);
            request.setAttribute("errors", errors);
            numeroProblemi++;
      }
      else {
        // Rimuovo dalla sessione il precedente filtro del particellare
        session.removeAttribute("filtriParticellareRicercaVO");
        session.removeAttribute("filtriUnitaArboreaRicercaVO");
        // Lo ricreo
        FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
        filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        filtriParticellareRicercaVO.setIdZonaAltimetrica(Long.decode(idZonaAltimetrica));
        filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
        filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
        filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
        // Rimetto il filtro in sessione
        session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
      }
    }
    // RIEPILOGO Caso Particolare
    else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_CASO_PARTICOLARE) == 0) 
    {
      String idCasoParticolare = request.getParameter("idCasoParticolare");
      // Controllo che sia stata selezionata una voce dall'elenco
      if(!Validator.isNotEmpty(idCasoParticolare)) {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
            errors.add("error", error);
            request.setAttribute("errors", errors);
            numeroProblemi++;
      }
      else {
        // Rimuovo dalla sessione il precedente filtro del particellare
        session.removeAttribute("filtriParticellareRicercaVO");
        session.removeAttribute("filtriUnitaArboreaRicercaVO");
        // Lo ricreo
        FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
        filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        filtriParticellareRicercaVO.setIdCasoParticolare(Long.decode(idCasoParticolare));
        filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
        filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
        filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
        // Rimetto il filtro in sessione
        session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
      }
    }
    // RIEPILOGO Tipo Efa
    else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TIPO_EFA) == 0) 
    {
      String idTipoEfa = request.getParameter("idTipoEfa");
      // Controllo che sia stata selezionata una voce dall'elenco
      if(!Validator.isNotEmpty(idTipoEfa)) {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
            errors.add("error", error);
            request.setAttribute("errors", errors);
            numeroProblemi++;
      }
      else 
      {
        // Rimuovo dalla sessione il precedente filtro del particellare
        session.removeAttribute("filtriParticellareRicercaVO");
        session.removeAttribute("filtriUnitaArboreaRicercaVO");
        // Lo ricreo
        FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
        filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        filtriParticellareRicercaVO.setIdTipoEfa(Long.decode(idTipoEfa));
        filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
        filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
        filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
        // Rimetto il filtro in sessione
        session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
      }
    }
    // RIEPILOGO Tipo Area
    else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TIPO_AREA) == 0) 
    {
      String idTipoValoreArea = request.getParameter("idTipoValoreArea");
            
      // Controllo che sia stata selezionata una voce dall'elenco
      if(!Validator.isNotEmpty(idTipoValoreArea)) {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT);
            errors.add("error", error);
            request.setAttribute("errors", errors);
            numeroProblemi++;
      }
      else 
      {
        StringTokenizer strToken = new StringTokenizer(idTipoValoreArea, "_");
        Long idTipoValoreAreaLg = new Long(strToken.nextToken());
        String flagFoglio = strToken.nextToken();
        
        // Rimuovo dalla sessione il precedente filtro del particellare
        session.removeAttribute("filtriParticellareRicercaVO");
        session.removeAttribute("filtriUnitaArboreaRicercaVO");
        // Lo ricreo
        FiltriParticellareRicercaVO filtriParticellareRicercaVO = new FiltriParticellareRicercaVO();
        filtriParticellareRicercaVO.setIdPianoRiferimento(idPianoRiferimento);
        filtriParticellareRicercaVO.setIdTipoValoreArea(idTipoValoreAreaLg);
        filtriParticellareRicercaVO.setFlagFoglio(flagFoglio);
        filtriParticellareRicercaVO.setIdUtilizzo(new Long(-1));
        filtriParticellareRicercaVO.setCheckEscludiAsservimento(escludiAsservimento);
        filtriParticellareRicercaVO.setCheckSoloAsservite(SolmrConstants.FLAG_N);
        // Rimetto il filtro in sessione
        session.setAttribute("filtriParticellareRicercaVO", filtriParticellareRicercaVO);
      }
    }
		// Torno alla pagina di ricerca/elenco dei terreni
		if(numeroProblemi == 0) {
			%>
				<jsp:forward page="<%= terreniParticellaElencoCtrlUrl %>" />
			<%
			return;
		}
	}
	
	Long tipoRicerca = null;
	if(request.getParameter("aggiornaRiepilogo") != null || numeroProblemi > 0) 
  {
     tipoRicerca = Long.decode(request.getParameter("idTipoRiepilogo"));
  }
  //prima volta che entro su riepiloghi
  //prendo quello di dafault
  else
  {  
    if(Validator.isNotEmpty(vTipoRiepilogo))
	  {
	    for(int i=0;i<vTipoRiepilogo.size();i++) 
	    {
	      TipoRiepilogoVO tipoRiepilogoVO = vTipoRiepilogo.get(i);
	      if("S".equalsIgnoreCase(tipoRiepilogoVO.getFlagDefault()))
	      {
	        tipoRicerca = new Long(tipoRiepilogoVO.getIdTipoRiepilogo());
	        break;
	      }
	    }
	  }
  }
  request.setAttribute("idTipoRicerca",tipoRicerca);

  // Se la ricerca è per titolo possesso
  if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TITOLO_POSSESSO) == 0) 
  {
  	// Se l'utente ha selezionato l'anno corrente
  	if(idPianoRiferimento.longValue() == -1) 
    {
  		StoricoParticellaVO[] elencoParticelleRiepiloghi = null;
    	try 
      {
    		elencoParticelleRiepiloghi = anagFacadeClient.riepilogoTitoloPossesso(anagAziendaVO.getIdAzienda());
    		request.setAttribute("elencoParticelleRiepiloghi", elencoParticelleRiepiloghi);
    	}
    	catch(SolmrException se) 
    	{
      	String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
      	request.setAttribute("messaggioErrore", messaggioErrore);
      	%>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
    	}
  	}
  	// Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
  	else 
  	{
  	  StoricoParticellaVO[] elencoParticelleRiepiloghi = null;
    	try 
    	{
    		elencoParticelleRiepiloghi = anagFacadeClient.riepilogoTitoloPossessoDichiarato(anagAziendaVO.getIdAzienda(), idPianoRiferimento);
    		request.setAttribute("elencoParticelleRiepiloghi", elencoParticelleRiepiloghi);
    	}
    	catch(SolmrException se) 
    	{
      	String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
      	request.setAttribute("messaggioErrore", messaggioErrore);
      	%>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
   		}
  	}
  }
  // Se la ricerca è per titolo di possesso e comune
  else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TITOLO_POSSESSO_COMUNE) == 0) 
  {
  	StoricoParticellaVO[] elencoStorico = null;
  	// Se l'utente ha selezionato l'anno corrente
  	if(idPianoRiferimento.longValue() == -1)
    {
  		try 
      {
   			elencoStorico = anagFacadeClient.riepilogoPossessoComune(anagAziendaVO.getIdAzienda());
   			request.setAttribute("elencoStorico", elencoStorico);
   		}
   		catch(SolmrException se) 
      {
     		String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
     		request.setAttribute("messaggioErrore", messaggioErrore);
        %>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
				<%
      }
    }
  	else 
    {
    	// Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
    	try 
      {
    		elencoStorico = anagFacadeClient.riepilogoPossessoComuneDichiarato(anagAziendaVO.getIdAzienda(), idPianoRiferimento);
    		request.setAttribute("elencoStorico", elencoStorico);
    	}
    	catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
			    <jsp:forward page="<%= riepiloghiUrl %>" />
		    <%
    	}
  	}
  }
  // Il tipo di riepilogo è per comune
	else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_COMUNE) == 0) 
  {
		StoricoParticellaVO[] elencoStorico = null;
		// Se l'utente ha selezionato l'anno corrente
		if(idPianoRiferimento.longValue() == -1) 
    {
 			try 
      {
 				elencoStorico = anagFacadeClient.riepilogoComune(anagAziendaVO.getIdAzienda(), escludiAsservimento);
 				request.setAttribute("elencoStorico", elencoStorico);
    	}
 			catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
     	}
   	}
   	else 
    {
 			// Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
 			try 
      {
 				elencoStorico = anagFacadeClient.riepilogoComuneDichiarato(anagAziendaVO.getIdAzienda(), escludiAsservimento, idPianoRiferimento);
 				request.setAttribute("elencoStorico", elencoStorico);
     	}
 			catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
     	}
 		}
	}
 	// Il tipo riepilogo è per destinazione d'uso
 	else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_DESTINAZIONE_USO) == 0) 
  {
 		// Se l'utente ha selezionato l'anno corrente
 		if(idPianoRiferimento.longValue() == -1) 
    {
 			UtilizzoParticellaVO[] elencoUtilizzi = null;
   		try 
      {
   			elencoUtilizzi = anagFacadeClient.riepilogoUsoPrimario(anagAziendaVO.getIdAzienda());
        request.setAttribute("elencoUtilizzi", elencoUtilizzi);
        BigDecimal totSenzaUsoSuolo = anagFacadeClient.getTotSupSfriguAndAsservimento(
           anagAziendaVO.getIdAzienda(), escludiAsservimento);
  			request.setAttribute("totSenzaUsoSuolo", totSenzaUsoSuolo);
  		}
   		catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
			    <jsp:forward page="<%= riepiloghiUrl %>" />
		    <%
   		}
    }
 		// Prendo i dati dalla dichiarazione di consistenza
 		else 
    {
 			UtilizzoDichiaratoVO[] elencoUtilizziDichiarati = null;
   		try 
      {
   			elencoUtilizziDichiarati = anagFacadeClient.riepilogoUsoPrimarioDichiarato(anagAziendaVO.getIdAzienda(), idPianoRiferimento);
   			request.setAttribute("elencoUtilizziDichiarati", elencoUtilizziDichiarati);
        if(Validator.isNotEmpty(escludiAsservimento) 
           && escludiAsservimento.equalsIgnoreCase(SolmrConstants.FLAG_N)) 
        {
          BigDecimal totSenzaUsoSuoloDichiarato = anagFacadeClient.getTotSupAsservimento(
             anagAziendaVO.getIdAzienda(), idPianoRiferimento);
          request.setAttribute("totSenzaUsoSuoloDichiarato", totSenzaUsoSuoloDichiarato);
        }
         
   		}
   		catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
			    <jsp:forward page="<%= riepiloghiUrl %>" />
		    <%
   		}
 		}
 	}
 	// Il tipo riepilogo è per uso secondario
 	else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_USO_SECONDARIO) == 0) 
  {
 		// L'utente sta effettuando una ricerca per anno corrente
   	if(idPianoRiferimento.longValue() == -1) 
    {
   		UtilizzoParticellaVO[] elencoUtilizziSecondari = null;
     	try 
      {
     		elencoUtilizziSecondari = anagFacadeClient.riepilogoUsoSecondario(anagAziendaVO.getIdAzienda());
     		request.setAttribute("elencoUtilizziSecondari", elencoUtilizziSecondari);
     	}
     	catch(SolmrException se) 
      {
       	String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
     	}
   	}
   	// Altrimenti l'utente sta effettuando un riepilogo ad una certa data relativa alla
   	// dichiarazione di consistenza selezionata
 		else 
    {
 			UtilizzoDichiaratoVO[] elencoUtilizziSecondariDichiarati = null;
 			String[] orderBy = new String[] {SolmrConstants.ORDER_BY_TIPO_UTILIZZO_FLAG_SAU_DESC, SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
   		// Ricerco le particelle per uso secondario ad una data dichiarazione di consistenza
   		try 
      {
   			elencoUtilizziSecondariDichiarati = anagFacadeClient.riepilogoUsoSecondarioDichiarato(anagAziendaVO.getIdAzienda(), idPianoRiferimento);
   			request.setAttribute("elencoUtilizziSecondariDichiarati", elencoUtilizziSecondariDichiarati);
   		}
   		catch(SolmrException se) 
      {
       	String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
			    <jsp:forward page="<%= riepiloghiUrl %>" />
		    <%
   		}
 		}
	}
  // Il tipo di riepilogo è per macro uso
	else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_MACRO_USO) == 0) 
  {
		UtilizzoParticellaVO[] elencoUtilizzi = null;
 		// Se l'utente ha selezionato l'anno corrente
 		if(idPianoRiferimento.longValue() == -1) 
    {
     	try 
      {
     		elencoUtilizzi = anagFacadeClient.riepilogoMacroUso(anagAziendaVO.getIdAzienda());
     		request.setAttribute("elencoUtilizzi", elencoUtilizzi);
     	}
     	catch(SolmrException se) 
      {
       	String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
     	}
   	}
   	else 
    {
     	// Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
     	UtilizzoDichiaratoVO[] elencoUtilizziDichiarati = null;
     	try 
      {
     		elencoUtilizziDichiarati = anagFacadeClient.riepilogoMacroUsoDichiarato(anagAziendaVO.getIdAzienda(), idPianoRiferimento);
     		request.setAttribute("elencoUtilizziDichiarati", elencoUtilizziDichiarati);
     	}
     	catch(SolmrException se) 
      {
       	String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
       	request.setAttribute("messaggioErrore", messaggioErrore);
       	%>					
				  <jsp:forward page="<%= riepiloghiUrl %>" />
			  <%
     	}
   	}
	}
  // Il tipo di riepilogo è per Zona Altimetrica
  else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_ZONA_ALTIMETRICA) == 0) 
  {
    StoricoParticellaVO[] elencoStorico = null;
    // Se l'utente ha selezionato l'anno corrente
    if(idPianoRiferimento.longValue() == -1) 
    {
      try 
      {
        elencoStorico = anagFacadeClient.riepilogoZonaAltimetrica(anagAziendaVO.getIdAzienda(), escludiAsservimento);
        request.setAttribute("elencoStorico", elencoStorico);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
    else 
    {
      // Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
      try 
      {
        elencoStorico = anagFacadeClient.riepilogoZonaAltimetricaParticelleDichiarate(anagAziendaVO.getIdAzienda(), escludiAsservimento, idPianoRiferimento);
        request.setAttribute("elencoStorico", elencoStorico);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
  }
  // Il tipo di riepilogo è per Caso Particolare
  else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_CASO_PARTICOLARE) == 0) 
  {
    StoricoParticellaVO[] elencoStorico = null;
    // Se l'utente ha selezionato l'anno corrente
    if(idPianoRiferimento.longValue() == -1) 
    {
      try 
      {
        elencoStorico = anagFacadeClient.riepilogoCasoParticolare(anagAziendaVO.getIdAzienda(), escludiAsservimento);
        request.setAttribute("elencoStorico", elencoStorico);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
    else 
    {
      // Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
      try 
      {
        elencoStorico = anagFacadeClient.riepilogoCasoParticolareParticelleDichiarate(anagAziendaVO.getIdAzienda(), escludiAsservimento, idPianoRiferimento);
        request.setAttribute("elencoStorico", elencoStorico);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
  }
  // Il tipo di riepilogo è tipo Efa
  else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TIPO_EFA) == 0) 
  {
    Vector<UtilizzoParticellaVO> elencoUtilizzi = null;
    // Se l'utente ha selezionato l'anno corrente
    if(idPianoRiferimento.longValue() == -1) 
    {
      try 
      {
        elencoUtilizzi = gaaFacadeClient.riepilogoTipoEfaPianoLavorazione(anagAziendaVO.getIdAzienda().longValue());
        request.setAttribute("elencoUtilizzi", elencoUtilizzi);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
    else 
    {
      // Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
      try 
      {
        elencoUtilizzi = gaaFacadeClient.riepilogoTipoEfaDichiarazione(idPianoRiferimento);
        request.setAttribute("elencoUtilizzi", elencoUtilizzi);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
  }
  else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TIPO_AREA) == 0) 
  {
    Vector<TipoAreaVO> elencoTipoArea = null;
    // Se l'utente ha selezionato l'anno corrente
    if(idPianoRiferimento.longValue() == -1) 
    {
      try 
      {
        elencoTipoArea = gaaFacadeClient.riepilogoTipoArea(anagAziendaVO.getIdAzienda().longValue());
        request.setAttribute("elencoTipoArea", elencoTipoArea);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
    else 
    {
      // Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
      try 
      {
        elencoTipoArea = gaaFacadeClient.riepilogoTipoAreaDichiarato(idPianoRiferimento);
        request.setAttribute("elencoTipoArea", elencoTipoArea);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
  }
  // Il tipo di riepilogo è tipo Greening
  else if(tipoRicerca.compareTo(SolmrConstants.RIEPILOGO_TIPO_GREENING) == 0) 
  {
    Vector<UtilizzoParticellaVO> elencoUtilizzi = null;
    // Se l'utente ha selezionato l'anno corrente
    if(idPianoRiferimento.longValue() == -1) 
    {
      try 
      {
        elencoUtilizzi = gaaFacadeClient.riepilogoTipoGreeningPianoLavorazione(anagAziendaVO.getIdAzienda().longValue());
        request.setAttribute("elencoUtilizzi", elencoUtilizzi);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
    else 
    {
      // Altrimenti prelevo i dati da DB_DICHIARAZIONE_CONSISTENZA
      try 
      {
        elencoUtilizzi = gaaFacadeClient.riepilogoTipoGreeningDichiarazione(idPianoRiferimento);
        request.setAttribute("elencoUtilizzi", elencoUtilizzi);
      }
      catch(SolmrException se) 
      {
        String messaggioErrore = AnagErrors.ERRORE_KO_RIEPILOGHI;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>          
          <jsp:forward page="<%= riepiloghiUrl %>" />
        <%
      }
    }
  }                        
 	// Vado alla pagina di di riepiloghi terreni
	%>
		<jsp:forward page="<%= riepiloghiUrl %>" />
	
