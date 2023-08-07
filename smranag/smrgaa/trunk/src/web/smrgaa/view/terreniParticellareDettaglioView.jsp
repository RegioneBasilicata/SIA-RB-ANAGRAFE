<%@page import="it.csi.solmr.etc.SolmrConstants"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.ParticellaBioVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareDettaglio.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String urlRSDI = (String) SolmrConstants.get("RSDI_URL");
 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	Long idConduzione = (Long)request.getAttribute("idConduzione");
 	ParticellaCertificataVO particellaCertificataVO = (ParticellaCertificataVO)request.getAttribute("particellaCertificataVO");
  ParticellaBioVO particellaBioVO = (ParticellaBioVO)request.getAttribute("particellaBioVO");
 	Vector<ProprietaCertificataVO> vProprietaCertificataVO = (Vector<ProprietaCertificataVO>)request.getAttribute("vProprietaCertificataVO");
 	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  Vector<TipoAreaVO> vTipoArea = (Vector<TipoAreaVO>)request.getAttribute("vTipoArea");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	// Se si è verificato qualche errore visualizzo il messaggio all'utente
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti estraggo i dati
 	else 
  {
 		htmpl.set("idParticella", storicoParticellaVO.getIdParticella().toString());
 		htmpl.set("idConduzione", idConduzione.toString());
		htmpl.set("idAzienda", anagAziendaVO.getIdAzienda().toString());
		htmpl.set("urlRSDI", urlRSDI);
 		htmpl.newBlock("blkDatiDettaglio");
		// Dati di destata
		htmpl.set("blkDatiDettaglio.idConduzione", idConduzione.toString());
		htmpl.set("blkDatiDettaglio.idAzienda", anagAziendaVO.getIdAzienda().toString());
		htmpl.set("blkDatiDettaglio.urlRSDI", urlRSDI);
 		
 		String titleGis = "GIS";
 		if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()))
 		{          
	    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	        .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	    {
	      titleGis += " - foglio stabilizzato.";
	    }
	    else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	       && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	         .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
	    {
	      titleGis += "- foglio in corso di stabilizzazione.";
	    }
	  }                
    htmpl.set("blkDatiDettaglio.controlliP", titleGis);
    
    String immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS;
    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO()))
    {
	    if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	        .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
	    {
	      immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS_STAB;
	    }
	    else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
	      && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
	          .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
	    {
	      immaginiControlliP = SolmrConstants.IMMAGINE_GIS_GIS_STAB_CORSO;
	    }
	  }          
    htmpl.set("blkDatiDettaglio.immaginiControlliP", immaginiControlliP);
 		
 		
 		
 		
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
      
      
      htmpl.newBlock("blkDatiDettaglio.blkDatiStoricoEleggibilita");
      
 			htmpl.set("blkDatiDettaglio.descrizioneFonte", particellaCertificataVO.getFonteDato().getDescription());
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
      
      //Nuova eleggibilita inizio
      if(particellaCertificataVO.getIdFonteElegg() != null) {
        String descrizioneFonte = particellaCertificataVO.getFonteDatoElegg().getDescription();
        if(particellaCertificataVO.getDataValidazioneFonteElegg() != null) {
          descrizioneFonte += " del " + DateUtils.formatDate(particellaCertificataVO.getDataValidazioneFonteElegg());
        }
        htmpl.set("blkDatiDettaglio.descFonteElegg", " - Fonte "+descrizioneFonte);
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
      
      //Nuova eleggibilita ********** fine
 		}
 		else 
    {
 			htmpl.set("blkDatiDettaglio.descrizioneFonte", SolmrConstants.FONTE_DATI_PARTICELLA_CERTIFICATA_DEFAULT);
 			htmpl.set("blkDatiDettaglio.flagPorzioneCertificata", SolmrConstants.FLAG_NO);
 			if(!particellaCertificataVO.isCertificata()) 
      {
 				htmpl.set("blkDatiDettaglio.supCatastaleCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA); 				
 				htmpl.set("blkDatiDettaglio.qualitaCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
 				htmpl.set("blkDatiDettaglio.classeCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
 				htmpl.set("blkDatiDettaglio.partitaCertificata", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
        
        
        htmpl.set("blkDatiDettaglio.descFonteElegg", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
        htmpl.set("blkDatiDettaglio.supGrafica", SolmrConstants.PARTICELLA_CERTIFICATA_NON_CENSITA);
 				
 			}
 			else 
      {
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
 		
 		if(Validator.isNotEmpty(storicoParticellaVO.getFlagIrrigabile())) 
    {
      if(storicoParticellaVO.getFlagIrrigabile().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        htmpl.set("blkDatiDettaglio.flagIrrigabile", SolmrConstants.FLAG_SI);       
      }
      else 
      {
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
      htmpl.set("blkDatiDettaglio.descRotazioneColturale", storicoParticellaVO
        .getTipoRotazioneColturaleVO().getDescrizione());       
    }
    else
    {
      htmpl.set("blkDatiDettaglio.descRotazioneColturale", "non presente");
    }
    
    if(storicoParticellaVO.getTipoTerrazzamentoVO() != null) {
      htmpl.set("blkDatiDettaglio.descTerrazzamento", storicoParticellaVO
        .getTipoTerrazzamentoVO().getDescrizione());       
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
    
    
    //Dati biologico
    if(particellaBioVO != null)
    {
      htmpl.set("blkDatiDettaglio.dataAggiornamentoAbio", "del " +DateUtils.formatDateNotNull(particellaBioVO.getDataInizioValidita())); 
      htmpl.newBlock("blkDatiDettaglio.blkDatiAbio");
      htmpl.set("blkDatiDettaglio.blkDatiAbio.supConvenzionale", Formatter.formatDouble4(particellaBioVO.getSupConvenzionale()));
      htmpl.set("blkDatiDettaglio.blkDatiAbio.supBiologico", Formatter.formatDouble4(particellaBioVO.getSupBiologico()));
      htmpl.set("blkDatiDettaglio.blkDatiAbio.supInConversione", Formatter.formatDouble4(particellaBioVO.getSupInConversione()));
      htmpl.set("blkDatiDettaglio.blkDatiAbio.dataInizioConversione", DateUtils.formatDateNotNull(particellaBioVO.getDataInzioConversione())); 
      htmpl.set("blkDatiDettaglio.blkDatiAbio.dataFineConversione", DateUtils.formatDateNotNull(particellaBioVO.getDataFineConversione())); 
    }
    else
    {
      htmpl.newBlock("blkDatiDettaglio.blkNoDatiAbio");
      htmpl.set("blkDatiDettaglio.blkNoDatiAbio.nessunDato", SolmrConstants.NO_BIOLOGICO);
    }
   
    
    //Proprieta certificata
    if(vProprietaCertificataVO != null)
    {
      htmpl.newBlock("blkDatiDettaglio.blkDatiTitolarita");
      String descFonte = "";
      String dataInizioTitolarita = "";
      Date dataMaxTitolarità = null;
      for(int i=0;i<vProprietaCertificataVO.size();i++)
      {
        htmpl.newBlock("blkDatiDettaglio.blkDatiTitolarita.blkElencoDatiTitolarita");
        
        if(Validator.isEmpty(dataMaxTitolarità))
        {
          dataMaxTitolarità = vProprietaCertificataVO.get(i).getDataInizioTitolarita();
          //prendo la prima volta tanto è uguale per tutti..
          descFonte = vProprietaCertificataVO.get(i).getDescFonte();
        }
        else
        {
          //prendo la più recente
          if(dataMaxTitolarità.before(vProprietaCertificataVO.get(i).getDataInizioTitolarita()))
            dataMaxTitolarità = vProprietaCertificataVO.get(i).getDataInizioTitolarita();
        }
        
        htmpl.set("blkDatiDettaglio.blkDatiTitolarita.blkElencoDatiTitolarita.codiceFiscale",
           vProprietaCertificataVO.get(i).getCuaa());
        String denominazione = vProprietaCertificataVO.get(i).getDenominazione();
        if(Validator.isEmpty(denominazione))
        {
          denominazione = vProprietaCertificataVO.get(i).getCognome()+" "+vProprietaCertificataVO.get(i).getNome();
        }
        htmpl.set("blkDatiDettaglio.blkDatiTitolarita.blkElencoDatiTitolarita.denominazione",
           denominazione);
        String diritto = "["+vProprietaCertificataVO.get(i).getCodiceDiritto()+"] "
          +vProprietaCertificataVO.get(i).getDescDiritto();
        htmpl.set("blkDatiDettaglio.blkDatiTitolarita.blkElencoDatiTitolarita.diritto", diritto);
        htmpl.set("blkDatiDettaglio.blkDatiTitolarita.blkElencoDatiTitolarita.idTitoloPossesso", 
          ""+vProprietaCertificataVO.get(i).getIdTitoloPossesso());
        htmpl.set("blkDatiDettaglio.blkDatiTitolarita.blkElencoDatiTitolarita.percentualePossesso", 
          Formatter.formatDouble2(vProprietaCertificataVO.get(i).getPercentualePossesso()));
        htmpl.set("blkDatiDettaglio.blkDatiTitolarita.blkElencoDatiTitolarita.dataAggiornamento", 
          DateUtils.formatDate(vProprietaCertificataVO.get(i).getDataAggiornamento()));
        htmpl.set("blkDatiDettaglio.blkDatiTitolarita.blkElencoDatiTitolarita.descUtenteAgg", 
          vProprietaCertificataVO.get(i).getDescUtenteAggiornamento());
      }
      
      if(Validator.isNotEmpty(descFonte))
      {
        htmpl.set("blkDatiDettaglio.fonteTitolarita", " - Fonte "+descFonte); 
      }
      
      if(Validator.isNotEmpty(dataMaxTitolarità))
      {
        htmpl.set("blkDatiDettaglio.dataTitolarita", " del "+DateUtils.formatDate(dataMaxTitolarità)); 
      }
      
       
    }
    else
    {
      htmpl.newBlock("blkDatiDettaglio.blkNoDatiTitolarita");
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
      
 		if(storicoParticellaVO.getCausaleModParticella() != null) 
    {
 			htmpl.set("blkDatiDettaglio.descCausaleModParticella", storicoParticellaVO.getCausaleModParticella().getDescription());
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getMotivoModifica())) 
    {
 			htmpl.set("blkDatiDettaglio.motivoModifica", " - "+storicoParticellaVO.getMotivoModifica());
 		}
 		if(storicoParticellaVO.getDocumentoVO() != null) 
    {
 			htmpl.set("blkDatiDettaglio.descTipoDocumento", storicoParticellaVO.getDocumentoVO().getTipoDocumentoVO().getDescrizione());
 			if(Validator.isNotEmpty(storicoParticellaVO.getDocumentoVO().getNumeroProtocollo())) 
      {
 				htmpl.set("blkDatiDettaglio.numeroProtocolloDocumento", " Prot. "+StringUtils.parseNumeroProtocolloField(storicoParticellaVO.getDocumentoVO().getNumeroProtocollo()));
 				htmpl.set("blkDatiDettaglio.dataProtocolloDocumento", " del "+DateUtils.formatDate(storicoParticellaVO.getDocumentoVO().getDataProtocollo()));
 			}
 		}
 	}

%>
<%= htmpl.text()%>
