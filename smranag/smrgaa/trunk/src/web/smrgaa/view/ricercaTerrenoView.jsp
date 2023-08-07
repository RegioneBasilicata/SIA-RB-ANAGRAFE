<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoVO"%>
<%!
  public static final String LAYOUT="/layout/ricercaTerreno.htm";
%>

<%
  SolmrLogger.debug(this,"[RicercaTerrenoView:service] BEGIN.");

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(LAYOUT);
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  // Recupero l'oggetto contenente i parametri di ricerca
  FiltriRicercaTerrenoVO filtriRicercaTerrenoVO = (FiltriRicercaTerrenoVO) session
      .getAttribute("filtriRicercaTerrenoVO");

  // Setto i parametri dei flag
  // Quello relativo alla particella provvisoria
  if (filtriRicercaTerrenoVO.isParticellaProvvisoria())
  {
    htmpl.set("checkedProvvisoria", "checked");
  }

  // Quello relativo alla particella attiva
  if (filtriRicercaTerrenoVO.isParticellaAttiva())
  {
    htmpl.set("checkedAttiva", "checked");
  }
  htmpl.set("siglaProvinciaParticella", filtriRicercaTerrenoVO
      .getSiglaProvinciaParticella());
  htmpl.set("descComuneParticella", filtriRicercaTerrenoVO
      .getDescComuneParticella());
  htmpl.set("descStatoEsteroParticella", filtriRicercaTerrenoVO
      .getDescStatoEsteroParticella());
  htmpl.set("sezione", filtriRicercaTerrenoVO.getSezione());
  String foglio = filtriRicercaTerrenoVO.getFoglio();
  htmpl.set("strFoglio", foglio);

  if (isNumeric(foglio))
  {
    htmpl.set("foglio", foglio);
  }

  String particella = filtriRicercaTerrenoVO.getParticella();
  htmpl.set("strParticella", particella);
  if (isNumeric(particella))
  {
    htmpl.set("particella", particella);
  }
  htmpl.set("subalterno", filtriRicercaTerrenoVO.getSubalterno());
  htmpl.set("istatComuneParticella", filtriRicercaTerrenoVO
      .getIstatComuneParticella());

  // Gestione errori
  ValidationErrors errors = (ValidationErrors) request
      .getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);
%>
<%=htmpl.text()%><%!private boolean isNumeric(String value)
  {
    try
    {
      new Long(value);
      return true;
    }
    catch (Exception e)
    {
      return false;
    }
  }%>
