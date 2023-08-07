<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "modificaRappLegaleCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

 AnagFacadeClient client = new AnagFacadeClient();
 String url = "/view/modificaRappLegaleView.jsp";
 String richiestaModificaUrl = "/view/confirmModResidenzaView.jsp";
 String urlForm = "../layout/anagraficaRappLegale_mod.htm";
 String rappresentanteLegaleUrl = "/view/rappLegaleView.jsp";

 AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

 RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 Validator validator = new Validator(url);

 if(request.getParameter("salva")!=null) {

   PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaVO");
   session.removeAttribute("pfVOModifica");
   PersonaFisicaVO pfVOModifica = new PersonaFisicaVO();
   // setto i campi non modificabili in tale operazione
   if(personaVO!=null){
     pfVOModifica.setIdPersonaFisica(personaVO.getIdPersonaFisica());
     pfVOModifica.setCodiceFiscale(personaVO.getCodiceFiscale());
     pfVOModifica.setIdSoggetto(personaVO.getIdSoggetto());
   }
   else {
     pfVOModifica.setIdPersonaFisica(null);
   }
   // recupero dalla view i dati modificati
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
   String provinciaResidenza = request.getParameter("resProvincia");
   String comuneResidenza = request.getParameter("descResComune");
   String capResidenza = request.getParameter("resCAP");
   String statoEsteroResidenza = request.getParameter("statoEsteroRes");
   String cittaEsteroResidenza = request.getParameter("cittaResidenza");
   String telefono = request.getParameter("resTelefono");
   String fax = request.getParameter("resFax");
   String numeroCellulare = request.getParameter("numeroCellulare");
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

   pfVOModifica.setCodiceFiscale(codiceFiscale);
   pfVOModifica.setCognome(cognome);
   pfVOModifica.setNome(nome);
   pfVOModifica.setSesso(sesso);
   pfVOModifica.setStrNascitaData(dataNascita);
   pfVOModifica.setNascitaProv(provinciaNascita);
   pfVOModifica.setDescNascitaComune(descrizioneLuogoNascita);
   pfVOModifica.setNascitaStatoEstero(statoEsteroNascita);
   if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("")) {
     pfVOModifica.setLuogoNascita(descrizioneLuogoNascita);
   }
   else {
     pfVOModifica.setLuogoNascita(statoEsteroNascita);
   }
   pfVOModifica.setCittaNascita(cittaEsteroNascita);
   pfVOModifica.setResIndirizzo(indirizzoResidenza);
   pfVOModifica.setDescResProvincia(provinciaResidenza);
   pfVOModifica.setResProvincia(provinciaResidenza);
   pfVOModifica.setDescResComune(comuneResidenza);
   pfVOModifica.setResCAP(capResidenza);
   pfVOModifica.setDescStatoEsteroResidenza(statoEsteroResidenza);
   pfVOModifica.setStatoEsteroRes(statoEsteroResidenza);
   pfVOModifica.setNascitaCittaEstero(cittaEsteroNascita);
   pfVOModifica.setCittaResidenza(cittaEsteroResidenza);
   pfVOModifica.setResCittaEstero(cittaEsteroResidenza);
   pfVOModifica.setResTelefono(telefono);
   pfVOModifica.setResFax(fax);
   pfVOModifica.setNumeroCellulare(numeroCellulare);
   pfVOModifica.setResMail(mail);
   pfVOModifica.setDomIndirizzo(indirizzoDomicilio);
   pfVOModifica.setDomComune(comuneDomicilio);
   pfVOModifica.setDomCAP(capDomicilio);
   pfVOModifica.setDomProvincia(provinciaDomicilio);
   pfVOModifica.setDomicilioStatoEstero(statoEsteroDomicilio);
   pfVOModifica.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);
   pfVOModifica.setNote(note);

   Long idTitoloStudio = null;
   if(titoloStudio != null && !titoloStudio.equals("")) {
     idTitoloStudio = Long.decode(titoloStudio);
   }
   pfVOModifica.setIdTitoloStudio(idTitoloStudio);
   Long idIndirizzoStudio = null;
   if(indirizzoStudio != null && !indirizzoStudio.equals("")) {
     idIndirizzoStudio = Long.decode(indirizzoStudio);
   }
   pfVOModifica.setIdIndirizzoStudio(idIndirizzoStudio);
   // Effettuo il controllo formale dei dati inputati dall'utente
   ValidationErrors errors = pfVOModifica.validateModificaNuovoRappresentanteLegale();

   session.setAttribute("sesso", pfVOModifica.getSesso());

   // Se il titolo di studio prevede l'indirizzo, quest'ultimo è obbligatorio
   Vector<CodeDescription> indirizzi = null;
   if(pfVOModifica.getIdTitoloStudio() != null) 
   {
     try 
     {
       indirizzi = client.getIndirizzoStudioByTitolo(pfVOModifica.getIdTitoloStudio());
     }
     catch(SolmrException se) {
     }
   }

   if(indirizzi != null) 
   {
     if(indirizzi.size() > 0 && pfVOModifica.getIdTitoloStudio() != null) 
     {
       if(!Validator.isNotEmpty(pfVOModifica.getIdIndirizzoStudio())) 
       {
         errors.add("idIndirizzoStudio",new ValidationError((String)AnagErrors.get("ERR_INDIRIZZO_STUDIO_OBBLIGATORIO")));
       }
     }
   }

   if(dataNascita==null || dataNascita.equals("")) 
   {
     pfVOModifica.setNascitaData(null);
   }
   else 
   {
     pfVOModifica.setNascitaData(DateUtils.parseDate(dataNascita));
   }

   String codFiscaleComune = "";
   String etichettaStr = "";
   try 
   {
     if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") 
       || Validator.isNotEmpty(provinciaNascita)) 
     {
       etichettaStr = "descNascitaComune";
       codFiscaleComune = client.ricercaCodiceFiscaleComune(descrizioneLuogoNascita,provinciaNascita);
     }
     else 
     {
       etichettaStr = "nascitaStatoEstero";
       codFiscaleComune = client.ricercaCodiceFiscaleComune(statoEsteroNascita,"");
     }
   }
   catch(SolmrException se) 
   {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add(etichettaStr, error);
   }
   // Recupero i codici istat dei comuni
   // NASCITA
   String istatComuneNascita = "";
   String istatEsteroNascita = "";
   etichettaStr = "";
   try 
   {
     if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("") ||
        provinciaNascita != null && !provinciaNascita.equals("")) 
     {
       etichettaStr = "descNascitaComune";
       istatComuneNascita = client.ricercaCodiceComune(descrizioneLuogoNascita,provinciaNascita);
     }
     else 
     {
       etichettaStr = "nascitaStatoEstero";
       istatEsteroNascita = client.ricercaCodiceComune(statoEsteroNascita,"");
     }
   }
   catch(SolmrException se) 
   {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add(etichettaStr, error);
   }
   if(!istatComuneNascita.equals("")) 
   {
     pfVOModifica.setNascitaComune(istatComuneNascita);
   }
   else 
   {
     pfVOModifica.setNascitaStatoEstero(istatEsteroNascita);
   }
   // RESIDENZA
   String istatComuneResidenza = "";
   etichettaStr = "";
   try 
   {
     if(provinciaResidenza != null && !provinciaResidenza.equals("") 
       || comuneResidenza != null && !comuneResidenza.equals("")) 
     {
       etichettaStr = "descResComune";
       istatComuneResidenza = client.ricercaCodiceComuneNonEstinto(comuneResidenza,provinciaResidenza);
     }
     else 
     {
       etichettaStr = "statoEsteroRes";
       istatComuneResidenza = client.ricercaCodiceComuneNonEstinto(statoEsteroResidenza,"");
     }
   }
   catch(SolmrException se) 
   {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add(etichettaStr, error);
   }

   pfVOModifica.setResComune(istatComuneResidenza);

   // Controllo che il domicilio inserito dall'utente sia valido
   String istatComuneDomicilio = null;
   if((comuneDomicilio != null && !comuneDomicilio.equals("")) 
     || (provinciaDomicilio != null && !provinciaDomicilio.equals(""))) 
   {
     try 
     {
       istatComuneDomicilio = client.ricercaCodiceComuneNonEstinto(comuneDomicilio,provinciaDomicilio);
     }
     catch(SolmrException se) 
     {
       ValidationError error = new ValidationError(se.getMessage());
       errors.add("domComune", error);
     }
   }
   if(Validator.isNotEmpty(statoEsteroDomicilio)) 
   {
     try 
     {
       istatComuneDomicilio = client.ricercaCodiceComuneNonEstinto(statoEsteroDomicilio ,"");
     }
     catch(SolmrException se) 
     {
       ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
       errors.add("domicilioStatoEstero", error);
     }
   }
   pfVOModifica.setIstatComuneDomicilio(istatComuneDomicilio);

   session.setAttribute("pfVOModifica",pfVOModifica);
   if(errors != null && errors.size() != 0) 
   {
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }
   // Controllo della coerenza del codice fiscale non
   try 
   {
     Validator.verificaCf(pfVOModifica.getNome(),pfVOModifica.getCognome(),
                          pfVOModifica.getSesso(),pfVOModifica.getNascitaData(),
                          codFiscaleComune, pfVOModifica.getCodiceFiscale());
   }
   catch(CodiceFiscaleException ce) 
   {
     if(ce.getNome()) 
     {
       errors.add("nome",new ValidationError(""+AnagErrors.get("ERR_NOME_CODICE_FISCALE")));
     }
     else if(ce.getCognome()) 
     {
       errors.add("cognome",new ValidationError(""+AnagErrors.get("ERR_COGNOME_CODICE_FISCALE")));
     }
     else if(ce.getAnnoNascita()) 
     {
       errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_ANNO_NASCITA_CODICE_FISCALE")));
     }
     else if(ce.getMeseNascita()) 
     {
       errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_MESE_NASCITA_CODICE_FISCALE")));
     }
     else if(ce.getGiornoNascita()) 
     {
       errors.add("strNascitaData",new ValidationError(""+AnagErrors.get("ERR_GIORNO_NASCITA_CODICE_FISCALE")));
     }
     else if(ce.getComuneNascita()) 
     {
       errors.add("descNascitaComune",new ValidationError(""+AnagErrors.get("ERR_COMUNE_NASCITA_CODICE_FISCALE")));
     }
     else if(ce.getSesso()) 
     {
       errors.add("sesso",new ValidationError(""+AnagErrors.get("ERR_SESSO_CODICE_FISCALE")));
     }
     else 
     {
       errors.add("codiceFiscale",new ValidationError(ce.getMessage()+": impossibile procedere"));
     }
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }
   session.removeAttribute("sesso");

   // Se l'utente ha modificato i dati della residenza(indirizzo, provincia, comune, cap
   // o indirizzo ,stato estero e città) chiedo all'utente se intende storicizzare o effettuare
   // solo una correzione di errori.

   // Recupero la persona fisica presente su DB che si intende modificare partendo da id_azienda
   PersonaFisicaVO oldPersonaFisicaVO = null;
   try 
   {
     oldPersonaFisicaVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
   }
   catch(SolmrException se) 
   {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   PersonaFisicaVO personaForBonificaVO = null;
   try 
   {
     personaForBonificaVO = client.getPersonaFisica(pfVOModifica.getCodiceFiscale().toUpperCase());
   }
   catch(SolmrException se) {}

   // Se non trovo un'altra persona con quel codice fiscale allora controllo che non sia stata modificata
   // l'identità della persona e cioè non siano stati modificati(cognome, nome, data di nascita e comune
   // di nascita)
   if(personaForBonificaVO == null) 
   {

     if(pfVOModifica.isEqualIdentity(oldPersonaFisicaVO)) 
     {
       // Effettuo la modifica su DB_PERSONA_FISICA ed eventualmente storicizzo la residenza su
       // DB_STORICO_RESIDENZA
       pfVOModifica.setIdPersonaFisica(oldPersonaFisicaVO.getIdPersonaFisica());
       try 
       {
         // Se la data di inizio  residenza è diversa da quella di sistema allora confronto la persona
         // fisica prelevata dal DB con la nuova persona fisica derivante dai nuovi dati inseriti come input
         // dall'utente.
         if(Validator.isNotEmpty(oldPersonaFisicaVO.getDataInizioResidenza())
          && (oldPersonaFisicaVO.getDataInizioResidenza()
           .compareTo(DateUtils.parseDate(DateUtils.getCurrent())) != 0)) 
         {
           // Se è diversa la mando ad una pagina che richiede se l'utente intende effettuare una modifica o una correzione
           if(!pfVOModifica.equalsResidenza(oldPersonaFisicaVO)) {
             request.setAttribute("urlForm", urlForm);
             %>
              <jsp:forward page =  "<%= richiestaModificaUrl %>" />
             <%
             return;
           }
           else 
           {
             client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
           }
         }
         else 
         {
           client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
         }
       }
       catch(SolmrException se) 
       {
         ValidationError error = new ValidationError(se.getMessage());
         errors.add("error", error);
         request.setAttribute("errors", errors);
         request.getRequestDispatcher(url).forward(request, response);
         return;
       }
     }
     // Altrimenti cesso su DB_CONTITOLARE il ruolo del vecchio soggetto e inserisco un nuovo soggetto
     // legandolo all'azienda con un ruolo attivo
     else 
     {

       client.cessaLegameBetweenPersonaAndAzienda(oldPersonaFisicaVO.getIdSoggetto(), anagAziendaVO.getIdAzienda());
       client.insertRappLegaleTitolare(anagAziendaVO.getIdAzienda(), pfVOModifica, ruoloUtenza.getIdUtente());
     }

     PersonaFisicaVO personaModVO = client.getTitolareORappresentanteLegaleAzienda(anagAziendaVO.getIdAzienda(), DateUtils.parseDate(anagAziendaVO.getDataSituazioneAlStr()));
     session.removeAttribute("personaVO");
     session.setAttribute("personaVO", personaModVO);
   }
   // Se invece la trova sostituisce il vecchio soggetto con il nuovo in tutti i legami con le aziende
   // ed effettue le modifiche su DB_PERSONA_FISICA ed eventualmente la storicizzazione della residenza
   // su DB_STORICO_RESIDENZA
   else 
   {

     // Se la data di inizio  residenza è diversa da quella di sistema allora confronto la persona
     // fisica prelevata dal DB con la nuova persona fisica derivante dai nuovi dati inseriti come input
     // dall'utente.
     if(Validator.isNotEmpty(personaForBonificaVO.getDataInizioResidenza())
       && (personaForBonificaVO.getDataInizioResidenza().compareTo(DateUtils.parseDate(DateUtils.getCurrent())) != 0)) 
     {
       // Se è diversa la mando ad una pagina che richiede se l'utente intende effettuare una modifica o una correzione
       if(!pfVOModifica.equalsResidenza(oldPersonaFisicaVO)) 
       {
         request.setAttribute("urlForm", urlForm);
         %>
          <jsp:forward page =  "<%= richiestaModificaUrl %>" />
         <%
         return;
       }
       else if(!pfVOModifica.equalsResidenza(personaForBonificaVO)) 
       {
         request.setAttribute("urlForm", urlForm);
         %>
          <jsp:forward page =  "<%= richiestaModificaUrl %>" />
         <%
         return;
       }
       else 
       {
         try 
         {
        	 //anagAziendaVO.getIdAzienda()
           client.changeLegameBetweenPersoneAndAziende(personaForBonificaVO.getIdSoggetto(),
                                                       oldPersonaFisicaVO.getIdSoggetto(), anagAziendaVO.getIdAzienda());
           pfVOModifica.setIdPersonaFisica(personaForBonificaVO.getIdPersonaFisica());
           pfVOModifica.setIdSoggetto(personaForBonificaVO.getIdSoggetto());
           client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
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
     else {
       try {
         client.changeLegameBetweenPersoneAndAziende(personaForBonificaVO.getIdSoggetto(),
                                                     oldPersonaFisicaVO.getIdSoggetto(), anagAziendaVO.getIdAzienda());
         /////////////////////////////////////////////////////
         pfVOModifica.setIdPersonaFisica(personaForBonificaVO.getIdPersonaFisica());
         pfVOModifica.setIdSoggetto(personaForBonificaVO.getIdSoggetto());
         client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
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

   // Recupero il titolare e vado alla pagina di dettaglio
   try {
     personaVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
   }
   catch(SolmrException se) {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }
   session.setAttribute("personaVO", personaVO);
   %>
     <jsp:forward page = "/view/rappLegaleView.jsp" />
   <%
 }

 // Arrivo dalla pagina che chiede all'utente se intende effettuare solo una modifica o anche una storicizzazione
 // dei dati della persona.
 // Se l'utente ha scelto di modificare
 if(request.getParameter("operazione") != null 
   && request.getParameter("operazione").equals("modificaResidenzaSoggetto")) 
 {


   ValidationErrors errors = new ValidationErrors();

   // Recupero i dati inseriti dall'utente
   PersonaFisicaVO pfVOModifica = (PersonaFisicaVO)session.getAttribute("pfVOModifica");
   pfVOModifica.setIsModificaResidenza(true);
   pfVOModifica.setIsStoricizzaResidenza(false);

   // Recupero la persona fisica presente su DB che si intende modificare partendo da id_azienda
   PersonaFisicaVO oldPersonaFisicaVO = null;
   try 
   {
     oldPersonaFisicaVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
   }
   catch(SolmrException se) 
   {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   PersonaFisicaVO personaForBonificaVO = null;
   try 
   {
     personaForBonificaVO = client.getPersonaFisica(pfVOModifica.getCodiceFiscale().toUpperCase());
   }
   catch(SolmrException se) 
   {}

   try 
   {
     if(personaForBonificaVO == null) 
     {
       client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
     }
     else 
     {
       pfVOModifica.setIdPersonaFisica(personaForBonificaVO.getIdPersonaFisica());
       pfVOModifica.setIdSoggetto(personaForBonificaVO.getIdSoggetto());
       client.changeLegameBetweenPersoneAndAziende(personaForBonificaVO.getIdSoggetto(), oldPersonaFisicaVO.getIdSoggetto(), anagAziendaVO.getIdAzienda());
       client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
     }
   }
   catch(SolmrException se) 
   {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   PersonaFisicaVO personaModVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
   session.removeAttribute("personaVO");
   session.setAttribute("personaVO", personaModVO);

   %>
    <jsp:forward page = "<%= rappresentanteLegaleUrl %>" />
   <%
 }

 // Se invece l'utente ha scelto di storicizzare
 if(request.getParameter("operazione") != null 
   && request.getParameter("operazione").equals("storicizzaResidenzaSoggetto")) 
 {

   ValidationErrors errors = new ValidationErrors();

   // Recupero i dati inseriti dall'utente
   PersonaFisicaVO pfVOModifica = (PersonaFisicaVO)session.getAttribute("pfVOModifica");
   pfVOModifica.setIsModificaResidenza(false);
   pfVOModifica.setIsStoricizzaResidenza(true);

   // Recupero la persona fisica presente su DB che si intende modificare partendo da id_azienda
   PersonaFisicaVO oldPersonaFisicaVO = null;
   try 
   {
     oldPersonaFisicaVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
   }
   catch(SolmrException se) 
   {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   PersonaFisicaVO personaForBonificaVO = null;
   try 
   {
     personaForBonificaVO = client.getPersonaFisica(pfVOModifica.getCodiceFiscale().toUpperCase());
   }
   catch(SolmrException se1) {
   }


   // Effettuo la storicizzazione
   try 
   {
     if(personaForBonificaVO == null) 
     {
       client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
     }
     else 
     {
       pfVOModifica.setIdPersonaFisica(personaForBonificaVO.getIdPersonaFisica());
       pfVOModifica.setIdSoggetto(personaForBonificaVO.getIdSoggetto());
       client.changeLegameBetweenPersoneAndAziende(personaForBonificaVO.getIdSoggetto(), oldPersonaFisicaVO.getIdSoggetto(), anagAziendaVO.getIdAzienda());
       client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
     }
     session.removeAttribute("personaVO");
   }
   catch(SolmrException se) 
   {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   PersonaFisicaVO personaModVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
   session.removeAttribute("personaVO");
   session.setAttribute("personaVO", personaModVO);

   %>
    <jsp:forward page = "<%= rappresentanteLegaleUrl %>"/>
   <%
 }

 // L'utente ha selezionato il pulsante annulla dalla pagina che richiede all'utente se intende modificare
 // o storicizzare i dati della persona
 if(request.getParameter("indietroModificaResidenza") != null) {
   %>
      <jsp:forward page = "<%= url %>" />
   <%
 }

 if(request.getParameter("annulla")!=null){
   %>
   <jsp:forward page = "/view/rappLegaleView.jsp" />
   <%
 }

 // L'utente ha modificato il titolo di Studio
 if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("cambioTitoloStudio")) {

   PersonaFisicaVO personaVO = (PersonaFisicaVO)session.getAttribute("personaVO");
   session.removeAttribute("pfVOModifica");
   PersonaFisicaVO pfVOModifica = new PersonaFisicaVO();

   String codiceFiscale = request.getParameter("codiceFiscale");
   pfVOModifica.setCodiceFiscale(codiceFiscale);

   // setto i campi non modificabili in tale operazione
   if(personaVO!=null){
     pfVOModifica.setIdPersonaFisica(personaVO.getIdPersonaFisica());
   }
   else
     pfVOModifica.setIdPersonaFisica(null);

   // recupero dalla view i dati modificati
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
   String numeroCellulare = request.getParameter("numeroCellulare");
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

   pfVOModifica.setCognome(cognome);
   pfVOModifica.setNome(nome);
   pfVOModifica.setSesso(sesso);
   pfVOModifica.setStrNascitaData(dataNascita);
   pfVOModifica.setNascitaProv(provinciaNascita);
   pfVOModifica.setDescNascitaComune(descrizioneLuogoNascita);
   pfVOModifica.setNascitaStatoEstero(statoEsteroNascita);
   if(descrizioneLuogoNascita != null && !descrizioneLuogoNascita.equals("")) {
     pfVOModifica.setLuogoNascita(descrizioneLuogoNascita);
   }
   else {
     pfVOModifica.setLuogoNascita(statoEsteroNascita);
   }
   pfVOModifica.setCittaNascita(cittaEsteroNascita);
   pfVOModifica.setResIndirizzo(indirizzoResidenza);
   pfVOModifica.setDescResProvincia(provinciaResidenza);
   pfVOModifica.setResProvincia(provinciaResidenza);
   pfVOModifica.setDescResComune(comuneResidenza);
   pfVOModifica.setResCAP(capResidenza);
   pfVOModifica.setDescStatoEsteroResidenza(statoEsteroResidenza);
   pfVOModifica.setStatoEsteroRes(statoEsteroResidenza);
   pfVOModifica.setNascitaCittaEstero(cittaEsteroNascita);
   pfVOModifica.setCittaResidenza(cittaEsteroResidenza);
   pfVOModifica.setResTelefono(telefono);
   pfVOModifica.setResFax(fax);
   pfVOModifica.setNumeroCellulare(numeroCellulare);
   pfVOModifica.setResMail(mail);
   pfVOModifica.setDomIndirizzo(indirizzoDomicilio);
   pfVOModifica.setDomComune(comuneDomicilio);
   pfVOModifica.setDomCAP(capDomicilio);
   pfVOModifica.setDomProvincia(provinciaDomicilio);
   pfVOModifica.setDomicilioStatoEstero(statoEsteroDomicilio);
   pfVOModifica.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);
   pfVOModifica.setNote(note);
   Long idTitoloStudio = null;
   if(titoloStudio != null && !titoloStudio.equals("")) {
     idTitoloStudio = Long.decode(titoloStudio);
   }
   pfVOModifica.setIdTitoloStudio(idTitoloStudio);
   session.setAttribute("pfVOModifica",pfVOModifica);
   %>
      <jsp:forward page= "<%= url %>" />
   <%
  }

  // Arrivo dalla pagina che chiede all'utente se intende effettuare solo una modifica o anche una storicizzazione
 // dei dati della persona.
 // Se l'utente ha scelto di modificare
 if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("modificaResidenzaSoggetto")) {


   ValidationErrors errors = new ValidationErrors();

   // Recupero i dati inseriti dall'utente
   PersonaFisicaVO pfVOModifica = (PersonaFisicaVO)session.getAttribute("pfVOModifica");

   // Effettuo la modifica
   try {
     client.updateRappLegale(pfVOModifica, ruoloUtenza.getIdUtente());
     PersonaFisicaVO personaModVO = client.getTitolareORappresentanteLegaleAzienda(anagAziendaVO.getIdAzienda(), DateUtils.parseDate(anagAziendaVO.getDataSituazioneAlStr()));
     session.removeAttribute("personaVO");
     session.setAttribute("personaVO", personaModVO);
   }
   catch(SolmrException se) {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   %>
      <jsp:forward page = "/view/rappLegaleView.jsp" />
   <%
 }
 // Se invece l'utente ha scelto di storicizzare
 if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("storicizzaResidenzaSoggetto")) {

   ValidationErrors errors = new ValidationErrors();

   // Recupero i dati inseriti dall'utente
   PersonaFisicaVO pfVOModifica = (PersonaFisicaVO)session.getAttribute("pfVOModifica");

   // Effettuo la storicizzazione
   try {
     client.storicizzaDatiResidenza(pfVOModifica, ruoloUtenza.getIdUtente());
     session.removeAttribute("personaVO");
     PersonaFisicaVO personaModVO = client.getTitolareORappresentanteLegaleAzienda(anagAziendaVO.getIdAzienda(), DateUtils.parseDate(anagAziendaVO.getDataSituazioneAlStr()));
     session.setAttribute("personaVO", personaModVO);
   }
   catch(SolmrException se) {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }

   %>
      <jsp:forward page = "/view/rappLegaleView.jsp" />
   <%
 }
 // L'utente ha selezionato il pulsante annulla dalla pagina che richiede all'utente se intende modificare
 // o storicizzare i dati della persona
 if(request.getParameter("indietroModificaResidenza") != null) {
   %>
      <jsp:forward page = "<%= url %>" />
   <%
 }
 // E' la prima volta che entro
 else{


   // recupero dalla sessione i dati del rappresentante legale
   session.removeAttribute("pfVOModifica");
   session.removeAttribute("isOk");
   PersonaFisicaVO pfVO = (PersonaFisicaVO)session.getAttribute("personaVO");

   ValidationErrors errors = new ValidationErrors();
   url = "/view/rappLegaleView.jsp";
   try {
     pfVO = client.getRappresentanteLegaleFromIdAnagAzienda(anagAziendaVO.getIdAnagAzienda());
     session.setAttribute("personaVO",pfVO);
   }
   catch (SolmrException ex) {
     if(ex.getMessage().equals(""+AnagErrors.get("NESSUN_RAPPRESENTANTE_LEGALE"))){
       url = "/view/anagraficaView.jsp";
     }

     ValidationError error = new ValidationError(ex.getMessage());
     errors.add("error", error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(url).forward(request, response);
     return;
   }
   String isOk = "false";
   if(!Validator.isNotEmpty(pfVO.getCodiceFiscale()) || !Validator.controlloCf(pfVO.getCodiceFiscale())
      || SolmrConstants.FLAG_N.equalsIgnoreCase(pfVO.getFlagCfOk()))
   {
     isOk = "true";
   }
   else 
   {
     isOk = "false";
   }
   session.setAttribute("isOk", isOk);
   session.setAttribute("pfVOModifica",pfVO);
   %>
    <jsp:forward page = "/view/modificaRappLegaleView.jsp" />
   <%
 }
%>

