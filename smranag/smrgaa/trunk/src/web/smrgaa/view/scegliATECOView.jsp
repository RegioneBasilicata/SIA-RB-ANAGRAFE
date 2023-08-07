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

  java.io.InputStream layout = application.getResourceAsStream("/layout/scegliATECO.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);


  String codiceATECO = null;
  String descrizioneATECO = null;
  
  
  //Ricerca coi codici secondari
  if(Validator.isNotEmpty(request.getParameter("codiceATECOSec"))
    || Validator.isNotEmpty(request.getParameter("descrizioneATECOSec")))
  {
    codiceATECO = request.getParameter("codiceATECOSec");
    descrizioneATECO = request.getParameter("descrizioneATECOSec");
  }
  else
  {
    codiceATECO = request.getParameter("codiceATECO");
    descrizioneATECO = request.getParameter("descrizioneATECO");
  }

  Vector tipiAttivitaATECO = null;

  try 
  {
    tipiAttivitaATECO = anagFacadeClient.getTipiAttivitaATECO(codiceATECO, descrizioneATECO);
    if(tipiAttivitaATECO == null)
    {
      htmpl.set("exception",AnagErrors.RICERCAATTIVITAATECO);
      htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
    }
  }
  catch(SolmrException se) 
  {
    htmpl.set("exception", "Problemi alla query della ricerca ATECO");
    htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
  }
  
  if(tipiAttivitaATECO != null) 
  {
    htmpl.set("conferma.pathToFollow", (String)session.getAttribute("pathToFollow"));
    Iterator tipiAttivitaATECOIterator = tipiAttivitaATECO.iterator();
    while(tipiAttivitaATECOIterator.hasNext()) {
      CodeDescription codeDescription = (CodeDescription)tipiAttivitaATECOIterator.next();
      htmpl.newBlock("tipiAttivitaATECO");
      htmpl.set("tipiAttivitaATECO.idAttivitaATECO",codeDescription.getCode().toString());
      htmpl.set("tipiAttivitaATECO.codiceATECO",codeDescription.getSecondaryCode().toString());
      htmpl.set("tipiAttivitaATECO.descrizioneATECO",codeDescription.getDescription());
    }
  }


%>
<%= htmpl.text()%>


