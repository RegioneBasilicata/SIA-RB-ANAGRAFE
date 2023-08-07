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

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboCategoria.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  String idGenereMacchina = request.getParameter("idGenereMacchina");  
  
  if(Validator.isNotEmpty(idGenereMacchina))
  {
  
	  try
	  {
	  
	    Vector<TipoCategoriaGaaVO> vCategoria = gaaFacadeClient.getElencoTipoCategoria(new Long(idGenereMacchina));
		  if(vCategoria != null)
		  {
		    for(int i=0;i<vCategoria.size();i++) 
		    {
		      TipoCategoriaGaaVO tipoCategoriaGaaVO = vCategoria.get(i);
		      htmpl.newBlock("blkTipoCategoria");
		      htmpl.set("blkTipoCategoria.idCategoria", ""+tipoCategoriaGaaVO.getIdCategoria());
		      htmpl.set("blkTipoCategoria.descrizione", tipoCategoriaGaaVO.getDescrizione());
		    }
		  }
		  else
		  {
		    htmpl.newBlock("blkNoTipoCategoria");
		  }
	   
		  
			  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
	{
	  htmpl.newBlock("blkNoTipoCategoria");
	}
    
  
	
 	

%>
<%= htmpl.text()%>
