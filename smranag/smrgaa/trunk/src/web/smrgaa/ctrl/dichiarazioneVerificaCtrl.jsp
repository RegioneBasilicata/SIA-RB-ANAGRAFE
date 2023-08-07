<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="java.util.*" %>



<%!
  public static String PREVISIONE_PIANO_URL="/view/dichiarazioneVerificaView.jsp";
  public static String ATTENDERE_URL="/view/dichiarazioneVerificaAttendereView.jsp";
  public static String ACTION = "../layout/dichiarazioneVerifica.htm";
  public static String URL_ATTENDERE_PREGO = "/view/attenderePregoView.jsp";
  public static String URL_DICHIARAZIONE_CONSISTENZA = "../ctrl/dichiarazioneConsistenzaCtrl.jsp";    
%>


<%

	String iridePageName = "dichiarazioneVerificaCtrl.jsp";
  %>
  	<%@include file = "/include/autorizzazione.inc" %>
  <%
  
    //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
	
	String PAGE_OK ="";
	String PAGE_ANOMALIA ="";
	String NEXT_PAGE_ERRORE_PLSQL="";	
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI)){
		PAGE_OK = "../layout/";
		PAGE_ANOMALIA = "../layout/";
		NEXT_PAGE_ERRORE_PLSQL = "../layout/";
	}	
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY)){
		PAGE_OK = "/layout/";
		PAGE_ANOMALIA = "/layout/";
		NEXT_PAGE_ERRORE_PLSQL = "/layout/";
	}	
	PAGE_OK += "dichiarazioneVerifica_ok.htm";
	PAGE_ANOMALIA += "dichiarazioneVerifica_ko.htm";
	NEXT_PAGE_ERRORE_PLSQL += "errore.htm";
  

  String operazione = request.getParameter("operazione");

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

	if(Validator.isNotEmpty("operazione") && "attenderePrego".equalsIgnoreCase(operazione)) 
  {
  	request.setAttribute("action", ACTION);
  	operazione = null;
  	request.setAttribute("operazione", operazione);
  	%>
    	<jsp:forward page= "<%= URL_ATTENDERE_PREGO %>" />
  	<%
	}
	else 
  {
  	if(!Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
    {
  		// Non posso proseguire con la dichiarazione
  		String messaggio = (String)AnagErrors.get("ERR_CUAA_NO_VALORIZZATO");
  		request.setAttribute("messaggioErrore",messaggio);
  		request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
  		%>
    		<jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
  		<%
  		return;
  	}
  	// Recupero i parametri AAEP, TRIB per verificare se
  	// devo o meno effettuare i controlli di validità con
  	// i richiami ai servizi di AAEP e SIAN
  	String parametroAAEP = null;
  	String parametroSIAN = null;
    String parametroCCAV = null;
    String parametroCCSI = null;
  	try 
    {
  		parametroAAEP = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_AAEP"));
  		parametroSIAN = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_TRIB"));
      parametroCCAV = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_CCAV);
      parametroCCSI = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_CCSI);
  	}
  	catch(SolmrException se) 
    {
  		// Non posso proseguire con la dichiarazione
  		String messaggio = se.getMessage();
  		request.setAttribute("messaggioErrore",messaggio);
  		request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
  		%>
    		<jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
  		<%
  		return;
  	}
    	
  	// Recupero il parametro ALLD
    String parametroALLD = null;
    try 
    {
  		parametroALLD = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_ALLD);
    }
    catch(SolmrException se) 
    {
  		String messaggio = AnagErrors.ERRORE_KO_PARAMETRO_ALLD;
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
      %>
        <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
      <%
      return;
    }

  	// Se il parametro del SIAN è valorizzato a "S" significa
  	// che devo effettuare i controlli e gli aggiornamenti
  	// relativi al SIAN
  	if(parametroSIAN.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
    	// Aggiorno la tabella DB_AZIENDA_TRIBUTARIA in relazione
    	// ai valori restituitimi dal SIAN
    	try 
      {
      	String elencoCuaa[] = new String[1];
      	elencoCuaa[0] = anagAziendaVO.getCUAA();
      	anagFacadeClient.sianAggiornaDatiTributaria(elencoCuaa, ProfileUtils.getSianUtente(ruoloUtenza));
    	}
    	catch(SolmrException se) 
      {
      	// Non posso proseguire con la dichiarazione
      	String messaggio = se.getMessage();
      	request.setAttribute("messaggioErrore",messaggio);
      	request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
      	%>
      		<jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
      	<%
      	return;
    	}

    	// Nel caso di persone giuridiche (cuaa dell'azienda lungo 11 chr - partita iva)
      // richiamare il servizio SIAN anagraficaSintetica passando come codice il codice fiscale
      // del titolare/rappr.legale attivo legato all'azienda. Tale servizio è da richiamare
      // solo nel caso in cui su DB_PARAMETRO il parametro TRIB='S'.
      if(anagAziendaVO.getCUAA() != null && anagAziendaVO.getCUAA().length()==11) 
      {
      	PersonaFisicaVO personaVO = anagFacadeClient.getTitolareORappresentanteLegaleAzienda(
          anagAziendaVO.getIdAzienda(), DateUtils.parseDate(anagAziendaVO.getDataSituazioneAlStr()));
      	// Se non viene reperito il rappresentante legale dell'azienda oppure non ha
      	// il codice fiscale valorizzato impedisco la prosecuzione della nuova validazione
      	if(personaVO == null || !Validator.isNotEmpty(personaVO.getCodiceFiscale())) 
        {
        	String messaggio = AnagErrors.ERRORE_KO_VERIFICA_FOR_COD_FISC_RAPP_LEG;
        	request.setAttribute("messaggioErrore",messaggio);
        	request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
        	%>
          	<jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
          <%
          return;
        }
        // Aggiorno la tabella DB_AZIENDA_TRIBUTARIA in relazione
        // ai valori restituitimi dal SIAN
        try 
        {
        	String elencoCuaa[] = new String[1];
        	elencoCuaa[0] = personaVO.getCodiceFiscale();
        	anagFacadeClient.sianAggiornaDatiTributaria(elencoCuaa, ProfileUtils.getSianUtente(ruoloUtenza));
        }
        catch(SolmrException se) 
        {
        	// Non posso proseguire con la dichiarazione
        	String messaggio = se.getMessage();
        	request.setAttribute("messaggioErrore",messaggio);
        	request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
        	%>
        		<jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
        	<%
        	return;
        }
      }
  	}	

  	// Se il parametro di AAEP è valorizzato a "S" significa
  	// che devo effettuare i controlli e gli aggiornamenti
  	// relativi ad AAEP
  	if(parametroAAEP.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
    	try 
      {
      	anagFacadeClient.aggiornaDatiAAEP(anagAziendaVO.getCUAA());
    	}
    	catch(SolmrException se) {}
  	}
  	
  	// Se parametro ALLD = S
    if(parametroALLD.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
  		// Richiamo il web-service per l'aggiornamento della BDN allevamenti
	  	try 
      {
			  gaaFacadeClient.serviceWsBridgeAggiornaDatiBDN(anagAziendaVO.getCUAA());
	  	}
	  	catch(SolmrException se) 
      {
			// Intercetto l'eccezione ma non blocco l'operazione
	  		SolmrLogger.error(this, "--- Eccezione con la chiamata Sian serviceWsBridgeAggiornaDatiBDN :"+se.getMessage());
	  	}
    }
      
    // Richiamare il servizio erogato dalla CCIAA per l'elenco albo vigneti:
    // Eseguire il c.u. "CU-GAA14-13 Scarico aziendale albo vigneti fonte CCIAA" solamente se il parametro CCAV='S'.
    // Indipendentemente dall'esito di quanto sopra procedere comunque.
    if(parametroCCAV.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      // Richiamo il web-service per l'aggiornamento dell'albo vigneti
      try 
      {
        anagFacadeClient.sianAggiornaDatiAlboVigneti(anagAziendaVO);
      }
      catch(SolmrException se) {
      // Intercetto l'eccezione ma non blocco l'operazione
      }
    }
      
    // Richiamare il servizio erogato di sigmater per lo scarico delle titolarità
    // Eseguire il c.u. "CU-GAA144-14 Scarico titolarità SIGMATER v1" 
    // solamente se il parametro CCSI='S'.
    // Indipendentemente dall'esito di quanto sopra procedere comunque.
    if(parametroCCSI.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
      // Richiamo il web-service per l'aggiornamento di sigmater
      try 
      {
        gaaFacadeClient.scaricoTitolarita(SolmrConstants.ID_REG_PIEMONTE,
          anagAziendaVO.getCUAA(), anagAziendaVO.getIdAzienda().longValue(),
          null, null);
      }
      catch(SolmrException se) 
      {        
        // Non posso proseguire con la dichiarazione
        String messaggio = se.getMessage();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
        %>
          <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
        <%        
      }
    }
      
    	
  	try 
    {
  		if(anagFacadeClient.previsioneAnnoSucessivo(anagAziendaVO.getIdAzienda())) 
      {
    		// E' stata fatta una previsione del piano colturale
    		%>
      		<jsp:forward page="<%=PREVISIONE_PIANO_URL%>" />
    		<%
  		}
  		else 
      {
    		// Non è stata fatta una previsione del piano colturale
    		Integer anno;
    		anno = DateUtils.getCurrentYear();
    		String ris = null;
    		try 
        {
      		ris = anagFacadeClient.controlliVerificaPLSQL(
      		  anagAziendaVO.getIdAzienda(),anno,null, ruoloUtenza.getIdUtente());
    		}
    		catch(SolmrException se) 
        {
    			// Non posso proseguire con la dichiarazione
    			String messaggio = se.getMessage();
    			request.setAttribute("messaggioErrore",messaggio);
    			request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
    			%>
      			<jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
    			<%
    		}
    		request.setAttribute("anno", anno);
    		if(ris.equals("N")) 
        {
    			// Posso permettere il salvataggio della dichiarazione
    			%>
      			<jsp:forward page="<%=PAGE_OK%>" />
    			<%
    		}
    		else if(ris.equals("S")) 
        {
      		// Visualizzo gli errori e non permetto il proseguimento
      		%>
        		<jsp:forward page="<%=PAGE_ANOMALIA%>" />
      		<%
    		}
    		else 
        {
      		// Visualizzo gli errori e non permetto il proseguimento
      		%>
        		<jsp:forward page="<%=PAGE_ANOMALIA%>" />
      		<%
    		}
    	}
  	}
  	catch(SolmrException s) 
    {
  		ValidationErrors ve = new ValidationErrors();
  		ve.add("error",new ValidationError(s.getMessage()));
  		request.setAttribute("errors",ve);
  		s.printStackTrace();
  		request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
  		%>
    		<jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
  		<%
  	}	
  	catch(Exception e) 
    {
  		it.csi.solmr.util.ValidationErrors ve = new ValidationErrors();
  		ve.add("error",new it.csi.solmr.util.ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
  		request.setAttribute("errors",ve);
  		e.printStackTrace();
  		request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
  		%>
    		<jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
  		<%
  	}
	}
%>
