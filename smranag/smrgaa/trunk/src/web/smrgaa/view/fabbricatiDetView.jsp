<%@page import="it.csi.solmr.dto.anag.terreni.StoricoParticellaVO"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.fabbricati.TipoTipologiaFabbricatoVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.fabbricati.FabbricatoBioVO" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/fabbricatiDet.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    <%@include file = "/view/remoteInclude.inc" %>
 	<%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	UteVO uteVO = (UteVO)request.getAttribute("uteVO");

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	HtmplUtil.setValues(htmpl,anagVO);


 	FabbricatoVO fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");
  
  
  if (Validator.isNotEmpty(fabbricatoVO.getUnitaMisura()))
    htmpl.set("unitaMisura", "("+fabbricatoVO.getUnitaMisura()+")");
  else htmpl.set("unitaMisura", "");
  
  

 	fabbricatoVO.setSuperficieFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieFabbricato()));
 	fabbricatoVO.setDimensioneFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getDimensioneFabbricato()));
 	fabbricatoVO.setAltezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getAltezzaFabbricato()));
 	fabbricatoVO.setLarghezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLarghezzaFabbricato()));
 	fabbricatoVO.setLunghezzaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLunghezzaFabbricato()));
  fabbricatoVO.setVolumeUtilePresuntoFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getVolumeUtilePresuntoFabbricato()));
  fabbricatoVO.setSuperficieCopertaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieCopertaFabbricato()));
  fabbricatoVO.setSuperficieScopertaFabbricato(StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieScopertaFabbricato()));


 	htmpl.set("dataInizioValiditaFabbricato", DateUtils.formatDate(fabbricatoVO.getDataInizioValiditaFabbricato()));
 	if(fabbricatoVO.getDataFineValiditaFabbricato() != null) 
  {
  	htmpl.set("dataFineValiditaFabbricato", DateUtils.formatDate(fabbricatoVO.getDataFineValiditaFabbricato()));
 	}
 	if(uteVO != null) 
  {
  	if(Validator.isNotEmpty(uteVO.getIndirizzo())) 
    {
   		htmpl.set("indirizzoUte", "- "+uteVO.getIndirizzo());
   	}
 	}
  
  Long idTipologiaFabbricato=null;

  if((fabbricatoVO != null) && (fabbricatoVO.getIdTipologiaFabbricato() != null))
  { 
    idTipologiaFabbricato=fabbricatoVO.getIdTipologiaFabbricato();
    TipoTipologiaFabbricatoVO tipoTipologiaFabbricatoVO = fabbricatoVO.getTipoTipologiaFabbricatoVO();
    if(tipoTipologiaFabbricatoVO != null)
    {
      if(tipoTipologiaFabbricatoVO.getVLabel() !=null)
      {
        Vector vLabel = tipoTipologiaFabbricatoVO.getVLabel();
        if(vLabel.size() == 2)
        {
          htmpl.newBlock("tipologiaFabbricatoDimensioniA");
          htmpl.set("tipologiaFabbricatoDimensioniA.larghezzaFabbricatoLabel",(String)vLabel.get(0));
          htmpl.set("tipologiaFabbricatoDimensioniA.lunghezzaFabbricatoLabel",(String)vLabel.get(1));
          htmpl.set("tipologiaFabbricatoDimensioniA.larghezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLarghezzaFabbricato()));
          htmpl.set("tipologiaFabbricatoDimensioniA.lunghezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLunghezzaFabbricato()));
        }
        else if(vLabel.size() == 3)
        {
          htmpl.newBlock("tipologiaFabbricatoDimensioniB");
          htmpl.set("tipologiaFabbricatoDimensioniB.larghezzaFabbricatoLabel",(String)vLabel.get(0));
          htmpl.set("tipologiaFabbricatoDimensioniB.lunghezzaFabbricatoLabel",(String)vLabel.get(1));
          htmpl.set("tipologiaFabbricatoDimensioniB.altezzaFabbricatoLabel",(String)vLabel.get(2));
          htmpl.set("tipologiaFabbricatoDimensioniB.larghezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLarghezzaFabbricato()));
          htmpl.set("tipologiaFabbricatoDimensioniB.lunghezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getLunghezzaFabbricato()));
          htmpl.set("tipologiaFabbricatoDimensioniB.altezzaFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getAltezzaFabbricato()));
        }
        else
        {
          throw new SolmrException("Il Vettore con le Label ha dimensione diversa da 2 o 3");
        }
      }
    
      String flagStoccaggio = tipoTipologiaFabbricatoVO.getFlagStoccaggio();
      if(Validator.isNotEmpty(flagStoccaggio) && flagStoccaggio.equalsIgnoreCase(SolmrConstants.STRUTTURA_STANDARD))
      {
        htmpl.set("dimcolspan","1");
        htmpl.set("dimcolspanAnno","2");
        htmpl.newBlock("blkFlagStoccaggio");
        htmpl.set("blkFlagStoccaggio.volumeUtilePresunto", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getVolumeUtilePresuntoFabbricato()));
        htmpl.set("blkFlagStoccaggio.superficieScopertaExtraFabbricato", StringUtils.parseDoubleFieldOneDecimal(fabbricatoVO.getSuperficieScopertaExtraFabbricato()));
      }
    }
    
  }
  
  htmpl.set("dimcolspan","2");
  htmpl.set("dimcolspanAnno","5");
  
  
 	if(fabbricatoVO.getTipiFormaFabbricato() != null && !"".equals(fabbricatoVO.getTipiFormaFabbricato())) 
  {
  	htmpl.newBlock("cmbForma");
  	htmpl.set("cmbForma.descrizioneTipoFormaFabbricato",fabbricatoVO.getDescrizioneTipoFormaFabbricato());
 	}
 	if(fabbricatoVO.getTipologiaColturaSerra() != null 
    && !"".equals(fabbricatoVO.getTipologiaColturaSerra())) 
  {
  	htmpl.newBlock("cmbSerra");
  	htmpl.set("cmbSerra.descrizioneTipologiaColturaSerra",fabbricatoVO.getDescrizioneTipologiaColturaSerra());
  	htmpl.set("cmbSerra.mesiRiscSerra",fabbricatoVO.getMesiRiscSerra());
  	htmpl.set("cmbSerra.oreRisc",fabbricatoVO.getOreRisc());
 	}
  
  //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
  //al ruolo!
  String dateUlt = "";
  if(fabbricatoVO.getDataAggiornamento() !=null)
  {
    dateUlt = DateUtils.formatDate(fabbricatoVO.getDataAggiornamento());
  } 
  ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
    "ultimaModificaVw", dateUlt, fabbricatoVO.getUtenteUltimaModifica(),
    fabbricatoVO.getEnteUltimaModifica(),fabbricatoVO.getMotivoModifica());
  
 	HtmplUtil.setValues(htmpl, fabbricatoVO);

 	StoricoParticellaVO[] elencoParticelleFabbricato = 
    (StoricoParticellaVO[])request.getAttribute("elencoParticelleFabbricato");
 	if(elencoParticelleFabbricato != null && elencoParticelleFabbricato.length > 0) 
  {
  	htmpl.newBlock("blkParticelleFabbricato");
  	for(int i = 0; i < elencoParticelleFabbricato.length; i++) 
    {
    	htmpl.newBlock("blkParticelleFabbricato.blkElenco");
    	StoricoParticellaVO particellaFabbricatoVO = (StoricoParticellaVO)elencoParticelleFabbricato[i];
    	htmpl.set("blkParticelleFabbricato.blkElenco.descComuneParticellaFabbricato", particellaFabbricatoVO.getComuneParticellaVO().getDescom());
    	htmpl.set("blkParticelleFabbricato.blkElenco.siglaProv", particellaFabbricatoVO.getComuneParticellaVO().getSiglaProv());
    	htmpl.set("blkParticelleFabbricato.blkElenco.sezioneParticellaFabbricato", particellaFabbricatoVO.getSezione());
    	htmpl.set("blkParticelleFabbricato.blkElenco.foglioParticellaFabbricato", particellaFabbricatoVO.getFoglio());
    	htmpl.set("blkParticelleFabbricato.blkElenco.particellaFabbricato", particellaFabbricatoVO.getParticella().toString());
    	htmpl.set("blkParticelleFabbricato.blkElenco.subalternoParticellaFabbricato", particellaFabbricatoVO.getSubalterno());
    	htmpl.set("blkParticelleFabbricato.blkElenco.superficieCatastaleParticellaFabbricato", StringUtils.parseDoubleFieldOneDecimal(particellaFabbricatoVO.getSupCatastale()));
    	if(particellaFabbricatoVO.getDataFineValidita() != null) 
      {
      	htmpl.set("blkParticelleFabbricato.blkElenco.dataFineValidita", DateUtils.formatDate(particellaFabbricatoVO.getDataFineValidita()));
     	}
   	}
 	}
 	else 
  {
  	htmpl.newBlock("blkNoParticelleFabbricato");
 	}
  
  
  
  //Dati biologico
  FabbricatoBioVO fabbricatoBioVO = (FabbricatoBioVO)request.getAttribute("fabbricatoBioVO");
  if(fabbricatoBioVO != null)
  {
    //Prendo la prima data poichè dovrebbero essere tutte uguali!!!!
    htmpl.set("dataAggiornamentoAbio", "del " +DateUtils.formatDateNotNull(fabbricatoBioVO.getDataInizioValidita())); 
    htmpl.newBlock("blkDatiAbio");    
    
    htmpl.set("blkDatiAbio.dimensioneConvenzionale", Formatter.formatDouble4(
      fabbricatoBioVO.getDimensioneConvenzionale()));
    htmpl.set("blkDatiAbio.dimensioneBiologica", Formatter.formatDouble4(
      fabbricatoBioVO.getDimensioneBiologico()));     
    
  }
  else
  {
    htmpl.newBlock("blkNoDatiAbio");
    htmpl.set("blkNoDatiAbio.nessunDato", SolmrConstants.NO_BIOLOGICO_FABBRICATI);
  }
  
  

%>
<%= htmpl.text()%>
