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
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	
	String iridePageName = "unitaArboreeAssociaPercUsoEleggCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
  
  SolmrLogger.debug(this, " - unitaArboreeAssociaPercUsoEleggCtrl.jsp - INIZIO PAGINA");
	
	String unitaArboreeAssociaPercUsoEleggUrl = "/view/unitaArboreeAssociaPercUsoEleggView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione associa % uso."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  
	ValidationError error = null;
	HashMap<String,Vector<Long>> numUnitaArboreeSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numUnitaArboreeSelezionate");
	String[] idStoricoUnitaArborea = request.getParameterValues("idUnita");
	// Recupero il valore della pagina dell'elenco da cui ho cliccato il pulsante modifica multipla
	String pagina = request.getParameter("pagina");
	if(Validator.isNotEmpty(pagina)) {
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
	
	// Recupero il parametro che mi indica il numero massimo di record selezionabili
	String parametroRUVM = null;
	try 
  {
		parametroRUVM = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RUVM);;
	}
	catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeAssociaPercUsoEleggCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_RUVM+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
	}
   
	
	// Verifico che non siano state selezionate più uv rispetto a quelle consentite
	Set<String> elencoKeys = numUnitaArboreeSelezionate.keySet();
	Iterator<String> iteraKey = elencoKeys.iterator();
	while(iteraKey.hasNext()) 
  {
		Vector<Long> selezioni = (Vector<Long>)numUnitaArboreeSelezionate.get((String)iteraKey.next());
		if(selezioni != null && selezioni.size() > 0) {
			for(int a = 0; a < selezioni.size(); a++) {
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
 				
		// Verifico che tra le unità arboree selezionate dall'utente non ve ne sia nessuna
		// con data_fine_validita valorizzata
		StoricoParticellaVO storicoParticellaVO = null;
		StoricoUnitaArboreaVO storicoUnitaArboreaVO = null;
		StoricoParticellaVO[] elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticellaArboreaVO");
		if(elencoStoricoParticellaArboreaVO == null) 
    {
			boolean isErrato = false;
	  	try 
      {
				temp = new Vector<StoricoParticellaVO>();
				Long idStorUnitaArborea = null;
				for(int i = 0; i < elencoIdDaModificare.size(); i++) 
        {
					idStorUnitaArborea = (Long)elencoIdDaModificare.elementAt(i);
					storicoParticellaVO = anagFacadeClient.findStoricoParticellaArboreaConduzione(idStorUnitaArborea, 
					  anagAziendaVO.getIdAzienda().longValue());          
					storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
          
					if(storicoUnitaArboreaVO.getDataFineValidita() != null || storicoUnitaArboreaVO.getDataCessazione() != null) {
						isErrato = true;
						break;
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
				elencoStoricoParticellaArboreaVO = (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]);
			}
			catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - unitaArboreeAssociaPercUsoEleggCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_UNAR+".\n"+se.toString();
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
    
 
    
		
	 	// L'utente ha cliccato il pulsante conferma
	 	if(request.getParameter("conferma") != null)
	 	{
      
    
	 		Vector<ValidationErrors> elencoErrori = new Vector<ValidationErrors>();
	 		ValidationErrors errors = null;
	 		int countErrori = 0;
	 		// Hash usata per verificare che per la stess particella tutte le uv abbiamo la 
	 		// stessa percentuale_utilizzo
	 		HashMap<Long, BigDecimal> hPartPercUsoEleg = new HashMap<Long, BigDecimal>();
	 		//hash usata per memorizzate un record per ogni particella
	 		HashMap<Long, ConduzioneEleggibilitaVO> hPartCondEleg = new HashMap<Long, ConduzioneEleggibilitaVO>();
	 		for(int i = 0; i < elencoStoricoParticellaArboreaVO.length; i++) 
      {      
	 			StoricoParticellaVO storicoParticellaConfermaVO = (StoricoParticellaVO)elencoStoricoParticellaArboreaVO[i];
	 			StoricoUnitaArboreaVO storicoUnitaArboreaConfermaVO = storicoParticellaConfermaVO.getStoricoUnitaArboreaVO();
	 			
	 			errors = new ValidationErrors();	 			
	 			
	 			// PERCENTUALE_UTILIZZO
        if(Validator.isNotEmpty(request.getParameterValues("percUtilizzoEleg"))
          && Validator.isNotEmpty(request.getParameterValues("percUtilizzoEleg")[i])) 
        {
  	 			if(!Validator.isNumberPercentualeMaggioreZeroDecimali(request.getParameterValues("percUtilizzoEleg")[i])) 
          {
            errors.add("percUtilizzoEleg", new ValidationError(AnagErrors.ERRORE_KO_PERCENTUALE_CONDUZIONE_ELEGG));
            countErrori++;
          }
          else
          {
            BigDecimal percentualeUtilizzoElegTmp = new BigDecimal(request.getParameterValues("percUtilizzoEleg")[i].replace(',','.'));
            storicoParticellaConfermaVO.setPercentualeUtilizzoEleg(percentualeUtilizzoElegTmp);   
            if(hPartPercUsoEleg.get(storicoParticellaConfermaVO.getIdParticella()) != null)
            {
              if(hPartPercUsoEleg.get(storicoParticellaConfermaVO.getIdParticella())
                .compareTo(percentualeUtilizzoElegTmp) !=0)
              {
                errors.add("percUtilizzoEleg", new ValidationError(AnagErrors.ERRORE_KO_PART_PERC_COND_ELEGG));
                countErrori++;
              }
            }
            else
            {
              hPartPercUsoEleg.put(storicoParticellaConfermaVO.getIdParticella(), percentualeUtilizzoElegTmp);
              ConduzioneEleggibilitaVO conduzioneEleggibilitaVO = new ConduzioneEleggibilitaVO();
              conduzioneEleggibilitaVO.setidParticella(storicoParticellaConfermaVO.getIdParticella().longValue());
              conduzioneEleggibilitaVO.setIdAzienda(anagAziendaVO.getIdAzienda().longValue());
              conduzioneEleggibilitaVO.setIdEleggibilitaFit(SolmrConstants.ELEGGIBILITA_FIT_VINO);
              conduzioneEleggibilitaVO.setPercentualeUtilizzo(percentualeUtilizzoElegTmp);
              conduzioneEleggibilitaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente().longValue());
              hPartCondEleg.put(storicoParticellaConfermaVO.getIdParticella(), conduzioneEleggibilitaVO);
            }            	 				
  	 			} 	 			
        }
        else 
        {
          storicoParticellaConfermaVO.setPercentualeUtilizzoEleg(null);
          errors.add("percUtilizzoEleg", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }

        elencoErrori.add(errors);
	 		}
			// Se si sono verificati degli errori li visualizzo
	 		if(countErrori > 0) 
	 		{
	 			request.setAttribute("elencoErrori", elencoErrori);
	 			%>
	 				<jsp:forward page="<%= unitaArboreeAssociaPercUsoEleggUrl %>" />
	 			<%
	 		}
			// Altrimenti procedo con la modifica dell'eleggibilita
			else 
      {     
      
				try 
        {          
					gaaFacadeClient.modificaConduzioneEleggibileUV(hPartCondEleg);
				}
				catch(SolmrException se) 
        {
          SolmrLogger.info(this, " - unitaArboreeAssociaPercUsoEleggCtrl.jsp - FINE PAGINA");
          String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_MODIFICA_COND_ELEG_UNITA_ARBOREE+".\n"+se.toString();
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
		<jsp:forward page="<%= unitaArboreeAssociaPercUsoEleggUrl %>" />

