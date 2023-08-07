<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.jsf.htmpl.*"%>
<%@page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO"%>
<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@page import="it.csi.solmr.dto.anag.ParticellaCertificataVO"%>
<%@page import="it.csi.solmr.dto.anag.ParticellaCertElegVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%!public static final String LAYOUT       = "/layout/ricercaTerrenoDettaglio.htm";
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
  ParticellaCertificataVO particellaCertificataVO = (ParticellaCertificataVO) request
      .getAttribute("particellaCertificataVO");  
  Vector<TipoAreaVO> vTipoArea = (Vector<TipoAreaVO>)request.getAttribute("vTipoArea");
      
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
    
  // Dati relativi alla particella certificata
  if(particellaCertificataVO.isCertificata() && particellaCertificataVO.isUnivoca()) 
  {
    htmpl.set("blkDatiDettaglio.descrizioneFonte", "Fonte " +particellaCertificataVO.getFonteDato().getDescription());
    htmpl.set("blkDatiDettaglio.dataValidazioneFonteCatasto", "del "+DateUtils.formatDate(particellaCertificataVO.getDataValidazioneFonteCatasto()));
    htmpl.set("blkDatiDettaglio.supCatastaleCertificata", StringUtils.parseSuperficieField(particellaCertificataVO.getSupCatastaleCertificata()));
    if(particellaCertificataVO.getTipoQualita() != null) {
      htmpl.set("blkDatiDettaglio.qualitaCertificata", "["+particellaCertificataVO.getTipoQualita().getSecondaryCode()+"] "+particellaCertificataVO.getTipoQualita().getDescription());
    }
    if(Validator.isNotEmpty(particellaCertificataVO.getClasse())) {
      htmpl.set("blkDatiDettaglio.classeCertificata", particellaCertificataVO.getClasse());
    }
    if(particellaCertificataVO.getElencoPorzioniCertificate() != null && particellaCertificataVO.getElencoPorzioniCertificate().length > 0) {
      htmpl.set("blkDatiDettaglio.flagPorzioneCertificata", SolmrConstants.FLAG_SI);
    }
    else {
      htmpl.set("blkDatiDettaglio.flagPorzioneCertificata", SolmrConstants.FLAG_NO);        
    }
    if(Validator.isNotEmpty(particellaCertificataVO.getPartita())) {
      htmpl.set("blkDatiDettaglio.partitaCertificata", particellaCertificataVO.getPartita());         
    }
      
      
    //nuova eleggibilità ************
    if(particellaCertificataVO.getIdFonteElegg() != null) {
      String descrizioneFonte = "- Fonte " + particellaCertificataVO.getFonteDatoElegg().getDescription();
      if(particellaCertificataVO.getDataValidazioneFonteElegg() != null) {
        descrizioneFonte += " del " + DateUtils.formatDate(particellaCertificataVO.getDataValidazioneFonteElegg());
      }
      htmpl.set("blkDatiDettaglio.descFonteElegg", descrizioneFonte);
    }
    if(Validator.isNotEmpty(particellaCertificataVO.getSupGrafica())) {
      htmpl.set("blkDatiDettaglio.supGrafica", StringUtils.parseSuperficieField(particellaCertificataVO.getSupGrafica()));
    }
    else
    {
      htmpl.set("blkDatiDettaglio.supGrafica", SolmrConstants.DEFAULT_SUPERFICIE);
    }
    
    if(particellaCertificataVO.getVParticellaCertEleg() !=null)
    {
      int size = particellaCertificataVO.getVParticellaCertEleg().size();
      int level = 0;
      for(int i=0;i<size;i++)
      {
        ParticellaCertElegVO partVO = (ParticellaCertElegVO)
            particellaCertificataVO.getVParticellaCertEleg().get(i);
        if(i % 2 == 0)
        {
          htmpl.newBlock("blkDatiEleggibilita");
          level=0;
        }
        
        String desc = partVO.getDescrizione();
        desc = desc.substring(0,1).toUpperCase() + desc.substring(1,desc.length()).toLowerCase();           
        htmpl.set("blkDatiDettaglio.blkDatiEleggibilita.descEleggibilita"+level, desc);
        if(partVO.getDescrizioneFit() !=null)
        {
          String descFit = partVO.getDescrizioneFit();
          descFit = descFit.substring(0,1).toUpperCase() + descFit.substring(1,descFit.length()).toLowerCase(); 
          htmpl.set("blkDatiDettaglio.blkDatiEleggibilita.descEleggibilitaFit"+level, " - "+descFit);
        }
        
        htmpl.set("blkDatiDettaglio.blkDatiEleggibilita.supEleggibilita"+level, Formatter.formatDouble4(partVO.getSuperficie()));
        level++;
      } 
    }
    
    //nuova eleggibilità******Fine
  }
  else 
  {
    htmpl.set("blkDatiDettaglio.descrizioneFonte", SolmrConstants.FONTE_DATI_PARTICELLA_CERTIFICATA_DEFAULT);
    htmpl.set("blkDatiDettaglio.flagPorzioneCertificata", SolmrConstants.FLAG_NO);
    if(!particellaCertificataVO.isCertificata()) {
      htmpl.set("blkDatiDettaglio.supCatastaleCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);         
      htmpl.set("blkDatiDettaglio.qualitaCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
      htmpl.set("blkDatiDettaglio.classeCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
      htmpl.set("blkDatiDettaglio.partitaCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
      
      htmpl.set("blkDatiDettaglio.descFonteElegg", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
      htmpl.set("blkDatiDettaglio.supGrafica", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
      
    }
    else {
      htmpl.set("blkDatiDettaglio.supCatastaleCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_UNIVOCA);
      htmpl.set("blkDatiDettaglio.qualitaCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_UNIVOCA);
      htmpl.set("blkDatiDettaglio.classeCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_UNIVOCA);
      htmpl.set("blkDatiDettaglio.partitaCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_UNIVOCA);
      
      
      htmpl.set("blkDatiDettaglio.descFonteElegg", SolmrConstants.PARTICELLA_CERTIFICATA_NON_UNIVOCA);
      htmpl.set("blkDatiDettaglio.supGrafica", SolmrConstants.PARTICELLA_CERTIFICATA_NON_UNIVOCA);
    }
  }
  
    
  // Dati territoriali e di validita
  if(storicoParticellaVO.getZonaAltimetrica() != null) {
    htmpl.set("blkDatiDettaglio.descZonaAltimetrica", storicoParticellaVO.getZonaAltimetrica().getDescription());       
  }
  if(storicoParticellaVO.getCasoParticolare() != null) {
    htmpl.set("blkDatiDettaglio.descCasoParticolare", storicoParticellaVO.getCasoParticolare().getDescription());       
  }
  if(storicoParticellaVO.getPercentualePendenzaMedia() != null) {
    htmpl.set("blkDatiDettaglio.descPendenzaMedia", Formatter.formatDouble2(storicoParticellaVO.getPercentualePendenzaMedia())+"%");       
  }
  if(storicoParticellaVO.getGradiPendenzaMedia() != null) {
    htmpl.set("blkDatiDettaglio.gradiPendenzaMedia", Formatter.formatDouble2(storicoParticellaVO.getGradiPendenzaMedia()));       
  }
  if(storicoParticellaVO.getGradiEsposizioneMedia() != null) {
    htmpl.set("blkDatiDettaglio.gradiEsposizioneMedia", Formatter.formatDouble2(storicoParticellaVO.getGradiEsposizioneMedia()));       
  }
  if(storicoParticellaVO.getMetriAltitudineMedia() != null) {
    htmpl.set("blkDatiDettaglio.descAltitudineMedia", Formatter.formatDouble(storicoParticellaVO.getMetriAltitudineMedia())+"m");       
  }
  if(Validator.isNotEmpty(storicoParticellaVO.getFlagIrrigabile())) {
    if(storicoParticellaVO.getFlagIrrigabile().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
      htmpl.set("blkDatiDettaglio.flagIrrigabile", SolmrConstants.FLAG_SI);       
    }
    else {
      htmpl.set("blkDatiDettaglio.flagIrrigabile", SolmrConstants.FLAG_NO);
    }
  }
  else {
    htmpl.set("blkDatiDettaglio.flagIrrigabile", SolmrConstants.FLAG_NO);       
  }
  if(storicoParticellaVO.getIdIrrigazione() != null) {
    htmpl.set("blkDatiDettaglio.descIrrigazione", storicoParticellaVO.getTipoIrrigazioneVO().getDescrizione());       
  }  
  
  if(Validator.isNotEmpty(storicoParticellaVO.getFlagCaptazionePozzi())) {
    if(storicoParticellaVO.getFlagCaptazionePozzi().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
      htmpl.set("blkDatiDettaglio.flagCaptazionePozzi", SolmrConstants.FLAG_SI);      
    }
    else {
      htmpl.set("blkDatiDettaglio.flagCaptazionePozzi", SolmrConstants.FLAG_NO);
    }
  }
  else {
    htmpl.set("blkDatiDettaglio.flagCaptazionePozzi", SolmrConstants.FLAG_NO);      
  }
  
  if(storicoParticellaVO.getTipoRotazioneColturaleVO() != null) {
    htmpl.set("blkDatiDettaglio.descRotazioneColturale", storicoParticellaVO.getTipoRotazioneColturaleVO()
      .getDescrizione());       
  }
  else
  {
    htmpl.set("blkDatiDettaglio.descRotazioneColturale", "non presente");
  }
  if(storicoParticellaVO.getTipoTerrazzamentoVO() != null) {
    htmpl.set("blkDatiDettaglio.descTerrazzamento", storicoParticellaVO.getTipoTerrazzamentoVO()
      .getDescrizione());       
  }
  else
  {
    htmpl.set("blkDatiDettaglio.descTerrazzamento", "non presente");
  }
  if(storicoParticellaVO.getTipoMetodoIrriguo() != null) {
    htmpl.set("blkDatiDettaglio.descMetodoIrriguo", storicoParticellaVO.getTipoMetodoIrriguo().getDescrizioneMetodoIrriguo());
  }
  
  if(Validator.isNotEmpty(vTipoArea))
  {
    for(int i=0;i<vTipoArea.size();i++)
    {
      if(i%2 == 0)
      {
        htmpl.newBlock("blkDatiDettaglio.blkRiga");
        htmpl.set("blkDatiDettaglio.blkRiga.desc1",vTipoArea.get(i).getDescrizione());
        htmpl.set("blkDatiDettaglio.blkRiga.valore1",vTipoArea.get(i).getvTipoValoreArea().get(0).getDescrizione());
      }
      else
      {
        htmpl.set("blkDatiDettaglio.blkRiga.desc2",vTipoArea.get(i).getDescrizione());
        htmpl.set("blkDatiDettaglio.blkRiga.valore2",vTipoArea.get(i).getvTipoValoreArea().get(0).getDescrizione());
      }      
    }
  
  }
    
    
    
    
  htmpl.set("blkDatiDettaglio.dataInizioValidita", DateUtils.formatDate(storicoParticellaVO.getDataInizioValidita()));
  if(Validator.isNotEmpty(storicoParticellaVO.getDataCessazione())) {
    htmpl.set("blkDatiDettaglio.dataCessazione", DateUtils.formatDate(storicoParticellaVO.getDataCessazione()));
  }
  
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  String dateUlt = "";
  if(storicoParticellaVO.getDataAggiornamento() !=null)
  {
    dateUlt = DateUtils.formatDate(storicoParticellaVO.getDataAggiornamento());
  }
  String utenteAgg = "";
  String enteAgg = "";
  if(storicoParticellaVO.getUtenteAggiornamento() !=null)
  {
    utenteAgg = storicoParticellaVO.getUtenteAggiornamento().getDenominazione();
    enteAgg = storicoParticellaVO.getUtenteAggiornamento().getDescrizioneEnteAppartenenza();
  }
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "blkDatiDettaglio.ultimaModificaVw", dateUlt, utenteAgg, enteAgg, null);
      
 if(storicoParticellaVO.getCausaleModParticella() != null) {
    htmpl.set("blkDatiDettaglio.descCausaleModParticella", storicoParticellaVO.getCausaleModParticella().getDescription());
  }
  if(Validator.isNotEmpty(storicoParticellaVO.getMotivoModifica())) {
    htmpl.set("blkDatiDettaglio.motivoModifica", " - "+storicoParticellaVO.getMotivoModifica());
  }
  if(storicoParticellaVO.getDocumentoVO() != null) {
    htmpl.set("blkDatiDettaglio.descTipoDocumento", storicoParticellaVO.getDocumentoVO().getTipoDocumentoVO().getDescrizione());
    if(Validator.isNotEmpty(storicoParticellaVO.getDocumentoVO().getNumeroProtocollo())) {
      htmpl.set("blkDatiDettaglio.numeroProtocolloDocumento", " Prot. "+StringUtils.parseNumeroProtocolloField(storicoParticellaVO.getDocumentoVO().getNumeroProtocollo()));
      htmpl.set("blkDatiDettaglio.dataProtocolloDocumento", " del "+DateUtils.formatDate(storicoParticellaVO.getDocumentoVO().getDataProtocollo()));
    }
  }
  htmpl.set("idParticella", StringUtils.checkNull(storicoParticellaVO.getIdParticella()));
  htmpl.set("idStoricoParticella", StringUtils.checkNull(idStoricoParticella));
    
%><%=htmpl.text()%>
<%
  SolmrLogger.debug(this, "[RicercaTerrenoDettaglioView:service] END.");
%>
