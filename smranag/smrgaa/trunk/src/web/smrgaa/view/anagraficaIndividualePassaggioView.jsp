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

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/anagraficaIndividualePassaggio.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagPassaggioVO");
  if(anagAziendaVO == null) {
    anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  }

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  PersonaFisicaVO personaTitolareOld = (PersonaFisicaVO)session.getAttribute("personaTitolareOld");
  htmpl.set("cognomeTitolareOld",personaTitolareOld.getCognome());
  htmpl.set("nomeTitolareOld",personaTitolareOld.getNome());

  String ruolo = (String)session.getAttribute("ruolo");
  String primo = (String)session.getAttribute("primo");
  session.removeAttribute("primo");

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  if(errors == null) {
    if(anagAziendaVO != null) {
      if(anagAziendaVO.getTipoAttivitaOTE().getSecondaryCode() != null) {
        htmpl.set("codiceOTE",anagAziendaVO.getTipoAttivitaOTE().getSecondaryCode().toString());
      }
      if(anagAziendaVO.getTipoAttivitaOTE().getDescription() != null) {
        htmpl.set("descrizioneOTE",anagAziendaVO.getTipoAttivitaOTE().getDescription());
      }
      if(anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode() != null) {
        htmpl.set("codiceATECO",anagAziendaVO.getTipoAttivitaATECO().getSecondaryCode().toString());
      }
      if(anagAziendaVO.getTipoAttivitaATECO().getDescription() != null) {
        htmpl.set("descrizioneATECO",anagAziendaVO.getTipoAttivitaATECO().getDescription());
      }
    }
    if(ruolo != null && !ruolo.equals("")) {
      if(ruolo.equalsIgnoreCase(SolmrConstants.FLAG_N)) {
        htmpl.set("checkedN","checked");
      }
      else {
        htmpl.set("checkedS","checked");
      }
    }
    if(primo != null && !primo.equals("")) {
      htmpl.set("CUAA","");
    }
    else {
      htmpl.set("CUAA",anagAziendaVO.getCUAA());
    }
    HtmplUtil.setValues(htmpl, anagAziendaVO);
  }
  else {
    if(ruolo != null && !ruolo.equals("")) {
      if(ruolo.equalsIgnoreCase(SolmrConstants.FLAG_N)) {
        htmpl.set("checkedN","checked");
      }
      else {
        htmpl.set("checkedS","checked");
      }
    }
    HtmplUtil.setErrors(htmpl, errors, request, application);
    HtmplUtil.setValues(htmpl, request);
  }

%>
<%= htmpl.text()%>
