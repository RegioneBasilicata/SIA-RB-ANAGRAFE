<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaAziendeCollegateVO" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="java.math.BigDecimal" %>

<%


  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl("/layout/aziendeCollegate.htm");

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  htmpl.set("headMenuScroll", headMenuScroll, null);
  htmpl.set("headMenuScrollIE6", headMenuScrollIE6, null);

 
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  ArrayList arAziendeLink = (ArrayList)session.getAttribute("elencoAziendeLink");
  String operazione = request.getParameter("operazione");
  BigDecimal totSupAziendeCollegate[] = (BigDecimal[])session.getAttribute("totSupAziendeCollegate");
  
  
  FiltriRicercaAziendeCollegateVO filtriRicercaAziendeCollegateVO 
    = (FiltriRicercaAziendeCollegateVO) session.getAttribute("filtriRicercaAziendeCollegateVO");
  
  
  
  
  
  
  if(arAziendeLink !=null)
  {
    for(int i=0;i<arAziendeLink.size();i++)
    {
      htmpl.newBlock("blkLink");
      CodeDescription code = (CodeDescription)arAziendeLink.get(i);
      htmpl.set("blkLink.idAziendaHidden", code.getSecondaryCode());
      htmpl.set("blkLink.cuaaLink", code.getCodeFlag());
      htmpl.set("blkLink.DenominazioneLink", code.getDescription());
      
    }
  }
  
  if(filtriRicercaAziendeCollegateVO.isStorico())
  {
    htmpl.set("checkedStorico","checked=\"checked\"", null);
  }
  
  String cuaaRicerca = filtriRicercaAziendeCollegateVO.getCuaaRicerca();
  if(Validator.isNotEmpty(cuaaRicerca))
  {
    htmpl.set("cuaaRicerca",cuaaRicerca.toUpperCase());
  }
  
  String partitaIvaRicerca = filtriRicercaAziendeCollegateVO.getPartitaIvaRicerca();
  if(Validator.isNotEmpty(partitaIvaRicerca))
  {
    htmpl.set("partitaIvaRicerca",partitaIvaRicerca);
  }
  
  String denominazioneRicerca = filtriRicercaAziendeCollegateVO.getDenominazioneRicerca();
  if(Validator.isNotEmpty(denominazioneRicerca))
  {
    htmpl.set("denominazioneRicerca",denominazioneRicerca.toUpperCase());
  }
  
  String provinciaRicerca = filtriRicercaAziendeCollegateVO.getProvinciaRicerca();
  if(Validator.isNotEmpty(provinciaRicerca))
  {
    htmpl.set("provinciaRicerca",provinciaRicerca.toUpperCase());
  }
  
  String comuneRicerca = filtriRicercaAziendeCollegateVO.getComuneRicerca();
  if(Validator.isNotEmpty(comuneRicerca))
  {
    htmpl.set("comuneRicerca",comuneRicerca.toUpperCase());
  }
  
  
  PaginazioneUtils pager = (PaginazioneUtils) request.getAttribute("pager");
  if (pager != null && pager.getTotaleRighe() > 0)
  {
    htmpl.newBlock("blkElenco");
    
    if(totSupAziendeCollegate != null)
    {
      htmpl.newBlock("blkElenco.blkTotali");    
      htmpl.set("blkElenco.blkTotali.supCond", Formatter.formatDouble4(totSupAziendeCollegate[0]));
      htmpl.set("blkElenco.blkTotali.supSAU", Formatter.formatDouble4(totSupAziendeCollegate[1]));
    }
    
    if(filtriRicercaAziendeCollegateVO.isStorico())
    {
      htmpl.newBlock("blkElenco.blkStorico");
      htmpl.set("blkElenco.blkTotali.colTotale", "11");
    }
    else
    {
      htmpl.set("blkElenco.blkTotali.colTotale", "8");
    }
    
    
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
  

  
  if(Validator.isNotEmpty(messaggioErrore)) {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
  
%>
