<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="it.csi.solmr.dto.anag.services.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<jsp:useBean id="delegaVO" scope="request" class="it.csi.solmr.dto.anag.DelegaVO"/>



<%
	String iridePageName = "registrazioneDelegaCtrl.jsp";

	%>
  	<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");

  String avantiIndietroURL = "/view/elencoView.jsp";
  String registrazioneDelegaURL = "/view/registrazioneDelegaView.jsp";
  String dettaglioURL = "/view/anagraficaView.jsp";

  ValidationErrors errors = new ValidationErrors();
  String messaggioErrore = null;
  UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = null;
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    
  if(request.getParameter("conferma") != null) 
  {
  	
  	// Recupero la data inizio mandato
  	String dataInizioMandato = request.getParameter("dataInizioMandato");
  	request.setAttribute("dataInizioMandato", dataInizioMandato);
  	
  	// Controllo che sia stata valorizzata
  	if(!Validator.isNotEmpty(dataInizioMandato)) 
    {
  		ValidationError error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
     	errors.add("dataInizioMandato", error);
  	}
  	// Se lo è...
  	else 
    {
  		// ... controllo che sia valida
  		if(!Validator.validateDateF(dataInizioMandato)) 
      {
  			ValidationError error = new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO);
       	errors.add("dataInizioMandato", error);
  		}
  		// Se lo è, controllo che non sia maggiore della data odierna
  		else if(DateUtils.parseDate(dataInizioMandato).after(DateUtils.parseDate(DateUtils.getCurrentDateString()))) 
      {
  			ValidationError error = new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_MANDATO_POST_SYSDATE);
       	errors.add("dataInizioMandato", error);
  		}
  		//... Se non è maggiore della data odierna controllo infine che non 
  		// sia maggiore della max data fine mandato
  		else 
      {
    		java.util.Date maxDataFineMandato = null;
    		try 
        {
    			maxDataFineMandato = anagFacadeClient.getDataMaxFineMandato(anagAziendaVO.getIdAzienda());
    		}
    		catch(SolmrException se) 
        {
         	messaggioErrore = AnagErrors.ERRORE_KO_MAX_DATA_FINE_MANDATO;
         	request.setAttribute("messaggioErrore", messaggioErrore);
         	%>
         		<jsp:forward page = "<%=registrazioneDelegaURL%>" />
         	<%
        }
    		if(maxDataFineMandato != null) 
        {
    			if(DateUtils.parseDate(dataInizioMandato).before(maxDataFineMandato)) 
          {
    				ValidationError error = new ValidationError(AnagErrors.ERRORE_DATA_INIZIO_MANDATO_POST_MAX_DATA_FINE_MANDATO);
           	errors.add("dataInizioMandato", error);
    			}
    		}
  		}
  	}
    	
  	// Recupero gli uffici zona intermediario
  	Vector elencoUfficiZonaIntermediario = null;
  	try 
    {
    	elencoUfficiZonaIntermediario = anagFacadeClient.getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(utenteAbilitazioni);
  	}
  	catch(SolmrException se) 
    {
    	messaggioErrore = se.getMessage();
    	request.setAttribute("messaggioErrore", messaggioErrore);
    	%>
      	<jsp:forward page = "<%=registrazioneDelegaURL%>" />
    	<%
  	}
  	request.setAttribute("elencoUfficiZonaIntermediario", elencoUfficiZonaIntermediario);

  	String tipiUffici = request.getParameter("idUfficioZonaIntermediario");
  	if(!Validator.isNotEmpty(tipiUffici)) 
    {
    	ValidationError error = new ValidationError((String)AnagErrors.get("ERR_TIPO_UFFICIO_INTERMEDIARIO_OBBLIGATORIO"));
    	errors.add("idUfficioZonaIntermediario", error);
  	}
  	else 
    {
  		try 
      {
  			ufficioZonaIntermediarioVO = anagFacadeClient.findUfficioZonaIntermediarioVOByPrimaryKey(Long.decode(tipiUffici));
  			request.setAttribute("ufficioZonaIntermediarioVO", ufficioZonaIntermediarioVO);
  		}
  		catch(SolmrException se) 
      {
    		messaggioErrore = AnagErrors.ERRORE_KO_UFFICIO_ZONA_INTERMEDIARIO;
    		request.setAttribute("messaggioErrore", messaggioErrore);
    		%>
      		<jsp:forward page = "<%=registrazioneDelegaURL%>" />
    		<%
  		}
  	}
  	if(errors != null && errors.size() > 0) 
    {
  		request.setAttribute("errors", errors);
    	request.getRequestDispatcher(registrazioneDelegaURL).forward(request, response);
    	return;	
  	}
  	else 
    {
    	// Recupero da COMUNE il valore del parametro "MAPR" per la gestione dell'inserimento del documento
    	String parametroComuneMapr = null;
    	try 
      {
      	parametroComuneMapr = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_COMUNE_MAPR);
    	}
    	catch(SolmrException se) 
      {
      	// Catturo l'eccezione ma non modifico il comportamento dell'applicativo: se non fosse possibile reperire il
      	// valore da comune lo considero = null
    	}
    	DocumentoVO documentoVO = null;
    	it.csi.solmr.dto.comune.IntermediarioVO intermediarioVO = null;
	    // Recupero il codice fiscale dell'ufficio di zona a cui è stato assegnato il mandato
  		try 
      {
    		intermediarioVO = anagFacadeClient.getIntermediarioVOByIdUfficioZonaIntermediario(Long.decode(tipiUffici));
  		}
  		catch(SolmrException se) 
      {
    		ValidationError error = new ValidationError(se.getMessage());
    		errors.add("idUfficioZonaIntermediario", error);
    		request.setAttribute("errors", errors);
    		request.getRequestDispatcher(registrazioneDelegaURL).forward(request, response);
    		return;
  		}
    	if(Validator.isNotEmpty(parametroComuneMapr) && parametroComuneMapr.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
    		documentoVO = new DocumentoVO();
    		documentoVO.setExtIdDocumento(SolmrConstants.ID_TIPO_DOCUMENTO_MANDATO_ASSISTENZA);
    		documentoVO.setIdAzienda(anagAziendaVO.getIdAzienda());
    		documentoVO.setCuaa(anagAziendaVO.getCUAA());
    		documentoVO.setDataInizioValidita(new java.util.Date(System.currentTimeMillis()));
    		documentoVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
		    documentoVO.setDataInserimento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
    		documentoVO.setNote("CAA: "+intermediarioVO.getCodiceFiscale());
    	}
    	try 
      {
    		anagAziendaVO.setIdIntermediarioDelegato(new Long(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario()).toString());
    		ufficioZonaIntermediarioVO.setIdUfficioZonaIntermediario(Long.decode(tipiUffici));
    		DelegaVO delegaInsVO = new DelegaVO();
    		delegaInsVO.setIdIntermediario(intermediarioVO.getIdIntermediarioLong());
    		delegaInsVO.setIdProcedimento(Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)));
    		delegaInsVO.setIdAzienda(anagAziendaVO.getIdAzienda());
    		delegaInsVO.setDataInizio(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
    		delegaInsVO.setIdUtenteInsDelega(ruoloUtenza.getIdUtente());
    		delegaInsVO.setIdUfficioZonaIntermediario(ufficioZonaIntermediarioVO.getIdUfficioZonaIntermediario());
    		delegaInsVO.setDataInizioMandato(DateUtils.parseDate(dataInizioMandato));
    		anagFacadeClient.insertDelegaForMandato(anagAziendaVO, ruoloUtenza, delegaInsVO, documentoVO);
    		delegaVO = anagFacadeClient.getDelegaByAziendaAndIdProcedimento(anagAziendaVO.getIdAzienda(), Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)));
    		anagAziendaVO.setDelegaVO(delegaVO);
    		anagAziendaVO.setPossiedeDelegaAttiva(true);
          		
				// Se l'azienda selezionata ha flag_provvisoria = NULL o = N e id_azienda_provenienza
  			// not null
  			if(anagAziendaVO.getIdAziendaProvenienza() != null) 
        {
  				AnagAziendaVO anagAziendaProvenienzaVO = null;
  				try 
          {
  					anagAziendaProvenienzaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaProvenienza());
  					request.setAttribute("anagAziendaProvenienzaVO", anagAziendaProvenienzaVO);
  				}
  				catch(SolmrException se) 
          {
  	       	ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_PROVENIENZA);
  	        errors.add("error", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(registrazioneDelegaURL).forward(request, response);
            return;
  	      }
  			}
  			// Recupero le eventuali aziende di destinazione dell'azienda in esame
  			AnagAziendaVO[] elencoAziendeDestinazione = null;
  			try 
        {
  				elencoAziendeDestinazione = anagFacadeClient.getListAnagAziendaDestinazioneByIdAzienda(anagAziendaVO.getIdAzienda(), true, null);
  				request.setAttribute("elencoAziendeDestinazione", elencoAziendeDestinazione);
  			}
  			catch(SolmrException se) 
        {
  	     	ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_DESTINAZIONE);
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(registrazioneDelegaURL).forward(request, response);
          return;
        }
        		
    		session.setAttribute("anagAziendaVO",anagAziendaVO);
    		ruoloUtenza.setIsIntermediarioConDelega(true);
    		ruoloUtenza.setIsIntermediarioPadre(false);
    		session.setAttribute("ruoloUtenza",ruoloUtenza);
    		%>
      		<jsp:forward page = "<%=dettaglioURL%>" />
    		<%
    	}
    	catch(SolmrException se) 
      {
      	// Come richiesto dal dominio, se l'errore è relativo ad un tentativo contemporaneo
      	// di inserimento delega, mostro l'errore vicino alla combo degli uffici di zona
        if(se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_DELEGA_CONTEMPORANEA"))) 
        {
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("idUfficioZonaIntermediario", error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(registrazioneDelegaURL).forward(request, response);
        	return;
      	}
      	messaggioErrore = se.getMessage();
      	request.setAttribute("messaggioErrore", messaggioErrore);
      	%>
        	<jsp:forward page = "<%=registrazioneDelegaURL%>" />
      	<%
    	}
    }
  }
  // L'utente ha modificato il valore della combo contenenti gli uffici
  // zona intermediario
  else if(Validator.isNotEmpty(request.getParameter("operazione"))) 
  {
  	
	  // Recupero la data inizio mandato
  	String dataInizioMandato = request.getParameter("dataInizioMandato");
  	request.setAttribute("dataInizioMandato", dataInizioMandato);
  	
  	String tipiUffici = request.getParameter("idUfficioZonaIntermediario");
  	// Recupero gli uffici zona intermediario
  	Vector elencoUfficiZonaIntermediario = null;
  	try 
    {
    	elencoUfficiZonaIntermediario = anagFacadeClient.getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(utenteAbilitazioni);
  	}
  	catch(SolmrException se) {
    	messaggioErrore = se.getMessage();
    	request.setAttribute("messaggioErrore", messaggioErrore);
    	%>
      		<jsp:forward page = "<%=registrazioneDelegaURL%>" />
    	<%
  	}
  	request.setAttribute("elencoUfficiZonaIntermediario", elencoUfficiZonaIntermediario);
  	if(Validator.isNotEmpty(tipiUffici)) 
    {
    	try 
      {
      	ufficioZonaIntermediarioVO = anagFacadeClient.findUfficioZonaIntermediarioVOByPrimaryKey(Long.decode(tipiUffici));
    	}
    	catch(SolmrException se) 
      {
      	messaggioErrore = se.getMessage();
      	request.setAttribute("messaggioErrore", messaggioErrore);
      	%>
       		<jsp:forward page = "<%=registrazioneDelegaURL%>" />
      	<%
    	}
  	}
  	else 
    {
    	ufficioZonaIntermediarioVO = null;
  	}
  	request.setAttribute("ufficioZonaIntermediarioVO", ufficioZonaIntermediarioVO);
  	%>
    	<jsp:forward page = "<%=registrazioneDelegaURL%>" />
  	<%
  }
  // L'utente ha selezionato la voce di menù "registra mandato".
  else 
  {
  	session.removeAttribute("anagAziendaVO");
    Vector rangeAnagAzienda = (Vector)session.getAttribute("listRange");
    String dataInizioMandato = DateUtils.getCurrentDateString();
    request.setAttribute("dataInizioMandato", dataInizioMandato);
    // Controllo che l'utente abbia selezionato un'azienda
    if(!Validator.isNotEmpty(request.getParameter("idAzienda"))) 
    {
    	ValidationError error = new ValidationError((String)AnagErrors.get("ERR_SELEZIONE_ELENCO_KO"));
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(avantiIndietroURL).forward(request, response);
    	return;
  	}
  	if(rangeAnagAzienda!= null)
    {
    	anagAziendaVO = null;
    	for(int i = 0; i< rangeAnagAzienda.size();i++) 
      {
      	anagAziendaVO = (AnagAziendaVO)rangeAnagAzienda.elementAt(i);
      	if(anagAziendaVO.getIdAnagAzienda().toString().equals(request.getParameter("idAzienda"))) 
        {
       		break;
      	}
    	}
    	// Recupero i dati relativi alla delega
    	try 
      {
    		MandatoVO mandatoVO = null;
    		DelegaAnagrafeVO delegaAnagrafeVO = null;
    		try 
        {
      		mandatoVO = anagFacadeClient.serviceGetMandato(anagAziendaVO.getIdAzienda(), ruoloUtenza.getCodiceEnte());
      		if(mandatoVO != null) 
          {
      			delegaAnagrafeVO = mandatoVO.getDelegaAnagrafe();
      		}	
    		}
    		catch(Exception e) 
        {}
    		if(!anagFacadeClient.controllaRegistrazioneMandato(anagAziendaVO, ruoloUtenza.getCodiceEnte(), delegaAnagrafeVO)) 
        {
      		throw new SolmrException((String)AnagErrors.get("ERR_REG_MANDATO_NON_OBBLIGATORIO"));
    		}
    		// Recupero in modo corretto i dati dell'intermediario
    		//non dovrebeb servirepiù!!!
    		/*it.csi.solmr.dto.comune.IntermediarioVO intermediarioVO = null;
    		if(Validator.isNotEmpty(ruoloUtenza.getCodiceEnte())) 
        {
      		try 
          {
      			intermediarioVO = anagFacadeClient.serviceFindIntermediarioByCodiceFiscale(ruoloUtenza.getCodiceEnte());
      		}
      		catch(SolmrException se) 
          {
      			messaggioErrore = se.getMessage();
      			request.setAttribute("messaggioErrore", messaggioErrore);
      			%>
      				<jsp:forward page = "<%=registrazioneDelegaURL%>" />
        		<%
      		}
    		}
    		profile.setIntermediarioVO(intermediarioVO);*/
    		// Recupero gli uffici zona intermediario
    		Vector elencoUfficiZonaIntermediario = null;
    		try 
        {
      		elencoUfficiZonaIntermediario = anagFacadeClient.getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(utenteAbilitazioni);
    		}
    		catch(SolmrException se) 
        {
      		messaggioErrore = se.getMessage();
      		request.setAttribute("messaggioErrore", messaggioErrore);
      		%>
        		<jsp:forward page = "<%=registrazioneDelegaURL%>" />
      		<%
    		}
    		request.setAttribute("elencoUfficiZonaIntermediario", elencoUfficiZonaIntermediario);
    	}
    	catch(SolmrException sex) 
      {
      	ValidationError error = new ValidationError(sex.getMessage());
      	errors.add("error", error);
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(avantiIndietroURL).forward(request, response);
      	return;
    	}
    	
    	
    	
    	//controllo che non sia un intermediario
    	try 
      {
        boolean isAziendIntermediario = anagFacadeClient.isAziendaIntermediario(anagAziendaVO.getIdAzienda().longValue());
        if(isAziendIntermediario)
        {
          ValidationError error = new ValidationError(AnagErrors.ERRORE_REG_MANDATO_INTERMEDIARIO);
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(avantiIndietroURL).forward(request, response);
          return;        
        }
      
      }
      catch(SolmrException se) 
      {
        messaggioErrore = se.getMessage();
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>
          <jsp:forward page = "<%=registrazioneDelegaURL%>" />
        <%
      }
    	
    	
    	
    	session.setAttribute("anagAziendaVO",anagAziendaVO);
    	%>
      	<jsp:forward page = "<%=registrazioneDelegaURL%>" />
    	<%
    	return;
  	}
  }
%>