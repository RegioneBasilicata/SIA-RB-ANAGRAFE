<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.ricerca.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>
<%@ page import="it.csi.papua.papuaserv.presentation.ws.profilazione.axis.UtenteAbilitazioni" %>

<%

  String iridePageName = "trasferimentoUfficioCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  SolmrLogger.debug(this, " - trasferimentoUfficioCtrl.jsp - INIZIO PAGINA");
  
  String trasferimentoUfficioUrl = "/view/trasferimentoUfficioView.jsp";
  String confermaTrasferimentoUfficioUrl = "/ctrl/confermaTrasferimentoUfficioCtrl.jsp";
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  Vector<CodeDescription> elencoCAA = null;
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  UtenteAbilitazioni utenteAbilitazioni = (UtenteAbilitazioni)session.getAttribute("utenteAbilitazioni");
  ValidationErrors errors = new ValidationErrors();
  ValidationError errori = (ValidationError)request.getAttribute("error");
  if(errori != null) 
  {
    errors.add("error", errori);
    request.setAttribute("errors", errors);
  }
  
  String paginaCorrente = request.getParameter("pagina");
  // Se pagina corrente è null o "" significa che è la prima volta che accedo e
  // quindi lo setto de default a 1
  if(!Validator.isNotEmpty(paginaCorrente)) 
  {
    paginaCorrente = "1";
  }
  request.setAttribute("paginaCorrente", paginaCorrente);
  String operazione = request.getParameter("operazione");
  
  //inizializza
  if(!Validator.isNotEmpty(operazione) && !Validator.isNotEmpty((String)request.getParameter("vai")))
  {
    session.removeAttribute("numUtilizziSelezionati");
    WebUtils.removeUselessFilter(session, null);      
  }
  
  
  try 
  {
    elencoCAA = anagFacadeClient.getElencoCAAByUtente(utenteAbilitazioni);      
  }
  catch(SolmrException se) 
  {
    request.setAttribute("messaggioErrore", se.getMessage());
    SolmrLogger.info(this, " - trasferimentoUfficioCtrl.jsp - FINE PAGINA");
    //watcher.dumpElapsed("trasferimentoUfficioCtrl.jsp", "controller of trasferimento ufficio", "In trasferimentoUfficioCtrl.jsp from the beginning to the end", null);
    %>
      <jsp:forward page= "<%= trasferimentoUfficioUrl %>" />
    <%
    return;
    
  }
  
  //gestione della seconda combo sul valore scelto nella prima.
  String idIntermediario = request.getParameter("idIntermediario_1");
  if(!Validator.isNotEmpty(idIntermediario))
  {
    idIntermediario = (String)session.getAttribute("idIntermediario_1");
  }
  
  popolaSecondaCombo(request, idIntermediario, elencoCAA);
  
  request.setAttribute("elencoCAA",elencoCAA);
  
  // L'utente ha cliccato sul pulsante ricerca
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("ricerca")) 
  {
    //Svuoto il numUtilizzi in quanto rifaccio la ricerca!
    session.removeAttribute("numUtilizziSelezionati");
    
    
    GestioneUtilitiVO gVO = new GestioneUtilitiVO();
    errors = gVO.validateRicercaTrasferimentoAzienda(request,null);
 
    
    if(errors != null && errors.size() > 0) 
    {
      request.setAttribute("errors", errors);
      %>
        <jsp:forward page="<%= trasferimentoUfficioUrl %>" />
      <%
      return;
    }
    
    // Rimetto la pagina di partenza a 1
    paginaCorrente = "1";
    request.setAttribute("paginaCorrente", paginaCorrente);
    
    Vector<AnagAziendaVO> elencoAziende = null;
    
    // Effettuo la ricerca in relazione ai nuovi parametri di ricerca inseriti
    // dall'utente
    try 
    {      
      elencoAziende = anagFacadeClient.getAziendaByIntermediarioAndCuaa(new Long(request.getParameter("idIntermediario_1")), (String)request.getParameter("cuaa"));
    } 
    catch (SolmrException se) 
    {
      String messaggioErrore = (String)AnagErrors.ERRORE_KO_SEARCH_AZIENDE_GU;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= trasferimentoUfficioUrl %>" />
      <%
      return;
    }
    
    // Se non sono state trovate delle aziende per i criteri di ricerca impostati
    // avviso l'utente con opportuno messaggio di errore
    if(elencoAziende == null || elencoAziende.size() == 0) 
    {
      String messaggioErrore = (String) AnagErrors.ERRORE_KO_SEARCH_NO_AZIENDE_FOUND_GU;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= trasferimentoUfficioUrl %>" />
      <%
      
      return;
    }
    request.setAttribute("elencoAziende", elencoAziende);
    
  }
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("cambioIntermediario"))
  {    
    
    //popolaSecondaCombo(request, elencoCAA);
    //Non fa niente
  }
  else if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("annullaDaConferma"))
  { //Arrivo qui dopo aver cliccato il tasto annulla nella pagina di conferma   
    
    session.removeAttribute("numUtilizziSelezionati");
    String idIntermediario_1 = (String)session.getAttribute("idIntermediario_1");
    String cuaa = (String)session.getAttribute("cuaaTrasferimentoUfficio");
    //String idIntermediario_2 = (String)session.getAttribute("idIntermediario_2");
    
    // Rimetto la pagina di partenza a 1
    paginaCorrente = "1";
    request.setAttribute("paginaCorrente", paginaCorrente);
    
    Vector<AnagAziendaVO> elencoAziende = null;
    
    // Effettuo la ricerca in relazione ai nuovi parametri di ricerca inseriti
    // dall'utente
    try 
    {      
      elencoAziende = anagFacadeClient.getAziendaByIntermediarioAndCuaa(new Long(idIntermediario_1), cuaa);
    } 
    catch (SolmrException se) {
      String messaggioErrore = (String)AnagErrors.ERRORE_KO_SEARCH_AZIENDE_GU;
      request.setAttribute("messaggioErrore", messaggioErrore);
      %>
        <jsp:forward page="<%= trasferimentoUfficioUrl %>" />
      <%
      return;
    }
    
    request.setAttribute("elencoAziende", elencoAziende);
  }  
  else  //Gestisco la paginazione 
  {
    // Se l'utente ha selezionato il pulsante vai
    String paginaChiave = "";
    if(request.getParameter("vai") != null) 
    {
      String numPagina = request.getParameter("paginaCorrente");
      paginaChiave = request.getParameter("pagina");
      // Controllo che sia stato indicato il numero di pagina
      if(!Validator.isNotEmpty(numPagina)) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_OBBLIGO_NUM_PAGINAZIONE_PARTICELLE);
        errors.add("paginaCorrente", error);
          request.setAttribute("errors", errors);
      }
      // Controllo che sia un numero intero e positivo
      else if(!Validator.isNumericInteger(numPagina) || Integer.parseInt(numPagina) <= 0) 
      {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_KO_NUM_PAGINAZIONE_PARTICELLE);
        errors.add("paginaCorrente", error);
        request.setAttribute("errors", errors);
      }
      // Controllo che non sia superiore al rapporto con il numero totale di pagine possibili
      else if(Integer.parseInt(numPagina) > Integer.parseInt(request.getParameter("numeroTotale"))) {
        ValidationError error = new ValidationError(AnagErrors.ERRORE_NUM_PAGINAZIONE_PARTICELLE_SUP_NUM_TOT);
        errors.add("paginaCorrente", error);
          request.setAttribute("errors", errors);
      }
      // Se passo i controlli metto il valore in request
      if(errors.size() == 0) {
        request.setAttribute("paginaCorrente", numPagina);
      }
      else {
        request.setAttribute("paginaCorrente", paginaCorrente);
      }
    }
    else
    {
      paginaChiave = request.getParameter("paginaCorrente");
    }
    // L'utente ha cliccato su uno dei numeri della paginazione
    if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("paginazione")) {
      String numPagina = request.getParameter("pagina");
      request.setAttribute("paginaCorrente", numPagina);
    }
    
    if(request.getParameter("idIntermediario_1") !=null)
    {    
      Vector<AnagAziendaVO> elencoAziende = null;
      
      // Gestisco la selezione delle particelle prima di effettuare la ricerca ed 
      // andare alla pagina selezionata: se sono nella prima di default ovviamente
      // non farò nulla
      String[] elencoIdUtilizzo = request.getParameterValues("idUtilizzo");
      
      HashMap<String,Vector<Long>> numUtilizziSelezionati = (HashMap<String,Vector<Long>>)session.getAttribute("numUtilizziSelezionati");
      if(numUtilizziSelezionati == null) {
        numUtilizziSelezionati = new HashMap<String,Vector<Long>>();
      }
      
      Vector<Long> elencoUtilizziSelezionati = new Vector<Long>();
      //String paginaChiave = request.getParameter("paginaCorrente");
      
      if(elencoIdUtilizzo != null && elencoIdUtilizzo.length > 0) 
      {
        for(int i = 0; i < elencoIdUtilizzo.length; i++) 
        {
          Long idUtilizzo = Long.decode((String)elencoIdUtilizzo[i]);
          elencoUtilizziSelezionati.add(idUtilizzo);
        }
        
        numUtilizziSelezionati.put(paginaChiave, elencoUtilizziSelezionati);
        session.setAttribute("numUtilizziSelezionati", numUtilizziSelezionati);
      }
      else 
      {
        numUtilizziSelezionati.remove(paginaChiave);
        session.setAttribute("numUtilizziSelezionati", numUtilizziSelezionati);
      }
      
      if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("trasferisci")) 
      {
        GestioneUtilitiVO gVO = new GestioneUtilitiVO();
        errors = gVO.validateRicercaTrasferimentoAzienda(request,null);
        
        if(errors != null && errors.size() > 0) {
          request.setAttribute("errors", errors);
          %>
            <jsp:forward page="<%= trasferimentoUfficioUrl %>" />
          <%
          return;
        }
        
        //Metto in request i nomi dell'intermediari per la visualizzzazione
        //nella conferma del trasferimento
        String nomeIntermediario_1 = "";
        String nomeIntermediario_2 = "";
        if(elencoCAA != null && elencoCAA.size() > 0) 
        {
          Iterator<CodeDescription> iteraCAA = elencoCAA.iterator();
          String idInterm_1 = request.getParameter("idIntermediario_1");
          String idInterm_2 = request.getParameter("idIntermediario_2");
          String cuaa = request.getParameter("cuaa");
          while(iteraCAA.hasNext()) 
          {
            CodeDescription code = (CodeDescription)iteraCAA.next();
            if(idInterm_1.equalsIgnoreCase(code.getCode().toString()))
            {
              nomeIntermediario_1 = code.getDescription();
            }
            
            if(idInterm_2.equalsIgnoreCase(code.getCode().toString()))
            {
              nomeIntermediario_2 = code.getDescription();
            }
          }
          
          request.setAttribute("nomeIntermediario_1",nomeIntermediario_1);
          request.setAttribute("nomeIntermediario_2",nomeIntermediario_2);
          session.setAttribute("idIntermediario_1", idInterm_1);
          session.setAttribute("idIntermediario_2", idInterm_2);
          session.setAttribute("cuaaTrasferimentoUfficio", cuaa);
        }
        
    
        
        %>
          <jsp:forward page="<%= confermaTrasferimentoUfficioUrl %>" />
          
        <%
        return;
        
      }
      else //resetta gli id intermediari in sessione poiche non sono utilizzati
      {
        session.removeAttribute("idIntermediario_1");
        session.removeAttribute("idIntermediario_2");
      }
      
      // Effettuo la ricerca in relazione ai nuovi parametri di ricerca inseriti
      // dall'utente
      try {
        
        elencoAziende = anagFacadeClient.getAziendaByIntermediarioAndCuaa(new Long(request.getParameter("idIntermediario_1")), (String)request.getParameter("cuaa"));
      } 
      catch (SolmrException se) {
        String messaggioErrore = (String)AnagErrors.ERRORE_KO_SEARCH_AZIENDE_GU;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>
          <jsp:forward page="<%= trasferimentoUfficioUrl %>" />
        <%
        return;
      }
      
      // Se non sono state trovate delle particelle per i criteri di ricerca impostati
      // avviso l'utente con opportuno messaggio di errore
      if(elencoAziende == null || elencoAziende.size() == 0) {
        String messaggioErrore = (String) AnagErrors.ERRORE_KO_SEARCH_NO_AZIENDE_FOUND_GU;
        request.setAttribute("messaggioErrore", messaggioErrore);
        %>
          <jsp:forward page="<%= trasferimentoUfficioUrl %>" />
        <%
        
        return;
      }
      request.setAttribute("elencoAziende", elencoAziende);
      
    }
    
  }
  
  if(errors != null && errors.size() > 0) {
      request.setAttribute("errors", errors);
  }

  
  SolmrLogger.debug(this, " - trasferimentoUfficioCtrl.jsp - FINE PAGINA");
  
  %>
    <jsp:forward page= "<%= trasferimentoUfficioUrl %>" />
    
