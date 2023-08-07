<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaMacchineAgricoleVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@ page import="it.csi.solmr.dto.uma.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>


<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/importaMacchinaAgricola.htm");
  Htmpl htmpl = new Htmpl(layout);
  
  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO 
    = (FiltriRicercaMacchineAgricoleVO) session.getAttribute("filtriRicercaMacchineAgricoleVO");
  String operazione = request.getParameter("operazione");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  
  
  String regimeImportaMacchinaAgricola = request.getParameter("regimeImportaMacchinaAgricola");
  
  if(Validator.isNotEmpty(operazione) && "ricerca".equalsIgnoreCase(operazione))
  {
    htmpl.set("primoIngressso", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_0")))
  {
    htmpl.set("azienda", "azienda");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_1")))
  {
    htmpl.set("tipologia", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_2")))
  {
    htmpl.set("modello", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_3")))
  {
    htmpl.set("targa", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_4")))
  {
    htmpl.set("telaio", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_5")))
  {
    htmpl.set("annoCostruzione", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_6")))
  {
    htmpl.set("dataScarico", "true");
  }
  
  
  
  
  
  
  if(Validator.isNotEmpty(filtriRicercaMacchineAgricoleVO))  
    htmpl.set("cuaa", filtriRicercaMacchineAgricoleVO.getCuaa());  
  
  
  PaginazioneUtils pager = (PaginazioneUtils) request.getAttribute("pager");
  if (pager != null && pager.getTotaleRighe() > 0)
  {
    htmpl.newBlock("blkElenco");    
    
    pager.paginazione(htmpl);
    String paginaCorrente = request.getParameter("paginaCorrente");
    //setto a 1 la pagina sto facendo la ricerca
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("aggiorna"))
    {
      paginaCorrente = "1";
    }
    if (!Validator.isNotEmpty(paginaCorrente))
    {
      paginaCorrente = String.valueOf(pager.getPaginaCorrente());
    }
    htmpl.set("blkElenco.paginaCorrente", paginaCorrente);
    if(errors == null)
    {
      errors = pager.getErrors();
    }
  }
	
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>
  

  
