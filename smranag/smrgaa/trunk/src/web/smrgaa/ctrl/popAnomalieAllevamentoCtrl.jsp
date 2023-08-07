<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.allevamenti.EsitoControlloAllevamento" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

  String iridePageName = "popAnomalieAllevamentoCtrl.jsp";
  
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

 	String popAnomalieAllevamentoUrl = "/view/popAnomalieAllevamentoView.jsp";
 	String erroreUrl = "/view/erroreView.jsp";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	Long idAllevamento = Long.decode(request.getParameter("idAllevamento"));
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  

  // Vado a reperire i dati in fuzione di id_conduzione_particella...
	StoricoParticellaVO storicoParticellaVO = null;
	Vector<EsitoControlloAllevamento> elencoEsito = null;
	try 
  {
	  AllevamentoAnagVO allevamentoVO = anagFacadeClient.getAllevamento(idAllevamento);
 	  request.setAttribute("allevamentoVO", allevamentoVO);
 		// Ricerco le segnalazioni
 		elencoEsito = gaaFacadeClient.getElencoEsitoControlloAllevamento(idAllevamento);
 		
 		if(elencoEsito == null)
 		{
 		 	// Metto in request il messaggio di errore
 		 	request.setAttribute("messaggio", AnagErrors.ERR_NESSUN_CONTROLLO_ALLEVAMENTO_TROVATO);
 		 	// Vado alla pagina dove "eventualmente" mostrerò il messaggio di errore
 		 	%>
 		   	<jsp:forward page= "<%= popAnomalieAllevamentoUrl %>" />
 		 	<%
 		}
  }
	// ... altrimenti segnalo messaggio di errore
 	catch(SolmrException se) 
 	{
  	String messaggio = (String)AnagErrors.ERRORE_KO_ANOMALIE_ALLEVAMENTO;;
  	request.setAttribute("messaggioErrore",messaggio);
  	session.setAttribute("chiudi", "chiudi");
  	%>
   		<jsp:forward page="<%=erroreUrl%>" />
  	<%
  	return;
 	}
	
	// Metto il vettore in request se sono stati recuperati elementi da visualizzare
	if(elencoEsito != null) 
	{
	  request.setAttribute("elencoEsito", elencoEsito);
	}

 	// Vado alla pop-up
 	%>
   	<jsp:forward page= "<%= popAnomalieAllevamentoUrl %>" />
 	<%

%>

