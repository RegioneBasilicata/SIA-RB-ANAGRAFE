<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<jsp:useBean id="delegaVO" scope="request" class="it.csi.solmr.dto.anag.DelegaVO"/>


<%

  String iridePageName = "gestoreFascicoloCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  String gestoreFascicoloURL = "/view/gestoreFascicoloView.jsp";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  
  String operazione = request.getParameter("operazione");
  String action = "../layout/gestoreFascicolo.htm";
  
  WebUtils.removeUselessFilter(session, "gestoreFascicoloSIAN");
  
  if("attenderePrego".equalsIgnoreCase(operazione)) 
  {
		request.setAttribute("action", action);
		operazione = null;
		request.setAttribute("operazione", operazione);
		%>
		   <jsp:forward page= "<%= attenderePregoUrl %>" />
		<%
	}
	else 
	{


		AnagAziendaVO anagAziendaVO=(AnagAziendaVO)session.getAttribute("anagAziendaVO");
		ValidationErrors errors = new ValidationErrors();
		AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
		UfficioZonaIntermediarioVO ufficioZonaIntermediarioVO = null;
		Vector elencoUffici = null;

    // Ricerco nuovamente l'azienda per poter avere la situazione aggiornata in relazione
    // alla delega
    try 
    {
      anagAziendaVO = anagFacadeClient.findAziendaAttiva(anagAziendaVO.getIdAzienda());
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(gestoreFascicoloURL).forward(request, response);
      return;
    }
    session.setAttribute("anagAziendaVO", anagAziendaVO);

    //L'utente arriva a questa pagina dal menu
    if(anagAziendaVO.isPossiedeDelegaAttiva()) 
    {
      try 
      {
        delegaVO=anagFacadeClient.getDelegaByAziendaAndIdProcedimento(anagAziendaVO.getIdAzienda(), Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)));
        ufficioZonaIntermediarioVO = anagFacadeClient.findUfficioZonaIntermediarioVOByPrimaryKey(delegaVO.getIdUfficioZonaIntermediario());
      }
      catch(SolmrException se) 
      {
        ValidationError error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(gestoreFascicoloURL).forward(request, response);
        return;
      }
      request.setAttribute("ufficioZonaIntermediarioVO", ufficioZonaIntermediarioVO);
      request.setAttribute("elencoUffici", elencoUffici);
      request.setAttribute("delegaVO",delegaVO);
    }

%>

<jsp:forward page = "<%=gestoreFascicoloURL%>"/>

<% } %>
