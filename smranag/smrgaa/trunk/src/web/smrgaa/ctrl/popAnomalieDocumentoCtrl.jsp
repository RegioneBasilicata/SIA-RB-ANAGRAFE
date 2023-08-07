<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "popAnomalieDocumentoCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

  	String popAnomalieDocumentoUrl = "/view/popAnomalieDocumentoView.jsp";
  	String erroreUrl = "/view/erroreView.jsp";

  	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  	Long idDocumento = Long.decode(request.getParameter("idDocumentoPop"));
  	DocumentoVO documentoVO = null;
  	EsitoControlloDocumentoVO[] elencoEsitoControlloDocumento = null;
  	// Recupero il documento
  	try {
  		documentoVO = anagFacadeClient.getDettaglioDocumento(idDocumento, false);
  		request.setAttribute("documentoVO", documentoVO);
  	}
   	catch(SolmrException se) {
    	String messaggio = AnagErrors.ERRORE_KO_DOCUMENTO;
    	request.setAttribute("messaggioErrore",messaggio);
    	session.setAttribute("chiudi", "chiudi");
    	%>
       		<jsp:forward page="<%=erroreUrl%>" />
    	<%
    	return;
 	}
	// Recupero le anomalie del documento
	try {
		String[] orderBy = {SolmrConstants.ORDER_BY_ID_GRUPPO_CONTROLLO_ASC};
		elencoEsitoControlloDocumento = anagFacadeClient.getListEsitoControlloDocumentoByIdDocumento(documentoVO.getIdDocumento(), orderBy);
		request.setAttribute("elencoEsitoControlloDocumento", elencoEsitoControlloDocumento);
	}
	catch(SolmrException se) {
    	String messaggio = AnagErrors.ERRORE_KO_ANOMALIE_DOCUMENTI;
    	request.setAttribute("messaggioErrore",messaggio);
    	session.setAttribute("chiudi", "chiudi");
    	%>
       		<jsp:forward page="<%=erroreUrl%>" />
    	<%
    	return;
 	}
  	// Vado alla pop-up
  	%>
    	<jsp:forward page= "<%= popAnomalieDocumentoUrl %>" />
  	<%

%>

