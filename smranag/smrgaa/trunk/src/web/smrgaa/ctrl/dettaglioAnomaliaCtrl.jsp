<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>

<%

	String iridePageName = "dettaglioAnomaliaCtrl.jsp";
  	%>
  		<%@include file = "/include/autorizzazione.inc" %>
  	<%
  	SolmrLogger.debug(this, " - dettaglioAnomaliaCtrl.jsp - INIZIO PAGINA");

  	String dettaglioAnomaliaUrl = "/view/dettaglioAnomaliaView.jsp";
  	String dichiarazioneAnomaliaUrl = "/view/dichiarazioneAnomaliaView.jsp";

  	try {
      Long idMotivoDichiarazione = (Long)session.getAttribute("idMotivoDichiarazione");
    	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    	if(request.getParameter("indietro") != null) {
      		AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
      		Vector anomalie = anagFacadeClient.getErroriAnomalieDichiarazioneConsistenza(anagAziendaVO.getIdAzienda(),idMotivoDichiarazione);
      		if(anomalie != null) {
        		session.setAttribute("anomalieDichiarazioniConsistenza", anomalie);
      		}
      		%>
        		<jsp:forward page= "<%= dichiarazioneAnomaliaUrl %>" />
      		<%
    	}
    	else {
      		String idDichiarazioneSegnalazione = request.getParameter("idDichiarazioneSegnalazione");
      		ErrAnomaliaDicConsistenzaVO errAnomaliaDicConsistenzaVO = anagFacadeClient.getAnomaliaDichiarazioneConsistenza(new Long(idDichiarazioneSegnalazione));
      		request.setAttribute("errAnomaliaDicConsistenzaVO", errAnomaliaDicConsistenzaVO);
      		if(errAnomaliaDicConsistenzaVO.getIdDocumento() != null) {
    	  		DocumentoVO documentoVO = null;
    	  		try {
    		  		documentoVO = anagFacadeClient.findDocumentoVOByPrimaryKey(errAnomaliaDicConsistenzaVO.getIdDocumento());
    		  		request.setAttribute("documentoVO", documentoVO);
    	  		}
    	  		catch(SolmrException se) {
    				setError(request, AnagErrors.ERRORE_KO_DOCUMENTO);
    	    		%>
    	      			<jsp:forward page="<%=dettaglioAnomaliaUrl%>" />
    	    		<%
    	    		return;
    		  
    	  		}
      		}
     		%>
        		<jsp:forward page= "<%= dettaglioAnomaliaUrl %>" />
      		<%
    	}
  	}
  	catch(Exception e) {
    	if(e instanceof SolmrException) {
      		setError(request, e.getMessage());
    	}
    	else {
      		e.printStackTrace();
      		setError(request, "Si è verificato un errore di sistema");
    	}
    	%>
      		<jsp:forward page="<%=dichiarazioneAnomaliaUrl%>" />
    	<%
    	return;
  	}
  	SolmrLogger.debug(this, " - dettaglioAnomaliaCtrl.jsp - FINE PAGINA");
%>

<%!

	private void setError(HttpServletRequest request, String msg) {
	    SolmrLogger.error(this, "\n\n\n\n\n\n\n\n\n\n\nmsg="+msg+"\n\n\n\n\n\n\n\n");
	    ValidationErrors errors = new ValidationErrors();
	    errors.add("error", new ValidationError(msg));
	    request.setAttribute("errors", errors);
  	}
%>
