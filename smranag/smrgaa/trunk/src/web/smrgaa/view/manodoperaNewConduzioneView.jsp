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

<%
  SolmrLogger.debug(this, " - manodoperaNewConduzioneView.jsp - INIZIO PAGINA");

  java.io.InputStream layout = application.getResourceAsStream("/layout/manodoperaNewConduzione.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());

  //HashMap tenuto in session e contenente tutti i dati per l'inserimento della manodopera
  HashMap hmManodopera = (HashMap) session.getAttribute("common");

  //Manodopera recuperata dalla session
  ManodoperaVO manodoperaVO = (ManodoperaVO) hmManodopera.get("manodoperaVO");
  htmpl.set("dataInizioValidita", DateUtils.getCurrentDateString());
  HtmplUtil.setValues(htmpl, manodoperaVO);

  //Caricamento combo Tipo Forma Conduzione, Tipo Attivita Complementari
  HtmplUtil.setValues(htmpl, request);

  //Caricamento dell'elenco delle attività complementari già inserite dall'utente
  String elemVectorDinamicTable;
  SolmrLogger.debug(this, "hmManodopera.get(idTipoAttCompl): " + hmManodopera.get("idTipoAttCompl"));
  SolmrLogger.debug(this, "hmManodopera.get(desTipoAttCompl): " + hmManodopera.get("desTipoAttCompl"));
  if (hmManodopera.get("idTipoAttCompl") != null & hmManodopera.get("desTipoAttCompl") != null) {
    String[] idTipoStr = (String[]) hmManodopera.get("idTipoAttCompl");
    String[] desTipoStr = (String[]) hmManodopera.get("desTipoAttCompl");
    String[] desStr = (String[]) hmManodopera.get("descrizioneAttivitaCompl");
    //String[] idTipoStr = request.getParameterValues("idTipoAttCompl");
    //String[] desTipoStr = request.getParameterValues("desTipoAttCompl");
    //String[] desStr = request.getParameterValues("descrizioneAttCompl");
    for (int i=0; i<idTipoStr.length; i++){
      htmpl.newBlock("blk_vectorDinamicTable");
      elemVectorDinamicTable = "['" + idTipoStr[i] + "', '" + desTipoStr[i] + "','" + StringUtils.replace(desStr[i],"'","\\'") + "']";
      if(i != (idTipoStr.length-1)){
        elemVectorDinamicTable = elemVectorDinamicTable + ",";
      }
      htmpl.set("blk_vectorDinamicTable.elemVectorDinamicTable", elemVectorDinamicTable);
    }
  }
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  if (Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }

  //Questa operazione è stata spostata qui altrimenti la combo tipo forma conduzione
  //non viene settata perché anaAziendaVO contiene un campo con lo stesso nome
  HtmplUtil.setValues(htmpl, anagAziendaVO);

  HtmplUtil.setErrors(htmpl, errors, request, application);

  SolmrLogger.debug(this, " - manodoperaNewConduzioneView.jsp - FINE PAGINA");
%>
<%= htmpl.text()%>
