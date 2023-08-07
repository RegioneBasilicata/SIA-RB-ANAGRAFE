<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%
  SolmrLogger.debug(this, " - terreniContrattiView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/terreniContratti.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  HtmplUtil.setValues(htmpl, anagAziendaVO);
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());
  Vector elencoContratti=(Vector)request.getAttribute("contratti");

  SolmrLogger.debug(this, "elencoContratti: " + elencoContratti);
  if (elencoContratti != null && elencoContratti.size() > 0) {
    htmpl.newBlock("blkSiContratti");
    htmpl.newBlock("elencoContratti");

    Iterator iteraContratti = elencoContratti.iterator();
    ContrattoVO contrattiVO = null;
    while (iteraContratti.hasNext()) {
      contrattiVO = (ContrattoVO)iteraContratti.next();
      htmpl.set("blkSiContratti.elencoContratti.idContratto", contrattiVO.getIdContratto());
      htmpl.set("blkSiContratti.elencoContratti.dataInizioAffitto", contrattiVO.getDataInizioAffitto());
      htmpl.set("blkSiContratti.elencoContratti.dataScadenzaAffitto", contrattiVO.getDataScadenzaAffitto());
      htmpl.set("blkSiContratti.elencoContratti.descScadenza", contrattiVO.getDescScadenza());
      htmpl.set("blkSiContratti.elencoContratti.descTipologiaContratto", contrattiVO.getDescTipologiaContratto());
      htmpl.set("blkSiContratti.elencoContratti.numeroRegistrazione", contrattiVO.getNumeroRegistrazione());
      htmpl.set("blkSiContratti.elencoContratti.dataRegistrazione", contrattiVO.getDataRegistrazione());
    }
  }
  else {
    htmpl.newBlock("noContratti");
  }

  SolmrLogger.debug(this, "request.getParameter(\"err_error\"): " + request.getParameter("err_error"));
  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - terreniContrattiView.jsp - FINE PAGINA");
%>
<%= htmpl.text()%>
