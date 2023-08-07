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

  
  SolmrLogger.info(this, " - visualizzaFileAllegatoIndexCtrl.jsp - INIZIO PAGINA");
  
  String erroreViewUrl = "/view/erroreView.jsp";

  ValidationErrors errors = new ValidationErrors();
  
  
  try
  {
    GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();  
    
    Long idDocumentoIndex = new Long(request.getParameter("idDocumentoIndex"));
    
    
    //Lettura dati
    
    AgriWellEsitoVO agriWellEsitoVO = gaaFacadeClient
      .agriwellServiceLeggiDoquiAgri(idDocumentoIndex);
    if(Validator.isNotEmpty(agriWellEsitoVO) 
      && SolmrConstants.ESITO_AGRIWELL_OK == agriWellEsitoVO.getEsito().intValue())
    {}
    else
    {
      String messaggio = "Attenzione si è verificato un problema nella lettura agriwell ";
      if(Validator.isNotEmpty(agriWellEsitoVO))
      {
        messaggio += "-"+agriWellEsitoVO.getMessaggio();
      }
      throw new SolmrException(messaggio);      
    }

    response.resetBuffer();
    response.setContentType("application/x-download");
    //String prova = WebUtils.encodeUrl("attachment;filename = "+agriWellEsitoVO.getNomeFile());
    //response.addHeader("Content-Disposition","attachment;filename = "+agriWellEsitoVO.getNomeFile().replaceAll(" ", "_"));
    
    response.setHeader("Content-Disposition","attachment;filename=\"" + agriWellEsitoVO.getNomeFile() + "\"");
    //response.addHeader("Content-Disposition", prova);
            
    byte[] b = agriWellEsitoVO.getContenutoFile();
            
    if (b != null && b.length > 0)
    {
      response.getOutputStream().write(b);
    }
    response.getOutputStream().flush();
    response.getOutputStream().close();

    SolmrLogger.info(this, " - visualizzaFileAllegatoIndexCtrl.jsp - FINE PAGINA");
             
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - visualizzaFileAllegatoIndexCtrl.jsp - FINE PAGINA");
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore", messaggio);
    String actionUrl = "../layout/fascicoloDematerializzato.htm";
    request.setAttribute("pageBack", actionUrl);
    
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
   
  }
  
%>
