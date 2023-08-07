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

  java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaFiliale.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String abi = request.getParameter("abi");
  String cab = request.getParameter("cab");
  String comuneFiliale = request.getParameter("descrizioneComuneSportello");
  BancaSportelloVO elencoFiliali[]=null;
  try {
    elencoFiliali=anagFacadeClient.searchSportello(abi,cab,comuneFiliale);
  }
  catch(Exception e) {
    htmpl.set("errorMsg",e.getMessage());
  }
  int size=elencoFiliali==null?0:elencoFiliali.length;
  for(int i=0;i<size;i++) {
    BancaSportelloVO sportelloVO=elencoFiliali[i];
    htmpl.newBlock("elencoFiliali");
    if (i==0) {
      htmpl.set("elencoFiliali.checked","checked");
    }
    htmpl.set("elencoFiliali.indice",i+"");
    htmpl.set("elencoFiliali.banca",sportelloVO.getDenominazioneBanca());
    htmpl.set("elencoFiliali.cab",sportelloVO.getCab());
    htmpl.set("elencoFiliali.indirizzo",sportelloVO.getIndirizzoSportello());
    htmpl.set("elencoFiliali.comune",sportelloVO.getDescrizioneComuneSportello());
    htmpl.set("elencoFiliali.provincia",sportelloVO.getSiglaProvincia());
    htmpl.set("elencoFiliali.cap",sportelloVO.getCapSportello());
    htmpl.set("elencoFiliali.siglaprov",sportelloVO.getSiglaProvincia());
    htmpl.set("elencoFiliali.codPaese",sportelloVO.getCodPaeseSportello());
  }
  if (size==0) {
    htmpl.set("errorMsg",(String)AnagErrors.get("ERR_RICERCA_BANCHE_NOT_FOUND"));
    htmpl.newBlock("blkChiudi");
  }
  else
    htmpl.newBlock("blkConferma");

%>
<%= htmpl.text()%>
