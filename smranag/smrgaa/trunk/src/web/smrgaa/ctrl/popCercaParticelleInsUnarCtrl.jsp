<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%
	
	String iridePageName = "popCercaParticelleInsUnarCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	String popCercaParticelleInsUnarUrl = "/view/popCercaParticelleInsUnarView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	String messaggioErrore = null;
	
	// Recupero i parametri di ricerca impostati dall'utente
	String siglaProvincia = request.getParameter("siglaProvincia");
	String descComune = request.getParameter("descComune");
	String sezione = request.getParameter("sezione");
	String foglio = request.getParameter("foglio");
	String particella = request.getParameter("particella");
	String importa = request.getParameter("importa");
	if(Validator.isNotEmpty(importa)) 
  {
		request.setAttribute("importa", importa);
	}
	
	// L'utente ha cliccato il pulsante inserisci
	if(Validator.isNotEmpty(request.getParameter("conferma"))) 
  {
		// Recupero l'id storico selezionato
		Long idStoricoParticella = Long.decode(request.getParameter("idStoricoParticella"));
		// Cerco la particella sul DB
		try 
    {
			StoricoParticellaVO storicoParticellaVO = anagFacadeClient.findStoricoParticellaByPrimaryKey(idStoricoParticella);
			session.setAttribute("storicoParticellaVO", storicoParticellaVO);
		}
		catch(SolmrException se) {
			messaggioErrore = AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= popCercaParticelleInsUnarUrl %>" />
			<%
		}
		if(!Validator.isNotEmpty(importa)) {
			%>
				<html>
					<head>
						<script type="text/javascript">
							window.opener.location.href='../layout/unitaArboreeInserisci.htm';
							window.close();
						</script>
					</head>
				</html>
			<%
			return;
		}
		else {
			%>
			<html>
				<head>
					<script type="text/javascript">
						window.opener.location.href='../layout/unitaArboreeInserisciFrazionate.htm';
						window.close();
					</script>
				</head>
			</html>
		<%
		return;
		}
	}
	// L'utente ha scelto cerca dall'inserimento unità arborea
	else 
  {
		// Recupero il codice istat comune
	 	ComuneVO comuneVO = null;
		String istatComune = null;
		if(Validator.isNotEmpty(descComune)) {
		 	try {
		     	comuneVO = anagFacadeClient.getComuneByParameters(descComune, siglaProvincia, null, SolmrConstants.FLAG_S, null);
		     	if(comuneVO != null) {
		     		istatComune = comuneVO.getIstatComune();
		     	}
		     	else {
		     		messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND;
		     		request.setAttribute("messaggioErrore", messaggioErrore);
		     	}
		   	}
		 	catch(SolmrException se) {
		   		messaggioErrore = AnagErrors.ERRORE_COMUNE_INESISTENTE;
		   		request.setAttribute("messaggioErrore", messaggioErrore);
				%>
					<jsp:forward page="<%= popCercaParticelleInsUnarUrl %>" />
				<%
		 	}
		}
		
		if(!Validator.isNotEmpty(messaggioErrore)) 
    {
			// Recupero le particelle a cui associare l'unità arborea
			StoricoParticellaVO[] elencoParticelle = null;
			try {
				String orderBy = SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY;
				elencoParticelle = anagFacadeClient.getListStoricoParticellaVOByParametersImpUnar(istatComune, sezione, foglio, particella, null, anagAziendaVO.getIdAzienda().longValue());
				request.setAttribute("elencoParticelle", elencoParticelle);
			}
			catch(SolmrException se) {
				if(!se.getMessage().equalsIgnoreCase(AnagErrors.ERRORE_FILTRI_GENERICI)) {
					messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
				}
				else {
					messaggioErrore = se.getMessage(); 
				}
				request.setAttribute("messaggioErrore", messaggioErrore);
				%>
					<jsp:forward page="<%= popCercaParticelleInsUnarUrl %>" />
				<%
			}	
		}
		
		// Vado all'elenco delle particelle
		%>
			<jsp:forward page="<%= popCercaParticelleInsUnarUrl %>" />
		<%
	}
	
%>