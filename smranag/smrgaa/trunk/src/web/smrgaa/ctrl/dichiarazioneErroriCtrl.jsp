<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>

<%!
  public final static String VIEW = "../view/dichiarazioneErroriView.jsp";
%>

<%

  String iridePageName = "dichiarazioneErroriCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  SolmrLogger.debug(this, " - dichiarazioneErroriCtrl.jsp - INIZIO PAGINA");

  try
  {
    Long idMotivoDichiarazione = (Long)session.getAttribute("idMotivoDichiarazione");
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

    SolmrLogger.debug(this, "radiobutton: " + request.getParameter("radiobutton"));

    //Recupero le evntuali anomalie
    Vector errori = anagFacadeClient.getErroriAnomalieDichiarazioneConsistenza(anagAziendaVO.getIdAzienda(),idMotivoDichiarazione);

    request.setAttribute("erroriDichiarazioniConsistenza", errori);

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
  SolmrLogger.debug(this, " - dichiarazioneErroriCtrl.jsp - FINE PAGINA");
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