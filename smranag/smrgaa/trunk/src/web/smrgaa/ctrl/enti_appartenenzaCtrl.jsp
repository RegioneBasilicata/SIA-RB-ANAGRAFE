<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>

<%

  String iridePageName = "enti_appartenenzaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - enti_appartenenzaCtrl.jsp - INIZIO PAGINA");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  Vector elencoCollegate = null;
  String operazione = request.getParameter("operazione");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String enti_appartenenzaUrl = "/view/enti_appartenenzaView.jsp";
  String actionUrl = "../layout/anagrafica.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String accessoAziendaURL = "/ctrl/accessoAziendaCtrl.jsp";
  boolean flagDettaglio = false;
  
  WebUtils.removeUselessFilter(session,null);
  
  
  //id azienda corrente
  String idAziendaCollegata = "";
  
  final String errMsg = "Impossibile procedere nella sezione enti di appartenenza. "+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
  
  idAziendaCollegata = anagAziendaVO.getIdAzienda().toString();
  
  
  //ho selezionato la voce dettaglio  
  if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("dettaglio"))
  {
    String[] elencoIdAzienda = request.getParameterValues("chkIdAzienda");
    // Controllo che venga selezionato un solo elemento dell'elenco
    if((elencoIdAzienda != null) && (elencoIdAzienda.length > 1)) 
    {
      request.setAttribute("messaggioErrore",AnagErrors.ERRORE_PIU_VOCI_SELEZIONATE_ET);      
    }
    else //Altrimenti redirigo sulla pagina di anagrafica.
    {
      if((elencoIdAzienda != null)) 
      {
        idAziendaCollegata = getIdAziendaFromChk(elencoIdAzienda[0]);
        
        request.setAttribute("idAziendaAccesso",idAziendaCollegata);
        %>
          <jsp:forward page = "<%=accessoAziendaURL%>" />
        <%
        return;          
               
      }
      else //nessuna azienda selezionata
      {
        request.setAttribute("messaggioErrore",AnagErrors.ERRORE_NO_AZIENDE_SELEZIONATE);
        flagDettaglio = true;
      }
    }
  } 
  else //ho selezionato aggiorna, non fa nulla
  {} 
  
  String storico = request.getParameter("storico");
  boolean flagStorico = false;
  if(Validator.isNotEmpty(storico))
  {
    flagStorico = true;
  }
  
  
  try 
  {
    elencoCollegate = anagFacadeClient.getEntiAppartenenzaByIdAzienda(new Long(idAziendaCollegata), flagStorico);
  }
  catch (SolmrException se) {
    SolmrLogger.info(this, " - aziendeColleagateCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  boolean flagPagErrore = true;
  if(elencoCollegate !=null)
  {
    request.setAttribute("elencoCollegate", elencoCollegate);
  }
  else //nessun record trovato!!
  {
     
    //se flagDettaglio == true significa che non è stata selezionata nessuna azienda
    //e scrive il messaggio d'errore corretto anche quando non esitono azienda collegate
    if(!flagDettaglio) 
    {
      flagPagErrore = false;
      request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_AZIENDE_COLLEGATE );
    }
    
    
  }
  
  if(flagPagErrore && (request.getAttribute("messaggioErrore") !=null))
  {
    request.setAttribute("history","true");
    //request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;    
  } 
  
  
  

  

%>

<jsp:forward page= "<%= enti_appartenenzaUrl %>" />

<%!
    
    //Ritorna l'idAzienda della tripla "idAzienda,idAziendaCollegata,storicizzata" settata nel checkbox 
    String getIdAziendaFromChk(String chk)
    {      
      return getIdFromChk(chk,0);
    }
    
    String getIdFromChk(String chk, int i)
    {
      ArrayList arr = new ArrayList();
      StringTokenizer strZ = new StringTokenizer(chk,",");
      while(strZ.hasMoreTokens())
      {
        arr.add(strZ.nextToken());
      }
      
      return (String)arr.get(i);
    }


 %>
