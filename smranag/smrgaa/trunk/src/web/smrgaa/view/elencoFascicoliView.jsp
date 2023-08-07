<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*"%>
<%@ page import="java.text.*"%>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/elencoFascicoli.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 Vector elencoAziende = (Vector)request.getAttribute("elencoAziende");

 if(elencoAziende != null && elencoAziende.size() > 0) {
   htmpl.newBlock("blkAziende");
   Iterator iteraAziende = elencoAziende.iterator();
   while(iteraAziende.hasNext()) {
     AnagAziendaVO anagAziendaVO = (AnagAziendaVO)iteraAziende.next();
     htmpl.newBlock("blkAziende.blkElencoAziende");
     htmpl.set("blkAziende.blkElencoAziende.denominazione", anagAziendaVO.getDenominazione());
     htmpl.set("blkAziende.blkElencoAziende.cuaa", anagAziendaVO.getCUAA());
     htmpl.set("blkAziende.blkElencoAziende.partitaIva", anagAziendaVO.getPartitaIVA());
     htmpl.set("blkAziende.blkElencoAziende.descComuneSede", anagAziendaVO.getDescComune());
     htmpl.set("blkAziende.blkElencoAziende.indirizzoSede", anagAziendaVO.getSedelegIndirizzo());
     htmpl.set("blkAziende.blkElencoAziende.capSede", anagAziendaVO.getSedelegCAP());
     htmpl.set("blkAziende.blkElencoAziende.provinciaSede", anagAziendaVO.getSedelegProv());
     htmpl.set("blkAziende.blkElencoAziende.istatComuneSede", anagAziendaVO.getSedelegComune());
   }
   // Vado ad impostare l'output del file perchè venga riconosciuto come excel
   response.setContentType("application/vnd.ms-excel");
 }
 else {
   String messaggioErrore = (String)request.getAttribute("messaggioErrore");
   htmpl.newBlock("blkErrore");
   htmpl.set("blkErrore.messaggio", messaggioErrore);
 }

%>
<%= htmpl.text()%>
