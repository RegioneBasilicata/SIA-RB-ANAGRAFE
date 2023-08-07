<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.smranag.smrgaa.dto.search.FiltriRicercaTerrenoImportaAsservimentoVO" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>

<%

  String iridePageName = "terreniImportaAsservimentoCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  final String errMsg = "Impossibile procedere nella sezione Terreni->importa asservimento. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String terreniImportaAsservimentoUrl = "/view/terreniImportaAsservimentoView.jsp";
  String particellareTerreniImportaAsservimentoUrl = "/ctrl/particellareTerreniImportaAsservimentoCtrl.jsp";
  String erroreViewUrl = "/view/erroreView.jsp";
  String actionUrl = "../layout/terreniParticellareElenco.htm";
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  ValidationErrors errors = null;
  
  String idTitoloPossesso = request.getParameter("idTitoloPossesso");
  
  String operazione = request.getParameter("operazione");
  
  // Recupero il CUAA
  String cuaa = request.getParameter("cuaa");
  if(Validator.isNotEmpty(cuaa))
  {
    cuaa = cuaa.toUpperCase().trim();
  }
  session.setAttribute("cuaa", cuaa);
  
  
  
  // L'utente ha selezionato la funzionalità "importa asservimento" dal menù
  // Inizializzazione
  if(!Validator.isNotEmpty(operazione))
  {
    WebUtils.removeUselessFilter(session,null);

    // Se l'azienda selezionata è priva di CUAA impedisco l'accesso alla funzionalità
    if(!Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
    {     
      request.setAttribute("messaggioErrore",(String)AnagErrors.get("ERR_IMPOSSIBILE_IMPORT_FOR_CUAA"));
      request.setAttribute("history","true");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;    
    }

    // Controllo che l'azienda selezionata abbia un ute
    Vector elencoUte = null;
    try 
    {
      elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
    }
    catch(SolmrException se) 
    {       
      request.setAttribute("messaggioErrore",(String)AnagErrors.get("ERR_IMPOSSIBILE_IMPORT")+" "+se.getMessage());
      request.setAttribute("history","true");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;    
    }
  }
  
   // TITOLO DI POSSESSO
  it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = null;
  try 
  {
    elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
    request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
  }
  catch(SolmrException se) 
  {       
    request.setAttribute("messaggioErrore",(String)AnagErrors.ERRORE_KO_TITOLO_POSSESSO+" "+se.getMessage());
    request.setAttribute("history","true");
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;    
  }
  
  

  // Ricerca Azienda
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA))
  {
    
    // Recupero il VO
    FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoImportaAsservimentoVO = 
      (FiltriRicercaTerrenoImportaAsservimentoVO)session.getAttribute("filtriRicercaTerrenoImportaAsservimentoVO");
      
    if(!Validator.isNotEmpty(filtriRicercaTerrenoImportaAsservimentoVO))
    {
      filtriRicercaTerrenoImportaAsservimentoVO = new FiltriRicercaTerrenoImportaAsservimentoVO(); 
    }

    // Setto i valori all'interno del VO
    filtriRicercaTerrenoImportaAsservimentoVO.setCUAA(cuaa);
    filtriRicercaTerrenoImportaAsservimentoVO.setTipoRicerca(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CUAA);

    session.setAttribute("filtriRicercaTerrenoImportaAsservimentoVO", filtriRicercaTerrenoImportaAsservimentoVO);
    
     // Effettuo la validazione formale dei dati
    errors = filtriRicercaTerrenoImportaAsservimentoVO.validateRicercaCUAA(anagAziendaVO.getCUAA());
    
    if(errors !=null)
    {
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page= "<%= terreniImportaAsservimentoUrl %>" />
      <%
      return;
    }

    // Se ho passato i controlli di validità del CUAA ricerco un'azienda attiva
    // cioè con DATA_FINE_VALIDITA = NULL
    Vector elencoAziende = null;
    try 
    {
      elencoAziende = anagFacadeClient.getAziendaByCriterioCessataAndProvvisoria(cuaa);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }

    // Se la ricerca non ha prodotto nessun risultato avviso l'utente
    if(elencoAziende == null) 
    {
      String messaggio = (String)AnagErrors.get("ERR_CUAA_NON_TROVATO_FOR_IMPORTA_PARTICELLE");
      request.setAttribute("messaggioErrore", messaggio);
      %>
        <jsp:forward page= "<%= terreniImportaAsservimentoUrl %>" />
      <%
      return;
    }
    // Se la ricerca ha prodotto più risultati mando nuovamente alla pagina di ricerca
    // azienda per importazione in modo che l'utente possa selezionare l'azienda desiderata
    else if(elencoAziende.size() > 1) 
    {
      // Metto il vettore in request
      request.setAttribute("elencoAziende", elencoAziende);
      // Vado alla pagina iniziale di importazione
      %>
        <jsp:forward page= "<%= terreniImportaAsservimentoUrl %>" />
      <%
      return;
    }
    // Se la ricerca invece ha prodotto un unico risultato allora proseguo con le
    // operazioni per l'importazione delle particelle
    else 
    {
      // Recupero l'azienda frutto della ricerca
      AnagAziendaVO anagAziendaSearchVO = (AnagAziendaVO)elencoAziende.elementAt(0);
      filtriRicercaTerrenoImportaAsservimentoVO.setIdAziendaSearch(anagAziendaSearchVO.getIdAzienda().longValue());
      
      session.setAttribute("filtriRicercaTerrenoImportaAsservimentoVO", filtriRicercaTerrenoImportaAsservimentoVO);

      // Metto l'azienda selezionata dall'utente in sessione
      session.setAttribute("anagAziendaSearchVoImportaAsservimento", anagAziendaSearchVO);


      //Se l'utente è un intermediario...
      //devo verificare: 
      //- se le aziende hanno mandato attivo allo stesso CAA procedo
      //- se una o entrambe non hanno mandato attivo ad un CAA procedo
      if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) 
      {
        String codFiscaleIntermediarioDelegaRicevente = null;
        String codFiscaleIntermediarioDelegaDonante = null;
        if((anagAziendaVO.getDelegaVO() != null) &&  (anagAziendaSearchVO.getDelegaVO() !=null))
        {
          codFiscaleIntermediarioDelegaRicevente = anagAziendaVO.getDelegaVO()
            .getCodiceFiscaleIntermediario().substring(0, 3);
          codFiscaleIntermediarioDelegaDonante = anagAziendaSearchVO.getDelegaVO()
            .getCodiceFiscaleIntermediario().substring(0, 3);
            
          //Se sono in conferimento non permetto la ricerca per chiave catastale
          if("6".equals(idTitoloPossesso))
          {
            boolean isFiglia = false;
            try 
            {
              isFiglia = gaaFacadeClient.isAziendeCollegataFiglia(
                anagAziendaVO.getIdAzienda().longValue(),
                anagAziendaSearchVO.getIdAzienda().longValue());
              if(!isFiglia)
              {
                request.setAttribute("messaggioErrore", AnagErrors.NO_CONFERIMENTO_FIGLIA);
                request.setAttribute("history","true");
                %>
                  <jsp:forward page="<%= erroreViewUrl %>" />
                <%
                return;  
              }
            }
            catch(SolmrException se) 
            {
              SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
              String messaggio = errMsg+""+se.toString();
              request.setAttribute("messaggioErrore",messaggio);
              request.setAttribute("pageBack", actionUrl);
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;
            }
          }
          else //Asservimento
          {
            //Entrambe le aziende hanno il mandato attivo ma a due diversi CAA
            if(!codFiscaleIntermediarioDelegaRicevente.equalsIgnoreCase(codFiscaleIntermediarioDelegaDonante))
            {
              request.setAttribute("messaggioErrore",AnagErrors.DIFF_MANDATO_ATTIVO);
              request.setAttribute("history","true");
              %>
                <jsp:forward page="<%= erroreViewUrl %>" />
              <%
              return;    
            }
          }
        }
      }
      //Se l'utente è un PA
      // non occorre controllare i mandati
      else if(ruoloUtenza.isUtentePA())
      {}
      //In tutti gli altri casi
      else
      {
        request.setAttribute("messaggioErrore",AnagErrors.DIFF_MANDATO_ATTIVO);
        request.setAttribute("history","true");
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;  
      }
      
      // Ricerco le particelle importabili in relazione all'azienda selezionata
      // dall'utente

      long elencoId[] = null;
      try 
      {
        elencoId = anagFacadeClient.ricercaIdConduzioneTerreniImportaAsservimento(filtriRicercaTerrenoImportaAsservimentoVO);
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
      }

      // Metto il vettore in request ??????
      //session.setAttribute("elencoParticelleImportaAsservimento", elencoParticelle);
      session.setAttribute("elencoIdParticelleImportaAsservimento", elencoId);

      // Recupero l'elenco delle UTE a cui associare le particelle
      Vector elencoUte = null;
      try 
      {
        elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
      }
     
      // Metto il vettore in request
      request.setAttribute("elencoUte", elencoUte);

      // Vado alla pagina di importazione elenco particelle
      %>
        <jsp:forward page= "<%= particellareTerreniImportaAsservimentoUrl %>" />
      <% 
    }
  }// Ricerca Particelle
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CHIAVE_CATASTALE))
  {
    //???????????????????????????
    //session.setAttribute("ricercaImportaAsservimento",SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CHIAVE_CATASTALE);
    String istatComune = request.getParameter("istatComune");
    String comune = request.getParameter("comune");
    String provincia = request.getParameter("provincia");
    String foglio = request.getParameter("foglio");
    String particella = request.getParameter("particella");
    String sezione = request.getParameter("sezione");
    // Controllo che almeno il comune o il foglio siano valorizzati
    if(!(Validator.isNotEmpty(istatComune) && Validator.isNotEmpty(foglio))) 
    {
      if(Validator.isNotEmpty(comune) && Validator.isNotEmpty(provincia))
      { 
        //caso in cui inseriti provincia e comune a mano
        //String istatComune = null;
        try 
        {
          istatComune = anagFacadeClient.ricercaCodiceComuneNonEstinto(comune, provincia);
        }
        catch(SolmrException se) 
        {
          if(errors == null)
          {
            errors = new ValidationErrors();
          }
          errors.add("comune", new ValidationError(se.getMessage()));
          //errors.add("provincia", new ValidationError(se.getMessage()));
        }        
      }
      else
      {
        if(errors == null)
        {
          errors = new ValidationErrors();
        }
        errors.add("comune", new ValidationError(AnagErrors.ERRORE_NO_COMUNE_FOGLIO_PROVINCIA));
        errors.add("foglio", new ValidationError(AnagErrors.ERRORE_NO_COMUNE_FOGLIO_PROVINCIA));
        errors.add("provincia", new ValidationError(AnagErrors.ERRORE_NO_COMUNE_FOGLIO_PROVINCIA));
      }
      
      if(!Validator.isNotEmpty(foglio))
      {
        if(errors == null)
        {
          errors = new ValidationErrors();
        }
        errors.add("comune", new ValidationError(AnagErrors.ERRORE_NO_COMUNE_FOGLIO_PROVINCIA));
        errors.add("foglio", new ValidationError(AnagErrors.ERRORE_NO_COMUNE_FOGLIO_PROVINCIA));
        errors.add("provincia", new ValidationError(AnagErrors.ERRORE_NO_COMUNE_FOGLIO_PROVINCIA));
      }
    }
    // Se è valorizzato...
    else 
    {
      //Controllo che i campi siano numerici
      if(Validator.isNotEmpty(foglio) && !Validator.isNumericInteger(foglio)) 
      {
        if(errors == null)
        {
          errors = new ValidationErrors();
        }
        errors.add("foglio", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
      if(Validator.isNotEmpty(particella) && !Validator.isNumericInteger(particella)) 
      {
        if(errors == null)
        {
          errors = new ValidationErrors();
        }
        errors.add("particella", new ValidationError(AnagErrors.ERRORE_CAMPO_ERRATO));
      }
    }
    
    if(errors !=null)
    {
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page= "<%= terreniImportaAsservimentoUrl %>" />
      <%
      return;
    }
      
      
    FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoImportaAsservimentoVO = new FiltriRicercaTerrenoImportaAsservimentoVO();
    filtriRicercaTerrenoImportaAsservimentoVO.setFoglio(foglio);
    filtriRicercaTerrenoImportaAsservimentoVO.setIstatComuneParticella(istatComune);
    filtriRicercaTerrenoImportaAsservimentoVO.setParticella(particella);
    filtriRicercaTerrenoImportaAsservimentoVO.setSezione(sezione);
    filtriRicercaTerrenoImportaAsservimentoVO.setTipoRicerca(SolmrConstants.RICERCA_IMPORTA_ASSERVIMENO_CHIAVE_CATASTALE);
    
    session.setAttribute("filtriRicercaTerrenoImportaAsservimentoVO", filtriRicercaTerrenoImportaAsservimentoVO);
    
    // Ricerco gli ID_Particella per passarli
    Vector elencoIdParticelle = null;
    long[] idPart = null;
    try 
    {
      idPart = anagFacadeClient.ricercaIdParticelleTerreni(filtriRicercaTerrenoImportaAsservimentoVO);
      /*if(idPart !=null)
      {
        elencoIdParticelle = Converter.longArrayToVector(idPart);
      }*/
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
    }
    

    // Metto il vettore in request
    session.setAttribute("elencoIdParticelleImportaAsservimento", idPart);

    // Recupero l'elenco delle UTE a cui associare le particelle
    Vector elencoUte = null;
    try 
    {
      elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
    }
   
    // Metto il vettore in request
    request.setAttribute("elencoUte", elencoUte);

    // Vado alla pagina di importazione elenco particelle
    %>
      <jsp:forward page= "<%= particellareTerreniImportaAsservimentoUrl %>" />
    <% 
  }
   // L'utente ha selezionato il pulsante avanti
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("avanti")) 
  {
    // Recupero il cuaa
    FiltriRicercaTerrenoImportaAsservimentoVO filtriRicercaTerrenoImportaAsservimentoVO 
      = (FiltriRicercaTerrenoImportaAsservimentoVO)session.getAttribute("filtriRicercaTerrenoImportaAsservimentoVO");
    //String cuaa = (String)session.getAttribute("cuaaImportaAsservimento");
    
    cuaa = filtriRicercaTerrenoImportaAsservimentoVO.getCUAA();

    // Recupero le aziende
    Vector elencoAziende = null;
    try 
    {
      elencoAziende = anagFacadeClient.getAziendaByCriterioCessataAndProvvisoria(cuaa);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
    }
    request.setAttribute("elencoAziende", elencoAziende);

    // Verifico che sia stata selezionata un'azienda
    String aziendaSelezionata = request.getParameter("idAnagraficaAzienda");
    if(!Validator.isNotEmpty(aziendaSelezionata)) 
    {
      request.setAttribute("messaggioErrore",(String)AnagErrors.get("ERR_PARTICELLA_FILTRO"));
      request.setAttribute("history","true");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;    
    }
    // Metto l'id in request
    request.setAttribute("idAnagraficaAzienda", Long.decode(aziendaSelezionata));

    // Se è stata selezionata ne recupero i valori
    AnagAziendaVO anagAziendaSearchVO = null;
    try 
    {
      anagAziendaSearchVO = anagFacadeClient.getAziendaById(Long.decode(aziendaSelezionata));
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
    }

    // Metto l'azienda selezionata dall'utente in sessione
    session.setAttribute("anagAziendaSearchVoImportaAsservimento", anagAziendaSearchVO);
    filtriRicercaTerrenoImportaAsservimentoVO.setIdAziendaSearch(anagAziendaSearchVO.getIdAzienda().longValue());
    session.setAttribute("filtriRicercaTerrenoImportaAsservimentoVO",filtriRicercaTerrenoImportaAsservimentoVO);
    
    //Se l'utente è un intermediario...
    //devo verificare: 
    //- se le aziende hanno mandato attivo allo stesso CAA procedo
    //- se una o entrambe non hanno mandato attivo ad un CAA procedo
    if(ruoloUtenza.isUtenteIntermediario() || ruoloUtenza.isUtenteOPRGestore()) 
    {
      String codFiscaleIntermediarioDelegaRicevente = null;
      String codFiscaleIntermediarioDelegaDonante = null;
      if((anagAziendaVO.getDelegaVO() != null) &&  (anagAziendaSearchVO.getDelegaVO() !=null))
      {
        codFiscaleIntermediarioDelegaRicevente = anagAziendaVO.getDelegaVO()
          .getCodiceFiscaleIntermediario().substring(0, 3);
        codFiscaleIntermediarioDelegaDonante = anagAziendaSearchVO.getDelegaVO()
          .getCodiceFiscaleIntermediario().substring(0, 3);
        //Entrambe le aziende hanno il mandato attivo ma a due diversi CAA
        if(!codFiscaleIntermediarioDelegaRicevente.equalsIgnoreCase(codFiscaleIntermediarioDelegaDonante))
        {
          request.setAttribute("messaggioErrore",AnagErrors.DIFF_MANDATO_ATTIVO);
          request.setAttribute("history","true");
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;    
        }
      }
    }
    //Se l'utente è un PA
    // non occorre controllare i mandati
    else if(ruoloUtenza.isUtentePA())
    {}
    //In tutti gli altri casi
    else
    {
      request.setAttribute("messaggioErrore",AnagErrors.ABILITAZIONE_UTENTE);
      request.setAttribute("history","true");
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;  
    }

    //Ricerco le particelle importabili in relazione all'azienda selezionata
    // dall'utente
    long idParticelle[] = null;
    try 
    {
      idParticelle = anagFacadeClient.ricercaIdConduzioneTerreniImportaAsservimento(filtriRicercaTerrenoImportaAsservimentoVO);
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
    }

    // Metto il vettore in request
    session.setAttribute("elencoIdParticelleImportaAsservimento", idParticelle);

    // Recupero l'elenco delle UTE a cui associare le particelle
    Vector elencoUte = null;
    try 
    {
      elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
    }
    catch(SolmrException se) 
    {
      SolmrLogger.info(this, " - terreniImportaAsservimentoCtrl.jsp - FINE PAGINA");
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
    }

    // Metto il vettore in request
    request.setAttribute("elencoUte", elencoUte);

    // Vado alla pagina di importazione elenco particelle
    %>
       <jsp:forward page= "<%= particellareTerreniImportaAsservimentoUrl %>" />
    <%
    return;
  }

%>

<jsp:forward page= "<%= terreniImportaAsservimentoUrl %>" />


