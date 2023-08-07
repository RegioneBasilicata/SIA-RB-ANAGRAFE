  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.solmr.business.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/anagraficaIndividualePassaggio_nuovoTitolare.htm");
  Htmpl htmpl = new Htmpl(layout);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagAziendaVO anagPassaggioVO = (AnagAziendaVO)session.getAttribute("anagPassaggioVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  ComuneVO comuneVO = null;
  try {
    String codiceFiscale = StringUtils.convertiCFOmonimo(anagPassaggioVO.getCUAA());
    comuneVO = anagFacadeClient.getComuneByCUAA(codiceFiscale.substring(11,15).toUpperCase());
  }
  catch(Exception e) {
  }

  PersonaFisicaVO personaFisicaVO = (PersonaFisicaVO)session.getAttribute("personaTitolareVO");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  if(errors == null) {
    if(personaFisicaVO != null) {
      if(personaFisicaVO.isNewPersonaFisica()) {
        htmpl.set("codiceFiscale",anagPassaggioVO.getCUAA().toUpperCase());
        Date dataNascita = DateUtils.getDataNascitaFromCF(anagPassaggioVO.getCUAA());
        htmpl.set("strNascitaData",DateUtils.formatDate(dataNascita));
        String sesso = StringUtils.getSessoFromCF(anagPassaggioVO.getCUAA());
        if(sesso.equalsIgnoreCase("M")) {
          htmpl.set("checkedM","checked");
        }
        else {
          htmpl.set("checkedF","checked");
        }
        if(comuneVO != null) {
          if(!comuneVO.getFlagEstero().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
            htmpl.set("descNascitaComune",comuneVO.getDescom());
            htmpl.set("nascitaProv",comuneVO.getSiglaProv());
            htmpl.set("nascitaStatoEstero","");
          }
          else {
            htmpl.set("nascitaStatoEstero",comuneVO.getDescom());
            htmpl.set("cittaNascita",personaFisicaVO.getCittaNascita());
            htmpl.set("descNascitaComune","");
            htmpl.set("nascitaProv","");
          }
        }
        HtmplUtil.setValues(htmpl, personaFisicaVO);
      }
      else {
        if(personaFisicaVO.getSesso() != null) {
          if(personaFisicaVO.getSesso().equalsIgnoreCase("M")) {
            htmpl.set("checkedM","checked");
          }
          else {
            htmpl.set("checkedF","checked");
          }
        }
        HtmplUtil.setValues(htmpl, personaFisicaVO);
      }
      String cfErrato = (String)session.getAttribute("cfErrato");
      session.removeAttribute("cfErrato");
      if(cfErrato != null) {
        htmpl.set("cfErrato",cfErrato);
      }
    }
  }
  else {
    htmpl.set("codiceFiscale",anagPassaggioVO.getCUAA().toUpperCase());
    String sesso = (String)session.getAttribute("sesso");
    session.removeAttribute("sesso");
    if(sesso != null) {
      if(sesso.equalsIgnoreCase("M")) {
        htmpl.set("checkedM","checked");
      }
      else {
        htmpl.set("checkedF","checked");
      }
    }
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }

  // Informazioni relative al titolo di studio
  Vector elencoTitoliStudio = null;
  Long idTitoloStudio = (Long)session.getAttribute("idTitoloStudio");
  try {
    elencoTitoliStudio = anagFacadeClient.getTitoliStudio();
  }
  catch(SolmrException se) {}

  if(elencoTitoliStudio != null) {
    Iterator iteraTitoliStudio = elencoTitoliStudio.iterator();
    while(iteraTitoliStudio.hasNext()) {
      htmpl.newBlock("elencoTitoliStudio");
      CodeDescription code = (CodeDescription)iteraTitoliStudio.next();
      if(personaFisicaVO.getIdTitoloStudio() != null) {
        if(personaFisicaVO.getIdTitoloStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
          htmpl.set("elencoTitoliStudio.check","selected");
        }
      }
      htmpl.set("elencoTitoliStudio.idCodice", code.getCode().toString());
      htmpl.set("elencoTitoliStudio.descrizione", code.getDescription());
    }
    if(personaFisicaVO.getIdTitoloStudio() != null) {
      Vector elencoIndirizziStudio = null;
      try {
        elencoIndirizziStudio = anagFacadeClient.getIndirizzoStudioByTitolo(personaFisicaVO.getIdTitoloStudio());
      }
      catch(SolmrException se) {
      }
      if(elencoIndirizziStudio != null) {
        Iterator iteraIndirizziStudio = elencoIndirizziStudio.iterator();
        while(iteraIndirizziStudio.hasNext()) {
          htmpl.newBlock("elencoIndirizziStudio");
          CodeDescription code = (CodeDescription)iteraIndirizziStudio.next();
          if(personaFisicaVO.getIdIndirizzoStudio() != null) {
            if(personaFisicaVO.getIdIndirizzoStudio().compareTo(Long.decode(code.getCode().toString())) == 0) {
              htmpl.set("elencoIndirizziStudio.check","selected");
            }
          }
          htmpl.set("elencoIndirizziStudio.idCodice",code.getCode().toString());
          htmpl.set("elencoIndirizziStudio.descrizione",code.getDescription());
        }
      }
    }
  }
%>
<%= htmpl.text()%>
