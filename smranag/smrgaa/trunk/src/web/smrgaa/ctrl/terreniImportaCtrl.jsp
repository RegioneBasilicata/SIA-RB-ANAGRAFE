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
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "terreniImportaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%

  String terreniImportaUrl = "/view/terreniImportaView.jsp";
  String particellareTerreniImportaUrl = "/view/particellareTerreniImportaView.jsp";
  String erroreView = "/view/erroreView.jsp";
  String pageBack = "../"+request.getHeader("referer").substring(request.getHeader("referer").indexOf("layout"), request.getHeader("referer").length());

  ValidationErrors errors = new ValidationErrors();
  ValidationError error = null;
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String messaggio = null;

  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
   
   // TITOLO DI POSSESSO
  it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = null;
  try 
  {
    elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_DESCRIPTION);
    request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
  }
  catch(SolmrException se) 
  {
    messaggio = (String)AnagErrors.ERRORE_KO_TITOLO_POSSESSO+" "+se.getMessage();
    request.setAttribute("messaggioErrore", messaggio);
    request.setAttribute("pageBack", pageBack);
    request.getRequestDispatcher(erroreView).forward(request, response);
    return;
  }
  
  // Recupero il CUAA
  String cuaa = request.getParameter("cuaa");
  session.setAttribute("cuaa", cuaa);

  // L'utente ha selezionato il pulsante "cerca"
  if(request.getParameter("cerca") != null) 
  {
    // Controllo che il CUAA sia stato valorizzato
    if(!Validator.isNotEmpty(cuaa)) 
    {
      error = new ValidationError((String)AnagErrors.get("ERR_INSERIRE_CUAA"));
      errors.add("cuaa", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
      return;
    }
    // Se è valorizzato...
    else 
    {
      // Controllo che non sia lo stesso dell'azienda su cui si sta lavorando
      if(cuaa.toUpperCase().equalsIgnoreCase(anagAziendaVO.getCUAA().toUpperCase())) 
      {
        error = new ValidationError((String)AnagErrors.get("ERR_CUAA_EQUALS_AZIENDA_IMPORT"));
        errors.add("cuaa", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
        return;
      }
      // Se non lo è controllo la correttezza formale
      else 
      {
        // Se si tratta di un codice fiscale
        if(cuaa.length() == 16) 
        {
          if(!Validator.controlloCf(cuaa)) 
          {
            error = new ValidationError((String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
            errors.add("cuaa", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
            return;
          }
        }
        else 
        {
          if(!Validator.controlloPIVA(cuaa)) 
          {
            error = new ValidationError((String)AnagErrors.get("ERR_CUAA_NO_CORRETTO"));
            errors.add("cuaa", error);
            request.setAttribute("errors", errors);
            request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
            return;
          }
        }
      }
    }

    // Se ho passato i controlli di validità del CUAA ricerco un'azienda attiva
    // cioè con DATA_FINE_VALIDITA = NULL
    Vector<AnagAziendaVO> elencoAziende = null;
    try 
    {
      elencoAziende = anagFacadeClient.getAziendaCUAA(cuaa);
    }
    catch(SolmrException se) 
    {
      messaggio = se.getMessage();
      request.setAttribute("messaggioErrore", messaggio);
      request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
      return;
    }

    // Se la ricerca non ha prodotto nessun risultato avviso l'utente
    if(elencoAziende == null || elencoAziende.size() == 0) 
    {
      messaggio = (String)AnagErrors.get("ERR_CUAA_NON_TROVATO_FOR_IMPORTA_PARTICELLE");
      request.setAttribute("messaggioErrore", messaggio);
      request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
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
        <jsp:forward page= "<%= terreniImportaUrl %>" />
      <%
    }
    // Se la ricerca invece ha prodotto un unico risultato allora proseguo con le
    // operazioni per l'importazione delle particelle
    else 
    {
      // Recupero l'azienda frutto della ricerca
      AnagAziendaVO anagAziendaSearchVO = (AnagAziendaVO)elencoAziende.elementAt(0);

      // Metto l'azienda selezionata dall'utente in sessione
      session.setAttribute("anagAziendaSearchVO", anagAziendaSearchVO);

      // Ricerco le particelle importabili in relazione all'azienda selezionata
      // dall'utente
      Vector<ParticellaVO> elencoParticelle = null;
      try 
      {
        elencoParticelle = anagFacadeClient.getElencoParticelleForImportByAzienda(
        anagAziendaSearchVO, anagAziendaVO, ruoloUtenza);
      }
      catch(SolmrException se) 
      {
        error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
        return;
      }

      // Metto il vettore in request
      session.setAttribute("elencoParticelle", elencoParticelle);

      // Recupero l'elenco delle UTE a cui associare le particelle
      Vector<UteVO> elencoUte = null;
      try 
      {
        elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
      }
      catch(SolmrException se) 
      {
        error = new ValidationError(se.getMessage());
        errors.add("error", error);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
        return;
      }

      // Metto il vettore in request
      request.setAttribute("elencoUte", elencoUte);

      // Vado alla pagina di importazione elenco particelle
      %>
        <jsp:forward page= "<%= particellareTerreniImportaUrl %>" />
      <%
    }
  }
  // L'utente ha selezionato il pulsante avanti
  else if(request.getParameter("avanti") != null) 
  {
    // Recupero le aziende
    Vector<AnagAziendaVO> elencoAziende = null;
    try 
    {
      elencoAziende = anagFacadeClient.getAziendaCUAA(cuaa);
    }
    catch(SolmrException se) 
    {
      messaggio = se.getMessage();
      request.setAttribute("messaggioErrore", messaggio);
      request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
      return;
    }
    request.setAttribute("elencoAziende", elencoAziende);

    // Verifico che sia stata selezionata un'azienda
    String aziendaSelezionata = request.getParameter("idAnagraficaAzienda");
    if(!Validator.isNotEmpty(aziendaSelezionata)) 
    {
      error = new ValidationError((String)AnagErrors.get("ERR_PARTICELLA_FILTRO"));
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
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
      error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
      return;
    }

    // Metto l'azienda selezionata dall'utente in sessione
    session.setAttribute("anagAziendaSearchVO", anagAziendaSearchVO);

    //Ricerco le particelle importabili in relazione all'azienda selezionata
    // dall'utente
    Vector<ParticellaVO> elencoParticelle = null;
    try 
    {
      elencoParticelle = anagFacadeClient.getElencoParticelleForImportByAzienda(
      anagAziendaSearchVO, anagAziendaVO, ruoloUtenza);
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
      return;
    }

    // Metto il vettore in request
    session.setAttribute("elencoParticelle", elencoParticelle);

    // Recupero l'elenco delle UTE a cui associare le particelle
    Vector<UteVO> elencoUte = null;
    try 
    {
      elencoUte = anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(terreniImportaUrl).forward(request, response);
      return;
    }

    // Metto il vettore in request
    request.setAttribute("elencoUte", elencoUte);

    // Vado alla pagina di importazione elenco particelle
    %>
      <jsp:forward page= "<%= particellareTerreniImportaUrl %>" />
    <%
  }
  // L'utente ha selezionato la funzionalità "importa" dal menù
  else 
  {
    session.removeAttribute("elencoParticelle");
    session.removeAttribute("anagAziendaSearchVO");

    // Se l'azienda selezionata è priva di CUAA impedisco l'accesso alla funzionalità
    if(!Validator.isNotEmpty(anagAziendaVO.getCUAA())) 
    {
      messaggio = (String)AnagErrors.get("ERR_IMPOSSIBILE_IMPORT_FOR_CUAA");
      request.setAttribute("messaggioErrore", messaggio);
      request.setAttribute("pageBack", pageBack);
      request.getRequestDispatcher(erroreView).forward(request, response);
      return;
    }

    // Controllo che l'azienda selezionata abbia un ute
    try 
    {
      anagFacadeClient.getUTE(anagAziendaVO.getIdAzienda(), new Boolean(false));
    }
    catch(SolmrException se) 
    {
      messaggio = (String)AnagErrors.get("ERR_IMPOSSIBILE_IMPORT")+" "+se.getMessage();
      request.setAttribute("messaggioErrore", messaggio);
      request.setAttribute("pageBack", pageBack);
      request.getRequestDispatcher(erroreView).forward(request, response);
      return;
    }

    // Mando alla view di importa
    %>
      <jsp:forward page= "<%= terreniImportaUrl %>" />
    <%
  }

%>

