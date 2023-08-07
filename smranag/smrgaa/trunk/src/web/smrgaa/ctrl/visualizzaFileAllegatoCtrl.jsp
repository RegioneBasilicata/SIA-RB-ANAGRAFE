<!-- apre il file cliccando sul link --> 

<%@ page language="java"
  contentType="text/html"
  isErrorPage="false"
%>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page import="it.csi.smranag.smrgaa.dto.AllegatoDocumentoVO"%>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoVO" %>


<%

  
  SolmrLogger.info(this, " - visualizzaFileAllegatoCtrl.jsp - INIZIO PAGINA");
  
  String erroreViewUrl = "/view/erroreView.jsp";

  ValidationErrors errors = new ValidationErrors();
  
  Long idDocumento = null;
  //arrivo dall'elenco documenti
  if("elenco".equalsIgnoreCase(request.getParameter("arrivo")))
  {
    idDocumento = new Long(request.getParameter("idDocumentoPerAllegato"));
  }
  else
  {
    if(Validator.isNotEmpty(request.getParameter("idDocumento")))
    {
      idDocumento = new Long(request.getParameter("idDocumento"));
    }
  }

  try
  {
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();  
    
    Long idAllegato = new Long(request.getParameter("idAllegato"));
    
    
    //Lettura dati    
    AllegatoDocumentoVO allegatoDocumentoVO = gaaFacadeClient.getFileAllegato(idAllegato); 
    if(allegatoDocumentoVO.getFileAllegato() == null)
    {
      AgriWellEsitoVO agriWellEsitoVO = gaaFacadeClient
        .agriwellServiceLeggiDoquiAgri(allegatoDocumentoVO.getExtIdDocumentoIndex());
      if(Validator.isNotEmpty(agriWellEsitoVO) 
        && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito().intValue())
      {
        allegatoDocumentoVO.setFileAllegato(agriWellEsitoVO.getContenutoFile());      
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
    //response.addHeader("Content-Disposition","attachment;filename = \""+allegatoDocumentoVO.getNomeFisico().replaceAll(" ", "_")+"\"");
    response.setHeader("Content-Disposition","attachment;filename=\"" + allegatoDocumentoVO.getNomeFisico() + "\"");
            
    byte[] b = allegatoDocumentoVO.getFileAllegato();
            
    if (b != null && b.length > 0)
    {
      response.getOutputStream().write(b);
    }
    response.getOutputStream().flush();
    response.getOutputStream().close();

    SolmrLogger.info(this, " - visualizzaFileAllegatoCtrl.jsp - FINE PAGINA");
             
    return;
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - visualizzaFileAllegatoCtrl.jsp - FINE PAGINA");
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore", messaggio);
    String actionUrl = "../layout/documentoModifica.htm";
    if("allegaFile".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/allegaFile.htm";
    }
    else if("elenco".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/documentoElenco.htm";
    }
    else if("elencoNotifiche".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/notifiche.htm";
    }
    else if("ricercaNotifica".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/ricercaNotifica.htm";
    }
    else if("dettaglio".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/documentoDettaglio.htm";
    }
    else if("dettaglioNotifica".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/dettaglioNotifica.htm";
    }
    else if("ricercaDettaglioNotifica".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/ricercaDettaglioNotifica.htm";
    }
    else if("allegaFileRichiesta".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/allegaFileRichiesta.htm";
    }
    else if("allegaFileNotifica".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/allegaFileNotifica.htm";
    }
    else if("newInserimentoAllegati".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/newInserimentoAllegati.htm";
    }
    else if("documentoAllegati".equalsIgnoreCase(request.getParameter("arrivo")))
    {
      actionUrl = "../layout/documentoAllegati.htm";
    }
    session.setAttribute("idDocumentoErr", idDocumento);
    request.setAttribute("pageBack", actionUrl);
    
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
   
  }
  
%>
