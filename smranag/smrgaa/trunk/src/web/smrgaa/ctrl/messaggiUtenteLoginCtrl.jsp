<%@ page language="java"  
  contentType="text/html"
  isErrorPage="false"
%>


<%@page import="it.csi.papua.papuaserv.exception.messaggistica.LogoutException"%>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.DettagliMessaggio"%>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi"%>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.iride2.Iride2AbilitazioniVO" %>
<%@page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%
  String iridePageName = "messaggiUtenteLoginCtrl.jsp";
%>
  <%@include file="/include/autorizzazione.inc"%>
<%

  final String VIEW = "../view/messaggiUtenteLoginView.jsp";
  final String LOGOUT = "../layout/force_logout.htm";

  try
  {
    SolmrLogger.debug(this, "messaggiUtenteLoginCtrl BEGIN");
    
    final String APPLICATIVO_HOME = "../layout/indexswhttp.htm";
    final String APPLICATIVO_HOME_AZIENDA = "../layout/indexAziendaswhttp.htm";
    final String APPLICATIVO_HOME_PAGE = "../layout/indexswhttpHome.htm";
    
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");    
    Iride2AbilitazioniVO iride2AbilitazioniVO = (Iride2AbilitazioniVO)session.getAttribute("iride2AbilitazioniVO");
    
    String homePage = APPLICATIVO_HOME;
    
    if (iride2AbilitazioniVO.isUtenteAbilitato("AZIENDE_TITOLARE"))
      homePage = APPLICATIVO_HOME_AZIENDA;
    else
    {      
      if (!iride2AbilitazioniVO.isUtenteAbilitato("RICERCA_AZIENDA"))
        homePage = APPLICATIVO_HOME_PAGE;
    }
    
    
    
    
    
    
    // pulisco sessione dai messaggi di testata (in modo che vengano riletti alla prima occasione)
    session.removeAttribute(SolmrConstants.SESSION_MESSAGGI_TESTATA);

	  String funzione = request.getParameter("funzione");
	
	  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	  ListaMessaggi listaMessaggi = null;
	  try
	  {
		  listaMessaggi = gaaFacadeClient.getListaMessaggi(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE, ruoloUtenza.getCodiceRuolo(), ruoloUtenza.getCodiceFiscale(),ListaMessaggi.FLAG.GENERICO, Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
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
	
    if( listaMessaggi==null || listaMessaggi.getMessaggi()==null 
      || listaMessaggi.getMessaggi().length==0)
    {
      response.sendRedirect(homePage);
      return;
    }
	  if(SolmrConstants.OPERATION_CONFIRM.equals(funzione))
	  {
      ValidationErrors errors = new ValidationErrors();
      errors.add("error", new ValidationError( "Non è possibile continuare: esistono messaggi obbligatori non ancora letti"));
      request.setAttribute("errors", errors);
    }
	
	

    SolmrLogger.debug(this, "messaggiUtenteLoginCtrl END");
  }
  catch(Exception e) 
  {
    SolmrLogger.fatal(this,"\n\n Errore nella pagina messaggiUtenteLoginCtrl.jsp: "+e.toString()+"\n\n");
    ValidationErrors errors = new ValidationErrors();
    errors.add("error", new ValidationError((String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION")));
  }
%>
<jsp:forward page ="<%=VIEW%>" />