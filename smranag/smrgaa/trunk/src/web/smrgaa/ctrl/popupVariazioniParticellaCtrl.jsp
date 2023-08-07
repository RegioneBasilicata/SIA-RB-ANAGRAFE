<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.dto.anag.ParticellaVO" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%

	String iridePageName = "popupVariazioniParticellaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String popupVisualizzazioniParticellaUrl = "/view/popupVariazioniParticellaView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

	// Recupero l'id particella selezionato dall'utente
	Long idParticella = Long.decode(request.getParameter("idParticella"));

	// Recupero l'elenco delle variazioni storiche della particella selezionata(compresa la
	// situazione attuale)
	Vector<ParticellaVO> elencoStoricoParticella = null;
	try 
  {
  	elencoStoricoParticella = anagFacadeClient.getElencoStoricoParticella(idParticella);
	}
	catch(SolmrException se) {
  	String messaggioErrore = se.getMessage();
  	request.setAttribute("messaggioErrore", messaggioErrore);
  	%>
     		<jsp:forward page= "<%= popupVisualizzazioniParticellaUrl %>" />
  	<%
	}
	
	// Recupero l'elenco delle variazioni storiche relative alle particella selezionata
	EventoParticellaVO[] elencoEventoParticella = null;
	try {
		String[] orderBy = {SolmrConstants.ORDER_BY_DATA_AGGIORNAMENTO_EVENTO_PARTICELLA_DESC};
		elencoEventoParticella = anagFacadeClient.getEventiParticellaByIdParticellaNuovaOrCessata(idParticella, orderBy);
		request.setAttribute("elencoEventoParticella", elencoEventoParticella);
	}
	catch(SolmrException se) {
  	String messaggioErrore = AnagErrors.ERRORE_KO_EVENTO_PARTICELLA;
  	request.setAttribute("messaggioErrore", messaggioErrore);
  	%>
     		<jsp:forward page= "<%= popupVisualizzazioniParticellaUrl %>" />
  	<%
	}

	// Metto in request il vettore
	request.setAttribute("elencoStoricoParticella", elencoStoricoParticella);

	// Vado alla pop-up
	%>
  	<jsp:forward page= "<%= popupVisualizzazioniParticellaUrl %>" />
	<%

%>
