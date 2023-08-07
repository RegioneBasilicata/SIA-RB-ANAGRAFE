<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

  String iridePageName = "aziendeCollegateEliminaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - aziendeColleagateEliminaCtrl.jsp - INIZIO PAGINA");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  //ValidationError error = null;
  String operazione = request.getParameter("operazione");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String aziendeCollegateEliminaUrl = "/view/aziendeCollegateEliminaView.jsp";
  String actionUrl = "../layout/aziendeCollegate.htm";
  
  String erroreViewUrl = "/view/erroreView.jsp";
  String aziendeCollegateCtrlUrl = "/ctrl/aziendeCollegateCtrl.jsp";
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");

  Vector vIdAziendaCollegate = (Vector)session.getAttribute("elencoIdAziendeCollegate");
  Vector elencoCollegate = null;
  
  
  
  final String errMsg = "Impossibile procedere nella sezione "+anagAziendaVO.getLabelElencoAssociati()+"."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/aziendeCollegateElimina.htm";
 
 
  try
  { 
    if("attenderePrego".equalsIgnoreCase(operazione)) 
    {     
      request.setAttribute("action", action);
      operazione = "conferma";
      request.setAttribute("operazione", operazione);
      %>
        <jsp:forward page= "<%= attenderePregoUrl %>" />
      <%
    }
    else 
    {
      
      try 
      {
        if((vIdAziendaCollegate != null) && (vIdAziendaCollegate.size() > 0))
        {
          elencoCollegate = anagFacadeClient.getAziendeCollegateByRangeIdAziendaCollegata(vIdAziendaCollegate);
        }
      }
      catch (SolmrException se) {
        SolmrLogger.info(this, " - aziendeColleagateEliminaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      
      if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("conferma"))
      {
        Long idAziendaPadre = anagAziendaVO.getIdAzienda();          
    
        try
        {
        
          AnagFacadeClient anagClient = new AnagFacadeClient();
          anagClient.eliminaAziendeCollegateBlocco(ruoloUtenza.getIdUtente(),idAziendaPadre,vIdAziendaCollegate);
          %>
            <jsp:forward page="<%= aziendeCollegateCtrlUrl %>" />
          <%
          return;
          
        }
        catch(Exception se)
        {
          SolmrLogger.info(this, " - AziendeCollegateEliminaCtrl.jsp - FINE PAGINA");
          String messaggioErrore = (String)AnagErrors.ERRORE_KO_PARAMETRO_RAZA;
          String messaggio = messaggioErrore +": "+se.toString();
          request.setAttribute("messaggioErrore",messaggio);
          request.setAttribute("pageBack", actionUrl);
          %>
            <jsp:forward page="<%= erroreViewUrl %>" />
          <%
          return;
        }
      }
      
      if(elencoCollegate !=null)
      {
        request.setAttribute("elencoCollegate", elencoCollegate);
      }
      
    } //fine else attendere prego
  }
  catch(Exception ex)
  {
    SolmrLogger.info(this, " - aziendeColleagateEliminaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  

  

%>

<jsp:forward page= "<%= aziendeCollegateEliminaUrl %>" />

