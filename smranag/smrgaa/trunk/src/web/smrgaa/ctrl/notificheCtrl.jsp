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
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, noRemove);

 	String iridePageName = "notificheCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%

 	String notificheUrl = "/view/notificheView.jsp";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

 	ValidationErrors errors = new ValidationErrors();

 	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
 	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
 	// in modo che venga sempre effettuato
 	try {
   	anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
 	}
 	catch(SolmrException se) {
   	request.setAttribute("statoAzienda", se);
 	}

 	// Creo l'oggetto contenente i criteri per effettuare la ricerca
 	NotificaVO notificaVO = new NotificaVO();
 	notificaVO.setIdProcedimentoDestinatario((Long)SolmrConstants.get("ID_PROCEDIMENTO_ANAG"));
 	notificaVO.setIdAzienda(anagAziendaVO.getIdAzienda());

 	Vector<NotificaVO> elencoNotifiche = null;

 	// L'utente ha premuto il pulsante aggiorna
 	if(Validator.isNotEmpty(request.getParameter("operazione")) 
 	  && request.getParameter("operazione").equalsIgnoreCase("aggiorna")) 
 	{
   	session.removeAttribute("indice");
   	// Recupero il tipo di elenco che vuole visualizzare l'utente
   	boolean storico = request.getParameter("storico") != null;

   	// Metto il valore in request
   	request.setAttribute("storico", new Boolean(storico));

   	// Rieffettuo la ricerca delle notifiche
   	try 
   	{
    	elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaVO, new Boolean(storico), null);
   	}
   	catch(SolmrException se) 
   	{
    	if(!storico) 
    	{
      	if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
      	{
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("error",error);
         	request.setAttribute("errors", errors);
         	request.getRequestDispatcher(notificheUrl).forward(request, response);
         	return;
       	}
       	else 
       	{
        	request.setAttribute("messaggio", se.getMessage());
        	%>
        		<jsp:forward page= "<%= notificheUrl %>" />
        	<%
       	}
     	}
     	else 
     	{
      	if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_FOR_AZIENDA"))) 
      	{
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("error",error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(notificheUrl).forward(request, response);
        	return;
       	}
       	else 
       	{
        	request.setAttribute("messaggio", se.getMessage());
        	%>
        		<jsp:forward page= "<%= notificheUrl %>" />
        	<%
       	}
     	}
    }
    // Se trovo delle notifiche metto il vettore in request
    request.setAttribute("elencoNotifiche", elencoNotifiche);
    // Vado alla pagina di elenco delle notifiche
    %>
     		<jsp:forward page="<%= notificheUrl %>"/>
    <%
  }
  // L'utente ha selezionato una delle due frecce per proseguire la navigazione
  // su più pagine
  else if(Validator.isNotEmpty(request.getParameter("operazione")) 
    && request.getParameter("operazione").equalsIgnoreCase("freccia")) 
  {
  	// Recupero il tipo di elenco richiesto dall'utente nell'ultimo
   	// aggiornamento
   	boolean storico = request.getParameter("storico") != null;
   	// Metto il valore in request
   	request.setAttribute("storico", new Boolean(storico));
   	// Rieffettuo la ricerca delle notifiche
    try 
    {
    	elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaVO, new Boolean(storico), null);
    }
    catch(SolmrException se) 
    {
    	if(!storico) 
    	{
      	if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
      	{
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("error",error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(notificheUrl).forward(request, response);
        	return;
        }
        else 
        {
        	request.setAttribute("messaggio", se.getMessage());
        	%>
        		<jsp:forward page= "<%= notificheUrl %>" />
          <%
        }
      }
      else 
      {
      	if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_FOR_AZIENDA"))) 
      	{
        	ValidationError error = new ValidationError(se.getMessage());
        	errors.add("error",error);
        	request.setAttribute("errors", errors);
        	request.getRequestDispatcher(notificheUrl).forward(request, response);
        	return;
        }
        else 
        {
        	request.setAttribute("messaggio", se.getMessage());
        	%>
        		<jsp:forward page= "<%= notificheUrl %>" />
        	<%
        }
      }
    }
    // Se trovo delle notifiche metto il vettore in request
    request.setAttribute("elencoNotifiche", elencoNotifiche);
    String indice = request.getParameter("valoreIndice");
    session.setAttribute("indice",indice);
    %>
    	<jsp:forward page="<%= notificheUrl %>"/>
    <%
 	}
  // L'utente ha selezionato la funzione notifiche
  else 
  {
    boolean storico = request.getParameter("storico") != null;
    // Metto il valore in request
    request.setAttribute("storico", new Boolean(storico));
   	// Effettuo la ricerca delle notifiche legate all'azienda agricola selezionata
   	try 
   	{
    	elencoNotifiche = anagFacadeClient.getElencoNotificheByIdAzienda(notificaVO, new Boolean(storico), null);
    }
    catch(SolmrException se) 
    {
    	if(!se.getMessage().equalsIgnoreCase((String)AnagErrors.get("ERR_NO_NOTIFICHE_ATTIVE_FOR_AZIENDA"))) 
    	{
      	ValidationError error = new ValidationError(se.getMessage());
      	errors.add("error",error);
      	request.setAttribute("errors", errors);
      	request.getRequestDispatcher(notificheUrl).forward(request, response);
      	return;
      }
      else 
      {
      	request.setAttribute("messaggio", se.getMessage());
      	%>
      		<jsp:forward page= "<%= notificheUrl %>" />
      	<%
      }
    }

    // Se trovo delle notifiche metto il vettore in request
    request.setAttribute("elencoNotifiche", elencoNotifiche);

    // Vado alla pagina di elenco delle notifiche
    %>
    	<jsp:forward page= "<%= notificheUrl %>" />
    <%
  }

%>
