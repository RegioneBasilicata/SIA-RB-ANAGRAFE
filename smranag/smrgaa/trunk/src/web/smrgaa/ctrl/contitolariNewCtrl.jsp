<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%

  String iridePageName = "contitolariNewCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String url = "/view/contitolariNewView.jsp";
  String nuovoSoggettoUrl = "/view/contitolariNewView.jsp";
  String salvaUrl = "/layout/contitolari.htm";
  String elencoContitolariUrl = "../view/contitolariView.jsp";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  PersonaFisicaVO personaIndividualeVO = (PersonaFisicaVO)session.getAttribute("personaIndividualeVO");
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  String notifica = "";
  try {
    anagFacadeClient.checkStatoAzienda(anagVO.getIdAzienda());
  }
  catch(SolmrException se) {
    request.setAttribute("statoAzienda", se);
  }


  if(request.getParameter("annulla") != null) {
    session.removeAttribute("nuovaPersonaFisicaVO");
    %>
      <jsp:forward page = "<%= elencoContitolariUrl %>" />
    <%
  }
  // L'utente ha premuto il tasto cerca relativo al codice fiscale imputato per recuoerare i dati di
  // una persona già centita in archivio.
  else if(request.getParameter("cercaCF.x") != null) {
    ValidationErrors errors = new ValidationErrors();
    String codiceFiscale = request.getParameter("codiceFiscale");
    Long ruolo = null;
    if(request.getParameter("tipiRuoloNonTitolareAndNonSpecificato")!=null&&
       !request.getParameter("tipiRuoloNonTitolareAndNonSpecificato").equals(""))
      ruolo = new Long(request.getParameter("tipiRuoloNonTitolareAndNonSpecificato"));
    String dataInizioRuoloMod = request.getParameter("strDataInizioRuoloMod");

    // Controllo che l'utente abbia inserito il codice fiscale per effettuare la ricerca.
    if(codiceFiscale == null || codiceFiscale.equals("")) {
      ValidationError error = new ValidationError("Valorizzare il codice fiscale per effettuare la ricerca!");
      errors.add("codiceFiscale", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    // Se lo ha inserito controllo che sia valido.
    else if(!Validator.controlloCf(codiceFiscale)) {
      ValidationError error = new ValidationError("Inserire un codice fiscale corretto per effettuare la ricerca!");
      errors.add("codiceFiscale", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // Se ha passato tutti i controlli allora effettuo la ricerca per recuperare i dati della persona
    // presenti in archivio
    PersonaFisicaVO nuovaPersonaFisicaVO = null;
    try {
      nuovaPersonaFisicaVO = anagFacadeClient.getPersonaFisica(codiceFiscale.toUpperCase());
    }
    catch(SolmrException se) {
      if(se.getMessage().equalsIgnoreCase((String)AnagErrors.get("CUAA_INESISTENTE"))) {
        nuovaPersonaFisicaVO = new PersonaFisicaVO();
        nuovaPersonaFisicaVO.setCodiceFiscale(codiceFiscale.toUpperCase());
        nuovaPersonaFisicaVO.setTipiRuoloNonTitolareAndNonSpecificato(ruolo);
        nuovaPersonaFisicaVO.setStrDataInizioRuoloMod(dataInizioRuoloMod);
        session.setAttribute("nuovaPersonaFisicaVO",nuovaPersonaFisicaVO);
        %>
           <jsp:forward page="<%= url %>"/>
        <%
      }
      else {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
    if(ruolo!=null) {
      nuovaPersonaFisicaVO.setTipiRuoloNonTitolareAndNonSpecificato(ruolo);
    }
    nuovaPersonaFisicaVO.setStrDataInizioRuoloMod(dataInizioRuoloMod);
    session.setAttribute("nuovaPersonaFisicaVO",nuovaPersonaFisicaVO);
    %>
      <jsp:forward page="<%= url %>"/>
    <%
  }
  // L'utente ha premuto il tasto salva
  else if(request.getParameter("salva") != null && session.getAttribute("inserimento") != null) {
    // Recupero i parametri
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("nuovaPersonaFisicaVO");
    Long idRuolo = null;
    if(request.getParameter("tipiRuoloNonTitolareAndNonSpecificato")!=null&&!request.getParameter("tipiRuoloNonTitolareAndNonSpecificato").equals(""))
      idRuolo = new Long(request.getParameter("tipiRuoloNonTitolareAndNonSpecificato"));

    String strDataInizioRuoloMod = request.getParameter("strDataInizioRuoloMod");
    Long idAzienda = anagVO.getIdAzienda();
    String codiceFiscale = request.getParameter("codiceFiscale");
    Long idPersonaFisica = null;
    /**/
    boolean isNewPersonaFisica = true;
    try {
      personaFisicaVO = anagFacadeClient.getPersonaFisica(codiceFiscale.toUpperCase());
    }
    catch(SolmrException se) {
    }

    if(personaFisicaVO != null) {
      if(personaFisicaVO.getIdPersonaFisica() != null) {
        idPersonaFisica = personaFisicaVO.getIdPersonaFisica();
        isNewPersonaFisica = false;
      }
      else {
        personaFisicaVO = new PersonaFisicaVO();
        isNewPersonaFisica = true;
      }
    }
    else {
      personaFisicaVO = new PersonaFisicaVO();
      isNewPersonaFisica = true;
    }
    /**/
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
    String cittaEsteroResidenza = request.getParameter("resCittaEstero");
    String telefono = request.getParameter("resTelefono");
    String fax = request.getParameter("resFax");
    String mail = request.getParameter("resMail");
    //Informazioni sul cellulare
    //String desPrefissoCellulareInt = request.getParameter("idPrefixCellulareInt");
    //String idPrefissoCellulareNaz = request.getParameter("idPrefixCellulare");
    String desNumeroCellulare = request.getParameter("idCellulareNumero");
    SolmrLogger.debug(this,"CELLULARE"+desNumeroCellulare );
    
    
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String domicilioStatoEstero = request.getParameter("domicilioStatoEstero");
    String descCittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    String note = request.getParameter("note");
    // Informazioni aggiuntive relative al titolo di studio
    String titoloStudio = request.getParameter("idTitoloStudio");
    String indirizzoStudio = request.getParameter("idIndirizzoStudio");
    // Setto il value object
    personaFisicaVO.setNewPersonaFisica(isNewPersonaFisica);
    personaFisicaVO.setIdRuolo(idRuolo);
    personaFisicaVO.setStrDataInizioRuoloMod(strDataInizioRuoloMod);
    personaFisicaVO.setIdAzienda(idAzienda);
    personaFisicaVO.setCodiceFiscale(codiceFiscale);
    personaFisicaVO.setCognome(cognome);
    personaFisicaVO.setNome(nome);
    personaFisicaVO.setSesso(sesso);
    personaFisicaVO.setStrNascitaData(dataNascita);
    personaFisicaVO.setNascitaProv(provinciaNascita);
    personaFisicaVO.setDescNascitaComune(descrizioneLuogoNascita);
    personaFisicaVO.setNascitaStatoEstero(statoEsteroNascita);
    if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("")) {
      personaFisicaVO.setLuogoNascita(descrizioneLuogoNascita);
    }
    personaFisicaVO.setNascitaCittaEstero(cittaEsteroNascita);
    personaFisicaVO.setCittaNascita(cittaEsteroNascita);
    personaFisicaVO.setResIndirizzo(indirizzoResidenza);
    personaFisicaVO.setResProvincia(provinciaResidenza);
    personaFisicaVO.setDescResComune(comuneResidenza);
    personaFisicaVO.setResCAP(capResidenza);
    personaFisicaVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    personaFisicaVO.setResCittaEstero(cittaEsteroResidenza);
    personaFisicaVO.setCittaResidenza(cittaEsteroResidenza);
    personaFisicaVO.setResTelefono(telefono);
    personaFisicaVO.setResFax(fax);
    personaFisicaVO.setResMail(mail);
    /*personaFisicaVO.setdesPrefissoCellulareInt(desPrefissoCellulareInt);
    Long idPrefissoCellulareNazLong = null;
        if(idPrefissoCellulareNaz != null && !idPrefissoCellulareNaz.equals("")) {
      idPrefissoCellulareNazLong = Long.decode(idPrefissoCellulareNaz);
    }
    personaFisicaVO.setIdPrefissoCellulareNaz(idPrefissoCellulareNazLong);*/
    personaFisicaVO.setdesNumeroCellulare(desNumeroCellulare);
    personaFisicaVO.setDomIndirizzo(indirizzoDomicilio);
    personaFisicaVO.setDomComune(comuneDomicilio);
    personaFisicaVO.setDomCAP(capDomicilio);
    personaFisicaVO.setDomProvincia(provinciaDomicilio);
    personaFisicaVO.setDomicilioStatoEstero(domicilioStatoEstero);
    personaFisicaVO.setDescCittaEsteroDomicilio(descCittaEsteroDomicilio);
    personaFisicaVO.setNote(note);
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
    // Effettuo il controllo formale dei dati inputati dall'utente
    ValidationErrors errors = personaFisicaVO.validateNuovoSoggettoCollegato();

    // Controllo che il ruolo sia coerente con la forma giuridica dell'azienda
    if(personaFisicaVO.getIdRuolo() != null) {
      if(anagVO.getTipoFormaGiuridica().getCode().compareTo(Integer.decode(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_PERSONA_NO_ESERCITA")).toString())) == 0 ||
         anagVO.getTipoFormaGiuridica().getCode().compareTo(Integer.decode(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_INDIVIDUALE")).toString())) == 0) {
        if(personaFisicaVO.getIdRuolo().compareTo(Long.decode((String)SolmrConstants.get("TIPORUOLO_SOCIO"))) == 0) {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_RUOLO_NO_VALIDO_INDIVIDUALE"));
          errors.add("tipiRuoloNonTitolareAndNonSpecificato", error);
        }
      }
      else {
        if(personaFisicaVO.getIdRuolo().compareTo(Long.decode((String)SolmrConstants.get("TIPORUOLO_COADIUVANTE"))) == 0) {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_RUOLO_NO_VALIDO"));
          errors.add("tipiRuoloNonTitolareAndNonSpecificato", error);
        }
      }
    }

    if(personaFisicaVO.getSesso() != null) {
      session.setAttribute("_sesso_", personaFisicaVO.getSesso());
    }
    if(!Validator.isNotEmpty(codiceFiscale)) {
      ValidationError error = new ValidationError(AnagErrors.ERR_CODICE_FISCALE_OBBLIGATORIO);
      errors.add("codiceFiscale", error);
    }
    else if(!Validator.controlloCf(codiceFiscale)) {
      ValidationError error = new ValidationError(AnagErrors.ERR_CODICE_FISCALE);
      errors.add("codiceFiscale", error);
    }

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

    session.setAttribute("nuovaPersonaFisicaVO",personaFisicaVO);

    if(errors!=null&&errors.size()!=0){
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // La data di inizio ruolo non dev'essere maggiore della data di sistema
    Date oggi = DateUtils.parseDate(DateUtils.getCurrentDateString());
    Date dataInizioRuoloMod = null;
    if(strDataInizioRuoloMod!=null&&!strDataInizioRuoloMod.equals("")){
      dataInizioRuoloMod = DateUtils.parseDate(strDataInizioRuoloMod);
      if(dataInizioRuoloMod.after(oggi)){
        ValidationError error = new ValidationError("La data di inizio ruolo non può essere maggiore della data odierna");
        errors.add("strDataInizioRuoloMod", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
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
      if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") ||
         provinciaNascita != null && !provinciaNascita.equals("")) {
        codFiscaleComune = anagFacadeClient.ricercaCodiceFiscaleComune(descrizioneLuogoNascita,provinciaNascita);
      }
      else
        codFiscaleComune= anagFacadeClient.ricercaCodiceFiscaleComune(statoEsteroNascita,"");
    }
    catch(SolmrException se) {
      se.printStackTrace();
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("descNascitaComune", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    // Recupero i codici istat dei comuni
    // NASCITA
    String istatComuneNascita = "";
    try {
      if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") ||
         provinciaNascita != null && !provinciaNascita.equals("")) {
        istatComuneNascita = anagFacadeClient.ricercaCodiceComune(descrizioneLuogoNascita,provinciaNascita);
      }
      else {
        istatComuneNascita = anagFacadeClient.ricercaCodiceComune(statoEsteroNascita,"");
      }
    }
    catch(SolmrException se) {
      se.printStackTrace();
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    personaFisicaVO.setNascitaComune(istatComuneNascita);
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
      se.printStackTrace();
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    personaFisicaVO.setResComune(istatComuneResidenza);

    String istatComuneDomicilio = null;

    // DOMICILIO
    if((comuneDomicilio != null && !comuneDomicilio.equals("")) || (provinciaDomicilio != null && !provinciaDomicilio.equals(""))) {
      try {
        istatComuneDomicilio = anagFacadeClient.ricercaCodiceComuneNonEstinto(comuneDomicilio,provinciaDomicilio);
      }
      catch(SolmrException se) {
        se.printStackTrace();
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("domComune", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
    else {
      if(Validator.isNotEmpty(domicilioStatoEstero)) {
        try {
          istatComuneDomicilio = anagFacadeClient.ricercaCodiceComuneNonEstinto(domicilioStatoEstero,"");
        }
        catch(SolmrException se) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("domComune", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(url).forward(request, response);
          return;
        }
      }
    }

    personaFisicaVO.setIstatComuneDomicilio(istatComuneDomicilio);

    // Controllo coerenza codice fiscale
    try {
      Validator.verificaCf(personaFisicaVO.getNome(),personaFisicaVO.getCognome(),
                           personaFisicaVO.getSesso(),personaFisicaVO.getNascitaData(),
                           codFiscaleComune, personaFisicaVO.getCodiceFiscale());
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
    session.removeAttribute("sesso");
    try 
    {
      anagFacadeClient.inserisciSoggetto(personaFisicaVO, ruoloUtenza.getIdUtente());
    }
    catch (SolmrException se) {
      se.printStackTrace();
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    catch (Exception exc){
    }
    session.removeAttribute("nuovaPersonaFisicaVO");

    // Una volta inserito il soggetto recupero l'elenco dei soggetti collegati e vado alla
    // pagina di elenco
    Vector elencoSoggettiCollegati = null;
    try {
      elencoSoggettiCollegati = anagFacadeClient.getSoggetti(anagVO.getIdAzienda(), new Boolean(false));
    }
    catch (SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
    session.setAttribute("v_soggetti",elencoSoggettiCollegati);
    session.removeAttribute("inserimento");
    %>
      <jsp:forward page = "<%= elencoContitolariUrl %>" />
    <%
  }
  // L'utente ha modificato il titolo di Studio
  if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("cambioTitoloStudio")
     && Validator.isNotEmpty(session.getAttribute("inserimento"))) {
    PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("nuovaPersonaFisicaVO");

    Long idRuolo = null;
    if(request.getParameter("tipiRuoloNonTitolareAndNonSpecificato")!=null&&!request.getParameter("tipiRuoloNonTitolareAndNonSpecificato").equals("")) {
      idRuolo = new Long(request.getParameter("tipiRuoloNonTitolareAndNonSpecificato"));
    }
    String strDataInizioRuoloMod = request.getParameter("strDataInizioRuoloMod");
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
    String cittaEsteroResidenza = request.getParameter("resCittaEstero");
    String telefono = request.getParameter("resTelefono");
    String fax = request.getParameter("resFax");
    String mail = request.getParameter("resMail");
        //Informazioni sul cellulare
    String desPrefissoCellulareInt = request.getParameter("idPrefixCellulareInt");
    String idPrefissoCellulareNaz = request.getParameter("idPrefixCellulare");
    String desNumeroCellulare = request.getParameter("idCellulareNumero");
    SolmrLogger.debug(this,"CELLULARE [" + desPrefissoCellulareInt + "] [" + idPrefissoCellulareNaz + "] [" + desNumeroCellulare + "]");
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String domicilioStatoEstero = request.getParameter("domicilioStatoEstero");
    String descCittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    String note = request.getParameter("note");
    // Informazioni aggiuntive relative al titolo di studio
    String titoloStudio = request.getParameter("idTitoloStudio");

    if(personaFisicaVO == null) {
      personaFisicaVO = new PersonaFisicaVO();
    }
    personaFisicaVO.setIdRuolo(idRuolo);
    personaFisicaVO.setTipiRuoloNonTitolareAndNonSpecificato(idRuolo);
    personaFisicaVO.setStrDataInizioRuoloMod(strDataInizioRuoloMod);
    personaFisicaVO.setCodiceFiscale(codiceFiscale);
    personaFisicaVO.setCognome(cognome);
    personaFisicaVO.setNome(nome);
    personaFisicaVO.setSesso(sesso);
    personaFisicaVO.setStrNascitaData(dataNascita);
    personaFisicaVO.setNascitaProv(provinciaNascita);
    personaFisicaVO.setDescNascitaComune(descrizioneLuogoNascita);
    personaFisicaVO.setNascitaStatoEstero(statoEsteroNascita);
    if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("")) {
      personaFisicaVO.setLuogoNascita(descrizioneLuogoNascita);
    }
    personaFisicaVO.setNascitaCittaEstero(cittaEsteroNascita);
    personaFisicaVO.setResIndirizzo(indirizzoResidenza);
    personaFisicaVO.setResProvincia(provinciaResidenza);
    personaFisicaVO.setDescResComune(comuneResidenza);
    personaFisicaVO.setResCAP(capResidenza);
    personaFisicaVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    personaFisicaVO.setStatoEsteroRes(statoEsteroResidenza);
    personaFisicaVO.setResCittaEstero(cittaEsteroResidenza);
    personaFisicaVO.setResTelefono(telefono);
    personaFisicaVO.setResFax(fax);
    personaFisicaVO.setResMail(mail);
    personaFisicaVO.setdesPrefissoCellulareInt(desPrefissoCellulareInt);
    Long idPrefissoCellulareNazLong = null;
    if(idPrefissoCellulareNaz != null && !idPrefissoCellulareNaz.equals("")) {
      idPrefissoCellulareNazLong = Long.decode(idPrefissoCellulareNaz);
    }
    personaFisicaVO.setIdPrefissoCellulareNaz(idPrefissoCellulareNazLong);
    personaFisicaVO.setdesNumeroCellulare(desNumeroCellulare);
    personaFisicaVO.setDomIndirizzo(indirizzoDomicilio);
    personaFisicaVO.setDomComune(comuneDomicilio);
    personaFisicaVO.setDomCAP(capDomicilio);
    personaFisicaVO.setDomProvincia(provinciaDomicilio);
    personaFisicaVO.setDomicilioStatoEstero(domicilioStatoEstero);
    personaFisicaVO.setDescCittaEsteroDomicilio(descCittaEsteroDomicilio);
    personaFisicaVO.setNote(note);
    Long idTitoloStudio = null;
    if(titoloStudio != null && !titoloStudio.equals("")) {
      idTitoloStudio = Long.decode(titoloStudio);
    }
    personaFisicaVO.setIdTitoloStudio(idTitoloStudio);
    boolean isNewPersonaFisica = false;
    personaFisicaVO.setNewPersonaFisica(isNewPersonaFisica);
    session.setAttribute("nuovaPersonaFisicaVO",personaFisicaVO);

    %>
       <jsp:forward page = "<%= url %>" />
    <%
  }
  else if(Validator.isNotEmpty(session.getAttribute("inserimento"))) {
    session.removeAttribute("nuovaPersonaFisicaVO");
  %>
    <jsp:forward page = "<%= nuovoSoggettoUrl %>" />
  <%
  }
  else {
    %>
      <jsp:forward page = "<%= elencoContitolariUrl %>" />
    <%
  }
%>
