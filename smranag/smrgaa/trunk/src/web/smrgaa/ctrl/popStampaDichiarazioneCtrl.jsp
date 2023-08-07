<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.smranag.smrgaa.util.LoggerUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>



<%!
	public final static String VIEW = "/view/popStampaDichiarazioneView.jsp";
%>

<%

	String iridePageName = "popStampaDichiarazioneCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	SolmrLogger.debug(this, " - popStampaDichiarazioneCtrl.jsp - INIZIO PAGINA");
	
	String stampaFascicoloNewUrl = "/servlet/StampaFascicoloModolServlet";
	
	
	//final String errMsg = "Impossibile procedere nella sezione crea una validazione."+
    //"Contattare l'assistenza comunicando il seguente messaggio: ";
	
	
	//se valorizzato vuol dire che è stata fatta una validazione da una nuova richiesta....
  //Long idDichiarazioneConsistenza = (Long)session.getAttribute("idDichiarazioneConsistenza");

  //per prima cosa vado a vedere se posso permettere di protocollare
  // Recupero da COMUNE il valore del parametro "DCNP" per la gestione della protocollazione
  //AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  //AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  
  
  %>
    <jsp:forward page="<%= stampaFascicoloNewUrl %>"/>
  <%
  
	SolmrLogger.debug(this, " - popStampaDichiarazioneCtrl.jsp - FINE PAGINA");
%>


