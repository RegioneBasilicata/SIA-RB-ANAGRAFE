<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>


<%

  String iridePageName = "sceltaAllineaPercUsoEleggCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  
  SolmrLogger.debug(this, " - sceltaAllineaPercUsoEleggCtrl.jsp - INIZIO PAGINA");
  
  String sceltaAllineaPercUsoEleggURL = "/view/sceltaAllineaPercUsoEleggView.jsp";
  String actionUrl = "../layout/terreniUnitaArboreeElenco.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String confermaAllineaPercUsoEleggURL = "/ctrl/confermaAllineaPercUsoEleggCtrl.jsp";
  
  final String errMsg = "Impossibile procedere nella sezione allinea % uso eleggibile."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  HashMap<String,Vector<Long>> numUnitaArboreeSelezionate = (HashMap<String,Vector<Long>>)session.getAttribute("numUnitaArboreeSelezionate");
  
  Vector<Long> elencoIdDaModificare = new Vector<Long>();
  //prima volta che entro
  if(request.getParameter("regimeAllineaPercUsoElegg") == null)
  {
    WebUtils.removeUselessFilter(session,"filtriUnitaArboreaRicercaVO");
    
    String[] idStoricoUnitaArborea = request.getParameterValues("idUnita");
    String pagina = request.getParameter("pagina");
    if(Validator.isNotEmpty(pagina)) {
      session.setAttribute("pagina", pagina);
    }
    // Gestione della selezione delle unità arboree
    Vector<Long> elenco = new Vector<Long>();
    if(numUnitaArboreeSelezionate != null && numUnitaArboreeSelezionate.size() > 0) 
    {
      numUnitaArboreeSelezionate.remove(pagina);
      if(idStoricoUnitaArborea != null && idStoricoUnitaArborea.length > 0) {
        numUnitaArboreeSelezionate.remove(pagina);
        for(int i = 0; i < idStoricoUnitaArborea.length; i++) {
          elenco.add(Long.decode((String)idStoricoUnitaArborea[i]));
        }
      }
    }
    else 
    {
      if(numUnitaArboreeSelezionate == null) {
        numUnitaArboreeSelezionate = new HashMap<String,Vector<Long>>();
      }
      // Se non ho selezionato il pulsante annulla
      if(idStoricoUnitaArborea != null && idStoricoUnitaArborea.length > 0) {
        for(int i = 0; i < idStoricoUnitaArborea.length; i++) {
          Long idElemento = Long.decode((String)idStoricoUnitaArborea[i]);
          elenco.add(idElemento);
        }
      }
    }
    if(elenco.size() > 0) 
    {
      numUnitaArboreeSelezionate.put(pagina, elenco);
      session.setAttribute("numUnitaArboreeSelezionate", numUnitaArboreeSelezionate);
    }
  }
  
  
  //sono già nella pagine e richiamo uno scarico
  if(request.getParameter("regimeAllineaPercUsoElegg") != null)
  {   
  
    //Scarico per id unita vitate
    if(Validator.isNotEmpty(request.getParameter("tipoAllineaPercUsoElegg"))
     && request.getParameter("tipoAllineaPercUsoElegg").equalsIgnoreCase("1"))
    {
      String parametroRUVM = null;
      try 
      {
        parametroRUVM = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RUVM);;
      }
      catch(SolmrException se) 
      {
        SolmrLogger.info(this, " - sceltaAllineaPercUsoEleggCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+": "+AnagErrors.ERRORE_KO_PARAMETRO_RUVM+".\n"+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    
      // Verifico che non siano state selezionate più particelle rispetto a quelle consentite
      Set<String> elencoKeys = numUnitaArboreeSelezionate.keySet();
      Iterator<String> iteraKey = elencoKeys.iterator();
      while(iteraKey.hasNext()) 
      {
        Vector<Long> selezioni = (Vector<Long>)numUnitaArboreeSelezionate.get((String)iteraKey.next());
        if(selezioni != null && selezioni.size() > 0) {
          for(int a = 0; a < selezioni.size(); a++) {
            elencoIdDaModificare.add((Long)selezioni.elementAt(a));
          }
        }
      }
      
      if(elencoIdDaModificare.size() == 0)
      {
        request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_SEL_UV);
        %>
          <jsp:forward page="<%= sceltaAllineaPercUsoEleggURL %>" />
        <%
        return;
      }      
      else if(elencoIdDaModificare.size() > Integer.parseInt(parametroRUVM)) 
      {
        request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1+parametroRUVM+AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2);
        %>
          <jsp:forward page="<%= sceltaAllineaPercUsoEleggURL %>" />
        <%
        return;
      }
    }
    
    if(elencoIdDaModificare.size() > 0)
    {
      session.setAttribute("elencoIDAllineaPercUsoElegg", elencoIdDaModificare);
    }
    %>
      <jsp:forward page="<%= confermaAllineaPercUsoEleggURL %>" />
    <%
    return;
    
    
    
      
  }
%>
   <jsp:forward page="<%=sceltaAllineaPercUsoEleggURL %>" />