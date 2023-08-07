<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="java.util.*"%>

<%
	
	String iridePageName = "terreniParticellareIsoleParcelleCtrl.jsp";
  	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%

	String terreniParticellareIsoleParcelleUrl = "/view/terreniParticellareIsoleParcelleView.jsp";
	String actionUrl = "../layout/anagrafica.htm";
	String erroreViewUrl = "/view/erroreView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  Vector<TipoSettoreAbacoVO> vTipoSettoreAbaco = null;

 	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
 	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
 	// in modo che venga sempre effettuato
 	try {
 		anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
 	}
 	catch(SolmrException se) {
 		request.setAttribute("statoAzienda", se);
 	}
 
 
  try 
  {
  
    vTipoSettoreAbaco = anagFacadeClient.getListSettoreAbaco();
    request.setAttribute("vTipoSettoreAbaco", vTipoSettoreAbaco);
  }
  catch(SolmrException se) 
  {
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
	
 	
	%>
		<jsp:forward page="<%= terreniParticellareIsoleParcelleUrl %>" />

