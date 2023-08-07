<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "anagraficaIndividualePassaggioNuovoTitolareCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String url = "/view/anagraficaIndividualePassaggioNuovoTitolareView.jsp";
  String passaggioSedeUrl = "/view/anagraficaIndividualePassaggioSedeView.jsp";
  String indietroUrl = "/view/anagraficaIndividualePassaggioView.jsp";
  String urlForm = "../layout/anagraficaIndividualePassaggio_nuovoTitolare.htm";
  String richiestaModificaUrl = "/view/confirmModResidenzaView.jsp";


  AnagAziendaVO anagPassaggioVO = (AnagAziendaVO)session.getAttribute("anagPassaggioVO");
  PersonaFisicaVO personaTitolareVO = (PersonaFisicaVO)session.getAttribute("personaTitolareVO");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  // L'utente ha premuto il tasto avanti
  if(request.getParameter("avanti") != null) {
    // Recupero i parametri
    String codiceFiscale = anagPassaggioVO.getCUAA();
    String cognome = request.getParameter("cognome");
    String nome = request.getParameter("nome");
    String sesso = request.getParameter("sesso");
    String dataNascita = request.getParameter("strNascitaData");
    String provinciaNascita = request.getParameter("nascitaProv");
    String descrizioneLuogoNascita = request.getParameter("descNascitaComune");
    String statoEsteroNascita = request.getParameter("nascitaStatoEstero");
    String cittaEsteroNascita = request.getParameter("cittaNascita");
    String indirizzoResidenza = request.getParameter("resIndirizzo");
    String provinciaResidenza = request.getParameter("resProvincia");
    String comuneResidenza = request.getParameter("descResComune");
    String capResidenza = request.getParameter("resCAP");
    String statoEsteroResidenza = request.getParameter("statoEsteroRes");
    String cittaEsteroResidenza = request.getParameter("cittaResidenza");
    String telefono = request.getParameter("resTelefono");
    String fax = request.getParameter("resFax");
    String mail = request.getParameter("resMail");
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
    String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    // Informazioni aggiuntive relative al titolo di studio
    String titoloStudio = request.getParameter("idTitoloStudio");
    String indirizzoStudio = request.getParameter("idIndirizzoStudio");

    // Setto il value object
    personaTitolareVO.setCodiceFiscale(codiceFiscale);
    personaTitolareVO.setCognome(cognome);
    personaTitolareVO.setNome(nome);
    personaTitolareVO.setSesso(sesso);
    personaTitolareVO.setStrNascitaData(dataNascita);
    personaTitolareVO.setDescNascitaComune(descrizioneLuogoNascita);
    personaTitolareVO.setNascitaStatoEstero(statoEsteroNascita);
    if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("")) {
      personaTitolareVO.setNascitaProv(provinciaNascita);
      personaTitolareVO.setLuogoNascita(descrizioneLuogoNascita);
      personaTitolareVO.setNascitaStatoEstero(null);
    }
    else {
      personaTitolareVO.setNascitaStatoEstero(statoEsteroNascita);
      personaTitolareVO.setNascitaProv(null);
      personaTitolareVO.setLuogoNascita(null);
    }
    personaTitolareVO.setCittaNascita(cittaEsteroNascita);
    personaTitolareVO.setResIndirizzo(indirizzoResidenza);
    personaTitolareVO.setDescResProvincia(provinciaResidenza);
    personaTitolareVO.setResProvincia(provinciaResidenza);
    personaTitolareVO.setDescResComune(comuneResidenza);
    personaTitolareVO.setResCAP(capResidenza);
    personaTitolareVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    personaTitolareVO.setStatoEsteroRes(statoEsteroResidenza);
    personaTitolareVO.setCittaResidenza(cittaEsteroResidenza);
    personaTitolareVO.setResCittaEstero(cittaEsteroResidenza);
    personaTitolareVO.setResTelefono(telefono);
    personaTitolareVO.setResFax(fax);
    personaTitolareVO.setResMail(mail);
    personaTitolareVO.setDomIndirizzo(indirizzoDomicilio);
    personaTitolareVO.setDomComune(comuneDomicilio);
    personaTitolareVO.setDomCAP(capDomicilio);
    personaTitolareVO.setDomProvincia(provinciaDomicilio);
    personaTitolareVO.setDomicilioStatoEstero(statoEsteroDomicilio);
    personaTitolareVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);

    Long idTitoloStudio = null;
    if(titoloStudio != null && !titoloStudio.equals("")) {
      idTitoloStudio = Long.decode(titoloStudio);
    }
    personaTitolareVO.setIdTitoloStudio(idTitoloStudio);
    Long idIndirizzoStudio = null;
    if(indirizzoStudio != null && !indirizzoStudio.equals("")) {
      idIndirizzoStudio = Long.decode(indirizzoStudio);
    }
    personaTitolareVO.setIdIndirizzoStudio(idIndirizzoStudio);
    // Effettuo il controllo formale dei dati inputati dall'utente
    ValidationErrors errors = personaTitolareVO.validateNuovoRappresentanteLegale();
    if(personaTitolareVO.getSesso() != null) {
      session.setAttribute("sesso",personaTitolareVO.getSesso());
    }

    // Se il titolo di studio prevede l'indirizzo, quest'ultimo è obbligatorio
    Vector indirizzi = null;
    try {
      if(personaTitolareVO.getIdTitoloStudio() != null) {
        indirizzi = anagFacadeClient.getIndirizzoStudioByTitolo(personaTitolareVO.getIdTitoloStudio());
      }
    }
    catch(SolmrException se) {
    }

    if(indirizzi != null) {
      if(indirizzi.size() > 0 && personaTitolareVO.getIdTitoloStudio() != null) {
        if(!Validator.isNotEmpty(personaTitolareVO.getIdIndirizzoStudio())) {
          errors.add("idIndirizzoStudio",new ValidationError((String)AnagErrors.get("ERR_INDIRIZZO_STUDIO_OBBLIGATORIO")));
        }
      }
    }

    if(errors != null && errors.size() != 0) {
      // Dal momento che il campo radio è gestito nella vecchia maniera metto il valore sesso in sessione
      // e lo recupero nella view solo in caso di errore
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    String codFiscaleComune = "";
    try {
      if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("")) {
        codFiscaleComune = anagFacadeClient.ricercaCodiceFiscaleComune(descrizioneLuogoNascita,provinciaNascita);
      }
      else {
        codFiscaleComune = anagFacadeClient.ricercaCodiceFiscaleComune(statoEsteroNascita,"");
      }
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("descNascitaComune", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // Recupero i codici istat dei comuni
    // NASCITA
    String istatComuneNascita = "";
    String istatEsteroNascita = "";
    try {
      if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") ||
         provinciaNascita != null && !provinciaNascita.equals("")) {
         istatComuneNascita = anagFacadeClient.ricercaCodiceComune(descrizioneLuogoNascita,provinciaNascita);
      }
      else {
        istatEsteroNascita = anagFacadeClient.ricercaCodiceComune(statoEsteroNascita,"");
      }
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("descNascitaComune", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    if(!istatComuneNascita.equals("")) {
      personaTitolareVO.setNascitaComune(istatComuneNascita);
    }
    else {
      personaTitolareVO.setNascitaStatoEstero(istatEsteroNascita);
    }
    // RESIDENZA
    String istatComuneResidenza = "";
    try {
      if(provinciaResidenza != null && !provinciaResidenza.equals("") || comuneResidenza != null && !comuneResidenza.equals("")) {
        istatComuneResidenza = anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneResidenza,provinciaResidenza);
      }
      else {
        istatComuneResidenza = anagFacadeClient.ricercaCodiceComune(statoEsteroResidenza,"");
      }
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("descResComune", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    personaTitolareVO.setResComune(istatComuneResidenza);
    if((comuneDomicilio != null && !comuneDomicilio.equals("")) || (provinciaDomicilio != null && !provinciaDomicilio.equals(""))) {
      try {
        anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneDomicilio,provinciaDomicilio);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("domComune", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }

    // Se valorizzato, ricerco l'istat del domicilio
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
    personaTitolareVO.setIstatComuneDomicilio(istatComuneDomicilio);

    session.removeAttribute("personaTitolareVO");
    session.setAttribute("personaTitolareVO",personaTitolareVO);

    try {
    Validator.verificaCf(nome,cognome,sesso,DateUtils.parseDate(dataNascita),codFiscaleComune,
                         codiceFiscale);
    }
    catch(CodiceFiscaleException ce) {
      if(ce.getNome()) {
        errors.add("nome",new ValidationError(AnagErrors.ERR_NOME_CODICE_FISCALE));
      }
      if(ce.getCognome()) {
        errors.add("cognome",new ValidationError(AnagErrors.ERR_COGNOME_CODICE_FISCALE));
      }
      if(ce.getAnnoNascita()) {
        errors.add("strNascitaData",new ValidationError(AnagErrors.ERR_ANNO_NASCITA_CODICE_FISCALE));
      }
      if(ce.getMeseNascita()) {
        errors.add("strNascitaData",new ValidationError(AnagErrors.ERR_MESE_NASCITA_CODICE_FISCALE));
      }
      if(ce.getGiornoNascita()) {
        errors.add("strNascitaData",new ValidationError(AnagErrors.ERR_GIORNO_NASCITA_CODICE_FISCALE));
      }
      if(ce.getComuneNascita()) {
        errors.add("descNascitaComune",new ValidationError(AnagErrors.ERR_COMUNE_NASCITA_CODICE_FISCALE));
      }
      if(ce.getSesso()) {
        errors.add("sesso",new ValidationError(AnagErrors.ERR_SESSO_CODICE_FISCALE));
      }
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    // Se per il codice fiscale inserito dall'utente esiste già una persona su DB controllo che non siano
    // cambiati i dati relativi alla residenza
    PersonaFisicaVO oldPersonaFisicaVO = null;
    if(!personaTitolareVO.isNewPersonaFisica()) {
      // recupero la persona fisica su DB
      try {
        oldPersonaFisicaVO = anagFacadeClient.getPersonaFisica(personaTitolareVO.getCodiceFiscale().toUpperCase());
      }
      catch(SolmrException se) {
      }
    }
    // Se i dati della residenza sono cambiati mando l'utente alla pagina di richiesta di modifica o storicizzazione
    // della persona fisica
    if((oldPersonaFisicaVO != null)
      && Validator.isNotEmpty(oldPersonaFisicaVO.getDataInizioResidenza())) 
    {
      // Se la data di inizio  residenza è diversa da quella di sistema allora confronto la persona
      // fisica prelevata dal DB con la nuova persona fisica derivante dai nuovi dati inseriti come input
      // dall'utente.
      if(oldPersonaFisicaVO.getDataInizioResidenza().compareTo(DateUtils.parseDate(DateUtils.getCurrent())) != 0) {
        if(!personaTitolareVO.equalsResidenza(oldPersonaFisicaVO)) {
          request.setAttribute("urlForm", urlForm);
          session.removeAttribute("sesso");
          %>
            <jsp:forward page="<%= richiestaModificaUrl %>"/>
          <%
          return;
        }
      }
    }

    session.removeAttribute("sesso") ;
    %>
      <jsp:forward page="<%= passaggioSedeUrl %>"/>
    <%
  }
  // L'utente ha premuto il tasto annulla
  if(request.getParameter("indietro") != null) {
    // Recupero i parametri
    String codiceFiscale = request.getParameter("codiceFiscale");
    String cognome = request.getParameter("cognome");
    String nome = request.getParameter("nome");
    String sesso = request.getParameter("sesso");
    String dataNascita = request.getParameter("strNascitaData");
    String provinciaNascita = request.getParameter("nascitaProv");
    String descrizioneLuogoNascita = request.getParameter("descNascitaComune");
    String statoEsteroNascita = request.getParameter("nascitaStatoEstero");
    String cittaEsteroNascita = request.getParameter("nascitaCittaEstero");
    String indirizzoResidenza = request.getParameter("resIndirizzo");
    String provinciaResidenza = request.getParameter("resProvincia");
    String comuneResidenza = request.getParameter("descResComune");
    String capResidenza = request.getParameter("resCAP");
    String statoEsteroResidenza = request.getParameter("statoEsteroRes");
    String cittaEsteroResidenza = request.getParameter("cittaResidenza");
    String telefono = request.getParameter("resTelefono");
    String fax = request.getParameter("resFax");
    String mail = request.getParameter("resMail");
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
    String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    String titoloStudio = request.getParameter("idTitoloStudio");
    String indirizzoStudio = request.getParameter("idIndirizzoStudio");

    // Setto il value object
    personaTitolareVO.setCodiceFiscale(codiceFiscale);
    personaTitolareVO.setCognome(cognome);
    personaTitolareVO.setNome(nome);
    personaTitolareVO.setSesso(sesso);
    personaTitolareVO.setStrNascitaData(dataNascita);
    personaTitolareVO.setNascitaProv(provinciaNascita);
    personaTitolareVO.setDescNascitaComune(descrizioneLuogoNascita);
    personaTitolareVO.setLuogoNascita(descrizioneLuogoNascita);
    personaTitolareVO.setNascitaStatoEstero(statoEsteroNascita);
    personaTitolareVO.setCittaNascita(cittaEsteroNascita);
    personaTitolareVO.setResIndirizzo(indirizzoResidenza);
    personaTitolareVO.setDescResProvincia(provinciaResidenza);
    personaTitolareVO.setResProvincia(provinciaResidenza);
    personaTitolareVO.setDescResComune(comuneResidenza);
    personaTitolareVO.setResCAP(capResidenza);
    personaTitolareVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    personaTitolareVO.setStatoEsteroRes(statoEsteroResidenza);
    personaTitolareVO.setCittaResidenza(cittaEsteroResidenza);
    personaTitolareVO.setResTelefono(telefono);
    personaTitolareVO.setResFax(fax);
    personaTitolareVO.setResMail(mail);
    personaTitolareVO.setDomIndirizzo(indirizzoDomicilio);
    personaTitolareVO.setDomComune(comuneDomicilio);
    personaTitolareVO.setDomCAP(capDomicilio);
    personaTitolareVO.setDomProvincia(provinciaDomicilio);
    personaTitolareVO.setDomicilioStatoEstero(statoEsteroDomicilio);
    personaTitolareVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);

    Long idTitoloStudio = null;
    if(titoloStudio != null && !titoloStudio.equals("")) {
      idTitoloStudio = Long.decode(titoloStudio);
    }
    personaTitolareVO.setIdTitoloStudio(idTitoloStudio);
    Long idIndirizzoStudio = null;
    if(indirizzoStudio != null && !indirizzoStudio.equals("")) {
      idIndirizzoStudio = Long.decode(indirizzoStudio);
    }
    personaTitolareVO.setIdIndirizzoStudio(idIndirizzoStudio);

    session.removeAttribute("personaTitolareVO");
    session.setAttribute("personaTitolareVO",personaTitolareVO);
    %>
      <jsp:forward page="<%= indietroUrl %>"/>
    <%
  }
  // L'utente ha modificato il titolo di Studio
  if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("cambioTitoloStudio")) {
    // Recupero i parametri
    String codiceFiscale = request.getParameter("codiceFiscale");
    String cognome = request.getParameter("cognome");
    String nome = request.getParameter("nome");
    String sesso = request.getParameter("sesso");
    String dataNascita = request.getParameter("strNascitaData");
    String provinciaNascita = request.getParameter("nascitaProv");
    String descrizioneLuogoNascita = request.getParameter("descNascitaComune");
    String statoEsteroNascita = request.getParameter("nascitaStatoEstero");
    String cittaEsteroNascita = request.getParameter("nascitaCittaEstero");
    String indirizzoResidenza = request.getParameter("resIndirizzo");
    String provinciaResidenza = request.getParameter("resProvincia");
    String comuneResidenza = request.getParameter("descResComune");
    String capResidenza = request.getParameter("resCAP");
    String statoEsteroResidenza = request.getParameter("statoEsteroRes");
    String cittaEsteroResidenza = request.getParameter("cittaResidenza");
    String telefono = request.getParameter("resTelefono");
    String fax = request.getParameter("resFax");
    String mail = request.getParameter("resMail");
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
    String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    // Informazioni aggiuntive relative al titolo di studio
    String titoloStudio = request.getParameter("idTitoloStudio");

    // Setto il value object
    personaTitolareVO.setCodiceFiscale(codiceFiscale);
    personaTitolareVO.setCognome(cognome);
    personaTitolareVO.setNome(nome);
    personaTitolareVO.setSesso(sesso);
    personaTitolareVO.setStrNascitaData(dataNascita);
    personaTitolareVO.setNascitaProv(provinciaNascita);
    personaTitolareVO.setDescNascitaComune(descrizioneLuogoNascita);
    personaTitolareVO.setLuogoNascita(descrizioneLuogoNascita);
    personaTitolareVO.setNascitaStatoEstero(statoEsteroNascita);
    personaTitolareVO.setCittaNascita(cittaEsteroNascita);
    personaTitolareVO.setResIndirizzo(indirizzoResidenza);
    personaTitolareVO.setDescResProvincia(provinciaResidenza);
    personaTitolareVO.setResProvincia(provinciaResidenza);
    personaTitolareVO.setDescResComune(comuneResidenza);
    personaTitolareVO.setResCAP(capResidenza);
    personaTitolareVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    personaTitolareVO.setStatoEsteroRes(statoEsteroResidenza);
    personaTitolareVO.setCittaResidenza(cittaEsteroResidenza);
    personaTitolareVO.setResTelefono(telefono);
    personaTitolareVO.setResFax(fax);
    personaTitolareVO.setResMail(mail);
    personaTitolareVO.setDomIndirizzo(indirizzoDomicilio);
    personaTitolareVO.setDomComune(comuneDomicilio);
    personaTitolareVO.setDomCAP(capDomicilio);
    personaTitolareVO.setDomProvincia(provinciaDomicilio);
    personaTitolareVO.setDomicilioStatoEstero(statoEsteroDomicilio);
    personaTitolareVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);

    Long idTitoloStudio = null;
    if(titoloStudio != null && !titoloStudio.equals("")) {
      idTitoloStudio = Long.decode(titoloStudio);
    }
    personaTitolareVO.setIdTitoloStudio(idTitoloStudio);
    %>
      <jsp:forward page="<%= url %>"/>
    <%
  }
  // Arrivo dalla pagina di richiesta modifica/storicizzazione della persona fisica
  // L'utente ha scelto di modificare la persona fisica
  if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("modificaResidenzaSoggetto")) {
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("personaTitolareVO");
    personaFisicaVO.setIsModificaResidenza(true);
    personaFisicaVO.setIsStoricizzaResidenza(false);
    %>
      <jsp:forward page="<%= passaggioSedeUrl %>"/>
    <%
  }
  // L'utente ha scelto di storicizzare la persona fisica
  if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("storicizzaResidenzaSoggetto")) {
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("personaTitolareVO");
    personaFisicaVO.setIsModificaResidenza(false);
    personaFisicaVO.setIsStoricizzaResidenza(true);
    %>
      <jsp:forward page="<%= passaggioSedeUrl %>"/>
    <%
  }
  // l'utente ha selezionato il pulsante indietro dalla pagina di richiesta modifica/storicizzazione
  // della persona fisica
  if(request.getParameter("indietroModificaResidenza") != null) {
    %>
      <jsp:forward page="<%= url %>"/>
    <%
  }
%>

