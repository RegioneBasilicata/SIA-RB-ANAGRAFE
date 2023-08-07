<%@ page language="java"
    contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.DateUtils" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<jsp:useBean id="personaVO" scope="page" class="it.csi.solmr.dto.anag.PersonaFisicaVO">
<jsp:setProperty name="personaVO" property="*" />
</jsp:useBean>

<%!

  private static final String validateUrl="/view/anagraficaSocieta_cambiaRappresentanteView.jsp";
  private static final String DETTAGLIO="../ctrl/anagraficaCtrl.jsp";

%>

<%

  String iridePageName = "anagraficaSocieta_cambiaRappresentanteCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  request.setAttribute("rappresentanteVO", personaVO);

  String anagUrl="/ctrl/anagraficaCtrl.jsp";
  String urlForm = "../layout/anagraficaSocieta_cambiaRappresentante.htm";
  String richiestaModificaUrl = "/view/confirmModResidenzaView.jsp";
  String url = "/view/anagraficaSocieta_cambiaRappresentanteView.jsp";

  AnagFacadeClient client = new AnagFacadeClient();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  // FINE
  ValidationException valEx  = null;
  String codiceFiscale=personaVO.getCodiceFiscale();
  String dataInizioCaricoStr=request.getParameter("dataInizioCarico");
  Date dataInizioCarico=null;
  if (codiceFiscale!=null) {
    codiceFiscale=codiceFiscale.toUpperCase();
    personaVO.setCodiceFiscale(codiceFiscale);
  }
  if (request.getParameter("annulla.x")!=null) {
    response.sendRedirect("../layout/anagrafica.htm");
    return;
  }
  if (request.getParameter("cerca.x")!=null) {
    try {
      personaVO = client.getPersonaFisica(codiceFiscale);
      if (personaVO.getNascitaData()!=null) {
        personaVO.setStrNascitaData(DateUtils.formatDate(personaVO.getNascitaData()));
      }
      session.setAttribute("personaVO", personaVO);
      session.removeAttribute("dataInizioCarico");
      request.setAttribute("rappresentanteVO", personaVO);
      request.setAttribute("resultAnagAziendaVO",anagVO);
    }
    catch (SolmrException ex) 
    {
      String msg=ex.getMessage();
      if (AnagErrors.CUAA_INESISTENTE.equalsIgnoreCase(msg)) 
      {
        String cuaa=request.getParameter("codiceFiscale").toUpperCase();
        personaVO=new PersonaFisicaVO();
        personaVO.setCodiceFiscale(cuaa);
        request.setAttribute("rappresentanteVO", personaVO);

        if (Validator.controlloCf(cuaa)) 
        {
          ComuneVO comuneVO=null;
          try 
          {
            comuneVO=client.getComuneByCUAA(cuaa.substring(11,15));
          }
          catch(Exception e) {}
          if (comuneVO!=null) 
          {
            personaVO.setNascitaComune(comuneVO.getIstatComune());
            if (it.csi.solmr.etc.SolmrConstants.FLAG_S.equalsIgnoreCase(comuneVO.getFlagEstero())) 
            {
              personaVO.setNascitaStatoEstero(comuneVO.getDescom());
            }
            else {
              personaVO.setDescNascitaComune(comuneVO.getDescom());
              personaVO.setNascitaProv(comuneVO.getSiglaProv());
            }
          }
          setDataSessoPersonaVO(codiceFiscale,personaVO);
        }
        else 
        {
          valEx = new ValidationException("Codice fiscale non valido",validateUrl);
          valEx.addMessage("Codice fiscale non valido","exception");
          throw  valEx;
        }
        personaVO.setCodiceFiscale(cuaa);
        request.setAttribute("rappresentanteVO", personaVO);
      }
      else 
      {
        valEx = new ValidationException("Codice fiscale non trovato", validateUrl);
        valEx.addMessage("Codice fiscale non trovato","exception");
        throw  valEx;
      }
    }
    %>
      <jsp:forward page="<%=validateUrl%>"/>
    <%
  }

  else {
    if (request.getParameter("salva.x")!=null || CONTROLLO_CF_OK.equalsIgnoreCase(request.getParameter("controlloCF"))) {

      personaVO.setDescStatoEsteroResidenza(request.getParameter("statoEsteroRes"));

      codiceFiscale = request.getParameter("codiceFiscale");
      // Recupero i dati relativi al domicilio
      String cittaEsteroResidenza = request.getParameter("resCittaEstero");
      String indirizzoDomicilio = request.getParameter("domIndirizzo");
      String comuneDomicilio = request.getParameter("domComune");
      String capDomicilio = request.getParameter("domCAP");
      String provinciaDomicilio = request.getParameter("domProvincia");
      String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
      String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");

      // Setto i valori all'interno del VO
      personaVO.setDomIndirizzo(indirizzoDomicilio);
      personaVO.setDomComune(comuneDomicilio);
      personaVO.setDomCAP(capDomicilio);
      personaVO.setDomProvincia(provinciaDomicilio);
      personaVO.setDomicilioStatoEstero(statoEsteroDomicilio);
      personaVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);
      personaVO.setResCittaEstero(cittaEsteroResidenza);

      personaVO.setCodiceFiscale(codiceFiscale);

      it.csi.solmr.util.ValidationErrors errors=personaVO.validateModificaNuovoRappresentanteLegale();
      if (errors!=null && errors.get("strNascitaData")==null) {
        if (personaVO.getStrNascitaData()!=null && personaVO.getStrNascitaData().trim().length()!=10) {
          errors.add("strNascitaData",new ValidationError("Inserire una data in formato gg/mm/aaaa"));
        }
      }

      if (!Validator.isNotEmpty(dataInizioCaricoStr)) {
        errors.add("dataInizioCarico",new ValidationError("Inserire una data"));
      }
      else {
        if (!Validator.isDate(dataInizioCaricoStr)) {
          errors.add("dataInizioCarico", new ValidationError("Formato della data non corretto. Inserire data in formato gg/mm/aaaa"));
        }
        else {
          try {
            dataInizioCarico=DateUtils.parseDate(dataInizioCaricoStr);
            Date today=new Date();
            if(dataInizioCarico != null) {
              session.setAttribute("dataInizioCarico", dataInizioCarico);
            }
            PersonaFisicaVO rappresentanteCorrente = null;
            if(rappresentanteCorrente == null) {
              try {
                rappresentanteCorrente = client.getRappresentanteLegaleFromIdAnagAzienda(anagVO.getIdAnagAzienda());
              }
              catch(SolmrException se) {
                ValidationError error = new ValidationError(se.getMessage());
                errors.add("error", error);
                request.setAttribute("errors", errors);
                request.getRequestDispatcher(url).forward(request, response);
                return;
              }
            }

            Date inizioRuolo=rappresentanteCorrente.getDataInizioRuolo();
            if (dataInizioCaricoStr.trim().length()!=10) {
              errors.add("dataInizioCarico",new ValidationError("Inserire una data nel formato gg/mm/aaaa"));
            }
            else {
              if(inizioRuolo != null) {
                if (dataInizioCarico.compareTo(today) > 0 || dataInizioCarico.before(inizioRuolo)) {
                  errors.add("dataInizioCarico",new ValidationError("La data deve essere compresa tra "+
                                                                     DateUtils.formatDate(inizioRuolo)+" e "+
                                                                     DateUtils.formatDate(today)));
                }
              }
            }
          }
          catch(SolmrException e) {
            valEx = new ValidationException("Codice fiscale non trovato",validateUrl);
            valEx.addMessage(e.getMessage(),"exception");
            throw valEx;
          }
        }
      }

      checkComuniStati(personaVO,client,errors);
      if (errors.get("descNascitaComune")==null && errors.get("nascitaProv")==null &&
          errors.get("nascitaStatoEstero")==null && errors.get("strNascitaData")==null &&
          errors.get("cognome")==null && errors.get("nome")==null) {
        String nascitaComuneCUAA=null;
        nascitaComuneCUAA=client.getComuneByISTAT(personaVO.getNascitaComune()).getCodfisc();
        try {
        Validator.verificaCf(personaVO.getNome(),
                             personaVO.getCognome(),
                             personaVO.getSesso(),
                             personaVO.getNascitaData(),
                             nascitaComuneCUAA,
                             codiceFiscale);
        }
        catch(it.csi.solmr.exception.CodiceFiscaleException ce) {
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
            errors.add("codiceFiscale",new ValidationError(ce.getMessage()));
          }
          request.setAttribute("errors", errors);
        }
      }

      // Informazioni aggiuntive relative al titolo di studio
      String titoloStudio = request.getParameter("idTitoloStudio");
      String indirizzoStudio = request.getParameter("idIndirizzoStudio");
      Long idTitoloStudio = null;
      if(titoloStudio != null && !titoloStudio.equals("")) {
        idTitoloStudio = Long.decode(titoloStudio);
      }
      personaVO.setIdTitoloStudio(idTitoloStudio);
      Long idIndirizzoStudio = null;
      if(indirizzoStudio != null && !indirizzoStudio.equals("")) {
        idIndirizzoStudio = Long.decode(indirizzoStudio);
      }
      personaVO.setIdIndirizzoStudio(idIndirizzoStudio);

      // Se il titolo di studio prevede l'indirizzo, quest'ultimo è obbligatorio
      Vector indirizzi = null;
      try {
        if(personaVO.getIdTitoloStudio() != null) {
          indirizzi = client.getIndirizzoStudioByTitolo(personaVO.getIdTitoloStudio());
        }
      }
      catch(SolmrException se) {
      }

      if(indirizzi != null) {
        if(indirizzi.size() > 0 && personaVO.getIdTitoloStudio() != null) {
          if(!Validator.isNotEmpty(personaVO.getIdIndirizzoStudio())) {
            errors.add("idIndirizzoStudio",new ValidationError((String)AnagErrors.get("ERR_INDIRIZZO_STUDIO_OBBLIGATORIO")));
          }
        }
      }

      // Controllo che l'utente abbia inserito un valore corretto relativo al domicilio
    String istatComuneDomicilio = null;
    if((comuneDomicilio != null && !comuneDomicilio.equals("")) || (provinciaDomicilio != null && !provinciaDomicilio.equals(""))) {
      try {
        istatComuneDomicilio = client.ricercaCodiceComuneNonEstinto(comuneDomicilio,provinciaDomicilio);
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("domComune", error);
      }
    }
    if(Validator.isNotEmpty(statoEsteroDomicilio)) {
      try {
        istatComuneDomicilio = client.ricercaCodiceComuneNonEstinto(statoEsteroDomicilio ,"");
      }
      catch(SolmrException se) {
        ValidationError error = new ValidationError((String)AnagErrors.get("ERR_STATO_ESTERO_NO_VALIDO"));
        errors.add("domicilioStatoEstero", error);
      }
    }
    personaVO.setIstatComuneDomicilio(istatComuneDomicilio);


      if (errors.size()!=0) {
        request.setAttribute("errors",errors);
        %>
           <jsp:forward page="<%=validateUrl%>"/>
        <%
        return;
      }
      else {
        session.setAttribute("personaRappresentanteVO", personaVO);
        // Controllo che la persona inserita dall'utente non esista già in archivio
        PersonaFisicaVO rappresentante = null;
        try {
          rappresentante = client.getPersonaFisica(personaVO.getCodiceFiscale().toUpperCase());
        }
        catch(SolmrException se) {
        }

        if(rappresentante != null) 
        {
          if(!rappresentante.isNewPersonaFisica()) 
          {
            if(Validator.isNotEmpty(rappresentante.getDataInizioResidenza())
              && (rappresentante.getDataInizioResidenza().compareTo(DateUtils.parseDate(DateUtils.getCurrent())) != 0)) 
            {
              if(!personaVO.equalsResidenza(rappresentante)) {
                request.setAttribute("urlForm", urlForm);
                session.removeAttribute("sesso");
                %>
                  <jsp:forward page="<%= richiestaModificaUrl %>"/>
                <%
                  return;
              }
            }
          }
        }

        personaVO.setDataInizioRuolo(dataInizioCarico);
        client.cambiaRappresentanteLegale(anagVO.getIdAzienda(),personaVO,ruoloUtenza.getIdUtente());
        session.setAttribute("personaVO",personaVO);
        response.sendRedirect("../layout/anagrafica.htm");
        return;
      }
    }

    // Arrivo dalla pagina di richiesta modifica/storicizzazione della persona fisica
    // L'utente ha scelto di modificare la persona fisica
    if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("modificaResidenzaSoggetto")) {

      PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("personaRappresentanteVO");
      personaFisicaVO.setIsModificaResidenza(true);
      personaFisicaVO.setIsStoricizzaResidenza(false);

      dataInizioCarico = (Date)session.getAttribute("dataInizioCarico");
      personaFisicaVO.setDataInizioRuolo(dataInizioCarico);

      try {
        client.cambiaRappresentanteLegale(anagVO.getIdAzienda(),personaFisicaVO,ruoloUtenza.getIdUtente());
      }
      catch(SolmrException se) {}

      session.setAttribute("personaVO",personaVO);
      %>
        <jsp:forward page="<%= anagUrl %>"/>
      <%
      return;
    }
    // L'utente ha scelto di storicizzare la persona fisica
    if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("storicizzaResidenzaSoggetto")) {
      PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("personaRappresentanteVO");
      personaFisicaVO.setIsModificaResidenza(false);
      personaFisicaVO.setIsStoricizzaResidenza(true);

      dataInizioCarico = (Date)session.getAttribute("dataInizioCarico");
      personaFisicaVO.setDataInizioRuolo(dataInizioCarico);
      try {
        client.cambiaRappresentanteLegale(anagVO.getIdAzienda(),personaFisicaVO,ruoloUtenza.getIdUtente());
      }
      catch(SolmrException se) {
      }

      session.setAttribute("personaVO",personaVO);
      %>
        <jsp:forward page="<%= anagUrl %>"/>
      <%
      return;
    }
    // l'utente ha selezionato il pulsante indietro dalla pagina di richiesta modifica/storicizzazione
    // della persona fisica
    if(request.getParameter("indietroModificaResidenza") != null) {
      %>
        <jsp:forward page="<%= url %>"/>
      <%
    }


    if(request.getParameter("operazione") != null && request.getParameter("operazione").equals("cambioTitoloStudio")) {

      // Recupero i dati relativi al domicilio
      String statoEsteroDomicilio = request.getParameter("domicilioStatoEstero");
      String cittaEsteroDomicilio = request.getParameter("descCittaEsteroDomicilio");

      // Setto i valori all'interno del VO
      personaVO.setDomicilioStatoEstero(statoEsteroDomicilio);
      personaVO.setDescCittaEsteroDomicilio(cittaEsteroDomicilio);

      // Informazioni aggiuntive relative al titolo di studio
      String titoloStudio = request.getParameter("idTitoloStudio");
      Long idTitoloStudio = null;
      if(titoloStudio != null && !titoloStudio.equals("")) {
        idTitoloStudio = Long.decode(titoloStudio);
      }
      personaVO.setIdTitoloStudio(idTitoloStudio);
      request.setAttribute("rappresentanteVO", personaVO);
      %>
         <jsp:forward page= "<%= validateUrl %>" />
      <%
    }
    else {
      session.removeAttribute("personaRappresentanteVO");
      session.removeAttribute("dataInizioCarico");
      PersonaFisicaVO pfVO=null;

      if (pfVO==null)

      {

        try

        {

          pfVO=client.getTitolareORappresentanteLegaleAzienda(anagVO.getIdAzienda(), new Date());

        }

        catch(Exception e)

        {

          if (AnagErrors.NESSUN_RAPPRESENTANTE_LEGALE.equals(e.getMessage()))

          {

            throwValidation(e.getMessage(),DETTAGLIO);

          }

          else

          {

            throwValidation(e.getMessage(), validateUrl);

          }

        }

        //session.setAttribute("personaVO",pfVO);

      }

      if (pfVO==null || pfVO.getDataFineRuolo()!=null) {

        throwValidation("Impossibile modificare il rappresentante legale in quanto quello "+

                        "attualmente selezionato non è più incarico. Per modificare il "+

                        "rappresentante legale fare riferimento ad una situazione azienda "+

                        "relativa ad una data più recente", DETTAGLIO);

      }

      %><jsp:forward page="<%=validateUrl%>"/><%

    }

  }

