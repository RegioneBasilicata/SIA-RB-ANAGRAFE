<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/cessazioneAttivita.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 Long idElemento = (Long)request.getAttribute("idElemento");
 String operazione = request.getParameter("operazione");
 String anno = (String)request.getAttribute("annoRicerca");

 htmpl.set("idElemento", idElemento.toString());
 if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_ALLEVAMENTO"))) {
   htmpl.set("operazione", (String)SolmrConstants.get("FUNZIONALITA_ALLEVAMENTO"));
 }
 else if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_FABBRICATO"))) {
   htmpl.set("operazione", (String)SolmrConstants.get("FUNZIONALITA_FABBRICATO"));
 }
 else if(operazione.equalsIgnoreCase((String)SolmrConstants.get("FUNZIONALITA_MANODOPERA"))) {
   htmpl.set("operazione", (String)SolmrConstants.get("FUNZIONALITA_MANODOPERA"));
 }
 htmpl.set("idAnno", anno);

 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
