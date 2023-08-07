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

  
  SolmrLogger.info(this, " - visualizzaFileStampaCtrl.jsp - INIZIO PAGINA");
  
  String erroreViewUrl = "/view/erroreView.jsp";

  ValidationErrors errors = new ValidationErrors();

  try
  {
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();  
    
    Long idAllegato = new Long(request.getParameter("idAllegato"));
    
    
    //Lettura dati
    
    AllegatoDocumentoVO allegatoDocumentoVO = gaaFacadeClient.getFileAllegato(idAllegato); 
    if(allegatoDocumentoVO.getFileAllegato() == null
      && allegatoDocumentoVO.getExtIdDocumentoIndex() != null) 
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
        request.setAttribute("messaggioErrore", messaggio);
        session.setAttribute("chiudi","true");
	      %>
	        <jsp:forward page="<%= erroreViewUrl %>" />
	      <%
	      return;   
      }
    }
    else
    {
      String messaggio = "Stampa non disponibile. Deve essere rigenerata dal menu validazioni utilizzando l'apposita icona";
      request.setAttribute("messaggioErrore", messaggio);
	    
	    session.setAttribute("chiudi","true");
	    %>
	      <jsp:forward page="<%= erroreViewUrl %>" />
	    <%
	    return;
    }

    response.resetBuffer();
    response.setContentType("application/pdf");
            
    byte[] b = allegatoDocumentoVO.getFileAllegato();
            
    if (b != null && b.length > 0)
    {
      response.getOutputStream().write(b);
    }
    response.getOutputStream().flush();
    response.getOutputStream().close();

    SolmrLogger.info(this, " - visualizzaFileStampaCtrl.jsp - FINE PAGINA");
             
    return;
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - visualizzaFileStampaCtrl.jsp - FINE PAGINA");
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore", messaggio);
    String actionUrl = "../layout/scelta_stampa.htm";
    
    request.setAttribute("pageBack", actionUrl);
    
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
   
  }
  
%>
