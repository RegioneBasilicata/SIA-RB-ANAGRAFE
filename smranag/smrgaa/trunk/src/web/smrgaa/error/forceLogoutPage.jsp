<%@page import="java.net.URL"%>
<%@page import="it.csi.papua.papuaserv.exception.messaggistica.LogoutException"%>
<%@ page language="java"
         contentType="text/html"
         isErrorPage="true"
%>
<%@ page import="it.csi.jsf.htmpl.*" %>
<%@ page import="it.csi.solmr.util.*" %>
<%@ page import="java.io.PrintStream" %>
<%@ page import="java.io.ByteArrayOutputStream" %>
<%@ page import="it.csi.solmr.dto.profile.RuoloUtenza" %>

<%
  /**
   * Recupera il layout errore.htm come base della pagina di errore
   */
   String iridePageName = "forceLogoutPage.jsp";
	%>
	  <%@include file="/include/autorizzazione.inc"%>
	<%
   
   
  java.io.InputStream layout = application.getResourceAsStream("/layout/force_logout.htm");
  Htmpl htmpl = new Htmpl(layout);
  
  %>
     <%@include file = "/view/remoteInclude.inc" %>
  <%
  
  // Nuova gestione fogli di stile
  htmpl.set("head", head, null);
  htmpl.set("header", header, null);
  htmpl.set("footer", footer, null);
  
  LogoutException e = (LogoutException)session.getAttribute("LogoutException");
  
  System.out.println("---- forseLogoutPage.jsp - invalidate()");
  session.invalidate();

  /**
   * Setta i messaggi di errore sul layout errore.htm
   */
  htmpl.set("titolo", "Forzatura logout");
  
  String msgErrore="E' avvenuto il logout per il seguente motivo: " + e.getMessage();
  if(Validator.isNotEmpty(msgErrore)){
    htmpl.set("msg", msgErrore);
  }
  if(e.getTestoMessaggio()!=null){
    htmpl.set("messaggioCompleto", e.getTestoMessaggio());
  }
  htmpl.set("pulsante","logout");

  URL url=new URL(request.getRequestURL().toString());
  String s="http://"+url.getHost()+":"+url.getPort()+"/";
  htmpl.set("hrefPulsante",s);
%>

<%= htmpl.text() %>
