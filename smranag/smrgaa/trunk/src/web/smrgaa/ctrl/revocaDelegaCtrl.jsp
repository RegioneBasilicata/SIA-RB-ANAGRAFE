<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.sql.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.anag.services.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "revocaDelegaCtrl.jsp";

	%>
  	<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String revocaDelegaURL = "/view/revocaDelegaView.jsp";
  String confermaRevocaDelegaURL = "/ctrl/confermaRevocaDelegaCtrl.jsp";
	String ricercaURL = "/layout/ricerca.htm";
  String avantiIndietroURL = "/view/elencoView.jsp";

	PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	ValidationError error = null;
	ValidationErrors errors = new ValidationErrors();
  
  //arrivo dall'elenco!!!!!
  String revoca = request.getParameter("noAzienda");
  if(Validator.isNotEmpty(revoca))
  {
  
    session.removeAttribute("anagAziendaVO");
    Vector rangeAnagAzienda = (Vector)session.getAttribute("listRange");
    String dataInizioMandato = DateUtils.getCurrentDateString();
    //request.setAttribute("dataInizioMandato", dataInizioMandato);
    // Controllo che l'utente abbia selezionato un'azienda
    if(!Validator.isNotEmpty(request.getParameter("idAzienda"))) 
    {
      error = new ValidationError((String)AnagErrors.get("ERR_SELEZIONE_ELENCO_KO"));
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
        
        if(delegaAnagrafeVO == null)
        {
          throw new SolmrException((String)AnagErrors.get("ERR_VIS_NO_MANDATO"));
        }       
        
        //Fa schifo però fa delle query che genarano delle eccezioni
        //sul flusso della gestione per eseguire la revoca
        anagFacadeClient.controllaRevocaMandato(anagAziendaVO, ruoloUtenza, delegaAnagrafeVO);
        
      }
      catch(SolmrException sex) 
      {
        error = new ValidationError(sex.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(avantiIndietroURL).forward(request, response);
        return;
      }
      session.setAttribute("anagAziendaVO", anagAziendaVO);
      
    }
  
  
  }
  
  
  
  boolean intermediarioConDelega = false;    
  if(ruoloUtenza.isUtenteIntermediario())
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
    
    
    //delega stesso intermediario
    if (ruoloUtenza.getCodiceEnte().equalsIgnoreCase(anagAziendaVO.getDelegaVO().getCodiceFiscaleIntermediario()) 
        || SolmrConstants.FLAG_S.equalsIgnoreCase(delegaAnagrafeVO.getFlagFiglio()))
    {
      intermediarioConDelega = true;
    }
  }
  if(intermediarioConDelega)
  {  
    request.setAttribute("intermediarioConDelega", "true");
  }

  if(request.getParameter("conferma")!= null) 
  {
  
  
    // Recupero la data fine mandato
    String dataFineMandato = request.getParameter("dataFineMandato");
    request.setAttribute("dataFineMandato", dataFineMandato);
    	
    // Controllo che sia stata valorizzata
    if(!Validator.isNotEmpty(dataFineMandato)) 
    {
    	error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
    	errors.add("dataFineMandato", error);
    }
    // Se lo è ...
    else 
    {
    	// ... controllo che sia valida
    	if(!Validator.validateDateF(dataFineMandato)) 
      {
    		error = new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO);
        errors.add("dataFineMandato", error);
    	}
    	// Se lo è ...
    	else 
      {
    		// Controllo che non sia maggiore della data odierna
    		if(DateUtils.parseDate(dataFineMandato).after(new Date(System.currentTimeMillis()))) 
        {
    			error = new ValidationError(AnagErrors.ERRORE_DATA_FINE_MANDATO_POST_SYSDATE);
        	errors.add("dataFineMandato", error);
    		}
    		// Se non lo è ...
    		else 
        {
    			// Recupero la max data inizio mandato
    			Date dataMaxInizioMandato = null;
    			try 
          {
    				dataMaxInizioMandato = anagFacadeClient.getDataMaxInizioMandato(anagAziendaVO.getIdAzienda());
    			}
    			catch(SolmrException se) 
          {
    				error = new ValidationError(AnagErrors.ERRORE_KO_MAX_DATA_INIZIO_MANDATO);
    	    	errors.add("error", error);
    	    	request.setAttribute("errors", errors);
    	    	request.getRequestDispatcher(revocaDelegaURL).forward(request, response);
    	     	return;
    			}
    			// Controllo che la data di fine mandato non sia minore della
    			// max data inizio mandato
    			if(DateUtils.parseDate(dataFineMandato).before(dataMaxInizioMandato)) 
          {
    				error = new ValidationError(AnagErrors.ERRORE_DATA_FINE_MANDATO_POST_MAX_DATA_INIZIO_MANDATO);
          	errors.add("dataFineMandato", error);
    		  }
    	  }
      }
    }
    
    String dataRicRitorno = request.getParameter("dataRicRitorno");
    // Controllo che sia stata valorizzata
    if(Validator.isEmpty(dataRicRitorno)) 
    {
      if(ruoloUtenza.isUtenteIntermediario())
      {      
        //delega ad un altro intermediario
        if (!intermediarioConDelega)
        {
          error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
          errors.add("dataRicRitorno", error);
        }
      }
      else
      {
        error = new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO);
        errors.add("dataRicRitorno", error);
      }
    }
    // Se lo è ...
    else 
    {
      // ... controllo che sia valida
      if(!Validator.validateDateF(dataRicRitorno)) 
      {
        error = new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO);
        errors.add("dataRicRitorno", error);
      }
      // Se lo è ...
      else 
      {
        // Controllo che non sia maggiore della data odierna
        if(DateUtils.parseDate(dataRicRitorno).after(new Date(System.currentTimeMillis()))) 
        {
          error = new ValidationError(AnagErrors.ERRORE_DATA_RIC_RIT_POST_SYSDATE);
          errors.add("dataRicRitorno", error);
        }
        // Se non lo è ...
        else 
        {
          // Recupero la max data inizio mandato
          Date dataMaxInizioMandato = null;
          try 
          {
            dataMaxInizioMandato = anagFacadeClient.getDataMaxInizioMandato(anagAziendaVO.getIdAzienda());
          }
          catch(SolmrException se) 
          {
            error = new ValidationError(AnagErrors.ERRORE_KO_MAX_DATA_INIZIO_MANDATO);
            errors.add("error", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(revocaDelegaURL).forward(request, response);
            return;
          }
          // Controllo che la data di fine mandato non sia minore della
          // max data inizio mandato
          if(DateUtils.parseDate(dataRicRitorno).before(dataMaxInizioMandato)) 
          {
            error = new ValidationError(AnagErrors.ERRORE_DATA_RIC_RIT_POST_MAX_DATA_INIZIO_MANDATO);
            errors.add("dataRicRitorno", error);
          }
        }
      }
    }    	
		
    // Se si sono verificati degli errori li visualizzo
    if(errors.size() > 0) 
    {
  	  request.setAttribute("errors", errors);
  	  request.getRequestDispatcher(revocaDelegaURL).forward(request, response);
   	  return;
    }
    // Altrimenti proseguo con la revoca della delega
    else 
    {
      //Costrutto vecchio intermediario condelega
      if(intermediarioConDelega)
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
  	    if(Validator.isNotEmpty(parametroComuneMapr) 
          && parametroComuneMapr.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
        {
  	   	  documentoVO = new DocumentoVO();
  	   	  documentoVO.setExtIdDocumento(SolmrConstants.ID_TIPO_DOCUMENTO_REVOCA_MANDATO_ASSISTENZA);
  	   	  documentoVO.setIdAzienda(anagAziendaVO.getIdAzienda());
  	   	  documentoVO.setCuaa(anagAziendaVO.getCUAA());
  	   	  documentoVO.setDataInizioValidita(anagAziendaVO.getDelegaVO().getDataInizioMandato());
  	   	  documentoVO.setDataFineValidita(DateUtils.parseDate(dataFineMandato));
  	   	  documentoVO.setDataUltimoAggiornamento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
  			  documentoVO.setDataInserimento(new java.util.Date(new Timestamp(System.currentTimeMillis()).getTime()));
  	   	  // Recupero il valore del codice fiscale dell'utente intermediario che aveva registrato la delega
  	   	  it.csi.solmr.dto.comune.IntermediarioVO intermediarioVO = null;
  	   	  try 
          {
  	    	  intermediarioVO = anagFacadeClient.findIntermediarioVOByPrimaryKey(anagAziendaVO.getDelegaVO().getIdIntermediario());
  	      }
  	      catch(SolmrException se) 
          {
  	    	  error = new ValidationError(se.getMessage());
  	    	  errors.add("error", error);
  	    	  request.setAttribute("errors", errors);
  	    	  request.getRequestDispatcher(revocaDelegaURL).forward(request, response);
  	    	  return;
  	      }
  	      documentoVO.setNote("CAA: "+intermediarioVO.getCodiceFiscale());
  	    }
  	    try 
        {
  	  	  anagAziendaVO.getDelegaVO().setDataFineMandato(DateUtils.parseDate(dataFineMandato));
  	   	  anagFacadeClient.storicizzaDelega(anagAziendaVO.getDelegaVO(),ruoloUtenza, documentoVO, anagAziendaVO);
  	    }
  	    catch(Exception ex) 
        {
  	   	  error = new ValidationError(ex.getMessage());
  	   	  errors.add("error", error);
  	   	  request.setAttribute("errors", errors);
  	   	  request.getRequestDispatcher(revocaDelegaURL).forward(request, response);
  	   	  return;
  	    }
  	    session.removeAttribute("anagAziendaVO");
  	    request.getRequestDispatcher(ricercaURL).forward(request, response);
  	    return;
      }
      //delega ritardata
      else
      {
        request.setAttribute("dataFineMandato", dataFineMandato);
        request.setAttribute("dataRicRitorno", dataRicRitorno);
        %>
          <jsp:forward page = "<%=confermaRevocaDelegaURL%>"/>
        <%
        return;
        
      
      }
    }
  }
  else 
  {
    
    String dataFineMandato = request.getParameter("dataFineMandato");
    if(Validator.isEmpty(dataFineMandato))
    {
  	  dataFineMandato = DateUtils.getCurrentDateString();
  	}
   	request.setAttribute("dataFineMandato", dataFineMandato);
   	if(personaVO == null) 
    {
    	if(anagAziendaVO != null) 
      {
      	DelegaVO delegaVO = null;
      	try 
        {
        	personaVO = anagFacadeClient.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
        	session.setAttribute("personaVO", personaVO);
        	delegaVO = anagFacadeClient.getDelegaByAziendaAndIdProcedimento(anagAziendaVO.getIdAzienda(), Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)));
        	anagAziendaVO.setDelegaVO(delegaVO);
        }
        catch(SolmrException ex) 
        {
        	error = new ValidationError(ex.getMessage());
        	errors.add("error", error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(revocaDelegaURL).forward(request, response);
        	return;
        }
      }
    }
    %>
	    <jsp:forward page = "<%=revocaDelegaURL%>"/>
    <%
  }
%>