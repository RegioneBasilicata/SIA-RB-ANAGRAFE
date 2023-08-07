<%@ page language="java"
  contentType="text/html"
  isErrorPage="false"
%>

<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%
  String iridePageName = "visualizzaAllegatoMessaggioCtrl.jsp";
%>
  <%@include file="/include/autorizzazione.inc"%>
<%

  SolmrLogger.info(this, " - visualizzaAllegatoMessaggioCtrl.jsp - INIZIO PAGINA");
  String erroreViewUrl = "/view/erroreView.jsp";

  ValidationErrors errors = new ValidationErrors();

  try
  {
    
    Long idAllegato = new Long(request.getParameter("idAllegato"));
    String nomeFile =request.getParameter("nomeFile");
    
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    
    response.resetBuffer();
    response.setContentType("application/x-download");
    //response.addHeader("Content-Disposition","attachment;filename = "+ nomeFile.replaceAll(" ", "_"));
    response.setHeader("Content-Disposition","attachment;filename=\"" + nomeFile + "\"");
            
    byte[] b = gaaFacadeClient.getAllegato(idAllegato);
            
    if (b != null && b.length > 0)
    {
      response.getOutputStream().write(b);
    }
    response.getOutputStream().flush();
    response.getOutputStream().close();

    SolmrLogger.info(this, " - visualizzaAllegatoMessaggioCtrl.jsp - FINE PAGINA");
             
    return;
  }
  catch (Exception se) 
  {
    SolmrLogger.info(this, " - visualizzaAllegatoMessaggioCtrl.jsp - FINE PAGINA");
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore", messaggio);
    String actionUrl = "../layout/dett_messaggi_utente.htm";
    if("allegaFile".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/allegaFile.htm";
    }
    else if("newInserimentoAllegati".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/newInserimentoAllegati.htm";
    }
    
    request.setAttribute("pageBack", actionUrl);
    
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
   
  }
%>
