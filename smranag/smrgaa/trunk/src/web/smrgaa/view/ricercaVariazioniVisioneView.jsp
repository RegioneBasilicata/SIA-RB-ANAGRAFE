  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.RigaRicercaVariazioniAziendaliVO"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/ricercaVariazioniVisione.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");   

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  RigaRicercaVariazioniAziendaliVO righe[] = (RigaRicercaVariazioniAziendaliVO[]) request.getAttribute("righe");
  
  //etichetta del gruppo ruolo
  htmpl.bset("etichettaDescGruppoRuolo", ruoloUtenza.getTipoGruppoRuolo().getDescription());
  
  if (righe!=null)
    for (int i=0;i<righe.length;i++) righe[i].scriviRiga(htmpl,"blkRiga");

%>
<%= htmpl.text()%>