%>





<%!

private static String mesiCf = "ABCDEHLMPRST";

private static String CONTROLLO_CF_OK="true";



public void checkComuniStati(PersonaFisicaVO personaVO,AnagFacadeClient client,ValidationErrors errors) throws Exception {

  if (errors.get("descNascitaComune")==null && errors.get("nascitaProv")==null &&
      errors.get("nascitaStatoEstero")==null) {
    String nascitaStato=personaVO.getNascitaStatoEstero();
    if (nascitaStato==null) {
      try {
        personaVO.setNascitaComune(client.ricercaCodiceComune(personaVO.getDescNascitaComune(),personaVO.getNascitaProv()));
      }
      catch(SolmrException e) {
          errors.add("descNascitaComune",new ValidationError("Comune e/o provincia di nascita inesistenti"));
          errors.add("nascitaProv",new ValidationError("Comune e/o provincia di nascita inesistenti"));
        }
    }
    else {
      try {
        //Vector stati=client.ricercaStatoEstero(personaVO.getNascitaStatoEstero());
        String istatStatoEstero = client.ricercaCodiceComune(nascitaStato, "");
        personaVO.setNascitaComune(istatStatoEstero);
      }
      catch(SolmrException e) {
        errors.add("nascitaStatoEstero",new ValidationError("Stato di nascita inesistente"));      }

    }
  }

  if(errors.get("descResComune")==null && errors.get("resProvincia")==null &&
     errors.get("statoEsteroRes")==null && errors.get("resCAP")==null) {
       String statoRes=personaVO.getStatoEsteroRes();
       if (statoRes==null) {
         try {
           personaVO.setResComune(client.ricercaCodiceComuneNonEstinto(personaVO.getDescResComune(),personaVO.getResProvincia()));
         }
         catch(SolmrException e) {
           errors.add("resProvincia",new ValidationError("Comune e/o provincia di residenza inesistenti"));
           errors.add("descResComune",new ValidationError("Comune e/o provincia di residenza inesistenti"));
         }
       }
       else {
         try {
           //Vector stati=client.ricercaStatoEstero(personaVO.getStatoEsteroRes());
           String istatEsteroRes = client.ricercaCodiceComuneNonEstinto(statoRes, "");
           personaVO.setResComune(istatEsteroRes);
         }
         catch(SolmrException se) {
           errors.add("statoEsteroRes",new ValidationError("Stato di nascita inesistente"));
         }
       }
     }
   }



