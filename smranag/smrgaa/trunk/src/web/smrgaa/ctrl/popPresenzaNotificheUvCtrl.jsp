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

	String popPresenzaNotificheUvUrl = "/view/popPresenzaNotificheUvView.jsp";
	String erroreUrl = "/view/erroreView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  Long idStoricoUnitaArborea = null;
	FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
	  
  idStoricoUnitaArborea = Long.decode(request.getParameter("idStoricoUnitaArborea"));

	// Vado a reperire i dati in fuzione di id_conduzione_particella...
	StoricoParticellaVO storicoParticellaVO = null;
	try 
  {
		storicoParticellaVO = anagFacadeClient.findStoricoParticellaArborea(idStoricoUnitaArborea);
		// Se non sono stato in grado di recuperare i dati...
		if(storicoParticellaVO == null) 
    {
			String messaggio = AnagErrors.ERRORE_KO_RICERCA_NOTIFICHE_UV;
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
		String messaggio = AnagErrors.ERRORE_KO_RICERCA_NOTIFICHE_UV;
		request.setAttribute("messaggioErrore",messaggio);
		session.setAttribute("chiudi", "chiudi");
		%>
 			<jsp:forward page="<%=erroreUrl%>" />
		<%
		return;
	} 
  
  // Cerco i dati della particella certificata in relazione ai dati presenti
  // in anagrafe e ai criteri di ricerca precedentemente impostati
  try 
  {
    Long idDichiarazioneConsistenza = null;
    if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().intValue() > 0) 
    {
      idDichiarazioneConsistenza = filtriUnitaArboreaRicercaVO.getIdPianoRiferimento();
    }
      
    Vector<NotificaVO> vNotifiche = anagFacadeClient.getElencoNotificheForIdentificato(
      storicoParticellaVO.getStoricoUnitaArboreaVO().getIdUnitaArborea(), SolmrConstants.COD_TIPO_ENTITA_UV, 
      anagAziendaVO.getIdAzienda().longValue(), idDichiarazioneConsistenza);
        
    request.setAttribute("vNotifiche", vNotifiche);    
    
  }
  catch(SolmrException se) 
  {
    String messaggio = (String)AnagErrors.ERRORE_KO_RICERCA_NOTIFICHE_UV;
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
		<jsp:forward page= "<%=popPresenzaNotificheUvUrl %>" />
	

