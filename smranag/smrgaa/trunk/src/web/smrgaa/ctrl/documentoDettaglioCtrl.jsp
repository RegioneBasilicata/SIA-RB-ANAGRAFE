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
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "documentoDettaglioCtrl.jsp";

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

 	String documentoDettaglioUrl = "/view/documentoDettaglioView.jsp";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	String messaggioErrore = null;
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

 	// Recupero l'id documento selezionato
 	Long idDocumento = Long.decode(request.getParameter("idDocumento"));
 	String operazione = request.getParameter("operazione");
 	boolean onlyActive = false;
 	if(!Validator.isNotEmpty(operazione)) 
  {
 		onlyActive = true;
 		request.setAttribute("legamiAttivi", SolmrConstants.FLAG_S);
 	}
 	else 
  {
 		if(Validator.isNotEmpty(request.getParameter("legamiAttivi"))) 
    {
 			onlyActive = true;
 			request.setAttribute("legamiAttivi", SolmrConstants.FLAG_S);
 		}
 	}
 	// Setto l'area di provenienza
 	request.setAttribute("pageFrom", "documentale");
 	// Ricerco il VO su DB
 	DocumentoVO documentoVO = null;
 	try 
  {
  	documentoVO = anagFacadeClient.getDettaglioDocumento(idDocumento, onlyActive);
    //se il documento è di tipo correttiva terreni devo caricare anche le particelle associate
    if(documentoVO.getTipoDocumentoVO() != null 
        && documentoVO.getTipoDocumentoVO()
     .getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))
    {
      documentoVO.setParticelleAssociate(anagFacadeClient.getParticelleDocCor(idDocumento));
    }
 	}
 	catch(SolmrException se) 
  {
  	messaggioErrore = AnagErrors.ERRORE_KO_DOCUMENTO;
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
      <jsp:forward page="<%= documentoDettaglioUrl %>" />
   	<%
 	}
 	// Metto l'oggetto in request
 	request.setAttribute("documentoVO", documentoVO);
 	
  //  Ricerco la combo del titolo possesso e del caso particolare
  // solo se il tipo documento selezionato è di tipo territoriale
  if(documentoVO.getTipoDocumentoVO() != null 
    && (documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_TERRENI)
    || documentoVO.getTipoDocumentoVO()
      .getFlagAnagTerr().equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CORR_TERR))) 
  {
 		// COMBO TITOLO POSSESSO
 		it.csi.solmr.dto.CodeDescription[] elencoTitoloPossesso = null;
 		try 
    {
  		elencoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
 		}
 		catch(SolmrException se) 
    {
  		messaggioErrore = se.getMessage();
   		request.setAttribute("messaggioErrore", messaggioErrore);
   		%>
      	<jsp:forward page="<%= documentoDettaglioUrl %>" />
   		<%
 		}
 		request.setAttribute("elencoTitoloPossesso", elencoTitoloPossesso);
   		
 		// COMBO CASO PARTICOLARE
 		it.csi.solmr.dto.CodeDescription[] elencoCasoParticolare = null;
 		try 
    {
 			elencoCasoParticolare = anagFacadeClient.getListTipoCasoParticolare(SolmrConstants.ORDER_BY_GENERIC_CODE);
 		}
 		catch(SolmrException se) 
    {
  		messaggioErrore = AnagErrors.ERRORE_KO_CASI_PARTICOLARI;
   		request.setAttribute("messaggioErrore", messaggioErrore);
   		%>
      	<jsp:forward page="<%= documentoDettaglioUrl %>" />
   		<%
 		}
 		request.setAttribute("elencoCasoParticolare", elencoCasoParticolare);
 	}
  else if(documentoVO.getTipoDocumentoVO() != null 
    && documentoVO.getTipoDocumentoVO().getFlagAnagTerr()
      .equalsIgnoreCase(SolmrConstants.FLAG_ANAG_TERR_DOCUMENTI_CONTI_CORRENTI)) 
  {
    if(documentoVO.getIdContoCorrente() != null)
    {
      try
      {
        ContoCorrenteVO contoCorrenteVO = anagFacadeClient
          .getContoCorrente(documentoVO.getIdContoCorrente().toString());
        request.setAttribute("contoCorrenteVO",contoCorrenteVO);
      }
      catch(SolmrException s)
      {
        messaggioErrore = AnagErrors.ERR_DETTAGLIO_DOCUMENTO_CONTO_CORRENTE;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>
          <jsp:forward page="<%= documentoDettaglioUrl %>" />
        <%
      }
    
    }
  
  }
   	
 	// Vado alla pagina del dettaglio
 	%>
  	<jsp:forward page="<%= documentoDettaglioUrl %>" />
 
