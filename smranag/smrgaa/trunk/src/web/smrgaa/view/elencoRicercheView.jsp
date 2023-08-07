<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/elencoRicerche.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 String tipoRicerca = (String)request.getAttribute("tipoRicerca");
 if(!Validator.isNotEmpty(tipoRicerca) || tipoRicerca.equalsIgnoreCase((String)SolmrConstants.get("RIEPILOGO_PER_MANDATO"))) {
   htmpl.set("blkReportisticaRiepilogoMandati.checkedM", "checked=\"checked\"");
 }
 else {
   htmpl.set("blkReportisticaElencoAziendeMandato.checkedF", "checked=\"checked\"");
 }

 // Recupero eventuale messaggio di errore generico
 String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 if(Validator.isNotEmpty(messaggioErrore)) {
   htmpl.newBlock("blkErrore");
   htmpl.set("blkErrore.messaggio", messaggioErrore);
 }


%>
<%= htmpl.text()%>