/**

 * Setta la data di nascita e il sesso di una persona in base al codice fiscale.

 */

public void setDataSessoPersonaVO(String cuaa,PersonaFisicaVO personaVO)

{

  int annoNascita;

  int meseNascita;

  int giornoNascita;

  Date date=null;

  try

  {

    annoNascita=new Integer(cuaa.substring(6,8)).intValue();

    meseNascita=mesiCf.indexOf(cuaa.charAt(8))+1;

    giornoNascita=new Integer(cuaa.substring(9,11)).intValue();

    personaVO.setSesso(giornoNascita<40?"M":"F");

    if (giornoNascita>=40)

    {

      giornoNascita-=40;

    }

    annoNascita+=1900;

    if (annoNascita<1950)

    {

      annoNascita+=100;

    }

    date=DateUtils.parseDate(""+giornoNascita,""+meseNascita,""+annoNascita);

    if (date!=null)

    {

      personaVO.setNascitaData(date);

      personaVO.setStrNascitaData(DateUtils.formatDate(date));

    }

  }

  catch(Exception e)

  {

  }

}



private void throwValidation(String msg,String validateUrl) throws ValidationException

{

  ValidationException valEx = new ValidationException(msg,validateUrl);

  valEx.addMessage(msg,"exception");

  throw valEx;

}



%>
