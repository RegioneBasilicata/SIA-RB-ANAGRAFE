<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "nuovaNotificaConfermaCtrl.jsp";

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

 	String nuovaNotificaConfermaUrl = "/view/nuovaNotificaConfermaView.jsp";
 	String notificheCtrlUrl = "/ctrl/notificheCtrl.jsp";
 	
 	
 	String regimeNuovaNotificaConferma = request.getParameter("regimeNuovaNotificaConferma");
  //Chiudo la pop allegati
  if(Validator.isNotEmpty(regimeNuovaNotificaConferma))
  {
    %>
      <jsp:forward page="<%= notificheCtrlUrl %>" />
    <%
    return;    
  }

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

 	// Recupero l'id documento
 	//Long idDocumento = (Long)request.getAttribute("idDocumento");
 	
 	String doc = request.getParameter("idDocumento");
  
  Long idNotifica = Long.decode(request.getParameter("idNotifica"));

 	// Effettuo la ricerca del documento inserito
 	NotificaVO notificaVO = null;
 	try 
  {
  	notificaVO = anagFacadeClient.findNotificaByPrimaryKey(idNotifica, null);
 	}
 	catch(SolmrException se) 
  {
  	String messaggioErrore = se.getMessage();
   	request.setAttribute("messaggioErrore", messaggioErrore);
   	%>
      	<jsp:forward page="<%= nuovaNotificaConfermaUrl %>" />
   	<%
 	}
 	request.setAttribute("notificaVO", notificaVO);
 	// Vado alla pagina di elenco/ricerca dei documenti
 	%>
  	<jsp:forward page="<%= nuovaNotificaConfermaUrl %>" />
 	
