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

 java.io.InputStream layout = application.getResourceAsStream("/layout/nuovaNotificaConferma.htm");

 Htmpl htmpl = new Htmpl(layout);

 %>
    <%@include file = "/view/remoteInclude.inc" %>
 <%

 String messaggioErrore = (String)request.getAttribute("messaggioErrore");

 // Nuova gestione fogli di stile
 htmpl.set("head", head, null);
 htmpl.set("header", header, null);
 htmpl.set("footer", footer, null);

 // Setto l'area di provenienza
 htmpl.set("pageFrom", "documentale");
 
 NotificaVO notificaVO = (NotificaVO)request.getAttribute("notificaVO");
 
 htmpl.set("idNotifica", ""+notificaVO.getIdNotifica());
 
 
 
 

 // Se si � verificato un errore durante il reperimento dei filtri di ricerca o non � possibile
 // accedere alla funzione
 if(Validator.isNotEmpty(messaggioErrore)) 
 {
   htmpl.newBlock("blkDocumentoKo");
   htmpl.set("blkDocumentoKo.messaggioErrore", messaggioErrore);
 }


%>
<%= htmpl.text()%>
