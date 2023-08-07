<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="it.csi.solmr.exception.*" %>

<%
 java.io.InputStream layout = application.getResourceAsStream("/layout/sede.htm");
 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

	// Nuova gestione fogli di stile
	htmpl.set("head", head, null);
	htmpl.set("header", header, null);
	htmpl.set("footer", footer, null);
	
	AnagAziendaVO anagVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
	
	htmpl.set("indirizzo", anagVO.getSedelegIndirizzo());
	
	if(anagVO.getSedelegEstero() == null || anagVO.getSedelegEstero().equals("")) 
	{
	  htmpl.newBlock("blkStatoItalia");
	  htmpl.set("blkStatoItalia.comune", anagVO.getDescComune());
	  htmpl.set("blkStatoItalia.provincia", anagVO.getSedelegProv());
	  htmpl.set("blkStatoItalia.cap", anagVO.getSedelegCAP());
	}
	else
	{
	  htmpl.newBlock("blkStatoEstero");
	  htmpl.set("blkStatoEstero.stato", anagVO.getStatoEstero());
	  if(anagVO.getSedelegCittaEstero()!= null)
	    htmpl.set("blkStatoEstero.citta", anagVO.getSedelegCittaEstero());
	}

  ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");

  //HtmplUtil.setValues(htmpl, request);
  HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
