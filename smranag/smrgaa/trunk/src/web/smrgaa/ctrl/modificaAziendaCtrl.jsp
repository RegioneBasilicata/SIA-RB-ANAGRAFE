<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.util.ErrorUtils" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.PlSqlCalcoloOteVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<jsp:useBean id="anagErrorVO" scope="request" class="it.csi.solmr.dto.anag.AnagAziendaVO">
<jsp:setProperty name="anagErrorVO" property="*" />
<%
  anagErrorVO.setCUAA(request.getParameter("CUAA"));  
%>
</jsp:useBean>


<%

	String iridePageName = "modificaAziendaCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

 	AnagFacadeClient client = new AnagFacadeClient();
 	String url = "/view/modificaAziendaView.jsp" ;
 	String pageDup= "../layout/anagrafica_mod.htm";
 	String urlCUAAdup = "/view/confermaNuovaAziendaCUAAdupView.jsp";
 	String operazione = request.getParameter("operazione");
 	String richiestaModifica = "ok";
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	String idTipoIntermediarioDelegato = null;
 	boolean salva = false;
 	AnagAziendaVO voAnagModifica = null;
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	ValidationErrors errors = new ValidationErrors();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione modifica azienda. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
 

 	if(anagAziendaVO != null) 
  {
  
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
        request.getRequestDispatcher(url).forward(request, response);
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
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
	}

 	if(request.getParameter("salva") != null) 
  {
 		SolmrLogger.debug(this, "-- SALVA --");
 		if(anagAziendaVO.getDataFineVal() != null || anagAziendaVO.getDataCessazione() != null) 
    {
    	ValidationError error = new ValidationError(""+AnagErrors.get("NO_MOD_AZIENDA_CESSATA"));
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

   	anagErrorVO.setFlagAziendaProvvisoria(anagAziendaVO.isFlagAziendaProvvisoria());

    voAnagModifica = new AnagAziendaVO();
   	try 
    {
   	  SolmrLogger.debug(this, "-- getAziendaById");	
      voAnagModifica = client.getAziendaById(anagAziendaVO.getIdAnagAzienda());
     	session.removeAttribute("voAnagModifica");
   	}
   	catch(SolmrException sex) 
    {
      errors = new ValidationErrors();
     	ValidationError error = new ValidationError(sex.getMessage());
     	errors.add("error", error);
     	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(url).forward(request, response);
     	return;
   	}
   	Integer idCodFormaGiurOLD = null;
   	if(voAnagModifica.getTipoFormaGiuridica() != null 
      && voAnagModifica.getTipoFormaGiuridica().getCode() != null) 
    {
      idCodFormaGiurOLD = voAnagModifica.getTipoFormaGiuridica().getCode();
   	}

   	errors = new ValidationErrors();

   	String provinciaCompetenza = request.getParameter("provincePiemonte");

   	String cuaa = request.getParameter("cuaa");
   	voAnagModifica.setCUAA(cuaa);

   	voAnagModifica.setDenominazione(request.getParameter("denominazione"));
   	voAnagModifica.setIntestazionePartitaIva(request.getParameter("intestazionePartitaIva"));
   	voAnagModifica.setPartitaIVA(request.getParameter("partitaIVA"));
   	voAnagModifica.setProvCompetenza(provinciaCompetenza);
   	voAnagModifica.setProvincePiemonte(provinciaCompetenza);
   	voAnagModifica.setTelefono(request.getParameter("telefono"));
    voAnagModifica.setFax(request.getParameter("fax"));
    voAnagModifica.setSitoWEB(request.getParameter("sitoWEB"));
    voAnagModifica.setMail(request.getParameter("mail"));
    voAnagModifica.setPec(request.getParameter("pec"));
   	
    
    

   	Integer idTipoAzienda = null;
   	if(Validator.isNotEmpty(request.getParameter("tipiAzienda"))) 
    {
      idTipoAzienda = Integer.decode(request.getParameter("tipiAzienda"));
    }
   	if(idTipoAzienda != null) 
    {
      voAnagModifica.setTipiAzienda(String.valueOf(idTipoAzienda));
      String tipiAzienda = client.getDescriptionFromCode(SolmrConstants.TAB_TIPO_TIPOLOGIA_AZIENDA, idTipoAzienda);
      voAnagModifica.setTipoTipologiaAzienda(new CodeDescription(idTipoAzienda,tipiAzienda));
    }
    else 
    {
      voAnagModifica.setTipiAzienda(null);
      voAnagModifica.setTipoTipologiaAzienda(null);
    }

    String tipiFormaGiuridica = request.getParameter("tipiFormaGiuridica");
    Integer idTipoFormaGiuridica = null;
    if(Validator.isNotEmpty(tipiFormaGiuridica))
    {
      idTipoFormaGiuridica = Integer.decode(request.getParameter("tipiFormaGiuridica"));
    }
    if(idTipoFormaGiuridica != null) 
    {
      voAnagModifica.setTipiFormaGiuridica(String.valueOf(idTipoFormaGiuridica));
      anagErrorVO.setTipiFormaGiuridica(String.valueOf(idTipoFormaGiuridica));
      voAnagModifica.setTipoFormaGiuridica(new CodeDescription(Integer.decode(tipiFormaGiuridica),""));
    }
    else 
    {
      voAnagModifica.setTipiFormaGiuridica(null);
      voAnagModifica.setTipoFormaGiuridica(null);
    }
    
   	voAnagModifica.setNote(request.getParameter("note"));
   	voAnagModifica.setIdUtenteAggiornamento(ruoloUtenza.getIdUtente());
   	voAnagModifica.setIdIntermediarioDelegato(null);
   	session.setAttribute("voAnagModifica", voAnagModifica);
   	if(idCodFormaGiurOLD != null && voAnagModifica.getTipoFormaGiuridica() != null 
      && voAnagModifica.getTipoFormaGiuridica().getCode() != null) 
    {
      if((idCodFormaGiurOLD.equals(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) 
          && (!voAnagModifica.getTipoFormaGiuridica().getCode().equals(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) 
          && !voAnagModifica.getTipoFormaGiuridica().getCode().equals(new Integer(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO)))) 
        || (idCodFormaGiurOLD.equals(new Integer(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO)) 
          && (!voAnagModifica.getTipoFormaGiuridica().getCode().equals(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) 
          && !voAnagModifica.getTipoFormaGiuridica().getCode().equals(new Integer(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO))))) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_AVAILABLE_MODIFICA_AZIENDA);
       	errors.add("error", error);
       	request.setAttribute("errors", errors);
       	request.getRequestDispatcher(url).forward(request, response);
       	return;
     	}
     	if((!idCodFormaGiurOLD.equals(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) 
          && !idCodFormaGiurOLD.equals(new Integer(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO))) 
        && (voAnagModifica.getTipoFormaGiuridica().getCode().equals(SolmrConstants.TIPO_FORMA_GIURIDICA_INDIVIDUALE) 
          || voAnagModifica.getTipoFormaGiuridica().getCode().equals(new Integer(SolmrConstants.ID_SOGGETTO_NON_COSTITUITO)))) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_NO_AVAILABLE_MODIFICA_AZIENDA);
       	errors.add("error", error);
       	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(url).forward(request, response);
       	return;
     	}
   	}
   	String flagCCIAA = null;
   	String flagPartitaIva = null;
   	if(Validator.isNotEmpty(idTipoFormaGiuridica)) 
    {
      try 
      {
        flagCCIAA = client.getFormaGiuridicaFlagCCIAA(new Long(idTipoFormaGiuridica.longValue()));
       	flagPartitaIva = client.getFlagPartitaIva(new Long(idTipoFormaGiuridica.longValue()));
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
   	anagErrorVO.setCUAA(voAnagModifica.getCUAA());
   	anagErrorVO.setFlagCCIAA(flagCCIAA);
   	anagErrorVO.setFlagPartitaIva(flagPartitaIva);
   	anagErrorVO.setCUAA(voAnagModifica.getCUAA());
   	anagErrorVO.setProvCompetenza(voAnagModifica.getProvCompetenza());
   	anagErrorVO.setProvincePiemonte(voAnagModifica.getProvincePiemonte());
   	anagErrorVO.setMail(voAnagModifica.getMail());
   	anagErrorVO.setPec(voAnagModifica.getPec());
   	errors = anagErrorVO.validateUpdateAnag();
    
    
   	
   	// Controllo che non esista già un'azienda attiva con il cuaa inserito
   	SolmrLogger.debug(this, "-- Controllo che non esista gia' un'azienda attiva con il cuaa inserito");
   	if(!Validator.isNotEmpty(anagAziendaVO.getCUAA()) 
      || !anagAziendaVO.getCUAA().equalsIgnoreCase(voAnagModifica.getCUAA())) 
    {
      try 
      {
    	SolmrLogger.debug(this, "-- checkCUAA");
        client.checkCUAA(voAnagModifica.getCUAA().toUpperCase());
     	}
     	catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(se.getMessage());
       	errors.add("cuaa", error);
       	request.setAttribute("errors", errors);
       	request.getRequestDispatcher(url).forward(request, response);
       	return;
     	}
   	}
    
   	if(voAnagModifica.getTipoFormaGiuridica() != null) 
    {
      // Se l'azienda inserita è una ditta individuale o con forma giuridica uguale a soggetto non giuridamente
     	// costituito allora recupero il titolare dell'azienda
     	if(voAnagModifica.getTipoFormaGiuridica().getCode()
          .compareTo(Integer.decode(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_INDIVIDUALE")).toString())) == 0 
        || voAnagModifica.getTipoFormaGiuridica().getCode()
          .compareTo(Integer.decode(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_PERSONA_NO_ATTIVITA_IMPRESA")).toString())) == 0) 
      {
        PersonaFisicaVO personaTitolareVO = null;
       	try 
        {
          personaTitolareVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
       	}
       	catch(SolmrException se) 
        {
          ValidationError error = new ValidationError(se.getMessage());
         	errors.add("cuaa", error);
         	request.setAttribute("errors", errors);
         	request.getRequestDispatcher(url).forward(request, response);
         	return;
       	}
       	// Se il titolare non possiede un codice fiscale impedisco di effettuare la bonifica dell'azienda
       	if(!Validator.isNotEmpty(personaTitolareVO.getCodiceFiscale())) 
        {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_BONIFICA_KO_NO_CODICE_FISCALE_TITOLARE"));
         	errors.add("error", error);
         	request.setAttribute("errors", errors);
         	request.getRequestDispatcher(url).forward(request, response);
         	return;
       	}
       	// Se invece lo possiede controllo che il CUAA dell'azienda sia uguale al codice fiscale del titolare
       	else 
        {
          if(!voAnagModifica.getCUAA().equalsIgnoreCase(personaTitolareVO.getCodiceFiscale())) 
          {
            ValidationError error = new ValidationError((String)AnagErrors.get("ERR_BONIFICA_KO_NO_CODICE_FISCALE_EQUALS_CUAA"));
           	errors.add("cuaa", error);
           	request.setAttribute("errors", errors);
           	request.getRequestDispatcher(url).forward(request, response);
           	return;
         	}
       	}
     	}
   	}
    
   	if(errors != null && errors.size() != 0) 
    {
      request.setAttribute("errors", errors);
     	request.getRequestDispatcher(url).forward(request, response);
     	return;
   	}
    
   	if(!voAnagModifica.isFlagAziendaProvvisoria() && client.isFlagUnivocitaAzienda(idTipoAzienda)) 
    {
      // controllo che non esista un'azienda attiva diversa da quella che sto modificando
     	// con la stessa partita iva
     	try 
      {
        if(Validator.isNotEmpty(voAnagModifica.getPartitaIVA())) 
        {
          SolmrLogger.debug(this, "-- checkPartitaIVA");
          client.checkPartitaIVA(voAnagModifica.getPartitaIVA(), voAnagModifica.getIdAzienda());
         	session.setAttribute("voAnagModifica",voAnagModifica);
         	session.setAttribute("load","true");
       	}
        SolmrLogger.debug(this, "salva = true");
       	salva = true;
     	}
     	catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(se.getMessage());
       	if(se.getMessage().equalsIgnoreCase(""+AnagErrors.get("PIVA_GIA_ESISTENTE"))) 
        {
          errors.add("partitaIVA", error);
       	}
       	else 
        {
          errors.add("error", error);
       	}
       	request.setAttribute("errors", errors);
       	request.getRequestDispatcher(url).forward(request, response);
       	return;
     	}
   	}
   	else 
    {
      // controllo che non esista un'azienda attiva diversa da quella che sto modificando
     	// con la stessa partita iva
     	SolmrLogger.debug(this, "-- controllo che non esista un'azienda attiva diversa da quella che sto modificando con la stessa partita iva");
     	if(Validator.isNotEmpty(voAnagModifica.getPartitaIVA())) 
      {
        AnagAziendaVO anagTemp = client.getAltraAziendaFromPartitaIVA(voAnagModifica.getPartitaIVA(), voAnagModifica.getIdAzienda());
       	session.setAttribute("voAnagModifica",voAnagModifica);
       	session.setAttribute("load","true");
       	if(anagTemp == null) 
        {
       		SolmrLogger.debug(this, "-- Non ho trovato record quindi procedo normalmente");
          // Non ho trovato record quindi procedo normalmente
         	salva = true;
       	}
       	else 
        {
          // Ho trovato un record quindi visualizzarlo e chedere all'utente se
          // è sicuro di voler proseguire
          SolmrLogger.debug(this, "-- Ho trovato un record quindi lo devo visualizzare, pageDup ="+pageDup);
          request.setAttribute("pageDup",pageDup);
          request.setAttribute("domanda", (String)AnagErrors.get("ERR_PARTITA_IVA_MOD_DUP"));
          request.setAttribute("anagDup",anagTemp);
          request.getRequestDispatcher(urlCUAAdup).forward(request, response);
          return;
        }
     	}
     	else 
      {
     	  SolmrLogger.debug(this, "-- salva = true"); 
     	  salva = true;
     	}
   	}
	} //if salva
  
 	if(request.getParameter("conferma") != null || salva) 
  {
    voAnagModifica = (AnagAziendaVO)session.getAttribute("voAnagModifica");
   	anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   	errors = new ValidationErrors();
    boolean datiUguali = true;
    
    //sono PA
    if(session.getAttribute("modificaCampiAnagrafici") != null)
    {
      datiUguali=anagAziendaVO.equalsForUpdateTabAnagrafica(voAnagModifica,true);
    }
    else
    {
      datiUguali=anagAziendaVO.equalsForUpdateTabAnagrafica(voAnagModifica,false);
    }
    
    SolmrLogger.debug(this, "-- datiUguali ="+datiUguali);
    SolmrLogger.debug(this, "-- anagAziendaVO.getDataInizioVal() ="+anagAziendaVO.getDataInizioVal());
   	if(DateUtils.isToday(anagAziendaVO.getDataInizioVal()) || datiUguali)
    {
      // Non è possibile modificare i dati di una azienda storicizzata.
     	if(anagAziendaVO.getDataFineVal() != null || anagAziendaVO.getDataCessazione() != null) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("NO_MOD_AZIENDA_CESSATA"));
       	errors.add("error", error);
       	request.setAttribute("errors", errors);
       	request.getRequestDispatcher(url).forward(request, response);
       	return;
     	}
     	// Se sono qui è perchè o la modifica è stata fatta lo stesso giorno,
     	//oppure sono stati modificati solo dati meno importanti (mail,tel ecc....)
     	if(!anagAziendaVO.equalsForUpdateTabAnagrafica(voAnagModifica,true))
     	{
	     	try 
	      {
	     	SolmrLogger.debug(this, "-- updateAzienda");
	        client.updateAzienda(voAnagModifica);
	     	}
	     	catch(SolmrException se) 
	      {
	        ValidationError error = new ValidationError(se.getMessage());
	       	errors.add("error", error);
	       	request.setAttribute("errors", errors);
	       	request.getRequestDispatcher(url).forward(request, response);
	       	return;
	     	}
	     	AnagAziendaVO nuovoAnagAziendaVO = new AnagAziendaVO();
	     	try 
	      {
	        nuovoAnagAziendaVO = client.findAziendaAttiva(voAnagModifica.getIdAzienda());
	       	nuovoAnagAziendaVO.setTipiFormaGiuridica(nuovoAnagAziendaVO.getTipoFormaGiuridica().getDescription());
	       	//nuovoAnagAziendaVO.setStrCCIAAnumeroREA(voAnagModifica.getStrCCIAAnumeroREA());
	     	}
	     	catch(SolmrException se) 
	      {
	        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_AZIENDA"));
	       	errors.add("error", error);
	       	request.setAttribute("errors", errors);
	       	request.getRequestDispatcher(url).forward(request, response);
	       	return;
	     	}
	     	session.removeAttribute("anagAziendaVO");
	     	session.setAttribute("anagAziendaVO",nuovoAnagAziendaVO);
	    }
     	%>
        <jsp:forward page = "/view/anagraficaView.jsp" />
     	<%
   	}
   	else 
    {
      // Sono stati modificati i dati quindi avviso l'utente che sta per effettuare una storicizzazione
      // dell'azienda
      session.setAttribute("richiestaModifica", richiestaModifica);
     	%>
        <jsp:forward page = "<%= url %>" />
     	<%
     	return;
   	}
	}
 	else if (request.getParameter("annulla")!=null)
  {
    %>
  	 <jsp:forward page = "/view/anagraficaView.jsp" />
  	<%
 	}
 	// Arrivo dalla POP-UP
 	else if (request.getParameter("operazione") != null) 
  {
    // Arrivo dal pop-up e ho scelto di storicizzare
    if(operazione.equalsIgnoreCase("S")) 
    {
      voAnagModifica = (AnagAziendaVO)session.getAttribute("voAnagModifica");
      errors = new ValidationErrors();
      // Recupero il motivo della modifica da salvare solo in caso di storicizzazione
      String motivoModifica = request.getParameter("motivoModifica");
      voAnagModifica.setMotivoModifica(motivoModifica);
      // Storicizzo l'azienda
      try 
      {
        client.storicizzaAzienda(voAnagModifica,ruoloUtenza.getIdUtente());
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error", error);
        session.removeAttribute("richiestaModifica");
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      catch(Exception se) 
      {
        ValidationError error = new ValidationError(""+SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      // Recupero i nuovi dati della ditta modificata
      AnagAziendaVO nuovoAnagAziendaVO = new AnagAziendaVO();
      try 
      {
        nuovoAnagAziendaVO = client.findAziendaAttiva(voAnagModifica.getIdAzienda());
        nuovoAnagAziendaVO.setTipiFormaGiuridica(nuovoAnagAziendaVO.getTipoFormaGiuridica().getDescription());
        //nuovoAnagAziendaVO.setStrCCIAAnumeroREA(voAnagModifica.getStrCCIAAnumeroREA());
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_AZIENDA"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      catch(Exception se) 
      {
        ValidationError error = new ValidationError(""+SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION"));
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      session.removeAttribute("anagAziendaVO");
      session.setAttribute("anagAziendaVO",nuovoAnagAziendaVO);
      %>
        <jsp:forward page = "/view/anagraficaView.jsp" />
      <%
    }
 	}
 	// E' la prima volta che entro
 	else 
  {
    // recupero dalla sessione i dati dell'anagrafica
   	session.removeAttribute("voAnagModifica");
   	session.removeAttribute("isOk");
   	anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   	errors = new ValidationErrors();
   	url = "/view/anagraficaView.jsp";
   	try 
    {
     	anagAziendaVO = client.findAziendaAttiva(anagAziendaVO.getIdAzienda());
     	anagAziendaVO.setTipiFormaGiuridica(anagAziendaVO.getTipoFormaGiuridica().getDescription());
     	//anagAziendaVO.setStrCCIAAnumeroREA(anagAziendaVO.getStrCCIAAnumeroREA());
   	}
   	catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("ERR_RICERCA_AZIENDA"));
     	errors.add("error", error);
     	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(url).forward(request, response);
     	return;
   	}
   	session.setAttribute("load","true");
   	
    if(anagAziendaVO.getTipoFormaGiuridica() != null 
      && anagAziendaVO.getTipoFormaGiuridica().getCode() != null) 
    {
      anagAziendaVO.setTipiFormaGiuridica(anagAziendaVO.getTipoFormaGiuridica().getCode().toString());
   	}
    
   	if(anagAziendaVO.getTipoTipologiaAzienda() != null 
      && anagAziendaVO.getTipoTipologiaAzienda().getCode() != null) 
    {
      anagAziendaVO.setTipiAzienda(anagAziendaVO.getTipoTipologiaAzienda().getCode().toString());
   	}
   	if(anagAziendaVO.getDelegaVO() != null) 
    {
      idTipoIntermediarioDelegato = anagAziendaVO.getDelegaVO().getIdIntermediario().toString();
     	anagAziendaVO.setIdIntermediarioDelegato(idTipoIntermediarioDelegato);
   	}
   	session.setAttribute("voAnagModifica",anagAziendaVO);
    
    if(anagAziendaVO.getTipiFormaGiuridica() != null) 
    {
      // Controllo che l'azienda selezionata abbia un CUAA
     	String isOk = "false";
     	if(!Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
      {
        isOk = "false";
       	session.setAttribute("isOk", isOk);
       	%>
          <jsp:forward page = "/view/modificaAziendaView.jsp" />
       	<%
     	}
     	// Se lo possiede verifico che se si tratta di un'azienda individuale o di persona che non esercita
     	// attività d'impresa il cuaa corrisponda ad un codice fiscale corretto.
     	else if(Long.decode(anagAziendaVO.getTipiFormaGiuridica())
          .compareTo((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_INDIVIDUALE")) == 0  
        || Long.decode(anagAziendaVO.getTipiFormaGiuridica())
          .compareTo((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_PERSONA_NO_ATTIVITA_IMPRESA")) == 0) 
      {
        if(!Validator.controlloCf(anagAziendaVO.getCUAA())) 
        {
          isOk = "false";
         	session.setAttribute("isOk", isOk);
         	%>
            <jsp:forward page = "/view/modificaAziendaView.jsp" />
         	<%
       	}
       	// Se è corretto allora controllo che, se il titolare dell'azienda possiede un codice fiscale, sia
       	// uguale al CUAA dell'azienda
       	PersonaFisicaVO personaTitolareVO = null;
       	try 
        {
          personaTitolareVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
       	}
       	catch(SolmrException se) 
        {
          ValidationError error = new ValidationError(se.getMessage());
         	errors.add("error", error);
         	request.setAttribute("errors", errors);
         	request.getRequestDispatcher(url).forward(request, response);
         	return;
       	}
       	if(Validator.isNotEmpty(personaTitolareVO.getCodiceFiscale())) 
        {
          if(!anagAziendaVO.getCUAA().equalsIgnoreCase(personaTitolareVO.getCodiceFiscale())) 
          {
            isOk = "false";
           	session.setAttribute("isOk", isOk);
           	%>
              <jsp:forward page = "/view/modificaAziendaView.jsp" />
           	<%
          }
       	}
     	}
     	// Per tutte le altre forme giuridiche controllo che il cuaa corrisponda ad una partita iva corretta
     	else 
      {
        if(!Validator.controlloPIVA(anagAziendaVO.getPartitaIVA())) 
        {
          isOk = "false";
         	session.setAttribute("isOk", isOk);
         	%>
            <jsp:forward page = "/view/modificaAziendaView.jsp" />
         	<%
       	}
     	}
     	// Se passo tutti i cotrolli proseguo normalmente con la modifica
     	isOk = "true";
     	session.setAttribute("isOk", isOk);
     	%>
        <jsp:forward page = "/view/modificaAziendaView.jsp" />
     	<%
   	}
   	else 
    {
      session.setAttribute("isOk", "true");
     	%>
        <jsp:forward page = "/view/modificaAziendaView.jsp" />
     	<%
   	}
 	}

%>
