<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.profile.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "elencoFascicoliCtrl.jsp";
  %>
     <%@include file = "/include/autorizzazione.inc" %>
  <%

  String elencoFascicoliUrl = "/view/elencoFascicoliView.jsp";
  String excelUrl = "/servlet/ExcelBuilderServlet";
  String fascicoliUrl = "/view/fascicoliView.jsp";

  Vector elencoAziende = null;
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Vector elencoCAA = null;
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  ValidationErrors errors = new ValidationErrors();

  // Recupero i parametri che mi interessano
  String intermediario = request.getParameter("idIntermediario");
  if(Validator.isNotEmpty(intermediario)) {
    request.setAttribute("idIntermediario", Long.decode(intermediario));
  }
  Long idIntermediario = null;

  try {
    elencoCAA = anagFacadeClient.getElencoCAAByUtente(utenteAbilitazioni);
  }
  catch(SolmrException se) {
    request.setAttribute("messaggioErrore", se.getMessage());
    %>
      <jsp:forward page= "<%= fascicoliUrl %>" />
    <%
  }
  request.setAttribute("elencoCAA",elencoCAA);


  if(Validator.isNotEmpty(intermediario)) {
    idIntermediario = Long.decode(intermediario);

    // Creo il VO con i filtri di ricerca
    DelegaVO delegaVO = new DelegaVO();
    delegaVO.setIdIntermediario(idIntermediario);
    // Effettuo la ricerca
    try {
      elencoAziende = anagFacadeClient.getElencoAziendeByCAA(delegaVO);
    }
    catch(SolmrException se) {
      request.setAttribute("messaggioErrore", se.getMessage());
      %>
        <jsp:forward page= "<%= fascicoliUrl %>" />
      <%
    }
    request.setAttribute("elenco", elencoAziende);
    request.setAttribute("foglioExcel", "elencoFascicoli");
    %>
      <jsp:forward page= "<%= excelUrl %>" />
    <%
  }
  else {
    ValidationError error = new ValidationError((String)AnagErrors.get("ERR_CAA_OBBLIGATORIO"));
    errors.add("idIntermediario",error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(fascicoliUrl).forward(request, response);
    return;
  }
%>
