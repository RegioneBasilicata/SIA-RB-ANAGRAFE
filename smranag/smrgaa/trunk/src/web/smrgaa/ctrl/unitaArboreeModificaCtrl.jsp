<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>

<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.RitornoAgriservUvVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.agriserv.praticaprocedimento.PraticaProcedimentoVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%
	
	String iridePageName = "unitaArboreeModificaCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
  
  SolmrLogger.debug(this, " - unitaArboreeModificaCtrl.jsp - INIZIO PAGINA");
	
	String unitaArboreeModificaUrl = "/view/unitaArboreeModificaView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione modifica Unità Arboree."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  
	ValidationError error = null;
	HashMap<String,Vector<Long>> numUnitaArboreeSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numUnitaArboreeSelezionate");
	String[] idStoricoUnitaArborea = request.getParameterValues("idUnita");
	// Recupero il valore della pagina dell'elenco da cui ho cliccato il pulsante modifica multipla
	String pagina = request.getParameter("pagina");
	if(Validator.isNotEmpty(pagina)) 
	{
		session.setAttribute("pagina", pagina);
	}
	Vector<Long> elencoIdDaModificare = new Vector<Long>();
	Vector<StoricoParticellaVO> temp = null;
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	// Rimuovo dalla sessione il numero dell'UV selezionata in precedenza per modificare i dati da pop-up
	session.removeAttribute("numero");
	
	// Controllo che siano stati selezionati degli elementi dall'elenco
	if((numUnitaArboreeSelezionate == null || numUnitaArboreeSelezionate.size() == 0) 
    && (idStoricoUnitaArborea == null || idStoricoUnitaArborea.length == 0)) 
  {
		error = new ValidationError(AnagErrors.ERRORE_NO_ELEMENTI_SELEZIONATI);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
	}
	
	// Gestione della selezione delle unità arboree
	Vector<Long> elenco = new Vector<Long>();
	if(numUnitaArboreeSelezionate != null && numUnitaArboreeSelezionate.size() > 0) 
  {
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
		if(numUnitaArboreeSelezionate == null) {
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
	if(elenco.size() > 0) 
  {
		numUnitaArboreeSelezionate.put(pagina, elenco);
		session.setAttribute("numUnitaArboreeSelezionate", numUnitaArboreeSelezionate);
	}
	
	
	String parametroRUVM = null;
	try 
  {
		parametroRUVM = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RUVM);;
	}
	catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_RUVM+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
  
  // Recupero il parametro che mi indica il numero massimo di record selezionabili
  String parametroVTUV = null;
  try 
  {
    parametroVTUV = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_VTUV);;
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_VTUV+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  // Recupero il parametro che mi indica l'obbligatorietà di un campo o meno
  boolean datiObbligatori=false;
  try 
  {
    String parametroAlbo = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.ID_PARAMETRO_ALBO);;
    if (SolmrConstants.FLAG_S.equals(parametroAlbo)) datiObbligatori=true;
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ALBO+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  // Recupero il parametro che mi indica l'obbligatioità di alcuni dati dell'idoneità
  String parametroDAID = null;
  try 
  {
    parametroDAID = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DAID);;
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DAID+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
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
    SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_COMPENSAZIONE_AZIENDA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  boolean flagTolleranzaCompensazione = true;
  if(Validator.isNotEmpty(compensazioneAziendaVO)
    && Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis()))
  {
    flagTolleranzaCompensazione = false;
  }*/
  
  
	
	// Verifico che non siano state selezionate più particelle rispetto a quelle consentite
	Set<String> elencoKeys = numUnitaArboreeSelezionate.keySet();
	Iterator<String> iteraKey = elencoKeys.iterator();
	while(iteraKey.hasNext()) 
  {
		Vector<Long> selezioni = (Vector<Long>)numUnitaArboreeSelezionate.get((String)iteraKey.next());
		if(selezioni != null && selezioni.size() > 0) 
		{
			for(int a = 0; a < selezioni.size(); a++) 
			{
				elencoIdDaModificare.add((Long)selezioni.elementAt(a));
			}
		}
	}
	if(elencoIdDaModificare.size() > Integer.parseInt(parametroRUVM)) 
  {
		error = new ValidationError(AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1+parametroRUVM+AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
	}
	
	// L'utente ha cliccato il pulsante annulla
 	if(request.getParameter("annulla") != null) 
  {
 		// Torno alla pagina di ricerca/elenco
 	 	String valorePagina = (String)session.getAttribute("pagina");
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" >
				<jsp:param name="pagina" value="<%= valorePagina %>" /> 
			</jsp:forward>
		<%
 	}
 	else 
  {   
    
    String parametroAccessoIdoneita = null;
    try 
    {
      parametroAccessoIdoneita = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_IDONEITA);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_IDONEITA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("parametroAccessoIdoneita", parametroAccessoIdoneita);
    
    String parametroAccessoVarArea = null;
    try 
    {
      parametroAccessoVarArea = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_VAR_AREA);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_IDONEITA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("parametroAccessoVarArea", parametroAccessoVarArea);
    
    String parametroAccessoDtSovr = null;
    try 
    {
      parametroAccessoDtSovr = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_DT_SOVR);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_DT_SOVR+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("parametroAccessoDtSovr", parametroAccessoDtSovr);
    
    
    String parametroAccessoAltriDati = null;
    try 
    {
      parametroAccessoAltriDati = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_ACCESSO_ALTRI_DATI);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_ACCESSO_ALTRI_DATI+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    request.setAttribute("parametroAccessoAltriDati", parametroAccessoAltriDati);
    
    
    // Recupero il parametro UVAG da comune
    String parametroUVAG = null;
    try 
    {
      parametroUVAG = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_UVAG);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_UVAG+".\n"+se.toString();
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
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_LOCK_UV_CONSOLIDATE+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
 				
		// Verifico che tra le unità arboree selezionate dall'utente non ve ne sia nessuna
		// con data_fine_validita valorizzata
		StoricoParticellaVO storicoParticellaVO = null;
		StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
		StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
		if(elencoStoricoParticellaArboreaVO == null) 
    {
			boolean isErrato = false;
			int countUvValidate = 0;
			int countUvConsolidate = 0;
			try 
      {
				temp = new Vector<StoricoParticellaVO>();
				Long idStorUnitaArborea = null;
				for(int i = 0; i < elencoIdDaModificare.size(); i++) 
        {
					idStorUnitaArborea = (Long)elencoIdDaModificare.elementAt(i);
					storicoParticellaVO = anagFacadeClient.findStoricoParticellaArboreaTolleranza(
            idStorUnitaArborea,anagAziendaVO.getIdAzienda().longValue(),parametroUVAG);          
					storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
					
					if("S".equalsIgnoreCase(parametroLockUvConsolidate))
					{
						if(storicoUnitaArboreaVO.getDataConsolidamentoGis() != null)
						{
						  countUvConsolidate++;
						}
				  }
					
          
          //Se Ruolo intermediario, UV validatata e Parametro accesso idoneita è S,
          //posso modificare l'uv solo se nn ha già valorizzata l'idoneità
          if((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore())
           && SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea()))
          {
            //conto le uv validate solo se posso modifcare l'idoneità per tutto
            //ma ho una idoneità valorizzata in essa
            if(parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_TUTTO))
            {
              if((storicoUnitaArboreaVO.getTipoTipologiaVinoVO() !=null) 
                  || (storicoUnitaArboreaVO.getAnnoIscrizioneAlbo() !=null)
                  || (storicoUnitaArboreaVO.getMatricolaCCIAA() != null))
              {
                
                storicoUnitaArboreaVO.setBloccaModificaIdoneitaValida(true);
                //NN + usato qui
                //countUvValidate++;
              }
            }
          }
          
					if(storicoUnitaArboreaVO.getDataFineValidita() != null 
					  || storicoUnitaArboreaVO.getDataCessazione() != null) 
					{
						isErrato = true;
						break;
					}
					
					// Verifico che, se valorizzato, lo stato della/e unità arborea/e selezionate
					// non sia incompatibile con il profilo dell'utente che sta cercando di effettuare la modifica
					if(Validator.isNotEmpty(storicoUnitaArboreaVO.getStatoUnitaArborea())) 
          {
					  if((ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) 
              && SolmrConstants.STATO_UV_VALIDATO_PA.equalsIgnoreCase(storicoUnitaArboreaVO.getStatoUnitaArborea())) 
            {
              //conto le uv validate se per tutti e quattro i parametri è possibile
              //modifcare solo i dati per lo stato nn validate
              if((parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
                   || parametroAccessoIdoneita.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)) 
		            && (parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
		               || parametroAccessoVarArea.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)) 
		            && (parametroAccessoDtSovr.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
		               || parametroAccessoDtSovr.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)) 
		            && (parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_NON_PERMESSA)
		               || parametroAccessoAltriDati.equalsIgnoreCase(SolmrConstants.MODIFICA_UV_PARAMETRO_SOLO_NON_VALIDATE)) )
		          {
						    countUvValidate++;
						  }
						}
					}
					
					storicoUnitaArboreaVO.setIdCausaleModifica(null);
					// Se il numero ceppi è vuoto o vale 0
					if(!Validator.isNotEmpty(storicoUnitaArboreaVO.getNumCeppi()) 
            || Integer.parseInt(storicoUnitaArboreaVO.getNumCeppi()) == 0) 
          {
						// E se i campi area, sesto su fila e sesto tra file sono valorizzati
						// e maggiori di 0
						if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea()) 
              && Double.parseDouble(storicoUnitaArboreaVO.getArea().replace(',', '.')) > 0 
              && Validator.isNotEmpty(storicoUnitaArboreaVO.getSestoSuFila()) 
              && Integer.parseInt(storicoUnitaArboreaVO.getSestoSuFila()) > 0 
              && Validator.isNotEmpty(storicoUnitaArboreaVO.getSestoTraFile()) 
              && Integer.parseInt(storicoUnitaArboreaVO.getSestoTraFile()) > 0) 
            {
							// Ricalcolo il numero ceppi e lo propongo di default all'utente
							double totArea = Double.parseDouble(storicoUnitaArboreaVO.getArea().replace(',', '.')) * 10000;
							double totSestoSuFila = Double.parseDouble(storicoUnitaArboreaVO.getSestoSuFila()) / 100;
							double totSestoTraFile = Double.parseDouble(storicoUnitaArboreaVO.getSestoTraFile()) / 100;
							double numeroCeppi = totArea / (totSestoSuFila * totSestoTraFile);
							storicoUnitaArboreaVO.setNumCeppi(StringUtils.parseIntegerField(String.valueOf(NumberUtils.arrotonda(numeroCeppi, 0))));
						}
					}
					temp.add(storicoParticellaVO);
				}
				if(isErrato) 
        {
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
					error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREA_STORICIZZATA + messaggio);
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
        SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_UNAR+".\n"+se.toString();
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
			// Se il parametro chiamata è valorizzato...
			if(Validator.isNotEmpty(request.getParameter("chiamata"))) 
      {
				// ... vuol dire che arrivo dalla pagina di modifica e quindi recupero
				// gli id_utilizzi primari e secondari dai quali ricalcolare la varietà
				String[] idTipoUtilizzo = request.getParameterValues("idTipoUtilizzo");
				String[] idTipoDestinazione = request.getParameterValues("idTipoDestinazione");
				String[] idTipoDettaglioUso = request.getParameterValues("idTipoDettaglioUso");
				String[] idTipoQualitaUso = request.getParameterValues("idTipoQualitaUso");
        String[] idVarieta = request.getParameterValues("idVarieta");
				int contatore = 0;
				for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
        {
					StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
					StoricoUnitaArboreaVO unitaArborea = storicoVO.getStoricoUnitaArboreaVO();
          boolean cambioDestinazione = false;
          //Long idUtilizzoOld = unitaArborea.getIdUtilizzo();
          
					if(idTipoUtilizzo != null && Validator.isNotEmpty(idTipoUtilizzo[contatore])) 
          {
						unitaArborea.setIdUtilizzo(Long.decode(idTipoUtilizzo[contatore]));
					}
					else {
						unitaArborea.setIdUtilizzo(null);
					}
					
					if(idTipoDestinazione != null && Validator.isNotEmpty(idTipoDestinazione[contatore])) 
          {
            unitaArborea.setIdTipoDestinazione(Long.decode(idTipoDestinazione[contatore]));
          }
          else {
            unitaArborea.setIdTipoDestinazione(null);
          }
          
          if(idTipoDettaglioUso != null && Validator.isNotEmpty(idTipoDettaglioUso[contatore])) 
          {
            unitaArborea.setIdTipoDettaglioUso(Long.decode(idTipoDettaglioUso[contatore]));
          }
          else {
            unitaArborea.setIdTipoDettaglioUso(null);
          }
          
          if(idTipoQualitaUso != null && Validator.isNotEmpty(idTipoQualitaUso[contatore])) 
          {
            unitaArborea.setIdTipoQualitaUso(Long.decode(idTipoQualitaUso[contatore]));
          }
          else {
            unitaArborea.setIdTipoQualitaUso(null);
          }
          
          if(idVarieta != null && Validator.isNotEmpty(idVarieta[contatore])) 
          {
            unitaArborea.setIdVarieta(Long.decode(idVarieta[contatore]));
          }
          else 
          {
            unitaArborea.setIdVarieta(null);
          }
          
          //Controllo se cambiato la destinazione produtttiva dall'opertazione precedente
          /*if((idUtilizzoOld == null) && (unitaArborea.getIdUtilizzo() != null))
          {
            cambioDestinazione = true;
          }
          else if((idUtilizzoOld != null) && (unitaArborea.getIdUtilizzo() == null))
          {
            cambioDestinazione = true;
          }
          else if(idUtilizzoOld.compareTo(unitaArborea.getIdUtilizzo()) !=0)
          {
            cambioDestinazione = true;
          }
          
          if(!cambioDestinazione)
          {
            if(varieta != null && Validator.isNotEmpty(varieta[contatore])) 
            {
              unitaArborea.setIdVarieta(Long.decode(varieta[contatore]));
            }
            else {
              unitaArborea.setIdVarieta(null);
            }
          }
          else
          {
            unitaArborea.setIdVarieta(null);
          }*/
					contatore++;
				}
			}
			else 
			{
				// TO DO...: da implementare quando sarà implementata la pop-up per la modifica degli altri
				// dati dell'unità arborea
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
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
 		}
 		
 		// Recupero il vitigno in funzione della destinazione produttiva selezionata
    Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioni = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
    try 
    {
      for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
        Vector<TipoDestinazioneVO> vTipoDestinazione = null;
        if(storico.getStoricoUnitaArboreaVO().getIdUtilizzo() != null) 
        {
          vTipoDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(storico.getStoricoUnitaArboreaVO().getIdUtilizzo());
        }
        if(vTipoDestinazione == null)
        {  
          vTipoDestinazione = new Vector<TipoDestinazioneVO>();
        }
        
        elencoDestinazioni.put(new Integer(i), vTipoDestinazione);
      }
      request.setAttribute("elencoDestinazioni", elencoDestinazioni);     
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DESTINAZIONE_PRIMARIA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettaglioUso = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
    try 
    {
      for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
        Vector<TipoDettaglioUsoVO> vTipoDettaglioUso = null;
        if((storico.getStoricoUnitaArboreaVO().getIdUtilizzo() != null)         
          && (storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione() != null)) 
        {
          vTipoDettaglioUso = gaaFacadeClient.getListDettaglioUsoByMatrice(
            storico.getStoricoUnitaArboreaVO().getIdUtilizzo(), storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione());
        }        
	      if(vTipoDettaglioUso == null)
	      {
	        vTipoDettaglioUso = new Vector<TipoDettaglioUsoVO>(); 
	      }
        
        elencoDettaglioUso.put(new Integer(i), vTipoDettaglioUso);
      }
      request.setAttribute("elencoDettaglioUso", elencoDettaglioUso);     
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETT_USO_PRIMARIA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
    try 
    {
      for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
        Vector<TipoQualitaUsoVO> vTipoQualita = null;
        if((storico.getStoricoUnitaArboreaVO().getIdUtilizzo() != null)
          && (storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione() != null)
          && (storico.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso() != null)) 
        {
          vTipoQualita = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(
            storico.getStoricoUnitaArboreaVO().getIdUtilizzo(), storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione(),
            storico.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso());
        }
        
        if(vTipoQualita == null)
        {
          vTipoQualita = new Vector<TipoQualitaUsoVO>(); 
        }
        elencoQualitaUso.put(new Integer(i), vTipoQualita);
        
      }
      request.setAttribute("elencoQualitaUso", elencoQualitaUso);     
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_QUALITA_USO_PRIMARIA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
 		
 		
	 	
		// Recupero il vitigno in funzione della destinazione produttiva selezionata
	 	Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = new Hashtable<Integer,Vector<TipoVarietaVO>>();
	 	try 
    {
	 		for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {
				StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
				Vector<TipoVarietaVO> tipoVarieta = null;
	 			if((storico.getStoricoUnitaArboreaVO().getIdUtilizzo() != null)
			    && (storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione() != null)
			    && (storico.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso() != null)
			    && (storico.getStoricoUnitaArboreaVO().getIdTipoQualitaUso() != null))  
        {
					tipoVarieta = anagFacadeClient.getListTipoVarietaVitignoByMatriceAndComune(
					  storico.getStoricoUnitaArboreaVO().getIdUtilizzo(), 
		        storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione(), 
		        storico.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso(), 
		        storico.getStoricoUnitaArboreaVO().getIdTipoQualitaUso(), 
		        storico.getIstatComune());
		        
		        if(tipoVarieta == null) 
		        {
		          tipoVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(
		            storico.getStoricoUnitaArboreaVO().getIdUtilizzo(), 
		            storico.getStoricoUnitaArboreaVO().getIdTipoDestinazione(), 
		            storico.getStoricoUnitaArboreaVO().getIdTipoDettaglioUso(), 
		            storico.getStoricoUnitaArboreaVO().getIdTipoQualitaUso());
		        } 		      
		    }
		    
				if(tipoVarieta == null) 
				{
					tipoVarieta = new Vector<TipoVarietaVO>();
				}
				
				elencoVarieta.put(new Integer(i), tipoVarieta);
	 			
			}
	 		request.setAttribute("elencoVarieta", elencoVarieta); 		
	 	}
	 	catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_VITIGNO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
		}
    
    
    // Recupero la tipologia vino in base alla varieta
    Hashtable<Integer,Vector<TipoTipologiaVinoVO>> elencoTipoTipologiaVino = new Hashtable<Integer,Vector<TipoTipologiaVinoVO>>();
    try 
    {
      int contatore = 0;
      for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
        if(storico.getStoricoUnitaArboreaVO().getIdVarieta() != null) 
        {
          Vector<TipoTipologiaVinoVO> tipoTipologiaVino = anagFacadeClient.getListActiveTipoTipologiaVinoByComuneAndVarieta(
            storico.getIstatComune(), storico.getStoricoUnitaArboreaVO().getIdVarieta(), storico.getIdParticella());
            
          if(tipoTipologiaVino == null)
          {
            tipoTipologiaVino = new Vector<TipoTipologiaVinoVO>();
          }
          elencoTipoTipologiaVino.put(new Integer(contatore), tipoTipologiaVino);
        }
        else 
        {
          Vector<TipoTipologiaVinoVO> tipoTipologiaVino = new Vector<TipoTipologiaVinoVO>();
          elencoTipoTipologiaVino.put(new Integer(contatore), tipoTipologiaVino);
        }
        contatore++;
      }
      request.setAttribute("elencoTipoTipologiaVino", elencoTipoTipologiaVino);     
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_VITIGNO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
    
    
	 	
	 	// Recupero le forme allevamento
	 	TipoFormaAllevamentoVO[] elencoFormeAllevamento = null;
	 	try 
    {
	 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
	 		elencoFormeAllevamento = anagFacadeClient.getListTipoFormaAllevamento(true, orderBy);
	 		request.setAttribute("elencoFormeAllevamento", elencoFormeAllevamento);
	 	}
	 	catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_FORME_ALLEVAMENTO+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
	 	}
	 	
	 	// Recupero le causali modifica
	 	TipoCausaleModificaVO[] elencoCausaleModifica = null;
	 	try 
	 	{
	 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
	 		elencoCausaleModifica = anagFacadeClient.getListTipoCausaleModifica(true, orderBy);
	 		request.setAttribute("elencoCausaleModifica", elencoCausaleModifica);
	 	}
	 	catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_CAUSALE_MOD_UNITA_ARBOREA+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
	 	}
    
    
    
    
    
    
		// L'utente ha modificato il valore della destinazione produttiva
	 	if(Validator.isNotEmpty(request.getParameter("cambio"))) 
    {
			// Ciclo l'array per settare i nuovi valori
			for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {
	 			StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
	 			StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
	 			// DESTINAZIONE PRODUTTIVA
	 			if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i])) {
	 				storicoUnitaArboreaConfermaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[i])); 					
	 			}
	 			else {
	 				storicoUnitaArboreaConfermaVO.setIdUtilizzo(null); 
	 			}
	 			if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")[i])) {
          storicoUnitaArboreaConfermaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazione")[i]));          
        }
        else {
          storicoUnitaArboreaConfermaVO.setIdTipoDestinazione(null); 
        }
        if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i])) {
          storicoUnitaArboreaConfermaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUso")[i]));          
        }
        else {
          storicoUnitaArboreaConfermaVO.setIdTipoDettaglioUso(null); 
        }
        if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")[i])) {
          storicoUnitaArboreaConfermaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUso")[i]));          
        }
        else {
          storicoUnitaArboreaConfermaVO.setIdTipoQualitaUso(null); 
        }
	 			// VITIGNO
	 			if(Validator.isNotEmpty(request.getParameterValues("idVarieta")[i])) {
	 				storicoUnitaArboreaConfermaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[i]));
	 			}
	 			else {
	 				storicoUnitaArboreaConfermaVO.setIdVarieta(null); 
	 			}
	 			// SUPERFICIE VITATA
	 			if(Validator.isNotEmpty(request.getParameterValues("area")[i])) {
	 				storicoUnitaArboreaConfermaVO.setArea(request.getParameterValues("area")[i]); 					
	 			}
	 			else {
	 				storicoUnitaArboreaConfermaVO.setArea(null); 
	 			}
	 			// ANNO RIFERIMENTO
	 			if(Validator.isNotEmpty(request.getParameterValues("annoRiferimento")[i])) 
        {
	 				storicoUnitaArboreaConfermaVO.setAnnoRiferimento(request.getParameterValues("annoRiferimento")[i]); 					
	 			}
	 			else 
        {
	 				storicoUnitaArboreaConfermaVO.setAnnoRiferimento(null); 
	 			}
	 			// DATA IMPIANTO
	 			if(Validator.isNotEmpty(request.getParameterValues("dataImpianto")[i])) 
        {
          try
          {
            storicoUnitaArboreaConfermaVO.setDataImpianto(DateUtils
              .parseDate(request.getParameterValues("dataImpianto")[i]));
          }
          catch(Exception ex)
          {
            storicoUnitaArboreaConfermaVO.setDataImpianto(null);
          }
	 			}
	 			else 
        {
	 				storicoUnitaArboreaConfermaVO.setDataImpianto(null); 
	 			}
        // DATA PRIMA PRODUZIONE
        if(Validator.isNotEmpty(request.getParameterValues("dataPrimaProduzione")[i])) 
        {
          try
          {
            storicoUnitaArboreaConfermaVO.setDataPrimaProduzione(DateUtils
              .parseDate(request.getParameterValues("dataPrimaProduzione")[i]));
          }
          catch(Exception ex)
          {
            storicoUnitaArboreaConfermaVO.setDataPrimaProduzione(null);
          }        
        }
        else 
        {
          storicoUnitaArboreaConfermaVO.setDataPrimaProduzione(null); 
        }
        // DATA SOVRAINNESTO
        if(Validator.isNotEmpty(request.getParameterValues("dataSovrainnesto")[i])) 
        {
          try
          {
            storicoUnitaArboreaConfermaVO.setDataSovrainnesto(DateUtils
              .parseDate(request.getParameterValues("dataSovrainnesto")[i]));
          }
          catch(Exception ex)
          {
            storicoUnitaArboreaConfermaVO.setDataSovrainnesto(null);
          }        
        }
        else 
        {
          storicoUnitaArboreaConfermaVO.setDataSovrainnesto(null); 
        }
	 			// SESTO SU FILA
	 			if(Validator.isNotEmpty(request.getParameterValues("sestoSuFila")[i])) 
	 			{
	 				storicoUnitaArboreaConfermaVO.setSestoSuFila(request.getParameterValues("sestoSuFila")[i]); 					
	 			}
	 			else 
	 			{
	 				storicoUnitaArboreaConfermaVO.setSestoSuFila(null); 
	 			}
				// SESTO TRA FILE
	 			if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) 
	 			{
	 				storicoUnitaArboreaConfermaVO.setSestoTraFile(request.getParameterValues("sestoTraFile")[i]); 					
	 			}
	 			else 
	 			{
	 				storicoUnitaArboreaConfermaVO.setSestoTraFile(null); 
	 			}
				// NUMERO CEPPI
				if(Validator.isNotEmpty(request.getParameterValues("numeroCeppi")[i])) 
				{
	 				storicoUnitaArboreaConfermaVO.setNumCeppi(request.getParameterValues("numeroCeppi")[i]); 					
	 			}
	 			else 
	 			{
	 				storicoUnitaArboreaConfermaVO.setNumCeppi(null); 
	 			}
	 			
	 			// PERCENTUALE FALLANZA
        if(Validator.isNumberPercentuale(request.getParameterValues("percentualeFallanza")[i])) 
        {
          storicoUnitaArboreaConfermaVO.setPercentualeFallanza(new BigDecimal(request.getParameterValues("percentualeFallanza")[i]));          
        }
        else 
        {
          storicoUnitaArboreaConfermaVO.setPercentualeFallanza(new BigDecimal(0)); 
        }
        
        // FLAG IMPRODUTTIVA
        storicoUnitaArboreaConfermaVO.setFlagImproduttiva(request.getParameter("flagImproduttiva"+String.valueOf(i)));
        
        
        
        //Tipologia di vino
        String idTipologiaVino=request.getParameterValues("idTipologiaVino")[i];
        if(Validator.isNotEmpty(idTipologiaVino))
        {
          if(storicoParticellaConfermaVO.getIstatComune() != null)
          {
            Long idTipologiaVinoLg = new Long(idTipologiaVino);
            storicoUnitaArboreaConfermaVO.setIdTipologiaVino(idTipologiaVinoLg);
            Vector<TipoTipologiaVinoVO> vTipoTipologiaVino = anagFacadeClient.getListActiveTipoTipologiaVinoByComune(storicoParticellaConfermaVO.getIstatComune());
            //Tipologia di vino
            if(vTipoTipologiaVino != null)
            { 
              for(TipoTipologiaVinoVO tipoTipologiaVinoVO:vTipoTipologiaVino)
              {
                if(idTipologiaVinoLg.compareTo(tipoTipologiaVinoVO.getIdTipologiaVino()) == 0)
                {
                  storicoUnitaArboreaConfermaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
                  break;
                }   
              }
            }
          }
        }
        else
        {
          storicoUnitaArboreaConfermaVO.setIdTipologiaVino(null);
          storicoUnitaArboreaConfermaVO.setTipoTipologiaVinoVO(null);
        }
        
        //Anno iscrizione albo
        if(Validator.isNotEmpty(request.getParameterValues("annoIscrizioneAlbo")[i]))
        {
          storicoUnitaArboreaConfermaVO.setAnnoIscrizioneAlbo(request.getParameterValues("annoIscrizioneAlbo")[i]);
        }          
        else
        {
          storicoUnitaArboreaConfermaVO.setAnnoIscrizioneAlbo(null);
        } 
        
        //Matricola
        if(Validator.isNotEmpty(request.getParameterValues("matricolaCCIAA")[i]))
        {
          storicoUnitaArboreaConfermaVO.setMatricolaCCIAA(request.getParameterValues("matricolaCCIAA")[i].trim());
        }          
        else
        { 
          storicoUnitaArboreaConfermaVO.setMatricolaCCIAA(null);
        } 
        
        
        
        
				// FORMA ALLEVAMENTO
				if(Validator.isNotEmpty(request.getParameterValues("idFormaAllevamento")[i])) 
				{
	 				storicoUnitaArboreaConfermaVO.setIdFormaAllevamento(Long.decode(request.getParameterValues("idFormaAllevamento")[i])); 					
	 			}
	 			else 
	 			{
	 				storicoUnitaArboreaConfermaVO.setIdFormaAllevamento(null); 
	 			}
				// PERCENTUALE VITIGNO
				if(Validator.isNotEmpty(request.getParameterValues("percentualeVitigno")[i])) 
				{
	 				storicoUnitaArboreaConfermaVO.setPercentualeVarieta(request.getParameterValues("percentualeVitigno")[i]); 					
	 			}
	 			else 
	 			{
	 				storicoUnitaArboreaConfermaVO.setPercentualeVarieta(null); 
	 			}
				// ALTRI VITIGNI
				storicoUnitaArboreaConfermaVO.setPresenzaAltriVitigni(request.getParameter("altroVitigno"+String.valueOf(i)));
				// CAUSALE MODIFICA
				if(Validator.isNotEmpty(request.getParameterValues("idCausaleModifica")[i])) 
				{
	 				storicoUnitaArboreaConfermaVO.setIdCausaleModifica(Long.decode(request.getParameterValues("idCausaleModifica")[i])); 					
	 				request.setAttribute("idCausaleModifica", Long.decode(request.getParameterValues("idCausaleModifica")[i]));
	 			}
	 			else 
	 			{
	 				storicoUnitaArboreaConfermaVO.setIdCausaleModifica(null); 
	 			}
	 		}
	 	}
	 	// L'utente ha cliccato il pulsante conferma
	 	else if((request.getParameter("conferma") != null)
	 	 ||  (request.getParameter("confermaPA") != null))
    {
      //se settato anche se trattasi di PA non deve essere fatta la validazione
      String provenienza = null;
      if(request.getParameter("confermaPA") != null)
      {
        provenienza = SolmrConstants.CONFERMA_PA;
      } 
    
	 		Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
	 		ValidationErrors errors = null;
	 		int countErrori = 0;
	 		Hashtable<String,ValidationErrors> elencoErroriAltriVitigni = new Hashtable<String,ValidationErrors>();
	 		// Ciclo sulle unità arboree selezionate
	 		for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {      
	 			StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
	 			StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
	 			
        //La provinciaCCIAA deve essere sempre valorizzata quindi se è null devo prenderla dal comune
        //della particella        
        if(Validator.isEmpty(storicoUnitaArboreaConfermaVO.getProvinciaCCIAA()))
        {
          if(storicoParticellaConfermaVO.getComuneParticellaVO() != null)
          { 
            storicoUnitaArboreaConfermaVO.setProvinciaCCIAA(
              storicoParticellaConfermaVO.getComuneParticellaVO().getSiglaProv());
          }
        } 
        
        
        errors = new ValidationErrors();
	 			int totPercentualeAltriVitigni = 0;
	 			// Setto e verifico tutti i parametri inseriti:
	 			// DESTINAZIONE PRODUTTIVA
        if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")))
        {
  	 			if(Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[i])) 
          {
  	 				storicoUnitaArboreaConfermaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[i]));
  	 				
  	 				boolean errFlagUnar = false;
  	 				if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")[i]) && Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i])
  	 				  && Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")[i]) && Validator.isNotEmpty(request.getParameterValues("idVarieta")[i]))
  	 				{ 
  	 				
  	 				  CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(storicoUnitaArboreaConfermaVO.getIdUtilizzo(),
                new Long(request.getParameterValues("idVarieta")[i]), new Long(request.getParameterValues("idTipoDestinazione")[i]), 
                new Long(request.getParameterValues("idTipoDettaglioUso")[i]), 
                new Long(request.getParameterValues("idTipoQualitaUso")[i]));
                				
	  	 				if(ruoloUtenza.isUtenteIntermediario() && Validator.isNotEmpty(catalogoMatriceVO))
				      {
				        if("N".equalsIgnoreCase(catalogoMatriceVO.getFlagUnarModificabile()))
				        {
				          errFlagUnar = true;
				          errors.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_DEST_PROD_CAA));
				          countErrori++;
				        }
				      }
				    }
  	 				
  	 				
  	 				// La destinazione produttiva del vitigno principale deve essere la stessa degli altri vitigni associati
  	 				if(!errFlagUnar && storicoUnitaArboreaConfermaVO.getElencoAltriVitigni() != null 
  	 				  && storicoUnitaArboreaConfermaVO.getElencoAltriVitigni().length > 0) 
            {
  						AltroVitignoVO[] elencoAltriVitigno = storicoUnitaArboreaConfermaVO.getElencoAltriVitigni();
  						for(int a = 0; a < elencoAltriVitigno.length; a++) 
              {
  							AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAltriVitigno[a];
                if(altroVitignoVO.getTipoVarietaVO() != null)
                {
    							if(storicoUnitaArboreaConfermaVO.getIdUtilizzo().compareTo(altroVitignoVO.getTipoVarietaVO().getIdUtilizzo()) != 0) 
                  {
    								errors.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_ID_UTILIZZO_UV_SU_ALTRI_VIGNETI));
    				 				countErrori++;
    							}
                }
                
                if(altroVitignoVO.getPercentualeVitigno() != null)
                {
                  totPercentualeAltriVitigni += Integer.parseInt(altroVitignoVO.getPercentualeVitigno());
                }
  							
  						}
  	 				}
  	 			}
  	 			else 
  	 			{
  	 				storicoUnitaArboreaConfermaVO.setIdUtilizzo(null); 
  	 				errors.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
  	 				countErrori++;
  	 			}
        }
        // Destinazione
        if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")))
        {
          if(Validator.isNotEmpty(request.getParameterValues("idTipoDestinazione")[i])) 
          {
            storicoUnitaArboreaConfermaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazione")[i]));            
          }
          else 
          {
            storicoUnitaArboreaConfermaVO.setIdTipoDestinazione(null); 
            errors.add("idTipoDestinazione", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
        }
        // Destinazione
        if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")))
        {
          if(Validator.isNotEmpty(request.getParameterValues("idTipoDettaglioUso")[i])) 
          {
            storicoUnitaArboreaConfermaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUso")[i]));            
          }
          else 
          {
            storicoUnitaArboreaConfermaVO.setIdTipoDettaglioUso(null);
            errors.add("idTipoDettaglioUso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
        }
        // Destinazione
        if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")))
        {
          if(Validator.isNotEmpty(request.getParameterValues("idTipoQualitaUso")[i])) 
          {
            storicoUnitaArboreaConfermaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUso")[i]));            
          }
          else 
          {
            storicoUnitaArboreaConfermaVO.setIdTipoQualitaUso(null);
            errors.add("idTipoQualitaUso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
        }
	 			// VITIGNO
        if(Validator.isNotEmpty(request.getParameterValues("idVarieta")))
        {
  	 			if(Validator.isNotEmpty(request.getParameterValues("idVarieta")[i])) 
          {
  	 				storicoUnitaArboreaConfermaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[i]));
            if(storicoUnitaArboreaConfermaVO.getElencoAltriVitigni() != null && storicoUnitaArboreaConfermaVO.getElencoAltriVitigni().length > 0) 
            {
              AltroVitignoVO[] elencoAltriVitigno = storicoUnitaArboreaConfermaVO.getElencoAltriVitigni();
              for(int a = 0; a < elencoAltriVitigno.length; a++) 
              {
                AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAltriVitigno[a];
                
                if(Validator.isNotEmpty(storicoUnitaArboreaConfermaVO.getIdVarieta()) && Validator.isNotEmpty(altroVitignoVO.getIdVarieta()))
                {
                  if(storicoUnitaArboreaConfermaVO.getIdVarieta().compareTo(altroVitignoVO.getIdVarieta()) == 0) 
                  {
                    errors.add("idVarieta", new ValidationError(AnagErrors.ERRORE_VITIGNO_PRINCIPALE_UGUALE_ALTRO_VITIGNO_VARIETA));
                    countErrori++;
                  }
                }
              }
            }
            
  	 			}
  	 			else 
  	 			{
  	 				storicoUnitaArboreaConfermaVO.setIdVarieta(null); 
  	 				errors.add("idVarieta", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
  	 				countErrori++;
  	 			}
        }
        
	 			// SUPERFICIE VITATA
        if(Validator.isNotEmpty(request.getParameterValues("area"))) 
        {
  	 			if(Validator.isNotEmpty(request.getParameterValues("area")[i])) 
          {
  	 				storicoUnitaArboreaConfermaVO.setArea(request.getParameterValues("area")[i]); 					
  	 				if(Validator.validateDouble(storicoUnitaArboreaConfermaVO.getArea(), 999999.9999) == null) 
  	 				{
  	 					errors.add("area", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  	 				else 
            {
  	 					storicoUnitaArboreaConfermaVO.setArea(StringUtils.parseSuperficieField(request.getParameterValues("area")[i]));
  	 					//boolean flagTolleranza = false;
              
              //Il controllo sulla tolleranza è fatto solo per utenti diversi da PA
              /*if(!ruoloUtenza.isUtentePA())
              {
                //vado avanti solo se parametro uguale a S e se non è stato fato ancora il consolidamento
                if("S".equalsIgnoreCase(parametroVTUV) && flagTolleranzaCompensazione)
                {
                  Integer tollerenza = gaaFacadeClient.getTolleranzaPlSql(parametroUVAG,
                    anagAziendaVO.getIdAzienda().longValue(),
                    storicoUnitaArboreaConfermaVO.getIdUnitaArborea());
                    
                  if(tollerenza != null)
                  {
                    StoricoUnitaArboreaVO storicoTollVO = anagFacadeClient.findStoricoUnitaArborea(
                      storicoUnitaArboreaConfermaVO.getIdStoricoUnitaArborea());
                    String oldArea = storicoTollVO.getArea();
                    String newArea = storicoUnitaArboreaConfermaVO.getArea();                  
                    if(tollerenza.compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0)
                    {
                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                      {
                        flagTolleranza = true;
                        errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_FUORI_TOLLERANZA));
                        countErrori++;
                      
                      } 
                    }
                    else if(tollerenza.compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0)
                    {
                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                      {
                        flagTolleranza = true;
                        errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_UVDOPPIE));
                        countErrori++;                    
                      }
                    
                    }
                    else if(tollerenza.compareTo(SolmrConstants.NO_PARCELLE_TOLLERANZA) == 0)
                    {
                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                      {
                        flagTolleranza = true;
                        errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_NO_PARCELLE));
                        countErrori++;                    
                      }                  
                    }
                    else if(tollerenza.compareTo(SolmrConstants.ERR_PL_TOLLERANZA) == 0)
                    {
                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                      {
                        flagTolleranza = true;
                        errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_ERR_PL));
                        countErrori++;                    
                      }
                    
                    }
                    else if(tollerenza.compareTo(SolmrConstants.UV_NON_PRESENTE) == 0)
                    {
                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                      {
                        flagTolleranza = true;
                        errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_NON_PRESENTE));
                        countErrori++;                    
                      }
                    
                    }
                    else if(tollerenza.compareTo(SolmrConstants.PARTICELLA_ORFANA) == 0)
                    {
                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                      {
                        flagTolleranza = true;
                        errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_PARTICELLA_ORFANA));
                        countErrori++;                    
                      }
                    
                    }
                    else if(tollerenza.compareTo(SolmrConstants.PARCELLE_NO_VITE) == 0)
                    {
                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                      {
                        flagTolleranza = true;
                        errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_PARCELLE_NO_VITE));
                        countErrori++;                    
                      }
                    
                    }
                    else if(tollerenza.compareTo(SolmrConstants.UV_PIU_OCCORR_ATTIVE) == 0)
                    {
                      if(NumberUtils.arrotonda(Double.parseDouble(newArea.replace(',', '.')), 4) 
                        > NumberUtils.arrotonda(Double.parseDouble(oldArea.replace(',', '.')), 4))
                      {
                        flagTolleranza = true;
                        errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_TOLLERANZA_UV_PIU_OCCORR_ATTIVE));
                        countErrori++;                    
                      }
                    
                    }
                  }
                                
                }
              } */
              
              
              
              // La superficie vitata non può essere maggiore della superficie catastale
              //controllo solo se non ci sono già stati problemi per la tolleranza
              //if(!flagTolleranza)
              //{
              String supConfronto = AnagUtils.valSupCatGraf(
                storicoParticellaConfermaVO.getSupCatastale(), storicoParticellaConfermaVO.getSuperficieGrafica());
            
            
 					    if(NumberUtils.arrotonda(Double.parseDouble(storicoUnitaArboreaConfermaVO.getArea().replace(',', '.')), 4) 
                > NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',', '.')), 4)) 
              {
 						    errors.add("area", new ValidationError(AnagErrors.ERRORE_KO_SUP_VITATA_MAGGIORE_SUP_GRAFICA_CAT));
 						    countErrori++;
 					    }
 					  }
            
  	 			}
  	 			else 
  	 			{
  	 			  storicoUnitaArboreaConfermaVO.setArea(null); 
  	 			  errors.add("area", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
  	 				countErrori++;
  	 			}
        }
	 			// data impianto
	 			boolean isOkImpianto = false;
        if(Validator.isNotEmpty(request.getParameterValues("dataImpianto"))) 
        {
  	 			if(Validator.isNotEmpty(request.getParameterValues("dataImpianto")[i])) 
          {
  	 				if(!Validator.validateDateF(request.getParameterValues("dataImpianto")[i])) 
            {
  	 					errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  	 				else 
            {
              storicoUnitaArboreaConfermaVO.setDataImpianto(
                DateUtils.parseDate(request.getParameterValues("dataImpianto")[i]));
              storicoUnitaArboreaConfermaVO.setAnnoImpianto(new Integer(DateUtils
                  .extractYearFromDate(storicoUnitaArboreaConfermaVO.getDataImpianto())).toString());
  	 					// La data impianto non può essere maggiore della data di sistema
  	 					if(storicoUnitaArboreaConfermaVO.getDataImpianto().after(new Date())) 
              {
  	 						errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_MAGGIORE_DATA_CORRENTE));
  	 						countErrori++;
  	 					}
  	 					// L'anno impianto non deve essere minore del 1900
  	 					else if(storicoUnitaArboreaConfermaVO.getDataImpianto().before(DateUtils.parseDate("31/12/1900"))) 
              {
  	 						errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_KO_DATA_IMPIANTO_UV_MINORE_1900));
  		 					countErrori++;
  	 					}
  	 					else 
              {
  	 						isOkImpianto = true;
  	 					}
  	 				}
  	 			}
  	 			else 
          {
            storicoUnitaArboreaConfermaVO.setDataImpianto(null); 
  	 				storicoUnitaArboreaConfermaVO.setAnnoImpianto(null); 
  	 				errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
  	 				countErrori++;
  	 			}
        }
        else 
        {
          storicoUnitaArboreaConfermaVO.setDataImpianto(null); 
          storicoUnitaArboreaConfermaVO.setAnnoImpianto(null); 
          errors.add("dataImpianto", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        // data sovrainnesto
        boolean isOkSovrainnesto = false;
        if(Validator.isNotEmpty(request.getParameterValues("dataSovrainnesto"))) 
        {
          if(Validator.isNotEmpty(request.getParameterValues("dataSovrainnesto")[i])) 
          {
            if(!Validator.validateDateF(request.getParameterValues("dataSovrainnesto")[i])) 
            {
              errors.add("dataSovrainnesto", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
              countErrori++;
            }
            else 
            {
              // La data prima produzione non deve essere minore del 1900
              storicoUnitaArboreaConfermaVO.setDataSovrainnesto(
                DateUtils.parseDate(request.getParameterValues("dataSovrainnesto")[i]));
              // La data sovrainnesto non può essere maggiore della data di sistema
              if(storicoUnitaArboreaConfermaVO.getDataSovrainnesto().after(new Date())) 
              {
                errors.add("dataSovrainnesto", new ValidationError(AnagErrors.ERRORE_KO_DATA_SOVRAINNESTO_UV_MAGGIORE_DATA_CORRENTE));
                countErrori++;
              }
              else if(storicoUnitaArboreaConfermaVO.getDataSovrainnesto().before(DateUtils.parseDate("31/12/1900")))  
              {
                errors.add("dataSovrainnesto", new ValidationError(AnagErrors.ERRORE_KO_DATA_SOVRAINNESTO_UV_MINORE_1900));
                countErrori++;
              }
              else
              {
                isOkSovrainnesto = true;
              }
              
              // La data sisovrainnesto non può essere minore dell'anno impiano
              if(isOkImpianto) 
              {
                if(storicoUnitaArboreaConfermaVO.getDataSovrainnesto().before(
                  storicoUnitaArboreaConfermaVO.getDataImpianto()))  
                {
                  errors.add("dataSovrainnesto", new ValidationError(AnagErrors.ERRORE_KO_DATA_SOVRAINNESTO_UV_MINORE_DT_IMPIANTO));
                  isOkSovrainnesto = false;
                  countErrori++;
                }               
              }
              
            }
          }
          else 
          {
            storicoUnitaArboreaConfermaVO.setDataSovrainnesto(null);
          }
        }
        // data Prima produzione
        if(Validator.isNotEmpty(request.getParameterValues("dataPrimaProduzione"))) 
        {
          if(Validator.isNotEmpty(request.getParameterValues("dataPrimaProduzione")[i])) 
          {
            if(!Validator.validateDateF(request.getParameterValues("dataPrimaProduzione")[i])) 
            {
              errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
              countErrori++;
            }
            else 
            {
              // La data prima produzione non deve essere minore del 1900
              storicoUnitaArboreaConfermaVO.setDataPrimaProduzione(
                DateUtils.parseDate(request.getParameterValues("dataPrimaProduzione")[i]));
              storicoUnitaArboreaConfermaVO.setAnnoPrimaProduzione(new Integer(DateUtils
                  .extractYearFromDate(storicoUnitaArboreaConfermaVO.getDataPrimaProduzione())).toString());
              if(storicoUnitaArboreaConfermaVO.getDataPrimaProduzione().before(DateUtils.parseDate("31/12/1900")))  
              {
                errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_KO_DATA_PRIMA_PRODUZIONE_UV_MINORE_1900));
                countErrori++;
              }
              
              // La data prima produzione non può essere minore dell'anno impiano piu il parametro APUV
              if(isOkImpianto) 
              {
                int numConfronto = 2; 
                //int yearDataImpianto =  DateUtils.extractYearFromDate(storicoUnitaArboreaConfermaVO.getDataImpianto());
                //int monthDataImpianto =  DateUtils.extractMonthFromDate(storicoUnitaArboreaConfermaVO.getDataImpianto());
                Date dataConfronto = storicoUnitaArboreaConfermaVO.getDataImpianto();
                //sa valorizzata o corretta devo usare la data di sovrainnesto pe ril confornto con la data priam produzione
                if(isOkSovrainnesto)
                {
                  numConfronto = 1;
                  dataConfronto = storicoUnitaArboreaConfermaVO.getDataSovrainnesto();
                }
                int yearDataConfronto =  DateUtils.extractYearFromDate(dataConfronto);
                int monthDataConfronto =  DateUtils.extractMonthFromDate(dataConfronto);
                int annoPrimaProduzioneInt = DateUtils.extractYearFromDate(storicoUnitaArboreaConfermaVO.getDataPrimaProduzione());
                
                //prima del 31/07
                if(monthDataConfronto <= 7)
                {
                  if(annoPrimaProduzioneInt != yearDataConfronto + numConfronto)
                  {
                    errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_ANNO_PRIMA_PROD+" "+(yearDataConfronto + numConfronto)));
                    countErrori++;
                  }
                }
                //dopo il 31/07
                else
                {
                  if(annoPrimaProduzioneInt != yearDataConfronto + (numConfronto+1))
                  {
                    errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_ANNO_PRIMA_PROD+" "+(yearDataConfronto + (numConfronto+1))));
                    countErrori++;
                  }
                }
                
              }
              
            }
          }
          else 
          {
            storicoUnitaArboreaConfermaVO.setDataPrimaProduzione(null);
            storicoUnitaArboreaConfermaVO.setAnnoPrimaProduzione(null); 
            errors.add("dataPrimaProduzione", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
            countErrori++;
          }
        }
	 			// ANNO RIFERIMENTO
        if(Validator.isNotEmpty(request.getParameterValues("annoRiferimento"))) 
        {
  	 			if(Validator.isNotEmpty(request.getParameterValues("annoRiferimento")[i])) 
          {
  	 				storicoUnitaArboreaConfermaVO.setAnnoRiferimento(request.getParameterValues("annoRiferimento")[i]); 					
  	 				if(!Validator.isNumericInteger(storicoUnitaArboreaConfermaVO.getAnnoRiferimento())) {
  	 					errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  	 				else {
  	 					// L'anno riferimento non può essere maggiore dell'anno di sistema
  	 					if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getAnnoRiferimento()) > DateUtils.getCurrentYear().intValue()) {
  	 						errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_KO_ANNO_RIFERIMENTO_UV_MAGGIORE_ANNO_CORRENTE));
  	 						countErrori++;
  	 					}
  	 					// L'anno riferimento non deve essere minore del 1900
  	 					else if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getAnnoRiferimento()) < 1900) {
  	 						errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  		 					countErrori++;
  	 					}
  	 					// L'anno di riferimento non può essere minore dell'anno di impianto
  	 					if(isOkImpianto) {
  	 						if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getAnnoImpianto()) > Integer.parseInt(storicoUnitaArboreaConfermaVO.getAnnoRiferimento())) {
  	 							errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_KO_ANNO_RIFERIMENTO_UV_MINORE_ANNO_IMPIANTO));
  	 							countErrori++;
  	 						}
  	 					}
  	 				}
  	 			}
  	 			else {
  	 				storicoUnitaArboreaConfermaVO.setAnnoRiferimento(null); 
  	 				errors.add("annoRiferimento", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
  	 				countErrori++;
  	 			}
        }
	 			// SESTO SU FILA
        if(Validator.isNotEmpty(request.getParameterValues("sestoSuFila"))) 
        {
  	 			if(Validator.isNotEmpty(request.getParameterValues("sestoSuFila")[i])) 
          {
  	 				storicoUnitaArboreaConfermaVO.setSestoSuFila(request.getParameterValues("sestoSuFila")[i]); 					
  	 				if(!Validator.isNumericInteger(storicoUnitaArboreaConfermaVO.getSestoSuFila())) {
  	 					errors.add("sestoSuFila", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  	 				// Deve essere maggiore di 0
  	 				else if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getSestoSuFila()) <= 0) {
  	 					errors.add("sestoSuFila", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  	 			}
  	 			// Commento l'obbligatorietà del sesto su fila: controllo momentaneamente
  	 			// sospeso su richiesta del dominio
  	 			else 
  	 			{
  	 				storicoUnitaArboreaConfermaVO.setSestoSuFila(null);
  	 			}
        }
				// SESTO TRA FILE
        if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile"))) 
        {
  	 			if(Validator.isNotEmpty(request.getParameterValues("sestoTraFile")[i])) 
  	 			{
  	 				storicoUnitaArboreaConfermaVO.setSestoTraFile(request.getParameterValues("sestoTraFile")[i]); 					
  	 				if(!Validator.isNumericInteger(storicoUnitaArboreaConfermaVO.getSestoTraFile())) 
  	 				{
  	 					errors.add("sestoTraFile", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  					// Deve essere maggiore di 0
  	 				else if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getSestoTraFile()) <= 0) 
  	 				{
  	 					errors.add("sestoTraFile", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  	 			}
  	 			// Commento l'obbligatorietà del sesto tra file: controllo momentaneamente
  	 			// sospeso su richiesta del dominio
  	 			else 
  	 			{
  	 				storicoUnitaArboreaConfermaVO.setSestoTraFile(null);
  	 			}
        }
				// NUMERO CEPPI
        if(Validator.isNotEmpty(request.getParameterValues("numeroCeppi"))) 
        {
  				if(Validator.isNotEmpty(request.getParameterValues("numeroCeppi")[i])) 
  				{
  	 				storicoUnitaArboreaConfermaVO.setNumCeppi(request.getParameterValues("numeroCeppi")[i]); 					
  	 				// Dal momento che viene calcolato automaticamente, verifico che abbia dimensione > di 6
  	 				if(storicoUnitaArboreaConfermaVO.getNumCeppi().length() > 6) 
  	 				{
  	 					errors.add("numeroCeppi", new ValidationError(AnagErrors.ERRORE_KO_NUMERO_CEPPI));
  	 				}
  	 				else if(!Validator.isNumericInteger(storicoUnitaArboreaConfermaVO.getNumCeppi())) 
  	 				{
  	 					errors.add("numeroCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  					// Deve essere maggiore di 0
  	 				else if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getNumCeppi()) <= 0) 
  	 				{
  	 					errors.add("numeroCeppi", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  	 			}
  				// Commento l'obbligatorietà del numero ceppi: controllo momentaneamente
  	 			// sospeso su richiesta del dominio
  	 			else 
  	 			{
  	 				storicoUnitaArboreaConfermaVO.setNumCeppi(null);
  	 			}
        }
        
        // PERCENTUALE FALLANZA
		    if(Validator.isNotEmpty(request.getParameterValues("percentualeFallanza"))
		      && Validator.isNotEmpty(request.getParameterValues("percentualeFallanza")[i])) 
		    {  
		      if(!Validator.isNumberPercentuale(request.getParameterValues("percentualeFallanza")[i])) 
		      {
		        errors.add("percentualeFallanza", new ValidationError(AnagErrors.ERRORE_KO_PERCENTUALE_FALLANZA));
		        countErrori++;
		      }
		      else 
		      {
		        storicoUnitaArboreaConfermaVO.setPercentualeFallanza(new BigDecimal(request.getParameterValues("percentualeFallanza")[i]));
		      }
		    }
		    else 
		    {
		      storicoUnitaArboreaConfermaVO.setPercentualeFallanza(new BigDecimal(0)); 
		      errors.add("percentualeFallanza", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		      countErrori++;
		    }
		    
		    // FLAG IMPRODUTTIVA
        if(Validator.isNotEmpty(request.getParameter("flagImproduttiva"+String.valueOf(i))))
        {
          storicoUnitaArboreaConfermaVO.setFlagImproduttiva(request.getParameter("flagImproduttiva"+String.valueOf(i)));
        }
        
        
        
        
        //IDONEITA'
        
        //Tipologia di vino         
        String idTipologiaVino = null;
        if(Validator.isNotEmpty(request.getParameterValues("idTipologiaVino")))
        {
          idTipologiaVino=request.getParameterValues("idTipologiaVino")[i];
        }
        
        String vinoDoc = null;
        if(Validator.isNotEmpty(idTipologiaVino))
        {
          if((storicoParticellaConfermaVO.getIstatComune() != null)
            && (storicoUnitaArboreaConfermaVO.getIdVarieta() != null))
          {
            Long idTipologiaVinoLg = new Long(idTipologiaVino);
            storicoUnitaArboreaConfermaVO.setIdTipologiaVino(idTipologiaVinoLg);
            Vector<TipoTipologiaVinoVO> vTipoTipologiaVino = anagFacadeClient
              .getListActiveTipoTipologiaVinoByComuneAndVarieta(storicoParticellaConfermaVO.getIstatComune(), 
              storicoUnitaArboreaConfermaVO.getIdVarieta(), storicoParticellaConfermaVO.getIdParticella());
            //Tipologia di vino
            if(vTipoTipologiaVino != null)
            { 
              for(TipoTipologiaVinoVO tipoTipologiaVinoVO:vTipoTipologiaVino)
              {
                if(idTipologiaVinoLg.compareTo(tipoTipologiaVinoVO.getIdTipologiaVino()) == 0)
                {
                  vinoDoc=tipoTipologiaVinoVO.getVinoDoc();
                  storicoUnitaArboreaConfermaVO.setTipoTipologiaVinoVO(tipoTipologiaVinoVO);
                  if("N".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneVigna()))
                  {
                    storicoUnitaArboreaConfermaVO.setVigna(null);
                  }
                  if("N".equalsIgnoreCase(tipoTipologiaVinoVO.getFlagGestioneEtichetta()))
                  {
                    storicoUnitaArboreaConfermaVO.setEtichetta(null);
                  }
                  
                  if(Validator.isNotEmpty(tipoTipologiaVinoVO.getVignaVO()))
                  {
                    storicoUnitaArboreaConfermaVO.setIdVigna(tipoTipologiaVinoVO.getVignaVO().getIdVigna());
                  }
                  
                  break;
                }   
              }
            }
            else
            {
              errors = ErrorUtils.setValidErrNoNull(errors, "idTipologiaVino", AnagErrors.ERR_TIPOLOGIAVINO);
              countErrori++;
            }
          }
          else
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "idTipologiaVino", AnagErrors.ERR_TIPOLOGIAVINO_NO_VITIGNO);
            countErrori++;
          }
        }
        else
        {
          storicoUnitaArboreaConfermaVO.setIdTipologiaVino(null);
          storicoUnitaArboreaConfermaVO.setTipoTipologiaVinoVO(null);
        }     
         
        storicoUnitaArboreaConfermaVO.setSuperficieDaIscrivereAlbo(storicoUnitaArboreaConfermaVO.getArea()); 
        
        //Matricola
        String matricolaCCIAA = null;
        if(Validator.isNotEmpty(request.getParameterValues("matricolaCCIAA")))
        {
          matricolaCCIAA = request.getParameterValues("matricolaCCIAA")[i];
        }
        
        if(Validator.isNotEmpty(matricolaCCIAA))
        {
          storicoUnitaArboreaConfermaVO.setMatricolaCCIAA(matricolaCCIAA.trim());          
          if(matricolaCCIAA.trim().length() > 15)
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "matricolaCCIAA", AnagErrors.ERR_MATRICOLA_CCIAA);
            countErrori++;
          }
        }  
        else 
          storicoUnitaArboreaConfermaVO.setMatricolaCCIAA(null); 
          
          
        //Controllo anno iscrizione albo minore anno di sitema e maggiore 1900 e >= anno impianto
        String annoIscrizioneAlbo = null;
        boolean isOKAnnoIscrizione = false;
        if(Validator.isNotEmpty(request.getParameterValues("annoIscrizioneAlbo")))
        {
          annoIscrizioneAlbo = request.getParameterValues("annoIscrizioneAlbo")[i];
        } 
        if(Validator.isNotEmpty(annoIscrizioneAlbo))
        {
          storicoUnitaArboreaConfermaVO.setAnnoIscrizioneAlbo(annoIscrizioneAlbo);    
          if(!Validator.isNumericInteger(annoIscrizioneAlbo))
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "annoIscrizioneAlbo", AnagErrors.ERR_FORMA_ANNO_ISCRIZIONE_ALBO);
            countErrori++;
          }
          else
          {
            Integer currYear = DateUtils.getCurrentYear();
            Integer year1900 = new Integer(1900);
            Integer annoIscrizioneAlboInt = new Integer(annoIscrizioneAlbo);
            Integer annoImpianto = null;
            
            if(storicoUnitaArboreaConfermaVO.getAnnoImpianto() != null)
            {
              annoImpianto = new Integer(storicoUnitaArboreaConfermaVO.getAnnoImpianto());        
            }
            else //se null metto l'anno più basso
            {
              annoImpianto = year1900;
            }
            
            if((annoIscrizioneAlboInt.intValue() < year1900.intValue())
              || (annoIscrizioneAlboInt.intValue() < annoImpianto.intValue()))
            {
              errors = ErrorUtils.setValidErrNoNull(errors, "annoIscrizioneAlbo", AnagErrors.ERR_ISCRIZIONE_ALBO_ANNI);
              countErrori++;
            }
            else
            {
              isOKAnnoIscrizione = true;
            }        
          }
        }
        else storicoUnitaArboreaConfermaVO.setAnnoIscrizioneAlbo(null);   
        
        
        //Se il vino è DOC, il paramtero ALBO della tabella DB_PARAMTERO è a S
        // e l'anno iscrizione è <= parametro DAID 
        //deve essere valorizzati i campi matricola
        if(Validator.isNotEmpty(vinoDoc) && vinoDoc.equalsIgnoreCase("S") && datiObbligatori
         && isOKAnnoIscrizione && (new Integer(storicoUnitaArboreaConfermaVO.getAnnoIscrizioneAlbo()).intValue()
         <= new Integer(parametroDAID).intValue()))
        {
          if(Validator.isEmpty(matricolaCCIAA))
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "matricolaCCIAA", AnagErrors.ERR_CAMPO_OBBLIGATORIO_MATRICOLA_VINODOC);
            countErrori++;
          } 
        }
        
        
        //Se il vino è DOC, il paramtero ALBO della tabella DB_PARAMTERO è a S
        //devono essere valorizzato il campo anno iscrizione
        if(Validator.isNotEmpty(vinoDoc) && vinoDoc.equalsIgnoreCase("S") && datiObbligatori)
        {
          if(Validator.isEmpty(annoIscrizioneAlbo))
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "annoIscrizioneAlbo", AnagErrors.ERR_CAMPO_OBBLIGATORIO_ANNO_VINODOC);
            countErrori++;
          }
          
        }
        
          
        
        if(Validator.isNotEmpty(matricolaCCIAA) || Validator.isNotEmpty(annoIscrizioneAlbo))
        {
          if(!Validator.isNotEmpty(idTipologiaVino))
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "idTipologiaVino", AnagErrors.ERR_TIPOVINO_OBBLIGATORIO);
            countErrori++;
          }    
        }
        
        
        
        
        
        
        
        
        
        
        
				// FORMA ALLEVAMENTO
        if(Validator.isNotEmpty(request.getParameterValues("idFormaAllevamento")))
        {
  				if(Validator.isNotEmpty(request.getParameterValues("idFormaAllevamento")[i])) 
          {
  	 				storicoUnitaArboreaConfermaVO.setIdFormaAllevamento(Long.decode(request.getParameterValues("idFormaAllevamento")[i])); 					
  	 			}
  				// Commento l'obbligatorietà del sesto su fila: controllo momentaneamente
  	 			// sospeso su richiesta del dominio
  	 			else 
          {
  	 				storicoUnitaArboreaConfermaVO.setIdFormaAllevamento(null); 
  	 			}
        }
				// ALTRI VITIGNI
        if(Validator.isNotEmpty(request.getParameter("altroVitigno"+String.valueOf(i))))
        {
  				storicoUnitaArboreaConfermaVO.setPresenzaAltriVitigni(request.getParameter("altroVitigno"+String.valueOf(i)));
  				// Se presenza altri vitigni == N ...
  				if(storicoUnitaArboreaConfermaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
  					//... non deve essere possibile che siano stati inseriti altri vitigni
  					if(storicoUnitaArboreaConfermaVO.getElencoAltriVitigni() != null && storicoUnitaArboreaConfermaVO.getElencoAltriVitigni().length > 0) {
  						errors.add("altroVitignoN", new ValidationError(AnagErrors.ERRORE_PRESENZA_ALTRI_VITIGNI_N));
  						elencoErroriAltriVitigni.put(String.valueOf(i), errors);
  						countErrori++;
  					}
  				}
  				// Se presenza altri vitigni == S ...
  				else {
  					//... non deve essere possibile che non siano stati inseriti altri vitigni
  					if(storicoUnitaArboreaConfermaVO.getElencoAltriVitigni() == null || storicoUnitaArboreaConfermaVO.getElencoAltriVitigni().length == 0) {
  						errors.add("altroVitignoS", new ValidationError(AnagErrors.ERRORE_PRESENZA_ALTRI_VITIGNI_S));
  						elencoErroriAltriVitigni.put(String.valueOf(i), errors);
  	 					countErrori++;
  					}
  				}
        }
				// PERCENTUALE VITIGNO
        if(Validator.isNotEmpty(request.getParameterValues("percentualeVitigno"))) 
        {
  				if(Validator.isNotEmpty(request.getParameterValues("percentualeVitigno")[i])) 
          {
  	 				storicoUnitaArboreaConfermaVO.setPercentualeVarieta(request.getParameterValues("percentualeVitigno")[i]); 					
  	 				if(!Validator.isNumericInteger(storicoUnitaArboreaConfermaVO.getPercentualeVarieta())) {
  	 					errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  	 					countErrori++;
  	 				}
  	 				// Controllo che non sia maggiore di 100
  	 				else {
  	 					if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getPercentualeVarieta()) > 100) {
  	 						errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
  		 					countErrori++;
  	 					}
  	 					else if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getPercentualeVarieta()) == 100 && storicoUnitaArboreaConfermaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
  	 						errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_PERCENTO_VITIGNO_ALTRI_VITIGNI));
  		 					countErrori++;
  	 					}
  	 					else if(Integer.parseInt(storicoUnitaArboreaConfermaVO.getPercentualeVarieta()) < 100 && storicoUnitaArboreaConfermaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_N)) {
  	 						errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_PERCENTO_VITIGNO_ALTRI_VITIGNI));
  		 					countErrori++;
  	 					}
  	 					else if((Integer.parseInt(storicoUnitaArboreaConfermaVO.getPercentualeVarieta()) + totPercentualeAltriVitigni) > 100) {
  	 						errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_PERCENTUALE_ALTRI_VITIGNI_ERRATA));
  		 					countErrori++;
  	 					}
              else if((Integer.parseInt(storicoUnitaArboreaConfermaVO.getPercentualeVarieta()) + totPercentualeAltriVitigni) < 100) {
                errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_PERCENTUALE_ALTRI_VITIGNI_MINORE_100));
                countErrori++;
              }
  	 				}
  	 			}
  	 			else {
  	 				storicoUnitaArboreaConfermaVO.setPercentualeVarieta(null); 
  	 				errors.add("percentualeVitigno", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
  	 				countErrori++;
  	 			}
        }
        
        
				// CAUSALE MODIFICA        
        if(Validator.isNotEmpty(request.getParameterValues("idCausaleModifica"))) 
        {
  				if(Validator.isNotEmpty(request.getParameterValues("idCausaleModifica")[i])) {
  	 				storicoUnitaArboreaConfermaVO.setIdCausaleModifica(Long.decode(request.getParameterValues("idCausaleModifica")[i])); 					
  	 				request.setAttribute("idCausaleModifica", Long.decode(request.getParameterValues("idCausaleModifica")[i]));
  	 			}
  	 			else {
  	 				storicoUnitaArboreaConfermaVO.setIdCausaleModifica(null); 
  	 				errors.add("idCausaleModifica", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
  	 				countErrori++;
  	 			}
        }       
        
				elencoErrori.add(errors);
	 		}
			// Se si sono verificati degli errori li visualizzo
	 		if(countErrori > 0) {
	 			request.setAttribute("elencoErrori", elencoErrori);
	 			%>
	 				<jsp:forward page="<%= unitaArboreeModificaUrl %>" />
	 			<%
	 		}
			// Altrimenti procedo con la modifica dell'unità vitata
			else 
      {
      
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
                <jsp:forward page="<%= unitaArboreeModificaUrl %>" />
              <%
              return;     
            }
            
            //errore nella chiamata al servizio
            if(Validator.isNotEmpty(ritornoAgriservUvVO.getErrori())  && (ritornoAgriservUvVO.getErrori().length > 0))
            {
              request.setAttribute("messaggioErrore", ritornoAgriservUvVO.getErrori()[0]);
              %>
                <jsp:forward page="<%= unitaArboreeModificaUrl %>" />
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
              <jsp:forward page="<%= unitaArboreeModificaUrl %>" />
            <%
            return;     
          }
          
          
          
          
          
          
        }
      
      
				try 
        { 
          for(int i=0;i<elencoStoricoParticellaArboreaVO.length;i++)
          {
            StoricoUnitaArboreaVO storicoVO = elencoStoricoParticellaArboreaVO[i].getStoricoUnitaArboreaVO();
            CatalogoMatriceVO catalogoMatriceVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(storicoVO.getIdUtilizzo(),
              storicoVO.getIdVarieta(), storicoVO.getIdTipoDestinazione(), storicoVO.getIdTipoDettaglioUso(), storicoVO.getIdTipoQualitaUso());
            storicoVO.setIdCatalogoMatrice(catalogoMatriceVO.getIdCatalogoMatrice());
          }                 
					anagFacadeClient.modificaUnitaArboree(elencoStoricoParticellaArboreaVO, ruoloUtenza, provenienza);
				}
				catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - unitaArboreeModificaCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_MODIFICA_UNITA_ARBOREE+".\n"+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
			 	}
				// Torno alla pagina di ricerca/elenco
		 	 	String valorePagina = (String)session.getAttribute("pagina");
				%>
					<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" >
						<jsp:param name="pagina" value="<%= valorePagina %>" /> 
					</jsp:forward>
				<%
			}
	 	}
 	}
	
 	// Vado alla pagina di modifica
 	%>
		<jsp:forward page="<%= unitaArboreeModificaUrl %>" />

