<%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%!
	public static String URL="/view/dichiarazioneConsistenzaView.jsp";
  public static String INSERT_URL="/ctrl/dichiarazioneConsistenzaNewCtrl.jsp";
%>
<%

  String iridePageName = "dichiarazioneConsistenzaCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

	// Pulisco la sessione dai filtri di altre sezioni
  String noRemove = "";
  WebUtils.removeUselessFilter(session, noRemove);
  
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione validazioni."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	session.removeAttribute("errAnomaliaDicConsistenzaRicercaVO");
	session.removeAttribute("anomalieDichiarazioniConsistenza");
	

 	

 	AnagFacadeClient client = new AnagFacadeClient();
 	AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 	ValidationError errori = (ValidationError)request.getAttribute("error");
 	ValidationErrors errors = new ValidationErrors();
  
  Date parametroDataSTMPLivecycle = null;
  try 
  {
    parametroDataSTMPLivecycle = (Date)client.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DATA_STMP_LIVECYCLE);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - dichiarazoneConsistenzaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DATA_STMP_LIVECYCLE+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  request.setAttribute("parametroDataSTMPLivecycle", parametroDataSTMPLivecycle);
  
  BigDecimal parametroDeleyNewStampa = null;
  try 
  {
    parametroDeleyNewStampa = (BigDecimal)client.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DELAY_NEW_STAMPA);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - dichiarazoneConsistenzaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DELAY_NEW_STAMPA+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  request.setAttribute("parametroDeleyNewStampa", parametroDeleyNewStampa);
 	
 	
 	if(errori != null) 
 	{
    errors.add("error", errori);
    request.setAttribute("errors", errors);
  }
 	
 	
 	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
 	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
 	// in modo che venga sempre effettuato
 	try 
 	{
   	client.checkStatoAzienda(anagVO.getIdAzienda());
 	}
 	catch(SolmrException se) 
 	{
   	request.setAttribute("statoAzienda", se);
 	}

 	if(request.getParameter("inserisci.x") != null) 
 	{
   	try 
   	{
     	%>
     		<jsp:forward page="<%=INSERT_URL%>" />
     	<%
   	}
   	catch(Exception e) 
   	{
     	SolmrLogger.info(this, " - dichiarazoneConsistenzaCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DATA_STMP_LIVECYCLE+".\n"+e.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
   	}
 	}
 	else 
 	{
   	// Visualizzazione dichiarazioni di consistenza
   	try 
   	{
     	//Recupero tutte le dichiarazioni di consistenza
     	Vector<ConsistenzaVO> dichiarazioniConsistenza = client.getDichiarazioniConsistenza(anagVO.getIdAzienda());
     	request.setAttribute("dichiarazioniConsistenza", dichiarazioniConsistenza);
   	}
   	catch(Exception e) 
   	{
     	SolmrLogger.info(this, " - dichiarazoneConsistenzaCtrl.jsp - FINE PAGINA");
	    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DATA_STMP_LIVECYCLE+".\n"+e.toString();
	    request.setAttribute("messaggioErrore",messaggio);
	    request.setAttribute("pageBack", actionUrl);
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
   	}
   	%>
   		<jsp:forward page="<%=URL%>" />
   	<%
 	}
%>