<%! 
  private void popolaSecondaCombo(HttpServletRequest request, String idIntermediario_1, Vector<CodeDescription> elencoCAA)
  {
     //Gestizio il cambio del primo combo
    if(Validator.isNotEmpty(idIntermediario_1))
    {
      Vector<CodeDescription> elencoCAA_2 = null;
      String codFisc = "";
      for(int i=0;i<elencoCAA.size();i++)
      {
        CodeDescription code = (CodeDescription)elencoCAA.get(i);
        if(code.getCode().compareTo(new Integer(idIntermediario_1)) == 0)
        {
          codFisc = code.getSecondaryCode();
          break;
        }
      }
      
      codFisc = codFisc.substring(0,3);
      for(int i=0;i<elencoCAA.size();i++)
      {
        CodeDescription code = (CodeDescription)elencoCAA.get(i);
        String codFiscTmp = code.getSecondaryCode().substring(0,3);
        //solo quelli attivi
        if(codFisc.equalsIgnoreCase(codFiscTmp)
          && Validator.isEmpty(code.getCodeFlag()))
        {
          if(elencoCAA_2 == null)
          {
            elencoCAA_2 = new Vector<CodeDescription>();
          }          
          elencoCAA_2.add((CodeDescription)elencoCAA.get(i));
        }
      }
      
      
      
      request.setAttribute("elencoCAA_2", elencoCAA_2);
    }
  
  }


%>
  
