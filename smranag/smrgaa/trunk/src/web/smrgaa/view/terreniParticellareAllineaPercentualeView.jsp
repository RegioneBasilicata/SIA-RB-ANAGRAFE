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
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/terreniParticellareAllineaPercentuale.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getSession().getAttribute("anagAziendaVO");
  String percentualePossessoIn = "";
  String operazione = request.getParameter("operazione");
  
  
  if(Validator.isNotEmpty(session.getAttribute("percentualePossessoIn")))
  {  
    percentualePossessoIn = (String)session.getAttribute("percentualePossessoIn");
  }
  else
  {
    percentualePossessoIn = request.getParameter("percentualePossessoIn");
  }
  
  htmpl.set("percentualePossessoIn", percentualePossessoIn);
  
  String tipoAllinea = request.getParameter("tipoAllinea");
  
  
  
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
  
  
  
  
  if(Validator.isNotEmpty(operazione) 
     && operazione.equalsIgnoreCase("confermaAllineaSupUtil")) 
  {
    htmpl.set("checkedAllineaSupUtilPercPoss", "checked=\"true\"",null);   
  }
  else
  {
    htmpl.set("checkedAllineaPercPoss", "checked=\"true\"",null); 
  }
  
  
  
  
  
  
    

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
