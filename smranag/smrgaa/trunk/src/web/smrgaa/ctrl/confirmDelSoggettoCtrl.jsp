<%@ page language="java"
         contentType="text/html"
%>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>


<%

  String iridePageName = "confirmDelSoggettoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  String url = "/layout/contitolari.htm";

  AnagFacadeClient client = new AnagFacadeClient();
  AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

  PersonaFisicaVO personaVO = null;
  Long idContitolare = null;
  ValidationError error = null;
  ValidationErrors errors = null;

  if(request.getParameter("submit")!= null) {
    if(session.getAttribute("idContitolare")!= null && !session.getAttribute("idContitolare").equals("")) {
      idContitolare = new Long(""+session.getAttribute("idContitolare"));
      try {
        client.checkForDeleteSoggetto(idContitolare);
      }
      catch(SolmrException se) {
        error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(url).forward(request, response);
        return;
      }
    }
  }
  else if(request.getParameter("submit2")!=null){
    url = "/layout/contitolari.htm";
  }
  else {
    url = "/view/confirmDelSoggettoView.jsp";
  }
%>
<jsp:forward page="<%=url%>"/>
