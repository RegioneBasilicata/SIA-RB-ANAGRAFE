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
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/newInserimentoRichiestaCessazione.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("testoHelp", (String)request.getAttribute("testoHelp"), null);
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)request.getAttribute("anagAziendaVO");
  AziendaNuovaVO aziendaNuovaVO = (AziendaNuovaVO)request.getAttribute("aziendaNuovaVO");
  String regimeInserimentoRichiestaCessazione = request.getParameter("regimeInserimentoRichiestaCessazione");

  //Valorizza segnaposto errore htmpl
  HtmplUtil.setErrors(htmpl, errors, request, application);
  htmpl.set("idAzienda", request.getParameter("idAzienda"));
  
  htmpl.set("cuaa", anagAziendaVO.getCUAA());
  htmpl.set("denominazione", anagAziendaVO.getDenominazione());
  
  String idCessazione = request.getParameter("idCessazione");
  TipoCessazioneVO[] aTipoCessazione = (TipoCessazioneVO[])request.getAttribute("aTipoCessazione");
  if(Validator.isNotEmpty(aTipoCessazione))
  {
    for(int i=0;i<aTipoCessazione.length;i++)
    {
      htmpl.newBlock("blkElencoCessazione");
      htmpl.set("blkElencoCessazione.idCessazione", ""+aTipoCessazione[i].getIdCessazione());         
      htmpl.set("blkElencoCessazione.descrizione", aTipoCessazione[i].getDescrizione());
      
      if(Validator.isEmpty(regimeInserimentoRichiestaCessazione)
        && Validator.isNotEmpty(aziendaNuovaVO))
      {
        if(Validator.isNotEmpty(aziendaNuovaVO.getIdCessazione()) 
          && (aziendaNuovaVO.getIdCessazione().compareTo(aTipoCessazione[i].getIdCessazione()) == 0))
        {
          htmpl.set("blkElencoCessazione.selected", "selected=\"selected\"", null);
        }
      }
      else
      {
	      if(Validator.isNotEmpty(idCessazione) 
	        && (new Long(idCessazione).compareTo(aTipoCessazione[i].getIdCessazione()) == 0))
	      {
	        htmpl.set("blkElencoCessazione.selected", "selected=\"selected\"", null);
	      }
	    }
      
    }   
  
  }
  
  
  if(Validator.isEmpty(regimeInserimentoRichiestaCessazione)
     && Validator.isNotEmpty(aziendaNuovaVO))
  {
    htmpl.set("dataCessazione", DateUtils.formatDateNotNull(aziendaNuovaVO.getDataCessazione()));
  }
  else
  {  
    htmpl.set("dataCessazione", request.getParameter("dataCessazione"));
  }
  
  if(Validator.isEmpty(regimeInserimentoRichiestaCessazione)
     && Validator.isNotEmpty(aziendaNuovaVO))
  {
    htmpl.set("cuaaSubentrante", aziendaNuovaVO.getCuaaSubentrante());
  }
  else
  {  
    htmpl.set("cuaaSubentrante", request.getParameter("cuaaSubentrante"));
  }
  
  if(Validator.isEmpty(regimeInserimentoRichiestaCessazione)
     && Validator.isNotEmpty(aziendaNuovaVO))
  {
    htmpl.set("denominazioneSubentrante", aziendaNuovaVO.getDenominazioneSubentrante());
  }
  else
  {  
    htmpl.set("denominazioneSubentrante", request.getParameter("denominazioneSubentrante"));
  }
  
  if(Validator.isEmpty(regimeInserimentoRichiestaCessazione)
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
