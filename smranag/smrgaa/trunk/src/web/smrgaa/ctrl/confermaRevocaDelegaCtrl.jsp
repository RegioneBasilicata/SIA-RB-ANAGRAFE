<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%

	String iridePageName = "confermaRevocaDelegaCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%
	String revocaDelegaCtrl = "../ctrl/revocaDelegaCtrl.jsp";
	String confermaRevocaDelegaUrl = "../view/confermaRevocaDelegaView.jsp";
  String actionUrl = "../layout/ricerca.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione revoca delega."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";

	AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  String operazione = request.getParameter("operazione");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  DelegaVO delegaVO = null;

	ValidationErrors errors = new ValidationErrors();
  
  String parametroGDEL = null;
  try 
  {
    parametroGDEL = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_GDEL);
    request.setAttribute("parametroGDEL", parametroGDEL);
  }
  catch(SolmrException se) 
  {
    String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_GDEL+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  try 
  {
    delegaVO = anagFacadeClient.getDelegaByAziendaAndIdProcedimento(anagAziendaVO.getIdAzienda(), Long.decode(String.valueOf(SolmrConstants.ID_PROCEDIMENTO_ANAGRAFE)));
    request.setAttribute("delegaVO", delegaVO);
  }
  catch(SolmrException se) 
  {
    String messaggio = errMsg+": "+AnagErrors.ERR_ESTRAZIONE_GEST_FASCICOLO+".\n"+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  

	// Ho selezionato il pulsante indietro
	if(Validator.isNotEmpty(operazione)  && operazione.equalsIgnoreCase("conferma")) 
  {
  	try
    {
      String dataFineMandato = request.getParameter("dataFineMandato");
      Date dataTmp = DateUtils.parseDate(dataFineMandato);
      delegaVO.setDataFineMandato(dataTmp); 
      String dataRicRitorno = request.getParameter("dataRicRitorno");
      dataTmp = DateUtils.parseDate(dataRicRitorno);      
      delegaVO.setDataRicevutaRitornoDelega(dataTmp);
      int valore = anagFacadeClient.storicizzaDelegaTemporanea(delegaVO, ruoloUtenza, anagAziendaVO);
      if(valore == SolmrConstants.SI_RICHIESTE_REVOCA_ATTIVE)
      {
        String messaggioErrore = "Impossibile proseguire: esiste gia' una richiesta di revoca attiva"; 
        request.setAttribute("messaggioErrore", messaggioErrore);        
        %>
          <jsp:forward page= "<%= confermaRevocaDelegaUrl %>" />
        <%        
      }
    }
    catch(SolmrException se) 
    {
      String messaggio = errMsg+": "+AnagErrors.ERR_STOR_REVOCA_MAND_TEMP+".\n"+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
    
  	//String actionUrl = "../layout/ricerca.htm";
  	//Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
	String url ="";
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI))
		url = "../layout/";
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY))
		url = "/layout/";
	url += "ricerca.htm";
	
  	
    %>
      <jsp:forward page="<%= url %>" />
    <%
    return;
    
	}
	
  	
	
	// Vado alla pagina di richiesta conferma eliminazione
	%>
  	<jsp:forward page="<%= confermaRevocaDelegaUrl %>"/>
	

