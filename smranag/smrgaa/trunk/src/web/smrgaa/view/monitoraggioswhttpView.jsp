<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors"%>
<%!
  public static final String LAYOUT = "/layout/monitoraggioswhttp.htm";
%>

<%
  SolmrLogger.debug(this, "monitoraggioswhttpView.jsp - Begin");

  Htmpl htmpl = HtmplFactory.getInstance(application).getHtmpl(LAYOUT);
  
  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String percorsoErrori = null;
  if(pathToFollow.equalsIgnoreCase("rupar"))
    percorsoErrori = "/css_rupar/agricoltura/im/";
  else if(pathToFollow.equalsIgnoreCase("sispie")) {
	 percorsoErrori = "/css/agricoltura/im/";
  }
  else if(pathToFollow.equalsIgnoreCase("TOBECONFIG")){
	 percorsoErrori="/css/agricoltura/im/";  
  }
  
  String resultMonitoraggio = (String) request.getAttribute("resultMonitoraggio");
  String strMonitoraggio = "";
  if(resultMonitoraggio.equalsIgnoreCase(AnagErrors.MONITORAGGIO_OK))
  {
    strMonitoraggio = AnagErrors.MONITORAGGIO_MSG_OK;
    htmpl.newBlock("blkOk");
  }
  else
  {
    strMonitoraggio = AnagErrors.MONITORAGGIO_MSG_KO;
    htmpl.newBlock("blkErrore");
    String problemiDB=(String)request.getAttribute("problemiDB");    
    if (problemiDB!=null)
    {
      htmpl.newBlock("blkErrore.blkAnomalia");      
      
      htmpl.set("blkErrore.blkAnomalia.tipo", "SMRGAA_RW");
      htmpl.set("blkErrore.blkAnomalia.valore", AnagErrors.MONITORAGGIO_MSG_KO_DB + problemiDB); 
      htmpl.set("blkErrore.blkAnomalia.immagine", percorsoErrori + "Bloccante.gif");
      htmpl.set("blkErrore.blkAnomalia.descImmagine",(String)AnagErrors.get("ERR_ANOMALIA_BLOCCANTE"));
      htmpl.set("blkErrore.blkAnomalia.note", "Contattare Assistenza Oracle");              
    }  
  }
  htmpl.set("strMonitoraggio", strMonitoraggio);

  
  SolmrLogger.debug(this, "monitoraggioswhttpView.jsp - End");
%>
<%= htmpl.text() %>