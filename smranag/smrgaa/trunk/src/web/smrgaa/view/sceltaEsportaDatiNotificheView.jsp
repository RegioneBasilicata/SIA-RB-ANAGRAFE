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
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/sceltaEsportaDatiNotifiche.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  //AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  //AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");  
 
  String tipoEsportaDatiNotifiche = request.getParameter("tipoEsportaDatiNotifiche");
  
  
  String idTipologiaNotifica = request.getParameter("idTipologiaNotifica");
  htmpl.set("idTipologiaNotifica", idTipologiaNotifica);
  
  
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
  
  if(Validator.isNotEmpty(idTipologiaNotifica))
  {
    Long idTipologiaNotificaLg = new Long(idTipologiaNotifica);
    if(idTipologiaNotificaLg.compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_VARIAZIONECATASTALE) == 0)
    {
      htmpl.newBlock("blkExcelParticelle");
    }
    else if(idTipologiaNotificaLg.compareTo(SolmrConstants.ID_TIPO_TIPOLOGIA_BLOCCAPROCEDIMENTI) == 0)
    {
      htmpl.newBlock("blkExcelUv");
    }
  
  }
  
  
  if(Validator.isNotEmpty(tipoEsportaDatiNotifiche))
  {
    if(tipoEsportaDatiNotifiche.equalsIgnoreCase("2"))
    {
      htmpl.set("blkExcelParticelle.checkedParticelle", "checked=\"true\"",null); 
    }
    else if(tipoEsportaDatiNotifiche.equalsIgnoreCase("1"))
    {
      htmpl.set("blkExcelUv.checkeduv", "checked=\"true\"",null); 
    }
    else
    {
      htmpl.set("checkedNormale", "checked=\"true\"",null); 
    }    
  }
  else
  {
    htmpl.set("checkedNormale", "checked=\"true\"",null); 
  }
  
  
  
  
  
  
    

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
