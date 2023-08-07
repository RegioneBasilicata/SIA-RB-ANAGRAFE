<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.text.*"%>
<%@ page import="java.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.util.*" %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter"%>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.* " %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboMarca.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String idGenereMacchina = request.getParameter("idGenereMacchina");  
  
  try
  {
    if(Validator.isNotEmpty(idGenereMacchina))
    {   
	    Vector<CodeDescription> vTipoMarca = gaaFacadeClient.getElencoTipoMarcaByIdGenere(new Long(idGenereMacchina));
		  if(vTipoMarca == null)
		    vTipoMarca = gaaFacadeClient.getElencoTipoMarca();
		    
		  if(vTipoMarca != null)
		  {
		    for(int i=0;i<vTipoMarca.size();i++) 
        {
          CodeDescription tipoMarca = vTipoMarca.get(i);
          htmpl.newBlock("blkTipoMarca");
          htmpl.set("blkTipoMarca.idMarca", ""+tipoMarca.getCode());
          htmpl.set("blkTipoMarca.descrizione", tipoMarca.getDescription());
        }
		  }		  
		}
		else
    {
      Vector<CodeDescription> vTipoMarca = gaaFacadeClient.getElencoTipoMarca();
      if(vTipoMarca != null)
		  {
		    for(int i=0;i<vTipoMarca.size();i++) 
		    {
		      CodeDescription tipoMarca = vTipoMarca.get(i);
		      htmpl.newBlock("blkTipoMarca");
		      htmpl.set("blkTipoMarca.idMarca", ""+tipoMarca.getCode());
		      htmpl.set("blkTipoMarca.descrizione", tipoMarca.getDescription());
		    }
		  }
    }
	}
	catch(Throwable ex)
  {
    htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex));  
  }
	
    
  
	
 	

%>
<%= htmpl.text()%>
