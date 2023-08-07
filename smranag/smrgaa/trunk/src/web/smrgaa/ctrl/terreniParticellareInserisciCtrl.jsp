<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%
	
	String iridePageName = "terreniParticellareInserisciCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String terreniParticellareInserisciUrl = "/view/terreniParticellareInserisciView.jsp";
	String terreniParticellareInserisciCondUsoCtrlUrl = "/ctrl/terreniParticellareInserisciCondUsoCtrl.jsp";
	
  String actionUrl = "../layout/ricerca.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione inserimento terreni."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
	String regimeParticellareInserimento = request.getParameter("regimeParticellareInserimento");
	
	
	if(errors == null) 
  {
		errors = new ValidationErrors();
	}
	else 
  {
		request.setAttribute("errors", errors);
	}
	ValidationError error = null;
	ComuneVO comuneVO = null;
	FoglioVO foglioVO = null;
	Vector<TipoAreaVO> vTipoValoreAreaFoglio = null;
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
	Long idEvento = null;
	if(Validator.isNotEmpty(request.getParameter("idEvento"))) 
  {
		idEvento = Long.decode(request.getParameter("idEvento"));
		request.setAttribute("idEvento", idEvento);
	}
	
	// Recupero qui lo stato estero perchè potrei recuperarlo anche della pop-up
	// di inserimento particella evento
	String descStatoEstero = request.getParameter("descStatoEstero");
	request.setAttribute("descStatoEstero", descStatoEstero);
	
	// Parametro che mappa le pagine relative al caso d'uso di inserimento particella
	String inserimento = request.getParameter("inserimento");
	// Parametro che mi indica se arrivo dal secondo step di inserimento della particella
	String indietro = request.getParameter("indietro");
	//request.setAttribute("indietro", indietro);
	// Variabile d'appoggio necessaria per la gestione del pulsante indietro: necessaria
	// per capire se è stato cambiato o meno il valore del comune
	String oldIstatComune = null;
	
	// Recupero i valori relativi alle unità produttive
 	Vector<UteVO> elencoUte = new Vector<UteVO>();
 	try 
  {
		String[] orderBy = {SolmrConstants.ORDER_BY_DESC_COMUNE, SolmrConstants.ORDER_BY_UTE_INDIRIZZO};
	 	elencoUte = anagFacadeClient.getListUteByIdAzienda(anagAziendaVO.getIdAzienda(), true, orderBy);
	 	request.setAttribute("elencoUte", elencoUte);
 	}
 	catch(SolmrException se) 
  {
		error = new ValidationError(AnagErrors.ERRORE_KO_UNITA_PRODUTTIVA);
	 	errors.add("idUnitaProduttiva", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
   	return;
	}
 	
 	// Recupero l'elenco delle motivazioni
 	Vector<TipoEventoVO> elencoTipoEvento = null;
 	try 
  {
 		elencoTipoEvento = anagFacadeClient.getListTipiEvento();
 		request.setAttribute("elencoTipoEvento", elencoTipoEvento);
 	}
 	catch(SolmrException se) 
  {
		error = new ValidationError(AnagErrors.ERRORE_KO_TIPI_EVENTO);
	 	errors.add("idTipoEvento", error);
   	request.setAttribute("errors", errors);
   	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
   	return;
	}
	
 	
 	//nn arrivo dal tasto indietro......
 	if(Validator.isEmpty(indietro)
 	  && Validator.isEmpty(regimeParticellareInserimento)) 
  {
    //Se arrivo da una particella appena inserita!!!
    StoricoParticellaVO storicoParticellaAppInsVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
		session.removeAttribute("storicoParticellaVO");
		session.removeAttribute("elencoStoricoEvento");
    session.removeAttribute("flagObbligoCatastale");
    session.removeAttribute("arrVIdStoricoUnitaArborea");
    session.removeAttribute("arrVAreaUV");
    
		storicoParticellaVO = new StoricoParticellaVO();
		if(elencoUte != null && elencoUte.size() == 1) 
    {
			UteVO uteVO = (UteVO)elencoUte.elementAt(0);
			ConduzioneParticellaVO conduzioneParticellaVO = new ConduzioneParticellaVO();
			conduzioneParticellaVO.setIdUte(uteVO.getIdUte());
			storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{conduzioneParticellaVO});
			//lascio comune e folgio della particella inserita prima
			if(storicoParticellaAppInsVO != null)
			{
			  storicoParticellaVO.setComuneParticellaVO(storicoParticellaAppInsVO.getComuneParticellaVO());			
			  storicoParticellaVO.setFoglio(storicoParticellaAppInsVO.getFoglio());
			}
			else
			{
			  ProvinciaVO provinciaVO = new ProvinciaVO();
			  comuneVO = new ComuneVO();			
				provinciaVO.setSiglaProvincia(uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia());
				comuneVO.setProvinciaVO(provinciaVO);
				comuneVO.setDescom(uteVO.getComuneUte().getDescom());
				comuneVO.setFlagEstero(uteVO.getComuneUte().getFlagEstero());
				storicoParticellaVO.setComuneParticellaVO(comuneVO);
		  }
			session.setAttribute("storicoParticellaVO", storicoParticellaVO);
		}
	}
 	else 
  {
 		if(storicoParticellaVO != null && storicoParticellaVO.getComuneParticellaVO() != null) 
    {
 			oldIstatComune = storicoParticellaVO.getComuneParticellaVO().getIstatComune();
 		}
 	}
 	
 	
 	StoricoParticellaVO[] elencoStoricoEventoSave = (StoricoParticellaVO[])session
    .getAttribute("elencoStoricoEvento");
 	if(Validator.isNotEmpty(regimeParticellareInserimento))
 	{
	 	String[] particelleDaCessare = request.getParameterValues("idParticellaDaCessare");
	  if((elencoStoricoEventoSave != null) && (elencoStoricoEventoSave.length >0 ))
	  {	    
	    for(int i=0;i<elencoStoricoEventoSave.length;i++)
	    {
	      boolean flagTrovato = false;
	      if((particelleDaCessare != null) && (particelleDaCessare.length > 0))
	      {
		      for(int j=0;j<particelleDaCessare.length;j++)
		      {
		        if(elencoStoricoEventoSave[i].getIdParticella().compareTo(new Long(particelleDaCessare[j])) == 0)
		        {
		          flagTrovato = true;
		          break;
		        }
		      }
		    }
	      
	      elencoStoricoEventoSave[i].setCessaParticella(flagTrovato);
	    }
	  }
	}
  
  session.setAttribute("elencoStoricoEvento",elencoStoricoEventoSave);
  
 	
 	// L'utente ha cliccato il pulsante avanti
 	if(request.getParameter("avanti") != null) 
  {
 		// Costruisco gli oggetti per gestire correttamente l'inserimento
 		if(storicoParticellaVO == null) 
    {
 			storicoParticellaVO = new StoricoParticellaVO();
 			storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{new ConduzioneParticellaVO()});
 		}
 		comuneVO = new ComuneVO();
 		ProvinciaVO provinciaVO = new ProvinciaVO();
 		String flagEstero = null;
 		boolean isPiemontese = false;
 		
 		// Recupero i parametri
 		String ute = request.getParameter("idUte");
 		String siglaProvincia = request.getParameter("siglaProvincia");
 		request.setAttribute("siglaProvincia", siglaProvincia);
 		String descComune = request.getParameter("descComune");
 		request.setAttribute("descComune", descComune);
 		String sezione = request.getParameter("sezione");
 		String foglio = request.getParameter("foglio");
 		String particella = request.getParameter("particella");
 		boolean isProvvisoria = Validator.isNotEmpty(request.getParameter("provvisoria"));
 		String provvisoria = null;
    
    
    
    
    
 		if(isProvvisoria) 
    {
 			request.setAttribute("provvisoria", SolmrConstants.FLAG_S);
 			provvisoria = SolmrConstants.FLAG_S;
 		}
 		String subalterno = request.getParameter("subalterno");
 		
 		// Controllo la correttezza formale dei parametri:
 		// L'ute è obbligatoria
 		if(!Validator.isNotEmpty(ute)) 
    {
 			error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 			errors.add("idUte", error);
 			storicoParticellaVO.getElencoConduzioni()[0].setIdUte(null);
 		}
 		else 
    {
 			storicoParticellaVO.getElencoConduzioni()[0].setIdUte(Long.decode(ute));
 		}
 		// Se lo stato estero è nullo...
 		if(!Validator.isNotEmpty(descStatoEstero)) 
    {
 			flagEstero = SolmrConstants.FLAG_N;
 			// ... la sigla provincia e il comune sono obbligatori
 			if(!Validator.isNotEmpty(siglaProvincia)) 
      {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 	 			errors.add("siglaProvincia", error);
 			}
 			if(!Validator.isNotEmpty(descComune)) 
      {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
 	 			errors.add("descComune", error);
 			}
 			provinciaVO.setSiglaProvincia(siglaProvincia);
 			comuneVO.setProvinciaVO(provinciaVO);
 			comuneVO.setDescom(descComune);
 		}
 		// Altrimenti...
 		else 
    {
 			flagEstero = SolmrConstants.FLAG_S;
			// ... la sigla provincia e il comune non sono valorizzabili
 			if(Validator.isNotEmpty(siglaProvincia)) {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE);
 	 			errors.add("siglaProvincia", error);
 			}
 			if(Validator.isNotEmpty(descComune)) {
 				error = new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE);
 	 			errors.add("descComune", error);
 			}
 			comuneVO.setDescom(descStatoEstero);
 		} 		
		comuneVO.setFlagEstero(flagEstero);
		// In funzione di quali elementi di ubicazione ha inserito l'utente
		// ne controllo la loro effettiva esistenza
		ComuneVO comuneControlloVO = null;
		if(comuneVO.getFlagEstero().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
    {
			try 
      {
				comuneControlloVO = anagFacadeClient.getComuneByParameters(comuneVO.getDescom(), comuneVO.getProvinciaVO().getSiglaProvincia(), null, SolmrConstants.FLAG_S, SolmrConstants.FLAG_N);
				if(comuneControlloVO == null) 
        {
					error = new ValidationError(AnagErrors.ERRORE_COMUNE_INESISTENTE_FOR_PARTICELLA);
	 	 			errors.add("descComune", error);
				}
				else 
        {
					comuneVO.setIstatComune(comuneControlloVO.getIstatComune());
					comuneVO.setZonaAlt(comuneControlloVO.getZonaAlt());
					comuneVO.setSiglaProv(comuneControlloVO.getSiglaProv());
          comuneVO.setGestioneSezione(comuneControlloVO.getGestioneSezione());
					// Controllo se si tratta di un comune piemontese
					ProvinciaVO provinciaControlloVO = null;
					try 
          {
						provinciaControlloVO = anagFacadeClient.getProvinciaByCriterio(comuneVO.getSiglaProv());
						if(provinciaControlloVO != null) 
            {
							if(provinciaControlloVO.getIdRegione().equalsIgnoreCase(SolmrConstants.ID_REG_PIEMONTE)) 
              {
								isPiemontese = true;		
							}
						}
					}
					catch(SolmrException se) 
          {
						error = new ValidationError(AnagErrors.ERRORE_KO_PROVINCIA);
		 	 			errors.add("siglaProvincia", error);
					}
				}
			}
			catch(SolmrException se) 
      {
				error = new ValidationError(AnagErrors.ERRORE_KO_COMUNE);
 	 			errors.add("descComune", error);
			}
		}
		else 
    {
			try 
      {
				comuneControlloVO = anagFacadeClient.getComuneByParameters(comuneVO.getDescom(), null, null, SolmrConstants.FLAG_S, SolmrConstants.FLAG_S);
				if(comuneControlloVO == null) 
        {
					error = new ValidationError(AnagErrors.ERRORE_STATO_ESTERO_INESISTENTE_FOR_PARTICELLA);
	 	 			errors.add("descStatoEstero", error);
				}
				else 
        {
					comuneVO.setIstatComune(comuneControlloVO.getIstatComune());
					comuneVO.setZonaAlt(comuneControlloVO.getZonaAlt());
				}
			}
			catch(SolmrException se) 
      {
				error = new ValidationError(AnagErrors.ERRORE_KO_STATO_ESTERO);
 	 			errors.add("descStatoEstero", error);
			}
		}
		storicoParticellaVO.setIstatComune(comuneVO.getIstatComune());
		storicoParticellaVO.setComuneParticellaVO(comuneVO);
		StoricoParticellaVO storicoConfronto = new StoricoParticellaVO();
		storicoConfronto.setIstatComune(oldIstatComune);
		storicoConfronto.setSezione(request.getParameter("sezione"));
		storicoConfronto.setFoglio(request.getParameter("foglio"));
		storicoConfronto.setParticella(request.getParameter("particella"));
		storicoConfronto.setSubalterno(request.getParameter("subalterno"));
		boolean isEqualForIndietro = storicoParticellaVO.equalsKey(storicoConfronto);
		// Setto la sezione
    if(Validator.isNotEmpty(sezione))
    {
		  storicoParticellaVO.setSezione(sezione.toUpperCase());
    }
    else
    {
      storicoParticellaVO.setSezione(null);
    }
    if(isPiemontese && Validator.isNotEmpty(comuneVO.getGestioneSezione()))
    {
      //nuovi controlli sezione 06/09/2011
      //Sezione obbligatoria  
      if("O".equalsIgnoreCase(comuneVO.getGestioneSezione()))
      {
        if(Validator.isEmpty(storicoParticellaVO.getSezione()))
        {
          error = new ValidationError("Per il comune di "+comuneVO.getDescom()+" la sezione"
           +" e'' obbligatoria.");
          errors.add("sezione", error);
        }
        else
        {
          SezioneVO sezioneVO = null;
          // ... Verifico che effettivamente la sezione inserita sia censito su DB_SEZIONE
          try 
          {
            sezioneVO = anagFacadeClient.getSezioneByParameters(
              storicoParticellaVO.getComuneParticellaVO().getIstatComune(), 
              storicoParticellaVO.getSezione());
          }
          catch(SolmrException se) 
          {
            String messaggio = errMsg+"::"+AnagErrors.ERRORE_KO_SEZIONE+"::"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          if(sezioneVO == null)
          {
            error = new ValidationError("La sezione specificata non e'' prevista per il comune di "+comuneVO.getDescom());
            errors.add("sezione", error);
          }
        }
      }
      //Sezione facoltativa....
      else if("M".equalsIgnoreCase(comuneVO.getGestioneSezione()))
      {
        if(Validator.isNotEmpty(storicoParticellaVO.getSezione()))
        {
          SezioneVO sezioneVO = null;
          // ... Verifico che effettivamente la sezione inserita sia censito su DB_SEZIONE
          try 
          {
            sezioneVO = anagFacadeClient.getSezioneByParameters(
              storicoParticellaVO.getComuneParticellaVO().getIstatComune(), 
              storicoParticellaVO.getSezione());
          }
          catch(SolmrException se) 
          {
            String messaggio = errMsg+"::"+AnagErrors.ERRORE_KO_SEZIONE+"::"+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          if(sezioneVO == null)
          {
            error = new ValidationError("La sezione specificata non e'' prevista per il comune di "+comuneVO.getDescom());
            errors.add("sezione", error);
          }
        }
      }
      //N
      //Sezione non inseribile
      else
      {
        if(Validator.isNotEmpty(storicoParticellaVO.getSezione()))
        {
          error = new ValidationError("Per il comune di "+comuneVO.getDescom()+" la sezione non e'' prevista");
          errors.add("sezione", error);
        }
      }
      
    }
		// Setto il foglio
		storicoParticellaVO.setFoglio(foglio);
		// Se il foglio è stato valorizzato
		if(Validator.isNotEmpty(storicoParticellaVO.getFoglio())) 
    {
			// Verifico che si tratti di un valore numerico
			if(!Validator.isNumericInteger(storicoParticellaVO.getFoglio())) 
      {
				error = new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO);
			 	errors.add("foglio", error);
			}
			else 
      {
				// Se il comune indicato è situato in Piemonte
				if(isPiemontese) 
        {
					// ... verifico che il foglio indicato sia effettivamente presente
					// su DB_FOGLIO
					try 
          {
						foglioVO = anagFacadeClient.findFoglioByParameters(storicoParticellaVO.getComuneParticellaVO().getIstatComune(), storicoParticellaVO.getFoglio(), storicoParticellaVO.getSezione());
						vTipoValoreAreaFoglio = gaaFacadeClient.getValoriTipoAreaFoglio(storicoParticellaVO.getComuneParticellaVO().getIstatComune(), storicoParticellaVO.getFoglio(), storicoParticellaVO.getSezione());
						if(foglioVO == null) 
            {
							error = new ValidationError(AnagErrors.ERRORE_FOGLIO_ERRATO);
						 	errors.add("foglio", error);
						}
					}
					catch(SolmrException se) {
						error = new ValidationError(AnagErrors.ERRORE_KO_FOGLIO);
					 	errors.add("foglio", error);
					}
				}
			}
		}
		// Altrimenti segnalo che si tratta di un campo obbligatorio
		else 
    {
			error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
		 	errors.add("foglio", error);
		}
		// Setto la particella e il subalterno
		storicoParticellaVO.setParticella(particella);
		storicoParticellaVO.setSubalterno(subalterno);
		// Se l'utente ha scelto il check "provvisoria"...
		if(isProvvisoria) 
    {
			// ... il numero particella non può essere valorizzato
			if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) 
      {
				error = new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE);
			 	errors.add("particella", error);
			}
			// ... il subalterno non può essere valorizzato
			if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) 
      {
				error = new ValidationError(AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE);
			 	errors.add("subalterno", error);
			}
			
			// Se l'evento scelto non è "nuova particella" segnalo errore
	    if(idEvento.compareTo(SolmrConstants.ID_EVENTO_NUOVA_PARTICELLA) != 0) 
	    {
	      error = new ValidationError(AnagErrors.ERRORE_MOTIVAZIONE_PARTICELLA_PROV);
        errors.add("idEvento", error);
	    }
		}
		// Altrimenti...
		else 
    {
			// ... il numero particella è obbligatorio
			if(!Validator.isNotEmpty(storicoParticellaVO.getParticella())) 
      {
				error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
			 	errors.add("particella", error);
			}
			else 
      {
				if(!Validator.isNumericInteger(storicoParticellaVO.getParticella())) 
        {
					error = new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO);
				 	errors.add("particella", error);
				}	
			}
		}
 		session.setAttribute("storicoParticellaVO", storicoParticellaVO); 		
 		
		// Se l'evento scelto non è "nuova particella"
 		if(idEvento.compareTo(SolmrConstants.ID_EVENTO_NUOVA_PARTICELLA) != 0) 
    {
			// Controllo che l'utente abbia inserito almeno una particella da cui
			// scaturisca la nuova
			if(session.getAttribute("elencoStoricoEvento") == null || ((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento")).length == 0) 
      {
				error = new ValidationError(AnagErrors.ERRORE_MOTIVAZIONE_PARTICELLE_OBBLIGATORIE);
			 	errors.add("motivazione", error);
			}
			// Se invece l'evento selezionato non è accorpamento
			else 
      {
				if(idEvento.compareTo(SolmrConstants.ID_EVENTO_ACCORPAMENTO_PARTICELLA) != 0) 
        {
					// Controllo che l'utente abbia inserito solo una particella da cui
					// scaturisca la nuova
					if(((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento")).length > 1) 
          {
						error = new ValidationError(AnagErrors.ERRORE_MOTIVAZIONE_PARTICELLE_ERRATA);
					 	errors.add("motivazione", error);
					}
		 		}	
			}
 		}
		// Se ci sono errori li visualizzo
 		if(errors.size() > 0) 
    {
 			request.setAttribute("errors", errors);
     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
     	return;
 		}
		
		// Se non ci sono errori e l'evento scelto non è "nuova particella"
		if(idEvento.compareTo(SolmrConstants.ID_EVENTO_NUOVA_PARTICELLA) == 0) 
    {
			// Rimuovo le eventuali precedenti particelle evento dalla sessione
			session.removeAttribute("elencoStoricoEvento");
 		}
		
		// Cerco le informazioni relative all'U.T.E.
		UteVO uteVO = null;
		try 
    {
			uteVO = anagFacadeClient.findUteByPrimaryKey(storicoParticellaVO.getElencoConduzioni()[0].getIdUte());
			if(uteVO != null) 
      {
				storicoParticellaVO.getElencoConduzioni()[0].setUteVO(uteVO);
			}
		}
		catch(SolmrException se) 
    {
			error = new ValidationError(AnagErrors.ERRORE_KO_UTE);
		 	errors.add("error", error);
		 	request.setAttribute("errors", errors);
 	    request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
 	    return;
		}
    //forse non serve più...ma attendiamo lo stabilizzarsi di Terry!!!
		boolean isCertificata = false;
		if(session.getAttribute("isCertificata") != null) 
    {
			isCertificata = true;	
		}
		// Ricerco nuovamente i dati sulle tabelle di SMRGAA solo se non arrivo 
		// dal secondo step di inserimento della particella ed è
		// stata cambiata la chiave logica della particella
		if(Validator.isEmpty(indietro) || (Validator.isNotEmpty(indietro) && !isEqualForIndietro)) 
    {
			// Se la particella inserita non è provvisoria 
			//eliminato controllo per provvisoria richiesta sergio teresa 02/03/2012
			//if(!isProvvisoria) 
      //{
			// Verifico se per la chiave logica indicata esiste già una particella
			// attiva(data_fine_validità == null)censita su DB_STORICO_PARTICELLA
			StoricoParticellaVO storicoParticellaAnagrafeVO = null;
			try 
      {
				storicoParticellaAnagrafeVO = anagFacadeClient.findStoricoParticellaVOByParameters(storicoParticellaVO.getComuneParticellaVO().getIstatComune(), storicoParticellaVO.getSezione(), storicoParticellaVO.getFoglio(), storicoParticellaVO.getParticella(), storicoParticellaVO.getSubalterno(), null);
			}
			catch(SolmrException se) 
      {
				error = new ValidationError(AnagErrors.ERRORE_KO_SEARCH_PARTICELLE);
			 	errors.add("error", error);
			 	request.setAttribute("errors", errors);
	 	   	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
	 	  	return;
			}
			// Se la trovo...
			if(storicoParticellaAnagrafeVO != null) 
      {
        //carico nuovi valori del foglio
        storicoParticellaAnagrafeVO.setvValoriTipoArea(
          gaaFacadeClient.getValoriTipoAreaParticella(storicoParticellaAnagrafeVO.getIdParticella(), null));
				// Definisco censita e non modificabile la particella solo nel caso in cui sia stata inserita, nel caso di precedenti 
				// insert da import SIAN, con superficie grafica > 0
				if(Double.parseDouble(StringUtils.parseSuperficieField(storicoParticellaAnagrafeVO.getSuperficieGrafica()).replace(',', '.')) > 0) 
        {
					isCertificata = true;
				}
				// Altrimenti forzo il parametro a false per permettere la modifica della superficie catastale
				// dal momento che presenta un valore non consentito
				else 
        {
					isCertificata = false;
				}
				// ... e setto  tutti i valori all'interno del nuovo oggetto che sto inserendo
				storicoParticellaVO.setvValoriTipoArea(storicoParticellaAnagrafeVO.getvValoriTipoArea());
				storicoParticellaVO.setIdStoricoParticella(storicoParticellaAnagrafeVO.getIdStoricoParticella());
				storicoParticellaVO.setIdParticella(storicoParticellaAnagrafeVO.getIdParticella());
				storicoParticellaVO.setSupCatastale(StringUtils.parseSuperficieField(storicoParticellaAnagrafeVO.getSupCatastale()));
        storicoParticellaVO.setSuperficieGrafica(StringUtils.parseSuperficieField(storicoParticellaAnagrafeVO.getSuperficieGrafica()));
				storicoParticellaVO.setIdAreaA(storicoParticellaAnagrafeVO.getIdAreaA());
				storicoParticellaVO.setIdAreaB(storicoParticellaAnagrafeVO.getIdAreaB());
				storicoParticellaVO.setIdAreaC(storicoParticellaAnagrafeVO.getIdAreaC());
				storicoParticellaVO.setIdAreaD(storicoParticellaAnagrafeVO.getIdAreaD());
				storicoParticellaVO.setIdAreaM(storicoParticellaAnagrafeVO.getIdAreaM());
        storicoParticellaVO.setIdAreaG(storicoParticellaAnagrafeVO.getIdAreaG()); //Modifica GAA09-48 Inserisci particella - v7
				storicoParticellaVO.setIdAreaH(storicoParticellaAnagrafeVO.getIdAreaH()); //Modifica GAA09-48 Inserisci particella - v7
        storicoParticellaVO.setAreaG(storicoParticellaAnagrafeVO.getAreaG());  //Modifica GAA09-48 Inserisci particella - v7
        storicoParticellaVO.setAreaH(storicoParticellaAnagrafeVO.getAreaH()); //Modifica GAA09-48 Inserisci particella - v7
        storicoParticellaVO.setIdAreaI(storicoParticellaAnagrafeVO.getIdAreaI());
        storicoParticellaVO.setIdAreaL(storicoParticellaAnagrafeVO.getIdAreaL());
        storicoParticellaVO.setAreaI(storicoParticellaAnagrafeVO.getAreaI());
        storicoParticellaVO.setAreaL(storicoParticellaAnagrafeVO.getAreaL());
        storicoParticellaVO.setIdCasoParticolare(storicoParticellaAnagrafeVO.getIdCasoParticolare());
				storicoParticellaVO.setFlagCaptazionePozzi(storicoParticellaAnagrafeVO.getFlagCaptazionePozzi());
				storicoParticellaVO.setZonaAltimetrica(storicoParticellaAnagrafeVO.getZonaAltimetrica());
				storicoParticellaVO.setIdZonaAltimetrica(storicoParticellaAnagrafeVO.getIdZonaAltimetrica());
				storicoParticellaVO.setFlagIrrigabile(storicoParticellaAnagrafeVO.getFlagIrrigabile());
        storicoParticellaVO.setIdFasciaFluviale(storicoParticellaAnagrafeVO.getIdFasciaFluviale());
        storicoParticellaVO.setFasciaFluviale(storicoParticellaAnagrafeVO.getFasciaFluviale());
				storicoParticellaVO.setIdIrrigazione(storicoParticellaAnagrafeVO.getIdIrrigazione());
				storicoParticellaVO.setTipoIrrigazioneVO(storicoParticellaAnagrafeVO.getTipoIrrigazioneVO());	
        storicoParticellaVO.setIdPotenzialitaIrrigua(storicoParticellaAnagrafeVO.getIdPotenzialitaIrrigua());
        storicoParticellaVO.setTipoPotenzialitaIrriguaVO(storicoParticellaAnagrafeVO.getTipoPotenzialitaIrriguaVO());
        storicoParticellaVO.setIdTerrazzamento(storicoParticellaAnagrafeVO.getIdTerrazzamento());
        storicoParticellaVO.setTipoTerrazzamentoVO(storicoParticellaAnagrafeVO.getTipoTerrazzamentoVO());
        storicoParticellaVO.setIdRotazioneColturale(storicoParticellaAnagrafeVO.getIdRotazioneColturale());
        storicoParticellaVO.setTipoRotazioneColturaleVO(storicoParticellaAnagrafeVO.getTipoRotazioneColturaleVO());
        storicoParticellaVO.setPercentualePendenzaMedia(storicoParticellaAnagrafeVO.getPercentualePendenzaMedia());
        storicoParticellaVO.setGradiPendenzaMedia(storicoParticellaAnagrafeVO.getGradiPendenzaMedia());
        storicoParticellaVO.setGradiEsposizioneMedia(storicoParticellaAnagrafeVO.getGradiEsposizioneMedia());
        storicoParticellaVO.setMetriAltitudineMedia(storicoParticellaAnagrafeVO.getMetriAltitudineMedia());
        storicoParticellaVO.setIdMetodoIrriguo(storicoParticellaAnagrafeVO.getIdMetodoIrriguo());
        storicoParticellaVO.setTipoMetodoIrriguo(storicoParticellaAnagrafeVO.getTipoMetodoIrriguo()); 
				storicoParticellaVO.setParticellaCertificataVO(null);
				// Recupero il totale delle superfici condotte  non in assevimento già associate alla particella
				// selezionata relativa all'azienda agricola in esame
				// solo se la particella non è provvisoria oppure non se ne inserisce una uguale..
				
				boolean trovatoUgualePadre = false;
				if((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento") != null && ((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento")).length > 0) 
	      {
	        for(int i = 0; i < ((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento")).length; i++) 
	        {
	          StoricoParticellaVO storicoEventoVO = (StoricoParticellaVO)((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento"))[i];
	          if(storicoEventoVO.equalsKey(storicoParticellaVO)) 
	          {
	            trovatoUgualePadre = true;
	            break;
	          }
	        }
	      }
				
				if(!isProvvisoria && !trovatoUgualePadre)
				{
					BigDecimal totSupCondotte = null;
					try 
	        {
						totSupCondotte = anagFacadeClient.getTotSupCondottaByAziendaAndParticella(anagAziendaVO.getIdAzienda(), storicoParticellaVO.getIdParticella());
						String supConfronto = AnagUtils.valSupCatGraf(storicoParticellaVO.getSupCatastale(), storicoParticellaVO.getSuperficieGrafica());
	          //Per stabilizzati...
	          if(Validator.isNotEmpty(foglioVO) 
               && (foglioVO.getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
               && Validator.validateDouble(storicoParticellaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
               && Double.parseDouble(storicoParticellaVO.getSuperficieGrafica().replace(',', '.')) > 0)
            {
              supConfronto = storicoParticellaVO.getSuperficieGrafica();
            }
	          
	          if(Validator.isNotEmpty(totSupCondotte)) 
	          {
							if(NumberUtils.arrotonda(totSupCondotte.doubleValue(), 4) >= NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',', '.')), 4))
	            {
								error = new ValidationError(AnagErrors.ERRORE_KO_INSERIMENTO_PARTICELLA_FOR_SUPERO_GRAFICA_CATASTALE);
							 	errors.add("idEvento", error);
							 	request.setAttribute("errors", errors);
					 	    request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
					 	    return;
							}
							else 
	            {
								double differenza = NumberUtils.arrotonda(Double.parseDouble(supConfronto.replace(',','.')), 4) - NumberUtils.arrotonda(totSupCondotte.doubleValue(), 4);
								storicoParticellaVO.getElencoConduzioni()[0].setSuperficieCondotta(StringUtils.parseSuperficieField(String.valueOf(differenza)));
								
	              //proporzione per ottenere la percentuale possesso
	              double supConfrontoDb = Double.parseDouble(supConfronto.replace(',','.'));
	              double percentualePossessoDb = NumberUtils.arrotonda(100 * differenza / supConfrontoDb,2);
	              storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso((new BigDecimal(percentualePossessoDb)));
	              storicoParticellaVO.getElencoConduzioni()[0].setSuperficieAgronomica(null);
							}
						}
						else 
	          {
	            storicoParticellaVO.getElencoConduzioni()[0].setSuperficieCondotta(supConfronto);
	            storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso(new BigDecimal(100));
							storicoParticellaVO.getElencoConduzioni()[0].setSuperficieAgronomica(null);
						}
					}
					catch(SolmrException se) 
	        {
						error = new ValidationError(AnagErrors.ERRORE_KO_TOT_SUPERFICI_CONDOTTE);
					 	errors.add("idEvento", error);
					 	request.setAttribute("errors", errors);
		 	     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
		 	     	return;
					}
				}
			}
			// Se non la trovo...
			else 
      {
				// Verifico se per la chiave logica indicata esiste già una particella
				// censita su DB_PARTICELLA_CERTIFICATA
				ParticellaCertificataVO particellaCertificataVO = null;
				try 
        {
					particellaCertificataVO = anagFacadeClient.findParticellaCertificataByParameters(storicoParticellaVO.getComuneParticellaVO().getIstatComune(), storicoParticellaVO.getSezione(), storicoParticellaVO.getFoglio(), storicoParticellaVO.getParticella(), storicoParticellaVO.getSubalterno(), true, null);
				}
				catch(SolmrException se) 
        {
					error = new ValidationError(AnagErrors.ERRORE_KO_PARTICELLA_CERTIFICATA);
				 	errors.add("error", error);
				 	request.setAttribute("errors", errors);
		 	    request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
		 	    return;
				}
				// Se la trovo ed è censita e univoca
				if(particellaCertificataVO != null && particellaCertificataVO.isCertificata() && particellaCertificataVO.isUnivoca()) 
        {
        
          //carico valori del tipo area
          if(Validator.isNotEmpty(particellaCertificataVO.getIdParticella()))
          {
            storicoParticellaVO.setvValoriTipoArea(gaaFacadeClient.getValoriTipoAreaParticella(particellaCertificataVO.getIdParticella(), null));
					}
					else
          {          
            //nel caso esistesse il foglio....
            storicoParticellaVO.setvValoriTipoArea(vTipoValoreAreaFoglio);
          }
          
          if(storicoParticellaVO.getvValoriTipoArea() == null)
          {
            storicoParticellaVO.setvValoriTipoArea(gaaFacadeClient.getAllValoriTipoArea());
          }
          
          
					// Definisco censita e non modificabile la particella solo nel caso in cui sia stata reperita dal SIAN con una 
					// superficie > 0
					if((particellaCertificataVO.getSupGrafica() != null)
             && (Double.parseDouble(StringUtils.parseSuperficieField(particellaCertificataVO.getSupGrafica()).replace(',', '.')) > 0)) 
          {
						isCertificata = true;
					}
					// Altrimenti forzo il parametro a false per permettere la modifica della superficie catastale
					// dal momento che presenta un valore non consentito
					else 
          {
						isCertificata = false;
					}
					// ... e setto  tutti i valori all'interno del nuovo oggetto che sto inserendo
					storicoParticellaVO.setSupCatastale(StringUtils.parseSuperficieField(particellaCertificataVO.getSupCatastaleCertificata()));
          storicoParticellaVO.setSuperficieGrafica(StringUtils.parseSuperficieField(particellaCertificataVO.getSupGrafica()));
					storicoParticellaVO.setZonaAltimetrica(particellaCertificataVO.getZonaAltimetrica());
					storicoParticellaVO.setIdZonaAltimetrica(Long.decode(particellaCertificataVO.getZonaAltimetrica().getCode().toString()));
					if(Validator.isNotEmpty(foglioVO) 
            && (foglioVO.getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
            && Validator.validateDouble(particellaCertificataVO.getSupGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
            && Double.parseDouble(particellaCertificataVO.getSupGrafica().replace(',', '.')) > 0)
          {
            storicoParticellaVO.getElencoConduzioni()[0].setSuperficieCondotta(StringUtils.parseSuperficieField(particellaCertificataVO.getSupGrafica()));
          }
          else
          {
            storicoParticellaVO.getElencoConduzioni()[0].setSuperficieCondotta(StringUtils.parseSuperficieField(particellaCertificataVO.getSupCatastaleCertificata()));
          }
          storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso(new BigDecimal(100));
					storicoParticellaVO.getElencoConduzioni()[0].setSuperficieAgronomica(null);
					storicoParticellaVO.setParticellaCertificataVO(particellaCertificataVO);
					// e forzo a null i valori che non presentano corrispondenza, in modo da non creare informazioni
					// "promiscue" causate dalla navigazione avanti/indietro con particelle diverse
					storicoParticellaVO.setIdStoricoParticella(null);
					storicoParticellaVO.setIdParticella(null);
					storicoParticellaVO.setIdCasoParticolare(null);
					storicoParticellaVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_N);
					storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_N);
          storicoParticellaVO.setIdFasciaFluviale(null);
          storicoParticellaVO.setFasciaFluviale(null);
					storicoParticellaVO.setIdIrrigazione(null);
					storicoParticellaVO.setTipoIrrigazioneVO(null);
          storicoParticellaVO.setIdPotenzialitaIrrigua(null);
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(null);
          storicoParticellaVO.setIdTerrazzamento(null);
          storicoParticellaVO.setTipoTerrazzamentoVO(null);
          storicoParticellaVO.setIdRotazioneColturale(null);
          storicoParticellaVO.setTipoRotazioneColturaleVO(null);
          storicoParticellaVO.setIdAreaH(null);
          storicoParticellaVO.setAreaH(null);
          storicoParticellaVO.setIdAreaM(null);
          storicoParticellaVO.setAreaM(null);
          storicoParticellaVO.setPercentualePendenzaMedia(null);
          storicoParticellaVO.setGradiPendenzaMedia(null);
          storicoParticellaVO.setGradiEsposizioneMedia(null);
          storicoParticellaVO.setMetriAltitudineMedia(null);
          storicoParticellaVO.setIdMetodoIrriguo(null);
          storicoParticellaVO.setTipoMetodoIrriguo(null);
				}
				else 
        {
					// Altrimenti vuol dire che non è censita su nessuna delle tabelle
					// di SMRGAA e quindi svuoto tutti i possibili dati catastali
					// precedentemente settati dall'utente
					storicoParticellaVO.setSuperficieGrafica(null);
					storicoParticellaVO.setIdStoricoParticella(null);
					storicoParticellaVO.setIdParticella(null);
					storicoParticellaVO.setSupCatastale(null);
					storicoParticellaVO.setIdCasoParticolare(null);
					storicoParticellaVO.setFlagCaptazionePozzi(SolmrConstants.FLAG_N);
					storicoParticellaVO.setFlagIrrigabile(SolmrConstants.FLAG_N);
          storicoParticellaVO.setIdFasciaFluviale(null);
          storicoParticellaVO.setFasciaFluviale(null);
					storicoParticellaVO.setIdIrrigazione(null);
					storicoParticellaVO.setTipoIrrigazioneVO(null);
          storicoParticellaVO.setIdPotenzialitaIrrigua(null);
          storicoParticellaVO.setTipoPotenzialitaIrriguaVO(null);
          storicoParticellaVO.setIdTerrazzamento(null);
          storicoParticellaVO.setTipoTerrazzamentoVO(null);
          storicoParticellaVO.setIdRotazioneColturale(null);
          storicoParticellaVO.setTipoRotazioneColturaleVO(null);
          storicoParticellaVO.setIdAreaH(null);
          storicoParticellaVO.setAreaH(null);
          storicoParticellaVO.setIdAreaM(null);
          storicoParticellaVO.setAreaM(null);
          storicoParticellaVO.setPercentualePendenzaMedia(null);
          storicoParticellaVO.setGradiPendenzaMedia(null);
          storicoParticellaVO.setGradiEsposizioneMedia(null);
          storicoParticellaVO.setMetriAltitudineMedia(null);
          storicoParticellaVO.setIdMetodoIrriguo(null);
          storicoParticellaVO.setTipoMetodoIrriguo(null); 
					storicoParticellaVO.getElencoConduzioni()[0].setSuperficieCondotta(null);
          storicoParticellaVO.getElencoConduzioni()[0].setPercentualePossesso(null);
					storicoParticellaVO.getElencoConduzioni()[0].setSuperficieAgronomica(null);
					isCertificata = false;
					storicoParticellaVO.setParticellaCertificataVO(null);
					
					//nel caso esistesse il foglio....
					if(isPiemontese)
					{
					  storicoParticellaVO.setvValoriTipoArea(vTipoValoreAreaFoglio);
					}
					//carico Tutte Le aree
					else
					{
					  //potrei arrivare dall'indietro
					  if(storicoParticellaVO.getvValoriTipoArea() == null)
					  {
					    storicoParticellaVO.setvValoriTipoArea(gaaFacadeClient.getAllValoriTipoArea());
					  }
					}
				}
			}
			
			
			if(foglioVO != null) 
      {
				storicoParticellaVO.setFoglioVO(foglioVO);
			}
			// Il Foglio può essere null nel caso in cui l'utente abbia scelto
			// un comune non piemontese
			else 
      {
				foglioVO = new FoglioVO();
				foglioVO.setDescrizioneAreaE("sconosciuta");
				foglioVO.setDescrizioneAreaF("sconosciuta");
				storicoParticellaVO.setFoglioVO(foglioVO);
			}
			
			
			
			// Se la particella inserita non risulta censita in archivio 
			if(storicoParticellaVO.getIdStoricoParticella() == null) 
      {
        // Se l'evento scelto non è "nuova particella"
        if(idEvento.compareTo(SolmrConstants.ID_EVENTO_NUOVA_PARTICELLA) != 0) 
        {
          //in caso di accorpamento/frazionamento prendo i dati della particella "madre" (la prima se ce ne sono di più!!!)
          //if messo per sicurezza ma non dovrebbe mai accadere!!!
          if((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento") != null 
             && ((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento")).length > 0)
          {
            StoricoParticellaVO storicoParticellaPadreVO = (StoricoParticellaVO)((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento"))[0];
            storicoParticellaVO.setvValoriTipoArea(gaaFacadeClient.getValoriTipoAreaParticella(storicoParticellaPadreVO.getIdParticella(), null));
            storicoParticellaVO.setIdAreaA(storicoParticellaPadreVO.getIdAreaA());
		        storicoParticellaVO.setIdAreaB(storicoParticellaPadreVO.getIdAreaB());
		        storicoParticellaVO.setIdAreaC(storicoParticellaPadreVO.getIdAreaC());
		        storicoParticellaVO.setIdAreaD(storicoParticellaPadreVO.getIdAreaD());
		        storicoParticellaVO.setIdAreaM(storicoParticellaPadreVO.getIdAreaM());
		        storicoParticellaVO.setIdAreaG(storicoParticellaPadreVO.getIdAreaG()); 
		        storicoParticellaVO.setIdAreaH(storicoParticellaPadreVO.getIdAreaH()); 
		        storicoParticellaVO.setAreaG(storicoParticellaPadreVO.getAreaG());  
		        storicoParticellaVO.setAreaH(storicoParticellaPadreVO.getAreaH()); 
		        storicoParticellaVO.setIdAreaI(storicoParticellaPadreVO.getIdAreaI());
		        storicoParticellaVO.setIdAreaL(storicoParticellaPadreVO.getIdAreaL());
		        storicoParticellaVO.setAreaI(storicoParticellaPadreVO.getAreaI());
		        storicoParticellaVO.setAreaL(storicoParticellaPadreVO.getAreaL());
		        storicoParticellaVO.setIdCasoParticolare(storicoParticellaPadreVO.getIdCasoParticolare());
		        storicoParticellaVO.setFlagCaptazionePozzi(storicoParticellaPadreVO.getFlagCaptazionePozzi());
		        storicoParticellaVO.setZonaAltimetrica(storicoParticellaPadreVO.getZonaAltimetrica());
		        storicoParticellaVO.setIdZonaAltimetrica(storicoParticellaPadreVO.getIdZonaAltimetrica());
		        storicoParticellaVO.setFlagIrrigabile(storicoParticellaPadreVO.getFlagIrrigabile());
		        storicoParticellaVO.setIdFasciaFluviale(storicoParticellaPadreVO.getIdFasciaFluviale());
		        storicoParticellaVO.setFasciaFluviale(storicoParticellaPadreVO.getFasciaFluviale());
		        storicoParticellaVO.setIdIrrigazione(storicoParticellaPadreVO.getIdIrrigazione());
		        storicoParticellaVO.setTipoIrrigazioneVO(storicoParticellaPadreVO.getTipoIrrigazioneVO()); 
		        storicoParticellaVO.setIdPotenzialitaIrrigua(storicoParticellaPadreVO.getIdPotenzialitaIrrigua());
		        storicoParticellaVO.setTipoPotenzialitaIrriguaVO(storicoParticellaPadreVO.getTipoPotenzialitaIrriguaVO());
		        storicoParticellaVO.setIdTerrazzamento(storicoParticellaPadreVO.getIdTerrazzamento());
		        storicoParticellaVO.setTipoTerrazzamentoVO(storicoParticellaPadreVO.getTipoTerrazzamentoVO());
		        storicoParticellaVO.setIdRotazioneColturale(storicoParticellaPadreVO.getIdRotazioneColturale());
		        storicoParticellaVO.setTipoRotazioneColturaleVO(storicoParticellaPadreVO.getTipoRotazioneColturaleVO());
		        storicoParticellaVO.setPercentualePendenzaMedia(storicoParticellaPadreVO.getPercentualePendenzaMedia());
            storicoParticellaVO.setGradiPendenzaMedia(storicoParticellaPadreVO.getGradiPendenzaMedia());
            storicoParticellaVO.setGradiEsposizioneMedia(storicoParticellaPadreVO.getGradiEsposizioneMedia());
            storicoParticellaVO.setMetriAltitudineMedia(storicoParticellaPadreVO.getMetriAltitudineMedia());
            storicoParticellaVO.setIdMetodoIrriguo(storicoParticellaPadreVO.getIdMetodoIrriguo());
            storicoParticellaVO.setTipoMetodoIrriguo(storicoParticellaPadreVO.getTipoMetodoIrriguo());
          } 
          
        }
        else
        {
					if(foglioVO != null) 
	        {
	          if(isPiemontese)
	            storicoParticellaVO.setvValoriTipoArea(vTipoValoreAreaFoglio);
						// Setto i tipi area della particella con quelli del foglio a cui fa riferimento
						storicoParticellaVO.setIdAreaA(foglioVO.getIdAreaA());
						storicoParticellaVO.setIdAreaB(foglioVO.getIdAreaB());
						storicoParticellaVO.setIdAreaC(foglioVO.getIdAreaC());
						storicoParticellaVO.setIdAreaD(foglioVO.getIdAreaD());
	          storicoParticellaVO.setIdAreaG(foglioVO.getIdAreaG()); //Modifica GAA09-48 Inserisci particella - v7
	          if(Validator.isNotEmpty(foglioVO.getIdAreaG()))
	          {
	            CodeDescription codeG = new CodeDescription();
	            codeG.setCode(new Integer(foglioVO.getIdAreaG().intValue()));
	            codeG.setDescription(foglioVO.getDescrizioneAreaG());
	            storicoParticellaVO.setAreaG(codeG);
	          }
	          storicoParticellaVO.setIdAreaI(foglioVO.getIdAreaI());
	          if(Validator.isNotEmpty(foglioVO.getIdAreaI()))
	          {
	            CodeDescription codeI = new CodeDescription();
	            codeI.setCode(new Integer(foglioVO.getIdAreaI().intValue()));
	            codeI.setDescription(foglioVO.getDescrizioneAreaI());
	            storicoParticellaVO.setAreaI(codeI);
	          }
	          storicoParticellaVO.setIdAreaL(foglioVO.getIdAreaL());
	          if(Validator.isNotEmpty(foglioVO.getIdAreaL()))
	          {
	            CodeDescription codeL = new CodeDescription();
	            codeL.setCode(new Integer(foglioVO.getIdAreaL().intValue()));
	            codeL.setDescription(foglioVO.getDescrizioneAreaL());
	            storicoParticellaVO.setAreaL(codeL);
	          }
					}
					else 
	        {
						// Setto i tipi area della particella a null per gestire correttamente la navigazione avanti/indietro
						storicoParticellaVO.setIdAreaA(null);
						storicoParticellaVO.setIdAreaB(null);
						storicoParticellaVO.setIdAreaC(null);
	          storicoParticellaVO.setIdAreaD(null);
						storicoParticellaVO.setIdAreaG(null);
	          storicoParticellaVO.setIdAreaI(null);
	          storicoParticellaVO.setIdAreaL(null);
					}
				}
			}
			
			
			if(idEvento.compareTo(SolmrConstants.ID_EVENTO_NUOVA_PARTICELLA) != 0) 
      {
        //in caso di accorpamento/frazionamento prendo i dati della particella "madre" (la prima se ce ne sono di più!!!)
        //if messo per sicurezza ma non dovrebbe mai accadere!!!
        if((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento") != null 
           && ((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento")).length > 0)
        {
          StoricoParticellaVO storicoParticellaPadreVO = (StoricoParticellaVO)((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento"))[0];
          storicoParticellaVO.setZonaAltimetrica(storicoParticellaPadreVO.getZonaAltimetrica());
          storicoParticellaVO.setIdZonaAltimetrica(storicoParticellaPadreVO.getIdZonaAltimetrica());        
        }
      }
      else
      {
				// Se non ho reperito la zona altimetrica
	      //Modifica GAA09-48 Inserisci particella - v6
	      if(storicoParticellaVO.getZonaAltimetrica() == null) 
	      {
	        // idZonaAltimetrica lo prendo da DB_FOGLIO!!!
	        //Se "foglioVO !=null" (indica che il comune è piemontese) e idZonaAltimetrica (quest'ultimo più che altro per evitare null pointer) 
	        //sono  diversi da null dovremmo essere in un comune piemontese Spero!!!
	        if((foglioVO !=null) &&
	          (foglioVO.getIdZonaAltimetrica() !=null))
	        {
	          CodeDescription zonaAltimetrica = new CodeDescription();    
	          
	          try 
	          {
	            String descZonaAltimetrica = anagFacadeClient.getTipoZonaAltimetrica(Integer.decode(foglioVO.getIdZonaAltimetrica().toString()));
	            zonaAltimetrica.setCode(Integer.decode(foglioVO.getIdZonaAltimetrica().toString()));
	            zonaAltimetrica.setDescription(descZonaAltimetrica);
	            storicoParticellaVO.setZonaAltimetrica(zonaAltimetrica);
	            storicoParticellaVO.setIdZonaAltimetrica(Long.decode(foglioVO.getIdZonaAltimetrica().toString()));
	          }
	          catch(SolmrException se) 
	          {
	            error = new ValidationError(AnagErrors.ERRORE_KO_ZONA_ALTIMETRICA);
	            errors.add("motivazione", error);
	            request.setAttribute("errors", errors);
	            request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
	            return;
	          }
	        }
	      }  
	      //**********************************************
	      
	      // Fatto prima del "Modifica GAA09-48 Inserisci particella - v6" se non era ancora valorizzata
	      // la zona altimetrica.
	      // Se non ho reperito la zona altimetrica perchè sono un comune non piemontese!!
				if(storicoParticellaVO.getZonaAltimetrica() == null) 
	      {
					// La ricerco in funzione del comune inserito dall'utente
					CodeDescription zonaAltimetrica = new CodeDescription();
					try 
	        {
						String descZonaAltimetrica = anagFacadeClient.getTipoZonaAltimetrica(Integer.decode(comuneVO.getZonaAlt().toString()));
						zonaAltimetrica.setCode(Integer.decode(comuneVO.getZonaAlt().toString()));
						zonaAltimetrica.setDescription(descZonaAltimetrica);
						storicoParticellaVO.setZonaAltimetrica(zonaAltimetrica);
						storicoParticellaVO.setIdZonaAltimetrica(Long.decode(comuneVO.getZonaAlt().toString()));
					}
					catch(SolmrException se) 
	        {
						error = new ValidationError(AnagErrors.ERRORE_KO_ZONA_ALTIMETRICA);
					 	errors.add("motivazione", error);
					 	request.setAttribute("errors", errors);
			 	   	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
			 	   	return;
					}
				}
		  }
			// Sovrascrivo l'oggetto in sessione
			session.setAttribute("storicoParticellaVO", storicoParticellaVO);
		}
		if(isCertificata) 
    {
			session.setAttribute("isCertificata", SolmrConstants.FLAG_S);
		}
		else //la superficie grafica non è > 0 
    {
      session.removeAttribute("isCertificata");
    }
		// Vado al secondo step(conduzione e utilizzi)
		String valoreNullo = null;
		%>
	    	<jsp:forward page= "<%= terreniParticellareInserisciCondUsoCtrlUrl %>" >
	    		<jsp:param name="idEvento" value="<%= idEvento %>"/>
	    		<jsp:param name="inserimento" value="<%= inserimento %>"/>
	    		<jsp:param name="provvisoria" value="<%= provvisoria %>"/>	    		
	    	</jsp:forward>
	  	<%
	  	return;
 	}
 	// L'utente ha selezionato il pulsante inserisci
 	if(request.getParameter("inserisci") != null) 
  {
 		// Recupero i parametri relativi alla nuova particella
 		if(storicoParticellaVO == null) 
    {
 			storicoParticellaVO = new StoricoParticellaVO();
 			storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{new ConduzioneParticellaVO()});
 		}
 		comuneVO = new ComuneVO();
 		ProvinciaVO provinciaVO = new ProvinciaVO();
 		String flagEstero = null;
 		String ute = request.getParameter("idUte");
 		String siglaProvincia = request.getParameter("siglaProvincia");
 		request.setAttribute("siglaProvincia", siglaProvincia);
 		String descComune = request.getParameter("descComune");
 		if(Validator.isNotEmpty(descComune))
 		  descComune = descComune.trim(); 		  
 		request.setAttribute("descComune", descComune); 		
 		
 		String sezione = request.getParameter("sezione");
 		String foglio = request.getParameter("foglio");
 		String particella = request.getParameter("particella");
 		boolean isProvvisoria = request.getParameter("provvisoria") != null;
 		if(isProvvisoria) 
    {
 			request.setAttribute("provvisoria", SolmrConstants.FLAG_S);
 		}
 		String subalterno = request.getParameter("subalterno");
    if(Validator.isNotEmpty(ute))
    {
 		  storicoParticellaVO.getElencoConduzioni()[0].setIdUte(Long.decode(ute));
    }
 		flagEstero = SolmrConstants.FLAG_N;
		provinciaVO.setSiglaProvincia(siglaProvincia);
		comuneVO.setProvinciaVO(provinciaVO);
		comuneVO.setDescom(descComune);
		comuneVO.setFlagEstero(flagEstero);
		ComuneVO comuneConfronto = null;
    try 
    {
      comuneConfronto = anagFacadeClient.getComuneByParameters(comuneVO.getDescom(), provinciaVO.getSiglaProvincia(), null, SolmrConstants.FLAG_S, null);
      if(comuneConfronto != null) 
      {
        storicoParticellaVO.setIstatComune(comuneConfronto.getIstatComune());
      }
    }
    catch(SolmrException se) 
    {}
		storicoParticellaVO.setComuneParticellaVO(comuneVO);
		
		storicoParticellaVO.setSezione(sezione);
		storicoParticellaVO.setFoglio(foglio);
		storicoParticellaVO.setParticella(particella);
		storicoParticellaVO.setSubalterno(subalterno);
		// Recupero i parametri relativi ai filtri di ricerca e li metto in request
		String provinciaEvento = request.getParameter("provinciaEvento");
		String comuneEvento = request.getParameter("comuneEvento");
		if(Validator.isNotEmpty(comuneEvento))
		  comuneEvento = comuneEvento.trim();
		String sezioneEvento = request.getParameter("sezioneEvento");
		String foglioEvento = request.getParameter("foglioEvento");
		String particellaEvento = request.getParameter("particellaEvento");
		request.setAttribute("provinciaEvento", provinciaEvento);
		request.setAttribute("comuneEvento", comuneEvento);
		request.setAttribute("sezioneEvento", sezioneEvento);
		request.setAttribute("foglioEvento", foglioEvento);
		request.setAttribute("particellaEvento", particellaEvento);
		
		// Inserisco l'oggetto in sessione nel caso in cui sia la prima volta
		// che acceda alla pagina senza aver ancora mai confermato i dati
		session.setAttribute("storicoParticellaVO", storicoParticellaVO);
		
		// Se nessuno dei parametri è stato inserito
		if(!Validator.isNotEmpty(provinciaEvento) 
      && !Validator.isNotEmpty(comuneEvento) && !Validator.isNotEmpty(sezioneEvento) 
      && !Validator.isNotEmpty(foglioEvento) && !Validator.isNotEmpty(particellaEvento)) 
   {
			error = new ValidationError(AnagErrors.ERRORE_MOTIVAZIONE_PARTICELLE_ERRATA_NO_PARAMETERS);
		 	errors.add("motivazione", error);
		 	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
     	return;
		}
		
		// Cerco la particella in funzione dei filtri indicati
		ComuneVO comuneEventoVO = null;
		if(Validator.isNotEmpty(comuneEvento)) 
    {
			try 
      {
				comuneEventoVO = anagFacadeClient.getComuneByParameters(comuneEvento, provinciaEvento, null, SolmrConstants.FLAG_S, null);
			}
			catch(SolmrException se) {
				error = new ValidationError(AnagErrors.ERRORE_KO_SEARCH_PARTICELLE);
			 	errors.add("motivazione", error);
			 	request.setAttribute("errors", errors);
 	     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
 	     	return;
			}
		}
		
		// Verifico la correttezza dei parametri "foglio" e "particella" prima di
		// effettuare la ricerca
		if((Validator.isNotEmpty(foglioEvento) && !Validator.isNumericInteger(foglioEvento)) 
    || (Validator.isNotEmpty(particellaEvento) 
    && !Validator.isNumericInteger(particellaEvento))) 
    {
			error = new ValidationError(AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
		 	errors.add("motivazione", error);
		 	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
     	return;
		}
		// Se sono corretti proseguo con l'inserimento
		else 
    {
			StoricoParticellaVO[] storicoParticellaElencoVO = null;
			try 
      {
				if(comuneEventoVO != null) 
        {
					storicoParticellaElencoVO = anagFacadeClient.getListStoricoParticellaVOByParameters(comuneEventoVO.getIstatComune(), sezioneEvento, foglioEvento, particellaEvento, null, true, null, anagAziendaVO.getIdAzienda());
				}
				else 
        {
					storicoParticellaElencoVO = anagFacadeClient.getListStoricoParticellaVOByParameters(null, sezioneEvento, foglioEvento, particellaEvento, null, true, null, anagAziendaVO.getIdAzienda());
				}
			}
			catch(SolmrException se) 
      {
				if(se.getMessage().equalsIgnoreCase(AnagErrors.ERRORE_FILTRI_GENERICI)) 
        {
					error = new ValidationError(se.getMessage());
				 	errors.add("motivazione", error);
				 	request.setAttribute("errors", errors);
	 	     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
	 	     	return;
				}
				else 
        {
					error = new ValidationError(AnagErrors.ERRORE_KO_SEARCH_PARTICELLE);
				 	errors.add("motivazione", error);
				 	request.setAttribute("errors", errors);
	 	     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
	 	     	return;
				}
			}
			// Se non la trovo indico all'utente che non esistono particelle relative
			// ai filtri indicati
			if(storicoParticellaElencoVO == null || storicoParticellaElencoVO.length == 0) 
      {
				error = new ValidationError(AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND);
			 	errors.add("motivazione", error);
			 	request.setAttribute("errors", errors);
 	     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
 	     	return;
			}
			// Se ne trovo più di una avviso l'utente che non è stato possibile identificare
			// univocamente la particella
			else if(storicoParticellaElencoVO.length > 1) {
				error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_NON_IDENTIFICATA_UNIVOCAMENTE);
			 	errors.add("motivazione", error);
			 	request.setAttribute("errors", errors);
	 	     	request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
	 	     	return;
			}
			// Altrimenti la inserisco
			else 
      {
				StoricoParticellaVO[] elencoStoricoEvento = (StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento");
				Vector<StoricoParticellaVO> temp = new Vector<StoricoParticellaVO>();
		    if(elencoStoricoEvento != null && elencoStoricoEvento.length > 0) 
        {
		    	for(int i = 0; i < elencoStoricoEvento.length; i++) 
          {
		    		temp.add((StoricoParticellaVO)elencoStoricoEvento[i]);
		    	}
		    }
			  boolean isCensite = false;
				if(temp.size() == 0) 
        {
			    temp.add((StoricoParticellaVO)storicoParticellaElencoVO[0]);
			  }
				else 
        {
					int countParticelleGiaCensite = 0;
		    	for(int j = 0; j < temp.size(); j++) 
          {
	        	StoricoParticellaVO storicoVO = (StoricoParticellaVO)temp.elementAt(j);
	          if(storicoVO.getIdStoricoParticella().compareTo(((StoricoParticellaVO)storicoParticellaElencoVO[0]).getIdStoricoParticella()) == 0) 
            {
             	countParticelleGiaCensite++;
             	isCensite = true;
             	break;
           	}
		    	}
	        if(countParticelleGiaCensite == 0) 
          {
	        	temp.add((StoricoParticellaVO)storicoParticellaElencoVO[0]);
	        }
			  }
				if(isCensite) 
        {
		    	error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_GIA_INSERITO);
		     	errors = new ValidationErrors();
		     	errors.add("error", error);
		     	request.setAttribute("errors", errors);
		   	}
				else 
        {
					session.setAttribute("elencoStoricoEvento", (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]));
				}
			}
		}
 	}
 	// L'utente ha selezionato il pulsante elimina
 	if(request.getParameter("elimina") != null) 
  {
		// Recupero i parametri relativi alla nuova particella
 		if(storicoParticellaVO == null) 
    {
 			storicoParticellaVO = new StoricoParticellaVO();
 			storicoParticellaVO.setElencoConduzioni(new ConduzioneParticellaVO[]{new ConduzioneParticellaVO()});
 		}
 		comuneVO = new ComuneVO();
 		ProvinciaVO provinciaVO = new ProvinciaVO();
 		String flagEstero = null;
 		String ute = request.getParameter("idUte");
 		String siglaProvincia = request.getParameter("siglaProvincia");
 		request.setAttribute("siglaProvincia", siglaProvincia);
 		String descComune = request.getParameter("descComune");
 		request.setAttribute("descComune", descComune);
 		String sezione = request.getParameter("sezione");
 		String foglio = request.getParameter("foglio");
 		String particella = request.getParameter("particella");
 		boolean isProvvisoria = request.getParameter("provvisoria") != null;
 		if(isProvvisoria) {
 			request.setAttribute("provvisoria", SolmrConstants.FLAG_S);
 		}
 		String subalterno = request.getParameter("subalterno");
    if(Validator.isNotEmpty(ute))
    { 
 		  storicoParticellaVO.getElencoConduzioni()[0].setIdUte(Long.decode(ute));
    }
 		flagEstero = SolmrConstants.FLAG_N;
		provinciaVO.setSiglaProvincia(siglaProvincia);
		comuneVO.setProvinciaVO(provinciaVO);
		comuneVO.setDescom(descComune);
		comuneVO.setFlagEstero(flagEstero);
		ComuneVO comuneConfronto = null;
    try 
    {
      comuneConfronto = anagFacadeClient.getComuneByParameters(comuneVO.getDescom(), provinciaVO.getSiglaProvincia(), null, SolmrConstants.FLAG_S, null);
      if(comuneConfronto != null) 
      {
        storicoParticellaVO.setIstatComune(comuneConfronto.getIstatComune());
      }
    }
    catch(SolmrException se) 
    {}
		storicoParticellaVO.setComuneParticellaVO(comuneVO);
		storicoParticellaVO.setSezione(sezione);
		storicoParticellaVO.setFoglio(foglio);
		storicoParticellaVO.setParticella(particella);
		storicoParticellaVO.setSubalterno(subalterno);
		// Recupero i parametri relativi ai filtri di ricerca e li metto in request
		String provinciaEvento = request.getParameter("provinciaEvento");
		String comuneEvento = request.getParameter("comuneEvento");
		String sezioneEvento = request.getParameter("sezioneEvento");
		String foglioEvento = request.getParameter("foglioEvento");
		String particellaEvento = request.getParameter("particellaEvento");
		request.setAttribute("provinciaEvento", provinciaEvento);
		request.setAttribute("comuneEvento", comuneEvento);
		request.setAttribute("sezioneEvento", sezioneEvento);
		request.setAttribute("foglioEvento", foglioEvento);
		request.setAttribute("particellaEvento", particellaEvento);
 		
		// Recupero gli elementi da eliminare
    String[] elementsToRemove = request.getParameterValues("idParticella");
   	// Controllo che sia stato selezionato qualcosa dall'utente
   	if(elementsToRemove == null || elementsToRemove.length == 0) {
     		errors.add("error", new ValidationError(AnagErrors.ERRORE_NO_ELEMENTI_SELEZIONATI));
     		request.setAttribute("errors", errors);
     		request.getRequestDispatcher(terreniParticellareInserisciUrl).forward(request, response);
     		return;
   	}
   	// Se sì, elimino gli elementi selezionati
   	else 
    {
    	if(elementsToRemove.length == ((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento")).length) 
      {
      	session.removeAttribute("elencoStoricoEvento");
    	}
     	else 
      {
   			Hashtable<Long,StoricoParticellaVO> hash = new Hashtable<Long,StoricoParticellaVO>();
     		for(int i = 0; i < ((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento")).length; i++) 
        {
       		StoricoParticellaVO storicoParticellaRemoveVO = (StoricoParticellaVO)((StoricoParticellaVO[])session.getAttribute("elencoStoricoEvento"))[i];
       		hash.put(storicoParticellaRemoveVO.getIdParticella(), storicoParticellaRemoveVO);
     		}
     		for(int j = 0; j < elementsToRemove.length; j++) 
        {
       		hash.remove(Long.decode((String)elementsToRemove[j]));
     		}
     		Vector<StoricoParticellaVO> temp = new Vector<StoricoParticellaVO>();
     		Enumeration<StoricoParticellaVO> enumeration = hash.elements();
     		while(enumeration.hasMoreElements()) 
        {
       		temp.add((StoricoParticellaVO)enumeration.nextElement());
     		}
		    // Metto il vettore in sessione
        session.setAttribute("elencoStoricoEvento", (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]));
   		}			
   	}
 	}
	
	// Vado alla pagina di inserimento
	%>
		<jsp:forward page="<%=terreniParticellareInserisciUrl%>" />
	<%

%>
