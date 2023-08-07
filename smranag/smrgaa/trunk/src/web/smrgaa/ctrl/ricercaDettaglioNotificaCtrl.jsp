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
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "ricercaDettaglioNotificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String dettaglioNotificaUrl = "/view/ricercaDettaglioNotificaView.jsp";
  String ricercaNotificaUrl = "/view/ricercaNotificaView.jsp";
  
  
  final String errMsg = "Impossibile procedere nella sezione ricerca dettaglio notifiche."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  ValidationErrors errors = new ValidationErrors();
  

  // L'utente ha selezionato il pulsante indietro
  if(request.getParameter("indietro") != null) 
  {

    // Recupero l'oggetto contenente i filtri dell'ultima ricerca effettuata
    // dall'utente
    NotificaVO notificaRicercaVO = (NotificaVO)session.getAttribute("notificaRicercaVO");

    // Effettuo la ricerca dei tipi tipologia notifica inserendo anche la
	  // voce "tutte" all'interno del vettore come richiesto nella segnalazione evolutiva
	  // 110 presente su test director e nel caso d'uso 2.19.6 CU-GAA18-05 Ricerca notifiche versione 34
	  Vector<CodeDescription> vTipologiaNotifica = null;
	  try 
	  {
	    vTipologiaNotifica = anagFacadeClient.getTipiTipologiaNotifica();
	  }
	  catch(SolmrException se) 
	  {
	    ValidationError error = new ValidationError(se.getMessage());
	    errors.add("error",error);
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(dettaglioNotificaUrl).forward(request, response);
	    return;
	  }
    // Metto il vettore in request
    request.setAttribute("vTipologiaNotifica", vTipologiaNotifica);
    
    
    Vector<CodeDescription> vCategoriaNotifica = null;
	  try 
	  {
	    vCategoriaNotifica = anagFacadeClient.getCategoriaNotifica();
	  }
	  catch(SolmrException se) 
	  {
	    ValidationError error = new ValidationError(se.getMessage());
	    errors.add("error",error);
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(dettaglioNotificaUrl).forward(request, response);
	    return;
	  }
	  // Metto il vettore in request
	  request.setAttribute("vCategoriaNotifica", vCategoriaNotifica);


    // Carico la combo con le province del Piemonte più la voce "tutte"
    Vector<ProvinciaVO> elencoProvincePiemonte = null;
    try 
    {
      elencoProvincePiemonte = anagFacadeClient.getProvinceByRegione((String)SolmrConstants.get("ID_REG_PIEMONTE"));
      ProvinciaVO provinciaVO = new ProvinciaVO();
      provinciaVO.setIstatProvincia("0");
      provinciaVO.setSiglaProvincia("tutte");
      elencoProvincePiemonte.add(provinciaVO);
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(dettaglioNotificaUrl).forward(request, response);
      return;
    }

    // Metto il vettore in request
    request.setAttribute("elencoProvincePiemonte", elencoProvincePiemonte);
    
    
    // Recupero il tipo di elenco che vuole visualizzare l'utente
	  boolean storico = Validator.isNotEmpty(request.getParameter("storico"));
	
	  // Metto il valore in request
	  request.setAttribute("storico", new Boolean(storico));
    
    
    BigDecimal parametroNotifiRmax = null;
	  try 
	  {
	    parametroNotifiRmax = (BigDecimal)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_NOTIFI_RMAX);
	  }
	  catch(SolmrException se) 
	  {
	    SolmrLogger.info(this, " - ricercaDettaglioNotificheCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_NOTIFI_RMAX+".\n"+se.toString();   
	    ValidationError error = new ValidationError(messaggio);
	    errors.add("error",error);
	    request.setAttribute("errors", errors);
	    request.getRequestDispatcher(dettaglioNotificaUrl).forward(request, response);
	    return;
	  }

    // Effettuo la ricerca delle notifiche in relazione ai filtri di ricerca
    ElencoNotificheVO elencoNotificheVO = null;
    try 
    {
      elencoNotificheVO = anagFacadeClient.ricercaNotificheByParametri(notificaRicercaVO, utenteAbilitazioni, ruoloUtenza, new Boolean(storico), parametroNotifiRmax.intValue());
      if(Validator.isNotEmpty(elencoNotificheVO.getMessaggioErrore()))
      {
        String messaggio = elencoNotificheVO.getMessaggioErrore();
        request.setAttribute("messaggio", messaggio);
      }
    }
    catch(SolmrException se) 
    {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(dettaglioNotificaUrl).forward(request, response);
      return;
    }

    // Metto il vettore in request
    request.setAttribute("elencoNotifiche", elencoNotificheVO.getvElencoNotifiche());

    // Torno alla pagina di ricerca/elenco delle notifiche
    %>
      <jsp:forward page= "<%= ricercaNotificaUrl %>" />
    <%

  }
  // L'utente ha selezionato la funzione di dettaglio della notifica
  else 
  {
    // Recupero il parametro contenente la chiave primaria composta da
    // id_notifica e id_anagrafica_azienda
    String primaryKey = request.getParameter("primaryKey");

    // Recupero la chiave
    int valore = primaryKey.indexOf("/");
    Long idNotifica = Long.decode(primaryKey.substring(0, valore));

    // Recupero l'oggetto notifica
    NotificaVO dettaglioRicercaNotificaVO = null;
    try 
    {
      dettaglioRicercaNotificaVO = anagFacadeClient.findNotificaByPrimaryKey(idNotifica, "dettaglio");
    }
    catch(SolmrException se) {
      ValidationError error = new ValidationError(se.getMessage());
      errors.add("error",error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(dettaglioNotificaUrl).forward(request, response);
      return;
    }

    // Metto in request l'oggetto notifica
    request.setAttribute("dettaglioRicercaNotificaVO", dettaglioRicercaNotificaVO);
    
    if(Validator.isNotEmpty(dettaglioRicercaNotificaVO.getvNotificaEntita()))
	  {
	    ConsistenzaVO consistenzaVO = anagFacadeClient.getDichiarazioneConsistenza(
	      dettaglioRicercaNotificaVO.getvNotificaEntita().get(0).getIdDichiarazioneConsistenza());
	    request.setAttribute("consistenzaVO", consistenzaVO);
	  }

    // Vado alla pagina di dettaglio della notifica
    %>
      <jsp:forward page= "<%= dettaglioNotificaUrl %>" />
    <%
  }

%>
