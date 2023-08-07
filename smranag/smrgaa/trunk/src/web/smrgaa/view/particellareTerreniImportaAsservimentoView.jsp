<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniImportaAsservimentoVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoImportaAsservimentoVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.client.anag.*" %>



<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/particellareTerreniImportaAsservimento.htm");

  Htmpl htmpl = new Htmpl(layout);

  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%

  Vector<UteVO> elencoUte = (Vector<UteVO>)request.getAttribute("elencoUte");
  Long idUte = (Long)request.getAttribute("idUte");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoImportaAsservimentoVO 
    = (FiltriRicercaTerrenoImportaAsservimentoVO) session.getAttribute("filtriRicercaTerrenoImportaAsservimentoVO");
  String idUteTmp = (String)session.getAttribute("idUteImportaAsservimento");
  
  it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
  
  String idTitoloPossesso = (String)session.getAttribute("idTitoloPossesso");
  
  if((idUte == null) && Validator.isNotEmpty(idUteTmp))
  {
    idUte = new Long(idUteTmp);
  }
  
  htmpl.set("idTitoloPossesso", idTitoloPossesso);
  
   if(filtriRicercaTerrenoImportaAsservimentoVO.getTipoRicerca()
      .equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA))
    {
      htmpl.newBlock("blkLegenda");
    }
  
  // Valorizzo il titolo di possesso
  if(elencoTipoTitoloPossesso != null && elencoTipoTitoloPossesso.length > 0) 
  {
    for(int i = 0; i < elencoTipoTitoloPossesso.length; i++) 
    {
      CodeDescription codeDescription = (CodeDescription)elencoTipoTitoloPossesso[i];
      if (codeDescription!=null)
      {
        if(filtriRicercaTerrenoImportaAsservimentoVO.getTipoRicerca().equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA))
        {
          htmpl.newBlock("blkLegenda.blkLegConduzione");
          htmpl.set("blkLegenda.blkLegConduzione.idTitoloPossessoLeg", String.valueOf(codeDescription.getCode())+" - ");         
          if(i == (elencoTipoTitoloPossesso.length - 1))
            htmpl.set("blkLegenda.blkLegConduzione.descTitoloPossesso", codeDescription.getDescription());
          else
            htmpl.set("blkLegenda.blkLegConduzione.descTitoloPossesso", codeDescription.getDescription()+", ");
        }
      
        if(idTitoloPossesso != null && idTitoloPossesso.equals(codeDescription.getCode().toString()))
          htmpl.set("descrizioneTitoloPossesso", codeDescription.getDescription());
      }
    }
  }
  
  String operazione = request.getParameter("operazione");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  // COMBO UTE
  Iterator<UteVO> iteraUte = elencoUte.iterator();
  while(iteraUte.hasNext()) 
  {
    UteVO uteVO = (UteVO)iteraUte.next();
    htmpl.newBlock("blkElencoUte");
    htmpl.set("blkElencoUte.codice", uteVO.getIdUte().toString());
    htmpl.set("blkElencoUte.descrizione", uteVO.getComune()+" - "+uteVO.getIndirizzo());
    
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("importa"))
    {
      if(Validator.isNotEmpty(idUte) && idUte.compareTo(uteVO.getIdUte()) == 0) 
      {
        htmpl.set("blkElencoUte.selected", "selected=\"selected\"");
      }
    }
    //Entro qui la prima volta!
    else
    {
      if(elencoUte.size() == 1) 
      {
        htmpl.set("blkElencoUte.selected", "selected=\"selected\"");
      }
      else if(Validator.isNotEmpty(idUte) && idUte.compareTo(uteVO.getIdUte()) == 0) 
      {
        htmpl.set("blkElencoUte.selected", "selected=\"selected\"");
      }
    }
  }
  // TABELLA PARTICELLE
  PaginazioneUtils pager = (PaginazioneUtils) request.getAttribute("pager");
  if (pager != null && pager.getTotaleRighe() > 0)
  {
    BigDecimal        totSupCatastale = new BigDecimal(0);
    BigDecimal        totSuperficieGrafica  = new BigDecimal(0);  
    RigaRicercaTerreniImportaAsservimentoVO righe[] = (RigaRicercaTerreniImportaAsservimentoVO[]) pager
        .getRighe();
    htmpl.newBlock("blkElenco");
    
    for(int i=0;i < righe.length; i++)
    {
    
     
      totSupCatastale = totSupCatastale.add(righe[i].getSupCatastale());
      totSuperficieGrafica = totSuperficieGrafica.add(righe[i].getSuperficieGrafica()); 
      /*if(filtriRicercaTerrenoImportaAsservimentoVO.getTipoRicerca()
        .equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA))
      {
        totSupCondotta = totSupCondotta.add(righe[i].getSupCatastale());
      }*/
    }
    
    htmpl.set("blkElenco.totSupCat", Formatter.formatDouble4(totSupCatastale));
    htmpl.set("blkElenco.totSuperficieGrafica", Formatter.formatDouble4(totSuperficieGrafica));
    if(filtriRicercaTerrenoImportaAsservimentoVO.getTipoRicerca()
      .equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA))
    {
      htmpl.newBlock("blkElenco.blkConduzione");
      //htmpl.set("blkElenco.blkConduzione.totSupCond", Formatter.formatDouble4(totSupCondotta));
    }
    pager.paginazione(htmpl);
    String paginaCorrente = request.getParameter("paginaCorrente");
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

  if(errors != null) 
  {
    HtmplUtil.setErrors(htmpl, errors, request, application);
  }

%>
<%= htmpl.text()%>
