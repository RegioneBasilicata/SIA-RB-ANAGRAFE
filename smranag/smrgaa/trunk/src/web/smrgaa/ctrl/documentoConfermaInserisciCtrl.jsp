<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "documentoConfermaInserisciCtrl.jsp";

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

 	String documentoConfermaInserisciUrl = "/view/documentoConfermaInserisciView.jsp";
 	String documentiElencoCtrlUrl = "/ctrl/documentiElencoCtrl.jsp";
 	
 	
 	String regimeDocumentoConfermaInserisci = request.getParameter("regimeDocumentoConfermaInserisci");
 	//Chiudo la pop allegati
 	if(Validator.isNotEmpty(regimeDocumentoConfermaInserisci))
 	{
 	  if (!"stampaConfermaInserisciPdf".equals(request.getParameter("stampaConfermaInserisciPdf")))
    {      
	 	  %>
	      <jsp:forward page="<%= documentiElencoCtrlUrl %>" />
	    <%
	    return;
	  }
 	}

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

 	// Recupero l'id documento
 	//Long idDocumento = (Long)request.getAttribute("idDocumento");
  
  Long idDocumento = Long.decode(request.getParameter("idDocumento"));

 	// Effettuo la ricerca del documento inserito
 	DocumentoVO documentoVO = null;
 	try 
  {
  	documentoVO = anagFacadeClient.findDocumentoVOByPrimaryKey(idDocumento);
 	}
 	catch(SolmrException se) 
  {
  	String messaggioErrore = se.getMessage();
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
      	<jsp:forward page="<%= documentoConfermaInserisciUrl %>" />
   	<%
 	}
 	request.setAttribute("documentoVO", documentoVO);
 	// Vado alla pagina di elenco/ricerca dei documenti
 	%>
  	<jsp:forward page="<%= documentoConfermaInserisciUrl %>" />
 	
