<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%

	String iridePageName = "eliminaFabbricatiCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	String urlCtrl = "../ctrl/fabbricatiCtrl.jsp";
	String dettaglioUrl = "../view/fabbricatiDetView.jsp";
	String eliminaUrl = "../view/eliminaFabbricatiView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

	ValidationErrors errors = new ValidationErrors();

	FabbricatoVO fabbricatoVO = null;

	String pagina = request.getParameter("pagina");

	// Ho selezionato il pulsante indietro
	if(request.getParameter("operazione") != null && request.getParameter("operazione").equalsIgnoreCase("indietro")) {
  	if(pagina == null || pagina.equals("")) {
     		%>
      		<jsp:forward page="<%= urlCtrl %>"/>
     		<%
  	}
  	else {
     		%>
      		<jsp:forward page="<%= dettaglioUrl %>"/>
     		<%
  	}
	}
	// Ho cliccato il pulsante elimina
	else if(request.getParameter("operazione") != null && request.getParameter("operazione").equalsIgnoreCase("elimina")) {
  	fabbricatoVO = (FabbricatoVO)session.getAttribute("fabbricatoVO");
  	try {
    		anagFacadeClient.eliminaFabbricato(fabbricatoVO, anagAziendaVO.getIdAzienda().longValue());
  	}
  	catch(SolmrException se) {
    		errors.add("error", new ValidationError(se.getMessage()));
    		request.setAttribute("errors", errors);
    		request.getRequestDispatcher(eliminaUrl).forward(request, response);
    		return;
  	}
  	%>
     		<jsp:forward page="<%= urlCtrl %>"/>
  	<%
	}
	// Ho cliccato sulla voce di menù "elimina"
	else 
  {
  	// Controllo che l'utente abbia selezionato un fabbricato da eliminare
  	String idFabbricato = request.getParameter("idFabbricato");
  	// Se lo ha selezionato recupero i dati dal DB e controllo che il fabbricato
  	// selezionato sia ancora in utilizzo all'azienda trattata
  	try {
    	fabbricatoVO = anagFacadeClient.findFabbricatoByPrimaryKey(Long.decode(idFabbricato));
  	}
  	catch(SolmrException se) {
    	errors.add("error", new ValidationError(se.getMessage()));
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(eliminaUrl).forward(request, response);
    	return;
  	}
  	if(fabbricatoVO.getDataFineValiditaFabbricato() != null) {
    	errors.add("error", new ValidationError((String)AnagErrors.get("ERR_FABBRICATO_NO_ELIMINABILE")));
    	request.setAttribute("errors", errors);
    	request.getRequestDispatcher(urlCtrl).forward(request, response);
    	return;
  	}
  	
  	// Se passo i controlli metto il VO in sessione
  	session.setAttribute("fabbricatoVO", fabbricatoVO);
  	// Vado alla pagina di richiesta conferma eliminazione
  	%>
    	<jsp:forward page="<%= eliminaUrl %>"/>
  	<%
	}
%>

