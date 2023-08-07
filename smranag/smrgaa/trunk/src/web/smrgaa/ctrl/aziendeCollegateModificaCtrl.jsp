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

  String iridePageName = "aziendeCollegateModificaCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - aziendeColleagateModificaCtrl.jsp - INIZIO PAGINA");
  
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  //ValidationError error = null;
  String operazione = request.getParameter("operazione");
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String aziendeCollegateModificaUrl = "/view/aziendeCollegateModificaView.jsp";
  String actionUrl = "../layout/aziendeCollegate.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String aziendeCollegateCtrlUrl = "/ctrl/aziendeCollegateCtrl.jsp";
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  Vector vIdAziendaCollegate = (Vector)session.getAttribute("elencoIdAziendeCollegate");
  Vector elencoCollegate = null;
  
  String[] elencoIdAziendaCollegataAzCollAll = request.getParameterValues("idAziendaCollegataHid");
  String[] elencoDataIngressoAzCollAll = request.getParameterValues("dataIngresso");
  String[] elencoDataUscitaAzCollAll = request.getParameterValues("dataUscita");
  
  
  if(elencoIdAziendaCollegataAzCollAll !=null) 
    session.setAttribute("elencoIdAziendaCollegataAzCollAll",elencoIdAziendaCollegataAzCollAll);
  if(elencoDataIngressoAzCollAll !=null)
    session.setAttribute("elencoDataIngressoAzCollAll",elencoDataIngressoAzCollAll);
  if(elencoDataUscitaAzCollAll !=null)
    session.setAttribute("elencoDataUscitaAzCollAll",elencoDataUscitaAzCollAll); 
  
  
  
  final String errMsg = "Impossibile procedere nella sezione "+anagAziendaVO.getLabelElencoAssociati()+"."+
    "Contattare l'assistenza comunicando il seguente messaggio: ";
    
  String attenderePregoUrl = "/view/attenderePregoView.jsp";
  String action = "../layout/aziendeCollegateModifica.htm";
 
 
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
        SolmrLogger.info(this, " - aziendeColleagateModificaCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
      
      //provengo da aziende collegate --- INIZIALIZZAZIONE
      if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("modifica"))
      {
        WebUtils.removeUselessFilter(session,"elencoIdAziendeCollegate");
      }
      
      
      if(Validator.isNotEmpty(operazione) && operazione.equalsIgnoreCase("conferma"))
      {
        //mi ricavo l'idAzienda dell'azienda corrente a cui si voglio aggiungere figli
        HashMap hElencoErrori = null;
        ValidationErrors errors = null;
        
        Long idAziendaPadreL = anagAziendaVO.getIdAzienda();
        
        
        //tutte le aziende per associare correttamente le date
        String[] elencoIdAziendaCollegataAll = (String[])session.getAttribute("elencoIdAziendaCollegataAzCollAll");
        String[] elencoDataIngressoAll = (String[])session.getAttribute("elencoDataIngressoAzCollAll");
        String[] elencoDataUscitaAll = (String[])session.getAttribute("elencoDataUscitaAzCollAll");
        
        if(elencoIdAziendaCollegataAll !=null)
        {
          Hashtable hashT = new Hashtable();
          for(int i=0;i<elencoIdAziendaCollegataAll.length;i++)
          {
            AziendaCollegataVO azCollVO = new AziendaCollegataVO();
            azCollVO.setIdAzienda(idAziendaPadreL);
            azCollVO.setIdAziendaCollegata(new Long(elencoIdAziendaCollegataAll[i]));
            azCollVO.setDataIngressoStr(elencoDataIngressoAll[i]);
            azCollVO.setDataUscitaStr(elencoDataUscitaAll[i]);
            hashT.put(new Long(elencoIdAziendaCollegataAll[i]),azCollVO);
          }
          
          Vector vAzSel = new Vector();
          for(int i=0;i<elencoIdAziendaCollegataAll.length;i++)
          {
            Long idAziendaSelL = new Long(elencoIdAziendaCollegataAll[i]);
            AziendaCollegataVO azCollVO = (AziendaCollegataVO)hashT.get(idAziendaSelL);
            errors = azCollVO.validateInsertAziende();
            if(errors != null)
            {
              if(hElencoErrori == null)
              {
                hElencoErrori = new HashMap();
              }
              hElencoErrori.put(idAziendaSelL,errors);
            }
            vAzSel.add(hashT.get(new Long(elencoIdAziendaCollegataAll[i])));
          }
          
          
          if(hElencoErrori !=null)
          {
            request.setAttribute("hElencoErrori",hElencoErrori);
          }
          else // non sono stati rilevati errori storicizzo
          {
            try {
        
              anagFacadeClient.storicizzaAziendeCollegateBlocco(ruoloUtenza, vAzSel);
           
              %>
                <jsp:forward page="<%= aziendeCollegateCtrlUrl %>" />
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
        }
        else
        {
          request.setAttribute("messaggioErrore", AnagErrors.ERRORE_NO_AZIENDE_SELEZIONATE);
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
    SolmrLogger.info(this, " - aziendeColleagateModificaCtrl.jsp - FINE PAGINA");
    String messaggio = errMsg+""+SolmrLogger.getStackTrace(ex);
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  
  
  

  

%>

<jsp:forward page= "<%= aziendeCollegateModificaUrl %>" />

