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
<%@ page import="it.csi.smranag.smrgaa.dto.uma.TipoGenereMacchinaGaaVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaMacchineAgricoleVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@ page import="it.csi.solmr.dto.uma.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>


<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/motori_agricoli_incarico.htm");
  Htmpl htmpl = new Htmpl(layout);


  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%


  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String storicizzazione=request.getParameter("storico");
  //Vector macchine=null;
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  
  
  String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String imok = "ok.gif";
  
  

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  FiltriRicercaMacchineAgricoleVO filtriRicercaMacchineAgricoleVO 
    = (FiltriRicercaMacchineAgricoleVO) session.getAttribute("filtriRicercaMacchineAgricoleVO");
  String operazione = request.getParameter("operazione");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  DittaUMAVO dittaUmaVO = (DittaUMAVO)request.getAttribute("dittaUmaVO");
  Date parametroScadenzaImpoMacc = (Date)request.getAttribute("parametroScadenzaImpoMacc");
  
  
  
  String regimeElencoMotoriAgricoli = request.getParameter("regimeElencoMotoriAgricoli");
  
  if(Validator.isEmpty(regimeElencoMotoriAgricoli))
  {
    htmpl.set("primoIngressso", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_0")))
  {
    htmpl.set("ute", "true");
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
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_7")))
  {
    htmpl.set("confermata", "true");
  }
  
  
  
  
  
  htmpl.newBlock("blkDittaUma");
    
  if(parametroScadenzaImpoMacc.after(new Date()))
  {
    htmpl.newBlock("blkDittaUma.blkAllineaUma");  
  }  
  
  if(dittaUmaVO == null) 
  {
    messaggioErrore = (String)AnagErrors.get("ERR_DITTA_UMA_NO_PRESENTE");
  }
  else
  {    
    
    htmpl.set("blkDittaUma.numeroDittaUma", dittaUmaVO.getDittaUMA());
    htmpl.set("blkDittaUma.siglaProvinciaUma", dittaUmaVO.getProvincia());
  }
    
  htmpl.set("blkDittaUma.dataUltimoAllineamento", DateUtils.formatDateTimeNotNull(anagAziendaVO.getDataAggiornamentoUma()));
   
   
  Vector<TipoGenereMacchinaGaaVO> vTipoGenereMacchina = (Vector<TipoGenereMacchinaGaaVO>)request.getAttribute("vTipoGenereMacchina");
  
  if(vTipoGenereMacchina != null) 
  {
    for(int i=0;i<vTipoGenereMacchina.size();i++) 
    {
      TipoGenereMacchinaGaaVO tipoGenereMacchinaGaaVO = vTipoGenereMacchina.get(i);
      htmpl.newBlock("blkDittaUma.blkTipoGenereMacchina");
      htmpl.set("blkDittaUma.blkTipoGenereMacchina.idGenereMacchina", ""+tipoGenereMacchinaGaaVO.getIdGenereMacchina());
      htmpl.set("blkDittaUma.blkTipoGenereMacchina.descrizione", tipoGenereMacchinaGaaVO.getDescrizione());
      if(filtriRicercaMacchineAgricoleVO.getIdGenereMacchina() != null 
        && filtriRicercaMacchineAgricoleVO.getIdGenereMacchina().longValue() == tipoGenereMacchinaGaaVO.getIdGenereMacchina()) 
        htmpl.set("blkDittaUma.blkTipoGenereMacchina.selected", "selected=\"selected\"", null);
    }
  }
  
  if(filtriRicercaMacchineAgricoleVO.isStorico())
  {
    htmpl.set("blkDittaUma.checkedStorico","checked=\"checked\"", null);
  }
  
  
  
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
  

  
