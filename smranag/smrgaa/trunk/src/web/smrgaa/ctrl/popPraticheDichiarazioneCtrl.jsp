<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.comune.*"%>

<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%

	String iridePageName = "popPraticheDichiarazioneCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%

  	String popPraticheDichiarazioneUrl = "/view/popPraticheDichiarazioneView.jsp";
  	String erroreUrl = "/view/erroreView.jsp";

  	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
   	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
   	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
   	Long idDichiarazioneConsistenza = Long.decode(request.getParameter("idDichiarazioneConsistenza"));

	// Recupero le pratiche in funzione della dichiarazione selezionata
	Vector elencoPratiche = null;
	try {
		elencoPratiche = anagFacadeClient.aggiornaPraticaAziendaPLQSL(anagAziendaVO.getIdAzienda(), ruoloUtenza.getIdUtente(), idDichiarazioneConsistenza);
		request.setAttribute("elencoPratiche", elencoPratiche);
	}
	catch(SolmrException se) {
		String messaggio = AnagErrors.ERRORE_KO_PRATICHE;
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
			<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
	}
	
	// Richiamo il servizio di comune per estrarre l'elenco delle amministrazioni
	// di competenza
	Hashtable elencoAmmCompetenza = new Hashtable();
	try {
		AmmCompetenzaVO[] elenco = anagFacadeClient.serviceGetListAmmCompetenza();
		if(elenco != null && elenco.length > 0) {
			for(int i = 0; i < elenco.length; i++) {
				AmmCompetenzaVO ammCompetenzaVO = (AmmCompetenzaVO)elenco[i];
				elencoAmmCompetenza.put(ammCompetenzaVO.getIdAmmCompetenza(), ammCompetenzaVO);
			}
			request.setAttribute("elencoAmmCompetenza", elencoAmmCompetenza);
		}
	}
	catch(SolmrException se) {
		String messaggio = AnagErrors.ERRORE_KO_LIST_AMM_COMPETENZA;
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
			<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
	}

  	// Vado alla pop-up
  	%>
    	<jsp:forward page= "<%= popPraticheDichiarazioneUrl %>" />
  	<%

%>

