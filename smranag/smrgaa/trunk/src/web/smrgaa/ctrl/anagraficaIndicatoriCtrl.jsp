<%@ page language="java"
    contentType="text/html"
%>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.AziendaAtecoSecVO" %>
<%@ page import="it.csi.smranag.smrgaa.dto.anagrafe.GruppoGreeningVO" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.PianoRiferimentoUtils" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.exception.SolmrException" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.Vector"%>
<%@ page import="it.csi.solmr.dto.CodeDescription" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
  String iridePageName = "anagraficaIndicatoriCtrl.jsp";
   %><%@include file = "/include/autorizzazione.inc" %><%
   
  SolmrLogger.debug(this, "- anagraficaIndicatoriCtrl.jsp -  INIZIO PAGINA");
  
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Vector<AziendaAtecoSecVO> vAziendaAtecoSec = null;
  Vector<GruppoGreeningVO> gruppiGreening = null;
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  
  // Recupero la dichiarazione di consistenza selezionata
 	String idDichiarazioneConsistenza = request.getParameter("idDichiarazioneConsistenza");
	Long idDichConsistenza = null;
	// Nel caso in cui non sia valorizzato vuol dire che è la prima volta che accedo alla pagina 
  //e quindi imposto il piano di riferimento alla data odierna (idDichConsistenza == -1)
	if (Validator.isEmpty(idDichiarazioneConsistenza)) {
    PianoRiferimentoUtils pianoRiferimentoUtils = new PianoRiferimentoUtils();
    AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
    RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
    
    idDichConsistenza = pianoRiferimentoUtils.primoIngressoAlPianoRiferimento(anagFacadeClient, 
      ruoloUtenza, anagAziendaVO.getIdAzienda(), null);
	}else {
		idDichConsistenza = new Long(idDichiarazioneConsistenza);
	}
	
	if((idDichConsistenza != null)
	  && (idDichConsistenza.compareTo(new Long(0)) > 0))
  {
    vAziendaAtecoSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAziendaAndValid(
      anagAziendaVO.getIdAzienda().longValue(), idDichConsistenza.longValue());
    CodeDescription codeAteco = gaaFacadeClient.getAttivitaAtecoAllaValid(
      anagAziendaVO.getIdAzienda().longValue(), idDichConsistenza.longValue());
    request.setAttribute("codeAteco", codeAteco);
  }
  else
  {	
	  vAziendaAtecoSec = gaaFacadeClient.getListActiveAziendaAtecoSecByIdAzienda(
      anagAziendaVO.getIdAzienda().longValue());
  }
  
  request.setAttribute("vAziendaAtecoSec", vAziendaAtecoSec);
	
	// Effettuo la ricerca dei pagamenti ecologici in funzione della dichiarazione di consistenza selezionata
	try {
		gruppiGreening = gaaFacadeClient.getListGruppiGreening(idDichConsistenza, anagAziendaVO.getIdAzienda());
  	request.setAttribute("gruppiGreening", gruppiGreening);
	}catch (SolmrException se) {
		String messaggio = AnagErrors.ERR_KO_SEARCH_ANAG_GREENING;
 		request.setAttribute("messaggioErrore", messaggio);
	}
	
	//Controllo se in sessione esistono errori da altre pagine (ad esempio la pagina di modifica)
	ValidationError errors = (ValidationError)session.getAttribute(SolmrConstants.SESSION_ERRORI_PAGINA);
	
	if (errors!=null) {
		request.setAttribute("errors", errors);
		session.removeAttribute(SolmrConstants.SESSION_ERRORI_PAGINA);
	}

%>
    <jsp:forward page="/view/anagraficaIndicatoriView.jsp"/>
  <%
  SolmrLogger.debug(this,"- anagraficaIndicatoriCtrl.jsp -  FINE PAGINA");
%>