<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.consistenza.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.AziendaNuovaVO" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>

<%
	
	String iridePageName = "richiamoPianoGraficoCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	
	String richiamoPianoGraficoUrl = "/view/richiamoPianoGraficoView.jsp";
	String erroreViewUrl = "/view/erroreView.jsp";
  
	
	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String operazione = request.getParameter("operazione");
  String idDichiarazioneConsistenzaStr = request.getParameter("idDichiarazioneConsistenza");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
	
	
	
	String parametroUrlPianoGrafico = null;
  try 
  {
    parametroUrlPianoGrafico = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_URL_PIANO_GRAFICO);
    request.setAttribute("parametroUrlPianoGrafico", parametroUrlPianoGrafico);
  }
  catch(SolmrException se) 
  {
    String messaggio = "Url piano grafico non trovata";
    request.setAttribute("messaggioErrore",messaggio);
    %>
      <jsp:forward page="<%=richiamoPianoGraficoUrl%>" />
    <%
    return;
  }
	
	
	// Recupero l'elenco dei tipi motivo dichiarazione
	Vector<ConsistenzaVO> vElencoDichiarazioni = null;
	try 
	{
		vElencoDichiarazioni = anagFacadeClient.getListDichiarazioniPianoGrafico(
		  anagAziendaVO.getIdAzienda().longValue());
		request.setAttribute("vElencoDichiarazioni", vElencoDichiarazioni);
	}
	catch(SolmrException se) 
	{
		String messaggio = AnagErrors.ERRORE_KO_DICHIARAZIONE_CONSISTENZA;
  	request.setAttribute("messaggioErrore",messaggio);
  	%>
  		<jsp:forward page="<%=richiamoPianoGraficoUrl%>" />
  	<%
  	return;
	}
	
	if(vElencoDichiarazioni == null)
	{
	  String messaggio = "NON E' POSSIBILE GENERARE IL PIANO GRAFICO PER L'AZIENDA SELEZIONATA";
    request.setAttribute("messaggioErrore",messaggio);
    %>
      <jsp:forward page="<%=richiamoPianoGraficoUrl%>" />
    <%
    return;
	}
	  
	
	
	
   
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("lancio"))
  {
    //Leggo controlli e messaggio...
    StringTokenizer strToken = new StringTokenizer(idDichiarazioneConsistenzaStr, "_");
    Long idDichiarazioneConsistenza = new Long(strToken.nextToken());    
    String codiceUtilita = strToken.nextToken();
    Long idAccessoPianoGrafico = new Long(strToken.nextToken());
    Vector<ErrAnomaliaDicConsistenzaVO> vErroriAnomalie = anagFacadeClient.getErroriAnomalieDichConsPG(
        idDichiarazioneConsistenza, 10);
    request.setAttribute("vErroriAnomalie", vErroriAnomalie);
    //Salvo su db indicando che è in corso la generazione del piano grafico
    
    EsitoPianoGraficoVO esitoPianoGraficoVOOld = anagFacadeClient.getEsitoPianoGraficoFromAccesso(
          idAccessoPianoGrafico);
    Long idAccessoPianoGraficoNew = null; 
    if("S".equalsIgnoreCase(esitoPianoGraficoVOOld.getFlagEseguiControlli()))
    {
	       
	    try 
		  {
		    idAccessoPianoGraficoNew = anagFacadeClient.insertStatoIncorsoPG(idAccessoPianoGrafico, ruoloUtenza.getIdUtente());
		  }
		  catch(SolmrException se) 
		  {
		    String messaggio = "Errore aggiornamento a stato in corso";
		    request.setAttribute("messaggioErrore",messaggio);
		    %>
		      <jsp:forward page="<%=richiamoPianoGraficoUrl%>" />
		    <%
		    return;
		  }
		}
		else
		{
		  idAccessoPianoGraficoNew = idAccessoPianoGrafico;
		}
    
    
    
    
    
    EsitoPianoGraficoVO esitoPianoGraficoVO = anagFacadeClient.getEsitoPianoGraficoFromAccesso(
        idAccessoPianoGraficoNew);
    request.setAttribute("esitoPianoGraficoVO", esitoPianoGraficoVO);
     
  }  
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("refresh"))
  {
    //Salvo su db indicando che è in corso la generazione del piano grafico
    //Leggo controlli e messaggio...
    StringTokenizer strToken = new StringTokenizer(idDichiarazioneConsistenzaStr, "_");
    Long idDichiarazioneConsistenza = new Long(strToken.nextToken());
    String codiceUtilita = strToken.nextToken();
    Long idAccessoPianoGrafico = new Long(strToken.nextToken());
    Vector<ErrAnomaliaDicConsistenzaVO> vErroriAnomalie = anagFacadeClient.getErroriAnomalieDichConsPG(
        idDichiarazioneConsistenza, 10);
    request.setAttribute("vErroriAnomalie", vErroriAnomalie);
    
    EsitoPianoGraficoVO esitoPianoGraficoVO = anagFacadeClient.getEsitoPianoGraficoFromAccesso(
        idAccessoPianoGrafico);
    request.setAttribute("esitoPianoGraficoVO", esitoPianoGraficoVO);
  }
    
	
	
	// Vado alla pagina della scelta tipo motivo dichiarazione
	%>
		<jsp:forward page="<%=richiamoPianoGraficoUrl%>" />
	