<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>

<%

	String iridePageName = "popLegendaEfaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String popLegendaEfaUrl = "/view/popLegendaEfaView.jsp";
	String erroreUrl = "/view/erroreView.jsp";

	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	
  
	
  
  Vector<TipoEfaVO> vTipoEfa = null;
  try 
  {
		vTipoEfa = gaaFacadeClient.getListLegendaTipoEfa();
		// Se non sono stato in grado di recuperare i dati...
		if(vTipoEfa == null) 
     {
			String messaggio = AnagErrors.ERRORE_KO_POP_ISTANZA_LEGENDA_EFA;
   		request.setAttribute("messaggioErrore",messaggio);
   		session.setAttribute("chiudi", "chiudi");
   		%>
    		<jsp:forward page="<%=erroreUrl%>" />
   		<%
   		return;
		}
	}
	catch(SolmrException se) 
  {
		String messaggio = AnagErrors.ERRORE_KO_POP_ISTANZA_LEGENDA_EFA;
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
 			<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
	}  
  
  
  // Metto in request l'oggetto particella trovato
  request.setAttribute("vTipoEfa", vTipoEfa);
  
  
  
  
	

	// Vado alla pop-up
	%>
		<jsp:forward page= "<%=popLegendaEfaUrl %>" />
	

