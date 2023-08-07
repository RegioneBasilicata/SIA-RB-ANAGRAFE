<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.math.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "popPianoGraficoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	final String errMsg = "Impossibile procedere nella sezione piano grafico."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
	
	String popPianoGraficoUrl = "/view/popPianoGraficoView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	
	String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
	String idAccessoPianoGrafico = request.getParameter("idAccessoPianoGrafico");
	String codiUtilita = request.getParameter("codiUtilita");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	
 
	
	
	
	try 
	{
		int result = anagFacadeClient.preCaricamentoPianoGrafico(
		  new Long(idDichiarazioneConsistenza).longValue());
		if(result == 0)
		{
		  String urlPianoGrafico = null;
		  if(Validator.isNotEmpty(idAccessoPianoGrafico))
	    {
	      EsitoPianoGraficoVO esitoPianoGraficoVO = anagFacadeClient.getEsitoPianoGraficoFromAccesso(
	        new Long(idAccessoPianoGrafico));
	      if(Validator.isNotEmpty(esitoPianoGraficoVO))
	      {
	        urlPianoGrafico = esitoPianoGraficoVO.getLink();
	      }
	    }
		
		  //response.flushBuffer();
		  //response.getOutputStream().flush();
		  // or `HttpServletResponse.getWriter().flush()
		  urlPianoGrafico += "&cuaa="+anagAziendaVO.getCUAA()+"&utilita="+codiUtilita;
		  SolmrLogger.info(this, " - popPianoGraficoCtrl.jsp - url="+urlPianoGrafico);
		  response.sendRedirect(urlPianoGrafico);
		  response.flushBuffer();
      response.getOutputStream().flush();
		  return;
		}
		else
		{
		  EsitoPianoGraficoVO esitoPianoGraficoVO = anagFacadeClient.getEsitoPianoGraficoFromPK(result);
		  String messaggio = esitoPianoGraficoVO.getMessaggio();
      request.setAttribute("messaggioErrore",messaggio);
      %>
       <jsp:forward page="<%=popPianoGraficoUrl%>" />
      <%
		}
	}
	catch(SolmrException se) 
	{
		SolmrLogger.info(this, " - popPianoGraficoCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PL_PRACARICAMENTO_PG+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
		%>
			<jsp:forward page="<%= popPianoGraficoUrl %>" />
		<%
	}
%>
	
	

  

