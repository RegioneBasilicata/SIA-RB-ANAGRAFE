<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "eliminaUtilizzoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String terreniElencoUtilizziUrl = "/view/terreniElencoUtilizziView.jsp";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  ParticellaVO particellaRicercaUtilizziVO = (ParticellaVO)session.getAttribute("particellaVO");

  ValidationErrors errors = new ValidationErrors();

  // L'utente ha selezionato il pulsante ok dal pop-up di richiesta conferma
  // eliminazione utilizzo
  if(Validator.isNotEmpty(request.getParameter("elimina"))) {
    SolmrLogger.debug(this, "Invocating elimina operation from utente in eliminaUtilizzoCtrl.jsp");
    SolmrLogger.debug(this, "Taking vector [ELENCO_CONDUZIONI] in eliminaUtilizzoCtrl.jsp");
    // Recupero il vettore contenente gli utilizzi selezionati dall'utente
    Vector elencoConduzioni = (Vector)session.getAttribute("elencoConduzioni");
    SolmrLogger.debug(this, "Obtained vector [ELENCO_CONDUZIONI] in eliminaUtilizzoCtrl.jsp and it values: "+elencoConduzioni);
    // Recupero il vettore contenente gli utilizzi selezionati dall'utente
    Vector elencoUtilizzi = (Vector)session.getAttribute("elencoUtilizzi");
    // Effettuo l'eliminazione degli utilizzi selezionati
    try {
      SolmrLogger.debug(this, "Invocating eliminaUtilizzoParticella in eliminaUtilizzoCtrl.jsp");
      anagFacadeClient.eliminaUtilizzoParticella(elencoConduzioni, elencoUtilizzi, ruoloUtenza.getIdUtente());
      SolmrLogger.debug(this, "Invocated eliminaUtilizzoParticella in eliminaUtilizzoCtrl.jsp");
    }
    catch(SolmrException se) {
      SolmrLogger.error(this, "Catch SolmrException in eliminaUtilizzoCtrl.jsp. Type of problem: "+se.getClass()+" with message: "+se.getMessage());
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
      return;
    }

    // Ricerco gli utilizzi rimasti in relazione a quei filtri
    Vector elencoUtilizziParticella = null;
    // L'utente ha selezionato il flag "tutti" relativo agli utilizzi
    if(particellaRicercaUtilizziVO.getFlagUtilizziTutti()) {
      try {
        elencoUtilizziParticella = anagFacadeClient.ricercaParticelleByParametriAndUtilizzi(particellaRicercaUtilizziVO,
                                                                                            anagAziendaVO.getIdAzienda());
      }
      catch(SolmrException se) {
        if(se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_PARTICELLE"))) {
          session.removeAttribute("elencoParticelleUtilizzi");
          %>
            <jsp:forward page= "<%= terreniElencoUtilizziUrl %>" />
          <%
        }
        else {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
          return;
        }
      }
    }
    // Se l'utente ha selezionato una particella con tipo utilizzo specificato
    else if(particellaRicercaUtilizziVO.getFlagSingoloUtilizzo()) {
      try {
        elencoUtilizziParticella = anagFacadeClient.ricercaParticelleByParametriAndUtilizzoSpecificato(anagAziendaVO.getIdAzienda(),
                                                                                                       particellaRicercaUtilizziVO);
      }
      catch(SolmrException se) {
        if(se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_PARTICELLE"))) {
          session.removeAttribute("elencoParticelleUtilizzi");
          %>
            <jsp:forward page= "<%= terreniElencoUtilizziUrl %>" />
          <%
        }
        else {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
          return;
        }
      }
    }
    // Altrimenti effettuo la ricerca per uso del suolo non specificato
    else {
      try {
        elencoUtilizziParticella = anagFacadeClient.ricercaParticelleByParametriSenzaUsoSuolo(particellaRicercaUtilizziVO,
                                                                                              anagAziendaVO.getIdAzienda());
      }
      catch(SolmrException se) {
        if(se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_PARTICELLE"))) {
          session.removeAttribute("elencoParticelleUtilizzi");
          %>
            <jsp:forward page= "<%= terreniElencoUtilizziUrl %>" />
          <%
        }
        else {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
          return;
        }
      }
    }

    // Se inceve ritrovo degli utilizzi metto il vettore in sessione
    session.setAttribute("elencoParticelleUtilizzi", elencoUtilizziParticella);

    // ... e vado alla pagina di elenco
    %>
      <jsp:forward page= "<%= terreniElencoUtilizziUrl %>" />
    <%

  }
  // L'utente ha selezionato la funzione elimina utilizzo dalla pagina di elenco degli
  // utilizzi
  else {

    // Rimuovo il vettore contenente l'elenco degli utilizzi precedentemente selezionati
    session.removeAttribute("elencoUtilizzi");

    String[] elencoUtilizziParticella = request.getParameterValues("idUtilizzoParticella");
    // Se si sta lavorando sul risultato di una ricerca su dichiarazioni di consistenza
    // storiche non è possibile effettuare l'eliminazione di un utilizzo;
    String firstElement = (String)elencoUtilizziParticella[0];
    if(firstElement.startsWith("-")) {
      errors.add("error",new ValidationError((String)(AnagErrors.get("ERR_RECORD_STORICIZZATO"))));
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
      return;
    }

    // Controllo che l'utente non abbia selezionato nessun record relativo a particelle
    // senza uso del suolo specificato
    Vector<String> elencoUtilizzi = new Vector<String>();
    Vector<String> elencoConduzioni = new Vector<String>();
    for(int i = 0; i < elencoUtilizziParticella.length; i++) {
      String elemento = (String)elencoUtilizziParticella[i];
      int valoreInizio = elemento.indexOf("/+");
      String conduzioneParticella = elemento.substring(1, valoreInizio);
      SolmrLogger.debug(this, "Value of parameter [CONDUZIONE_PARTICELLA] in eliminaUtilizzoCtrl.jsp: "+conduzioneParticella);
      String utilizzoParticella = elemento.substring(valoreInizio+2, elemento.length());
      if(utilizzoParticella.equalsIgnoreCase("0")) {
        errors.add("error",new ValidationError((String)(AnagErrors.get("ERR_ELIMINAZIONE_SENZA_USO_SUOLO_IMPOSSIBILE"))));
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
        return;
      }
      else {
        SolmrLogger.debug(this, "Adding parameters [CONDUZIONE_PARTICELLA] to the Vector in eliminaUtilizzoCtrl.jsp");
        elencoConduzioni.add(conduzioneParticella);
        SolmrLogger.debug(this, "Added parameters [CONDUZIONE_PARTICELLA] to the Vector in eliminaUtilizzoCtrl.jsp");
        elencoUtilizzi.add(utilizzoParticella);
      }
    }

    // Metto in sessione il vettore contenente gli utilizzi selezionati dall'utente
    SolmrLogger.debug(this, "Adding vector [ELENCO_CONDUZIONI] in the session in eliminaUtilizzoCtrl.jsp");
    session.setAttribute("elencoConduzioni", elencoConduzioni);
    SolmrLogger.debug(this, "Added vector [ELENCO_CONDUZIONI] in the session in eliminaUtilizzoCtrl.jsp");
    // Metto in sessione il vettore contenente gli utilizzi selezionati dall'utente
    session.setAttribute("elencoUtilizzi", elencoUtilizzi);

    // Recupero i filtri di ricerca
    ParticellaVO particellaVO = (ParticellaVO)session.getAttribute("particellaVO");

    // Se l'utente sta lavorando sul risultato di una ricerca per anno corrente
    if(particellaVO != null) {
      if(particellaVO.getIdDichiarazioneConsistenza().compareTo(new Long(0)) == 0) {
        // Recupero il valore del parametro DAUS sulla tabella DB_PARAMETRO
        String daus = null;
        try {
          daus = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_DAUS"));
        }
        catch(SolmrException se) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
          return;
        }

        // Creo la data in relazione al valore del parametro DAUS presente sulla tabella
        // DB_PARAMETRO
        String dataDausStr = daus.substring(0,2) + "/" + daus.substring(2,4) + "/" + String.valueOf(DateUtils.getCurrentYear().intValue());
        Date dateDaus = DateUtils.parseDate(dataDausStr);

        // Recupero il valore del flag VARIAZIONE_UTILIZZI_AMMESSA sulla tabella DB_AZIENDA in relazione
        // all'azienda agricola selezionata dall'utente
        String variazioneUtilizziAmmessa = anagAziendaVO.getVariazioneUtilizziAmmessa();

        // Se la data di sistema è maggiore della data limite memorizzata nel parametro DAUS della
        // tabella DB_PARAMETRO e il campo VARIAZIONE_UTILIZZI_AMMESSA nella tabella DB_AZIENDA
        // in relazione all'azienda selezionata è uguale a N allora impedisco l'operazione
        if(DateUtils.parseDate(DateUtils.getCurrent()).after(dateDaus) &&
           (!Validator.isNotEmpty(variazioneUtilizziAmmessa) ||
             variazioneUtilizziAmmessa.equalsIgnoreCase(SolmrConstants.FLAG_N))) {
           ValidationError error = new ValidationError((String)AnagErrors.get("ERR_ELIMINAZIONE_UTILIZZO_IMPOSSIBILE"));
           errors.add("error", error);
           request.setAttribute("errors", errors);
           request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
           return;
        }

        // Controllo che tra i record selezionati, se ci sono particelle ad utilizzo
        // vigneto, il parametro COVI sia uguale a S altrimenti impedisco l'operazione
        try {
          anagFacadeClient.checkEliminaUtilizziVigneto(elencoUtilizzi);
        }
        catch(SolmrException se) {
          ValidationError error = new ValidationError(se.getMessage());
          errors.add("error", error);
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(terreniElencoUtilizziUrl).forward(request, response);
          return;
        }

        // Se passo i controlli mando l'utente alla pagina di elenco con la richiesta di
        // conferma per l'eliminazione degli utilizzi
        request.setAttribute("conferma", new String("conferma"));
        %>
          <jsp:forward page= "<%= terreniElencoUtilizziUrl %>" />
        <%
      }
    }
  }


%>
