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

 java.io.InputStream layout = application.getResourceAsStream("/layout/annullaRichiesta.htm");

 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 ValidationErrors errors = (ValidationErrors)request.getAttribute("errors");
 String msgAnnulla = (String)request.getAttribute("msgAnnulla");
 String note = request.getParameter("note");
 String chkMail = request.getParameter("chkMail");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);
 
 htmpl.set("idRichiestaAzienda", request.getParameter("idRichiestaAzienda"));
 htmpl.set("arrivo", request.getParameter("arrivo"));
 
 htmpl.set("msgAnnulla", msgAnnulla);
 htmpl.set("note", note);
 if(Validator.isNotEmpty(chkMail))
 {
   htmpl.set("checkedMail", "checked=\"true\"",null);
 }

 
 
 

 

 HtmplUtil.setErrors(htmpl, errors, request, application);

%>
<%= htmpl.text()%>
