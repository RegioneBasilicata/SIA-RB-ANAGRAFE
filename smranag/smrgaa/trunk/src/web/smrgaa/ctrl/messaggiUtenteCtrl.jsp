<%@ page language="java"  
  contentType="text/html"
  isErrorPage="false"
%>


<%@page import="it.csi.papua.papuaserv.exception.messaggistica.LogoutException"%>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.DettagliMessaggio"%>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi"%>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%@page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%
  String iridePageName = "messaggiUtenteCtrl.jsp";
%>
  <%@include file="/include/autorizzazione.inc"%>
<%

  final String VIEW = "../view/messaggiUtenteView.jsp";
  //final String NEXT = "../layout/indexswhttp.shtml";
  final String LOGOUT = "../layout/forceLogOut.shtml";

  try
  {
    SolmrLogger.debug(this, "messaggiUtenteCtrl BEGIN");

    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

	  String funzione = request.getParameter("funzione");
	
	  GaaFacadeClient messaggisticaClient = GaaFacadeClient.getInstance();
	  ListaMessaggi listaMessaggi = null;
	  try
	  {
		  // messaggi obbligatori e non, letti e non letti
		  listaMessaggi = messaggisticaClient.getListaMessaggi(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE, ruoloUtenza.getCodiceRuolo(), ruoloUtenza.getCodiceFiscale(),ListaMessaggi.FLAG.GENERICO, null, null, Boolean.TRUE);
		  request.setAttribute("listaMessaggi", listaMessaggi);
	  }
	  catch(LogoutException ex)
	  {
		  SolmrLogger.error(this, "Forzare il logout");
		  session.setAttribute("LogoutException", ex);
		  response.sendRedirect(LOGOUT);
		  return;
	  }
	  catch(Exception ex)
	  {
		  SolmrLogger.error(this, "Problema nel richiamo dei servizi di messaggistica");
	  }	
	   
  }
  catch(Exception e) 
  {
    SolmrLogger.fatal(this,"\n\n Errore nella pagina storicoMandatiView.jsp: "+e.toString()+"\n\n");
    ValidationErrors errors = new ValidationErrors();
    errors.add("error", new ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
  }
%>
<jsp:forward page ="<%=VIEW%>" />