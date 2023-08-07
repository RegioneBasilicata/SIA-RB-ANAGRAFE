<%@ page language="java"
contentType="text/html"
isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.fascicoli.InvioFascicoliVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	SolmrLogger.debug(this, " - dichiarazioneConsistenzaDetView.jsp - INIZIO PAGINA");

	java.io.InputStream layout = application.getResourceAsStream("/layout/dichiarazione_det.htm");
	Htmpl htmpl = new Htmpl(layout);

	%>
  	<%@include file = "/view/remoteInclude.inc" %>
	<%

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	HashMap elencoDocumentiAssociati = (HashMap)request.getAttribute("elencoDocumentiAssociati");
	ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaRicercaVO = (ErrAnomaliaDicConsistenzaVO)session.getAttribute("errAnomaliaDicConsistenzaRicercaVO");
	CodeDescription[] elencoTipoGruppoControllo = (CodeDescription[])request.getAttribute("elencoTipoGruppoControllo");
	Long idGruppoControllo = (Long)request.getAttribute("idGruppoControllo");
	TipoControlloVO[] elencoTipoControllo = (TipoControlloVO[])request.getAttribute("elencoTipoControllo");
	Long idControllo = (Long)request.getAttribute("idControllo");
  
  String htmlStringKO = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imko = "ko.gif";
  String imok = "ok.gif";
  
  
  
  	
	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);

	HtmplUtil.setValues(htmpl, anagAziendaVO);

	String percorsoErrori = null;
	if(pathToFollow.equalsIgnoreCase("rupar")) {
  	percorsoErrori = "/css_rupar/agricoltura/im/";
	}
	else if(pathToFollow.equalsIgnoreCase("sispie")) {
  	    percorsoErrori = "/css/agricoltura/im/";
  	}
  	else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
  	    percorsoErrori="/css/agricoltura/im/";  
  	}

	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");


	SolmrLogger.debug(this, "anagAziendaVO.getCUAA(): " + anagAziendaVO.getCUAA());

	ConsistenzaVO consistenzaVO = (ConsistenzaVO)request.getAttribute("consistenzaVO");
	ErrAnomaliaDicConsistenzaVO[] anomalie = (ErrAnomaliaDicConsistenzaVO[])session.getAttribute("anomalieDichiarazioniConsistenza");
	SolmrLogger.debug(this, "consistenzaVO: " + consistenzaVO);
  FascicoloNazionaleVO fascicoloNazionaleVO = (FascicoloNazionaleVO)request.getAttribute("fascicoloNazionaleVO");
  InvioFascicoliVO invioFascicoliVO = (InvioFascicoliVO)request.getAttribute("invioFascicoliVO");
  AllegatoDichiarazioneVO allegatoDichiarazioneVO = (AllegatoDichiarazioneVO)request.getAttribute("allegatoDichiarazioneVO");
  Vector<AllegatoDichiarazioneVO> vAllegatiDichiarazioneVO = (Vector<AllegatoDichiarazioneVO>)request.getAttribute("vAllegatiDichiarazioneVO");

	if(consistenzaVO != null) 
  {
		htmpl.set("idDichiarazioneConsistenza", consistenzaVO.getIdDichiarazioneConsistenza());
  	htmpl.newBlock("blkDichiarazioneConsistenza");
  	htmpl.set("blkDichiarazioneConsistenza.anno", consistenzaVO.getAnno());
  	htmpl.set("blkDichiarazioneConsistenza.data", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataDichiarazione()));
  	htmpl.set("blkDichiarazioneConsistenza.motivo",consistenzaVO.getMotivo());
    if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo())) 
    {
    	htmpl.set("blkDichiarazioneConsistenza.numeroProtocollo", StringUtils.parseNumeroProtocolloField(consistenzaVO.getNumeroProtocollo()));
    	htmpl.set("blkDichiarazioneConsistenza.dataProtocollo", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataProtocollo()));
    }
    
    /*if(Validator.isNotEmpty(allegatoDichiarazioneVO))
    {
      String descTipoFirma = "Validazione non firmata";
      if(Validator.isNotEmpty(allegatoDichiarazioneVO.getDescTipoFirma()))
      {
        descTipoFirma = allegatoDichiarazioneVO.getDescTipoFirma();
      }
      htmpl.set("blkDichiarazioneConsistenza.descTipoFirma", descTipoFirma);
      
      if(Validator.isNotEmpty(allegatoDichiarazioneVO.getIdTipoFirma()))
      {
        if(allegatoDichiarazioneVO.getIdTipoFirma().compareTo(new Long(1)) == 0)
        { 
          htmpl.set("blkDichiarazioneConsistenza.dataFirma", DateUtils.formatDateTimeNotNull(consistenzaVO.getDataProtocollo()));
        }
        else if(allegatoDichiarazioneVO.getIdTipoFirma().compareTo(new Long(2)) == 0)
        {
          htmpl.set("blkDichiarazioneConsistenza.dataFirma", DateUtils.formatDateTimeNotNull(allegatoDichiarazioneVO.getDataUltimoAggiornamento()));
        }
        
      }
    }*/
    
    if(Validator.isNotEmpty(invioFascicoliVO))
	  { 
	    String statoInvio = "";
	    if(SolmrConstants.SCHED_STATO_INVIATO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
	    {
	      statoInvio = "Invio in corso";
	    }
	    else if(SolmrConstants.SCHED_STATO_SCHEDULATO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
	    {
	      statoInvio = "Schedulato";
	    }
	    else if(SolmrConstants.SCHED_STATO_CHIUSO.equalsIgnoreCase(invioFascicoliVO.getStatoInvio()))
	    {
	      statoInvio = "Chiuso";
	    }
	    
	    htmpl.set("blkDichiarazioneConsistenza.statoInvio", statoInvio);
	    htmpl.set("blkDichiarazioneConsistenza.dataRichiesta", DateUtils.formatDateTimeNotNull(invioFascicoliVO.getDataRichiesta()));
	    htmpl.set("blkDichiarazioneConsistenza.dataConclusione", DateUtils.formatDateTimeNotNull(invioFascicoliVO.getDataConclusione()));
	    htmpl.set("blkDichiarazioneConsistenza.numTentativi",""+invioFascicoliVO.getNumTentativiInvio());
	  }
    
    
    
    if(consistenzaVO.getDataAggiornamentoFascicolo() != null) 
    {
    	htmpl.set("blkDichiarazioneConsistenza.dataAggiornamentoSian", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataAggiornamentoFascicolo()));
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomalia()) && consistenzaVO.getFlagAnomalia().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSian", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_OK, ""}), null);
      }
      else 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSian", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_KO, ""}), null);              
      }
      
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomalia()) && consistenzaVO.getFlagAnomalia().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSian", SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_OK);
      }
      else 
      {
        String esitoAggiornamento = SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_KO;
        if(Validator.isNotEmpty(fascicoloNazionaleVO) 
          && Validator.isNotEmpty(fascicoloNazionaleVO.getCodErroreFascicolo())
          && Validator.isNotEmpty(fascicoloNazionaleVO.getMsgErroreFascicolo()))
        {
          esitoAggiornamento = "["+fascicoloNazionaleVO.getCodErroreFascicolo()+"] "
           +fascicoloNazionaleVO.getMsgErroreFascicolo();
        }        
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSian", esitoAggiornamento);
      }      
    }
    else 
    {
      htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSian", SolmrConstants.DATA_AGGIORNAMENTO_SIAN_KO);
    }
    
    if(consistenzaVO.getDataAggiornamentoColtura() != null) 
    {
      htmpl.set("blkDichiarazioneConsistenza.dataAggiornamentoSianCT", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataAggiornamentoColtura()));
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaColtura()) && consistenzaVO.getFlagAnomaliaColtura().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSianCT", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_CT_OK, ""}), null);
      }
      else 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSianCT", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_CT_KO, ""}), null);              
      }
      
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaColtura()) && consistenzaVO.getFlagAnomaliaColtura().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianCT", SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_CT_OK);
      }
      else 
      {
        String esitoAggiornamento = SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_CT_KO;
        if(Validator.isNotEmpty(fascicoloNazionaleVO) 
          && Validator.isNotEmpty(fascicoloNazionaleVO.getCodErroreConsistenza())
          && Validator.isNotEmpty(fascicoloNazionaleVO.getMsgErroreConsistenza()))
        {
          esitoAggiornamento = "["+fascicoloNazionaleVO.getCodErroreConsistenza()+"] "
           +fascicoloNazionaleVO.getMsgErroreConsistenza();
        }        
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianCT", esitoAggiornamento);
      }      
    }
    else 
    {
      htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianCT", SolmrConstants.DATA_AGGIORNAMENTO_SIAN_CT_KO);
    }
    
    if(consistenzaVO.getDataAggiornamentoUV() != null) 
    {
      htmpl.set("blkDichiarazioneConsistenza.dataAggiornamentoSianUV", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataAggiornamentoUV()));
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaUV()) && consistenzaVO.getFlagAnomaliaUV().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSianUV", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_UV_OK, ""}), null);
      }
      else 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSianUV", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_UV_KO, ""}), null);              
      }
      
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaUV()) && consistenzaVO.getFlagAnomaliaUV().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianUV", SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_UV_OK);
      }
      else 
      {
        String esitoAggiornamento = SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_UV_KO;
        if(Validator.isNotEmpty(fascicoloNazionaleVO) 
          && Validator.isNotEmpty(fascicoloNazionaleVO.getCodErroreUV())
          && Validator.isNotEmpty(fascicoloNazionaleVO.getMsgErroreUV()))
        {
          esitoAggiornamento = "["+fascicoloNazionaleVO.getCodErroreUV()+"] "
           +fascicoloNazionaleVO.getMsgErroreUV();
        }        
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianUV", esitoAggiornamento);
      }      
    }
    else 
    {
      htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianUV", SolmrConstants.DATA_AGGIORNAMENTO_SIAN_UV_KO);
    }
    
    if(consistenzaVO.getDataAggiornamentoFabbricati() != null) 
    {
      htmpl.set("blkDichiarazioneConsistenza.dataAggiornamentoSianFab", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataAggiornamentoFabbricati()));
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaFabbricati()) && consistenzaVO.getFlagAnomaliaFabbricati().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSianFab", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_FABBRICATI_OK, ""}), null);
      }
      else 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSianFab", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_FABBRICATI_KO, ""}), null);              
      }
      
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaFabbricati()) && consistenzaVO.getFlagAnomaliaFabbricati().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianFab", SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_FABBRICATI_OK);
      }
      else 
      {
        String esitoAggiornamento = SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_FABBRICATI_KO;
        if(Validator.isNotEmpty(fascicoloNazionaleVO) 
          && Validator.isNotEmpty(fascicoloNazionaleVO.getCodErroreFabbricati())
          && Validator.isNotEmpty(fascicoloNazionaleVO.getMsgErroreFabbricati()))
        {
          esitoAggiornamento = "["+fascicoloNazionaleVO.getCodErroreFabbricati()+"] "
           +fascicoloNazionaleVO.getMsgErroreFabbricati();
        }        
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianFab", esitoAggiornamento);
      }      
    }
    else 
    {
      htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianFab", SolmrConstants.DATA_AGGIORNAMENTO_SIAN_FABBRICATI_KO);
    }
    
    if(consistenzaVO.getDataAggiornamentoCC() != null) 
    {
      htmpl.set("blkDichiarazioneConsistenza.dataAggiornamentoSianCC", new SimpleDateFormat(DateUtils.DATA_ORA).format(consistenzaVO.getDataAggiornamentoCC()));
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaCC()) && consistenzaVO.getFlagAnomaliaCC().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSianCC", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imok, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_CC_OK, ""}), null);
      }
      else 
      {
        htmpl.set("blkDichiarazioneConsistenza.aggiornamentoSianCC", MessageFormat.format(htmlStringKO, new Object[] { pathErrori + "/"+ imko, SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_CC_KO, ""}), null);              
      }
      
      
      if(Validator.isNotEmpty(consistenzaVO.getFlagAnomaliaCC()) && consistenzaVO.getFlagAnomaliaCC().equalsIgnoreCase(SolmrConstants.FLAG_N)) 
      {
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianCC", SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_CC_OK);
      }
      else 
      {
        String esitoAggiornamento = SolmrConstants.ESITO_AGGIORNAMENTO_SIAN_CC_KO;
        if(Validator.isNotEmpty(fascicoloNazionaleVO) 
          && Validator.isNotEmpty(fascicoloNazionaleVO.getCodErroreCC())
          && Validator.isNotEmpty(fascicoloNazionaleVO.getMsgErroreCC()))
        {
          esitoAggiornamento = "["+fascicoloNazionaleVO.getCodErroreCC()+"] "
           +fascicoloNazionaleVO.getMsgErroreCC();
        }        
        htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianCC", esitoAggiornamento);
      }      
    }
    else 
    {
      htmpl.set("blkDichiarazioneConsistenza.esitoAggiornamentoSianCC", SolmrConstants.DATA_AGGIORNAMENTO_SIAN_CC_KO);
    }
    
  	htmpl.set("blkDichiarazioneConsistenza.note",consistenzaVO.getNote());
    
    //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
    //al ruolo!
    ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
      "blkDichiarazioneConsistenza.utente", null,consistenzaVO.getUtenteAggiornamento(), 
      null, null);
      
    //htmpl.set("blkDichiarazioneConsistenza.utente",consistenzaVO.getUtenteAggiornamento());
    // Immagini relative alle segnalazioni
		String sourceImage = null;
		if(pathToFollow.equalsIgnoreCase("rupar")) {
			sourceImage = application.getInitParameter("erroriRupar");
		}
		else {
			sourceImage = application.getInitParameter("erroriSispie");
		}
		htmpl.set("srcBloccante", sourceImage+SolmrConstants.IMMAGINE_BLOCCANTE);
		htmpl.set("descImmagineBloccante", SolmrConstants.DESC_TITLE_BLOCCANTE);	
		htmpl.set("srcWarning", sourceImage+SolmrConstants.IMMAGINE_WARNING);
		htmpl.set("descImmagineWarning", SolmrConstants.DESC_TITLE_WARNING);	
		htmpl.set("srcOk", sourceImage+SolmrConstants.IMMAGINE_OK);
		htmpl.set("descImmagineOk", SolmrConstants.DESC_TITLE_POSITIVO);	
		if(Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO.getTipoAnomaliaBloccante())) {
			htmpl.set("checkedBloccante", "checked=\"checked\"");	
		}
		if(Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO.getTipoAnomaliaWarning())) {
			htmpl.set("checkedWarning", "checked=\"checked\"");	
		}
		if(Validator.isNotEmpty(errAnomaliaDicConsistenzaRicercaVO.getTipoAnomaliaOk())) {
			htmpl.set("checkedOk", "checked=\"checked\"");	
		}
		
		// Gruppo controllo
		if(elencoTipoGruppoControllo != null && elencoTipoGruppoControllo.length > 0) {
			for(int i = 0; i < elencoTipoGruppoControllo.length; i++) {
				CodeDescription code = (CodeDescription)elencoTipoGruppoControllo[i];
				htmpl.newBlock("blkElencoGruppoControllo");
				htmpl.set("blkElencoGruppoControllo.idGruppoControllo", code.getCode().toString());
				htmpl.set("blkElencoGruppoControllo.descrizione", code.getDescription());
				if(idGruppoControllo != null && idGruppoControllo.compareTo(Long.decode(code.getCode().toString())) == 0) {
					htmpl.set("blkElencoGruppoControllo.selected", "selected=\"selected\"");
				}
			}
		}
		
		// Controllo
		if(elencoTipoControllo != null && elencoTipoControllo.length > 0) {
			for(int i = 0; i < elencoTipoControllo.length; i++) {
				TipoControlloVO tipoControlloVO = (TipoControlloVO)elencoTipoControllo[i];
				htmpl.newBlock("blkElencoControllo");
				htmpl.set("blkElencoControllo.idControllo", tipoControlloVO.getIdControllo().toString());
				htmpl.set("blkElencoControllo.descrizione", tipoControlloVO.getDescrizione());
				if(idControllo != null && idControllo.compareTo(tipoControlloVO.getIdControllo()) == 0) {
					htmpl.set("blkElencoControllo.selected", "selected=\"selected\"");
				}
			}
		}
		
   	if(anomalie != null) 
   	{
    	int size = anomalie.length;
    	if(size > 0) 
    	{
      	htmpl.newBlock("blkElencoAnomalie");
      	ErrAnomaliaDicConsistenzaVO err = null;
      	String descAnomaliaOld = "";
      	for(int i = 0; i < size; i++) 
      	{
          err = (ErrAnomaliaDicConsistenzaVO)anomalie[i];
         	if(!descAnomaliaOld.equals(err.getDescGruppoControllo())) 
         	{
          	descAnomaliaOld = err.getDescGruppoControllo();
          	htmpl.newBlock("blkElencoAnomalie.blkTabAnomalia");
          	htmpl.set("blkElencoAnomalie.blkTabAnomalia.descrizioneAnomalia",descAnomaliaOld);
         	}
         	htmpl.newBlock("blkElencoAnomalie.blkTabAnomalia.blkAnomalia");
         	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.tipologia",err.getTipoAnomaliaErrore());
         	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.descrizione",err.getDescAnomaliaErrore());
         	if(err.getIdDichiarazioneSegnalazione() == null 
         	  || "".equals(err.getIdDichiarazioneSegnalazione())) 
         	{
          	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "ok.gif");
          	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.descImmagine",AnagErrors.CONTROLLO_OK);
         	}
         	else 
         	{
          	if(err.isBloccante()) 
          	{
            	// Reperisco l'immagine da ANDROMEDA
            	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Bloccante.gif");
            	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
           	}
           	else 
           	{
            	// Reperisco l'immagine da ANDROMEDA
            	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.immagine", percorsoErrori + "Warning.gif");
            	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_NON_BLOCCANTE"));
           	}
         	}
         	if(err.getIdDichiarazioneCorrezione() != null 
         	  && !"".equals(err.getIdDichiarazioneCorrezione())) 
         	{
          	htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.ris1",(String)SolmrConstants.get("VALORE_SI"));
          	if(elencoDocumentiAssociati != null && elencoDocumentiAssociati.size() > 0) 
          	{
           		if(elencoDocumentiAssociati.get(err.getIdDichiarazioneCorrezione()) != null) 
           		{
          			DocumentoVO documentoVO = (DocumentoVO)elencoDocumentiAssociati.get(err.getIdDichiarazioneCorrezione());
           			String descrizione = documentoVO.getTipoDocumentoVO().getDescrizione();
           			if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) 
           			{
           				descrizione += " Prot. "+StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo())+" del "+DateUtils.formatDate(documentoVO.getDataProtocollo());
           			}
							  htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.ris2",descrizione);
           		}
           		else 
           		{
           			htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.ris2",err.getRisoluzione());
           		}
           	}
           	else 
           	{
       				htmpl.set("blkElencoAnomalie.blkTabAnomalia.blkAnomalia.ris2",err.getRisoluzione());
       			}
         	}
       	}
     	}
     	// Se non sono state rilevate anomalie
      else 
      {
      	htmpl.newBlock("blkNoAnomalie");
      	htmpl.set("blkNoAnomalie.messaggioErrore", AnagErrors.ERRORE_NO_ANOMALIE_FOR_PARAMETERS);
      }
   	}
   	// Se non sono state rilevate anomalie
   	else 
   	{
   		htmpl.newBlock("blkNoAnomalie");
   		htmpl.set("blkNoAnomalie.messaggioErrore", AnagErrors.ERRORE_NO_ANOMALIE_FOR_PARAMETERS);
   	}
   	
   	if(Validator.isNotEmpty(vAllegatiDichiarazioneVO))
   	{
   	  htmpl.newBlock("blkElencoAllegati");
   	  for(int i=0;i<vAllegatiDichiarazioneVO.size();i++)
   	  {
   	    htmpl.newBlock("blkElencoAllegati.blkAllegato");
   	    AllegatoDichiarazioneVO allegatoDichiarazioneElVO = vAllegatiDichiarazioneVO.get(i);
   	    htmpl.set("blkElencoAllegati.blkAllegato.descTipoAlegato", allegatoDichiarazioneElVO.getDescTipoAllegato());
   	    htmpl.set("blkElencoAllegati.blkAllegato.dataAllegato", DateUtils.formatDateTimeNotNull(allegatoDichiarazioneElVO.getDataInizioValidita()));
   	    
   	    String descTipoFirma = "Allegato non firmato";   	    
   	    if(Validator.isNotEmpty(allegatoDichiarazioneElVO.getIdTipoFirma()))
		    {
		      descTipoFirma = allegatoDichiarazioneElVO.getDescTipoFirma();
		    }
		    htmpl.set("blkElencoAllegati.blkAllegato.descTipoFirma", descTipoFirma);
   	  
   	    Date dataFirma = null;
   	    if(Validator.isNotEmpty(allegatoDichiarazioneElVO.getIdTipoFirma()))
        {
          if(allegatoDichiarazioneElVO.getIdTipoFirma().compareTo(new Long(1)) == 0)
          {
            if(allegatoDichiarazioneElVO.getIdTipoAllegato().compareTo(new Long(SolmrConstants.VALIDAZIONE_ALLEGATO)) == 0)
            {
              dataFirma = consistenzaVO.getDataProtocollo();
            }
            else
            {
              dataFirma = allegatoDichiarazioneElVO.getDataInizioValidita();
            }
          }
          else if((allegatoDichiarazioneElVO.getIdTipoFirma().compareTo(new Long(2)) == 0)
            || (allegatoDichiarazioneElVO.getIdTipoFirma().compareTo(new Long(4)) == 0)) 
          {
            dataFirma = allegatoDichiarazioneElVO.getDataUltimoAggiornamento();
          }
        }  
   	    htmpl.set("blkElencoAllegati.blkAllegato.dataFirma", DateUtils.formatDateTimeNotNull(dataFirma));
   	  
   	  }
   	
   	}
   	
   	
   	
   	
 	}

 	HtmplUtil.setErrors(htmpl, errors, request, application);

 	SolmrLogger.debug(this, " - dichiarazioneConsistenzaDetView.jsp - FINE PAGINA");
%>
<%= htmpl.text()%>
