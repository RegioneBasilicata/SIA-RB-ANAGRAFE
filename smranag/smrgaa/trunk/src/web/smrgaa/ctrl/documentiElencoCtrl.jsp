<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.util.performance.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "documentoRicercaVO";
  

	String iridePageName = "documentiElencoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%  
  
  //ero già in questa pagina non cancello gli id da stampare
  if(Validator.isNotEmpty(request.getParameter("regimeDocumenti")))
  {
    WebUtils.removeUselessFilter(session, noRemove+",idDocumentoStampaProtocollo");
  }
  else
  {
    WebUtils.removeUselessFilter(session, noRemove);
  }  

 	String documentiElencoUrl = "/view/documentiElencoView.jsp";
 	String excelUrl = "/servlet/ExcelBuilderServlet";
 	String actionUrl = "../layout/documentiElenco.htm";
 	String attenderePregoUrl = "/view/attenderePregoView.jsp";
    
    
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String operazione = request.getParameter("operazione");
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	DocumentoVO documentoRicercaVO = (DocumentoVO)session.getAttribute("documentoRicercaVO");
 	Vector elencoDocumenti = (Vector)session.getAttribute("elencoDocumenti");
 	if(documentoRicercaVO == null) 
  {
 		documentoRicercaVO = new DocumentoVO();
 		documentoRicercaVO.setIdStatoDocumento(Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_ATTIVO));
 		documentoRicercaVO.setMinDataFineValidita(DateUtils.parseDate(SolmrConstants.DATA_INIZIO_ANNO_AGRICOLO));
 		documentoRicercaVO.setCheckScaduti(SolmrConstants.FLAG_S);
 		session.setAttribute("documentoRicercaVO", documentoRicercaVO);
 		session.removeAttribute("protocollazione");
 	}
 	String messaggioErrore = "";
 	String messaggioErroreData = "";
 	String messaggioErroreDataAl = "";
 	String messaggioErrorePuntuale = "";
 	String refresh = request.getParameter("refresh");
 	String pageFrom = (String)request.getAttribute("pageFrom");
 	if(!Validator.isNotEmpty(pageFrom)) 
  {
  	pageFrom = request.getParameter("pageFrom");
 	}
 	// Recupero eventualmente gli id valorizzati provenienti dagli errori
 	String[] elencoId = (String[])request.getAttribute("elencoId");
 	if(elencoId != null && elencoId.length > 0) 
  {
 		request.setAttribute("elencoId", elencoId);
 	}

 	// Recupero tutti gli elementi che mi servono per popolare i filtri di ricerca della pagina
 	// COMBO DEI CUAA
 	Vector elencoCUAA = null;
 	String cuaa = request.getParameter("CUAA");
 	try 
  {
  	elencoCUAA = anagFacadeClient.getListCUAAByIdAzienda(anagAziendaVO.getIdAzienda());
 	}
 	catch(SolmrException se) 
  {
   	messaggioErrore = AnagErrors.ERRORE_KO_CUAA_DOCUMENTI;
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
		<jsp:forward page="<%= documentiElencoUrl %>" />
   	<%
 	}
 	if(elencoCUAA == null || elencoCUAA.size() == 0)
  {
 		messaggioErrore = AnagErrors.ERRORE_NO_CUAA_FOR_AZIENDA;
 		request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
     	<jsp:forward page="<%= documentiElencoUrl %>" />
   	<%
 	}
  request.setAttribute("elencoCUAA", elencoCUAA);

	// COMBO TIPO TIPOLOGIA DOCUMENTO
  Vector elencoTipiTipologiaDocumento = null;
  try 
  {
  	elencoTipiTipologiaDocumento = anagFacadeClient.getTipiTipologiaDocumento();
  }
  catch(SolmrException se) 
  {
   	messaggioErrore = se.getMessage();
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
     	<jsp:forward page="<%= documentiElencoUrl %>" />
   	<%
  }
  request.setAttribute("elencoTipiTipologiaDocumento", elencoTipiTipologiaDocumento);
   	
	// COMBO TIPO DOCUMENTO
	//Se valorizzzato arrivo dalla popUp di ricerca documenti
  String tipoTipologiaDocumento = request.getParameter("idTipologiaDocumentoPopUp");
  if(Validator.isEmpty(tipoTipologiaDocumento))
  {
    tipoTipologiaDocumento = request.getParameter("idTipologiaDocumento");
  }
	//String tipoTipologiaDocumento = request.getParameter("idTipologiaDocumento");
	if(!Validator.isNotEmpty(tipoTipologiaDocumento) && !Validator.isNotEmpty(refresh)) 
  {
		if(documentoRicercaVO.getTipoTipologiaDocumento() != null && documentoRicercaVO.getTipoTipologiaDocumento().getCode() != null) 
    {
			tipoTipologiaDocumento = documentoRicercaVO.getTipoTipologiaDocumento().getCode().toString();
		}
	}
	Long idTipologiaDocumento = null;
	if(Validator.isNotEmpty(tipoTipologiaDocumento)) 
  {
 		idTipologiaDocumento = Long.decode(tipoTipologiaDocumento);
 		request.setAttribute("idTipologiaDocumento", idTipologiaDocumento);
 		TipoCategoriaDocumentoVO[] elencoTipiCategoriaDocumento = null;
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		try 
    {
 			elencoTipiCategoriaDocumento = anagFacadeClient.getListTipoCategoriaDocumentoByIdTipologiaDocumento(idTipologiaDocumento, orderBy);
 		}
 		catch(SolmrException se) 
    {
 			messaggioErrore = AnagErrors.ERRORE_KO_CATEGORIA_DOCUMENTO;
 			request.setAttribute("messaggioErrore", messaggioErrore);
 			%>
     		<jsp:forward page="<%= documentiElencoUrl %>" />
    	<%
 		}
 		request.setAttribute("elencoTipiCategoriaDocumento", elencoTipiCategoriaDocumento);
	}
	
	// COMBO DESCRIZIONE DOCUMENTO
	String tipoCategoriaDocumento = request.getParameter("idCategoriaDocumentoPopUp");
  if(Validator.isEmpty(tipoCategoriaDocumento))
  {
    tipoCategoriaDocumento = request.getParameter("idCategoriaDocumento");
  }
	//String tipoCategoriaDocumento = request.getParameter("idCategoriaDocumento");
	if(!Validator.isNotEmpty(tipoCategoriaDocumento) && !Validator.isNotEmpty(refresh)) 
  {
		if(documentoRicercaVO.getTipoCategoriaDocumentoVO() != null && documentoRicercaVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento() != null) {
			tipoCategoriaDocumento = documentoRicercaVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento().toString();
		}
	}
	Long idCategoriaDocumento = null;
	if(Validator.isNotEmpty(tipoCategoriaDocumento) && idTipologiaDocumento != null) 
  {
		idCategoriaDocumento = Long.decode(tipoCategoriaDocumento);
 		request.setAttribute("idCategoriaDocumento", idCategoriaDocumento);
 		TipoDocumentoVO[] elencoTipiDocumento = null;
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		try 
    {
 			elencoTipiDocumento = anagFacadeClient.getListTipoDocumentoByIdCategoriaDocumentoAndIdAzienda(idCategoriaDocumento, anagAziendaVO.getIdAzienda(), orderBy);
 		}
 		catch(SolmrException se) 
    {
 			messaggioErrore = AnagErrors.ERRORE_KO_TIPO_DOCUMENTO;
   		%>
      	<jsp:forward page="<%= documentiElencoUrl %>" />
     	<%
 		}
 		request.setAttribute("elencoTipiDocumento", elencoTipiDocumento);
	}
	// Recupero il tipo documento per la corretta gestione dei dati dell'inserimento
  //Se valorizzzato arrivo dalla popUp di ricerca documenti
  String tipoDocumento = request.getParameter("idTipoDocumentoPopUp");
  if(Validator.isEmpty(tipoDocumento))
  {
    tipoDocumento = request.getParameter("idTipoDocumento");
  }
  
	Long idTipoDocumento = null;
	if(Validator.isNotEmpty(tipoDocumento)) 
  {
		idTipoDocumento = Long.decode(tipoDocumento);
		request.setAttribute("idTipoDocumento", idTipoDocumento);
	}
	else if(documentoRicercaVO.getTipoDocumentoVO() != null && documentoRicercaVO.getTipoDocumentoVO().getIdDocumento() != null && !Validator.isNotEmpty(refresh)) 
  {
		idTipoDocumento = documentoRicercaVO.getTipoDocumentoVO().getIdDocumento();
	}
	
 	// COMBO STATO DOCUMENTO
 	Vector elencoTipoStatoDocumento = null;
 	try 
  {
 		elencoTipoStatoDocumento = anagFacadeClient.getListTipoStatoDocumento(true);
 	}
 	catch(SolmrException se) 
  {
  	messaggioErrore = se.getMessage();
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
      <jsp:forward page="<%= documentiElencoUrl %>" />
   	<%
 	}
  request.setAttribute("elencoTipoStatoDocumento", elencoTipoStatoDocumento);
 	String protocollazione = request.getParameter("protocollazione");
 	if(!Validator.isNotEmpty(protocollazione)) 
  {
 		protocollazione = (String)session.getAttribute("protocollazione");
 	}
  documentoRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
   	
  // Se sto ricaricando la pagina per via del cambiamento dei valori delle combo
  if(Validator.isNotEmpty(refresh)) 
  {
    //Cancello i check dei documenti selezionati
    request.removeAttribute("idDocumentoStampaProtocollo");
 		// Setto i valori all'interno del VO
 		TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = null;
 		TipoDocumentoVO tipoDocumentoVO = null;
 		documentoRicercaVO.setCuaa(cuaa);
 		if(Validator.isNotEmpty(request.getParameter("idTipologiaDocumento"))) 
    {
 			documentoRicercaVO.setTipoTipologiaDocumento(new CodeDescription(Integer.decode(request.getParameter("idTipologiaDocumento")), null));
 		}
 		else 
    {
 			documentoRicercaVO.setTipoTipologiaDocumento(null);
 		}
 		if(Validator.isNotEmpty(request.getParameter("idCategoriaDocumento"))) {
 			tipoCategoriaDocumentoVO = new TipoCategoriaDocumentoVO();
 			tipoCategoriaDocumentoVO.setIdCategoriaDocumento(Long.decode(request.getParameter("idCategoriaDocumento")));
 			documentoRicercaVO.setTipoCategoriaDocumentoVO(tipoCategoriaDocumentoVO);
 		}
 		else 
    {
 			documentoRicercaVO.setTipoCategoriaDocumentoVO(null);
 		}
 		if(Validator.isNotEmpty(request.getParameter("idTipoDocumento"))) { 
 			tipoDocumentoVO = new TipoDocumentoVO();
 			tipoDocumentoVO.setIdDocumento(Long.decode(request.getParameter("idTipoDocumento")));
   		documentoRicercaVO.setTipoDocumentoVO(tipoDocumentoVO);
 		}
 		else {
 			documentoRicercaVO.setTipoDocumentoVO(null);
 		}
 		if(Validator.isNotEmpty(request.getParameter("idStatoDocumento"))) {
 			documentoRicercaVO.setIdStatoDocumento(Long.decode(request.getParameter("idStatoDocumento")));
 		}
 		else {
 			documentoRicercaVO.setIdStatoDocumento(null);
 		}
 		if(Validator.isNotEmpty(request.getParameter("protocollazione"))) 
    {
     	session.setAttribute("protocollazione", request.getParameter("protocollazione"));
   	}
   	else 
    {
    	session.removeAttribute("protocollazione");
   	}
 		if(Validator.isNotEmpty(request.getParameter("scaduti"))) 
    {
    	documentoRicercaVO.setCheckScaduti(request.getParameter("scaduti"));
   	}
   	else 
    {
   		documentoRicercaVO.setCheckScaduti(null);
   	}
 		if(Validator.isNotEmpty(request.getParameter("dataDal"))) 
    {
 	  	if(Validator.validateDateF(request.getParameter("dataDal"))) 
      {
 	   		documentoRicercaVO.setMinDataFineValidita(DateUtils.parseDate(request.getParameter("dataDal")));
 	   	}
 	   	else 
      {
 	   		documentoRicercaVO.setMinDataFineValidita(null);
 	   	}
 	 	}
   	if(Validator.isNotEmpty(request.getParameter("dataAl"))) 
    {
 	  	if(Validator.validateDateF(request.getParameter("dataAl"))) 
      {
 	   		documentoRicercaVO.setMaxDataFineValidita(DateUtils.parseDate(request.getParameter("dataAl")));
 	   	}
 	   	else 
      {
 	   		documentoRicercaVO.setMaxDataFineValidita(null);
 	   	}
 	  }
 	  else 
    {
 	  	documentoRicercaVO.setMaxDataFineValidita(null);
 	  }
   	session.setAttribute("documentoRicercaVO", documentoRicercaVO);
  }
 	// Altrimenti...
 	else 
  {
 		// Se non arrivo da una pagina del documentale genero il filtro in relazione ai criteri di default
 		if(!Validator.isNotEmpty(pageFrom) || !pageFrom.equalsIgnoreCase("documentale")) 
    {
   		documentoRicercaVO.setCuaa((String)elencoCUAA.elementAt(0));
   		documentoRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
   	}
    //Non sono nella ricerca
    if(Validator.isEmpty(operazione)
      || (Validator.isNotEmpty(operazione) &&  !operazione.equalsIgnoreCase("ricerca")))
    { 
			// Pulisco la sessione dai possibili precedenti dati
	    session.removeAttribute("elencoDocumenti");
	    session.removeAttribute("documentoVO");
	    session.removeAttribute("documentoModificaVO");
	    session.removeAttribute("elencoProprietari");
	    session.removeAttribute("elencoParticelle");
      
	    try 
      {
	   	 	elencoDocumenti = anagFacadeClient.searchDocumentiByParameters(documentoRicercaVO, null, null);
	   	}
 	    catch(SolmrException se) 
      {
 	    	messaggioErrorePuntuale = AnagErrors.ERRORE_KO_SEARCH_DOCUMENTI;
 	    	request.setAttribute("messaggioErrorePuntuale", messaggioErrorePuntuale);
 	    	%>
 	      		<jsp:forward page="<%= documentiElencoUrl %>" />
 	    	<%
 	    }
 	    if(elencoDocumenti != null && elencoDocumenti.size() > 0) 
      {
 	    	session.setAttribute("elencoDocumenti", elencoDocumenti);
 	    }
 	    else 
      {
 	    	messaggioErrorePuntuale = AnagErrors.ERRORE_KO_SEARCH_NO_DOCUMENTI_FOUND;
 	    	request.setAttribute("messaggioErrorePuntuale", messaggioErrorePuntuale);
 	    	%>
 	     		<jsp:forward page="<%= documentiElencoUrl %>" />
 	    	<%
 	    }
   	}
  }
 	// L'utente ha selezionato il pulsante "ricerca"
  if(Validator.isNotEmpty(request.getParameter("operazione")))
  { 
   	if(request.getParameter("operazione").equalsIgnoreCase("ricerca")) 
    {
      //Rimuovo i check dei documenti selezionati
      request.removeAttribute("idDocumentoStampaProtocollo");
      
     	// Costruisco il VO per la ricerca
     	// Setto i valori all'interno del VO
     	documentoRicercaVO = new DocumentoVO();
   		TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = null;
   		TipoDocumentoVO tipoDocumentoVO = null;
   		documentoRicercaVO.setCuaa(cuaa);
   		documentoRicercaVO.setIdAzienda(anagAziendaVO.getIdAzienda());
   		if(Validator.isNotEmpty(request.getParameter("idTipologiaDocumento"))) 
      {
   			documentoRicercaVO.setTipoTipologiaDocumento(new CodeDescription(Integer.decode(request.getParameter("idTipologiaDocumento")), null));
   		}
   		else 
      {
   			documentoRicercaVO.setTipoTipologiaDocumento(null);
   		}
   		if(Validator.isNotEmpty(request.getParameter("idCategoriaDocumento"))) 
      {
   			tipoCategoriaDocumentoVO = new TipoCategoriaDocumentoVO();
   			tipoCategoriaDocumentoVO.setIdCategoriaDocumento(Long.decode(request.getParameter("idCategoriaDocumento")));
   			documentoRicercaVO.setTipoCategoriaDocumentoVO(tipoCategoriaDocumentoVO);
   		}
   		else 
      {
   			documentoRicercaVO.setTipoCategoriaDocumentoVO(null);
   		}
   		if(Validator.isNotEmpty(request.getParameter("idTipoDocumento"))) 
      { 
   			tipoDocumentoVO = new TipoDocumentoVO();
   			tipoDocumentoVO.setIdDocumento(Long.decode(request.getParameter("idTipoDocumento")));
     		documentoRicercaVO.setTipoDocumentoVO(tipoDocumentoVO);
   		}
   		else 
      {
   			documentoRicercaVO.setTipoDocumentoVO(null);
   		}
   		if(Validator.isNotEmpty(request.getParameter("idStatoDocumento"))) 
      {
   			documentoRicercaVO.setIdStatoDocumento(Long.decode(request.getParameter("idStatoDocumento")));
   		}
   		else 
      {
   			documentoRicercaVO.setIdStatoDocumento(null);
   		}
   		if(Validator.isNotEmpty(request.getParameter("protocollazione"))) 
      {
      	session.setAttribute("protocollazione", request.getParameter("protocollazione"));
     	}
     	else 
      {
      	session.removeAttribute("protocollazione");
     	}
   		if(Validator.isNotEmpty(request.getParameter("scaduti"))) 
      {
      	documentoRicercaVO.setCheckScaduti(request.getParameter("scaduti"));
      	if(!Validator.isNotEmpty(request.getParameter("dataDal"))) 
        {
       		messaggioErroreData = AnagErrors.ERRORE_CAMPO_OBBLIGATORIO;
       	}
     	}
      else 
      {
      	documentoRicercaVO.setCheckScaduti(null);
      	if(Validator.isNotEmpty(request.getParameter("dataDal"))) 
        {
        	messaggioErroreData = AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE;
        }
       	if(Validator.isNotEmpty(request.getParameter("dataAl"))) 
        {
        	messaggioErroreDataAl = AnagErrors.ERRORE_CAMPO_NON_VALORIZZABILE;
        }
      }
     	if(Validator.isNotEmpty(request.getParameter("dataDal"))) 
      {
     		if(Validator.validateDateF(request.getParameter("dataDal"))) 
        {
     	  	documentoRicercaVO.setMinDataFineValidita(DateUtils.parseDate(request.getParameter("dataDal")));
     	  }
     	  else 
        {
     	  	documentoRicercaVO.setMinDataFineValidita(null);
     	  	request.setAttribute("dataDal", request.getParameter("dataDal"));
     	  	messaggioErroreData = AnagErrors.ERRORE_CAMPO_ERRATO;
     	  }
     	}
     	if(Validator.isNotEmpty(request.getParameter("dataAl"))) 
      {
     		if(Validator.validateDateF(request.getParameter("dataAl"))) 
        {
     	  	if(documentoRicercaVO.getMinDataFineValidita() != null) 
          {
     	   		if(DateUtils.parseDate(request.getParameter("dataAl")).before(documentoRicercaVO.getMinDataFineValidita())) 
            {
  	   	   		documentoRicercaVO.setMaxDataFineValidita(null);
  	   	   		request.setAttribute("dataAl", request.getParameter("dataAl"));
  	   	   		messaggioErroreDataAl = AnagErrors.ERRORE_CAMPO_ERRATO;
     	   		}
     	   		else 
            {
     	   			documentoRicercaVO.setMaxDataFineValidita(DateUtils.parseDate(request.getParameter("dataAl")));
     	   		}
     	   	}
     	   	else 
          {
     	   		documentoRicercaVO.setMaxDataFineValidita(DateUtils.parseDate(request.getParameter("dataAl")));
     	   	}
     	  }
     	  else 
        {
     	  	documentoRicercaVO.setMaxDataFineValidita(null);
     	  	request.setAttribute("dataAl", request.getParameter("dataAl"));
     	  	messaggioErroreDataAl = AnagErrors.ERRORE_CAMPO_ERRATO;
     	  }
     	}
     	session.setAttribute("documentoRicercaVO", documentoRicercaVO);
      // Se ci sono errori li visualizzo
      if(Validator.isNotEmpty(messaggioErroreData) || Validator.isNotEmpty(messaggioErroreDataAl)) 
      {
      	request.setAttribute("messaggioErroreData", messaggioErroreData);
      	request.setAttribute("messaggioErroreDataAl", messaggioErroreDataAl);
      }
      else 
      {
  	  	// Se non ci sono errori effettuo la ricerca
  	   	try 
        {
  	    	elencoDocumenti = anagFacadeClient.searchDocumentiByParameters(documentoRicercaVO, protocollazione, null);
  	    	session.setAttribute("elencoDocumenti", elencoDocumenti);
  	    }
  	    catch(SolmrException se) 
        {
  	    	messaggioErrorePuntuale = AnagErrors.ERRORE_KO_SEARCH_DOCUMENTI;
  	    	request.setAttribute("messaggioErrorePuntuale", messaggioErrorePuntuale);
  	    	%>
  	     		<jsp:forward page="<%= documentiElencoUrl %>" />
  	    	<%
  	    }
  	    if(elencoDocumenti == null || elencoDocumenti.size() == 0) 
        {
  	    	messaggioErrorePuntuale = AnagErrors.ERRORE_KO_SEARCH_NO_DOCUMENTI_FOUND;
  		    request.setAttribute("messaggioErrorePuntuale", messaggioErrorePuntuale);
  	    }
      }
    }
    // L'utente ha selezionato il pulsante "scarica in excel"
  	else if(request.getParameter("operazione").equalsIgnoreCase("scarica")) 
    {
	   	DocumentoFiltroExcelVO documentoFiltroExcelVO = new DocumentoFiltroExcelVO();
	   	documentoFiltroExcelVO.setIntestazione(documentoRicercaVO.getCuaa()+" - "+anagAziendaVO.getDenominazione());
	   	// Recupero la descrizione della tipologia documento
	   	if(documentoRicercaVO.getTipoTipologiaDocumento() != null && documentoRicercaVO.getTipoTipologiaDocumento().getCode() != null) 
      {
	    	String descTipologiaDocumento = null;
	    	try 
        {
	      	descTipologiaDocumento = anagFacadeClient.getDescriptionFromCode(SolmrConstants.TAB_TIPO_TIPOLOGIA_DOCUMENTO, documentoRicercaVO.getTipoTipologiaDocumento().getCode());
	      }
	      catch(SolmrException se) 
        {
	      	messaggioErrore = AnagErrors.ERRORE_KO_TIPO_TIPOLOGIA_DOCUMENTO;
	      	request.setAttribute("messaggioErrore", messaggioErrore);
	      	%>
	       		<jsp:forward page="<%= documentiElencoUrl %>" />
	      	<%
	      }
	      documentoFiltroExcelVO.setDescTipoTipologiaDocumento(descTipologiaDocumento);
	    }
			// Recupero la descrizione della categoria documento
     	if(documentoRicercaVO.getTipoCategoriaDocumentoVO() != null 
        && documentoRicercaVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento() != null) 
      {
      	String descTipoCategoriaDocumento = null;
      	try 
        {
       	  TipoCategoriaDocumentoVO tipoCategoriaDocumentoVO = anagFacadeClient.findTipoCategoriaDocumentoByPrimaryKey(documentoRicercaVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento());
       		descTipoCategoriaDocumento = tipoCategoriaDocumentoVO.getDescrizione();
       	}
       	catch(SolmrException se) 
        {
        	messaggioErrore = AnagErrors.ERRORE_KO_CATEGORIA_DOCUMENTO;
        	request.setAttribute("messaggioErrore", messaggioErrore);
        	%>
         		<jsp:forward page="<%= documentiElencoUrl %>" />
        	<%
       	}
       	documentoFiltroExcelVO.setDescTipoCategoriaDocumento(descTipoCategoriaDocumento);
	    }
			// Recupero la descrizione del tipo documento
     	if(documentoRicercaVO.getTipoDocumentoVO() != null 
        && documentoRicercaVO.getTipoDocumentoVO().getIdDocumento() != null) 
      {
      	String descTipoDocumento = null;
      	try 
        {
       	  TipoDocumentoVO tipoDocumentoVO = anagFacadeClient.findTipoDocumentoVOByPrimaryKey(documentoRicercaVO.getTipoDocumentoVO().getIdDocumento());
       		descTipoDocumento = tipoDocumentoVO.getDescrizione();
       	}
       	catch(SolmrException se) 
        {
        	messaggioErrore = AnagErrors.ERRORE_KO_TIPO_DOCUMENTO;
        	request.setAttribute("messaggioErrore", messaggioErrore);
        	%>
         		<jsp:forward page="<%= documentiElencoUrl %>" />
        	<%
       	}
       	documentoFiltroExcelVO.setDescTipoDocumento(descTipoDocumento);
     	}
     	// Recupero la descrizione dello stato del documento
     	if(Validator.isNotEmpty(documentoRicercaVO.getIdStatoDocumento())) 
      {
      	String descStatoDocumento = null;
      	try 
        {
       		if(documentoRicercaVO.getIdStatoDocumento().compareTo(Long.decode(SolmrConstants.ID_STATO_DOCUMENTO_ATTIVO)) != 0) 
          {
         		descStatoDocumento = anagFacadeClient.getDescriptionFromCode((String)SolmrConstants.get("TAB_TIPO_STATO_DOCUMENTO"), Integer.decode(documentoRicercaVO.getIdStatoDocumento().toString()));
       		}
       		else 
          {
       		  descStatoDocumento = SolmrConstants.DESCRIZIONE_DOCUMENTO_ATTIVO;
       		}
       	}
       	catch(SolmrException se) 
        {
        	messaggioErrore = se.getMessage();
        	request.setAttribute("messaggioErrore", messaggioErrore);
        	%>
         		<jsp:forward page="<%= documentiElencoUrl %>" />
        	<%
       	}
       		
       	documentoFiltroExcelVO.setDescStatoDocumento(descStatoDocumento);
	    }
	    // Recupero la descrizione della protocollazione
	    if(Validator.isNotEmpty(protocollazione)) 
      {
	    	if(protocollazione.equalsIgnoreCase(SolmrConstants.PROTOCOLLAZIONE_EFFETTUATA)) 
        {
	      	protocollazione = "effettuata";
	      }
	      else 
        {
	      	protocollazione = "da effettuare";
	      }
	      documentoFiltroExcelVO.setProtocollazione(protocollazione);
	    }
	
	    if(Validator.isNotEmpty(documentoRicercaVO.getMinDataFineValidita())) 
      {
	    	String dataScadenza = DateUtils.formatDate(documentoRicercaVO.getMinDataFineValidita());
	    	if(documentoRicercaVO.getMaxDataFineValidita() != null) 
        {
	     		dataScadenza += " al "+DateUtils.formatDate(documentoRicercaVO.getMaxDataFineValidita());
	     	}
	      documentoFiltroExcelVO.setDataScadenza(dataScadenza);
	    }
	
	    Vector elencoDocumentiExcel = new Vector();
	
	    for(int i = 0; i < elencoDocumenti.size(); i++) 
      {
     		DocumentoVO documentoVO = (DocumentoVO)elencoDocumenti.elementAt(i);
     		DocumentoExcelVO documentoExcelVO = new DocumentoExcelVO();
     		documentoExcelVO.setDocumentoFiltroExcelVO(documentoFiltroExcelVO);
     		documentoExcelVO.setDescTipoTipologiaDocumento(documentoVO.getTipoTipologiaDocumento().getDescription());
     		documentoExcelVO.setDescTipoCategoriaDocumento(documentoVO.getTipoCategoriaDocumentoVO().getDescrizione());
     		documentoExcelVO.setDescTipoDocumento(documentoVO.getTipoDocumentoVO().getDescrizione());
     		documentoExcelVO.setDataInizioDocumento(DateUtils.formatDate(documentoVO.getDataInizioValidita()));
     		if(Validator.isNotEmpty(documentoVO.getDataFineValidita())) {
       		documentoExcelVO.setDataFineDocumento(DateUtils.formatDate(documentoVO.getDataFineValidita()));
     		}
     		if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo())) {
       		documentoExcelVO.setNumeroProtocollo(StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
     		}
     		if(Validator.isNotEmpty(documentoVO.getDataProtocollo())) {
       		documentoExcelVO.setDataProtocollo(DateUtils.formatDate(documentoVO.getDataProtocollo()));
     		}
     		if(documentoVO.getTipoStatoDocumentoVO() == null || !Validator.isNotEmpty(documentoVO.getTipoStatoDocumentoVO().getDescrizione())) {
       		if(Validator.isNotEmpty(documentoVO.getDataFineValidita()) && documentoVO.getDataFineValidita().before(DateUtils.parseDate(DateUtils.getCurrent()))) {
         			documentoExcelVO.setDescStatoDocumento("Scaduto");
       		}
     		}
     		else {
       		documentoExcelVO.setDescStatoDocumento(documentoVO.getTipoStatoDocumentoVO().getDescrizione());
     		}
     		elencoDocumentiExcel.add(documentoExcelVO);
	    }
	
	    request.setAttribute("elenco", elencoDocumentiExcel);
	    request.setAttribute("anagAziendaVO", anagAziendaVO);
	    request.setAttribute("fileName", "Documenti");
	    request.setAttribute("foglioExcel", "elencoDocumenti");
	    // Vado alla servlet per la gestione del foglio excel
	    %>
	     	<jsp:forward page="<%= excelUrl %>" />
	    <%
   	}
		// L'utente ha selezionato il pulsante esegui controlli
   	else if(request.getParameter("operazione").equalsIgnoreCase("attenderePrego")) 
    {
   		request.setAttribute("action", actionUrl);
    	String operation = "eseguiControlli";
    	request.setAttribute("operazione", operation);
    	%>
		  	<jsp:forward page= "<%= attenderePregoUrl %>" />
      <%
    }
		// L'utente arriva dalla pagina di "attendere prego" dopo la richiesta di
		// esecuzione dei controlli
   	else if(request.getParameter("operazione").equalsIgnoreCase("eseguiControlli")) 
    {
			// Creo l'oggetto Stop Watch per monitorare le operazioni eseguite all'interno del metodo
   		StopWatch watcher = new StopWatch(SolmrConstants.LOGGER_STOPWATCH);
   	  // START
   		watcher.start();
   			
   		// Recupero il parametro DOAT
   		String parametroDOAT = null;
   		try 
      {
   			parametroDOAT = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_DOAT);
   		}
   		catch(SolmrException se) 
      {
      	messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_DOAT;
      	request.setAttribute("messaggioErrore", messaggioErrore);
        %>
        	<jsp:forward page="<%= documentiElencoUrl %>" />
        <%
      }
   			
   		// Se il parametro DOAT == S
   		if(parametroDOAT.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
   			// Recupero l'elenco dei cuaa dei proprietari dei documenti dell'azienda
   			String[] elencoCuaa = null;
   			try 
        {
   				elencoCuaa = anagFacadeClient.getCuaaProprietariDocumentiAzienda(anagAziendaVO.getIdAzienda(), anagAziendaVO.getCUAA(), true);
   			}
   			catch(SolmrException se) 
        {
        	messaggioErrore = AnagErrors.ERRORE_KO_CUAA_PROPRIETARI;
        	request.setAttribute("messaggioErrore", messaggioErrore);
        	%>
        		<jsp:forward page="<%= documentiElencoUrl %>" />
        	<%
       	}	
   			// Se sono stati trovati dei CUAA...
   			if(elencoCuaa != null && elencoCuaa.length > 0) 
        {
   				//... richiamo il servizio di aggiornamento del SIAN
   				try 
          {
   					anagFacadeClient.sianAggiornaDatiTributaria(elencoCuaa, ProfileUtils.getSianUtente(ruoloUtenza));
   				}
   				catch(SolmrException se) 
          {
   	      	messaggioErrore = AnagErrors.ERRORE_KO_SIAN_AGGIORNA_DATI_TRIBUTARIA;
   	      	request.setAttribute("messaggioErrore", messaggioErrore);
   	      	%>
   	      		<jsp:forward page="<%= documentiElencoUrl %>" />
   	      	<%
   	      }	
   			}
				// Primo monitoraggio
   			watcher.dumpElapsed("documentiElencoCtrl.jsp", "sianAggiornaDatiTributaria", "In eseguiControlli method from the beginning to the execution of Sian's method", "List of parameters: elencoCuaa: "+elencoCuaa);
   		}
   		// Richiamo la procedura PL-SQL per i controlli del documentale
   		try 
      {
   			anagFacadeClient.controlliVerificaPLSQL(anagAziendaVO.getIdAzienda(), DateUtils.getCurrentYear(), Integer.decode(SolmrConstants.ID_GRUPPO_CONTROLLO_DOCUMENTALE.toString()), ruoloUtenza.getIdUtente());
				// Secondo monitoraggio
   		  watcher.dumpElapsed("documentiElencoCtrl.jsp", "controlliVerificaPLSQL", "In eseguiControlli method from the beginning to the execution of controlliVerificaPLQSL", "List of parameters: anagAziendaVO.getIdAzienda(): "+anagAziendaVO.getIdAzienda()+" ANNO: "+DateUtils.getCurrentYear()+" ID_GRUPPO_CONTROLLO: "+Integer.decode(SolmrConstants.ID_GRUPPO_CONTROLLO_DOCUMENTALE.toString()));
   		}
   		catch(SolmrException se) 
      {
   			// Uso il messaggio dell'eccezione perchè questa volta è già puntuale
   			messaggioErrore = se.getMessage();
      	request.setAttribute("messaggioErrore", messaggioErrore);
      	%>
      		<jsp:forward page="<%= documentiElencoUrl %>" />
      	<%	
   		}
			// Se non ci sono errori effettuo la ricerca
   	  try 
      {
   	  	elencoDocumenti = anagFacadeClient.searchDocumentiByParameters(documentoRicercaVO, protocollazione, null);
   	  	session.setAttribute("elencoDocumenti", elencoDocumenti);
   	  }
   	  catch(SolmrException se) 
      {
   	  	messaggioErrorePuntuale = AnagErrors.ERRORE_KO_SEARCH_DOCUMENTI;
   	  	request.setAttribute("messaggioErrorePuntuale", messaggioErrorePuntuale);
   	  	%>
   	   		<jsp:forward page="<%= documentiElencoUrl %>" />
   	  	<%
   	  }
   	  if(elencoDocumenti == null || elencoDocumenti.size() == 0) 
      {
   	  	messaggioErrorePuntuale = AnagErrors.ERRORE_KO_SEARCH_NO_DOCUMENTI_FOUND;
   		 	request.setAttribute("messaggioErrorePuntuale", messaggioErrorePuntuale);
   	  }
    }
  }
   	
	// DATA_ESECUZIONE
  String dataEsecuzione = null;
  try 
  {
  	dataEsecuzione = anagFacadeClient.getDataMaxEsecuzioneDocumento(anagAziendaVO.getIdAzienda());
  	if(!Validator.isNotEmpty(dataEsecuzione)) 
    {
   		dataEsecuzione = SolmrConstants.MAX_DATA_ESECUZIONE_NOT_EXECUTED;
   	}
   	else 
    {
   		dataEsecuzione = StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, dataEsecuzione);
   	}
   	request.setAttribute("dataEsecuzione", dataEsecuzione);
  }
  catch(SolmrException se) 
  {
   	messaggioErrore = AnagErrors.ERRORE_KO_DATA_MAX_ESECUZIONE;
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
     	<jsp:forward page="<%= documentiElencoUrl %>" />
   	<%
  }
   	
  // Vado alla pagina di elenco/ricerca dei documenti
  %>
   	<jsp:forward page="<%= documentiElencoUrl %>" />
  