<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="it.csi.solmr.client.anag.*"%>
<%@ page import="it.csi.solmr.etc.*"%>
<%@ page import="it.csi.solmr.etc.anag.*"%>
<%@ page import="it.csi.solmr.exception.*"%>
<%@ page import="it.csi.solmr.dto.anag.*"%>
<%@ page import="it.csi.solmr.dto.anag.terreni.*"%>
<%@ page import="it.csi.solmr.util.*"%>
<%@ page import="com.abacogroup.sso.client.Iride2SSO"%>

<%
  String erroreUrl = "/view/erroreView.jsp";

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO) session
      .getAttribute("anagAziendaVO");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();

  String abaco = request.getParameter("abaco");
  //String ricercaTerreni = request.getParameter("ricercaTerreni");
  //Arrivo da inserisci della ricerca terreni
  String idStoricoParticellaRicTerr = request.getParameter("idStoricoParticellaRicTerr");
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
        "Value of object [IRIDE2SSO - ABACO] given by sessione in abacoGisCtrl.jsp: "
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
        "Value of parameter [DO_LOGIN] in abacoGisCtrl.jsp: " + doLogin
            + "\n");
    if (doLogin)
    {
      // Effettuo la prima login
      sso.initLogin();

      // Recupero user e password dell'utente che si è loggato su anagrafe
      String userId = (String) session.getAttribute("userid");
      SolmrLogger.debug(this,
          "Value of parameter [USER_ID] in abacoGisCtrl.jsp: " + userId
              + "\n");
      String password = (String) session.getAttribute("password");
      SolmrLogger.debug(this,
          "Value of parameter [PASSWORD] in abacoGisCtrl.jsp: " + password
              + "\n");
      byte[] certificato = (byte[]) session.getAttribute("certificato");
      SolmrLogger.debug(this,
          "Value of parameter [CERTIFICATO] in abacoGisCtrl.jsp: "
              + certificato + "\n");

      // Se non mi sono loggato con i certificati
      if (certificato == null || certificato.length == 0)
      {
        // Passo i parametri ed effettuo l'autenticazione per ABACO
        SolmrLogger.debug(this, "Before loginPlain in abacoGisCtrl.jsp:\n");
        userId = ((it.csi.iride2.policy.entity.Identita) session
            .getAttribute(SolmrConstants.IDENTITA)).getRappresentazioneInterna()
            + "/"
            + ((it.csi.iride2.policy.entity.Identita) session
                .getAttribute(SolmrConstants.IDENTITA)).getMac();
        password = "-";
        sso.loginCSI(userId, password, SolmrConstants.ABACO_CONTEXT);
        SolmrLogger.debug(this, "After loginPlain in abacoGisCtrl.jsp:\n");
      }
      // Altrimenti recupero il certificato e provo a riloggarmi
      else
      {
        SolmrLogger.debug(this,
            "Before loginWithCertificate in abacoGisCtrl.jsp:\n");
        sso.loginWithCertificate(certificato, SolmrConstants.ABACO_CONTEXT);
        SolmrLogger.debug(this,
            "After loginWithCertificate in abacoGisCtrl.jsp:\n");
      }
    }

    // Recupero il token dall'SSO
    token = sso.getNewToken();
    SolmrLogger.debug(this,
        "Value of parameter [TOKEN] in abacoGisCtrl.jsp:" + token + "\n");
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
  // Se richiamo l'elenco delle particelle da un'azienda
  if (!Validator.isNotEmpty(abaco))
  {
    response.sendRedirect(abacoSitiUrl
        + "/siticata/sitiviewer/csiFwdCuaa.jsp?" + Iride2SSO.SSO_TOKEN
        + "=" + token + "&cuaa=" + anagAziendaVO.getCUAA().toUpperCase());
  }
  else
  {
    Long idConduzioneParticella = null;
    Long idConduzioneDichiarata = null;
    Long idStoricoParticella = null;
    //Ricerca Terreni
    if (Validator.isNotEmpty(idStoricoParticellaRicTerr))
    {
      idStoricoParticella = Long.decode(request
          .getParameter("idStoricoParticellaRicTerr"));
    }
    else
    {
      FiltriParticellareRicercaVO filtriParticellareRicercaVO = (FiltriParticellareRicercaVO) session
          .getAttribute("filtriParticellareRicercaVO");
      FiltriUnitaArboreaRicercaVO filtriUnitaArboreaRicercaVO = (FiltriUnitaArboreaRicercaVO) session
          .getAttribute("filtriUnitaArboreaRicercaVO");
      if (filtriParticellareRicercaVO == null
          && filtriUnitaArboreaRicercaVO == null)
      {
        String messaggio = (String) AnagErrors.ERRORE_KO_GIS_FIND_TERRENI_PARAMETER;
        request.setAttribute("messaggioErrore", messaggio);
        session.setAttribute("chiudi", "chiudi");
        %>
          <jsp:forward page="<%=erroreUrl%>" />
        <%
        return;
      }
      else
      {
        // Menù terreni
        if (filtriParticellareRicercaVO != null)
        {
          String storicoParticella = request
              .getParameter("idStoricoParticella");
          // Se id_storico_particella non è valorizzato vuol dire che arrivo da dettaglio terreni e quindi necessito dell'id_conduzione_particella
          if (!Validator.isNotEmpty(storicoParticella))
          {
            if (filtriParticellareRicercaVO.getIdPianoRiferimento()
                .intValue() <= 0)
            {
              idConduzioneParticella = Long.decode(request
                  .getParameter("idConduzione"));
            }
            else
            {
              idConduzioneDichiarata = Long.decode(request
                  .getParameter("idConduzione"));
            }
          }
          // Altrimenti vuol dire che arrivo da inserisci particella e quindi mi serve reperire l'id_storico_particella
          else
          {
            idStoricoParticella = Long.decode(storicoParticella);
          }
        }
        // Menù unità vitate
        else
        {
          idStoricoParticella = Long.decode(request
              .getParameter("idStoricoParticella"));
        }
      }
    }

    // Vado a reperire i dati in fuzione di id_conduzione_particella...
    StoricoParticellaVO storicoParticellaVO = null;
    if (idConduzioneParticella != null)
    {
      try
      {
        storicoParticellaVO = anagFacadeClient
            .findStoricoParticellaVOByIdConduzioneParticella(idConduzioneParticella);
      }
      catch (SolmrException se)
      {
        String messaggio = AnagErrors.ERRORE_KO_GIS_FIND_TERRENI_PARAMETER;
        request.setAttribute("messaggioErrore", messaggio);
        session.setAttribute("chiudi", "chiudi");
        %>
          <jsp:forward page="<%=erroreUrl%>" />
        <%
        return;
      }
    }
    // ... o di id_conduzione_dichiarata
    else if (idConduzioneDichiarata != null)
    {
      try
      {
        storicoParticellaVO = anagFacadeClient
            .findStoricoParticellaVOByIdConduzioneDichiarata(idConduzioneDichiarata);
      }
      catch (SolmrException se)
      {
        String messaggio = AnagErrors.ERRORE_KO_GIS_FIND_TERRENI_PARAMETER;
        request.setAttribute("messaggioErrore", messaggio);
        session.setAttribute("chiudi", "chiudi");
        %>
          <jsp:forward page="<%=erroreUrl%>" />
        <%
        return;
      }
    }
    // o id_storico_particella
    else if(idStoricoParticella != null)
    {
      try
      {
        storicoParticellaVO = anagFacadeClient
            .findStoricoParticellaByPrimaryKey(idStoricoParticella);
      }
      catch (SolmrException se)
      {
        String messaggio = AnagErrors.ERRORE_KO_GIS_FIND_TERRENI_PARAMETER;
        request.setAttribute("messaggioErrore", messaggio);
        session.setAttribute("chiudi", "chiudi");
        %>
          <jsp:forward page="<%=erroreUrl%>" />
        <%
        return;
      }
    }

    // Se si tratta di particella provvisoria impedisco l'accesso al GIS
    if (!Validator.isNotEmpty(storicoParticellaVO.getParticella()))
    {
      String messaggio = AnagErrors.ERRORE_KO_GIS_PART_PROVVISORIA;
      request.setAttribute("messaggioErrore", messaggio);
      session.setAttribute("chiudi", "chiudi");
      %>
        <jsp:forward page="<%=erroreUrl%>" />
      <%
      return;
    }

    String url = "&codprov="
        + storicoParticellaVO.getIstatComune().substring(0, 3) + "&codcom="
        + storicoParticellaVO.getIstatComune().substring(3);
    if (Validator.isNotEmpty(storicoParticellaVO.getSezione()))
    {
      url += "&sezione=" + storicoParticellaVO.getSezione();
    }
    url += "&foglio=" + storicoParticellaVO.getFoglio();
    if (Validator.isNotEmpty(storicoParticellaVO.getParticella()))
    {
      url += "&particella=" + storicoParticellaVO.getParticella();
    }
    if (Validator.isNotEmpty(storicoParticellaVO.getSubalterno()))
    {
      url += "&subalterno=" + storicoParticellaVO.getSubalterno();
    }
    response.sendRedirect(abacoSitiUrl
        + "/siticata/sitiviewer/csiFwdPart.jsp?" + Iride2SSO.SSO_TOKEN
        + "=" + token + url);
  }
%>

