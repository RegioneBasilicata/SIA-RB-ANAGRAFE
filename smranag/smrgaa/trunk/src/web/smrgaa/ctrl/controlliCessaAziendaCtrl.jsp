<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.util.*,it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "controlliCessaAziendaCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%
 	String url = "/view/cessaAziendaView.jsp";
 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	ValidationErrors errors = new ValidationErrors();

 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	/**
 	 * Inizio controlli con PLSQL
 	 */
 	String result[] = null;
 	try 
 	{
   	result = anagFacadeClient.cessazioneAziendaPLQSL(anagAziendaVO.getIdAzienda());
 	}
 	catch(SolmrException ex) 
 	{
    ValidationError error = new ValidationError(ex.getMessage());
    url = "/view/anagraficaView.jsp";
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(url).forward(request, response);
    return;
 	}

 	if("S".equals(result[0])) 
 	{
    // L'azienda può essere cessata
    boolean common[] = new boolean[2];
    common[0] = true;
    common[1] = true;
    session.setAttribute("common",common);
    url = "/ctrl/cessaAziendaCtrl.jsp";
 	}
 	if("W".equals(result[0])) 
 	{
    boolean common[] = new boolean[2];
    common[0] = true;
    common[1] = true;
    session.setAttribute("common",common);
    //Prima di cessare l'azienda bisogna chiedere all'utente
    request.setAttribute("p_msg",result[1]);
    url = "/view/confermaCessaAziendaPLSQLView.jsp";
 	}
 	if("B".equals(result[0])) 
 	{
    boolean common[] = new boolean[2];
    common[0] = false;
    common[1] = false;
    session.setAttribute("common",common);
    // L'azienda non può essere cessata
    request.setAttribute("p_msg",result[1]);
    url = "/view/confermaCessaAziendaNOView.jsp";
 	}
 	/**
 	 * Fine controlli con PLSQL
 	 */

%>
<jsp:forward page="<%=url%>"/>