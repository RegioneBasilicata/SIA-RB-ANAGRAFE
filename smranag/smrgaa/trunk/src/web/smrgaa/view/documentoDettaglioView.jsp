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
<%@ page import="java.text.*"%>
<%@ page import="it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/documentoDettaglio.htm");
 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	DocumentoVO documentoVO = (DocumentoVO)request.getAttribute("documentoVO");
 	CodeDescription[] elencoTitoloPossesso = (CodeDescription[])request.getAttribute("elencoTitoloPossesso");
 	CodeDescription[] elencoCasoParticolare = (CodeDescription[])request.getAttribute("elencoCasoParticolare");
 	String legamiAttivi = (String)request.getAttribute("legamiAttivi");
  ContoCorrenteVO contoCorrenteVO = (ContoCorrenteVO)request.getAttribute("contoCorrenteVO");
  
  
  
  String htmlStringKOCC = "<img src=\"{0}\" "+
                              "title=\"{1}\" border=\"0\"></a>";
  String imkoCC = "ko.gif";
  String imokCC = "ok.gif";

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

 	// Setto l'area di provenienza
 	htmpl.set("pageFrom", "documentale");

 	// Se si è verificato un errore
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
 		htmpl.newBlock("blkDettaglioDocumentoKo");
 		htmpl.set("blkDettaglioDocumentoKo.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti popolo la sezione relativa al dettaglio
 	else 
  {
  
    if ("stampaDettaglioPdf".equals(request.getParameter("stampaDettaglioPdf")))
    {
      htmpl.set("stampaProtocollo", "stampaProtocolloImmediata()");
    }
    else
    {
      htmpl.set("stampaProtocollo", "");
    }
  
  
 		htmpl.set("idDocumento", documentoVO.getIdDocumento().toString());
 		htmpl.set("flagIstanzaRiesame", documentoVO.getFlagIstanzaRiesame());
 		htmpl.newBlock("blkDettaglioDocumentoOk");
 		htmpl.set("blkDettaglioDocumentoOk.descTipoTipologiaDocumento", documentoVO.getTipoTipologiaDocumento().getDescription());
 		htmpl.set("blkDettaglioDocumentoOk.descTipoCategoriaDocumento", documentoVO.getTipoCategoriaDocumentoVO().getDescrizione());
 		htmpl.set("blkDettaglioDocumentoOk.descTipoDocumento", documentoVO.getTipoDocumentoVO().getDescrizione());
 		htmpl.set("blkDettaglioDocumentoOk.dataInizioValidita", DateUtils.formatDate(documentoVO.getDataInizioValidita()));
 		if(Validator.isNotEmpty(documentoVO.getDataFineValidita())) {
   		htmpl.set("blkDettaglioDocumentoOk.dataFineValidita", DateUtils.formatDate(documentoVO.getDataFineValidita()));
 		}
 		if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) {
   		htmpl.set("blkDettaglioDocumentoOk.numeroProtocollo", StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
 		}
 		if(Validator.isNotEmpty(documentoVO.getDataProtocollo())) {
   		htmpl.set("blkDettaglioDocumentoOk.dataProtocollo", DateUtils.formatDate(documentoVO.getDataProtocollo()));
 		}
 		if(Validator.isNotEmpty(documentoVO.getNumeroProtocolloEsterno())) {
   		htmpl.set("blkDettaglioDocumentoOk.numeroProtocolloEsterno", documentoVO.getNumeroProtocolloEsterno());
 		}
    if(Validator.isNotEmpty(documentoVO.getDescCausaleModificaDocumento())) {
      htmpl.set("blkDettaglioDocumentoOk.descCausaleModifica", documentoVO.getDescCausaleModificaDocumento());
    }    
 		if(Validator.isNotEmpty(documentoVO.getNote())) {
   		htmpl.set("blkDettaglioDocumentoOk.note", documentoVO.getNote());
 		}
 		
 		if(documentoVO.getvAllegatoDocumento() != null)
	  {
	    for(int i=0;i<documentoVO.getvAllegatoDocumento().size();i++)
	    {
	      AllegatoDocumentoVO allegatoDocumentoVO = documentoVO.getvAllegatoDocumento().get(i);
	      htmpl.newBlock("blkDettaglioDocumentoOk.blkFileAllegato");
	      htmpl.set("blkDettaglioDocumentoOk.blkFileAllegato.idAllegato", 
	        ""+allegatoDocumentoVO.getIdAllegato());
	        
        String immagineStampa = "";
        if(Validator.isNotEmpty(allegatoDocumentoVO.getIdTipoFirma()))
        {
          if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 1)
          {
            immagineStampa = "firmaCarta";
          }
          else if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 2)
          {
            immagineStampa = "firmaTablet";
          }
          else if(allegatoDocumentoVO.getIdTipoFirma().longValue() == 3)
          {
            immagineStampa = "firmaElettronica";
          }
        }
        else
        {
          immagineStampa = "noFirma";
        }
        
        htmpl.set("blkDettaglioDocumentoOk.blkFileAllegato.immagineStampa", 
          immagineStampa);
	        
	        
	      htmpl.set("blkDettaglioDocumentoOk.blkFileAllegato.titleAllegato", 
	        allegatoDocumentoVO.getNomeLogico() +" (" +
	        allegatoDocumentoVO.getNomeFisico()+")");
	    }
	  }
    
    //Verifico se devono essere visualizzati l'ente e utente aggiornamento in base 
    //al ruolo!
    String dateUlt = "";
    if(documentoVO.getDataUltimoAggiornamento() !=null)
    {
      dateUlt = DateUtils.formatDate(documentoVO.getDataUltimoAggiornamento());
    }
    String utenteAgg = "";
    String enteAgg = "";
    if(documentoVO.getUtenteAggiornamento() !=null)
    {
      utenteAgg = documentoVO.getUtenteAggiornamento().getDenominazione();
      enteAgg = documentoVO.getUtenteAggiornamento().getDescrizioneEnteAppartenenza();
    }
    ProfileUtils.setFieldUltimaModificaByRuolo(ruoloUtenza,htmpl,
      "blkDettaglioDocumentoOk.ultimaModificaVw", dateUlt, utenteAgg, enteAgg, null);
    
    
   	
   	// Se il documento è di tipo "A" visualizzo i dati anagrafici
   	if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ANAGRAFICI)) 
    {
   		htmpl.newBlock("blkDettaglioDocumentoOk.blkDatiAnagrafici");
   		if(Validator.isNotEmpty(documentoVO.getNumeroDocumento())) {
     		htmpl.set("blkDettaglioDocumentoOk.blkDatiAnagrafici.numeroDocumento", documentoVO.getNumeroDocumento());
   		}
   		if(Validator.isNotEmpty(documentoVO.getEnteRilascioDocumento())) {
     		htmpl.set("blkDettaglioDocumentoOk.blkDatiAnagrafici.enteRilascioDocumento", documentoVO.getEnteRilascioDocumento());
   		}
   	}
		// Se il documento è di tipo "Z" visualizzo i dati zootecnici
   	if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ZOOTECNICI)) 
    {
   		htmpl.newBlock("blkDettaglioDocumentoOk.blkDatiZootecnici");
   		if(Validator.isNotEmpty(documentoVO.getCuaaSoccidario())) {
     		htmpl.set("blkDettaglioDocumentoOk.blkDatiZootecnici.cuaaSoccidario", documentoVO.getCuaaSoccidario());
   		}
   	}
   	// Se il documento è di tipo "T" visualizzo i dati territoriali
   	else if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
        || documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
    {
     		// Sezione proprietari
     		Vector<DocumentoProprietarioVO> elencoProprietari = documentoVO.getElencoProprietari();
     		if(elencoProprietari != null && elencoProprietari.size() > 0) {
    	 		htmpl.newBlock("blkDettaglioDocumentoOk.blkProprietariDocumento");
     			for(int i = 0; i < elencoProprietari.size(); i++) {
         		htmpl.newBlock("blkDettaglioDocumentoOk.blkProprietariDocumento.blkElencoProprietariDocumento");
         		DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO)elencoProprietari.elementAt(i);
         		htmpl.set("blkDettaglioDocumentoOk.blkProprietariDocumento.blkElencoProprietariDocumento.cuaa", documentoProprietarioVO.getCuaa());
         		htmpl.set("blkDettaglioDocumentoOk.blkProprietariDocumento.blkElencoProprietariDocumento.denominazione", documentoProprietarioVO.getDenominazione());
     			}
     		}
     		else {
     			htmpl.newBlock("blkDettaglioDocumentoOk.blkNoProprietariDocumento");
              htmpl.set("blkDettaglioDocumentoOk.blkNoProprietariDocumento.messaggioErrore", AnagErrors.ERRORE_NO_PROPRIETARI_FOR_DOCUMENTO);
     		}       			
     		// Sezione particelle
     		Vector<?> elencoParticelle = documentoVO.getElencoParticelle();
     		htmpl.newBlock("blkDettaglioDocumentoOk.blkRicercaParticelle");
     		if(Validator.isNotEmpty(legamiAttivi)) {
     			htmpl.set("blkDettaglioDocumentoOk.blkRicercaParticelle.checkedLegamiAttivi", "checked=\"checked\"");
     		}
     		if(elencoParticelle == null || elencoParticelle.size() == 0) 
        {
          //devo far vedere questo errore solo se il documento non è di tipo correttiva terreni
          if (!documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
          {
         		htmpl.newBlock("blkDettaglioDocumentoOk.blkNoParticelleDocumento");
         		htmpl.set("blkDettaglioDocumentoOk.blkNoParticelleDocumento.messaggioErrore", AnagErrors.ERRORE_NO_PARTICELLE_FOR_DOCUMENTO_ASS);
          }
     		}
     		else 
        {
       		htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento");
          
          
          //Istanza riesame
          if("TC".equalsIgnoreCase(documentoVO.getTipoCategoriaDocumentoVO().getTipoIdentificativo())
            && "518".equalsIgnoreCase(documentoVO.getTipoCategoriaDocumentoVO().getIdentificativo()))
          {
            htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkTabPrioritaLaborazione");
          }          
          
       		if(!Validator.isNotEmpty(legamiAttivi)) {
       			htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLabelScadenza");
       		}
       		for(int i = 0; i < elencoParticelle.size(); i++) 
          {
         		StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle.elementAt(i);
            
            
            
       			htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
       			htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
       			if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) {
         				htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.sezione", storicoParticellaVO.getSezione());
       			}
       			htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.foglio", storicoParticellaVO.getFoglio());
       			if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) {
         				htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.particella", storicoParticellaVO.getParticella());
       			}
       			if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) {
       				htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.subalterno", storicoParticellaVO.getSubalterno());
       			}
       			htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
       			htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.idTitoloPossesso", storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso().toString());
            htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.percentualePossesso", ""+storicoParticellaVO.getElencoConduzioni()[0].getPercentualePossesso());
       			if(Validator.isNotEmpty(storicoParticellaVO.getIdCasoParticolare())) {
       				htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.idCasoParticolare", storicoParticellaVO.getIdCasoParticolare().toString());
       			}
            
            
            if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni())
                && Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
            {            
              htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.noteDocConduzione", storicoParticellaVO.getElencoConduzioni()[0]
                .getElencoDocumentoConduzione()[0].getNote());
            }
            
            
            if("TC".equalsIgnoreCase(documentoVO.getTipoCategoriaDocumentoVO().getTipoIdentificativo())
              && "518".equalsIgnoreCase(documentoVO.getTipoCategoriaDocumentoVO().getIdentificativo()))
            {
              htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.blkElencPrioritaLavorazione");
            
            
              if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni())
                      && Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
              { 
                if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria())
                  && "S".equalsIgnoreCase(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0].getLavorazionePrioritaria()))
                {              
                  htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.blkElencPrioritaLavorazione.checkedLavorazionePrioritaria", "checked=\"cheched\"",null);
                }                  
              }
            }
            
            
            
            
            
            
            
            
       			if(!Validator.isNotEmpty(legamiAttivi)) 
            {
       				DocumentoConduzioneVO documentoConduzioneVO = storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()[0];
       				htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.blkDataScadenza");
       				if(documentoConduzioneVO.getMaxDataFineValidita() != null) 
              {
       					htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkElencoParticelleDocumento.blkDataScadenza.dataScadenza", DateUtils.formatDate(documentoConduzioneVO.getMaxDataFineValidita()));
       				}
       			}
       		}
          htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende");
			    // LEGENDA TITOLO POSSESSO
     			for(int i = 0; i < elencoTitoloPossesso.length; i++) 
          {
       			htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaConduzione");
       			CodeDescription code = (CodeDescription)elencoTitoloPossesso[i];
       			htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaConduzione.idTitoloPossesso", String.valueOf(code.getCode())+" - ");         			
       			if(i == (elencoTitoloPossesso.length - 1)) {
       				htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaConduzione.descTitoloPossesso", code.getDescription());
       			}
       			else {
       				htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaConduzione.descTitoloPossesso", code.getDescription()+", ");
       			}
     			}
			    // LEGENDA CASO PARTICOLARE
     			for(int i = 0; i < elencoCasoParticolare.length; i++) 
          {
       			htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaCasiParticolari");
       			CodeDescription code = (CodeDescription)elencoCasoParticolare[i];
       			htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaCasiParticolari.idCasoParticolare", String.valueOf(code.getCode())+" - ");         			
       			if(i == (elencoCasoParticolare.length - 1)) {
       				htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription());
       			}
       			else {
       				htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription()+", ");
       			}
     			}
     		}
        if (documentoVO.getParticelleAssociate()!=null)
        {
          int size=documentoVO.getParticelleAssociate().size();
          if (size>0)
          {
            htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkTabellaParticelleDaAssociate");
            for(int i=0;i<size;i++)
            {
              ParticellaAssVO particella=(ParticellaAssVO)documentoVO.getParticelleAssociate().get(i);
              String blk="blkDettaglioDocumentoOk.blkParticelleDocumento.blkTabellaParticelleDaAssociate.blkTabellaParticelleDaAssociareBody";
              htmpl.newBlock(blk);
              
              htmpl.set(blk+".descComuneParticella", particella.getDescComuneParticella());
              htmpl.set(blk+".siglaProvinciaParticella", particella.getSiglaProvinciaParticella());
             
              if(Validator.isNotEmpty(particella.getSezione()))
                htmpl.set(blk+".sezione", particella.getSezione().toUpperCase());
              htmpl.set(blk+".foglio", particella.getFoglio().toString());
              if(Validator.isNotEmpty(particella.getParticella()))
                htmpl.set(blk+".particella", particella.getParticella().toString());
              if(Validator.isNotEmpty(particella.getSubalterno()))
                htmpl.set(blk+".subalterno", particella.getSubalterno());
              htmpl.set(blk+".supCatastale", StringUtils.parseSuperficieField(particella.getSupCatastale()));
              htmpl.set(blk+".descEvento", particella.getDescrizioneEvento());
              htmpl.set(blk+".idConduzione", particella.getIdConduzione());              
              htmpl.set(blk+".supConduzione", StringUtils.parseSuperficieField(particella.getSupCondotta()));              
              htmpl.set(blk+".supUtilizzata", particella.getSupUtilizzata());
              htmpl.set(blk+".descUsoPrimario", particella.getDescUsoPrimario());
              if (particella.getIdCasoParticolare()!=null)
                htmpl.set(blk+".idCasoParticolare", particella.getIdCasoParticolare().toString());
            }
            
            htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende");
            // LEGENDA TITOLO POSSESSO
            for(int i = 0; i < elencoTitoloPossesso.length; i++) {
              htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaConduzione");
              CodeDescription code = (CodeDescription)elencoTitoloPossesso[i];
              htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaConduzione.idTitoloPossesso", String.valueOf(code.getCode())+" - ");               
              if(i == (elencoTitoloPossesso.length - 1)) {
                htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaConduzione.descTitoloPossesso", code.getDescription());
              }
              else {
                htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaConduzione.descTitoloPossesso", code.getDescription()+", ");
              }
            }
            // LEGENDA CASO PARTICOLARE
            for(int i = 0; i < elencoCasoParticolare.length; i++) {
              htmpl.newBlock("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaCasiParticolari");
              CodeDescription code = (CodeDescription)elencoCasoParticolare[i];
              htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaCasiParticolari.idCasoParticolare", String.valueOf(code.getCode())+" - ");               
              if(i == (elencoCasoParticolare.length - 1)) {
                htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription());
              }
              else {
                htmpl.set("blkDettaglioDocumentoOk.blkParticelleDocumento.blkLegende.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription()+", ");
              }
            }
            
            
          }
          /*else //Eliminato per richiesta di Terry
          {
            htmpl.newBlock("blkDettaglioDocumentoOk.blkNoParticelleDocumento");
            htmpl.set("blkDettaglioDocumentoOk.blkNoParticelleDocumento.messaggioErrore", AnagErrors.ERRORE_NO_PARTICELLE_FOR_DOCUMENTO_DA_ASS);
          }*/
        }
        /*else
        {
          htmpl.newBlock("blkDettaglioDocumentoOk.blkNoParticelleDocumento");
          htmpl.set("blkDettaglioDocumentoOk.blkNoParticelleDocumento.messaggioErrore", AnagErrors.ERRORE_NO_PARTICELLE_FOR_DOCUMENTO_DA_ASS);
        }*/
     	}
      
      
      // Se il documento è di tipo "A" visualizzo i dati anagrafici
      if(documentoVO.getTipoDocumentoVO().getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CONTI_CORRENTI)
        && (contoCorrenteVO != null)) 
      {
        
        
        
        String blkCC = "blkDettaglioDocumentoOk.blkContiCorrenti";
        htmpl.newBlock(blkCC);         
          
        htmpl.set(blkCC+".banca",contoCorrenteVO.getDenominazioneBanca());
        htmpl.set(blkCC+".filiale",contoCorrenteVO.getDenominazioneSportello());
        htmpl.set(blkCC+".codpaese",contoCorrenteVO.getCodPaese());
        htmpl.set(blkCC+".cctrl",contoCorrenteVO.getCifraCtrl());
        htmpl.set(blkCC+".cin",contoCorrenteVO.getCin());
        htmpl.set(blkCC+".abi",contoCorrenteVO.getAbi());
        htmpl.set(blkCC+".cab",contoCorrenteVO.getCab());
        htmpl.set(blkCC+".numeroconto",contoCorrenteVO.getNumeroContoCorrente());
        htmpl.set(blkCC+".intestatario",contoCorrenteVO.getIntestazione());
        String dataInizioValidita="";
        try 
        {
          dataInizioValidita=DateUtils.formatDate(contoCorrenteVO.getDataInizioValiditaContoCorrente());
        }
        catch(Exception e) {
          // Data inizio validità vuota
        }
        htmpl.set(blkCC+".dataInizioValidita",dataInizioValidita);
        
        Date dataEstinzione=contoCorrenteVO.getDataEstinzione();
        if (dataEstinzione!=null)
        {
          htmpl.set(blkCC+".dataEstinzione",DateUtils.formatDate(dataEstinzione));
        }
        else if(contoCorrenteVO.getDataFineValiditaContoCorrente() != null)
        {
          htmpl.set(blkCC+".dataEstinzione",DateUtils.formatDate(contoCorrenteVO.getDataFineValiditaContoCorrente()));
        }
              
        // Icona relativa alla validazione del conto corrente
        if(Validator.isNotEmpty(contoCorrenteVO.getflagValidato()) 
          && contoCorrenteVO.getflagValidato().equalsIgnoreCase("S"))
        {
          htmpl.set(blkCC+".validazioneCC", MessageFormat.format(htmlStringKOCC, new Object[] { pathErrori + "/"+ imokCC, SolmrConstants.MSG_VALIDAZIONE_CC_OK, ""}), null);
        }
        else if(Validator.isNotEmpty(contoCorrenteVO.getflagValidato()) 
          && contoCorrenteVO.getflagValidato().equalsIgnoreCase("N"))
        {
          htmpl.set(blkCC+".validazioneCC", MessageFormat.format(htmlStringKOCC, new Object[] { pathErrori + "/"+ imkoCC, SolmrConstants.MSG_VALIDAZIONE_CC_KO, ""}), null);              
        }
        else
        {
          htmpl.set(blkCC+".validazioneCC", "");
        }
        
        
        
        
        
      }
      
      
   	}
%>
<%= htmpl.text()%>
