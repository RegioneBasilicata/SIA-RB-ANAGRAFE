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

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboComune.htm");

 	Htmpl htmpl = new Htmpl(layout);

  AnagFacadeClient anagFacadeClient = new AnagFacadeClient();
  String istatProvincia = request.getParameter("istatProvincia");
  String provenienza = request.getParameter("provenienza");
  String indice = request.getParameter("indice");
  
  
  if(Validator.isNotEmpty(istatProvincia))
  {
    
    htmpl.set("idRiga", indice);
	  try
	  {	  
	    Vector<ComuneVO> vComune = anagFacadeClient
	      .getComuniAttiviByIstatProvincia(istatProvincia);
		  for(int i=0;i<vComune.size();i++) 
	    {
	      ComuneVO comuneVO = vComune.get(i);
	      htmpl.newBlock("blkElencoComuni");
        htmpl.set("blkElencoComuni.istatComune", comuneVO.getIstatComune());
        htmpl.set("blkElencoComuni.desCom", comuneVO.getDescom());
              
	      
	      if(i==0)
	      {
	        htmpl.set("blkElencoComuni.selected", "selected=\"selected\"", null);
	      }
	    }
	   
		  
			  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
	{
	  htmpl.newBlock("blkNoComuni");
	}	
 	

%>
<%= htmpl.text()%>
