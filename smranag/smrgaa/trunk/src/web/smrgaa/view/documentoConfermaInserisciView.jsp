<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/documentoConfermaInserisci.htm");

 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 String messaggioErrore = (String)request.getAttribute("messaggioErrore");
 DocumentoVO documentoVO = (DocumentoVO)request.getAttribute("documentoVO");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 // Setto l'area di provenienza
 htmpl.set("pageFrom", "documentale");
 
 
 
 if ("stampaConfermaInserisciPdf".equals(request.getParameter("stampaConfermaInserisciPdf")))
 {
   htmpl.set("stampaProtocollo", "stampaProtocolloImmediata()");
 }
 else
 {
   htmpl.set("stampaProtocollo", "");
 }
 
 

 // Se si è verificato un errore durante il reperimento dei filtri di ricerca o non è possibile
 // accedere alla funzione
 if(Validator.isNotEmpty(messaggioErrore)) 
 {
   htmpl.newBlock("blkDocumentoKo");
   htmpl.set("blkDocumentoKo.messaggioErrore", messaggioErrore);
 }
 // Altrimenti popolo la sezione relativa ai filtri di ricerca
 else 
 {
   //E' un documento protocollato
   if(Validator.isNotEmpty(documentoVO.getNumeroProtocollo()))
   {
	   htmpl.newBlock("blkDocumentoOk");
	   htmpl.set("blkDocumentoOk.numeroProtocollo", StringUtils.parseNumeroProtocolloField(documentoVO.getNumeroProtocollo()));
	   htmpl.set("blkDocumentoOk.dataProtocollo", DateUtils.formatDate(documentoVO.getDataProtocollo()));
	 }
   htmpl.set("idDocumento", documentoVO.getIdDocumento().toString());
   htmpl.set("flagIstanzaRiesame", documentoVO.getFlagIstanzaRiesame());
 }


%>
<%= htmpl.text()%>
