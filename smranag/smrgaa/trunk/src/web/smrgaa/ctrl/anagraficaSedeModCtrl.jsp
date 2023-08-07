<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "anagraficaSedeModCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

 	String validateUrl = "/view/anagraficaSedeModView.jsp";
 	String sedeUrl = "/view/sedeView.jsp";
 	String richiestaModifica = "ok";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	String operazione = request.getParameter("operazione");
 	ValidationErrors errors = new ValidationErrors();
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	if(anagAziendaVO != null) 
  {
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
        request.getRequestDispatcher(validateUrl).forward(request, response);
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
      request.getRequestDispatcher(validateUrl).forward(request, response);
      return;
    }
  }

 	if(request.getParameter("conferma") != null) 
  {
  	String idAnagAzienda = request.getParameter("idAnagAzienda");
  	
  	Long idAnagrafica = Long.decode(idAnagAzienda);
   	anagAziendaVO = anagFacadeClient.getAziendaById(idAnagrafica);
  	
  	if(anagAziendaVO.getDataFineVal() != null || anagAziendaVO.getDataCessazione() != null) 
    {
     	ValidationError error = new ValidationError(""+AnagErrors.get("NO_MOD_AZIENDA_CESSATA"));
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(validateUrl).forward(request, response);
      return;
    }     	

   	String indirizzo = request.getParameter("sedelegIndirizzo");
   	String provincia = request.getParameter("sedelegProv");
   	String comune = request.getParameter("descComune");
   	String cap = request.getParameter("sedelegCAP");
   	String statoEstero = request.getParameter("sedelegEstero");
   	String cittaEstero = request.getParameter("sedelegCittaEstero");

   	anagAziendaVO.setSedelegIndirizzo(indirizzo);
   	anagAziendaVO.setSedelegProv(provincia);
   	anagAziendaVO.setDescComune(comune);
   	anagAziendaVO.setSedelegCAP(cap);
   	anagAziendaVO.setSedelegEstero(statoEstero);
   	anagAziendaVO.setStatoEstero(statoEstero);
   	anagAziendaVO.setSedelegCittaEstero(cittaEstero);

   	errors = anagAziendaVO.validateSedeLegale();

   	String istatComune = null;
   	String istatStatoEstero = null;
   	if(comune != null && !comune.equals("")) 
    {
    	try 
      {
      	istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(comune,provincia);
      	anagAziendaVO.setSedelegComune(istatComune);
      	anagAziendaVO.setSedelegEstero("");
     	}
     	catch(SolmrException se) 
      {
      	ValidationError error = new ValidationError(""+AnagErrors.get("ERR_COMUNE_SEDE_INESISTENTE"));
      	errors.add("sedelegComune",error);
     	}
   	}
    else 
    {
      if(statoEstero != null && !statoEstero.equals("")) 
      {
      	try 
        {
        	istatStatoEstero = anagFacadeClient.ricercaCodiceComune(statoEstero,"");
        	anagAziendaVO.setSedelegComune(istatStatoEstero);
        }
        catch(SolmrException se) 
        {
        	ValidationError error = new ValidationError(""+AnagErrors.get("ERR_STATO_ESTERO_SEDE_INESISTENTE"));
        	errors.add("sedelegEstero",error);
        }
      }
    }
   	if(errors != null && errors.size() != 0) 
    {
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(validateUrl).forward(request, response);
    	return;
   	}
   	session.removeAttribute("anagAziendaVO");
   	session.setAttribute("anagAziendaVO",anagAziendaVO);
     	
   	AnagAziendaVO anagAziendaConfrontoVO = null;
   	try 
    {
   		anagAziendaConfrontoVO = anagFacadeClient.getAziendaById(idAnagrafica);
   	}
   	catch(SolmrException se) 
    {
   		ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_ANAGRAFICA_AZIENDA);
   		errors.add("error", error);
   		request.setAttribute("errors", errors);
   		request.getRequestDispatcher(validateUrl).forward(request, response);
   		return;
 		}
      
    boolean datiUguali=anagAziendaConfrontoVO.equalsSedeLegale(anagAziendaVO);
     	
   	// Se la data di inizio validità dell'azienda è uguale alla data odierna
   	if(DateUtils.isToday(anagAziendaVO.getDataInizioVal()) || datiUguali) 
    {
     		// Se è stato effettivamente modificato qualcosa procedo con update
   		if(!anagAziendaConfrontoVO.equalsSedeLegale(anagAziendaVO)) 
      {
     		RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
       	anagAziendaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
       	AnagAziendaVO newVO = null;
       	// Effettuo la modifica della sede legale
       	try 
        {
        	anagFacadeClient.updateSedeLegale(anagAziendaVO,ruoloUtenza.getIdUtente());
        	newVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
       	}
       	catch(SolmrException se) 
        {
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("error", error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(validateUrl).forward(request, response);
        	return;
       	}
       	session.removeAttribute("anagAziendaVO");
       	session.setAttribute("anagAziendaVO",newVO);
   		}
   		%>
   			<jsp:forward page = "<%= sedeUrl %>" />
 			<%
   	}
   	else 
    {
   		// Se è stato effettivamente modificato qualcosa mando l'utente alla pop-up di storicizzazione
   		if(!anagAziendaConfrontoVO.equalsSedeLegale(anagAziendaVO)) 
      {
     		session.setAttribute("richiestaModifica",richiestaModifica);
     		%>
      		<jsp:forward page = "<%= validateUrl %>" />
     		<%
   		}
   		// Altrimenti torno alla visualizzazione dei dati della sede legale
   		else 
      {
   			%>
   				<jsp:forward page = "<%= sedeUrl %>" />
 				<%
   		}
   	}
 	}
 	else if(request.getParameter("annulla")!=null)
  {
  	%>
     	<jsp:forward page = "<%= sedeUrl %>" />
   	<%
 	}
 	// Arrivo dalla pop-up e ho scelto di modificare
 	else if(operazione != null) 
  {
  	// Arrivo dal pop-up e ho scelto di storicizzare
   	if(operazione.equalsIgnoreCase("S")) 
    {
    	anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    	errors = new ValidationErrors();
    	// Recupero il motivo della modifica da salvare solo in caso di storicizzazione
    	String motivoModifica = request.getParameter("motivoModifica");
    	anagAziendaVO.setMotivoModifica(motivoModifica);
    	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    	anagAziendaVO.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
    	session.removeAttribute("anagAziendaVO");
    	session.setAttribute("anagAziendaVO",anagAziendaVO);
    	// Storicizzo l'azienda
    	AnagAziendaVO newVO = null;
    	try 
      {
      	anagFacadeClient.storicizzaSedeLegale(anagAziendaVO);
      	newVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
     	}
     	catch(SolmrException se) 
      {
      	ValidationError error = new ValidationError(se.getMessage());
      	errors.add("error", error);
      	session.removeAttribute("richiestaModifica");
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(validateUrl).forward(request, response);
      	return;
     	}
     	session.removeAttribute("anagAziendaVO");
     	session.setAttribute("anagAziendaVO",newVO);
     	%>
      	<jsp:forward page = "<%= sedeUrl %>" />
     	<%
   	}
 	}
 	// Ho cliccato il link modifica dalla pagina di visualizzazione dei dati della sede legale
 	// dell'anagrafica
 	else 
  {
  	%>
     	<jsp:forward page = "<%= validateUrl %>" />
   	<%
 	}

%>
