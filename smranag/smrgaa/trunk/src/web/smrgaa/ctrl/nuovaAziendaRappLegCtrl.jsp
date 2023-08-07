<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

  String iridePageName = "nuovaAziendaRappLegCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String url = "/view/nuovaAziendaRappLegView.jsp";
  String annullaUrl = "/view/nuovaAziendaAnagraficaView.jsp";
  String sedeUrl = "/view/nuovaAziendaSedeView.jsp";
  String richiestaModificaUrl = "/view/confirmModResidenzaView.jsp";
  String urlForm = "../layout/nuovaAziendaRappLeg.htm";
  ValidationErrors errors = new ValidationErrors();


  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	
	String radiobuttonAzienda = (String)request.getAttribute("radiobuttonAzienda");
	request.setAttribute("radiobuttonAzienda", radiobuttonAzienda);
	String cuaaProvenienza = (String)request.getAttribute("cuaaProvenienza");  
	request.setAttribute("cuaaProvenienza", cuaaProvenienza);
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("insAnagVO");
  	
  // L'utente ha premuto il tasto per la ricerca attraverso codiceFiscale
  if (request.getParameter("cercaCF") != null) 
  {
    session.removeAttribute("insPerFisVO");
    String codiceFiscale = request.getParameter("codiceFiscale");
    // Controllo che l'utente abbia inserito il codice fiscale per effettuare la ricerca
    if(codiceFiscale == null || codiceFiscale.equals("")) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("COD_FISC_OBBLIGATORIO"));
      errors.add("codiceFiscale", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // Se lo ha inserito controllo che sia valido
    else if(!Validator.controlloCf(codiceFiscale)) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("INSERT_CODFISC_CORRETTO"));
      errors.add("codiceFiscale", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // Se il codice fiscale è stato valorizzato allora effettuo la ricerca
    PersonaFisicaVO personaFisicaVO = null;
    try 
    {
      personaFisicaVO = anagFacadeClient.getPersonaFisica(codiceFiscale.toUpperCase());
    }
    catch(SolmrException se) 
    {
      session.setAttribute("erroreCF","erroreCF");
      session.setAttribute("codiceFiscaleRapp",codiceFiscale);
    }
    if(personaFisicaVO != null) {
      session.setAttribute("insPerFisVO", personaFisicaVO);
    }
    %>
       <jsp:forward page="<%= url %>"/>
    <%
  }
  // L'utente ha premuto il tasto avanti e proseguo con l'inserimento
  if (request.getParameter("avanti")!=null) 
  {
    PersonaFisicaVO persVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");
    // Prelevo i parametri
    String codiceFiscale = request.getParameter("codiceFiscale");
    String cognome = request.getParameter("cognome");
    String nome = request.getParameter("nome");
    String sesso = request.getParameter("sesso");

    // Controllo che l'utente abbia inserito il codice fiscale per effettuare la ricerca
    if(codiceFiscale == null || codiceFiscale.equals("")) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("ERR_GENERIC_CODICE_FISCALE_OBBLIGATORIO"));
      errors.add("codiceFiscale", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // Se lo ha inserito controllo che sia valido
    else if(!Validator.controlloCf(codiceFiscale)) 
    {
      ValidationError error = new ValidationError(""+AnagErrors.get("ERR_GENERIC_CODICE_FISCALE"));
      errors.add("codiceFiscale", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    Long idPersonaFisica = null;
    Long idSoggetto = null;
    boolean isNewPersonaFisica = true;
    try 
    {
      persVO = anagFacadeClient.getPersonaFisica(codiceFiscale.toUpperCase());
    }
    catch(SolmrException se) 
    {
    }
    if(persVO != null) 
    {
      if(!persVO.isNewPersonaFisica()) 
      {
        idPersonaFisica = persVO.getIdPersonaFisica();
        idSoggetto = persVO.getIdSoggetto();
        isNewPersonaFisica = false;
      }
    }
    else 
    {
      persVO = new PersonaFisicaVO();
      isNewPersonaFisica = true;
    }
    String dataNascita = request.getParameter("nascitaData");
    String luogoNascita = request.getParameter("descNascitaComune");
    String provinciaNascita = request.getParameter("nascitaProv");
    String cittaNascita = request.getParameter("cittaNascita");
    String nascitaStatoEstero = request.getParameter("nascitaStatoEstero");
    session.setAttribute("nascitaStatoEstero",nascitaStatoEstero);
    String istatEsteroNascita = request.getParameter("provinciaN");
    String indirizzoResidenza = request.getParameter("resIndirizzo");
    String provinciaResidenza = request.getParameter("resProvincia");
    String comuneResidenza = request.getParameter("descResComune");
    String capResidenza = request.getParameter("resCAP");
    String statoEsteroResidenza = request.getParameter("descStatoEsteroResidenza");
    String cittaResidenza = request.getParameter("cittaResidenza");
    String telefonoResidenza = request.getParameter("resTelefono");
    String numeroCellulare = request.getParameter("numeroCellulare");
    String faxResidenza = request.getParameter("resFax");
    String eMailResidenza = request.getParameter("resMail");
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
    String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    // Informazioni aggiuntive relative al titolo di studio
    String titoloStudio = request.getParameter("idTitoloStudio");
    String indirizzoStudio = request.getParameter("idIndirizzoStudio");
    // Setto i parametri al value object
    PersonaFisicaVO personaFisicaVO = new PersonaFisicaVO();
    personaFisicaVO.setIdPersonaFisica(idPersonaFisica);
    personaFisicaVO.setIdSoggetto(idSoggetto);
    personaFisicaVO.setCodiceFiscale(codiceFiscale);
    personaFisicaVO.setCognome(cognome);
    personaFisicaVO.setNome(nome);
    personaFisicaVO.setSesso(sesso);
    if(!dataNascita.equals("")) 
    {
      if(Validator.validateDateF(dataNascita)) 
      {
        personaFisicaVO.setNascitaData(DateUtils.parseDate(dataNascita));
      }
    }
    personaFisicaVO.setStrNascitaData(dataNascita);
    personaFisicaVO.setLuogoNascita(luogoNascita);
    personaFisicaVO.setDescNascitaComune(luogoNascita);
    personaFisicaVO.setNascitaProv(provinciaNascita);
    personaFisicaVO.setIstatEsteroNascita(istatEsteroNascita);
    personaFisicaVO.setNascitaStatoEstero(nascitaStatoEstero);
    personaFisicaVO.setCittaNascita(cittaNascita);
    personaFisicaVO.setResIndirizzo(indirizzoResidenza);
    personaFisicaVO.setResProvincia(provinciaResidenza);
    personaFisicaVO.setDescResComune(comuneResidenza);
    personaFisicaVO.setResCAP(capResidenza);

    personaFisicaVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    personaFisicaVO.setCittaResidenza(cittaResidenza);
    personaFisicaVO.setResCittaEstero(cittaResidenza);

    personaFisicaVO.setResTelefono(telefonoResidenza);
    personaFisicaVO.setNumeroCellulare(numeroCellulare);
    personaFisicaVO.setResFax(faxResidenza);
    personaFisicaVO.setResMail(eMailResidenza);
    personaFisicaVO.setDomIndirizzo(indirizzoDomicilio);
    personaFisicaVO.setDomComune(comuneDomicilio);
    personaFisicaVO.setDomCAP(capDomicilio);
    personaFisicaVO.setDomProvincia(provinciaDomicilio);
    personaFisicaVO.setDomicilioStatoEstero(statoEsteroDomicilio);
    personaFisicaVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);
    personaFisicaVO.setNewPersonaFisica(isNewPersonaFisica);
    Long idTitoloStudio = null;
    if(titoloStudio != null && !titoloStudio.equals("")) {
      idTitoloStudio = Long.decode(titoloStudio);
    }
    personaFisicaVO.setIdTitoloStudio(idTitoloStudio);
    Long idIndirizzoStudio = null;
    if(indirizzoStudio != null && !indirizzoStudio.equals("")) {
      idIndirizzoStudio = Long.decode(indirizzoStudio);
    }
    personaFisicaVO.setIdIndirizzoStudio(idIndirizzoStudio);
    session.setAttribute("insPerFisVO",personaFisicaVO);
    // Effettuo i controlli sulla validità dei dati inseriti dall'utente.
    errors = personaFisicaVO.validateNuovoRappresentanteLegale();

    // Se il titolo di studio prevede l'indirizzo, quest'ultimo è obbligatorio
    Vector indirizzi = null;
    try {
      if(personaFisicaVO.getIdTitoloStudio() != null) {
        indirizzi = anagFacadeClient.getIndirizzoStudioByTitolo(personaFisicaVO.getIdTitoloStudio());
      }
    }
    catch(SolmrException se) {
    }

    if(indirizzi != null) {
      if(indirizzi.size() > 0 && personaFisicaVO.getIdTitoloStudio() != null) {
        if(!Validator.isNotEmpty(personaFisicaVO.getIdIndirizzoStudio())) {
          errors.add("idIndirizzoStudio",new ValidationError((String)AnagErrors.get("ERR_INDIRIZZO_STUDIO_OBBLIGATORIO")));
        }
      }
    }

    String codiceFiscaleComuneNascita = request.getParameter("codiceFiscaleComune");

    try {
      if(luogoNascita != null && !luogoNascita.equals("") && provinciaNascita != null && !provinciaNascita.equals("")) {
        codiceFiscaleComuneNascita = anagFacadeClient.ricercaCodiceFiscaleComune(luogoNascita,provinciaNascita);
      }
      else {
        codiceFiscaleComuneNascita = anagFacadeClient.ricercaCodiceFiscaleComune(nascitaStatoEstero,"");
      }
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("descNascitaComune", error);
    }

    String istatComuneNascita = "";
    if(luogoNascita != null && !luogoNascita.equals("") && provinciaNascita != null && !provinciaNascita.equals("")) {
      try {
        istatComuneNascita = anagFacadeClient.ricercaCodiceComune(luogoNascita,provinciaNascita);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("descNascitaComune", error);
      }
      personaFisicaVO.setNascitaComune(istatComuneNascita);
    }

    String istatStatoEsteroNascita = null;
    if(nascitaStatoEstero != null && !nascitaStatoEstero.equals("")) {
      try {
        istatStatoEsteroNascita = anagFacadeClient.ricercaCodiceComune(nascitaStatoEstero,"");
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("nascitaStatoEstero", error);
      }
    }
    personaFisicaVO.setNascitaStatoEstero(istatStatoEsteroNascita);

    String istatComuneResidenza = request.getParameter("resComune");
    if(provinciaResidenza != null && !provinciaResidenza.equals("") || comuneResidenza != null && !comuneResidenza.equals("")) {
      try {
        istatComuneResidenza = anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneResidenza,provinciaResidenza);
        personaFisicaVO.setResComune(istatComuneResidenza);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("descResComune",error);
      }
    }
    if(statoEsteroResidenza != null && !statoEsteroResidenza.equals("")) {
      try {
        istatComuneResidenza = anagFacadeClient.ricercaCodiceComune(statoEsteroResidenza,"");
        personaFisicaVO.setResComune(istatComuneResidenza);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(""+AnagErrors.get("ERR_STATO_ESTERO_RESIDENZA_TITOLARE_ERRATO"));
        errors.add("descStatoEsteroResidenza",error);
      }
    }

    // Controllo che l'utente abbia inserito un valore corretto relativo al domicilio
    String istatComuneDomicilio = null;
    if((comuneDomicilio != null && !comuneDomicilio.equals("")) || (provinciaDomicilio != null && !provinciaDomicilio.equals(""))) {
      try {
        istatComuneDomicilio = anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneDomicilio,provinciaDomicilio);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("domComune", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
    if(Validator.isNotEmpty(statoEsteroDomicilio)) {
      try {
        istatComuneDomicilio = anagFacadeClient.ricercaCodiceComuneNonEstinto(statoEsteroDomicilio ,"");
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
        errors.add("domicilioStatoEstero", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
    personaFisicaVO.setIstatComuneDomicilio(istatComuneDomicilio);

    if(errors != null && errors.size() != 0) {
      request.setAttribute("errors", errors);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    session.removeAttribute("insPerFisVO");
    session.setAttribute("insPerFisVO",personaFisicaVO);
    String indietro = (String)session.getAttribute("indietro");
    // Se tutti i controlli sono stati superati proseguo e vado ad inserire i dati della sede legale
    // e dal momento che devo proporre di default i dati relativi alla redidenza del titolare setto il VO
    if(indietro == null || indietro.equals("")) 
    {
      if(anagAziendaVO.getTipiFormaGiuridica().compareTo(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_INDIVIDUALE")).toString()) == 0 ||
         anagAziendaVO.getTipiFormaGiuridica().compareTo(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_PERSONA_NO_ESERCITA")).toString()) == 0 ||
         anagAziendaVO.getTipiFormaGiuridica().compareTo(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_SOCIETA_SEMPLICE")).toString()) == 0) 
      {
        anagAziendaVO.setSedelegIndirizzo(personaFisicaVO.getResIndirizzo());
        anagAziendaVO.setSedelegProv(personaFisicaVO.getResProvincia());
        anagAziendaVO.setSedelegComune(personaFisicaVO.getDescResComune());
        anagAziendaVO.setCodiceComune(personaFisicaVO.getResComune());
        anagAziendaVO.setSedelegCAP(personaFisicaVO.getResCAP());
        anagAziendaVO.setSedelegEstero(personaFisicaVO.getDescStatoEsteroResidenza());
        anagAziendaVO.setStatoEstero(personaFisicaVO.getStatoEsteroRes());
      }
    }

    try 
    {
      Validator.verificaCf(personaFisicaVO.getNome(),personaFisicaVO.getCognome(),
                           personaFisicaVO.getSesso(),personaFisicaVO.getNascitaData(),
                           codiceFiscaleComuneNascita, personaFisicaVO.getCodiceFiscale());
    }
    catch(CodiceFiscaleException ce) 
    {
      if(ce.getNome()) 
      {
        errors.add("nome",new ValidationError(""+AnagErrors.get("ERR_NOME_CODICE_FISCALE")));
      }
      if(ce.getCognome()) 
      {
        errors.add("cognome",new ValidationError(""+AnagErrors.get("ERR_COGNOME_CODICE_FISCALE")));
      }
      if(ce.getAnnoNascita()) 
      {
        errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_ANNO_NASCITA_CODICE_FISCALE")));
      }
      if(ce.getMeseNascita()) 
      {
        errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_MESE_NASCITA_CODICE_FISCALE")));
      }
      if(ce.getGiornoNascita()) 
      {
        errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_GIORNO_NASCITA_CODICE_FISCALE")));
      }
      if(ce.getComuneNascita()) 
      {
        errors.add("descNascitaComune",new ValidationError(""+AnagErrors.get("ERR_COMUNE_NASCITA_CODICE_FISCALE")));
      }
      if(ce.getSesso()) 
      {
        errors.add("sesso",new ValidationError(""+AnagErrors.get("ERR_SESSO_CODICE_FISCALE")));
      }
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    // Se per il codice fiscale inserito dall'utente esiste già una persona su DB controllo che non siano
    // cambiati i dati relativi alla residenza
    PersonaFisicaVO oldPersonaFisicaVO = null;
    if(!personaFisicaVO.isNewPersonaFisica()) 
    {
      // recupero la persona fisica su DB
      try 
      {
        oldPersonaFisicaVO = anagFacadeClient.getPersonaFisica(personaFisicaVO.getCodiceFiscale().toUpperCase());
      }
      catch(SolmrException se) 
      {}
    }
    // Se i dati della residenza sono cambiati mando l'utente alla pagina di richiesta di modifica o storicizzazione
    // della persona fisica
    if((oldPersonaFisicaVO != null)
      && Validator.isNotEmpty(oldPersonaFisicaVO.getDataInizioResidenza()))
    {
      // Se la data di inizio  residenza è diversa da quella di sistema allora confronto la persona
      // fisica prelevata dal DB con la nuova persona fisica derivante dai nuovi dati inseriti come input
      // dall'utente.
      if(oldPersonaFisicaVO.getDataInizioResidenza().compareTo(DateUtils.parseDate(DateUtils.getCurrent())) != 0) 
      {
        if(!personaFisicaVO.equalsResidenza(oldPersonaFisicaVO)) 
        {
          request.setAttribute("urlForm", urlForm);
          %>
            <jsp:forward page="<%= richiestaModificaUrl %>"/>
          <%
        }
      }
    }

    %>
      <jsp:forward page="<%= sedeUrl %>"/>
    <%
  }
  // L'utente ha premuto il tasto annulla
  if(request.getParameter("indietro") != null) 
  {
    PersonaFisicaVO persVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");
    String codiceFiscale = request.getParameter("codiceFiscale");
    String cognome = request.getParameter("cognome");
    String nome = request.getParameter("nome");
    String sesso = request.getParameter("sesso");
    String dataNascita = request.getParameter("nascitaData");
    String luogoNascita = request.getParameter("descNascitaComune");
    String provinciaNascita = request.getParameter("nascitaProv");
    String indirizzoResidenza = request.getParameter("resIndirizzo");
    String provinciaResidenza = request.getParameter("resProvincia");
    String comuneResidenza = request.getParameter("descResComune");
    String capResidenza = request.getParameter("resCAP");
    String statoEsteroResidenza = request.getParameter("descStatoEsteroResidenza");
    String telefonoResidenza = request.getParameter("resTelefono");
    String numeroCellulare = request.getParameter("numeroCellulare");
    String faxResidenza = request.getParameter("resFax");
    String eMailResidenza = request.getParameter("resMail");
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
    String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    // Informazioni aggiuntive relative al titolo di studio
    String titoloStudio = request.getParameter("idTitoloStudio");
    String indirizzoStudio = request.getParameter("idIndirizzoStudio");

    Long idPersonaFisica = null;
    Long idSoggetto = null;
    if(persVO != null) {
      idPersonaFisica = persVO.getIdPersonaFisica();
      idSoggetto = persVO.getIdSoggetto();
    }
    else {
      persVO = new PersonaFisicaVO();
    }
    if(codiceFiscale != null && !codiceFiscale.equals("")) {
      persVO.setCodiceFiscale(codiceFiscale.toUpperCase());
    }
    persVO.setIdPersonaFisica(idPersonaFisica);
    persVO.setIdSoggetto(idSoggetto);
    persVO.setCognome(cognome);
    persVO.setNome(nome);
    persVO.setSesso(sesso);
    if(!dataNascita.equals("")) {
      persVO.setNascitaData(DateUtils.parseDate(dataNascita));
    }
    persVO.setStrNascitaData(dataNascita);
    persVO.setLuogoNascita(luogoNascita);
    persVO.setDescNascitaComune(luogoNascita);
    persVO.setNascitaProv(provinciaNascita);
    persVO.setResIndirizzo(indirizzoResidenza);
    persVO.setResProvincia(provinciaResidenza);
    persVO.setDescResComune(comuneResidenza);
    persVO.setResCAP(capResidenza);
    persVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    persVO.setResTelefono(telefonoResidenza);
    persVO.setNumeroCellulare(numeroCellulare);
    persVO.setResFax(faxResidenza);
    persVO.setResMail(eMailResidenza);
    persVO.setDomIndirizzo(indirizzoDomicilio);
    persVO.setDomComune(comuneDomicilio);
    persVO.setDomCAP(capDomicilio);
    persVO.setDomProvincia(provinciaDomicilio);
    persVO.setDomicilioStatoEstero(statoEsteroDomicilio);
    persVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);
    Long idTitoloStudio = null;
    if(titoloStudio != null && !titoloStudio.equals("")) {
      idTitoloStudio = Long.decode(titoloStudio);
    }
    persVO.setIdTitoloStudio(idTitoloStudio);
    Long idIndirizzoStudio = null;
    if(indirizzoStudio != null && !indirizzoStudio.equals("")) {
      idIndirizzoStudio = Long.decode(indirizzoStudio);
    }
    persVO.setIdIndirizzoStudio(idIndirizzoStudio);
    session.setAttribute("insPerFisVO",persVO);
    request.getRequestDispatcher(annullaUrl).forward(request, response);
    return;
  }

  // L'utente ha modificato il titolo di Studio
  if(request.getParameter("operazione") != null 
    && request.getParameter("operazione").equals("cambioTitoloStudio")) 
  {
    PersonaFisicaVO persVO = (PersonaFisicaVO)session.getAttribute("modPerFisVO");
    String codiceFiscale = request.getParameter("codiceFiscale");
    String cognome = request.getParameter("cognome");
    String nome = request.getParameter("nome");
    String sesso = request.getParameter("sesso");
    String dataNascita = request.getParameter("nascitaData");
    String luogoNascita = request.getParameter("descNascitaComune");
    String provinciaNascita = request.getParameter("nascitaProv");
    String cittaNascita = request.getParameter("cittaNascita");
    String nascitaStatoEstero = request.getParameter("nascitaStatoEstero");
    String indirizzoResidenza = request.getParameter("resIndirizzo");
    String provinciaResidenza = request.getParameter("resProvincia");
    String comuneResidenza = request.getParameter("descResComune");
    String cittaResidenza = request.getParameter("cittaResidenza");
    String capResidenza = request.getParameter("resCAP");
    String statoEsteroResidenza = request.getParameter("descStatoEsteroResidenza");
    String telefonoResidenza = request.getParameter("resTelefono");
    String numeroCellulare = request.getParameter("numeroCellulare");
    String faxResidenza = request.getParameter("resFax");
    String eMailResidenza = request.getParameter("resMail");
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
    String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    // Informazioni aggiuntive relative al titolo di studio
    String titoloStudio = request.getParameter("idTitoloStudio");

    Long idPersonaFisica = null;
    Long idSoggetto = null;
    if(persVO != null) {
      idPersonaFisica = persVO.getIdPersonaFisica();
      idSoggetto = persVO.getIdSoggetto();
    }

    // Setto i parametri al value object
    PersonaFisicaVO personaFisicaVO = new PersonaFisicaVO();
    personaFisicaVO.setIdPersonaFisica(idPersonaFisica);
    personaFisicaVO.setIdSoggetto(idSoggetto);
    personaFisicaVO.setCodiceFiscale(codiceFiscale);
    personaFisicaVO.setCognome(cognome);
    personaFisicaVO.setNome(nome);
    personaFisicaVO.setSesso(sesso);
    if(!dataNascita.equals("")) {
      personaFisicaVO.setNascitaData(DateUtils.parseDate(dataNascita));
    }
    personaFisicaVO.setStrNascitaData(dataNascita);
    personaFisicaVO.setLuogoNascita(luogoNascita);
    personaFisicaVO.setDescNascitaComune(luogoNascita);
    personaFisicaVO.setNascitaProv(provinciaNascita);
    personaFisicaVO.setNascitaStatoEstero(nascitaStatoEstero);
    personaFisicaVO.setCittaNascita(cittaNascita);
    personaFisicaVO.setResIndirizzo(indirizzoResidenza);
    personaFisicaVO.setResProvincia(provinciaResidenza);
    personaFisicaVO.setDescResComune(comuneResidenza);
    personaFisicaVO.setResCAP(capResidenza);
    personaFisicaVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    personaFisicaVO.setStatoEsteroRes(statoEsteroResidenza);
    personaFisicaVO.setCittaResidenza(cittaResidenza);
    personaFisicaVO.setResTelefono(telefonoResidenza);
    personaFisicaVO.setNumeroCellulare(numeroCellulare);
    personaFisicaVO.setResFax(faxResidenza);
    personaFisicaVO.setResMail(eMailResidenza);
    personaFisicaVO.setDomIndirizzo(indirizzoDomicilio);
    personaFisicaVO.setDomComune(comuneDomicilio);
    personaFisicaVO.setDomCAP(capDomicilio);
    personaFisicaVO.setDomProvincia(provinciaDomicilio);
    personaFisicaVO.setDomicilioStatoEstero(statoEsteroDomicilio);
    personaFisicaVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);
    Long idTitoloStudio = null;
    if(titoloStudio != null && !titoloStudio.equals("")) {
      idTitoloStudio = Long.decode(titoloStudio);
    }
    personaFisicaVO.setIdTitoloStudio(idTitoloStudio);
    session.setAttribute("insPerFisVO",personaFisicaVO);
    %>
      <jsp:forward page= "<%= url %>" />
    <%
  }
  // Arrivo dalla pagina di richiesta modifica/storicizzazione della persona fisica
  // L'utente ha scelto di modificare la persona fisica
  if(request.getParameter("operazione") != null 
    && request.getParameter("operazione").equals("modificaResidenzaSoggetto")) 
  {
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");
    personaFisicaVO.setIsModificaResidenza(true);
    personaFisicaVO.setIsStoricizzaResidenza(false);
    %>
      <jsp:forward page="<%= sedeUrl %>"/>
    <%
  }
  // L'utente ha scelto di storicizzare la persona fisica
  if(request.getParameter("operazione") != null 
    && request.getParameter("operazione").equals("storicizzaResidenzaSoggetto")) 
  {
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("insPerFisVO");
    personaFisicaVO.setIsModificaResidenza(false);
    personaFisicaVO.setIsStoricizzaResidenza(true);
    %>
      <jsp:forward page="<%= sedeUrl %>"/>
    <%
  }
  // l'utente ha selezionato il pulsante indietro dalla pagina di richiesta modifica/storicizzazione
  // della persona fisica
  if(request.getParameter("indietroModificaResidenza") != null) 
  {
    %>
      <jsp:forward page="<%= url %>"/>
    <%
  }
%>
