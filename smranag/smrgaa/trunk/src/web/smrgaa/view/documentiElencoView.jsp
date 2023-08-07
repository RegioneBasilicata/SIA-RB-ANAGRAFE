<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/documentiElenco.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 	Vector elencoCUAA = (Vector)request.getAttribute("elencoCUAA");
 	Vector elencoTipiTipologiaDocumento = (Vector)request.getAttribute("elencoTipiTipologiaDocumento");
 	TipoCategoriaDocumentoVO[] elencoTipiCategoriaDocumento = (TipoCategoriaDocumentoVO[])request.getAttribute("elencoTipiCategoriaDocumento");
 	TipoDocumentoVO[] elencoTipiDocumento = (TipoDocumentoVO[])request.getAttribute("elencoTipiDocumento");
 	Vector elencoTipoStatoDocumento = (Vector)request.getAttribute("elencoTipoStatoDocumento");
 	DocumentoVO documentoRicercaVO = (DocumentoVO)session.getAttribute("documentoRicercaVO");
 	String protocollazione = (String)session.getAttribute("protocollazione");
 	String messaggioErroreData = (String)request.getAttribute("messaggioErroreData");
 	String messaggioErroreDataAl = (String)request.getAttribute("messaggioErroreDataAl");
 	String messaggioErrorePuntuale = (String)request.getAttribute("messaggioErrorePuntuale");
 	Vector elencoDocumenti = (Vector)session.getAttribute("elencoDocumenti");
 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 	String[] elencoId = (String[])request.getAttribute("elencoId");
  long[] elencoIdStampa = (long[])session.getAttribute("idDocumentoStampaProtocollo");
 	String dataEsecuzione = (String)request.getAttribute("dataEsecuzione");
  String operazione = request.getParameter("operazione");
 	
 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
	
 	// Gestione immagini errori per i blocchi html
 	String htmlStringKO = "<a href=\"#\"><img src=\"{0}\" onClick=\"alert({1})\" "+
    					  "title=\"{2}\" border=\"0\"></a>";
	String imko = "ko.gif";
	StringProcessor jssp = new JavaScriptStringProcessor();
	
	
	
	
	
	String regimeDocumenti = request.getParameter("regimeDocumenti");
  
  if(Validator.isEmpty(regimeDocumenti))
  {
    htmpl.set("primoIngressso", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_0")))
  {
    htmpl.set("esito", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_1")))
  {
    htmpl.set("stato", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_2")))
  {
    htmpl.set("tipologia", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_3")))
  {
    htmpl.set("validita", "true");
  }
  if(Validator.isNotEmpty(request.getParameter("hidden_nascondibile_4")))
  {
    htmpl.set("protocollo", "true");
  }
  
  
  if ("stampaPdf".equals(request.getParameter("stampaPdf")))
  {
    htmpl.set("stampaProtocollo", "stampaProtocolloImmediata()");
  }
  else
  {
    htmpl.set("stampaProtocollo", "");
  }
  
  
 	
 	// Se si è verificato un errore durante il reperimento dei filtri di ricerca o non è possibile
 	// accedere alla funzione
 	if(Validator.isNotEmpty(messaggioErrore)) 
  {
 		htmpl.newBlock("blkErroreGenerico");
 		htmpl.set("blkErroreGenerico.messaggioErrore", messaggioErrore);
 	}
 	// Altrimenti popolo la sezione relativa ai filtri di ricerca
 	else 
  {
 		// SEZIONE RICERCA
 		htmpl.newBlock("blkRicercaDocumenti");

 		// COMBO CUAA
 		Iterator iteraCUAA = elencoCUAA.iterator();
 		while(iteraCUAA.hasNext()) {
   		String cuaa = (String)iteraCUAA.next();
   		htmpl.newBlock("blkRicercaDocumenti.blkElencoCUAA");
   		if(Validator.isNotEmpty(documentoRicercaVO)) {
     			if(cuaa.equalsIgnoreCase(documentoRicercaVO.getCuaa())) {
       			htmpl.set("blkRicercaDocumenti.blkElencoCUAA.selected", "selected=\"selected\"");
     			}
   		}
   		htmpl.set("blkRicercaDocumenti.blkElencoCUAA.codice", cuaa.toUpperCase());
   		htmpl.set("blkRicercaDocumenti.blkElencoCUAA.descrizione", cuaa.toUpperCase());
 		}

		// COMBO TIPO TIPOLOGIA DOCUMENTO
 	 	Iterator iteraTipiTipologiaDocumento = elencoTipiTipologiaDocumento.iterator();
 	 	while(iteraTipiTipologiaDocumento.hasNext()) 
 	 	{
 	  	CodeDescription code = (CodeDescription)iteraTipiTipologiaDocumento.next();
 	  	htmpl.newBlock("blkRicercaDocumenti.blkElencoTipiTipologiaDocumento");
 	   	if(documentoRicercaVO != null && documentoRicercaVO.getTipoTipologiaDocumento() != null 
 	   	  && documentoRicercaVO.getTipoTipologiaDocumento().getCode().equals(code.getCode())) 
 	   	{
      	htmpl.set("blkRicercaDocumenti.blkElencoTipiTipologiaDocumento.selected", "selected=\"selected\"", null);
     	}
     	else if("arrivoPopUp".equalsIgnoreCase(operazione))
     	{
     	  String tipoTipologiaDocumento = request.getParameter("idTipologiaDocumentoPopUp");
     	  Integer idTipologiaDocumento = Integer.decode(tipoTipologiaDocumento);
     	  if(idTipologiaDocumento.compareTo(code.getCode()) == 0) 
	      {
	        htmpl.set("blkRicercaDocumenti.blkElencoTipiTipologiaDocumento.selected", "selected=\"selected\"", null);
	      }     	  
     	}
 	   	htmpl.set("blkRicercaDocumenti.blkElencoTipiTipologiaDocumento.codice", code.getCode().toString());
 	   	htmpl.set("blkRicercaDocumenti.blkElencoTipiTipologiaDocumento.descrizione", code.getDescription());
 	 	}

 	 	// COMBO TIPO DOCUMENTO
 	 	if(elencoTipiCategoriaDocumento != null && elencoTipiCategoriaDocumento.length > 0) 
    {
   		for(int i = 0; i < elencoTipiCategoriaDocumento.length; i++) 
   		{
   			TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = (TipoCategoriaDocumentoVO)elencoTipiCategoriaDocumento[i];
     		htmpl.newBlock("blkRicercaDocumenti.blkElencoTipiCategoriaDocumento");
 	     	if(documentoRicercaVO != null && documentoRicercaVO.getTipoCategoriaDocumentoVO() != null 
 	     	  && documentoRicercaVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento().compareTo(tipoCategoriaDocumentoVO.getIdCategoriaDocumento()) == 0) 
 	     	{
       		htmpl.set("blkRicercaDocumenti.blkElencoTipiCategoriaDocumento.selected", "selected=\"selected\"", null);
     		}
     		else if("arrivoPopUp".equalsIgnoreCase(operazione))
        {
          String tipoCategoriaDocumento = request.getParameter("idCategoriaDocumentoPopUp");
          Long idCategoriaDocumento = Long.decode(tipoCategoriaDocumento);
          if(idCategoriaDocumento.compareTo(tipoCategoriaDocumentoVO.getIdCategoriaDocumento()) == 0) 
	        {
	          htmpl.set("blkRicercaDocumenti.blkElencoTipiCategoriaDocumento.selected", "selected=\"selected\"", null);
	        }
        }
     		htmpl.set("blkRicercaDocumenti.blkElencoTipiCategoriaDocumento.codice", tipoCategoriaDocumentoVO.getIdCategoriaDocumento().toString());
     		htmpl.set("blkRicercaDocumenti.blkElencoTipiCategoriaDocumento.descrizione", tipoCategoriaDocumentoVO.getDescrizione());
     	}
   	}
   	 	
 		// COMBO DESCRIZIONE DOCUMENTO
 	 	if(elencoTipiDocumento != null && elencoTipiDocumento.length > 0) 
    {
   		for(int i = 0; i < elencoTipiDocumento.length; i++) 
   		{
   			TipoDocumentoVO tipoDocumentoElencoVO = (TipoDocumentoVO)elencoTipiDocumento[i];
     		htmpl.newBlock("blkRicercaDocumenti.blkElencoTipiDocumento");
     		if(documentoRicercaVO != null && documentoRicercaVO.getTipoDocumentoVO() != null 
     		  && documentoRicercaVO.getTipoDocumentoVO().getIdDocumento().compareTo(tipoDocumentoElencoVO.getIdDocumento()) == 0) 
     		{
   			  htmpl.set("blkRicercaDocumenti.blkElencoTipiDocumento.selected", "selected=\"selected\"", null);     			
   		  }
   		  else if("arrivoPopUp".equalsIgnoreCase(operazione))
        {
          String tipoDocumento = request.getParameter("idTipoDocumentoPopUp");
          Long idTipoDocumento = Long.decode(tipoDocumento);
          if(idTipoDocumento.compareTo(tipoDocumentoElencoVO.getIdDocumento()) == 0) 
	        {
	          htmpl.set("blkRicercaDocumenti.blkElencoTipiDocumento.selected", "selected=\"selected\"", null);          
	        }
        }
     		htmpl.set("blkRicercaDocumenti.blkElencoTipiDocumento.codice", tipoDocumentoElencoVO.getIdDocumento().toString());
     		htmpl.set("blkRicercaDocumenti.blkElencoTipiDocumento.descrizione", tipoDocumentoElencoVO.getDescrizione());
     	}
   	}

 		// COMBO STATO DOCUMENTO
 		Iterator iteraTipoStatoDocumento = elencoTipoStatoDocumento.iterator();
 		int count = 0;
 		while(iteraTipoStatoDocumento.hasNext()) {
   		TipoStatoDocumentoVO tipoStatoDocumentoVO = (TipoStatoDocumentoVO)iteraTipoStatoDocumento.next();
   		htmpl.newBlock("blkRicercaDocumenti.blkElencoTipoStatoDocumento");
   		if(count == 0) {
   			htmpl.set("blkRicercaDocumenti.blkElencoTipoStatoDocumento.codice", SolmrConstants.ID_STATO_DOCUMENTO_ATTIVO);
       		htmpl.set("blkRicercaDocumenti.blkElencoTipoStatoDocumento.descrizione", SolmrConstants.DESCRIZIONE_DOCUMENTO_ATTIVO);
       		if(Validator.isNotEmpty(documentoRicercaVO) && Validator.isNotEmpty(documentoRicercaVO.getIdStatoDocumento())) {
       			if(documentoRicercaVO.getIdStatoDocumento().toString().equalsIgnoreCase(SolmrConstants.ID_STATO_DOCUMENTO_ATTIVO)) {
           			htmpl.set("blkRicercaDocumenti.blkElencoTipoStatoDocumento.selected", "selected=\"selected\"");
         			}
       		}	
   		}		
   		htmpl.set("blkRicercaDocumenti.blkElencoTipoStatoDocumento.codice", tipoStatoDocumentoVO.getIdStatoDocumento().toString());
   		htmpl.set("blkRicercaDocumenti.blkElencoTipoStatoDocumento.descrizione", tipoStatoDocumentoVO.getDescrizione());
   		if(Validator.isNotEmpty(documentoRicercaVO) && Validator.isNotEmpty(documentoRicercaVO.getIdStatoDocumento())) {
   			if(tipoStatoDocumentoVO.getIdStatoDocumento().compareTo(documentoRicercaVO.getIdStatoDocumento()) == 0) {
       			htmpl.set("blkRicercaDocumenti.blkElencoTipoStatoDocumento.selected", "selected=\"selected\"");
     			}
   		}
   		count++;
 		}

 		// COMBO PROTOCOLLAZIONE
 		if(Validator.isNotEmpty(protocollazione)) {
   		if(protocollazione.equalsIgnoreCase(SolmrConstants.PROTOCOLLAZIONE_EFFETTUATA)) {
     			htmpl.set("blkRicercaDocumenti.selectedEffettuata", "selected=\"selected\"");
   		}
   		else {
     			htmpl.set("blkRicercaDocumenti.selectedDaEffettuare", "selected=\"selected\"");
   		}
 		}
 		// GESTIONE ERRORE CAMPO DATA SCADENZA
 		if(Validator.isNotEmpty(messaggioErroreData)) {
   		htmpl.set("blkRicercaDocumenti.err_dataDal", MessageFormat.format(htmlStringKO,
             		  new Object[] { pathErrori + "/"+ imko, "'"+jssp.process(messaggioErroreData)+"'", messaggioErroreData}),null);
 		}
 		else {	
 			if(Validator.isNotEmpty(messaggioErroreDataAl)) {
 	     		htmpl.set("blkRicercaDocumenti.err_dataAl", MessageFormat.format(htmlStringKO,
 	               		  new Object[] { pathErrori + "/"+ imko, "'"+jssp.process(messaggioErroreDataAl)+"'", messaggioErroreDataAl}),null);
 	   		}   		
 		}
 		if(Validator.isNotEmpty(documentoRicercaVO) && Validator.isNotEmpty(documentoRicercaVO.getCheckScaduti())) {
 			htmpl.set("blkRicercaDocumenti.checkedScaduti", "checked=\"checked\"");
 		}
 		if(Validator.isNotEmpty(documentoRicercaVO) && Validator.isNotEmpty(documentoRicercaVO.getMinDataFineValidita())) {
   		htmpl.set("blkRicercaDocumenti.dataDal", DateUtils.formatDate(documentoRicercaVO.getMinDataFineValidita()));
   	}
   	else if(Validator.isNotEmpty(request.getAttribute("dataDal"))) {
   		htmpl.set("blkRicercaDocumenti.dataDal", (String)request.getAttribute("dataDal"));
   	}
   	if(Validator.isNotEmpty(documentoRicercaVO) && Validator.isNotEmpty(documentoRicercaVO.getMaxDataFineValidita())) {
		  htmpl.set("blkRicercaDocumenti.dataAl", DateUtils.formatDate(documentoRicercaVO.getMaxDataFineValidita()));
		}
		else if(Validator.isNotEmpty(request.getAttribute("dataAl"))) {
			htmpl.set("blkRicercaDocumenti.dataAl", (String)request.getAttribute("dataAl"));
		}
   		
 		// DATA_ESECUZIONE
 		if(Validator.isNotEmpty(dataEsecuzione)) {
 			htmpl.set("blkRicercaDocumenti.dataEsecuzioneControlli", dataEsecuzione);
 		}
 		// SEZIONE ELENCO DOCUMENTI (ESITO RICERCA)
 		if(Validator.isNotEmpty(messaggioErrorePuntuale)) 
    {
   		htmpl.newBlock("blkRicercaDocumenti.blkErrorePuntuale");
   		htmpl.set("blkRicercaDocumenti.blkErrorePuntuale.messaggioErrorePuntuale", messaggioErrorePuntuale);
 		}
   	else 
    {
   		if(elencoDocumenti != null && elencoDocumenti.size() > 0) 
      {
   			Iterator iteraDocumenti = elencoDocumenti.iterator();
   			htmpl.newBlock("blkRicercaDocumenti.blkEsitoRicercaDocumenti");
   			while(iteraDocumenti.hasNext()) 
        {
     			htmpl.newBlock("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti");
     			DocumentoVO documentoVO = (DocumentoVO)iteraDocumenti.next();
     			htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.idDocumento", documentoVO.getIdDocumento().toString());
          htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.flagIstanzaRiesame", documentoVO.getFlagIstanzaRiesame());
          //Non sono nella ricerca
          if(Validator.isEmpty(operazione)
            || (Validator.isNotEmpty(operazione) &&  !operazione.equalsIgnoreCase("ricerca")))
          {          
       			if(elencoId != null && elencoId.length > 0 && elencoId.length <= elencoDocumenti.size()) 
            {
      	 			for(int i = 0; i < elencoId.length; i++) 
              {
      	 				String id = (String)elencoId[i];
       					if(documentoVO.getIdDocumento().compareTo(Long.decode(id)) == 0) 
                {
      		 				htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.checked", "checked=\"checked\"");
      	 				}
      	 			}
       			}
            else
            {
              if(elencoIdStampa != null && elencoIdStampa.length > 0 && elencoIdStampa.length <= elencoDocumenti.size()) 
              {
                for(int i = 0; i < elencoIdStampa.length; i++) 
                {
                  long id = elencoIdStampa[i];
                  if(documentoVO.getIdDocumento().compareTo(new Long(id)) == 0) 
                  {
                    htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.checked", "checked=\"checked\"");
                  }
                }
              }          
            }
          }
     			if(Validator.isNotEmpty(documentoVO.getEsitoControllo())) {
     				if(documentoVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_BLOCCANTE)) {
     					htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.classEsitoDocumento", SolmrConstants.IMMAGINE_ESITO_BLOCCANTE);
   						htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.titleEsitoDocumento", SolmrConstants.DESC_TITLE_BLOCCANTE);         				
     				}
     				else if(documentoVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_WARNING)) {
     					htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.classEsitoDocumento", SolmrConstants.IMMAGINE_ESITO_WARNING);
   						htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.titleEsitoDocumento", SolmrConstants.DESC_TITLE_WARNING);         				
     				}
     				else if(documentoVO.getEsitoControllo().equalsIgnoreCase(SolmrConstants.ESITO_CONTROLLO_POSITIVO)) {
     					htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.classEsitoDocumento", SolmrConstants.IMMAGINE_ESITO_POSITIVO);
   						htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.titleEsitoDocumento", SolmrConstants.DESC_TITLE_POSITIVO);         				
     				}
     			}
     			if(Validator.isNotEmpty(documentoVO.getIdStatoDocumento())) 
          {
     				if(documentoVO.getIdStatoDocumento().compareTo(Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_ANNULLATO)) == 0) {
      				// ICONA ANNULLATO
       				htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.classStatoDocumento", SolmrConstants.CLASS_STATO_DOCUMENTO_ANNULLATO);
       				htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.titleStatoDocumento", SolmrConstants.TITLE_STATO_DOCUMENTO_ANNULLATO);
     				}
     				else 
            {
      				// ICONA STORICIZZATO
       				htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.classStatoDocumento", SolmrConstants.CLASS_STATO_DOCUMENTO_STORICIZZATO);
       				htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.titleStatoDocumento", SolmrConstants.TITLE_STATO_DOCUMENTO_STORICIZZATO);
     				}
     			}
     			else 
          {
     				if(Validator.isNotEmpty(documentoVO.getDataFineValidita())) 
            {
      				if(documentoVO.getDataFineValidita().before(DateUtils.parseDate(DateUtils.getCurrent()))) 
              {
         				// ICONA SCADUTO
         				htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.classStatoDocumento", SolmrConstants.CLASS_STATO_DOCUMENTO_SCADUTO);
         				htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.titleStatoDocumento", SolmrConstants.TITLE_STATO_DOCUMENTO_SCADUTO);
       				}
     				}
       		}         			
    	 		htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.descTipoTipologiaDocumento", documentoVO.getTipoTipologiaDocumento().getDescription());
    	 		htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.idTipologiaDocumentoHd", documentoVO.getTipoTipologiaDocumento().getSecondaryCode());
     			htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.descTipoCategoriaDocumento", documentoVO.getTipoCategoriaDocumentoVO().getDescrizione());
     			htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.descTipoDocumento", documentoVO.getTipoDocumentoVO().getDescrizione());
     			htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.dataInizioDocumento", DateUtils.formatDate(documentoVO.getDataInizioValidita()));
     			if(Validator.isNotEmpty(documentoVO.getDataFineValidita())) 
          {
       			htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.dataFineDocumento", DateUtils.formatDate(documentoVO.getDataFineValidita()));
     			}
     			if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) 
          {
       			htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.numeroProtocollo", StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
     			}
     			if(Validator.isNotEmpty(documentoVO.getDataProtocollo())) 
          {
       			htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.dataProtocollo", DateUtils.formatDate(documentoVO.getDataProtocollo()));
     			}
     			
     			if(documentoVO.getvAllegatoDocumento() != null)
				  {
				    for(int i=0;i<documentoVO.getvAllegatoDocumento().size();i++)
				    {
				      AllegatoDocumentoVO allegatoDocumentoVO = documentoVO.getvAllegatoDocumento().get(i);
				    
				    
				      htmpl.newBlock("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.blkFileAllegato");
				      htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.blkFileAllegato.idDocumento", 
                ""+documentoVO.getIdDocumento());
				      htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.blkFileAllegato.idAllegato", 
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
			        
			        htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.blkFileAllegato.immagineStampa", 
			          immagineStampa);
				        
				        
				        
				      htmpl.set("blkRicercaDocumenti.blkEsitoRicercaDocumenti.blkElencoDocumenti.blkFileAllegato.titleAllegato", 
				        allegatoDocumentoVO.getNomeLogico()+" (" +
                allegatoDocumentoVO.getNomeFisico()+")");
				    }
				  }
     			
     			
   			}
     	}
   	}
 		// GESTIONE GENERICA DEGLI ERRORI
 		if(errors != null && errors.size() > 0) 
    {
   		HtmplUtil.setErrors(htmpl, errors, request, application);
 		}
 	}
%>
<%= htmpl.text()%>
