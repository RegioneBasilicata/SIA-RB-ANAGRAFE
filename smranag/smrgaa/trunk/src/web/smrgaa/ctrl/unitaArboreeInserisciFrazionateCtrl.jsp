<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>

<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
	
	String iridePageName = "unitaArboreeInserisciFrazionateCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	String unitaArboreeInserisciFrazionateUrl = "/view/unitaArboreeInserisciFrazionateView.jsp";
	String terreniUnitaArboreeElencoCtrlUrl = "/ctrl/terreniUnitaArboreeElencoCtrl.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	ValidationErrors errors = new ValidationErrors();
	ValidationError error = null;
	StoricoParticellaVO storicoParticellaVO = (StoricoParticellaVO)session.getAttribute("storicoParticellaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	Hashtable elencoUnitaArboree = (Hashtable)session.getAttribute("elencoUnitaArboree");
	
	// Recupero le causali modifica
 	TipoCausaleModificaVO[] elencoCausaleModifica = null;
 	try {
 		String[] orderBy = {SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION};
 		elencoCausaleModifica = anagFacadeClient.getListTipoCausaleModifica(true, orderBy);
 		request.setAttribute("elencoCausaleModifica", elencoCausaleModifica);
 	}
 	catch(SolmrException se) {
 		error = new ValidationError(AnagErrors.ERRORE_KO_CAUSALE_MOD_UNITA_ARBOREA);
		errors.add("idUtilizzo", error);
     	request.setAttribute("errors", errors);
     	request.getRequestDispatcher(unitaArboreeInserisciFrazionateUrl).forward(request, response);
     	return;
 	}
 	// L'utente ha cliccato il pulsante elimina
 	if(request.getParameter("elimina") != null) {
 		// Recupero gli id da elminare
 		String[] elementsToRemove = request.getParameterValues("idStoricoUnitaArborea");
 		if(elementsToRemove == null || elementsToRemove.length == 0) {
       		errors.add("error", new ValidationError(AnagErrors.ERRORE_NO_VOCE_SELEZIONATA_ALERT));
       		request.setAttribute("errors", errors);
       		request.getRequestDispatcher(unitaArboreeInserisciFrazionateUrl).forward(request, response);
       		return;
     	}
     	// Se sì, elimino gli elementi selezionati
     	else {
         	for(int i = 0; i < elementsToRemove.length; i++) {
           		Long idStoricoUnitaArborea = Long.decode((String)elementsToRemove[i]);
           		elencoUnitaArboree.remove(idStoricoUnitaArborea);
         	}
     	}
 		if(elencoUnitaArboree.size() == 0) {
 			session.removeAttribute("elencoUnitaArboree");
 		}
 	}
 	// L'utente ha cliccato il pulsante conferma
 	else if(request.getParameter("conferma") != null) {
 		// Recupero il motivo inserimento
 		String causaleInserimento = request.getParameter("idCausaleModifica");
 		if(!Validator.isNotEmpty(causaleInserimento)) {
 			errors.add("idCausaleModifica", new ValidationError(AnagErrors.ERRORE_CAMPO_OBBLIGATORIO));
       		request.setAttribute("errors", errors);
       		request.getRequestDispatcher(unitaArboreeInserisciFrazionateUrl).forward(request, response);
       		return;
 		}
 		else {
 			Long[] idStoricoUV = new Long[elencoUnitaArboree.size()];
 			Enumeration enumeraUnitaArboree = elencoUnitaArboree.keys();
 			int indice = 0;
 			while(enumeraUnitaArboree.hasMoreElements()) {
 				Long key = (Long)enumeraUnitaArboree.nextElement();
 				idStoricoUV[indice] = key;
 				indice++;
 			}
			// Effettuo importazione delle unità arboree selezionate
 			try {
 				anagFacadeClient.importUnitaArboreeBySchedario(idStoricoUV, ruoloUtenza, anagAziendaVO, Long.decode(causaleInserimento), storicoParticellaVO.getIdParticella());
 			}
 			catch(SolmrException se) {
 				errors.add("idCausaleModifica", new ValidationError(AnagErrors.ERRORE_KO_UV_IMPORT_BY_SCHEDARIO));
 				request.setAttribute("errors", errors);
 	       		request.getRequestDispatcher(unitaArboreeInserisciFrazionateUrl).forward(request, response);
 	       		return;
 			}
 			// Torno alla pagina di ricerca/elenco delle UV
 			%>
				<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
			<%
 		}
 	}
	// L'utente ha cliccato il pulsante annulla
	else if(request.getParameter("annulla") != null) {
		%>
			<jsp:forward page="<%= terreniUnitaArboreeElencoCtrlUrl %>" />
		<%
 	}
 	
 	// Vado alla pagina di inserimento
 	%>
		<jsp:forward page="<%= unitaArboreeInserisciFrazionateUrl %>" />
	<%
%>

