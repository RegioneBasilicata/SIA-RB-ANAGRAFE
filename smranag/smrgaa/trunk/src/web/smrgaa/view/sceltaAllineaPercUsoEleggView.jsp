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

<%
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/sceltaAllineaPercUsoElegg.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  //Gestione errori
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors"); 
  
  String tipoAllineaPercUsoElegg = request.getParameter("tipoAllineaPercUsoElegg");
  if(Validator.isNotEmpty(tipoAllineaPercUsoElegg))
  {
    if("1".equalsIgnoreCase(tipoAllineaPercUsoElegg))
    {
      htmpl.set("checkedSolo", "checked=\"checked\"",null);
    }
    else
    {
      htmpl.set("checkedTutto", "checked=\"checked\"",null);
    }
  }
  else
  {
    htmpl.set("checkedTutto", "checked=\"checked\"",null);
  }
  
  
  
  
  
 
 
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }

 
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
