<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/unitaArboreeDettaglio.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
		<%@include file = "/view/remoteInclude.inc" %>
	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
 	FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	AltroVitignoVO[] elencoAltriVitigni = (AltroVitignoVO[])request.getAttribute("elencoAltriVitigni");
  Vector<TipoTipologiaVinoVO> elencoDocRicaduta = (Vector<TipoTipologiaVinoVO>)request.getAttribute("elencoDocRicaduta");
 	AltroVitignoDichiaratoVO[] elencoAltriVitigniDichiarati = (AltroVitignoDichiaratoVO[])request.getAttribute("elencoAltriVitigniDichiarati");
 	
 	// Se si è verificato qualche errore visualizzo il messaggio all'utente
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti estraggo i dati
 	else 
  {
  
    String idUnita = request.getParameter("idUnita");
    htmpl.set("idUnita", idUnita);
    htmpl.set("idParticella", ""+storicoParticellaVO.getIdParticella());
  
 		htmpl.newBlock("blkDatiDettaglio");
 		htmpl.set("blkDatiDettaglio.idStoricoParticella", ""+storicoParticellaVO.getIdStoricoParticella());
		// Dati di testata
 		htmpl.set("blkDatiDettaglio.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
 		htmpl.set("blkDatiDettaglio.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
 		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) 
    {
 			htmpl.set("blkDatiDettaglio.sezione", storicoParticellaVO.getSezione());
 		}
 		htmpl.set("blkDatiDettaglio.foglio", storicoParticellaVO.getFoglio());
 		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) 
    {
 			htmpl.set("blkDatiDettaglio.particella", storicoParticellaVO.getParticella());
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) 
    {
 			htmpl.set("blkDatiDettaglio.subalterno", storicoParticellaVO.getSubalterno());
 		}
 		htmpl.set("blkDatiDettaglio.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
    htmpl.set("blkDatiDettaglio.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
    //Nuova eleggibilità!!!
    if(storicoParticellaVO.getParticellaCertificataVO() != null) 
    {
      ParticellaCertificataVO particellaCertificataVO = storicoParticellaVO.getParticellaCertificataVO();
      
      if(Validator.isNotEmpty(particellaCertificataVO.getVParticellaCertEleg()) 
        && (particellaCertificataVO.getVParticellaCertEleg().size() > 0)) 
      {
        //Per la query è popolato solo il primo elemento
        ParticellaCertElegVO partCertVO = (ParticellaCertElegVO)particellaCertificataVO.getVParticellaCertEleg().get(0);
        if(Validator.isNotEmpty(partCertVO.getSuperficie())) {
          htmpl.set("blkDatiDettaglio.supEleggibile", Formatter.formatDouble4(partCertVO.getSuperficie()));         
        }
        else 
        {
          htmpl.set("blkDatiDettaglio.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
        }
      }
      else
      {
        htmpl.set("blkDatiDettaglio.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
      }
    }
    else 
    {
      //htmpl.set("blkDatiDettaglio.supUsoGrafica", SolmrConstants.DEFAULT_SUPERFICIE);
      htmpl.set("blkDatiDettaglio.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
    }    
    //****************************
    
    
    
 		if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) 
    {
    
	 		StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
      
      
      
      
      String menzioneGeografica = "";
      if(Validator.isNotEmpty(storicoUnitaArboreaVO.getElencoMenzioneGeografica()))
      {
        Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = storicoUnitaArboreaVO.getElencoMenzioneGeografica();
        for(int i=0;i<elencoMenzioneGeografica.size();i++)
        {
          TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = elencoMenzioneGeografica.get(i);
          if(i !=0)
          {
            menzioneGeografica += " - "; 
          }          
          menzioneGeografica += tipoMenzioneGeograficaVO.getDescrizione();
        }
      }
      
      htmpl.set("blkDatiDettaglio.menzioneGeografica", menzioneGeografica);
      
      
      
      
	 		htmpl.set("blkDatiDettaglio.progressivo", storicoUnitaArboreaVO.getProgrUnar());
      if(Validator.isNotEmpty(storicoUnitaArboreaVO.getDataImpianto())) {
        htmpl.set("blkDatiDettaglio.dataImpianto", DateUtils.formatDate(storicoUnitaArboreaVO.getDataImpianto()));      
      }
	 		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdUtilizzo())) 
	 		{
	 			htmpl.set("blkDatiDettaglio.descUtilizzo", "["+storicoUnitaArboreaVO.getTipoUtilizzoVO().getCodice()+"] "
				 +storicoUnitaArboreaVO.getTipoUtilizzoVO().getDescrizione());
	 		}
	 		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdTipoDestinazione())) 
      {
        htmpl.set("blkDatiDettaglio.descDestinazione", "["+storicoUnitaArboreaVO.getTipoDestinazioneVO().getCodiceDestinazione()+"] "
         +storicoUnitaArboreaVO.getTipoDestinazioneVO().getDescrizioneDestinazione());
      }
      if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdTipoDettaglioUso())) 
      {
        htmpl.set("blkDatiDettaglio.descDettaglioUso", "["+storicoUnitaArboreaVO.getTipoDettaglioUsoVO().getCodiceDettaglioUso()+"] "
         +storicoUnitaArboreaVO.getTipoDettaglioUsoVO().getDescrizione());
      }
      if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdTipoQualitaUso())) 
      {
        htmpl.set("blkDatiDettaglio.descQualitaUso", "["+storicoUnitaArboreaVO.getTipoQualitaUsoVO().getCodiceQualitaUso()+"] "
         +storicoUnitaArboreaVO.getTipoQualitaUsoVO().getDescrizioneQualitaUso());
      }
	 		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdVarieta())) 
	 		{
	 			htmpl.set("blkDatiDettaglio.descVarieta", "["
	 			  +storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione());
	 		}
	 		// ANNO RIFERIMENTO
      htmpl.set("blkDatiDettaglio.annoRiferimento", storicoUnitaArboreaVO.getAnnoRiferimento());
	 		htmpl.set("blkDatiDettaglio.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
	 		htmpl.set("blkDatiDettaglio.numeroCeppi", storicoUnitaArboreaVO.getNumCeppi());
	 		htmpl.set("blkDatiDettaglio.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
	 		htmpl.set("blkDatiDettaglio.percentualeFallanza", Formatter.formatDouble(storicoUnitaArboreaVO.getPercentualeFallanza()));
	 		if(storicoUnitaArboreaVO.getFlagImproduttiva().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
	 		{
        htmpl.set("blkDatiDettaglio.flagImproduttiva", SolmrConstants.FLAG_SI);
      }
      else 
      {
        htmpl.set("blkDatiDettaglio.flagImproduttiva", SolmrConstants.FLAG_NO);
      }
	 		
	 		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
	 			htmpl.set("blkDatiDettaglio.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
	 		}
	 		if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) {
	 			htmpl.set("blkDatiDettaglio.descFormaAllevamento", storicoUnitaArboreaVO.getTipoFormaAllevamentoVO().getDescrizione());
	 		}
	 		htmpl.set("blkDatiDettaglio.percentualeVarieta", storicoUnitaArboreaVO.getPercentualeVarieta());
	 		// COLTURA SPECIALIZZATA
	 		if(storicoUnitaArboreaVO.getColturaSpecializzata().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	 			htmpl.set("blkDatiDettaglio.colturaSpecializzata", SolmrConstants.FLAG_SI);
	 		}
	 		else {
	 			htmpl.set("blkDatiDettaglio.colturaSpecializzata", SolmrConstants.FLAG_NO);
	 		}
	 		if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	 			htmpl.set("blkDatiDettaglio.altriVitigni", SolmrConstants.FLAG_SI);
	 		}
	 		else {
	 			htmpl.set("blkDatiDettaglio.altriVitigni", SolmrConstants.FLAG_NO);
	 		}
	 		if(storicoUnitaArboreaVO.getIdIrrigazioneUnar() != null) {
	 			htmpl.set("blkDatiDettaglio.descIrrigazione", storicoUnitaArboreaVO.getTipoIrrigazioneUnitaArboreaVO().getDescrizione());
	 		}
	 		// RICADUTA
	 		if(storicoUnitaArboreaVO.getRicaduta().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	 			htmpl.set("blkDatiDettaglio.ricaduta", SolmrConstants.FLAG_SI);
	 		}
	 		else {
	 			htmpl.set("blkDatiDettaglio.ricaduta", SolmrConstants.FLAG_NO);
	 		}
	 		if(Validator.isNotEmpty(storicoUnitaArboreaVO.getDataFineValidita())) {
	 			htmpl.set("blkDatiDettaglio.dataFineValidita", DateUtils.formatDate(storicoUnitaArboreaVO.getDataFineValidita())); 			
	 		}
	 		if(storicoUnitaArboreaVO.getIdCessazioneUnar() != null) {
	 			htmpl.set("blkDatiDettaglio.descCausaleModifica", storicoUnitaArboreaVO.getTipoCessazioneUnarVO().getDescrizione());
	 			htmpl.set("blkDatiDettaglio.labelAggiornamento", "Motivo cessazione");
	 		}
	 		else {
	 			htmpl.set("blkDatiDettaglio.descCausaleModifica", storicoUnitaArboreaVO.getTipoCausaleModificaVO().getDescrizione());
	 			htmpl.set("blkDatiDettaglio.labelAggiornamento", "Motivo aggiornamento");
	 		}
      
      //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
      //al ruolo!
      String dateUlt = "";
      if(storicoUnitaArboreaVO.getDataAggiornamento() !=null)
      {
        dateUlt = DateUtils.formatDate(storicoUnitaArboreaVO.getDataAggiornamento());
      }
      String utenteAgg = "";
      String enteAgg = "";
      if(storicoUnitaArboreaVO.getUtenteIrideVO() !=null)
      {
        utenteAgg = storicoUnitaArboreaVO.getUtenteIrideVO().getDenominazione();
        enteAgg = storicoUnitaArboreaVO.getUtenteIrideVO().getDescrizioneEnteAppartenenza();
      }
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkDatiDettaglio.ultimaModificaVw", dateUlt, utenteAgg, enteAgg, null);
      
	 		// 	 ALTRI VITIGNI
	 		if(elencoAltriVitigni != null && elencoAltriVitigni.length > 0) {
	 			htmpl.newBlock("blkDatiDettaglio.blkAltriVitigni");
	 			for(int i = 0; i < elencoAltriVitigni.length; i++) {
	 				htmpl.newBlock("blkDatiDettaglio.blkAltriVitigni.blkElenco");
	 				AltroVitignoVO altroVitignoVO = (AltroVitignoVO)elencoAltriVitigni[i];
	 				htmpl.set("blkDatiDettaglio.blkAltriVitigni.blkElenco.varietaVitigni", "["+altroVitignoVO.getTipoVarietaVO().getCodiceVarieta()+"] "+altroVitignoVO.getTipoVarietaVO().getDescrizione());
	 				htmpl.set("blkDatiDettaglio.blkAltriVitigni.blkElenco.percentualeVitigno", altroVitignoVO.getPercentualeVitigno());
	 			}
	 		}
			// ISCRIZIONE ALBO VIGNETI
      if(storicoUnitaArboreaVO.getProvinciaCCIAA() != null)
      {
	 		  htmpl.set("blkDatiDettaglio.provinciaCCIAA", storicoUnitaArboreaVO.getProvinciaCCIAA());
      }
      else
      {
        if(storicoParticellaVO.getComuneParticellaVO() != null)
          htmpl.set("blkDatiDettaglio.provinciaCCIAA", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
      }
	 		htmpl.set("blkDatiDettaglio.matricolaCCIAA", storicoUnitaArboreaVO.getMatricolaCCIAA());
	 		htmpl.set("blkDatiDettaglio.annoIscrizioneAlbo", storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());
	 		
      
      
      if(storicoUnitaArboreaVO.getTipoTipologiaVinoVO() !=null)
      {
        
        htmpl.set("blkDatiDettaglio.descTipologiaVino", storicoUnitaArboreaVO
          .getTipoTipologiaVinoVO().getDescrizione());
      
        if((storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getVinoDoc() != null)
          && storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getVinoDoc().equalsIgnoreCase("S"))
        {
          htmpl.set("blkDatiDettaglio.checkedVinoDoc", "checked=\"checked\"");
          htmpl.set("blkDatiDettaglio.vinoDoc", "S");
        }
        else
        {
          htmpl.set("blkDatiDettaglio.checkedVinoDoc", "");
          htmpl.set("blkDatiDettaglio.vinoDoc", "N");
        }
        
        htmpl.set("blkDatiDettaglio.codiceMipaf", storicoUnitaArboreaVO
            .getTipoTipologiaVinoVO().getCodiceMipaf());
        htmpl.set("blkDatiDettaglio.resa", Formatter.formatAndRoundBigDecimal0(
          storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getResa()));
          
        if(Validator.isNotEmpty(storicoUnitaArboreaVO.getVignaVO()))
        {          
          htmpl.set("blkDatiDettaglio.provVignaRegionale", storicoUnitaArboreaVO.getVignaVO().getMenzione()); 
        }
        
        htmpl.set("blkDatiDettaglio.densitaNumCeppiHa", 
          ""+storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getDensitaMinCeppiHa());
      }
      else
      {
        htmpl.set("blkDatiDettaglio.descTipologiaVino", "");
        htmpl.set("blkDatiDettaglio.checkedVinoDoc", "");
        htmpl.set("blkDatiDettaglio.vinoDoc", "N");
        htmpl.set("blkDatiDettaglio.codiceMipaf", "");
        htmpl.set("blkDatiDettaglio.resa","");
        htmpl.set("blkDatiDettaglio.provVignaRegionale","");
        htmpl.set("blkDatiDettaglio.densitaNumCeppiHa", "");
      }
      
      if(storicoUnitaArboreaVO.getDataPrimaProduzione() != null)
      {
        htmpl.set("blkDatiDettaglio.dataPrimaProduzione", DateUtils.formatDate(storicoUnitaArboreaVO.getDataPrimaProduzione()));
      }
      
      if(storicoUnitaArboreaVO.getVigna() != null)
      {
        htmpl.set("blkDatiDettaglio.vigna", storicoUnitaArboreaVO.getVigna());
      }
      else
      {
        htmpl.set("blkDatiDettaglio.vigna", "");
      }
      
      if(storicoUnitaArboreaVO.getEtichetta() != null)
      {
        htmpl.set("blkDatiDettaglio.annotazioneEtichetta", storicoUnitaArboreaVO.getEtichetta());
      }
      else
      {
        htmpl.set("blkDatiDettaglio.annotazioneEtichetta", "");
      }
      
      
      if(storicoUnitaArboreaVO.getTipoGenereIscrizioneVO() != null)
      {
        htmpl.set("blkDatiDettaglio.genereIscrizione", storicoUnitaArboreaVO
          .getTipoGenereIscrizioneVO().getDescrizione());
      }
      
      
      
      int countRigheFiltri = 0;
      if((elencoDocRicaduta != null) && (storicoUnitaArboreaVO.getTipoTipologiaVinoVO() != null))
      {
        htmpl.newBlock("blkDatiDettaglio.blkDocRicaduta");
        for(int j=0;j<elencoDocRicaduta.size();j++)
        {
          TipoTipologiaVinoVO tipoTipologiaVinoVO = elencoDocRicaduta.get(j);
          
          aggiungiBloccoFiltro(countRigheFiltri, htmpl);
          aggiungiFiltro(countRigheFiltri, htmpl, 
            tipoTipologiaVinoVO.getDescrizione(),
            Formatter.formatAndRoundBigDecimal0(
              tipoTipologiaVinoVO.getResa()));
          countRigheFiltri++;          
                    
        }      
      }
      
      if(storicoUnitaArboreaVO.getTipoInterventoViticoloVO() != null)
      {
        htmpl.set("blkDatiDettaglio.descIntervento", storicoUnitaArboreaVO.getTipoInterventoViticoloVO().getDescrizione());
      }
      
      htmpl.set("blkDatiDettaglio.dataIntervento", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataIntervento()));
      htmpl.set("blkDatiDettaglio.dataSovrainnesto", DateUtils.formatDateNotNull(storicoUnitaArboreaVO.getDataSovrainnesto()));
      
           
      
 		}
 		else 
    {
 			UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = storicoParticellaVO.getUnitaArboreaDichiarataVO();
	 		
      
      String menzioneGeografica = "";
      if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getElencoMenzioneGeografica()))
      {
        Vector<TipoMenzioneGeograficaVO> elencoMenzioneGeografica = unitaArboreaDichiarataVO.getElencoMenzioneGeografica();
        for(int i=0;i<elencoMenzioneGeografica.size();i++)
        {
          TipoMenzioneGeograficaVO tipoMenzioneGeograficaVO = elencoMenzioneGeografica.get(i);
          if(i !=0)
          {
            menzioneGeografica += " - "; 
          }          
          menzioneGeografica += tipoMenzioneGeograficaVO.getDescrizione();
        }
      }
      
      htmpl.set("blkDatiDettaglio.menzioneGeografica", menzioneGeografica);
      
      
      htmpl.set("blkDatiDettaglio.progressivo", unitaArboreaDichiarataVO.getProgrUnar());
	 		htmpl.set("blkDatiDettaglio.annoImpianto", unitaArboreaDichiarataVO.getAnnoImpianto());
      if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getDataImpianto())) {
        htmpl.set("blkDatiDettaglio.dataImpianto", DateUtils.formatDate(unitaArboreaDichiarataVO.getDataImpianto()));      
      }
	 		if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getIdUtilizzo())) 
	 		{
	 			htmpl.set("blkDatiDettaglio.descUtilizzo", "["+unitaArboreaDichiarataVO.getTipoUtilizzoVO().getCodice()+"] "
	 			  +unitaArboreaDichiarataVO.getTipoUtilizzoVO().getDescrizione());
	 		}
	 		if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getIdTipoDestinazione())) 
      {
        htmpl.set("blkDatiDettaglio.descDestinazione", "["+unitaArboreaDichiarataVO.getTipoDestinazioneVO().getCodiceDestinazione()+"] "
         +unitaArboreaDichiarataVO.getTipoDestinazioneVO().getDescrizioneDestinazione());
      }
      if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getIdTipoDettaglioUso())) 
      {
        htmpl.set("blkDatiDettaglio.descDettaglioUso", "["+unitaArboreaDichiarataVO.getTipoDettaglioUsoVO().getCodiceDettaglioUso()+"] "
         +unitaArboreaDichiarataVO.getTipoDettaglioUsoVO().getDescrizione());
      }
      if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getIdTipoQualitaUso())) 
      {
        htmpl.set("blkDatiDettaglio.descQualitaUso", "["+unitaArboreaDichiarataVO.getTipoQualitaUsoVO().getCodiceQualitaUso()+"] "
         +unitaArboreaDichiarataVO.getTipoQualitaUsoVO().getDescrizioneQualitaUso());
      }
      if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getIdVarieta())) 
      {
        htmpl.set("blkDatiDettaglio.descVarieta", "["+unitaArboreaDichiarataVO.getTipoVarietaUnitaArboreaVO().getCodice()+"] "
          +unitaArboreaDichiarataVO.getTipoVarietaUnitaArboreaVO().getDescrizione());
      }
	 		// ANNO RIFERIMENTO
	 		htmpl.set("blkDatiDettaglio.annoRiferimento", unitaArboreaDichiarataVO.getAnnoRiferimento());
	 		htmpl.set("blkDatiDettaglio.sestoSuFila", unitaArboreaDichiarataVO.getSestoSuFila());
	 		htmpl.set("blkDatiDettaglio.numeroCeppi", unitaArboreaDichiarataVO.getNumCeppi());
	 		htmpl.set("blkDatiDettaglio.sestoTraFile", unitaArboreaDichiarataVO.getSestoTraFile());
	 		
	 		htmpl.set("blkDatiDettaglio.percentualeFallanza", Formatter.formatDouble(unitaArboreaDichiarataVO.getPercentualeFallanza()));
      if(unitaArboreaDichiarataVO.getFlagImproduttiva().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        htmpl.set("blkDatiDettaglio.flagImproduttiva", SolmrConstants.FLAG_SI);
      }
      else 
      {
        htmpl.set("blkDatiDettaglio.flagImproduttiva", SolmrConstants.FLAG_NO);
      }
	 		
	 		
	 		if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getArea())) {
	 			htmpl.set("blkDatiDettaglio.area", StringUtils.parseSuperficieField(unitaArboreaDichiarataVO.getArea()));
	 		}
	 		if(unitaArboreaDichiarataVO.getIdFormaAllevamento() != null) {
	 			htmpl.set("blkDatiDettaglio.descFormaAllevamento", unitaArboreaDichiarataVO.getTipoFormaAllevamentoVO().getDescrizione());
	 		}
	 		htmpl.set("blkDatiDettaglio.percentualeVarieta", unitaArboreaDichiarataVO.getPercentualeVarieta());
	 		// COLTURA SPECIALIZZATA
	 		if(unitaArboreaDichiarataVO.getColturaSpecializzata().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	 			htmpl.set("blkDatiDettaglio.colturaSpecializzata", SolmrConstants.FLAG_SI);
	 		}
	 		else {
	 			htmpl.set("blkDatiDettaglio.colturaSpecializzata", SolmrConstants.FLAG_NO);
	 		}
	 		if(unitaArboreaDichiarataVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	 			htmpl.set("blkDatiDettaglio.altriVitigni", SolmrConstants.FLAG_SI);
	 		}
	 		else {
	 			htmpl.set("blkDatiDettaglio.altriVitigni", SolmrConstants.FLAG_NO);
	 		}
	 		if(unitaArboreaDichiarataVO.getIdIrrigazioneUnar() != null) {
	 			htmpl.set("blkDatiDettaglio.descIrrigazione", unitaArboreaDichiarataVO.getTipoIrrigazioneUnitaArboreaVO().getDescrizione());
	 		}
	 		// RICADUTA
	 		if(unitaArboreaDichiarataVO.getRicaduta().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	 			htmpl.set("blkDatiDettaglio.ricaduta", SolmrConstants.FLAG_SI);
	 		}
	 		else {
	 			htmpl.set("blkDatiDettaglio.ricaduta", SolmrConstants.FLAG_NO);
	 		}
	 		if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getDataFineValidita())) {
	 			htmpl.set("blkDatiDettaglio.dataFineValidita", DateUtils.formatDate(unitaArboreaDichiarataVO.getDataFineValidita())); 			
	 		}
	 		if(unitaArboreaDichiarataVO.getIdCessazioneUnar() != null) {
	 			htmpl.set("blkDatiDettaglio.descCausaleModifica", unitaArboreaDichiarataVO.getTipoCessazioneUnarVO().getDescrizione());
	 			htmpl.set("blkDatiDettaglio.labelAggiornamento", "Motivo cessazione");
	 		}
	 		else {
	 			htmpl.set("blkDatiDettaglio.descCausaleModifica", unitaArboreaDichiarataVO.getTipoCausaleModificaVO().getDescrizione());
	 			htmpl.set("blkDatiDettaglio.labelAggiornamento", "Motivo aggiornamento");
	 		}
      
      //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
      //al ruolo!
      String dateUlt = "";
      if(unitaArboreaDichiarataVO.getDataAggiornamento() !=null)
      {
        dateUlt = DateUtils.formatDate(unitaArboreaDichiarataVO.getDataAggiornamento());
      }
      String utenteAgg = "";
      String enteAgg = "";
      if(unitaArboreaDichiarataVO.getUtenteIrideVO() !=null)
      {
        utenteAgg = unitaArboreaDichiarataVO.getUtenteIrideVO().getDenominazione();
        enteAgg = unitaArboreaDichiarataVO.getUtenteIrideVO().getDescrizioneEnteAppartenenza();
      }
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkDatiDettaglio.ultimaModificaVw", dateUlt, utenteAgg, enteAgg, null);
        
        
	 		// ALTRI VITIGNI
	 		if(elencoAltriVitigniDichiarati != null && elencoAltriVitigniDichiarati.length > 0) {
	 			htmpl.newBlock("blkDatiDettaglio.blkAltriVitigni");
	 			for(int i = 0; i < elencoAltriVitigniDichiarati.length; i++) {
	 				htmpl.newBlock("blkDatiDettaglio.blkAltriVitigni.blkElenco");
	 				AltroVitignoDichiaratoVO altroVitignoDichiaratoVO = (AltroVitignoDichiaratoVO)elencoAltriVitigniDichiarati[i];
	 				htmpl.set("blkDatiDettaglio.blkAltriVitigni.blkElenco.varietaVitigni", "["+altroVitignoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()+"] "+altroVitignoDichiaratoVO.getTipoVarietaVO().getDescrizione());
	 				htmpl.set("blkDatiDettaglio.blkAltriVitigni.blkElenco.percentualeVitigno", altroVitignoDichiaratoVO.getPercentualeVitigno());
	 			}
	 		}
			// ISCRIZIONE ALBO VIGNETI
      if(unitaArboreaDichiarataVO.getProvinciaCCIAA() != null)
      {
        htmpl.set("blkDatiDettaglio.provinciaCCIAA", unitaArboreaDichiarataVO.getProvinciaCCIAA());
      }
      else
      {
        if(storicoParticellaVO.getComuneParticellaVO() != null)
          htmpl.set("blkDatiDettaglio.provinciaCCIAA", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
      }
      htmpl.set("blkDatiDettaglio.matricolaCCIAA", unitaArboreaDichiarataVO.getMatricolaCCIAA());
      htmpl.set("blkDatiDettaglio.annoIscrizioneAlbo", unitaArboreaDichiarataVO.getAnnoIscrizioneAlbo());
      
     
      
      if(unitaArboreaDichiarataVO.getTipoTipologiaVinoVO() !=null)
      {
        
        htmpl.set("blkDatiDettaglio.descTipologiaVino", unitaArboreaDichiarataVO
          .getTipoTipologiaVinoVO().getDescrizione());
      
        if((unitaArboreaDichiarataVO.getTipoTipologiaVinoVO().getVinoDoc() != null)
          && unitaArboreaDichiarataVO.getTipoTipologiaVinoVO().getVinoDoc().equalsIgnoreCase("S"))
        {
          htmpl.set("blkDatiDettaglio.checkedVinoDoc", "checked=\"checked\"");
          htmpl.set("blkDatiDettaglio.vinoDoc", "S");
        }
        else
        {
          htmpl.set("blkDatiDettaglio.checkedVinoDoc", "");
          htmpl.set("blkDatiDettaglio.vinoDoc", "N");
        }
        
        htmpl.set("blkDatiDettaglio.codiceMipaf", unitaArboreaDichiarataVO
            .getTipoTipologiaVinoVO().getCodiceMipaf());
            
        htmpl.set("blkDatiDettaglio.resa", Formatter.formatAndRoundBigDecimal0(unitaArboreaDichiarataVO
            .getTipoTipologiaVinoVO().getResa()));
            
        if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getVignaVO()))
        {          
          htmpl.set("blkDatiDettaglio.provVignaRegionale", unitaArboreaDichiarataVO
              .getVignaVO().getMenzione()); 
        }
        
        htmpl.set("blkDatiDettaglio.densitaNumCeppiHa", 
          ""+unitaArboreaDichiarataVO.getTipoTipologiaVinoVO().getDensitaMinCeppiHa());
      }
      else
      {
        htmpl.set("blkDatiDettaglio.descTipologiaVino", "");
        htmpl.set("blkDatiDettaglio.checkedVinoDoc", "");
        htmpl.set("blkDatiDettaglio.vinoDoc", "N");
        htmpl.set("blkDatiDettaglio.codiceMipaf", "");
        htmpl.set("blkDatiDettaglio.resa","");
        htmpl.set("blkDatiDettaglio.provVignaRegionale", "");
        htmpl.set("blkDatiDettaglio.densitaNumCeppiHa", ""); 
      }
      
      if(unitaArboreaDichiarataVO.getDataPrimaProduzione() != null)
      {
        htmpl.set("blkDatiDettaglio.dataPrimaProduzione", DateUtils.formatDate(unitaArboreaDichiarataVO.getDataPrimaProduzione()));
      }
      
      if(unitaArboreaDichiarataVO.getVigna() != null)
      {
        htmpl.set("blkDatiDettaglio.vigna", unitaArboreaDichiarataVO.getVigna());
      }
      else
      {
        htmpl.set("blkDatiDettaglio.vigna", "");
      }
      
      if(unitaArboreaDichiarataVO.getEtichetta() != null)
      {
        htmpl.set("blkDatiDettaglio.annotazioneEtichetta", unitaArboreaDichiarataVO.getEtichetta());
      }
      else
      {
        htmpl.set("blkDatiDettaglio.annotazioneEtichetta", "");
      }
      
      
      if(unitaArboreaDichiarataVO.getTipoGenereIscrizioneVO() != null)
      {
        htmpl.set("blkDatiDettaglio.genereIscrizione", unitaArboreaDichiarataVO
          .getTipoGenereIscrizioneVO().getDescrizione());
      }
      
      
      int countRigheFiltri = 0;
      if((elencoDocRicaduta != null) && (unitaArboreaDichiarataVO.getTipoTipologiaVinoVO() != null))
      {
        htmpl.newBlock("blkDatiDettaglio.blkDocRicaduta");
        for(int j=0;j<elencoDocRicaduta.size();j++)
        {
          TipoTipologiaVinoVO tipoTipologiaVinoVO = elencoDocRicaduta.get(j);
          
          aggiungiBloccoFiltro(countRigheFiltri, htmpl);
          aggiungiFiltro(countRigheFiltri, htmpl, 
            tipoTipologiaVinoVO.getDescrizione(),
            Formatter.formatAndRoundBigDecimal0(
              tipoTipologiaVinoVO.getResa()));
          countRigheFiltri++;          
                    
        }      
      }
      
      
      if(unitaArboreaDichiarataVO.getTipoInterventoViticoloVO() != null)
      {
        htmpl.set("blkDatiDettaglio.descIntervento", unitaArboreaDichiarataVO.getTipoInterventoViticoloVO().getDescrizione());
      }
      
      htmpl.set("blkDatiDettaglio.dataIntervento", DateUtils.formatDateNotNull(unitaArboreaDichiarataVO.getDataIntervento()));
      htmpl.set("blkDatiDettaglio.dataSovrainnesto", DateUtils.formatDateNotNull(unitaArboreaDichiarataVO.getDataSovrainnesto()));
      
      
 		}
 	}

%>
<%= htmpl.text()%>


<%!
  
  public void aggiungiBloccoFiltro(int count, Htmpl htmpl)
  {
    if((count % 2) == 0)
    {
      htmpl.newBlock("blkDatiDettaglio.blkDocRicaduta.blkElencoDocRicaduta");
    }
  }
  
  public void aggiungiFiltro(int count, Htmpl htmpl, String val1, String val2)
  {
    if((count % 2) == 0)
    {
      htmpl.set("blkDatiDettaglio.blkDocRicaduta.blkElencoDocRicaduta.docRicadutaZero",val1);
      htmpl.set("blkDatiDettaglio.blkDocRicaduta.blkElencoDocRicaduta.resaRicadutaZero",val2);
    }
    
    if((count % 2)  == 1)
    {
      htmpl.set("blkDatiDettaglio.blkDocRicaduta.blkElencoDocRicaduta.docRicadutaUno",val1);
      htmpl.set("blkDatiDettaglio.blkDocRicaduta.blkElencoDocRicaduta.resaRicadutaUno",val2);
    }
    
  }
  

%>
