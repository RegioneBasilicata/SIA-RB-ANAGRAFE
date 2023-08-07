<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.ParticellaCertElegVO" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%

	String iridePageName = "popVariazioniEleggibilitaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String popVariazioniEleggibilitaUrl = "/view/popVariazioniEleggibilitaView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

	// Recupero l'id particella selezionato dall'utente
	Long idParticella = Long.decode(request.getParameter("idParticella"));
  
  StoricoParticellaVO storicoParticellaVO = null;
  
  try 
  {
    storicoParticellaVO = anagFacadeClient.findCurrStoricoParticellaByIdParticella(idParticella);
    request.setAttribute("storicoParticellaVO", storicoParticellaVO);
  }
  catch(SolmrException se) 
  {
    String messaggioErrore = se.getStackTraceMessage();
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
        <jsp:forward page= "<%= popVariazioniEleggibilitaUrl %>" />
    <%
  } 
  
  
  

	// Recupero l'elenco delle variazioni storiche della particella selezionata(compresa la
	// situazione attuale)
	Vector<Vector<ParticellaCertElegVO>> elencoStoricoEleggibilita = null;
	try 
  {
  	elencoStoricoEleggibilita = anagFacadeClient.getListStoricoParticellaCertEleg(idParticella);
    request.setAttribute("elencoStoricoEleggibilita", elencoStoricoEleggibilita);
	}
	catch(SolmrException se) 
  {
  	String messaggioErrore = se.getStackTraceMessage();
  	request.setAttribute("messaggioErrore", messaggioErrore);
  	%>
     		<jsp:forward page= "<%= popVariazioniEleggibilitaUrl %>" />
  	<%
	}
	
  
  if(elencoStoricoEleggibilita == null)
  {
    request.setAttribute("messaggioErrore", "Nessun valore trovato");
    %>
        <jsp:forward page= "<%= popVariazioniEleggibilitaUrl %>" />
    <%
  }
	

	


	// Vado alla pop-up
	%>
  	<jsp:forward page= "<%= popVariazioniEleggibilitaUrl %>" />
	<%

%>
