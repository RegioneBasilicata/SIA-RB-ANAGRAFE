<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "contiCorrentiDetCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  String actionUrl = "../layout/conti_correnti.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione dettaglio dei conti correnti. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  
  
  
  String obbligoGF = null;
  try 
  {
    if((anagAziendaVO.getTipoFormaGiuridica() != null)
      &&  (anagAziendaVO.getTipoFormaGiuridica().getCode() != null))
    {
      obbligoGF = anagFacadeClient.getObbligoGfFromFormaGiuridica(new Long(anagAziendaVO.getTipoFormaGiuridica().getCode().intValue()));
      request.setAttribute("obbligoGF", obbligoGF);
    }
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - contiCorrentiModCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+" "+AnagErrors.ERRORE_KO_OBBLIGO_CF+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  try 
  {
    ContoCorrenteVO contoCorrenteVO = anagFacadeClient.getContoCorrente(request.getParameter("idContoCorrente"));
    request.setAttribute("contoCorrenteVO",contoCorrenteVO);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - contiCorrentiDetCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+" "+AnagErrors.ERRORE_KO_DET_CC+": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
 
%>
<jsp:forward page="../view/contiCorrentiDetView.jsp" />
