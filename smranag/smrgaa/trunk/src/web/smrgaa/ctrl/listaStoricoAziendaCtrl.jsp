<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.Vector" %>
<%

  String iridePageName = "listaStoricoAziendaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
   String listaStoricoAziendaUrl = "../view/listaStoricoAziendaView.jsp";

   ValidationErrors errors = new ValidationErrors();

   AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

   AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

   if(anagAziendaVO == null) {
     ValidationError error = new ValidationError((String)AnagErrors.get("ERR_SISTEMA"));
     errors.add("error",error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(listaStoricoAziendaUrl).forward(request, response);
     return;
   }

   Long idAzienda = anagAziendaVO.getIdAzienda();

   // Se l'azienda è proveniente da un'altra azienda allora ricerco i dati dell'azienda
   // di provenienza per visualizzarli nella pagina contenente le variazioni
   // storiche
   if(Validator.isNotEmpty(anagAziendaVO.getIdAziendaSubentro())) {
     AnagAziendaVO anagAziendaProvenienzaVO = null;
     try {
       anagAziendaProvenienzaVO = anagFacadeClient.getAziendaByIdAzienda(anagAziendaVO.getIdAziendaSubentro());
     }
     catch(SolmrException se) {
       ValidationError error = new ValidationError(se.getMessage());
       errors.add("error",error);
       request.setAttribute("errors", errors);
       request.getRequestDispatcher(listaStoricoAziendaUrl).forward(request, response);
       return;
     }
     request.setAttribute("anagAziendaProvenienzaVO", anagAziendaProvenienzaVO);
   }

   Vector listaAziende = null;
   try {
     listaAziende = anagFacadeClient.getListaStoricoAzienda(idAzienda);
   }
   catch(SolmrException se) {
     ValidationError error = new ValidationError(se.getMessage());
     errors.add("error",error);
     request.setAttribute("errors", errors);
     request.getRequestDispatcher(listaStoricoAziendaUrl).forward(request, response);
     return;
   }

   session.setAttribute("listaStoricoAziende",listaAziende);

   %>
      <jsp:forward page="<%= listaStoricoAziendaUrl %>"/>
   <%

%>

