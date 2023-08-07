<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="java.util.*" %>
<%

  String iridePageName = "eliminaCorrezioneCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  SolmrLogger.debug(this, " - \n\n\neliminaCorrezioneCtrl.jsp - - INIZIO PAGINA\n\n\n");

  String dichiarazioneAnomaliaUrl = "/view/dichiarazioneAnomaliaView.jsp";

  try
  {
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    Long idMotivoDichiarazione = (Long)session.getAttribute("idMotivoDichiarazione");

    String elencoIdDichiarazioneSegnalazione[] = request.getParameterValues("idDichiarazioneSegnalazione");

    anagFacadeClient.deleteDichiarazioneCorrezione(elencoIdDichiarazioneSegnalazione);

    Vector anomalie = anagFacadeClient.getErroriAnomalieDichiarazioneConsistenza(anagAziendaVO.getIdAzienda(),idMotivoDichiarazione);
    if(anomalie != null)
      session.setAttribute("anomalieDichiarazioniConsistenza", anomalie);
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
  }
  %>
    <jsp:forward page="<%=dichiarazioneAnomaliaUrl%>" />
  <%
  SolmrLogger.debug(this, " - eliminaCorrezioneCtrl.jsp - FINE PAGINA");
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
