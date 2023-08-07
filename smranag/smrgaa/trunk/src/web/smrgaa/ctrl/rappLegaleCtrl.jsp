<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.Date" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%

  String iridePageName = "rappLegaleCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String url = "/view/rappLegaleView.jsp";
  PersonaFisicaVO personaVO = null;
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  if (anagVO!=null) {
    AnagFacadeClient client = new AnagFacadeClient();
    try {
      personaVO = client.getTitolareORappresentanteLegaleAzienda(anagVO.getIdAzienda(), DateUtils.parseDate(anagVO.getDataSituazioneAlStr()));
      session.setAttribute("personaVO", personaVO);
    }
    catch (SolmrException ex) {
      ValidationErrors errors = new ValidationErrors();
      ValidationError error = new ValidationError(ex.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(url).forward(request, response);
      return;
    }
  }
%>
<jsp:forward page="<%=url%>"/>
