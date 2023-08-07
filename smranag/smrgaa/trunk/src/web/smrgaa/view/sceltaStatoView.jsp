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
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>

<%

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaStato.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String obiettivo=request.getParameter("obiettivo");
  if (obiettivo!=null)
  {
    htmpl.set("obiettivo",obiettivo);
  }
  String statoEstero = request.getParameter("stato");
  String provenienza = request.getParameter("provenienza");
  String estinto = null;
  String flagCatastoAttivo = null;
  if(Validator.isNotEmpty(provenienza)) {
    estinto = SolmrConstants.FLAG_N;
    if(provenienza.equalsIgnoreCase("territoriale")) {
    	flagCatastoAttivo = SolmrConstants.FLAG_S;
    }
  }

  Vector elencoStati = null;
  try {
    elencoStati = anagFacadeClient.ricercaStatoEstero(statoEstero, estinto, flagCatastoAttivo);
  }
  catch(SolmrException se) {
    htmpl.set("exception",""+AnagErrors.get("RICERCASTATOESTERO"));
    htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
  }
  if(elencoStati != null) {
    htmpl.set("conferma.pathToFollow", (String)session.getAttribute("pathToFollow"));
    Iterator statiIterator = elencoStati.iterator();
    while(statiIterator.hasNext()) {
      ComuneVO comuneVO = (ComuneVO)statiIterator.next();
      htmpl.newBlock("elencoStati");
      htmpl.set("elencoStati.stato",comuneVO.getDescom());
      htmpl.set("elencoStati.istat",comuneVO.getIstatComune());
      htmpl.set("elencoStati.siglaStato",comuneVO.getDescom());
    }
  }


%>
<%= htmpl.text()%>


