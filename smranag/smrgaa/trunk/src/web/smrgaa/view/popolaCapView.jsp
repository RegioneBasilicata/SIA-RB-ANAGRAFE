<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.anag.terreni.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaCap.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String istatComune = request.getParameter("istatComune");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  if(Validator.isNotEmpty(istatComune))
  {
  
	  try
	  {	  
	    ComuneVO comune = anagFacadeClient.getComuneByISTAT(istatComune);		  
      htmpl.set("cap", comune.getCap());		  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
 	

%>
<%= htmpl.text()%>
