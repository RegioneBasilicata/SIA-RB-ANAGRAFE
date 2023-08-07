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
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>


<%
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/newInserimentoRichiestaValidazione.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  htmpl.set("descDichiarazione", (String)request.getAttribute("descDichiarazione"), null);
  
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getAttribute("anagAziendaVO");
  AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
  String regimeInserimentoRichiestaValidazione = request.getParameter("regimeInserimentoRichiestaValidazione");

  //Valorizza segnaposto errore htmpl
  HtmplUtil.setErrors(htmpl, errors, request, application);
  htmpl.set("idAzienda", request.getParameter("idAzienda"));
  
  htmpl.set("cuaa", anagAziendaVO.getCUAA());
  htmpl.set("denominazione", anagAziendaVO.getDenominazione());
  
  String idMotivoDichiarazione = request.getParameter("idMotivoDichiarazione");
  Vector<CodeDescription> vMotivoDichiarazione = (Vector<CodeDescription>)request.getAttribute("vMotivoDichiarazione");
  if(Validator.isNotEmpty(vMotivoDichiarazione))
  {
    for(int i=0;i<vMotivoDichiarazione.size();i++)
    {
      htmpl.newBlock("blkElencoTipoMotivazione");
      htmpl.set("blkElencoTipoMotivazione.idMotivoDichiarazione", ""+vMotivoDichiarazione.get(i).getCode());         
      htmpl.set("blkElencoTipoMotivazione.descrizione", vMotivoDichiarazione.get(i).getDescription());
      
      if(Validator.isEmpty(regimeInserimentoRichiestaValidazione)
        && Validator.isNotEmpty(aziendaNuovaVO))
      {
        if(Validator.isNotEmpty(aziendaNuovaVO.getIdMotivoDichiarazione()) 
          && (aziendaNuovaVO.getIdMotivoDichiarazione().compareTo(vMotivoDichiarazione.get(i).getCode()) == 0))
        {
          htmpl.set("blkElencoTipoMotivazione.selected", "selected=\"selected\"", null);
        }
      }
      else
      {
	      if(Validator.isNotEmpty(idMotivoDichiarazione) 
	        && (new Integer(idMotivoDichiarazione).compareTo(vMotivoDichiarazione.get(i).getCode()) == 0))
	      {
	        htmpl.set("blkElencoTipoMotivazione.selected", "selected=\"selected\"", null);
	      }
	    }
      
    }   
  
  }
  
  
  if(Validator.isEmpty(regimeInserimentoRichiestaValidazione)
     && Validator.isNotEmpty(aziendaNuovaVO))
  {
    htmpl.set("note", aziendaNuovaVO.getNote());
  }
  else
  {  
    String note = request.getParameter("note");
    htmpl.set("note", note);
  }
  

  
  
  
  
  
  String messaggioErrore = (String)request.getAttribute("msgErrore");
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }


%>
<%= htmpl.text()%>
