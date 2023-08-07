<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "modificaSoggettoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

 AnagFacadeClient client = new AnagFacadeClient();

 String url = "/view/modificaSoggettoView.jsp";
 String urlForm = "../layout/contitolari_mod.htm";
 String richiestaModificaUrl = "/view/confirmModResidenzaView.jsp";

 AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 PersonaFisicaVO pfVO = null;


 try {
   client.checkStatoAzienda(anagAziendaVO.getIdAzienda());
 }
 catch(SolmrException se) {
   request.setAttribute("statoAzienda", se);
 }

  // L'utente ha premuto il pulsante salva
  if(request.getParameter("salva") != null) {
  
    PersonaFisicaVO personaModificaVO = (PersonaFisicaVO)session.getAttribute("personaModificaVO");

    personaModificaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
    // recupero dalla view i dati modificati
    String ruolo = null;
    String strDataInizioRuoloMod = null;
    String strDataFineRuoloMod = null;
    String descRuolo = null;
    Date dataInizioRuoloModPersona = null;
    Date dataFineRuoloModPersona = null;
    if(personaModificaVO.getTipiRuoloNonTitolareAndNonSpecificato() != null &&
       personaModificaVO.getTipiRuoloNonTitolareAndNonSpecificato().compareTo(Long.decode((String)SolmrConstants.get("TIPORUOLO_TITOL_RAPPR_LEG"))) == 0) {
      ruolo = personaModificaVO.getTipiRuoloNonTitolareAndNonSpecificato().toString();
      descRuolo = personaModificaVO.getRuolo();
      strDataInizioRuoloMod = request.getParameter("strDataInizioRuoloMod");
      if(personaModificaVO.getStrDataFineRuoloMod() != null) {
        strDataFineRuoloMod = DateUtils.formatDate(personaModificaVO.getDataFineRuoloMod());
        dataFineRuoloModPersona = personaModificaVO.getDataFineRuoloMod();
      }
    }
    else {
      ruolo = request.getParameter("tipiRuoloNonTitolareAndNonSpecificato");
      strDataInizioRuoloMod = request.getParameter("strDataInizioRuoloMod");
      strDataFineRuoloMod = request.getParameter("strDataFineRuoloMod");
    }
    String codiceFiscale = request.getParameter("codiceFiscale");
    String cognome = request.getParameter("cognome");
    String nome = request.getParameter("nome");
    String sesso = request.getParameter("sesso");
    String dataNascita = request.getParameter("strNascitaData");
    String provinciaNascita = request.getParameter("nascitaProv");
    String descrizioneLuogoNascita = request.getParameter("descNascitaComune");
    String statoEsteroNascita = request.getParameter("nascitaStatoEstero");
    String cittaEsteroNascita = request.getParameter("cittaNascita");
    String indirizzoResidenza = request.getParameter("resIndirizzo");
    String provinciaResidenza = request.getParameter("descResProvincia");
    String comuneResidenza = request.getParameter("descResComune");
    String capResidenza = request.getParameter("resCAP");
    String statoEsteroResidenza = request.getParameter("statoEsteroRes");
    String cittaEsteroResidenza = request.getParameter("cittaResidenza");
    String telefono = request.getParameter("resTelefono");
    String fax = request.getParameter("resFax");
        //Informazioni sul cellulare
    //String desPrefissoCellulareInt = request.getParameter("idPrefixCellulareInt");
    //String idPrefissoCellulareNaz = request.getParameter("idPrefixCellulare");
    String desNumeroCellulare = request.getParameter("idCellulareNumero");
    String mail = request.getParameter("resMail");
    String indirizzoDomicilio = request.getParameter("domIndirizzo");
    String comuneDomicilio = request.getParameter("domComune");
    String capDomicilio = request.getParameter("domCAP");
    String provinciaDomicilio = request.getParameter("domProvincia");
    String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
    String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
    String note = request.getParameter("note");
    // Informazioni aggiuntive relative al titolo di studio
    String titoloStudio = request.getParameter("idTitoloStudio");
    String indirizzoStudio = request.getParameter("idIndirizzoStudio");

    // Setto i valori recuperati all'interno del VO
    if(ruolo!=null&&!ruolo.equals("")) {
      personaModificaVO.setIdRuolo(new Long(ruolo));
      personaModificaVO.setTipiRuoloNonTitolareAndNonSpecificato(new Long(ruolo));
    }
    else {
      personaModificaVO.setIdRuolo(null);
      personaModificaVO.setTipiRuoloNonTitolareAndNonSpecificato(null);
    }
    personaModificaVO.setRuolo(descRuolo);
    personaModificaVO.setStrDataInizioRuoloMod(strDataInizioRuoloMod);
    personaModificaVO.setStrDataFineRuoloMod(strDataFineRuoloMod);
    personaModificaVO.setDataInizioRuoloMod(dataInizioRuoloModPersona);
    personaModificaVO.setDataFineRuoloMod(dataFineRuoloModPersona);
    personaModificaVO.setCognome(cognome);
    personaModificaVO.setNome(nome);
    personaModificaVO.setCodiceFiscale(codiceFiscale);
    personaModificaVO.setSesso(sesso);
    personaModificaVO.setStrNascitaData(dataNascita);
    if(Validator.isNotEmpty(dataNascita)) {
      if(Validator.validateDateF(dataNascita)) {
        personaModificaVO.setNascitaData(DateUtils.parseDate(dataNascita));
      }
    }
    personaModificaVO.setNascitaProv(provinciaNascita);
    personaModificaVO.setDescNascitaComune(descrizioneLuogoNascita);
    personaModificaVO.setNascitaStatoEstero(statoEsteroNascita);
    if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("")) {
      personaModificaVO.setLuogoNascita(descrizioneLuogoNascita);
    }
    personaModificaVO.setNascitaCittaEstero(cittaEsteroNascita);
    personaModificaVO.setCittaNascita(cittaEsteroNascita);
    personaModificaVO.setResIndirizzo(indirizzoResidenza);
    personaModificaVO.setDescResProvincia(provinciaResidenza);
    personaModificaVO.setResProvincia(provinciaResidenza);
    personaModificaVO.setDescResComune(comuneResidenza);
    personaModificaVO.setResCAP(capResidenza);
    personaModificaVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
    personaModificaVO.setResCittaEstero(cittaEsteroResidenza);
    personaModificaVO.setResTelefono(telefono);
    /*personaModificaVO.setdesPrefissoCellulareInt(desPrefissoCellulareInt);
    Long idPrefissoCellulareNazLong = null;
        if(idPrefissoCellulareNaz != null && !idPrefissoCellulareNaz.equals("")) {
      idPrefissoCellulareNazLong = Long.decode(idPrefissoCellulareNaz);
    }
    personaModificaVO.setIdPrefissoCellulareNaz(idPrefissoCellulareNazLong);*/
    personaModificaVO.setdesNumeroCellulare(desNumeroCellulare);
    personaModificaVO.setResFax(fax);
    personaModificaVO.setResMail(mail);
    personaModificaVO.setDomIndirizzo(indirizzoDomicilio);
    personaModificaVO.setDomComune(comuneDomicilio);
    personaModificaVO.setDomCAP(capDomicilio);
    personaModificaVO.setDomProvincia(provinciaDomicilio);
    personaModificaVO.setDomicilioStatoEstero(statoEsteroDomicilio);
    personaModificaVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);
    personaModificaVO.setNote(note);
    Long idTitoloStudio = null;
    if(titoloStudio != null && !titoloStudio.equals("")) {
      idTitoloStudio = Long.decode(titoloStudio);
    }
    personaModificaVO.setIdTitoloStudio(idTitoloStudio);
    Long idIndirizzoStudio = null;
    if(indirizzoStudio != null && !indirizzoStudio.equals("")) {
      idIndirizzoStudio = Long.decode(indirizzoStudio);
    }
    personaModificaVO.setIdIndirizzoStudio(idIndirizzoStudio);

    // Metto in sessione il nuovo oggetto creato con i parametri dell'utente
    session.setAttribute("personaModificaVO", personaModificaVO);

    // Effettuo il controllo formale dei dati inputati dall'utente
    ValidationErrors errors = personaModificaVO.validateModificaSoggetto();

    // Se il titolo di studio prevede l'indirizzo, quest'ultimo è obbligatorio
    Vector indirizzi = null;
    try {
      if(personaModificaVO.getIdTitoloStudio() != null) {
        indirizzi = client.getIndirizzoStudioByTitolo(personaModificaVO.getIdTitoloStudio());
      }
    }
    catch(SolmrException se) {
    }

    if(indirizzi != null) {
      if(indirizzi.size() > 0 && personaModificaVO.getIdTitoloStudio() != null) {
        if(!Validator.isNotEmpty(personaModificaVO.getIdIndirizzoStudio())) {
          errors.add("idIndirizzoStudio",new ValidationError((String)AnagErrors.get("ERR_INDIRIZZO_STUDIO_OBBLIGATORIO")));
        }
      }
    }

    // Controllo che il ruolo sia coerente con la forma giuridica dell'azienda
    String messaggio = "";
    if(personaModificaVO.getIdRuolo() != null) {
      if(anagAziendaVO.getTipoFormaGiuridica().getCode().compareTo(Integer.decode(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_PERSONA_NO_ESERCITA")).toString())) == 0 ||
         anagAziendaVO.getTipoFormaGiuridica().getCode().compareTo(Integer.decode(((Long)SolmrConstants.get("TIPO_FORMA_GIURIDICA_INDIVIDUALE")).toString())) == 0) {
        if(personaModificaVO.getIdRuolo().compareTo(Long.decode((String)SolmrConstants.get("TIPORUOLO_SOCIO"))) == 0) {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_RUOLO_NO_VALIDO_INDIVIDUALE"));
          //errors.add("tipiRuoloNonTitolareAndNonSpecificato", error);
          messaggio += error.getMessage();
        }
      }
      else {
        if(personaModificaVO.getIdRuolo().compareTo(Long.decode((String)SolmrConstants.get("TIPORUOLO_COADIUVANTE"))) == 0) {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_RUOLO_NO_VALIDO"));
          //errors.add("tipiRuoloNonTitolareAndNonSpecificato", error);
          messaggio += error.getMessage();
        }
      }
    }

    if(Validator.isNotEmpty(messaggio)) {
      errors.add("error", new ValidationError(messaggio));
    }

    if(errors != null && errors.size() != 0) {
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    String codFiscaleComune = "";
    boolean isErrNascitaStatoEstero = false;
    try {
      if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") ||
        provinciaNascita != null && !provinciaNascita.equals("")) {
        codFiscaleComune = client.ricercaCodiceFiscaleComune(descrizioneLuogoNascita,provinciaNascita);
      }
      else {
        isErrNascitaStatoEstero = true;
        codFiscaleComune = client.ricercaCodiceFiscaleComune(statoEsteroNascita, "");
      }
    }
    catch(SolmrException se) {
     if(isErrNascitaStatoEstero) {
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
        errors.add("nascitaStatoEstero", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      else {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("descNascitaComune", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }

    // Recupero i codici istat dei comuni
    // NASCITA
    String istatComuneNascita = "";
    try {
      if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") ||
         provinciaNascita != null && !provinciaNascita.equals("")) {
        istatComuneNascita = client.ricercaCodiceComune(descrizioneLuogoNascita,provinciaNascita);
      }
      else {
        isErrNascitaStatoEstero = true;
        istatComuneNascita = client.ricercaCodiceComune(statoEsteroNascita,"");
      }
    }
    catch(SolmrException se) {
      if(isErrNascitaStatoEstero) {
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
        errors.add("nascitaStatoEstero", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
      else {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("descNascitaComune", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }

    personaModificaVO.setNascitaComune(istatComuneNascita);

   // RESIDENZA

   String istatComuneResidenza = "";
   boolean isErrStatoEstero = false;
   try {
     if(provinciaResidenza != null && !provinciaResidenza.equals("") || comuneResidenza != null && !comuneResidenza.equals("")) {
       istatComuneResidenza = client.ricercaCodiceComuneNonEstinto(comuneResidenza,provinciaResidenza);
     }
     else {
       isErrStatoEstero = true;
       istatComuneResidenza = client.ricercaCodiceComune(statoEsteroResidenza,"");
     }
   }
   catch(SolmrException se) {
     if(isErrStatoEstero) {
       ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
       errors.add("statoEsteroRes", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(url).forward(request, response);
       return;
     }
     else {
       ValidationError error = new ValidationError(se.getMessage());
       errors.add("descResComune", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(url).forward(request, response);
       return;
     }
   }
   personaModificaVO.setResComune(istatComuneResidenza);

   // Controllo che l'utente abbia inserito un valore corretto relativo al domicilio
   String istatComuneDomicilio = null;
   if((comuneDomicilio != null && !comuneDomicilio.equals("")) || (provinciaDomicilio != null && !provinciaDomicilio.equals(""))) {
     try {
       istatComuneDomicilio = client.ricercaCodiceComuneNonEstinto(comuneDomicilio,provinciaDomicilio);
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
       istatComuneDomicilio = client.ricercaCodiceComuneNonEstinto(statoEsteroDomicilio ,"");
     }
     catch(SolmrException se) {
       ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
       errors.add("domicilioStatoEstero", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(url).forward(request, response);
       return;
     }
   }
   personaModificaVO.setIstatComuneDomicilio(istatComuneDomicilio);

   // Nuovo oggetto con i codici istat dei comuni inseriti
   session.setAttribute("personaModificaVO",personaModificaVO);

   // Controllo della coerenza del codice fiscale
   if(codiceFiscale!=null&&!codiceFiscale.equals("")){
     try {
       Validator.verificaCf(personaModificaVO.getNome(),personaModificaVO.getCognome(),
                            personaModificaVO.getSesso(),personaModificaVO.getNascitaData(),
                            codFiscaleComune, personaModificaVO.getCodiceFiscale());
     }
     catch(CodiceFiscaleException ce) {
       if(ce.getNome()) {
         errors.add("nome",new ValidationError(AnagErrors.ERR_NOME_CODICE_FISCALE));
       }
       else if(ce.getCognome()) {
         errors.add("cognome",new ValidationError(AnagErrors.ERR_COGNOME_CODICE_FISCALE));
       }
       else if(ce.getAnnoNascita()) {
         errors.add("strNascitaData",new ValidationError(AnagErrors.ERR_ANNO_NASCITA_CODICE_FISCALE));
       }
       else if(ce.getMeseNascita()) {
         errors.add("strNascitaData",new ValidationError(AnagErrors.ERR_MESE_NASCITA_CODICE_FISCALE));
       }
       else if(ce.getGiornoNascita()) {
         errors.add("strNascitaData",new ValidationError(AnagErrors.ERR_GIORNO_NASCITA_CODICE_FISCALE));
       }
       else if(ce.getComuneNascita()) {
         errors.add("descNascitaComune",new ValidationError(AnagErrors.ERR_COMUNE_NASCITA_CODICE_FISCALE));
       }
       else if(ce.getSesso()) {
         errors.add("sesso",new ValidationError(AnagErrors.ERR_SESSO_CODICE_FISCALE));
       }
       else {
         errors.add("error", new ValidationError(ce.getMessage()+": impossibile procedere"));
       }
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(url).forward(request, response);
       return;
     }
   }

   // Se ci sono errori li visualizzo
   if(errors!=null&&errors.size()!=0){
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   // La data di inizio ruolo dev'essere minore o uguale della data di sistema
    Date oggi = DateUtils.parseDate(DateUtils.getCurrentDateString());
    Date dataInizioRuoloMod = null;

    if(strDataInizioRuoloMod!=null&&!strDataInizioRuoloMod.equals("")) {
      dataInizioRuoloMod = DateUtils.parseDate(strDataInizioRuoloMod);
      if(dataInizioRuoloMod.after(oggi)) {
        ValidationError error = new ValidationError("La data di inizio ruolo non può essere maggiore della data odierna");
        errors.add("strDataInizioRuoloMod", error);
        
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }

    // La data di fine ruolo (se valorizzata) dev'essere compresa tra data inizio ruolo e data
    // do sistema
    Date dataFineRuoloMod = null;
    if(strDataFineRuoloMod != null && !strDataFineRuoloMod.equals("")){
      dataFineRuoloMod = DateUtils.parseDate(strDataFineRuoloMod);
      if(dataFineRuoloMod.after(oggi)){
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_FINE_RUOLO_ERRATA_POST_ODIERNA"));
        errors.add("strDataFineRuoloMod", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }

    // Recuperiamo l'oggetto presente su DB partendo da id_contitolare
    PersonaFisicaVO oldPersonaFisicaVO = null;
    try {
      oldPersonaFisicaVO = client.getDettaglioSoggettoByIdContitolare(personaModificaVO.getIdContitolare());
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }

    // Se è stato modificato il codice fiscale dall'utente
    if(!personaModificaVO.getCodiceFiscale().equalsIgnoreCase(oldPersonaFisicaVO.getCodiceFiscale())) {
      PersonaFisicaVO personaFisicaPresenteVO = null;
      try {
        personaFisicaPresenteVO = client.getPersonaFisica(personaModificaVO.getCodiceFiscale());
      }
      catch(SolmrException se) {
        if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("CUAA_INESISTENTE"))) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(url).forward(request, response);
          return;
        }
      }
      // Se troviamo un record....
      if(personaFisicaPresenteVO != null) {
        personaModificaVO.setIdSoggetto(personaFisicaPresenteVO.getIdSoggetto());
        personaModificaVO.setIdPersonaFisica(personaFisicaPresenteVO.getIdPersonaFisica());
        // Effettuo la modifica del soggetto su DB_CONTITOLARE, DB_PERSONA_FISICA e
        // storicizzo la residenza
        try {
          client.updateDatiSoggettoAndStoricizzaResidenza(personaModificaVO,
                                                          personaFisicaPresenteVO,
                                                          ruoloUtenza.getIdUtente());
        }
        catch(SolmrException se) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(url).forward(request, response);
          return;
        }
      }
      // Altrimenti se la persona non esiste in archivio
      else {
        // Se l'utente ha modificato i dati identificativi della persona e cioè
        // cognome, nome, data di nascita, comune di nascita
        if(!personaModificaVO.isEqualIdentity(oldPersonaFisicaVO)) {
          try {
            // Cesso il legame della vecchia persona fisica
            client.cessaLegameBetweenPersonaAndAzienda(oldPersonaFisicaVO.getIdSoggetto(),
                                                       anagAziendaVO.getIdAzienda());
            // Inserisco un nuovo soggetto e legame con l'azienda
            personaModificaVO.setNewPersonaFisica(true);
            client.inserisciSoggetto(personaModificaVO, ruoloUtenza.getIdUtente());
          }
          catch(SolmrException se) {
            ValidationError error = new ValidationError(se.getMessage());
            errors.add("error", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(url).forward(request, response);
            return;
          }
        }
        // Altrimenti se non sono stati modificati i dati identificativi
        else {
          // Se l'utente ha modificato i dati della residenza...
          if(!personaModificaVO.equalsResidenza(oldPersonaFisicaVO)) {
            // Mando l'utente ad una pagina di richiesta operazione(modifica/storicizza)
            // da effettuare
            request.setAttribute("urlForm", urlForm);
            %>
              <jsp:forward page =  "<%= richiestaModificaUrl %>" />
            <%
          }
          // Altrimenti effettuo update su DB_CONTITOLARE e DB_PERSONA_FISICA
          else {
            try {
              client.updateSoggetto(personaModificaVO, ruoloUtenza.getIdUtente());
            }
            catch(SolmrException se) {
              ValidationError error = new ValidationError(se.getMessage());
              errors.add("error", error);
              request.setAttribute("errors", errors);
              request.getRequestDispatcher(url).forward(request, response);
              return;
            }
          }
        }
      }
    }
    // Altrimenti se non è stato modificato il codice fiscale
    else {
      // Se l'utente ha modificato i dati della residenza...
      if(!personaModificaVO.equalsResidenza(oldPersonaFisicaVO)) {
        // Mando l'utente ad una pagina di richiesta operazione(modifica/storicizza)
        // da effettuare
        request.setAttribute("urlForm", urlForm);
        %>
          <jsp:forward page =  "<%= richiestaModificaUrl %>" />
        <%
      }
      // Altrimenti effettuo update su DB_CONTITOLARE e DB_PERSONA_FISICA
      else {
        try {
          client.updateSoggetto(personaModificaVO, ruoloUtenza.getIdUtente());
        }
        catch(SolmrException se) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(url).forward(request, response);
          return;
        }
      }
    }

    // Rimuovo gli oggetti dalla sessione
    session.removeAttribute("personaModificaVO");
    session.removeAttribute("isOk");

    %>
      <jsp:forward page = "/layout/contitolari.htm" />
    <%
 }
 else if(request.getParameter("elenco") != null||request.getParameter("annulla") != null){
   %>
     <jsp:forward page = "/layout/contitolari.htm"/>
   <%
 }

 // L'utente ha modificato il titolo di Studio
 if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("cambioTitoloStudio")) {
   PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaModificaVO");

   // recupero dalla view i dati modificati
   String ruolo = null;
   String strDataInizioRuoloMod = null;
   String strDataFineRuoloMod = null;
   String descRuolo = null;
   Date dataInizioRuoloModPersona = null;
   Date dataFineRuoloModPersona = null;
   if(personaVO.getTipiRuoloNonTitolareAndNonSpecificato().compareTo(Long.decode((String)SolmrConstants.get("TIPORUOLO_TITOL_RAPPR_LEG"))) == 0) {
     ruolo = personaVO.getTipiRuoloNonTitolareAndNonSpecificato().toString();
     descRuolo = personaVO.getRuolo();
     strDataInizioRuoloMod = DateUtils.formatDate(personaVO.getDataInizioRuoloMod());
     dataInizioRuoloModPersona = personaVO.getDataInizioRuoloMod();
     if(personaVO.getStrDataFineRuoloMod() != null) {
       strDataFineRuoloMod = DateUtils.formatDate(personaVO.getDataFineRuoloMod());
       dataFineRuoloModPersona = personaVO.getDataFineRuoloMod();
     }
   }
   else {
     ruolo = request.getParameter("tipiRuoloNonTitolareAndNonSpecificato");
     strDataInizioRuoloMod = request.getParameter("strDataInizioRuoloMod");
     strDataFineRuoloMod = request.getParameter("strDataFineRuoloMod");
     if(Validator.isNotEmpty(strDataInizioRuoloMod)) {
       if(Validator.validateDateF(strDataInizioRuoloMod)) {
         dataInizioRuoloModPersona = DateUtils.parseDate(strDataInizioRuoloMod);
       }
     }
   }
   String cognome = request.getParameter("cognome");
   String nome = request.getParameter("nome");
   String codiceFiscale = request.getParameter("codiceFiscale");
   String sesso = request.getParameter("sesso");
   String dataNascita = request.getParameter("strNascitaData");
   String provinciaNascita = request.getParameter("nascitaProv");
   String descrizioneLuogoNascita = request.getParameter("descNascitaComune");
   String statoEsteroNascita = request.getParameter("nascitaStatoEstero");
   String cittaEsteroNascita = request.getParameter("cittaNascita");
   String indirizzoResidenza = request.getParameter("resIndirizzo");
   String provinciaResidenza = request.getParameter("descResProvincia");
   String comuneResidenza = request.getParameter("descResComune");
   String capResidenza = request.getParameter("resCAP");
   String statoEsteroResidenza = request.getParameter("statoEsteroRes");
   String cittaEsteroResidenza = request.getParameter("cittaResidenza");
   String telefono = request.getParameter("resTelefono");
   String fax = request.getParameter("resFax");
   //Informazioni sul cellulare
   String desPrefissoCellulareInt = request.getParameter("idPrefixCellulareInt");
   String idPrefissoCellulareNaz = request.getParameter("idPrefixCellulare");
   String desNumeroCellulare = request.getParameter("idCellulareNumero");
   String mail = request.getParameter("resMail");
   String indirizzoDomicilio = request.getParameter("domIndirizzo");
   String comuneDomicilio = request.getParameter("domComune");
   String capDomicilio = request.getParameter("domCAP");
   String provinciaDomicilio = request.getParameter("domProvincia");
   String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
   String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");
   String note = request.getParameter("note");
   // Informazioni aggiuntive relative al titolo di studio
   String titoloStudio = request.getParameter("idTitoloStudio");

   // Setto i valori recuperati all'interno del VO
   if(ruolo!=null&&!ruolo.equals("")) {
     personaVO.setIdRuolo(new Long(ruolo));
     personaVO.setTipiRuoloNonTitolareAndNonSpecificato(new Long(ruolo));
   }
   personaVO.setRuolo(descRuolo);
   personaVO.setStrDataInizioRuoloMod(strDataInizioRuoloMod);
   personaVO.setStrDataFineRuoloMod(strDataFineRuoloMod);
   personaVO.setDataInizioRuoloMod(dataInizioRuoloModPersona);
   personaVO.setDataFineRuoloMod(dataFineRuoloModPersona);
   personaVO.setCognome(cognome);
   personaVO.setNome(nome);
   personaVO.setCodiceFiscale(codiceFiscale);
   personaVO.setSesso(sesso);
   personaVO.setStrNascitaData(dataNascita);
   personaVO.setNascitaProv(provinciaNascita);
   personaVO.setDescNascitaComune(descrizioneLuogoNascita);
   personaVO.setNascitaStatoEstero(statoEsteroNascita);
   if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("")) {
     personaVO.setLuogoNascita(descrizioneLuogoNascita);
   }
   personaVO.setNascitaCittaEstero(cittaEsteroNascita);
   personaVO.setResIndirizzo(indirizzoResidenza);
   personaVO.setDescResProvincia(provinciaResidenza);
   personaVO.setResProvincia(provinciaResidenza);
   personaVO.setDescResComune(comuneResidenza);
   personaVO.setResCAP(capResidenza);
   personaVO.setDescStatoEsteroResidenza(statoEsteroResidenza);
   personaVO.setResCittaEstero(cittaEsteroResidenza);
   personaVO.setResTelefono(telefono);
   personaVO.setResFax(fax);
   personaVO.setdesPrefissoCellulareInt(desPrefissoCellulareInt);
   Long idPrefissoCellulareNazLong = null;
        if(idPrefissoCellulareNaz != null && !idPrefissoCellulareNaz.equals("")) {
      		idPrefissoCellulareNazLong = Long.decode(idPrefissoCellulareNaz);
    }
   personaVO.setIdPrefissoCellulareNaz(idPrefissoCellulareNazLong);
   personaVO.setdesNumeroCellulare(desNumeroCellulare);
   personaVO.setResMail(mail);
   personaVO.setDomIndirizzo(indirizzoDomicilio);
   personaVO.setDomComune(comuneDomicilio);
   personaVO.setDomCAP(capDomicilio);
   personaVO.setDomProvincia(provinciaDomicilio);
   personaVO.setDomicilioStatoEstero(statoEsteroDomicilio);
   personaVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);
   personaVO.setNote(note);
   Long idTitoloStudio = null;
   if(titoloStudio != null && !titoloStudio.equals("")) {
     idTitoloStudio = Long.decode(titoloStudio);
   }
   personaVO.setIdTitoloStudio(idTitoloStudio);
   session.setAttribute("personaModificaVO",personaVO);

   %>
     <jsp:forward page="<%= url %>"/>
   <%
 }

 // Arrivo dalla pagina che chiede all'utente se intende effettuare solo una modifica o anche una storicizzazione
 // dei dati della persona.
 // Se l'utente ha scelto di modificare
 if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("modificaResidenzaSoggetto")) {
   ValidationErrors errors = new ValidationErrors();

   // Recupero i dati inseriti dall'utente
   PersonaFisicaVO personaModificaVO = (PersonaFisicaVO)session.getAttribute("personaModificaVO");

   try {
     // Effettuo la modifica
     client.updateSoggetto(personaModificaVO, ruoloUtenza.getIdUtente());
     session.removeAttribute("personaModificaVO");
     session.removeAttribute("isOk");
   }
   catch(SolmrException se) {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   // Torno alla pagina di elenco dei soggetti
   %>
    <jsp:forward page = "/layout/contitolari.htm" />
   <%
 }

 // Se invece l'utente ha scelto di storicizzare
 if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("storicizzaResidenzaSoggetto")) {
   ValidationErrors errors = new ValidationErrors();
   // Recupero i dati inseriti dall'utente
   PersonaFisicaVO personaModificaVO = (PersonaFisicaVO)session.getAttribute("personaModificaVO");

   // Recupero i dati presenti su DB
   PersonaFisicaVO oldPersonaFisicaVO = null;
   try {
     oldPersonaFisicaVO = client.getDettaglioSoggettoByIdContitolare(personaModificaVO.getIdContitolare());
   }
   catch(SolmrException se) {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   // Effettuo la storicizzazione
   try {
     client.updateDatiSoggettoAndStoricizzaResidenza(personaModificaVO,
                                                     oldPersonaFisicaVO,
                                                     ruoloUtenza.getIdUtente());
     session.removeAttribute("personaModificaVO");
     session.removeAttribute("isOK");
   }
   catch(SolmrException se) {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   // Torno alla pagina di elenco
   %>
    <jsp:forward page = "/layout/contitolari.htm" />
   <%
 }

 // L'utente ha selezionato il pulsante annulla dalla pagina che richiede all'utente se intende modificare
 // o storicizzare i dati della persona
 if(request.getParameter("indietroModificaResidenza") != null) {
   %>
      <jsp:forward page = "<%= url %>" />
   <%
 }

 // L'utente ha selezionato la funzione di modifica dall'elenco dei soggetti collegati
 // e il primo controller(contitolariCtrl) mi ha dirottato qui
 else {
   session.removeAttribute("personaModificaVO");
   session.removeAttribute("isOk");
   ValidationErrors errors = new ValidationErrors();


   // Recupero il record selezionato dall'utente attraverso l'id_contitolare
   Long idContitolare = (Long)request.getAttribute("idContitolare");
   PersonaFisicaVO personaModificaVO = null;
   try {
     personaModificaVO = client.getDettaglioSoggettoByIdContitolare(idContitolare);
     personaModificaVO.setIdContitolare(idContitolare);
     session.setAttribute("personaModificaVO",personaModificaVO);

     // Controllo che il soggetto collegato non abbia la data di fine ruolo valorizzata
     if(Validator.isNotEmpty(personaModificaVO.getDataFineRuolo())) {
       ValidationError error = new ValidationError((String)AnagErrors.get("ERR_SOGGETTO_RUOLO_CESSATO"));
       errors.add("error", error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher("/view/contitolariView.jsp").forward(request, response);
       return;
     }

     String isOk = "false";
     if(!Validator.isNotEmpty(personaModificaVO.getCodiceFiscale()) || !Validator.controlloCf(personaModificaVO.getCodiceFiscale())
        || SolmrConstants.FLAG_S.equalsIgnoreCase(personaModificaVO.getFlagCfOk()))
     {
       isOk = "true";
     }
     else {
       isOk = "false";
     }
     session.setAttribute("isOk", isOk);

     %>
      <jsp:forward page = "/view/modificaSoggettoView.jsp" />
     <%
   }
   catch (SolmrException ex) {
     ValidationError error = new ValidationError(ex.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher("/view/contitolariView.jsp").forward(request, response);
     return;
   }
 }
%>
