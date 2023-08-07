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
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/newInserimentoRichiestaVariazione.htm");

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
  String regimeInserimentoRichiestaVariazione = request.getParameter("regimeInserimentoRichiestaVariazione");

  //Valorizza segnaposto errore htmpl
  HtmplUtil.setErrors(htmpl, errors, request, application);
  String idAzienda = request.getParameter("idAzienda");
  if(Validator.isEmpty(idAzienda))
    idAzienda = request.getParameter("idAziendaInd");
  htmpl.set("idAzienda", idAzienda);
  
  htmpl.set("cuaa", anagAziendaVO.getCUAA());
  htmpl.set("denominazione", anagAziendaVO.getDenominazione());
  
  String idTipoRichiesta = request.getParameter("idTipoRichiesta");
  Vector<CodeDescription> vTipoRichiestaVariazione = (Vector<CodeDescription>)request.getAttribute("vTipoRichiestaVariazione");
  if(Validator.isNotEmpty(vTipoRichiestaVariazione))
  {
    for(int i=0;i<vTipoRichiestaVariazione.size();i++)
    {
      htmpl.newBlock("blkElencoTipoRichiesta");
      htmpl.set("blkElencoTipoRichiesta.idTipoRichiesta", ""+vTipoRichiestaVariazione.get(i).getCode());         
      htmpl.set("blkElencoTipoRichiesta.descrizione", vTipoRichiestaVariazione.get(i).getDescription());
      
      if(Validator.isNotEmpty(regimeInserimentoRichiestaVariazione))
      {
	      if(Validator.isNotEmpty(idTipoRichiesta) 
	        && (new Integer(idTipoRichiesta).compareTo(vTipoRichiestaVariazione.get(i).getCode()) == 0))
	      {
	        htmpl.set("blkElencoTipoRichiesta.selected", "selected=\"selected\"", null);
	      }
	    }
      
    }   
  
  }
  
  if(Validator.isNotEmpty(idTipoRichiesta) 
    && (new Integer(idTipoRichiesta).compareTo(new Integer(6)) == 0))
  {
    Vector<String> vActor = (Vector<String>)session.getAttribute("vActor");
    if(!vActor.contains(SolmrConstants.GESTORE_CAA))
    {
      htmpl.newBlock("blkFlagNoTutte");
    }
  }
  
  String chkSoloAggiunta = request.getParameter("chkSoloAggiunta");
  if(Validator.isNotEmpty(chkSoloAggiunta))
    htmpl.set("checkedSoloAggiunta", "checked=\"checked\"", null);
  
  
  if(Validator.isEmpty(regimeInserimentoRichiestaVariazione)
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
