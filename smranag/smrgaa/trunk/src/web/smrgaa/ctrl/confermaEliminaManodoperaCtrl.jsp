<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>

<%!
  public static String VALIDATE_URL = "/ctrl/manodoperaCtrl.jsp";
  public static String VIEW = "/view/confermaEliminaManodoperaView.jsp";
  public static String VALIDATE_DET_URL = "/ctrl/manodoperaDetCtrl.jsp";
%>

<%


    //Controllo se sono su un ambiente JBoss 6.4 o un ambiente JBoss WildFly
	ResourceBundle res = ResourceBundle.getBundle("config");
	String ambienteDeploy = res.getString("ambienteDeploy");
	SolmrLogger.debug(this, "-- ambienteDeploy ="+ambienteDeploy);
		
	  
	String ELENCO_URL ="";
	String ELENCO_URL_SEND_REDIRECT ="";
	String DETTAGLIO_URL ="";
	  
	if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_CSI)){
		ELENCO_URL = "../layout/";
		ELENCO_URL_SEND_REDIRECT = "../layout/";
		DETTAGLIO_URL = "../layout/";
	}	
	else if(ambienteDeploy.equals(SolmrConstants.AMBIENTE_JBOSS_WILDFLY)){
		ELENCO_URL = "/layout/";
		ELENCO_URL_SEND_REDIRECT = "/layout/";
		DETTAGLIO_URL = "/layout/";
	}	
	ELENCO_URL += "manodopera.htm";
	ELENCO_URL_SEND_REDIRECT += "manodopera.htm";
	DETTAGLIO_URL +="manodoperaDet.htm";

  String iridePageName = "confermaEliminaManodoperaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  SolmrLogger.debug(this, " - confermaEliminaManodoperaCtrl.jsp - INIZIO PAGINA");

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  try
  {

    SolmrLogger.debug(this, "request.getParameter(conferma.x): " + request.getParameter("conferma.x"));
    SolmrLogger.debug(this, "request.getParameter(annulla.x): " + request.getParameter("annulla.x"));
    SolmrLogger.debug(this, "request.getParameter(idManodopera): " + request.getParameter("idManodopera"));

    if (request.getParameter("conferma.x") != null)
    {
      anagFacadeClient.deleteManodopera(new Long(request.getParameter("idManodopera")));
      %>
        <jsp:forward page="<%= ELENCO_URL_SEND_REDIRECT %>"/>
      <%
      return;
    }
    else if(request.getParameter("operazione") != null 
      && request.getParameter("operazione").equalsIgnoreCase("indietro")) 
    {
      if (request.getParameter("pageFrom") != null && request.getParameter("pageFrom").equalsIgnoreCase("dettaglio") )
      {   
        %>
          <jsp:forward page="<%= DETTAGLIO_URL %>"/>
        <%
        return;
      }
      SolmrLogger.debug(this, "2request.getParameter(\"pageFrom\"): "+request.getParameter("pageFrom"));
      %>
        <jsp:forward page="<%= ELENCO_URL %>"/>
      <%
    }
  }
  catch(Exception e)
  {
    if (request.getParameter("pageFrom") != null && request.getParameter("pageFrom").equalsIgnoreCase("dettaglio"))
    {
      //rimanda al dettaglio manodopera
      throwValidation(e.getMessage(), VALIDATE_DET_URL);
    }
    else
    {
      //rimanda all'elenco manodopera
      throwValidation(e.getMessage(), VALIDATE_URL);
    }
  }


  ManodoperaVO manodoperaVO = null;
  ValidationErrors errors = new ValidationErrors();
  // Recupero l'oggetto manodopera
  try {
    manodoperaVO = anagFacadeClient.dettaglioManodopera(new Long(request.getParameter("idManodopera")));
  }
  catch(SolmrException se) {
    ValidationError error = new ValidationError(se.getMessage());
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(ELENCO_URL).forward(request, response);
    return;
  }

  // Controllo che la manodopera selezionata non sia già cessata
  if(manodoperaVO.getDataFineValiditaDate() != null) {
    // Metto in request il valore selezionato dall'utente
    request.setAttribute("idElemento", new Long(request.getParameter("idManodopera")));
    ValidationError error = new ValidationError((String)AnagErrors.get("ERR_RECORD_STORICIZZATO"));
    errors.add("error", error);
    request.setAttribute("errors", errors);
    %>
      <jsp:forward page="<%= ELENCO_URL %>"/>
    <%

  }

  %><jsp:forward page="<%= VIEW %>"/><%

  SolmrLogger.debug(this, " - confermaEliminaManodoperaCtrl.jsp - FINE PAGINA");
%>

<%!
  private void throwValidation(String msg, String validateUrl) throws ValidationException
  {
    ValidationException valEx = new ValidationException(msg, validateUrl);
    valEx.addMessage(msg, "exception");
    throw valEx;
  }
%>
