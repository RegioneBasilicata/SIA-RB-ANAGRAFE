<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.rmi.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.client.profile.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "dettaglioNotificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  
  final String errMsg = "Impossibile procedere nella sezione dettaglio notifica."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String actionUrl = "../layout/notifiche.htm";
  String erroreViewUrl = "/view/erroreView.jsp";

  String dettaglioNotificaUrl = "/view/dettaglioNotificaView.jsp";

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  ValidationErrors errors = new ValidationErrors();

  // L'utente ha selezionato la funzione di dettaglio della notifica

  // Recupero il parametro contenente la chiave primaria composta da
  // id_notifica e id_anagrafica_azienda
  Long idNotifica = Long.decode(request.getParameter("idNotifica"));

  // Recupero l'oggetto notifica
  NotificaVO dettaglioNotificaVO = null;
  try 
  {
    dettaglioNotificaVO = anagFacadeClient.findNotificaByPrimaryKey(idNotifica, "dettaglio");
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - dettaglioNotificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+":"+ se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }

  // Metto in request l'oggetto notifica
  request.setAttribute("dettaglioNotificaVO", dettaglioNotificaVO);
  
  if(Validator.isNotEmpty(dettaglioNotificaVO.getvNotificaEntita())
   && Validator.isNotEmpty(dettaglioNotificaVO.getvNotificaEntita().get(0).getIdDichiarazioneConsistenza()))
  {
    ConsistenzaVO consistenzaVO = anagFacadeClient.getDichiarazioneConsistenza(
      dettaglioNotificaVO.getvNotificaEntita().get(0).getIdDichiarazioneConsistenza());
    request.setAttribute("consistenzaVO", consistenzaVO);
  }
  

  // Vado alla pagina di dettaglio della notifica
  %>
    <jsp:forward page= "<%= dettaglioNotificaUrl %>" />
