<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.attestazioni.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "attestazioniDettaglioCtrl.jsp";

	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	String attestazioniDettaglioUrl = "/view/attestazioniDettaglioView.jsp";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

	// Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  	// E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  	// in modo che venga sempre effettuato
	try 
  {
		anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) 
  {
   	request.setAttribute("statoAzienda", se);
  }

	// Recupero il piano di riferimento selezionato
 	String pianoRiferimento = request.getParameter("idDichiarazioneConsistenza");
	Long idPianoRiferimento = null;
	// Nel caso in cui non sia valorizzato vuol dire che è la prima volta che accedo alla pagina e quindi imposto il piano di riferimento alla data odierna
	if(!Validator.isNotEmpty(pianoRiferimento)) 
  {
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    idPianoRiferimento = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null);
		//idPianoRiferimento = new Long("-1");
	}
	else 
	{
		idPianoRiferimento = Long.decode(pianoRiferimento);
	}
	//request.setAttribute("idPianoRiferimento", idPianoRiferimento);

	// Effettuo la ricerca in funzione del piano di riferimento selezionato
	TipoAttestazioneVO[] elencoAttestazioni = null;
	String[] orderBy = {SolmrConstants.ORDER_BY_CODICE_ATTESTAZIONE_ASC, SolmrConstants.ORDER_BY_TIPO_ATTESTAZIONE_GRUPPO_ASC, SolmrConstants.ORDER_BY_TIPO_ATTESTAZIONE_ORDINAMENTO_ASC};
	try 
  {
		if(idPianoRiferimento.longValue() == -1) 
    {
			elencoAttestazioni = anagFacadeClient.getListTipoAttestazioneOfPianoRiferimento(
			  anagAziendaVO.getIdAzienda(), null, null, null, orderBy, 
			  SolmrConstants.VOCE_MENU_ATTESTAZIONI_DICHIARAZIONI);
		}
		else 
    {
			ConsistenzaVO consistenzaVO = null;
			try 
      {
				consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(idPianoRiferimento);
			}
			catch(SolmrException se) 
      {
				String messaggio = AnagErrors.ERRORE_KO_DICHIARAZIONE_CONSISTENZA;
	   		request.setAttribute("messaggioErrore",messaggio);
	   		%>
	    		<jsp:forward page="<%=attestazioniDettaglioUrl%>" />
	   		<%
	   		return;
			}
			
			Date dataAnnoCampagna = DateUtils.parseDate("31/12/"+consistenzaVO.getAnno());
      if(Validator.isNotEmpty(consistenzaVO.getAnnoCampagna()))
      {
        dataAnnoCampagna = DateUtils.parseDate("31/12/"+consistenzaVO.getAnnoCampagna());
      }   
      
      elencoAttestazioni = anagFacadeClient.getListTipoAttestazioneOfPianoRiferimento(
        null, String.valueOf(consistenzaVO.getCodiceFotografiaTerreni()), 
        dataAnnoCampagna, null, orderBy, SolmrConstants.VOCE_MENU_ATTESTAZIONI_DICHIARAZIONI);
      
		}
	}
	catch(SolmrException se) 
	{
		String messaggio = AnagErrors.ERR_KO_SEARCH_ATTESTAZIONI;
 		request.setAttribute("messaggioErrore",messaggio);
 		%>
  		<jsp:forward page="<%=attestazioniDettaglioUrl%>" />
 		<%
 		return;
	}
	request.setAttribute("elencoAttestazioni", elencoAttestazioni);

	// Vado alla pagina di dettaglio
	%>
		<jsp:forward page="<%=attestazioniDettaglioUrl%>" />
	
