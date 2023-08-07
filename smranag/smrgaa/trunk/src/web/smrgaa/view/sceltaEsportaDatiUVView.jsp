<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>


<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.stampe.TipoReportVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/sceltaEsportaDatiUV.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");  
  String descrizionePiano = (String)request.getAttribute("descrizionePiano");
  FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
  
  String tipoEsportaDatiUV = request.getParameter("tipoEsportaDatiUV");
  
  
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  else
  {
    htmpl.newBlock("blkNoErrore");
  }  
  
  htmpl.set("descrizionePiano", descrizionePiano); 
  
  
  String rowcount = "2";
  if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO)
    && Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento()))
  {
    if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() == -1)
    {
      htmpl.newBlock("blkScaricoCompensazione");
      rowcount = "3";
    }
  }
  
  htmpl.set("rowcount", rowcount);
  
  
  
  if(Validator.isNotEmpty(tipoEsportaDatiUV))
  {
    if(tipoEsportaDatiUV.equalsIgnoreCase("2"))
    {
      htmpl.set("blkScaricoCompensazione.checkedcompensazione", "checked=\"true\"",null);
      if(session.getAttribute("flagFinePlsql") != null)
      {
        htmpl.set("riabilitaTasto", "javascript:riabilitaTasto()"); 
      } 
    }
    else if(tipoEsportaDatiUV.equalsIgnoreCase("1"))
    {
      htmpl.set("checkeduv", "checked=\"true\"",null); 
    }
    else
    {
      htmpl.set("checkedisole", "checked=\"true\"",null); 
    }    
  }
  else
  {
    htmpl.set("checkedisole", "checked=\"true\"",null); 
  }
  
  
  
  
  
  
    

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
