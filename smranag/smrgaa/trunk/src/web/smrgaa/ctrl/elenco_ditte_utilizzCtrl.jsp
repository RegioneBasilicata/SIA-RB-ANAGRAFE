<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String caratteristicheFisicheUrl = "/view/elenco_ditte_utilizzView.jsp";
  String actionUrl = "../layout/motori_agricoli_incarico.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  final String errMsg = "Impossibile procedere nella sezione elenco ditte utilizzatrici. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  String iridePageName = "elenco_ditte_utilizzCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  
  
  try 
  {
  
    String idPossessoMacchinaStr = request.getParameter("idPossessoMacchina");
    Long idPossessoMacchina = null;
    if(Validator.isNotEmpty(idPossessoMacchinaStr))
      idPossessoMacchina = new Long(idPossessoMacchinaStr);
    else
      throw new SolmrException("idPossessoMacchina a null");
  
    PossessoMacchinaVO possessoMacchinaVO = gaaFacadeClient.getPosessoMacchinaFromId(idPossessoMacchina.longValue());
    request.setAttribute("possessoMacchinaVO", possessoMacchinaVO);
    
    Vector<PossessoMacchinaVO> vPossessoMacchina = gaaFacadeClient
      .getElencoDitteUtilizzatrici(possessoMacchinaVO.getMacchinaVO().getIdMacchina(), possessoMacchinaVO.getDataScarico());
    request.setAttribute("vPossessoMacchina", vPossessoMacchina);
    
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - elenco_ditta_utilizzCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_DATI_UMA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
%>

<jsp:forward page="<%=caratteristicheFisicheUrl%>" />

