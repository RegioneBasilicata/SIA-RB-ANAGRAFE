<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>


<%

  String iridePageName = "reportisticaValidazioniCtrl.jsp";
  %>
     <%@include file = "/include/autorizzazione.inc" %>
  <%

  String reportisticaValidazioniUrl = "/view/reportisticaValidazioniView.jsp";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Vector elencoMandati = null;
  ValidationErrors errors = new ValidationErrors();
  Date dataDal = null;
  Date dataAl = null;

  // Recupero i parametri
  boolean forZona = request.getParameter("suddividi")!=null;
  request.setAttribute("forZona", new Boolean(forZona));
  String dal = request.getParameter("dal");
  String al = request.getParameter("al");
  request.setAttribute("dal", dal);
  request.setAttribute("al", al);

  // Controllo la presenza e la correttezza formale delle date
  if(!Validator.isNotEmpty(dal)) {
    ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_DAL_OBBLIGATORIO"));
    errors.add("dal", error);
  }
  else {
    if(!Validator.validateDateF(dal)) {
      ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_DAL_ERRATA"));
      errors.add("dal", error);
    }
    else {
      dataDal = DateUtils.parseDate(dal);
    }
  }

  if(!Validator.isNotEmpty(al)) {
    ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_AL_OBBLIGATORIO"));
    errors.add("al", error);
  }
  else {
    if(!Validator.validateDateF(al)) {
      ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_AL_ERRATA"));
      errors.add("al", error);
    }
    else {
      dataAl = DateUtils.parseDate(al);
      if(Validator.isNotEmpty(dataDal)) {
        if(dataAl.before(dataDal)) {
          ValidationError error = new ValidationError((String)AnagErrors.get("ERR_DATA_AL_MINORE_DATA_DAL"));
          errors.add("al", error);
        }
      }
    }
  }

  // Se ci sono errori li visualizzo
  if(errors.size() > 0) {
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(reportisticaValidazioniUrl).forward(request, response);
    return;
  }


  request.setAttribute("dal", dataDal);
  request.setAttribute("al", dataAl);
  // Altrimenti aggiorno il riepilogo in relazione ai dati inseriti dall'utente
  try {
    elencoMandati = anagFacadeClient.getMandatiValidatiByUtente(utenteAbilitazioni, forZona, dataDal, dataAl);
  }
  catch(SolmrException se) {
    request.setAttribute("messaggioErrore", se.getMessage());
    %>
      <jsp:forward page= "<%= reportisticaValidazioniUrl %>" />
    <%
  }
  request.setAttribute("elencoMandati",elencoMandati);
  %>
    <jsp:forward page= "<%= reportisticaValidazioniUrl %>" />
  <%

%>
