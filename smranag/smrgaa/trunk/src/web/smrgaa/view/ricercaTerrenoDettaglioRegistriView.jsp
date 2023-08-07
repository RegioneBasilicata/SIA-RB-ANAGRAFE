<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%!public static final String LAYOUT       = "/layout/ricercaTerrenoDettaglioRegistri.htm";
  public static final String NON_PRESENTE = "non presente";%>

<%
  SolmrLogger.debug(this, "[RicercaTerrenoDettaglioView:service] BEGIN.");

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(LAYOUT);
%>
<%@include file="/view/remoteInclude.inc"%>
<%
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  String idParticella=request
      .getParameter("idParticella");
  htmpl.set("idParticella", idParticella);
  StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO) request
      .getAttribute("storicoParticellaVO");
  Vector<RegistroPascoloVO> vRegistroPascoloVO = (Vector<RegistroPascoloVO>)request
      .getAttribute("vRegistroPascoloVO");
      
  Long idStoricoParticella=storicoParticellaVO.getIdStoricoParticella();
  htmpl.newBlock("blkDatiDettaglio");
  htmpl.set("blkDatiDettaglio.idParticella", idParticella);
  htmpl.set("blkDatiDettaglio.idStoricoParticella", StringUtils.checkNull(idStoricoParticella));
  // Dati di destata
  htmpl.set("blkDatiDettaglio.descComune", storicoParticellaVO.getComuneParticellaVO().getDescom());
  if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia())) {
    htmpl.set("blkDatiDettaglio.siglaProvincia", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")");       
  }
  if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
    htmpl.set("blkDatiDettaglio.sezione", storicoParticellaVO.getSezione());
  }
  htmpl.set("blkDatiDettaglio.foglio", storicoParticellaVO.getFoglio());
  if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
    htmpl.set("blkDatiDettaglio.particella", storicoParticellaVO.getParticella());
  }
  if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
    htmpl.set("blkDatiDettaglio.subalterno", storicoParticellaVO.getSubalterno());
  }
  htmpl.set("blkDatiDettaglio.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
  htmpl.set("blkDatiDettaglio.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
    
  
  
  //Registro storico pascoli
  if(vRegistroPascoloVO != null)
  {
    htmpl.newBlock("blkDatiDettaglio.blkDatiRegistroPascoli");
    for(int i=0;i<vRegistroPascoloVO.size();i++)
    {
      htmpl.newBlock("blkDatiDettaglio.blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli");
      htmpl.set("blkDatiDettaglio.blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.annoCampagna",
         ""+vRegistroPascoloVO.get(i).getAnnoCampagna());
      htmpl.set("blkDatiDettaglio.blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.descFonte",
         vRegistroPascoloVO.get(i).getDescFonte());
      htmpl.set("blkDatiDettaglio.blkDatiRegistroPascoli.blkElencoDatiRegistroPascoli.superficie", 
        Formatter.formatDouble4(vRegistroPascoloVO.get(i).getSuperficie()));
    }
     
  }
  else
  {
    htmpl.newBlock("blkDatiDettaglio.blkNoDatiRegistroPascoli");
  }

 
  htmpl.set("idParticella", StringUtils.checkNull(storicoParticellaVO.getIdParticella()));
  htmpl.set("idStoricoParticella", StringUtils.checkNull(idStoricoParticella));
    
%><%=htmpl.text()%>
<%
  SolmrLogger.debug(this, "[RicercaTerrenoDettaglioView:service] END.");
%>
