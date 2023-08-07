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
	
	String iridePageName = "terreniParticellareDettaglioCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String terreniParticellareDettaglioUrl = "/view/terreniParticellareDettaglioView.jsp";
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO)session.getAttribute("filtriParticellareRicercaVO");
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  StoricoParticellaVO storicoParticellaVO = null;
	ConsistenzaVO consistenzaVO = null;
  Vector<ProprietaCertificataVO> vProprietaCertificataVO = null;
	ParticellaCertificataVO particellaCertificataVO = null;
  ParticellaBioVO particellaBioVO = null;
	Long idConduzione = Long.decode(request.getParameter("idConduzione"));
	request.setAttribute("idConduzione", idConduzione);
  int annoCampagna = 0;
  Vector<TipoAreaVO> vTipoArea = null;
	
	// Per capire quali tabelle interrogare devo verificare qual è il piano di riferimento
	// selezionato dall'utente: se per qualche motivo non riesco a reperirlo segnalo messaggio
	// di errore
	if(filtriParticellareRicercaVO == null 
    || !Validator.isNotEmpty(filtriParticellareRicercaVO.getIdPianoRiferimento())) 
  {
		String messaggio = (String)AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
 		request.setAttribute("messaggioErrore",messaggio);
 		%>
  		<jsp:forward page="<%=terreniParticellareDettaglioUrl%>" />
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
        particellaBioVO = gaaFacadeClient.getParticellaBio(storicoParticellaVO.getIdParticella().longValue(),
          anagAziendaVO.getIdAzienda().longValue(), null);
        vTipoArea = gaaFacadeClient.getValoriTipoAreaParticella(storicoParticellaVO.getIdParticella(), null);
			}
			else 
      {
				storicoParticellaVO = anagFacadeClient.findStoricoParticellaVOByIdConduzioneDichiarata(idConduzione);
				consistenzaVO = anagFacadeClient.getDichiarazioneConsistenza(filtriParticellareRicercaVO.getIdPianoRiferimento());
        particellaBioVO = gaaFacadeClient.getParticellaBio(storicoParticellaVO.getIdParticella().longValue(),
          anagAziendaVO.getIdAzienda().longValue(), consistenzaVO.getDataInserimentoDichiarazione());
        if(consistenzaVO.getAnnoCampagna() != null)
        {
          annoCampagna =  new Integer(consistenzaVO.getAnnoCampagna()).intValue();
        }
        
        vTipoArea = gaaFacadeClient.getValoriTipoAreaParticella(storicoParticellaVO.getIdParticella(), consistenzaVO.getDataInserimentoDichiarazione());
			}
		}
		catch(SolmrException se) 
    {
			String messaggio = (String)AnagErrors.ERRORE_KO_DETTAGLIO_PARTICELLA;
   		request.setAttribute("messaggioErrore",messaggio);
   		%>
    		<jsp:forward page="<%=terreniParticellareDettaglioUrl%>" />
   		<%
   		return;
		}
		request.setAttribute("storicoParticellaVO", storicoParticellaVO);
    request.setAttribute("particellaBioVO", particellaBioVO);
    request.setAttribute("vTipoArea", vTipoArea);
		
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
    		<jsp:forward page="<%=terreniParticellareDettaglioUrl%>" />
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
          vProprietaCertificataVO = anagFacadeClient
            .getListDettaglioProprietaCertifByIdParticella(
            particellaCertificataVO.getIdParticella().longValue(), null);
        }
        // Altrimenti vado a reperire i dati validi alla data della dichiarazione
        // di consistenza relativa al piano di riferimento precedentemente selezionato
        else 
        {          
          vProprietaCertificataVO = anagFacadeClient
            .getListDettaglioProprietaCertifByIdParticella(
            particellaCertificataVO.getIdParticella().longValue(), consistenzaVO.getDataInserimentoDichiarazione());
        }
      }
      catch(SolmrException se) 
      {
        String messaggio = (String)AnagErrors.ERRORE_KO_PARTICELLA_CERTIFICATA;
        request.setAttribute("messaggioErrore",messaggio);
        %>
          <jsp:forward page="<%=terreniParticellareDettaglioUrl%>" />
        <%
        return;
      }
      //request.setAttribute("vRegistroPascoloVO", vRegistroPascoloVO);
      request.setAttribute("vProprietaCertificataVO", vProprietaCertificataVO);
    }        
	}
	
	// Vado alla pagina di dettaglio
	%>
		<jsp:forward page="<%=terreniParticellareDettaglioUrl%>" />
	
