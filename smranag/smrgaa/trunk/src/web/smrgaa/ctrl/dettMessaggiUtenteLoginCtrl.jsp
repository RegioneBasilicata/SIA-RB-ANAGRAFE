<%@page import="it.csi.papua.papuaserv.dto.messaggistica.DettagliMessaggio"%>
<%@page import="it.csi.papua.papuaserv.exception.messaggistica.LogoutException"%>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi"%>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@page import="it.csi.solmr.dto.profile.RuoloUtenza"%>
<%@page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%@ page language="java"
  contentType="text/html"
  isErrorPage="false"
%>

<%
  String iridePageName = "dettMessaggiUtenteLoginCtrl.jsp";
%>
  <%@include file="/include/autorizzazione.inc"%>
<%


  final String VIEW = "../view/dettMessaggiUtenteLoginView.jsp";
  //final String LOGOUT = "../layout/logOut.shtml";
  final String CONFERMA="../layout/";

  SolmrLogger.info(this, " - dettMessaggiUtenteLoginCtrl.jsp - INIZIO PAGINA");

  try
  {
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();

	  DettagliMessaggio dettagliMessaggio = null;
	  String funzione = request.getParameter("funzione");
	  if(Validator.isEmpty(funzione))
	  {
		  String idElencoMessaggi = request.getParameter("idElencoMessaggi");
		  String chiamante = request.getParameter("chiamante");
		  dettagliMessaggio = gaaFacadeClient.getDettagliMessaggio(new Long(idElencoMessaggi).longValue(), ruoloUtenza.getCodiceFiscale());
		  request.setAttribute("dettagliMessaggio", dettagliMessaggio);
		  request.setAttribute("chiamante", chiamante);
	 	  session.setAttribute("dettagliMessaggio", dettagliMessaggio);
	  }
	  else if(SolmrConstants.OPERATION_CONFIRM.equals(funzione))
	  {
		  dettagliMessaggio = (DettagliMessaggio)session.getAttribute("dettagliMessaggio");
		
		  if(dettagliMessaggio.isLetturaObbligatoria())
		  {
			  if(!dettagliMessaggio.isLetto())
			  {
				  if(SolmrConstants.FLAG_S.equals(request.getParameter("flagDichLettura")))
				  {
					  gaaFacadeClient.confermaLetturaMessaggio(dettagliMessaggio.getIdElencoMessaggi(), ruoloUtenza.getCodiceFiscale());
					  // forzo il reload dei messaggi di testata (con il conteggio dei messaggi letti)
					  session.removeAttribute(SolmrConstants.SESSION_MESSAGGI_TESTATA);
				  }
			  }
		  }
		  else
		  {
			  gaaFacadeClient.confermaLetturaMessaggio(dettagliMessaggio.getIdElencoMessaggi(), ruoloUtenza.getCodiceFiscale());
			  // forzo il reload dei messaggi di testata (con il conteggio dei messaggi letti)
			  session.removeAttribute(SolmrConstants.SESSION_MESSAGGI_TESTATA);
		  }
		
		  response.sendRedirect(CONFERMA + request.getParameter("chiamante"));
		  return;
	  }
	
    
    
  }
  catch(Exception e) 
  {
    SolmrLogger.fatal(this,"\n\n Errore nella pagina storicoMandatiView.jsp: "+e.toString()+"\n\n");
    ValidationErrors errors = new ValidationErrors();
    errors.add("error", new ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
  }
  %><jsp:forward page="<%=VIEW%>"/><%

  SolmrLogger.info(this, " - dettMessaggiUtenteLoginCtrl.jsp - FINE PAGINA");
%>