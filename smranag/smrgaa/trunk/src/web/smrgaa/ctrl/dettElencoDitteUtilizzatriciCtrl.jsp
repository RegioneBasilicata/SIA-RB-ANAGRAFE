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

  String dettElencoDitteUtilizzatriciUrl = "/view/dettElencoDitteUtilizzatriciView.jsp";
  String actionUrl = "../layout/motori_agricoli_incarico.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  final String errMsg = "Impossibile procedere nella sezione ditte utilizzatrici dettaglio. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  String iridePageName = "dettElencoDitteUtilizzatriciCtrl.jsp";

  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  
  
  try 
  {
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
    
    
    
    String idPossessoMacchinaElStr = request.getParameter("idPossessoMacchinaEl");
    Long idPossessoMacchina = null;
    if(Validator.isNotEmpty(idPossessoMacchinaElStr))
      idPossessoMacchina = new Long(idPossessoMacchinaElStr);
    else
      throw new SolmrException("idPossessoMacchinaEl a null");
    
    PossessoMacchinaVO possessoMacchinaVO = gaaFacadeClient.getPosessoMacchinaFromId(idPossessoMacchina.longValue());
    request.setAttribute("possessoMacchinaVO", possessoMacchinaVO);
    
    UteVO uteVO = anagFacadeClient.getUteById(possessoMacchinaVO.getIdUte());
    
    Vector<PossessoMacchinaVO> vPossessoMacchina = gaaFacadeClient
      .getElencoPossessoDitteUtilizzatrici(possessoMacchinaVO.getMacchinaVO().getIdMacchina(),uteVO.getIdAzienda().longValue());
    request.setAttribute("vPossessoMacchina", vPossessoMacchina);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " -dettElencoDitteUtilizzatriciCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_DETTAGLIO_DATI_UMA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
%>
<jsp:forward page="<%=dettElencoDitteUtilizzatriciUrl%>" />
