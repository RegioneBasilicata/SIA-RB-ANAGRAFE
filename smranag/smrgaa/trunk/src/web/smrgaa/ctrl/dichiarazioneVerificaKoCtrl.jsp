<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>

<%!
  public final static String VIEW = "../view/dichiarazioneVerificaKoView.jsp";
%>

<%

  String iridePageName = "dichiarazioneVerificaKoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  SolmrLogger.debug(this, " - dichiarazioneVerificaKoCtrl.jsp - INIZIO PAGINA");

  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

    //Recupero le evntuali anomalie
    Vector anomalie = anagFacadeClient.getErroriAnomalieDichiarazioneConsistenza(anagAziendaVO.getIdAzienda(),null);
    request.setAttribute("anomalieDichiarazioniConsistenza", anomalie);

    %><jsp:forward page="<%= VIEW %>"/><%
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
  SolmrLogger.debug(this, " - dichiarazioneVerificaKoCtrl.jsp - FINE PAGINA");
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
