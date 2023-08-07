<%@ page language="java" contentType="text/html" isErrorPage="true"

%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<jsp:useBean id="contoCorrenteVO" scope="request" class="it.csi.solmr.dto.anag.ContoCorrenteVO"/>

<%

  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/conti_correnti_det.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);


  htmpl.set("dataInizioValiditaContoCorrente",DateUtils.formatDate(contoCorrenteVO.getDataInizioValiditaContoCorrente()));
  
  Date dataEstinzione=contoCorrenteVO.getDataEstinzione();
  if (dataEstinzione!=null)
  {
    htmpl.set("dataEstinzione",DateUtils.formatDate(dataEstinzione));
  }
  else if(contoCorrenteVO.getDataFineValiditaContoCorrente() != null)
  {
    htmpl.set("dataEstinzione",DateUtils.formatDate(contoCorrenteVO.getDataFineValiditaContoCorrente()));
  }
  
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  String dateUlt = "";
  if(contoCorrenteVO.getDataAggiornamento() !=null)
  {
    dateUlt = DateUtils.formatDate(contoCorrenteVO.getDataAggiornamento());
  } 
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", dateUlt, contoCorrenteVO.getDescUtenteAggiornamento(),
    null,null);

  HtmplUtil.setValues(htmpl,contoCorrenteVO,(String)session.getAttribute("pathToFollow"));

  HtmplUtil.setValues(htmpl,request,(String)session.getAttribute("pathToFollow"));

  ValidationErrors errors=(ValidationErrors)request.getAttribute("errors");

  HtmplUtil.setErrors(htmpl, errors, request, application);
  htmpl.set("codpaese",contoCorrenteVO.getCodPaese());
  htmpl.set("cctrl",contoCorrenteVO.getCifraCtrl());

  //IBAN
  htmpl.newBlock("iban");
  htmpl.set("iban.iban",contoCorrenteVO.getIban());
  
  
  
  
   String obbligoGF = (String)request.getAttribute("obbligoGF");
   if("S".equalsIgnoreCase(obbligoGF))
   {
     htmpl.newBlock("blkCCSpeciale");
    
     if(Validator.isNotEmpty(contoCorrenteVO.getFlagContoGf())
       && "S".equalsIgnoreCase(contoCorrenteVO.getFlagContoGf()))
     {
       htmpl.set("blkCCSpeciale.flagContoGf", "Si");
     }
     else
     {
       htmpl.set("blkCCSpeciale.flagContoGf", "No");
     }
   }
  
  
  
  
  //Invalidazione
  String flagContoVincolato = "No";
  if(Validator.isNotEmpty(contoCorrenteVO.getFlagContoVincolato()) 
	  && "S".equalsIgnoreCase(contoCorrenteVO.getFlagContoVincolato()))
  {
	  flagContoVincolato = "Si";
  }
  htmpl.set("flagContoVincolatoPag", flagContoVincolato);
  htmpl.set("causale",contoCorrenteVO.getDescrizioneCausaInvalidazione());
  htmpl.set("note",contoCorrenteVO.getNote());
  htmpl.set("motivoValidazione",contoCorrenteVO.getMotivoRivalidazione());

  out.print(htmpl.text());

%>

