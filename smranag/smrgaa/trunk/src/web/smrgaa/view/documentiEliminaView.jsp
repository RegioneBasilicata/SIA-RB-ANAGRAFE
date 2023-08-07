<%@ page language="java"
    contentType="text/html"
    isErrorPage="true"
%>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%@ page import="it.csi.solmr.dto.*" %>
<%@ page import="it.csi.solmr.dto.anag.*" %>
<%@ page import="it.csi.solmr.etc.*" %>
<%@ page import="it.csi.solmr.util.*" %>

<%@ page import="java.text.*"%>
<%@ page import="java.util.*"%>

<%

 java.io.InputStream layout = application.getResourceAsStream("/layout/documentiElimina.htm");

 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 AnagAziendaVO anagAziendaVO = (AnagAziendaVO)session.getAttribute("anagAziendaVO");
 String hasNumeroProtocollo = (String)session.getAttribute("hasNumeroProtocollo");
 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 String note = (String)request.getAttribute("note");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 // Setto l'area di provenienza
 htmpl.set("pageFrom", "documentale");

 if(Validator.isNotEmpty(hasNumeroProtocollo)) {
   htmpl.newBlock("blkProtocollo");
   if(Validator.isNotEmpty(note)) {
     htmpl.set("blkProtocollo.note", note);
   }
   HtmplUtil.setErrorsSpecifiedBlock(htmpl, errors, request, application, "blkProtocollo");
 }
 else {
   htmpl.newBlock("blkNoProtocollo");
 }

 HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
