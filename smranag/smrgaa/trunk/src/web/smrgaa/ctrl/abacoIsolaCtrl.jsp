<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="com.abacogroup.sso.client.Iride2SSO"%>
<%@ page import="java.util.*" %>

<%

  String iridePageName = "abacoIsolaCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  
  String erroreUrl = "/view/erroreView.jsp";

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session
      .getAttribute("anagAziendaVO");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  String settore = request.getParameter("codSettore");
  String pathToFollow = (String) session.getAttribute("pathToFollow");
  String abacoSitiUrl = null;
  if (pathToFollow.equalsIgnoreCase((String) SolmrConstants
      .get("UTENTE_SISPIE")))
  {
    abacoSitiUrl = (String) SolmrConstants.get("ABACO_SITI_URL_SISPIE");
  }
  else
  {
    abacoSitiUrl = (String) SolmrConstants.get("ABACO_SITI_URL_RUPAR");
  }

  com.abacogroup.sso.client.Iride2SSO sso = null;
  String token = null;
  boolean doLogin = false;
  try
  {
    sso = (Iride2SSO) session.getAttribute(Iride2SSO.SSO_SESSION_KEY);
    SolmrLogger.debug(this,
        "Value of object [IRIDE2SSO - ABACO] given by sessione in abacoIsolaCtrl.jsp: "
            + sso + "\n");

    if (sso == null
        || !sso.checkLogin((String) SolmrConstants.ABACO_CONTEXT))
    {
      SolmrLogger.debug(this,
          "Before create new object [IRIDE2SSO - ABACO]\n");
      // Creo l'oggetto SSO per l'invocazione del servizio GIS - ABACO
	  //Modificato 21/04/2010 cz --------------
      //sso = new Iride2SSO((String) SolmrConstants.get("ABACO_SSO_URL"), Integer.parseInt((String)SolmrConstants.get("ABACO_SSO_TIMEOUT")).intValue() );
	  sso = new Iride2SSO((String) SolmrConstants.get("ABACO_SSO_URL"), Integer.parseInt((String)SolmrConstants.get("ABACO_SSO_TIMEOUT")) );
	  //---------------------------------------
      session.setAttribute(Iride2SSO.SSO_SESSION_KEY, sso);
      SolmrLogger.debug(this,
          "After create new object [IRIDE2SSO - ABACO]\n");
      // Occorre rieffettuare il login
      doLogin = true;
    }
    else
    {
      // SSO in sessione e login ancora valido:
      // Non occorre rieffettuare il login
      doLogin = false;
    }
    SolmrLogger.debug(this,
        "Value of parameter [DO_LOGIN] in abacoIsolaCtrl.jsp: " + doLogin
            + "\n");
    if (doLogin)
    {
      // Effettuo la prima login
      sso.initLogin();

      // Recupero user e password dell'utente che si è loggato su anagrafe
      String userId = (String) session.getAttribute("userid");
      SolmrLogger.debug(this,
          "Value of parameter [USER_ID] in abacoIsolaCtrl.jsp: " + userId
              + "\n");
      String password = (String) session.getAttribute("password");
      SolmrLogger.debug(this,
          "Value of parameter [PASSWORD] in abacoIsolaCtrl.jsp: " + password
              + "\n");
      byte[] certificato = (byte[]) session.getAttribute("certificato");
      SolmrLogger.debug(this,
          "Value of parameter [CERTIFICATO] in abacoIsolaCtrl.jsp: "
              + certificato + "\n");

      // Se non mi sono loggato con i certificati
      if (certificato == null || certificato.length == 0)
      {
        // Passo i parametri ed effettuo l'autenticazione per ABACO
        SolmrLogger.debug(this, "Before loginPlain in abacoIsolaCtrl.jsp:\n");
        userId = ((it.csi.iride2.policy.entity.Identita) session
            .getAttribute(SolmrConstants.IDENTITA)).getRappresentazioneInterna()
            + "/"
            + ((it.csi.iride2.policy.entity.Identita) session
                .getAttribute(SolmrConstants.IDENTITA)).getMac();
        password = "-";
        sso.loginCSI(userId, password, SolmrConstants.ABACO_CONTEXT);
        SolmrLogger.debug(this, "After loginPlain in abacoIsolaCtrl.jsp:\n");
      }
      // Altrimenti recupero il certificato e provo a riloggarmi
      else
      {
        SolmrLogger.debug(this,
            "Before loginWithCertificate in abacoIsolaCtrl.jsp:\n");
        sso.loginWithCertificate(certificato, SolmrConstants.ABACO_CONTEXT);
        SolmrLogger.debug(this,
            "After loginWithCertificate in abacoIsolaCtrl.jsp:\n");
      }
    }

    // Recupero il token dall'SSO
    token = sso.getNewToken();
    SolmrLogger.debug(this,
        "Value of parameter [TOKEN] in abacoIsolaCtrl.jsp:" + token + "\n");
    // Se il token che mi deve restituire l'SSO è null impedisco l'accesso
    // al GIS
    if (!Validator.isNotEmpty(token))
    {
      String messaggio = AnagErrors.ERRORE_KO_GIS_INVALID_PARAMETER;
      request.setAttribute("messaggioErrore", messaggio);
      session.setAttribute("chiudi", "chiudi");
%>
<jsp:forward page="<%=erroreUrl%>" />
<%
  return;
    }
  }
  catch (Exception e)
  {
    //String messaggio = AnagErrors.ERRORE_KO_GIS_CONNECTION + ": "+ e.getMessage();
    String messaggio = AnagErrors.ERRORE_KO_GIS_CONNECTION;
    e.printStackTrace();
    request.setAttribute("messaggioErrore", messaggio);
    session.setAttribute("chiudi", "chiudi");
%>
<jsp:forward page="<%=erroreUrl%>" />
<%
  return;
  }

  // Effettuo la chiamata vera e propria a SITI in relazione all'operazione richiesta:
  
  if(!Validator.isNotEmpty(settore))
  {
    //Default
    settore = "100";
  }
  
  String annoDich = anagFacadeClient
    .getLastDichConsNoCorrettiva(anagAziendaVO.getIdAzienda().longValue());
  if(!Validator.isNotEmpty(annoDich))
  {
    GregorianCalendar gcActual = new GregorianCalendar();
    Integer currYear = new Integer(gcActual.get(Calendar.YEAR));
    annoDich = currYear.toString();
  }
    
    
  response.sendRedirect(abacoSitiUrl
      + "/siticata/siti.actions?" + Iride2SSO.SSO_TOKEN
      + "=" + token 
      + "&action=LO.isole"
      + "&subAction=listIlo"
      + "&cuaa=" + anagAziendaVO.getCUAA().toUpperCase()
      + "&settore="+settore
      + "&campagna="+annoDich);
  
%>

