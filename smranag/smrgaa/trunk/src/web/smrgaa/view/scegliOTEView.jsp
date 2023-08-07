  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="java.rmi.RemoteException" %>
<%@ page import="java.sql.Timestamp" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  java.io.InputStream layout = application.getResourceAsStream("/layout/scegliOTE.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String codiceOTE = request.getParameter("codiceOTE");
  String descrizioneOTE = request.getParameter("descrizioneOTE");

  Vector tipiAttivitaOTE = null;

  try {
    tipiAttivitaOTE = anagFacadeClient.getTipiAttivitaOTE(codiceOTE, descrizioneOTE);
  }
  catch(SolmrException se) {
    htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
    htmpl.set("exception",AnagErrors.RICERCAATTIVITAOTE);
  }
  if(tipiAttivitaOTE != null) {
    htmpl.set("conferma.pathToFollow", (String)session.getAttribute("pathToFollow"));
    Iterator tipiAttivitaOTEIterator = tipiAttivitaOTE.iterator();
    while(tipiAttivitaOTEIterator.hasNext()) {
      CodeDescription codeDescription = (CodeDescription)tipiAttivitaOTEIterator.next();
      htmpl.newBlock("tipiAttivitaOTE");
      htmpl.set("tipiAttivitaOTE.idAttivitaOTE",codeDescription.getCode().toString());
      htmpl.set("tipiAttivitaOTE.codiceOTE",codeDescription.getSecondaryCode().toString());
      htmpl.set("tipiAttivitaOTE.descrizioneOTE",codeDescription.getDescription());
    }
  }


%>
<%= htmpl.text()%>


