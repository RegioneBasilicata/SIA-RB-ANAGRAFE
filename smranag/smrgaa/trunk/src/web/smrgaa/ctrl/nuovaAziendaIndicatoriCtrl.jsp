<%@ page language="java" contentType="text/html" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.TipoDimensioneAziendaVO" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

	String iridePageName = "nuovaAziendaIndicatoriCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String url = "/view/nuovaAziendaIndicatoriView.jsp";
	String annullaUrl = "/view/nuovaAziendaSedeView.jsp";
	String anagraficaUrl = "/view/anagraficaView.jsp";
	String registrazioneDelegaURL = "/view/registrazioneDelegaView.jsp";
	
	String actionUrl = "../layout/nuovaAziendaCCIAA.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
	
	
	final String errMsg = "Impossibile procedere nella sezione inserisci azienda. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");
	UteVO uteVO = (UteVO)session.getAttribute("insUteVO");
	String operazione = request.getParameter("operazione");
	Vector<AziendaAtecoSecVO> vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)session.getAttribute("insVAziendaAtecoSec");
	
	PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  
  ValidationErrors errors = new ValidationErrors();

	String radiobuttonAzienda = (String)request.getAttribute("radiobuttonAzienda");
	request.setAttribute("radiobuttonAzienda", radiobuttonAzienda);
	String cuaaProvenienza = (String)request.getAttribute("cuaaProvenienza");  
	request.setAttribute("cuaaProvenienza", cuaaProvenienza);
	
	
	
	


	// L'utente ha premuto il tasto avanti e procedo con l'inserimento
	if (request.getParameter("salva") != null) 
  {
  
    String CCIAAProvinciaREA = request.getParameter("CCIAAprovREA");
    Long CCIAANumeroREA = null;
    boolean isNumerico = false;
    if(request.getParameter("strCCIAAnumeroREA") != null 
      && !request.getParameter("strCCIAAnumeroREA").equals("")) 
    {
      isNumerico = Validator.isNumericInteger(request.getParameter("strCCIAAnumeroREA"));
      if(isNumerico == true) 
      {
        try 
        {
          CCIAANumeroREA = Long.decode(request.getParameter("strCCIAAnumeroREA"));
        }
        catch(Exception ex) 
        {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_NUMERO_REA_ERRATO"));
          errors.add("strCCIAAnumeroREA",error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(url).forward(request, response);
          return;
        }
      }
    }
    
    String CCIAAannoIscrizione = request.getParameter("CCIAAannoIscrizione");
    String CCIAAnumRegImprese = request.getParameter("CCIAAnumRegImprese");
    anagAziendaVO.setStrCCIAAnumeroREA(request.getParameter("strCCIAAnumeroREA"));
    
    
    
    anagAziendaVO.setCCIAAprovREA(CCIAAProvinciaREA.toUpperCase());
    anagAziendaVO.setCCIAAnumeroREA(CCIAANumeroREA);
    anagAziendaVO.setCCIAAnumRegImprese(CCIAAnumRegImprese);
    anagAziendaVO.setCCIAAannoIscrizione(CCIAAannoIscrizione);
    
    String flagCCIAA = null;
    if (Validator.isNotEmpty(anagAziendaVO.getTipoFormaGiuridica()) 
      && Validator.isNotEmpty(anagAziendaVO.getTipoFormaGiuridica().getCode()))
    {
      try 
      {
        flagCCIAA = anagFacadeClient.getFormaGiuridicaFlagCCIAA(new Long(anagAziendaVO.getTipoFormaGiuridica().getCode().intValue()));
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_FORMA_GIURIDICA"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
    anagAziendaVO.setFlagCCIAA(flagCCIAA);
    
    
    if(CCIAAProvinciaREA != null && !CCIAAProvinciaREA.equals("")) 
    {
      try 
      {
        boolean isValida = anagFacadeClient.isProvinciaReaValida(CCIAAProvinciaREA.toUpperCase());
        if(!isValida) 
        {
          ValidationError error = new ValidationError(""+AnagErrors.get("ERR_PROV_REA"));
          errors.add("CCIAAprovREA",error);
        }
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error",error);
      }
    }
    
    
    
    //Setto l'esoneroPAgamentoGF col valore impostato nell'interfaccia 
    String  esoneroPagamentoGf = request.getParameter("esoneroPagamentoGf");
    if(Validator.isNotEmpty(esoneroPagamentoGf))
    {
      anagAziendaVO.setEsoneroPagamentoGF(esoneroPagamentoGf);
    }
  
  
  	
  	// Effettuo la validazione formale dei dati.
  	errors =  anagAziendaVO.validateInsertIndicatori();
  	
  	
  	String codiceOTE = null;
    if(request.getParameter("codiceOTE") != null && !request.getParameter("codiceOTE").equals("")) 
    {
      codiceOTE = request.getParameter("codiceOTE");
    }
    String descrizioneOTE = "";
    if(request.getParameter("descrizioneOTE") != null && !request.getParameter("descrizioneOTE").equals("")) 
    {
      descrizioneOTE = request.getParameter("descrizioneOTE");
    }
  	
  	if(codiceOTE != null && !codiceOTE.equals("") || descrizioneOTE != null && !descrizioneOTE.equals("")) 
    {
      Long idCodiceOTE = null;
      try 
      {
        idCodiceOTE = anagFacadeClient.ricercaIdAttivitaOTE(codiceOTE,descrizioneOTE);
      }
      catch (SolmrException ex) 
      {
        ValidationError error = new ValidationError(ex.getMessage());
        errors.add("codiceOTE", error);
      }
      if(idCodiceOTE != null) 
      {
        try 
        {
          descrizioneOTE = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_ATTIVITA_OTE,new Integer(idCodiceOTE.toString()));
        }
        catch(SolmrException ex) 
        {
          ValidationError error = new ValidationError(ex.getMessage());
          errors.add("codiceOTE", error);
        }
        CodeDescription descriptionAttivitaOTE = new CodeDescription(Integer.decode(idCodiceOTE.toString()),descrizioneOTE);
        descriptionAttivitaOTE.setSecondaryCode(codiceOTE.toString());
        anagAziendaVO.setTipoAttivitaOTE(descriptionAttivitaOTE);
        anagAziendaVO.setStrAttivitaOTE(descrizioneOTE);
      }
    }
    
    
    String codiceATECO = null;
    if(request.getParameter("codiceATECO") != null && !request.getParameter("codiceATECO").equals("")) {
        codiceATECO = request.getParameter("codiceATECO");
    }
    String descrizioneATECO = "";
    if(request.getParameter("descrizioneATECO") != null && !request.getParameter("descrizioneATECO").equals("")) {
        descrizioneATECO = request.getParameter("descrizioneATECO");
    }
    
    
    if(codiceATECO != null && !codiceATECO.equals("") 
      || descrizioneATECO != null && !descrizioneATECO.equals("")) 
    {
      Long idCodiceATECO = null;
      try 
      {
        idCodiceATECO = anagFacadeClient.ricercaIdAttivitaATECO(codiceATECO,descrizioneATECO);
      }
      catch (SolmrException ex) 
      {
        ValidationError error = new ValidationError(ex.getMessage());
        errors.add("codiceATECO", error);
      }
      if(idCodiceATECO != null) 
      {
        try 
        {
          descrizioneATECO = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_ATTIVITA_ATECO,new Integer(idCodiceATECO.toString()));
        }
        catch(SolmrException ex) 
        {
          ValidationError error = new ValidationError(ex.getMessage());
          errors.add("codiceATECO", error);
        }
        CodeDescription descriptionAttivitaATECO = new CodeDescription(Integer.decode(idCodiceATECO.toString()),descrizioneATECO);
        descriptionAttivitaATECO.setSecondaryCode(codiceATECO.toString());
        anagAziendaVO.setTipoAttivitaATECO(descriptionAttivitaATECO);
        anagAziendaVO.setStrAttivitaATECO(descrizioneATECO);
      }
    }
    
    session.setAttribute("insAnagVO",anagAziendaVO);
    
    if(errors != null && errors.size() != 0) 
    {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }   
  	
  	
  	anagAziendaVO.setStrAttivitaOTE(anagAziendaVO.getTipoAttivitaOTE().getDescription());
    anagAziendaVO.setStrAttivitaATECO(anagAziendaVO.getTipoAttivitaATECO().getDescription());
    
    
    
    //Setto la dimensione azienda col valore impostato nell'interfaccia 
    String idDimensioneAzienda = request.getParameter("idDimensioneAzienda");
    if(Validator.isNotEmpty(idDimensioneAzienda))
    {
      anagAziendaVO.setIdDimensioneAzienda(new Long(idDimensioneAzienda));
    }
    
    
    anagAziendaVO.setVAziendaATECOSec(vAziendaAtecoSec);
  	
  
  	String cognomeNomeUtente = ruoloUtenza.getDenominazione();
  	//getInfoAggiuntiva di prifiloUtenza ***da verificare 
  	anagAziendaVO.setUltimaModifica(DateUtils.formatDate(new Date(System.currentTimeMillis()))
  	  +" "+cognomeNomeUtente+" "+ruoloUtenza.getDescrizioneEnte());
  	Long idAnagAzienda = null;

  	// Se tutti i controlli sono superati inserisco l'azienda anagrafica
  	try 
    {
    	idAnagAzienda = anagFacadeClient.insertAzienda(anagAziendaVO, personaFisicaVO, uteVO, ruoloUtenza.getIdUtente());
    	anagAziendaVO.setIdAnagAzienda(idAnagAzienda);
  	}
  	catch(SolmrException se) 
    {
      String errore = se.getMessage();
      if(Validator.isNotEmpty(errore))
      {
        errore = errore.replaceAll("\"","");
      }
    	ValidationError error = new ValidationError(errore);
    	errors.add("error", error);
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
  	}

  	// Rimuovo tutto ciò che avevo inserito in sessione per la gestione dei dati
  	session.removeAttribute("rappresentanteLegaleAAEP");
  	session.removeAttribute("sedeAAEP");
    session.removeAttribute("anagTribForInsert");
  	session.removeAttribute("indietro");
  	session.removeAttribute("insAnagVO");
  	session.removeAttribute("insUteVO");
  	session.removeAttribute("insVAziendaAtecoSec");
  	//session.removeAttribute("pecAAEP");

  	// Recupero l'azienda inserita
  	try 
    {
    	anagAziendaVO = anagFacadeClient.getAziendaById(anagAziendaVO.getIdAnagAzienda());
  	}
 	 	catch(SolmrException se) 
    {
    	ValidationError error = new ValidationError(se.getMessage());
    	errors.add("error", error);
   	 	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(url).forward(request, response);
    	return;
  	}
  	session.setAttribute("anagAziendaVO",anagAziendaVO);
  	
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

  	// Se l'utente è un intermediario vado alla pagina di registra mandato
  	if(ruoloUtenza.isUtenteIntermediario())
    {
    	// Recupero gli uffici zona intermediario
    	Vector elencoUfficiZonaIntermediario = null;
    	String messaggioErrore = null;
    	String dataInizioMandato = DateUtils.getCurrentDateString();
     	request.setAttribute("dataInizioMandato", dataInizioMandato);
  		try 
      {
    		elencoUfficiZonaIntermediario = anagFacadeClient.getElencoUfficiZonaIntermediarioAttiviByIdIntermediario(utenteAbilitazioni);
  		}
  		catch(SolmrException se) 
      {
    		messaggioErrore = se.getMessage();
    		request.setAttribute("messaggioErrore", messaggioErrore);
    		iridePageName = "registrazioneDelegaCtrl.jsp";
    		%>
    			<%@include file = "/include/autorizzazione.inc" %>
      		<jsp:forward page = "<%=registrazioneDelegaURL%>" />
    		<%
  		}
  		// Li metto in request
  		request.setAttribute("elencoUfficiZonaIntermediario", elencoUfficiZonaIntermediario);
  		// Vado alla pagina di inserimento delega
  		iridePageName = "registrazioneDelegaCtrl.jsp";
  		%>
  			<%@include file = "/include/autorizzazione.inc" %>
    		<jsp:forward page = "<%=registrazioneDelegaURL%>" />
  		<%
  	}
  	iridePageName = "anagraficaCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
    	<jsp:forward page="<%= anagraficaUrl %>"/>
  	<%
	}
	// L'utente ha premuto il tasto annulla
	if(request.getParameter("indietro") != null) 
  {
    String CCIAAannoIscrizione = request.getParameter("CCIAAannoIscrizione");
    String CCIAAnumRegImprese = request.getParameter("CCIAAnumRegImprese");
    String CCIAAProvinciaREA = request.getParameter("CCIAAprovREA");
    if(Validator.isNotEmpty(CCIAAProvinciaREA))
    {
  	  anagAziendaVO.setCCIAAprovREA(CCIAAProvinciaREA.toUpperCase());
  	}
  	
  	boolean isNumerico = false;
  	Long CCIAANumeroREA = null;
    if(request.getParameter("strCCIAAnumeroREA") != null 
      && !request.getParameter("strCCIAAnumeroREA").equals("")) 
    {
      isNumerico = Validator.isNumericInteger(request.getParameter("strCCIAAnumeroREA"));
      if(isNumerico == true) 
      {
        try 
        {
          CCIAANumeroREA = Long.decode(request.getParameter("strCCIAAnumeroREA"));
        }
        //Non sollevo nessuna eccezione poichè vado indietro!!!!
        catch(Exception ex) 
        {}
      }
    }
    
    anagAziendaVO.setCCIAAnumeroREA(CCIAANumeroREA);
    anagAziendaVO.setCCIAAnumRegImprese(CCIAAnumRegImprese);
    anagAziendaVO.setCCIAAannoIscrizione(CCIAAannoIscrizione);
    anagAziendaVO.setStrCCIAAnumeroREA(request.getParameter("strCCIAAnumeroREA"));
    
    
    
    //Setto l'esoneroPAgamentoGF col valore impostato nell'interfaccia 
    String  esoneroPagamentoGf = request.getParameter("esoneroPagamentoGf");
    if(Validator.isNotEmpty(esoneroPagamentoGf))
    {
      anagAziendaVO.setEsoneroPagamentoGF(esoneroPagamentoGf);
    }
    
    
    
    //Setto la dimensione azienda col valore impostato nell'interfaccia 
    String idDimensioneAzienda = request.getParameter("idDimensioneAzienda");
    if(Validator.isNotEmpty(idDimensioneAzienda))
    {
      anagAziendaVO.setIdDimensioneAzienda(new Long(idDimensioneAzienda));
    }
    
    
    
    String codiceOTE = null;
    if(request.getParameter("codiceOTE") != null && !request.getParameter("codiceOTE").equals("")) 
    {
      codiceOTE = request.getParameter("codiceOTE");
    }
    String descrizioneOTE = "";
    if(request.getParameter("descrizioneOTE") != null && !request.getParameter("descrizioneOTE").equals("")) 
    {
      descrizioneOTE = request.getParameter("descrizioneOTE");
    }
    
    if(codiceOTE != null && !codiceOTE.equals("") || descrizioneOTE != null && !descrizioneOTE.equals("")) 
    {
      Long idCodiceOTE = null;
      try 
      {
        idCodiceOTE = anagFacadeClient.ricercaIdAttivitaOTE(codiceOTE,descrizioneOTE);
      }
      catch (SolmrException ex) 
      {}
      
      if(idCodiceOTE != null) 
      {
        try 
        {
          descrizioneOTE = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_ATTIVITA_OTE,new Integer(idCodiceOTE.toString()));
        }
        catch(SolmrException ex) 
        {}
        CodeDescription descriptionAttivitaOTE = new CodeDescription(Integer.decode(idCodiceOTE.toString()),descrizioneOTE);
        descriptionAttivitaOTE.setSecondaryCode(codiceOTE.toString());
        anagAziendaVO.setTipoAttivitaOTE(descriptionAttivitaOTE);
        anagAziendaVO.setStrAttivitaOTE(descrizioneOTE);
      }
    }
    
    
    String codiceATECO = null;
    if(request.getParameter("codiceATECO") != null && !request.getParameter("codiceATECO").equals("")) 
    {
      codiceATECO = request.getParameter("codiceATECO");
    }
    String descrizioneATECO = "";
    if(request.getParameter("descrizioneATECO") != null && !request.getParameter("descrizioneATECO").equals("")) 
    {
      descrizioneATECO = request.getParameter("descrizioneATECO");
    }
    
    
    if(codiceATECO != null && !codiceATECO.equals("") 
      || descrizioneATECO != null && !descrizioneATECO.equals("")) 
    {
      Long idCodiceATECO = null;
      try 
      {
        idCodiceATECO = anagFacadeClient.ricercaIdAttivitaATECO(codiceATECO,descrizioneATECO);
      }
      catch (SolmrException ex) 
      {}
      
      if(idCodiceATECO != null) 
      {
        try 
        {
          descrizioneATECO = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_ATTIVITA_ATECO,new Integer(idCodiceATECO.toString()));
        }
        catch(SolmrException ex) 
        {}
        
        CodeDescription descriptionAttivitaATECO = new CodeDescription(Integer.decode(idCodiceATECO.toString()),descrizioneATECO);
        descriptionAttivitaATECO.setSecondaryCode(codiceATECO.toString());
        anagAziendaVO.setTipoAttivitaATECO(descriptionAttivitaATECO);
        anagAziendaVO.setStrAttivitaATECO(descrizioneATECO);
      }
    }
    
    
    
    
    
    session.setAttribute("insAnagVO",anagAziendaVO);
    session.setAttribute("indietro","indietro");
  	
    request.getRequestDispatcher(annullaUrl).forward(request, response);
    return;
	}
	
	if(Validator.isNotEmpty(operazione))
	{
	  if(operazione.equalsIgnoreCase("inserisciATECOSec")) 
    {
      if(vAziendaAtecoSec == null)
      {
        vAziendaAtecoSec = new Vector<AziendaAtecoSecVO>();
      }
      String idAttivitaATECOSec = request.getParameter("idAttivitaATECOSec");
      String codiceATECOSec = request.getParameter("codiceATECOSec");
      String descrizioneATECOSec = request.getParameter("descrizioneATECOSec");
      
      String codiceATECO = request.getParameter("codiceATECO");
      
      //Ho cliccato su inserisci
      if(Validator.isNotEmpty(request.getParameter("insAtecoSec")))
      {
        if(!Validator.isNotEmpty(codiceATECOSec) && !Validator.isNotEmpty(descrizioneATECOSec)) 
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "codiceATECOSec", AnagErrors.ERR_COD_DESC_ATECO_OBBLIGATORI);
          errors = ErrorUtils.setValidErrNoNull(errors, "descrizioneATECOSec", AnagErrors.ERR_COD_DESC_ATECO_OBBLIGATORI);
        }
        else
        {
          Vector tipiAttivitaATECO = null;
          try 
          {
            tipiAttivitaATECO = anagFacadeClient.getTipiAttivitaATECO(codiceATECOSec, descrizioneATECOSec);
          }
          catch(SolmrException se) 
          {      
            SolmrLogger.info(this, " - nuovaAziendaIndicatoriCtrl.jsp - FINE PAGINA");
            String messaggio = errMsg+""+se.toString();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", actionUrl);
            %>
              <jsp:forward page="<%= erroreViewUrl %>" />
            <%
            return;
          }
          
          if(tipiAttivitaATECO == null)
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec", AnagErrors.RICERCAATTIVITAATECO);
          }
          else
          {
            if(tipiAttivitaATECO.size() > 1)
            {
              errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec", AnagErrors.ERRORE_FILTRI_GENERICI);
            }
            else
            {
              CodeDescription cd = (CodeDescription)tipiAttivitaATECO.get(0);
              idAttivitaATECOSec = cd.getCode().toString();
              codiceATECOSec = cd.getSecondaryCode();
              descrizioneATECOSec = cd.getDescription();
            }
          }
        }
      }  
      
      
      if(Validator.isNotEmpty(codiceATECO))
      {
        if(codiceATECO.equalsIgnoreCase(codiceATECOSec))
        {
          errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec",AnagErrors.ERR_COD_ATECO_GIA_PRESENTE);
        }
      }
      
      if(vAziendaAtecoSec.size() > 0)
      {
        for(int j=0;j<vAziendaAtecoSec.size();j++)
        {
          AziendaAtecoSecVO aziendaAtecoSecVOtmp = (AziendaAtecoSecVO)vAziendaAtecoSec.get(j);
          if(aziendaAtecoSecVOtmp.getCodAttivitaAteco().equalsIgnoreCase(codiceATECOSec))
          {
            errors = ErrorUtils.setValidErrNoNull(errors, "inserisciATECOSec",AnagErrors.ERR_COD_ATECO_SEC_GIA_PRESENTE);
            break;
          } 
        }
      }
      
      if((errors !=null) && (errors.size() > 0))
      {
        request.setAttribute("errors", errors);
        %>
          <jsp:forward page= "<%= url %>" />
        <%
        return;
      }
      
      AziendaAtecoSecVO aziendaAtecoSecVO = new AziendaAtecoSecVO();
      aziendaAtecoSecVO.setIdAttivitaAteco(new Long(idAttivitaATECOSec).longValue());
      aziendaAtecoSecVO.setCodAttivitaAteco(codiceATECOSec);
      aziendaAtecoSecVO.setDescAttivitaAteco(descrizioneATECOSec);
      
      vAziendaAtecoSec.add(aziendaAtecoSecVO);
      
      session.setAttribute("insVAziendaAtecoSec", vAziendaAtecoSec);
      operazione = "";
      
      %>
        <jsp:forward page= "<%= url %>" />
      <%
      return;
      
    }
    else if(operazione.equalsIgnoreCase("eliminaATECOSec")) 
    {
      String[] chkAttivitaAtecoSec = request.getParameterValues("chkAttivitaAtecoSec");
      if(Validator.isNotEmpty(chkAttivitaAtecoSec))
      {
        for(int j=(chkAttivitaAtecoSec.length - 1);j>=0;j--)
        {
          vAziendaAtecoSec.remove(new Integer(chkAttivitaAtecoSec[j]).intValue());
        }
      }
      else
      {
        errors = ErrorUtils.setValidErrNoNull(errors, "eliminaATECOSec",AnagErrors.ERR_NO_ELEMENTI);
      }
      
      if((errors !=null) && (errors.size() > 0))
      {
        request.setAttribute("errors", errors);
        %>
          <jsp:forward page= "<%= url %>" />
        <%
        return;
      }
      
      if(vAziendaAtecoSec.size() == 0)
      {
        vAziendaAtecoSec = null;
      }
      
      session.setAttribute("insVAziendaAtecoSec",vAziendaAtecoSec);
      
      %>
        <jsp:forward page= "<%= url %>" />
      <%
      return;
    }
	
	}
	
%>
