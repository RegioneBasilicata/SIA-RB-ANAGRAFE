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

  java.io.InputStream layout = application.getResourceAsStream("/layout/confermaRevocaDelega.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  String dataFineMandato = (String)request.getAttribute("dataFineMandato");
  if(Validator.isEmpty(dataFineMandato))
  {
    dataFineMandato = request.getParameter("dataFineMandato");
  }
  htmpl.set("dataFineMandato", dataFineMandato);
  
  String dataRicRitorno = (String)request.getAttribute("dataRicRitorno");
  if(Validator.isEmpty(dataRicRitorno))
  {
    dataRicRitorno = request.getParameter("dataRicRitorno");
  }
  htmpl.set("dataRicRitorno", dataRicRitorno);
  String parametroGDEL = (String)request.getAttribute("parametroGDEL");
  int incremento = new Integer(parametroGDEL).intValue();
  
  Date dataTmp = DateUtils.parseDate(dataRicRitorno);
  Calendar cDateTmp = Calendar.getInstance();
  cDateTmp.setTime(dataTmp);
  cDateTmp.roll(Calendar.DAY_OF_YEAR, incremento);
  
  //se è minore della data odierna dopo l'incremento di 15 GG
  if(cDateTmp.getTime().before(new Date()))
  {
    cDateTmp = Calendar.getInstance();
    cDateTmp.roll(Calendar.DAY_OF_YEAR, 1);
  }
  
  htmpl.set("dataRevoca", DateUtils.formatDate(cDateTmp.getTime()));
  
  DelegaVO delegaVO = (DelegaVO)request.getAttribute("delegaVO"); 
  htmpl.set("denomIntermediario", delegaVO.getDenomIntermediario());
  
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
