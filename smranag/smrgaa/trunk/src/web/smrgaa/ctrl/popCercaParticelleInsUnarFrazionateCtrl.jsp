<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%
	
	String iridePageName = "popCercaParticelleInsUnarFrazionateCtrl.jsp";
	%>
		<%@include file="/include/autorizzazione.inc"%>
	<%
	
	String popCercaParticelleInsUnarFrazionateUrl = "/view/popCercaParticelleInsUnarFrazionateView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	String messaggioErrore = null;
	
	// Recupero i parametri di ricerca impostati dall'utente
	String siglaProvincia = request.getParameter("siglaProvinciaAssocia");
	String descComune = request.getParameter("descComuneAssocia");
	String sezione = request.getParameter("sezioneAssocia");
	String foglio = request.getParameter("foglioAssocia");
	String particella = request.getParameter("particellaAssocia");
	
	
	
	// Recupero i valori relativi alla destinazione produttiva
  TipoUtilizzoVO[] elencoTipiUsoSuolo = null;
  try 
  {
    String[] orderBy = {(String)SolmrConstants.ORDER_BY_TIPO_UTILIZZO_DESCRIZIONE};
    elencoTipiUsoSuolo = anagFacadeClient.getListTipiUsoSuoloByTipo(SolmrConstants.TIPO_UTILIZZO_VIGNETO, true, orderBy);
    request.setAttribute("elencoTipiUsoSuolo", elencoTipiUsoSuolo);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - unitaArboreeEliminaCtrl.jsp - FINE PAGINA");
    String messaggio = AnagErrors.ERRORE_KO_DESTINAZIONE_PRODUTTIVA+".\n"+se.toString();
    request.setAttribute("messaggioErrore", messaggio);
    %>
      <jsp:forward page="<%= popCercaParticelleInsUnarFrazionateUrl %>" />
    <%
  }
	
	
	
	
	
	// L'utente ha cliccato il pulsante inserisci
	if(Validator.isNotEmpty(request.getParameter("conferma"))) 
	{
		// Recupero l'elenco delle particelle arboree selezionate
		String[] elencoStoricoUnitaArborea = request.getParameterValues("idStoricoUnitaArborea");
		Hashtable elencoUnitaArboree = new Hashtable();
		// Cerco la particella arborea sul DB e la inserisco su hashMap
		try 
		{
			for(int i = 0; i < elencoStoricoUnitaArborea.length; i++) 
			{
				Long idStoricoUnitaArborea = Long.decode((String)elencoStoricoUnitaArborea[i]);
				StoricoParticellaVO storicoParticellaArboreaVO = anagFacadeClient.findStoricoParticellaArborea(idStoricoUnitaArborea);
				elencoUnitaArboree.put(storicoParticellaArboreaVO.getStoricoUnitaArboreaVO().getIdStoricoUnitaArborea(), storicoParticellaArboreaVO);
				session.setAttribute("elencoUnitaArboree", elencoUnitaArboree);
			}
		}
		catch(SolmrException se) 
		{
			messaggioErrore = AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
			request.setAttribute("messaggioErrore", messaggioErrore);
			%>
				<jsp:forward page="<%= popCercaParticelleInsUnarFrazionateUrl %>" />
			<%
		}
		%>
			<html>
				<head>
					<script type="text/javascript">
						window.opener.location.href='../layout/unitaArboreeInserisciFrazionate.htm';
						window.close();
					</script>
				</head>
			</html>
		<%
		return;
	}
	// L'utente ha scelto cerca dall'importa unità arborea
	else 
	{
		// Recupero il codice istat comune
	 	ComuneVO comuneVO = null;
		String istatComune = null;
		if(Validator.isNotEmpty(descComune)) 
		{
		 	try 
		 	{
		    comuneVO = anagFacadeClient.getComuneByParameters(descComune, siglaProvincia, null, SolmrConstants.FLAG_S, null);
		    if(comuneVO != null) 
		    {
		    	istatComune = comuneVO.getIstatComune();
		    }
		    else 
		    {
		    	messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_NO_PARTICELLE_FOUND;
		    	request.setAttribute("messaggioErrore", messaggioErrore);
		    }
		  }
		 	catch(SolmrException se) 
		 	{
		  	messaggioErrore = AnagErrors.ERRORE_COMUNE_INESISTENTE;
		  	request.setAttribute("messaggioErrore", messaggioErrore);
				%>
					<jsp:forward page="<%= popCercaParticelleInsUnarFrazionateUrl %>" />
				<%
		 	}
		}
		
		if(!Validator.isNotEmpty(messaggioErrore)) 
    {
			// Recupero le particelle a cui associare l'unità arborea
			StoricoParticellaVO[] elencoParticelle = null;
			try 
			{
				String[] orderBy = {SolmrConstants.ORDER_BY_STORICO_PARTICELLA_LOGIC_KEY};
				elencoParticelle = anagFacadeClient.getListStoricoParticelleArboreeByParameters(istatComune, sezione, foglio, particella, null, true, orderBy);
				request.setAttribute("elencoParticelle", elencoParticelle);
			}
			catch(SolmrException se) 
			{
				if(!se.getMessage().equalsIgnoreCase(AnagErrors.ERRORE_FILTRI_GENERICI)) 
				{
					messaggioErrore = AnagErrors.ERRORE_KO_SEARCH_PARTICELLE;
				}
				else 
				{
					messaggioErrore = se.getMessage(); 
				}
				request.setAttribute("messaggioErrore", messaggioErrore);
				%>
					<jsp:forward page="<%= popCercaParticelleInsUnarFrazionateUrl %>" />
				<%
			}	
		}
		
		// Vado all'elenco delle particelle
		%>
			<jsp:forward page="<%= popCercaParticelleInsUnarFrazionateUrl %>" />
		<%
	}
	
%>