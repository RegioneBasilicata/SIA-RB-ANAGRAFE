<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%!
  public final static String VIEW = "../view/manodoperaDetView.jsp";
%>

<%

  String iridePageName = "manodoperaDetCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  SolmrLogger.debug(this, " - manodoperaDetCtrl.jsp - INIZIO PAGINA");
  
  ValidationErrors errors = new ValidationErrors();

  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    
    String idManodopera = request.getParameter("idManodopera");
    if(!Validator.isNotEmpty(idManodopera)) 
    {
      ValidationError error = new ValidationError(AnagErrors.ERR_MANODOPERA_NO_SELEZIONATO);
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(VIEW).forward(request, response);
      return;
    }
    // Se è stato selezionato il fabbricato da visualizzare ricerco i suoi dati su DB
    else 
    {
  
      ManodoperaVO manodoperaVO = anagFacadeClient.dettaglioManodopera(Long.decode(idManodopera));
      request.setAttribute("manodoperaVO", manodoperaVO);
    }

    %>
      <jsp:forward page="<%= VIEW %>"/>
    <%
  }
  catch(Exception e)
  {
    if (e instanceof SolmrException)
    {
      setError(request, e.getMessage());
    }
    else
    {
      e.printStackTrace();
      setError(request, "Si è verificato un errore di sistema");
    }
    %><jsp:forward page="<%=VIEW%>" /><%
  }

  SolmrLogger.debug(this, " - manodoperaDetCtrl.jsp - FINE PAGINA");
%>

<%!
  private void setError(HttpServletRequest request, String msg)
  {
    SolmrLogger.error(this, "\n\n\n\n\n\n\n\n\n\n\nmsg="+msg+"\n\n\n\n\n\n\n\n");
    ValidationErrors errors = new ValidationErrors();
    errors.add("error", new ValidationError(msg));
    request.setAttribute("errors", errors);
  }
%>