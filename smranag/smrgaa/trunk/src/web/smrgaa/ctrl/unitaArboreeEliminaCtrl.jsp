<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
	
	String iridePageName = "unitaArboreeEliminaCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
  
  SolmrLogger.debug(this, " - unitaArboreeEliminaCtrl.jsp - INIZIO PAGINA");
	
	String unitaArboreeEliminaUrl = "/view/unitaArboreeEliminaView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione cessazione Unità Arboree."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	ValidationError error = null;
	// Recupero il valore della pagina dell'elenco da cui ho cliccato il pulsante modifica multipla
	String pagina = request.getParameter("pagina");
	if(Validator.isNotEmpty(pagina)) {
		session.setAttribute("pagina", pagina);
	}
	
	HashMap<String,Vector<Long>> numUnitaArboreeSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numUnitaArboreeSelezionate");
	String[] idStoricoUnitaArborea = request.getParameterValues("idUnita");
	Vector<Long> elencoIdDaModificare = new Vector<Long>();
	
	// Controllo che siano stati selezionati degli elementi dall'elenco
	if((numUnitaArboreeSelezionate == null || numUnitaArboreeSelezionate.size() == 0) && (idStoricoUnitaArborea == null || idStoricoUnitaArborea.length == 0)) {
		error = new ValidationError(AnagErrors.ERRORE_NO_ELEMENTI_SELEZIONATI);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
	}
	
	// Gestione della selezione delle unità arboree
	Vector<Long> elenco = new Vector<Long>();
	if(numUnitaArboreeSelezionate != null && numUnitaArboreeSelezionate.size() > 0) {
		numUnitaArboreeSelezionate.remove(pagina);
		if(idStoricoUnitaArborea != null && idStoricoUnitaArborea.length > 0) {
			numUnitaArboreeSelezionate.remove(pagina);
			for(int i = 0; i < idStoricoUnitaArborea.length; i++) {
				elenco.add(Long.decode((String)idStoricoUnitaArborea[i]));
			}
		}
	}
	else 
  {
		if(numUnitaArboreeSelezionate == null) 
    {
			numUnitaArboreeSelezionate = new HashMap<String,Vector<Long>>();
		}
		// Se non ho selezionato il pulsante annulla
		if(idStoricoUnitaArborea != null && idStoricoUnitaArborea.length > 0) {
			for(int i = 0; i < idStoricoUnitaArborea.length; i++) {
				Long idElemento = Long.decode((String)idStoricoUnitaArborea[i]);
				elenco.add(idElemento);
			}
		}
	}
	if(elenco.size() > 0) {
		numUnitaArboreeSelezionate.put(pagina, elenco);
		session.setAttribute("numUnitaArboreeSelezionate", numUnitaArboreeSelezionate);
	}
	
	// Recupero il parametro che mi indica il numero massimo di record selezionabili
	String parametroRUVM = null;
	try 
  {
		parametroRUVM = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RUVM);;
	}
	catch(SolmrException se) 
  {    
    SolmrLogger.info(this, " - unitaArboreeEliminaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_RUVM+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
	
	// Verifico che non siano state selezionate più particelle rispetto a quelle consentite
	Set<String> elencoKeys = numUnitaArboreeSelezionate.keySet();
	Iterator<String> iteraKey = elencoKeys.iterator();
	while(iteraKey.hasNext()) {
		Vector<Long> selezioni = (Vector<Long>)numUnitaArboreeSelezionate.get((String)iteraKey.next());
		if(selezioni != null && selezioni.size() > 0) {
			for(int a = 0; a < selezioni.size(); a++) {
				elencoIdDaModificare.add((Long)selezioni.elementAt(a));
			}
		}
	}
	if(elencoIdDaModificare.size() > Integer.parseInt(parametroRUVM)) {
		error = new ValidationError(AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1+parametroRUVM+AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
	}
  
  // Recupero il parametro BUCV da comune
  // blocca la possibilita di cessare ai Caa le UV validate
  // se il parametro uguale a S 
  String parametroBUCV = null;
  try 
  {
    parametroBUCV = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_BUCV);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeEliminaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_BUCV+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  String parametroLockUvConsolidate = null;
  try 
  {
    parametroLockUvConsolidate = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_LOCK_UV_CONSOLIDATE);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeEliminaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_LOCK_UV_CONSOLIDATE+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
	
	// Verifico che tra le particelle selezionate dall'utente non ve ne sia nessuna
	// con data_fine_conduzione valorizzata
	StoricoParticellaVO storicoParticellaVO = null;
	StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
	StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
	boolean isErrato = false;
	int countUvValidate = 0;
	int countUvConsolidate = 0;
	if(elencoStoricoParticellaArboreaVO == null) 
  {
		try 
    {
			Vector<StoricoParticellaVO> temp = new Vector<StoricoParticellaVO>();
			Long idStorUnitaArborea = null;
			for(int i = 0; i < elencoIdDaModificare.size(); i++) 
      {
				idStorUnitaArborea = (Long)elencoIdDaModificare.elementAt(i);
				storicoParticellaVO = anagFacadeClient.findStoricoParticellaArborea(idStorUnitaArborea);
				storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
				if(storicoUnitaArboreaVO.getDataFineValidita() != null) 
        {
					isErrato = true;
					break;
				}
				
				
				
				if("S".equalsIgnoreCase(parametroLockUvConsolidate))
        {
          if(storicoUnitaArboreaVO.getDataConsolidamentoGis() != null)
          {
            countUvConsolidate++;
          }
        }
				
				
				// Se il parametro BUCV = S
				if(parametroBUCV.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
        {
					// Verifico che, se valorizzato, lo stato della/e unità arborea/e selezionate
					// non sia incompatibile con il profilo dell'utente che sta cercando di effettuare la modifica
					if(Validator.isNotEmpty(storicoUnitaArboreaVO.getStatoUnitaArborea())) 
          {
						if((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) && SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())) {
							countUvValidate++;
						}
					}
				}
				temp.add(storicoParticellaVO);
			}
			if(isErrato) {
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
				error = new ValidationError(AnagErrors.ERRORE_KO_CESSA_UNITA_ARBOREA_STORICIZZATA + messaggio);
				request.setAttribute("error", error);
				%>
					<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
				<%
			}
			// Se ho trovato UV già validate dalla PA per l'utente selezionato blocco l'operazione
      else if((countUvConsolidate > 0) && !ruoloUtenza.isUtentePA())
      {
        error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE_CONSOLIDATE);
        request.setAttribute("error", error);
        request.setAttribute("erroreModificaUv", SolmrConstants.FLAG_S);
        %>
          <jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
        <%          
      }
			// Se ho trovato UV già validate dalla PA per l'utente selezionato blocco l'operazione
			else if(countUvValidate > 0) 
      {
				error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE_VALIDATE_PA);
				request.setAttribute("error", error);
				request.setAttribute("erroreModificaUv", SolmrConstants.FLAG_S);
				%>
					<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
				<%					
			}
			elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]);
		}
		catch(SolmrException se) 
    {
			SolmrLogger.info(this, " - unitaArboreeEliminaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_SEARCH_PARTICELLE_FOR_CESSAZIONE+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
		}
	}
	session.setAttribute("elencoStoricoParticellaArboreaVO", elencoStoricoParticellaArboreaVO);
  //mi ricavo gli idParticella per vedere se sono associte pratiche all'uv
  Vector<Long> vIdParticella = null;
  if(Validator.isNotEmpty(elencoStoricoParticellaArboreaVO))
  {
    for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
    {
      if(vIdParticella == null)
      {
        vIdParticella = new Vector<Long>();
      }
      StoricoParticellaVO storicoParticellaVOTmp = elencoStoricoParticellaArboreaVO[i];
      if(!vIdParticella.contains(storicoParticellaVOTmp.getIdParticella()))
      {
        vIdParticella.add(storicoParticellaVOTmp.getIdParticella());
      }
    }
  }
  
  
  //mi ricavo gli idStoricoUnitaArborea per vedere se esiste una o piu' UV 
  // modificate/inserite dal procedimento VITI
  Vector<Long> vIdStoricoUvModVITI = null;
  if(Validator.isNotEmpty(elencoStoricoParticellaArboreaVO))
  {
    for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
    {
      StoricoUnitaArboreaVO storicoUnitaArboreaVOTmp = elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO();
      if("S".equalsIgnoreCase(storicoUnitaArboreaVOTmp.getTipoCausaleModificaVO().getAltroProcedimento()))
      {
        if(vIdStoricoUvModVITI == null)
        {
          vIdStoricoUvModVITI = new Vector<Long>();
        }
                  
        vIdStoricoUvModVITI.add(storicoUnitaArboreaVOTmp.getIdStoricoUnitaArborea());
      }
    }
  }
  
  
  
	// Passati i controlli generali recupero gli elementi da visualizzare nella view
 	// MOTIVO CESSAZIONE
	TipoCessazioneUnarVO[] elencoCessazioniUnar = null;
	try {
		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
		elencoCessazioniUnar = anagFacadeClient.getListTipoCessazioneUnar(true, orderBy);
		request.setAttribute("elencoCessazioniUnar", elencoCessazioniUnar);
	}
	catch(SolmrException se) 
  {    
    SolmrLogger.info(this, " - unitaArboreeEliminaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CAUSALE_CESSAZIONE_UNITA_ARBOREA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
 	
 	// L'utente ha selezionato il tasto conferma
 	if(request.getParameter("conferma") != null) 
  {
 		ValidationErrors errors = new ValidationErrors();
 		// Recupero i parametri
 		String cessazioneUnar = request.getParameter("idCessazionUnar");
 		String note = request.getParameter("note");
 		Long idCessazionUnar = null;
 		if(!Validator.isNotEmpty(cessazioneUnar)) 
    {
 			error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
			errors.add("idCessazionUnar", error);
 		}
 		else 
    {
 			idCessazionUnar = Long.decode(cessazioneUnar);
 			request.setAttribute("idCessazionUnar", idCessazionUnar);
 		}
 		if(Validator.isNotEmpty(note)) 
    {
 			request.setAttribute("note", note);
 			if(note.length() > 1000) 
      {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO);
 				errors.add("idCausaleModifica", error);
 			}
 		}
 		// Se si sono verificati errori li visualizzo
 		if(errors.size() > 0) 
    {
 			request.setAttribute("errors", errors);
 			%>
				<jsp:forward page="<%= unitaArboreeEliminaUrl %>" />
			<%
      return;
 		}
 		// Altrimenti procedo con la cessazione
 		else 
    {
    
    
      if(ruoloUtenza.isUtenteIntermediario())
      {
      
        // Recupero i valori relativi alla destinazione produttiva
		    TipoUtilizzoVO[] elencoTipiUsoSuolo = null;
		    try 
		    {
		      String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
		      elencoTipiUsoSuolo = anagFacadeClient.getListTipiUsoSuoloByTipo(SolmrConstants.TIPO_UTILIZZO_VIGNETO, true, orderBy);
		      request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
		    }
		    catch(SolmrException se) 
		    {
		      SolmrLogger.info(this, " - unitaArboreeEliminaCtrl.jsp - FINE PAGINA");
		      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA+".\n"+se.toString();
		      request.setAttribute("messaggioErrore",messaggio);
		      request.setAttribute("pageBack", actionUrl);
		      %>
		        <jsp:forward page="<%= erroreViewUrl %>" />
		      <%
		      return;
		    }
      
        for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
        {
          StoricoUnitaArboreaVO storicoUnitaArboreaVOTmp = elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO(); 
	        CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromPrimariKey(storicoUnitaArboreaVOTmp.getIdCatalogoMatrice());
	        if("N".equalsIgnoreCase(catalogoMatriceVO.getFlagUnarModificabile()))
          {	                
            request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_UV_NON_ELIMINABILI_CAA);
            %>
              <jsp:forward page="<%= unitaArboreeEliminaUrl %>" />
            <%
            return;     
          }
	      }
	      
      }
    
    
      //il controllo và fatto solo se il ruolo è diverso da PA
      if(!ruoloUtenza.isUtentePA())
      {
        //controllo che non ci siano pratiche uv associate alla particella
        if(Validator.isNotEmpty(vIdParticella))
        {
          long[] aIdParticella = new long[vIdParticella.size()];
          for(int i=0;i<vIdParticella.size();i++)
          {
            aIdParticella[i] = vIdParticella.get(i).longValue();
          }
          
          RitornoAgriservUvVO ritornoAgriservUvVO = gaaFacadeClient.existPraticheEstirpoUV(aIdParticella,
            anagAziendaVO.getIdAzienda(), null, null, null, 0);
          Vector<Long>  vPraticheIdParticella = ritornoAgriservUvVO.getvPraticheIdParticella();
          
          //Passando una sola particella se il vettore è valorizzato vuol dire che ad
          // essa sono associate pratiche
          if(Validator.isNotEmpty(vPraticheIdParticella))
          {
            request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_PRATICHE_ASSOCIATE_UV_MULTIPLE);
            request.setAttribute("vPraticheIdParticella",vPraticheIdParticella);
            %>
              <jsp:forward page="<%= unitaArboreeEliminaUrl %>" />
            <%
            return;     
          }
          
          //errore nella chiamata al servizio
          if(Validator.isNotEmpty(ritornoAgriservUvVO.getErrori())  && (ritornoAgriservUvVO.getErrori().length > 0))
          {
            request.setAttribute("messaggioErrore", ritornoAgriservUvVO.getErrori()[0]);
            %>
              <jsp:forward page="<%= unitaArboreeEliminaUrl %>" />
            <%
            return;         
          }
        }
        
        
        //Se il vettore è valorizzato vuol dire che ci sono UV modificate/inserite
        //dal procedimenti VITI
        if(Validator.isNotEmpty(vIdStoricoUvModVITI))
        {
          request.setAttribute("messaggioErrore", AnagErrors.ERRORE_KO_MOD_PROC_VITI_UV_MULTIPLE);
          request.setAttribute("vIdStoricoUvModVITI", vIdStoricoUvModVITI);
          %>
            <jsp:forward page="<%= unitaArboreeEliminaUrl %>" />
          <%
          return;     
        }
        
        
      }
    
    
 			try 
      {
 				anagFacadeClient.cessaUnitaArboree((Long[])elencoIdDaModificare.toArray(new Long[elencoIdDaModificare.size()]), ruoloUtenza, idCessazionUnar, note);
 			}
 			catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - unitaArboreeEliminaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CESSA_UNITA_ARBOREE+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
			}
			// Torno alla pagina di ricerca/elenco
 	 		%>
 				<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
 			<%
      return;
 		}
 	}
 	// L'utente ha selezionato il pulsante "annulla"
 	else if(request.getParameter("annulla") != null) 
  {
 		// Torno alla pagina di ricerca/elenco
 		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
    return;
 	}
 	
 	// Vado alla pagina di cessazione
 	%>
		<jsp:forward page="<%= unitaArboreeEliminaUrl %>" />
	

