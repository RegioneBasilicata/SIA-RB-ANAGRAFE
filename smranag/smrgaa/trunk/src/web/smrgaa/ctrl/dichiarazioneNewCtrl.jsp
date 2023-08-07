<%@ page language="java" contentType="text/html" isErrorPage="true"%>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.profile.UtenteProcedimento" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%!
  public static String PREVISIONE_PIANO_URL="/view/dichiarazioneNuovaView.jsp";
  public static String CONTROLLA_MODIFICHE_URL="../layout/dichiarazioneControllaModifiche.htm";
  public static String DICHIARAZIONE_URL="../layout/dichiarazioneConsistenza.htm";
  public static String NO_DICHIARAZIONE_URL="/view/dichiarazioneNoModificheView.jsp";
  public static String ACTION = "../layout/dichiarazioneNuova.htm";
  public static String URL_ATTENDERE_PREGO = "/view/attenderePregoView.jsp";
  public static String NEXT_PAGE_ERRORE_PLSQL="/view/erroreView.jsp";
  public static String URL_DICHIARAZIONE_CONSISTENZA = "../ctrl/dichiarazioneConsistenzaCtrl.jsp";
%>

<%

	String iridePageName = "dichiarazioneNewCtrl.jsp";
	%>
		<%@include file = "/include/autorizzazione.inc" %>
	<%

	SolmrLogger.debug(this, " - \n\ndichiarazioneNewCtrl.jsp - INIZIO PAGINA\n\n\n");

  //Gli Att sono stati introdotti per retrocompatibilità, dato che ho aggiunto
  //nella dichiarazioneAnomaliScegliMotivo gli stessi parametri ma che in quel caso devono avere priorità!! 
	String operazione = request.getParameter("operazione");
  String operazioneAtt = (String)request.getAttribute("operazioneAtt");
      
  String idMotivoDichiarazioneStrAtt = (String)request.getAttribute("idMotivoDichiarazioneAtt");
  String idMotivoDichiarazioneStr = request.getParameter("idMotivoDichiarazione");
    
  if(Validator.isNotEmpty(operazioneAtt))
  {
    operazione = operazioneAtt;
  }
  
  if(Validator.isNotEmpty(idMotivoDichiarazioneStrAtt))
  {
    idMotivoDichiarazioneStr = idMotivoDichiarazioneStrAtt;
  }

	AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");

	if(Validator.isNotEmpty("operazione") && "attenderePrego".equalsIgnoreCase(operazione)) 
  {
  	request.setAttribute("action", ACTION);
  	operazione = null;
  	request.setAttribute("operazione", operazione);
  	Long idMotivoDichiarazione = Long.decode(idMotivoDichiarazioneStr);
  	session.setAttribute("idMotivoDichiarazione", idMotivoDichiarazione);
  	%>
    	<jsp:forward page= "<%= URL_ATTENDERE_PREGO %>" />
  	<%
	}
  else 
  {
    if(!Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
    {
      //Non posso proseguire con la dichiarazione
      String messaggio = (String)AnagErrors.get("ERR_CUAA_NO_VALORIZZATO");
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
      //E' stata fatta una previsione del piano colturale
      %>
        <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
      <%
      return;
    }
    try
    {
      AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
      GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
      RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
      UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");

      /*if (ruoloUtenza.isUtenteIntermediario())
      {
        
         // Se l'utente connesso è un INTERMEDIARIO il sistema verifica che l'utente sia
         // abilitato a tale funzione. In caso in cui l'utente non sia abilitato il
         // sistema visualizza il seguente messaggio Utente non abilitato a procedere,
        // altrimenti il caso d'uso procede con il passo 2.Se l'utente connesso è un
         // funzionario PA il sistema non fa alcun controllo.
        
        String ruolo="ASP2",dirittoAccesso="R";
        if (ruoloUtenza.isReadWrite())
        {
          ruolo="ASP1";
          dirittoAccesso="W";
        }
        Vector<UtenteProcedimento> utente=null;
        try
        {
          utente=anagFacadeClient.serviceGetUtenteProcedimento(utenteAbilitazioni.getCodiceFiscale(),
                                                         Long.decode((String)SolmrConstants.get("ID_TIPO_PROCEDIMENTO_ANAG")),
                                                         ruolo,
                                                         utenteAbilitazioni.getEnteAppartenenza().getIntermediario().getCodiceEnte(),
                                                         dirittoAccesso,
                                                         new Long(0));
        }
        catch(Exception e)
        {
          String messaggio = e.getMessage();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", DICHIARAZIONE_URL);
          %>
            <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
          <%
          return;
        }

        boolean utenteAbilitato=false;
        if (utente==null || utente.size()==0)
          utenteAbilitato=true;
        else
        {
          UtenteProcedimento utenteProcedimento=(UtenteProcedimento)utente.get(0);
          if (utenteProcedimento==null) utenteAbilitato=true;
          else
          {
            if ("S".equals(utenteProcedimento.getAbilitazioneTrasmissione()))
              utenteAbilitato=true;
          }
        }

        if (!utenteAbilitato)
        {
          request.setAttribute("messaggioErrore",(String)AnagErrors.get("ERR_SMRCOMM_NON_ABILITATO"));
          request.setAttribute("pageBack", DICHIARAZIONE_URL);
          %>
            <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
          <%
          return;
        }
      }*/
      
      

      // Recupero i parametri AAEP e TRIB per verificare se
      // devo o meno effettuare i controlli di validità con
      // i richiami ai servizi di AAEP e SIAN
      String parametroAAEP = null;
      String parametroSIAN = null;
      String parametroCCAV = null;
      String parametroCCSI = null;
      try 
      {
        parametroAAEP = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_AAEP"));
        parametroSIAN = anagFacadeClient.getValoreFromParametroByIdCode((String)SolmrConstants.get("ID_PARAMETRO_TRIB"));
        parametroCCAV = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_CCAV);
        parametroCCSI = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_CCSI);
      }
      catch(SolmrException se) {
        //Non posso proseguire con la dichiarazione
        String messaggio = se.getMessage();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
        //E' stata fatta una previsione del piano colturale
        %>
          <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
        <%
        return;
      }

      // Se il parametro del SIAN è valorizzato a "S" significa
      // che devo effettuare i controlli e gli aggiornamenti
      // relativi al SIAN
      if(parametroSIAN.equalsIgnoreCase(SolmrConstants.FLAG_S))
      {
        // Aggiorno la tabella DB_AZIENDA_TRIBUTARIA in relazione
        // ai valori restituitimi dal SIAN
        try
        {
          String elencoCuaa[]=new String[1];
          elencoCuaa[0]=anagAziendaVO.getCUAA();
          anagFacadeClient.sianAggiornaDatiTributaria(elencoCuaa, ProfileUtils.getSianUtente(ruoloUtenza));
        }
        catch(SolmrException se) {
          //Non posso proseguire con la dichiarazione
          String messaggio = se.getMessage();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
          //E' stata fatta una previsione del piano colturale
          %>
            <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
          <%
          return;
        }

        /** Nel caso di persone giuridiche (cuaa dell'azienda lungo 11 chr - partita iva)
         *  richiamare il servizio SIAN anagraficaSintetica passando come codice il codice fiscale
         * del titolare/rappr.legale attivo legato all'azienda. Tale servizio è da richiamare
         * solo nel caso in cui su DB_PARAMETRO il parametro TRIB='S'.
         */
        if (anagAziendaVO.getCUAA()!=null && anagAziendaVO.getCUAA().length()==11)
        {
          PersonaFisicaVO personaVO = anagFacadeClient.getTitolareORappresentanteLegaleAzienda(anagAziendaVO.getIdAzienda(), DateUtils.parseDate(anagAziendaVO.getDataSituazioneAlStr()));
          // Se non viene reperito il rappresentante legale dell'azienda oppure non ha
          // il codice fiscale valorizzato impedisco la prosecuzione della nuova validazione
          if(personaVO == null || !Validator.isNotEmpty(personaVO.getCodiceFiscale())) {
            String messaggio = (String)AnagErrors.ERRORE_KO_VALIDAZIONE_FOR_COD_FISC_RAPP_LEG;
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
            //E' stata fatta una previsione del piano colturale
            %>
              <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
            <%
            return;
          }
          // Aggiorno la tabella DB_AZIENDA_TRIBUTARIA in relazione
          // ai valori restituitimi dal SIAN
          try
          {
            String elencoCuaa[]=new String[1];
            elencoCuaa[0]=personaVO.getCodiceFiscale();
            anagFacadeClient.sianAggiornaDatiTributaria(elencoCuaa, ProfileUtils.getSianUtente(ruoloUtenza));
          }
          catch(SolmrException se) {
            //Non posso proseguire con la dichiarazione
            String messaggio = se.getMessage();
            request.setAttribute("messaggioErrore",messaggio);
            request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
            //E' stata fatta una previsione del piano colturale
            %>
              <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
            <%
            return;
          }

        }
      }

      // Se sono in piemonte e il parametro di AAEP è valorizzato a "S" significa
      // che devo effettuare i controlli e gli aggiornamenti
      // relativi ad AAEP
      boolean piemonte=SolmrConstants.ID_REG_PIEMONTE.equals(ruoloUtenza.getIstatRegioneAttiva());
      if(piemonte && parametroAAEP.equalsIgnoreCase(SolmrConstants.FLAG_S)) {
        /**
        * Poi richiama il servizio AAEP
        * */
        try 
        {
          anagFacadeClient.aggiornaDatiAAEP(anagAziendaVO.getCUAA());
        }
        catch(Exception e){}
      }
      
      // Recupero il parametro ALLD
      String parametroALLD = null;
      try {
    	  parametroALLD = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_ALLD);
      }
      catch(SolmrException se) 
      {
    	  String messaggio = AnagErrors.ERRORE_KO_PARAMETRO_ALLD;
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
          //E' stata fatta una previsione del piano colturale
          %>
            <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
          <%
          return;
      }
      // Se parametro ALLD = S
      if(parametroALLD.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
    	  // Richiamo il web-service per l'aggiornamento della BDN allevamenti
    	  try 
        {
    		  gaaFacadeClient.serviceWsBridgeAggiornaDatiBDN(anagAziendaVO.getCUAA());
    	  }
    	  catch(SolmrException se) 
    	  {
    		  // Intercetto l'eccezione ma non blocco l'operazione
    		  SolmrLogger.error(this, "--- Eccezione con la chiamata Sian serviceWsBridgeAggiornaDatiBDN :"+se.getMessage());    		 
    	  }
      }
      
      // Richiamare il servizio erogato dalla CCIAA per l'elenco albo vigneti:
      // Eseguire il c.u. "CU-GAA14-13 Scarico aziendale albo vigneti fonte CCIAA" solamente se il parametro CCAV='S'.
      // Indipendentemente dall'esito di quanto sopra procedere comunque.
      if(parametroCCAV.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        // Richiamo il web-service per l'aggiornamento dell'albo vigneti
        try 
        {
         anagFacadeClient.sianAggiornaDatiAlboVigneti(anagAziendaVO);
        }
        catch(SolmrException se) {
        // Intercetto l'eccezione ma non blocco l'operazione
        }
      }
      
      
      
      // Richiamare il servizio erogato di sigmater per lo scarico delle titolarità
      // Eseguire il c.u. "CU-GAA144-14 Scarico titolarità SIGMATER v1" 
      // solamente se il parametro CCSI='S'.
      // Indipendentemente dall'esito di quanto sopra procedere comunque.
      if(parametroCCSI.equalsIgnoreCase(SolmrConstants.FLAG_S)) 
      {
        // Richiamo il web-service per l'aggiornamento di sigmater
        try 
        {
          gaaFacadeClient.scaricoTitolarita(SolmrConstants.ID_REG_PIEMONTE,
            anagAziendaVO.getCUAA(), anagAziendaVO.getIdAzienda().longValue(),
            null, null);
        }
        catch(SolmrException se) 
        {
          // Intercetto l'eccezione ma non blocco l'operazione
          String messaggio = se.getMessage();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
          //E' stata fatta una previsione del piano colturale
          %>
            <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
          <%
          return;
        }
      }
      
      

      if (anagFacadeClient.previsioneAnnoSucessivo(anagAziendaVO.getIdAzienda())) {
        //E' stata fatta una previsione del piano colturale
        %>
          <jsp:forward page="<%=PREVISIONE_PIANO_URL%>" />
        <%
      }
      else {
        //Non è stata fatta una previsione del piano colturale
        response.sendRedirect(CONTROLLA_MODIFICHE_URL);
      }
      return;
    }
    catch(SolmrException s) {
        String messaggio = s.getMessage();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
      %>
        <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
      <%
    }
    catch(Exception e) {
      request.setAttribute("messaggioErrore",(String)SolmrErrors.get("GENERIC_SYSTEM_EXCEPTION"));
      request.setAttribute("pageBack", URL_DICHIARAZIONE_CONSISTENZA);
      %>
        <jsp:forward page="<%=NEXT_PAGE_ERRORE_PLSQL%>" />
      <%
    }
    request.getRequestDispatcher(DICHIARAZIONE_URL).forward(request, response);
  }

  SolmrLogger.debug(this, " - \n\n\ndichiarazioneNewCtrl.jsp - FINE PAGINA\n\n\n");
%>
