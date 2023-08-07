<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%@ page import="java.util.*" %>

<%

	String iridePageName = "popParticelleEventoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

 	String documentoUrl = "";
 	String urlChiamante = request.getParameter("urlChiamante");
    
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
	if(Validator.isNotEmpty(tipoCategoriaDocumento) && idTipologiaDocumento != null) 
  {
		idCategoriaDocumento = Long.decode(tipoCategoriaDocumento);
 		request.setAttribute("idCategoriaDocumento", idCategoriaDocumento);
 		TipoDocumentoVO[] elencoTipiDocumento = null;
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		try 
    {
 			elencoTipiDocumento = anagFacadeClient.getListTipoDocumentoByIdCategoriaDocumento(idCategoriaDocumento, orderBy, true, new Boolean(cessata));
 		}
 		catch(SolmrException se) 
    {
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
	if(Validator.isNotEmpty(tipoDocumento) && idCategoriaDocumento != null && idTipologiaDocumento != null) 
  {
		idTipoDocumento = Long.decode(tipoDocumento);
		request.setAttribute("idTipoDocumento", idTipoDocumento);
		try 
    {
 			tipoDocumentoVO = anagFacadeClient.findTipoDocumentoVOByPrimaryKey(idTipoDocumento);
 			request.setAttribute("tipoDocumentoVO", tipoDocumentoVO);
 		}
 		catch(SolmrException se) 
    {
 			error = new ValidationError(AnagErrors.ERRORE_KO_TIPO_DOCUMENTO);
	 		errors.add("idTipoDocumento", error);
 			request.setAttribute("errors", errors);
 			request.getRequestDispatcher(documentoUrl).forward(request, response);
 			return;
 		}
	}
	
 	// Ricerco la combo del titolo possesso solo se il tipo documento selezionato è di
 	// tipo territoriale
 	if((tipoDocumentoVO != null && tipoDocumentoVO.getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR)) || !Validator.isNotEmpty(urlChiamante)) 
  {
 		// COMBO TITOLO POSSESSO
 		it.csi.solmr.dto.CodeDescription[] elencoTitoloPossesso = null;
 		try {
    
  		elencoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
 		}
 		catch(SolmrException se) 
    {
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
 	String[] idStoricoParticella = request.getParameterValues("idStoricoParticella");   
      
 	if(urlChiamante.indexOf("documentoInserisci") != -1) 
  {
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
	
      
    
 	// Ricerco i dati richiesti dall'utente
 	try 
  {
    if (idStoricoParticella!=null)
    {
      ParticellaAssVO[] particelleDaAss=(ParticellaAssVO[])session.getAttribute("particelleDaAssociare");
      Vector<ParticellaAssVO> particelleAssociate=(Vector) session.getAttribute("particelleAssociate");
      if (particelleAssociate==null) particelleAssociate=new Vector<ParticellaAssVO>();
      for(int i=0;i<idStoricoParticella.length;i++)
      {
        particelleAssociate.add(particelleDaAss[Integer.parseInt(idStoricoParticella[i])]);
      }
      
      ParticellaAssVO[] temp=(ParticellaAssVO[])particelleAssociate.toArray(new ParticellaAssVO[0]);
      
      
      Arrays.sort(temp, new ParticellaAssVOComparator());
              
      particelleAssociate= new Vector(Arrays.asList(temp));
      
      session.setAttribute("particelleAssociate",particelleAssociate); 
      session.removeAttribute("particelleDaAssociare");
    }
 	}
 	catch(Exception se) 
  {
 		messaggioErrore = se.getMessage();
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
      	<jsp:forward page="<%= documentoUrl %>" />
   	<%
  }
   

   

 	// Torno alla pagina di inserimento o modifica
 	%>
  	<jsp:forward page="<%= documentoUrl %>" />
 	<%

%>
