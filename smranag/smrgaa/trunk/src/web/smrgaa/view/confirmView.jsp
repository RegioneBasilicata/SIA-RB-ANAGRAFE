  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>

<%
	SolmrLogger.debug(this, "BEGIN confirmView");

	java.io.InputStream layout = application.getResourceAsStream("/layout/confirm.htm");
  	Htmpl htmpl = new Htmpl(layout);

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%

  	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  	String dataSistema = DateUtils.formatDate(new Date(System.currentTimeMillis()));
  	if(anagAziendaVO != null) {
    	String dataInizio = DateUtils.formatDate(anagAziendaVO.getDataInizioVal());
    	SolmrLogger.debug(this, "-- dataInizio ="+dataInizio);
    	if(dataInizio.equals(dataSistema)) {
    		SolmrLogger.debug(this, " -- visualizzo solo modifica");
      		htmpl.newBlock("modifica");
    	}
    	else {
    		SolmrLogger.debug(this, " -- visualizzo modifica e storicizza");
      		htmpl.newBlock("modifica");
      		htmpl.newBlock("storicizza");
    	}
  	}
  	
  	String provenienza = request.getParameter("provenienza");
  	if(Validator.isNotEmpty(provenienza)) {
  		SolmrLogger.debug(this, " -- actionStoricizza = anagrafica_mod.htm");
  		htmpl.set("actionStoricizza", "../layout/anagrafica_mod.htm");
  	}
  	else {
  		SolmrLogger.debug(this, " -- actionStoricizza = anagraficaSede_mod.htm");
  		htmpl.set("actionStoricizza", "../layout/anagraficaSede_mod.htm");
  	}

  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);


%>
<%= htmpl.text()%>
