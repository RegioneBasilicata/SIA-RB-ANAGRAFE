<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>


<%

	
	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, noRemove);

	String iridePageName = "";

	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

	String anagraficaUrl = "/view/anagraficaView.jsp";
	String anagraficaIndividualePassaggioUrl = "/view/anagraficaIndividualePassaggioView.jsp";
	String ricercaNotificaUrl = "/view/ricercaNotificaView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione dettaglio azienda. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	ValidationErrors errors = new ValidationErrors();


	// Recupero il tipo di operazione selezionata dall'utente
  String operazione = request.getParameter("operazione");

	// L'utente ha selezionato l'operazione "Cambio titolare".
	if(operazione != null && operazione.equalsIgnoreCase("cambioTitolare")) 
  {
  	session.removeAttribute("anagPassaggioVO");
  	session.removeAttribute("ruolo");
  	session.removeAttribute("personaTitolareVO");
  	session.removeAttribute("personaTitolareOld");
  	session.removeAttribute("indietro");
  	session.setAttribute("primo","primo");

  	PersonaFisicaVO personaFisicaVO = null;
  	try 
    {
    	personaFisicaVO = anagFacadeClient.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
  	}
  	catch(SolmrException se) 
    {
    	ValidationError error = new ValidationError(se.getMessage());
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	iridePageName = "anagraficaCtrl.jsp";
    	%>
     		<%@include file = "/include/autorizzazione.inc" %>
    	<%
    	request.getRequestDispatcher(anagraficaUrl).forward(request, response);
    	return;
  	}
  	session.setAttribute("personaTitolareOld",personaFisicaVO);
   	// Dato che questo controller lavora  con diverse view (appartenenti
   	// a macroCU diversi sono necessarie più include)
  	iridePageName = "anagraficaIndividualePassaggioCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%
  	%>
     	<jsp:forward page="<%= anagraficaIndividualePassaggioUrl %>"/>
  	<%
	}
  // L'utente ha selezionato l'operazione di cessazione
	else if(request.getParameter("cessazione") != null&&request.getParameter("cessazione").equals("true")) 
  {
  	session.removeAttribute("common");
  	String url = "";
  	ValidationException valEx = null;
  	if(anagAziendaVO!=null && anagAziendaVO.getDataCessazione() != null && !anagAziendaVO.getDataCessazione().equals("")) 
    {
    		url = "../view/anagraficaView.jsp";
    		valEx = new ValidationException("Azienda Cessata 1: ", url);
    		valEx.addMessage(""+AnagErrors.get("AZIENDA_CESSATA"), "exception");
    		throw valEx;
  	}
    else 
    {
  		// Richiamo il servizio di Infocamere solo se l'azienda sulla quale sto lavorando ha il CUAA
  		// valorizzato
  		boolean aziendaAAEPTrovataAndAttiva = false;
  		if(Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
      {  			  
  			// Recupero i parametri AAEP per verificare se
  			// devo o meno effettuare i controlli di validità con
   		  // i richiami ai servizi di AAEP
  			String parametroAAEP = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_AAEP"));
  			// Se il parametro di AAEP è valorizzato a "S" significa
  			// che devo effettuare i controlli e gli aggiornamenti
  			// relativi ad AAEP
  			SolmrLogger.debug(this, "-- parametroAAEP ="+parametroAAEP);
  			if(parametroAAEP.equalsIgnoreCase(SolmrConstants.FLAG_S)){
      		  // Inizio controlli su AAEP    		
      		  try {
      			it.csi.solmr.ws.infoc.Azienda aziendaAAEP = null;
      			SolmrLogger.debug(this, "-- chiamata a cercaPerCodiceFiscale");
      			aziendaAAEP = anagFacadeClient.cercaPerCodiceFiscale(anagAziendaVO.getCUAA());      			
      			if(aziendaAAEP.getDataCessazione().getValue() == null || aziendaAAEP.getDataCessazione().getValue().trim().equals("")){
      			  aziendaAAEPTrovataAndAttiva = true;
      			}
      		  }    			
              catch(SolmrException sex) {
            	SolmrLogger.error(this, "-- SolmrException con la chiamata a cercaPerCodiceFiscale ="+sex.getMessage());
                sex.printStackTrace();
                if(((String)AnagErrors.get("ERR_AAEP_GENERICO")).equals(sex.getMessage())) {
      				ValidationError error = new ValidationError((String)AnagErrors.get("ERR_AAEP_GENERICO"));
      				url = "/view/anagraficaView.jsp";
      				errors.add("error", error);
      				request.setAttribute("errors", errors);
      				iridePageName = "anagraficaCtrl.jsp";
      				%>
         				<%@include file = "/include/autorizzazione.inc" %>
      				<%
      				request.getRequestDispatcher(url).forward(request, response);
             	    return;
                }
	            if(((String)AnagErrors.get("ERR_AAEP_TO_CONNECT")).equals(sex.getMessage())) 
	            {
	      				ValidationError error = new ValidationError((String)AnagErrors.get("ERR_AAEP_TO_CONNECT"));
	      				url = "/view/anagraficaView.jsp";
	      				errors.add("error", error);
	      				request.setAttribute("errors", errors);
	      				iridePageName = "anagraficaCtrl.jsp";
	      				%>
	         				<%@include file = "/include/autorizzazione.inc" %>
	      				<%
	      				request.getRequestDispatcher(url).forward(request, response);
	      				return;
	            }
	            if(!((String)AnagErrors.get("ERR_AAEP_NO_ANAGRAFICA")).equals(sex.getMessage())) 
	            {
	      				ValidationError error = new ValidationError(sex.getMessage());
	      				url = "/view/anagraficaView.jsp";
	      				errors.add("error", error);
	      				request.setAttribute("errors", errors);
	      				iridePageName = "anagraficaCtrl.jsp";
	      				%>
	         				<%@include file = "/include/autorizzazione.inc" %>
	      				<%
	      				request.getRequestDispatcher(url).forward(request, response);
	      				return;
	            }
          	}
        	// Fine controlli su AAEP
          }
      }
      if(aziendaAAEPTrovataAndAttiva) 
      {
    		//Ho trovato l'azienda su AAEP ed è attiva, quindi devo comunicare
    		//all'utente se vuole proseguire
    		boolean common[] = new boolean[2];
    		common[0] = false;
    		common[1] = false;
    		session.setAttribute("common",common);
    		url = "/view/confermaCessaAziendaAAEPView.jsp";
    		iridePageName = "anagraficaCtrl.jsp";
  			%>
  				<%@include file = "/include/autorizzazione.inc" %>
  			<%
   	 		request.getRequestDispatcher(url).forward(request, response);
    		return;
      }
      else 
      {
    		boolean common[] = new boolean[2];
    		common[0] = true;
    		common[1] = false;
    		session.setAttribute("common",common);
    		url = "/layout/controlliCessaAzienda.htm";
  		}
    }
  	iridePageName = "anagraficaCtrl.jsp";
		%>
			<%@include file = "/include/autorizzazione.inc" %>
		<%
  	%>
     	<jsp:forward page="<%=url%>"/>
  	<%
  }
  else 
  {
    if(session.getAttribute("MDM")!=null)
    {
      String url = "";
      ValidationException valEx = null;
      url = "/view/anagraficaView.jsp";
      valEx = new ValidationException("Azienda Cessata 2: ", url);
      valEx.addMessage(""+AnagErrors.get("AZIENDA_CESSATA"), "exception");
      throw valEx;
    }

  	// Pulisco la sessione dagli oggetti relativi alle area coinvolte nel medesimo macro d'uso
  	// (Visualizza Dati Azienda) che non sono neccesari alle funzionalità presenti sotto la voce
  	// di menù anagrafica
  	session.removeAttribute("v_soggetti");
  	session.removeAttribute("v_ute");
  	session.removeAttribute("uteVO");

  	String azienda = request.getParameter("idAnagAzienda");
    
  	// Se il valore del parametro è nullo allora vuol dire che sto arrivando
  	// dal dettaglio dei terreni e quindi lo recupero in un altro modo
  	if(!Validator.isNotEmpty(azienda)) 
    {
    	azienda = request.getParameter("idAnagraficaAzienda");
  	}
  	// Se il parametro azienda continua ad essere nullo significa che arrivo
  	// dall'elenco delle notifiche e quindi lo recupero in un altro modo
  	if(!Validator.isNotEmpty(azienda)) 
    {
  		String valore = request.getParameter("primaryKey");
  		if(Validator.isNotEmpty(valore)) 
      {
    		int valoreInizio = valore.indexOf("/")+1;
    		azienda = valore.substring(valoreInizio);
  		}
  	}
  	// Se è ancora null vuol dire che arrivo dal SIAN e quindi...
  	Long idAzienda = null;
  	if(Validator.isNotEmpty(request.getParameter("idAzienda"))) 
    {
    	idAzienda = Long.decode(request.getParameter("idAzienda"));
  	}
  	if(azienda != null && !azienda.equals("")) 
    {
      Long idAnagAzienda = Long.decode(azienda);
    	try 
      {
        anagAziendaVO = anagFacadeClient.getAziendaById(idAnagAzienda);
        Vector<AnagAziendaVO> associazioniCollegateVector = anagFacadeClient.getAssociazioniCollegateByIdAzienda(anagAziendaVO.getIdAzienda(), anagAziendaVO.getDataFineVal());
        request.setAttribute("associazioniCollegateVector",associazioniCollegateVector);
   	 	}
    	catch(SolmrException se) 
      {
    		ValidationError error = new ValidationError(se.getMessage());
    		errors.add("error", error);
    		request.setAttribute("errors", errors);
    		iridePageName = "anagraficaCtrl.jsp";
  			%>
  				<%@include file = "/include/autorizzazione.inc" %>
  			<%
    		request.getRequestDispatcher(anagraficaUrl).forward(request, response);
    		return;
  		}
  	}
    else if(Validator.isNotEmpty(idAzienda)) 
    {
  		try 
      {
    		anagAziendaVO = anagFacadeClient.findAziendaAttiva(idAzienda);
  		}
  		catch(SolmrException se) 
      {
    		ValidationError error = new ValidationError(se.getMessage());
    		errors.add("error", error);
    		request.setAttribute("errors", errors);
    		iridePageName = "anagraficaCtrl.jsp";
  			%>
  				<%@include file = "/include/autorizzazione.inc" %>
  			<%
    		request.getRequestDispatcher(anagraficaUrl).forward(request, response);
    		return;
  		}
  	}
    Vector<AnagAziendaVO> associazioniCollegateVector = anagFacadeClient.getAssociazioniCollegateByIdAzienda(anagAziendaVO.getIdAzienda(), anagAziendaVO.getDataFineVal());
    request.setAttribute("associazioniCollegateVector",associazioniCollegateVector);

  	if(Validator.isNotEmpty(azienda) || Validator.isNotEmpty(idAzienda)) 
    {
  		// Se l'azienda selezionata ha id_azienda_provenienza
  		// not null
  		if(anagAziendaVO.getIdAziendaProvenienza() != null
                        || anagAziendaVO.getIdAziendaSubentro()!=null)
      {
  			AnagAziendaVO anagAziendaProvenienzaVO = null;
  			try 
        {
          if (anagAziendaVO.getIdAziendaProvenienza() != null)
            anagAziendaProvenienzaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaProvenienza());
          else
            anagAziendaProvenienzaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaSubentro());
            
          request.setAttribute("anagAziendaProvenienzaVO", anagAziendaProvenienzaVO);
  			}
  			catch(SolmrException se) 
        {
        	ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_PROVENIENZA);
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(anagraficaUrl).forward(request, response);
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
        request.getRequestDispatcher(anagraficaUrl).forward(request, response);
        return;
      }
      
  	  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
      Vector vAziendaAtecoSec = null;
      try
      {
        vAziendaAtecoSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAzienda(
          anagAziendaVO.getIdAzienda().longValue());
      }
      catch(SolmrException se) 
      {      
        SolmrLogger.info(this, " - anaraficaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      session.setAttribute("vAziendaAtecoSec", vAziendaAtecoSec);
      session.setAttribute("anagAziendaVO", anagAziendaVO);
  		if(ruoloUtenza!=null) 
      {
    		boolean delegaDiretta = false;
    		boolean padre = false;
    		if(ruoloUtenza.isUtenteIntermediario()) 
        {
      	  delegaDiretta = anagFacadeClient.isIntermediarioConDelegaDiretta(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario(),anagAziendaVO.getIdAzienda());
      	  padre = anagFacadeClient.isIntermediarioPadre(utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getIdIntermediario(),anagAziendaVO.getIdAzienda());
    		}
    		ruoloUtenza.setIsIntermediarioConDelega(delegaDiretta);
    		ruoloUtenza.setIsIntermediarioPadre(padre);
   	 		session.setAttribute("ruoloUtenza",ruoloUtenza);
  		}
  		
  		
  		
  		BigDecimal parametroNotifiRmax = null;
		  try 
		  {
		    parametroNotifiRmax = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_NOTIFI_RMAX);
		  }
		  catch(SolmrException se) 
		  {
		    SolmrLogger.info(this, " - anagraficaCtrl.jsp - FINE PAGINA");
		    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_NOTIFI_RMAX+".\n"+se.toString();   
		    request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
		  }
  
  		// Se il profilo è intermediario controllo che l'utente abbia la
  		// delega per lavorarci sopra
  		if(utenteAbilitazioni.getRuolo().isUtenteIntermediario()) 
      {
    		try 
    		{
      			anagFacadeClient.intermediarioConDelega(utenteAbilitazioni, anagAziendaVO.getIdAzienda());
    		}
    		catch(SolmrException se) //Senza delega 
        {
     	 		ValidationError error = new ValidationError(se.getMessage());
    			errors.add("error", error);

    			if(Validator.isNotEmpty(session.getAttribute("notificaRicercaVO"))) 
          {
      			NotificaVO notificaRicercaVO = (NotificaVO)session.getAttribute("notificaRicercaVO");
      			// Effettuo la ricerca delle notifiche in relazione ai filtri di ricerca
      			ElencoNotificheVO elencoNotificheVO = null;
      			try 
            {
       	 			elencoNotificheVO = anagFacadeClient.ricercaNotificheByParametri(notificaRicercaVO, utenteAbilitazioni, ruoloUtenza, new Boolean(false), parametroNotifiRmax.intValue());
      			  if(Validator.isNotEmpty(elencoNotificheVO.getMessaggioErrore()))
      			  {
      			    String messaggio = elencoNotificheVO.getMessaggioErrore();
                request.setAttribute("messaggio", messaggio);
      			  }
      			}
      			catch(SolmrException sex) 
            {}
      			// Metto il vettore in request
      			request.setAttribute("elencoNotifiche", elencoNotificheVO.getvElencoNotifiche());

       			// Dato che questo controller lavora  con diverse view (appartenenti
       			// a macroCU diversi sono necessarie più include)
      			iridePageName = "ricercaDettaglioNotificaCtrl.jsp";
      			%>
      				<%@include file = "/include/autorizzazione.inc" %>
      			<%

      			// Vado alla pagina di ricerca/elenco notifiche
      			%>
        			<jsp:forward page= "<%= ricercaNotificaUrl %>" />
      			<%
    			}
    			else 
          {
      			int valorePartenza = request.getHeader("referer").lastIndexOf("/");
      			String urlErrore = "/layout"+request.getHeader("referer").substring(valorePartenza);
      			request.setAttribute("errors", errors);
      			iridePageName = "anagraficaCtrl.jsp";
    				%>
    					<%@include file = "/include/autorizzazione.inc" %>
    				<%
      			request.getRequestDispatcher(urlErrore).forward(request, response);
     	 			return;
    			}
    		}
  		}
    	
      iridePageName = "anagraficaCtrl.jsp";
  		%>
  			<%@include file = "/include/autorizzazione.inc" %>
   			<jsp:forward page="<%= anagraficaUrl %>"/>
  		<%
    }
	  iridePageName = "anagraficaCtrl.jsp";
  	if(anagAziendaVO != null) 
    {
  	  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
      Vector vAziendaAtecoSec = null;
      try
      {
        vAziendaAtecoSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAzienda(
          anagAziendaVO.getIdAzienda().longValue());
      }
      catch(SolmrException se) 
      {      
        SolmrLogger.info(this, " - modificaAziendaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      session.setAttribute("vAziendaAtecoSec", vAziendaAtecoSec);
    
    
  	  // Se l'azienda selezionata ha e id_azienda_provenienza
  	  // not null
  		if(anagAziendaVO.getIdAziendaProvenienza() != null
                          || anagAziendaVO.getIdAziendaSubentro()!=null) 
      {
  		  AnagAziendaVO anagAziendaProvenienzaVO = null;
  			try 
        {
          if (anagAziendaVO.getIdAziendaProvenienza() != null)
            anagAziendaProvenienzaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaProvenienza());
          else
            anagAziendaProvenienzaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaSubentro());
            
  				request.setAttribute("anagAziendaProvenienzaVO", anagAziendaProvenienzaVO);
  			}
  			catch(SolmrException se) 
        {
  	      ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_PROVENIENZA);
  	      errors.add("error", error);
  	      request.setAttribute("errors", errors);
  	      request.getRequestDispatcher(anagraficaUrl).forward(request, response);
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
        request.getRequestDispatcher(anagraficaUrl).forward(request, response);
        return;
      }
    }
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	  <jsp:forward page="<%= anagraficaUrl %>"/>
	<%
  }
%>

