<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.math.*"%>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="java.text.*"%>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/terreniUnitaArboreeElenco.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	StoricoParticellaVO[] elencoParticelleArboree = (StoricoParticellaVO[])request.getAttribute("elencoParticelleArboree");
 	FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
 	TipoUtilizzoVO[] elencoTipiUsoSuolo = (TipoUtilizzoVO[])request.getAttribute("elencoTipiUsoSuolo");
 	TipoVarietaVO[] elencoVarieta = (TipoVarietaVO[])request.getAttribute("elencoVarieta");
 	Vector<ComuneVO> elencoComuni = (Vector<ComuneVO>)request.getAttribute("elencoComuni");
  Vector<ProvinciaVO> elencoProvincie = (Vector<ProvinciaVO>)request.getAttribute("elencoProvincie");
  TipoTipologiaVinoVO[] elencoTipiTipologiaVino = (TipoTipologiaVinoVO[])request.getAttribute("elencoTipiTipologiaVino");
  Vector<TipoGenereIscrizioneVO> vTipoGenereIscrizione = (Vector<TipoGenereIscrizioneVO>)request.getAttribute("vTipoGenereIscrizione");
  Vector<TipoCausaleModificaVO> vTipoCausaleModifica = (Vector<TipoCausaleModificaVO>)request.getAttribute("vTipoCausaleModifica");
 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	String paginaCorrente = (String)request.getAttribute("paginaCorrente");
 	HashMap<String,Vector<Long>> numUnitaArboreeSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numUnitaArboreeSelezionate");
 	Vector<Long> elencoIdStoricoSelezionati = null;
 	if(numUnitaArboreeSelezionate != null && numUnitaArboreeSelezionate.size() > 0) {
 		elencoIdStoricoSelezionati = (Vector<Long>)numUnitaArboreeSelezionate.get(paginaCorrente);
 	}
  Vector<TipoCausaleModificaVO> elencoCausMod = new Vector<TipoCausaleModificaVO>();
  TipoControlloVO[] elencoTipiControllo = (TipoControlloVO[])request.getAttribute("elencoTipiControllo");
  
  //CompensazioneAziendaVO compensazioneAziendaVO = (CompensazioneAziendaVO)request.getAttribute("compensazioneAziendaVO");
  ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
  
  
  String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String imok = "ok.gif";
 	
 	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	htmpl.set("headMenuScroll", headMenuScroll, null);
  htmpl.set("headMenuScrollIE6", headMenuScrollIE6, null);
	
	// Ripulisco sempre l'operazione selezionata in precedenza
	htmpl.set("operazione", "");
  
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String idDichiarazioneConsistenza = "";  
  if(filtriUnitaArboreaRicercaVO != null)
  {
    idDichiarazioneConsistenza = filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().toString();
  }
  else
  {
    idDichiarazioneConsistenza = "-1";
  }
	
	// Combo Piano di riferimento  
  String bloccoDichiarazioneConsistenza =  "blkPianoRiferimento";
  PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
    anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, SolmrConstants.PIANO_LAVORAZIONE_UV,
    ruoloUtenza);
	
	// DESTINAZIONE_PRODUTTIVA
	if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.length > 0)
  {
		for(int c = 0; c < elencoTipiUsoSuolo.length; c++) {
			TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo[c];
			htmpl.newBlock("blkTipiUsoSuolo");
			htmpl.set("blkTipiUsoSuolo.idTipoUtilizzoElenco", tipoUtilizzoVO.getIdUtilizzo().toString());
			htmpl.set("blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+tipoUtilizzoVO.getDescrizione());
			
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
  			if(filtriUnitaArboreaRicercaVO.getIdUtilizzo() != null && filtriUnitaArboreaRicercaVO.getIdUtilizzo().compareTo(tipoUtilizzoVO.getIdUtilizzo()) == 0) {
  				htmpl.set("blkTipiUsoSuolo.selected", "selected=\"selected\"");
  			}
      }
		}
	}
  
  // Combo Vitigno
  if(elencoVarieta != null && elencoVarieta.length > 0) 
  {
    for(int i=0; i<elencoVarieta.length; i++)
    {
      TipoVarietaVO tipoVarietaVO = elencoVarieta[i];
      htmpl.newBlock("blkVarietaCombo");
      htmpl.set("blkVarietaCombo.idVarieta", ""+tipoVarietaVO.getIdVarieta());
      htmpl.set("blkVarietaCombo.idUtilizzo", ""+tipoVarietaVO.getIdUtilizzo());
      htmpl.set("blkVarietaCombo.descVarieta", tipoVarietaVO.getDescrizione());
      htmpl.set("blkVarietaCombo.index", ""+i);
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdVarieta())) {
          if(filtriUnitaArboreaRicercaVO.getIdVarieta().compareTo(tipoVarietaVO.getIdVarieta()) == 0 ) 
          {
            htmpl.set("varietaSel",""+tipoVarietaVO.getIdVarieta());
          }
        }
      }
    }
  }
  
  
  // PROVINCIE
  if(Validator.isNotEmpty(elencoProvincie))
  {
    for(int c = 0; c < elencoProvincie.size(); c++) 
    {
      ProvinciaVO provinciaVO = elencoProvincie.get(c);
      htmpl.newBlock("blkProvinciaConduzioniParticelle");
      htmpl.set("blkProvinciaConduzioniParticelle.istatProvinciaConduzioniParticelle", ""+provinciaVO.getIstatProvincia());
      htmpl.set("blkProvinciaConduzioniParticelle.descrizione", provinciaVO.getDescrizione());
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
        if(filtriUnitaArboreaRicercaVO.getIstatProvincia() != null && filtriUnitaArboreaRicercaVO.getIstatProvincia().compareTo(provinciaVO.getIstatProvincia()) == 0) 
        {
          htmpl.set("blkProvinciaConduzioniParticelle.selected", "selected=\"selected\"");
        }
      }
    }
  }
	
	// Combo dei comuni su cui insistono le particelle
	if(elencoComuni != null && elencoComuni.size() > 0) 
  {
		Iterator<ComuneVO> iteraComuni = elencoComuni.iterator();
    int i=0;
		while(iteraComuni.hasNext()) 
    {
			ComuneVO comuneVO = (ComuneVO)iteraComuni.next();
      htmpl.newBlock("blkComuneCombo");
      htmpl.set("blkComuneCombo.istatComune", ""+comuneVO.getIstatComune());
      htmpl.set("blkComuneCombo.istatProvincia", ""+comuneVO.getIstatProvincia());
      htmpl.set("blkComuneCombo.descComune", comuneVO.getDescom()+" ("+comuneVO.getSiglaProv()+")");
      htmpl.set("blkComuneCombo.index", ""+i);
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
        if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIstatComune())) 
        {
          if(filtriUnitaArboreaRicercaVO.getIstatComune().compareTo(comuneVO.getIstatComune()) == 0 ) 
          {
            htmpl.set("comuneSel",""+comuneVO.getIstatComune());
          }
        }
      }
      i++;			
		}
	}
  
  // Tipologia Vino
  if(elencoTipiTipologiaVino != null && elencoTipiTipologiaVino.length > 0) 
  {
    for(int i = 0; i < elencoTipiTipologiaVino.length; i++) {
      TipoTipologiaVinoVO tipoTipologiaVinoVO = elencoTipiTipologiaVino[i];
      htmpl.newBlock("blkTipiTipologiaVino");
      htmpl.set("blkTipiTipologiaVino.idTipologiaVino", tipoTipologiaVinoVO.getIdTipologiaVino().toString());
      htmpl.set("blkTipiTipologiaVino.descrizione", tipoTipologiaVinoVO.getDescrizione().trim());
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
        if(filtriUnitaArboreaRicercaVO.getIdTipologiaVino() != null && filtriUnitaArboreaRicercaVO.getIdTipologiaVino().compareTo(tipoTipologiaVinoVO.getIdTipologiaVino()) == 0) {
          htmpl.set("blkTipiTipologiaVino.selected", "selected=\"selected\"", null);
        }
      }
      
    }
  }
  
  // Genere iscrizione
  if(vTipoGenereIscrizione != null) 
  {
    for(int i = 0; i < vTipoGenereIscrizione.size(); i++) 
    {
      TipoGenereIscrizioneVO tipoGenereIscrizioneVO = vTipoGenereIscrizione.get(i);
      htmpl.newBlock("blkTipoGenereIscrizione");
      htmpl.set("blkTipoGenereIscrizione.idGenereIscrizione", tipoGenereIscrizioneVO.getIdGenereIscrizione().toString());
      htmpl.set("blkTipoGenereIscrizione.descrizione", tipoGenereIscrizioneVO.getDescrizione().trim());
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
        if(filtriUnitaArboreaRicercaVO.getIdGenereIscrizione() != null && filtriUnitaArboreaRicercaVO.getIdGenereIscrizione().compareTo(tipoGenereIscrizioneVO.getIdGenereIscrizione()) == 0) {
          htmpl.set("blkTipoGenereIscrizione.selected", "selected=\"selected\"", null);
        }
      }
    
    }
  }
  
  // Combo relativa ai tipi tipologia notifica
  Vector<CodeDescription> vTipologiaNotifica = (Vector<CodeDescription>)request.getAttribute("vTipologiaNotifica");
  if(vTipologiaNotifica != null)
  {
    for(int i = 0; i < vTipologiaNotifica.size(); i++) 
    {
      CodeDescription code = vTipologiaNotifica.get(i);
      htmpl.newBlock("blkTipologiaNotifica");
      htmpl.set("blkTipologiaNotifica.idTipologiaNotifica", code.getCode().toString());
      htmpl.set("blkTipologiaNotifica.descTipologiaNotifica", code.getDescription());
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
	      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica())
	        && (filtriUnitaArboreaRicercaVO.getIdTipologiaNotifica().intValue() == code.getCode().intValue())) 
	      {
	        htmpl.set("blkTipologiaNotifica.selected", "selected=\"selected\"", null);
	      }
	    }
    }
  }
  
  // Combo Categoria
  Vector<TipoCategoriaNotificaVO> vCategoriaNotifica = (Vector<TipoCategoriaNotificaVO>)request.getAttribute("vCategoriaNotifica");
  if(vCategoriaNotifica != null) 
  {
    for(int i=0; i<vCategoriaNotifica.size(); i++)
    {
      TipoCategoriaNotificaVO tipoCategoriaNotificaVO = vCategoriaNotifica.get(i);
      htmpl.newBlock("blkCategoriaCombo");
      htmpl.set("blkCategoriaCombo.idCategoriaNotifica", ""+tipoCategoriaNotificaVO.getIdCategoriaNotifica());
      htmpl.set("blkCategoriaCombo.idTipologiaNotifica", ""+tipoCategoriaNotificaVO.getIdTipologiaNotifica());
      htmpl.set("blkCategoriaCombo.descCategoriaNotifica", tipoCategoriaNotificaVO.getDescrizione());
      htmpl.set("blkCategoriaCombo.index", ""+i);
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
	      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica()) 
	        && (filtriUnitaArboreaRicercaVO.getIdCategoriaNotifica().intValue() == tipoCategoriaNotificaVO.getIdCategoriaNotifica().intValue())) 
	      {
	        htmpl.set("categoriaSel",""+tipoCategoriaNotificaVO.getIdCategoriaNotifica());        
	      }
	    }
    }
  }
  
  if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
  {
    if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFlagNotificheChiuse())
      && "S".equalsIgnoreCase(filtriUnitaArboreaRicercaVO.getFlagNotificheChiuse()))
    {
      htmpl.set("checkedNotificaChiusa","checked=\"true\"", null);
    } 
  }
  
  // Causale modifica
  if(vTipoCausaleModifica != null) 
  {
    for(int i = 0; i < vTipoCausaleModifica.size(); i++) 
    {
      TipoCausaleModificaVO tipoCausaleModificaVO = vTipoCausaleModifica.get(i);
      htmpl.newBlock("blkTipoCausaleModifica");
      htmpl.set("blkTipoCausaleModifica.idCausaleModifica", ""+tipoCausaleModificaVO.getIdCausaleModifica());
      htmpl.set("blkTipoCausaleModifica.descrizione", tipoCausaleModificaVO.getDescrizione());
      
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
      {
        if(filtriUnitaArboreaRicercaVO.getIdCausaleModifica() != null 
          && filtriUnitaArboreaRicercaVO.getIdCausaleModifica().compareTo(tipoCausaleModificaVO.getIdCausaleModifica()) == 0) {
          htmpl.set("blkTipoCausaleModifica.selected", "selected=\"selected\"", null);
        }
      }
    
    }
  }
  
  // Combo tipo controllo
  if(elencoTipiControllo != null && elencoTipiControllo.length > 0) 
  {
    for(int i = 0; i < elencoTipiControllo.length; i++) {
      TipoControlloVO tipoControlloVO = (TipoControlloVO)elencoTipiControllo[i];
      htmpl.newBlock("blkTipoControllo");
      htmpl.set("blkTipoControllo.idControllo", tipoControlloVO.getIdControllo().toString());
      htmpl.set("blkTipoControllo.descrizione", tipoControlloVO.getDescrizione());
      if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO)
       && Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getIdControllo())) 
      {
        if(filtriUnitaArboreaRicercaVO.getIdControllo().compareTo(tipoControlloVO.getIdControllo()) == 0) {
          htmpl.set("blkTipoControllo.selected", "selected=\"selected\"", null);
        }
      }
    }
  }
	
  
  if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
  {
  	// Sezione
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSezione())) {
  		htmpl.set("sezione", filtriUnitaArboreaRicercaVO.getSezione());
  	}
  	
  	// Foglio
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getFoglio())) {
  		htmpl.set("foglio", filtriUnitaArboreaRicercaVO.getFoglio());
  	}
  	
  	// Particella
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getParticella())) {
  		htmpl.set("particella", filtriUnitaArboreaRicercaVO.getParticella());
  	}
  	// Subalterno
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getSubalterno())) {
  		htmpl.set("subalterno", filtriUnitaArboreaRicercaVO.getSubalterno());
  	}
  }
	
	// Immagini relative alle segnalazioni
	String sourceImage = null;
	if(pathToFollow.equalsIgnoreCase("rupar")) {
		sourceImage = application.getInitParameter("erroriRupar");
	}
	else if(pathToFollow.equalsIgnoreCase("sispie")){
		sourceImage = application.getInitParameter("erroriSispie");
	}
	else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
		sourceImage = application.getInitParameter("erroriTOBECONFIG");
	}
	htmpl.set("srcBloccante", sourceImage + SolmrConstants.IMMAGINE_BLOCCANTE);
	htmpl.set("descImmagineBloccante", SolmrConstants.DESC_TITLE_BLOCCANTE);	
	htmpl.set("srcWarning", sourceImage + SolmrConstants.IMMAGINE_WARNING);
	htmpl.set("descImmagineWarning", SolmrConstants.DESC_TITLE_WARNING);	
	htmpl.set("srcOk", sourceImage + SolmrConstants.IMMAGINE_OK);
	htmpl.set("descImmagineOk", SolmrConstants.DESC_TITLE_POSITIVO);	
	
  
  if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO))
  { 
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneBloccante())) {
  		htmpl.set("checkedBloccante", "checked=\"checked\"");	
  	}
  	
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneWarning())) {
  		htmpl.set("checkedWarning", "checked=\"checked\"");	
  	}
  	
  	
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getTipoSegnalazioneOk())) {
  		htmpl.set("checkedOk", "checked=\"checked\"");	
  	}
  	
  	// Data Esecuzione controlli
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getDataEsecuzioneControlli())) {
  		htmpl.set("dataEsecuzioneControlli", "Controlli effettuati il "+filtriUnitaArboreaRicercaVO.getDataEsecuzioneControlli());
  	}
  	else {
  		htmpl.set("dataEsecuzioneControlli",SolmrConstants.NO_CONTROLLI_PROCEDURA_UNITA_ARBOREE_ESEGUITI,null);
  	}
  	// Data esecuzione controlli dichiarati
  	if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getDataEsecuzioneControlliDichiarati())) {
  		htmpl.set("dataEsecuzioneControlliDichiarati", "Controlli effettuati il "+filtriUnitaArboreaRicercaVO.getDataEsecuzioneControlliDichiarati());
  	}
  	else {
  		htmpl.set("dataEsecuzioneControlliDichiarati",SolmrConstants.NO_CONTROLLI_PROCEDURA_UNITA_ARBOREE_ESEGUITI,null);
  	}
  }
	
	// Visualizzazione errori relativi ai filtri di ricerca o al nun max di particelle
	// selezionabili per le operazioni di modifica
	HtmplUtil.setErrors(htmpl, errors, request, application);
	
	// Visualizzazione degli errori relativi alla ricerca
	if(Validator.isNotEmpty(messaggioErrore)) 
  {
		htmpl.newBlock("blkErrore");
		htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
	}
	// Altrimenti visualizzo l'esito della ricerca
	else 
  {
		if(elencoParticelleArboree != null && elencoParticelleArboree.length > 0) 
    {
    
      //ruolo azienda o titolare
      if(Validator.isNotEmpty(request.getAttribute("elencoRidotto")))
      {
        htmpl.set("colonneLimitate","ok");
      }
    
			htmpl.newBlock("blkDati");
      
      
      if(session.getAttribute("esportaDatiUV") != null)
      {
        htmpl.newBlock("blkDati.blkEsportaDatiUV");
      }
      
      
			// Gestisco la paginazione
			elencoParticelleArboree = (StoricoParticellaVO[])HtmplUtil.paginazionePerGruppi(Integer.parseInt(paginaCorrente), elencoParticelleArboree, htmpl, request, errors, "blkDati", application, filtriUnitaArboreaRicercaVO);
			// Gestisco le frecce relative all'ordinamento:
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO)
        && Validator.isNotEmpty(filtriUnitaArboreaRicercaVO.getOrderBy())) 
      {
				// COMUNE
				if(filtriUnitaArboreaRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_DESC_COMUNE_UNAR_ASC)) 
        {
					htmpl.set("blkDati.ordinaComune", "giu");
					htmpl.set("blkDati.descOrdinaComune", "ordine decrescente");										
					htmpl.set("blkDati.tipoOrdinaComune", "comuneDisc");
				}
				else 
        {
					htmpl.set("blkDati.ordinaComune", "su");
					htmpl.set("blkDati.descOrdinaComune", "ordine crescente");
					htmpl.set("blkDati.tipoOrdinaComune", "comuneAsc");
				}
				// TIPOLOGIA UNAR
				if(filtriUnitaArboreaRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_DESC_TIPOLOGIA_UNAR_ASC)) 
        {
					htmpl.set("blkDati.ordinaDescUnar", "giu");
					htmpl.set("blkDati.descOrdinaDescUnar", "ordine decrescente");
					htmpl.set("blkDati.tipoOrdinaDescUnar", "descTipologiaUnarDisc");
				}
				else 
        {
					htmpl.set("blkDati.ordinaDescUnar", "su");
					htmpl.set("blkDati.descOrdinaDescUnar", "ordine crescente");
					htmpl.set("blkDati.tipoOrdinaDescUnar", "descTipologiaUnarAsc");
				}
				// PROGR
				if(filtriUnitaArboreaRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_PROGR_UNAR_ASC)) 
        {
					htmpl.set("blkDati.ordinaProgr", "giu");
					htmpl.set("blkDati.descOrdinaProgr", "ordine decrescente");
					htmpl.set("blkDati.tipoOrdinaProgr", "progrDisc");
				}
				else 
        {
					htmpl.set("blkDati.ordinaProgr", "su");
					htmpl.set("blkDati.descOrdinaProgr", "ordine crescente");
					htmpl.set("blkDati.tipoOrdinaProgr", "progrAsc");
				}
        // DATA IMPIANTO
        if(filtriUnitaArboreaRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_DATA_IMPIANTO_ASC)) 
        {
          htmpl.set("blkDati.ordinaDataImpianto", "giu");
          htmpl.set("blkDati.descOrdinaDataImpianto", "ordine decrescente");
          htmpl.set("blkDati.tipoOrdinaDataImpianto", "dataImpiantoDisc");
        }
        else 
        {
          htmpl.set("blkDati.ordinaDataImpianto", "su");
          htmpl.set("blkDati.descOrdinaDataImpianto", "ordine crescente");
          htmpl.set("blkDati.tipoOrdinaDataImpianto", "dataImpiantoAsc");
        }
				// VARIETA'
				if(filtriUnitaArboreaRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_DESC_VARIETA_UNAR_ASC)) 
        {
					htmpl.set("blkDati.ordinaVarieta", "giu");
					htmpl.set("blkDati.descOrdinaVarieta", "ordine decrescente");
					htmpl.set("blkDati.tipoOrdinaVarieta", "descVarietaDisc");
				}
				else 
        {
					htmpl.set("blkDati.ordinaVarieta", "su");
					htmpl.set("blkDati.descOrdinaVarieta", "ordine crescente");
					htmpl.set("blkDati.tipoOrdinaVarieta", "descVarietaAsc");
				}
        // TIPOLOGIA VINO
        if(filtriUnitaArboreaRicercaVO.getOrderBy().equalsIgnoreCase(SolmrConstants.ORDER_BY_TIPOLOGIA_VINO_ASC))
         {
          htmpl.set("blkDati.ordinaVino", "giu");
          htmpl.set("blkDati.descOrdinaVino", "ordine decrescente");
          htmpl.set("blkDati.tipoOrdinaVino", "descVinoDisc");
        }
        else 
        {
          htmpl.set("blkDati.ordinaVino", "su");
          htmpl.set("blkDati.descOrdinaVino", "ordine crescente");
          htmpl.set("blkDati.tipoOrdinaVino", "descVinoAsc");
        }
				
			}
			else 
      {
				htmpl.set("blkDati.ordinaComune", "giu");
				htmpl.set("blkDati.descOrdinaComune", "ordine decrescente");
				htmpl.set("blkDati.tipoOrdinaComune", "comuneDisc");
				htmpl.set("blkDati.ordinaDescUnar", "su");
				htmpl.set("blkDati.descOrdinaDescUnar", "ordine crescente");
				htmpl.set("blkDati.tipoOrdinaDescUnar", "descTipologiaUnarAsc");
				htmpl.set("blkDati.ordinaProgr", "su");
				htmpl.set("blkDati.descOrdinaProgr", "ordine crescente");
				htmpl.set("blkDati.tipoOrdinaProgr", "progrAsc");
        htmpl.set("blkDati.ordinaDataImpianto", "su");
        htmpl.set("blkDati.descOrdinaDataImpianto", "ordine crescente");
        htmpl.set("blkDati.tipoOrdinaDataImpianto", "dataImpiantoAsc");
				htmpl.set("blkDati.ordinaVarieta", "su");
				htmpl.set("blkDati.descOrdinaVarieta", "ordine crescente");
				htmpl.set("blkDati.tipoOrdinaVarieta", "descVarietaAsc");
        htmpl.set("blkDati.ordinaVino", "su");
        htmpl.set("blkDati.descOrdinaVino", "ordine crescente");
        htmpl.set("blkDati.tipoOrdinaVino", "descVinoAsc");
			}
			// SEZIONE DATI
			for(int i = 0; i < elencoParticelleArboree.length; i++) 
      {
				htmpl.newBlock("blkDati.blkElencoUnitaArboree");
				
				StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelleArboree[i];
        
        
        
        
				htmpl.set("blkDati.blkElencoUnitaArboree.idStoricoParticella", storicoParticellaVO.getIdStoricoParticella().toString());
				// Icona relativa allo schedario viti-vinicolo
				if(storicoParticellaVO.getParticellaVO() != null) {
					if(storicoParticellaVO.getParticellaVO().getFlagSchedario().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
						htmpl.set("blkDati.blkElencoUnitaArboree.descTitleSchedario", SolmrConstants.DESC_TITLE_SCHEDARIO);
						htmpl.set("blkDati.blkElencoUnitaArboree.classSchedario", SolmrConstants.CLASS_SCHEDARIO);
					}
					else if(storicoParticellaVO.getParticellaVO().getFlagSchedario().equalsIgnoreCase(SolmrConstants.FLAG_SCHEDARIO_MODIFICA)) {
						htmpl.set("blkDati.blkElencoUnitaArboree.descTitleSchedario", SolmrConstants.DESC_TITLE_SCHEDARIO_MODIFICATO);
						htmpl.set("blkDati.blkElencoUnitaArboree.classSchedario", SolmrConstants.CLASS_SCHEDARIO_MODIFICA);
					}
				}
				htmpl.set("blkDati.blkElencoUnitaArboree.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
				htmpl.set("blkDati.blkElencoUnitaArboree.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());					
				htmpl.set("blkDati.blkElencoUnitaArboree.sezione", storicoParticellaVO.getSezione());					
				htmpl.set("blkDati.blkElencoUnitaArboree.foglio", storicoParticellaVO.getFoglio());
				htmpl.set("blkDati.blkElencoUnitaArboree.particella", storicoParticellaVO.getParticella());					
				htmpl.set("blkDati.blkElencoUnitaArboree.subalterno", storicoParticellaVO.getSubalterno());
				htmpl.set("blkDati.blkElencoUnitaArboree.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
        htmpl.set("blkDati.blkElencoUnitaArboree.superficieGrafica", StringUtils.parseSuperficieField(storicoParticellaVO.getSuperficieGrafica()));
				
				
				if(storicoParticellaVO.getPercentualeUtilizzoEleg() != null)
				{
					BigDecimal percentualeUtilizzoElegTmp = storicoParticellaVO.getPercentualeUtilizzoEleg();
					htmpl.set("blkDati.blkElencoUnitaArboree.percUsoEleggDecimal", Formatter.formatDouble2(percentualeUtilizzoElegTmp));
	        if(percentualeUtilizzoElegTmp.compareTo(new BigDecimal(1)) < 0)
	        {
	          percentualeUtilizzoElegTmp = new BigDecimal(1);
	        }      
	        htmpl.set("blkDati.blkElencoUnitaArboree.percUsoElegg", Formatter.formatAndRoundBigDecimal0(percentualeUtilizzoElegTmp));
	      }
				
				
        
        ParticellaCertificataVO particellaCertificataVO = storicoParticellaVO.getParticellaCertificataVO();
        
        //Icone del gis
        String titleGis= "";
        String immaginiControlliP = "";
        if(storicoParticellaVO.getSospesaGis() != null)
        {
          titleGis += "GIS Anomalie: Lavorazione sospesa il ";
          titleGis += DateUtils.formatDateNotNull(particellaCertificataVO.getDataSospensione());
          if(Validator.isNotEmpty(particellaCertificataVO.getMotivazioneGis()))
          {
            titleGis += " - "+particellaCertificataVO.getMotivazioneGis();
          }
          if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
            && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
              .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
          {
            immaginiControlliP = SolmrConstants.IMMAGINE_SOSPESA_GIS_STAB;
          }
          else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
            && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
              .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
          {
            immaginiControlliP = SolmrConstants.IMMAGINE_SOSPESA_GIS_STAB_CORSO;
          }
          else
          {
            immaginiControlliP = SolmrConstants.IMMAGINE_SOSPESA_GIS;
          }  
        }         
        if(storicoParticellaVO.getGisP30() != null)
        {
          if(Validator.isNotEmpty(titleGis))
          {
            titleGis += ", P30";
          }
          else
          {
            titleGis += "GIS Anomalie: P30";
          }
          
          if(Validator.isEmpty(immaginiControlliP))
          {
            if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P30_STAB;
            }
            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P30_STAB_CORSO;
            }
            else
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P30;
            }  
          }          
        }   
        if(storicoParticellaVO.getGisP25() != null)
        {
          if(Validator.isNotEmpty(titleGis))
          {
            titleGis += ", P25";
          }
          else
          {
            titleGis += "GIS Anomalie: P25";
          }
          
          if(Validator.isEmpty(immaginiControlliP))
          {
            if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P25_STAB;
            }
            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P25_STAB_CORSO;
            }
            else
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P25;
            }
          }          
          
        }          
        if(storicoParticellaVO.getGisP26() != null)
        {
          if(Validator.isNotEmpty(titleGis))
          {
            titleGis += ", P26";
          }
          else
          {
            titleGis += "GIS Anomalie: P26";
          }
          
          if(Validator.isEmpty(immaginiControlliP))
          {
            if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26_STAB;
            }
            else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
              && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
                .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26_STAB_CORSO;
            }
            else
            {
              immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_P26;
            }
          }  
        }
        if(Validator.isEmpty(titleGis))
        {
          titleGis += "GIS";
        }
        
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
        
                  
        htmpl.set("blkDati.blkElencoUnitaArboree.controlliP", titleGis);
        if(Validator.isEmpty(immaginiControlliP))
        {
          if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
            && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
              .compareTo(SolmrConstants.FOGLIO_STABILIZZATO) == 0))
          {
            immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS_STAB;
          }
          else if(Validator.isNotEmpty(storicoParticellaVO.getFoglioVO().getFlagStabilizzazione())
            && (storicoParticellaVO.getFoglioVO().getFlagStabilizzazione()
              .compareTo(SolmrConstants.FOGLIO_STAB_IN_CORSO) == 0))
          {
            immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS_STAB_CORSO;
          }
          else
          {
            immaginiControlliP = (String)SolmrConstants.IMMAGINE_GIS_GIS;
          }
        }  
        htmpl.set("blkDati.blkElencoUnitaArboree.immaginiControlliP", immaginiControlliP);
        
        
        
        
        
        //Icone istanza riesame
        String titleIstanzaRiesame= "";
        String immaginiIstanzaRiesame = "";
        //E' stata lavorata
        if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) 
        {
	        if(Validator.isNotEmpty(storicoParticellaVO.getIstanzaRiesame()))
	        {
	          if(storicoParticellaVO.getIstanzaRiesame().compareTo(new Long(2)) == 0)
	          {
	            titleIstanzaRiesame += "Istanza di riesame evasa";
	            immaginiIstanzaRiesame = (String)SolmrConstants.IMMAGINE_ISTANZA_FINE;
	          }
	          else if(storicoParticellaVO.getIstanzaRiesame().compareTo(new Long(1)) == 0)
	          {
	            titleIstanzaRiesame += "Richiesta istanza di riesame";
	            immaginiIstanzaRiesame = (String)SolmrConstants.IMMAGINE_ISTANZA_INIZIO;         
	          }
	        }
	      }
	      else
	      {       
	        if(Validator.isNotEmpty(storicoParticellaVO.getvIstanzaRiesame()))
	        {
	          //la query ritona solo l'ultima fase
	          //se valorizzata data_evasione...
	          if(Validator.isNotEmpty(storicoParticellaVO.getvIstanzaRiesame().get(0).getDataEvasione()))
	          {
	            titleIstanzaRiesame += "Istanza di riesame evasa";
	            immaginiIstanzaRiesame = (String)SolmrConstants.IMMAGINE_ISTANZA_FINE;
	          }
	          //altrimenti se ritorna la fase cmq la data_richiesta deve essere per forza presente
	          else
	          {
	            titleIstanzaRiesame += "Richiesta istanza di riesame";
	            immaginiIstanzaRiesame = (String)SolmrConstants.IMMAGINE_ISTANZA_INIZIO;            
	          }
	        }
	      }        
                         
        htmpl.set("blkDati.blkElencoUnitaArboree.descTitleIstanzaRiesame", titleIstanzaRiesame);
        htmpl.set("blkDati.blkElencoUnitaArboree.immaginiIstanzaRiesame", immaginiIstanzaRiesame);
        
        
        
        
        
        
        //Nuova eleggibilità!!!
        if(particellaCertificataVO != null)
        {
          if(Validator.isNotEmpty(particellaCertificataVO.getVParticellaCertEleg()) 
            && (particellaCertificataVO.getVParticellaCertEleg().size() > 0)) 
          {
            //Per la query è popolato solo il primo elemento
            ParticellaCertElegVO partCertVO = (ParticellaCertElegVO)particellaCertificataVO.getVParticellaCertEleg().get(0);
            if(Validator.isNotEmpty(partCertVO.getSuperficie())) {
              htmpl.set("blkDati.blkElencoUnitaArboree.supEleggibile", Formatter.formatDouble4(partCertVO.getSuperficie()));         
            }
            else 
            {
              htmpl.set("blkDati.blkElencoUnitaArboree.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
            }
          }
          else
          {
            htmpl.set("blkDati.blkElencoUnitaArboree.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
          }
        }
        else 
        {
          htmpl.set("blkDati.blkElencoUnitaArboree.supEleggibile", SolmrConstants.DEFAULT_SUPERFICIE);
        }
        //****************************
				// Piano riferimento per anno corrente
				if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0) 
        {
        
          BigDecimal percentualePossessoTmp = storicoParticellaVO.getElencoConduzioni()[0].getPercentualePossesso();
          htmpl.set("blkDati.blkElencoUnitaArboree.percentualePossessoDecimal", Formatter.formatDouble2(percentualePossessoTmp));
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }
        
          htmpl.set("blkDati.blkElencoUnitaArboree.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));          
          
					StoricoUnitaArboreaVO storicoUnitaArboreaVO = storicoParticellaVO.getStoricoUnitaArboreaVO();
          
          if (storicoUnitaArboreaVO.getTipoTipologiaVinoVO()!=null)
            htmpl.set("blkDati.blkElencoUnitaArboree.tipologiaVino", storicoUnitaArboreaVO.getTipoTipologiaVinoVO().getDescrizione());
          
          htmpl.set("blkDati.blkElencoUnitaArboree.annoIscrizioneAlbo", storicoUnitaArboreaVO.getAnnoIscrizioneAlbo());
					htmpl.set("blkDati.blkElencoUnitaArboree.matricolaCCIAA", storicoUnitaArboreaVO.getMatricolaCCIAA());
          htmpl.set("blkDati.blkElencoUnitaArboree.idStoricoUnitaArborea", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
					htmpl.set("blkDati.blkElencoUnitaArboree.idUnita", storicoUnitaArboreaVO.getIdStoricoUnitaArborea().toString());
					// Gestione dei record selezionati per le varie operazioni di modifica multipla
					// del dato
					if(elencoIdStoricoSelezionati != null && elencoIdStoricoSelezionati.size() > 0) {
						for(int a = 0; a < elencoIdStoricoSelezionati.size(); a++) {
							Long idStoricoSelezionato = (Long)elencoIdStoricoSelezionati.elementAt(a);
							if(idStoricoSelezionato.compareTo(storicoUnitaArboreaVO.getIdStoricoUnitaArborea()) == 0) {
								htmpl.set("blkDati.blkElencoUnitaArboree.checked", "checked=\"checked\"", null);
							}
						}
					}
					// Icona di segnalazione delle anomalie
					if(Validator.isNotEmpty(storicoUnitaArboreaVO.getEsitoControllo())) {
						// Bloccante
						if(storicoUnitaArboreaVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_BLOCCANTE)) {
							htmpl.set("blkDati.blkElencoUnitaArboree.classImmagine", SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
							htmpl.set("blkDati.blkElencoUnitaArboree.descTitleAnomalia", SolmrConstants.DESC_TITLE_BLOCCANTE);
						}
						// Warning
						else if(storicoUnitaArboreaVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_WARNING)) {
							htmpl.set("blkDati.blkElencoUnitaArboree.classImmagine", SolmrConstants.IMMAGINE_ESITO_WARNING);
							htmpl.set("blkDati.blkElencoUnitaArboree.descTitleAnomalia", SolmrConstants.DESC_TITLE_WARNING);
						}
						// Positivo
						else if(storicoUnitaArboreaVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_POSITIVO)) {
							htmpl.set("blkDati.blkElencoUnitaArboree.classImmagine", SolmrConstants.IMMAGINE_ESITO_POSITIVO);
							htmpl.set("blkDati.blkElencoUnitaArboree.descTitleAnomalia", SolmrConstants.DESC_TITLE_POSITIVO);
						}
					}
					// Icona relativa allo notifica
					if(Validator.isNotEmpty(storicoUnitaArboreaVO.getInNotifica())) 
					{
						// Validato PA
						htmpl.set("blkDati.blkElencoUnitaArboree.classNotifica", SolmrConstants.STATO_BLOCCO_PROCEDIMENTI);
						htmpl.set("blkDati.blkElencoUnitaArboree.descNotifica", SolmrConstants.DESC_BLOCCO_PROCEDIMENTI);
					}
					// Icona relativa allo stato dell'unità arborea
          if(Validator.isNotEmpty(storicoUnitaArboreaVO.getStatoUnitaArborea())) {
            // Validato PA
            if(storicoUnitaArboreaVO.getStatoUnitaArborea().equalsIgnoreCase(SolmrConstants.STATO_UV_VALIDATO_PA)) {
              htmpl.set("blkDati.blkElencoUnitaArboree.descTitleStatoUnitaArborea", SolmrConstants.DESC_STATO_UV_VALIDATO_PA);
              htmpl.set("blkDati.blkElencoUnitaArboree.classStatoUnitaArborea", SolmrConstants.CLASS_STATO_UV_VALIDATO_PA);
            }
            htmpl.set("blkDati.blkElencoUnitaArboree.statoUnitaArborea", storicoUnitaArboreaVO.getStatoUnitaArborea());
          }
					// Visualizzo di colore diverso lo sfondo della chiave logica delle particelle modificate
					if(storicoUnitaArboreaVO.getDataFineValidita() != null) 
          {
						htmpl.set("blkDati.blkElencoUnitaArboree.color", SolmrConstants.COLOR_UNITA_ARBOREE_STORICIZZATE);
					}
					else if(Validator.isNotEmpty(storicoUnitaArboreaVO.getRecordModificato()) && storicoUnitaArboreaVO.getRecordModificato().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
						htmpl.set("blkDati.blkElencoUnitaArboree.color", SolmrConstants.COLOR_PARTICELLE_MODIFICATE);
					}
          
          
					if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() == 0) {
						if(i == 0) {
							htmpl.newBlock("blkDati.blkEtichettaDataFine");
						}
						htmpl.newBlock("blkDati.blkElencoUnitaArboree.blkDataFine");
						if(storicoUnitaArboreaVO.getDataFineValidita() != null) {
							htmpl.set("blkDati.blkElencoUnitaArboree.blkDataFine.dataFineValidita", DateUtils.formatDate(storicoUnitaArboreaVO.getDataFineValidita()));
						}
					}
					htmpl.set("blkDati.blkElencoUnitaArboree.progrUnar", storicoUnitaArboreaVO.getProgrUnar());
					htmpl.set("blkDati.blkElencoUnitaArboree.sestoSuFila", storicoUnitaArboreaVO.getSestoSuFila());
					htmpl.set("blkDati.blkElencoUnitaArboree.sestoTraFile", storicoUnitaArboreaVO.getSestoTraFile());
					htmpl.set("blkDati.blkElencoUnitaArboree.numeroCeppi", storicoUnitaArboreaVO.getNumCeppi());
					
          if(storicoUnitaArboreaVO.getDataImpianto() != null) 
          {
            htmpl.set("blkDati.blkElencoUnitaArboree.dataImpianto", DateUtils.formatDate(storicoUnitaArboreaVO.getDataImpianto()));
          }
					if(storicoUnitaArboreaVO.getIdFormaAllevamento() != null) {
						htmpl.set("blkDati.blkElencoUnitaArboree.descFormaAllevamento", storicoUnitaArboreaVO.getTipoFormaAllevamentoVO().getDescrizione());
					}
					if(Validator.isNotEmpty(storicoUnitaArboreaVO.getArea())) {
						htmpl.set("blkDati.blkElencoUnitaArboree.area", StringUtils.parseSuperficieField(storicoUnitaArboreaVO.getArea()));
					}
	     		if(storicoUnitaArboreaVO.getIdVarieta() != null) {
	     			htmpl.set("blkDati.blkElencoUnitaArboree.varieta", "["+storicoUnitaArboreaVO.getTipoVarietaVO().getCodiceVarieta()+"] "+storicoUnitaArboreaVO.getTipoVarietaVO().getDescrizione());
	     		}
	     		htmpl.set("blkDati.blkElencoUnitaArboree.percentualeVarieta", storicoUnitaArboreaVO.getPercentualeVarieta());
	     		if(storicoUnitaArboreaVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	     			htmpl.set("blkDati.blkElencoUnitaArboree.altriVitigni", SolmrConstants.FLAG_SI);
	     		}
	     		else {
	     			htmpl.set("blkDati.blkElencoUnitaArboree.altriVitigni", SolmrConstants.FLAG_NO);
	     		}
          
          if(Validator.isNotEmpty(storicoUnitaArboreaVO.getIdCausaleModifica()))
          {
            htmpl.set("blkDati.blkElencoUnitaArboree.idCausaleModifica", ""+storicoUnitaArboreaVO.getIdCausaleModifica());
            elencoCausMod.add(storicoUnitaArboreaVO.getTipoCausaleModificaVO());
          }
          
          
          
          /*if(Validator.isNotEmpty(compensazioneAziendaVO)
	          && Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis()))
	        {
	          //non metto la tolleranza se è stato fatto il consolidamento
	        }
	        else
	        {
	          if(i == 0) 
	          {
              htmpl.newBlock("blkDati.blkEtichettaTolleranza");
            }
	          htmpl.newBlock("blkDati.blkElencoUnitaArboree.blkTolleranza");
	          
	          
	          
	          if(storicoUnitaArboreaVO.getDataFineValidita() != null) 
	          {
	            htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.color", SolmrConstants.COLOR_UNITA_ARBOREE_STORICIZZATE);
	          }
	          else if(Validator.isNotEmpty(storicoUnitaArboreaVO.getRecordModificato()) && storicoUnitaArboreaVO.getRecordModificato().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	            htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.color", SolmrConstants.COLOR_PARTICELLE_MODIFICATE);
	          }
	          
	          
	          //TOLLERANZA
	          if(Validator.isNotEmpty(storicoUnitaArboreaVO.getTolleranza()))
	          { 
	            if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.IN_TOLLERANZA) == 0) 
	            {     
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.UV_TOLLERANZA_OK, ""}), null);
	            }
	            else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_FUORI_KO, ""}), null);              
	            }
	            else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_UVDOPPIE_KO, ""}), null);              
	            }
	            else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.NO_PARCELLE_TOLLERANZA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ISO_TOLLERANZA_NO_PARCELLE_KO, ""}), null);              
	            }
	            else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.ERR_PL_TOLLERANZA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ISO_TOLLERANZA_PLSQL_KO, ""}), null);              
	            }
	            else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.UV_NON_PRESENTE) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_NON_PRESENTE_KO, ""}), null);              
	            }
	            else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.PARTICELLA_ORFANA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_PARTICELLA_ORFANA_KO, ""}), null);              
	            }
	            else if(storicoUnitaArboreaVO.getTolleranza().compareTo(SolmrConstants.UV_PIU_OCCORR_ATTIVE) == 0) 
              {
                htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_PIU_OCCORR_ATTIVE_KO, ""}), null);              
              }
	            else 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_PARCELLE_NO_VITE_KO, ""}), null);              
	            }
	          }
	        }*/        
          
          
				}
				// Piano di riferimento alla dichiarazione di consistenza
				else 
        {
          BigDecimal percentualePossessoTmp = storicoParticellaVO.getElencoConduzioniDichiarate()[0].getPercentualePossesso();
          htmpl.set("blkDati.blkElencoUnitaArboree.percentualePossessoDecimal", Formatter.formatDouble2(percentualePossessoTmp));
          if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
          {
            percentualePossessoTmp = new BigDecimal(1);
          }
          htmpl.set("blkDati.blkElencoUnitaArboree.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));
        
					UnitaArboreaDichiarataVO unitaArboreaDichiarataVO = storicoParticellaVO.getUnitaArboreaDichiarataVO();         
          
          if (unitaArboreaDichiarataVO.getTipoTipologiaVinoVO()!=null)
            htmpl.set("blkDati.blkElencoUnitaArboree.tipologiaVino", unitaArboreaDichiarataVO.getTipoTipologiaVinoVO().getDescrizione());
          
          htmpl.set("blkDati.blkElencoUnitaArboree.annoIscrizioneAlbo", unitaArboreaDichiarataVO.getAnnoIscrizioneAlbo());
          htmpl.set("blkDati.blkElencoUnitaArboree.matricolaCCIAA", unitaArboreaDichiarataVO.getMatricolaCCIAA());
          htmpl.set("blkDati.blkElencoUnitaArboree.idStoricoUnitaArborea", unitaArboreaDichiarataVO.getIdStoricoUnitaArborea().toString());
					htmpl.set("blkDati.blkElencoUnitaArboree.idUnita", unitaArboreaDichiarataVO.getIdUnitaArboreaDichiarata().toString());
					// Icona di segnalazione delle anomalie
					if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getEsitoControllo())) {
						// Bloccante
						if(unitaArboreaDichiarataVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_BLOCCANTE)) {
							htmpl.set("blkDati.blkElencoUnitaArboree.classImmagine", SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
							htmpl.set("blkDati.blkElencoUnitaArboree.descTitleAnomalia", SolmrConstants.DESC_TITLE_BLOCCANTE);
						}
						// Warning
						else if(unitaArboreaDichiarataVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_WARNING)) {
							htmpl.set("blkDati.blkElencoUnitaArboree.classImmagine", SolmrConstants.IMMAGINE_ESITO_WARNING);
							htmpl.set("blkDati.blkElencoUnitaArboree.descTitleAnomalia", SolmrConstants.DESC_TITLE_WARNING);
						}
						// Positivo
						else if(unitaArboreaDichiarataVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_POSITIVO)) {
							htmpl.set("blkDati.blkElencoUnitaArboree.classImmagine", SolmrConstants.IMMAGINE_ESITO_POSITIVO);
							htmpl.set("blkDati.blkElencoUnitaArboree.descTitleAnomalia", SolmrConstants.DESC_TITLE_POSITIVO);
						}
					}
					// Icona relativa allo stato dell'unità arborea
					if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getStatoUnitaArborea())) {
						// Validato PA
						if(unitaArboreaDichiarataVO.getStatoUnitaArborea().equalsIgnoreCase(SolmrConstants.STATO_UV_VALIDATO_PA)) {
							htmpl.set("blkDati.blkElencoUnitaArboree.descTitleStatoUnitaArborea", SolmrConstants.DESC_STATO_UV_VALIDATO_PA);
							htmpl.set("blkDati.blkElencoUnitaArboree.classStatoUnitaArborea", SolmrConstants.CLASS_STATO_UV_VALIDATO_PA);
						}
						htmpl.set("blkDati.blkElencoUnitaArboree.statoUnitaArborea", unitaArboreaDichiarataVO.getStatoUnitaArborea());
					}
					// Icona relativa allo stato dell'unità arborea
          if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getInNotifica())) 
          {
            // Validato PA
            htmpl.set("blkDati.blkElencoUnitaArboree.classNotifica", SolmrConstants.STATO_BLOCCO_PROCEDIMENTI);
            htmpl.set("blkDati.blkElencoUnitaArboree.descNotifica", SolmrConstants.DESC_BLOCCO_PROCEDIMENTI);
          }
					// Visualizzo di colore diverso lo sfondo della chiave logica delle particelle modificate
					if(unitaArboreaDichiarataVO.getDataFineValidita() != null) {
						htmpl.set("blkDati.blkElencoUnitaArboree.color", SolmrConstants.COLOR_UNITA_ARBOREE_STORICIZZATE);
					}
					htmpl.set("blkDati.blkElencoUnitaArboree.progrUnar", unitaArboreaDichiarataVO.getProgrUnar());
					htmpl.set("blkDati.blkElencoUnitaArboree.sestoSuFila", unitaArboreaDichiarataVO.getSestoSuFila());
					htmpl.set("blkDati.blkElencoUnitaArboree.sestoTraFile", unitaArboreaDichiarataVO.getSestoTraFile());
					htmpl.set("blkDati.blkElencoUnitaArboree.numeroCeppi", unitaArboreaDichiarataVO.getNumCeppi());
					
          if(unitaArboreaDichiarataVO.getDataImpianto() != null) 
          {
            htmpl.set("blkDati.blkElencoUnitaArboree.dataImpianto", DateUtils.formatDate(unitaArboreaDichiarataVO.getDataImpianto()));
          }
          if(unitaArboreaDichiarataVO.getIdFormaAllevamento() != null) {
						htmpl.set("blkDati.blkElencoUnitaArboree.descFormaAllevamento", unitaArboreaDichiarataVO.getTipoFormaAllevamentoVO().getDescrizione());
					}
					if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getArea())) {
						htmpl.set("blkDati.blkElencoUnitaArboree.area", StringUtils.parseSuperficieField(unitaArboreaDichiarataVO.getArea()));
					}
	     		if(unitaArboreaDichiarataVO.getIdVarieta() != null) {
	     			htmpl.set("blkDati.blkElencoUnitaArboree.varieta", "["+unitaArboreaDichiarataVO.getTipoVarietaVO().getCodiceVarieta()+"] "+unitaArboreaDichiarataVO.getTipoVarietaVO().getDescrizione());
	     		}
	     		htmpl.set("blkDati.blkElencoUnitaArboree.percentualeVarieta", unitaArboreaDichiarataVO.getPercentualeVarieta());
	     		if(unitaArboreaDichiarataVO.getPresenzaAltriVitigni().equalsIgnoreCase(SolmrConstants.FLAG_S)) {
	     			htmpl.set("blkDati.blkElencoUnitaArboree.altriVitigni", SolmrConstants.FLAG_SI);
	     		}
	     		else {
	     			htmpl.set("blkDati.blkElencoUnitaArboree.altriVitigni", SolmrConstants.FLAG_NO);
	     		}
          
          if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getIdCausaleModifica()))
          {
            htmpl.set("blkDati.blkElencoUnitaArboree.idCausaleModifica", ""+unitaArboreaDichiarataVO.getIdCausaleModifica());
            elencoCausMod.add(unitaArboreaDichiarataVO.getTipoCausaleModificaVO());
          }
          
          
          /*if(Validator.isNotEmpty(compensazioneAziendaVO)
            && Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis())
            && compensazioneAziendaVO.getDataConsolidamentoGis().before(consistenzaVO.getDataInserimentoDichiarazione()))
          {
            //non metto la tolleranza se è stato fatto il consolidamento
          }
          else
          {
          
            if(i == 0) 
            {
              htmpl.newBlock("blkDati.blkEtichettaTolleranza");
            }
            htmpl.newBlock("blkDati.blkElencoUnitaArboree.blkTolleranza");            
	          //TOLLERANZA
	          if(Validator.isNotEmpty(unitaArboreaDichiarataVO.getTolleranza()))
	          { 
	            if(unitaArboreaDichiarataVO.getTolleranza().compareTo(SolmrConstants.IN_TOLLERANZA) == 0) 
	            {     
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.UV_TOLLERANZA_OK, ""}), null);
	            }
	            else if(unitaArboreaDichiarataVO.getTolleranza().compareTo(SolmrConstants.FUORI_TOLLERANZA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_FUORI_KO, ""}), null);              
	            }
	            else if(unitaArboreaDichiarataVO.getTolleranza().compareTo(SolmrConstants.UVDOPPIE_TOLLERANZA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_UVDOPPIE_KO, ""}), null);              
	            }
	            else if(unitaArboreaDichiarataVO.getTolleranza().compareTo(SolmrConstants.NO_PARCELLE_TOLLERANZA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ISO_TOLLERANZA_NO_PARCELLE_KO, ""}), null);              
	            }
	            else if(unitaArboreaDichiarataVO.getTolleranza().compareTo(SolmrConstants.ERR_PL_TOLLERANZA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ISO_TOLLERANZA_PLSQL_KO, ""}), null);              
	            }
	            else if(unitaArboreaDichiarataVO.getTolleranza().compareTo(SolmrConstants.UV_NON_PRESENTE) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_NON_PRESENTE_KO, ""}), null);              
	            }
	            else if(unitaArboreaDichiarataVO.getTolleranza().compareTo(SolmrConstants.PARTICELLA_ORFANA) == 0) 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_PARTICELLA_ORFANA_KO, ""}), null);              
	            }
	            else if(unitaArboreaDichiarataVO.getTolleranza().compareTo(SolmrConstants.UV_PIU_OCCORR_ATTIVE) == 0) 
              {
                htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_PIU_OCCORR_ATTIVE_KO, ""}), null);              
              }
	            else 
	            {
	              htmpl.set("blkDati.blkElencoUnitaArboree.blkTolleranza.tolleranza", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.UV_TOLLERANZA_PARCELLE_NO_VITE_KO, ""}), null);              
	            }
	          }
	        }*/
          
          
				}
			}
			
			
			/*if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO)
        && (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() <= 0))
      { 
				if(Validator.isNotEmpty(compensazioneAziendaVO)
	        && Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis()))
	      {
	        htmpl.set("blkDati.firstTotColspan", "12");
	      }
	      else 
	      {
	        htmpl.set("blkDati.firstTotColspan", "13");
	      }
	    }
	    else
	    {
	      if(Validator.isNotEmpty(compensazioneAziendaVO)
            && Validator.isNotEmpty(compensazioneAziendaVO.getDataConsolidamentoGis())
            && compensazioneAziendaVO.getDataConsolidamentoGis().before(consistenzaVO.getDataInserimentoDichiarazione()))
        {
          htmpl.set("blkDati.firstTotColspan", "12");
        }
        else 
        {
          htmpl.set("blkDati.firstTotColspan", "13");
        }	    
	    }*/
			
			if(Validator.isNotEmpty(filtriUnitaArboreaRicercaVO)
        && (filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().longValue() == 0)) 
      {
				htmpl.set("blkDati.lastTotColspan", "13");
			}
			else 
      {
				htmpl.set("blkDati.lastTotColspan", "12");
			}
      
      
      if(elencoCausMod != null && elencoCausMod.size() > 0) 
      {
        TreeMap<String,String> tElencoCausMod = StringUtils.getAndSortLegenda(elencoCausMod);
        Set<String> elencoKeys = tElencoCausMod.keySet();
        Iterator<String> itera = elencoKeys.iterator();
        int contatore = 0;
        while(itera.hasNext()) 
        {
          String key = (String)itera.next();
          String descrizione = (String)tElencoCausMod.get(key);
          htmpl.newBlock("blkDati.blkLegendaCausMod");
          htmpl.set("blkDati.blkLegendaCausMod.idCausaleModifica", key+" - ");
          if(contatore == (tElencoCausMod.size() - 1)) 
          {
            htmpl.set("blkDati.blkLegendaCausMod.descCausMod", descrizione);
          }
          else {
            htmpl.set("blkDati.blkLegendaCausMod.descCausMod", descrizione+", ");
          }
          contatore++;
        }
      }
      
		}
		else 
		{
			htmpl.newBlock("blkErrore");
			htmpl.set("blkErrore.messaggioErrore", AnagErrors.ERRORE_NO_UNITA_ARBOREE_AZIENDA_FOUND);
		}
	}
	

%>
<%= htmpl.text()%>

