<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smranag.smrgaa.dto.SuperficieDescription" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
	
	String iridePageName = "terreniParticellareModificaCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	String terreniParticellareModificaUrl = "/view/terreniParticellareModificaView.jsp";
	String terreniParticellareElencoCtrlUrl = "/ctrl/terreniParticellareElencoCtrl.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	ValidationError error = null;
	String messaggioErrore = null;
	
	HashMap<String,Vector<Long>> numParticelleSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numParticelleSelezionate");
	HashMap<String,Vector<Long>> numUtilizziSelezionati = (HashMap<String,Vector<Long>>)session.getAttribute("numUtilizziSelezionati");
	String[] idConduzione = request.getParameterValues("idConduzione");
	String[] elencoIdUtilizzo = request.getParameterValues("idUtilizzo");
	String numPagina = request.getParameter("pagina");
	String indice = (String)session.getAttribute("indice");
	Vector<Long> elencoIdDaModificare = new Vector<Long>();
	Vector<StoricoParticellaVO> temp = null;
  //raccoglie tutti gli idParticella per ricavare gli utilizzi fittizi
  Vector<Long> vIdParticella = null;
  //utilizzat aper caricare utilizzi attivi....senza matrice...
  Vector<Long> vIdUtilizziAttivi = new Vector<Long>();
  Vector<Long> vIdUtilizziAttiviSecondari = new Vector<Long>();
  
	
	// Recupero il valore della pagina dell'elenco da cui ho cliccato il pulsante modifica multipla
	String pagina = request.getParameter("pagina");
	if(Validator.isNotEmpty(pagina)) {
		session.setAttribute("pagina", pagina);
	}
	
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
	String parametroRTMo = null;
	try 
	{
		parametroRTMo = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RTMO);;
	}
	catch(SolmrException se) 
	{
		messaggioErrore = (String)AnagErrors.ERRORE_KO_PARAMETRO_RTMO;
		request.setAttribute("messaggioErrore", messaggioErrore);
		%>
			<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
		<%
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
				Long elemento = (Long)selezioni.elementAt(a);
				if(elencoIdDaModificare.size() == 0 || !elencoIdDaModificare.contains(elemento)) 
				{
					elencoIdDaModificare.add(elemento);
				}
			}
		}
	}
	if(elencoIdDaModificare.size() > Integer.parseInt(parametroRTMo)) 
	{
		error = new ValidationError(AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1+parametroRTMo+AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2);
		request.setAttribute("error", error);
		%>
			<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" />
		<%
	}
	
	// L'utente ha cliccato il pulsante annulla
 	if(request.getParameter("annulla") != null) 
 	{
		// Torno alla pagina di ricerca/elenco
 	 	String valorePagina = (String)session.getAttribute("pagina");
		%>
			<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" >
				<jsp:param name="pagina" value="<%= valorePagina %>" /> 
			</jsp:forward>
		<%
 	}
 	else 
  {
		// Verifico che tra le particelle selezionate dall'utente non ve ne sia nessuna
		// con data_fine_conduzione valorizzata
		StoricoParticellaVO storicoParticellaVO = null;
		StoricoParticellaVO[] elencoStoricoParticella = (StoricoParticellaVO[])session.getAttribute("elencoStoricoParticella");
		if(elencoStoricoParticella == null) 
    {
			boolean isErrato = false;
			try 
      {
				temp = new Vector<StoricoParticellaVO>();
				Long idConduzioneParticella = null;
				for(int i = 0; i < elencoIdDaModificare.size(); i++) 
        {
					idConduzioneParticella = (Long)elencoIdDaModificare.elementAt(i);
					storicoParticellaVO = anagFacadeClient.getDettaglioParticella(filtriParticellareRicercaVO, idConduzioneParticella);
          
          if(vIdParticella == null)
          {
            vIdParticella = new Vector<Long>();
          }
          if(!vIdParticella.contains(storicoParticellaVO.getIdParticella()))
          {
            vIdParticella.add(storicoParticellaVO.getIdParticella());
          }
					// Setto a null i dati relativi al documento protocollato, alla causale
					// modifica particella e al motivo modifica in modo da poterli gestire
					// nella pop-up di modifica dei dati territoriali della particella
					storicoParticellaVO.setIdDocumentoProtocollato(null);
					storicoParticellaVO.setIdCausaleModParticella(null);
					storicoParticellaVO.setMotivoModifica(null);
					if(storicoParticellaVO.getElencoConduzioni()[0].getDataFineConduzione() != null) 
          {
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
					error = new ValidationError(AnagErrors.ERRORE_KO_MODIFICA_MULTIPLA_FOR_CONDUZIONI_STORICIZZATE + messaggio);
					request.setAttribute("error", error);
					%>
						<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" />
					<%
				}
				elencoStoricoParticella = (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]);
			}
			catch(SolmrException se) 
			{
				messaggioErrore = (String)AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
				request.setAttribute("messaggioErrore", messaggioErrore);
				%>
					<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
				<%
			}
		}
		else 
    {
	    String[] usiPrimari = request.getParameterValues("idTipoUtilizzo");
      String[] usiSecondari = request.getParameterValues("idTipoUtilizzoSecondario");
      String[] destinazionePrimari = request.getParameterValues("idTipoDestinazione");
      String[] destinazioneSecondari = request.getParameterValues("idTipoDestinazioneSecondario");
      String[] tipoDettPrimari = request.getParameterValues("idTipoDettaglioUso");
      String[] tipoDettSecondari = request.getParameterValues("idTipoDettaglioUsoSecondario");
      String[] tipoQualitaPrimari = request.getParameterValues("idTipoQualitaUso");
      String[] tipoQualitaSecondari = request.getParameterValues("idTipoQualitaUsoSecondario");
      String[] varietaPrimari = request.getParameterValues("idVarieta");
      String[] varietaSecondari = request.getParameterValues("idVarietaSecondaria");
      
      String[] tipoSeminaPrimari = request.getParameterValues("idTipoSemina");
      String[] tipoSeminaSecondari = request.getParameterValues("idTipoSeminaSecondario");
      String[] tipoPeriodoSeminaPrimari = request.getParameterValues("idTipoPeriodoSemina");
      String[] tipoPeriodoSeminaSecondari = request.getParameterValues("idTipoPeriodoSeminaSecondario");
      
      String[] dataInizioDestinazionePrimari = request.getParameterValues("dataInizioDestinazione");
      String[] dataFineDestinazionePrimari = request.getParameterValues("dataFineDestinazione");
      String[] dataInizioDestinazioneSecondari = request.getParameterValues("dataInizioDestinazioneSec");
      String[] dataFineDestinazioneSecondari = request.getParameterValues("dataFineDestinazioneSec");
      
      int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storicoVO.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(Validator.isEmpty(utilizzoVO.getDichiarabileEfa())
             || (Validator.isNotEmpty(utilizzoVO.getDichiarabileEfa()) 
                 && !"S".equalsIgnoreCase(utilizzoVO.getDichiarabileEfa())))
          {
          
	          if(usiPrimari != null && Validator.isNotEmpty(usiPrimari[contatore])) 
	          {
	            utilizzoVO.setIdUtilizzo(Long.decode(usiPrimari[contatore]));
	            utilizzoVO.setIdTipoDestinazione(Long.decode(destinazionePrimari[contatore]));
	            utilizzoVO.setIdTipoDettaglioUso(Long.decode(tipoDettPrimari[contatore]));
	            utilizzoVO.setIdTipoQualitaUso(Long.decode(tipoQualitaPrimari[contatore]));
	            utilizzoVO.setIdVarieta(Long.decode(varietaPrimari[contatore]));
	            utilizzoVO.setIdSemina(Long.decode(tipoSeminaPrimari[contatore]));
	            utilizzoVO.setIdTipoPeriodoSemina(Long.decode(tipoPeriodoSeminaPrimari[contatore]));
	            utilizzoVO.setDataInizioDestinazioneStr(dataInizioDestinazionePrimari[contatore]);
	            utilizzoVO.setDataFineDestinazioneStr(dataFineDestinazionePrimari[contatore]);
	          }
	          else 
	          {
	            utilizzoVO.setIdUtilizzo(null);
	            utilizzoVO.setIdTipoDestinazione(null);
	            utilizzoVO.setIdTipoDettaglioUso(null);
              utilizzoVO.setIdTipoQualitaUso(null);
              utilizzoVO.setIdVarieta(null);
              utilizzoVO.setIdSemina(null);
              utilizzoVO.setIdTipoPeriodoSemina(null);
              utilizzoVO.setDataInizioDestinazioneStr(null);
              utilizzoVO.setDataFineDestinazioneStr(null);
              
	          }
	          
	                     
                   
            
	          if(usiSecondari != null && Validator.isNotEmpty(usiSecondari[contatore])) 
	          {
	            utilizzoVO.setIdUtilizzoSecondario(Long.decode(usiSecondari[contatore]));
	            utilizzoVO.setIdTipoDestinazioneSecondario(Long.decode(destinazioneSecondari[contatore]));
              utilizzoVO.setIdTipoDettaglioUsoSecondario(Long.decode(tipoDettSecondari[contatore]));
              utilizzoVO.setIdTipoQualitaUsoSecondario(Long.decode(tipoQualitaSecondari[contatore]));
              utilizzoVO.setIdVarietaSecondaria(Long.decode(varietaSecondari[contatore]));
              utilizzoVO.setIdSeminaSecondario(Long.decode(tipoSeminaSecondari[contatore]));
              utilizzoVO.setIdTipoPeriodoSeminaSecondario(Long.decode(tipoPeriodoSeminaSecondari[contatore]));
              utilizzoVO.setDataInizioDestinazioneSecStr(dataInizioDestinazioneSecondari[contatore]);
              utilizzoVO.setDataFineDestinazioneSecStr(dataFineDestinazioneSecondari[contatore]);
	          }
	          else 
	          {
	            utilizzoVO.setIdUtilizzoSecondario(null);
	            utilizzoVO.setIdTipoDestinazioneSecondario(null);
              utilizzoVO.setIdTipoDettaglioUsoSecondario(null);
              utilizzoVO.setIdTipoQualitaUsoSecondario(null);
              utilizzoVO.setIdVarietaSecondaria(null);
              utilizzoVO.setIdSeminaSecondario(null);
              utilizzoVO.setIdTipoPeriodoSeminaSecondario(null);
              utilizzoVO.setDataInizioDestinazioneSecStr(null);
              utilizzoVO.setDataFineDestinazioneSecStr(null);
	          }
	          
	          
            
            //il contatore è incrementato solo per quelli non disabilitati....
	          contatore++;
	        }
          
          utilizzo[a] = utilizzoVO;
        }
        storicoVO.getElencoConduzioni()[0].setElencoUtilizzi(utilizzo);
      }	
		}
		
		// Ricerco i tipi metodo irriguo
    Vector<TipoSeminaVO> vTipoSemina = null;
    try 
    {
      vTipoSemina = gaaFacadeClient.getElencoTipoSemina();
      request.setAttribute("vTipoSemina", vTipoSemina);
    }
    catch(SolmrException se) 
    {
      messaggioErrore = (String)AnagErrors.ERRORE_KO_TIPO_SEMINA;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
    }
    
    
    //é valorizzato solo la prima volta che entro...
    HashMap<Long,Vector<SuperficieDescription>> hSupElegFit = null;
    if(Validator.isNotEmpty(vIdParticella))
    {
      hSupElegFit = anagFacadeClient.getEleggibilitaTooltipByIdParticella(vIdParticella);
    }
    
    
    //Usato per inserite gli idConduzioni relativi alla particella presenti a video 
    HashMap<Long,Vector<Long>> hIdConduzioni = new HashMap<Long,Vector<Long>>();
    for(int i=0;i<elencoStoricoParticella.length;i++)
    {
      StoricoParticellaVO storicoParticellaTmpVO = (StoricoParticellaVO)elencoStoricoParticella[i];
      ConduzioneParticellaVO conduzioneVO = storicoParticellaTmpVO.getElencoConduzioni()[0];          
      if(hIdConduzioni.get(storicoParticellaTmpVO.getIdParticella()) != null)
      {
        Vector<Long> vIdConduzione = hIdConduzioni.get(storicoParticellaTmpVO.getIdParticella());
        vIdConduzione.add(conduzioneVO.getIdConduzioneParticella());
        hIdConduzioni.put(storicoParticellaTmpVO.getIdParticella(), vIdConduzione);
      }
      else
      {
        Vector<Long> vIdConduzione = new Vector<Long>();
        vIdConduzione.add(conduzioneVO.getIdConduzioneParticella());
        hIdConduzioni.put(storicoParticellaTmpVO.getIdParticella(), vIdConduzione);
      }
      
      if(Validator.isNotEmpty(hSupElegFit) 
        && (hSupElegFit.get(storicoParticellaTmpVO.getIdParticella()) != null))
      {
        storicoParticellaTmpVO.setvSupElegFit(hSupElegFit.get(storicoParticellaTmpVO.getIdParticella()));      
      }    
    }
    
		
		// Recupero i titoli di possesso
		it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = null;
		try 
		{
			elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
			request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
		}
		catch(SolmrException se) 
		{
			messaggioErrore = (String)AnagErrors.ERRORE_KO_TITOLO_POSSESSO;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
			<%
		}
		
		// Recupero i valori relativi ai tipi uso suolo utilizzati dall'azienda in esame
	 	Vector<TipoUtilizzoVO> elencoTipiUsoSuolo = (Vector<TipoUtilizzoVO>)session.getAttribute("elencoTipiUsoSuoloPrimario");
		if(elencoTipiUsoSuolo == null || elencoTipiUsoSuolo.size() == 0) 
    {
	 		try 
      {
		 		elencoTipiUsoSuolo = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), null);
		 		if((elencoTipiUsoSuolo != null) && (elencoTipiUsoSuolo.size() > 0))
		 		{
		 		  for(int g=0;g<elencoTipiUsoSuolo.size();g++)
		 		  {
		 		    vIdUtilizziAttivi.add(elencoTipiUsoSuolo.get(g).getIdUtilizzo());
		 		  }		 		
		 		}
		 		
		 		request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
        
	 		}
	 		catch(SolmrException se) {
				messaggioErrore = (String)AnagErrors.ERRORE_KO_USO_PRIMARIO;
				request.setAttribute("messaggioErrore", messaggioErrore);
				%>
					<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
				<%
	 		}
		}
		else 
		{
		  if((elencoTipiUsoSuolo != null) && (elencoTipiUsoSuolo.size() > 0))
      {
        for(int g=0;g<elencoTipiUsoSuolo.size();g++)
        {
          vIdUtilizziAttivi.add(elencoTipiUsoSuolo.get(g).getIdUtilizzo());
        }       
      }
			request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
		}
		
		// Carico gli utilizzi primari degli eventuali Efa
    HashMap<Long, TipoUtilizzoVO> elencoTipiUsoSuoloEfa = new HashMap<Long, TipoUtilizzoVO>();
    try 
    {
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storicoVO = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storicoVO.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          if("S".equalsIgnoreCase(utilizzo[a].getDichiarabileEfa()))
          {
            if(elencoTipiUsoSuoloEfa.get(utilizzo[a].getIdUtilizzo()) == null)
            {
              if(gaaFacadeClient.isUtilizzoAttivoSuMatrice(utilizzo[a].getIdUtilizzo()))
              {
                elencoTipiUsoSuoloEfa.put(utilizzo[a].getIdUtilizzo(), anagFacadeClient.findTipoUtilizzoByPrimaryKey(utilizzo[a].getIdUtilizzo()));
                vIdUtilizziAttivi.add(utilizzo[a].getIdUtilizzo());
              }
            }
          }
        }
      }       
      request.setAttribute("elencoTipiUsoSuoloEfa", elencoTipiUsoSuoloEfa);       
    }
    catch(SolmrException se) {
      messaggioErrore = (String)AnagErrors.ERRORE_KO_USO_PRIMARIO;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
    }
		
		
		Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazione = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzo(), vIdUtilizziAttivi)) 
          {
	          Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(utilizzoVO.getIdUtilizzo());
	          elencoDestinazione.put(new Integer(contatore), vDestinazione);
	        }
	        else
	        {
	          elencoDestinazione.put(new Integer(contatore), new Vector<TipoDestinazioneVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    request.setAttribute("elencoDestinazione", elencoDestinazione);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = (String)AnagErrors.ERRORE_KO_DESTINAZIONE_PRIMARIA;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }
	  
	  // Ricerco il dettaglio uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUso = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
	  try 
	  {
	    int contatore = 0;
	    for(int i = 0; i < elencoStoricoParticella.length; i++) 
	    {
	      StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
	      UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
	      for(int a = 0; a < utilizzo.length; a++) 
	      {
	        UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
	        if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzo(), vIdUtilizziAttivi))
	        {
	          Vector<TipoDettaglioUsoVO> vDettUso = null;
	          if(Validator.isNotEmpty(utilizzoVO.getIdTipoDestinazione()))
		        {
		          vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzoVO.getIdUtilizzo(),
		            utilizzoVO.getIdTipoDestinazione());		         
		        }
		        
		        //prendo default
            if(Validator.isEmpty(vDettUso))
            {
              utilizzoVO.setIdTipoDestinazione(elencoDestinazione.get(new Integer(contatore)).get(0).getIdTipoDestinazione());
              vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzoVO.getIdUtilizzo(),
                utilizzoVO.getIdTipoDestinazione());
            }
		        
		        elencoDettUso.put(new Integer(contatore), vDettUso);
		      }
	        else
	        {
	          elencoDettUso.put(new Integer(contatore), new Vector<TipoDettaglioUsoVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    request.setAttribute("elencoDettUso", elencoDettUso);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = AnagErrors.ERRORE_KO_DETT_USO_PRIMARIA;
	    request.setAttribute("messaggioErrore", messaggioErrore);
	    %>
	      <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
	    <%
	  }
	  
	  // Ricerco la qualita uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUso = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzo(), vIdUtilizziAttivi))
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = null;
            if(Validator.isNotEmpty(utilizzoVO.getIdTipoDestinazione())  
              && Validator.isNotEmpty(utilizzoVO.getIdTipoDettaglioUso()))
            {
              vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzoVO.getIdUtilizzo(),
	              utilizzoVO.getIdTipoDestinazione(), utilizzoVO.getIdTipoDettaglioUso());	           
	          }
	          
	          if(Validator.isEmpty(vQualitaUso))
	          {
	            utilizzoVO.setIdTipoDestinazione(elencoDestinazione.get(new Integer(contatore)).get(0).getIdTipoDestinazione());
	            utilizzoVO.setIdTipoDettaglioUso(elencoDettUso.get(new Integer(contatore)).get(0).getIdTipoDettaglioUso());
              vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzoVO.getIdUtilizzo(),
                utilizzoVO.getIdTipoDestinazione(), utilizzoVO.getIdTipoDettaglioUso());
            }
            
            elencoQualitaUso.put(new Integer(contatore), vQualitaUso);
	        }
	        else
	        {
	          elencoQualitaUso.put(new Integer(contatore), new Vector<TipoQualitaUsoVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    
	    request.setAttribute("elencoQualitaUso", elencoQualitaUso);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = AnagErrors.ERRORE_KO_QUALITA_USO_PRIMARIA;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }
	  
	  
	  // Ricerco la varietà primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarieta = new Hashtable<Integer,Vector<TipoVarietaVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzo(), vIdUtilizziAttivi))
          { 
            Vector<TipoVarietaVO> vVarieta = null; 
            if(Validator.isNotEmpty(utilizzoVO.getIdTipoDestinazione())  
              && Validator.isNotEmpty(utilizzoVO.getIdTipoDettaglioUso()) && Validator.isNotEmpty(utilizzoVO.getIdTipoQualitaUso()))
            {
              vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzoVO.getIdUtilizzo(),
	              utilizzoVO.getIdTipoDestinazione(), utilizzoVO.getIdTipoDettaglioUso(), utilizzoVO.getIdTipoQualitaUso());
	          }
	          if(Validator.isEmpty(vVarieta))
	          {
	            utilizzoVO.setIdTipoDestinazione(elencoDestinazione.get(new Integer(contatore)).get(0).getIdTipoDestinazione());
              utilizzoVO.setIdTipoDettaglioUso(elencoDettUso.get(new Integer(contatore)).get(0).getIdTipoDettaglioUso());
              utilizzoVO.setIdTipoQualitaUso(elencoQualitaUso.get(new Integer(contatore)).get(0).getIdTipoQualitaUso());
              
	            vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzoVO.getIdUtilizzo(),
                utilizzoVO.getIdTipoDestinazione(), utilizzoVO.getIdTipoDettaglioUso(), utilizzoVO.getIdTipoQualitaUso());
            }
	          
	          elencoVarieta.put(new Integer(contatore), vVarieta);
	          
	        }
	        else
	        {
	          elencoVarieta.put(new Integer(contatore), new Vector<TipoVarietaVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    
	    request.setAttribute("elencoVarieta", elencoVarieta);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = AnagErrors.ERRORE_KO_VARIETA_PRIMARIA;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }
	  
	  
	  // Ricerco il periodo semina in relazione alla mtarice
	  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSemina = new Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(Validator.isEmpty(utilizzoVO.getDichiarabileEfa())
            || (Validator.isNotEmpty(utilizzoVO.getDichiarabileEfa()))
               && !"S".equalsIgnoreCase(utilizzoVO.getDichiarabileEfa()))
          {
	          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzo(), vIdUtilizziAttivi))
	          {  
	            Vector<TipoPeriodoSeminaVO> vPerSemina = null;
	            if(Validator.isNotEmpty(utilizzoVO.getIdTipoDestinazione())  
	              && Validator.isNotEmpty(utilizzoVO.getIdTipoDettaglioUso()) && Validator.isNotEmpty(utilizzoVO.getIdTipoQualitaUso())
	              && Validator.isNotEmpty(utilizzoVO.getIdVarieta()))
	            {
	              vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzoVO.getIdUtilizzo(),
		              utilizzoVO.getIdTipoDestinazione(), utilizzoVO.getIdTipoDettaglioUso(), utilizzoVO.getIdTipoQualitaUso(),
		              utilizzoVO.getIdVarieta());
		          }
		              
		          if(Validator.isEmpty(vPerSemina))
		          {
		            utilizzoVO.setIdTipoDestinazione(elencoDestinazione.get(new Integer(contatore)).get(0).getIdTipoDestinazione());
	              utilizzoVO.setIdTipoDettaglioUso(elencoDettUso.get(new Integer(contatore)).get(0).getIdTipoDettaglioUso());
	              utilizzoVO.setIdTipoQualitaUso(elencoQualitaUso.get(new Integer(contatore)).get(0).getIdTipoQualitaUso());
	              utilizzoVO.setIdVarieta(elencoVarieta.get(new Integer(contatore)).get(0).getIdVarieta());
	              
		            vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzoVO.getIdUtilizzo(),
	                utilizzoVO.getIdTipoDestinazione(), utilizzoVO.getIdTipoDettaglioUso(), utilizzoVO.getIdTipoQualitaUso(),
	                utilizzoVO.getIdVarieta());
	            }
	            
	            Long idCatalogoMatricePerSemina = null;
	            Long idTipoPeriodoSemina = null;
	            if(Validator.isEmpty(utilizzoVO.getIdTipoPeriodoSemina()))
	            {            
	              for(int h=0;h<vPerSemina.size();h++)
	              {
	                if("S".equalsIgnoreCase(vPerSemina.get(h).getFlagDefault()))
	                {
	                  utilizzoVO.setIdTipoPeriodoSemina(vPerSemina.get(h).getIdTipoPeriodoSemina());
	                  idTipoPeriodoSemina = vPerSemina.get(h).getIdTipoPeriodoSemina();
	                  idCatalogoMatricePerSemina = vPerSemina.get(h).getIdCatalogoMatrice();
	                  break;
	                }
	              }
	            }
	            else
	            {
	              boolean trovatoSemina = false;
	              for(int h=0;h<vPerSemina.size();h++)
	              {
	                if(utilizzoVO.getIdTipoPeriodoSemina().compareTo(vPerSemina.get(h).getIdTipoPeriodoSemina()) == 0)
	                {
	                  utilizzoVO.setIdTipoPeriodoSemina(vPerSemina.get(h).getIdTipoPeriodoSemina());
	                  idTipoPeriodoSemina = vPerSemina.get(h).getIdTipoPeriodoSemina();
	                  idCatalogoMatricePerSemina = vPerSemina.get(h).getIdCatalogoMatrice();
	                  trovatoSemina = true;
	                  break;
	                }
	              }
	              
	              if(!trovatoSemina)
	              {
	                for(int h=0;h<vPerSemina.size();h++)
		              {
		                if("S".equalsIgnoreCase(vPerSemina.get(h).getFlagDefault()))
		                {
		                  utilizzoVO.setIdTipoPeriodoSemina(vPerSemina.get(h).getIdTipoPeriodoSemina());
		                  idTipoPeriodoSemina = vPerSemina.get(h).getIdTipoPeriodoSemina();
		                  idCatalogoMatricePerSemina = vPerSemina.get(h).getIdCatalogoMatrice();
		                  break;
		                }
		              }
	              }            
	            }
	             
	            if(Validator.isEmpty(utilizzoVO.getIdSemina())) 
	            { 
		            for(int h=0;h<vTipoSemina.size();h++)
		            {
		              if("S".equalsIgnoreCase(vTipoSemina.get(h).getFlagDefault()))
		                utilizzoVO.setIdSemina(vTipoSemina.get(h).getIdTipoSemina());  
		            }
		          }
	                
	                
	            if(Validator.isEmpty(utilizzoVO.getDataInizioDestinazioneStr())
	              && Validator.isEmpty(utilizzoVO.getDataFineDestinazioneStr()))
	            {  
	              //popolo date semina default
	              CatalogoMatriceSeminaVO catalogoMatriceSeminaVO = gaaFacadeClient
	              .getCatalogoMatriceSeminaByIdTipoPeriodo(idCatalogoMatricePerSemina, idTipoPeriodoSemina);
	        
				        if(catalogoMatriceSeminaVO != null)
				        {
				          String parametroDataSwapSemina = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DATA_SWAP_SEMINA);
				          SimpleDateFormat sdf = new SimpleDateFormat();
				          sdf.applyPattern("dd/MM/yyyy");
				          Date dataConfronto = sdf.parse(parametroDataSwapSemina+"/"+DateUtils.getCurrentYear());
				        
				          
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
				          
				          
				          utilizzoVO.setDataInizioDestinazioneStr(DateUtils.formatDateNotNull(dataInizioDestinazione));
				          utilizzoVO.setDataFineDestinazioneStr(DateUtils.formatDateNotNull(dataFineDestinazione));  
	                
	              } 
	                
		          }         
		            
		          elencoPerSemina.put(new Integer(contatore), vPerSemina);
		          
		        }
		        else
            {
              elencoPerSemina.put(new Integer(contatore), new Vector<TipoPeriodoSeminaVO>());
            }
		      }
	        else
	        {
	          elencoPerSemina.put(new Integer(contatore), new Vector<TipoPeriodoSeminaVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    request.setAttribute("elencoPerSemina", elencoPerSemina);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_PERIODO_SEMINA;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }
	  
	  
	  // Ricerco il pratica Mantenim in relazione alla mtarice
	  Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>> elencoPerMantenim = new Hashtable<Integer,Vector<TipoPraticaMantenimentoVO>>();
	  String[] arrSupUtilizzata = request.getParameterValues("supUtilizzata");
	  try 
	  {
	    int contatore = 0;
	    int contatoreTotale = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(Validator.isEmpty(utilizzoVO.getDichiarabileEfa())
            || (Validator.isNotEmpty(utilizzoVO.getDichiarabileEfa()))
               && !"S".equalsIgnoreCase(utilizzoVO.getDichiarabileEfa()))
          {
	          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzo(), vIdUtilizziAttivi))
	          {
	            CatalogoMatriceVO catalogoMatricePraticVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoVO.getIdUtilizzo(),
	              utilizzoVO.getIdVarieta(), utilizzoVO.getIdTipoDestinazione(), utilizzoVO.getIdTipoDettaglioUso(), utilizzoVO.getIdTipoQualitaUso());
	              
		          Vector<Long> vIdMantenimento = gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatricePraticVO.getIdCatalogoMatrice(), "N");
		          Vector<TipoPraticaMantenimentoVO> vPraticaMantenim = gaaFacadeClient.getElencoPraticaMantenimento(vIdMantenimento);
		          if(Validator.isEmpty(utilizzoVO.getIdPraticaMantenimento()))
		            utilizzoVO.setIdPraticaMantenimento(gaaFacadeClient.getListIdPraticaMantenimentoPlSql(catalogoMatricePraticVO.getIdCatalogoMatrice(), "S").get(0));
		          
		          if(Validator.isNotEmpty(vPraticaMantenim))
		            elencoPerMantenim.put(new Integer(contatoreTotale), vPraticaMantenim);
		          else
		            elencoPerMantenim.put(new Integer(contatoreTotale), new Vector<TipoPraticaMantenimentoVO>());
		          
		          if(Validator.isNotEmpty(storico.getParticellaCertificataVO())
		            && Validator.isNotEmpty(storico.getParticellaCertificataVO().getIdParticellaCertificata()))
		          {
		            utilizzoVO.setSupEleggibile(anagFacadeClient.getSupEleggPlSqlTotale(
	                storico.getParticellaCertificataVO().getIdParticellaCertificata(), catalogoMatricePraticVO.getIdCatalogoMatrice()));
	              utilizzoVO.setSupEleggibileNetta(anagFacadeClient.getSupEleggNettaPlSqlTotale(
	                storico.getParticellaCertificataVO().getIdParticellaCertificata(), catalogoMatricePraticVO.getIdCatalogoMatrice()));
	            }
	            else
	            {
	              utilizzoVO.setSupEleggibile(new BigDecimal(0));
	              utilizzoVO.setSupEleggibileNetta(new BigDecimal(0));
	            }
	              
	            BigDecimal supNetta = catalogoMatricePraticVO.getCoefficienteRiduzione();
	            String supUtilizzataStr = null;
	            if(Validator.isNotEmpty(arrSupUtilizzata)
	              && Validator.isNotEmpty(arrSupUtilizzata[contatore]))
	            {
	              supUtilizzataStr = arrSupUtilizzata[contatore];
	            }
	            else
	            {
	              supUtilizzataStr = utilizzoVO.getSuperficieUtilizzata();
	            }                
	            if(!(Validator.validateDouble(supUtilizzataStr, 999999.9999) == null)) 
	            {                  
	              supNetta = supNetta.multiply(new BigDecimal(
	                supUtilizzataStr.replace(',','.')));
	              utilizzoVO.setSupNetta(supNetta);
	            }
	            else
	            {
	              utilizzoVO.setSupNetta(new BigDecimal(0));
	            }             
	          }
	          else
	          {
	            elencoPerMantenim.put(new Integer(contatoreTotale), new Vector<TipoPraticaMantenimentoVO>());
	          }
	          
	          contatore++;  
	        }
	        else
	        {
	          elencoPerMantenim.put(new Integer(contatoreTotale), new Vector<TipoPraticaMantenimentoVO>());
	        }
	        
	        
	        contatoreTotale++;
	        
	      }
	    }
	    request.setAttribute("elencoPerMantenim", elencoPerMantenim);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = AnagErrors.ERRORE_KO_TIPO_PRATICA_MANTENIMENTO;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }  
		

	 	
		// Recupero gli usi del suolo secondari
		Vector<TipoUtilizzoVO> elencoTipiUsoSuoloSecondario = (Vector)session.getAttribute("elencoTipiUsoSuoloSecondario");
	 	if(elencoTipiUsoSuoloSecondario == null || elencoTipiUsoSuoloSecondario.size() == 0) 
	 	{
			try 
			{
				elencoTipiUsoSuoloSecondario = anagFacadeClient.findListTipiUsoSuoloByIdAzienda(anagAziendaVO.getIdAzienda(), SolmrConstants.FLAG_S);
				if((elencoTipiUsoSuoloSecondario != null)  && (elencoTipiUsoSuoloSecondario.size() > 0))
				{
				  for(int g=0;g<elencoTipiUsoSuoloSecondario.size();g++)
				  {
				    vIdUtilizziAttiviSecondari.add(elencoTipiUsoSuoloSecondario.get(g).getIdUtilizzo());
				  }
				}
				request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
			}
			catch(SolmrException se) 
			{
				messaggioErrore = (String)AnagErrors.ERRORE_KO_USO_SECONDARIO;
				request.setAttribute("messaggioErrore", messaggioErrore);
				%>
					<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
				<%
			}
	 	}
	 	else 
	 	{
	 	  if((elencoTipiUsoSuoloSecondario != null)  && (elencoTipiUsoSuoloSecondario.size() > 0))
      {
        for(int g=0;g<elencoTipiUsoSuoloSecondario.size();g++)
        {
          vIdUtilizziAttiviSecondari.add(elencoTipiUsoSuoloSecondario.get(g).getIdUtilizzo());
        }
      }
	 		request.setAttribute("elencoTipiUsoSuoloSecondario", elencoTipiUsoSuoloSecondario);
	 	}
	 	
	 	
	 	// Ricerco la destinazione primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoDestinazioneVO>> elencoDestinazioneSecondario = new Hashtable<Integer,Vector<TipoDestinazioneVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzoSecondario(), vIdUtilizziAttiviSecondari)) 
          {
            Vector<TipoDestinazioneVO> vDestinazione = gaaFacadeClient.getElencoTipoDestinazioneByMatrice(utilizzoVO.getIdUtilizzoSecondario());
	          elencoDestinazioneSecondario.put(new Integer(contatore), vDestinazione);
	        }
	        else
	        {
	          elencoDestinazioneSecondario.put(new Integer(contatore), new Vector<TipoDestinazioneVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    request.setAttribute("elencoDestinazioneSecondario", elencoDestinazioneSecondario);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = (String)AnagErrors.ERRORE_KO_DEST_USO_SECONDARIO;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }
	 	
	 	
	 	// Ricerco il dettaglio uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoDettaglioUsoVO>> elencoDettUsoSecondario = new Hashtable<Integer,Vector<TipoDettaglioUsoVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzoSecondario(), vIdUtilizziAttiviSecondari))
          {
            Vector<TipoDettaglioUsoVO> vDettUso = null;
            if(Validator.isNotEmpty(utilizzoVO.getIdTipoDestinazioneSecondario())) 
            {
              vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzoVO.getIdUtilizzoSecondario(),
	              utilizzoVO.getIdTipoDestinazioneSecondario());
	          }
	          
	          if(Validator.isEmpty(vDettUso))
	          {
	            utilizzoVO.setIdTipoDestinazioneSecondario(elencoDestinazioneSecondario.get(new Integer(contatore)).get(0).getIdTipoDestinazione());
	            vDettUso = gaaFacadeClient.getListDettaglioUsoByMatrice(utilizzoVO.getIdUtilizzoSecondario(),
                utilizzoVO.getIdTipoDestinazioneSecondario());
	          }
	          
	          
	          elencoDettUsoSecondario.put(new Integer(contatore), vDettUso);
	         
	        }
	        else
	        {
	          elencoDettUsoSecondario.put(new Integer(contatore), new Vector<TipoDettaglioUsoVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    request.setAttribute("elencoDettUsoSecondario", elencoDettUsoSecondario);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = (String)AnagErrors.ERRORE_KO_DETTAGLIO_USO_SECONDARIO;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }
		
		
    
    // Ricerco la qualita uso primaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoQualitaUsoVO>> elencoQualitaUsoSecondario = new Hashtable<Integer,Vector<TipoQualitaUsoVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzoSecondario(), vIdUtilizziAttiviSecondari))
          {
            Vector<TipoQualitaUsoVO> vQualitaUso = null;
            if(Validator.isNotEmpty(utilizzoVO.getIdTipoDestinazioneSecondario())
               && Validator.isNotEmpty(utilizzoVO.getIdTipoDettaglioUsoSecondario()))  
            {
              vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzoVO.getIdUtilizzoSecondario(),
	              utilizzoVO.getIdTipoDestinazioneSecondario(), utilizzoVO.getIdTipoDettaglioUsoSecondario());
	          }
	          
	          if(Validator.isEmpty(vQualitaUso))
	          {
	            utilizzoVO.setIdTipoDestinazioneSecondario(elencoDestinazioneSecondario.get(new Integer(contatore)).get(0).getIdTipoDestinazione());
	            utilizzoVO.setIdTipoDettaglioUsoSecondario(elencoDettUsoSecondario.get(new Integer(contatore)).get(0).getIdTipoDettaglioUso());
	            vQualitaUso = gaaFacadeClient.getElencoTipoQualitaUsoByMatrice(utilizzoVO.getIdUtilizzoSecondario(),
                utilizzoVO.getIdTipoDestinazioneSecondario(), utilizzoVO.getIdTipoDettaglioUsoSecondario());
	          }
	          
	          
	          elencoQualitaUsoSecondario.put(new Integer(contatore), vQualitaUso);
	          
	        }
	        else
	        {
	          elencoQualitaUsoSecondario.put(new Integer(contatore), new Vector<TipoQualitaUsoVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    request.setAttribute("elencoQualitaUsoSecondario", elencoQualitaUsoSecondario);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = (String)AnagErrors.ERRORE_KO_QUALITA_USO_SECONDARIO;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }
  
	  // Ricerco la varietà secondaria in relazione al tipo utilizzo primario selezionato dall'utente
	  Hashtable<Integer,Vector<TipoVarietaVO>> elencoVarietaSecondaria = new Hashtable<Integer,Vector<TipoVarietaVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzoSecondario(), vIdUtilizziAttiviSecondari))
          {
            Vector<TipoVarietaVO> vVarieta = null;
            if(Validator.isNotEmpty(utilizzoVO.getIdTipoDestinazioneSecondario())
              && Validator.isNotEmpty(utilizzoVO.getIdTipoDettaglioUsoSecondario()) 
              && Validator.isNotEmpty(utilizzoVO.getIdTipoQualitaUsoSecondario())) 
            {
              vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzoVO.getIdUtilizzoSecondario(),
                utilizzoVO.getIdTipoDestinazioneSecondario(), utilizzoVO.getIdTipoDettaglioUsoSecondario(), utilizzoVO.getIdTipoQualitaUsoSecondario());
            }
	          
	          if(Validator.isEmpty(vVarieta))
	          {
	            utilizzoVO.setIdTipoDestinazioneSecondario(elencoDestinazioneSecondario.get(new Integer(contatore)).get(0).getIdTipoDestinazione());
              utilizzoVO.setIdTipoDettaglioUsoSecondario(elencoDettUsoSecondario.get(new Integer(contatore)).get(0).getIdTipoDettaglioUso());
              utilizzoVO.setIdTipoQualitaUsoSecondario(elencoQualitaUsoSecondario.get(new Integer(contatore)).get(0).getIdTipoQualitaUso());
	            vVarieta = gaaFacadeClient.getElencoTipoVarietaByMatrice(utilizzoVO.getIdUtilizzoSecondario(),
                utilizzoVO.getIdTipoDestinazioneSecondario(), utilizzoVO.getIdTipoDettaglioUsoSecondario(), utilizzoVO.getIdTipoQualitaUsoSecondario());
	          }
	          
	          elencoVarietaSecondaria.put(new Integer(contatore), vVarieta);
	          
	        }
	        else
	        {
	          elencoVarietaSecondaria.put(new Integer(contatore), new Vector<TipoVarietaVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    request.setAttribute("elencoVarietaSecondaria", elencoVarietaSecondaria);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = (String)AnagErrors.ERRORE_KO_VARIETA_SECONDARIA;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  } 
  
	  // Ricerco il periodo semina in relazione alla mtarice
	  Hashtable<Integer,Vector<TipoPeriodoSeminaVO>> elencoPerSeminaSecondario = new Hashtable<Integer,Vector<TipoPeriodoSeminaVO>>();
	  try 
	  {
	    int contatore = 0;
      for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
        StoricoParticellaVO storico = (StoricoParticellaVO)elencoStoricoParticella[i];
        UtilizzoParticellaVO[] utilizzo = (UtilizzoParticellaVO[])storico.getElencoConduzioni()[0].getElencoUtilizzi();
        for(int a = 0; a < utilizzo.length; a++) 
        {
          UtilizzoParticellaVO utilizzoVO = (UtilizzoParticellaVO)utilizzo[a];
          if(isUtilizzoAttivo(utilizzoVO.getIdUtilizzoSecondario(), vIdUtilizziAttiviSecondari))
          {
            Vector<TipoPeriodoSeminaVO> vPerSemina = null;
            if(Validator.isNotEmpty(utilizzoVO.getIdTipoDestinazioneSecondario())
              && Validator.isNotEmpty(utilizzoVO.getIdTipoDettaglioUsoSecondario()) && Validator.isNotEmpty(utilizzoVO.getIdTipoQualitaUsoSecondario())
              && Validator.isNotEmpty(utilizzoVO.getIdVarietaSecondaria()))  
            {
              vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzoVO.getIdUtilizzoSecondario(),
                utilizzoVO.getIdTipoDestinazioneSecondario(), utilizzoVO.getIdTipoDettaglioUsoSecondario(), utilizzoVO.getIdTipoQualitaUsoSecondario(),
                utilizzoVO.getIdVarietaSecondaria());
            }
            
            
            if(Validator.isEmpty(vPerSemina))
            {
              utilizzoVO.setIdTipoDestinazioneSecondario(elencoDestinazioneSecondario.get(new Integer(contatore)).get(0).getIdTipoDestinazione());
              utilizzoVO.setIdTipoDettaglioUsoSecondario(elencoDettUsoSecondario.get(new Integer(contatore)).get(0).getIdTipoDettaglioUso());
              utilizzoVO.setIdTipoQualitaUsoSecondario(elencoQualitaUsoSecondario.get(new Integer(contatore)).get(0).getIdTipoQualitaUso());
              utilizzoVO.setIdVarietaSecondaria(elencoVarietaSecondaria.get(new Integer(contatore)).get(0).getIdVarieta());
               
              vPerSemina = gaaFacadeClient.getListTipoPeriodoSeminaByCatalogo(utilizzoVO.getIdUtilizzoSecondario(),
                utilizzoVO.getIdTipoDestinazioneSecondario(), utilizzoVO.getIdTipoDettaglioUsoSecondario(), utilizzoVO.getIdTipoQualitaUsoSecondario(),
                utilizzoVO.getIdVarietaSecondaria());
            }
                
                
            Long idCatalogoMatricePerSemina = null;
            Long idTipoPeriodoSemina = null;
            if(Validator.isEmpty(utilizzoVO.getIdTipoPeriodoSeminaSecondario()))
            {            
              for(int h=0;h<vPerSemina.size();h++)
              {
                if("S".equalsIgnoreCase(vPerSemina.get(h).getFlagDefault()))
                {
                  utilizzoVO.setIdTipoPeriodoSeminaSecondario(vPerSemina.get(h).getIdTipoPeriodoSemina());
                  idTipoPeriodoSemina = vPerSemina.get(h).getIdTipoPeriodoSemina();
                  idCatalogoMatricePerSemina = vPerSemina.get(h).getIdCatalogoMatrice();
                  break;
                }
              }
            }
            else
            {
              boolean trovatoSemina = false;
              for(int h=0;h<vPerSemina.size();h++)
              {
                if(utilizzoVO.getIdTipoPeriodoSeminaSecondario().compareTo(vPerSemina.get(h).getIdTipoPeriodoSemina()) == 0)
                {
                  utilizzoVO.setIdTipoPeriodoSeminaSecondario(vPerSemina.get(h).getIdTipoPeriodoSemina());
                  idTipoPeriodoSemina = vPerSemina.get(h).getIdTipoPeriodoSemina();
                  idCatalogoMatricePerSemina = vPerSemina.get(h).getIdCatalogoMatrice();
                  trovatoSemina = true;
                  break;
                }
              }
              
              if(!trovatoSemina)
              {
                for(int h=0;h<vPerSemina.size();h++)
                {
                  if("S".equalsIgnoreCase(vPerSemina.get(h).getFlagDefault()))
                  {
                    utilizzoVO.setIdTipoPeriodoSeminaSecondario(vPerSemina.get(h).getIdTipoPeriodoSemina());
                    idTipoPeriodoSemina = vPerSemina.get(h).getIdTipoPeriodoSemina();
                    idCatalogoMatricePerSemina = vPerSemina.get(h).getIdCatalogoMatrice();
                    break;
                  }
                }
              }            
            }
             
            if(Validator.isEmpty(utilizzoVO.getIdSeminaSecondario())) 
            { 
              for(int h=0;h<vTipoSemina.size();h++)
              {
                if("S".equalsIgnoreCase(vTipoSemina.get(h).getFlagDefault()))
                  utilizzoVO.setIdSeminaSecondario(vTipoSemina.get(h).getIdTipoSemina());  
              }
            }
                
                
            if(Validator.isEmpty(utilizzoVO.getDataInizioDestinazioneSecStr())
              && Validator.isEmpty(utilizzoVO.getDataFineDestinazioneSecStr()))
            {  
              //popolo date semina default
              CatalogoMatriceSeminaVO catalogoMatriceSeminaVO = gaaFacadeClient
              .getCatalogoMatriceSeminaByIdTipoPeriodo(idCatalogoMatricePerSemina, idTipoPeriodoSemina);
        
              if(catalogoMatriceSeminaVO != null)
              {
                String parametroDataSwapSemina = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DATA_SWAP_SEMINA);
                SimpleDateFormat sdf = new SimpleDateFormat();
                sdf.applyPattern("dd/MM/yyyy");
                Date dataConfronto = sdf.parse(parametroDataSwapSemina+"/"+DateUtils.getCurrentYear());
              
                
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
                
                
                utilizzoVO.setDataInizioDestinazioneSecStr(DateUtils.formatDateNotNull(dataInizioDestinazione));
                utilizzoVO.setDataFineDestinazioneSecStr(DateUtils.formatDateNotNull(dataFineDestinazione));  
                
              } 
                
            }         
              
            
	          elencoPerSeminaSecondario.put(new Integer(contatore), vPerSemina);
	          
	        }
	        else
	        {
	          elencoPerSeminaSecondario.put(new Integer(contatore), new Vector<TipoPeriodoSeminaVO>());
	        }
	        
	        contatore++;
	      }
	    }
	    
	    request.setAttribute("elencoPerSeminaSecondario", elencoPerSeminaSecondario);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = (String)AnagErrors.ERRORE_KO_TIPO_PERIODO_SEMINA_SEC;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  } 
	 	
	 	// Recupero i casi particolari
	 	it.csi.solmr.dto.CodeDescription[] elencoCasiParticolari = null;
	 	try 
	 	{
	 		elencoCasiParticolari = anagFacadeClient.getListTipoCasoParticolare(SolmrConstants.ORDER_BY_GENERIC_CODE);
	 		request.setAttribute("elencoCasiParticolari", elencoCasiParticolari);
	 	}
	 	catch(SolmrException se) 
	 	{
			messaggioErrore = (String)AnagErrors.ERRORE_KO_CASI_PARTICOLARI;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
			<%
		}
		
    
    Vector<CasoParticolareVO> elencoCasiParticolariPartProv = null;
    String particellaObbligatoria = "N";
	  try 
	  {
	    elencoCasiParticolariPartProv = gaaFacadeClient.getCasiParticolari(particellaObbligatoria);
	    request.setAttribute("elencoCasiParticolariPartProv", elencoCasiParticolariPartProv);
	  }
	  catch(SolmrException se) 
	  {
	    messaggioErrore = (String)AnagErrors.ERRORE_KO_CASI_PARTICOLARI;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= terreniParticellareModificaUrl %>" />
      <%
	  }
	  
	  
	   
    
    
    
    session.setAttribute("elencoStoricoParticella", elencoStoricoParticella);
    
    
    
	 	
	 	if(request.getParameter("conferma") != null) 
    {
	 		// Recupero i parametri ed effettuo i controlli formali
	 		Vector<ValidationErrors> elencoErroriConduzioniStorico = new Vector<ValidationErrors>();
	 		Vector<ValidationErrors> elencoErroriUtilizzi = new Vector<ValidationErrors>();
	 		ValidationErrors errors = null;
	 		ValidationErrors errori = null;
	 		int countErrori = 0;
	 		int countErroriUtilizzi = 0;
	 		int contaElementiNonEfa = 0;
	 		int contaElementiTotali = 0;
      //Totale della sup utilizzata della particella nel caso ci fossero più
      //Conduzioni sulla stessa
      double totSupUtilizzataParticella = 0;
      //Utilizzata per capire se è cambiata la particella!!!
      Long idParticellaChg = new Long(0);
      //Totale della percentualePossesso della particella nel caso ci fossero più
      //Conduzioni sulla stessa
      BigDecimal percentualPossessoTotBg = new BigDecimal(0);
      //Totale della supCondotta della particella nel caso ci fossero più
      //Conduzioni sulla stessa
      BigDecimal supCondottaTotBg = new BigDecimal(0);
	 		for(int i = 0; i < elencoStoricoParticella.length; i++) 
      {
	 			StoricoParticellaVO storicoConfermaVO = (StoricoParticellaVO)elencoStoricoParticella[i];
	 			ConduzioneParticellaVO conduzioneConfermaVO = (ConduzioneParticellaVO)storicoConfermaVO.getElencoConduzioni()[0];
	 			UtilizzoParticellaVO[] elencoUtilizziConfermaVO = (UtilizzoParticellaVO[])conduzioneConfermaVO.getElencoUtilizzi();
	 			errors = new ValidationErrors();
	 			// ID TITOLO POSSESSO
	 			if(Validator.isNotEmpty(request.getParameterValues("idTitoloPossesso")[i])) 
        {
	 				conduzioneConfermaVO.setIdTitoloPossesso(Long.decode(request.getParameterValues("idTitoloPossesso")[i])); 					
	 			}
	 			else 
        {
	 				conduzioneConfermaVO.setIdTitoloPossesso(null); 
	 				errors.add("idTitoloPossesso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
	 				countErrori++;
	 			}
	 			// PERCENTUALE POSSESSO
        if(Validator.isNotEmpty(request.getParameterValues("percentualePossesso")[i])) 
        {
          if(!Validator.isNumberPercentualeMaggioreZeroDecimali(request.getParameterValues("percentualePossesso")[i]))
          {
            errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_KO_PERCENTUALE_CONDUZIONE));
            countErrori++;
          }
          else
          {
            BigDecimal percentualePossessoTmp = new BigDecimal(request.getParameterValues("percentualePossesso")[i].replace(',','.'));            
            conduzioneConfermaVO.setPercentualePossesso(percentualePossessoTmp);
            
            //I controllii sono effettuati solo se la particella non è provvisoria
            if(Validator.isNotEmpty(storicoConfermaVO.getParticella()))
            {            
	            //Controllo che la somma delle percentuali possesso selezionate e non, non superi 100!!!
	            if(idParticellaChg.compareTo(storicoConfermaVO.getIdParticella()) !=0)
		          {
		            percentualPossessoTotBg = percentualePossessoTmp;
		          }
		          else
		          {
		            percentualPossessoTotBg = percentualPossessoTotBg.add(percentualePossessoTmp);
		          }
		          
		          BigDecimal percentualPossessoAltreCondBg = gaaFacadeClient
		            .getSumPercentualPossessoAltreConduzioni(anagAziendaVO.getIdAzienda().longValue(), storicoConfermaVO.getIdParticella().longValue(),
	              hIdConduzioni.get(storicoConfermaVO.getIdParticella()));
	            percentualPossessoTotBg = percentualPossessoTotBg.add(percentualPossessoAltreCondBg);
	              
	            if(percentualPossessoTotBg.compareTo(new BigDecimal(100)) > 0)
	            {
	              errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_KO_SUM_PERCENTUALE_CONDUZIONE));
	              countErrori++;
	            }
	          }             
            
          }
        }
        else 
        {
          conduzioneConfermaVO.setPercentualePossesso(null); 
          errors.add("percentualePossesso", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
	 			
	 			// CASO PARTICOLARE
	 			if(Validator.isNotEmpty(request.getParameterValues("idCasoParticolare")[i])) 
	 			{
	 				storicoConfermaVO.setIdCasoParticolare(Long.decode(request.getParameterValues("idCasoParticolare")[i])); 					
	 			}
	 			else 
	 			{
	 				storicoConfermaVO.setIdCasoParticolare(null); 
	 				errors.add("idCasoParticolare", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
	 				countErrori++;
	 			}
        
	 			// DATI USO DEL SUOLO            
        double totSupUtilizzata = 0;
        
        
	 			for(int a = 0; a < elencoUtilizziConfermaVO.length; a++) 
        {
	 				UtilizzoParticellaVO utilizzoConfermaVO = (UtilizzoParticellaVO)elencoUtilizziConfermaVO[a];
	 				errori = new ValidationErrors();
	 				
	 				if(Validator.isEmpty(utilizzoConfermaVO.getDichiarabileEfa())
	 				  || (Validator.isNotEmpty(utilizzoConfermaVO.getDichiarabileEfa()))
	 				     && !"S".equalsIgnoreCase(utilizzoConfermaVO.getDichiarabileEfa()))
	 				{
		 				// USO PRIMARIO
		 				CatalogoMatriceVO catalogoMatriceConfVO = null;
		 				if(request.getParameterValues("idTipoUtilizzo") != null 
		 				  && contaElementiNonEfa < request.getParameterValues("idTipoUtilizzo").length
		 				  && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzo")[contaElementiNonEfa])) 
	          {
		 					utilizzoConfermaVO.setIdUtilizzo(Long.decode(request.getParameterValues("idTipoUtilizzo")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdTipoDestinazione(Long.decode(request.getParameterValues("idTipoDestinazione")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdTipoDettaglioUso(Long.decode(request.getParameterValues("idTipoDettaglioUso")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdTipoQualitaUso(Long.decode(request.getParameterValues("idTipoQualitaUso")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdVarieta(Long.decode(request.getParameterValues("idVarieta")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdTipoPeriodoSemina(Long.decode(request.getParameterValues("idTipoPeriodoSemina")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdSemina(Long.decode(request.getParameterValues("idTipoSemina")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdPraticaMantenimento(Long.decode(request.getParameterValues("idPraticaMantenimento")[contaElementiNonEfa]));
		 					
		 					catalogoMatriceConfVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoConfermaVO.getIdUtilizzo(),
                    utilizzoConfermaVO.getIdVarieta(), utilizzoConfermaVO.getIdTipoDestinazione(), utilizzoConfermaVO.getIdTipoDettaglioUso(), 
                    utilizzoConfermaVO.getIdTipoQualitaUso());
              utilizzoConfermaVO.setIdCatalogoMatrice(catalogoMatriceConfVO.getIdCatalogoMatrice());
		 					
		 					if(Validator.isEmpty(request.getParameterValues("supUtilizzata")[contaElementiNonEfa])) 
	            {
		 						errori.add("supUtilizzata", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		 						countErroriUtilizzi++;
		 					}
		 					else
		 					{
		 					  utilizzoConfermaVO.setSuperficieUtilizzata(request.getParameterValues("supUtilizzata")[contaElementiNonEfa]);
	              if(Validator.validateDouble(utilizzoConfermaVO.getSuperficieUtilizzata(), 999999.9999) == null) 
	              {
	                errori.add("supUtilizzata", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
	                utilizzoConfermaVO.setSupNetta(new BigDecimal(0));
	                countErroriUtilizzi++;
	              }
	              else 
	              {
	                utilizzoConfermaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(request.getParameterValues("supUtilizzata")[contaElementiNonEfa]));
	                
	                double supUtilizzataDb = NumberUtils.arrotonda(Double.parseDouble(utilizzoConfermaVO.getSuperficieUtilizzata().replace(',', '.')), 4);
	                totSupUtilizzata += supUtilizzataDb;
	                
	                if(idParticellaChg.compareTo(storicoConfermaVO.getIdParticella()) !=0)
	                {
	                  totSupUtilizzataParticella = supUtilizzataDb;
	                }
	                else
	                {
	                  totSupUtilizzataParticella += supUtilizzataDb;
	                }
	                
	                //carica la superficie netta
	                BigDecimal supNetta = catalogoMatriceConfVO.getCoefficienteRiduzione();
	                supNetta = supNetta.multiply(new BigDecimal(utilizzoConfermaVO.getSuperficieUtilizzata().replace(',','.')));
	                utilizzoConfermaVO.setSupNetta(supNetta);
	                utilizzoConfermaVO.setCoefficienteRiduzione(catalogoMatriceConfVO.getCoefficienteRiduzione());
	                 	 					
		 					  }
		 					}
		 					
		 					
		 					
		 					
	            
	            
	            if(Validator.isNotEmpty(storicoConfermaVO.getParticellaCertificataVO())
                && Validator.isNotEmpty(storicoConfermaVO.getParticellaCertificataVO().getIdParticellaCertificata()))
              {
                String flagPratoPermanente = catalogoMatriceConfVO.getFlagPratoPermanente();
              
                boolean isRegistroPascoliPratoPolifita = gaaFacadeClient.isRegistroPascoliPratoPolifita(
                  storicoConfermaVO.getParticellaCertificataVO().getIdParticellaCertificata().longValue());
              
                String valore = gaaFacadeClient.getValoreAttivoTipoAreaFromParticellaAndId(storicoConfermaVO.getIdParticella(), 4);
               
                if(isRegistroPascoliPratoPolifita && Validator.isNotEmpty(valore)
                  && !"1".equalsIgnoreCase(valore))
                {
                  if(!"S".equalsIgnoreCase(flagPratoPermanente))
                  {
                    errori.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_CAMPO_VARIETA_REGISTRO_POLIFITA));
                    countErroriUtilizzi++;
                  }
                }             
	            }
	            
	            boolean flagDataIn = false;
				      if(!Validator.validateDateF(request.getParameterValues("dataInizioDestinazione")[contaElementiNonEfa])) 
				      {
				        utilizzoConfermaVO.setDataInizioDestinazioneStr(request.getParameterValues("dataInizioDestinazione")[contaElementiNonEfa]);
				        errori.add("dataInizioDestinazione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
				        countErroriUtilizzi++;
				      }
				      else
				      {
				        utilizzoConfermaVO.setDataInizioDestinazione(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazione")[contaElementiNonEfa]));
				        flagDataIn = true;
				      }
				      
				      boolean flagDataEnd = false;
				      if(!Validator.validateDateF(request.getParameterValues("dataFineDestinazione")[contaElementiNonEfa])) 
				      {
				        utilizzoConfermaVO.setDataFineDestinazioneStr(request.getParameterValues("dataFineDestinazione")[contaElementiNonEfa]);
				        errori.add("dataFineDestinazione", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
				        countErroriUtilizzi++;
				      }
				      else
				      {
				        utilizzoConfermaVO.setDataFineDestinazione(DateUtils.parseDate(request.getParameterValues("dataFineDestinazione")[contaElementiNonEfa]));
				        flagDataEnd = true;
				      }
				      
				      if(flagDataIn && flagDataEnd)
				      {
				        if(utilizzoConfermaVO.getDataInizioDestinazione()
				          .after(utilizzoConfermaVO.getDataFineDestinazione()))
				        {
				          errori.add("dataInizioDestinazione", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
				          errori.add("dataFineDestinazione", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
				          countErroriUtilizzi++;      
				        }
				      }        
				      
				      //Controllo set se frutta guscio..
		 					TipoUtilizzoVO tipoUtilizzoVOSel = anagFacadeClient.findTipoUtilizzoByPrimaryKey(utilizzoConfermaVO.getIdUtilizzo());
	            String flagFruttaGuscio = tipoUtilizzoVOSel.getFlagFruttaGuscio();
	            
	            boolean errFruttaGuscio = false;
	            
	            if("S".equalsIgnoreCase(flagFruttaGuscio))
			        {
			          if(Validator.isEmpty(utilizzoConfermaVO.getAnnoImpianto()))
			          {
			            errFruttaGuscio = true;
			          }
	     
	              if(Validator.isEmpty(utilizzoConfermaVO.getIdImpianto())
	                && !errFruttaGuscio)
	              {
					        errFruttaGuscio = true;        
					      }
					      
					      if(Validator.isEmpty(utilizzoConfermaVO.getSestoSuFile())
	                && !errFruttaGuscio)
	              {
	                errFruttaGuscio = true;        
	              }
	              
	              if(Validator.isEmpty(utilizzoConfermaVO.getSestoTraFile())
	                && !errFruttaGuscio)
	              {
	                errFruttaGuscio = true;        
	              }
	              
	              if(Validator.isEmpty(utilizzoConfermaVO.getNumeroPianteCeppi())
	                && !errFruttaGuscio)
	              {
	                errFruttaGuscio = true;        
	              }
	            }	 					
		 					
		 					if(errFruttaGuscio)
		 					{
		 					  errori.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_FRUTTA_GUSCIO_OBBLIGATORIO));
	              countErroriUtilizzi++;
	            }
		 					
		 				}
		 				else 
	          {
		 					utilizzoConfermaVO.setIdUtilizzo(null);
		 					utilizzoConfermaVO.setIdTipoDestinazione(null);
              utilizzoConfermaVO.setIdTipoDettaglioUso(null);
              utilizzoConfermaVO.setIdTipoQualitaUso(null);
              utilizzoConfermaVO.setIdVarieta(null);
              utilizzoConfermaVO.setIdTipoPeriodoSemina(null);
              utilizzoConfermaVO.setIdSemina(null);
              utilizzoConfermaVO.setDataInizioDestinazione(null);
              utilizzoConfermaVO.setDataFineDestinazione(null);
              utilizzoConfermaVO.setDataInizioDestinazioneStr(null);
              utilizzoConfermaVO.setDataFineDestinazioneStr(null);
              utilizzoConfermaVO.setIdPraticaMantenimento(null);
              utilizzoConfermaVO.setCoefficienteRiduzione(new BigDecimal(0));
		 					if((contaElementiNonEfa < request.getParameterValues("supUtilizzata").length)
		 					  && Validator.isNotEmpty(request.getParameterValues("supUtilizzata")[contaElementiNonEfa])) 
	            {
		 						errori.add("idTipoUtilizzo", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		 						countErroriUtilizzi++;
		 					}
		 				}
		 		
		 				
		 				if(request.getParameterValues("idTipoUtilizzoSecondario") != null
		 				  && contaElementiNonEfa < request.getParameterValues("idTipoUtilizzoSecondario").length 
	            && Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[contaElementiNonEfa])) 
	          {
		 					utilizzoConfermaVO.setIdUtilizzoSecondario(Long.decode(request.getParameterValues("idTipoUtilizzoSecondario")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdTipoDestinazioneSecondario(Long.decode(request.getParameterValues("idTipoDestinazioneSecondario")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdTipoDettaglioUsoSecondario(Long.decode(request.getParameterValues("idTipoDettaglioUsoSecondario")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdTipoQualitaUsoSecondario(Long.decode(request.getParameterValues("idTipoQualitaUsoSecondario")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdVarietaSecondaria(Long.decode(request.getParameterValues("idVarietaSecondaria")[contaElementiNonEfa]));
		 					utilizzoConfermaVO.setIdTipoPeriodoSeminaSecondario(Long.decode(request.getParameterValues("idTipoPeriodoSeminaSecondario")[contaElementiNonEfa]));
              utilizzoConfermaVO.setIdSeminaSecondario(Long.decode(request.getParameterValues("idTipoSeminaSecondario")[contaElementiNonEfa]));
              
              
              CatalogoMatriceVO catalogoMatriceConfSecVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoConfermaVO.getIdUtilizzoSecondario(),
                    utilizzoConfermaVO.getIdVarietaSecondaria(), utilizzoConfermaVO.getIdTipoDestinazioneSecondario(), utilizzoConfermaVO.getIdTipoDettaglioUsoSecondario(), 
                    utilizzoConfermaVO.getIdTipoQualitaUsoSecondario());
              utilizzoConfermaVO.setIdCatalogoMatriceSecondario(catalogoMatriceConfSecVO.getIdCatalogoMatrice());
		 					
		 					if(!Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[contaElementiNonEfa])) 
	            {
		 						errori.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		 						countErroriUtilizzi++;
		 					}
		 					
		 					
		 					
		 					if(Validator.isNotEmpty(storicoConfermaVO.getParticellaCertificataVO())
                && Validator.isNotEmpty(storicoConfermaVO.getParticellaCertificataVO().getIdParticellaCertificata()))
              {
                String flagPratoPermanente = catalogoMatriceConfSecVO.getFlagPratoPermanente();
              
                boolean isRegistroPascoliPratoPolifita = gaaFacadeClient.isRegistroPascoliPratoPolifita(
                  storicoConfermaVO.getParticellaCertificataVO().getIdParticellaCertificata().longValue());
              
                String valore = gaaFacadeClient.getValoreAttivoTipoAreaFromParticellaAndId(storicoConfermaVO.getIdParticella(), 4);
               
                if(isRegistroPascoliPratoPolifita && Validator.isNotEmpty(valore)
                  && !"1".equalsIgnoreCase(valore))
                {
                  if(!"S".equalsIgnoreCase(flagPratoPermanente))
                  {
                    errori.add("idTipoUtilizzoSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_VARIETA_REGISTRO_POLIFITA));
                    countErroriUtilizzi++;
                  }
                }             
              }
              
              boolean flagDataIn = false;
              if(!Validator.validateDateF(request.getParameterValues("dataInizioDestinazioneSec")[contaElementiNonEfa])) 
              {
                utilizzoConfermaVO.setDataInizioDestinazioneSecStr(request.getParameterValues("dataInizioDestinazioneSec")[contaElementiNonEfa]);
                errori.add("dataInizioDestinazioneSec", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
                countErroriUtilizzi++;
              }
              else
              {
                utilizzoConfermaVO.setDataInizioDestinazioneSec(DateUtils.parseDate(request.getParameterValues("dataInizioDestinazioneSec")[contaElementiNonEfa]));
                flagDataIn = true;
              }
              
              boolean flagDataEnd = false;
              if(!Validator.validateDateF(request.getParameterValues("dataFineDestinazioneSec")[contaElementiNonEfa])) 
              {
                utilizzoConfermaVO.setDataFineDestinazioneSecStr(request.getParameterValues("dataFineDestinazioneSec")[contaElementiNonEfa]);
                errori.add("dataFineDestinazioneSec", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
                countErroriUtilizzi++;
              }
              else
              {
                utilizzoConfermaVO.setDataFineDestinazioneSec(DateUtils.parseDate(request.getParameterValues("dataFineDestinazioneSec")[contaElementiNonEfa]));
                flagDataEnd = true;
              }
              
              if(flagDataIn && flagDataEnd)
              {
                if(utilizzoConfermaVO.getDataInizioDestinazioneSec()
                  .after(utilizzoConfermaVO.getDataFineDestinazioneSec()))
                {
                  errori.add("dataInizioDestinazioneSec", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
                  errori.add("dataFineDestinazioneSec", new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_POST_DATA_FINE));
                  countErroriUtilizzi++;      
                }
              }        
		 					
		 					
		 				}
		 				else 
	          {
	            utilizzoConfermaVO.setIdCatalogoMatriceSecondario(null);
		 					utilizzoConfermaVO.setIdUtilizzoSecondario(null);
		 					utilizzoConfermaVO.setIdTipoDestinazioneSecondario(null);
              utilizzoConfermaVO.setIdTipoDettaglioUsoSecondario(null);
              utilizzoConfermaVO.setIdTipoQualitaUsoSecondario(null);
              utilizzoConfermaVO.setIdVarietaSecondaria(null);
              utilizzoConfermaVO.setIdTipoPeriodoSeminaSecondario(null);
              utilizzoConfermaVO.setIdSeminaSecondario(null);
              utilizzoConfermaVO.setDataInizioDestinazioneSec(null);
              utilizzoConfermaVO.setDataFineDestinazioneSec(null);
              utilizzoConfermaVO.setDataInizioDestinazioneSecStr(null);
              utilizzoConfermaVO.setDataFineDestinazioneSecStr(null);
		 					if(contaElementiNonEfa < request.getParameterValues("supUtilizzataSecondaria").length 
		 					  && Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[contaElementiNonEfa])) 
	            {
		 						errori.add("idTipoUtilizzoSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		 						countErroriUtilizzi++;
		 					}
		 				}
						
			 			
			 			
		 				if(contaElementiNonEfa < request.getParameterValues("supUtilizzataSecondaria").length 
		 				  && Validator.isNotEmpty(request.getParameterValues("supUtilizzataSecondaria")[contaElementiNonEfa])) 
	          {
		 					utilizzoConfermaVO.setSupUtilizzataSecondaria(request.getParameterValues("supUtilizzataSecondaria")[contaElementiNonEfa]);
		 					if(Validator.validateDouble(utilizzoConfermaVO.getSupUtilizzataSecondaria(), 999999.9999) == null) 
	            {
		 						errori.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
		 						countErroriUtilizzi++;
		 					}
		 					else 
	            {
		 						utilizzoConfermaVO.setSuperficieUtilizzata(StringUtils.parseSuperficieField(request.getParameterValues("supUtilizzata")[contaElementiNonEfa]));
		 						if(Validator.isNotEmpty(utilizzoConfermaVO.getSuperficieUtilizzata()) 
	                && Validator.validateDouble(utilizzoConfermaVO.getSuperficieUtilizzata(), 999999.9999) != null) 
	              {
		 							if(NumberUtils.arrotonda(Double.parseDouble(utilizzoConfermaVO.getSupUtilizzataSecondaria().replace(',', '.')), 4) 
	                  > NumberUtils.arrotonda(Double.parseDouble(utilizzoConfermaVO.getSuperficieUtilizzata().replace(',', '.')), 4)) 
	                {
		 								errori.add("supUtilizzataSecondaria", new ValidationError(AnagErrors.ERRORE_KO_SUP_UTILIZZATA_SECONDARIA_MAGGIORE_SUP_UTILIZZATA));
		 		 						countErroriUtilizzi++;
		 							}
		 						}
		 					}
		 					if(request.getParameterValues("idTipoUtilizzoSecondario") == null 
	              || !Validator.isNotEmpty(request.getParameterValues("idTipoUtilizzoSecondario")[contaElementiNonEfa])) 
	            {
		 						errori.add("idTipoUtilizzoSecondario", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
		 						countErroriUtilizzi++;
		 					}
		 				}
		 				else 
	          {
		 					utilizzoConfermaVO.setSupUtilizzataSecondaria(null);
		 				}
		 				
		 				
		 			
		        
		        //se nn ci sono errori
		        if(errori.size() == 0)
		        {
		          TipoEfaVO tipoEfaVO = null;
		          if(Validator.isNotEmpty(catalogoMatriceConfVO))
		          {
			          tipoEfaVO = gaaFacadeClient.getTipoEfaFromIdCatalogoMatrice(catalogoMatriceConfVO.getIdCatalogoMatrice());
		          }
		          
		          if(Validator.isNotEmpty(tipoEfaVO))
		          {
		            utilizzoConfermaVO.setIdTipoEfa(new Long(tipoEfaVO.getIdTipoEfa()));
		            BigDecimal supUtilizzata = new BigDecimal(utilizzoConfermaVO.getSuperficieUtilizzata().replace(",", ".")); 
		            utilizzoConfermaVO.setValoreOriginale(supUtilizzata);
		            utilizzoConfermaVO.setValoreDopoConversione(supUtilizzata.multiply(tipoEfaVO.getFattoreDiConversione()));
		            BigDecimal valoreCoversione = utilizzoConfermaVO.getValoreDopoConversione();
		            utilizzoConfermaVO.setValoreDopoPonderazione(valoreCoversione.multiply(tipoEfaVO.getFattoreDiPonderazione()));
		          }
		          else
		          {
		            utilizzoConfermaVO.setIdTipoEfa(null);
		            utilizzoConfermaVO.setValoreOriginale(null);
                utilizzoConfermaVO.setValoreDopoConversione(null);
                utilizzoConfermaVO.setValoreDopoPonderazione(null);
		          
		          }
		        }
		 				
	        
	          contaElementiNonEfa++;         
          }          
          else //utilizzi efa
          {
          
            double supUtilizzataDb = NumberUtils.arrotonda(Double.parseDouble(utilizzoConfermaVO.getSuperficieUtilizzata().replace(',', '.')), 4);
	          totSupUtilizzata += supUtilizzataDb;
	            
	          if(idParticellaChg.compareTo(storicoConfermaVO.getIdParticella()) !=0)
	          {
	            totSupUtilizzataParticella = supUtilizzataDb;
	          }
	          else
	          {
	            totSupUtilizzataParticella += supUtilizzataDb;
	          }
	          
          
          
            
          
            if(Validator.isNotEmpty(utilizzoConfermaVO.getIdUtilizzo())
              && (elencoTipiUsoSuoloEfa.get(utilizzoConfermaVO.getIdUtilizzo()) != null))
            {
            
              CatalogoMatriceVO catalogoMatriceConfVO = gaaFacadeClient.getCatalogoMatriceFromMatrice(utilizzoConfermaVO.getIdUtilizzo(),
                    utilizzoConfermaVO.getIdVarieta(), utilizzoConfermaVO.getIdTipoDestinazione(), utilizzoConfermaVO.getIdTipoDettaglioUso(), 
                    utilizzoConfermaVO.getIdTipoQualitaUso());
              BigDecimal supNetta = catalogoMatriceConfVO.getCoefficienteRiduzione();
              String supUtilizzataStr = utilizzoConfermaVO.getSuperficieUtilizzata();
                                
              if(!(Validator.validateDouble(supUtilizzataStr, 999999.9999) == null)) 
              {                  
                supNetta = supNetta.multiply(new BigDecimal(
                  supUtilizzataStr.replace(',','.')));
                utilizzoConfermaVO.setSupNetta(supNetta);
                
              }
              else
              {
                utilizzoConfermaVO.setSupNetta(new BigDecimal(0));
              } 
              
            }
          
          }
          
          elencoErroriUtilizzi.add(contaElementiTotali, errori);
          contaElementiTotali++;
          
          
	 			}
        
        
        
        //SUPERFICIE CONDOTTA
        if(Validator.isNotEmpty(request.getParameterValues("supCondotta")[i])) 
        {
          conduzioneConfermaVO.setSuperficieCondotta(request.getParameterValues("supCondotta")[i]);
          if(Validator.validateDouble(conduzioneConfermaVO.getSuperficieCondotta(), 999999.9999) == null) 
          {
            errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
            countErrori++;
          }
          else 
          {
            conduzioneConfermaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(request.getParameterValues("supCondotta")[i]));
            double supCondottaDb = Double.parseDouble(conduzioneConfermaVO.getSuperficieCondotta().replace(',', '.'));
            BigDecimal supCondottaTmpBg = new BigDecimal(conduzioneConfermaVO.getSuperficieCondotta().replace(',', '.'));
            if(supCondottaDb == 0)
            {
              errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_ZERO));
              countErrori++;
            }
            else
            {
              //Controllo fatto solo se la particella non è provvisoria
              if(Validator.isNotEmpty(storicoConfermaVO.getParticella()))
              {
	              /*String maxSupGrafCat = AnagUtils.valSupCatGraf(storicoConfermaVO.getSupCatastale(), storicoConfermaVO.getSuperficieGrafica());
	              if(Validator.isNotEmpty(storicoConfermaVO.getFoglioVO())
	                && Validator.isNotEmpty(storicoConfermaVO.getFoglioVO().getFlagStabilizzazione())   
		              && (storicoConfermaVO.getFoglioVO().getFlagStabilizzazione().compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0)
		              && Validator.validateDouble(storicoConfermaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
		              && Double.parseDouble(storicoConfermaVO.getSuperficieGrafica().replace(',', '.')) > 0)
		            {
		              maxSupGrafCat = storicoConfermaVO.getSuperficieGrafica();
		            }*/
		            
		            
		            String supGrafCat = storicoConfermaVO.getSupCatastale();
                if(Validator.validateDouble(storicoConfermaVO.getSuperficieGrafica(), SolmrConstants.FORMAT_SUP_UTILIZZATA) != null 
                  && Double.parseDouble(storicoConfermaVO.getSuperficieGrafica().replace(',', '.')) > 0)
                {
                  supGrafCat = storicoConfermaVO.getSuperficieGrafica();
                }
	              
	              double supGrafCatDb = Double.parseDouble(supGrafCat.replace(',', '.'));
	              if(supCondottaDb > supGrafCatDb)
	              {
	                errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MAX_CAT_GRAF));
	                countErrori++;                
	              }
	              else
	              {
	              
	                //Controllo che la somma delle Conduzioni selezionate e non, non superi la maxSupGrafCatDb!!!
			            if(idParticellaChg.compareTo(storicoConfermaVO.getIdParticella()) !=0)
			            {
			              supCondottaTotBg = supCondottaTmpBg;
			            }
			            else
			            {
			              supCondottaTotBg = supCondottaTotBg.add(supCondottaTmpBg);
			            }
			            
			            BigDecimal supCondottaAltreCondBg = gaaFacadeClient
			              .getSumSupCondottaAltreConduzioni(anagAziendaVO.getIdAzienda().longValue(), storicoConfermaVO.getIdParticella().longValue(),
			              hIdConduzioni.get(storicoConfermaVO.getIdParticella()));
			            supCondottaTotBg = supCondottaTotBg.add(supCondottaAltreCondBg);
			              
			            if(supCondottaTotBg.doubleValue() > supGrafCatDb)
			            {
			              errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUM_SUPERFICIE_CONDUZIONE));
			              countErrori++;
			            }
			            else
			            {          
	              
		                //Nel caso di diverso dell'assevimento devo controllare che la condotta 
		                //sia maggiore della somma della superficie degli utilizzi
		                if(Validator.isNotEmpty(conduzioneConfermaVO.getIdTitoloPossesso()) 
		                  && (conduzioneConfermaVO.getIdTitoloPossesso().intValue() != 5))
		                {
		                  if(supCondottaDb < NumberUtils.arrotonda(totSupUtilizzata, 4))
		                  {
		                    errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MIN_SUP_UTILIZ));
		                    countErrori++;
		                  }
		                }
		              }
	              }
              
              }
              //Particelle provvisorie controllo solo con al superficie utilizzata
              else
              {
                //Nel caso di diverso dell'assevimento devo controllare che la condotta 
                //sia maggiore della somma della superficie degli utilizzi
                if(conduzioneConfermaVO.getIdTitoloPossesso().intValue() != 5)
                {
                  if(supCondottaDb < NumberUtils.arrotonda(totSupUtilizzata, 4))
                  {
                    errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_KO_SUP_COND_MIN_SUP_UTILIZ));
                    countErrori++;
                  }
                }
              
              }
        
            }
            
          }
        }
        else
        {
          conduzioneConfermaVO.setSuperficieCondotta(null);
          errors.add("supCondotta", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
          countErrori++;
        }
        
        
        
        
        //Per evitare null pointer
        if(countErrori == 0 && countErroriUtilizzi == 0) 
        {         
          
          // SUPERFICIE AGRONOMICA
          
          if(Validator.isNotEmpty(request.getParameterValues("superficieAgronomica")[i])
            && (conduzioneConfermaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) != 0)) 
          {
            conduzioneConfermaVO.setSuperficieAgronomica(request.getParameterValues("superficieAgronomica")[i]);          
            if(Validator.validateDouble(conduzioneConfermaVO.getSuperficieAgronomica(), 999999.9999) == null) 
            {
              errors.add("superficieAgronomica", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
              countErrori++;
            }
            else 
            {
              conduzioneConfermaVO.setSuperficieAgronomica(StringUtils.parseSuperficieField(request.getParameterValues("superficieAgronomica")[i]));
              if(NumberUtils.arrotonda(Double.parseDouble(conduzioneConfermaVO.getSuperficieAgronomica().replace(',', '.')), 4) 
                > NumberUtils.arrotonda(Double.parseDouble(conduzioneConfermaVO.getSuperficieCondotta().replace(',', '.')), 4)) 
              {
                errors.add("superficieAgronomica", new ValidationError(AnagErrors.ERRORE_SUP_AGRONOMICA_MAX_SUP_CONDOTTA));
                countErrori++;
              }
            }
          }
          else 
          {
            conduzioneConfermaVO.setSuperficieAgronomica(null); 
          }
        }
        
        
        idParticellaChg = storicoConfermaVO.getIdParticella();          
        
	 			elencoErroriConduzioniStorico.add(errors);
	 		}
	 		
	 		// Se si sono verificati degli errori li visualizzo
	 		if(countErrori > 0 || countErroriUtilizzi > 0) {
	 			request.setAttribute("elencoErroriConduzioniStorico", elencoErroriConduzioniStorico);
	 			request.setAttribute("elencoErroriUtilizzi", elencoErroriUtilizzi);
	 			%>
	 				<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
	 			<%
	 		}
	 		
	 		// Se non si sono verificati errori controllo se ci sono dei documenti attivi 
	 		// legati alle conduzioni selezionate
	 		if(!Validator.isNotEmpty(request.getParameter("confermoOperazione"))) 
      {
		 		Vector elencoDocumentiConduzioni = null;
		 		try 
        {
		 			for(int a = 0; a < elencoStoricoParticella.length; a++) 
          {
		 				StoricoParticellaVO storicoForDocumenti = (StoricoParticellaVO)elencoStoricoParticella[a];
		 				elencoDocumentiConduzioni = anagFacadeClient.getListDettaglioDocumentoByIdConduzione(storicoForDocumenti.getElencoConduzioni()[0].getIdConduzioneParticella(), false, false);
		 				// Se ci sono torno alla pagina di modifica e avviso l'utente degli
		 		 		// effetti della sua scelta
		 		 		if(elencoDocumentiConduzioni != null && elencoDocumentiConduzioni.size() > 0) 
            {
		 		 			// Verifico se effettivamente sono stati modificati i dati
		 		 			// della conduzione
		 		 			for(int b = 0; b < elencoDocumentiConduzioni.size(); b++) 
              {
		 		 				ConduzioneParticellaVO oldConduzioneParticellaVO = anagFacadeClient.findConduzioneParticellaByPrimaryKey(storicoForDocumenti.getElencoConduzioni()[0].getIdConduzioneParticella());
		 		 				if(oldConduzioneParticellaVO == null) 
                {
		 		 					messaggioErrore = (String)AnagErrors.ERRORE_KO_CONDUZIONE_PARTICELLE;
	 				    		request.setAttribute("messaggioErrore", messaggioErrore);
	 				    		%>
	 				    			<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
	 				    		<%
	 				    		break;
		 		 				}
		 		 				else 
                {
                  //Per mettere i datiuguali nel confronto!!!!
		 		 					oldConduzioneParticellaVO.setSuperficieCondotta(StringUtils.parseSuperficieField(oldConduzioneParticellaVO.getSuperficieCondotta()));
		 		 					if(!oldConduzioneParticellaVO.equalsDatiConduzione(storicoForDocumenti.getElencoConduzioni()[0])) 
                  {
		 		 						request.setAttribute("onLoad", "onLoad");
		 			 		 			%>
		 			 						<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
		 			 					<%
		 			 					break;			
		 		 					}
		 		 				}
		 		 			}
		 		 		}
		 			}
		 		}
		 		catch(SolmrException se) 
        {
	    		messaggioErrore = (String)AnagErrors.ERRORE_KO_DOCUMENTI_PARTICELLE;
	    		request.setAttribute("messaggioErrore", messaggioErrore);
	    		%>
	    			<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
	    		<%	
	    	}
	 		} 		
	 		
	 		// Terminati tutti i controlli effettuo la modifica
			try 
      {
				anagFacadeClient.modificaParticelle(elencoStoricoParticella, ruoloUtenza, anagAziendaVO);
			}
			catch(SolmrException se) {
				SolmrLogger.error(this,"--- Exception in terreniParticellareModificaCtrl ="+se.getMessage());
				messaggioErrore = (String)AnagErrors.ERRORE_KO_MODIFICA;
	    		request.setAttribute("messaggioErrore", messaggioErrore);
	    		%>
	    			<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
	    		<%
			}
			// Vado alla pagina di ricerca/elenco
			String valorePagina = (String)session.getAttribute("pagina");
			%>
				<jsp:forward page="<%= terreniParticellareElencoCtrlUrl %>" >
					<jsp:param name="pagina" value="<%= valorePagina %>" /> 
				</jsp:forward>
			<%
	 	}
 	}
	
 	// Vado alla pagina di modifica
 	%>
		<jsp:forward page="<%= terreniParticellareModificaUrl %>" />
		
<%!// Vengono effettuati controlli di validazione e suddivisione degli allegati da inserire, da modificare o da concellare
  protected boolean isUtilizzoAttivo(Long idUtilizzo, Vector<Long> vIdUtilizzo)
  {
    boolean attivo = false;

    if (Validator.isNotEmpty(idUtilizzo) && Validator.isNotEmpty(vIdUtilizzo)
        && (vIdUtilizzo.size() > 0))
    {
      for (int i = 0; i < vIdUtilizzo.size(); i++)
      {
        if (idUtilizzo.compareTo(vIdUtilizzo.get(i)) == 0)
        {
          attivo = true;
          break;
        }
      }

    }

    return attivo;
  }%>

