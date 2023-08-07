<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.comune.*"%>

<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.dto.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	String iridePageName = "dichiarazioneProtocollaCtrl.jsp";
 	%>
 		<%@include file = "/include/autorizzazione.inc" %>
 	<%
 	
 	final String errMsg = "Impossibile procedere nella sezione protocolla."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
 	
  String dichiarazioneConsistenzaCtrlUrl = "/ctrl/dichiarazioneConsistenzaCtrl.jsp";
 	String actionUrl = "../layout/dichiarazioneConsistenza.htm";
 	String erroreViewUrl = "/view/erroreView.jsp";

 	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
 	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
 	RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
 	
  String[] idDichiarazioni = request.getParameterValues("radiobutton");
  Long[] elencoDichiarazioni = new Long[idDichiarazioni.length];
  int countDichProtocollate = 0;
  ConsistenzaVO consistenzaVO = null;
  
  
  // Se una delle dichiarazioni di consistenza selezionata risulta già protocollata,
  // blocco l'operazione e segnalo il problema all'utente
  if(Validator.isNotEmpty(idDichiarazioni) && (idDichiarazioni.length > 1)) 
  {
    String messaggio = "Impossibile procedere: è possibile selzionare una sola validazione alla volta.";
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  for(int i = 0; i < idDichiarazioni.length; i++) 
  {
  	elencoDichiarazioni[i] = Long.decode((String)idDichiarazioni[i]);
  	try 
  	{
   		consistenzaVO = anagFacadeClient.findDichiarazioneConsistenzaByPrimaryKey(elencoDichiarazioni[i]);
   	}
   	catch(SolmrException se) 
   	{
   		String messaggio = AnagErrors.ERRORE_KO_DICHIARAZIONE_CONSISTENZA;
   		request.setAttribute("messaggioErrore",messaggio);
   	 	request.setAttribute("pageBack", actionUrl);
   	 	%>
				<jsp:forward page="<%= erroreViewUrl %>" />
   		<%
   	 	return;
   	}
   	
   	/*AllegatoDichiarazioneVO allegatoDichiarazioneVO = gaaFacadeClient
      .getAllegatoDichiarazioneFromIdDichiarazioneMaxStoric(
          new Long(consistenzaVO.getIdDichiarazioneConsistenza()), new Long(SolmrConstants.VALIDAZIONE_ALLEGATO));*/
    
    //idDichiarazioneConsistenzaStampa valorizzato = stampa in esecuzione!!!!
    if((Long)request.getSession().getAttribute("idDichiarazioneConsistenzaStampa") != null)
    {
      String messaggio = "Impossibile procedere: è in corso la creazione della stampa della validazione";
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;        
    }
   	
   	
   	
   	if(Validator.isNotEmpty(consistenzaVO.getNumeroProtocollo())) 
   	{
   		countDichProtocollate++;
   		break;
   	}
   	else 
   	{
   		// Se l'utente non è un intermediario e non è un OPR GESTORE
    	if(!ruoloUtenza.isUtenteIntermediario() && !ruoloUtenza.isUtenteOPRGestore()) 
    	{
      	// Contatto il servizio di comune per reperire la sigla amministrazione
      	String siglaAmministrazione = null;
      	try 
      	{
        	AmmCompetenzaVO ammCompetenzaVO = anagFacadeClient.serviceFindAmmCompetenzaByCodiceAmm(ruoloUtenza.getCodiceEnte());
          // Se comune mi restituisce l'amministrazione di competenza, recupero la sigla per
         	// l'inserimento del documento
         	if(ammCompetenzaVO != null) 
         	{
         		siglaAmministrazione = ammCompetenzaVO.getSiglaAmministrazione();
          	ruoloUtenza.setSiglaAmministrazione(siglaAmministrazione);
         	}
         	// Altrimenti segnalo l'errore perchè questa informazione è indispensabile per poter inserire
         	// correttamente il documento
         	else 
         	{
         		String messaggio = AnagErrors.ERRORE_KO_LIST_AMM_COMPETENZA;
         	  request.setAttribute("messaggioErrore",messaggio);
         		request.setAttribute("pageBack", actionUrl);
         		%>
         		  <jsp:forward page="<%= erroreViewUrl %>" />
         		<%
         		return;
         	}
       	}
       	catch(SolmrException se) 
       	{
       		String messaggio = AnagErrors.ERRORE_KO_LIST_AMM_COMPETENZA;
       	  request.setAttribute("messaggioErrore",messaggio);
       		request.setAttribute("pageBack", actionUrl);
       		%>
       			<jsp:forward page="<%= erroreViewUrl %>" />
       		<%
       		return;
       	}
       	
       	
       	
       	
       	
     	}
   	}
  }
   	
  // Se una delle dichiarazioni di consistenza selezionata risulta già protocollata,
  // blocco l'operazione e segnalo il problema all'utente
  if(countDichProtocollate > 0) 
  {
	  String messaggio = "Impossibile procedere: la validazione del "+StringUtils.parseDateFieldToEuropeStandard(SolmrConstants.FULL_DATE_ORACLE_FORMAT, SolmrConstants.FULL_DATE_EUROPE_FORMAT, consistenzaVO.getDataProtocollo().toString())+" risulta già repertoriata";
		request.setAttribute("messaggioErrore",messaggio);
	  request.setAttribute("pageBack", actionUrl);
	  %>
			<jsp:forward page="<%= erroreViewUrl %>" />
	  <%
	  return;
  }
  
  
  
   	
  // Effettuo la protocollazione delle  dichiarazioni di consistenza selezionate
  //pulisco la sessione da eventuali altre richieste appese...
  //session.removeAttribute("idDichiarazioneConsistenzaRigeneraPartenza");
  try 
  {
  	anagFacadeClient.protocollaDichiarazioniConsistenza(elencoDichiarazioni, ruoloUtenza, DateUtils.getCurrentYear().toString());
  }
  catch(SolmrException se) 
  {
  	String messaggio = AnagErrors.ERRORE_KO_PROTOCOLLA_DICHIARAZIONI_CONSISTENZA;
		request.setAttribute("messaggioErrore",messaggio);
   	request.setAttribute("pageBack", actionUrl);
   	%>
			<jsp:forward page="<%= erroreViewUrl %>" />
   	<%
   	return;
  }
  
  Date parametroDataSTMPLivecycle = null;
  try 
  {
    parametroDataSTMPLivecycle = (Date)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_DATA_STMP_LIVECYCLE);
  }
  catch(SolmrException se) 
  {
    SolmrLogger.info(this, " - dichiarazoneProtocollaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_DATA_STMP_LIVECYCLE+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  if(consistenzaVO.getDataDichiarazione().after(parametroDataSTMPLivecycle))
  {
    //elimino record su db per far venire fuori clessidra....  
    session.setAttribute("idDichiarazioneConsistenzaStampaPartenza", new Long(consistenzaVO.getIdDichiarazioneConsistenza()));  
  }
   	
  // Se tutto va bene torno alla pagina di elenco delle validazioni
  %>
   	<jsp:forward page="<%= dichiarazioneConsistenzaCtrlUrl %>" />
  
