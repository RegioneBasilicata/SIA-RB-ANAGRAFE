<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>

<%@page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO"%>
<%@page import="it.csi.smranag.smrgaa.util.PaginazioneUtils"%>
<%@page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaTerreniVO"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>

<%!
  public static final String LAYOUT="/layout/elencoTerreni.htm";
%>

<%
  SolmrLogger.debug(this,"[ElencoTerrenoView:service] BEGIN.");
  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(LAYOUT);
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  FiltriRicercaTerrenoVO filtriRicercaTerrenoVO = (FiltriRicercaTerrenoVO) session
      .getAttribute("filtriRicercaTerrenoVO");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  if (Validator.isNotEmpty(filtriRicercaTerrenoVO
      .getDescStatoEsteroParticella()))
  {
    htmpl.newBlock("blkLabelFiltroStato");
    htmpl.newBlock("blkFiltroStato");
    htmpl.set("blkFiltroStato.descStatoEsteroParticella",
        filtriRicercaTerrenoVO.getDescStatoEsteroParticella());
  }
  else
  {
    htmpl.newBlock("blkLabelFiltroComune");
    htmpl.newBlock("blkFiltroComune");
    htmpl.set("blkFiltroComune.descComuneParticella",
        filtriRicercaTerrenoVO.getDescComuneParticella());
    htmpl.set("blkFiltroComune.siglaProvinciaParticella",
        filtriRicercaTerrenoVO.getSiglaProvinciaParticella());
  }
  htmpl.set("sezione", filtriRicercaTerrenoVO.getSezione());
  htmpl.set("foglio", filtriRicercaTerrenoVO.getFoglio());
  htmpl.set("particella", filtriRicercaTerrenoVO.getParticella());
  htmpl.set("subalterno", filtriRicercaTerrenoVO.getSubalterno());
  PaginazioneUtils pager = (PaginazioneUtils) request.getAttribute("pager");
  if (pager != null && pager.getTotaleRighe() > 0)
  {
    RigaRicercaTerreniVO righe[] = (RigaRicercaTerreniVO[]) pager
        .getRighe();
    htmpl.newBlock("blkElenco");
    if (SolmrConstants.FLAG_S.equals(righe[0].getFlagEstero()))
    {
      htmpl.newBlock("blkElenco.blkIntestazioneStato");
    }
    else
    {
      htmpl.newBlock("blkElenco.blkIntestazioneComune");
    }
    pager.paginazione(htmpl);
    String paginaCorrente = request.getParameter("paginaCorrente");
    if (!Validator.isNotEmpty(paginaCorrente))
    {
      paginaCorrente = String.valueOf(pager.getPaginaCorrente());
    }
    htmpl.set("blkElenco.paginaCorrente", paginaCorrente);
    ValidationErrors errors = pager.getErrors();
    if (errors != null)
    {
      HtmplUtil.setErrors(htmpl, errors, request, application);
    }
    htmpl.newBlock("blkSiTerreni");
  }
  else
  {
    htmpl.newBlock("blkNoTerreni");
  }
%><%=htmpl.text()%>
