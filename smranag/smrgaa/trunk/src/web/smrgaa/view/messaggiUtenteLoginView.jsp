<%@page import="it.csi.papua.papuaserv.dto.messaggistica.Messaggio"%>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi"%>
<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.services.DelegaAnagrafeVO" %>
<%@page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>


<%
  
  SolmrLogger.debug(this, " - messaggiUtenteLoginView.jsp - INIZIO PAGINA");
  java.io.InputStream layout = application.getResourceAsStream("/layout/messaggi_utente_login.htm");
  Htmpl htmpl = new Htmpl(layout);
  
  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  
  
  
  htmpl.set("conferma", SolmrConstants.OPERATION_CONFIRM );
  
  ListaMessaggi listaMessaggi = (ListaMessaggi)request.getAttribute("listaMessaggi");

  if(listaMessaggi.getMessaggi()!=null && listaMessaggi.getMessaggi().length>0){
  	for(Messaggio messaggio : listaMessaggi.getMessaggi()){
  		htmpl.newBlock("blkMessaggio");
  		htmpl.set("blkMessaggio.idMessaggio", ""+messaggio.getIdElencoMessaggi() );
  		htmpl.set("blkMessaggio.titolo", messaggio.getTitolo() );
  		htmpl.set("blkMessaggio.dataInserimento", DateUtils.formatDateTimeNotNull(messaggio.getDataInizioValidita()));
  		htmpl.set("blkMessaggio.flagAllegati", messaggio.isConAllegati()? "SI" : "NO");
  	}
  }

  ValidationErrors errors = (ValidationErrors) request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);
  
  SolmrLogger.debug(this, "messaggiUtenteLoginView END");
%>
<%=htmpl.text() %>