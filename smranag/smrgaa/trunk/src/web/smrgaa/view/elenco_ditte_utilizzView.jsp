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
  java.io.InputStream layout = application.getResourceAsStream("/layout/elenco_ditte_utilizz.htm");
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
  
  if(Validator.isNotEmpty(vPossessoMacchina))
  {
    for(int i=0;i<vPossessoMacchina.size();i++)
    {
      PossessoMacchinaVO possessoMacchinaElVO = vPossessoMacchina.get(i);
      htmpl.newBlock("blkElencoAzienda");
      htmpl.set("blkElencoAzienda.idPossessoMacchinaEl", ""+possessoMacchinaElVO.getIdPossessoMacchina());
      htmpl.set("blkElencoAzienda.cuaa", possessoMacchinaElVO.getCuaa());
      htmpl.set("blkElencoAzienda.partitaIVA", possessoMacchinaElVO.getPartitaIva());
      htmpl.set("blkElencoAzienda.denominazione", possessoMacchinaElVO.getDenominazione());
      if(Validator.isNotEmpty(possessoMacchinaElVO.getDittaUma()))
      {
        String dittaUma = possessoMacchinaElVO.getDittaUma()+"/"+possessoMacchinaElVO.getSiglaProvinciaUma();
        htmpl.set("blkElencoAzienda.dittaUma", dittaUma);
      }
      htmpl.set("blkElencoAzienda.percentualePossesso", Formatter.formatDouble(possessoMacchinaElVO.getPercentualePossesso()));
      htmpl.set("blkElencoAzienda.descFormaPossesso", possessoMacchinaElVO.getTipoFormaPossessoGaaVO().getDescrizione());
      htmpl.set("blkElencoAzienda.dataInizioValidita", DateUtils.formatDateNotNull(possessoMacchinaElVO.getDataInizioValidita()));
      htmpl.set("blkElencoAzienda.dataFineValidita", DateUtils.formatDateNotNull(possessoMacchinaElVO.getDataFineValidita()));
      htmpl.set("blkElencoAzienda.dataCarico", DateUtils.formatDateNotNull(possessoMacchinaElVO.getDataCarico()));
      htmpl.set("blkElencoAzienda.dataScarico", DateUtils.formatDateNotNull(possessoMacchinaElVO.getDataScarico()));
      htmpl.set("blkElencoAzienda.descScarico", possessoMacchinaElVO.getDescTipoScarico());
    } 
  }
  
  
  
  
  
  
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
%>

