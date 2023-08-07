  <%@ page language="java"
      contentType="text/html"
      isErrorPage="true"
  %>

<%@ page import="it.csi.jsf.htmpl.*" %>

<%

  java.io.InputStream layout = application.getResourceAsStream("/layout/confermaAllineaGIS.htm");
  Htmpl htmpl = new Htmpl(layout);

  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%

  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  String messaggioErrore = (String)request.getAttribute("messaggioErrore");
  
  if(Validator.isNotEmpty(messaggioErrore)) 
  {
    htmpl.newBlock("blkErrore");
    htmpl.set("blkErrore.messaggioErrore", messaggioErrore);
  }
  else
  {
    htmpl.newBlock("blkNoErrore");
  }
  


%>
<%= htmpl.text()%>


