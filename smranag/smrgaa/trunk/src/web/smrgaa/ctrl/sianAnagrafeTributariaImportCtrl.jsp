<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.anag.services.SianAnagTributariaGaaVO" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
   String iridePageName = "sianAnagrafeTributariaImportCtrl.jsp";
   %>
      <%@include file = "/include/autorizzazione.inc" %>
   <%


   String sianAnagrafeTributariaUrl = "/view/sianAnagrafeTributariaView.jsp";
   String anagraficaUrl = "/view/anagraficaView.jsp";
   
   String actionUrl = "../layout/anagrafica.htm";
   String erroreViewUrl = "/view/erroreView.jsp";
  
   final String errMsg = "Impossibile procedere nella sezione importa dati da anagrafe tributaria."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   String messaggioErrore = null;
   ValidationErrors errors = new ValidationErrors();
   AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

   
   boolean isCUAAChecked = request.getParameter("CUAA") != null;
   boolean isPartitaIvaChecked = request.getParameter("partitaIVA") != null;
   boolean isDenominazioneChecked = request.getParameter("denominazione") != null;
   boolean isAtecoPrincChecked = request.getParameter("idAtecoPrincipale") != null;
   boolean isAtecoSecChecked = request.getParameter("idAtecoSec") != null;
   boolean isSedelegIndirizzoChecked = request.getParameter("sedeLegaleIndirizzo") != null;
   boolean isCheckedComuneResidenza = request.getParameter("comuneResidenza")!= null;
   boolean isCheckedIndirizzoResidenza = request.getParameter("indirizzoResidenza")!= null;
   boolean isCheckedCapResidenza = request.getParameter("capResidenza")!= null;
   boolean isCheckedComuneSedeLegale = request.getParameter("comuneSedeLegale")!= null;
   boolean isCheckedSedeLegaleCAP = request.getParameter("sedeLegaleCAP")!= null;
   boolean isCheckedComuneDomicilioFiscale = request.getParameter("comuneDomicilioFiscale")!= null;
   boolean isCheckedDomicilioIndirizzo = request.getParameter("domicilioIndirizzo")!= null;
   boolean isCheckedDomicilioCAP = request.getParameter("domicilioCAP")!= null;


   String cuaaSian = null;
   String partitaIVASian = null;
   String denominazioneSian = null;
   String sedelegIndirizzoSian = null;
   String istatResidenzaSian=(String)request.getParameter("istatResidenzaTributaria");
   String istatSedeLegaleSian=(String)request.getParameter("istatSedeLegaleTributaria");
   String istatDomicilioFiscaleSian=(String)request.getParameter("istatDomicilioFiscaleTributaria");

   boolean isCUAAChanged = false;
   boolean isPartivaChanged = false;
   boolean isDenominazioneChanged = false;
   boolean isAtecoPrincChanged = false;
   boolean isAtecoSecChanged = false;
   boolean isSedelegIndirizzoChanged = false;
   boolean isIstatResidenzaChanged = false;
   boolean isIndResidenzaChanged = false;
   boolean isCapResidenzaChanged = false;
   boolean isComuneSedeLegaleChanged = false;
   boolean isSedeLegaleCAPChanged = false;
   boolean isComuneDomicilioFiscaleChanged = false;
   boolean isDomicilioIndirizzoChanged = false;
   boolean isDomicilioCAPChanged = false;

   SianAnagTributariaGaaVO anagTrib = (SianAnagTributariaGaaVO)session.getAttribute("anagTrib");
   Vector<AziendaAtecoSecVO> vAziendaAtecoSec = (Vector<AziendaAtecoSecVO>)session.getAttribute("vAziendaAtecoSec");
   String indResidenzaSian = anagTrib.getIndirizzoResidenza();
   String capResidenzaSian = anagTrib.getCapResidenza();
   String sedeLegaleCAP = anagTrib.getCapSedeLegale();
   String domicilioIndirizzo = anagTrib.getIndirizzoDomicilioFiscale();
   String domicilioCAP = anagTrib.getCapDomicilioFiscale();



   PersonaFisicaVO personaFisicaVO = null;
   PersonaFisicaVO personaFisicaVOTributaria = new PersonaFisicaVO();
   try 
   {
     personaFisicaVO = anagFacadeClient.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
   }
   catch(SolmrException se) 
   {
     messaggioErrore = se.getMessage();
     request.setAttribute("messaggioErrore", messaggioErrore);
     %>
        <jsp:forward page="<%= sianAnagrafeTributariaUrl %>"/>
     <%
   }
   request.setAttribute("personaFisicaVO", personaFisicaVO);

   // Controllo che l'utente abbia selezionato almeno un valore
   // da importare
   if(!isCUAAChecked && !isPartitaIvaChecked && !isDenominazioneChecked 
      && !isAtecoPrincChecked && !isAtecoSecChecked && !isSedelegIndirizzoChecked
      && !isCheckedComuneResidenza && !isCheckedIndirizzoResidenza && !isCheckedCapResidenza
      && !isCheckedComuneSedeLegale && !isCheckedSedeLegaleCAP && !isCheckedComuneDomicilioFiscale
      && !isCheckedDomicilioIndirizzo && !isCheckedDomicilioCAP )
   {
     ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATI_TRIBUTI_NON_SELEZIONATI"));
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
     return;
   }

   
   //Carico l'oggetto personaFisicaVOTributaria per essere eventualmente usato
   //nel caso si sta cambiando il codice fiscale ad una azienda con forma giuridica
   //ditta individuale o persona fisica no impresa
   personaFisicaVOTributaria.setCodiceFiscale(request.getParameter("CUAATributaria"));
   personaFisicaVOTributaria.setCognome(request.getParameter("cognomeTributaria"));
   personaFisicaVOTributaria.setNome(request.getParameter("nomeTributaria"));
   personaFisicaVOTributaria.setSesso(request.getParameter("sessoTributaria"));
   personaFisicaVOTributaria.setNascitaComune(request.getParameter("istatNascitaTributaria"));
   
   String dataNascitaTributaria = request.getParameter("dataNascitaTributaria");
   if(dataNascitaTributaria != null)
   {
     String pattern = "dd/MM/yyyy";
     java.util.Date d;     
     SimpleDateFormat sdf = new SimpleDateFormat(pattern);
     try 
     {
       d = new java.sql.Date(sdf.parse(dataNascitaTributaria).getTime());
     }
     catch (Exception e) {
      d = null;
     }       
     personaFisicaVOTributaria.setNascitaData(d);
   }
   
   personaFisicaVOTributaria.setResIndirizzo(request.getParameter("indirizzoResidenzaTributaria"));   
   personaFisicaVOTributaria.setResComune(request.getParameter("istatResidenzaTributaria"));
   personaFisicaVOTributaria.setResCAP(request.getParameter("capResidenzaTributaria"));
   personaFisicaVOTributaria.setDomComune(request.getParameter("istatDomicilioFiscaleTributaria"));
   personaFisicaVOTributaria.setDomIndirizzo(request.getParameter("domicilioIndirizzoTributaria"));
   personaFisicaVOTributaria.setDomCAP(request.getParameter("domicilioCAPTributaria"));
   
   // Controllo che i valori eventualmente selezionati siano
   // diversi da null e quindi importabili e verifico se sono
   // diversi da quelli in nostro possesso
   if(isCUAAChecked) 
   {
     request.setAttribute("isCUAAChecked", new Boolean(isCUAAChecked));
     cuaaSian = request.getParameter("CUAATributaria");
     if(!Validator.isNotEmpty(cuaaSian)) {
       ValidationError error = new ValidationError((String)AnagErrors.get("ERR_CUAA_SIAN_NON_VALORIZZATO_IMPORTA_DATI"));
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!cuaaSian.equalsIgnoreCase(anagAziendaVO.getCUAA())) {
         isCUAAChanged = true;
         anagAziendaVO.setCUAA(cuaaSian);
       }
     }
   }
   
   if(isPartitaIvaChecked) {
     request.setAttribute("isPartitaIvaChecked", new Boolean(isPartitaIvaChecked));
     partitaIVASian = request.getParameter("partitaIVATributaria");
     if(!Validator.isNotEmpty(partitaIVASian)) {
       ValidationError error = new ValidationError((String)AnagErrors.get("ERR_PARTITA_IVA_SIAN_NON_VALORIZZATA"));
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!partitaIVASian.equalsIgnoreCase(anagAziendaVO.getPartitaIVA())) {
         isPartivaChanged = true;
         anagAziendaVO.setPartitaIVA(partitaIVASian);
       }
     }
   }
   if(isAtecoPrincChecked) 
   {
     request.setAttribute("isAtecoPrincChecked", new Boolean(isAtecoPrincChecked));
     if(Validator.isEmpty(anagTrib.getIdAttivitaAteco())) 
     {
       ValidationError error = new ValidationError(AnagErrors.ERR_ATECO_PRINC_SIAN_NON_VALORIZZATO);
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else 
     {
       if(Validator.isEmpty(anagAziendaVO.getIdAteco()) 
         || (Validator.isNotEmpty(anagAziendaVO.getIdAteco()) && anagTrib.getIdAttivitaAteco().compareTo(new Long(anagAziendaVO.getIdAteco())) != 0)) 
       {
         isAtecoPrincChanged = true;
         anagAziendaVO.setIdAteco(anagTrib.getIdAttivitaAteco().toString());
         
         CodeDescription tipoAttivitaATECO = new CodeDescription();
         tipoAttivitaATECO.setCode(new Integer(anagTrib.getIdAttivitaAteco().intValue()));
         anagAziendaVO.setTipoAttivitaATECO(tipoAttivitaATECO);
       }
     }
   }
   
  Vector<Long> vIdAtecoTrib = null;
  if(isAtecoSecChecked) 
  {
    request.setAttribute("isAtecoSecChecked", new Boolean(isAtecoSecChecked));
    if(Validator.isEmpty(anagTrib.getvAtecoSecSian())) 
    {
      ValidationError error = new ValidationError(AnagErrors.ERR_ATECO_SEC_SIAN_NON_VALORIZZATO);
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
      return;
    }
    else 
    {
      boolean aggiornaAtecoSec = false;
      
      for(int i=0;i<anagTrib.getvAtecoSecSian().size();i++)
      {
        if(vIdAtecoTrib == null)
          vIdAtecoTrib = new Vector<Long>();
        
        vIdAtecoTrib.add(anagTrib.getvAtecoSecSian().get(i).getIdAttivitaAteco());
      }
         
      if(Validator.isEmpty(vAziendaAtecoSec))
      {      
        isAtecoSecChanged = true;
      }
      else
      {       
	       boolean trovato = true;
	       for(int i=0;i<vAziendaAtecoSec.size();i++)
	       {
	         if(!vIdAtecoTrib.contains(new Long(vAziendaAtecoSec.get(i).getIdAttivitaAteco())))
	         {
	           trovato = false;
	           break;
	         }
	       }
	       
	       if(!trovato)
           isAtecoSecChanged = true;
      }
       
    }
  }
   
   if(isDenominazioneChecked) {
     request.setAttribute("isDenominazioneChecked", new Boolean(isDenominazioneChecked));
     denominazioneSian = request.getParameter("denominazioneTributaria");
     if(!Validator.isNotEmpty(denominazioneSian)) {
       ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DENOMINAZIONE_SIAN_NON_VALORIZZATA"));
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!denominazioneSian.equalsIgnoreCase(anagAziendaVO.getDenominazione())) {
         isDenominazioneChanged = true;
         anagAziendaVO.setDenominazione(denominazioneSian);
       }
     }
   }
   if(isSedelegIndirizzoChecked) {
     request.setAttribute("isSedelegIndirizzoChecked", new Boolean(isSedelegIndirizzoChecked));
     sedelegIndirizzoSian = request.getParameter("sedeLegaleIndirizzoTributaria");
     if(!Validator.isNotEmpty(sedelegIndirizzoSian)) {
       ValidationError error = new ValidationError((String)AnagErrors.get("ERR_SEDELEG_INDIRIZZO_SIAN_NON_VALORIZZATA"));
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!sedelegIndirizzoSian.equalsIgnoreCase(anagAziendaVO.getSedelegIndirizzo())) {
         isSedelegIndirizzoChanged = true;
         anagAziendaVO.setSedelegIndirizzo(sedelegIndirizzoSian);
       }
     }
   }

   if(isCheckedComuneResidenza) {
     request.setAttribute("isCheckedComuneResidenza", new Boolean(isCheckedComuneResidenza));
       if(!istatResidenzaSian.equalsIgnoreCase(personaFisicaVO.getResComune())) {
         isIstatResidenzaChanged = true;
         personaFisicaVO.setResComune(istatResidenzaSian);
       }
   }
   if(isCheckedIndirizzoResidenza) {
     request.setAttribute("isCheckedIndirizzoResidenza", new Boolean(isCheckedIndirizzoResidenza));
     if(!Validator.isNotEmpty(indResidenzaSian)) {
       ValidationError error = new ValidationError(AnagErrors.ERR_RESIDENZA_INDIRIZZO_SIAN_NON_VALORIZZATO);
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!indResidenzaSian.equalsIgnoreCase(personaFisicaVO.getResIndirizzo())) {
         isIndResidenzaChanged = true;
         personaFisicaVO.setResIndirizzo(indResidenzaSian);
       }
     }
   }
   if(isCheckedCapResidenza) {
     request.setAttribute("isCheckedCapResidenza", new Boolean(isCheckedCapResidenza));
     if(!Validator.isNotEmpty(capResidenzaSian)) {
       ValidationError error = new ValidationError(AnagErrors.ERR_RESIDENZA_CAP_SIAN_NON_VALORIZZATO);
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!capResidenzaSian.equalsIgnoreCase(personaFisicaVO.getResCAP())) {
         isCapResidenzaChanged = true;
         personaFisicaVO.setResCAP(capResidenzaSian);
       }
     }
   }

   if(isCheckedSedeLegaleCAP) {
     request.setAttribute("isCheckedSedeLegaleCAP", new Boolean(isCheckedSedeLegaleCAP));
     if(!Validator.isNotEmpty(sedeLegaleCAP)) {
       ValidationError error = new ValidationError(AnagErrors.ERR_SEDE_LEGALE_CAP_SIAN_NON_VALORIZZATO);
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!sedeLegaleCAP.equalsIgnoreCase(anagAziendaVO.getSedelegCAP())) {
         isSedeLegaleCAPChanged = true;
         anagAziendaVO.setSedelegCAP(sedeLegaleCAP);
       }
     }
   }

   if(isCheckedDomicilioCAP) {
     request.setAttribute("isCheckedDomicilioCAP", new Boolean(isCheckedDomicilioCAP));
     if(!Validator.isNotEmpty(domicilioCAP)) {
       ValidationError error = new ValidationError(AnagErrors.ERR_DOMICILIO_CAP_SIAN_NON_VALORIZZATO);
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!domicilioCAP.equalsIgnoreCase(personaFisicaVO.getDomCAP())) {
         isDomicilioCAPChanged = true;
         personaFisicaVO.setDomCAP(domicilioCAP);
       }
     }
   }


   if(isCheckedDomicilioIndirizzo) {
     request.setAttribute("isCheckedDomicilioIndirizzo", new Boolean(isCheckedDomicilioIndirizzo));
     if(!Validator.isNotEmpty(domicilioIndirizzo)) {
       ValidationError error = new ValidationError(AnagErrors.ERR_DOMICILIO_INDIRIZZO_SIAN_NON_VALORIZZATO);
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(sianAnagrafeTributariaUrl).forward(request, response);
       return;
     }
     else {
       if(!domicilioIndirizzo.equalsIgnoreCase(personaFisicaVO.getDomIndirizzo())) {
         isDomicilioIndirizzoChanged = true;
         personaFisicaVO.setDomIndirizzo(domicilioIndirizzo);
       }
     }
   }

   if(isCheckedComuneSedeLegale) {
     request.setAttribute("isCheckedComuneSedeLegale", new Boolean(isCheckedComuneSedeLegale));
       if(!istatSedeLegaleSian.equalsIgnoreCase(anagAziendaVO.getSedelegComune())) {
         isComuneSedeLegaleChanged = true;
         anagAziendaVO.setSedelegComune(istatSedeLegaleSian);
       }
   }

   if(isCheckedComuneDomicilioFiscale) {
     request.setAttribute("isCheckedComuneDomicilioFiscale", new Boolean(isCheckedComuneDomicilioFiscale));
       if(!istatDomicilioFiscaleSian.equalsIgnoreCase(personaFisicaVO.getIstatComuneDomicilio())) {
         isComuneDomicilioFiscaleChanged = true;
         personaFisicaVO.setIstatComuneDomicilio(istatDomicilioFiscaleSian);
       }
   }


   // Se qualcosa è stato modificato procedo con la storicizzazione
   if(isCUAAChanged || isPartivaChanged || isAtecoPrincChanged || isAtecoSecChanged
   || isDenominazioneChanged || isSedelegIndirizzoChanged
   || isIstatResidenzaChanged || isIndResidenzaChanged || isCapResidenzaChanged
   || isComuneSedeLegaleChanged || isSedeLegaleCAPChanged || isComuneDomicilioFiscaleChanged
   || isDomicilioIndirizzoChanged || isDomicilioCAPChanged ) 
   {
     // Setto il motivo della modifica
     anagAziendaVO.setMotivoModifica((String)SolmrConstants.get("SIAN_MOTIVO_MODIFICA"));
     //azzero vettore se fossero uguali..
     if(isAtecoSecChanged == false)
       vIdAtecoTrib = null;
     // Procedo con la storicizzazione
     try 
     {
       anagFacadeClient.updateAnagrafe(anagAziendaVO, ruoloUtenza.getIdUtente(), personaFisicaVO, isCUAAChanged, personaFisicaVOTributaria, vIdAtecoTrib);
     }
     catch(SolmrException se) 
     {       
       SolmrLogger.info(this, " - sianAnagrafeTributariaImportCtrl.jsp - FINE PAGINA");
	     String messaggio = errMsg+": "+se.toString();
	     request.setAttribute("messaggioErrore",messaggio);
	     request.setAttribute("pageBack", actionUrl);
	     %>
	       <jsp:forward page="<%= erroreViewUrl %>" />
	     <%
	     return;    
       
     }

     // Ricerco i dati dell'anagrafe in modo da presentare i dati aggiornati dopo
     // l'importazione dei dati dal SIAN
     try {
       boolean delegaAttiva=anagAziendaVO.isPossiedeDelegaAttiva();
       DelegaVO delega=anagAziendaVO.getDelegaVO();
       anagAziendaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAzienda());
       anagAziendaVO.setPossiedeDelegaAttiva(delegaAttiva);
       anagAziendaVO.setDelegaVO(delega);
     }
     catch(SolmrException se) {
       messaggioErrore = se.getMessage();
       request.setAttribute("messaggioErrore", messaggioErrore);
       %>
          <jsp:forward page="<%= sianAnagrafeTributariaUrl %>"/>
       <%
     }

     // Metto il nuovo oggetto in sessione
     session.setAttribute("anagAziendaVO", anagAziendaVO);

     // rimuovo l'oggetto del SIAN
     session.removeAttribute("anagTrib");
     // Torno alla pagina di dettaglio dell'anagrafe
     %>
        <jsp:forward page="<%= anagraficaUrl %>"/>
     <%
   }
   // Altrimenti torno alla pagina di dettaglio dell'anagrafe
   else {
     // rimuovo l'oggetto del SIAN
     session.removeAttribute("anagTrib");
     %>
        <jsp:forward page="<%= anagraficaUrl %>"/>
     <%
   }
%>

