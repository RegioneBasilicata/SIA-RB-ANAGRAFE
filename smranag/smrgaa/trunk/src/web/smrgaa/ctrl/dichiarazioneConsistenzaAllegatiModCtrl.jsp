<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@page import="it.csi.solmr.dto.StringcodeDescription"%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.attestazioni.*"%>

<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "dichiarazioneConsistenzaAllegatiModCtrl.jsp";

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String dichiarazioneConsistenzaAllegatiModUrl = "/view/dichiarazioneConsistenzaAllegatiModView.jsp";		
	
	//Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
	
	String dichiarazioneConsistenzaUrl ="";
	String dichiarazioneConsistenzaAllegatiUrl ="";
	
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI)){
		dichiarazioneConsistenzaUrl = "../layout/";
		dichiarazioneConsistenzaAllegatiUrl = "../layout/";
	}	
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY)){
		dichiarazioneConsistenzaUrl = "/layout/";
		dichiarazioneConsistenzaAllegatiUrl = "/layout/";
	}	
	dichiarazioneConsistenzaUrl += "dichiarazioneConsistenza.htm";
	dichiarazioneConsistenzaAllegatiUrl += "dichiarazioneConsistenzaAllegati";
	
	
	
	session.removeAttribute("idDichiarazioneConsistenzaAllegati");

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	
	// Recupero il piano di riferimento selezionato
	Long idDichiarazioneConsistenza = Long.decode(request.getParameter("idDichiarazioneConsistenza"));
	request.setAttribute("idDichiarazioneConsistenza", idDichiarazioneConsistenza);
	Integer idTipoAllegato = Integer.decode(request.getParameter("idTipoAllegato"));
	
	ConsistenzaVO consistenzaVO = null;
	TipoAllegatoVO tipoAllegatoVO = null;
	try 
  {
	  consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idDichiarazioneConsistenza);
		request.setAttribute("consistenzaVO", consistenzaVO);
		
		AllegatoDichiarazioneVO allegatoDichiarazioneVO = gaaFacadeClient
      .getAllegatoDichiarazioneFromIdDichiarazione(idDichiarazioneConsistenza, new Long(new Long(SolmrConstants.VALIDAZIONE_ALLEGATO)));
    request.setAttribute("allegatoDichiarazioneVO", allegatoDichiarazioneVO);
    
    tipoAllegatoVO = gaaFacadeClient.getTipoAllegatoById(idTipoAllegato);
    
    
  }
	catch(SolmrException se) 
	{
	 	String messaggio = AnagErrors.ERRORE_KO_PIANO_RIFERIMENTO;
	 	request.setAttribute("messaggioErrore",messaggio);
	 	%>
	 		<jsp:forward page="<%=dichiarazioneConsistenzaAllegatiModUrl%>" />
	 	<%
	 	return;
	}
	
	
	// Richiamo la procedura PL-SQL per l'aggiornamento delle attestazioni
	try 
  {
		anagFacadeClient.aggiornaAttestazioniPlSql(anagAziendaVO.getIdAzienda(), ruoloUtenza.getIdUtente(), idDichiarazioneConsistenza, tipoAllegatoVO.getCodiceAttestazione());	  
	}
	catch(SolmrException se) 
	{
		String messaggio = "ERRORE PLQL-PACK_AGGIORNA_ATTESTAZIONI.CARICA_ATTESTAZIONI: "+se.getMessage();
  	request.setAttribute("messaggioErrore",messaggio);
  	%>
	  	<jsp:forward page="<%=dichiarazioneConsistenzaAllegatiModUrl%>" />
   	<%
   	return;
	}

	// Effettuo la ricerca in funzione del piano di riferimento selezionato
	Vector<TipoAttestazioneVO> elencoAttestazioni = null;
	try 
	{
		//...ricerco i dati e presento le selezioni fatte in precedenza
		Date dataAnnoCampagna = DateUtils.parseDate("31/12/"+consistenzaVO.getAnno());
		if(Validator.isNotEmpty(consistenzaVO.getAnnoCampagna()))
		{
		  dataAnnoCampagna = DateUtils.parseDate("31/12/"+consistenzaVO.getAnnoCampagna());
		}			
		elencoAttestazioni = anagFacadeClient.getElencoAttestazioniAllaDichiarazione(consistenzaVO.getCodiceFotografiaTerreni().toString(), 
		  dataAnnoCampagna, tipoAllegatoVO.getCodiceAttestazione());
		
	}
	catch(SolmrException se) 
	{
		String messaggio = AnagErrors.ERR_KO_SEARCH_ATTESTAZIONI;
  	request.setAttribute("messaggioErrore",messaggio);
  	%>
   		<jsp:forward page="<%=dichiarazioneConsistenzaAllegatiModUrl%>" />
  	<%
  	return;
	}
	request.setAttribute("elencoAttestazioni", elencoAttestazioni);
	// L'utente ha selezionato il pulsante conferma
	if(request.getParameter("conferma") != null) 
  {
		// Recupero le dichiarazioni selezionate
		String[] attestazioniSelected = request.getParameterValues("valueIdAttestazione");
		Hashtable<Long,Long> elencoIdSelezionati = new Hashtable<Long,Long>();
		Hashtable<Long,StringcodeDescription> elencoErrori = new Hashtable<Long,StringcodeDescription>();
		Hashtable<Long, ParametriAttDichiarataVO> elencoParametri = new Hashtable<Long, ParametriAttDichiarataVO>();
		// Ciclo sugli id_selezionati per recuperare gli eventuali valori inputati dall'utente
		if(attestazioniSelected != null) 
    {
			for(int i = 0; i < attestazioniSelected.length; i++) 
      {
				Long idAttestazione = Long.decode(attestazioniSelected[i]);
				elencoIdSelezionati.put(idAttestazione, idAttestazione);
				// Cerco se sono previsti parametri per l'attestazione selezionata
				TipoParametriAttestazioneVO tipoParametriAttestazioneVO = null;
				try 
        {
					tipoParametriAttestazioneVO = anagFacadeClient.findTipoParametriAttestazioneByIdAttestazione(idAttestazione);
				}
				catch(SolmrException se) 
        {
					String messaggio = AnagErrors.ERR_KO_SEARCH_TIPO_PARAMETRI_ATTESTAZIONI;
		   		request.setAttribute("messaggioErrore",messaggio);
		   		%>
		    		<jsp:forward page="<%=dichiarazioneConsistenzaAllegatiModUrl%>" />
		   		<%
		   		return;
				}
				// Effettuo il controllo dei dati
				if(tipoParametriAttestazioneVO != null) 
        {
								
					//alla dichiarazione di consistenza
					ParametriAttDichiarataVO parametriAttDichiarataVO = new ParametriAttDichiarataVO();
					// PARAMETRO 1
					if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getParametro1())) 
					{
						// Verifico se è obbligatorio
						if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getObbligatorio1()) 
						  && tipoParametriAttestazioneVO.getObbligatorio1().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
						{
							if(!Validator.isNotEmpty(request.getParameter("Parametro1"+idAttestazione))) 
							{
								elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro1", AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
							}
						}
						if(Validator.isNotEmpty(request.getParameter("Parametro1"+idAttestazione))) 
						{
							if(Validator.isNotEmpty(request.getParameter("Parametro1"+idAttestazione))) 
							{
								String tipoParametro = tipoParametriAttestazioneVO.getParametro1();
								if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_DATA)) 
								{
									if(!Validator.validateDateF(request.getParameter("Parametro1"+idAttestazione))) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro1", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								}
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_NUMERICO)) 
								{
									if(!Validator.isNumericInteger(request.getParameter("Parametro1"+idAttestazione)) 
									  && Validator.validateDoubleDigit(request.getParameter("Parametro1"+idAttestazione), SolmrConstants.FORMAT_SUP_CATASTALE, 4) == null) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro1", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								} 
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO) 
								  || tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO_MULTILINE)) 
								{
									if(request.getParameter("Parametro1"+idAttestazione).length() > 1000) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro1", (String)AnagErrors.get("ERR_MAX_1000_CARATTERI")));
									}
								}
							}
						}
						parametriAttDichiarataVO.setParametro1(request.getParameter("Parametro1"+idAttestazione));
					}
					// PARAMETRO 2
					if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getParametro2())) 
					{
						// Verifico se è obbligatorio
						if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getObbligatorio2()) 
						  && tipoParametriAttestazioneVO.getObbligatorio2().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
						{
							if(!Validator.isNotEmpty(request.getParameter("Parametro2"+idAttestazione))) 
							{
								elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro2", AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
							}
						}
						if(Validator.isNotEmpty(request.getParameter("Parametro2"+idAttestazione))) 
						{
							if(Validator.isNotEmpty(request.getParameter("Parametro2"+idAttestazione))) 
							{
								String tipoParametro = tipoParametriAttestazioneVO.getParametro2();
								if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_DATA)) 
								{
									if(!Validator.validateDateF(request.getParameter("Parametro2"+idAttestazione))) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro2", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								}
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_NUMERICO)) 
								{
									if(!Validator.isNumericInteger(request.getParameter("Parametro2"+idAttestazione)) 
									  && Validator.validateDoubleDigit(request.getParameter("Parametro2"+idAttestazione), SolmrConstants.FORMAT_SUP_CATASTALE, 4) == null) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro2", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								} 
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO) 
								  || tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO_MULTILINE)) 
								{
									if(request.getParameter("Parametro2"+idAttestazione).length() > 1000) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro2", (String)AnagErrors.get("ERR_MAX_1000_CARATTERI")));
									}
								}
							}
						}
						parametriAttDichiarataVO.setParametro2(request.getParameter("Parametro2"+idAttestazione));
					}
					// PARAMETRO 3
					if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getParametro3())) 
					{
						// Verifico se è obbligatorio
						if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getObbligatorio3()) 
						  && tipoParametriAttestazioneVO.getObbligatorio3().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
						{
							if(!Validator.isNotEmpty(request.getParameter("Parametro3"+idAttestazione))) 
							{
								elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro3", AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
							}
						}
						if(Validator.isNotEmpty(request.getParameter("Parametro3"+idAttestazione))) 
						{
							if(Validator.isNotEmpty(request.getParameter("Parametro3"+idAttestazione))) 
							{
								String tipoParametro = tipoParametriAttestazioneVO.getParametro3();
								if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_DATA)) 
								{
									if(!Validator.validateDateF(request.getParameter("Parametro3"+idAttestazione))) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro3", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								}
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_NUMERICO)) 
								{
									if(!Validator.isNumericInteger(request.getParameter("Parametro3"+idAttestazione)) 
									  && Validator.validateDoubleDigit(request.getParameter("Parametro3"+idAttestazione), SolmrConstants.FORMAT_SUP_CATASTALE, 4) == null) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro3", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								} 
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO) 
								  || tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO_MULTILINE)) 
								{
									if(request.getParameter("Parametro3"+idAttestazione).length() > 1000) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro3", (String)AnagErrors.get("ERR_MAX_1000_CARATTERI")));
									}
								}
							}
						}
						parametriAttDichiarataVO.setParametro3(request.getParameter("Parametro3"+idAttestazione));
					}
					// PARAMETRO 4
					if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getParametro4())) 
					{
						// Verifico se è obbligatorio
						if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getObbligatorio4()) 
					    && tipoParametriAttestazioneVO.getObbligatorio4().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
					  {
							if(!Validator.isNotEmpty(request.getParameter("Parametro4"+idAttestazione))) 
							{
								elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro4", AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
							}
						}
						if(Validator.isNotEmpty(request.getParameter("Parametro4"+idAttestazione))) 
						{
							if(Validator.isNotEmpty(request.getParameter("Parametro4"+idAttestazione))) 
							{
								String tipoParametro = tipoParametriAttestazioneVO.getParametro4();
								if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_DATA)) 
								{
									if(!Validator.validateDateF(request.getParameter("Parametro4"+idAttestazione))) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro4", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								}
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_NUMERICO)) 
								{
									if(!Validator.isNumericInteger(request.getParameter("Parametro4"+idAttestazione)) 
									  && Validator.validateDoubleDigit(request.getParameter("Parametro4"+idAttestazione), SolmrConstants.FORMAT_SUP_CATASTALE, 4) == null) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro4", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								} 
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO) 
								  || tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO_MULTILINE)) 
								{
									if(request.getParameter("Parametro4"+idAttestazione).length() > 1000) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro4", (String)AnagErrors.get("ERR_MAX_1000_CARATTERI")));
									}
								}
							}
						}
						parametriAttDichiarataVO.setParametro4(request.getParameter("Parametro4"+idAttestazione));
					}
					// PARAMETRO 5
					if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getParametro5())) 
					{
						// Verifico se è obbligatorio
						if(Validator.isNotEmpty(tipoParametriAttestazioneVO.getObbligatorio5()) 
						  && tipoParametriAttestazioneVO.getObbligatorio5().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
						{
							if(!Validator.isNotEmpty(request.getParameter("Parametro5"+idAttestazione))) 
							{
								elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro5", AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
							}
						}
						if(Validator.isNotEmpty(request.getParameter("Parametro5"+idAttestazione))) 
						{
							if(Validator.isNotEmpty(request.getParameter("Parametro5"+idAttestazione))) 
							{
								String tipoParametro = tipoParametriAttestazioneVO.getParametro5();
								if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_DATA)) 
								{
									if(!Validator.validateDateF(request.getParameter("Parametro5"+idAttestazione))) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro5", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								}
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_NUMERICO)) 
								{
									if(!Validator.isNumericInteger(request.getParameter("Parametro5"+idAttestazione)) 
									  && Validator.validateDoubleDigit(request.getParameter("Parametro5"+idAttestazione), SolmrConstants.FORMAT_SUP_CATASTALE, 4) == null) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro5", AnagErrors.ERRORE_CAMPO_ERRATO));
									}
								} 
								else if(tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO) 
								  || tipoParametro.equalsIgnoreCase(SolmrConstants.TIPO_PARAMETRO_ATTESTAZIONE_TESTO_MULTILINE)) 
								{
									if(request.getParameter("Parametro5"+idAttestazione).length() > 1000) 
									{
										elencoErrori.put(idAttestazione, new StringcodeDescription("Parametro5", (String)AnagErrors.get("ERR_MAX_1000_CARATTERI")));
									}
								}
							}
						}
						parametriAttDichiarataVO.setParametro5(request.getParameter("Parametro5"+idAttestazione));
					}
					elencoParametri.put(idAttestazione, parametriAttDichiarataVO);
					
				}
			}
		}
		
		
		// Se ci sono errori li visualizzo
		if(elencoErrori.size() > 0) 
    {
			request.setAttribute("elencoErrori", elencoErrori);
			request.setAttribute("reload", new Boolean(true));
			request.setAttribute("elencoIdSelezionati", elencoIdSelezionati);
			%>
  			<jsp:forward page="<%=dichiarazioneConsistenzaAllegatiModUrl%>" />
 			<%
 			return;
		}
		// Altrimenti effettuo l'aggiornamento delle attestazioni
		else 
    {
			try 
      {
				anagFacadeClient.aggiornaElencoAllegatiAttestazioni(attestazioniSelected, ruoloUtenza, elencoParametri, 
				  tipoAllegatoVO, consistenzaVO);
			}
			catch(SolmrException se) 
      {
				String messaggio = AnagErrors.ERR_KO_AGGIORNA_ALLEGATI;
	   		request.setAttribute("messaggioErrore",messaggio);
	   		%>
	    		<jsp:forward page="<%=dichiarazioneConsistenzaAllegatiModUrl%>" />
	   		<%
	   		return;
			}
			
			session.setAttribute("idDichiarazioneConsistenzaStampaPartenza", idDichiarazioneConsistenza);
			
			// Vado alla pagina di validazioni
			%>
				<jsp:forward page="<%= dichiarazioneConsistenzaUrl%>" />
			<%
			return;
		}
	}
	// L'utente ha selezionato il pulsante annulla
	else if(request.getParameter("indietro") != null) 
	{
		// Vado alla pagina di dettaglio
		%>
			<jsp:forward page="<%=dichiarazioneConsistenzaAllegatiUrl%>" />
		<%
		return;
	}

	// Vado alla pagina di modifica
	%>
		<jsp:forward page="<%=dichiarazioneConsistenzaAllegatiModUrl%>" />
