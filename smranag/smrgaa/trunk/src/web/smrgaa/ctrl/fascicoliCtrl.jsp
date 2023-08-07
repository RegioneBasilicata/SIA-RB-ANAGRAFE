<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.profile.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>


<%

  String iridePageName = "fascicoliCtrl.jsp";
  %>
     <%@include file = "/include/autorizzazione.inc" %>
  <%

  String fascicoliUrl = "/view/fascicoliView.jsp";
  String excelUrl = "/servlet/ExcelBuilderServlet";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Vector elencoCAA = null;
  ValidationErrors errors = new ValidationErrors();
  String operazione = request.getParameter("operazione");

  try {
    elencoCAA = anagFacadeClient.getElencoCAAByUtente(utenteAbilitazioni);
  }
  catch(SolmrException se) {
    request.setAttribute("messaggioErrore", se.getMessage());
    %>
      <jsp:forward page= "<%= fascicoliUrl %>" />
    <%
  }
  request.setAttribute("elencoCAA",elencoCAA);

  // L'utente ha premuto il pulsante "vai" selezionando la pagina dell'elenco
  // che vuole visualizzare
  if(request.getParameter("vai") != null) {

    // Recupero i parametri
    String intermediario = request.getParameter("idIntermediario");
    String statoFascicolo = request.getParameter("statoFascicolo");
    String dal = request.getParameter("dal");
    String al = request.getParameter("al");

    if(Validator.isNotEmpty(intermediario)) {
      request.setAttribute("idIntermediario", Long.decode(intermediario));
    }
    request.setAttribute("statoFascicolo", statoFascicolo);

    // Recupero il vettore contenente i records
    Vector elencoAziende = (Vector)session.getAttribute("elencoAziende");
    // Recupero il numero pagina
    String numeroParziale = request.getParameter("numeroParziale");
    request.setAttribute("numeroParziale", numeroParziale);
    // Controllo che il valore inserito sia corretto
    Vector errori = new Vector();
    if(!Validator.isNotEmpty(numeroParziale)) {
      request.setAttribute("numeroParziale", "0");
      String error = (String)AnagErrors.get("ERR_NUMERO_PARZIALE_NO_VALORIZZATO");
      errori.add(error);
      request.setAttribute("errori", errori);
      request.getRequestDispatcher(fascicoliUrl).forward(request, response);
      return;
    }
    else {
      if(!Validator.isNumericInteger(numeroParziale)) {
        String error = (String)AnagErrors.get("ERR_NUMERO_PARZIALE_ERRATO");
        errori.add(error);
        request.setAttribute("errori", errori);
        request.getRequestDispatcher(fascicoliUrl).forward(request, response);
        return;
      }
      else {
        int intNumeroParziale = Integer.parseInt(numeroParziale);
        if(intNumeroParziale == 0 || intNumeroParziale > (int)Math.ceil(elencoAziende.size()/25.0)) {
          String error = (String)AnagErrors.get("ERR_NUMERO_PARZIALE_SUPERO_TOTALE")+" "+(int)Math.ceil(elencoAziende.size()/25.0);
          errori.add(error);
          request.setAttribute("errori", errori);
          request.getRequestDispatcher(fascicoliUrl).forward(request, response);
          return;
        }
      }
    }

    // Metto in session il valore proposto dall'utente
    session.setAttribute("indice", String.valueOf(((Integer.parseInt(numeroParziale)*25))-25));
    %>
       <jsp:forward page="<%= fascicoliUrl %>"/>
    <%
  }
  else {
    // L'utente ha selezionato una delle possibili operazioni dalla pagina
    if(Validator.isNotEmpty(operazione)) {
      // L'utente ha selezionato il pulsante scarica
      if(operazione.equalsIgnoreCase("scarica")) {
        // Recupero il vettore contenente l'elenco delle aziende
        Vector elencoAziende = (Vector)session.getAttribute("elencoAziende");

        // Se esistono vado alla servlet per gestire lo scarico
        request.setAttribute("elenco", elencoAziende);
        request.setAttribute("foglioExcel", "elencoFascicoli");
        %>
           <jsp:forward page= "<%= excelUrl %>" />
        <%
      }
      // L'utente ha selezionato il pulsante "ricerca"
      else if(operazione.equalsIgnoreCase("ricerca")) {

        // Ripulisco la sessione
        session.removeAttribute("elencoAziende");
        session.removeAttribute("indice");

        // Recupero i parametri
        String intermediario = request.getParameter("idIntermediario");
        String statoFascicolo = request.getParameter("statoFascicolo");
        String dal = request.getParameter("dal");
        String al = request.getParameter("al");

        // Controllo che l'utente abbia selezionato il CAA
        if(!Validator.isNotEmpty(intermediario)) {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_CAA_OBBLIGATORIO"));
          errors.add("idIntermediario", error);
        }
        // Se è stato valorizzato lo metto in request
        else {
          request.setAttribute("idIntermediario", Long.decode(intermediario));
        }
        // Metto in request lo stato del fascicolo che si desidera visualizzare
        request.setAttribute("statoFascicolo", statoFascicolo);
        // Metto in request i valori delle date
        request.setAttribute("dal", dal);
        request.setAttribute("al", al);

        // Se è stata selezionata l'ipotesi "validati dal/al" controllo la correttezza formale delle date
        // inserite
        Date dataDal = null;
        Date dataAl = null;
        if(statoFascicolo.equalsIgnoreCase((String)SolmrConstants.get("STATO_FASCICOLO_VALIDATI_DAL_AL"))) {
          // La "data dal" è obbligatoria
          if(!Validator.isNotEmpty(dal)) {
            ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_DAL_OBBLIGATORIO"));
            errors.add("dal", error);
          }
          // Se valorizzata...
          else {
            // ... Controllo che sia corretta
            if(!Validator.validateDateF(dal)) {
              ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_DAL_ERRATA"));
              errors.add("dal", error);
            }
            else {
              // Se è stata valorizzata la data al...
              dataDal = DateUtils.parseDate(dal);
              if(Validator.isNotEmpty(al)) {
                // Controllo che sia corretta
                if(!Validator.validateDateF(al)) {
                  ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_AL_ERRATA"));
                  errors.add("al", error);
                }
                // Se lo è...
                else {
                  // Controllo che non sia minore della "data dal"
                  if(DateUtils.parseDate(al).before(DateUtils.parseDate(dal))) {
                    ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_AL_MINORE_DATA_DAL"));
                    errors.add("al", error);
                  }
                  else {
                    dataAl = DateUtils.parseDate(al);
                  }
                }
              }
            }
          }
        }

        // Se si sono verificati degli errori non effettuo la ricerca e li visualizzo a video
        if(errors.size() > 0) {
          request.setAttribute("errors", errors);
          request.getRequestDispatcher(fascicoliUrl).forward(request, response);
          return;
        }

        // Se i controlli hanno dato esito positivo effettuo la ricerca
        DelegaVO delegaRicercaVO = new DelegaVO();
        delegaRicercaVO.setIdIntermediario(Long.decode(intermediario));
        delegaRicercaVO.setStatoRicercaFascicolo(statoFascicolo);
        delegaRicercaVO.setDataInizio(dataDal);
        delegaRicercaVO.setDataFine(dataAl);

        Vector elencoAziende = null;
        try {
          elencoAziende = anagFacadeClient.getElencoAziendeByCAA(delegaRicercaVO);
        }
        catch(SolmrException se) {
          request.setAttribute("messaggioErrore", se.getMessage());
          %>
             <jsp:forward page= "<%= fascicoliUrl %>" />
          <%
        }
        // Metto il vettore in session
        session.setAttribute("elencoAziende", elencoAziende);

        // Torno alla pagina di ricerca presentando però l'elenco dei fascicoli
        %>
           <jsp:forward page= "<%= fascicoliUrl %>" />
        <%
      }
      // L'utente ha selezionato una delle due frecce avanti/indietro
      else {

        // Recupero i parametri
        String intermediario = request.getParameter("idIntermediario");
        String statoFascicolo = request.getParameter("statoFascicolo");
        String dal = request.getParameter("dal");
        String al = request.getParameter("al");

        if(Validator.isNotEmpty(intermediario)) {
          request.setAttribute("idIntermediario", Long.decode(intermediario));
        }
        request.setAttribute("statoFascicolo", statoFascicolo);

        String indice = request.getParameter("valoreIndice");
        session.setAttribute("indice",indice);
        %>
           <jsp:forward page="<%= fascicoliUrl %>"/>
        <%
      }
    }
  }
%>
