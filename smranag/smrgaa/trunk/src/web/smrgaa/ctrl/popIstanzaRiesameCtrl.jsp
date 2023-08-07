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
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.IstanzaRiesameVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>

<%

	String iridePageName = "popIstanzaRiesameCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String popIstanzaRiesameUrl = "/view/popIstanzaRiesameView.jsp";
	String erroreUrl = "/view/erroreView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	Long idConduzioneParticella = null;
	Long idConduzioneDichiarata = null;
  Long idStoricoParticella = null;
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
  FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO)session.getAttribute("filtriUnitaArboreaRicercaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	ParticellaCertificataVO particellaCertificataVO = null;
  
	
  
  
  //menu' terreni
  if (filtriParticellareRicercaVO != null)
  {
    if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
    {
      idConduzioneParticella = Long.decode(request.getParameter("idConduzione"));
    }
    else 
    {
      idConduzioneDichiarata = Long.decode(request.getParameter("idConduzione"));
    }
  }
  //menu unità vitate
  else
  {
    idStoricoParticella = Long.decode(request.getParameter("idStoricoParticella"));
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
				String messaggio = AnagErrors.ERRORE_KO_POP_ISTANZA_RIESAME_TERRENI;
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
			String messaggio = AnagErrors.ERRORE_KO_POP_ISTANZA_RIESAME_TERRENI;
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
				String messaggio = AnagErrors.ERRORE_KO_POP_ISTANZA_RIESAME_TERRENI;
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
			String messaggio = AnagErrors.ERRORE_KO_POP_ISTANZA_RIESAME_TERRENI;;
			request.setAttribute("messaggioErrore",messaggio);
			session.setAttribute("chiudi", "chiudi");
			%>
   			<jsp:forward page="<%=erroreUrl%>" />
			<%
			return;
		}
	}
  //arrivo dal menu' delle unità vitate 
  else
  {
    try 
    {
      storicoParticellaVO = anagFacadeClient.findStoricoParticellaByPrimaryKey(idStoricoParticella);
      // Se non sono stato in grado di recuperare i dati...
      if(storicoParticellaVO == null) 
      {
        String messaggio = AnagErrors.ERRORE_KO_POP_ISTANZA_RIESAME_UV;
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
      String messaggio = AnagErrors.ERRORE_KO_POP_ISTANZA_RIESAME_UV;;
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
    //menu terreni
    if (filtriParticellareRicercaVO != null)
    {
      Date dataInserimentoDichiarazione = null;
      if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() > 0) 
      {
        ConsistenzaVO consistenzaVO = anagFacadeClient.getDichiarazioneConsistenza(filtriParticellareRicercaVO.getIdPianoRiferimento());
        dataInserimentoDichiarazione = consistenzaVO.getDataInserimentoDichiarazione();
      }
      
      Vector<IstanzaRiesameVO> vIstanzaRiesame = gaaFacadeClient.getFasiIstanzaRiesame(
        anagAziendaVO.getIdAzienda().longValue(), 
        storicoParticellaVO.getIdParticella().longValue(), dataInserimentoDichiarazione);
        
      request.setAttribute("dataInserimentoDichiarazione", dataInserimentoDichiarazione);
      request.setAttribute("vIstanzaRiesame", vIstanzaRiesame);
      
      
    }
    //menu unità vitate
    else
    {
      Date dataInserimentoDichiarazione = null;
      if(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento().intValue() > 0) 
      {
        ConsistenzaVO consistenzaVO = anagFacadeClient.getDichiarazioneConsistenza(filtriUnitaArboreaRicercaVO.getIdPianoRiferimento());
        dataInserimentoDichiarazione = consistenzaVO.getDataInserimentoDichiarazione();       
      }
      
      Vector<IstanzaRiesameVO> vIstanzaRiesame = gaaFacadeClient.getFasiIstanzaRiesame(
        anagAziendaVO.getIdAzienda().longValue(), 
        storicoParticellaVO.getIdParticella().longValue(), dataInserimentoDichiarazione);
        
      request.setAttribute("dataInserimentoDichiarazione", dataInserimentoDichiarazione);
      request.setAttribute("vIstanzaRiesame", vIstanzaRiesame);    
    }
    
  }
  catch(SolmrException se) 
  {
    String messaggio = (String)AnagErrors.ERRORE_KO_FASI_ISTANZA_RIESAME;
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
		<jsp:forward page= "<%=popIstanzaRiesameUrl %>" />
	

