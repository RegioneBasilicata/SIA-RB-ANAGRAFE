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
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.TipoControlloVO"%>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="java.math.BigDecimal" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.FaseRiesameDocumentoVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>



<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/documentoInserisci.htm");
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	Vector elencoTipiTipologiaDocumento = (Vector)request.getAttribute("elencoTipiTipologiaDocumento");
 	Long idTipologiaDocumento = (Long)request.getAttribute("idTipologiaDocumento");
 	TipoCategoriaDocumentoVO[] elencoTipiCategoriaDocumento = (TipoCategoriaDocumentoVO[])request.getAttribute("elencoTipiCategoriaDocumento");
 	Long idCategoriaDocumento = (Long)request.getAttribute("idCategoriaDocumento");
 	TipoDocumentoVO[] elencoTipiDocumento = (TipoDocumentoVO[])request.getAttribute("elencoTipiDocumento");
 	Long idTipoDocumento = (Long)request.getAttribute("idTipoDocumento");
 	TipoDocumentoVO tipoDocumentoVO = (TipoDocumentoVO)request.getAttribute("tipoDocumentoVO");
 	DocumentoVO documentoVO = (DocumentoVO)session.getAttribute("documentoVO");
 	Vector elencoProprietari = (Vector)session.getAttribute("elencoProprietari");
 	CodeDescription[] elencoTitoloPossesso = (CodeDescription[])request.getAttribute("elencoTitoloPossesso");
 	CodeDescription[] elencoCasoParticolare = (CodeDescription[])request.getAttribute("elencoCasoParticolare");
  TipoControlloVO[] elencoTipiControllo = (TipoControlloVO[])request.getAttribute("elencoTipiControllo");
 	StoricoParticellaVO[] elencoParticelle = (StoricoParticellaVO[])session.getAttribute("elencoParticelle");
	Vector elencoErrori = (Vector)request.getAttribute("elencoErrori");
  Vector<ValidationErrors> elencoErroriNote = (Vector<ValidationErrors>)request.getAttribute("elencoErroriNote");
  String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
  String idAnomalie = (String)request.getParameter("idAnomalie");
  Vector<ContoCorrenteVO> conti=(Vector<ContoCorrenteVO>)request.getAttribute("conti");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  //Variabile che mi identifica se sono entrato la prima volta!!!
  String regimeDocumentoInserisci = request.getParameter("regimeDocumentoInserisci");
  FaseRiesameDocumentoVO faseRiesameDocumentoVO = (FaseRiesameDocumentoVO)request.getAttribute("faseRiesameDocumentoVO");
 		
	// Creazione delle variabili necessarie alla gestione degli errori relativi
	// ai proprietari del documento
	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
		                  "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
	StringProcessor jssp = new JavaScriptStringProcessor();
  
  
  // Creazione delle variabili necessarie alla gestione delle anomalie
  // dei conti correnti
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

 	// COMBO TIPO TIPOLOGIA DOCUMENTO
 	Iterator iteraTipiTipologiaDocumento = elencoTipiTipologiaDocumento.iterator();
 	while(iteraTipiTipologiaDocumento.hasNext()) 
  {
 		CodeDescription code = (CodeDescription)iteraTipiTipologiaDocumento.next();
 		htmpl.newBlock("blkElencoTipiTipologiaDocumento");
 		if(idTipologiaDocumento != null && idTipologiaDocumento.toString().equalsIgnoreCase(code.getCode().toString())) {
     		htmpl.set("blkElencoTipiTipologiaDocumento.selected", "selected=\"selected\"", null);
   	}
 		htmpl.set("blkElencoTipiTipologiaDocumento.codice", code.getCode().toString());
 		htmpl.set("blkElencoTipiTipologiaDocumento.descrizione", code.getDescription());
 	}

 	// COMBO TIPO DOCUMENTO
  TipoCategoriaDocumentoVO tipoCategoriaDocumentoVOSelezionato = null;
 	if(elencoTipiCategoriaDocumento != null && elencoTipiCategoriaDocumento.length > 0) 
  {
 		for(int i = 0; i < elencoTipiCategoriaDocumento.length; i++) 
    {
 			TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = (TipoCategoriaDocumentoVO)elencoTipiCategoriaDocumento[i];
   		htmpl.newBlock("blkElencoTipiCategoriaDocumento");
   		if(idCategoriaDocumento != null 
        && tipoCategoriaDocumentoVO.getIdCategoriaDocumento().compareTo(idCategoriaDocumento) == 0) 
      {
      	htmpl.set("blkElencoTipiCategoriaDocumento.selected", "selected=\"selected\"", null);
        tipoCategoriaDocumentoVOSelezionato = tipoCategoriaDocumentoVO;
     	}
   		htmpl.set("blkElencoTipiCategoriaDocumento.codice", tipoCategoriaDocumentoVO.getIdCategoriaDocumento().toString());
   		htmpl.set("blkElencoTipiCategoriaDocumento.descrizione", tipoCategoriaDocumentoVO.getDescrizione());
   	}
 	}
 	
	// COMBO DESCRIZIONE DOCUMENTO
 	if(elencoTipiDocumento != null && elencoTipiDocumento.length > 0) 
  {
 		for(int i = 0; i < elencoTipiDocumento.length; i++) 
    {
 			TipoDocumentoVO tipoDocumentoElencoVO = (TipoDocumentoVO)elencoTipiDocumento[i];
   		htmpl.newBlock("blkElencoTipiDocumento");
   		if(idTipoDocumento != null 
        && idTipoDocumento.compareTo(tipoDocumentoElencoVO.getIdDocumento()) == 0) 
      {
   			htmpl.set("blkElencoTipiDocumento.selected", "selected=\"selected\"", null);     			
   		}
   		htmpl.set("blkElencoTipiDocumento.codice", tipoDocumentoElencoVO.getIdDocumento().toString());
   		htmpl.set("blkElencoTipiDocumento.descrizione", tipoDocumentoElencoVO.getDescrizione());
   	}
 	}

 	// SEZIONI RELATIVE AI DATI SPECIFICI DEL DOCUMENTO(vincolata ai valori inseriti nelle prima due combo gestite sopra)
 	if(tipoDocumentoVO != null 
    && Validator.isNotEmpty(tipoDocumentoVO.getFlagAnagTerr())) 
  {
 		htmpl.newBlock("blkDatiDocumentoComuni");
 		if(Validator.isNotEmpty(request.getParameter("dataInizioValidita"))) {
   		htmpl.set("blkDatiDocumentoComuni.dataInizioValidita", request.getParameter("dataInizioValidita"));
 		}
 		else if(Validator.isNotEmpty(request.getAttribute("dataInizioValidita"))) {
   		htmpl.set("blkDatiDocumentoComuni.dataInizioValidita", (String)request.getAttribute("dataInizioValidita"));
 		}
 		if(Validator.isNotEmpty(request.getParameter("dataFineValidita"))) {
   		htmpl.set("blkDatiDocumentoComuni.dataFineValidita", request.getParameter("dataFineValidita"));
 		}
 		else if(Validator.isNotEmpty(request.getAttribute("dataFineValidita"))) {
   		htmpl.set("blkDatiDocumentoComuni.dataFineValidita", (String)request.getAttribute("dataFineValidita"));
 		}
 		// Se FLAG_OBBLIGO_PROTOCOLLO == S disabilito il check relativo al protocollo
 		if(tipoDocumentoVO.getFlagObbligoProtocollo().equalsIgnoreCase(SolmrConstants.FLAG_S)) 
    {
   		htmpl.set("blkDatiDocumentoComuni.disabled", "disabled=\"disabled\"");
   		htmpl.set("blkDatiDocumentoComuni.checked", "checked=\"checked\"");
 		}
 		else 
    {
   		if(Validator.isNotEmpty(request.getParameter("protocolla")) 
        || Validator.isNotEmpty((String)request.getAttribute("protocolla"))) 
      {
     		htmpl.set("blkDatiDocumentoComuni.checked", "checked=\"checked\"");
   		}
 		}
 		if(Validator.isNotEmpty(request.getParameter("numeroProtocolloEsterno"))) {
 			htmpl.set("blkDatiDocumentoComuni.numeroProtocolloEsterno", request.getParameter("numeroProtocolloEsterno"));
 		}
 		else if(Validator.isNotEmpty(request.getAttribute("numeroProtocolloEsterno"))) {
 			htmpl.set("blkDatiDocumentoComuni.numeroProtocolloEsterno", (String)request.getAttribute("numeroProtocolloEsterno"));
 		}
 		if(Validator.isNotEmpty(request.getParameter("note"))) {
 			htmpl.set("blkDatiDocumentoComuni.note", request.getParameter("note"));
 		}
 		else if(Validator.isNotEmpty(request.getAttribute("note"))) {
 			htmpl.set("blkDatiDocumentoComuni.note", (String)request.getAttribute("note"));
 		}

 		// Se FLAG_ANAG_TERR == A valorizzo i dati comuni e anagrafici
 		if(tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ANAGRAFICI)) 
    {
   		htmpl.newBlock("blkDatiDocumentoAnagrafici");
   		if(errors != null && errors.size() > 0) 
      {
     		HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, "blkDatiDocumentoComuni");
     		HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, "blkDatiDocumentoAnagrafici");
   		}
      
      // NUMERO DOCUMENTO
      if(Validator.isNotEmpty(documentoVO.getNumeroDocumento())) {
          htmpl.set("blkDatiDocumentoAnagrafici.numeroDocumento", documentoVO.getNumeroDocumento());
      }
      else if(Validator.isNotEmpty(request.getParameter("numeroDocumento"))) {
          htmpl.set("blkDatiDocumentoAnagrafici.numeroDocumento", request.getParameter("numeroDocumento"));
      }
      // ENTE RILASCIO DOCUMENTO
      if(Validator.isNotEmpty(documentoVO.getEnteRilascioDocumento())) {
          htmpl.set("blkDatiDocumentoAnagrafici.enteRilascioDocumento", documentoVO.getEnteRilascioDocumento());
      }
      else if(Validator.isNotEmpty(request.getParameter("enteRilascioDocumento"))) {
          htmpl.set("blkDatiDocumentoAnagrafici.enteRilascioDocumento", request.getParameter("enteRilascioDocumento"));
      }
      
   		//HtmplUtil.setValuesSpecifiedBlock(htmpl, request, "blkDatiDocumentoAnagrafici", null);
 		}
		// Se FLAG_ANAG_TERR == Z valorizzo e visualizzo i dati zootecnici
		else if(tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_ZOOTECNICI)) 
    {
			htmpl.newBlock("blkDatiDocumentoZootecnici");
			if(errors != null && errors.size() > 0) 
      {
       	HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, "blkDatiDocumentoComuni");
       	HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, "blkDatiDocumentoZootecnici");
     	}
			if(documentoVO != null && Validator.isNotEmpty(documentoVO.getCuaaSoccidario())) {
				htmpl.set("blkDatiDocumentoZootecnici.cuaaSoccidario", documentoVO.getCuaaSoccidario());
			}
		}
 		// Se FLAG_ANAG_TERR == T valorizzo i dati comuni e territoriali 
    // Se FLAG_ANAG_TERR == C valorizzo i dati comuni e territoriali 
 		else if(tipoDocumentoVO.getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
      || tipoDocumentoVO.getFlagAnagTerr()
          .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
    {
   		htmpl.newBlock("blkDatiDocumentoTerritoriali");
   		htmpl.newBlock("blkDatiDocumentoTerritoriali.blkSezioneProprietari");
   		
   		String extraSistema = "";
   		if(Validator.isNotEmpty(faseRiesameDocumentoVO)
   		  && Validator.isNotEmpty(faseRiesameDocumentoVO.getExtraSistema()))
   		{
   		  extraSistema = faseRiesameDocumentoVO.getExtraSistema();
   		}
   		String bloccoParticelle = "blkDatiDocumentoTerritoriali.blkSezioneParticelle";
   		
   		if("C".equalsIgnoreCase(extraSistema))
   		{
   		  bloccoParticelle += ".blkValoreC";
   		}
   		else if("F".equalsIgnoreCase(extraSistema))
      {
        bloccoParticelle += ".blkExtraSistema";
      }
   		else
   		{
   		  bloccoParticelle += ".blkPrimoBlocco";
   		}
   		
   		htmpl.newBlock(bloccoParticelle);
   		
   		
   		
   		String bloccoDichiarazioneConsistenza =  bloccoParticelle+".blkPianoRiferimento";
      PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
      String noCessata = null;
      if (anagAziendaVO.getDataCessazione() == null) 
        noCessata = SolmrConstants.PIANO_LAVORAZIONE_NORMALE;
      pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
        anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, noCessata,
        null);
   		
   		
   		
   		

   		// Se ci sono errori li visualizzo
   		if(errors != null && errors.size() > 0) 
      {
   			HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, "blkDatiDocumentoComuni");
   			HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, "blkDatiDocumentoTerritoriali.blkSezioneProprietari");
   			HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, bloccoParticelle);
   			if(Validator.isNotEmpty(request.getParameter("codiceFiscale"))) {
     			htmpl.set("blkDatiDocumentoTerritoriali.blkSezioneProprietari.codiceFiscale", request.getParameter("codiceFiscale"));
   			}
   			if(Validator.isNotEmpty(request.getParameter("denominazione"))) {
     			htmpl.set("blkDatiDocumentoTerritoriali.blkSezioneProprietari.denominazione", request.getParameter("denominazione"));
   			}
   			// Sezione particelle da associare al documento
   			if(Validator.isNotEmpty(request.getParameter("siglaProvinciaParticella"))) {
     			htmpl.set(bloccoParticelle+".siglaProvinciaParticella", request.getParameter("siglaProvinciaParticella"));
   			}
   			if(Validator.isNotEmpty(request.getParameter("comune"))) {
     			htmpl.set(bloccoParticelle+".comune", request.getParameter("comune"));
   			}
   		}
   		// Sezione tabella elenco proprietari
   		if(elencoProprietari != null && elencoProprietari.size() > 0) 
      {
     	  htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaProprietari");
     		htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaProprietari.blkEtichettaProprietari");
     		Iterator iteraProprietari = elencoProprietari.iterator();
     		int i = 0;
     		while(iteraProprietari.hasNext()) 
        {
     			DocumentoProprietarioVO documentoProprietarioVO = (DocumentoProprietarioVO)iteraProprietari.next();
     			htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaProprietari.blkElencoProprietari");
     			htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaProprietari.blkElencoProprietari.countProprietario", String.valueOf(i));
     			htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaProprietari.blkElencoProprietari.cuaa", documentoProprietarioVO.getCuaa());
     			htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaProprietari.blkElencoProprietari.denominazione", documentoProprietarioVO.getDenominazione());
				  // Verifico se ci sono stati errori di incongruenza con anagrafe tributaria
       		if(elencoErrori != null && elencoErrori.size() > 0) 
          {
     				String errore = (String)elencoErrori.elementAt(i);
		       	if(errore != null) 
            {
		        	htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaProprietari.blkElencoProprietari.err_cuaa",
		                      MessageFormat.format(htmlStringKO,
		                      new Object[] {
		                      pathErrori + "/"+ imko,
		                      "'"+jssp.process(errore)+"'",
		                      errore}),
		                      null);
			      }
       		}
       		i++;
     		}
   		}
   		// Combo titolo possesso
   		if(elencoTitoloPossesso != null && elencoTitoloPossesso.length > 0) 
      {
   			for(int i = 0; i < elencoTitoloPossesso.length; i++) 
        {
     			htmpl.newBlock(bloccoParticelle+".blkElencoTitoloPossesso");
     			CodeDescription code = (CodeDescription)elencoTitoloPossesso[i];
     			if(!Validator.isNotEmpty(request.getParameter("provenienza"))) 
          {
       			if(Validator.isNotEmpty(request.getParameter("idTitoloPossesso"))) 
            {
       				if(code.getCode().toString().equalsIgnoreCase(request.getParameter("idTitoloPossesso"))) 
              {
         				htmpl.set(bloccoParticelle+".blkElencoTitoloPossesso.selected", "selected=\"selected\"");
       				}
       			}
       			else if(Validator.isNotEmpty(request.getAttribute("idTitoloPossesso"))) 
            {
       				if(code.getCode().toString()
                .equalsIgnoreCase((String)request.getAttribute("idTitoloPossesso"))) 
              {
         				htmpl.set(bloccoParticelle+".blkElencoTitoloPossesso.selected", "selected=\"selected\"");
       				}
       			}
     			}
     			htmpl.set(bloccoParticelle+".blkElencoTitoloPossesso.code", code.getCode().toString());
     			htmpl.set(bloccoParticelle+".blkElencoTitoloPossesso.description", code.getCode().toString());
   			}
   		}
			// Combo caso particolare
   		if(elencoCasoParticolare != null && elencoCasoParticolare.length > 0) 
      {
   			for(int i = 0; i < elencoCasoParticolare.length; i++) 
        {
     			htmpl.newBlock(bloccoParticelle+".blkElencoTipoCasoParticolare");
     			CodeDescription code = (CodeDescription)elencoCasoParticolare[i];
     			if(!Validator.isNotEmpty(request.getParameter("provenienza"))) 
          {
     				if(Validator.isNotEmpty(request.getParameter("idCasoParticolare"))) 
            {
     					if(code.getCode().toString().equalsIgnoreCase(request.getParameter("idCasoParticolare"))) 
              {
       					htmpl.set(bloccoParticelle+".blkElencoTipoCasoParticolare.selected", "selected=\"selected\"");
     					}
     				}
     				else if(Validator.isNotEmpty(request.getAttribute("idCasoParticolare"))) 
            {
     					if(code.getCode().toString()
                .equalsIgnoreCase((String)request.getAttribute("idCasoParticolare"))) 
              {
       					htmpl.set(bloccoParticelle+".blkElencoTipoCasoParticolare.selected", "selected=\"selected\"");
     					}
     				}
   				}
     			htmpl.set(bloccoParticelle+".blkElencoTipoCasoParticolare.code", code.getCode().toString());
     			htmpl.set(bloccoParticelle+".blkElencoTipoCasoParticolare.description", code.getCode().toString());
   			}
   		}
      
      // Combo anomalie
      if(elencoTipiControllo != null && elencoTipiControllo.length > 0) 
      {
        for(int i = 0; i < elencoTipiControllo.length; i++) 
        {
          htmpl.newBlock(bloccoParticelle+".blkAnomalie");
          TipoControlloVO tipoControlloVO = (TipoControlloVO)elencoTipiControllo[i];
          htmpl.set(bloccoParticelle+".blkAnomalie.idAnomalie", tipoControlloVO.getIdControllo().toString());
          htmpl.set(bloccoParticelle+".blkAnomalie.descrizione", tipoControlloVO.getDescrizione());
          if(!Validator.isNotEmpty(request.getParameter("provenienza"))) 
          {
            if(Validator.isNotEmpty(idAnomalie)) 
            {
              Long idAnomalieLg = new Long(idAnomalie);
              if(idAnomalieLg.compareTo(tipoControlloVO.getIdControllo()) == 0) 
              {
                htmpl.set(bloccoParticelle+".blkAnomalie.selected", "selected=\"selected\"");
              }
            }
          }
        }
      }
      
      if(tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI))
      {
        htmpl.newBlock(bloccoParticelle+".blkAssociaParticelle");
        
        String numRigheAssocia = "2";
        
        if(Validator.isNotEmpty(documentoVO)
          && (documentoVO.getFaseIstanzaRiesame() == SolmrConstants.FASE_IST_RIESAM_CONTRO)
          && !"C".equalsIgnoreCase(extraSistema))
        {
          numRigheAssocia = "3";
          htmpl.newBlock("blkDatiDocumentoTerritoriali.blkSezioneParticelle.blkContradditorio");          
        }
        
        htmpl.set(bloccoParticelle+".blkAssociaParticelle.numRigheAssocia", numRigheAssocia);
      
     		// Tabella delle particelle
     		if(elencoParticelle != null && elencoParticelle.length > 0) 
        {
     			htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaParticelle");
          
          
          //Istanza riesame
          if("TC".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getTipoIdentificativo())
            && "518".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getIdentificativo()))
          {
            htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaParticelle.blkTabPrioritaLaborazione");
          }
          
     			for(int i = 0; i < elencoParticelle.length; i++) 
          {
       			htmpl.newBlock("blkDatiDocumentoTerritoriali.blkElencoParticelle");
       			StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle[i];
       			htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.idConduzioneParticella", storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella().toString());
       			htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
       			htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getSiglaProv());
       			if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) 
            {
         			htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.sezione", storicoParticellaVO.getSezione());
       			}
       			htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.foglio", storicoParticellaVO.getFoglio());
       			if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) 
            {
         			htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.particella", storicoParticellaVO.getParticella());
       			}
       			if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno())) 
            {
       				htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.subalterno", storicoParticellaVO.getSubalterno());
       			}
       			htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
       			htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.idTitoloPossesso", storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso().toString());
       			
       			
       			BigDecimal percentualePossessoTmp = storicoParticellaVO.getElencoConduzioni()[0].getPercentualePossesso();
            if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
            {
              percentualePossessoTmp = new BigDecimal(1);
            }
            htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));	
       			
       			if(Validator.isNotEmpty(storicoParticellaVO.getIdCasoParticolare())) 
            {
       				htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.idCasoParticolare", storicoParticellaVO.getIdCasoParticolare().toString());
       			}
            
            
            
            String[] arrNoteDocConduzione = request.getParameterValues("noteDocConduzione");
            if(Validator.isNotEmpty(arrNoteDocConduzione)
              && (arrNoteDocConduzione[i] != null))
            {
              htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.noteDocConduzione", arrNoteDocConduzione[i]);
            }
            else
            { 
              if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni())
                && Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
              {            
                htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.noteDocConduzione", storicoParticellaVO.getElencoConduzioni()[0]
                  .getElencoDocumentoConduzione()[0].getNote());
              }
            }
              
            //Istanza riesame            
            if(Validator.isNotEmpty(tipoCategoriaDocumentoVOSelezionato))
            {
              String[] arrLavorazionePrioritaria = request.getParameterValues("lavorazionePrioritaria");
            
              if("TC".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getTipoIdentificativo())
                && "518".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getIdentificativo()))
              {
                htmpl.newBlock("blkDatiDocumentoTerritoriali.blkElencoParticelle.blkElencPrioritaLavorazione");
                
                htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.blkElencPrioritaLavorazione.lavorazionePrioritaria", 
                  ""+storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella());
                if(Validator.isNotEmpty(regimeDocumentoInserisci))
                {
                  if(Validator.isNotEmpty(arrLavorazionePrioritaria))
                  {
                    for(int j=0;j<arrLavorazionePrioritaria.length;j++)
                    {
                      if(arrLavorazionePrioritaria[j].equalsIgnoreCase(""+storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella()))
                      {                  
                        htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.blkElencPrioritaLavorazione.checkedLavorazionePrioritaria", "checked=\"cheched\"",null);
                        break;
                      }
                    }
                  }
                }
                
              
              }
            }
            
            
            
            
            // GESTIONE ERRORI Note
            if(elencoErroriNote != null && elencoErroriNote.size() > 0) 
            {
              ValidationErrors errorsNote = (ValidationErrors)elencoErroriNote.elementAt(i);
              if(errorsNote != null && errorsNote.size() > 0) {
                Iterator<String> iter = htmpl.getVariableIterator();
                while(iter.hasNext()) {
                  String key = (String)iter.next();
                    if(key.startsWith("err_")) {
                      String property = key.substring(4);
                      Iterator<ValidationError> errorIterator = errorsNote.get(property);
                      if(errorIterator != null) {
                        ValidationError error = (ValidationError)errorIterator.next();
                        htmpl.set("blkDatiDocumentoTerritoriali.blkElencoParticelle.err_"+property,
                                      MessageFormat.format(htmlStringKO,
                                      new Object[] {
                                      pathErrori + "/"+ imko,
                                      "'"+jssp.process(error.getMessage())+"'",
                                      error.getMessage()}),
                                      null);
                      }
                    }
                }
              }
            }
            
            
            
     			}
          htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaParticelle2");
          
          
          
          //Istanza riesame
          if(Validator.isNotEmpty(tipoCategoriaDocumentoVOSelezionato))
          {
            if("TC".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getTipoIdentificativo())
              && "518".equalsIgnoreCase(tipoCategoriaDocumentoVOSelezionato.getIdentificativo()))
            {
              htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelle2.numRowTabellaParticelle2", "12");
            }
            else
            {
              htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelle2.numRowTabellaParticelle2", "11");
            }
          }
          else
          {
            htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelle2.numRowTabellaParticelle2", "11");
          }
          
          
          
          htmpl.newBlock("blkDatiDocumentoTerritoriali.blkLegende");
     			// LEGENDA TITOLO POSSESSO
     			for(int i = 0; i < elencoTitoloPossesso.length; i++) 
          {
       			htmpl.newBlock("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaConduzione");
       			CodeDescription code = (CodeDescription)elencoTitoloPossesso[i];
       			htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaConduzione.idTitoloPossesso", String.valueOf(code.getCode())+" - ");         			
       			if(i == (elencoTitoloPossesso.length - 1)) 
            {
       				htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaConduzione.descTitoloPossesso", code.getDescription());
       			}
       			else 
            {
       				htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaConduzione.descTitoloPossesso", code.getDescription()+", ");
       			}
     			}
				  // LEGENDA CASO PARTICOLARE
     			for(int i = 0; i < elencoCasoParticolare.length; i++) 
          {
       			htmpl.newBlock("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaCasiParticolari");
       			CodeDescription code = (CodeDescription)elencoCasoParticolare[i];
       			htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaCasiParticolari.idCasoParticolare", String.valueOf(code.getCode())+" - ");         			
       			if(i == (elencoCasoParticolare.length - 1)) 
            {
       				htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription());
       			}
       			else 
            {
       				htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription()+", ");
       			}
     			}
     		}
      }
      if(tipoDocumentoVO.getFlagAnagTerr()
        .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) 
      {
        htmpl.newBlock(bloccoParticelle+".blkCercaParticellaCessata");
        
        
        // Tabella delle particelle
        if(elencoParticelle != null && elencoParticelle.length > 0) 
        {
          htmpl.newBlock("blkDatiDocumentoTerritoriali.blkCessTabellaParticelle");
          
          for(int i = 0; i < elencoParticelle.length; i++) 
          {
            htmpl.newBlock("blkDatiDocumentoTerritoriali.blkCessElencoParticelle");
            StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)elencoParticelle[i];
            htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.idParticella", storicoParticellaVO.getIdParticella().toString());
            htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.descComuneParticella", storicoParticellaVO.getComuneParticellaVO().getDescom());
            htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.siglaProvinciaParticella", storicoParticellaVO.getComuneParticellaVO().getProvinciaVO().getSiglaProvincia());
            if(Validator.isNotEmpty(storicoParticellaVO.getSezione())) 
                htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.sezione", storicoParticellaVO.getSezione());
            htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.foglio", storicoParticellaVO.getFoglio());
            if(Validator.isNotEmpty(storicoParticellaVO.getParticella())) 
                htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.particella", storicoParticellaVO.getParticella());
            if(Validator.isNotEmpty(storicoParticellaVO.getSubalterno()))
              htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.subalterno", storicoParticellaVO.getSubalterno());
            htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.supCatastale", StringUtils.parseSuperficieField(storicoParticellaVO.getSupCatastale()));
            htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.idTitoloPossesso", storicoParticellaVO.getElencoConduzioni()[0].getIdTitoloPossesso().toString());
            
            BigDecimal percentualePossessoTmp = storicoParticellaVO.getElencoConduzioni()[0].getPercentualePossesso();
            if(percentualePossessoTmp.compareTo(new BigDecimal(1)) < 0)
            {
              percentualePossessoTmp = new BigDecimal(1);
            }
            htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.percentualePossesso", Formatter.formatAndRoundBigDecimal0(percentualePossessoTmp));      
            
            if(Validator.isNotEmpty(storicoParticellaVO.getIdCasoParticolare()))
              htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.idCasoParticolare", storicoParticellaVO.getIdCasoParticolare().toString());
          
          
          
          
            //qui non c'e' il controllo note nel caso di cancellazione particelle poichè
            //se ne può inserire sempre e solo una...
            String[] arrCessNoteDocConduzione = request.getParameterValues("cessNoteDocConduzione");
            if(Validator.isNotEmpty(arrCessNoteDocConduzione)
              && (arrCessNoteDocConduzione[i] != null))
            {
              htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.cessNoteDocConduzione", arrCessNoteDocConduzione[i]);
            }
            else
            {  
              if(Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni())
                && Validator.isNotEmpty(storicoParticellaVO.getElencoConduzioni()[0].getElencoDocumentoConduzione()))
              {            
                htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.cessNoteDocConduzione", storicoParticellaVO.getElencoConduzioni()[0]
                  .getElencoDocumentoConduzione()[0].getNote());
              }
            }
            
            
            
            // GESTIONE ERRORI Note
            if(elencoErroriNote != null && elencoErroriNote.size() > 0) {
              ValidationErrors errorsNote = (ValidationErrors)elencoErroriNote.elementAt(i);
              if(errorsNote != null && errorsNote.size() > 0) {
                Iterator<String> iter = htmpl.getVariableIterator();
                while(iter.hasNext()) {
                  String key = (String)iter.next();
                    if(key.startsWith("err_")) {
                      String property = key.substring(4);
                      Iterator<ValidationError> errorIterator = errorsNote.get(property);
                      if(errorIterator != null) {
                        ValidationError error = (ValidationError)errorIterator.next();
                        htmpl.set("blkDatiDocumentoTerritoriali.blkCessElencoParticelle.err_"+property,
                                      MessageFormat.format(htmlStringKO,
                                      new Object[] {
                                      pathErrori + "/"+ imko,
                                      "'"+jssp.process(error.getMessage())+"'",
                                      error.getMessage()}),
                                      null);
                      }
                    }
                }
              }
            }
          
          
          
          
          
          
          
          
          }
            
          //Vado a vedere se ci sono particelle associate
          /*Vector particelleAssociate=(Vector) session.getAttribute("particelleAssociate");
          if (particelleAssociate!=null) 
          {
            Vector elencoTipiUsoSuolo = (Vector)session.getAttribute("elencoTipiUsoSuoloPrimario");
            int size=particelleAssociate.size();
            if (size>0)
            {
              //Se ci sono particelle le faccio vedere 
              htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareHeader");
              //visualizzo il pulsante che permette di eliminarle
              for(int i = 0; i < size; i++) 
              {
                ParticellaAssVO particella=(ParticellaAssVO)particelleAssociate.get(i);
                htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody");
                htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.idParticellaAssociata", ""+i);
                htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.descComuneParticella", particella.getDescComuneParticella());
                htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.siglaProvinciaParticella", particella.getSiglaProvinciaParticella());
               
                if(Validator.isNotEmpty(particella.getSezione()))
                  htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.sezione", particella.getSezione().toUpperCase());
                htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.foglio", particella.getFoglio().toString());
                if(Validator.isNotEmpty(particella.getParticella()))
                  htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.particella", particella.getParticella().toString());
                if(Validator.isNotEmpty(particella.getSubalterno()))
                  htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.subalterno", particella.getSubalterno());
                htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.supCatastale", StringUtils.parseSuperficieField(particella.getSupCatastale()));
                htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.descEvento", particella.getDescrizioneEvento());
                htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.supConduzione", particella.getSupCondotta());
                htmpl.set("blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody.supUtilizzata", particella.getSupUtilizzata());
                
                if(elencoTitoloPossesso != null && elencoTitoloPossesso.length > 0) 
                {
                  String blkParticelle="blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody";
                
                  for(int j = 0; j < elencoTitoloPossesso.length; j++) 
                  {
                    String blk=blkParticelle+".blkElencoConduzione";
                    htmpl.newBlock(blk);
                    CodeDescription code = (CodeDescription)elencoTitoloPossesso[j];
                    htmpl.set(blk+".code", code.getCode().toString());
                    htmpl.set(blk+".description", code.getCode().toString());
                    if (code.getCode().toString().equals(particella.getIdConduzione()))
                      htmpl.set(blk+".selected", "SELECTED");
                  }
                    
                    
                  //Visualizzazione degli errori sulle particelle da associare
                  //vado a scorrere un array di doppie stringhe:
                  //una contiene il nome del segnaposto
                  //l'altra contiene il valore dell'errore
                  if (particella.getErrori() != null)
                  {
                    for (int j = 0; j < particella.getErrori().length; j++)
                    {
                      String segnaPosto = particella.getErrori()[j][0];
                      String errore = particella.getErrori()[j][1];
                      htmpl.set(blkParticelle + "." + segnaPosto, MessageFormat.format(htmlStringKO, new Object[]
                      { pathErrori + "/" + imko, "'" + jssp.process(errore) + "'", errore }), null);
                    }
                    //Ripulisco gli errori per evitare che vengano riproposti
                    //ai reload sucessivi
                    particella.setErrori(null);
                  }
                  
                }
                  
                if(elencoTipiUsoSuolo != null && elencoTipiUsoSuolo.size() > 0) 
                {
                  String nomeBlocco="blkDatiDocumentoTerritoriali.blkTabellaParticelleDaAssociareBody";
                  for(int c = 0; c < elencoTipiUsoSuolo.size(); c++) 
                  {
                    TipoUtilizzoVO tipoUtilizzoVO = (TipoUtilizzoVO)elencoTipiUsoSuolo.elementAt(c);
                    htmpl.newBlock(nomeBlocco+".blkTipiUsoSuolo");
                    htmpl.set(nomeBlocco+".blkTipiUsoSuolo.idTipoUtilizzo", tipoUtilizzoVO.getIdUtilizzo().toString());
                    String descrizione = null;
                    if(tipoUtilizzoVO.getDescrizione().length() > 20) {
                      descrizione = tipoUtilizzoVO.getDescrizione().substring(0, 20);
                    }
                    else {
                      descrizione = tipoUtilizzoVO.getDescrizione();
                    }
                    htmpl.set(nomeBlocco+".blkTipiUsoSuolo.descrizione", "["+tipoUtilizzoVO.getCodice()+"] "+descrizione);
                    if(tipoUtilizzoVO.getIdUtilizzo().toString().equals(particella.getIdUtilizzo()))
                      htmpl.set(nomeBlocco+".blkTipiUsoSuolo.selected", "selected=\"selected\"");
                  }
                  
                  
                  // Carico la combo della varietà solo se l'utente ha selezionato il tipo
                  // uso suolo corrispondente
                  if(Validator.isNotEmpty(particella.getIdUtilizzo())) 
                  {
                    String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
                    TipoVarietaVO[] tipoVarieta = anagFacadeClient.getListTipoVarietaByIdUtilizzo(new Long(particella.getIdUtilizzo()), true, orderBy);
                
                    for(int d = 0; d < tipoVarieta.length; d++) 
                    {
                      TipoVarietaVO tipoVarietaVO = (TipoVarietaVO)tipoVarieta[d];
                      htmpl.newBlock(nomeBlocco+".blkTipiVarieta");
                      htmpl.set(nomeBlocco+".blkTipiVarieta.idVarieta", tipoVarietaVO.getIdVarieta().toString());
                      String descrizione = tipoVarietaVO.getDescrizione();
                      htmpl.set(nomeBlocco+".blkTipiVarieta.descrizione", "["+tipoVarietaVO.getCodiceVarieta()+"] "+descrizione);
                      
                      if(tipoVarietaVO.getIdVarieta().toString().equals(particella.getIdVarieta()))
                        htmpl.set(nomeBlocco+".blkTipiVarieta.selected", "selected=\"selected\"");
                    }
                  }
                  else
                  {
                    //carico un blocco fittizio, altrimenti quando vado a caricare i dati relativi alle particelle da associare
                    //l'array delle varietà ha una lughezza diversa dagli altri
                    htmpl.newBlock(nomeBlocco+".blkTipiVarieta");
                  }
                }
                  
                  
                  
                  
              }
              htmpl.newBlock("blkDatiDocumentoTerritoriali.blkTabellaParticelle3");
            }
            //ripristino in sessione le particelle associate dopo aver cancellato gli eventuali errori
            session.setAttribute("particelleAssociate",particelleAssociate);
          }*/
            
          htmpl.newBlock("blkDatiDocumentoTerritoriali.blkLegende");
            
            
          // LEGENDA TITOLO POSSESSO
          for(int i = 0; i < elencoTitoloPossesso.length; i++) 
          {
            htmpl.newBlock("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaConduzione");
            CodeDescription code = (CodeDescription)elencoTitoloPossesso[i];
            htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaConduzione.idTitoloPossesso", String.valueOf(code.getCode())+" - ");              
            if(i == (elencoTitoloPossesso.length - 1))
              htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaConduzione.descTitoloPossesso", code.getDescription());
            else
              htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaConduzione.descTitoloPossesso", code.getDescription()+", ");
          }
          // LEGENDA CASO PARTICOLARE
          for(int i = 0; i < elencoCasoParticolare.length; i++) 
          {
            htmpl.newBlock("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaCasiParticolari");
            CodeDescription code = (CodeDescription)elencoCasoParticolare[i];
            htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaCasiParticolari.idCasoParticolare", String.valueOf(code.getCode())+" - ");              
            if(i == (elencoCasoParticolare.length - 1))
              htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription());
            else
              htmpl.set("blkDatiDocumentoTerritoriali.blkLegende.blkLegendaCasiParticolari.descCasoParticolare", code.getDescription()+", ");
          }
        }
      }
   	}
    else if(tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CONTI_CORRENTI)) 
    {
      htmpl.newBlock("blkDatiDocumentoContoCorrente");
      
      // Se ci sono errori li visualizzo
      if(errors != null && errors.size() > 0) 
      {
        HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, "blkDatiDocumentoComuni");
      }
      
      
      int size=0;
      if (conti!=null) size=conti.size();
      if(size > 0) 
      {        
        htmpl.newBlock("blkDatiDocumentoContoCorrente.blkSiContiCorrenti");
        for(int i=0;i<size;i++) 
        {
          ContoCorrenteVO conto=(ContoCorrenteVO) conti.get(i);
          
          String blkCC = "blkDatiDocumentoContoCorrente.blkSiContiCorrenti.blkElencoContoCorrente";
          htmpl.newBlock(blkCC);
          
          
          if(size == 1)
          {
            htmpl.set(blkCC+".checked","checked");
          }
          htmpl.set(blkCC+".idContoCorrente", ""+conto.getIdContoCorrente());
          htmpl.set(blkCC+".banca",conto.getDenominazioneBanca());
          htmpl.set(blkCC+".filiale",conto.getDenominazioneSportello());
          htmpl.set(blkCC+".codpaese",conto.getCodPaese());
          htmpl.set(blkCC+".cctrl",conto.getCifraCtrl());
          htmpl.set(blkCC+".cin",conto.getCin());
          htmpl.set(blkCC+".abi",conto.getAbi());
          htmpl.set(blkCC+".cab",conto.getCab());
          htmpl.set(blkCC+".numeroconto",conto.getNumeroContoCorrente());
          htmpl.set(blkCC+".intestatario",conto.getIntestazione());
          String dataInizioValidita="";
          try 
          {
            dataInizioValidita=DateUtils.formatDate(conto.getDataInizioValiditaContoCorrente());
          }
          catch(Exception e) {
            // Data inizio validità vuota
          }
          htmpl.set(blkCC+".dataInizioValidita",dataInizioValidita);
          
          Date dataEstinzione=conto.getDataEstinzione();
          if (dataEstinzione!=null)
          {
            htmpl.set(blkCC+".dataEstinzione",DateUtils.formatDate(dataEstinzione));
          }
          else if(conto.getDataFineValiditaContoCorrente() != null)
          {
            htmpl.set(blkCC+".dataEstinzione",DateUtils.formatDate(conto.getDataFineValiditaContoCorrente()));
          }
                
          // Icona relativa alla validazione del conto corrente
          if(Validator.isNotEmpty(conto.getflagValidato()) 
            && conto.getflagValidato().equalsIgnoreCase("S"))
          {
            htmpl.set(blkCC+".validazioneCC", MessageFormat.format(htmlStringKOCC, new Object[] { pathErrori + "/"+ imokCC, SolmrConstants.MSG_VALIDAZIONE_CC_OK, ""}), null);
          }
          else if(Validator.isNotEmpty(conto.getflagValidato()) 
            && conto.getflagValidato().equalsIgnoreCase("N"))
          {
            htmpl.set(blkCC+".validazioneCC", MessageFormat.format(htmlStringKOCC, new Object[] { pathErrori + "/"+ imkoCC, SolmrConstants.MSG_VALIDAZIONE_CC_KO, ""}), null);              
          }
          else
          {
            htmpl.set(blkCC+".validazioneCC", "");
          }
                
        }
        
      }
      else 
      {
        htmpl.newBlock("blkDatiDocumentoContoCorrente.blkNoContiCorrenti");
      }
    }
 	}
  
  
  // Se il campo flag_anag_terr è = 'T' agiungiamo la Combo Piano di riferimento
  // Se FLAG_ANAG_TERR == C agiungiamo la Combo Piano di riferimento e anomalie
  /*if(tipoDocumentoVO!=null && (tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
              || tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))) 
  
  {
    String bloccoDichiarazioneConsistenza =  bloccoParticelle+".blkPianoRiferimento";
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    String noCessata = null;
    if (anagAziendaVO.getDataCessazione() == null) 
      noCessata = SolmrConstants.PIANO_LAVORAZIONE_NORMALE;
    pianoRiferimentoUtils.popolaComboPianoRiferimento(htmpl, anagFacadeClient,
      anagAziendaVO.getIdAzienda(), bloccoDichiarazioneConsistenza, idDichiarazioneConsistenza, noCessata,
      null);
  
    
  }*/
  
 	// GESTIONE ERRORI SEZIONE GENERICA
 	if(errors != null && errors.size() > 0) {
   		HtmplUtil.setErrors(htmpl, errors, request, application);
 	}

%>
<%= htmpl.text()%>
