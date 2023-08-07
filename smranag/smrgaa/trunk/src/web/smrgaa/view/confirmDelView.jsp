<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/confirmDel.htm");
  	Htmpl htmpl = new Htmpl(layout);

  	%>
    	<%@include file = "/view/remoteInclude.inc" %>
  	<%

  	// Nuova gestione fogli di stile
  	htmpl.set("head", head, null);
  	htmpl.set("header", header, null);
  	htmpl.set("footer", footer, null);
  
  	String operazione = request.getParameter("operazione");
	
  	/*if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("attenderePrego")) {
  		htmpl.set("opVal", "attenderePrego");
  		htmpl.set("page", "ribaltaUnitaArboree");
  		htmpl.set("msg", "Attenzione! Proseguendo il piano colturale verrà allineato ai dati delle UV con la sostituzione integrale dei dati di utilizzo correlati. Sei sicuro di voler proseguire?");
  	}*/
  	if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("validaUv")) {
  		htmpl.set("opVal", "validaUv");
  		htmpl.set("page", "unitaArboreeValida");
  		htmpl.set("msg", "Proseguendo tutte le unità vitate selezionate saranno validate e non potranno più essere modificate dal Gestore del Fascicolo. Sei sicuro di voler proseguire?");
  	}
  	else {
  		htmpl.set("page", "confirmDel");
  	  	htmpl.set("msg", "Eliminare l'unità produttiva solo se è stata inserita erroneamente. Dopo tale operazione i dati non potranno più essere reperiti. Sei sicuro di proseguire con l'eliminazione?");	
  	}

%>
<%= htmpl.text()%>
