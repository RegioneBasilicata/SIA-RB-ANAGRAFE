<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>


<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/dettElencoDitteUtilizzatrici.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  PossessoMacchinaVO possessoMacchinaVO = (PossessoMacchinaVO)request.getAttribute("possessoMacchinaVO");
  Vector<PossessoMacchinaVO> vPossessoMacchina = (Vector<PossessoMacchinaVO>)request.getAttribute("vPossessoMacchina");
  

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  
  htmpl.set("idPossessoMacchina",request.getParameter("idPossessoMacchina"));
  
  MacchinaGaaVO macchinaVO = possessoMacchinaVO.getMacchinaVO();
  if(Validator.isNotEmpty(macchinaVO))
  {
    TipoGenereMacchinaGaaVO genereMacchinaVO = macchinaVO.getGenereMacchinaVO();    
    if(Validator.isNotEmpty(genereMacchinaVO))
    {
      htmpl.set("descGenereMacchina", genereMacchinaVO.getDescrizione());
      TipoMacchinaGaaVO tipoMacchinaVO = genereMacchinaVO.getTipoMacchinaVO();
      if(Validator.isNotEmpty(tipoMacchinaVO))
      {
        htmpl.set("descTipologia", tipoMacchinaVO.getDescrizione());
      }
    }
    
    TipoCategoriaGaaVO tipoCategoriaVO = macchinaVO.getTipoCategoriaVO();
    if(Validator.isNotEmpty(tipoCategoriaVO))
    {
      htmpl.set("descCategoria", tipoCategoriaVO.getDescrizione());
    }
    
    htmpl.set("descMarca", macchinaVO.getDescMarca());
    htmpl.set("modello", macchinaVO.getModello());
    if(Validator.isNotEmpty(macchinaVO.getAnnoCostruzione()))
      htmpl.set("annoCostruzione",""+macchinaVO.getAnnoCostruzione());
    htmpl.set("matricolaTelaio", macchinaVO.getMatricolaTelaio());
    htmpl.set("matricolaMotore",macchinaVO.getMatricolaMotore());
    if(Validator.isNotEmpty(macchinaVO.getLastNumeroTargaVO()))
      htmpl.set("numeroTarga", macchinaVO.getLastNumeroTargaVO().getNumeroTarga()); 
    
  }
  
  htmpl.set("cuaa", possessoMacchinaVO.getCuaa());
  htmpl.set("partitaIVA", possessoMacchinaVO.getPartitaIva());
  htmpl.set("denominazione", possessoMacchinaVO.getDenominazione());
  if(Validator.isNotEmpty(possessoMacchinaVO.getDittaUma()))
  {
    String dittaUma = possessoMacchinaVO.getDittaUma()+"/"+possessoMacchinaVO.getSiglaProvinciaUma();
    htmpl.set("dittaUma", dittaUma);
  }
  htmpl.set("indirizzoSedeLeg", possessoMacchinaVO.getIndirizzoSedeLeg());
  htmpl.set("capSedeLeg", possessoMacchinaVO.getCapSedeLeg());
  htmpl.set("descComSedeLeg", possessoMacchinaVO.getComunSedeLeg());
  htmpl.set("siglaProvSedeLeg", possessoMacchinaVO.getSglProvinciaSedeLeg());
  String descUte = possessoMacchinaVO.getComunUte()+" ("+possessoMacchinaVO.getSglProvinciaUte()+") "+possessoMacchinaVO.getIndirizzoUte();
  htmpl.set("descUte", descUte);
  htmpl.set("dataCarico", DateUtils.formatDateNotNull(possessoMacchinaVO.getDataCarico()));
  htmpl.set("dataScarico", DateUtils.formatDateNotNull(possessoMacchinaVO.getDataScarico()));
  htmpl.set("descScarico", possessoMacchinaVO.getDescTipoScarico());
  
  if(Validator.isNotEmpty(vPossessoMacchina))
  {
    for(int i=0;i<vPossessoMacchina.size();i++)
    {
      PossessoMacchinaVO possessoMacchinaElVO = vPossessoMacchina.get(i);
      htmpl.newBlock("blkElencoPossesso");
      htmpl.set("blkElencoPossesso.descFormaPossesso", possessoMacchinaElVO.getTipoFormaPossessoGaaVO().getDescrizione());
      htmpl.set("blkElencoPossesso.percentualePossesso", Formatter.formatDouble(possessoMacchinaElVO.getPercentualePossesso()));
      htmpl.set("blkElencoPossesso.denominazioneLeasing", possessoMacchinaElVO.getDenominazioneAzLeasing());
      htmpl.set("blkElencoPossesso.dataScadenzaLeasing", DateUtils.formatDateNotNull(possessoMacchinaElVO.getDataScadenzaLeasing()));
      htmpl.set("blkElencoPossesso.dataInizioValidita", DateUtils.formatDateNotNull(possessoMacchinaElVO.getDataInizioValidita()));
      htmpl.set("blkElencoPossesso.dataFineValidita", DateUtils.formatDateNotNull(possessoMacchinaElVO.getDataFineValidita()));
    }  
  }
  
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
%>


