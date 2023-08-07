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
<%@ page import="java.math.BigDecimal" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellEsitoFolderVO" %>
<%@ page import="it.csi.smrcomms.smrcomm.dto.agriwell.axisgen.AgriWellFolderVO" %>

<%

	java.io.InputStream layout = application.getResourceAsStream("/layout/fascicoloDematerializzato.htm");

 	Htmpl htmpl = new Htmpl(layout);

 	%>
    	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	ValidationErrors errors = (ValidationErrors)request.getAttribute("errors"); 	
  AgriWellEsitoFolderVO agriWellEsitoFolderVO = (AgriWellEsitoFolderVO)request.getAttribute("agriWellEsitoFolderVO");
  
  
	
 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);
 	
 	
 	if(Validator.isNotEmpty(agriWellEsitoFolderVO)
 	  && Validator.isNotEmpty(agriWellEsitoFolderVO.getEsito())
 	  && (agriWellEsitoFolderVO.getEsito().intValue() == SolmrConstants.ESITO_AGRIWELL_OK)
 	  && (agriWellEsitoFolderVO.getElencoFolder().length > 0))
 	{
 	  htmpl.newBlock("blkElencoCartelle");
 	  for(int i=0;i<agriWellEsitoFolderVO.getElencoFolder().length;i++)
 	  {
 	    AgriWellFolderVO agriWellFolderVO = agriWellEsitoFolderVO.getElencoFolder()[i];
 	    htmpl.newBlock("blkElencoCartelle.blkCartella");
 	    /*if("S".equalsIgnoreCase(agriWellFolderVO.getHasChildren()))
      {
        htmpl.newBlock("blkCartella.blkRadio");
        htmpl.set("blkCartella.blkRadio.idFolder",""+agriWellFolderVO.getIdNomeFolder());
      }*/
 	    htmpl.set("blkElencoCartelle.blkCartella.divFolder","ch"+agriWellFolderVO.getIdNomeFolder());
 	    htmpl.set("blkElencoCartelle.blkCartella.aFolder","a"+agriWellFolderVO.getIdNomeFolder());
 	    htmpl.set("blkElencoCartelle.blkCartella.idFolder",""+agriWellFolderVO.getIdNomeFolder());
 	    htmpl.set("blkElencoCartelle.blkCartella.nomeFolder", agriWellFolderVO.getNomeFolder()); 	  
 	  }
 	
 	
 	}
 	else
 	{
 	  if(Validator.isNotEmpty(agriWellEsitoFolderVO.getEsito())
      && (agriWellEsitoFolderVO.getEsito().intValue() == SolmrConstants.ESITO_AGRIWELL_OK))
    {
      htmpl.newBlock("blkErrore");
      htmpl.set("blkErrore.messaggio", SolmrConstants.NO_DOC_DEM);    
    }
    else
    {
      htmpl.newBlock("blkErrore");
      htmpl.set("blkErrore.messaggio", "Si è verificato un errore: "+agriWellEsitoFolderVO.getMessaggio());    
    }	
 	}
 	

 	
  
 	// GESTIONE ERRORI SEZIONE GENERICA
 	if(errors != null && errors.size() > 0) {
   		HtmplUtil.setErrors(htmpl, errors, request, application);
 	}

%>
<%= htmpl.text()%>
