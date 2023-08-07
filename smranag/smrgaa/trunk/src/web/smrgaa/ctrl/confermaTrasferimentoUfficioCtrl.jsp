<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*"%>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>


<%

  String iridePageName = "confermaTrasferimentoUfficioCtrl.jsp";
  %>
    <%@include file = "/include/autorizzazione.inc" %>
  <%
  SolmrLogger.debug(this, " - confermaTrasferimentoUfficioCtrl.jsp - INIZIO PAGINA");
  final String errMsg = "Impossibile procedere con il trasferimento del mandato. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  String trasferimentoUfficioCtrlUrl = "/ctrl/trasferimentoUfficioCtrl.jsp";
  String confermaTrasferimentoUfficioUrl = "/view/confermaTrasferimentoUfficioView.jsp";
  String serviziCtrlUrl = "/ctrl/serviziCtrl.jsp";
  String actionUrl = "../layout/trasferimentoUfficio.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String idIntermediario_1 = (String)session.getAttribute("idIntermediario_1");
  String idIntermediario_2 = (String)session.getAttribute("idIntermediario_2");
  String operazione = (String)request.getParameter("operazione");
  //String metodo = (String)request.getAttribute("metodo");
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();  
  ValidationError error = null;
  String messaggioErrore = null;
  Vector<Long> elencoIdDaModificare = new Vector<Long>();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  
  HashMap<String,Vector<Long>> numUtilizziSelezionati = (HashMap<String,Vector<Long>>)session.getAttribute("numUtilizziSelezionati");
  
  
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/confermaTrasferimentoUfficio.htm";
  
  if("attenderePrego".equalsIgnoreCase(operazione)) {
     request.setAttribute("action", action);
     //request.setAttribute("metodo", "true");
     operazione = "conferma";
     request.setAttribute("operazione", operazione);
     %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
     <%
   }
   else {
  
  // Controllo che siano stati selezionati degli elementi dall'elenco
  if((numUtilizziSelezionati == null) || (numUtilizziSelezionati.size() == 0)) {
    error = new ValidationError(AnagErrors.ERRORE_NO_ELEMENTI_SELEZIONATI);
    request.setAttribute("error", error);
    %>
      <jsp:forward page="<%= trasferimentoUfficioCtrlUrl %>">
        <jsp:param name="operazione" value="paginazione" /> 
      </jsp:forward>
    <%
    return;
  }

  
  // Recupero il parametro che mi indica il numero massimo di record selezionabili
  String parametroRTra = null;
  try {
    parametroRTra = anagFacadeClient.getValoreFromParametroByIdCode(SolmrConstants.PARAMETRO_RTRA);;
  }
  catch(SolmrException se) {
    messaggioErrore = (String)AnagErrors.ERRORE_KO_PARAMETRO_RTRA;
    request.setAttribute("messaggioErrore", messaggioErrore);
    %>
      <jsp:forward page="<%= confermaTrasferimentoUfficioUrl %>" />
    <%
    return;
  }
  
  // Verifico che non siano state selezionate più particelle rispetto a quelle consentite
  // Verifico che non siano state selezionate più particelle rispetto a quelle consentite
  Set<String> elencoKeys = numUtilizziSelezionati.keySet();
  Iterator<String> iteraKey = elencoKeys.iterator();
  while(iteraKey.hasNext()) {
    Vector<Long> selezioni = (Vector<Long>)numUtilizziSelezionati.get((String)iteraKey.next());
    if(selezioni != null && selezioni.size() > 0) {
      for(int a = 0; a < selezioni.size(); a++) {
        elencoIdDaModificare.add((Long)selezioni.elementAt(a));
      }
    }
  }
  if(elencoIdDaModificare.size() > Integer.parseInt(parametroRTra)) {
    error = new ValidationError(AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART1+parametroRTra+AnagErrors.ERRORE_NUM_MAX_PARAMETRI_SELEZIONABILI_PART2);
    request.setAttribute("error", error);
    %>
      <jsp:forward page="<%= trasferimentoUfficioCtrlUrl %>" >
        <jsp:param name="operazione" value="paginazione" /> 
      </jsp:forward>
    <%
    return;
  }
  
  // Effettuo la ricerca in relazione ai nuovi parametri di ricerca inseriti
  // dall'utente
  
  Vector<AnagAziendaVO> elencoAziende = null;
  
  try {
    
    elencoAziende = anagFacadeClient.getAziendeByListOfId(elencoIdDaModificare);
  } 
  catch (SolmrException se) {
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  //aggiornamento tabelle:conferma
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("conferma")) 
  {
    try {
    
      anagFacadeClient.storicizzaDelegaBlocco(ruoloUtenza,elencoAziende,idIntermediario_1,idIntermediario_2);
      %>
        <jsp:forward page="<%= serviziCtrlUrl %>" />
      <%
      return;
    } 
    catch (SolmrException se) {
      String messaggio = errMsg+""+se.toString();
      request.setAttribute("messaggioErrore",messaggio);
      request.setAttribute("pageBack", actionUrl);
      %>
        <jsp:forward page="<%= erroreViewUrl %>" />
      <%
      return;
    }
  }
  
  
  

  SolmrLogger.debug(this, " - confermaTrasferimentoUfficioCtrl.jsp - FINE PAGINA");
%>

  <jsp:forward page= "<%= confermaTrasferimentoUfficioUrl %>" />
  
<% } %>


