<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.ParticellaBioVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.RegistroPascoloVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.terreni.TipoAreaVO" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%
	
	String iridePageName = "terreniParticellareDettaglioRegistriCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String terreniParticellareDettaglioRegistriUrl = "/view/terreniParticellareDettaglioRegistriView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  StoricoParticellaVO storicoParticellaVO = null;
	ConsistenzaVO consistenzaVO = null;
  Vector<RegistroPascoloVO> vRegistroPascoloVO = null;
	ParticellaCertificataVO particellaCertificataVO = null;
	Long idConduzione = Long.decode(request.getParameter("idConduzione"));
	request.setAttribute("idConduzione", idConduzione);
  int annoCampagna = 0;
	
	// Per capire quali tabelle interrogare devo verificare qual è il piano di riferimento
	// selezionato dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
	// di errore
	if(filtriParticellareRicercaVO == null 
    || !Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento())) 
  {
		String messaggio = (String)AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
 		request.setAttribute("messaggioErrore",messaggio);
 		%>
  		<jsp:forward page="<%=terreniParticellareDettaglioRegistriUrl%>" />
 		<%
 		return;
	}
	else 
  {		
		// Cerco i dati della particella in relazione all'id conduzione selezionato
		try 
    {
			if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
      {
				storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneParticella(idConduzione);
			}
			else 
      {
				storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneDichiarata(idConduzione);
				consistenzaVO = anagFacadeClient.getDichiarazioneConsistenza(filtriParticellareRicercaVO.getIdPianoRiferimento());
        if(consistenzaVO.getAnnoCampagna() != null)
        {
          annoCampagna =  new Integer(consistenzaVO.getAnnoCampagna()).intValue();
        }
			}
		}
		catch(SolmrException se) 
    {
			String messaggio = (String)AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
   		request.setAttribute("messaggioErrore",messaggio);
   		%>
    		<jsp:forward page="<%=terreniParticellareDettaglioRegistriUrl%>" />
   		<%
   		return;
		}
		request.setAttribute("storicoParticellaVO", storicoParticellaVO);
		
		// Cerco i dati della particella certificata in relazione ai dati presenti
		// in anagrafe e ai criteri di ricerca precedentemente impostati
		try 
    {
			// Se arrivo da una ricerca per piano di riferimento in lavorazione e/o comprensivo
			// di conduzioni storicizzati vado a ricercare solo i dati attivi presenti
			// sulla tabella DB_PARTICELLA_CERTIFICATA
			if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
      {
				particellaCertificataVO = anagFacadeClient.findParticellaCertificataByParametersNewElegFit(storicoParticellaVO.getIstatComune(), storicoParticellaVO.getSezione(), storicoParticellaVO.getFoglio(), storicoParticellaVO.getParticella(), storicoParticellaVO.getSubalterno(), true, null);
      }
			// Altrimenti vado a reperire i dati validi alla data della dichiarazione
			// di consistenza relativa al piano di riferimento precedentemente selezionato
			else 
      {
				particellaCertificataVO = anagFacadeClient.findParticellaCertificataAllaDichiarazione(storicoParticellaVO.getIdParticella(), consistenzaVO);
			}
		}
		catch(SolmrException se) 
    {
			String messaggio = (String)AnagErrors.ERRORE_KO_PARTICELLA_CERTIFICATA;
   		request.setAttribute("messaggioErrore",messaggio);
   		%>
    		<jsp:forward page="<%=terreniParticellareDettaglioRegistriUrl%>" />
   		<%
   		return;
		}
    
    request.setAttribute("particellaCertificataVO", particellaCertificataVO);
    
    if(Validator.isNotEmpty(particellaCertificataVO)
      && particellaCertificataVO.isCertificata() && particellaCertificataVO.isUnivoca()
      && Validator.isNotEmpty(particellaCertificataVO.getIdParticella()))
    {
      try 
      {
        // Se arrivo da una ricerca per piano di riferimento in lavorazione e/o comprensivo
        // di conduzioni storicizzati vado a ricercare solo i dati attivi presenti
        // sulla tabella DB_ESITO_PASCOLO_MAGRO
        if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
        {
          vRegistroPascoloVO = gaaFacadeClient.getRegistroPascoliPianoLavorazione(
            particellaCertificataVO.getIdParticellaCertificata().longValue());
        }
        // Altrimenti vado a reperire i dati validi alla data della dichiarazione
        // di consistenza relativa al piano di riferimento precedentemente selezionato
        else 
        {
          vRegistroPascoloVO = gaaFacadeClient.getRegistroPascoliDichCons(
            particellaCertificataVO.getIdParticellaCertificata().longValue(), annoCampagna);
        }
      }
      catch(SolmrException se) 
      {
        String messaggio = (String)AnagErrors.ERRORE_KO_PARTICELLA_CERTIFICATA;
        request.setAttribute("messaggioErrore",messaggio);
        %>
          <jsp:forward page="<%=terreniParticellareDettaglioRegistriUrl%>" />
        <%
        return;
      }
    }
    
    //non trovato nulla con particella certificata
    if(vRegistroPascoloVO == null)
    {
      try 
      {
        // Se arrivo da una ricerca per piano di riferimento in lavorazione e/o comprensivo
        // di conduzioni storicizzati vado a ricercare solo i dati attivi presenti
        // sulla tabella DB_ESITO_PASCOLO_MAGRO
        if(filtriParticellareRicercaVO.getIdPianoRiferimento().intValue() <= 0) 
        {
          vRegistroPascoloVO = gaaFacadeClient.getRegistroPascoliPianoLavorazioneChiaveCatastale(
            storicoParticellaVO.getIstatComune(), storicoParticellaVO.getFoglio(), 
            storicoParticellaVO.getParticella(), storicoParticellaVO.getSezione(), storicoParticellaVO.getSubalterno());
        }
        // Altrimenti vado a reperire i dati validi alla data della dichiarazione
        // di consistenza relativa al piano di riferimento precedentemente selezionato
        else 
        {
          vRegistroPascoloVO = gaaFacadeClient.getRegistroPascoliDichConsChiaveCatastale(
            storicoParticellaVO.getIstatComune(), storicoParticellaVO.getFoglio(), 
            storicoParticellaVO.getParticella(), storicoParticellaVO.getSezione(), 
            storicoParticellaVO.getSubalterno(), annoCampagna);
        }
      }
      catch(SolmrException se) 
      {
        String messaggio = (String)AnagErrors.ERRORE_KO_PARTICELLA_CERTIFICATA;
        request.setAttribute("messaggioErrore",messaggio);
        %>
          <jsp:forward page="<%=terreniParticellareDettaglioRegistriUrl%>" />
        <%
        return;
      }
    
    }
    
		
		request.setAttribute("vRegistroPascoloVO", vRegistroPascoloVO);
	}
	
	// Vado alla pagina di dettaglio
	%>
		<jsp:forward page="<%=terreniParticellareDettaglioRegistriUrl%>" />
	
