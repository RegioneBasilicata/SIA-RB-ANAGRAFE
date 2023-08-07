<%@ page language="java"
         contentType="text/html"
%>

<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "particellareTerreniImportaCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%

  String particellaTerreniImportaUrl = "/view/particellareTerreniImportaView.jsp";
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String terreniImportaOkUrl = "/view/terreniImportaOkView.jsp";

  ValidationErrors errors = new ValidationErrors();
  ValidationError error = null;
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  AnagAziendaVO anagAziendaSearchVO = (AnagAziendaVO)session.getAttribute("anagAziendaSearchVO");
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
   
  // TITOLO DI POSSESSO
  it.csi.solmr.dto.CodeDescription[] elencoTipoTitoloPossesso = null;
  try 
  {
    elencoTipoTitoloPossesso = anagFacadeClient.getListTipiTitoloPossesso(SolmrConstants.ORDER_BY_GENERIC_CODE);
    request.setAttribute("elencoTipoTitoloPossesso", elencoTipoTitoloPossesso);
  }
  catch(SolmrException se) 
  {       
    error = new ValidationError((String)AnagErrors.ERRORE_KO_TITOLO_POSSESSO+" "+se.getMessage());
    errors.add("error", error);
    request.setAttribute("errors", errors);
    request.getRequestDispatcher(particellaTerreniImportaUrl).forward(request, response);
    return; 
  }
   

  // L'utente ha premuto il pulsante "importa"
  if(request.getParameter("importa") != null) 
  {

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
      request.getRequestDispatcher(particellaTerreniImportaUrl).forward(request, response);
      return;
    }
    // Metto il vettore in request
    request.setAttribute("elencoUte", elencoUte);

    // Recupero l'unità produttiva selezionata
    String unitaProduttiva = request.getParameter("idUte");
    Long idUte = null;
    // Verifico che sia stata selezionata
    if(!Validator.isNotEmpty(unitaProduttiva)) 
    {
      error = new ValidationError((String)AnagErrors.get("SELEZIONA_UTE"));
      errors.add("idUte", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(particellaTerreniImportaUrl).forward(request, response);
      return;
    }
    else 
    {
      idUte = Long.decode(unitaProduttiva);
      request.setAttribute("idUte", idUte);
    }

    // Recupero l'elenco delle conduzioni selezionate
    String elencoConduzioni[] = request.getParameterValues("idConduzioneParticella");

    // Controllo che sia stata selezionata almeno una particella
    if(elencoConduzioni == null || elencoConduzioni.length == 0) 
    {
      error = new ValidationError((String)AnagErrors.get("ERR_NO_PARTICELLE_SELECTED_FOR_IMPORT"));
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(particellaTerreniImportaUrl).forward(request, response);
      return;
    }

    // Se ho passato tutti i controlli effettuo l'importazione delle particelle...
    try 
    {
      String idTitoloPossesso = request.getParameter("idTitoloPossesso");
      Long idTitoloPossessoL=new Long(idTitoloPossesso);
      anagFacadeClient.importParticelle(elencoConduzioni, idUte, anagAziendaVO, anagAziendaSearchVO, ruoloUtenza,idTitoloPossessoL);
    }
    catch(SolmrException se) 
    {
      error = new ValidationError(se.getMessage());
      errors.add("error", error);
      request.setAttribute("errors", errors);
      request.getRequestDispatcher(particellaTerreniImportaUrl).forward(request, response);
      return;
    }

    // Vado alla pagina di caricamento dati
    request.setAttribute("action", "../layout/particellareTerreniImporta.htm");
    request.setAttribute("operazione", "endOperation");
    %>
      <jsp:forward page="<%= attenderePregoUrl %>"/>
    <%
  }
  // L'utente arriva dalla pagina di attesa per il caricamento dati
  else if(request.getParameter("operazione").equalsIgnoreCase("endOperation")) 
  {
    session.removeAttribute("cuaa");
    session.removeAttribute("elencoParticelle");
    session.removeAttribute("anagAziendaSearchVO");
    // Vado alla pagina di conferma esito importazione particelle
    %>
      <jsp:forward page="<%= terreniImportaOkUrl %>"/>
    <%
  }

%>

