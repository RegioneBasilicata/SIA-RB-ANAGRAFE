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

  java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaBanca.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);

  String abi = request.getParameter("abi");
  String banca = request.getParameter("denominazioneBanca");
  BancaSportelloVO elencoBanche[]=null;
  try {
    elencoBanche=anagFacadeClient.searchBanca(abi,banca);
  }
  catch(Exception e) {
    htmpl.set("errorMsg",e.getMessage());
  }
  int size=elencoBanche==null?0:elencoBanche.length;
  for(int i=0;i<size;i++) {
    BancaSportelloVO bancaVO=elencoBanche[i];
    htmpl.newBlock("elencoBanche");
    if (i==0) {
      htmpl.set("elencoBanche.checked","checked");
    }
    htmpl.set("elencoBanche.indice",i+"");
    htmpl.set("elencoBanche.abi",bancaVO.getAbi());
    htmpl.set("elencoBanche.denominazione",bancaVO.getDenominazioneBanca());
    htmpl.set("elencoBanche.bic",bancaVO.getBic());
  }
  if (size==0) {
    htmpl.set("errorMsg",(String)AnagErrors.get("ERR_RICERCA_BANCHE_NOT_FOUND"));
    htmpl.newBlock("blkChiudi");
  }
  else{
    htmpl.newBlock("blkConferma");
  }

%>
<%= htmpl.text()%>
