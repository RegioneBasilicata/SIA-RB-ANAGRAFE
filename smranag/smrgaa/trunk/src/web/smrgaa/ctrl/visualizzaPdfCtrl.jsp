<!-- apre il file cliccando sul link --> 

<%@ page language="java"
  contentType="text/html"
  isErrorPage="false"
%>
<%@page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@page import="it.csi.smranag.smrgaa.dto.nuovaiscrizione.*"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoVO" %>


<%

  
  SolmrLogger.info(this, " - visualizzaPdfCtrl.jsp - INIZIO PAGINA");
  
  String erroreViewUrl = "/view/erroreView.jsp";

  ValidationErrors errors = new ValidationErrors();
  
  /*Long idStampa = null;
  //arrivo dall'elenco documenti
  if("elencoAziendaNuova".equalsIgnoreCase(request.getParameter("arrivo")))
  {
    idStampa = new Long(request.getParameter("idStampa"));
  }*/
  

  try
  {
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();  
    
    Long idStampa = new Long(request.getParameter("idStampa"));        
    //Lettura dati    
    RichiestaAziendaVO richiestaAziendaVO = gaaFacadeClient.getPdfAziendaNuova(idStampa);
    if(richiestaAziendaVO.getFileStampa() == null)
    {
      AgriWellEsitoVO agriWellEsitoVO = gaaFacadeClient
        .agriwellServiceLeggiDoquiAgri(richiestaAziendaVO.getExtIdDocumentoIndex());
      if(Validator.isNotEmpty(agriWellEsitoVO) 
        && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito().intValue())
      {
        richiestaAziendaVO.setFileStampa(agriWellEsitoVO.getContenutoFile());      
      }
      else
      {
        String messaggio = "Attenzione si è verificato un problema nella lettura agriwell ";
        if(Validator.isNotEmpty(agriWellEsitoVO))
        {
          messaggio += "-"+agriWellEsitoVO.getMessaggio();
        }
        throw new SolmrException(messaggio);      
      }
    }   

    response.resetBuffer();
    response.setContentType("application/x-download");
    response.addHeader("Content-Disposition","attachment;filename = stampa.pdf");
            
    byte[] b = richiestaAziendaVO.getFileStampa();
            
    if (b != null && b.length > 0)
    {
      response.getOutputStream().write(b);
    }
    response.getOutputStream().flush();
    response.getOutputStream().close();

    SolmrLogger.info(this, " - visualizzaPdfCtrl.jsp - FINE PAGINA");
             
    return;
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - visualizzaPdfCtrl.jsp - FINE PAGINA");
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore", messaggio);
    String actionUrl = "../layout/ricercaRichiesta.htm";
    if("elencoAziendaNuova".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/ricercaRichiesta.htm";
    }
    else if("elencoRicercaRichiesta".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/elencoRicercaRichiesta.htm";
    }
    else if("richConfVar".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/newInserimentoConfermaVariazione.htm";
    }
    
    request.setAttribute("pageBack", actionUrl);
    
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
   
  }
  
%>
