<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>
<%@ page import="it.csi.solmr.dto.anag.ParametroRitornoVO" %>
<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.util.SolmrLogger" %>
<%@ page import="it.csi.solmr.util.Validator" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.SolmrException" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.exception.services.ServiceSystemException" %>
<%@ page import="it.csi.solmr.exception.services.ErrorTypes" %>
<%@ page import="it.csi.solmr.util.ValidationError" %>
<%@ page import="it.csi.solmr.util.ValidationErrors" %>
<%@ page import="java.util.Arrays" %>
<%@ page import="it.csi.solmr.util.DateUtils" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%!
  private static final String VIEW="/view/elencoAziendeRapLegaleView.jsp";
  private static final String dettaglioURL = "/view/anagraficaView.jsp";
%>
<%
  
	SolmrLogger.debug(this,"CONTROL elencoAziendeRapLegaleCtrl.jsp");

  String iridePageName = "elencoAziendeRapLegaleCtrl.jsp";
  String accessoAziendaURL = "/ctrl/accessoAziendaCtrl.jsp";
  %>
  	<%@include file = "/include/autorizzazione.inc" %>
  <%

  ValidationErrors errors = new ValidationErrors();
  ParametroRitornoVO parametroVO = null;
  String newInserimentoAziendaURL = "/ctrl/newInserimentoAziendaCtrl.jsp";
  String elencoRichiesteRicercaURL = "/ctrl/elencoRichiesteRicercaCtrl.jsp";
  try 
  {
   	//Facade
   	AnagFacadeClient client = new AnagFacadeClient();
   	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
   	String messaggi[] = null;
   	Vector vElencoAziende = null;
    //Usato per capire a quali figli/nipoti ecc può accedere!!!
   	Vector<String> elencoCUAATitolare = null;
   	
  	/*********************
   	CARICAMENTO DATI
   	*********************/
   	SolmrLogger.debug(this,"ARRIVA FUNZIONE " + request.getParameter("operazione"));
   	if("dettaglio".equals(request.getParameter("operazione"))) 
    {
      if(request.getParameter("idAzienda") == null) 
      {
      	ValidationError error = new ValidationError("Selezionare un''azienda");
      	errors.add("error", error);
      	request.setAttribute("errors", errors);
      }
      else 
      {
      	session.removeAttribute("anagAziendaVO");
      	AnagAziendaVO anagAziendaVO=null;
      	// Ricerco nuovamente l'azienda per poter avere la situazione aggiornata in relazione
    		// alla delega
      	/*try 
        {
          anagAziendaVO = client.findAziendaAttiva(new Long(request.getParameter("idAzienda")));
          session.setAttribute("anagAziendaVO",anagAziendaVO);
          			
					// Se l'azienda selezionata ha flag_provvisoria = NULL o = N e id_azienda_provenienza
        	// not null
        	if(anagAziendaVO.getIdAziendaProvenienza() != null) 
          {
        		AnagAziendaVO anagAziendaProvenienzaVO = null;
        		try 
            {
        			anagAziendaProvenienzaVO = client.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaProvenienza());
        			request.setAttribute("anagAziendaProvenienzaVO", anagAziendaProvenienzaVO);
        		}
        		catch(SolmrException se) 
            {
        	   	ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_PROVENIENZA);
        	    errors.add("error", error);
        	    request.setAttribute("errors", errors);
        	    request.getRequestDispatcher(VIEW).forward(request, response);
        	    return;
        	  }
        	}
        	// Recupero le eventuali aziende di destinazione dell'azienda in esame
        	AnagAziendaVO[] elencoAziendeDestinazione = null;
        	try 
          {
        		elencoAziendeDestinazione = client.getListAnagAziendaDestinazioneByIdAzienda(anagAziendaVO.getIdAzienda(), true, null);
        		request.setAttribute("elencoAziendeDestinazione", elencoAziendeDestinazione);
        	}
        	catch(SolmrException se) 
          {
        	 	ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_AZIENDA_DESTINAZIONE);
        	  errors.add("error", error);
        	  request.setAttribute("errors", errors);
        	  request.getRequestDispatcher(VIEW).forward(request, response);
        	  return;
        	}
          			
          %>
          	<jsp:forward page = "<%=accessoAziendaURL%>" />
          <%
          return;
        }
        catch(SolmrException se) 
        {
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("error", error);
        	request.setAttribute("errors", errors);
        }*/
        
        //Da qui devo sempre avere accesso a tutto!!!!
        //Sovrascrivo l'azienda
        session.setAttribute("idAziendaElencoSoci",new Long(request.getParameter("idAzienda")));  
        
        request.setAttribute("idAziendaAccesso", request.getParameter("idAzienda"));
        
        %>
            <jsp:forward page = "<%=accessoAziendaURL%>" />
        <%
        return;
      }
    }
  
    // pulisco session
    session.removeAttribute("elencoCUAATitolare");
    session.removeAttribute("anagAziendaVO");
    //ParametroRitornoVO parametroVO = null;
    // CARICAMENTO Aziende per il rappresentante legale
    SolmrLogger.debug(this,"-- CARICAMENTO Aziende per il rappresentante legale");
    try 
    {
    	if(ruoloUtenza.isUtenteNonIscrittoCIIA()) 
      {
    	SolmrLogger.debug(this,"-- isUtenteNonIscrittoCIIA");
    	//AZIENDA AGRICOLA
        AnagAziendaVO aziendaVO = new AnagAziendaVO();
        aziendaVO.setCUAA(ruoloUtenza.getCUAA());
        Vector vectIdAnagAzienda = client.getListIdAziende(aziendaVO,DateUtils.parse(DateUtils.getCurrentDateString(),"dd/MM/yyyy"), false);
        if(vectIdAnagAzienda == null || vectIdAnagAzienda.size() == 0) 
        {
        	SolmrLogger.debug(this,"-- vectIdAnagAzienda == null");
        	messaggi = new String[1];
        	messaggi[0] = AnagErrors.NO_AZIENDE_ATTIVE_FOR_CUAA;
        	parametroVO = new ParametroRitornoVO();
        	parametroVO.setCodeResult(SolmrConstants.NESSUNA_AZIENDA_CENSITA);
        	request.setAttribute("messaggi",messaggi);
        }
        else 
        {
        	SolmrLogger.debug(this,"-- vectIdAnagAzienda != null");
        	SolmrLogger.debug(this,"-- getListAziendeByIdRange");
        	vElencoAziende = client.getListAziendeByIdRange(vectIdAnagAzienda);
        }
      }
      else 
      {
		// legale rappresentante entrato con carta o titolare CF autoregistrato:
		SolmrLogger.debug(this,"-- legale rappresentante entrato con carta o titolare CF autoregistrato:");
		SolmrLogger.debug(this,"-- ruoloUtenza.getCodiceRuolo() ="+ruoloUtenza.getCodiceRuolo());
		SolmrLogger.debug(this,"-- ruoloUtenza.getCodiceFiscale() = "+ruoloUtenza.getCodiceFiscale());
      	//if(ruoloUtenza.isUtenteLegaleRappresentante() || ruoloUtenza.isUtenteTitolareCf())
      	if(ruoloUtenza.getCodiceRuolo() != null && 
      	   (ruoloUtenza.getCodiceRuolo().equalsIgnoreCase("TITOLARE_CF@UTENTI_IRIDE2") ||
      			 ruoloUtenza.getCodiceRuolo().equalsIgnoreCase("LEGALE_RAPPRESENTANTE@ALI")
      		)	 
      	   )
        {
        	SolmrLogger.debug(this,"** LEGALE RAPPRESENTANTE");
        	SolmrLogger.debug(this,"** TITOLARE CF");          	
        	// ricerco su AAEP qualunque soggetto abbia un ruolo attivo sull'azienda
        	boolean controllaPresenzaSuAAEP = false;
        	Boolean bloccaAssenzaAAEP = new Boolean(false);
        	boolean controllaLegameRLsuAnagrafe = false;
        	boolean controllaPresenzaValidazione = false;
        	
        	SolmrLogger.debug(this,"-- serviceGetAziendeInfocAnagrafe");
        	parametroVO = client.serviceGetAziendeInfocAnagrafe(ruoloUtenza.getCodiceFiscale(), controllaPresenzaSuAAEP, bloccaAssenzaAAEP, controllaLegameRLsuAnagrafe, controllaPresenzaValidazione);        		
        }
        messaggi = parametroVO.getMessaggio();
        Long ids[] = parametroVO.getIdAzienda();
        if(ids != null) 
        {
        	Vector idVect = new Vector(Arrays.asList(ids));
        	SolmrLogger.debug(this,"-- serviceGetListAziendeByIdRange");
        	AnagAziendaVO anagAziendaList[] = client.serviceGetListAziendeByIdRange(idVect);
        	if(anagAziendaList != null) 
          {
          	vElencoAziende = new Vector(Arrays.asList(anagAziendaList));
          	elencoCUAATitolare = new Vector<String>();
          	for(int i=0;i<anagAziendaList.length;i++) 
            {
            	elencoCUAATitolare.add(anagAziendaList[i].getCUAA());
            }
          }
        }
      }
    }
    catch (ServiceSystemException ex) 
    {
    	SolmrLogger.error(this,"*** SYSTEM EXCEPTION ERROR TYPE " +ex.getErrorType());
    	SolmrLogger.debug(this,"*** STR_SERVICE_AAEP_EXCEPTION " +ErrorTypes.STR_SERVICE_AAEP_EXCEPTION);
    	SolmrLogger.error(this,"*** SERVICE_AAEP_EXCEPTION " +ErrorTypes.SERVICE_AAEP_EXCEPTION);
     	// AAEP NON RAGGIUNGIBILE imposto un messaggio di errore
    	request.setAttribute("errorAAEP",(String)AnagErrors.get("ERR_AAEP_TO_CONNECT"));
    	request.getRequestDispatcher(VIEW).forward(request, response);
    	return;
    }
    // imposto dati in session
    if(elencoCUAATitolare != null) 
    {
    	session.setAttribute("elencoCUAATitolare",elencoCUAATitolare);
    }
    request.setAttribute("messaggi",messaggi);
    request.setAttribute("vElencoAziende",vElencoAziende);
  }
  catch (Exception ex) 
  {
   	if(((String)AnagErrors.get("NESSUNA_AZIENDA_TROVATA")).equalsIgnoreCase(ex.getMessage())) 
    {
   		SolmrLogger.debug(this,"-- NESSUNA_AZIENDA_TROVATA");
    	String messaggi[] = new String[1];
    	messaggi[0] = AnagErrors.NO_AZIENDE_ATTIVE_FOR_CUAA;
    	if(parametroVO == null)
    	{
    	  parametroVO = new ParametroRitornoVO();
    	}
    	parametroVO.setCodeResult(SolmrConstants.NESSUNA_AZIENDA_CENSITA);
    	request.setAttribute("messaggi",messaggi);
    }
    else 
    {
    	SolmrLogger.debug(this,"-- Exception ="+ex.getMessage());
    	ValidationError error = new ValidationError(ex.getMessage());
    	errors.add("error",error);
    	request.setAttribute("errors", errors);
    }
  }
  
  
  //non esistono azienda censite vado nell'inserimento nuova iscrizione
  SolmrLogger.debug(this,"-- Non esistono azienda censite vado nell'inserimento nuova iscrizione");
  if(Validator.isNotEmpty(parametroVO)
    && Validator.isNotEmpty(parametroVO.getCodeResult())
    && (parametroVO.getCodeResult().compareTo(SolmrConstants.NESSUNA_AZIENDA_CENSITA) == 0))
  {
    WebUtils.removeUselessFilter(session, null);
    %>
      <jsp:forward page="<%=newInserimentoAziendaURL%>"/>
    <%
  }
  
  
	%>
	<jsp:forward page="<%=VIEW%>"/>

