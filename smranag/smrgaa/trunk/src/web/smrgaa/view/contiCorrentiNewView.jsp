<%@ page language="java" contentType="text/html" isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<jsp:useBean id="contoCorrenteVO" scope="request" class="it.csi.solmr.dto.anag.ContoCorrenteVO"/>

<%

  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/conti_correnti_new.htm");
  String intestatario = (String)request.getAttribute("intestatario");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  
  
  String obbligoGF = (String)request.getAttribute("obbligoGF");
  if("S".equalsIgnoreCase(obbligoGF))
  {
    htmpl.newBlock("blkCCSpeciale");
      
    String flagContoGf = request.getParameter("flagContoGf");
    if("S".equalsIgnoreCase(flagContoGf))
    {
      htmpl.set("blkCCSpeciale.checkedFlagContoGf", "checked=\"true\"", null);
    }
  }
  
  
  if(request.getParameter("regimeContiCorrentiNew") != null)
  {
     
    htmpl.set("abi", request.getParameter("abi"));
    htmpl.set("denominazioneBanca", request.getParameter("denominazioneBanca"));
    htmpl.set("bic", request.getParameter("bic"));
     
    htmpl.set("cab", request.getParameter("cab"));
    htmpl.set("descrizioneComuneSportello", request.getParameter("descrizioneComuneSportello"));
    htmpl.set("indirizzoSportello", request.getParameter("indirizzoSportello"));
    htmpl.set("codPaese", request.getParameter("codPaese"));
    htmpl.set("capSportello", request.getParameter("capSportello"));
     
     
    htmpl.set("cifraCtrl", request.getParameter("cifraCtrl"));
    htmpl.set("cin", request.getParameter("cin"));
    htmpl.set("numeroContoCorrente", request.getParameter("numeroContoCorrente"));
    htmpl.set("iban", request.getParameter("iban"));    
     
    htmpl.set("intestazione", request.getParameter("intestazione"));
  }
  else
  {
    // La prima volta che accedo alla funzionalità propongo nel campo intestatario
    // la denominazione dell'azienda agricola
    if(Validator.isNotEmpty(intestatario)) 
	  {
	    htmpl.set("intestazione", intestatario);
	  }
  }
  

  // La prima volta che accedo alla funzionalità propongo nel campo intestatario
  // la denominazione dell'azienda agricola
  /*if(Validator.isNotEmpty(intestatario)) 
  {
    htmpl.set("intestazione", intestatario);
  }
  else 
  {
    HtmplUtil.setValues(htmpl,contoCorrenteVO,(String)session.getAttribute("pathToFollow"));
    HtmplUtil.setValues(htmpl,request,(String)session.getAttribute("pathToFollow"));
    ValidationErrors errors=(ValidationErrors)request.getAttribute("errors");
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }*/
  
  
  ValidationErrors errors=(ValidationErrors)request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);
  	
  out.print(htmpl.text());

%>

