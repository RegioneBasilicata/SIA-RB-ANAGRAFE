<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniParticellareDettaglioConduzione.htm");
 	Htmpl htmpl = new Htmpl(layout);
 	
 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
	
 	String urlRSDI = (String) SolmrConstants.get("RSDI_URL");

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)request.getAttribute("storicoParticellaVO");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
 	Long idPianoRiferimento = (Long)request.getAttribute("idPianoRiferimento");
 	Vector elencoPianteConsociate = (Vector)request.getAttribute("elencoPianteConsociate");
 	UteVO uteVO = (UteVO)request.getAttribute("uteVO");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO"); 
  
  
  
  String regimePartDettCondTerreni = request.getParameter("regimePartDettCondTerreni");
  
  if(Validator.isEmpty(regimePartDettCondTerreni))
  {
    htmpl.set("primoIngressso", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_0")))
  {
    htmpl.set("usoSecondario", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_1")))
  {
    htmpl.set("impianto", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_2")))
  {
    htmpl.set("pianteCons", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_3")))
  {
    htmpl.set("elemCaratPaes", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_4")))
  {
    htmpl.set("mantenimento", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_5")))
  {
    htmpl.set("faseAllevamento", "true");
  }
  
  
   
 	
 	// Se si è verificato qualche errore visualizzo il messaggio all'utente
 	if(Validator.isNotEmpty(messaggioErrore)) {
 		htmpl.newBlock("blkErrore");
 		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti estraggo i dati
 	else 
  {
 		htmpl.set("idParticella", storicoParticellaVO.getIdParticella().toString());
 		htmpl.newBlock("blkDati");
 		Object[] elencoUtilizzi = null;
 		// Piano di riferimento anno corrente
 		if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
    {
 			// Dati di conduzione
 			ConduzioneParticellaVO conduzioneParticellaVO = (ConduzioneParticellaVO)storicoParticellaVO.getElencoConduzioni()[0];
 			htmpl.set("idConduzione", conduzioneParticellaVO.getIdConduzioneParticella().toString());
 			htmpl.set("idAzienda", anagAziendaVO.getIdAzienda().toString());
 			htmpl.set("urlRSDI", urlRSDI); 			
 			htmpl.set("blkDati.idConduzione", conduzioneParticellaVO.getIdConduzioneParticella().toString());
 			htmpl.set("blkDati.idAzienda", anagAziendaVO.getIdAzienda().toString());
 			htmpl.set("blkDati.urlRSDI", urlRSDI); 			
 			
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
	    htmpl.set("blkDati.controlliP", titleGis);
	    
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
	    htmpl.set("blkDati.immaginiControlliP", immaginiControlliP);
 			
 			
 			
 			htmpl.set("blkDati.descTitoloPossesso", conduzioneParticellaVO.getTitoloPossesso().getDescription());
 			BigDecimal percentualePossessoTmp = conduzioneParticellaVO.getPercentualePossesso();
      if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
      {
        percentualePossessoTmp = new BigDecimal(1);
      }
      htmpl.set("blkDati.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
 			//htmpl.set("blkDati.superficieCondotta", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
 			htmpl.set("blkDati.dataInizioConduzione", DateUtils.formatDate(conduzioneParticellaVO.getDataInizioConduzione()));
 			if(Validator.isNotEmpty(conduzioneParticellaVO.getDataFineConduzione())) {
 				htmpl.set("blkDati.dataFineConduzione", DateUtils.formatDate(conduzioneParticellaVO.getDataFineConduzione()));
 			}
 			
 			if(conduzioneParticellaVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
 			{
 			  htmpl.set("blkDati.superficieAgronomica", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieCondotta()));
 			}
 			else if(Validator.isNotEmpty(conduzioneParticellaVO.getSuperficieAgronomica())) 
 			{
 				htmpl.set("blkDati.superficieAgronomica", StringUtils.parseSuperficieField(conduzioneParticellaVO.getSuperficieAgronomica()));
 			}
 			if(uteVO != null) {
 				htmpl.set("blkDati.descComuneUte", uteVO.getComuneUte().getDescom());
 				if(uteVO.getComuneUte().getProvinciaVO() != null && Validator.isNotEmpty(uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia())) {
 					htmpl.set("blkDati.siglaProvUte", "("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")");
 				}
 				if(Validator.isNotEmpty(uteVO.getIndirizzo())) {
 					htmpl.set("blkDati.indirizzoUte", "- "+uteVO.getIndirizzo());
 				}
 			}
 			if(Validator.isNotEmpty(conduzioneParticellaVO.getNote())) {
 				htmpl.set("blkDati.note", conduzioneParticellaVO.getNote());
 			}
      
      //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
      //al ruolo!
      String dateUlt = "";
      if(conduzioneParticellaVO.getDataAggiornamento() !=null)
      {
        dateUlt = DateUtils.formatDate(conduzioneParticellaVO.getDataAggiornamento());
      }
      String utenteAgg = "";
      String enteAgg = "";
      if(conduzioneParticellaVO.getUtenteAggiornamento() !=null)
      {
        utenteAgg = conduzioneParticellaVO.getUtenteAggiornamento().getDenominazione();
        enteAgg = conduzioneParticellaVO.getUtenteAggiornamento().getDescrizioneEnteAppartenenza();
      }
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkDati.ultimaModificaVw", dateUlt, utenteAgg, enteAgg, null);
  		// Se ho selezionato un nuovo valore dalla combo...
 			if(idPianoRiferimento != null) 
      {
 				// Se si riferisce al piano di riferimento per anno corrente e/o comprensivo
 				// di conduzioni storicizzate
 				if(idPianoRiferimento.intValue() <= 0) {
 					elencoUtilizzi = (UtilizzoParticellaVO[])request.getAttribute("elencoUtilizzi");
 				}
 				// Altrimenti...
 				else {
 					elencoUtilizzi = (UtilizzoDichiaratoVO[])request.getAttribute("elencoUtilizzi"); 					
 				}
 			}
 			// Altrimenti recupero quello relativo al dettaglio in funzione della ricerca
 			// effettuata
 			else {
 				elencoUtilizzi = conduzioneParticellaVO.getElencoUtilizzi();
 			}
 		}
 		// Piano di riferimento ad una dichiarazione di consistenza
 		else 
    {
			// Dati di conduzione
 			ConduzioneDichiarataVO conduzioneDichiarataVO = (ConduzioneDichiarataVO)storicoParticellaVO.getElencoConduzioniDichiarate()[0];
 			htmpl.set("idConduzione", conduzioneDichiarataVO.getIdConduzioneDichiarata().toString());
 			
 			
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
      htmpl.set("blkDati.controlliP", titleGis);
      
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
      htmpl.set("blkDati.immaginiControlliP", immaginiControlliP);
 			
 			
 			
 			
 			
 			
 			htmpl.set("blkDati.idConduzione", conduzioneDichiarataVO.getIdConduzioneDichiarata().toString());
 			htmpl.set("blkDati.descTitoloPossesso", conduzioneDichiarataVO.getTitoloPossesso().getDescription());
 			htmpl.set("blkDati.percentualePossesso", ""+conduzioneDichiarataVO.getPercentualePossesso());
      //htmpl.set("blkDati.superficieCondotta", StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieCondotta()));
 			htmpl.set("blkDati.dataInizioConduzione", DateUtils.formatDate(conduzioneDichiarataVO.getDataInizioConduzione()));
 			if(Validator.isNotEmpty(conduzioneDichiarataVO.getDataFineConduzione())) {
 				htmpl.set("blkDati.dataFineConduzione", DateUtils.formatDate(conduzioneDichiarataVO.getDataFineConduzione()));
 			}
 			if(conduzioneDichiarataVO.getIdTitoloPossesso().compareTo(SolmrConstants.ID_TITOLO_POSSESSO_ASSERVIMENTO) == 0)
      {
        htmpl.set("blkDati.superficieAgronomica", StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieCondotta()));
      }
 			else if(Validator.isNotEmpty(conduzioneDichiarataVO.getSuperficieAgronomica())) 
 			{
 				htmpl.set("blkDati.superficieAgronomica", StringUtils.parseSuperficieField(conduzioneDichiarataVO.getSuperficieAgronomica()));
 			}
 			if(uteVO != null) {
 				htmpl.set("blkDati.descComuneUte", uteVO.getComuneUte().getDescom());
 				if(uteVO.getComuneUte().getProvinciaVO() != null && Validator.isNotEmpty(uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia())) {
 					htmpl.set("blkDati.siglaProvUte", "("+uteVO.getComuneUte().getProvinciaVO().getSiglaProvincia()+")");
 				}
 				if(Validator.isNotEmpty(uteVO.getIndirizzo())) {
 					htmpl.set("blkDati.indirizzoUte", "- "+uteVO.getIndirizzo());
 				}
 			}
 			if(Validator.isNotEmpty(conduzioneDichiarataVO.getNote())) {
 				htmpl.set("blkDati.note", conduzioneDichiarataVO.getNote());
 			}
      
      //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
      //al ruolo!
      String dateUlt = "";
      if(conduzioneDichiarataVO.getDataAggiornamento() !=null)
      {
        dateUlt = DateUtils.formatDate(conduzioneDichiarataVO.getDataAggiornamento());
      }
      String utenteAgg = "";
      String enteAgg = "";
      if(conduzioneDichiarataVO.getUtenteAggiornamento() !=null)
      {
        utenteAgg = conduzioneDichiarataVO.getUtenteAggiornamento().getDenominazione();
        enteAgg = conduzioneDichiarataVO.getUtenteAggiornamento().getDescrizioneEnteAppartenenza();
      }
      ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
        "blkDati.ultimaModificaVw", dateUlt, utenteAgg, enteAgg, null);
      
 			// Se ho selezionato un nuovo valore dalla combo...
 			if(idPianoRiferimento != null) {
 				// Se si riferisce al piano di riferimento per anno corrente e/o comprensivo
 				// di conduzioni storicizzate
 				if(idPianoRiferimento.intValue() <= 0) {
 					elencoUtilizzi = (UtilizzoParticellaVO[])request.getAttribute("elencoUtilizzi");
 				}
 				// Altrimenti...
 				else {
 					elencoUtilizzi = (UtilizzoDichiaratoVO[])request.getAttribute("elencoUtilizzi"); 					
 				}
 			}
 			// Altrimenti recupero quello relativo al dettaglio in funzione della ricerca
 			// effettuata
 			else {
 				elencoUtilizzi = conduzioneDichiarataVO.getElencoUtilizzi();
 			}
 		}
 		// Dati di destata
 		htmpl.set("blkDati.descComune", storicoParticellaVO.getComuneParticellaVO().getDescom());
 		if(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO() != null && Validator.isNotEmpty(storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia())) {
 			htmpl.set("blkDati.siglaProvincia", "("+storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia()+")"); 			
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
 			htmpl.set("blkDati.sezione", storicoParticellaVO.getSezione());
 		}
 		htmpl.set("blkDati.foglio", storicoParticellaVO.getFoglio());
 		if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
 			htmpl.set("blkDati.particella", storicoParticellaVO.getParticella());
 		}
 		if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
 			htmpl.set("blkDati.subalterno", storicoParticellaVO.getSubalterno());
 		}
 		htmpl.set("blkDati.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
		htmpl.set("blkDati.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
		// Combo Piano di riferimento
    String idDichiarazioneConsistenza = null;
    if(idPianoRiferimento != null) 
    {
      idDichiarazioneConsistenza = idPianoRiferimento.toString(); 
    }
    // Altrimenti visualizzo il dato relativo alla ricerca effettuata
    else if(filtriParticellareRicercaVO.getIdPianoRiferimento() != null) 
    {
      idDichiarazioneConsistenza = filtriParticellareRicercaVO.getIdPianoRiferimento().toString();
    }
    //Mi paro il culo!!!!
    if(Validator.isEmpty(idDichiarazioneConsistenza))
    {
      idDichiarazioneConsistenza = "-1";
    }
    String bloccoDichiarazioneConsistenza =  "blkDati.blkPianoRiferimento";
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
      anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_TERRENI,
      ruoloUtenza);
		
		// Dati relativi agli utilizzi
		if(elencoUtilizzi != null && elencoUtilizzi.length > 0) 
		{
			boolean visible = false;
 			// Il valore della combo selezionato o la ricerca da cui arrivo sono
 			// per anno corrente e/o comprensivo di conduzioni storicizzate
 			if((idPianoRiferimento != null && idPianoRiferimento.intValue() <= 0) 
 			  || idPianoRiferimento == null 
 			     && filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
 			{
 			  htmpl.newBlock("blkDati.blkUtilizzo");
 				// Dati relativi agli usi del suolo
 				for(int i = 0; i < elencoUtilizzi.length; i++) 
 				{
	 				UtilizzoParticellaVO utilizzoParticellaVO = (UtilizzoParticellaVO)elencoUtilizzi[i];
	 				if(utilizzoParticellaVO != null && utilizzoParticellaVO.getIdUtilizzoParticella().intValue() != -1) 
	 				{
		 				visible = true;
	 					htmpl.newBlock("blkDati.blkUtilizzo.blkElencoUtilizzi");
		 				htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descUsoPrimario", "["+utilizzoParticellaVO.getTipoUtilizzoVO().getCodice()+"] "+utilizzoParticellaVO.getTipoUtilizzoVO().getDescrizione());
                       
            if(utilizzoParticellaVO.getTipoDestinazione() != null 
              && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDestinazione().getDescrizioneDestinazione())) 
            {
              String usoPrimario = "["+utilizzoParticellaVO.getTipoDestinazione().getCodiceDestinazione()+"] "+utilizzoParticellaVO.getTipoDestinazione().getDescrizioneDestinazione();
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoDestinazionePrimario", usoPrimario);
            }
            if(utilizzoParticellaVO.getTipoDettaglioUso() != null 
              && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDettaglioUso().getDescrizione())) 
            {
              String usoPrimario = "["+utilizzoParticellaVO.getTipoDettaglioUso().getCodiceDettaglioUso()+"] "+utilizzoParticellaVO.getTipoDettaglioUso().getDescrizione();
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoDettUsoPrimario", usoPrimario);
            }
            if(utilizzoParticellaVO.getTipoQualitaUso() != null 
              && Validator.isNotEmpty(utilizzoParticellaVO.getTipoQualitaUso().getDescrizioneQualitaUso())) 
            {
              String usoPrimario = "["+utilizzoParticellaVO.getTipoQualitaUso().getCodiceQualitaUso()+"] "+utilizzoParticellaVO.getTipoQualitaUso().getDescrizioneQualitaUso();
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoQualitaUsoPrimario", usoPrimario);
            }
            if(utilizzoParticellaVO.getTipoVarietaVO() != null 
              && Validator.isNotEmpty(utilizzoParticellaVO.getTipoVarietaVO().getDescrizione())) 
            {
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descVarietaPrimario", "["+utilizzoParticellaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+utilizzoParticellaVO.getTipoVarietaVO().getDescrizione());
            }                           
            
            String descSemina = "";          
            if(utilizzoParticellaVO.getTipoPeriodoSemina() != null 
              && Validator.isNotEmpty(utilizzoParticellaVO.getTipoPeriodoSemina().getDescrizione())) 
            {
              descSemina += utilizzoParticellaVO.getTipoPeriodoSemina().getDescrizione();
            }
            if(utilizzoParticellaVO.getTipoSemina() != null 
              && Validator.isNotEmpty(utilizzoParticellaVO.getTipoSemina().getDescrizioneSemina())) 
            {
              descSemina += " "+utilizzoParticellaVO.getTipoSemina().getDescrizioneSemina();
            }
            if(utilizzoParticellaVO.getDataInizioDestinazione() != null)
            {
              descSemina += " "+DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataInizioDestinazione());
            }
            if(utilizzoParticellaVO.getDataFineDestinazione() != null)
            {
              descSemina += "-"+DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataFineDestinazione());
            }
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoSeminaPrimario", descSemina);
            
		 				htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.supUtilizzata", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSuperficieUtilizzata()));
		 				
		 				if(utilizzoParticellaVO.getTipoUtilizzoSecondarioVO() != null) 
		 				{
		 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descUsoSecondario", "["+utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getCodice()+"] "+utilizzoParticellaVO.getTipoUtilizzoSecondarioVO().getDescrizione());
                           
              if(utilizzoParticellaVO.getTipoDestinazioneSecondario() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione())) 
              {
                String usoSecondario = "["+utilizzoParticellaVO.getTipoDestinazioneSecondario().getCodiceDestinazione()+"] "+utilizzoParticellaVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione();
                htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoDestinazioneSecondario", usoSecondario);
              }
              if(utilizzoParticellaVO.getTipoDettaglioUsoSecondario() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getDescrizione())) 
              {
                String usoSecondario =  "["+utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getCodiceDettaglioUso()+"] "+utilizzoParticellaVO.getTipoDettaglioUsoSecondario().getDescrizione();
                htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoDettUsoSecondario", usoSecondario);
              }
              if(utilizzoParticellaVO.getTipoQualitaUsoSecondario() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso())) 
              {
                 String usoSecondario =  "["+utilizzoParticellaVO.getTipoQualitaUsoSecondario().getCodiceQualitaUso()+"] "+utilizzoParticellaVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso();
                 htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoQualitaUsoSecondario", usoSecondario);
              }
              if(utilizzoParticellaVO.getTipoVarietaSecondariaVO() != null) 
              {
                htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descVarietaSecondaria", "["+utilizzoParticellaVO.getTipoVarietaSecondariaVO().getCodiceVarieta()+"] "+utilizzoParticellaVO.getTipoVarietaSecondariaVO().getDescrizione());
              }
             
              String descSeminaSecondario = "";
              if(utilizzoParticellaVO.getTipoPeriodoSeminaSecondario() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoPeriodoSeminaSecondario().getDescrizione())) 
              {
                descSeminaSecondario += utilizzoParticellaVO.getTipoPeriodoSeminaSecondario().getDescrizione();
              }
              if(utilizzoParticellaVO.getTipoSeminaSecondario() != null 
                && Validator.isNotEmpty(utilizzoParticellaVO.getTipoSeminaSecondario().getDescrizioneSemina())) 
              {
                descSeminaSecondario += " "+utilizzoParticellaVO.getTipoSeminaSecondario().getDescrizioneSemina();
              }
              if(utilizzoParticellaVO.getDataInizioDestinazioneSec() != null)
	            {
	              descSeminaSecondario += " "+DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataInizioDestinazioneSec());
	            }
	            if(utilizzoParticellaVO.getDataFineDestinazioneSec() != null)
	            {
	              descSeminaSecondario += "-"+DateUtils.formatDateNotNull(utilizzoParticellaVO.getDataFineDestinazioneSec());
	            }
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoSeminaSecondario", descSeminaSecondario);
              
			 				if(Validator.isNotEmpty(utilizzoParticellaVO.getSupUtilizzataSecondaria())) {
				 				htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.supUtilizzataSecondaria", StringUtils.parseSuperficieField(utilizzoParticellaVO.getSupUtilizzataSecondaria()));
			 				}
		 				}
		 				
		 				if(Validator.isNotEmpty(utilizzoParticellaVO.getTipoPraticaMantenimento())
		 				  && Validator.isNotEmpty(utilizzoParticellaVO.getTipoPraticaMantenimento().getDescrizionePraticaMantenim())) 
		 				{
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoPraticaMantenimento", 
                utilizzoParticellaVO.getTipoPraticaMantenimento().getDescrizionePraticaMantenim());
            }
            
            if(Validator.isNotEmpty(utilizzoParticellaVO.getTipoFaseAllevamento())
              && Validator.isNotEmpty(utilizzoParticellaVO.getTipoFaseAllevamento().getDescrizioneFaseAllevamento())) 
            {
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoFaseAllevamento", 
                utilizzoParticellaVO.getTipoFaseAllevamento().getDescrizioneFaseAllevamento());
            }
		 				
		 				
		 				if(Validator.isNotEmpty(utilizzoParticellaVO.getAnnoImpianto())) {
		 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.annoImpianto", utilizzoParticellaVO.getAnnoImpianto());
		 				}
		 				if(utilizzoParticellaVO.getTipoImpiantoVO() != null) {
		 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descImpianto", utilizzoParticellaVO.getTipoImpiantoVO().getDescrizione());
		 				}
		 				if(Validator.isNotEmpty(utilizzoParticellaVO.getSestoSuFile())) {
		 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.sestoSuFile", utilizzoParticellaVO.getSestoSuFile());
		 				}
		 				if(Validator.isNotEmpty(utilizzoParticellaVO.getSestoTraFile())) {
		 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.sestoTraFile", utilizzoParticellaVO.getSestoTraFile());
		 				}
		 				if(Validator.isNotEmpty(utilizzoParticellaVO.getNumeroPianteCeppi())) {
		 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.numPianteCeppi", utilizzoParticellaVO.getNumeroPianteCeppi());
		 				}
		 				// Sezione relativa alle piante e agli utilizzi consociati
		 				UtilizzoConsociatoVO[] elencoUtilizziConsociati = utilizzoParticellaVO.getElencoUtilizziConsociati();
		 				if(elencoUtilizziConsociati != null && elencoUtilizziConsociati.length > 0) 
		 				{
		 					for(int a = 0; a < elencoUtilizziConsociati.length; a++) {
		 						UtilizzoConsociatoVO utilizzoConsociatoVO = (UtilizzoConsociatoVO)elencoUtilizziConsociati[a];
		 						htmpl.newBlock("blkDati.blkUtilizzo.blkElencoUtilizzi.blkElencoPianteConsociate");
		 						if(Validator.isNotEmpty(utilizzoConsociatoVO.getNumeroPiante())) {
		 							htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", utilizzoConsociatoVO.getNumeroPiante());
		 						}
		 						else {
		 							htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "0");
		 						}
		 					}
		 				}
		 				else {
		 					for(int a = 0; a < elencoPianteConsociate.size(); a++) {
		 						htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "0");
		 					}
		 				}
		 				
		 				
		 				if(Validator.isNotEmpty(utilizzoParticellaVO.getIdTipoEfa()))
            {
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoEfa", utilizzoParticellaVO.getDescTipoEfaEfa());
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.valoreOriginale", Formatter.formatDouble2(utilizzoParticellaVO.getValoreOriginale()));
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descUnitaMisura", utilizzoParticellaVO.getDescUnitaMisuraEfa());
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.valoreDopoConversione", Formatter.formatDouble4(utilizzoParticellaVO.getValoreDopoConversione()));
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.valoreDopoPonderazione", Formatter.formatDouble4(utilizzoParticellaVO.getValoreDopoPonderazione()));
            }
		 				
	 				}
 				}
 			}
			// Il valore della combo selezionato o la ricerca da cui arrivo sono
 			// ad una dichiarazione di consistenza
 			else 
 			{
 				visible = true;
 				for(int i = 0; i < elencoUtilizzi.length; i++) 
 				{
	 				UtilizzoDichiaratoVO utilizzoDichiaratoVO = (UtilizzoDichiaratoVO)elencoUtilizzi[i];
	 				htmpl.newBlock("blkDati.blkUtilizzo.blkElencoUtilizzi");
	 				htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descUsoPrimario", "["+utilizzoDichiaratoVO.getTipoUtilizzoVO().getCodice()+"] "+utilizzoDichiaratoVO.getTipoUtilizzoVO().getDescrizione());
          
          
          if(utilizzoDichiaratoVO.getTipoDestinazione() != null 
            && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoDestinazione().getDescrizioneDestinazione())) 
          {
            String usoPrimario = "["+utilizzoDichiaratoVO.getTipoDestinazione().getCodiceDestinazione()+"] "+utilizzoDichiaratoVO.getTipoDestinazione().getDescrizioneDestinazione();
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoDestinazionePrimario", usoPrimario);
          }
          if(utilizzoDichiaratoVO.getTipoDettaglioUso() != null 
            && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoDettaglioUso().getDescrizione())) 
          {
            String usoPrimario = " ["+utilizzoDichiaratoVO.getTipoDettaglioUso().getCodiceDettaglioUso()+"] "+utilizzoDichiaratoVO.getTipoDettaglioUso().getDescrizione();
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoDettUsoPrimario", usoPrimario);
          }
          if(utilizzoDichiaratoVO.getTipoQualitaUso() != null 
            && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoQualitaUso().getDescrizioneQualitaUso())) 
          {
            String usoPrimario = " ["+utilizzoDichiaratoVO.getTipoQualitaUso().getCodiceQualitaUso()+"] "+utilizzoDichiaratoVO.getTipoQualitaUso().getDescrizioneQualitaUso();
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoQualitaUsoPrimario", usoPrimario);
          }
          if(utilizzoDichiaratoVO.getTipoVarietaVO() != null 
            && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione())) 
          {
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descVarietaPrimario", "["+utilizzoDichiaratoVO.getTipoVarietaVO().getCodiceVarieta()+"] "+utilizzoDichiaratoVO.getTipoVarietaVO().getDescrizione());
          }                        
          
          
          String descSemina = "";          
          if(utilizzoDichiaratoVO.getTipoPeriodoSemina() != null 
            && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoPeriodoSemina().getDescrizione())) 
          {
            descSemina += utilizzoDichiaratoVO.getTipoPeriodoSemina().getDescrizione();
          }
          if(utilizzoDichiaratoVO.getTipoSemina() != null 
            && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoSemina().getDescrizioneSemina())) 
          {
            descSemina += " "+utilizzoDichiaratoVO.getTipoSemina().getDescrizioneSemina();
          }
          if(utilizzoDichiaratoVO.getDataInizioDestinazione() != null)
          {
            descSemina += " "+DateUtils.formatDateNotNull(utilizzoDichiaratoVO.getDataInizioDestinazione());
          }
          if(utilizzoDichiaratoVO.getDataFineDestinazione() != null)
          {
            descSemina += "-"+DateUtils.formatDateNotNull(utilizzoDichiaratoVO.getDataFineDestinazione());
          }
          htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoSeminaPrimario", descSemina);
            
	 				htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.supUtilizzata", StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSuperficieUtilizzata()));
	 				if(utilizzoDichiaratoVO.getTipoUtilizzoSecondarioVO() != null) 
	 				{
	 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descUsoSecondario", "["+utilizzoDichiaratoVO.getTipoUtilizzoSecondarioVO().getCodice()+"] "+utilizzoDichiaratoVO.getTipoUtilizzoSecondarioVO().getDescrizione());   
            
            if(utilizzoDichiaratoVO.getTipoDestinazioneSecondario() != null 
              && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione())) 
            {
              String usoSecondario = "["+utilizzoDichiaratoVO.getTipoDestinazioneSecondario().getCodiceDestinazione()+"] "+utilizzoDichiaratoVO.getTipoDestinazioneSecondario().getDescrizioneDestinazione();
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoDestinazioneSecondario", usoSecondario);
            }
            if(utilizzoDichiaratoVO.getTipoDettaglioUsoSecondario() != null 
              && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoDettaglioUsoSecondario().getDescrizione())) 
            {
              String usoSecondario =  "["+utilizzoDichiaratoVO.getTipoDettaglioUsoSecondario().getCodiceDettaglioUso()+"] "+utilizzoDichiaratoVO.getTipoDettaglioUsoSecondario().getDescrizione();
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoDettUsoSecondario", usoSecondario);
            }
            if(utilizzoDichiaratoVO.getTipoQualitaUsoSecondario() != null 
              && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso())) 
            {
              String usoSecondario =  "["+utilizzoDichiaratoVO.getTipoQualitaUsoSecondario().getCodiceQualitaUso()+"] "+utilizzoDichiaratoVO.getTipoQualitaUsoSecondario().getDescrizioneQualitaUso();
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoQualitaUsoSecondario", usoSecondario);
            }
            if(utilizzoDichiaratoVO.getTipoVarietaSecondariaVO() != null) 
            {
              htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descVarietaSecondaria", "["+utilizzoDichiaratoVO.getTipoVarietaSecondariaVO().getCodiceVarieta()+"] "+utilizzoDichiaratoVO.getTipoVarietaSecondariaVO().getDescrizione());
            }
            
            
            String descSeminaSecondario = "";
            if(utilizzoDichiaratoVO.getTipoPeriodoSeminaSecondario() != null 
              && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoPeriodoSeminaSecondario().getDescrizione())) 
            {
              descSeminaSecondario += utilizzoDichiaratoVO.getTipoPeriodoSeminaSecondario().getDescrizione();
            }
            if(utilizzoDichiaratoVO.getTipoSeminaSecondario() != null 
              && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoSeminaSecondario().getDescrizioneSemina())) 
            {
              descSeminaSecondario += " "+utilizzoDichiaratoVO.getTipoSeminaSecondario().getDescrizioneSemina();
            }
            if(utilizzoDichiaratoVO.getDataInizioDestinazioneSec() != null)
            {
              descSeminaSecondario += " "+DateUtils.formatDateNotNull(utilizzoDichiaratoVO.getDataInizioDestinazioneSec());
            }
            if(utilizzoDichiaratoVO.getDataFineDestinazioneSec() != null)
            {
              descSeminaSecondario += "-"+DateUtils.formatDateNotNull(utilizzoDichiaratoVO.getDataFineDestinazioneSec());
            }
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoSeminaSecondario", descSeminaSecondario);
              
		 				if(Validator.isNotEmpty(utilizzoDichiaratoVO.getSupUtilizzataSecondaria())) {
			 				htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.supUtilizzataSecondaria", StringUtils.parseSuperficieField(utilizzoDichiaratoVO.getSupUtilizzataSecondaria()));
		 				}
	 				}
	 				
	 				if(Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoPraticaMantenimento())
            && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoPraticaMantenimento().getDescrizionePraticaMantenim())) 
          {
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoPraticaMantenimento", 
              utilizzoDichiaratoVO.getTipoPraticaMantenimento().getDescrizionePraticaMantenim());
          }
          
          if(Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoFaseAllevamento())
            && Validator.isNotEmpty(utilizzoDichiaratoVO.getTipoFaseAllevamento().getDescrizioneFaseAllevamento())) 
          {
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoFaseAllevamento", 
              utilizzoDichiaratoVO.getTipoFaseAllevamento().getDescrizioneFaseAllevamento());
          }
	 				
	 				
	 				if(Validator.isNotEmpty(utilizzoDichiaratoVO.getAnnoImpianto())) {
	 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.annoImpianto", utilizzoDichiaratoVO.getAnnoImpianto());
	 				}
	 				if(utilizzoDichiaratoVO.getTipoImpiantoVO() != null) {
	 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descImpianto", utilizzoDichiaratoVO.getTipoImpiantoVO().getDescrizione());
	 				}
	 				if(Validator.isNotEmpty(utilizzoDichiaratoVO.getSestoSuFile())) {
	 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.sestoSuFile", utilizzoDichiaratoVO.getSestoSuFile());
	 				}
	 				if(Validator.isNotEmpty(utilizzoDichiaratoVO.getSestoTraFile())) {
	 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.sestoTraFile", utilizzoDichiaratoVO.getSestoTraFile());
	 				}
	 				if(Validator.isNotEmpty(utilizzoDichiaratoVO.getNumeroPianteCeppi())) {
	 					htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.numPianteCeppi", utilizzoDichiaratoVO.getNumeroPianteCeppi());
	 				}
	 				// Sezione relativa alle piante e agli utilizzi consociati
	 				UtilizzoConsociatoDichiaratoVO[] elencoUtilizziConsociatiDich = utilizzoDichiaratoVO.getElencoUtilizziConsociatiDich();
	 				if(elencoUtilizziConsociatiDich != null && elencoUtilizziConsociatiDich.length > 0) 
	 				{
	 					for(int a = 0; a < elencoUtilizziConsociatiDich.length; a++) 
	 					{
	 						UtilizzoConsociatoDichiaratoVO utilizzoConsociatoDichiaratoVO = (UtilizzoConsociatoDichiaratoVO)elencoUtilizziConsociatiDich[a];
	 						htmpl.newBlock("blkDati.blkUtilizzo.blkElencoUtilizzi.blkElencoPianteConsociate");
	 						if(Validator.isNotEmpty(utilizzoConsociatoDichiaratoVO.getNumeroPiante())) {
	 							htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", utilizzoConsociatoDichiaratoVO.getNumeroPiante());
	 						}
	 						else {
	 							htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.blkElencoPianteConsociate.numeroPianteConsociate", "0");
	 						}
	 					}
	 				}
	 				
	 				if(Validator.isNotEmpty(utilizzoDichiaratoVO.getIdTipoEfa()))
          {
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descTipoEfa", utilizzoDichiaratoVO.getDescTipoEfaEfa());
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.valoreOriginale", Formatter.formatDouble2(utilizzoDichiaratoVO.getValoreOriginale()));
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.descUnitaMisura", utilizzoDichiaratoVO.getDescUnitaMisuraEfa());
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.valoreDopoConversione", Formatter.formatDouble4(utilizzoDichiaratoVO.getValoreDopoConversione()));
            htmpl.set("blkDati.blkUtilizzo.blkElencoUtilizzi.valoreDopoPonderazione", Formatter.formatDouble4(utilizzoDichiaratoVO.getValoreDopoPonderazione()));
          }
	 				
	 				
 				}
 			}
			// Labels relative ai dati delle piante consociate
			if(elencoPianteConsociate != null && elencoPianteConsociate.size() > 0 
			  && visible) 
			{
			  htmpl.newBlock("blkDati.blkUtilizzo.blkTitoloPianteConsociate");
				htmpl.set("blkDati.blkUtilizzo.blkTitoloPianteConsociate.colspan", String.valueOf(elencoPianteConsociate.size()));
				for(int i = 0; i < elencoPianteConsociate.size(); i++) {
					TipoPiantaConsociataVO tipoPiantaConsociataVO = (TipoPiantaConsociataVO)elencoPianteConsociate.elementAt(i);
					htmpl.newBlock("blkDati.blkUtilizzo.blkEtichettaPianteConsociate");
					htmpl.set("blkDati.blkUtilizzo.blkEtichettaPianteConsociate.descPianteConsociate", tipoPiantaConsociataVO.getDescrizione());
				}
			}
 		}
		// Se invece non reperisco nessun utilizzo
		else {
			htmpl.newBlock("blkDati.blkErrori");
			htmpl.set("blkDati.blkErrori.messaggioErrore", AnagErrors.ERRORE_KO_UTILIZZI);
		}
 	}

%>
<%= htmpl.text()%>
