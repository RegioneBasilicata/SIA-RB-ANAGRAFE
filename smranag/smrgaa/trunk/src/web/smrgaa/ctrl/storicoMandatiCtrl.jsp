<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@page import="it.csi.solmr.etc.*"%>
<%@page import="it.csi.solmr.etc.anag.*"%>
<%@page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

  String iridePageName = "storicoMandatiCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

  String storicoMandatiURL = "/view/storicoMandatiView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione storico mandati."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
    
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String messaggioErrore = null;
  
  String parametroGDEL = null;
  try 
  {
    parametroGDEL = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_GDEL);
    request.setAttribute("parametroGDEL", parametroGDEL);
  }
  catch(SolmrException se) 
  {
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_GDEL+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
   
  DelegaVO[] elencoDeleghe = null;
  try 
  {
   	String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DATA_INIZIO_ASC};
   	elencoDeleghe = anagFacadeClient
   	  .getStoricoDelegheByAziendaAndIdProcedimento(anagAziendaVO.getIdAzienda(), 
   	  Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)), orderBy);
   	request.setAttribute("elencoDeleghe", elencoDeleghe);
  }
  catch(SolmrException se) 
  {
   	messaggioErrore = AnagErrors.ERRORE_KO_STORICO_MANDATI;
   	request.setAttribute("messaggioErrore", messaggioErrore);
  }
%>
<jsp:forward page = "<%=storicoMandatiURL%>"/>
