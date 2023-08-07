<%@page import="it.csi.papua.papuaserv.dto.messaggistica.Allegato"%>
<%@page import="it.csi.papua.papuaserv.dto.messaggistica.DettagliMessaggio"%>
<%@ page language="java"
  contentType="text/html"
  isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>



<%
  
  SolmrLogger.debug(this, " - dettMessaggiUtenteLoginView.jsp - INIZIO PAGINA");
  
  final String LAYOUT="/layout/dett_messaggi_utente_login.htm";
  final String LAYOUT_NORMALE="/layout/dett_messaggi_utente.htm";
  
  String pagina = LAYOUT_NORMALE;
  if(((String)request.getAttribute("chiamante")).indexOf("messaggi_utente_login.")!=-1){
    pagina = LAYOUT;
  }  
  
  java.io.InputStream layout = application.getResourceAsStream(pagina);
  Htmpl htmpl = new Htmpl(layout);
  
  %>
    <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  

  
  DettagliMessaggio dettagliMessaggio = (DettagliMessaggio) request.getAttribute("dettagliMessaggio");
  
  htmpl.set("CONFIRM", SolmrConstants.OPERATION_CONFIRM);

  htmpl.set("chiamante", (String)request.getAttribute("chiamante"));
  htmpl.set("titolo", dettagliMessaggio.getTitolo());
  htmpl.set("utenteInserimento",  dettagliMessaggio.getUtenteAggiornamento().getNome()+" "+ dettagliMessaggio.getUtenteAggiornamento().getCognome()+ " - " + dettagliMessaggio.getUtenteAggiornamento().getDenominazioneEnte());
  htmpl.set("dataInserimento", DateUtils.formatDateTimeNotNull(dettagliMessaggio.getDataInizioValidita()));
  
  String testoLungo = dettagliMessaggio.getTestoMessaggio();
  if(testoLungo!=null){ // sostituisco \n con <br> 
  	//testoLungo = testoLungo.replaceAll("<", "&lt;"); da mantenere per link ipertestuali
  	testoLungo = testoLungo.replaceAll("\n", "<br\\>");
  }
  	
  htmpl.set("testoMessaggio", testoLungo, null);
  if(dettagliMessaggio.getAllegati()!=null && dettagliMessaggio.getAllegati().length>0)
  {
    htmpl.newBlock("blkAllegati");
	  for(Allegato allegato : dettagliMessaggio.getAllegati())
	  {
		  htmpl.newBlock("blkAllegati.blkAllegato");
		  htmpl.set("blkAllegati.blkAllegato.descrizione", allegato.getDescrizione() );
		  htmpl.set("blkAllegati.blkAllegato.nomeFile", allegato.getNomeFile() );
		  htmpl.set("blkAllegati.blkAllegato.idAllegato", ""+allegato.getIdAllegato() );
	  }
  }
  if(dettagliMessaggio.isLetturaObbligatoria())
  {
   	htmpl.newBlock("blkDichLettura");
   	if(dettagliMessaggio.isLetto())
   	{
   		htmpl.set("blkDichLettura.checked", "checked=\"true\"", null);
   		htmpl.set("blkDichLettura.disabled", "disabled=\"true\"", null);
	  }
  }
    

  SolmrLogger.info(this, " - dettMessaggiUtenteLogin.jsp - FINE PAGINA");
%>
<%= htmpl.text()%>