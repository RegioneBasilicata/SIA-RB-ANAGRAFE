  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.infocamere.*" %>

<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/anagraficaVisuraCamerale.htm");
  SolmrLogger.debug(this, "Found layout: "+layout);
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  AnagAziendaInfocVO anagAAEPAziendaVO = (AnagAziendaInfocVO)request.getAttribute("anagAziendaInfocVO");
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  /*
    Valorizzo tutti i segnaposto: anagrafe e AAEP
  */
  
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setValues(htmpl, anagAziendaVO);
  HtmplUtil.setValues(htmpl, anagAAEPAziendaVO);

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);

  if ("true".equals(request.getAttribute("sedeLegaleAAEPValido")))
  {
    htmpl.set("selezionaSedeLegale", "true");
    htmpl.bset("checkSedeLegale", "");
  }
  else
  {
    htmpl.set("selezionaSedeLegale", "false");
    htmpl.bset("checkSedeLegale", "disabled");
  }

  if ("true".equals(request.getAttribute("rappLegaleAAEPValido")))
  {
    htmpl.set("selezionaTitolareRappresentante", "true");
    htmpl.bset("checkTitolareRappresentante", "");
  }
  else
  {
    htmpl.set("selezionaTitolareRappresentante", "false");
    htmpl.bset("checkTitolareRappresentante", "disabled");
  }

%>
<%= htmpl.text()%>
