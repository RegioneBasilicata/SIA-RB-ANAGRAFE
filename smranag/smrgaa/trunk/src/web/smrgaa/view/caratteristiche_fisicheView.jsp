<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
  java.io.InputStream layout = application.getResourceAsStream("/layout/caratteristiche_fisiche.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  DittaUMAVO dittaUmaVO = (DittaUMAVO)request.getAttribute("dittaUmaVO");
  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  PossessoMacchinaVO possessoMacchinaVO = (PossessoMacchinaVO)request.getAttribute("possessoMacchinaVO");
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  RuoloUtenza ruoloUtenzaMod = (RuoloUtenza)request.getAttribute("ruoloUtenzaMod");

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  htmpl.set("idPossessoMacchina",request.getParameter("idPossessoMacchina"));

  
  if(Validator.isNotEmpty(dittaUmaVO))
  {     
    htmpl.newBlock("blkDittaUma");
    htmpl.set("blkDittaUma.numeroDittaUma", dittaUmaVO.getDittaUMA());
    htmpl.set("blkDittaUma.siglaProvinciaUma", dittaUmaVO.getProvincia());
  }
  else
  {
    messaggioErrore = (String)AnagErrors.get("ERR_DITTA_UMA_NO_PRESENTE");
  }


  String descUte = possessoMacchinaVO.getComunUte()
    +" ("+possessoMacchinaVO.getSglProvinciaUte()+") "+possessoMacchinaVO.getIndirizzoUte();
  htmpl.set("descUte", descUte);
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
    htmpl.set("matricolaTelaio", macchinaVO.getMatricolaTelaio());
    if(Validator.isNotEmpty(macchinaVO.getAnnoCostruzione()))
      htmpl.set("annoCostruzione",""+macchinaVO.getAnnoCostruzione());      
    htmpl.set("matricolaMotore",macchinaVO.getMatricolaMotore());
    htmpl.set("numeroOmologazione",macchinaVO.getNumeroOmologazione());
    if(Validator.isNotEmpty(macchinaVO.getLastNumeroTargaVO()))
      htmpl.set("numeroTarga", macchinaVO.getLastNumeroTargaVO().getNumeroTarga());
    if(Validator.isNotEmpty(macchinaVO.getCalorie()))
      htmpl.set("calorie", ""+macchinaVO.getCalorie());
    if(Validator.isNotEmpty(macchinaVO.getPotenzaKw()))
      htmpl.set("potenzaKw", ""+macchinaVO.getPotenzaKw());
    if(Validator.isNotEmpty(macchinaVO.getPotenzaCv()))
      htmpl.set("potenzaCv", ""+macchinaVO.getPotenzaCv());
      
    htmpl.set("descTipoAlimentazione", macchinaVO.getDescTipoAlimentazione());
    htmpl.set("numeroAssi", macchinaVO.getNumeroAssi());
    htmpl.set("descNazionalita", macchinaVO.getDescTipoNazionalita());
    htmpl.set("tara", Formatter.formatDouble2(macchinaVO.getTara()));
    htmpl.set("lordo", Formatter.formatDouble2(macchinaVO.getLordo()));
    htmpl.set("descTrazione", macchinaVO.getDescTipoTrazione());
    htmpl.set("illuminazione", macchinaVO.getIlluminazione());
    htmpl.set("note", macchinaVO.getNote());
    ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
      "descUtente", DateUtils.formatDateNotNull(macchinaVO.getDataAggiornamento()), 
      ruoloUtenzaMod.getDenominazione(), ruoloUtenzaMod.getDescrizioneEnte(), null);
    
  }
  
  
     
      
  
  
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  
  
  HtmplUtil.setErrors(htmpl, errors, request, application);
  out.print(htmpl.text());
%>

