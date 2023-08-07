<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>

<%@ page import="java.io.OutputStream" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page  import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient"%>
<%@ page  import="it.csi.smranag.smrgaa.dto.manuali.ManualeVO"%>
<%@ page import="it.csi.solmr.exception.*" %>

<%

  String erroreViewUrl = "/view/erroreView.jsp";
  
  try
  {
	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
    ManualeVO manualeVO = gaaFacadeClient.getManuale(new Long(request.getParameter("idManuale")).longValue());
    
    if(manualeVO == null)
      throw new SolmrException("non è stato trovato il manuale sul db");
    
    response.resetBuffer();
    response.setContentType("application/x-download");
    //response.addHeader("Content-Disposition","attachment;filename = \""+manualeVO.getNomeFile().replaceAll(" ", "_")+"\"");
    response.setHeader("Content-Disposition","attachment;filename=\"" + manualeVO.getNomeFile() + "\"");
    
    byte[] b = manualeVO.getTestoManuale();
            
    if (b != null && b.length > 0)
    {
      response.getOutputStream().write(b);
    }
    response.getOutputStream().flush();
    response.getOutputStream().close();
    
    return;
  }
  catch (SolmrException se) 
  {
    SolmrLogger.info(this, " - manualeCtrl.jsp - FINE PAGINA");
    String messaggio = se.getMessage();
    request.setAttribute("messaggioErrore", messaggio);
    String actionUrl = "../layout/elencoManuali.htm";
    request.setAttribute("pageBack", actionUrl);
    
    %>
      <jsp:forward page="<%= erroreViewUrl %>" />
    <%
   
  }
  
%>