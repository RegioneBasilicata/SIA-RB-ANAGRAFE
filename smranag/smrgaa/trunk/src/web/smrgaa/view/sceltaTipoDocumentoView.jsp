  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="java.util.*" %>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.client.anag.*" %>
<%@ page import="it.csi.solmr.exception.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.etc.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.smranag.smrgaa.presentation.client.GaaFacadeClient" %>

<%

	GaaFacadeClient gaaFacadeClient = GaaFacadeClient.getInstance();

 	java.io.InputStream layout = application.getResourceAsStream("/layout/sceltaTipoDocumento.htm");
  	Htmpl htmpl = new Htmpl(layout);

	%>
   	<%@include file = "/view/remoteInclude.inc" %>
 	<%

 	// Nuova gestione fogli di stile
 	htmpl.set("head", head, null);
 	htmpl.set("header", header, null);
 	htmpl.set("footer", footer, null);

	String strDescDocumento = request.getParameter("strDescDocumento");
 	String provenienza = request.getParameter("provenienza");
 	
 	htmpl.set("provenienza", provenienza);
 	
 	Vector<DocumentoVO> elencoTipiDocumenti = null;
	try 
 	{
 	  if(Validator.isNotEmpty(strDescDocumento))
 	  {
 	    strDescDocumento = strDescDocumento.trim();
 	  }
   	if(Validator.isNotEmpty(provenienza) && provenienza.equalsIgnoreCase("inserisci")) 
   	{
    	elencoTipiDocumenti = gaaFacadeClient.getListDocFromRicerca(strDescDocumento, true);
   	}
   	else if(Validator.isNotEmpty(provenienza) && provenienza.equalsIgnoreCase("elenco")) 
   	{
    	elencoTipiDocumenti = gaaFacadeClient.getListDocFromRicerca(strDescDocumento, false);
   	}
 	}
 	catch(SolmrException se) 
 	{
   	htmpl.set("exception", AnagErrors.RICERCADOCUMENTI);
   	htmpl.set("chiudi.pathToFollow", (String)session.getAttribute("pathToFollow"));
 	}
 	if(elencoTipiDocumenti != null) 
 	{
 	  htmpl.newBlock("conferma");
   	for(int i=0;i<elencoTipiDocumenti.size();i++)
   	{   	  
   		DocumentoVO documentoVO = elencoTipiDocumenti.get(i);
   		htmpl.newBlock("elencoTipiDocumento");
   		htmpl.set("elencoTipiDocumento.idDescDocumento", ""+documentoVO.getTipoDocumentoVO().getIdDocumento());
   		htmpl.set("elencoTipiDocumento.descTipologiaDocumento", documentoVO.getTipoTipologiaDocumento().getDescription());
   		htmpl.set("elencoTipiDocumento.descTipoCategoria", documentoVO.getTipoCategoriaDocumentoVO().getDescrizione());
   		htmpl.set("elencoTipiDocumento.descTipoDocumento", documentoVO.getTipoDocumentoVO().getDescrizione());
   		htmpl.set("elencoTipiDocumento.idTipologiaDocumento", ""+documentoVO.getTipoTipologiaDocumento().getCode());
   		htmpl.set("elencoTipiDocumento.idCategoriaDocumento", ""+documentoVO.getTipoCategoriaDocumentoVO().getIdCategoriaDocumento());
   		
   	}
 	}
 	


%>
<%= htmpl.text()%>
