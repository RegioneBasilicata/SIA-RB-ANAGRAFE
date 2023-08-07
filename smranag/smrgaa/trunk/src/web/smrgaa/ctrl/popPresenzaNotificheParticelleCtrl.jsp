<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%

	String iridePageName = "popPresenzaNotificheParticelleCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String popPresenzaNotificheParticelleUrl = "/view/popPresenzaNotificheParticelleView.jsp";
	String erroreUrl = "/view/erroreView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	Long idConduzioneParticella = null;
	Long idConduzioneDichiarata = null;
  Long idStoricoParticella = null;
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
	  
  if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
  {
    idConduzioneParticella = Long.decode(request.getParameter("idConduzione"));
  }
  else 
  {
    idConduzioneDichiarata = Long.decode(request.getParameter("idConduzione"));
  }
  
  

	// Vado a reperire i dati in fuzione di id_conduzione_particella...
	StoricoParticellaVO storicoParticellaVO = null;
	if(idConduzioneParticella != null) 
  {
		try 
    {
			storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
			// Se non sono stato in grado di recuperare i dati...
			if(storicoParticellaVO == null) 
      {
				String messaggio = AnagErrors.ERRORE_KO_RICERCA_NOTIFICHE_PARTICELLA;
	   		request.setAttribute("messaggioErrore",messaggio);
	   		session.setAttribute("chiudi", "chiudi");
	   		%>
	    		<jsp:forward page="<%=erroreUrl%>" />
	   		<%
	   		return;
			}
		}
		catch(SolmrException se) 
    {
			String messaggio = AnagErrors.ERRORE_KO_RICERCA_NOTIFICHE_PARTICELLA;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
  			<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
	// ... o di id_conduzione_dichiarata
	else if(idConduzioneDichiarata != null) 
  {
 		try 
    {
 			storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneDichiarata(idConduzioneDichiarata);
			// Se non sono stato in grado di recuperare i dati...
 			if(storicoParticellaVO == null) 
      {
				String messaggio = AnagErrors.ERRORE_KO_RICERCA_NOTIFICHE_PARTICELLA;
	   		request.setAttribute("messaggioErrore",messaggio);
	   		session.setAttribute("chiudi", "chiudi");
	   		%>
	      	<jsp:forward page="<%=erroreUrl%>" />
	   		<%
	   		return;
			}
		}
		// ... altrimenti segnalo messaggio di errore
		catch(SolmrException se) 
    {
			String messaggio = AnagErrors.ERRORE_KO_RICERCA_NOTIFICHE_PARTICELLA;;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
   			<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
  
  
  
  
  // Cerco i dati della particella certificata in relazione ai dati presenti
  // in anagrafe e ai criteri di ricerca precedentemente impostati
  try 
  {
    Long idDichiarazioneConsistenza = null;
    if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() > 0) 
    {
      idDichiarazioneConsistenza = filtriParticellareRicercaVO.getIdPianoRiferimento();
    }
      
    Vector<NotificaVO> vNotifiche = anagFacadeClient.getElencoNotificheForIdentificato(
      storicoParticellaVO.getIdParticella().longValue(), SolmrConstants.COD_TIPO_ENTITA_PARTICELLE, 
      anagAziendaVO.getIdAzienda().longValue(), idDichiarazioneConsistenza);
        
    request.setAttribute("vNotifiche", vNotifiche);    
    
  }
  catch(SolmrException se) 
  {
    String messaggio = (String)AnagErrors.ERRORE_KO_RICERCA_NOTIFICHE_PARTICELLA;
    request.setAttribute("messaggioErrore", messaggio);
    session.setAttribute("chiudi", "chiudi");
    %>
      <jsp:forward page="<%=erroreUrl%>" />
    <%
    return;
  }
  
  
  
  // Metto in request l'oggetto particella trovato
  request.setAttribute("storicoParticellaVO", storicoParticellaVO);
  
  
  
  
	

	// Vado alla pop-up
	%>
		<jsp:forward page= "<%=popPresenzaNotificheParticelleUrl %>" />
	

