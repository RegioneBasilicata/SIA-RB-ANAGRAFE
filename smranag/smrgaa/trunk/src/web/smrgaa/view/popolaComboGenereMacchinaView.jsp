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
<%@ page import="it.csi.smranag.smrgaa.presentation.client.* " %>
<%@page import="it.csi.smranag.smrgaa.util.Formatter" %>
<%@ page import="it.csi.smranag.smrgaa.dto.uma.*" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/popolaComboGenereMacchina.htm");

 	Htmpl htmpl = new Htmpl(layout);

  GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();
  RuoloUtenza ruoloUtenza = (RuoloUtenza)session.getAttribute("ruoloUtenza");
  String idTipoMacchina = request.getParameter("idTipoMacchina");
  
  
  if(Validator.isNotEmpty(idTipoMacchina))
  {  
	  try
	  {
	  
	    Vector<TipoGenereMacchinaGaaVO> vGenereMacchina = gaaFacadeClient.getElencoTipoGenereMacchinaFromRuolo(
        new Long(idTipoMacchina), ruoloUtenza.getCodiceRuolo());
		  if(vGenereMacchina != null)
		  {
		    for(int i=0;i<vGenereMacchina.size();i++) 
		    {
		      TipoGenereMacchinaGaaVO tipoGenereMacchinaGaaVO = vGenereMacchina.get(i);
		      htmpl.newBlock("blkTipoGenereMacchina");
		      htmpl.set("blkTipoGenereMacchina.idGenereMacchina", ""+tipoGenereMacchinaGaaVO.getIdGenereMacchina());
		      htmpl.set("blkTipoGenereMacchina.descrizione", tipoGenereMacchinaGaaVO.getDescrizione());
		    }
		  }
		  else
		  {
		    htmpl.newBlock("blkNoTipoGenereMacchina");
		  }
	   
		  
			  
		}
		catch(Throwable ex)
		{
		  htmpl.set("blkNoElenco.messaggioErrore", LoggerUtils.getStackTrace(ex)); 	
		}
	}
	else
	{
	  htmpl.newBlock("blkNoTipoGenereMacchina");
	}
    
  
	
 	

%>
<%= htmpl.text()%>
