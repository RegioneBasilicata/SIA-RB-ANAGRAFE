<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.solmr.util.SolmrLogger"%>
<%@ page import="it.csi.solmr.client.anag.AnagFacadeClient"%>
<%@ page import="it.csi.solmr.exception.SolmrException"%>
<%@ page import="it.csi.sigop.dto.services.SchedaCreditoVO" %>
<%@ page import="it.csi.solmr.dto.anag.AnagAziendaVO"%>
<%@ page import="it.csi.solmr.etc.anag.AnagErrors" %>
<%@ page import="it.csi.solmr.etc.SolmrConstants" %>
<%@ page import="it.csi.solmr.util.Validator" %>
<%@ page import="it.csi.solmr.util.WebUtils" %>
<%@ page import="java.util.Vector" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza"%>



<%

  String iridePageName = "registroDebitoriCtrl.jsp";
  %><%@include file = "/include/autorizzazione.inc" %><%
  
  SolmrLogger.debug(this, " - registroDebitoriCtrl.jsp - INIZIO PAGINA");
  String erogazioniPraticheUrl = "/view/registroDebitoriView.jsp";
  String actionUrl = "../layout/pratiche.htm";
  String erroreViewUrl = "/view/erroreView.jsp";
  String excelUrl = "/servlet/ExcelRegistroDebitoriServlet";
  
  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  
  
  WebUtils.removeUselessFilter(session, "schedeCredito");
  AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
  
  String fondo = request.getParameter("fondo");
  String tipoDebito = request.getParameter("tipoDebito");
  String statoScheda = request.getParameter("statoScheda");
  
  
  
  
  final String errMsg = "Il servizio di consultazione registrazione debitori è momentaneamente non disponibile. ";
  
  SchedaCreditoVO[] schedeCredito = null;  
  
  // Effettuo il controllo sullo stato dell'azienda: CONSISTENZA VARIATA
  // E NOTIFICHE. Il controllo va fatto sempre quindi lo inserisco qui
  // in modo che venga sempre effettuato
  try 
  {
    anagFacadeClient.checkStatoAzienda(anagAziendaVO.getIdAzienda());
  }
  catch(SolmrException se) 
  {
    request.setAttribute("statoAzienda", se);
  }
  
  
  String parametroRegDebArpea = null;
  try 
  {
    parametroRegDebArpea = (String)anagFacadeClient.getValoreParametroAltriDati(SolmrConstants.PARAMETRO_REG_DEB_ARPEA);
  }
  catch(SolmrException se) 
  {
  
    SolmrLogger.info(this, " - registroDebitoriCtrl.jsp - FINE PAGINA");
    String messaggioErrore = AnagErrors.ERRORE_KO_PARAMETRO_REG_DEB_ARPEA;
    String messaggio = messaggioErrore +": "+se.toString();
    request.setAttribute("messaggioErrore",messaggio);
    request.setAttribute("pageBack", actionUrl);
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
    return;
  }
  
  if(Validator.isNotEmpty(parametroRegDebArpea) && parametroRegDebArpea.equalsIgnoreCase(SolmrConstants.FLAG_S))
  {
    if(schedeCredito == null)
    {  
      try
      {
        if(anagAziendaVO.getCUAA() != null)
        {
          schedeCredito = anagFacadeClient.sigopservVisualizzaDebiti(anagAziendaVO.getCUAA());      
          session.setAttribute("schedeCredito", schedeCredito);          
        }
      
      }
      catch (SolmrException se) 
      {
        SolmrLogger.info(this, " - registroDebitoriCtrl.jsp - FINE PAGINA");
        String messaggio = errMsg+""+se.toString();
        request.setAttribute("messaggioErrore",messaggio);
        request.setAttribute("pageBack", actionUrl);
        %>
          <jsp:forward page="<%= erroreViewUrl %>" />
        <%
        return;
      }
    }
    
    
    
    if(schedeCredito == null)
    {
      request.setAttribute("messaggioErrore", AnagErrors.ERR_NO_REGISTRO_DEBITORI);
    }
    else
    {
      //Applico filtri se valorizzati
      if(Validator.isNotEmpty(fondo) || Validator.isNotEmpty(tipoDebito) 
        || Validator.isNotEmpty(statoScheda))
      {
        Vector<SchedaCreditoVO> vRegistroDebitori = null;
        for(int i=0;i<schedeCredito.length;i++)
        {
          boolean flagFiltroFondoOK = true;
          boolean flagFiltroTipoDebitoOK = true;
          boolean flagFiltroStatoSchedaOK = true;
          
          if(Validator.isNotEmpty(fondo))
          {
            if((schedeCredito[i].getCodiceFondo() != null)
              && (schedeCredito[i].getCodiceFondo().equalsIgnoreCase(fondo)))
            {
              flagFiltroFondoOK = true;
            }
            else
            {
              flagFiltroFondoOK = false;
            }
          }
          
          if(Validator.isNotEmpty(tipoDebito))
          {
            if((schedeCredito[i].getTipoDebito() != null)
              && (schedeCredito[i].getTipoDebito().equalsIgnoreCase(tipoDebito)))
            {
              flagFiltroTipoDebitoOK = true;
            }
            else
            {
              flagFiltroTipoDebitoOK = false;
            }
          }
          
          if(Validator.isNotEmpty(statoScheda))
          {
            if((schedeCredito[i].getStatoScheda() != null)
              && (schedeCredito[i].getStatoScheda().equalsIgnoreCase(statoScheda)))
            {
              flagFiltroStatoSchedaOK = true;
            }
            else
            {
              flagFiltroStatoSchedaOK = false;
            }
          }
          
          if(flagFiltroFondoOK && flagFiltroTipoDebitoOK && flagFiltroStatoSchedaOK)
          {
            if(vRegistroDebitori == null)
            {
              vRegistroDebitori = new Vector<SchedaCreditoVO>();
            }            
            vRegistroDebitori.add(schedeCredito[i]);
            
          }
        
        }
        
        
        if(vRegistroDebitori != null)
        {
          SchedaCreditoVO[] schedeCreditoFiltro =
            new SchedaCreditoVO[vRegistroDebitori.size()];
            
          for(int i=0;i<vRegistroDebitori.size();i++)
          {
            schedeCreditoFiltro[i] = (SchedaCreditoVO)vRegistroDebitori.get(i);
          }
          
          
          request.setAttribute("schedeCredito", schedeCreditoFiltro);
            
        }
        else
        {
          request.setAttribute("messaggioErrore", AnagErrors.ERR_NO_PREGISTRO_DEBITORI_FILTRI);
        } 
      
      
      }
      else //Metto tutti perchè nessun filtro valorizzato
      {
        request.setAttribute("schedeCredito", schedeCredito);
      }
      
      
      //Controllo se è stato selezionato lo scarico del file excel
		  if("excel".equals(request.getParameter("operazione")))
		  {
		    %>
		        <jsp:forward page="<%=excelUrl%>" />
		    <%
		    return;
		  }
    
    }
  }
  else
  {
    request.setAttribute("messaggioErrore", errMsg);    
  }
  
  
  


  SolmrLogger.debug(this, " - registroDebitoriCtrl.jsp - FINE PAGINA");

 %>
 
 
 
 <jsp:forward page= "<%= erogazioniPraticheUrl %>" />
