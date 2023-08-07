
<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.text.*" %>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.Messaggio"%>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.ListaMessaggi"%>

<%@ page language="java"    
contentType="text/html"
    isErrorPage="true"
%>

<%
  
  SolmrLogger.debug(this, " - messaggiUtenteView.jsp - INIZIO PAGINA");
  java.io.InputStream layout = application.getResourceAsStream("/layout/messaggi_utente.htm");
  Htmpl htmpl = new Htmpl(layout);
  
  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  
  
  ListaMessaggi listaMessaggi = (ListaMessaggi)request.getAttribute("listaMessaggi");

  if(listaMessaggi!=null && listaMessaggi.getMessaggi()!=null 
    && listaMessaggi.getMessaggi().length>0)
  {
  	boolean primoDaLeggere = true;
  	boolean primoLetto = true;
	  htmpl.newBlock("blkData");
  	
  	for(Messaggio messaggio : listaMessaggi.getMessaggi())
  	{
  		String blk="";
  		if(messaggio.isLetto()){
  			blk = "blkData.blkMessaggioLetto";
  			if(primoLetto){
  				htmpl.newBlock("blkData.blktitoloLetti");
  				primoLetto = false;
  			}
  		}
  		else{
  			blk="blkData.blkMessaggioDaLeggere";
  			if(primoDaLeggere){
  				htmpl.newBlock("blkData.blktitoloDaLeggere");
  				primoDaLeggere = false;
  			}
  		}
  		htmpl.newBlock(blk);
  		htmpl.set(blk+".idMessaggio", ""+messaggio.getIdElencoMessaggi() );
  		htmpl.set(blk+".titolo", messaggio.getTitolo() );
  		htmpl.set(blk+".dataInserimento", DateUtils.formatDateTimeNotNull(messaggio.getDataInizioValidita()));
  		htmpl.set(blk+".flagAllegati", messaggio.isConAllegati()? "SI" : "NO");
  		if(messaggio.isLetturaObbligatoria()){
	  		htmpl.set(blk+".style", "color: red;");
  		}
  	 }
  }
  else
  {
  	htmpl.newBlock("blkNoData");
  }

  ValidationErrors errors = (ValidationErrors) request.getAttribute("errors");
  HtmplUtil.setErrors(htmpl, errors, request, application);
  
  SolmrLogger.debug(this, "messaggiUtenteView END");
%>

<%=htmpl.text() %>