<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%@ page import="java.util.*" %>

<%

	String iridePageName = "popCercaParticellaCessataCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

   	String documentoUrl = "";
   	String urlChiamante = request.getParameter("urlChiamante");
   	DocumentoVO documentoVO = (DocumentoVO)session.getAttribute("documentoModificaVO");
    
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    boolean cessata=false;
    if (anagAziendaVO.getDataCessazione()!=null) cessata=true;
    

   	if(urlChiamante.indexOf("documentoInserisci") != -1) {
    	documentoUrl = "/view/documentoInserisciView.jsp";
   	}
   	else {
    	documentoUrl = "/view/documentoModificaView.jsp";
   	}

   	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   	String messaggioErrore = null;
   	ValidationErrors errors = new ValidationErrors();
   	ValidationError error = null;
   	StoricoParticellaVO[] elencoParticelle = null;
   	if(documentoVO == null) 
    {
   		elencoParticelle = null;
   	}
   	else {
   		// Se il documento selezionato aveva già delle particelle associate
   		if(documentoVO.getElencoParticelle() != null) {
   			elencoParticelle = (StoricoParticellaVO[])documentoVO.getElencoParticelle().toArray(new StoricoParticellaVO[documentoVO.getElencoParticelle().size()]);
   		}
   		// In caso contrario...
   		else {
   			//... creo un array di size 0 al quale inserire poi le particelle selezionate
   			Vector elenco = new Vector();
   			elencoParticelle = (StoricoParticellaVO[])elenco.toArray(new StoricoParticellaVO[0]);   			
   		}
   	}
   	
	// COMBO TIPO TIPOLOGIA DOCUMENTO
   	Vector elencoTipiTipologiaDocumento = null;
   	try {
   		elencoTipiTipologiaDocumento = anagFacadeClient.getTipiTipologiaDocumento(cessata);
   	}
   	catch(SolmrException se) {
    	messaggioErrore = se.getMessage();
     	request.setAttribute("messaggioErrore", messaggioErrore);
     	%>
        	<jsp:forward page="<%= documentoUrl %>" />
     	<%
   	}
   	request.setAttribute("elencoTipiTipologiaDocumento", elencoTipiTipologiaDocumento);
	
   	// COMBO TIPO DOCUMENTO
	String tipoTipologiaDocumento = request.getParameter("idTipologiaDocumento");
	Long idTipologiaDocumento = null;
	if(Validator.isNotEmpty(tipoTipologiaDocumento)) {
 		idTipologiaDocumento = Long.decode(tipoTipologiaDocumento);
 		request.setAttribute("idTipologiaDocumento", idTipologiaDocumento);
 		TipoCategoriaDocumentoVO[] elencoTipiCategoriaDocumento = null;
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		try {
 			elencoTipiCategoriaDocumento = anagFacadeClient.getListTipoCategoriaDocumentoByIdTipologiaDocumento(idTipologiaDocumento, orderBy);
 		}
 		catch(SolmrException se) {
   			error = new ValidationError(AnagErrors.ERRORE_KO_CATEGORIA_DOCUMENTO);
   			errors.add("idCategoriaDocumento", error);
   			request.setAttribute("errors", errors);
   			request.getRequestDispatcher(documentoUrl).forward(request, response);
       		return;
 		}
 		request.setAttribute("elencoTipiCategoriaDocumento", elencoTipiCategoriaDocumento);
	}
	
	// COMBO DESCRIZIONE DOCUMENTO
	String tipoCategoriaDocumento = request.getParameter("idCategoriaDocumento");
	Long idCategoriaDocumento = null;
	if(Validator.isNotEmpty(tipoCategoriaDocumento) && idTipologiaDocumento != null) {
		idCategoriaDocumento = Long.decode(tipoCategoriaDocumento);
 		request.setAttribute("idCategoriaDocumento", idCategoriaDocumento);
 		TipoDocumentoVO[] elencoTipiDocumento = null;
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		try {
 			elencoTipiDocumento = anagFacadeClient.getListTipoDocumentoByIdCategoriaDocumento(idCategoriaDocumento, orderBy, true, new Boolean(cessata));
 		}
 		catch(SolmrException se) {
   			error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_DOCUMENTO);
   			errors.add("idTipoDocumento", error);
   			request.setAttribute("errors", errors);
   			request.getRequestDispatcher(documentoUrl).forward(request, response);
       		return;
 		}
 		request.setAttribute("elencoTipiDocumento", elencoTipiDocumento);
	}
   	
	// Recupero il tipo documento per la corretta gestione dei dati dell'inserimento
	String tipoDocumento = request.getParameter("idTipoDocumento");
	Long idTipoDocumento = null;
	TipoDocumentoVO tipoDocumentoVO = null;
	if(Validator.isNotEmpty(tipoDocumento) && idCategoriaDocumento != null && idTipologiaDocumento != null) {
		idTipoDocumento = Long.decode(tipoDocumento);
		request.setAttribute("idTipoDocumento", idTipoDocumento);
		try {
   			tipoDocumentoVO = anagFacadeClient.findTipoDocumentoVOByPrimaryKey(idTipoDocumento);
   			request.setAttribute("tipoDocumentoVO", tipoDocumentoVO);
 		}
 		catch(SolmrException se) {
   			error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_DOCUMENTO);
  	 		errors.add("idTipoDocumento", error);
   			request.setAttribute("errors", errors);
   			request.getRequestDispatcher(documentoUrl).forward(request, response);
   			return;
 		}
	}
	
   	// Ricerco la combo del titolo possesso solo se il tipo documento selezionato è di
   	// tipo territoriale
   	if((tipoDocumentoVO != null && tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) || !Validator.isNotEmpty(urlChiamante)) {
   		// COMBO TITOLO POSSESSO
   		it.csi.solmr.dto.CodeDescription[] elencoTitoloPossesso = null;
   		try {
    		elencoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
   		}
   		catch(SolmrException se) {
    		messaggioErrore = se.getMessage();
     		request.setAttribute("messaggioErrore", messaggioErrore);
     		%>
        		<jsp:forward page="<%= documentoUrl %>" />
     		<%
   		}
   		request.setAttribute("elencoTitoloPossesso", elencoTitoloPossesso);
   		
		// COMBO CASO PARTICOLARE
   		it.csi.solmr.dto.CodeDescription[] elencoCasoParticolare = null;
   		try {
   			elencoCasoParticolare = anagFacadeClient.getListTipoCasoParticolare(SolmrConstants.ORDER_BY_GENERIC_CODE);
   		}
   		catch(SolmrException se) {
    		messaggioErrore = AnagErrors.ERRORE_KO_CASI_PARTICOLARI;
     		request.setAttribute("messaggioErrore", messaggioErrore);
     		%>
        		<jsp:forward page="<%= documentoUrl %>" />
     		<%
   		}
   		request.setAttribute("elencoCasoParticolare", elencoCasoParticolare);
   	}

   	// Recupero i valori selezionati
   	String[] conduzioniSelezionate = request.getParameterValues("idConduzioneParticella");    
   	if(urlChiamante.indexOf("documentoInserisci") != -1) {
	   	request.setAttribute("idTipologiaDocumento", Long.decode(request.getParameter("idTipologiaDocumento")));
	    request.setAttribute("idCategoriaDocumento", Long.decode(request.getParameter("idCategoriaDocumento")));
	    request.setAttribute("idTipoDocumento", Long.decode(request.getParameter("idTipoDocumento")));
   	}
    request.setAttribute("dataInizioValidita", request.getParameter("dataInizioValidita"));
    request.setAttribute("dataFineValidita", request.getParameter("dataFineValidita"));
    request.setAttribute("protocolla", request.getParameter("protocolla"));
    request.setAttribute("numeroProtocolloEsterno", request.getParameter("numeroProtocolloEsterno"));
    request.setAttribute("note", request.getParameter("note"));
    request.setAttribute("urlChiamante", request.getParameter("urlChiamante"));
	
    Vector temp = new Vector();
    if(elencoParticelle != null && elencoParticelle.length > 0) {
    	for(int i = 0; i < elencoParticelle.length; i++) {
    		temp.add((StoricoParticellaVO)elencoParticelle[i]);
    	}
    }
   	// Ricerco i dati richiesti dall'utente
   	boolean isCensite = false;
   	try 
    {
      String idPianoRiferimento = (String)request.getParameter("idPianoRiferimento");
      long idDichiarazioneConsistenza=-1;
      try
      { 
        idDichiarazioneConsistenza=Long.parseLong(idPianoRiferimento);
      }
      catch(Exception e){}   
    	for(int i = 0; i < conduzioniSelezionate.length; i++) 
      {
        StoricoParticellaVO storicoParticellaVO =null;
    		int countParticelleGiaCensite = 0;
        if (idPianoRiferimento!=null && !idPianoRiferimento.equals("0"))
        {
          long idConduzioneDichiarata=anagFacadeClient.getIdConduzioneDichiarata(Long.parseLong((String)conduzioniSelezionate[i]),idDichiarazioneConsistenza);
          storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneDichiarata(new Long(idConduzioneDichiarata));
        }
        else
        {
          storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneParticella(Long.decode((String)conduzioniSelezionate[i]));
        }
     
     		ConduzioneParticellaVO conduzioneParticellaVO = anagFacadeClient.findConduzioneParticellaByPrimaryKey(Long.decode((String)conduzioniSelezionate[i]));
     		storicoParticellaVO.setElencoConduzioni((ConduzioneParticellaVO[])new ConduzioneParticellaVO[]{conduzioneParticellaVO});
     		if(temp.size() == 0) {
       		temp.add(storicoParticellaVO);
     		}
     		else {
       		for(int j = 0; j < temp.size(); j++) {
         			StoricoParticellaVO storicoParticellaElencoVO = (StoricoParticellaVO)temp.elementAt(j);
         			if(storicoParticellaElencoVO.getElencoConduzioni()[0].getIdConduzioneParticella().compareTo(storicoParticellaVO.getElencoConduzioni()[0].getIdConduzioneParticella()) == 0) {
           			countParticelleGiaCensite++;
           			isCensite = true;
           			break;
         			}
       		}
       		if(countParticelleGiaCensite == 0) {
         			temp.add(storicoParticellaVO);
       		}
     		}
     	}
   	}
   	catch(SolmrException se) {
   		messaggioErrore = se.getMessage();
     	request.setAttribute("messaggioErrore", messaggioErrore);
     	%>
        	<jsp:forward page="<%= documentoUrl %>" />
     	<%
	}
   	if(documentoVO == null) {
    	session.setAttribute("elencoParticelle", (StoricoParticellaVO[])temp.toArray(new StoricoParticellaVO[temp.size()]));
   	}
  	else {
    	documentoVO.setElencoParticelle(temp);
   	}

   	// Comunico l'eventuale selezione di particelle precedentemente selezionate
   	if(isCensite) {
    	error = new ValidationError(AnagErrors.ERRORE_PARTICELLA_DOCUMENTO_GIA_INSERITO);
     	errors.add("error", error);
     	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(documentoUrl).forward(request, response);
     	return;
   	}
    
    session.removeAttribute("particelleAssociate");

   	// Torno alla pagina di inserimento o modifica
   	%>
    	<jsp:forward page="<%= documentoUrl %>" />
   	<%

%>
